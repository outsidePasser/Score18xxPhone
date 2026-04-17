package com.op.score18xxphone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CashInputDialog(context: Context, playerName: String, label: String, initialValue: Int, private val onSave: (Int) -> Unit) {
    constructor(context: Context, player: Player, onSave: (Int) -> Unit) :
        this(context, player.name, context.getString(R.string.cash), player.cash, onSave)

    private val popupDialog: AlertDialog
    private var currentInput: String = ""
    private var clearOnNextPress: Boolean = false
    private val cashDisplay: TextView

    init {
        val builder = AlertDialog.Builder(context)
        val picker = LayoutInflater.from(context).inflate(
            R.layout.or_run_chooser, null, false
        ) as ViewGroup

        val playerNameView: TextView = picker.findViewById(R.id.run_input_company)
        playerNameView.text = playerName
        playerNameView.setBackgroundResource(R.drawable.rounded_corner_dark_gray)

        picker.findViewById<TextView>(R.id.run_input_run_number).text = label

        cashDisplay = picker.findViewById<TextView>(R.id.run_display)
        if (initialValue != 0) {
            currentInput = initialValue.toString()
            cashDisplay.text = currentInput
            clearOnNextPress = true
        }

        arrayOf(R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9).forEachIndexed { i, key ->
            picker.findViewById<TextView>(key).setOnClickListener { numberPress(i) }
        }

        picker.findViewById<TextView>(R.id.key_x).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                cashDisplay.text = currentInput
            }
        }

        picker.findViewById<TextView>(R.id.key_ok).setOnClickListener {
            onSave(if (currentInput.isEmpty()) 0 else currentInput.toInt())
            popupDialog.dismiss()
        }

        builder.setView(picker)
        popupDialog = builder.create()
    }

    fun show() {
        popupDialog.show()
    }

    private fun numberPress(i: Int) {
        if (clearOnNextPress) {
            currentInput = ""
            clearOnNextPress = false
        }
        currentInput += i.toString()
        cashDisplay.text = currentInput
    }
}
