package com.moontvdigital.app.ui

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.moontvdigital.app.R
import com.moontvdigital.app.api.ApiService
import com.moontvdigital.app.api.ServiceBuilder
import com.moontvdigital.app.data.AuthResponse
import com.moontvdigital.app.data.CountryItem
import com.moontvdigital.app.data.CountryListResponse
import com.moontvdigital.app.data.DistItem
import com.moontvdigital.app.data.DistListResponse
import com.moontvdigital.app.data.StateItem
import com.moontvdigital.app.data.StateListResponse
import com.moontvdigital.app.data.UserData
import com.moontvdigital.app.databinding.ActivityAuthBinding
import com.moontvdigital.app.utilities.PreferenceManager
import com.moontvdigital.app.utilities.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private lateinit var binding: ActivityAuthBinding
    private lateinit var preferenceManager: PreferenceManager

    private var countryList: List<CountryItem?> = listOf()
    private var stateList: List<StateItem?> = listOf()
    private var distList: List<DistItem?> = listOf()
    private var selectedCountryPos = -1
    private var selectedStatePos = -1
    private var selectedDistPos = -1

    private var current = "Login"
    private lateinit var fullName: String
    private lateinit var countryCode: String
    private lateinit var mobileNum: String
    private lateinit var passCode: String

    private var selectedCountryId = "1"
    private var selectedStateId = "1"
    private var selectedDistId = "1"

    private var countryCallFinished = false
    private var stateCallFinished = false
    private var distCallFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager.getInstance(this)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.default_back_light)
        }

        binding.tvLogin.paintFlags = binding.tvLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvRegister.paintFlags = binding.tvLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.tvLogin.setOnClickListener {
            setForLogin()
        }

        binding.tvRegister.setOnClickListener {
            setForRegister()
        }

        binding.btnLogin.setOnClickListener {
            if (validate()) {
                login()
            }
        }

        binding.btnRegister.setOnClickListener {
            if (validate()) {
                register()
            }
        }

        callGetCountryListApi()
        callStateListApi()
        callDistListApi()

        setForLogin()
    }

    private fun setForLogin() {
        current = "Login"
        supportActionBar?.title = "Login"
        binding.registerExtraUi.visibility = View.GONE

        binding.btnLogin.visibility = View.VISIBLE
        binding.noAccountView.visibility = View.VISIBLE

        binding.btnRegister.visibility = View.GONE
        binding.haveAccountView.visibility = View.GONE

        binding.editTextMobile.requestFocus()
    }

    private fun setForRegister() {
        current = "Register"
        supportActionBar?.title = "Register"
        binding.registerExtraUi.visibility = View.VISIBLE

        binding.btnLogin.visibility = View.GONE
        binding.noAccountView.visibility = View.GONE

        binding.btnRegister.visibility = View.VISIBLE
        binding.haveAccountView.visibility = View.VISIBLE

        binding.tvCountry.requestFocus()

        resetCountry()
        resetState()
        resetDist()
    }

    private fun validate(): Boolean {
        fullName = binding.editTextFullName.text.toString()
        countryCode = binding.editTextCountryCode.text.toString()
        mobileNum = binding.editTextMobile.text.toString()
        passCode = binding.editTextPasscode.text.toString()
        if (current == "Register") {
            if (selectedCountryPos == -1) {
                showSnack("Please select a country")
                return false
            } else {
                if (countryList[selectedCountryPos]?.countryId.equals(Util.COUNTRY_ID_INDIA)) {
                    //India is selected; check whether state is selected or not
                    if (selectedStatePos == -1) {
                        showSnack("Please select a state")
                        return false
                    } else {
                        if (stateList[selectedStatePos]?.stateId.equals(Util.STATE_ID_ODISHA)) {
                            //Odisha is selected; check whether city is selected or not
                            if (selectedDistPos == -1) {
                                showSnack("Please select a district")
                                return false
                            }
                        } else {
                            // Other State is selected; set dist id to default
                            selectedDistId = "1"
                        }
                    }
                } else {
                    // Other Country is selected; set state and dist id to default
                    selectedStateId = "1"
                    selectedDistId = "1"
                }
            }
            if (fullName.isEmpty()) {
                showSnack("Name should not be empty")
                return false
            }
        }
        if (mobileNum.isEmpty()) {
            showSnack("Mobile number should not be empty")
            return false
        }
        if (passCode.isEmpty()) {
            showSnack("Passcode should not be empty")
            return false
        }
        if (passCode.length != 4) {
            showSnack("Passcode should be 4 digit")
            return false
        }
        return true
    }

    private fun login() {
        binding.progressIndicator.visibility = View.VISIBLE
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val call = service.userLogin(mobileNum, passCode)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                binding.progressIndicator.visibility = View.GONE

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.code.equals("200")) {
                        showSnack("Success")
                        handleAuthSuccess(loginResponse?.data?.get(0))
                    } else {
                        showSnack(loginResponse?.message ?: "Invalid Mobile Number or Passcode")
                    }
                } else {
                    showSnack("Something went wrong. Try again.")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.localizedMessage}")
                binding.progressIndicator.visibility = View.GONE
                showSnack("Something went wrong. Try again.")
            }
        })
    }

    private fun register() {
        Log.d(
            TAG,
            "register => selectedCountryId: $selectedCountryId; selectedStateId: $selectedStateId; selectedDistId: $selectedDistId"
        )/*binding.progressIndicator.visibility = View.VISIBLE
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val call = service.userRegister(selectedCountryId, selectedStateId, selectedDistId, fullName, mobileNum, passCode)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                binding.progressIndicator.visibility = View.GONE
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.code.equals("200")) {
                        showSnack("Success")
                        handleAuthSuccess(loginResponse?.data?.get(0))
                    } else {
                        showSnack(loginResponse?.message ?: "Invalid Mobile Number or Passcode")
                    }
                } else {
                    showSnack("Something went wrong. Try again.")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.localizedMessage}")
                binding.progressIndicator.visibility = View.GONE
                showSnack("Something went wrong. Try again.")
            }
        })*/
    }

    private fun callGetCountryListApi() {
        binding.progressIndicator.visibility = View.VISIBLE
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val countryListCall = service.getCountryList()
        countryListCall.enqueue(object : Callback<CountryListResponse> {
            override fun onResponse(
                call: Call<CountryListResponse>, response: Response<CountryListResponse>
            ) {
                countryCallFinished = true
                binding.progressIndicator.visibility = View.GONE
                if (response.isSuccessful) {
                    val countryListResponse = response.body() as CountryListResponse
                    if (countryListResponse.code == "200") {
                        countryList = countryListResponse.countryList!!
                        setCountryAdapter()
                    }
                }
            }

            override fun onFailure(call: Call<CountryListResponse>, t: Throwable) {
                Log.e(TAG, "onFailure:\n${t.localizedMessage}")
                binding.progressIndicator.visibility = View.GONE
            }

        })
    }

    private fun setCountryAdapter() {
        val displayCountries: MutableList<String?> = mutableListOf()
        countryList.stream().forEach {
            displayCountries.add(it?.countryName)
        }

        val adapter = ArrayAdapter(this, R.layout.item_single_choice_list, displayCountries)
        binding.tvCountry.setAdapter(adapter)

        binding.tvCountry.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, parent, pos, id ->
                resetState()
                resetDist()
                selectedCountryPos = pos
                selectedCountryId = countryList[pos]?.countryId!!
                if (selectedCountryId == Util.COUNTRY_ID_INDIA) {
                    binding.inputState.visibility = View.VISIBLE
                } else {
                    binding.inputState.visibility = View.GONE
                    binding.inputDist.visibility = View.GONE
                }
            }
        binding.inputCountry.visibility = View.VISIBLE
    }

    private fun callStateListApi() {
        binding.progressIndicator.visibility = View.VISIBLE
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val stateListCall = service.getStateList(Util.COUNTRY_ID_INDIA)
        stateListCall.enqueue(object : Callback<StateListResponse> {
            override fun onResponse(
                call: Call<StateListResponse>, response: Response<StateListResponse>
            ) {
                stateCallFinished = true
                binding.progressIndicator.visibility = View.GONE
                if (response.isSuccessful) {
                    val stateListResponse = response.body() as StateListResponse
                    if (stateListResponse.code == "200") {
                        stateList = stateListResponse.stateList
                        setStateAdapter()
                    }
                }
            }

            override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
                Log.e(TAG, "onFailure:\n${t.localizedMessage}")
                binding.progressIndicator.visibility = View.GONE
            }

        })
    }

    private fun setStateAdapter() {
        val displayStates: MutableList<String?> = mutableListOf()
        stateList.stream().forEach {
            displayStates.add(it?.stateName)
        }

        val adapter = ArrayAdapter(this, R.layout.item_single_choice_list, displayStates)
        binding.tvState.setAdapter(adapter)

        binding.tvState.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, parent, pos, id ->
                resetDist()
                selectedStatePos = pos
                selectedStateId = stateList[pos]?.stateId!!
                if (selectedStateId == Util.STATE_ID_ODISHA) {
                    binding.inputDist.visibility = View.VISIBLE
                } else {
                    binding.inputDist.visibility = View.GONE
                }
            }
    }

    private fun callDistListApi() {
        binding.progressIndicator.visibility = View.VISIBLE
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val distListCall = service.getDistrictList(Util.STATE_ID_ODISHA)
        distListCall.enqueue(object : Callback<DistListResponse> {
            override fun onResponse(
                call: Call<DistListResponse>, response: Response<DistListResponse>
            ) {
                distCallFinished = true
                binding.progressIndicator.visibility = View.GONE
                if (response.isSuccessful) {
                    val distListResponse = response.body() as DistListResponse
                    if (distListResponse.code == "200") {
                        distList = distListResponse.distList
                        setDistAdapter()
                    }
                }
            }

            override fun onFailure(call: Call<DistListResponse>, t: Throwable) {
                Log.e(TAG, "onFailure:\n${t.localizedMessage}")
                binding.progressIndicator.visibility = View.GONE
            }
        })
    }

    private fun setDistAdapter() {
        val displayDists: MutableList<String?> = mutableListOf()
        distList.stream().forEach {
            displayDists.add(it?.districtName)
        }

        val adapter = ArrayAdapter(this, R.layout.item_single_choice_list, displayDists)
        binding.tvDist.setAdapter(adapter)

        binding.tvDist.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, parent, pos, id ->
                selectedDistPos = pos
                selectedDistId = distList[pos]?.districtId!!
            }
    }

    private fun resetCountry() {
        selectedCountryPos = -1
        binding.tvCountry.setText("")
    }

    private fun resetState() {
        selectedStatePos = -1
        binding.tvState.setText("")
    }

    private fun resetDist() {
        selectedDistPos = -1
        binding.tvDist.setText("")
    }

    private fun handleAuthSuccess(userData: UserData?) {
        if (userData != null) {
            preferenceManager.userId = userData.userId
            preferenceManager.userName = userData.fullName
            preferenceManager.mobileNo = userData.mobileNo
            finishAfterTransition()
        }
    }

    private fun showSnack(message: String) {
        Snackbar.make(this@AuthActivity, binding.parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun allowInput(): Boolean {
        return countryCallFinished && stateCallFinished && distCallFinished
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}