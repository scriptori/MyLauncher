package me.scriptori.mylauncher.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.scriptori.mylauncher.databinding.RecyclerviewItemBinding
import me.scriptori.mylauncher.model.ApplicationModel

/**
 * The recyclerview adapter to hold the collection of the application information to be
 * displayed to the user for easy launching
 */
class ApplicationViewAdapter(
    var applicationList: MutableList<ApplicationModel> = mutableListOf()
) : RecyclerView.Adapter<ApplicationViewAdapter.ApplicationsViewHolder>() {

    /**
     * See **[RecyclerView.Adapter.onCreateViewHolder]**
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationsViewHolder {
        val binding = RecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ApplicationsViewHolder(binding)
    }

    /**
     * See [RecyclerView.Adapter.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: ApplicationsViewHolder, position: Int) {
        val model = applicationList[position]
        holder.binding.apply {
            val listener = View.OnClickListener {
                val intent = root.context.packageManager.getLaunchIntentForPackage(
                    applicationList[position].packageName
                )
                root.context.startActivity(intent)
            }

            /*
             * I made de decision to use a ImageView and a Te3xtView instead of a compound
             * drawable for layout purposes
             */

            applicationIcon.let {
                it.setImageDrawable(model.icon)
                it.setOnClickListener(listener)
            }
            applicationLabel.let {
                it.text = model.label
                it.setOnClickListener(listener)
            }
        }
    }

    /**
     * See [RecyclerView.Adapter.getItemCount]
     */
    override fun getItemCount(): Int {
        return applicationList.size
    }

    /**
     * This view holder makes usage of an utility Kotlin extension to facilitate and make more
     * readable the creating of the a view holder using view binding
     */
    inner class ApplicationsViewHolder(binding: RecyclerviewItemBinding) :
        BindingViewHolder<RecyclerviewItemBinding>(binding)
}