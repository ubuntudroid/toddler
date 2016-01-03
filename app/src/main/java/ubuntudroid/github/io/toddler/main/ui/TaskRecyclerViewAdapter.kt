package ubuntudroid.github.io.toddler.main.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ubuntudroid.github.io.toddler.R
import ubuntudroid.github.io.toddler.provider.jira.models.IssueResponse

/**
 * [RecyclerView.Adapter] that can display a [IssueResponse.Issue] and makes a call to the
 * specified [OnIssueInteractionListener].
 */
class TaskRecyclerViewAdapter(private val listener: OnIssueInteractionListener?, private val values: MutableList<IssueResponse.Issue> = linkedListOf()) : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = values[position]
        holder.idView.text = values[position].key.toString()
        holder.contentView.text = values[position].fields.summary

        holder.view.setOnClickListener {
            listener?.onIssueClick(holder.item)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun getItemId(position: Int): Long {
        return values[position].id.toLong()
    }

    public fun appendIssues(issues: List<IssueResponse.Issue>): Boolean {
        val addSuccessful = values.addAll(issues)
        if (addSuccessful) {
            notifyDataSetChanged()
        }
        return addSuccessful
    }

    /**
     * Clears all data from the attached [RecyclerView]
     */
    public fun clear() {
        values.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView
        val contentView: TextView
        var item: IssueResponse.Issue? = null

        init {
            idView = view.findViewById(R.id.id) as TextView
            contentView = view.findViewById(R.id.content) as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    interface OnIssueInteractionListener {
        fun onIssueClick(item: IssueResponse.Issue?)
    }
}
