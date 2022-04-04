package com.example.giuaki2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {

    private lateinit var imgDTinh: ImageView
    private lateinit var tvNameDTinh: TextView
    private lateinit var tvPopulationDTinh: TextView
    private lateinit var tvDescriptionDTinh: TextView
    private lateinit var btnChangeImgTinh: ImageButton
    val REQUEST_CODE: Int = 100

    private lateinit var filePath: Uri

    private lateinit var studentData: Student2

    private val fb = Firebase()
    private val db = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val bundle: Bundle = intent.extras!!
        if (bundle == null) {
            return
        }

        studentData = bundle.get("object_student") as Student2

        imgDTinh = findViewById(R.id.img_d_student)
        tvNameDTinh = findViewById(R.id.tv_name_d_student)
        tvPopulationDTinh = findViewById(R.id.tv_population_d_student)
        tvDescriptionDTinh = findViewById(R.id.tb_description_d_student)
        btnChangeImgTinh = findViewById(R.id.btn_change_img_student)

        btnChangeImgTinh.setOnClickListener {
            xuLyDoiAnh()
        }


        Glide.with(this).load(studentData.resouerceUrl).into(imgDTinh)
        tvNameDTinh.setText(studentData.name)
        tvPopulationDTinh.setText(studentData.population)
        tvDescriptionDTinh.setText(studentData.description)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete_detail -> xuLyXoaItem()
        }

        return true
    }

    private fun xuLyXoaItem() {
        db.child("tinh").child(studentData.id.toString()).removeValue().addOnSuccessListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putByte("status", 1)

            intent.putExtras(bundle)

            this.startActivity(intent)
        }

    }

    private fun xuLyDoiAnh() {
        openGalleryForImage()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                filePath = data.data!!
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val progressDialog: ProgressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            fb.uploadImag("tinh", filePath).addOnSuccessListener { task ->
                task.storage.downloadUrl.addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        val url = task.result

                        db.child("tinh").child(studentData.id.toString()).child("resouerceUrl")
                            .setValue(url.toString())
                            .addOnSuccessListener {
                                Glide.with(this).load(url).into(imgDTinh)
                            }

                    }
                }
            }

        }
    }

}