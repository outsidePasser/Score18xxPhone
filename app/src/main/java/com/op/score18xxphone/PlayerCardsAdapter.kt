package com.op.score18xxphone

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

            itemView.findViewById<TextView>(R.id.player_card_other_assets).text = player.otherAssets.toString()

            val totalShares = game.companies.sumOf { it.shares.getOrElse(playerIndex) { 0 } }
            itemView.findViewById<TextView>(R.id.player_card_total_shares).text = totalShares.toString()

            val sharesContainer: LinearLayout = itemView.findViewById(R.id.player_card_shares)
            sharesContainer.removeAllViews()

            val ownedShares = game.companies.mapNotNull { company ->
                val shareCount = company.shares.getOrElse(playerIndex) { 0 }
                if (shareCount > 0) Pair(company, shareCount) else null
            }

            ownedShares.chunked(2).forEach { pair ->
                val rowLayout = LinearLayout(itemView.context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                pair.forEach { (company, shareCount) ->
                    val shareView = LayoutInflater.from(itemView.context).inflate(
                        R.layout.item_player_company_share, rowLayout, false
                    )
                    shareView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    val companyNameView: TextView = shareView.findViewById(R.id.player_share_company_name)
                    companyNameView.text = company.name
                    companyNameView.background.colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            company.colorInt(), BlendModeCompat.SRC_ATOP
                        )
                    companyNameView.setTextColor(company.textColorInt())
                    shareView.findViewById<TextView>(R.id.player_share_count).text = shareCount.toString()
                    rowLayout.addView(shareView)
                }
                if (pair.size == 1) {
                    rowLayout.addView(View(itemView.context).apply {
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    })
                }
                sharesContainer.addView(rowLayout)
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
