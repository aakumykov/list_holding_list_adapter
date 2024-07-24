package com.github.aakumykov.list_holding_list_adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes

/**
 * Адаптер списка, который сам управляет списком отображаемых элементов.
 */
abstract class ListHoldingListAdapter<T, V: ListHoldingListAdapter.ViewHolder<T>>(
    @LayoutRes private val itemLayoutResourceId: Int,
)
    : BaseAdapter()
{
    private val list: MutableList<T> = mutableListOf()
    private var selectedItem: T? = null

    fun setSelectedItem(item: T) {
        Log.d(TAG, "setSelectedItem(${item})")
        selectedItem = item
    }

    abstract fun createViewHolder(): ViewHolder<T>


    fun setList(newList: List<T>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        list.add(item)
        notifyDataSetChanged()
    }

    fun removeItem(item: T) {
        list.remove(item)
        notifyDataSetChanged()
    }

    fun removeItemAt(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }


    override fun getCount(): Int {
        return list.size
    }


    override fun getItem(position: Int): T {
        return list[position]
    }


    override fun getItemId(position: Int): Long {
        return list[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getViewFromResource(itemLayoutResourceId, position, convertView, parent) { viewHolder, item ->
            viewHolder.fill(item, item == selectedItem)
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getViewFromResource(itemLayoutResourceId, position, convertView, parent) { viewHolder, item ->
            viewHolder.fillAsDropDown(item, item == selectedItem)
        }
    }

    protected fun getViewFromResource(
        layoutResId: Int,
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
        fillViewHolder: (viewHolder: ViewHolder<T>, item: T) -> Unit
    ): View {

        val viewHolder: ViewHolder<T>

        val itemView =
            if (convertView == null) {
                val itemView = LayoutInflater.from(parent!!.context).inflate(layoutResId, parent, false)
                viewHolder = createViewHolder().apply { init(itemView) }
                itemView.tag = viewHolder
                itemView
            }
            else {
                viewHolder = convertView.tag as ViewHolder<T>
                convertView
            }

        fillViewHolder(viewHolder, list[position])

        return itemView
    }


    companion object {
        val TAG = ListHoldingListAdapter::class.java.simpleName
    }

    abstract class ViewHolder<ItemType> {
        abstract fun init(itemView: View)
        abstract fun fill(item: ItemType, isSelected: Boolean = false)
        abstract fun fillAsDropDown(item: ItemType, isSelected: Boolean = false)
    }
}