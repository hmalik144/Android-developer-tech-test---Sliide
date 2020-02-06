package com.example.h_mal.sliidenewsreader.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.h_mal.sliidenewsreader.R
import com.example.h_mal.sliidenewsreader.data.network.responses.FeedResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.feed_item.view.*


class ListAdapter(context: Context, objects: MutableList<FeedResponse>) :
    ArrayAdapter<FeedResponse>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.feed_item, null)!!
        }

        val currentFeedItem = getItem(position)

        view.textView.text = currentFeedItem?.title

        val slider = view.seekBar
        val imageView = view.imageView

        slider.visibility = View.GONE
        imageView.visibility = View.GONE

        val imgArray = currentFeedItem?.images

        imgArray?.let {
            imageView.visibility = View.VISIBLE
            it[0].url?.apply {
                displayImage(this,imageView)
            }

            val count = it.count() -1

            if (count > 0){
                slider.visibility = View.VISIBLE
                slider.max = count
                slider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        it[seekBar.progress].url?.let {url ->
                            displayImage(url,view.imageView)
                        }
                    }
                })
            }

        }
        return view
    }

    fun displayImage(url: String, imageView: ImageView){
        Picasso.get()
            .load(url)
            .placeholder(R.mipmap.ic_launcher)
            .resize(250, 250)
            .centerCrop()
            .into(imageView)
    }

}