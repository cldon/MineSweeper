package hu.ait.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.ait.minesweeper.models.MineModel
import hu.ait.minesweeper.views.MineView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.CompoundButton



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener {
            mineView.resetGame()
            flagToggle.isChecked = false
            mineView.flag = false
        }

        flagToggle.setOnClickListener {
            mineView.toggleFlag()
        }

//        easySwitch.setOnCheckedChangeListener { buttonView, isChecked ->  }
        easySwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mediumSwitch.isChecked = false
            expertSwitch.isChecked = false
            mineView.levelSelected = 0
        })
        mediumSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            easySwitch.isChecked = false
            expertSwitch.isChecked = false
            mineView.levelSelected = 1
        })
        expertSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mediumSwitch.isChecked = false
            easySwitch.isChecked = false
            mineView.levelSelected = 2

        })
    }
}
