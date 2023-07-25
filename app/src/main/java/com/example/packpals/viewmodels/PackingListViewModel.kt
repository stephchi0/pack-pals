package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.PackingListItem
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PackingListRepository
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackingListViewModel @Inject constructor(private val authRepo: AuthRepository,
                                               private val palsRepo: PalsRepository,
                                               private val tripsRepo: TripsRepository,
                                               private val packingListRepo: PackingListRepository):
    ViewModel() {

    private val _packingList: MutableLiveData<List<PackingListItem>> = MutableLiveData(emptyList())
    val packingList: LiveData<List<PackingListItem>> get() = _packingList

    fun fetchPackingList(){
        val userId = authRepo.getCurrentUID()
        val tripId = tripsRepo.selectedTrip.tripId
        if(userId!=null && tripId !=null){
            viewModelScope.launch{
                val result = packingListRepo.fetchPackingList(userId,tripId)
                if (result!=null){
                    _packingList.value = result!!
                }
            }
        }
    }

    fun getUserId(): String {
        return authRepo.getCurrentUID() ?: ""
    }

    fun editPacked(item: PackingListItem){
        val itemId = item.itemId
        if (itemId != null){
            val updatedItem = PackingListItem(item.itemId, item.title,item.tripId,item.ownerId,
                !item.packed!!,item.group)
            viewModelScope.launch{
                packingListRepo.updatePackingListItem(itemId,updatedItem)
                fetchPackingList()
            }
        }
    }

    fun createPackingListItem(title: String, group:Boolean) {
        val userId = authRepo.getCurrentUID()
        val tripId = tripsRepo.selectedTrip.tripId
        viewModelScope.launch{
            val newItem = PackingListItem(title=title, tripId = tripId, ownerId = userId, packed=false, group=group)
            packingListRepo.createPackingListItem(newItem)
            fetchPackingList()
        }
    }
}