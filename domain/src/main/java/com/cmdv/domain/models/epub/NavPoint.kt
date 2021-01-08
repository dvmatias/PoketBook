package com.cmdv.domain.models.epub
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/*
 * Navpoint entry in a Table of Contents.
 */
//class NavPoint : Parcelable { TODO
class NavPoint {
    var playOrder: Int
    var navLabel: String? = null
    var content: String? = null

    /*
   * Sometimes the content (resourceName) contains a tag
   * into the HTML.
   */
//    val contentWithoutTag: Uri TODO
//        get() {
//            val indexOf = content!!.indexOf('#')
//            var temp = content
//            if (0 < indexOf) {
//                temp = content!!.substring(0, indexOf)
//            }
//            return Book.resourceName2Url(temp)
//        }

    /*
   * Construct as part of reading from XML
   */
    constructor(playOrder: String) {
        this.playOrder = playOrder.toInt()
    }

    constructor(`in`: Parcel) {
        playOrder = `in`.readInt()
        navLabel = `in`.readString()
        content = `in`.readString()
    }

//    override fun describeContents(): Int { TODo
//        return 0
//    }
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeInt(playOrder)
//        dest.writeString(navLabel)
//        dest.writeString(content)
//    }

    companion object {
//        TODO
//        val CREATOR: Parcelable.Creator<NavPoint> = object : Parcelable.Creator<NavPoint?> {
//            override fun createFromParcel(`in`: Parcel): NavPoint? {
//                return NavPoint(`in`)
//            }
//
//            override fun newArray(size: Int): Array<NavPoint?> {
//                return arrayOfNulls(size)
//            }
//        }
    }
}