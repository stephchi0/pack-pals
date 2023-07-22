package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.PackingListItem
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PackingListViewModel @Inject constructor(private val authRepo: AuthRepository,
                                               private val palsRepo: PalsRepository,
                                               private val tripsRepo: TripsRepository): ViewModel() {

    private val _packingList: MutableLiveData<List<PackingListItem>> = MutableLiveData()

    val packingList: LiveData<List<PackingListItem>> get() = _packingList

    fun fetchPackingList(){

    }

    fun createPackingListItem(title: String, group:Boolean) {
        val userId = authRepo.getCurrentUID()
        val tripId = tripsRepo.selectedTrip.tripId
    }
}