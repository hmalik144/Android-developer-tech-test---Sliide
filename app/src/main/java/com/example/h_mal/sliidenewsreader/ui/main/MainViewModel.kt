package com.example.h_mal.sliidenewsreader.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.h_mal.sliidenewsreader.data.db.entities.User
import com.example.h_mal.sliidenewsreader.data.network.responses.FeedResponse
import com.example.h_mal.sliidenewsreader.data.repositories.Repository
import com.example.h_mal.sliidenewsreader.ui.CompletionListener
import kotlinx.coroutines.*
import java.io.IOException

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    fun getCurrentLoggedInUser() = repository.getUser()

    var completionLister : CompletionListener? = null

    val feed = MutableLiveData<MutableList<FeedResponse>>()

    fun setList(){
        completionLister?.onStarted()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val feedResponse = repository.fetchData()

                feedResponse?.let {
                    completionLister?.onSuccess()
                    feed.value = it
                    return@launch
                }

                completionLister?.onFailure("Failed to retrieve data")
            }catch(e: IOException){
                completionLister?.onFailure(e.message!!)
            }
        }

    }

    fun logOut(user: User?){
        CoroutineScope(Dispatchers.IO).launch {
            user?.username?.let {
                repository.setUserLoggedOut(it)
            }

        }
    }

}
