package com.op.score18XXphone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.op.score18XXphone.Games.currentGameIndex
import com.op.score18XXphone.Games.games

class PlayerSharesDialog(context: Context, private val playerIndex: Int) {
    private val popupDialog: AlertDialog

    init {
        val player = Players.players[playerIndex]
        val game = games[currentGameIndex]

        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_player_shares, null, false
        ) as ViewGroup

        view.findViewById<TextView>(R.id.player_shares_name).text = player.name

        var pendingCash = player.cash
        val cashView: TextView = view.findViewById(R.id.player_shares_cash)
        cashView.text = pendingCash.toString()
        cashView.setOnClickListener {
            CashInputDialog(context, player) { newCash ->
                pendingCash = newCash
                cashView.text = pendingCash.toString()
            }.show()
        }

        var pendingOtherAssets = player.otherAssets
        val otherAssetsView: TextView = view.findViewById(R.id.player_shares_other_assets)
        otherAssetsView.text = pendingOtherAssets.toString()
        otherAssetsView.setOnClickListener {
            CashInputDialog(context, player.name, "Other Assets", pendingOtherAssets) { newValue ->
                pendingOtherAssets = newValue
                otherAssetsView.text = pendingOtherAssets.toString()
            }.show()
        }

        val minShares = if (game.allowNegativeShares) -game.maxSharesPerPlayer else 0

        val pendingShares = game.companies.map { company ->
            company.shares.getOrElse(playerIndex) { 0 }
        }.toMutableList()

        val companyList: LinearLayout = view.findViewById(R.id.player_company_list)
        game.companies.forEachIndexed { i, company ->
            val row = LayoutInflater.from(context).inflate(
                R.layout.item_share_player, companyList, false
            )

            val nameView: TextView = row.findViewById(R.id.share_player_name)
            nameView.text = company.name
            nameView.setBackgroundResource(R.drawable.rounded_corner)
            nameView.background.colorFilter = BlendModeColorFilterCompat
                .createBlendModeColorFilterCompat(
                    company.colorInt(), BlendModeCompat.SRC_ATOP
                )
            nameView.setTextColor(company.textColorInt())

            val countView: TextView = row.findViewById(R.id.share_count)
            countView.text = pendingShares[i].toString()

            row.findViewById<TextView>(R.id.share_minus).setOnClickListener {
                if (pendingShares[i] > minShares) {
                    pendingShares[i]--
                    countView.text = pendingShares[i].toString()
                }
            }
            row.findViewById<TextView>(R.id.share_plus).setOnClickListener {
                if (pendingShares[i] < (company.maxShares ?: game.maxSharesPerPlayer)) {
                    pendingShares[i]++
                    countView.text = pendingShares[i].toString()
                }
            }

            companyList.addView(row)
        }

        builder.setView(view)
        builder.setPositiveButton(R.string.confirm) { _, _ ->
            player.cash = pendingCash
            player.otherAssets = pendingOtherAssets
            game.companies.forEachIndexed { i, company ->
                while (company.shares.size <= playerIndex) company.shares.add(0)
                company.shares[playerIndex] = pendingShares[i]
            }
            Games.changeHappened()
        }
        builder.setNegativeButton(R.string.cancel, null)
        popupDialog = builder.create()
    }

    fun show() {
        popupDialog.show()
    }
}
