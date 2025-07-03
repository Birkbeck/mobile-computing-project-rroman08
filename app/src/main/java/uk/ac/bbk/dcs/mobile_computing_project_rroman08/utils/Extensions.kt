package uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils

import android.content.res.Resources

/**
 * Extension property to convert an integer value from dp to to actual pixels (px), based on the
 * current screen density.
 */
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
