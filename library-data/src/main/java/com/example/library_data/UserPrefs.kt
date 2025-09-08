package com.example.library_data



import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import com.example.library_data.model.User
import androidx.core.content.edit
import com.example.library_data.constant.USER_INFO_DATA

object UserPrefs {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USER = USER_INFO_DATA

    fun saveUser(context: Context, user: User?) {
        val bytes = marshallParcelable(user)
        val base64Str = Base64.encodeToString(bytes, Base64.DEFAULT)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_USER, base64Str)
            }
    }

    fun getUser(context: Context): User? {
        val base64Str = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER, null) ?: return null

        return try {
            unmarshallParcelable<User>(Base64.decode(base64Str, Base64.DEFAULT))
        } catch (e: Exception) {
            null
        }
    }

    private fun marshallParcelable(parcelable: Parcelable?): ByteArray {
        val parcel = Parcel.obtain()
        return try {
            parcelable?.writeToParcel(parcel, 0)
            parcel.marshall()
        } finally {
            parcel.recycle()
        }
    }

    private inline fun <reified T : Parcelable> unmarshallParcelable(bytes: ByteArray): T? {
        val parcel = Parcel.obtain()
        return try {
            parcel.unmarshall(bytes, 0, bytes.size)
            parcel.setDataPosition(0)
            val creator = T::class.java.getField("CREATOR").get(null) as Parcelable.Creator<T>
            creator.createFromParcel(parcel)
        } finally {
            parcel.recycle()
        }
    }
}