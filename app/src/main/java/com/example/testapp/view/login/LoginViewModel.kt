package com.example.testapp.view.login

import android.accounts.Account
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.domain.InitializationBusinessLogic


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val initializationBusinessLogic = InitializationBusinessLogic()
    private val mutableAccountRequestIntent = MutableLiveData<Intent>()
    val accountRequestIntent: LiveData<Intent> = mutableAccountRequestIntent


    fun updateCredentials(accountName: String) {
        initializationBusinessLogic.updateAccountName(accountName)
    }

    fun initLoginProcess() {
        initializationBusinessLogic.initializeCredentials(getApplication())?.let {
            mutableAccountRequestIntent.value = it.newChooseAccountIntent()
        }
    }
}