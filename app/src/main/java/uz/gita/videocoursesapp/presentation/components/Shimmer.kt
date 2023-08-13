package uz.gita.videocoursesapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun Shimmer() {
    Column(modifier = Modifier.fillMaxSize()) {
        for (i in 0..5) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 0.dp, vertical = 8.dp)
                    .background(Color(0xFFFFFFFF))
                    .clip(RoundedCornerShape(28.dp))
                    .shimmer(),
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .padding(8.dp)
                        .background(Color(0xFFCFCCCC))
                )
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).height(30.dp).background(Color(0xFFCFCCCC)))
            }
        }
    }
}