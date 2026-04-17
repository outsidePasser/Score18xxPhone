package com.op.score18XXphone

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.op.score18XXphone.Games.currentGameIndex
import com.op.score18XXphone.Games.games

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
        companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(company.colorInt(), BlendModeCompat.SRC_ATOP)
        companyNameView.setTextColor(company.textColorInt())

        for (i in 0..game.stockPrices.size step 5) {
            val row = LayoutInflater.from(context).inflate(
                R.layout.stock_price_picker_row, picker, false
            ) as ViewGroup
            picker.addView(row)
            for (col in 0 until row.childCount) {
                val priceView = row.getChildAt(col) as? TextView ?: continue
                val priceIndex = i + col
                when {
                    priceIndex == 0 -> {
                        priceView.text = "0"
                        priceView.setOnClickListener { choosePrice(company, "0") }
                    }
                    priceIndex <= game.stockPrices.size -> {
                        val value = game.stockPrices[priceIndex - 1].toString()
                        priceView.text = value
                        priceView.setOnClickListener { choosePrice(company, value) }
                    }
                    else -> {
                        priceView.text = ""
                        priceView.setBackgroundColor(Color.BLACK)
                    }
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
