package com.koliesnikov.mathgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.koliesnikov.mathgame.databinding.FragmentEndPageBinding

class EndPageFragment : Fragment() {

    private val args: EndPageFragmentArgs by navArgs()

    private var binding: FragmentEndPageBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEndPageBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typeOfOperation = args.typeOfOperation
        val totalScore = args.totalScore
        val correctAnswers = args.correctAnswers
        val wrongAnswers = args.wrongAnswers

        binding?.totalScoreTextView?.text = totalScore.toString()
        binding?.totalCorrectAnswersTextView?.text = correctAnswers.toString()
        binding?.totalWrongAnswersTextView?.text = wrongAnswers.toString()

        binding?.backToMenuButton?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.endPageFragmentTo_startPageFragment)
        }

        binding?.tryAgainButton?.setOnClickListener {
            val action = EndPageFragmentDirections.actionEndPageFragmentToProcessPageFragment(typeOfOperation)
            Navigation.findNavController(view).navigate(action)
        }

        binding?.closeButton?.setOnClickListener {
            requireActivity().finish()
        }

    }

}