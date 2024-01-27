package com.koliesnikov.mathgame

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.koliesnikov.mathgame.databinding.FragmentProcessPageBinding
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.random.Random

class ProcessPageFragment : Fragment() {

    private var binding: FragmentProcessPageBinding? = null

    private val args: ProcessPageFragmentArgs by navArgs()

    private val handler = Handler()

    private val delayMills: Long = 2000

    private var score = 0

    private var lives = 5

    private var startTime = 60000L

    private var intervalTime = 1000L

    private var progressTime = 0

    private var correctAnswers = 0

    private var wrongAnswers = 0

    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProcessPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeOfOperation = args.typeOfOperation

        when (typeOfOperation) {
            "addition" -> startProcess(typeOfOperation)
            "subtraction" -> startProcess(typeOfOperation)
            "multiplication" -> startProcess(typeOfOperation)
            "division" -> startProcess(typeOfOperation)
            "mixed" -> startProcess(typeOfOperation)
        }

        binding?.closeButton?.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.processPageFragmentTo_startPageFragment)
        }

        binding?.skipButton?.setOnClickListener {
            generateQuestionString(typeOfOperation)
            generateAnswerString()
        }
    }

    private fun generateQuestionString(typeOfOperation: String) {
        var number1 = 0
        var number2 = 0
        var number3 = 0
        var questionStr: String? = null
        when (typeOfOperation) {
            "addition" -> {
                number1 = Random.nextInt(0, 100)
                number2 = Random.nextInt(0, 100)
                questionStr = "$number1 + $number2 = ?"
            }

            "subtraction" -> {
                number1 = Random.nextInt(50, 200)
                number2 = Random.nextInt(0, 100)
                questionStr = "$number1 - $number2 = ?"
            }

            "multiplication" -> {
                number1 = Random.nextInt(0, 20)
                number2 = Random.nextInt(0, 11)
                questionStr = "$number1 * $number2 = ?"
            }

            "division" -> {
                do {
                    number1 = Random.nextInt(1, 101)
                    number2 = Random.nextInt(1, 20)
                } while (number1 % number2 != 0)
                questionStr = "$number1 / $number2 = ?"
            }

            "mixed" -> {
                val arithmeticSigns = listOf(" + ", " - ", " * ")
                val shuffledSigns = arithmeticSigns.shuffled()
                number1 = Random.nextInt(0, 20)
                number2 = Random.nextInt(0, 11)
                number3 = Random.nextInt(-5, 7)
                questionStr = "$number3${shuffledSigns[0]}$number2${shuffledSigns[1]}$number1"
            }
        }
        binding?.questionTextView?.text = questionStr
    }

    private fun calculateRightAnswer(): Int {
        val rightString = binding?.questionTextView?.text.toString().substringBefore("=").trim()
        val buildAnswer = ExpressionBuilder(rightString).build()
        return buildAnswer.evaluate().toInt()
    }

    private fun generateAnswerString(): Int {
        val resultAnswer = calculateRightAnswer()
        var randomAnswer1 = 0
        var randomAnswer2 = 0
        var randomAnswer3 = 0
        do {
            randomAnswer1 = Random.nextInt(50, 150)
            randomAnswer2 = Random.nextInt(0, 120)
            randomAnswer3 = Random.nextInt(0, 100)
        } while (randomAnswer1 == resultAnswer || randomAnswer2 == resultAnswer || randomAnswer3 == resultAnswer)
        val answers = listOf(resultAnswer, randomAnswer1, randomAnswer2, randomAnswer3)
        val shuffledAnswers = answers.shuffled()
        binding?.answerFirst?.text = shuffledAnswers[0].toString()
        binding?.answerSecond?.text = shuffledAnswers[1].toString()
        binding?.answerThird?.text = shuffledAnswers[2].toString()
        binding?.answerFourth?.text = shuffledAnswers[3].toString()
        return resultAnswer
    }

    private fun checkAnswer(typeOfOperation: String) {
        binding?.firstVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerFirst?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerFirst?.setTextColor(Color.parseColor("#21BA39"))
                binding?.firstVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerFirst?.setTextColor(Color.parseColor("#FF0000"))
                binding?.firstVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            }
        }

        binding?.secondVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerSecond?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerSecond?.setTextColor(Color.parseColor("#21BA39"))
                binding?.secondVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerSecond?.setTextColor(Color.parseColor("#FF0000"))
                binding?.secondVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            }
        }

        binding?.thirdVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerThird?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerThird?.setTextColor(Color.parseColor("#21BA39"))
                binding?.thirdVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerThird?.setTextColor(Color.parseColor("#FF0000"))
                binding?.thirdVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            }
        }

        binding?.fourthVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerFourth?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerFourth?.setTextColor(Color.parseColor("#21BA39"))
                binding?.fourthVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerFourth?.setTextColor(Color.parseColor("#FF0000"))
                binding?.fourthVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation)
            }
        }
    }

    private fun reloadAllValues(typeOfOperation: String) {
        handler.postDelayed({
            binding?.firstVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default)
            binding?.answerFirst?.setTextColor(Color.parseColor("#FF000000"))
            binding?.firstVariantLayout?.isClickable = true

            binding?.secondVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default)
            binding?.answerSecond?.setTextColor(Color.parseColor("#FF000000"))
            binding?.secondVariantLayout?.isClickable = true

            binding?.thirdVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default)
            binding?.answerThird?.setTextColor(Color.parseColor("#FF000000"))
            binding?.thirdVariantLayout?.isClickable = true

            binding?.fourthVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default)
            binding?.answerFourth?.setTextColor(Color.parseColor("#FF000000"))
            binding?.fourthVariantLayout?.isClickable = true

            binding?.resultLayout?.isVisible = false

            binding?.skipButton?.isVisible = true

            resetProgressBar()

            generateQuestionString(typeOfOperation)

            generateAnswerString()

        }, delayMills)

    }

    private fun updateScore() {
        correctAnswers++
        score += 10
        binding?.scoreTextView?.text = score.toString()
    }

    private fun updateLives(typeOfOperation: String) {
        lives--
        binding?.livesTextView?.text = lives.toString()
        val action = ProcessPageFragmentDirections.processPageFragmentToEndPageFragment(
            typeOfOperation,
            score,
            correctAnswers,
            wrongAnswers
        )
        if (lives <= 0) {
            handler.postDelayed({
                view?.let { Navigation.findNavController(it).navigate(action) }
            }, delayMills)
        }
    }

    private fun startProgressBar(typeOfOperation: String) {
        timer = object : CountDownTimer(startTime, intervalTime) {
            override fun onTick(currentTime: Long) {
                progressTime = (currentTime / 1000).toInt()
                binding?.progressBar?.progress = progressTime
            }

            override fun onFinish() {
                updateLives(typeOfOperation)
                binding?.skipButton?.isVisible = false
                binding?.resultTextView?.text = "Oops, time is over! "
                binding?.resultImageView?.setBackgroundResource(R.drawable.ic_time_over)
                binding?.resultLayout?.setBackgroundColor(Color.parseColor("#FD974F"))
                binding?.resultLayout?.isVisible = true
                unClickButtons()
                reloadAllValues(typeOfOperation)
            }
        }
        timer.start()
    }

    private fun resetProgressBar() {
        timer.cancel()
        timer.start()
    }

    private fun stopProgressBar() {
        timer.cancel()
    }

    private fun unClickButtons() {
        binding?.firstVariantLayout?.isClickable = false
        binding?.secondVariantLayout?.isClickable = false
        binding?.thirdVariantLayout?.isClickable = false
        binding?.fourthVariantLayout?.isClickable = false
    }

    private fun startProcess(typeOfOperation: String) {
        startProgressBar(typeOfOperation)
        binding?.scoreTextView?.text = score.toString()
        binding?.livesTextView?.text = lives.toString()
        binding?.skipButton?.isVisible = true
        generateQuestionString(typeOfOperation)
        generateAnswerString()
        checkAnswer(typeOfOperation)
    }

    private fun setRightAnswer() {
        binding?.resultTextView?.text = "Correct! "
        binding?.resultImageView?.setBackgroundResource(R.drawable.happy_smile)
        binding?.resultLayout?.setBackgroundColor(Color.parseColor("#21BA39"))
        binding?.resultLayout?.isVisible = true
    }

    private fun setWrongAnswer() {
        binding?.resultTextView?.text = "Wrong! "
        binding?.resultImageView?.setBackgroundResource(R.drawable.angry_smile)
        binding?.resultLayout?.setBackgroundColor(Color.parseColor("#FF0000"))
        binding?.resultLayout?.isVisible = true
    }

    private fun prepareForAnswer() {
        stopProgressBar()
        binding?.skipButton?.isVisible = false
    }

}

