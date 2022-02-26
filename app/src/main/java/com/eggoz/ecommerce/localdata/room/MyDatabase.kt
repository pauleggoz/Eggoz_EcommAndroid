package com.eggoz.ecommerce.localdata.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomCart::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract val deatailcart: CartDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "subscriber_data_table"
                ).build()
            }
            return instance
        }
    }
}