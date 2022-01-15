package com.eggoz.ecommerce.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg roomCart: RoomCart)

    @Delete
    suspend fun delete(roomCart: RoomCart)

    @Query("SELECT * FROM cart")
    fun getAll(): LiveData<List<RoomCart>>

    @Query("SELECT * FROM cart")
    fun getAll2(): Flow<List<RoomCart>>?

    @Update
    suspend fun updateCart(vararg roomCart: RoomCart)

    @Query("update cart set quantaty=:qnt, price= :price where id= :mid")
    suspend fun updateCart(mid:Int,qnt:Int,price:String)

    @Query("update cart set quantaty=:qnt where id= :mid")
    suspend fun updateQnt(mid:Int,qnt:Int)


    @Query("SELECT COUNT(*) FROM cart")
    suspend fun Cartsize() :Int

    @Query("DELETE FROM cart")
    suspend fun clearCart()


    @Query("delete from cart where id= :mid")
    suspend fun deletebyid(mid:Int)

    @Query("SELECT quantaty FROM cart where id= :mid")
    suspend fun qntCart(mid:Int) :Int?


    @Query("SELECT quantaty=quantaty+1 FROM cart where id= :mid")
    suspend fun incCart(mid:Int,) :Int


    @Query("SELECT quantaty=quantaty-1 FROM cart where id= :mid")
    suspend fun decCart(mid:Int) :Int

    @Query("Select * from cart")
    fun getCart():List<RoomCart>

    @Query("delete from cart")
    suspend fun deletecart()
}