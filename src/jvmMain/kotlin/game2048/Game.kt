package game2048

import kotlin.random.Random

typealias Matrix<T> = List<List<T>>
typealias Field = Matrix<Int>

fun <T> Matrix<T>.mirrored() =
  map { it.reversed() }

fun Field.shiftLeft() = map {
  (it.collapsed() + listOf(0, 0, 0, 0)).take(4)
}

fun Field.shiftRight() = mirrored().shiftLeft().mirrored()
fun Field.shiftUp() = transpose().shiftLeft().transpose()
fun Field.shiftDown() = transpose().shiftRight().transpose()

fun <T> Matrix<T>.transpose() =
  List(size) { idx ->
    map { it[idx] }
  }

fun List<Int>.collapsed() =
  filter { it != 0 }
    .let { it + listOf(0) }
    .windowed(2)
    .step()

fun List<List<Int>>.step(): List<Int> =
  firstOrNull()?.let { (a, b) ->
    if (a == b) {
      listOf(a + b) + drop(2).step()
    } else {
      listOf(a) + drop(1).step()
    }
  } ?: listOf()

fun randomPiece() = if (Random.nextInt(10) == 0) 4 else 2

fun <T> Matrix<T>.putAt(x: Int, y: Int, value: T): Matrix<T> =
  mapIndexed { yy, row ->
    row.mapIndexed { xx, cell ->
      if (x == xx && y == yy) value else cell
    }
  }

fun Field.selectZeroes(): List<Pair<Int, Int>> =
  flatMapIndexed { y, row ->
    row.mapIndexed { x, cell ->
      (x to y) to cell
    }
  }.filter { (_, v) -> v == 0 }
    .map { (pos, _) -> pos }

fun Field.addPiece(): Field =
  selectZeroes().let { zs ->
    if (zs.isEmpty()) this
    else zs[Random.nextInt(zs.size)].let { (x, y) ->
      putAt(x, y, randomPiece())
    }
  }

fun randomField() =
  List(4) { List(4) { 0 } }
    .addPiece()
    .addPiece()
    .addPiece()
    .addPiece()
