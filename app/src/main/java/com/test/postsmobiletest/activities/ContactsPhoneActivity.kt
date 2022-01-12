package com.test.postsmobiletest.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.test.postsmobiletest.R
import contacts.core.Contacts

class ContactsPhoneActivity : AppCompatActivity() {

    val contacts = Contacts(this).query().find()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_phone)

        Toast.makeText(this, "" +contacts, Toast.LENGTH_LONG).show()
    }

}