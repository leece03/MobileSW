package com.example.re0.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.re0.R
import com.example.re0.model.NewsItem

@Composable
fun NewsCard(modifier: Modifier = Modifier,
             news: NewsItem
){
    Column (
        modifier = modifier
            .padding(1.dp)
            .width(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(0.2.dp, Color.Black,shape = RoundedCornerShape(12.dp))
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(id = news.imgId),
            contentDescription = "뉴스 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )
        Text(news.source+" · "+news.date,
            fontSize = 10.sp,
            maxLines = 3,
            color = Color(0xFF3093AC),
            overflow = TextOverflow.Ellipsis,)
        Text(
            text = news.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

        Text(
            text = news.content,
            fontSize = 15.sp,
            maxLines = 3,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

    }
}
@Preview(showBackground = true)
@Composable
fun NwsCardPreview() {
    NewsCard(
        news =NewsItem(title="폐플라스틱 재활용, '화학적 분해' 혁신 기술 도입",
        content="열분해 기술로 복합 재질 플라스틱을 고품질 원료로 되돌리는 기술이 상용화를 앞두고 있습니다.",
        source="과학기술정보통신부",
        date=" 2025. 10. 28.", imgId=R.drawable.rectangle1_1,)
    )
}