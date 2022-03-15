package com.s097t0r1.kode.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.s097t0r1.kode.ui.theme.KodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        SearchField(
            text = searchText,
            onTextChange = setSearchText,
            onFilterClick = {},
        )
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