package com.lokile.applibraries.utils.media

import com.lokile.applibraries.utils.dateFormat
import com.lokile.applibraries.utils.getFileKey
import com.lokile.applibraries.utils.getLastModifiedAsLong
import java.io.File
import java.util.*

open class MediaFile(
    var id: Int,
    var name: String,
    var path: String,
    var dateTaken: Long?,
) {
    open val dateTakenFormated = dateFormat.format(
        Date(
            dateTaken ?: getLastModifiedAsLong(path)
        )
    )

    open val bucketName by lazy {
        File(path).parentFile?.name.orEmpty()
    }

    fun getSignature(): String = path.getFileKey()
}