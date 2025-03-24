package com.example.notessqlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notessqlite.todo.titleExtra
import com.example.notessqlite.ui.theme.NotesSQLiteTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class HomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesSQLiteTheme {
                recycleCodeBase()
            }
        }
    }
}

@Composable
fun ReuseableCodeBase(imageResId:Int, imageTxt:String,onClick:()->Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .background(colorResource(R.color.status_bar_color))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = imageResId),
            contentDescription = "icons",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = imageTxt,
            fontSize = 16.sp
        )
    }
}
@Composable
fun recycleCodeBase(){
    Column(
        modifier = Modifier
            .padding(
            start = 20.dp,
            top = 35.dp,
            end = 10.dp,
            bottom = 20.dp,
        )
            .fillMaxSize()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ){
        ReuseableCodeBase(
            imageResId = R.drawable.ic_notes,
            imageTxt = "Notes",
            onClick = {

            }
        )
        ReuseableCodeBase(
            imageResId = R.drawable.ic_todo,
            imageTxt = "Todo",
            onClick = {

            }
        )
        ReuseableCodeBase(
            imageResId = R.drawable.ic_archives,
            imageTxt = "Archives",
            onClick = {

            }
        )
        ReuseableCodeBase(
            imageResId = R.drawable.category_icon,
            imageTxt = "Folders",
            onClick = {

            }
        )
        ReuseableCodeBase(
            imageResId = R.drawable.birthday_icon,
            imageTxt = "Notes",
            onClick = {

            }
        )

    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesSQLiteTheme {
        recycleCodeBase()
    }
}