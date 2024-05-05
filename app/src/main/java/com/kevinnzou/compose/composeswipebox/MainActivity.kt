package com.kevinnzou.compose.composeswipebox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kevinnzou.compose.composeswipebox.ui.theme.ComposeSwipeBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSwipeBoxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            dragBoxScreen(navController = navController)
                        }
                        composable("list") {
                            AnchoredDragBoxList()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun dragBoxScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnchoredDragBoxAtEnd()
        Spacer()
        AnchoredDragBoxAtStart()
        Spacer()
        AnchoredDragBoxAtEnd2()
        Spacer()
        AnchoredDragBoxAtBoth()
        Spacer()
        AnchoredDragBoxWithText(onAnchoredDragStateChanged = {
            Log.d(
                "KZ",
                "Outer AnchoredDragBox TargetValue: $it ${it.currentValue} ${it.targetValue}"
            )
        })
        Spacer()
        AnchoredDragBoxWithIconAndText()
        Spacer()
        mainContent("AnchoredDragBox List") {
            navController.navigate("list")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun mainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwipeBoxAtEnd()
        Spacer()
        SwipeBoxAtStart()
        Spacer()
        SwipeBoxAtEnd2()
        Spacer()
        SwipeBoxAtBoth()
        Spacer()
        SwipeBoxWithText()
        Spacer()
        mainContent("SwipeBox List") {
            navController.navigate("list")
        }
    }
}

@Composable
fun Spacer() {
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSwipeBoxTheme {
        Greeting("Android")
    }
}