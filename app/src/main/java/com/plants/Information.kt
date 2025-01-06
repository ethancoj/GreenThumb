package com.plants

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

@Keep
data class Information(
    @get:Exclude var id: String? = null,
    var scientificName: String = "",
    var name: String = "",
    var description: String = "",
    var longDescription: String = "",
    val timestamp: Timestamp = Timestamp.now()
)