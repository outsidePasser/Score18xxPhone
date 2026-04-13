package com.op.score18xxphone

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat.getString
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

class RunInputDialog {
    public var popupDialog: AlertDialog

    constructor(parent: ViewGroup, company: Company, runNumber: Int) {
        val builder = AlertDialog.Builder(parent.context)
        var picker = LayoutInflater.from(parent.context).inflate(
            R.layout.or_run_chooser, parent, false
        ) as ViewGroup

        var companyNameView: TextView = picker.findViewById(R.id.run_input_company)
        companyNameView.text = company.name
        companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(company.color), BlendModeCompat.SRC_ATOP)
        companyNameView.setTextColor(Color.parseColor(company.textColor))

        var runNumberView: TextView = picker.findViewById(R.id.run_input_run_number)
        runNumberView.text = getString(parent.context, arrayOf(R.string.run_1, R.string.run_2, R.string.run_3)[runNumber])

        arrayOf(R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9).forEachIndexed { i, key ->
            var view: TextView = picker.findViewById(key)
            view.setOnClickListener {
                numberPress(i)
            }
        }

        builder.setView(picker)
        popupDialog = builder.create()
    }

    fun show() {
        popupDialog.show()
    }

    fun chooseRun(company: Company, price: String) {
        company.stockPrice = price.toInt()
        Games.changeHappened()
        popupDialog.dismiss()
    }

    fun numberPress(i: Int) {
        Log.i("MHA", "key press " + i.toString())
    }
}