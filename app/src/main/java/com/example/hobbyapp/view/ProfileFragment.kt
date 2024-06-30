package com.example.hobbyapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hobbyapp.R
import com.example.hobbyapp.databinding.FragmentProfileBinding
import com.example.hobbyapp.global.DatabaseBuilder
import com.example.hobbyapp.global.Global.Companion.makeAlert
import com.example.hobbyapp.global.ProfileClickHandler
import com.example.hobbyapp.model.User
import com.example.hobbyapp.viewmodel.UserViewModel
import com.example.hobbyapp.viewmodel.UserViewModelFactory

class ProfileFragment : Fragment(), ProfileClickHandler {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModel:UserViewModel
    var savedUsername:String?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this
        binding.clickHandler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userDao = DatabaseBuilder.getInstance(this.requireActivity().applicationContext).userDao()
        val viewModelFactory = UserViewModelFactory(this.requireActivity().application, userDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        var sharedFile = requireActivity().packageName
        var shared: SharedPreferences = requireActivity().getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
        savedUsername = shared.getString(LoginFragment.EXTRA_USERNAME, "")
        savedUsername?.let {
            if (savedUsername!!.isNotEmpty()){
                Log.d("saved username", savedUsername!!)
                viewModel.fetch(savedUsername!!)
                observeViewModel()
            }else{
                Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionLogout())
            }
        }

        binding.buttonSave.setOnClickListener {

        }

        binding.textLogout.setOnClickListener {
            val username = ""
            var sharedFile = activity?.packageName
            var shared: SharedPreferences? = activity?.getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor? = shared?.edit()
            editor?.putString(LoginFragment.EXTRA_USERNAME,username)
            editor?.apply()
            val action = ProfileFragmentDirections.actionLogout()
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun observeViewModel() {
        viewModel.userLD.observe(viewLifecycleOwner, Observer {
            if (it != null){
//                binding.textUsername.text = it.username
//                binding.editTextFisrtname.setText(it.firstname)
//                binding.editTextLastname.setText(it.lastname)
                binding.user = it
            }
            Log.d("User", it.toString())
        })
    }

    override fun onProfileClick(view: View, newUser: User) {
        val firstname = newUser.firstname
        val lastname = newUser.lastname
        val password = newUser.password

        val newUser:User = if(TextUtils.isEmpty(password)){
            Log.d("password if", password ?: "")
            User(0,savedUsername!!,null,firstname,lastname)
        }else{
            Log.d("password else", password ?: "")
            User(0,savedUsername!!,password,firstname,lastname)

        }

        if (password.isNullOrEmpty()){
            Toast.makeText(this.context,"Password tidak boleh kosng", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.updateUser(newUser)
        }

        viewModel.updateSuccessLD.observe(viewLifecycleOwner, Observer {
            if(it){
                makeAlert(view.context,"Success!", "Update profile success!")
            }else{
                makeAlert(view.context,"ALERT!", "Update profile failed!")

            }
        })
    }
}