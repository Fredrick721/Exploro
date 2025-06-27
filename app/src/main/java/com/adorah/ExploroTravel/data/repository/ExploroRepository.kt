package com.adorah.ExploroTravel.data.repository


import android.net.Uri
import com.adorah.ExploroTravel.models.ExploroItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface ExploroRepository{
    fun getAllDestination(): Flow<List<ExploroItem>>
    suspend fun getExploroById(id: Int.Companion): ExploroItem?
    suspend fun insertExploro(exploro: ExploroItem)
    suspend fun uploadToFirebase(exploro: ExploroItem)
    suspend fun uploadImagetoFirebase(imageUri: Uri?): String
    suspend fun updateExploroFirebase(exploro: ExploroItem)
    suspend fun deleteExploroFirebase(exploro: ExploroItem)
    fun fetchdestinationFromFirebase(): Flow<List<ExploroItem>>
}
class ExploroRepositoryImpl() :
    ExploroRepository{
    override fun getAllDestination(): Flow<List<ExploroItem>> {
        return getAllDestination()

    }

    override suspend fun getExploroById(id: Int.Companion): ExploroItem? {
        TODO("Not yet implemented")
    }

    override suspend fun insertExploro(exploro: ExploroItem) {
        TODO("Not yet implemented")
    }

    override fun fetchdestinationFromFirebase(): Flow<List<ExploroItem>>
        = callbackFlow {
            val dbref = FirebaseDatabase.getInstance().reference
                .child("destination")
            // create a listener to listen to any change in our child ref.
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val destination = mutableListOf<ExploroItem>()
                    for (child in snapshot.children) {

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            // add the listener to the db reference
            dbref.addValueEventListener(listener)
            // if app is not launched close the connection
            awaitClose { dbref.removeEventListener(listener) }

        }



        override suspend fun uploadToFirebase(exploro: ExploroItem) {
            // firebase db initialization
            val database = FirebaseDatabase.getInstance()
                .reference
            // we target our db by name
            val newExploroRef = database.child("destination")
                .push()
            // generate and capture the unique id from firebase
            val firebaseId = newExploroRef.key ?: return
            val exploroWithId = exploro.copy(firebase_id = firebaseId)
            // then we insert the data to realtime db
            newExploroRef.setValue(exploroWithId)
        }

        // suspend function
        override suspend fun uploadImagetoFirebase(
            imageUri: Uri?
        ): String {
            // storage bucket reference ; initialize
            val storageRef = FirebaseStorage.getInstance()
                .reference
            val imageRef = storageRef.child(
                "exploro_images/${UUID.randomUUID()}.jpg"
            )
            // push image to the folder above
            if (imageUri != null) {
                try {
                    val uploadTask = imageRef.putFile(imageUri).await()
                    return imageRef.downloadUrl.await().toString()
                } catch (e: Exception) {
                    throw Exception(
                        "Failed to upload image " +
                                "${e.message}", e
                    )
                }
            }
            return imageRef.downloadUrl.await().toString()
        }

        override suspend fun updateExploroFirebase(exploro: ExploroItem) {
            val firebaseId = exploro.firebase_id
            if (firebaseId.isNotEmpty()) {
                // here we get a reference to the db in firebase
                val dbref = FirebaseDatabase.getInstance().reference
                    .child("destination").child(firebaseId)
                dbref.setValue(exploro).await()
            } else {
                throw IllegalArgumentException("Firebase id is empty")
            }
        }

        override suspend fun deleteExploroFirebase(exploro: ExploroItem) {
            val firebaseId = exploro.firebase_id
            if (firebaseId.isNotEmpty()) {
                // here we get a reference to the db in firebase
                val dbref = FirebaseDatabase.getInstance().reference
                    .child("destination").child(firebaseId)
                dbref.removeValue().await()
            } else {
                throw IllegalArgumentException("Firebase id is empty")
            }
        }
    }

