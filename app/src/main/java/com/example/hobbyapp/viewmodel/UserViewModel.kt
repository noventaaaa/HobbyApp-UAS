package com.example.hobbyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hobbyapp.global.Global
import com.example.hobbyapp.model.User
import com.example.hobbyapp.model.local.UserDao
import com.example.hobbyapp.util.Mapper.mapperRequest
import com.example.hobbyapp.util.Mapper.mapperUser
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserViewModel(application: Application, private val userDao: UserDao):AndroidViewModel(application) {
    var userLD = MutableLiveData<User>()
    private var userData = MutableLiveData<User?>()
    var registerSuccessLD = MutableLiveData<Boolean>()
    var loginSuccessLD = MutableLiveData<Boolean>()
    var updateSuccessLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null
    fun register(user:User){

        queue = Volley.newRequestQueue(getApplication())
        val url = "${Global.baseUrl}/register.php"

        val stringRequest = object:StringRequest(
            Method.POST, url, {
                val result = JSONObject(it)
                insertUserLocal(user)
                registerSuccessLD.value = result.getString("status") == "success"
            },{
                registerSuccessLD.value = false
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                val hashMap = HashMap<String, String>()
                hashMap["username"] = user.username
                user.password?.let { hashMap.put("password", it) }
                hashMap["firstname"] = user.firstname
                hashMap["lastname"] = user.lastname
                return hashMap
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun login(username:String, password:String){
        queue = Volley.newRequestQueue(getApplication())
        getUserByUsername(username)
        if (userData.value?.username == username && userData.value?.password == password){
            loginSuccessLD.value = true
        } else {
            val url = "${Global.baseUrl}/login.php"

            val stringRequest = object:StringRequest(
                Method.POST, url, {
                    val result = JSONObject(it)
                    Log.d("result login", result.toString())
                    loginSuccessLD.value = result.getString("status") == "success"
                },{
                    Log.d("result login error", it.toString())
                    loginSuccessLD.value = false
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val hashMap = HashMap<String, String>()
                    hashMap["username"] = username
                    hashMap["password"] = password
                    return hashMap
                }
            }
            stringRequest.tag = TAG
            queue?.add(stringRequest)
        }

    }

    fun fetch(username:String){
        getUserByUsername(username)
        if (userData.value?.username == username){
            userLD.value = userData.value
        } else {
            queue = Volley.newRequestQueue(getApplication())
            val url = "${Global.baseUrl}/getuser.php"

            val stringRequest = object:StringRequest(
                Method.POST, url, {
                    val obj = JSONObject(it)
                    if (obj.getString("status") == "success"){
                        val user = Gson().fromJson(obj.getString("data"), User::class.java)
                        Log.d("user ojb",user.toString())
                        userLD.value = user
                    }
                    Log.d("getuser", it.toString())
                },{
                    Log.d("getuser error", it.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val hashMap = HashMap<String, String>()
                    hashMap["username"] = username
                    return hashMap
                }
            }
            stringRequest.tag = TAG
            queue?.add(stringRequest)
        }

    }

    fun updateUser(newUser: User){
        queue = Volley.newRequestQueue(getApplication())
        val url = "${Global.baseUrl}/updateuser.php"

        val stringRequest = object:StringRequest(
            Method.POST, url, {
                Log.d("updateuser", it.toString())
                val obj = JSONObject(it)
                if (obj.getString("status") == "success"){
                    Log.d("result update",it.toString())
                    getUserByUsername(newUser.username)
                    if (userData.value?.username != newUser.username) {
                        insertUserLocal(newUser)
                    } else {
                        updateUserLocal(newUser)
                    }

                    updateSuccessLD.value = true
                }else{
                    updateSuccessLD.value = false
                }
            },{
                    updateSuccessLD.value = false
                Log.d("updateuser error", it.toString())
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                val hashMap = HashMap<String, String>()
                hashMap["username"] = newUser.username
                hashMap["firstname"] = newUser.firstname
                hashMap["lastname"] = newUser.lastname
                newUser.password?.let {
                    Log.d("new pass", newUser.password!!)
                    hashMap["password"]= newUser.password!! }

                return hashMap
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    private fun insertUserLocal(user: User) {
        viewModelScope.launch {
            userDao.insert(user.mapperRequest())
        }
    }

    private fun updateUserLocal(user: User) {
        viewModelScope.launch {
            userDao.update(user.mapperRequest())
        }
    }

    private fun getUserByUsername(username: String){
        viewModelScope.launch {
            val user = userDao.getUserByUsername(username)
            userData.value = user.mapperUser()
        }

    }


    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}