package com.example.inventory.data;

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item:Item );
    @Update
    suspend fun updateItem(item:Item);
    @Delete
    suspend fun deleteItem(item: Item);
    @Query("SELECT * FROM item WHERE id = :itemId")
    fun  getItem(itemId : Int): Flow<Item>;
    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<Item>>
}
