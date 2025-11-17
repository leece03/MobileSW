package com.example.re0.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.re0.components.FilterButton
import com.example.re0.model.PlaceType
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.PlacesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(viewModel: PlacesViewModel = hiltViewModel()){
    val placeList by viewModel.places.collectAsState()
    // 서울 좌표
    val seoul = LatLng(37.5665, 126.9780)
// 카메라 초기 위치
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(seoul, 12f)
    }
    Scaffold (
        topBar = { TopBar() },
        bottomBar ={ BottomBar()}
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
                        Marker(
                            state = MarkerState(
                                position = LatLng(item.lat, item.lng)
                            ),
                            title = item.name
                        )
                    }
                }
            }
            Column {
                Row {
                    FilterButton("제로웨이스트") { viewModel.applyFilter(PlaceType.ZERO_WASTE) }
                    FilterButton("텀블러 할인") { viewModel.applyFilter(PlaceType.TUMBLER_DISCOUNT) }
                    FilterButton("분리수거장") { viewModel.applyFilter(PlaceType.RECYCLE_BIN) }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen()
}
