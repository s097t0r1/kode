package com.s097t0r1.kode.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.kode.ui.theme.KodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeTheme {
                Scaffold {
                    MainScreen(Modifier.padding(it), {}, {})
                }

            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit,
    onTabClick: (Department?) -> Unit
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }

    Column(modifier = modifier) {
        SearchField(
            text = searchText,
            onTextChange = setSearchText,
            onFilterClick = onFilterClick,
        )
        DepartmentTabs(onTabClick)
    }
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