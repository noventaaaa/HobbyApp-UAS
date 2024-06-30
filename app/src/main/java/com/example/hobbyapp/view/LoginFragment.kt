package com.example.hobbyapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hobbyapp.databinding.FragmentLoginBinding
import com.example.hobbyapp.global.DatabaseBuilder
import com.example.hobbyapp.global.Global.Companion.makeAlert
import com.example.hobbyapp.viewmodel.UserViewModel
import com.example.hobbyapp.viewmodel.UserViewModelFactory

class LoginFragment : Fragment() {
    companion object{
        val EXTRA_USERNAME = "USERNAME"
    }
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentLoginBinding
    lateinit var shared: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userDao = DatabaseBuilder.getInstance(this.requireActivity().applicationContext).userDao()
        val viewModelFactory = UserViewModelFactory(this.requireActivity().application, userDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        var sharedFile = requireActivity().packageName
        shared = requireActivity().getSharedPreferences(sharedFile, Context.MODE_PRIVATE)

        binding.buttonSignIn.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.login(username, password)
            viewModel.loginSuccessLD.observe(viewLifecycleOwner, Observer {
                if(it){
                    var editor: SharedPreferences.Editor = shared.edit()
                    editor.putString(EXTRA_USERNAME,username)
                    editor.apply()
                    val action = LoginFragmentDirections.actionNewsListFromLogin()
                    Navigation.findNavController(view).navigate(action)
                }else{
                    makeAlert(view.context,"ALERT!","Username or password does not match out database")
                }
            })
        }

        binding.txtSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionRegister()
            Navigation.findNavController(it).navigate(action)
        }
    }
}