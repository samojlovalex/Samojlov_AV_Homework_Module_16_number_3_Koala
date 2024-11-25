package com.example.samojlov_av_homework_module_16_number_3_koala

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samojlov_av_homework_module_16_number_3_koala.databinding.ActivityMainBinding
import com.example.samojlov_av_homework_module_16_number_3_koala.fragments.PhoneBookFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dataSMS: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

    }

    private fun init() {

        enterFragment()
    }

    fun enterFragment() {

        supportFragmentManager.beginTransaction()
            .add(R.id.containerFragmentMainActivityFCV, PhoneBookFragment())
            .commit()


    }

    val permissionOfSMS = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        var allAreGranted = true
        for (isGranted in result.values) {
            allAreGranted = allAreGranted && isGranted
        }
        if (allAreGranted) {

        } else {
            Toast.makeText(
                this,
                this.getString(R.string.permission_call_phone_else_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val permissionOfCall = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

        } else {
            Toast.makeText(
                this,
                this.getString(R.string.permission_call_phone_else_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun sendDataToSMS(data: String) {
        dataSMS = data
    }

    fun getDataToSMS(): String? {
        return dataSMS
    }


}