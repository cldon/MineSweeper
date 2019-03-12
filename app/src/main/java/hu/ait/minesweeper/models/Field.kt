package hu.ait.minesweeper.models

data class Field(var isBomb: Boolean, var numBombs: Int,
                 var isFlagged: Boolean, var wasClicked: Boolean)

