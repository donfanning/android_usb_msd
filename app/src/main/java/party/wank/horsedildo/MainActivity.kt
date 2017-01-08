package party.wank.horsedildo

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_licenses -> {
                val intent = Intent(this, LicenseActivity::class.java)
                startActivity(intent)
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        val appContext = applicationContext as HorseDildoApplication
        appContext.onActivityResult(requestCode, resultCode, resultData)
    }

    @Suppress("unused")
    fun onServeClicked(@Suppress("UNUSED_PARAMETER") v: View) {
        // Escape the file name to avoid bugs in the shell
        // Could use some finer filters but who cares
        val file = "(.)".toRegex().replace(
                mPrefs!!.preferenceManager.sharedPreferences
                        .getString(mPrefs!!.SOURCE_KEY, ""),
                "\\\\$1")

        val ro = if (mPrefs!!.preferenceManager.sharedPreferences
                .getBoolean(mPrefs!!.RO_KEY, true)) "1" else "0"

        UsbScript().execute(file, ro, "1")
    }

    @Suppress("unused")
    fun onDisableClicked(@Suppress("UNUSED_PARAMETER") v: View) {
        UsbScript().execute("", "1", "0")
    }

    inner class UsbScript : AsyncTask<String, Void, Int>() {
        override fun doInBackground(vararg params: String): Int {
            val usb = "/sys/class/android_usb/android0"
            val file = params[0]
            val ro = params[1]
            val enable = params[2]

            if (!Shell.SU.run(arrayOf(
                    "echo 0 > $usb/enable",
                    // Try to append if the function is not already enabled (by ourselves most likely)
                    "grep mass_storage $usb/functions > /dev/null || sed -e 's/$/,mass_storage/' $usb/functions | cat > $usb/functions",
                    // If empty, set ourselves as the only function
                    "[[ -z $(cat $usb/functions) ]] && echo mass_storage > $usb/functions",
                    // Disable the feature if told to
                    "[[ 0 == $enable ]] && sed -e 's/mass_storage//' $usb/functions | cat > $usb/functions",
                    "echo disk > $usb/f_mass_storage/luns",
                    "echo 1 > $usb/enable",
                    "echo > $usb/f_mass_storage/lun0/file",
                    "echo $ro > $usb/f_mass_storage/lun0/ro",
                    "echo $file > $usb/f_mass_storage/lun0/file",
                    // Older kernels only support a single lun, cope with it
                    "echo > $usb/f_mass_storage/lun/file",
                    "echo $ro > $usb/f_mass_storage/lun/ro",
                    "echo $file > $usb/f_mass_storage/lun/file",
                    "echo success"
            )).isEmpty()) {
                if (enable != "0") {
                    return R.string.host_success
                } else {
                    return R.string.host_disable_success
                }
            } else {
                return R.string.host_noroot
            }
        }

        override fun onPostExecute(result: Int) {
            Toast.makeText(applicationContext, getString(result), Toast.LENGTH_SHORT).show()
        }
    }
}
