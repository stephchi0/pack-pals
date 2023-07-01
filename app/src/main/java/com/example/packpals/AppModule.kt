package com.example.packpals

import com.example.packpals.repositories.ExpensesRepository
import com.example.packpals.repositories.TripsRepository
import com.example.packpals.repositories.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideExpensesRepository(db: FirebaseFirestore) : ExpensesRepository {
        return ExpensesRepository(db.collection("expenses"))
    }
}