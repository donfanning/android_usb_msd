package party.wank.horsedildo

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import eu.chainfire.libsuperuser.Shell

class MainActivity : Activity() {
    private val TAG = "MainActivity"

    private var mPrefs: HostPreferenceFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPrefs = fragmentManager.findFragmentById(R.id.prefs) as HostPreferenceFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent) {
        mPrefs!!.onActivityResult(requestCode, resultCode, resultData)
    }

    @Suppress("unused")
    fun onServeClicked(@Suppress("UNUSED_PARAMETER") v: View) {
        object: AsyncTask<Void, Void, Int>() {
            override fun doInBackground(vararg params: Void): Int {
                val usb = "/sys/class/android_usb/android0"

                // Escape the file name to avoid bugs in the shell
                // Could use some finer filters but who cares
                val file = "(.)".toRegex().replace(
                        mPrefs!!.preferenceManager.sharedPreferences
                                .getString(mPrefs!!.SOURCE_KEY, ""),
                        "\\\\$1")

                val ro = if (mPrefs!!.preferenceManager.sharedPreferences
                        .getBoolean(mPrefs!!.RO_KEY, true)) "1" else "0"

                if (!Shell.SU.run(arrayOf(
                        "echo 0 > $usb/enable",
                        // Try to append if the function is not already enabled (by ourselves most likely)
                        "grep mass_storage $usb/functions > /dev/null || sed -e 's/$/,mass_storage/' $usb/functions | cat > $usb/functions",
                        // If empty, set ourselves as the only function
                        "[[ -z $(cat $usb/functions) ]] && echo mass_storage > $usb/functions",
                        "echo disk > $usb/f_mass_storage/luns",
                        "echo 1 > $usb/enable",
                        "echo > $usb/f_mass_storage/lun0/file",
                        "echo $ro > $usb/f_mass_storage/lun0/ro",
                        "echo $file > $usb/f_mass_storage/lun0/file",
                        "echo success"
                )).isEmpty()) {
                    return 0
                } else {
                    return 1
                }
            }

            override fun onPostExecute(result: Int?) {
                val str = if (result == 0) R.string.host_success else R.string.host_noroot
                Toast.makeText(applicationContext, getString(str), Toast.LENGTH_SHORT).show()
            }
        }.execute()
    }
}
