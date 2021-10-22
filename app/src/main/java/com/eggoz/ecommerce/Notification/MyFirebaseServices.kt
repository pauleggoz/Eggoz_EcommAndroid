package com.eggoz.ecommerce.Notification

class MyFirebaseServices{

} /*: FirebaseMessagingService() {
    private lateinit var body:String
    private lateinit var title:String
    private lateinit var type:String
    private var itemId =-1


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.size > 0) {
            val data = remoteMessage.data
            Log.d("TESTING", "onMessageReceivedData: $data");
            title = data.get("title") ?:""
            body = data.get("body") ?:""
            type = data.get("click_action") ?:""
            Log.d("NotificationS", "Message data payload: " + remoteMessage.data);
            Log.d("NotificationModel", "onMessageReceived: $title$body$itemId$type");
            val myNotificationManager =  MyNotification(this);
            *//*myNotificationManager.displayNotification(title, body, type,  Intent(this,
                MainActivity::class.java))*//*
            myNotificationManager.displayLargeNotification(title, body, type,  Intent(this,
                MainActivity::class.java))
        }
        if (remoteMessage.notification != null) {
            Log.d("TESTING", "onMessageReceivedNotifi: " + remoteMessage.notification!!.getBody());
            Log.d("TESTING", "onMessageReceivedNotifi: " + remoteMessage.notification!!.getBody());
        }

    }
}*/