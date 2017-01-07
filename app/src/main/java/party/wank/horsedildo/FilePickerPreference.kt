package party.wank.horsedildo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.EditTextPreference
import android.preference.Preference
import android.util.AttributeSet

class FilePickerPreference : EditTextPreference {
    val TAG = "FilePickerPreference"

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
        : super(context, attrs, defStyleAttr, defStyleRes)
}