package com.op.score18xxphone

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games
import android.graphics.Color
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

class StockPriceDialog {
    private var popupDialog: AlertDialog

    constructor(parent: ViewGroup, company: Company) {
        val builder = AlertDialog.Builder(parent.context)
        var picker = LayoutInflater.from(parent.context).inflate(
            R.layout.stock_price_picker, parent, false
        ) as ViewGroup
        var companyNameView: TextView = picker.findViewById(R.id.stock_picker_company)
        val game = games[currentGameIndex]

        companyNameView.text = company.name
        companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(company.color), BlendModeCompat.SRC_ATOP)
        companyNameView.setTextColor(Color.parseColor(company.textColor))

        for (i in 0..(game.stockPrices.size + 1) step 5) {
            var row = LayoutInflater.from(parent.context).inflate(
                R.layout.stock_price_picker_row, parent, false
            ) as ViewGroup
            picker.addView(row)
            for (j in i..(i + 4)) {
                var price: TextView = row.getChildAt(j - i) as TextView
                if (j == 0) {
                    price.text = "0"
                    price.setOnClickListener {
                        choosePrice(
                            company,
                            price.text.toString()
                        )
                    }
                } else if (j <= game.stockPrices.size) {
                    price.text = game.stockPrices[j - 1].toString()
                    price.setOnClickListener {
                        choosePrice(
                            company,
                            price.text.toString()
                        )
                    }
                } else {
                    price.text = ""
                    price.setBackgroundColor(Color.parseColor("#000000"))
                }
            }
        }

        builder.setView(picker)
        popupDialog = builder.create()
    }

    fun show() {
        popupDialog.show()
    }

    fun choosePrice(company: Company, price: String) {
        company.stockPrice = price.toInt()
        Games.changeHappened()
        popupDialog.dismiss()
    }

}