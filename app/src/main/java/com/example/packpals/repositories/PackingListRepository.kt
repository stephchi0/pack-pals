package com.example.packpals.repositories

import com.example.packpals.models.Expense
import com.example.packpals.models.PackingListItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

class PackingListRepository @Inject constructor(private val packingListCollectionRef: CollectionReference) {

    suspend fun fetchPackingList(userId: String, tripId: String): List<PackingListItem>? {
        val packingListFilter = Filter.and(
            Filter.equalTo("tripId", tripId),
            Filter.or(
                Filter.equalTo("ownerId", userId),
                Filter.equalTo("group", true)
            )
        )
        return try{
            val snapshot = packingListCollectionRef.where(packingListFilter).get().await()
            val result = mutableListOf<PackingListItem>()
            for (document in snapshot.documents){
                val packingListItem = document.toObject(PackingListItem::class.java)
                if (packingListItem!=null){
                    packingListItem.itemId = document.id
                    result.add(packingListItem)
                }
            }
            result
        } catch(e:Exception){
            null
        }

    }

    suspend fun createPackingListItem(item: PackingListItem){
        packingListCollectionRef.add(item).await()
    }

    suspend fun updatePackingListItem(itemId: String,  updatedItem: PackingListItem) {
        packingListCollectionRef.document(itemId).set(updatedItem).await()
    }

}