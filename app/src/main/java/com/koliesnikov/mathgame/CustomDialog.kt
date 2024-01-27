package com.koliesnikov.mathgame

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.koliesnikov.mathgame.databinding.CustomDialogWindowStyleBinding

class CustomDialog(context: Context) : Dialog(context) {
    private var binding: CustomDialogWindowStyleBinding? = null

    var onExitButtonClick: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomDialogWindowStyleBinding.inflate(layoutInflater)
        binding?.root?.let { setContentView(it) }

        binding?.noButton?.setOnClickListener {
            dismiss()
        }

        binding?.yesButton?.setOnClickListener {
            onExitButtonClick?.invoke()
            dismiss()
        }

    }
}