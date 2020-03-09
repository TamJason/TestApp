package com.example.testapp.view.login

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.testapp.R

import kotlinx.android.synthetic.main.fragment_login.*
import pub.devrel.easypermissions.EasyPermissions


class LoginFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private val viewModel: LoginViewModel by viewModels()
    private var accountRequestIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.accountRequestIntent.observe(this, Observer {
            accountRequestIntent = it
            loginSignInButton.isEnabled = true
        })

        viewModel.initLoginProcess()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginGoToSettingsButton.setOnClickListener { goToSettings() }
        loginSignInButton.setOnClickListener {
            accountRequestIntent?.let {
                startActivityForResult(
                    it,
                    REQUEST_ACCOUNT_PICKER
                )
            }
        }

        EasyPermissions.requestPermissions(
            this,
            getString(R.string.login_fragment_permission_rationale),
            REQUEST_PERMISSION_GET_ACCOUNTS,
            Manifest.permission.GET_ACCOUNTS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ACCOUNT_PICKER -> {
                if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
                    val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName != null) {
                        viewModel.updateCredentials(accountName)
                        navigateToPlayList()
                    }
                }
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        if (requestCode == REQUEST_PERMISSION_GET_ACCOUNTS) {
            setLoginStatus(false)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        if (requestCode == REQUEST_PERMISSION_GET_ACCOUNTS) {
            setLoginStatus(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_GET_ACCOUNTS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setLoginStatus(true)

                } else {
                    setLoginStatus(false)
                }
                return
            }
        }
    }

    private fun setLoginStatus(hasPermissions: Boolean) {
        loginSignInButton.isEnabled = hasPermissions
        loginUserEmpty.visibility = if (hasPermissions) View.GONE else View.VISIBLE
        loginGoToSettingsButton.visibility = if (hasPermissions) View.GONE else View.VISIBLE
    }

    private fun navigateToPlayList() {
        findNavController().navigate(
            R.id.toPlaylistFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
        )
    }

    private fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    companion object {
        private const val REQUEST_PERMISSION_GET_ACCOUNTS = 101
        private const val REQUEST_ACCOUNT_PICKER = 12131
    }
}