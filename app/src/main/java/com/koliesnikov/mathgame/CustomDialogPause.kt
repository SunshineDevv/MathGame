package com.koliesnikov.mathgame

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.koliesnikov.mathgame.databinding.CustomDialogPauseWindowStyleBinding

class CustomDialogPause(context:Context): Dialog(context) {

    private var binding: CustomDialogPauseWindowStyleBinding? = null

    var onHomeButtonClick: (() -> Unit)? = null
    var onContinueButtonClick: (() -> Unit)? = null
    var onRestartButtonClick: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomDialogPauseWindowStyleBinding.inflate(layoutInflater)
        binding?.root?.let { setContentView(it) }

        binding?.continueButton?.setOnClickListener {
            onContinueButtonClick?.invoke()
            dismiss()
        }

        binding?.homeButton?.setOnClickListener {
            onHomeButtonClick?.invoke()
            dismiss()
        }

        binding?.restartButton?.setOnClickListener {
            onRestartButtonClick?.invoke()
            dismiss()
        }

    }
}