package com.psr.ibssc_2021.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.psr.ibssc_2021.R

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()


        imageList.add(SlideModel("https://1.bp.blogspot.com/-UcBEqoAMgT8/YNKzbQhtjmI/AAAAAAAAhDY/uWOqWAQJGR0ChqHHhJx25lzs9FZ0ekWHACLcBGAsYHQ/s2547/one.jpg","IBSSC 2021"))
        imageList.add(SlideModel("https://1.bp.blogspot.com/-Fm69SR5yMB0/YNKy1NT3RWI/AAAAAAAAhDQ/NUaDXUbBeWcB977Pntz-98sLwVXnDB9hwCLcBGAsYHQ/s944/conference.jpg","Conference"))
        imageList.add(SlideModel("https://1.bp.blogspot.com/-P2w4MmcLaWY/YNKzhErWHMI/AAAAAAAAhDc/GtknUBrWyBMB_qEv2jpZo5wjyztfiJVGwCLcBGAsYHQ/s678/three.jpg","Gwalior"))
        imageList.add(SlideModel("https://1.bp.blogspot.com/-FEJyDAxQp2g/YNKzkU6KLHI/AAAAAAAAhDg/hssmwcVsVqYUzLNoCCFWAuvdZL70BcMkwCLcBGAsYHQ/s469/two.jpg","Logo"))
        //imageList.add(SlideModel()

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        val track1= findViewById<TextView>(R.id.track1)
        track1.setOnClickListener {
            val expand=findViewById<LinearLayout>(R.id.expandable)
            val card= findViewById<CardView>(R.id.track1_card)
            if(expand.visibility== View.GONE)
            {
                TransitionManager.beginDelayedTransition(card as ViewGroup, AutoTransition())
                card.setCardBackgroundColor(Color.parseColor("#9fff80"))
                expand.visibility= View.VISIBLE
                track1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0)

            }
            else{
                card.setCardBackgroundColor(Color.parseColor("#4dc3ff"))
                expand.visibility= View.GONE
                track1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0)
                TransitionManager.beginDelayedTransition(card as ViewGroup, AutoTransition())

            }
        }
        val track2= findViewById<TextView>(R.id.track2)
        track2.setOnClickListener {
            val expand=findViewById<LinearLayout>(R.id.expandable2)
            val card= findViewById<CardView>(R.id.track2_card)
            if(expand.visibility== View.GONE)
            {
                TransitionManager.beginDelayedTransition(card as ViewGroup, AutoTransition())
                card.setCardBackgroundColor(Color.parseColor("#9fff80"))
                expand.visibility= View.VISIBLE
                track2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0)

            }
            else{
                card.setCardBackgroundColor(Color.parseColor("#4dc3ff"))
                expand.visibility= View.GONE
                track2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0)
                TransitionManager.beginDelayedTransition(card as ViewGroup, AutoTransition())

            }
        }
        val track3= findViewById<TextView>(R.id.track3)
        track3.setOnClickListener {
            val expand=findViewById<LinearLayout>(R.id.expandable3)
            val card= findViewById<CardView>(R.id.track3_card)
            if(expand.visibility== View.GONE)
            {
                TransitionManager.beginDelayedTransition(card as ViewGroup, AutoTransition())
                card.setCardBackgroundColor(Color.parseColor("#9fff80"))
                expand.visibility= View.VISIBLE
                track3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0)

            }
            else{
                card.setCardBackgroundColor(Color.parseColor("#4dc3ff"))
                expand.visibility= View.GONE
                track3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0)
                TransitionManager.beginDelayedTransition(card as ViewGroup, AutoTransition())

            }
        }
        setUpActionBar()
    }
    private fun setUpActionBar(){
        val toolbar= findViewById<Toolbar>(R.id.toolbar_about)
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar!=null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.back_arrow)
            resources.getString(R.string.About).also { actionbar.title = it }
        }
        toolbar.setNavigationOnClickListener(){
            onBackPressed()
        }
    }
}