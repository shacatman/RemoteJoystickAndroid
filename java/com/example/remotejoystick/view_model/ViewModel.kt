package com.example.remotejoystick.view_model



import com.example.remotejoystick.model.FGPlayer

class ViewModel {
     var model = FGPlayer()
     lateinit var t: Thread
     var onConnect={}//default
     var onFailedConnect ={}//default behaviour(overridden in the view to notify user when connection fails)
    fun connect(ip: String, port: String) {
        if (this::t.isInitialized) {
            if (t.isAlive) {
                //end previous thread if such exists
                disconnect()
            }
        }
        //connect to the server on background thread
        t = Thread{ model.connect(ip, port)}
        t.start()

    }

    //data setters
    fun aileronSetter(a: Float){
        model.aileronSetter(a)
    }
    fun elevatorSetter(e: Float){
        model.elevatorSetter(e)
    }
    fun rudderSetter(r : Float){
        model.rudderSetter(r)
    }
    fun throttleSetter(t: Float){
        model.throttleSetter(t)
    }

    //set connection events
    fun setConnectEvent(function: () -> Unit) {//invoked whenever model is connected
        onConnect=function//notify the view
        model.setConnectEvent(onConnect)
    }
    fun setFailedConnect(function: () -> Unit) {//invoked whenever model fails to connect
        onFailedConnect=function//notify the view
        model.setFailedConnect(onFailedConnect)
    }

    fun disconnect() {//disconnect from server(closes the background thread)
        model.disconnect()
        t.join(200)//model should finish within that time limit(out of the loop which then exits the thread)
    }

}