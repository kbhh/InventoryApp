package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemRoomDB: RoomDatabase() {
    abstract fun itemDao(): ItemDao
    companion object{
        @Volatile
        private var INSTANCE : ItemRoomDB? = null
        fun getDatabase(context: Context): ItemRoomDB {
            return INSTANCE ?: synchronized(this) {
               val instance = Room.databaseBuilder(
                    context,
                    ItemRoomDB::class.java,
                    "inventory_db")
                   .fallbackToDestructiveMigration()
                    .build();

                INSTANCE = instance
               return  instance
            }
        }
    }
}