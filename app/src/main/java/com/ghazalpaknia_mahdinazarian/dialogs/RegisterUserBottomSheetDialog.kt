package com.ghazalpaknia_mahdinazarian.dialogs

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
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.Helpers
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.Calendar
import java.util.Date

class RegisterUserBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? = inflater.inflate(R.layout.register_bottom_sheet , container , false)

    companion object {
        const val TAG = "RegisterUserBottomSheet"
    }

    private val viewModelJob = SupervisorJob()

    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val nameInput : EditText = view.findViewById(R.id.RegisterNameInput)
        val lastNameInput : EditText = view.findViewById(R.id.RegisterLastNameInput)
        val emailInput : EditText = view.findViewById(R.id.RegisterEmailInput)
        val passwordInput : EditText = view.findViewById(R.id.RegisterPasswordInput)
        val repeatPasswordInput : EditText = view.findViewById(R.id.RegisterRepeatPasswordInput)
        val registerButton : Button = view.findViewById(R.id.RegisterUserButton)
        registerButton.setOnClickListener {
            var nameInputNull : Boolean = false
            var lastNameInputNull : Boolean = false
            var emailInputNull : Boolean = false
            var passwordInputNull : Boolean = false
            var repeatPasswordInputNull : Boolean = false
            if (nameInput.text == null || nameInput.text.isEmpty()) {
                nameInput.error = "نام نمی تواند خالی باشد"
                nameInputNull = true
            }
            if (lastNameInput.text == null || lastNameInput.text.isEmpty()) {
                lastNameInput.error = "نام خانوادگی نمی تواند خالی باشد"
                lastNameInputNull = true
            }
            if (emailInput.text == null || emailInput.text.isEmpty()) {
                emailInput.error = "ایمیل نمی تواند خالی باشد"
                emailInputNull = true
            }
            if (passwordInput.text == null || passwordInput.text.isEmpty()) {
                passwordInput.error = "رمزعبور نمی تواند خالی باشد"
                passwordInputNull = true
            }
            if (repeatPasswordInput.text == null || repeatPasswordInput.text.isEmpty()) {
                repeatPasswordInput.error = "تکرار رمز عبور نمی تواند خالی باشد"
                repeatPasswordInputNull = true
            }
            if (!nameInputNull && !lastNameInputNull && !emailInputNull && !passwordInputNull &&
                !repeatPasswordInputNull
            ) {
                if (passwordInput.text.toString() == repeatPasswordInput.text.toString()) {
                    viewModelScope.launch {
                        val db : DownloadManagerDatabase =
                            DownloadManagerDatabase.getInstance(context)
                        val userDao : DBUserDao = db.dbUserDao()
                        var registeredUser : DBUsers?
                        val enteredEmail : String = emailInput.text.toString()
                        withContext(Dispatchers.IO) {
                            registeredUser =
                                userDao.getUserByEmail(enteredEmail)
                        }

                        if (registeredUser == null) {
                            val user : DBUsers = DBUsers()
                            user.Name = nameInput.text.toString()
                            user.LastName = lastNameInput.text.toString()
                            user.Email = emailInput.text.toString()
                            user.Password = Helpers.md5(passwordInput.text.toString())
                            user.AccountCreationDate = Calendar.getInstance().timeInMillis
                            try {
                                withContext(Dispatchers.IO) {userDao.Insert(user)}
                                Toast.makeText(
                                    context ,
                                    R.string.SuccessFullDatabaseInsert ,
                                    Toast.LENGTH_LONG
                                ).show();
                            } catch (e : java.lang.Exception) {
                                Toast.makeText(
                                    context ,
                                    e.message.toString() ,
                                    Toast.LENGTH_LONG
                                )
                                    .show();
                            }
                        } else {
                            Toast.makeText(
                                context ,
                                "کاربری با این مشخصات ثبت شده است" ,
                                Toast.LENGTH_LONG
                            ).show();
                        }

                    }
                } else {
                    repeatPasswordInput.error = "رمزعبور و تکرار آن یکسان نیست"
                }
            }
        }
    }
}