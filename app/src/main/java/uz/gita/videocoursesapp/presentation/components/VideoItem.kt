package uz.gita.videocoursesapp.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uz.gita.videocoursesapp.R
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

@Composable
fun VideoItem(it: LessonData, onClick: (LessonData) -> Unit) {

    var expended by rememberSaveable { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expended) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .height(70.dp)
                    .width(110.dp)
                    .background(Color.Gray)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .alpha(0.7f)
            )

            AsyncImage(
                modifier = Modifier
                    .height(240.dp)
                    .width(380.dp)
                    .alpha(0.8f)
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .clip(RoundedCornerShape(0.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it.thumbnail)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_video),
                error = painterResource(id = R.drawable.ic_video),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            IconButton(
                modifier = Modifier
                    .width(110.dp)
                    .height(80.dp), onClick = { onClick(it) }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    modifier = Modifier
                        .alpha(0.7f)
                        .width(110.dp)
                        .height(60.dp)
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp),
                text = it.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = it.description,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 8.dp),
                maxLines = if (!expended) 2 else 10,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis
            )
            androidx.compose.material3.IconButton(onClick = { expended = !expended }) {
                Icon(
                    imageVector = if (!expended) Icons.Filled.ArrowDropDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = null
                )
            }
        }


//        Text(text = it.description, modifier = Modifier.padding(horizontal = 8.dp), maxLines = if (!expended)2 else 10, fontSize = 16.sp, overflow = TextOverflow.Ellipsis)
    }
}