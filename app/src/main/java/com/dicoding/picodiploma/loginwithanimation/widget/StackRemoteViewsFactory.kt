package com.dicoding.picodiploma.loginwithanimation.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.lifecycle.asFlow
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.db.Story
import com.dicoding.picodiploma.loginwithanimation.data.db.StoryDao
import com.dicoding.picodiploma.loginwithanimation.data.db.StoryRoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = ArrayList<Story>()
    private lateinit var dao: StoryDao

    override fun onCreate() {
        dao = StoryRoomDatabase.getDatabase(mContext.applicationContext).storyDao()
    }

    private fun fetchDataDB() {
        runBlocking {
            mWidgetItems = dao.getAllStories().asFlow().first().toMutableList() as ArrayList<Story>
            println(mWidgetItems)
        }
    }

    override fun onDataSetChanged() {
        fetchDataDB()
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        try {
            val bitmap: Bitmap = Glide.with(mContext.applicationContext)
                .asBitmap()
                .load(mWidgetItems[position].photoUrl)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.iv_item_photo, bitmap)
            rv.setTextViewText(R.id.tv_item_name, mWidgetItems[position].name)
            rv.setTextViewText(R.id.tv_item_description, mWidgetItems[position].description)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val extras = bundleOf(
            StoriesWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return rv


    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}