package hu.ait.minesweeper.models

import kotlin.random.Random

object MineModel {

    private val fieldMat : Array<Array<Field>> = arrayOf(
            arrayOf(
                Field(false, 0, false, false),
                Field(false, 0, false, false),
                Field(false, 0, false, false),
                Field(false, 0, false, false),
                Field(false, 0, false, false)
            ),
           arrayOf(
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false)
           ),
           arrayOf(
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false)
           ),
           arrayOf(
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false),
               Field(false, 0, false, false)
           ),
            arrayOf(
                Field(false, 0, false, false),
                Field(false, 0, false, false),
                Field(false, 0, false, false),
                Field(false, 0, false, false),
                Field(false, 0, false, false)
            )
           )




    private fun addMines() {
        for (i in 0..2) {
            var x = Random.nextInt(4)
            var y = Random.nextInt(4)

            while (fieldMat[x][y].isBomb) {
                x = Random.nextInt(4)
                y = Random.nextInt(4)
            }

            fieldMat[x][y].isBomb = true
        }
    }

    private fun countBombs(x: Int, y: Int): Int {
        var ret = 0

        for (i in x-1..x+1) {
            for (j in y-1..y+1) {
                if (i >= 0 && i < fieldMat.size && j >= 0 && j < fieldMat.size) {
                    ret = if (fieldMat[i][j].isBomb) ret + 1 else ret
                }
            }
        }
        return ret
    }

    private fun setFields() {
        for (i in 0..4) {
            for (j in 0..4) {
                fieldMat[i][j].numBombs = countBombs(i, j)
            }
        }
    }

    fun getFieldContent(x: Int, y: Int): Field {
        return fieldMat[x][y]
    }

    private fun clearModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                fieldMat[i][j] =  Field(false, 0, false, false)
            }
        }
    }

    fun resetModel() {

        clearModel()
        addMines()
        setFields()
    }
}
