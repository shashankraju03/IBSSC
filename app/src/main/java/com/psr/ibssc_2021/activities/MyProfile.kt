package com.psr.ibssc_2021.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.psr.ibssc_2021.R
import com.psr.ibssc_2021.firebase.FireStore
import com.psr.ibssc_2021.models.User
import com.psr.ibssc_2021.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException


class MyProfile : BaseActivity() {
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageURL: String = ""

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setUpActionBar()
        FireStore().loadUser(this)
        val iv_user_image=findViewById<CircleImageView>(R.id.iv_user_image)

        val strProvider = FirebaseAuth.getInstance().getAccessToken(false).result!!.signInProvider
        if (strProvider=="password"){
        iv_user_image.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }}else{
            iv_user_image.setOnClickListener{
                showErrorSnackBar("Current Profile photo is Provided by GoogleAuth, Change in Google Account will reflect here")
            }

        }
        val btn_update=findViewById<Button>(R.id.btn_update)
        btn_update.setOnClickListener {
            if (fvalidate())
                Update()
            else
            {   hideKeyboard()
                showErrorSnackBar("Mobile number cannot be null")}

        }
        val r_pass=findViewById<Button>(R.id.r_pass)
        if (strProvider=="password"){
        r_pass.setOnClickListener{
            Firebase.auth.sendPasswordResetEmail(mUserDetails.email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showErrorSnackBar("Password reset email to registered email address")
                    }
                }
        }
        }
        else{
            r_pass.setOnClickListener{
                showErrorSnackBar("Google Auth Accounts doesn't have a password")
            }
        }
    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun Update() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Update?")
            .setNegativeButton("NO") {dialog, which ->
                Toast.makeText(this,"Update Cancelled!",
                    Toast.LENGTH_SHORT).show()}
            .setPositiveButton("YES"){dialog,which->
                if (mSelectedImageFileUri != null) {
                    uploadUserImage()
                }else {
                    showProgressDialog(this)
                    updateUserProfileData()
                }}.show()
    }

    private fun fvalidate(): Boolean {
        val et_mobile=findViewById<EditText>(R.id.et_mobile)
        return when{
            TextUtils.isEmpty(et_mobile.text.toString())-> { false }
            else -> { true }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun showImageChooser() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            mSelectedImageFileUri = data.data!!
            val iv_user_image=findViewById<CircleImageView>(R.id.iv_user_image)

            try {
                Glide
                    .with(this@MyProfile)
                    .load(Uri.parse(mSelectedImageFileUri.toString()))
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_user_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun setUpActionBar(){
        val toolbar= findViewById<Toolbar>(R.id.toolbar_MyProfile)
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar!=null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.back_arrow)
            resources.getString(R.string.my_profile).also { actionbar.title = it }
        }
        toolbar.setNavigationOnClickListener(){
            onBackPressed()
        }
    }
    private fun uploadUserImage() {

        showProgressDialog(this)

        val ref=FirebaseStorage.getInstance().reference


            val sRef: StorageReference = ref.child(
                getCurrentUserId()
                )

            //adding the file to reference
            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.i(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.i("Downloadable Image URL", uri.toString())
                            mProfileImageURL = uri.toString()
                            updateUserProfileData()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this@MyProfile,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()

                    hideProgressDialog()
                }
        }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
    private fun updateUserProfileData() {

        val userHashMap = HashMap<String, Any>()
        var anyChanges=false

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChanges=true
        }
        val username=findViewById<EditText>(R.id.et_name)
        if (username.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = username.text.toString()
            anyChanges=true
        }
        val et_mobile=findViewById<EditText>(R.id.et_mobile)
        if (et_mobile.text.toString() != mUserDetails.mobile_no.toString()) {
            userHashMap[Constants.MOBILE] = et_mobile.text.toString().toLong()
            anyChanges=true
        }
        if(anyChanges)
        FireStore().updateUserProfileData(this@MyProfile, userHashMap)
        else
            profileUpdateSuccess()
    }

    fun setUserDataInUI(user: User) {
        mUserDetails=user
        val profile_img=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.iv_user_image)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(profile_img)
        val username=findViewById<EditText>(R.id.et_name)
        username.setText(user.name)
        val email=findViewById<EditText>(R.id.et_email)
        email.setText(user.email)
        val mobile=findViewById<EditText>(R.id.et_mobile)
        if (user.mobile_no!=0L)
        mobile.setText(user.mobile_no.toString())

    }
    fun profileUpdateSuccess() {

        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}