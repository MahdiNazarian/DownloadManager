package com.ghazalpaknia_mahdinazarian.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.dialogs.AddTimingBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.dialogs.DownloadLinkBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.DownloadLineItemAdapter
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.DownloadListItemsAdapter
import com.ghazalpaknia_mahdinazarian.static_values.StaticValues
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Downloads.newInstance] factory method to
 * create an instance of this fragment.
 */
class Downloads : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var SignedInUser : DBUsers? = null
    private var model : MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_downloads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity() as MainActivity)[MainActivityViewModel::class.java]
        viewModelScope.launch {
            val db : DownloadManagerDatabase =
                DownloadManagerDatabase.getInstance(context)
            val userDao : DBUserDao = db.dbUserDao()
            withContext(Dispatchers.IO) {
                SignedInUser = userDao.loggedInUser;
            }
            refreshDownloadsViewItem()
        }
        val downloadsObserver = Observer<List<DBDownload>> { newDownloads ->
            if(newDownloads != null && newDownloads.isNotEmpty()) {
                val downloadListRecyclerView = view.findViewById<RecyclerView>(R.id.DownloadsListRecycleView)
                downloadListRecyclerView.visibility = View.VISIBLE
                val downloadLinesListAdapter = DownloadListItemsAdapter(newDownloads)
                downloadListRecyclerView.adapter = downloadLinesListAdapter
                downloadListRecyclerView.layoutManager = LinearLayoutManager(context)
                requireView().findViewById<TextView>(R.id.NoDownloadsToShowText).visibility = View.GONE
            }else{
                val downloadListRecyclerView = view.findViewById<RecyclerView>(R.id.DownloadsListRecycleView)
                downloadListRecyclerView.visibility = View.VISIBLE
                requireView().findViewById<TextView>(R.id.NoDownloadsToShowText).visibility = View.VISIBLE
            }
        }
        val userObserver = Observer<DBUsers> {  newUser ->
            viewModelScope.launch {
                refreshDownloadsViewItem()
            }
        }
        view.findViewById<FloatingActionButton>(R.id.StartDownloadFloatActionButton)
            .setOnClickListener {
                val downloadLinkBottomSheetDialog : BottomSheetDialogFragment = DownloadLinkBottomSheetDialog()
                downloadLinkBottomSheetDialog.show(requireActivity().supportFragmentManager , downloadLinkBottomSheetDialog.tag)
            }
        model!!.downloads?.observe(viewLifecycleOwner, downloadsObserver)
        model!!.singedInUser?.observe(viewLifecycleOwner , userObserver)
    }
    private fun refreshDownloadsViewItem(){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(context)
        val downloadsDao : DBDownloadDao = db.dbDownloadDao()
        var downloads : List<DBDownload>?
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(model!!.singedInUser?.value != null){
                    downloads = downloadsDao.getAll(model!!.singedInUser?.value!!.Id)
                }else{
                    downloads = downloadsDao.getAll(0)
                }
            }
            model!!.downloads?.postValue(downloads)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Downloads.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Downloads().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}