package com.psr.ibssc_2021.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.icu.util.TimeUnit
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psr.ibssc_2021.R
import com.psr.ibssc_2021.firebase.FireStore
import com.psr.ibssc_2021.models.Announ
import com.psr.ibssc_2021.models.User
import com.psr.ibssc_2021.utils.Constants
import java.net.URI
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.ZoneId
import java.time.temporal.TemporalQueries.zoneId
import java.util.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val Firestore= FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun startCountDown(){
        var count=findViewById<TextView>(R.id.count_down)
        val typeface: Typeface = Typeface.createFromAsset(assets,"digital-7.ttf")
        count.typeface=typeface
        val mycalendar = java.util.Calendar.getInstance()
        val year=mycalendar.get(Calendar.YEAR)
        val month =mycalendar.get(Calendar.MONTH)
        val day= mycalendar.get(Calendar.DAY_OF_MONTH)
        val current_date="$day/${month+1}/$year"
        val selected_date="18/11/2021"
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val Sdate=sdf.parse(selected_date)
        val Cdate=sdf.parse(current_date)
        val time = Clock.systemDefaultZone()
        val result=Sdate.time-Cdate.time-(time.millis()-Cdate.time)
        val countdown = object: CountDownTimer(result, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                count.setText(timeString(millisUntilFinished))
            }

            override fun onFinish() {}
        }.start()
    }
    private fun timeString(millisUntilFinished:Long):String{
        var millisUntilFinished:Long = millisUntilFinished
        val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
        millisUntilFinished -= java.util.concurrent.TimeUnit.DAYS.toMillis(days)

        val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= java.util.concurrent.TimeUnit.HOURS.toMillis(hours)

        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= java.util.concurrent.TimeUnit.MINUTES.toMillis(minutes)

        val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "%02d  :  %02d  :  %02d  :  %02d ",
            days,hours, minutes,seconds
        )
    }

    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
    }
    private fun social(){
        val facebook = findViewById<ImageView>(R.id.facebook)
        val insta = findViewById<ImageView>(R.id.insta)
        val twitter = findViewById<ImageView>(R.id.twitter)
        val linkedin = findViewById<ImageView>(R.id.linkedin)
        val web = findViewById<ImageView>(R.id.web)
        facebook.setOnClickListener {
            openurl("https://www.facebook.com/Ibssc-2021-104151745216897/")

        }
        insta.setOnClickListener {
            openurl("https://www.instagram.com/IBSSC2021/")
        }
        twitter.setOnClickListener {
            openurl("https://twitter.com/ibssc2021")
        }
        linkedin.setOnClickListener {
            openurl("https://www.linkedin.com/in/IBSSC2021/")
        }
        web.setOnClickListener {
            openurl("https://www.ieeebombay.org/ibssc2021/")
        }
    }
    private fun openurl(s:String){
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW,uri))
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpActionBar()
        val nav_view=findViewById<com.google.android.material.navigation.NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)
        //FireStore().loadUser(this)
        startCountDown()
        social()
        val fragment= supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container,HomeFragment()).commit()
        val scrollingText = findViewById<TextView>(R.id.scrollingtext)
        scrollingText.setSelected(true)
            Firestore.collection(Constants.Announcements)
                .document("Scroll")
                .get()
                .addOnSuccessListener {document->
                    val scrollingText = findViewById<TextView>(R.id.scrollingtext)
                   val announ = document.toObject(Announ::class.java)!!
                    scrollingText.text= announ.data
                }

    }

    override fun onBackPressed() {
        val drawerLayout=findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }
    private fun setUpActionBar(){
        val toolbar= findViewById<Toolbar>(R.id.main_activity_toolBar)
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar!=null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.menu)
        }
        toolbar.setNavigationOnClickListener(){
            toggleDrawer()
        }
    }
    private fun toggleDrawer(){
        val drawerLayout=findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /*
            R.id.nav_my_profile->{
                if(isConnected())
                    startActivityForResult(Intent(this, MyProfile::class.java), MY_PROFILE_REQUEST_CODE)
                else
                    showErrorSnackBar("NO INTERNET")
            }
            R.id.nav_sign_out->{
                if(isConnected()){
                AlertDialog.Builder(this)
                    .setTitle("Confirm SignOut?")
                    .setNegativeButton("NO") {dialog, which ->
                        Toast.makeText(this,"SignOut Cancelled!",
                            Toast.LENGTH_SHORT).show()}
                    .setPositiveButton("YES"){dialog,which->
                        FirebaseAuth.getInstance().signOut()
                        val intent= Intent(this, intro::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(intent)
                        finishAffinity()}.show()
                } else
                    showErrorSnackBar("NO INTERNET")
            }*/
            R.id.home->{
                val text= findViewById<TextView>(R.id.head)
                text.text= "Home"
                val fragment= supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container,HomeFragment()).commit()
            }

            R.id.about->{
                startActivity(Intent(this, About::class.java))
            }
            R.id.dates->{
                val text= findViewById<TextView>(R.id.head)
                text.text= "Important Dates"
                val fragment= supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container,DatesFragment()).commit()

            }
            R.id.awards->{
                val text= findViewById<TextView>(R.id.head)
                text.text= "Awards"
                val fragment= supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container,AwardsFragment()).commit()
            }
            R.id.help->{
                val text= findViewById<TextView>(R.id.head)
                text.text= "Help"
                val fragment= supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container,HelpFragment()).commit()

            }
            R.id.callp->{
                startActivity(Intent(this, CallForPapers::class.java))
            }
            R.id.live_c->{
                if(isConnected())
                    startActivity(Intent(this, LiveConference::class.java))
                else
                    showErrorSnackBar("NO INTERNET")

            }

        }
        val drawerLayout=findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavdetails(loggedInUser: User) {
        /*val profile_img=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_img)
        Glide
            .with(this)
            .load(loggedInUser.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(profile_img)
        val username=findViewById<TextView>(R.id.tv_username)
        username.text=loggedInUser.name*/
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE
        ) {
            // Get the user updated details.
            FireStore().loadUser(this@MainActivity)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }


}