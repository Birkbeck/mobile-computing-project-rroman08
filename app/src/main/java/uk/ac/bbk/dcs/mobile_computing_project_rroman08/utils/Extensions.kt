package uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils

import android.content.res.Resources

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
