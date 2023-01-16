package com.psr.ibssc_2021.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.psr.ibssc_2021.R

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_help, container, false)
        val map=view.findViewById<ImageView>(R.id.map)
        map.setOnClickListener {
            openurl("https://www.google.co.in/maps/place/Indian+Institute+of+Information+Technology+and+Management+Gwalior/@26.2494569,78.1719501,17z/data=!3m1!4b1!4m5!3m4!1s0x3976c6e5d32a4d53:0xf834069adc0c9b89!8m2!3d26.2494521!4d78.1741388?shorturl=1")

        }
        val help= view.findViewById<FloatingActionButton>(R.id.fab)
        help.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data=Uri.parse("mailto:")
            val list = arrayOf("sriyascarlet@gmail.com","shashankrajuprathikantam@gmail.com")
            val sub:String="Bug Report"
            val pre_text:String="Please state your problem regarding the app.."
            intent.putExtra(Intent.EXTRA_EMAIL, list)
            intent.putExtra(Intent.EXTRA_SUBJECT,sub )
            intent.putExtra(Intent.EXTRA_TEXT, pre_text)
                startActivity(intent)
        }
        val help2= view.findViewById<FloatingActionButton>(R.id.fab2)

        help2.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data=Uri.parse("mailto:")
            val list = arrayOf("ibssc@ieeebombay.org")
            val sub:String="Please state your subject"
            val pre_text:String="Please state your need"
            intent.putExtra(Intent.EXTRA_EMAIL, list)
            intent.putExtra(Intent.EXTRA_SUBJECT,sub )
            intent.putExtra(Intent.EXTRA_TEXT, pre_text)
            startActivity(intent)
        }
        val help3= view.findViewById<FloatingActionButton>(R.id.fab3)
        help3.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data=Uri.parse("tel: +917009594389")
            startActivity(intent)
        }
        val help4= view.findViewById<FloatingActionButton>(R.id.fab4)
        help4.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data=Uri.parse("tel: +919569955745")
            startActivity(intent)
        }
        return view
    }
    private fun openurl(s:String){
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW,uri))
    }

}