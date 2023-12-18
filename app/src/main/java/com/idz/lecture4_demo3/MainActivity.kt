package com.idz.lecture4_demo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.res.Configuration
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val addStudentButton = findViewById<Button>(R.id.btnMainAddStudent)
        val addStudentButton: Button = findViewById(R.id.btnMainAddStudent)
        val findMyDogIconId: ImageView = findViewById(R.id.findMyDogIcon)
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        val imageResourceId = if (isDarkMode) R.drawable.find_my_dog_dm else R.drawable.find_my_dog
        findMyDogIconId.setImageResource(imageResourceId)
        // Option 1
//        class ButtonOnClickListener : View.OnClickListener {
//            override fun onClick(v: View?) {
//
//                val intent = Intent(this@MainActivity, AddStudentActivity::class.java)
//            }
//
//        }

//        val listener = ButtonOnClickListener()
//        addStudentButton.setOnClickListener(ButtonOnClickListener()) //(listener)

//        addStudentButton.setOnClickListener {
//
//        }

        addStudentButton.setOnClickListener(::onAddStudentButtonClicked)
    }

    fun onAddStudentButtonClicked(view: View) {
        val intent = Intent(this, AddStudentActivity::class.java)
        startActivity(intent)
    }
}