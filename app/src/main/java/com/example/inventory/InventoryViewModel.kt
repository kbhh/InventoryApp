package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao): ViewModel() {


    private val _selectedItem: MutableLiveData<Item> = MutableLiveData();
    val selectedItem: LiveData<Item>
        get() = _selectedItem

    fun setSelectedItem(item: Item) {
        _selectedItem.value = item
    }
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }
    fun getItems() : Flow<List<Item>> = itemDao.getAllItems()
   private fun insertItem(item: Item) {
       viewModelScope.launch {
           itemDao.insertItem(item)
       }
    }
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }
    fun updateItem(item:Item) {
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }
     fun addNewItem(itemName: String, itemPrice: String, itemQuantity: String) {
        val item = getNewItemEntry(itemName, itemPrice, itemQuantity)
        insertItem(item)
    }
   private fun getNewItemEntry(itemName: String, itemPrice: String, itemQuantity: String): Item {
         return Item(itemName= itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemQuantity.toInt())
   }
}

class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}