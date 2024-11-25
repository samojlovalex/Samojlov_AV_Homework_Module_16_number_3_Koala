@file:Suppress("DEPRECATION")

package com.example.samojlov_av_homework_module_16_number_3_koala.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_3_koala.databinding.FragmentPhoneBookBinding
import com.example.samojlov_av_homework_module_16_number_3_koala.models.PhoneBook
import com.example.samojlov_av_homework_module_16_number_3_koala.utils.PhoneBookAdapter
import android.Manifest
import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.samojlov_av_homework_module_16_number_3_koala.MainActivity
import com.example.samojlov_av_homework_module_16_number_3_koala.R




class PhoneBookFragment : Fragment() {

    private lateinit var binding: FragmentPhoneBookBinding
    private lateinit var contactListRV: RecyclerView
    private lateinit var toolbarPhoneBook: androidx.appcompat.widget.Toolbar

    private var adapter: PhoneBookAdapter? = null
    private var contactsModelList: MutableList<PhoneBook>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPhoneBookBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        initToolbar()

        contactListRV = binding.contactListRV
        contactListRV.layoutManager = LinearLayoutManager(context)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionContact.launch(Manifest.permission.READ_CONTACTS)
        } else {
            getContact()
        }
    }

    private fun initToolbar() {
        toolbarPhoneBook = binding.toolbarPhoneBook
        toolbarPhoneBook.inflateMenu(R.menu.menu)
        toolbarPhoneBook.setTitle(R.string.toolbar_title)
        toolbarPhoneBook.setSubtitle(R.string.toolbar_subtitle)
        toolbarPhoneBook.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.exitMenu -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_exit),
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.finish()
                }
            }
            return@setOnMenuItemClickListener super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("Range")
    private fun getContact() {
        contactsModelList = ArrayList()
        val phones = requireActivity().contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactModel = PhoneBook(name, phoneNumber)
            contactsModelList?.add(contactModel)
        }
        phones.close()
        if (contactsModelList != null) {
            adapter = PhoneBookAdapter(activity, contactsModelList!!)
        }
        contactListRV.adapter = adapter
        contactListRV.setHasFixedSize(true)
        adapter!!.setOnPhoneBookClickListener(object : PhoneBookAdapter.OnPhoneBookClickListener {
            override fun onPhoneClick(phoneBook: PhoneBook, position: Int) {
                Toast.makeText(context, phoneBook.name, Toast.LENGTH_LONG).show()
            }

        })
        adapter!!.setSmsDataListener(object : PhoneBookAdapter.SmsDataListener {
            override fun onData(data: String) {
                (activity as MainActivity).sendDataToSMS(data)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.containerFragmentMainActivityFCV, SMSFragment())
                    ?.remove(this@PhoneBookFragment)
                    ?.commit()
            }

        })
    }

    private val permissionContact = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(
                context,
                getString(R.string.permissionContact_true_Toast), Toast.LENGTH_LONG
            ).show()
            getContact()
        } else {
            Toast.makeText(context, R.string.permission_call_phone_else_toast, Toast.LENGTH_LONG)
                .show()
        }
    }

}