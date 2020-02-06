package com.example.h_mal.sliidenewsreader.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.h_mal.sliidenewsreader.data.db.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getUserDao(): UserDao
//    abstract fun getCurrentUserDao(): CurrentUserDao
//    abstract fun getWidgetDao(): WidgetDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
                CoroutineScope(Dispatchers.Default).launch {
                    it.getUserDao().initiateUsers()
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.db"
            ).allowMainThreadQueries().build()
    }
}