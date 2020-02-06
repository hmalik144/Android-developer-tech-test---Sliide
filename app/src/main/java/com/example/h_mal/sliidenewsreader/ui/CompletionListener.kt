package com.example.h_mal.sliidenewsreader.ui

interface CompletionListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}