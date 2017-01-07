package party.wank.horsedildo

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.util.Log

class HostPreferenceFragment : PreferenceFragment() {
    private val TAG = "HostPreferenceFragment"

    val SOURCE_KEY = "host_source_file"
    val RO_KEY = "host_ro"
    private var mSourcePreference: FilePickerPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.host_preferences)

        mSourcePreference = preferenceScreen.findPreference(SOURCE_KEY) as? FilePickerPreference
        val sourceValue = preferenceManager.sharedPreferences.getString(SOURCE_KEY, "")
        mSourcePreference!!.summary = sourceSummary(sourceValue)
    }

    private fun sourceSummary(value: String): String {
        if (value.equals("")) {
            return "" //getString(R.string.host_source_none)
        } else {
            return value
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent) {
        super.onActivityResult(requestCode, resultCode, resultData)
        Log.d(TAG, "Result: " + resultData.data)
    }
}