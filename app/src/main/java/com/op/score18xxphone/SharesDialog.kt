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

class SharesDialog(context: Context, company: Company) {
    private val popupDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_shares, null, false
        ) as ViewGroup

        val companyNameView: TextView = view.findViewById(R.id.shares_company_name)
        companyNameView.text = company.name
        companyNameView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            company.colorInt(), BlendModeCompat.SRC_ATOP
        )
        companyNameView.setTextColor(company.textColorInt())

        val game = games[currentGameIndex]
        val minShares = if (game.allowNegativeShares) -game.maxSharesPerPlayer else 0

        val pendingShares = Players.players.indices.map { i ->
            company.shares.getOrElse(i) { 0 }
        }.toMutableList()

        val playerList: LinearLayout = view.findViewById(R.id.shares_player_list)
        Players.players.forEachIndexed { i, player ->
            val row = LayoutInflater.from(context).inflate(
                R.layout.item_share_player, playerList, false
            )
            row.findViewById<TextView>(R.id.share_player_name).text = player.name
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
            playerList.addView(row)
        }

        builder.setView(view)
        builder.setPositiveButton(R.string.confirm) { _, _ ->
            Players.players.indices.forEach { i ->
                while (company.shares.size <= i) company.shares.add(0)
                company.shares[i] = pendingShares[i]
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
