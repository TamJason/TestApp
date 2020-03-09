package com.example.testapp.domain

import android.content.Context
import com.example.testapp.domain.model.Session
import com.example.testapp.service.remote.YoutubeApiService
import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes


class InitializationBusinessLogic {

    fun initializeCredentials(context: Context): GoogleAccountCredential? {

        if (isGooglePlayServicesAvailable(context)) {
            // Initialize credentials and service object.
            YoutubeApiService.initService(GoogleAccountCredential.usingOAuth2(
                context, listOf(YouTubeScopes.YOUTUBE_READONLY)
            )
                .setBackOff(ExponentialBackOff()))
            return YoutubeApiService.mCredential
        }
        return null
    }

    fun updateAccountName(accountName: String) {
        YoutubeApiService.mCredential?.selectedAccountName = accountName
        Session.accountName = accountName
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

}