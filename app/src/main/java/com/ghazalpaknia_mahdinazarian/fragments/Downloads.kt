package com.ghazalpaknia_mahdinazarian.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.DownloadListItemsAdapter
import com.ghazalpaknia_mahdinazarian.static_values.StaticValues

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
        var downloadItems : List<DownloadsItemModel> = listOf<DownloadsItemModel>(
            DownloadsItemModel(
                "موزیک پلیر",
                StaticValues.DownloadStates.DOWNLOADING,
                50,
                80000000000,
                1,
                5,
            ),
            DownloadsItemModel(
                "موزیک پلیر",
                StaticValues.DownloadStates.DOWNLOADING,
                50,
                10,
                10,
                5,
            ),
        );
        val downloadListRecyclerView = view.findViewById<RecyclerView>(R.id.DonwloadsTimingsListRecycleView)
        val downloadListAdapter = DownloadListItemsAdapter(downloadItems)
        downloadListRecyclerView.adapter = downloadListAdapter
        downloadListRecyclerView.layoutManager = LinearLayoutManager(context)
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