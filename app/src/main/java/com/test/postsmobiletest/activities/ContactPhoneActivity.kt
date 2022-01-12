package com.test.postsmobiletest.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.postsmobiletest.R
import com.test.postsmobiletest.adapter.ContactPhoneAdapter

import android.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.test.postsmobiletest.fragments.UpdateContactFragment
import com.test.postsmobiletest.utils.SwipeToDeleteCallback


class ContactPhoneActivity : AppCompatActivity() {
    val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    private lateinit var contactRecyclerView: RecyclerView
    private var adapter: ContactPhoneAdapter? = null
    private var myContacts: MutableList<String> = ArrayList()
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_phone)

        contactRecyclerView = findViewById(R.id.contactRecyclerView)
        frameLayout = findViewById(R.id.frameLayout)

        contactRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contactRecyclerView.setHasFixedSize(true)
        adapter = ContactPhoneAdapter(this, myContacts)
        contactRecyclerView.adapter = adapter

        requestContactPermission()
        itemClickAdapter()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val builder = AlertDialog.Builder(this@ContactPhoneActivity)
                builder.setTitle("Delete")
                builder.setMessage("Are you sure to delete?")
                builder.setIcon(R.drawable.ic_warning)

                builder.setPositiveButton("DELETE"){ _, _ ->
                    adapter!!.removeAt(viewHolder.absoluteAdapterPosition)
                }

                builder.setNegativeButton("CANCEL"){ dialog, _ ->
                    dialog.dismiss()
                }
                builder.create()
                builder.setCancelable(false)
                builder.show()
            }

            }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(contactRecyclerView)
        }

    @SuppressLint("Range")
    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()
                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )

                    if (cursorPhone!!.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            builder.append(", Phone Number: ")
                                .append(
                                    phoneNumValue
                                )
                            myContacts.add("$name \n $phoneNumValue")

                            Log.e("Name ===>", phoneNumValue)
                            adapter!!.setContactList()

                        }
                    }
                    cursorPhone.close()
                }
            }
        } else {
            Toast.makeText(this, "Contact not available", Toast.LENGTH_LONG).show()
        }
        cursor.close()
        return builder
    }



    private  fun itemClickAdapter() {
        adapter!!.onItemClick = {
            contactRecyclerView.visibility = View.GONE
            frameLayout.visibility = View.VISIBLE
            val updateContactFragment = UpdateContactFragment()
            supportFragmentManager.beginTransaction().add(R.id.frameLayout, updateContactFragment).addToBackStack("updateContactFragment").commit()
            getContactToEdit(it)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 0){
            contactRecyclerView.visibility = View.VISIBLE
            frameLayout.visibility = View.GONE
        }else
            supportFragmentManager.popBackStack()
    }

    private fun getContactToEdit(it: String){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val prefsname = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val editorName= prefsname.edit()
        editor.putString("phoneNumValue", it)
        editorName.putString("name", it[0].toString())
        editor.apply()
        editorName.apply()
    }

    private fun requestContactPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Read contacts access needed")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setMessage("Please enable access to contacts.")
                builder.setOnDismissListener(DialogInterface.OnDismissListener {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_CONTACTS
                        ), PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                })
                builder.show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        } else {
          //  adapter!!.setContactList()
           getContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    Toast.makeText(
                        this,
                        "You have disabled a contacts permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

}