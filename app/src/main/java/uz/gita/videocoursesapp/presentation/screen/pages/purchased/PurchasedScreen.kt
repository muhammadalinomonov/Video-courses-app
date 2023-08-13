package uz.gita.videocoursesapp.presentation.screen.pages.purchased

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.videocoursesapp.R
import uz.gita.videocoursesapp.navigation.AppScreen
import uz.gita.videocoursesapp.presentation.components.Shimmer
import uz.gita.videocoursesapp.presentation.components.VideoItem

class PurchasedScreen : Tab, AppScreen() {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Your courses "
            val icon = painterResource(id = R.drawable.ic_basket)
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
        val viewModel: PurchasedContract.ViewModel = getViewModel<PurchasedViewModelImpl>()
        val uiState = viewModel.collectAsState()

        viewModel.onEventDispatcher(PurchasedContract.Intent.Load)
        PurchasedScreenContent(uiState, viewModel::onEventDispatcher)
    }

    @Composable
    fun PurchasedScreenContent(
        uiState: State<PurchasedContract.UiState>,
        onEventDispatcher: (PurchasedContract.Intent) -> Unit
    ) {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Lessons",
                        modifier = Modifier
                            .padding(start = 16.dp),
                        fontStyle = FontStyle(500),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp
                    )
                }
                when (val uiStateValue = uiState.value) {
                    is PurchasedContract.UiState.Lessons -> {
                        LazyColumn {
                            Log.d("KKK", uiStateValue.list.toString())
                            items(uiStateValue.list) {
                                VideoItem(it = it, onClick = {
                                    onEventDispatcher(PurchasedContract.Intent.OpenDetailsScreen(it))
                                })
                            }
                        }
                    }

                    PurchasedContract.UiState.Load -> {
                        Shimmer()
                    }
                }
            }
        }
    }

}