package com.example.tictactoe

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class GameActivity : AppCompatActivity() {

    //Creating a 2D Array of ImageViews
    private val boardCells = Array(3) { arrayOfNulls<TextView>(3) }

    //internal state
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        //calling the function to load our tic tac toe board
        loadBoard()

    }
    //function is mapping
    //the internal board to the ImageView array board
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j]?.text = getString(R.string.player1_icon)

                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.text = getString(R.string.player2_icon)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.text = ""
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }
// generate board
    private fun loadBoard() {

        val layout_board = findViewById<GridLayout>(R.id.layout_board)

        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = TextView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                boardCells[i][j]?.setTextColor(ContextCompat.getColor(this, R.color.black))

                //to make text centrally aligned
                boardCells[i][j]?.gravity = Gravity.CENTER

                //to make bold
                boardCells[i][j]?.setTypeface(null,Typeface.BOLD )

                //set text size
                boardCells[i][j]?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

                //attached a click listener to the board
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))

                layout_board.addView(boardCells[i][j])
            }
        }

    }

    inner class CellClickListener(
        private val i: Int,
        private val j: Int
    ) : View.OnClickListener {

        override fun onClick(p0: View?) {

            //checking if the game is not over
            if (!board.isGameOver) {

                //creating a new cell with the clicked index
                val cell = Cell(i, j)

                //placing the move for player
                board.placeMove(cell, Board.PLAYER)

                //calling minimax to calculate the computers move
                board.minimax(0, Board.COMPUTER)

                //performing the move for computer
                board.computersMove?.let {
                    board.placeMove(it, Board.COMPUTER)
                }

                //mapping the internal board to visual board
                mapBoardToUi()
            }

            var text_view_result = findViewById<TextView>(R.id.result)
            val result_str = getString(R.string.result_default)
            //Displaying the results
            //according to the game status
            when {
                board.hasComputerWon() -> text_view_result.text = result_str + "Computer Won"
                board.hasPlayerWon() -> text_view_result.text = result_str + "Player Won"
                board.isGameOver -> text_view_result.text = result_str + "Game Tied"
            }
        }
    }

}
