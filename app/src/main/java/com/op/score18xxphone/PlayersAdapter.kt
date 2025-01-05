package com.op.score18xxphone

import com.op.score18xxphone.Players.players
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


enum class PlayerViewType {
    PLAYER, ADD_BUTTON
}

class PlayersAdapter : RecyclerView.Adapter<ViewHolder>() {

    class PlayerViewHolder(
        itemView: View
    ) : ViewHolder(itemView) {

        private val playerView: TextView = itemView.findViewById(R.id.item_player_name)
        private var playerIndex: Int = 0

        fun bind(index: Int) {
            playerIndex = index
            playerView.text = players[index].name
            itemView.setOnClickListener { removePlayer(itemView) }
        }

        private fun removePlayer(itemView: View) {
            var alert = AlertDialog.Builder(itemView.context);

            alert.setTitle("Remove " + players[playerIndex].name + "?");

            alert.setPositiveButton("OK") { dialog, which ->
                Players.removePlayerByIndex(playerIndex)
            }

            alert.setNegativeButton("Cancel") { dialog, which ->
            }

            alert.show();
        }
    }

    class AddButtonViewHolder(
        itemView: View
    ) : ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener { addPlayer(itemView) }
        }

        private fun addPlayer(itemView: View) {
            var alert = AlertDialog.Builder(itemView.context);

            alert.setTitle("Add New Player");

            var input = EditText(itemView.context);
            alert.setView(input);

            alert.setPositiveButton("OK") { dialog, which ->
                Log.i("MHA - add", input.text.toString())
                Players.addPlayerByName(input.text.toString())
            }

            alert.setNegativeButton("Cancel") { dialog, which ->
            }

            alert.show();
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < players.size) {
            return PlayerViewType.PLAYER.ordinal
        } else {
            return PlayerViewType.ADD_BUTTON.ordinal
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        if (viewType == PlayerViewType.PLAYER.ordinal) {
            return PlayerViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_player, parent, false
                )
            )
        } else {
            return AddButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_add_player, parent, false
                )
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

    override fun getItemCount() = players.size + 1
}