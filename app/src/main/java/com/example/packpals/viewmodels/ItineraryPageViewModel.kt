    package com.example.packpals.viewmodels

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.packpals.models.Expense
    import com.example.packpals.models.Itinerary_Item
    import com.example.packpals.repositories.ItineraryRepository
    import com.example.packpals.repositories.PlacesRepository
    import com.example.packpals.repositories.TripsRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.launch
    import java.text.SimpleDateFormat
    import java.util.Date
    import javax.inject.Inject
    import kotlin.math.round

    @HiltViewModel
    class ItineraryPageViewModel @Inject constructor(
        private val tripsRepo: TripsRepository,
        private val placesRepo: PlacesRepository,
        private val itineraryRepo: ItineraryRepository
    ) : ViewModel() {

        private val _itineraryItemsList: MutableLiveData<List<Itinerary_Item>> = MutableLiveData(
            emptyList()
        )
        val itineraryItemsList: LiveData<List<Itinerary_Item>> get() = _itineraryItemsList

//        private val _itineraryItemsInfoList: MutableLiveData<List<Itinerary_Item>> = _itineraryItemsList
//

//        private val _reccItineraryItemsList: MutableLiveData<MutableList<Itinerary_Item>> = MutableLiveData()


        init{
            fetchItineraryItems()
        }

        fun fetchItineraryItems() {
            // TODO: not hardcore this later
            val tripId = "SxXsH6jHElrx3oocliFY"
            if (tripId != null) {
                viewModelScope.launch {
                    val result = itineraryRepo.fetchItems(tripId)
                    if (result != null) {
                        result.forEach {item ->
                            val photoReference = placesRepo.photoIdFromName(item.location!!)
                            val photoURL = null
                            if (photoReference != null){
                                val photoURL = placesRepo.photoUrlFromId(photoReference)
                                item.addPhotoReference(photoURL!!)
                            }
                        }
                        _itineraryItemsList.value = result!!
                    }
                }
            }
        }

    }
