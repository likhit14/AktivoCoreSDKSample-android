package com.test.aktivocoresdksample_android.utils.actionadapter.actionviewholder

import androidx.recyclerview.widget.RecyclerView
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ItemButtonBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener

class ActionViewHolder(private val itemBinding: ItemButtonBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun setData(action: Action, actionListener: ActionListener) {
        itemBinding.actionBtn.text = action.actionName
        itemBinding.actionBtn.setOnClickListener {
            actionListener.onActionClick(action)
        }
    }

}