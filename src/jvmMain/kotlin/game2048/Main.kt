package game2048

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun App(field: Field) {
  var cellSize by remember { mutableStateOf(100) }

  MaterialTheme {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .onSizeChanged { s ->
          minOf(s.width, s.height).let {
            cellSize = it / 4
          }
        }
    ) {
      field.forEach { row ->
        Row(modifier = Modifier
          .fillMaxWidth()
        ) {
          row.forEach { cell ->
            if (cell == 0) {
              Hole(cellSize)
            } else {
              Piece(cell, cellSize)
            }
          }
        }
      }
    }
  }
}

@Composable
fun Hole(size: Int) =
  Spacer(modifier = Modifier
    .size(with(LocalDensity.current) {
      maxOf(size, 100).toDp()
    })
  )

@Composable
fun Piece(value: Int, size: Int) =
  Text(
    text = value.toString(),
    fontSize = with(LocalDensity.current) {
      maxOf(size / 3, 30).toSp()
    },
    modifier = Modifier
      .background(
        color = value.asColor,
        shape = RoundedCornerShape(10.dp)
      )
      .border(
        width = 3.dp,
        color = Color(0,0,0),
        shape = RoundedCornerShape(10.dp)
      )
      .size(with(LocalDensity.current) {
        maxOf(size, 100).toDp()
      })
      .wrapContentSize()
  )

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
  var field by remember { mutableStateOf(randomField()) }

  fun move(f: Field.() -> Field) {
    val new = f(field)
    if (new != field) {
      field = new.addPiece()
    }
  }

  Window(
    title = "2048.kt",
    state = WindowState(
      position = WindowPosition(Alignment.Center),
      size = DpSize(600.dp, 600.dp),
    ),
    onCloseRequest = ::exitApplication,
    onKeyEvent = {
      if (it.type == KeyEventType.KeyDown) {
        when (it.key) {
          Key.DirectionUp -> move(Field::shiftUp)
          Key.DirectionDown -> move(Field::shiftDown)
          Key.DirectionLeft -> move(Field::shiftLeft)
          Key.DirectionRight -> move(Field::shiftRight)
          else -> Unit
        }
      }
      true
    }
  ) {
    App(field)
  }
}

private val Int.asColor: Color
  get() = when (this) {
    2 -> Color(0xffeee4da)
    4 -> Color(0xffede0c8)
    8 -> Color(0xfff2b179)
    16 -> Color(0xfff59563)
    32 -> Color(0xfff67c5f)
    64 -> Color(0xfff65e3b)
    128 -> Color(0xffedcf72)
    256 -> Color(0xffedcc61)
    512 -> Color(0xffedc850)
    1024 -> Color(0xffedc53f)
    2048 -> Color(0xffedc22e)
    else -> Color.Magenta
  }