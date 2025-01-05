package com.op.score18xxphone

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games
import android.graphics.Color

class RunInputDialog {
    public var popupDialog: AlertDialog

    constructor(parent: ViewGroup, company: Company, runNumber: Int) {
        val builder = AlertDialog.Builder(parent.context)
        var picker = LayoutInflater.from(parent.context).inflate(
            R.layout.or_run_chooser, parent, false
        ) as ViewGroup
        var companyNameView: TextView = picker.findViewById(R.id.stock_picker_company)
        val game = games[currentGameIndex]

        companyNameView.text = company.name
        companyNameView.setBackgroundColor(Color.parseColor(company.color))
        companyNameView.setTextColor(Color.parseColor(company.textColor))

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
}