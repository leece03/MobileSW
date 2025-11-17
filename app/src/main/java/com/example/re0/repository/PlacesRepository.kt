package com.example.re0.repository

import com.example.re0.model.Place
import com.example.re0.model.PlaceType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlacesRepository@Inject constructor (
    private val firestore: FirebaseFirestore
){
    suspend fun getPlaces(): List<Place> {
        return firestore.collection("places")
            .get()
            .await()
            .toObjects(Place::class.java)
    }


    fun filterPlaces(
        items: List<Place>,
        type: PlaceType
    ): List<Place> {
        return items.filter { it.type == type }
    }
}