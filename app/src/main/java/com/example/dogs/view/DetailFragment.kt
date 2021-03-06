package com.example.dogs.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Layout
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dogs.R
import com.example.dogs.databinding.FragmentDetailBinding
import com.example.dogs.databinding.SendSmsDialogBinding
import com.example.dogs.model.DogPalette
import com.example.dogs.model.SmsInfo
import com.example.dogs.util.getProgressDrawable
import com.example.dogs.util.loadImage
import com.example.dogs.viewmodel.DetailViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private var sendSmsStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        observe()

        val args: DetailFragmentArgs by navArgs()
        viewModel.fetchDogFromDatabaseByUuid(args.dogUuid.toLong())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "This is something dogful")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Dogs of ${viewModel.dogBreed.value?.dogBreed} are nice"
                )
                intent.putExtra(Intent.EXTRA_STREAM, viewModel.dogBreed.value?.imageUrl)
                startActivity(Intent.createChooser(intent, "Share withh"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "This is my dog: ${viewModel.dogBreed.value?.dogBreed ?: ""}",
                    viewModel.dogBreed.value?.imageUrl ?: ""
                )
                val dialogBinding =
                    SendSmsDialogBinding.inflate(LayoutInflater.from(it), null, false)
                dialogBinding.smsInfo = smsInfo

                AlertDialog.Builder(context)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { _, _ ->
                        if (dialogBinding.smsDestination.text != null && dialogBinding.smsDestination.text.isNotEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null)

    }

    private fun observe() {
        viewModel.dogBreed.observe(viewLifecycleOwner, { dogBreed ->
            binding.dog = dogBreed
            setupBackgroundColor(dogBreed.imageUrl)
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            binding.dogNameView.text = if (!it) viewModel.dogBreed.value?.dogBreed
                ?: "NO DOG BREED PROVIDED" else "Loading..."
        })
    }

    private fun setupBackgroundColor(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val palette = Palette.from(resource).generate()
                    val intColor = palette.getVibrantColor(0)
                    binding.palette = DogPalette(intColor)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        }
    }
}