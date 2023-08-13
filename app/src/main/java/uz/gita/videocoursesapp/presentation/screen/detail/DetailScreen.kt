package uz.gita.videocoursesapp.presentation.screen.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.hilt.getViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.videocoursesapp.R
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData
import uz.gita.videocoursesapp.navigation.AppScreen
import uz.gita.videocoursesapp.presentation.components.Shimmer
import uz.gita.videocoursesapp.presentation.components.VideoItem

class DetailScreen(private val lessonData: LessonData) : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel :DetailsContract.ViewModel = getViewModel<DetailViewModelImpl>()
        val uiState = viewModel.collectAsState()


        viewModel.onEventDispatcher(DetailsContract.Intent.Load(lessonData))
        DetailsScreenContent(lessonData = lessonData, uiState, viewModel::onEventDispatcher)
    }

    @Composable
    fun DetailsScreenContent(
        lessonData: LessonData,
        uiState: State<DetailsContract.UiState>,
        onEventDispatcher: (DetailsContract.Intent) -> Unit
    ) {
        val context = LocalContext.current
        var fullDescVisible by remember { mutableStateOf(false) }
        var lesson by remember { mutableStateOf(lessonData) }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                VideoView(videoUri = lesson.videoUrl)
                Image(
                    painter = painterResource(id = R.drawable.ic_landscape),
                    modifier = Modifier
                        .alpha(0.7f)
                        .height(40.dp)
                        .width(60.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            if ((context as Activity).requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                context.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            } else {
                                context.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            }
                        },
                    contentDescription = null
                )
            }
            Text(
                text = lesson.name,
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (!fullDescVisible) lesson.description.substring(
                        0,
                        40
                    ) else lesson.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Text(
                    text = if (!fullDescVisible) "...more" else "less",
                    fontSize = 16.sp,
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 2.dp)
                        .align(Alignment.BottomEnd)
                        .clickable { fullDescVisible = !fullDescVisible })
            }

            when(val uiStateValue = uiState.value){
                is DetailsContract.UiState.Lessons ->{
                    LazyColumn{
                        item {
                            Text(text = "Recommended for you", modifier = Modifier.padding(start = 12.dp), color = Color.Blue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        items(uiStateValue.list){
                            VideoItem(it = it, onClick = {
                                onEventDispatcher(DetailsContract.Intent.ChangeVideo(it))
                            })
                        }
                    }
                }
                DetailsContract.UiState.Load ->{
                    Shimmer()
                }
            }
        }
    }

    @SuppressLint("OpaqueUnitKey")
    @Composable
    fun VideoView(videoUri: String) {
        //        LocalContext.current.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        val context = LocalContext.current

        val exoPlayer = ExoPlayer.Builder(LocalContext.current)
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.Builder()
                    .setUri(videoUri)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
            }


        DisposableEffect(
            AndroidView(modifier = Modifier
                .fillMaxWidth(), factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                }
            })
        ) {
            onDispose { exoPlayer.release() }
        }
    }
}