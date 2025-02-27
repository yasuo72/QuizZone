package com.example.quizwiz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.example.quizwiz.R
import com.example.quizwiz.constants.Constants
import com.example.quizwiz.databinding.ActivityCustomQuizBinding
import com.example.quizwiz.quiz.QuizClass

class CustomQuizActivity : AppCompatActivity() {

    private var binding: ActivityCustomQuizBinding? = null
    private var amount = 10
    private var category: Int? = null
    private var difficulty: String? = null
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomQuizBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        handleSeekBar()
        val categoryList = Constants.getCategoryStringArray()
        binding?.categorySpinner?.adapter = getSpinnerAdapter(categoryList)
        binding?.difficultySpinner?.adapter = getSpinnerAdapter(Constants.difficultyList)
        binding?.typeSpinner?.adapter = getSpinnerAdapter(Constants.typeList)
        handleCategorySpinner()
        handleDifficultySpinner()
        handleTypeSpinner()
        val quizClass = QuizClass(this)
        binding?.startCustomQuiz?.setOnClickListener {
            quizClass.getQuizList(amount, category, difficulty, type) 
        }
    }

    private fun handleSeekBar() {
        binding?.seekBarAmount?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                amount = progress
                val text = "Amount: $progress"
                binding?.tvAmount?.text = text
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun handleCategorySpinner() {
        binding?.categorySpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = if (position == 0)
                        null
                    else
                        position + 8
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // Nothing to do here
                }

            }
    }

    private fun handleDifficultySpinner() {
        binding?.difficultySpinner?.onItemSelectedListener = object :OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                difficulty = when(position)
                {
                    0-> null
                    1-> "easy"
                    2-> "medium"
                    else-> "hard"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Nothing to do here
            }

        }
    }

    private fun handleTypeSpinner(){
        binding?.typeSpinner?.onItemSelectedListener = object :OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                type = when(position)
                {
                    0-> null
                    1-> "multiple"
                    else-> "boolean"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Nothing to do here
            }

        }
    }

    private fun getSpinnerAdapter(list: List<String>): SpinnerAdapter {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}