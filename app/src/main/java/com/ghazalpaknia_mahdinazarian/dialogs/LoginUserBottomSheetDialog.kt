package com.ghazalpaknia_mahdinazarian.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.Helpers
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class LoginUserBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? = inflater.inflate(R.layout.login_bottom_sheet , container , false)

    companion object {
        const val TAG = "LoginUserBottomSheet"
    }
    private val viewModelJob = SupervisorJob()

    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val starterIntent : Intent = Intent(context,MainActivity::class.java)
        val loginEmailInput : EditText = view.findViewById(R.id.LoginEmailInput)
        val loginPasswordInput : EditText = view.findViewById(R.id.LoginPasswordInput)
        val loginButton : Button = view.findViewById(R.id.LoginUserButton)
        loginButton.setOnClickListener {
            var emailInputNull : Boolean = false
            var passwordInputNull : Boolean = false
            if (loginEmailInput.text == null || loginEmailInput.text.isEmpty()) {
                loginEmailInput.error = "نام نمی تواند خالی باشد"
                emailInputNull = true
            }
            if (loginPasswordInput.text == null || loginPasswordInput.text.isEmpty()) {
                loginPasswordInput.error = "نام خانوادگی نمی تواند خالی باشد"
                passwordInputNull = true
            }
            if (!emailInputNull && !passwordInputNull){
                viewModelScope.launch {
                    val db : DownloadManagerDatabase =
                        DownloadManagerDatabase.getInstance(context)
                    val userDao : DBUserDao = db.dbUserDao()
                    var allUsers : List<DBUsers>?
                    var registeredUser : DBUsers?
                    val enteredEmail : String = loginEmailInput.text.toString()
                    val hashedPassword : String = Helpers.md5(loginPasswordInput.text.toString())
                    withContext(Dispatchers.IO) {
                        registeredUser =
                            userDao.getUserByEmailAndPassword(enteredEmail,hashedPassword)
                    }
                    if(registeredUser != null){
                        withContext(Dispatchers.IO) {
                            allUsers =
                                userDao.all
                        }
                        if(allUsers != null && allUsers!!.isNotEmpty()){
                            allUsers!!.forEach {
                                it.loggedIn = false
                                withContext(Dispatchers.IO){
                                    userDao.Update(it)
                                }
                            }
                        }
                        registeredUser!!.loggedIn = true
                        try{
                            withContext(Dispatchers.IO){
                                userDao.Update(registeredUser)
                            }
                            activity?.finish()
                            activity?.overridePendingTransition( 0, 0);
                            startActivity(starterIntent)
                            activity?.overridePendingTransition( 0, 0);
                        }catch (e : Exception){
                            Toast.makeText(
                                context ,
                                e.message.toString() ,
                                Toast.LENGTH_LONG
                            )
                                .show();
                        }
                    }else{
                        Toast.makeText(
                            context ,
                            "نام کاربری یا رمزعبور اشتباه است" ,
                            Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }
        }
    }
}