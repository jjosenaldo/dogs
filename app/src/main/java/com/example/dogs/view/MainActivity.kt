package com.example.dogs.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dogs.R
import com.example.dogs.util.SEND_SMS_PERMISSION_CODE

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun isSmsPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.SEND_SMS
    ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestSmsPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.SEND_SMS
        )

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.SEND_SMS),
            SEND_SMS_PERMISSION_CODE
        )
    }

    private fun notifyDetailFragment(permissionGranted: Boolean) {
        val fragments =
            supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments
        fragments?.let {
            if (it.isNotEmpty()) {
                val activeFragment = it[0]
                if (activeFragment is DetailFragment) {
                    activeFragment.onPermissionResult(permissionGranted)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SEND_SMS_PERMISSION_CODE -> {
                notifyDetailFragment(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
        }
    }

    fun checkSmsPermission() {
        if (!isSmsPermissionGranted()) {
            if (shouldShowRequestSmsPermissionRationale()) {
                AlertDialog.Builder(this)
                    .setTitle("Send SMS permission")
                    .setMessage("Dogs want SMS")
                    .setPositiveButton("Ask me") { _, _ ->
                        requestSmsPermission()
                    }
                    .setNegativeButton("No") { _, _ ->
                        notifyDetailFragment(false)
                    }.show()
            } else {
                requestSmsPermission()
            }

        } else {
            notifyDetailFragment(true)
        }
    }
}