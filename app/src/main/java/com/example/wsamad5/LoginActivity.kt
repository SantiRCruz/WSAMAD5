package com.example.wsamad5

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.wsamad5.core.Constants
import com.example.wsamad5.core.Validations
import com.example.wsamad5.core.networkInfo
import com.example.wsamad5.data.post
import com.example.wsamad5.data.signIn
import com.example.wsamad5.databinding.ActivityLoginBinding
import com.example.wsamad5.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.invoke.ConstantCallSite
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()


        clicks()
//        writingValidations()

    }

    private fun writingValidations() {
        emailWriting()
    }

    private fun emailWriting() {
        binding.edtEmail.addTextChangedListener {
            val regex = Pattern.compile("^([a-zA-Z]{1,}@wsa[.]com)")
            if (it.isNullOrEmpty()) {
                binding.edtEmail.error = "The email can't be empty"
                binding.btnSignIn.isEnabled = false
            } else if (!regex.matcher(it).matches()) {
                binding.edtEmail.error = "The Must have and email format"
                binding.btnSignIn.isEnabled = false
            } else {
                binding.edtEmail.error = null
                binding.btnSignIn.isEnabled = true
            }
        }
    }

    private fun clicks() {
        binding.btnSignIn.setOnClickListener { validate() }
    }

    private fun validate() {
        val result = arrayOf(validateEmail(), validatePassword())
        if (false in result) {
            return
        }
        if (!networkInfo(applicationContext)) {
            alertMessage("We find a problem with your network")
            return
        }
        visibleProgress(true)
        sendSignIn()
    }

    private fun sendSignIn() {
        Constants.OKHTTP.newCall(
            post(
                "signin",
                signIn(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            )
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
                runOnUiThread {
                    alertMessage("Server Error!")
                    visibleProgress(false)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONTokener(response.body!!.string()).nextValue() as JSONObject
                if (json.getBoolean("success")) {
                    val data = json.getJSONObject("data")
                    val sharedPreferences =
                        getSharedPreferences(Constants.USER, Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("id", data.getString("id"))
                        putString("name", data.getString("name"))
                        putString("token", data.getString("token"))
                        apply()
                    }
                    runOnUiThread {
                        visibleProgress(false)
                    }
                    val i = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(i)
                } else {
                    runOnUiThread {
                        visibleProgress(false)
                        alertMessage("We can’t find account with this credentials")
                    }
                }
            }
        })
    }

    private fun visibleProgress(b: Boolean) {
        if (b) {
            binding.progress.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.GONE
        } else {
            binding.progress.visibility = View.GONE
            binding.btnSignIn.visibility = View.VISIBLE
        }
    }

    private fun validatePassword(): Boolean {
        return if (!Validations.password(binding.edtPassword.text.toString())) {
            alertMessage("Any Field Can't be empty")
            false
        } else {
            true
        }
    }

    private fun validateEmail(): Boolean {
        return when (Validations.email(binding.edtEmail.text.toString())) {
            Validations.ValidationValues.EMPTY -> {
                alertMessage("Any Field Can't be empty")
                false
            }
            Validations.ValidationValues.REGEX_ERROR -> {
                alertMessage("The Email Field is with a wrong format")
                false
            }
            Validations.ValidationValues.CORRECT -> {
                true
            }
        }
    }

    private fun alertMessage(s: String) {
        binding.llAlert.visibility = View.VISIBLE
        binding.txtAlert.text = s
        binding.btnSignIn.animate().translationY(300f).setDuration(200).withEndAction {
            binding.llAlert.animate().alpha(1f).setDuration(200).withEndAction {
                binding.llAlert.animate().alpha(1f).setDuration(800).withEndAction {
                    binding.llAlert.animate().alpha(0f).setDuration(200)
                    binding.btnSignIn.animate().translationY(0f).setDuration(200)
                    binding.llAlert.visibility = View.GONE

                }
            }
        }
    }

}