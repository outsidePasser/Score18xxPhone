package com.op.score18xxphone

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games

enum class CompanyViewType {
    LABELS, COMPANY
}

class CompaniesAdapter : RecyclerView.Adapter<ViewHolder>() {

    class CompanyViewHolder(itemView: View) : ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(index: Int) {
            val company = games[currentGameIndex].companies[index - 1]

            val companyNameView: TextView = itemView.findViewById(R.id.item_company_name)
            companyNameView.text = company.name
            companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(company.color), BlendModeCompat.SRC_ATOP)
            companyNameView.setTextColor(Color.parseColor(company.textColor))
            companyNameView.setOnClickListener { SharesDialog(itemView.context, company).show() }

            val stockPriceView: TextView = itemView.findViewById(R.id.item_company_stock_price)
            stockPriceView.text = company.stockPrice.toString()
            stockPriceView.setOnClickListener { StockPriceDialog(itemView.context, company).show() }

            val runViews = listOf<TextView>(
                itemView.findViewById(R.id.item_company_or1),
                itemView.findViewById(R.id.item_company_or2),
                itemView.findViewById(R.id.item_company_or3)
            )
            runViews.forEachIndexed { i, view ->
                view.text = company.runs[i].toString()
                val isPrePopulated = !company.runsExplicitlySet[i] && company.runs[i] != 0
                view.setTypeface(null, if (isPrePopulated) Typeface.ITALIC else Typeface.NORMAL)
                view.setOnClickListener { RunInputDialog(itemView.context, company, i).show() }
            }
        }
    }

    class LabelViewHolder(itemView: View) : ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) CompanyViewType.LABELS.ordinal else CompanyViewType.COMPANY.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == CompanyViewType.LABELS.ordinal) {
            LabelViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_company_header, parent, false)
            )
        } else {
            CompanyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_company, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != 0) {
            (holder as CompanyViewHolder).bind(position)
        }
    }

    override fun getItemCount() = games[currentGameIndex].companies.size + 1
}
