package com.op.score18XXphone

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.op.score18XXphone.Games.currentGameIndex
import com.op.score18XXphone.Games.games

class SummaryAdapter : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    data class PlayerRow(val rank: Int, val name: String, val totalValue: Int)

    private var rows: List<PlayerRow> = buildRows()

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        rows = buildRows()
        notifyDataSetChanged()
    }

    private fun buildRows(): List<PlayerRow> {
        val game = games[currentGameIndex]
        return Players.players
            .mapIndexed { playerIndex, player ->
                val totalValue = player.cash + game.companies.sumOf { company ->
                    val shareCount = company.shares.getOrElse(playerIndex) { 0 }
                    val shareValue = company.stockPrice + company.runs.take(game.operatingRounds).sum().toDouble()
                    (shareCount * shareValue).toInt()
                }
                Pair(player.name, totalValue)
            }
            .sortedByDescending { it.second }
            .mapIndexed { index, (name, totalValue) ->
                PlayerRow(index + 1, name, totalValue)
            }
    }

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(row: PlayerRow) {
            itemView.findViewById<TextView>(R.id.summary_rank).text = row.rank.toString()
            itemView.findViewById<TextView>(R.id.summary_player_name).text = row.name
            itemView.findViewById<TextView>(R.id.summary_total_value).text = row.totalValue.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SummaryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_summary_player, parent, false)
    )

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) = holder.bind(rows[position])

    override fun getItemCount() = rows.size
}
