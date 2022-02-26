package com.eggoz.ecommerce.localdata.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class RoomCart(
    @PrimaryKey(autoGenerate = true)
    val id:Int ,
    val name:String?,
    val img:String?,
    val price:String?,
    val quantaty:Int?,
    val des:String?,
    val status:Boolean?,
    val sKUCount:Int?
) {


}