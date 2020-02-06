package com.example.h_mal.sliidenewsreader.data.repositories

import com.example.h_mal.sliidenewsreader.data.db.AppDatabase
import com.example.h_mal.sliidenewsreader.data.network.MyApi
import com.example.h_mal.sliidenewsreader.data.network.SafeApiRequest
import com.example.h_mal.sliidenewsreader.data.network.responses.FeedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class Repository(
    private val db: AppDatabase,
    private val api: MyApi
) : SafeApiRequest() {

    fun attemptUser(username: String, password: String) = db.getUserDao().getUser(username,password)

    fun setUserLoggedIn(username: String) = db.getUserDao().trueLoginState(username)

    fun setUserLoggedOut(username: String) = db.getUserDao().falseLoginState(username)

    fun getUser() = db.getUserDao().getCurrentLoginUser()

    fun getCurrentUserRole() = db.getUserDao().getCurrentRole()

    suspend fun fetchData(): MutableList<FeedResponse>? {
        val response = apiRequest { api.getFeed() }

        val remover = removeFinder()
        response.removeIf { item ->
            item.id == null || item.datatype == remover
        }

        return response
    }

    fun removeFinder() : String{
        val role = getCurrentUserRole()
        return if (role == "PREMIUM"){
            "ADS"
        }else{
            "PREMIUM_NEWS"
        }
    }

}
