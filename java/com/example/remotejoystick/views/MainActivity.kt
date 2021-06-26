package com.example.remotejoystick.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.remotejoystick.R
import com.example.remotejoystick.view_model.ViewModel

class MainActivity : AppCompatActivity() {
    var vm = ViewModel()
     lateinit var joystick : Joystick

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //dependency injection-overrides default joystick behaviour
        joystick = findViewById(R.id.joystick)
        joystick.onChange= {a,e->
            vm.aileronSetter(a)
            vm.elevatorSetter(e)
        }

        //connection events behaviour
        vm.setFailedConnect { onFailedConnect() }
        vm.setConnectEvent { onConnect() } //this means onConnect() is invoked whenever the vm is 'connected'

        //create listeners for the seekbars
        val rudderBar = findViewById<SeekBar>(R.id.rudderBar)
        rudderBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //whenever progress changes we want to gather the information and send to the viewModel
                vm.rudderSetter(progress/100f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing...
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //do nothing...
            }
        })

        val throttleBar = findViewById<SeekBar>(R.id.throttleBar)
        throttleBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //whenever progress changes we want to gather the information and send to the viewModel
                vm.throttleSetter(progress/100f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing...
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //do nothing...
            }
        })
    }

    fun connect(view: View) {//check validity of ip and port,connect to FG simulator

        if(!TextUtils.isEmpty(findViewById<EditText>(R.id.ip).getText()) && !TextUtils.isEmpty(findViewById<EditText>(R.id.port).getText())) {
            //tell viewModel to connect to the server on given ip and port
            findViewById<View>(R.id.ProgressBar).visibility= View.VISIBLE//loading icon
            findViewById<Button>(R.id.button).isClickable=false//prevent user from clicking multiple times
            vm.connect(findViewById<EditText>(R.id.ip).text.toString(), findViewById<EditText>(R.id.port).text.toString())
        }
        else{//missing info
            Toast.makeText(applicationContext,"Please enter IP and Port!", Toast.LENGTH_LONG).show()
        }

    }

    private fun onConnect() {//display message when connected successfully
        findViewById<View>(R.id.ProgressBar).visibility= View.GONE
        findViewById<Button>(R.id.button).isClickable=true
        Toast.makeText(applicationContext, "Connected to the server!", Toast.LENGTH_LONG).show()
    }
    private fun onFailedConnect() {//displays message on ui when connection fails
        findViewById<View>(R.id.ProgressBar).visibility= View.GONE
        findViewById<Button>(R.id.button).isClickable=true
        Toast.makeText(applicationContext,"Failed to connect to the server!", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {//when closing the app
        vm.disconnect()
        super.onDestroy()
    }

}