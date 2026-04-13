package com.op.score18xxphone

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.RecyclerView
import com.op.score18xxphone.Games.currentGameIndex
import com.op.score18xxphone.Games.games

class PlayerCardsAdapter : RecyclerView.Adapter<PlayerCardsAdapter.PlayerCardViewHolder>() {

    class PlayerCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(playerIndex: Int) {
            val player = Players.players[playerIndex]
            val game = games[currentGameIndex]

            val nameView: TextView = itemView.findViewById(R.id.player_card_name)
            nameView.text = player.name

            itemView.setOnClickListener {
                PlayerSharesDialog(itemView.context, playerIndex).show()
            }

            itemView.findViewById<TextView>(R.id.player_card_cash).text = player.cash.toString()

            val totalShares = game.companies.sumOf { it.shares.getOrElse(playerIndex) { 0 } }
            itemView.findViewById<TextView>(R.id.player_card_total_shares).text = totalShares.toString()

            val totalValue = player.cash + game.companies.sumOf { company ->
                val shareCount = company.shares.getOrElse(playerIndex) { 0 }
                val shareValue = company.stockPrice + company.runs.sum() / 10.0
                (shareCount * shareValue).toInt()
            }
            itemView.findViewById<TextView>(R.id.player_card_total_value).text = totalValue.toString()

            val sharesContainer: LinearLayout = itemView.findViewById(R.id.player_card_shares)
            sharesContainer.removeAllViews()

            game.companies.forEachIndexed { companyIndex, company ->
                val shareCount = company.shares.getOrElse(playerIndex) { 0 }
                if (shareCount > 0) {
                    val row = LayoutInflater.from(itemView.context).inflate(
                        R.layout.item_player_company_share, sharesContainer, false
                    )
                    val companyNameView: TextView = row.findViewById(R.id.player_share_company_name)
                    companyNameView.text = company.name
                    companyNameView.background.colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            Color.parseColor(company.color), BlendModeCompat.SRC_ATOP
                        )
                    companyNameView.setTextColor(Color.parseColor(company.textColor))

                    val countView: TextView = row.findViewById(R.id.player_share_count)
                    countView.text = shareCount.toString()

                    sharesContainer.addView(row)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerCardViewHolder {
        return PlayerCardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlayerCardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = Players.players.size
}
