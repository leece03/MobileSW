package com.example.re0.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.re0.components.FilterButton
import com.example.re0.components.PlaceItem
import com.example.re0.model.PlaceType
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.PlacesViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(navController: NavController, viewModel: PlacesViewModel = hiltViewModel(), backStackEntry: NavBackStackEntry){

    val places by viewModel.places.collectAsState()
    val filteredPlaces by viewModel.filteredPlaces.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.applyFilter(PlaceType.ZERO_WASTE)
    }
    val placeList = if (filteredPlaces.isNotEmpty()) {
        filteredPlaces
    } else {
        places
    }

    // 어떤 리스트를 보여줄지 선택
    val listToShow = if (filteredPlaces.isNotEmpty()) filteredPlaces else placeList
    // 서울 좌표
    var userLat= 37.5665
    var userLng=126.9780
    val seoul = LatLng(userLat, userLng)
// 카메라 초기 위치
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    viewModel.setUserLocation(userLat, userLng)


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(seoul, 12f)
    }
    val cameraPosition = cameraPositionState.position.target
    LaunchedEffect(cameraPosition) {
        viewModel.updateDistances(cameraPosition.latitude, cameraPosition.longitude)
    }

    Scaffold (
        topBar = { TopBar() },
        bottomBar ={ BottomBar(navController)}
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
// 지도 컴포넌트
            Column(
                modifier = Modifier.height(400.dp)
                    .fillMaxWidth()
                    .background(Mint)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    placeList.forEach { item ->
                        val markerIcon = if (item.type == PlaceType.ZERO_WASTE) {
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        } else if(item.type == PlaceType.TUMBLER_DISCOUNT){
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        }
                        else{
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                        }

                        Marker(
                            state = MarkerState(
                                position = LatLng(item.lat, item.lng),
                            ),
                            title = item.name,
                            icon = markerIcon
                        )
                    }
                }
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Row {
                    FilterButton(
                        text = "제로웨이스트",
                        selected = selectedFilter == PlaceType.ZERO_WASTE
                    ) { viewModel.applyFilter(PlaceType.ZERO_WASTE) }

                    FilterButton(
                        text = "텀블러 할인",
                        selected = selectedFilter == PlaceType.TUMBLER_DISCOUNT
                    ) { viewModel.applyFilter(PlaceType.TUMBLER_DISCOUNT) }

                    FilterButton(
                        text = "분리수거장",
                        selected = selectedFilter == PlaceType.RECYCLE_BIN
                    ) { viewModel.applyFilter(PlaceType.RECYCLE_BIN) }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(listToShow) { place ->
                        PlaceItem(place)
                    }
                }

            }
        }
    }
}
