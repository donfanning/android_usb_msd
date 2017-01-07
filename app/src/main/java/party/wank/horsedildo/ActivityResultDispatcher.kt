package party.wank.horsedildo

import android.content.Intent
import android.util.Log

class ActivityResultDispatcher {
    private val TAG = "ActivityResDispatcher"

    private val mHandlers: MutableMap<Int, ActivityResultHandler> = mutableMapOf()
    private var mCurId = 0

    interface ActivityResultHandler {
        fun onActivityResult(resultCode: Int, resultData: Intent?)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (mHandlers.containsKey(requestCode)) {
            mHandlers[requestCode]?.onActivityResult(resultCode, resultData)
        } else {
            Log.w(TAG, "No handler for request ID $requestCode!")
        }
    }

    fun registerHandler(handler: ActivityResultHandler): Int {
        mHandlers[mCurId] = handler
        return mCurId++
    }

    fun removeHandler(id: Int) {
        mHandlers.remove(id)
    }
}
