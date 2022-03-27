package com.test.aktivocoresdksample_android.utils.actionadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ItemButtonBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.actionviewholder.ActionViewHolder

class ActionAdapter(
    private val actionList: MutableList<Action>,
    private val actionListener: ActionListener
) :
    RecyclerView.Adapter<ActionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        return ActionViewHolder(ItemButtonBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = actionList[position]
        holder.setData(action, actionListener)
    }

    override fun getItemCount(): Int {
        return actionList.size
    }
}