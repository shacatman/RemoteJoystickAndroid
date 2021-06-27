# RemoteJoystickAndroid

The Android Application allows the user to remotely connect to FlightGear simulator and control the plane's movement through UI controls.<br>
The Application is built according to the MVVM principles(Model-ViewModel-View) and written in Kotlin and Xml languages.<br>
The Application was developed in Android Studio environment.<br><br>
In order to run the Application it is required to:
1. download the FlightGear Simulator from https://www.flightgear.org/
2. open the FG simulator and in the settings menu input the following line:
--telnet=socket,in,10,127.0.0.1,6400,tcp
3. after clicking fly make sure to Autostart the plane under the Cessna C172P menu (if in need for better view of the plane press v)
4. run the app(through the Android Studio environment).
5. you can now use the displayed emulator to connect to your ip and port.<br>
\***make sure to use port 6400** - this is where the FG simulator is waiting for connection.<br>
\*if you cannot manage to connect to your computer's ip - this might be due to a firewall issue. (using ip 10.0.2.2 instead should work)
6. take control and fly through the sky!



<br><br>Link for explanation and demonstration of the app: https://youtu.be/Jl-NAYUhHu0
