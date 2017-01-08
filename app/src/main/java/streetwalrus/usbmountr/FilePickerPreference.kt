package streetwalrus.usbmountr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.Preference
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.io.File

class FilePickerPreference : Preference, ActivityResultDispatcher.ActivityResultHandler {
    val TAG = "FilePickerPreference"

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
        : super(context, attrs, defStyleAttr, defStyleRes)

    private var mActivityResultId = -1

    init {
        val appContext = context.applicationContext as UsbMountrApplication
        mActivityResultId = appContext.mActivityResultDispatcher.registerHandler(this)
    }

    override fun onCreateView(parent: ViewGroup?): View {
        updateSummary()
        return super.onCreateView(parent)
    }
    override fun onPrepareForRemoval() {
        super.onPrepareForRemoval()

        val appContext = context.applicationContext as UsbMountrApplication
        appContext.mActivityResultDispatcher.removeHandler(mActivityResultId)
    }

    override fun onClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "file/*"

        val activity = context as Activity
        activity.startActivityForResult(intent, mActivityResultId)
    }

    override fun onActivityResult(resultCode: Int, resultData: Intent?) {
        if (resultCode == Activity.RESULT_OK && resultData != null) {
            val path = PathResolver.getPath(context, resultData.data)
            Log.d(TAG, "Picked file $path")
            persistString(path)
            updateSummary()
        }
    }

    private fun updateSummary() {
        val value = getPersistedString("")
        if (value.equals("")) {
            summary = context.getString(R.string.file_picker_nofile)
        } else {
            summary = File(value).name
        }
    }
}