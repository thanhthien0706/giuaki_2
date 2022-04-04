package com.example.giuaki2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var rcvTinh: RecyclerView
    private lateinit var layoutMain: RelativeLayout
    private lateinit var studentAdapter: StudentAdapter

    private var listData = mutableListOf<Student2>()

    private val fb = Firebase()
    private val db = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rcvTinh = findViewById(R.id.rcv_student)
        layoutMain = findViewById(R.id.layout_main)

//         setDataTinh()

//        fb.setAllStudent(
//            "student",
//            mutableListOf(
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_1.jpeg?alt=media&token=0ba0bdaa-e91d-484f-8c34-ec619bd55cb4",
//                    "Sinh vien 1",
//                    "2002",
//                ),
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_2.jpg?alt=media&token=fcb7b73f-cac4-4fa2-8e4b-c69a05ed9a47",
//                    "Sinh vien 2",
//                    "2002",
//                ),
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_3.jpg?alt=media&token=4332c31d-46ac-4aa3-b7e2-3d8e29fe3c21",
//                    "Sinh vien 3",
//                    "2002",
//                ),
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_4.jpg?alt=media&token=34213709-8a67-44f3-9c24-89659a97ee93",
//                    "Sinh vien 4",
//                    "2002",
//                ),
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_5.jpg?alt=media&token=bdd7793a-f4ab-455a-b01b-b7d8c6022f57",
//                    "Sinh vien 5",
//                    "2002",
//                ),
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_6.jpg?alt=media&token=26c1f03d-7794-4ab4-adf8-bb6b025ad63a",
//                    "Sinh vien 6",
//                    "2002",
//                ),
//                Student2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/student%2Fsv_7.jpeg?alt=media&token=4791ca34-f179-4934-ae1f-930510ff475b",
//                    "Sinh vien 7",
//                    "2002",
//                ),
//            )
//        )

        rcvTinh.layoutManager = LinearLayoutManager(this)

        setDataTinh()

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
//                    listData.removeAt(position)
//                    tinhAdapter.notifyDataSetChanged()

                    val tinh = listData[position]
                    db.child("tinh").child(tinh.id.toString()).removeValue().addOnSuccessListener {
                        listData.removeAt(position)
                        studentAdapter.notifyDataSetChanged()
//                        setDataTinh()
                    }

                }

            }

        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rcvTinh)

    }

    override fun onResume() {
        super.onResume()

        val bundle: Bundle? = intent.extras
        if (bundle == null) {
            return
        }

        val status = bundle.get("status")

        if (status == 1) {
            studentAdapter.notifyDataSetChanged()
        } else {
            return
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_giua_ky, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_change_color -> {
                val rnd: Random = Random
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                layoutMain.setBackgroundColor(color)
            }
            R.id.item_about -> Toast.makeText(
                this,
                "Ứng dụng xem thông tin student",
                Toast.LENGTH_LONG
            ).show()
        }
        return true
    }

    private fun setDataTinh() {

        fb.getAllTinh("student").get().addOnSuccessListener {
            for (item in it.children) {
                var student2: Student2 = Student2()
                val product = item.getValue(Student2::class.java)
                student2.id = item.key
                student2.resouerceUrl = product?.resouerceUrl.toString()
                student2.name = product?.name.toString()
                student2.population = product?.population.toString()

//                    if (product != null) {
                listData.add(student2)
//                    }
            }

            studentAdapter = StudentAdapter(this@MainActivity, listData)
            rcvTinh.adapter = studentAdapter
        }
    }
}
