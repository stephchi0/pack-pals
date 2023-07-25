package com.example.packpals

import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.ExpensesRepository
import com.example.packpals.repositories.ItineraryRepository
import com.example.packpals.repositories.OpenWeatherRepository
import com.example.packpals.repositories.PackingListRepository
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.PlacesRepository
import com.example.packpals.repositories.StorageRepository
import com.example.packpals.repositories.TripPhotosRepository
import com.example.packpals.repositories.TripsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @Named("openWeatherApiKey")
    fun provideOpenWeatherApiKey(): String {
        return BuildConfig.OPEN_WEATHER_API_KEY
    }

    @Provides
    @Singleton
    @Named("placesApiKey")
    fun providePlacesApiKey(): String {
        return BuildConfig.PLACES_API_KEY
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepository(auth)
    }

    @Singleton
    @Provides
    fun provideStorageRepository(storage: FirebaseStorage): StorageRepository {
        return StorageRepository(storage)
    }

    @Singleton
    @Provides
    fun providePalsRepository(db: FirebaseFirestore): PalsRepository {
        return PalsRepository(db.collection("pals"))
    }

    @Singleton
    @Provides
    fun provideTripsRepository(db: FirebaseFirestore): TripsRepository {
        return TripsRepository(db.collection("trips"))
    }

    @Singleton
    @Provides
    fun provideTripPhotosRepository(db: FirebaseFirestore): TripPhotosRepository {
        return TripPhotosRepository(db.collection("trip_photos"))
    }

    @Singleton
    @Provides
    fun provideExpensesRepository(db: FirebaseFirestore): ExpensesRepository {
        return ExpensesRepository(db.collection("expenses"))
    }

    @Singleton
    @Provides
    fun provideOpenWeatherRepository(@Named("openWeatherApiKey") apiKey: String): OpenWeatherRepository {
        return OpenWeatherRepository(apiKey)
    }

    @Singleton
    @Provides
    fun providePlacesRepository(openWeatherRepo: OpenWeatherRepository, @Named("placesApiKey") apiKey: String): PlacesRepository {
        return PlacesRepository(openWeatherRepo, apiKey)
    }

    @Singleton
    @Provides
    fun provideItineraryRepository(db: FirebaseFirestore, openWeatherRepo: OpenWeatherRepository, @Named("openWeatherApiKey") apiKey: String): ItineraryRepository {
        return ItineraryRepository(db.collection("itinerary"), OpenWeatherRepository(apiKey))
    }

    @Singleton
    @Provides
    fun providePackingListRepository(db: FirebaseFirestore): PackingListRepository {
        return PackingListRepository(db.collection("packing_list"))
    }
}