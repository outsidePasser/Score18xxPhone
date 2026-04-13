package com.op.score18xxphone

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games

class StockPriceDialog(context: Context, company: Company) {
    private val popupDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        val picker = LayoutInflater.from(context).inflate(
            R.layout.stock_price_picker, null, false
        ) as ViewGroup
        val companyNameView: TextView = picker.findViewById(R.id.stock_picker_company)
        val game = games[currentGameIndex]

        companyNameView.text = company.name
        companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(company.color), BlendModeCompat.SRC_ATOP)
        companyNameView.setTextColor(Color.parseColor(company.textColor))

        for (i in 0..(game.stockPrices.size + 1) step 5) {
            val row = LayoutInflater.from(context).inflate(
                R.layout.stock_price_picker_row, picker, false
            ) as ViewGroup
            picker.addView(row)
            for (j in i..(i + 4)) {
                val price: TextView = row.getChildAt(j - i) as TextView
                if (j == 0) {
                    price.text = "0"
                    price.setOnClickListener { choosePrice(company, price.text.toString()) }
                } else if (j <= game.stockPrices.size) {
                    price.text = game.stockPrices[j - 1].toString()
                    price.setOnClickListener { choosePrice(company, price.text.toString()) }
                } else {
                    price.text = ""
                    price.setBackgroundColor(Color.BLACK)
                }
            }
        }

        builder.setView(picker)
        popupDialog = builder.create()
    }

    fun show() {
        popupDialog.show()
    }

    private fun choosePrice(company: Company, price: String) {
        company.stockPrice = price.toInt()
        Games.changeHappened()
        popupDialog.dismiss()
    }
}
