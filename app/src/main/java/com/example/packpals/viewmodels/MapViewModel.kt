package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.packpals.models.MapModel

class MapViewModel : ViewModel() {

    /**
     *  The representation of a [MapModel.ModelLocation] in the ViewModel. Only [VMLocation]s are exposed to the View.
     */
    data class VMLocation(val id: Int, var title: String, var content: String, var visited: Boolean = false)  {
        constructor(location : MapModel.ModelLocation) : this(location.id, location.title, location.content, location.visited)
    }

    // model
    private val mapModel = MapModel()

    // list of all locations
    val map = MutableLiveData<MutableList<MutableLiveData<VMLocation>>>(mutableListOf())

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel>
                    create(modelClass: Class<T>, extras: CreationExtras): T {
                return MapViewModel() as T
            }
        }
    }

    /**
     * Returns a read-only version of [map], which stores read-only observables of [VMLocation].
     */
    fun getLocations() : LiveData<MutableList<LiveData<VMLocation>>> {
        return map as LiveData<MutableList<LiveData<VMLocation>>>
    }
}