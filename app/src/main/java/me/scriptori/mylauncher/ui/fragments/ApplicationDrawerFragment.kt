package me.scriptori.mylauncher.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import me.scriptori.mylauncher.R
import me.scriptori.mylauncher.databinding.FragmentAppDrawerBinding
import me.scriptori.mylauncher.model.ApplicationModel
import me.scriptori.mylauncher.model.DenyListViewModel
import me.scriptori.mylauncher.retrofit.DenyListRequest
import me.scriptori.mylauncher.retrofit.DenyListResponse
import me.scriptori.mylauncher.ui.recyclerview.ApplicationViewAdapter
import me.scriptori.mylauncher.util.Constants
import me.scriptori.mylauncher.util.DenyList
import me.scriptori.mylauncher.util.DenyList.currentDeniedList


class ApplicationDrawerFragment : Fragment() {
    companion object {
        internal var TAG = ApplicationDrawerFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAppDrawerBinding

    private var adapter = ApplicationViewAdapter()

    internal val denyListViewModel: DenyListViewModel by lazy {
        DenyListViewModel().also {
            context?.let { ctx ->
                if (DenyList.getDenyListFile(ctx).exists()) {
                    it.denyListResponse.value = DenyListResponse(
                        DenyList.readFromFile(ctx).denylist
                    )
                } else {
                    DenyListRequest(it).getDenyList()
                }
            } ?: run {
                DenyListRequest(it).getDenyList()
            }
        }
    }

    private val appModel: MutableList<ApplicationModel>
        get() {
            val appModelList = mutableListOf<ApplicationModel>()
            activity?.packageManager?.let { pm ->
                val intent = Intent(Intent.ACTION_MAIN, null).also {
                    it.addCategory(Intent.CATEGORY_LAUNCHER)
                }
                pm.queryIntentActivities(intent, 0).forEach {
                    val packageName = it.activityInfo.packageName
                    if ( // Hiding denied application
                        !currentDeniedList.contains(packageName) &&
                        // Hiding system application
                        pm.getLaunchIntentForPackage(packageName) != null &&
                        // Hiding launcher application
                        packageName != Constants.APP_PACKAGE_NAME
                    ) {
                        // adding application information to the available app list
                        appModelList.add(
                            ApplicationModel(
                                it.loadLabel(pm).toString(),
                                it.activityInfo.packageName,
                                it.activityInfo.loadIcon(pm)
                            )
                        )
                    }
                }
            }
            // Sorting the list alphabetically
            appModelList.sortBy { it.label }
            return appModelList
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        denyListViewModel.denyListResponse.observeForever {
            // Save deny list to a json file if the content doesn't match
            if (!currentDeniedList.containsAll(it.denylist)) {
                currentDeniedList = it.denylist
                context?.let { ctx ->
                    DenyList.writeToFile(ctx, it)
                }
            }
            // Update the adapter item including the deny list
            adapter.apply {
                applicationList.clear()
                applicationList.addAll(appModel)
                notifyDataSetChanged()
            }
        }
        binding.appsRecyclerView.also { rv ->
            rv.layoutManager = GridLayoutManager(
                context,
                context?.resources?.getInteger(R.integer.number_columns) ?: 3
            )
            rv.adapter = adapter
        }
    }
}
