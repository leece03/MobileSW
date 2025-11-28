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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun calculateDistance(
    lat1: Double, lng1: Double,
    lat2: Double, lng2: Double
): String  {
    val R = 6371e3 // 지구 반지름 (미터)

    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)

    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLng / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val distanceMeters= R * c // 결과는 미터 단위
    val distanceKm = distanceMeters / 1000
    return String.format("%.1f km", distanceKm)
}
@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val repository: PlacesRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    init {
        //loadDummyPlaces()
        loadPlaces()
    }
    private val _filteredPlaces = MutableStateFlow<List<Place>>(emptyList())
    val filteredPlaces: StateFlow<List<Place>> = _filteredPlaces
    private val _selectedFilter = MutableStateFlow<PlaceType?>(PlaceType.ZERO_WASTE)
    val selectedFilter: StateFlow<PlaceType?> = _selectedFilter
    fun applyFilter(type: PlaceType) {
        _selectedFilter.value = type
        val list = _places.value.filter { it.type == type }
        _filteredPlaces.value = list
    }
    fun updateDistances(lat: Double, lng: Double) {
        val updated = places.value.map { place ->
            place.copy(length = calculateDistance(lat, lng, place.lat, place.lng))
        }
        _places.value = updated
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