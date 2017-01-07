package party.wank.horsedildo

import android.app.Application
import android.content.Intent

class HorseDildoApplication : Application() {
    val mActivityResultDispatcher: ActivityResultDispatcher = ActivityResultDispatcher()

    fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        mActivityResultDispatcher.onActivityResult(requestCode, resultCode, resultData)
    }
}