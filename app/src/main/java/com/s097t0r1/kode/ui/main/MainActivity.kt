package com.s097t0r1.kode.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.theme.KodeTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeTheme {
                Scaffold {
                    val viewState by viewModel.viewState.collectAsState(MainViewState.InitialLoadingUsers())

                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    onFilterClick: () -> Unit,
    onTabClick: (Department?) -> Unit,
    onRetryClick: () -> Unit,
) {


    when (viewState) {
        MainViewState.CriticalError -> MainErrorScreen(onRetryClick = onRetryClick)
//        else -> MainContentScreen()
    }



}

@Composable
fun MainContentScreen(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    onFilterClick: () -> Unit,
    onTabClick: (Department?) -> Unit,
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    Column(modifier = modifier) {
        SearchField(
            text = searchText,
            onTextChange = setSearchText,
            onFilterClick = onFilterClick,
        )
        DepartmentTabs(onTabClick)
        when (viewState) {
        }
    }

}

@Composable
fun MainErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(56.dp),
            painter = painterResource(R.drawable.ic_flying_saucer),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.critical_error_title),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = stringResource(R.string.critical_error_subtitle),
            style = MaterialTheme.typography.caption
        )
        TextButton(onClick = onRetryClick) {
            Text(text = stringResource(R.string.critical_error_retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainErrorScreenPreview() {
    MainErrorScreen(onRetryClick = {})
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    KodeTheme {
        SearchField(
            text = searchText,
            onTextChange = setSearchText,
            onFilterClick = {}
        )
    }
}