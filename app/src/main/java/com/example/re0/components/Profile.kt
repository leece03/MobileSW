package com.example.re0.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.re0.model.Achievement
import com.example.re0.model.Profile
import com.example.re0.ui.theme.Mint
import com.example.re0.ui.theme.SkyBlue
import com.example.re0.ui.theme.Yello
import com.example.re0.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun MyProfile(
    navController: NavController,
    uiState: Profile,
    achievements: List<Achievement>,
    viewModel: ProfileViewModel,
) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email

    val context = LocalContext.current
    var editing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(uiState.name) }

    CardTemplate(
        topColor = Yello,
        bottomColor = Yello,
        topContent = {
            Text(text = "내 정보", color = Color.Black, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("나만의 목표 등록 및 달성 기록", fontSize = 20.sp)
        },
        bottomContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .height(IntrinsicSize.Min)
                ,

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(Color.White)
                        .fillMaxWidth()
                        .padding(10.dp),

                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    AsyncImage(
                        model = uiState.imageUrl,
                        contentDescription = "프로필 이미지",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Mint),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(25.dp))
                    Column (
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center){
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            if (!editing) {
                                Text("닉네임: ${uiState.name}", fontSize = 15.sp)
                            } else {
                                OutlinedTextField(
                                    value = newName,
                                    onValueChange = { newName = it },
                                    label = { Text("닉네임 수정") },
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                            FloatingActionButton( onClick = {
                                if (!editing) {
                                    editing = true
                                } else {
                                    val profileUpdate = userProfileChangeRequest {
                                        displayName = newName
                                    }

                                    user?.updateProfile(profileUpdate)
                                        ?.addOnSuccessListener {
                                            viewModel.updateUserProfile(
                                                name = newName,
                                                email = uiState.email
                                            )
                                            editing = false
                                            Toast.makeText(context, "닉네임 변경 완료!", Toast.LENGTH_SHORT).show()
                                        }
                                        ?.addOnFailureListener {
                                            Toast.makeText(context, "변경 실패", Toast.LENGTH_SHORT).show()
                                        }
                                }

                            },
                                containerColor = Yello,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit,
                                    contentDescription = "edit",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        Text("이메일: ${email}", fontSize = 15.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("달성기록:  ${achievements.size}", fontSize = 15.sp)
                                FloatingActionButton(onClick = {
                                    FirebaseAuth.getInstance().signOut()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true } // 뒤로가기로 홈 못 돌아오게
                                    }
                                },
                                    containerColor = Mint,
                                    modifier = Modifier.width(60.dp)
                                        .height(30.dp)
                                ) {
                                    Text("로그아웃", fontSize = 10.sp, color = SkyBlue)
                            }
                        }
                    }


                }
            }
        }
    )
}


/*
@Preview(showBackground = true)
@Composable
fun MyProfilePreview() {
    MyProfile(
        uiState = Profile(
            imageUrl = R.drawable.rectangle1_1,
            name = "홍길동",
            email = "hong@test.com",
            achievements = listOf(
                Achievement(
                    iconUrl = R.drawable.rectangle1_1,
                    date = "2023.10.25",
                    description = "재활용 10회 이상",
                    title = "배지1",

                    ),
                Achievement(
                    iconUrl = R.drawable.rectangle1_1,
                    date = "2023.10.25",
                    description = "재활용 10회 이상",
                    title = "배지1",

                    )
            )
        )
    )
}

 */