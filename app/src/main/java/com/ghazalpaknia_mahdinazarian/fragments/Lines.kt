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
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadLineDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.dialogs.AddLineBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.dialogs.LoginUserBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.DownloadLineItemAdapter
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.TimingListItemsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Lines.newInstance] factory method to
 * create an instance of this fragment.
 */
class Lines : Fragment() {
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
        return inflater.inflate(R.layout.fragment_lines, container, false)
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        model = ViewModelProvider(requireActivity() as MainActivity)[MainActivityViewModel::class.java]
        viewModelScope.launch {
            val db : DownloadManagerDatabase =
                DownloadManagerDatabase.getInstance(context)
            val userDao : DBUserDao = db.dbUserDao()
            withContext(Dispatchers.IO) {
                SignedInUser = userDao.loggedInUser;
            }
            refreshLineViewItem()
        }
        view.findViewById<FloatingActionButton>(R.id.AddNewLineFloatButton)
            .setOnClickListener {
                val lineBottomSheet : BottomSheetDialogFragment = AddLineBottomSheetDialog(null)
                lineBottomSheet.show(requireActivity().supportFragmentManager , lineBottomSheet.tag)
            }
        val downloadLinesObserver = Observer<List<DBDownloadLine>> { newDownloadLine ->
            if(newDownloadLine != null && newDownloadLine.isNotEmpty()) {
                val downloadLinesListRecyclerView = requireView().findViewById<RecyclerView>(R.id.DownloadsLinesListRecycleView)
                downloadLinesListRecyclerView.visibility = View.VISIBLE
                val downloadLinesListAdapter = DownloadLineItemAdapter(newDownloadLine)
                downloadLinesListRecyclerView.adapter = downloadLinesListAdapter
                downloadLinesListRecyclerView.layoutManager = LinearLayoutManager(context)
                requireView().findViewById<TextView>(R.id.NoLineToShowText).visibility = View.GONE
            }else{
                val downloadLinesListRecyclerView = requireView().findViewById<RecyclerView>(R.id.DownloadsLinesListRecycleView)
                downloadLinesListRecyclerView.visibility = View.GONE
                requireView().findViewById<TextView>(R.id.NoLineToShowText).visibility = View.VISIBLE
            }
        }
        val userObserver = Observer<DBUsers> {  newUser ->
            viewModelScope.launch {
                refreshLineViewItem()
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model!!.downloadLines?.observe(viewLifecycleOwner, downloadLinesObserver)
        model!!.singedInUser?.observe(viewLifecycleOwner , userObserver)
    }
    private fun refreshLineViewItem(){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(context)
        val downloadLinesDao : DBDownloadLineDao = db.dbDownloadLineDao()
        var downloadLines : List<DBDownloadLine>?
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(model!!.singedInUser?.value != null){
                    downloadLines = downloadLinesDao.getAll(model!!.singedInUser?.value!!.Id)
                }else{
                    downloadLines = downloadLinesDao.getAll(0)
                }
            }
            model!!.downloadLines?.postValue(downloadLines)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Lines.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Lines().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}