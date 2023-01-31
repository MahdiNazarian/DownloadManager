package com.ghazalpaknia_mahdinazarian.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadLineDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.TimingListItemsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.util.Calendar

class AddLineBottomSheetDialog(private var editLineItem:DBDownloadLine? ) : BottomSheetDialogFragment() , AdapterView.OnItemSelectedListener{
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var selectedDownloadLineId : Int = 0
    var SignedInUser : DBUsers? = null
    private var model : MainActivityViewModel? = null
    private var addLinesTimingValue : DBTimings? = null
    private var arrayAdapter : ArrayAdapter<String>? = null
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? = inflater.inflate(R.layout.add_line_bottom_sheet , container , false)

    companion object {
        const val TAG = "AddTimingBottomSheet"
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        model = ViewModelProvider(requireActivity() as MainActivity)[MainActivityViewModel::class.java]
        SignedInUser = model!!.singedInUser?.value
        val editLineIdInput : EditText = view.findViewById(R.id.EditLineIdInput)
        val addLineNameInput : EditText = view.findViewById(R.id.AddLineNameInput)
        val addLinesTimingInput : Spinner = view.findViewById(R.id.AddLinesTimingInput)
        val addLineButton : MaterialButton = view.findViewById(R.id.AddLineButton)
        val editLineButton : MaterialButton = view.findViewById(R.id.EditLineButton)
        var editLineIdNull : Boolean = false
        var addLineNameNull : Boolean = false
        addLinesTimingValue= if(model!!.timings?.value != null && !(model!!.timings?.value?.isEmpty())!!) model!!.timings?.value?.first() else null
        setAddLinesTimingsItems(addLinesTimingInput)
        if(editLineItem != null){
            editLineIdInput.visibility = View.VISIBLE
            addLineButton.visibility = View.GONE
            editLineButton.visibility = View.VISIBLE
            editLineIdInput.setText(editLineItem?.Id.toString())
            addLineNameInput.setText(editLineItem?.Name.toString())
            val selectedTiming : DBTimings? =
                if(model!!.timings?.value?.filter { it.Id == editLineItem!!.TimingId }?.count()!! >0)
                    model!!.timings?.value?.filter { it.Id == editLineItem!!.TimingId }?.first()
                else
                    null
            addLinesTimingInput.setSelection(arrayAdapter!!.getPosition(selectedTiming?.TimingName))
            addLinesTimingValue = selectedTiming
        }
        addLineButton.setOnClickListener {
            if(addLineNameInput.text == null || addLineNameInput.text.toString() == ""){
                addLineNameInput.error = getString(R.string.AddLinesNameInputNullError);
                addLineNameNull = true
            }
            if(!addLineNameNull){
                val db : DownloadManagerDatabase =
                    DownloadManagerDatabase.getInstance(context)
                val downloadLineDao : DBDownloadLineDao = db.dbDownloadLineDao()
                val newDownloadLine : DBDownloadLine = DBDownloadLine()
                newDownloadLine.Name = addLineNameInput.text.toString()
                if(addLinesTimingValue != null)
                    newDownloadLine.TimingId = addLinesTimingValue!!.Id
                else
                    newDownloadLine.TimingId = 0
                if(model!!.singedInUser?.value != null)
                    newDownloadLine.UserCreatorId = model!!.singedInUser?.value!!.Id
                else
                    newDownloadLine.UserCreatorId = 0
                newDownloadLine.DateCreated = Calendar.getInstance().timeInMillis
                viewModelScope.launch {
                    var downloadLines : List<DBDownloadLine>
                    try {
                        withContext(Dispatchers.IO) {
                            downloadLineDao.Insert(newDownloadLine)
                            downloadLines = if(SignedInUser!=null) downloadLineDao.getAll(SignedInUser!!.Id) else downloadLineDao.getAll(0)
                        }
                        model!!.downloadLines?.postValue(downloadLines)
                        Toast.makeText(
                            context ,
                            resources.getString(R.string.AddLineSuccessMessage) ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }catch (e : Exception){
                        withContext(Dispatchers.IO) {
                            downloadLines =
                                if (SignedInUser != null) downloadLineDao.getAll(SignedInUser!!.Id) else downloadLineDao.getAll(
                                    0
                                )
                        }
                        model!!.downloadLines?.postValue(downloadLines)
                        Toast.makeText(
                            context ,
                            e.message ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }
                }
            }
        }
        editLineButton.setOnClickListener {
            if(editLineIdInput.text == null || editLineIdInput.text.toString() == ""){
                editLineIdInput.error = getString(R.string.AddLineIdInputNullError);
                editLineIdNull = true
            }
            if(addLineNameInput.text == null || addLineNameInput.text.toString() == ""){
                addLineNameInput.error = getString(R.string.AddLinesNameInputNullError);
                addLineNameNull = true
            }
            if(model!!.singedInUser?.value != null)
            if(!addLineNameNull && !editLineIdNull && model!!.singedInUser?.value?.Id == editLineItem?.UserCreatorId) {
                val db : DownloadManagerDatabase =
                    DownloadManagerDatabase.getInstance(context)
                val downloadLineDao : DBDownloadLineDao = db.dbDownloadLineDao()
                editLineItem?.Name = addLineNameInput.text.toString()
                if(addLinesTimingValue != null)
                    editLineItem?.TimingId = addLinesTimingValue!!.Id
                else
                    editLineItem?.TimingId = 0
                if(model!!.singedInUser?.value != null)
                    editLineItem?.DateCreated = Calendar.getInstance().timeInMillis
                viewModelScope.launch {
                    var downloadLines : List<DBDownloadLine>
                    try {
                        withContext(Dispatchers.IO) {
                            downloadLineDao.Update(editLineItem)
                            downloadLines = if(SignedInUser!=null) downloadLineDao.getAll(SignedInUser!!.Id) else downloadLineDao.getAll(0)
                        }
                        model!!.downloadLines?.postValue(downloadLines)
                        Toast.makeText(
                            context ,
                            resources.getString(R.string.EditLineSuccessText) ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }catch (e : Exception){
                        withContext(Dispatchers.IO) {
                            downloadLines =
                                if (SignedInUser != null) downloadLineDao.getAll(SignedInUser!!.Id) else downloadLineDao.getAll(
                                    0
                                )
                        }
                        model!!.downloadLines?.postValue(downloadLines)
                        Toast.makeText(
                            context ,
                            e.message ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }
                }
            }
        }
        val timingsObserver = Observer<List<DBTimings>> {
            setAddLinesTimingsItems(addLinesTimingInput)
        }
        model!!.timings?.observe(viewLifecycleOwner, timingsObserver)
    }
    private fun setAddLinesTimingsItems(spinner : Spinner){
        val timings : List<DBTimings>? = model!!.timings?.value
        val timingNames : MutableList<String> = mutableListOf<String>()
        timings?.forEach {
            timingNames.add(it.TimingName)
        }
        timingNames.add("هیچکدام")
        arrayAdapter= ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            timingNames.toList()
        )
        arrayAdapter?.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = this
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        addLinesTimingValue =
            if(model!!.timings?.value != null)
                if(pos < model!!.timings!!.value!!.count())
                    model!!.timings?.value?.get(pos)
                else null
            else null
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}