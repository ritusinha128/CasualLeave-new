package com.example.casualleaveapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.casualleaveapp.model.Class
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main1.*
import org.apache.commons.io.IOUtils
import java.util.*

class teacherPage : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        val appleave = findViewById<Button>(R.id.applyleave)
        val appTime = findViewById<Button>(R.id.myTIME)
        val user = FirebaseAuth.getInstance().currentUser;
        appleave.setOnClickListener {
            // Get token
// [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w("RITUUUUU", "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }
                        // Get new Instance ID token
                        val token = task.result!!.token
                        //val report = Report(rep, token)
                        user?.let{val uid = user.uid;
                        write_to_db(token,uid)}
                        // Log and toast
                        val msg = getString(R.string.msg_token_fmt, token)
                        Log.d("RITUUUU", msg)
                        Toast.makeText(this@teacherPage, msg, Toast.LENGTH_SHORT).show()
                    })
            startActivity(Intent(this@teacherPage, composeMsg::class.java)) }
        val timetable = Gson().fromJson<ArrayList<Class>>(
                IOUtils.toString(assets.open("timetable.json"), "UTF-8"),
                object : TypeToken<ArrayList<Class>>() {}.type
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }
        if (intent.extras != null) {
            for (key in intent.extras.keySet()) {
                val value = intent.extras[key]
                Log.d("RITUUUUUU", "Key: $key Value: $value")
            }
        }
        if (timetable != null && timetable.isNotEmpty()) {
            pager.adapter = DaysPagerAdapter(timetable, supportFragmentManager)
            tabs.setupWithViewPager(pager)

            // Normalize day value - our adapter works with five days, the first day (0) being Monday.
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2
            pager.currentItem = if (today in 0..4) today else 0
        }
    }

    fun write_to_db(token: String, uid:String) {
        val hash: MutableMap<String, Any> = HashMap()
        hash["token"] = token
        db.collection("Teachers").document(uid)
                .set(hash, SetOptions.merge())
                .addOnSuccessListener { Log.d("RITUUU", "DocumentSnapshot successfully written!") }
                .addOnFailureListener(OnFailureListener { e -> Log.w("RITUUUU", "Error adding document", e) })
        return
    }
}
