package com.example.re0.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.re0.R
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController? = null,
                viewModel: AuthViewModel = hiltViewModel(),
                modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(// 재pr
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .width(332.dp)
                    .height(138.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "RE:0",
                    style = TextStyle(
                        fontSize = 50.02.sp,
                        lineHeight = 70.03.sp,

                        fontWeight = FontWeight(700),
                        color = Mint,
                    )
                )
                Text(
                    text = "지구를 위한 오늘의 실천이 \n내일의 제로를 만듭니다. ",
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 33.6.sp,

                        fontWeight = FontWeight(700),
                        color = Color(0xFF000000),
                    )
                )
            }
            Image(
                painter = painterResource(id = R.drawable.earth),
                contentDescription = null,
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(11.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally ,
            ) {
                OutlinedTextField(
                    modifier= Modifier.width(332.dp),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    modifier= Modifier.width(332.dp),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") }
                )
                Spacer(modifier= Modifier.height(20.dp))
                Row(
                    modifier=Modifier
                        .width(332.dp)
                        .height(66.dp)
                        .background(color = Mint, shape = RoundedCornerShape(size = 20.dp))
                        .padding(start = 66.dp, top = 16.dp, end = 66.dp, bottom = 16.dp)
                        .clickable {
                            viewModel.login(
                                email,
                                password,
                                onSuccess = {
                                    navController?.navigate("home") {
                                        popUpTo("Login") { inclusive = true }
                                    }
                                },
                                onFail = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                    ,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "로그인",
                        style = TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 33.6.sp,

                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Mint,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .width(332.dp)
                        .height(66.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(start = 66.dp, top = 16.dp, end = 66.dp, bottom = 16.dp)
                        .clickable {
                            viewModel.signUp(
                                email,
                                password,
                                onSuccess = {
                                    navController?.navigate("home") {
                                        popUpTo("Login") { inclusive = true }
                                    }
                                },
                                onFail = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                Text(
                    text = "회원가입",
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 33.6.sp,

                        fontWeight = FontWeight(700),
                        color =Mint,
                    )
                )
                }

            }
            Spacer(modifier= Modifier.height(80.dp))
        }
    }
}

