package com.example.quizwiz.models

import android.os.Parcel
import android.os.Parcelable

data class ResultModel(
    var time:Int,
    var type: String?,
    var difficulty: String?,
    var score:Double
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(time)
        parcel.writeString(type)
        parcel.writeString(difficulty)
        parcel.writeDouble(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultModel> {
        override fun createFromParcel(parcel: Parcel): ResultModel {
            return ResultModel(parcel)
        }

        override fun newArray(size: Int): Array<ResultModel?> {
            return arrayOfNulls(size)
        }
    }
}
