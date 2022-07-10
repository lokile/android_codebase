package com.lokile.applibraries.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class OnItemClickEvent<T>(val position: Int, val item: T, val view: View)
abstract class AppBaseAdapter<T, VB : ViewBinding>(var listItem: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<AppBaseRecyclerViewHolder<VB>>() {
    private val itemClickLiveData = MutableLiveData<OnItemClickEvent<T>>()
    abstract fun onCreateViewBinding(inflater: LayoutInflater, viewType: Int): VB
    abstract fun onBindView(
        context: Context,
        holder: AppBaseRecyclerViewHolder<VB>,
        position: Int,
        item: T
    )

    fun registerItemClickListener(): LiveData<OnItemClickEvent<T>> = itemClickLiveData

    protected fun setOnItemClickListener(
        context: Context,
        holder: AppBaseRecyclerViewHolder<VB>,
        position: Int,
        item: T, onItemCLick: () -> Unit
    ) {
        holder.viewBinding.root.setOnClickListener {
            onItemCLick()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppBaseRecyclerViewHolder<VB> {
        return AppBaseRecyclerViewHolder(
            onCreateViewBinding(
                LayoutInflater.from(parent.context),
                viewType
            )
        )
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: AppBaseRecyclerViewHolder<VB>, position: Int) {
        onBindView(holder.itemView.context, holder, position, listItem[position])
        setOnItemClickListener(holder.itemView.context, holder, position, listItem[position]) {
            itemClickLiveData.postValue(
                OnItemClickEvent(
                    position,
                    listItem[position],
                    holder.itemView
                )
            )
        }
    }

    fun updateAll(list: List<T>) {
        listItem = list.toMutableList()
        notifyDataSetChanged()
    }
}

class AppBaseRecyclerViewHolder<V : ViewBinding>(val viewBinding: V) :
    RecyclerView.ViewHolder(viewBinding.root)