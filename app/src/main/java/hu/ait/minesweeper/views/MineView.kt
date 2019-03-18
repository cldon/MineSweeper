package hu.ait.minesweeper.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.ait.minesweeper.models.MineModel
import android.support.design.widget.Snackbar


class MineView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mineBlock = Paint()
    private val coveredBlock = Paint()
    private val safeBlock = Paint()
    private val paintLine = Paint()
    private val paintText = Paint()
    private val paintFlag = Paint()

    private var bombsLeft = MineModel.numBombs[MineModel.level] + 1
    private var finished = true

    var levelSelected = -1

    var flag = false

    init {
        mineBlock.color = Color.rgb(233, 177, 189)
        mineBlock.style = Paint.Style.FILL

        safeBlock.color = Color.rgb(169, 243, 254)
        safeBlock.style = Paint.Style.FILL

        coveredBlock.color = Color.rgb(106, 216, 126)
        coveredBlock.style = Paint.Style.FILL

        paintFlag.color = Color.RED
        paintFlag.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 8f

        paintText.color = Color.WHITE
        paintText.textSize = 100f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawFields(canvas)
        drawGameBoard(canvas)
    }

    private fun drawFields(canvas: Canvas?) {
        for (i in 0..MineModel.dim[MineModel.level]) {
            for (j in 0..MineModel.dim[MineModel.level]) {

                val left = i * width / (MineModel.dim[MineModel.level] + 1).toFloat()
                val top = j * height / (MineModel.dim[MineModel.level] + 1).toFloat() +
                        height / (MineModel.dim[MineModel.level] + 1).toFloat()
                val right = i * width / (MineModel.dim[MineModel.level] + 1).toFloat() +
                        width / (MineModel.dim[MineModel.level] + 1).toFloat()
                val bottom = j * height / (MineModel.dim[MineModel.level] + 1).toFloat()

                val cX = (left + right) / 2
                val cY = (top + bottom) / 2

                if (!MineModel.getFieldContent(i, j).wasClicked) {
                    canvas?.drawRect(left, top, right, bottom, coveredBlock)

                } else if (MineModel.getFieldContent(i, j).isBomb) {
                    canvas?.drawRect(left, top, right, bottom, mineBlock)

                } else {
                    canvas?.drawRect(left, top, right, bottom, safeBlock)
                    if (MineModel.getFieldContent(i, j).numBombs != 0) {
                        canvas?.drawText(MineModel.getFieldContent(i, j).numBombs.toString(), left, top, paintText)

                    }

                }
                if (MineModel.getFieldContent(i, j).isFlagged) {
                    canvas?.drawCircle(cX, cY, width / ((MineModel.dim[MineModel.level] + 1) * 3).toFloat(), paintFlag)
                }
            }
        }
    }

    fun toggleFlag() {
        flag = !flag
    }


    private fun drawGameBoard(canvas: Canvas?) {
        // border
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)

        for (i in 1..MineModel.dim[MineModel.level]) {
            canvas?.drawLine(
                0f, (i * height / (MineModel.dim[MineModel.level] + 1)).toFloat(), width.toFloat(),
                (i * height / (MineModel.dim[MineModel.level] + 1)).toFloat(), paintLine
            )
            canvas?.drawLine(
                (i * width / (MineModel.dim[MineModel.level] + 1)).toFloat(), 0f,
                (i * width / (MineModel.dim[MineModel.level] + 1)).toFloat(), height.toFloat(),
                paintLine
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (levelSelected == -1) {
            val snackbar = Snackbar.make(this, "Choose level to start playing!", Snackbar.LENGTH_LONG)
            snackbar.show()
        } else if (event?.action == MotionEvent.ACTION_DOWN && !finished) {

            val tX = event.x.toInt() / (width / (MineModel.dim[MineModel.level] + 1))
            val tY = event.y.toInt() / (height / (MineModel.dim[MineModel.level] + 1))

            if (tX <= MineModel.dim[MineModel.level] && tY <= MineModel.dim[MineModel.level]) {
                if (!MineModel.getFieldContent(tX, tY).wasClicked && !flag) {
                    //                (context as MainActivity).setStatusText(status)\
                    MineModel.getFieldContent(tX, tY).wasClicked = true

                    if (MineModel.getFieldContent(tX, tY).isBomb) {
                        gameOver()
                    }
                    if (MineModel.getFieldContent(tX, tY).numBombs == 0) {
                        revealNeighbors(tX, tY)
                    }
                } else if (flag) {
                    if (!MineModel.getFieldContent(tX, tY).wasClicked) {
                        MineModel.getFieldContent(tX, tY).isFlagged = !MineModel.getFieldContent(tX, tY).isFlagged
                        if (!MineModel.getFieldContent(tX, tY).isBomb) {
                            gameOver()
                        } else {
                            bombsLeft--
                        }
                    }
                }
                invalidate()
                if (bombsLeft == 0) {
                    finished = true
                    revealBombs()
                    val snackbar = Snackbar.make(this, "You won!", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        }
        return true
    }

    private fun revealBombs() {
        for (i in 0..MineModel.dim[MineModel.level]) {
            for (j in 0..MineModel.dim[MineModel.level]) {
                if (MineModel.getFieldContent(i, j).isBomb) {
                    MineModel.getFieldContent(i, j).wasClicked = true
                }
            }
        }
    }


    private fun gameOver() {
        finished = true
        revealBombs()
        val snackbar = Snackbar.make(this, "You lost!", Snackbar.LENGTH_LONG)
        snackbar.show()

    }

    private fun revealNeighbors(x: Int, y: Int) {
        for (i in x - 1..x + 1) {
            for (j in y - 1..y + 1) {
                if (i in 0..MineModel.dim[MineModel.level] && j in 0..MineModel.dim[MineModel.level]) {
                    if (!MineModel.getFieldContent(i, j).wasClicked && MineModel.getFieldContent(
                            i,
                            j
                        ).numBombs == 0
                    ) {
                        MineModel.getFieldContent(i, j).wasClicked = true
                        revealNeighbors(i, j)
                    } else {
                        MineModel.getFieldContent(i, j).wasClicked = true
                    }
                }
            }
        }
    }

    fun resetGame() {
        if (levelSelected == -1) {
            val snackbar = Snackbar.make(this, "Choose level to start playing", Snackbar.LENGTH_LONG)
            snackbar.show()
        } else {
            MineModel.level = levelSelected
            finished = false
            MineModel.resetModel()
            bombsLeft = MineModel.numBombs[MineModel.level] + 1
            invalidate()
        }

    }


}