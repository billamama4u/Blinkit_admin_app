package com.tarun.admin_blinkit.ViewModel


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.tarun.admin_blinkit.Models.CartProducts
import com.tarun.admin_blinkit.Models.NotificationData
import com.tarun.admin_blinkit.Models.Product
import com.tarun.admin_blinkit.Models.order
import com.tarun.admin_blinkit.R
import com.tarun.admin_blinkit.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class Dataupload: ViewModel() {

    private val _isImageUploaded= MutableStateFlow(false)
    val isImageUploaded= _isImageUploaded

    private val _downloadedImageUrl= MutableStateFlow<ArrayList<String?>>(arrayListOf())

    val downloadedurl=_downloadedImageUrl

    private val _isProductUploaded= MutableStateFlow(false)
    val isProductUploaded= _isProductUploaded

    fun saveimage(imageUri:ArrayList<Uri>) {
        val downloadeduri = ArrayList<String?>()

        imageUri.forEach {

        val imageref =
            FirebaseStorage.getInstance().reference.child(Utils.getauthinstance().currentUser?.uid.toString()).child("Images").child(UUID.randomUUID().toString())

        imageref.putFile(it).continueWithTask {
            imageref.downloadUrl
        }.addOnCompleteListener{
            val url=it.result
            downloadeduri.add(url.toString())
            if(downloadeduri.size==imageUri.size){
                _isImageUploaded.value=true
                _downloadedImageUrl.value=downloadeduri
            }
        }
    }
    }

    fun savedata(product: Product){
        FirebaseDatabase.getInstance().getReference("AllAdmin").child("AllProducts").child(product.id.toString()).setValue(product).addOnSuccessListener {
        FirebaseDatabase.getInstance().getReference("AllAdmin").child("ProductCategory/${product.productCategory}/${product.id}").setValue(product).addOnSuccessListener {
            FirebaseDatabase.getInstance().getReference("AllAdmin")
                .child("ProductType/${product.productType}/${product.id}").setValue(product)
                .addOnSuccessListener {

                    _isProductUploaded.value = true
                }


        }
        }
    }

    fun getAllProducts(title: String?): LiveData<List<Product>> {
        val productsLiveData = MutableLiveData<List<Product>>()
        val db = FirebaseDatabase.getInstance().getReference("AllAdmin").child("AllProducts")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val prod = productSnapshot.getValue(Product::class.java)
                    if(title=="All" || prod?.productCategory==title){
                    prod?.let { products.add(it)
                    }
                    }
                }
                productsLiveData.value = products
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return productsLiveData
    }

    fun saveupdatedproduct(product: Product, context: Context) {
        FirebaseDatabase.getInstance().getReference("AllAdmin").child("AllProducts").child(product.id.toString()).setValue(product)
            .addOnSuccessListener {
            FirebaseDatabase.getInstance().getReference("AllAdmin")
                .child("ProductCategory/${product.productCategory}/${product.id}")
                .setValue(product).addOnSuccessListener {

                FirebaseDatabase.getInstance().getReference("AllAdmin")
                    .child("ProductType/${product.productType}/${product.id}").setValue(product)
                    .addOnSuccessListener {
                        Utils.showToast(context, "Your Product is updated....")
                    }


            }
        }
    }

    fun getOrderProduct(orderId: String): LiveData<List<CartProducts>> {
        val productsLiveData = MutableLiveData<List<CartProducts>>()
        val db = FirebaseDatabase.getInstance()
            .getReference("AllUsers").child("Orders").child(orderId).child("list")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<CartProducts>()
                for (productSnapshot in snapshot.children) {
                    try {
                        val prod = productSnapshot.getValue(CartProducts::class.java)
                        prod?.let { products.add(it) }
                    } catch (e: Exception) {
                        // Log the problematic snapshot for debugging
                        Log.e("FirebaseDataError", "Error converting product snapshot: ${productSnapshot.value}", e)
                    }
                }
                productsLiveData.value = products
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("FirebaseError", "Error fetching data: ${error.message}")
            }
        })

        return productsLiveData
    }

    fun setstatus(orderId: String,status:Int,context:Context){

        val db = FirebaseDatabase.getInstance()
            .getReference("AllUsers").child("Orders").child(orderId).child("itemStatus").setValue(status).addOnSuccessListener{
            }

    }

    fun getAllOrders(): LiveData<List<order>> {
        val ordersLiveData = MutableLiveData<List<order>>()
        val db = FirebaseDatabase.getInstance().getReference("AllUsers").child("Orders").orderByChild("itemStatus")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ordersList = mutableListOf<order>()
                for (productSnapshot in snapshot.children) {
                    val singleOrder = productSnapshot.getValue(order::class.java)
                    singleOrder?.let { ordersList.add(it) }
                }
                ordersLiveData.value = ordersList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return ordersLiveData
    }

    suspend fun fetchStatus(orderId: String): Int? {
        return try {
            val snapshot = FirebaseDatabase.getInstance()
                .getReference("AllUsers")
                .child("Orders")
                .child(orderId)
                .child("itemStatus")
                .get()
                .await()
            snapshot.getValue(Int::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


  //  fun getNotification(context: Context) {
//        val database = FirebaseDatabase.getInstance().getReference("Notifications")
//
//        database.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                Utils.showToast(context, "New child added: ${snapshot.key}")
//                val notification = snapshot.getValue(NotificationData::class.java)
//                notification?.let {
//                    sendNotification(context, it.heading, it.message)
//                }
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onChildRemoved(snapshot: DataSnapshot) {}
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onCancelled(error: DatabaseError) {
//                Utils.showToast(context, "Error: ${error.message}")
//            }
//        })
//    }



        //    fun sendNotification(context: Context, title: String?, messageBody: String?) {
//        Utils.showToast(context, "Sending notification with title: $title and message: $messageBody")
//        val notificationBuilder = NotificationCompat.Builder(context, "default_channel_id")
//            .setSmallIcon(R.drawable.baseline_notifications_24)
//            .setContentTitle(title)
//            .setContentText(messageBody)
//            .setAutoCancel(true)
//
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create a notification channel if necessary (Android O and above)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "default_channel_id",
//                "Default Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(0, notificationBuilder.build())
//    }
//
//
//




}