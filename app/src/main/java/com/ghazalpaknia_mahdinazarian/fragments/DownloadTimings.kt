package com.ghazalpaknia_mahdinazarian.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.dialogs.AddTimingBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
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
 * Use the [DownloadTimings.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadTimings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var SignedInUser : DBUsers? = null
    private var model : MainActivityViewModel? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download_timings , container , false)
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
            refreshTimingRecycleViewItem()
        }
        view.findViewById<FloatingActionButton>(R.id.AddNewTimingFloatButton)
            .setOnClickListener {
                val addTimingBottomSheet : BottomSheetDialogFragment = AddTimingBottomSheetDialog()
                addTimingBottomSheet.show(requireActivity().supportFragmentManager , addTimingBottomSheet.tag)
            }
        val timingsObserver = Observer<List<DBTimings>> { newDBTimings ->
            if(newDBTimings != null && newDBTimings.isNotEmpty()) {
                val downloadTimingsListRecyclerView = requireView().findViewById<RecyclerView>(R.id.DownloadsTimingsListRecycleView)
                downloadTimingsListRecyclerView.visibility = View.VISIBLE
                val downloadTimingsListAdapter = TimingListItemsAdapter(newDBTimings)
                downloadTimingsListRecyclerView.adapter = downloadTimingsListAdapter
                downloadTimingsListRecyclerView.layoutManager = LinearLayoutManager(context)
                requireView().findViewById<TextView>(R.id.NoTimingToShowText).visibility = View.GONE
            }else{
                val downloadTimingsListRecyclerView = requireView().findViewById<RecyclerView>(R.id.DownloadsTimingsListRecycleView)
                downloadTimingsListRecyclerView.visibility = View.GONE
                requireView().findViewById<TextView>(R.id.NoTimingToShowText).visibility = View.VISIBLE
            }
        }
        val userObserver = Observer<DBUsers> {  newUser ->
            viewModelScope.launch {
                refreshTimingRecycleViewItem()
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model!!.timings?.observe(viewLifecycleOwner, timingsObserver)
        model!!.singedInUser?.observe(viewLifecycleOwner , userObserver)
    }
    private fun refreshTimingRecycleViewItem(){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(context)
        val timingDao : DBTimingsDao = db.dbTimingsDao()
        var timings : List<DBTimings>?
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(model!!.singedInUser?.value != null){
                    timings = timingDao.getAll(model!!.singedInUser?.value!!.Id)
                }else{
                    timings = timingDao.getAll(0)
                }
            }
            model!!.timings?.postValue(timings)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DownloadTimings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1 : String , param2 : String) =
            DownloadTimings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1 , param1)
                    putString(ARG_PARAM2 , param2)
                }
            }
    }
}