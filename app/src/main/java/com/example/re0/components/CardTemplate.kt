package com.example.re0.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.re0.ui.theme.Mint

@Composable
fun CardTemplate(
    modifier: Modifier = Modifier,
    topColor: Color= Mint,
    bottomColor: Color=Color.White,
    topContent: @Composable () -> Unit = {},
    bottomContent: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = topColor,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.height(50.dp)
                .background(color = topColor)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Spacer(modifier = Modifier.height(10.dp))
            topContent()
            //Spacer(modifier = Modifier.height(10.dp))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = bottomColor)
                .padding(10.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                //Spacer(modifier = Modifier.height(10.dp))
                bottomContent()
                //Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CardPreview() {
    CardTemplate(
        topContent = {
            Text("챌린지 2", color = Color.White)
        },
        bottomContent = {
            Text("플라스틱 줄이기", color = Color.Black)
        }
    )
}