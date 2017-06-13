package com.taishi.kapp_mvvm.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class Repository : Parcelable {
    var id: Long = 0
    var name: String? = null
    var description: String? = null
    var forks: Int = 0
    var watchers: Int = 0
    @SerializedName("stargazers_count")
    var stars: Int = 0
    var language: String? = null
    var homepage: String? = null
    var owner: User? = null
    var isFork: Boolean = false

    constructor() {}

    fun hasHomepage(): Boolean {
        return homepage != null && !homepage!!.isEmpty()
    }

    fun hasLanguage(): Boolean {
        return language != null && !language!!.isEmpty()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.name)
        dest.writeString(this.description)
        dest.writeInt(this.forks)
        dest.writeInt(this.watchers)
        dest.writeInt(this.stars)
        dest.writeString(this.language)
        dest.writeString(this.homepage)
        dest.writeParcelable(this.owner, 0)
        dest.writeByte(if (isFork) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readLong()
        this.name = `in`.readString()
        this.description = `in`.readString()
        this.forks = `in`.readInt()
        this.watchers = `in`.readInt()
        this.stars = `in`.readInt()
        this.language = `in`.readString()
        this.homepage = `in`.readString()
        this.owner = `in`.readParcelable<User>(User::class.java.classLoader)
        this.isFork = `in`.readByte().toInt() != 0
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as Repository?

        if (id != that!!.id) return false
        if (forks != that.forks) return false
        if (watchers != that.watchers) return false
        if (stars != that.stars) return false
        if (isFork != that.isFork) return false
        if (if (name != null) name != that.name else that.name != null) return false
        if (if (description != null) description != that.description else that.description != null)
            return false
        if (if (language != null) language != that.language else that.language != null)
            return false
        if (if (homepage != null) homepage != that.homepage else that.homepage != null)
            return false
        return !if (owner != null) owner != that.owner else that.owner != null

    }

    override fun hashCode(): Int {
        var result = (id xor id.ushr(32)).toInt()
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (description != null) description!!.hashCode() else 0
        result = 31 * result + forks
        result = 31 * result + watchers
        result = 31 * result + stars
        result = 31 * result + if (language != null) language!!.hashCode() else 0
        result = 31 * result + if (homepage != null) homepage!!.hashCode() else 0
        result = 31 * result + if (owner != null) owner!!.hashCode() else 0
        result = 31 * result + if (isFork) 1 else 0
        return result
    }

    companion object {

        val CREATOR: Parcelable.Creator<Repository> = object : Parcelable.Creator<Repository> {
            override fun createFromParcel(source: Parcel): Repository {
                return Repository(source)
            }

            override fun newArray(size: Int): Array<Repository?> {
                return arrayOfNulls(size)
            }
        }
    }
}
