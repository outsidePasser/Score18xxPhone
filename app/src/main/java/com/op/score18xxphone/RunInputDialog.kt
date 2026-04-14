package com.op.score18xxphone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

class RunInputDialog(context: Context, company: Company, runNumber: Int) {
    private val popupDialog: AlertDialog
    private var currentInput: String = ""
    private var clearOnNextPress: Boolean = false
    private lateinit var runDisplay: TextView

    init {
        val builder = AlertDialog.Builder(context)
        val picker = LayoutInflater.from(context).inflate(
            R.layout.or_run_chooser, null, false
        ) as ViewGroup

        val companyNameView: TextView = picker.findViewById(R.id.run_input_company)
        companyNameView.text = company.name
        companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(company.colorInt(), BlendModeCompat.SRC_ATOP)
        companyNameView.setTextColor(company.textColorInt())

        val runNumberView: TextView = picker.findViewById(R.id.run_input_run_number)
        runNumberView.text = context.getString(arrayOf(R.string.run_1, R.string.run_2, R.string.run_3)[runNumber])

        runDisplay = picker.findViewById(R.id.run_display)
        val existingValue = company.runs[runNumber]
        if (existingValue != 0) {
            currentInput = existingValue.toString()
            runDisplay.text = currentInput
            clearOnNextPress = true
        }

        arrayOf(R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9).forEachIndexed { i, key ->
            picker.findViewById<TextView>(key).setOnClickListener { numberPress(i) }
        }

        picker.findViewById<TextView>(R.id.key_x).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                runDisplay.text = currentInput
            }
        }

        picker.findViewById<TextView>(R.id.key_ok).setOnClickListener {
            chooseRun(company, runNumber)
        }

        builder.setView(picker)
        popupDialog = builder.create()
    }

    fun show() = popupDialog.show()

    private fun chooseRun(company: Company, runNumber: Int) {
        val value = if (currentInput.isEmpty()) 0 else currentInput.toInt()
        company.runs[runNumber] = value
        company.runsExplicitlySet[runNumber] = true

        if (runNumber == 0) {
            if (!company.runsExplicitlySet[1]) company.runs[1] = value
            if (!company.runsExplicitlySet[2]) company.runs[2] = value
        }

        Games.changeHappened()
        popupDialog.dismiss()
    }

    private fun numberPress(i: Int) {
        if (clearOnNextPress) {
            currentInput = ""
            clearOnNextPress = false
        }
        currentInput += i.toString()
        runDisplay.text = currentInput
    }
}
