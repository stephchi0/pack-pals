    package com.example.packpals.viewmodels

    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.packpals.models.Itinerary_Item
    import com.example.packpals.models.SearchResultItem
    import com.example.packpals.repositories.ItineraryRepository
    import com.example.packpals.repositories.OpenWeatherRepository
    import com.example.packpals.repositories.PlacesRepository
    import com.example.packpals.repositories.TripsRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.launch
    import java.time.LocalDate
    import java.time.LocalDateTime
    import java.time.ZoneId
    import java.util.Date
    import javax.inject.Inject

    @HiltViewModel
    class ItineraryPageViewModel @Inject constructor(
        private val tripsRepo: TripsRepository,
        private val placesRepo: PlacesRepository,
        private val itineraryRepo: ItineraryRepository,
        private val openWeatherRepo : OpenWeatherRepository
    ) : ViewModel() {

        private val _itineraryItemsList: MutableLiveData<List<Itinerary_Item>> = MutableLiveData(
            emptyList()
        )
        val itineraryItemsList: LiveData<List<Itinerary_Item>> get() = _itineraryItemsList

        private val _searchResultsList: MutableLiveData<List<SearchResultItem>> = MutableLiveData(emptyList())

        val searchResultList: LiveData<List<SearchResultItem>> get() = _searchResultsList

        private val _currentItem =  MutableLiveData<Itinerary_Item>()
        val currentItem: LiveData<Itinerary_Item> get() = _currentItem

        @RequiresApi(Build.VERSION_CODES.O)
        private val _startDate: MutableLiveData<LocalDateTime> = MutableLiveData<LocalDateTime>(LocalDateTime.now())
        val startDate: LiveData<LocalDateTime> @RequiresApi(Build.VERSION_CODES.O)
        get() = _startDate

        @RequiresApi(Build.VERSION_CODES.O)
        private val _endDate: MutableLiveData<LocalDateTime> = MutableLiveData<LocalDateTime>(
            LocalDateTime.now())
        val endDate: LiveData<LocalDateTime> @RequiresApi(Build.VERSION_CODES.O)
        get() = _endDate

        private val _add: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        val add: LiveData<Boolean> get() = _add



        init{
            fetchItineraryItems()
        }

        fun fetchItineraryItems() {
            // TODO: not hardcore this later
            val tripId = tripsRepo.selectedTrip.tripId
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

        fun setCurrentItem(item:Itinerary_Item){
            _currentItem.value = item
        }

        fun setAdd(b:Boolean){
            _add.value = b
        }

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun createItem(name:String){

            _currentItem.value?.addLocation(name)

            val zoneId = ZoneId.systemDefault()
            val sDate = Date.from(startDate.value?.atZone(zoneId)?.toInstant())

            val eDate = Date.from(startDate.value?.atZone(zoneId)?.toInstant())
            _currentItem.value?.addStartDate(sDate)
            _currentItem.value?.addEndDate(eDate)

            _currentItem.value?.tripId = tripsRepo.selectedTrip.tripId

            if(_add.value == true){
                itineraryRepo.createItem(_currentItem.value!!)
            }else{
                itineraryRepo.updateItem(_currentItem.value!!)
            }
        }

        suspend fun setSearchedItem(main:String, secondary:String){

            val item = Itinerary_Item()
            item.addLocation(main)
            item.addAddress(secondary)

            val tripId = tripsRepo.selectedTrip.tripId
            //Todo: dont hardcore this later
            item.addTripId(tripId!!)

            val geo = placesRepo.locationDetailsFromString(main)
            item.addGeoPoint(geo!!)

            val weather = openWeatherRepo.fetchWeatherForLocation(geo.latitude, geo.longitude)
            item.addForecast(weather!!)

            _currentItem.value = item
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setStartDate(date:LocalDateTime){
            val temp = _startDate.value
            _startDate.value = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, temp?.hour!!, temp?.minute!!)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setStartTime(hour:Int, minute: Int){
            val temp = _startDate.value
            _startDate.value = LocalDateTime.of(temp?.year!!, temp?.monthValue!!, temp?.dayOfMonth!!, hour, minute)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun setEndDate(date:LocalDateTime){
            val temp = _endDate.value
            var hour = temp?.hour
            if (temp?.hour != null){
                hour = LocalDateTime.now().hour
            }
            var minute = temp?.minute
            if (temp?.minute != null){
                minute = LocalDateTime.now().minute
            }
            var sec = temp?.second
            if (temp?.second != null){
                sec = LocalDateTime.now().second
            }
            _endDate.value = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, hour!!, minute!!)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setEndTime(hour:Int, minute: Int){
            val temp = _endDate.value
            _endDate.value = LocalDateTime.of(temp?.year!!, temp?.monthValue!!, temp?.dayOfMonth!!, hour, minute)
        }

        suspend fun searchResults(search:String){
            val predictions = placesRepo.autocompleteResults(search)
            _searchResultsList.value = predictions!!
        }

        fun getCurrentTripId(): String? {
            return tripsRepo.selectedTrip.title
        }


    }
