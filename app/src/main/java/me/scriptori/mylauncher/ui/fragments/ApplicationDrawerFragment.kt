package me.scriptori.mylauncher.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import me.scriptori.mylauncher.R
import me.scriptori.mylauncher.databinding.FragmentAppDrawerBinding
import me.scriptori.mylauncher.model.DenyListViewModel
import me.scriptori.mylauncher.retrofit.DenyListRequest
import me.scriptori.mylauncher.retrofit.DenyListResponse
import me.scriptori.mylauncher.ui.recyclerview.ApplicationViewAdapter
import me.scriptori.mylauncher.util.AllowedPackagerHandler
import me.scriptori.mylauncher.util.DenyPackageHandler
import me.scriptori.mylauncher.util.DenyPackageHandler.currentDeniedList

/**
 * This is the fragment for the application drawer.
 */
class ApplicationDrawerFragment : Fragment() {
    companion object {
        internal var TAG = ApplicationDrawerFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAppDrawerBinding

    private var adapter = ApplicationViewAdapter()

    private val denyListViewModel: DenyListViewModel by lazy {
        DenyListViewModel()
    }

    /**
     * See [Fragment.onCreateView]
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    /**
     * See [Fragment.onCreate]
     * @see [AllowedPackagerHandler.getAllowedApps]
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = view.context
        // start observing the deny list package model
        denyListViewModel.denyListResponse.observeForever {
            // Save deny list to a json file if the content doesn't match
            if (!currentDeniedList.containsAll(it.denylist)) {
                currentDeniedList = it.denylist
                DenyPackageHandler.writeToFile(ctx, it)
            }
            // Update the adapter item including the deny list
            adapter.apply {
                applicationList.clear()
                applicationList.addAll(AllowedPackagerHandler.getAllowedApps(ctx))
                notifyDataSetChanged()
            }
        }
        // populate the deny list model
        populateDenyListModel(ctx)
        // populate the recyclerview
        binding.appsRecyclerView.also { rv ->
            rv.layoutManager = GridLayoutManager(
                ctx,
                ctx.resources.getInteger(R.integer.number_columns)
            )
            rv.adapter = adapter
        }
    }

    /**
     * Populate the deny list model
     *
     * @param context - Application context
     */
    private fun populateDenyListModel(context: Context) {
        if (DenyPackageHandler.getDenyListFile(context).exists()) {
            // Use the deny package json file from the device
            denyListViewModel.denyListResponse.value = DenyListResponse(
                DenyPackageHandler.readFromFile(context).denylist
            )
        } else {
            // The deny package json file doesn't exist in the device.
            // Make a request to retrieve form the server URL
            DenyListRequest(denyListViewModel).getDenyList()
        }
    }
}
