package com.example.h_mal.sliidenewsreader.ui.login

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.h_mal.sliidenewsreader.R
import com.example.h_mal.sliidenewsreader.data.repositories.Repository
import com.example.h_mal.sliidenewsreader.ui.CompletionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {

    var name: String? = null
    var password: String? = null

    var authListener: CompletionListener? = null

    fun getCurrentLoggedInUser() = repository.getUser()


    fun onLoginButtonClick(view: View){
        authListener?.onStarted()
        if(name.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.onFailure(view.context.getString(R.string.login_invalid))
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val user = repository.attemptUser(name!!, password!!)

                user?.let {
                    withContext(Dispatchers.Main){
                        authListener?.onSuccess()
                    }

                    repository.setUserLoggedIn(name!!)
                    return@launch
                }

                withContext(Dispatchers.Main){
                    authListener?.onFailure(view.context.getString(R.string.login_failed))
                }

            }catch(e: Exception){
                withContext(Dispatchers.Main){
                    authListener?.onFailure(e.message!!)
                }

            }
        }

    }

}
