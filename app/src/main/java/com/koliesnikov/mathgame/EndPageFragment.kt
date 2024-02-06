package com.koliesnikov.mathgame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.koliesnikov.mathgame.databinding.FragmentEndPageBinding

class EndPageFragment : Fragment() {

    private var binding: FragmentEndPageBinding? = null

    private lateinit var sharedPreferences: SharedPreferences

    private var score : String? = null
    private var correct : String? = null
    private var wrong : String? = null

    private var viewStopped: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEndPageBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        val typeOfOperation = sharedPreferences.getString("key_operation", "").toString()

        loadData()

        binding?.backToMenuButton?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.endPageFragmentTo_startPageFragment)
        }

        binding?.tryAgainButton?.setOnClickListener {
            val action =
                EndPageFragmentDirections.actionEndPageFragmentToProcessPageFragment(typeOfOperation)
            Navigation.findNavController(view).navigate(action)
        }

        binding?.closeButton?.setOnClickListener {
            closeApp()
        }

    }

    override fun onPause() {
        super.onPause()
        viewStopped = true
    }

    override fun onResume() {
        super.onResume()
        if (viewStopped){
            closeApp()
        }
    }

    private fun loadData(){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        score = sharedPreferences.getString("key_score", "0")
        correct = sharedPreferences.getString("key_correct", "0")
        wrong = sharedPreferences.getString("key_wrong", "0")

        binding?.totalScoreTextView?.text = score
        binding?.totalCorrectAnswersTextView?.text = correct
        binding?.totalWrongAnswersTextView?.text = wrong
    }
    private fun closeApp(){
        val customDialog = CustomDialog(requireContext())

        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog.setCancelable(false)

        customDialog.onExitButtonClick = {
            requireActivity().finish()
        }

        customDialog.show()
    }
}