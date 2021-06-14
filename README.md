# Container_KioskMode
Creating of an application container in kiosk mode -Android App-

### Run_App
After installing the app , w must make it as device owner app , by the following command :

$adb shell dpm set-device-owner com.onblock.myapp/.controllers.MyDeviceAdminReceiver

u can't unistall the app after desined as device admin App 
so u must remove this conf  by the following command  :
 $ adb shell dpm remove-active-admin com.onblock.myapp/com.onblock.myapp.controllers.MyDeviceAdminReceiver
before unstall app $ adb uninstall com.onblock.myapp

### To Go to admin Home ,
Long press on the screen(or on Back button) to go to the login page 
