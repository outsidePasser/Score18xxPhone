package com.op.score18xxphone

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games

enum class CompanyViewType {
    LABELS, COMPANY
}

class CompaniesAdapter : RecyclerView.Adapter<ViewHolder>() {

    class CompanyViewHolder(
        itemView: View, parent: ViewGroup
    ) : ViewHolder(itemView) {

        private var parentViewGroup = parent

        @SuppressLint("SetTextI18n")
        fun bind(index: Int) {
            var company = games[currentGameIndex].companies[index - 1]

           // var companyNameView: TextView = itemView.findViewById(R.id.item_company_name)
            //companyNameView.text = company.name
            //companyNameView.setBackgroundColor(Color.parseColor(company.color))
            //companyNameView.setTextColor(Color.parseColor(company.textColor))

            var stockPriceView: TextView = itemView.findViewById(R.id.item_company_stock_price)
            stockPriceView.text = company.stockPrice.toString()
            stockPriceView.setOnClickListener {
                launchStockPricePopup(
                    parentViewGroup, company
                )
            }
            var or1RunView: TextView = itemView.findViewById(R.id.item_company_or1)
            or1RunView.text = company.runs[0].toString()
            or1RunView.setOnClickListener { launchRunPopup(parentViewGroup, company, 0) }
        }

        fun launchStockPricePopup(parent: ViewGroup, company: Company) {
            StockPriceDialog(parent, company).show()
        }

        fun launchRunPopup(parent: ViewGroup, company: Company, runNumber: Int) {
            RunInputDialog(parent, company, runNumber).show()
        }
    }

    class LabelViewHolder(
        itemView: View
    ) : ViewHolder(itemView) {

        fun bind(index: Int) {
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return CompanyViewType.LABELS.ordinal
        } else {
            return CompanyViewType.COMPANY.ordinal
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        if (viewType == CompanyViewType.LABELS.ordinal) {
            return LabelViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_company_header, parent, false
                )
            )
        } else {
            return CompanyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_company, parent, false
                ), parent
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            (holder as LabelViewHolder).bind(position)
        } else {
            (holder as CompanyViewHolder).bind(position)
        }
    }

    override fun getItemCount() = games[currentGameIndex].companies.size + 1
}