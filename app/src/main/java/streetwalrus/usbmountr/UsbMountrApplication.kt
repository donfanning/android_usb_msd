package streetwalrus.usbmountr

import android.app.Application
import android.content.Intent

class UsbMountrApplication : Application() {
    val mActivityResultDispatcher: ActivityResultDispatcher = ActivityResultDispatcher()

    fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        mActivityResultDispatcher.onActivityResult(requestCode, resultCode, resultData)
    }
}