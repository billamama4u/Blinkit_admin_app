package com.tarun.admin_blinkit.Models



data class CartProducts (


    var id:String="random" ,

    var productTitle:String?="",
    var productQuantity:String?="",
    var productPrice:String?="",
    var productCount:Int? = 0,
    var productCategory:String?="",
//    var productType:String?="",
    var adminUid:String?="",
    var productImage:String?="",
    var productStock:Int=0

)
{}