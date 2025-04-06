package com.example.afer_login.dataFetch

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Provider information data class
data class ProviderInfo(
    val providerId: String = "",
    val email: String = "",
    val displayName: String = "",
    val password: String = ""  // Added password field to match database structure
)

// Custom claims data class for user measurements and other details
data class CustomClaims(
    val username: String = "",
    val gender: String = "",
    val height: Int = 0,
    val shoulder: Int = 0,
    val chest: Int = 0,
    val waist: Int = 0,
    val hip: Int = 0,
    val back: Int = 0,
    val hiphigh: Int = 0,
    val armlength: Int = 0,
    val leglength: Int = 0,
    val armthickness: Int = 0,
    val legthickness: Int = 0,
    val avatarname: String = "",
    val imgLink: String = ""
)

// Updated User data class to match Firebase structure
data class User(
    val localId: String = "",
    val email: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val providerUserInfo: List<ProviderInfo> = listOf(),
    val customClaims: CustomClaims = CustomClaims()
)

class UserService {
    // Reference to Firebase database
    private val database = Firebase.database("https://csci3310project-77acc.asia-southeast1.firebasedatabase.app")
    private val usersRef = database.getReference("users")
    
    // Fetch user by display name
    suspend fun getUserByDisplayName(displayName: String): User? {
        return suspendCancellableCoroutine { continuation ->
            usersRef.orderByChild("displayName").equalTo(displayName).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Since we're querying by displayName, we should get at most one result
                        val user = snapshot.children.firstOrNull()?.getValue(User::class.java)
                        continuation.resume(user)
                    } else {
                        continuation.resume(null)
                    }
                }
                
                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }
    
    // Get all users
    suspend fun getAllUsers(): List<User> {
        return suspendCancellableCoroutine { continuation ->
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usersList = mutableListOf<User>()
                    for (userSnapshot in snapshot.children) {
                        userSnapshot.getValue(User::class.java)?.let {
                            usersList.add(it)
                        }
                    }
                    continuation.resume(usersList)
                }
                
                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }
    
    // Update user information
    suspend fun updateUser(userId: String, userUpdates: Map<String, Any>): Boolean {
        return try {
            // Find the user first
            val userQuery = usersRef.orderByChild("localId").equalTo(userId)
            val snapshot = userQuery.get().await()
            
            if (snapshot.exists()) {
                val userKey = snapshot.children.first().key
                userKey?.let {
                    usersRef.child(it).updateChildren(userUpdates).await()
                    return true
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }
    
    // Create new user
    suspend fun createUser(user: User): Boolean {
        return try {
            // Generate a new key for the user
            val newUserRef = usersRef.push()
            newUserRef.setValue(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Update custom claims (measurements)
    suspend fun updateCustomClaims(userId: String, customClaims: CustomClaims): Boolean {
        return try {
            val userQuery = usersRef.orderByChild("localId").equalTo(userId)
            val snapshot = userQuery.get().await()
            
            if (snapshot.exists()) {
                val userKey = snapshot.children.first().key
                userKey?.let {
                    usersRef.child(it).child("customClaims").setValue(customClaims).await()
                    return true
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }
}


