package com.test.postsmobiletest.fragments

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.test.postsmobiletest.R
import android.provider.ContactsContract

import android.provider.ContactsContract.CommonDataKinds.Phone

import android.content.ContentProviderOperation
import android.content.Context

import android.content.OperationApplicationException
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.RemoteException
import android.preference.PreferenceManager
import android.provider.CallLog.CONTENT_URI
import android.provider.CallLog.Calls.CONTENT_URI
import android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.github.pavlospt.roundedletterview.RoundedLetterView
import java.security.Permissions


class UpdateContactFragment: Fragment() {
    private lateinit var txtName: RoundedLetterView
    private lateinit var txtPh: EditText
    private lateinit var btnUpdate: Button


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_edit_contact, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtName= view.findViewById(R.id.r_letter)
        txtPh= view.findViewById(R.id.txtNPhNo)
        btnUpdate = view.findViewById(R.id.btnUpdate)

      val name =  getPreferences("name", requireContext())
       val phoneNumber = getPreferences("phoneNumValue", requireContext())

        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )

        if (!hasPermissions(requireContext(), PERMISSIONS.toString())) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)
        }

        txtName.titleText = name
        txtPh.text = Editable.Factory.getInstance().newEditable(phoneNumber)

        btnUpdate.setOnClickListener {
            updateContact(txtPh.toString(), txtPh.toString(), txtPh.toString())
        }

    }

    @Throws(RemoteException::class, OperationApplicationException::class)
    fun updateContact(contactId: String?, newNumber: String?, phoneType: String?) {
        //ASSERT: @contactId alreay has a work phone number
        val ops = ArrayList<ContentProviderOperation>()
        val selectPhone: String = ContactsContract.Data.DISPLAY_NAME + "=? AND " + ContactsContract.Data.MIMETYPE + "='" +
                Phone.CONTENT_ITEM_TYPE + "'" + " AND " + Phone.TYPE + "=?"
        val phoneArgs = arrayOf(contactId, phoneType)
        ops.add(
            ContentProviderOperation.newUpdate(ContactsContract.Contacts.CONTENT_URI)
                .withSelection(selectPhone, phoneArgs)
                .withValue(Phone.NUMBER, newNumber)
                .build()
        )
        activity?.contentResolver?.applyBatch(ContactsContract.AUTHORITY, ops)

    }

    private fun getPreferences(key: String, context: Context): String {
       val prefs = PreferenceManager.getDefaultSharedPreferences(context)
       return prefs.getString(key, null)!!
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all{
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
