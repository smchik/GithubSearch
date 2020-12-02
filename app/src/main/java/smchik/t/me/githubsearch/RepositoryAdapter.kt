package smchik.t.me.githubsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoryAdapter : RecyclerView.Adapter<RepositoryViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val repositories = mutableListOf<Repository>()

    fun setItems(list: List<Repository>) {
        repositories.clear()
        repositories.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RepositoryViewHolder(inflater.inflate(R.layout.item_repository, parent, false))
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) =
        holder.onBind(repositories[position])

    override fun getItemCount(): Int = repositories.size

    override fun getItemId(position: Int): Long = repositories[position].id
}

class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    fun onBind(repository: Repository) {
        (itemView as TextView).text = repository.name
    }
}