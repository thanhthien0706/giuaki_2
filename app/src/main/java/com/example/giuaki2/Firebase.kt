package com.example.giuaki2

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

class Firebase {
    private val db = FirebaseDatabase.getInstance().getReference()
    private val storage = FirebaseStorage.getInstance().getReference()

    fun setAllStudent(collection: String, list: MutableList<Student2>) {
        val collectRef = db.child(collection)

        for (item in list) {
            val id: String? = collectRef.push().key
            if (id != null) {
                collectRef.child(id).setValue(item)
            }
//            collectRef.setValue(Arrays.asList(item))
        }

    }

    fun getAllTinh(collection: String): DatabaseReference {
        return db.child(collection)
    }

    fun uploadImag(collection: String, filImg: Uri): UploadTask {
        if (filImg != null) {
            val ref = storage.child("/$collection/" + UUID.randomUUID())
            return ref.putFile(filImg)
        }
        return throw IOException()
    }

}