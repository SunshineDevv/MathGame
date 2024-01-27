package com.koliesnikov.mathgame

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.koliesnikov.mathgame.databinding.FragmentStartPageBinding

class StartPageFragment : Fragment() {

    private var binding: FragmentStartPageBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.additionLayout?.setOnClickListener {
            val action =
                StartPageFragmentDirections.startPageFragmentToProcessPageFragment("addition")
            Navigation.findNavController(view).navigate(action)
        }

        binding?.subtractionLayout?.setOnClickListener {
            val action =
                StartPageFragmentDirections.startPageFragmentToProcessPageFragment("subtraction")
            Navigation.findNavController(view).navigate(action)
        }

        binding?.multiplicationLayout?.setOnClickListener {
            val action =
                StartPageFragmentDirections.startPageFragmentToProcessPageFragment("multiplication")
            Navigation.findNavController(view).navigate(action)
        }

        binding?.divisionLayout?.setOnClickListener {
            val action =
                StartPageFragmentDirections.startPageFragmentToProcessPageFragment("division")
            Navigation.findNavController(view).navigate(action)
        }

        binding?.mixedQuestionsLayout?.setOnClickListener {
            val action =
                StartPageFragmentDirections.startPageFragmentToProcessPageFragment("mixed")
            Navigation.findNavController(view).navigate(action)
        }

        binding?.closeButton?.setOnClickListener {
            closeApp()
        }
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