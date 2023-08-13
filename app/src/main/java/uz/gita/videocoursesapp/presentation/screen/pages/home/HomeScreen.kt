package uz.gita.videocoursesapp.presentation.screen.pages.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.videocoursesapp.R
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData
import uz.gita.videocoursesapp.navigation.AppScreen
import uz.gita.videocoursesapp.presentation.components.CustomSearchView
import uz.gita.videocoursesapp.presentation.components.Shimmer
import uz.gita.videocoursesapp.presentation.components.VideoItem
import uz.gita.videocoursesapp.ui.theme.EditeTextColor

class HomeScreen : AppScreen(), Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "All lessons"
            val icon = painterResource(id = R.drawable.ic_home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val viewModel: HomeScreenContract.ViewModel = getViewModel<HomeViewModelImpl>()

        val uiState = viewModel.collectAsState()

        HomeScreenContent(uiState, viewModel::onEventDispatcher)

    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    private fun HomeScreenContent(
        uiState: State<HomeScreenContract.UiState>,
        onEventDispatcher: (HomeScreenContract.Intent) -> Unit
    ) {
        val context = LocalContext.current
        var search by remember { mutableStateOf("") }
        var dialogDescription by remember { mutableStateOf("") }
        var buttontext by remember { mutableStateOf("") }
        var firstThreeIndex by remember { mutableIntStateOf(0) }
        var forPurchase by remember { mutableStateOf(true) }
        var lessonData: LessonData? by remember {
            mutableStateOf(null)
        }
        val swipeRefresh = rememberSwipeRefreshState(isRefreshing = false)

        val sheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = sheetState
        )
        val scope = rememberCoroutineScope()

        onEventDispatcher(HomeScreenContract.Intent.Load(search, context))
        BottomSheetScaffold(
            sheetPeekHeight = 0.dp,
            sheetGesturesEnabled = true,
            scaffoldState = scaffoldState,
            sheetContent = {
                if (forPurchase) PurchaseBottomSheet(
                    lessonData,
                    firstThreeIndex < 3
                ) {
                    onEventDispatcher(HomeScreenContract.Intent.PurchaseLesson(it))
                    onEventDispatcher(HomeScreenContract.Intent.OpenDetailScreen(it))
                } else ErrorBottomSheet {
                    onEventDispatcher(
                        HomeScreenContract.Intent.Load(search, context)
                    )
                }
            },
        ) {

            Surface(modifier = Modifier
                .background(EditeTextColor)
                .clickable {
                    scope.launch {
                        sheetState.collapse()
                    }
                }) {
                SwipeRefresh(
                    state = swipeRefresh,
                    onRefresh = {
                        onEventDispatcher(HomeScreenContract.Intent.Load(search, context))
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(EditeTextColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Lessons",
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                fontStyle = FontStyle(500),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp
                            )
                        }


                        CustomSearchView(search = search, onValueChange = {
                            search = it
                        })

                        when (val uiStateValue = uiState.value) {
                            is HomeScreenContract.UiState.Courses -> {
                                firstThreeIndex = uiStateValue.index
                                Log.d("TTT", "$firstThreeIndex")
                                scope.launch {
                                    sheetState.collapse()
                                }
                                swipeRefresh.isRefreshing = false
                                forPurchase = true


                                LazyColumn(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    items(uiStateValue.list) { it ->
                                        VideoItem(it = it) { lesson ->
                                            Log.d("TTT", lesson.isPurchased.toString())
                                            if (!lesson.isPurchased) {
                                                lessonData = lesson

                                                /* onEventDispatcher(
                                                     HomeScreenContract.Intent.PurchaseLesson(
                                                         lesson
                                                     )
                                                 )*/

                                                scope.launch {
                                                    if (sheetState.isCollapsed) {
                                                        sheetState.expand()
                                                    } else {
                                                        sheetState.collapse()
                                                    }
                                                }
                                            }
                                            else{
                                                onEventDispatcher(HomeScreenContract.Intent.OpenDetailScreen(lesson))
                                            }
                                            /*if (lesson.isPurchased || firstThreeIndex < 2) {
                                                onEventDispatcher(
                                                    HomeScreenContract.Intent.PurchaseLesson(
                                                        lesson
                                                    )
                                                )
                                            } else {
                                                lessonData = lesson
                                                scope.launch {
                                                    if (sheetState.isCollapsed) {
                                                        sheetState.expand()
                                                    } else {
                                                        sheetState.collapse()
                                                    }
                                                }
                                            }*/
                                        }
                                    }

                                }
                            }

                            HomeScreenContract.UiState.HasNowConnection -> {
                                scope.launch {
                                    if (sheetState.isCollapsed) {

                                        forPurchase = false
                                        sheetState.expand()
                                    } else {
                                        sheetState.collapse()
                                    }
                                }
                            }

                            is HomeScreenContract.UiState.Loading -> {
                                firstThreeIndex = uiStateValue.index
                                swipeRefresh.isRefreshing = uiStateValue.refreshState
                                Shimmer()
                            }
                        }
                    }
                }
            }
        }
    }




    @Composable
    fun PurchaseBottomSheet(
        lessonData: LessonData?,
        isFree: Boolean,
        onClick: (LessonData) -> Unit
    ) {
        Box(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .background(color = Color.Transparent)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = lessonData?.name ?: "",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = if (!isFree) "Do you want purchase this course?" else "The first 3 lessons are free",
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(
                    modifier = Modifier
                        .height(0.dp)
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Blue)
                        .clickable {
                            onClick(lessonData!!)
                        }

                ) {
                    Text(
                        text = if (isFree) "See free" else "Purchase",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
            }
        }
    }

    @Composable
    fun ErrorBottomSheet(onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Please, try again", fontSize = 22.sp, fontWeight = FontWeight(500))
            TextButton(onClick = { onClick() }) {
                Text(text = "Try again", color = Color.Blue)
            }
        }
    }

}