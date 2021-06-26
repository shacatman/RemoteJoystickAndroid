package com.example.remotejoystick.model


import android.os.Handler
import android.os.Looper
import java.io.PrintWriter
import java.lang.Exception
import java.net.*


class FGPlayer {//model
    //fields
    lateinit var out : PrintWriter
    var aileron: Float=0f
    var elevator: Float=0f
    var rudder: Float=0f
    var throttle: Float=0f
    var updateA: Boolean=true
    var updateE: Boolean=true
    var updateR: Boolean=true
    var updateT: Boolean=true
    lateinit var fgClient: Socket
    var onConnect={}//show message in UI when connected(overridden in view class)
    var onFailedConnect ={}//default behaviour(overridden in the view to notify user when connection fails)
    var done: Boolean = false

    fun connect(ip: String, port: String) {//runs on background thread-connecting to server and sending data

        if(this::fgClient.isInitialized && !fgClient.isClosed) {//if socket was already open- close it and reopen
            fgClient.close()
            if(this::out.isInitialized){
                out.close()
            }
        }
        try {//create connection
            Thread.sleep(1000)//so that loading screen does not flash too quickly...
            fgClient=Socket()
            var sa = InetSocketAddress(ip,port.toInt())
            fgClient.connect(sa,4000)
            out = PrintWriter(fgClient.getOutputStream(), true)
        }
        catch (e: Exception){//notify the user
            Handler(Looper.getMainLooper()).post {
                //code that runs in main thread:
                onFailedConnect()
            }
            return
        }
        //if successful connection notify the user
        Handler(Looper.getMainLooper()).post {
            //code that runs in main thread:
            onConnect()//should notify the vm
        }
        //send the data to the server(on a loop)
        sendData()
        //close everything when done sending data
        out.close()
        fgClient.close()
        done=false//for next time
    }

    private fun sendData() {
        while (!done){
            //if aileron was changed by user send the info to the server
            if(updateA){
                out.write("set /controls/flight/aileron $aileron\r\n")
                out.flush()
                updateA=false
            }
            //if elevator was changed by user send the info to the server
            if(updateE){
                out.write("set /controls/flight/elevator $elevator\r\n")
                out.flush()
                updateE=false
            }
            //if rudder was changed by user send the info to the server
            if(updateR){
                out.write("set /controls/flight/rudder $rudder\r\n")
                out.flush()
                updateR=false
            }
            //if throttle was changed by user send the info to the server
            if(updateT){
                out.write("set /controls/engines/current-engine/throttle $throttle\r\n")
                out.flush()
                updateT=false
            }
            //wait so that updates are synchronized with the frames per second of the simulator
            Thread.sleep(100)
        }
    }

    //setters
    fun aileronSetter(a: Float){//update aileron data(make sure background thread would notice)
        aileron = a
        updateA=true
    }
    fun elevatorSetter(e: Float){//update elevator data(make sure background thread would notice)
        elevator = e
        updateE=true
    }
    fun rudderSetter(r : Float){//update rudder data(make sure background thread would notice)
        rudder = r
        updateR=true
    }
    fun throttleSetter(t: Float){//update throttle data(make sure background thread would notice)
        throttle = t
        updateT=true
    }
    //connection notifiers definitions
    fun setConnectEvent(function: () -> Unit) {//notifies the vm when connection is created
        onConnect=function
    }
    fun setFailedConnect(function: () -> Unit) {
        onFailedConnect=function
    }

    fun disconnect(){//stops the thread loop(which then terminates the connection with the server)
        done=true
    }

}