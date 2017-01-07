package party.wank.horsedildo

import android.os.Bundle
import android.preference.PreferenceFragment

class HostPreferenceFragment : PreferenceFragment() {
    private val TAG = "HostPreferenceFragment"

    val SOURCE_KEY = "host_source_file"
    val RO_KEY = "host_ro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.host_preferences)
    }
}