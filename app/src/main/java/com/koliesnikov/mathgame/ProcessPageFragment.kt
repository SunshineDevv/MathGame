package com.koliesnikov.mathgame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

    private var score = 0

    private var lives = 5

    private var delayMills = 2000L

    private var startTime = 60000L

    private var intervalTime = 1000L

    private var interruptedTime: Long? = null

    private var correctAnswers = 0

    private var wrongAnswers = 0

    private var timer: CountDownTimer? = null

    private lateinit var sharedPreferences: SharedPreferences
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

        binding?.pauseButton?.setOnClickListener {
            pauseApp(typeOfOperation)
        }

        binding?.skipButton?.setOnClickListener {
            generateQuestionString(typeOfOperation)
            generateAnswerString()
        }
    }

    private fun pauseApp(typeOfOperation: String) {
        stopProgressBar()
        val customDialogPause = CustomDialogPause(requireContext())

        val delay = 0L
        customDialogPause.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialogPause.setCancelable(false)

        customDialogPause.onContinueButtonClick = {
            interruptedTime?.let { startProgressBar(typeOfOperation, it) }
        }

        customDialogPause.onHomeButtonClick = {
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.processPageFragmentTo_startPageFragment)
            }
        }

        customDialogPause.onRestartButtonClick = {
            reloadLivesAndScore()
            reloadAllValues(typeOfOperation, delay)
        }

        customDialogPause.show()
    }

    private fun generateQuestionString(typeOfOperation: String) {
        var number1: Int
        var number2: Int
        val number3: Int
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
        var randomAnswer1: Int
        var randomAnswer2: Int
        var randomAnswer3: Int
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
                binding?.firstVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct_style)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerFirst?.setTextColor(Color.parseColor("#FF0000"))
                binding?.firstVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong_style)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            }
        }

        binding?.secondVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerSecond?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerSecond?.setTextColor(Color.parseColor("#21BA39"))
                binding?.secondVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct_style)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerSecond?.setTextColor(Color.parseColor("#FF0000"))
                binding?.secondVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong_style)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            }
        }

        binding?.thirdVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerThird?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerThird?.setTextColor(Color.parseColor("#21BA39"))
                binding?.thirdVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct_style)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerThird?.setTextColor(Color.parseColor("#FF0000"))
                binding?.thirdVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong_style)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            }
        }

        binding?.fourthVariantLayout?.setOnClickListener {
            prepareForAnswer()
            if (binding?.answerFourth?.text?.toString() == calculateRightAnswer().toString()) {
                updateScore()
                binding?.answerFourth?.setTextColor(Color.parseColor("#21BA39"))
                binding?.fourthVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_correct_style)
                setRightAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            } else {
                wrongAnswers++
                updateLives(typeOfOperation)
                binding?.answerFourth?.setTextColor(Color.parseColor("#FF0000"))
                binding?.fourthVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_wrong_style)
                setWrongAnswer()
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            }
        }
    }

    private fun reloadAllValues(typeOfOperation: String, delayMills: Long) {
        handler.postDelayed({
            binding?.firstVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default_style)
            binding?.answerFirst?.setTextColor(Color.parseColor("#FF000000"))
            binding?.firstVariantLayout?.isClickable = true

            binding?.secondVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default_style)
            binding?.answerSecond?.setTextColor(Color.parseColor("#FF000000"))
            binding?.secondVariantLayout?.isClickable = true

            binding?.thirdVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default_style)
            binding?.answerThird?.setTextColor(Color.parseColor("#FF000000"))
            binding?.thirdVariantLayout?.isClickable = true

            binding?.fourthVariantLayout?.setBackgroundResource(R.drawable.shape_rounded_default_style)
            binding?.answerFourth?.setTextColor(Color.parseColor("#FF000000"))
            binding?.fourthVariantLayout?.isClickable = true

            binding?.resultLayout?.isVisible = false

            binding?.skipButton?.isVisible = true

            binding?.pauseButton?.isClickable = true

            resetProgressBar(typeOfOperation, startTime)

            generateQuestionString(typeOfOperation)

            generateAnswerString()

        }, delayMills)

    }

    private fun reloadLivesAndScore() {
        score = 0
        lives = 5
        correctAnswers = 0
        wrongAnswers = 0
        binding?.scoreTextView?.text = score.toString()
        binding?.livesTextView?.text = lives.toString()
    }

    private fun updateScore() {
        correctAnswers++
        score += 10
        binding?.scoreTextView?.text = score.toString()
    }

    private fun updateLives(typeOfOperation: String) {
        lives--

        binding?.livesTextView?.text = lives.toString()

        if (lives <= 0) {
            saveEndPageData(typeOfOperation)
            handler.postDelayed({
                view?.let { Navigation.findNavController(it).navigate(R.id.processPageFragmentTo_endPageFragment) }
                reloadLivesAndScore()
            }, delayMills)
        }
    }

    private fun saveEndPageData(typeOfOperation: String){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putString("key_score", score.toString())
        editor.putString("key_correct", correctAnswers.toString())
        editor.putString("key_wrong", wrongAnswers.toString())

        editor.putString("key_operation", typeOfOperation)

        editor.apply()
    }

    private fun startProgressBar(typeOfOperation: String, startTime: Long) {
        timer = object : CountDownTimer(startTime, intervalTime) {
            override fun onTick(currentTime: Long) {
                interruptedTime = currentTime
                binding?.progressBar?.progress = (currentTime / 1000).toInt()
            }

            override fun onFinish() {
                updateLives(typeOfOperation)
                binding?.skipButton?.isVisible = false
                binding?.resultTextView?.text = getString(R.string.oops_time_is_over)
                binding?.resultImageView?.setBackgroundResource(R.drawable.ic_time_over)
                binding?.resultLayout?.setBackgroundColor(Color.parseColor("#FD974F"))
                binding?.resultLayout?.isVisible = true
                unClickButtons()
                reloadAllValues(typeOfOperation, delayMills)
            }
        }
        timer?.start()
    }

    private fun resetProgressBar(typeOfOperation: String, startTime: Long) {
        timer?.cancel()
        startProgressBar(typeOfOperation, startTime)
    }

    private fun stopProgressBar() {
        timer?.cancel()
    }

    private fun unClickButtons() {
        binding?.firstVariantLayout?.isClickable = false
        binding?.secondVariantLayout?.isClickable = false
        binding?.thirdVariantLayout?.isClickable = false
        binding?.fourthVariantLayout?.isClickable = false

        binding?.pauseButton?.isClickable = false
    }

    private fun startProcess(typeOfOperation: String) {
        startProgressBar(typeOfOperation, startTime)

        binding?.scoreTextView?.text = score.toString()
        binding?.livesTextView?.text = lives.toString()
        binding?.skipButton?.isVisible = true

        generateQuestionString(typeOfOperation)
        generateAnswerString()
        checkAnswer(typeOfOperation)
    }

    private fun setRightAnswer() {
        binding?.resultTextView?.text = getString(R.string.correct)
        binding?.resultImageView?.setBackgroundResource(R.drawable.ic_happy_smile)
        binding?.resultLayout?.setBackgroundColor(Color.parseColor("#21BA39"))
        binding?.resultLayout?.isVisible = true
    }

    private fun setWrongAnswer() {
        binding?.resultTextView?.text = getString(R.string.wrong)
        binding?.resultImageView?.setBackgroundResource(R.drawable.ic_angry_smile)
        binding?.resultLayout?.setBackgroundColor(Color.parseColor("#FF0000"))
        binding?.resultLayout?.isVisible = true
    }

    private fun prepareForAnswer() {
        stopProgressBar()

        binding?.skipButton?.isVisible = false
    }

}

