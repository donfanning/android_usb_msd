package party.wank.horsedildo

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.XmlResourceParser
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import java.io.InputStreamReader

class LicenseActivity : Activity() {
    private val TAG = "LicenseActivity"

    private var prefLayout = -1

    private var mList: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licenses)

        // HAX
        prefLayout = Preference(this).layoutResource

        val licenseList: MutableList<License> = mutableListOf()
        val xrp = resources.getXml(R.xml.licenses)
        while (xrp.eventType != XmlResourceParser.END_DOCUMENT) {
            xrp.next()
            if (xrp.eventType == XmlResourceParser.START_TAG && xrp.name == "license") {
                licenseList.add(License(xrp))
            }
        }

        mList = findViewById(R.id.license_list) as ListView
        val licensesAdapter = LicenseArrayAdapter(licenseList)
        mList!!.adapter = licensesAdapter
    }

    private inner class License(xrp: XmlResourceParser) {
        val name: String = xrp.getAttributeValue(null, "name")
        val type: String? = xrp.getAttributeValue(null, "type")
        val file: String? = xrp.getAttributeValue(null, "file")
        val url: String? = xrp.getAttributeValue(null, "url")

        var view: View? = null
        fun getView(parent: ViewGroup): View? {
            if (view == null) {
                // MOAR HAX
                // Abuse the Preference layout whose ID was retrieved earlier to create our View
                view = layoutInflater.inflate(prefLayout, parent, false)
                if (android.os.Build.VERSION.SDK_INT >= 24)
                    (view!!.findViewById(android.R.id.icon_frame) as View).visibility = View.GONE
                (view!!.findViewById(android.R.id.icon) as ImageView).visibility = View.GONE
                (view!!.findViewById(android.R.id.title) as TextView).text = name
                val summary = view!!.findViewById(android.R.id.summary) as TextView
                if (type != null)
                    summary.text = type
                else
                    summary.visibility = View.GONE
                (view!!.findViewById(android.R.id.widget_frame) as ViewGroup).visibility = View.GONE

                view!!.setOnClickListener {
                    val licenseTextLayout = layoutInflater.inflate(R.layout.dialog_license, null, false)
                    val licenseTextView = licenseTextLayout.findViewById(R.id.textView) as TextView
                    licenseTextView.text = InputStreamReader(assets.open("licenses/$file")).readText()
                    licenseTextView.movementMethod = ScrollingMovementMethod()
                    AlertDialog.Builder(this@LicenseActivity)
                            .setTitle(name)
                            .setView(licenseTextLayout)
                            .setPositiveButton(R.string.licenses_upstream, { dialog, which ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            })
                            .show()
                }
            }
            return view
        }
    }

    private inner class LicenseArrayAdapter(licenses: List<License>)
        : ArrayAdapter<License>(this, 0, licenses) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            return getItem(position).getView(parent)
        }
    }
}
