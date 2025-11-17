package com.example.re0.viewModel

import androidx.lifecycle.ViewModel
import com.example.re0.model.Place
import com.example.re0.model.PlaceType
import com.example.re0.repository.PlacesRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val repository: PlacesRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    init {
        loadPlaces()
    }
    private val _filteredPlaces = MutableStateFlow<List<Place>>(emptyList())

    fun applyFilter(type: PlaceType) {
        val list = _places.value.filter { it.type == type }
        _filteredPlaces.value = list
    }
    private fun loadPlaces() {
        db.collection("places")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.toObject(Place::class.java)
                }
                _places.value = list
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}