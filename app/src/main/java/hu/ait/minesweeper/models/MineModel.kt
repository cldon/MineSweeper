package hu.ait.minesweeper.models

import kotlin.random.Random

object MineModel {

    var fieldMat = Array<Array<Field>>(5) { Array<Field>(5) {Field(false, 0, false,
        false)} }

    var dim = arrayOf(4, 6, 8) // dimension
    var numBombs = arrayOf(2, 6, 10)

    var level = 0

    private fun addMines() {
        for (i in 0..numBombs[level]) {
            var x = Random.nextInt(dim[level])
            var y = Random.nextInt(dim[level])

            while (fieldMat[x][y].isBomb) {
                x = Random.nextInt(dim[level])
                y = Random.nextInt(dim[level])
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
        for (i in 0..dim[level]) {
            for (j in 0..dim[level]) {
                fieldMat[i][j].numBombs = countBombs(i, j)
            }
        }
    }

    fun getFieldContent(x: Int, y: Int): Field {
        return fieldMat[x][y]
    }

    private fun clearModel() {
        for (i in 0..dim[level]) {
            for (j in 0..dim[level]) {
                fieldMat[i][j] =  Field(false, 0, false, false)
            }
        }
    }

    private fun setDims() {
        fieldMat = Array<Array<Field>>(dim[level]+1) { Array<Field>(dim[level]+1) {Field(false, 0, false,
            false)} }
//        System.out.println("Made mat have dim of " + dim[level]+1)
    }

    fun resetModel() {
        setDims()
//        clearModel()
        addMines()
        setFields()
    }
}
