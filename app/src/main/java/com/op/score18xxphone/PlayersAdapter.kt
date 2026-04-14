package com.op.score18xxphone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.op.score18xxphone.Players.players

enum class PlayerViewType {
    PLAYER, ADD_BUTTON
}

private const val MAX_PLAYERS = 8

class PlayersAdapter : RecyclerView.Adapter<ViewHolder>() {

    class PlayerViewHolder(itemView: View) : ViewHolder(itemView) {

        private val playerView: TextView = itemView.findViewById(R.id.item_player_name)
        private var playerIndex: Int = 0

        fun bind(index: Int) {
            playerIndex = index
            playerView.text = players[index].name
            itemView.setOnClickListener { removePlayer() }
        }

        private fun removePlayer() {
            val alert = AlertDialog.Builder(itemView.context)
            alert.setTitle(itemView.context.getString(R.string.remove_player_prompt, players[playerIndex].name))
            alert.setPositiveButton(R.string.confirm) { _, _ -> Players.removePlayerByIndex(playerIndex) }
            alert.setNegativeButton(R.string.cancel, null)
            alert.show()
        }
    }

    class AddButtonViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener { addPlayer() }
        }

        private fun addPlayer() {
            val alert = AlertDialog.Builder(itemView.context)
            alert.setTitle(R.string.add_new_player)

            val input = EditText(itemView.context).apply {
                inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                hint = context.getString(R.string.player)
            }
            val container = FrameLayout(itemView.context).apply {
                val padding = (16 * itemView.resources.displayMetrics.density).toInt()
                setPadding(padding, 0, padding, 0)
            }
            container.addView(input)
            alert.setView(container)

            alert.setPositiveButton(R.string.confirm) { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) Players.addPlayerByName(name)
            }
            alert.setNegativeButton(R.string.cancel, null)

            val dialog = alert.show()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            input.requestFocus()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < players.size) PlayerViewType.PLAYER.ordinal else PlayerViewType.ADD_BUTTON.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == PlayerViewType.PLAYER.ordinal) {
            PlayerViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
            )
        } else {
            AddButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_player, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < players.size) {
            (holder as PlayerViewHolder).bind(position)
        } else {
            (holder as AddButtonViewHolder).bind()
        }
    }

    override fun getItemCount() = if (players.size < MAX_PLAYERS) players.size + 1 else players.size
}
