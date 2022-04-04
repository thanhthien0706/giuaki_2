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
//            "tinh",
//            mutableListOf(
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/khanh_hoa.jpg?alt=media&token=e48a36ae-5c87-49b1-b23f-09bbd9878804",
//                    "Khánh Hòa",
//                    "1.246.358",
//                    "Tinh nay rat la dep"
//                ),
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/hue.jpg?alt=media&token=abd1959c-83bc-4245-b236-f3f336883c00",
//                    "Huế",
//                    "1.137.045",
//                    "Tinh nay rat la dep"
//                ),
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/ca_mau.jpg?alt=media&token=596f936c-86f7-46f3-a6a7-b5734e80a3fd",
//                    "Cà Mau",
//                    "1.191.999",
//                    "Tinh nay rat la dep"
//                ),
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/cao_bang.jpg?alt=media&token=f54c9b82-67cc-4259-8935-d8d81844d71f",
//                    "Cao Bằng",
//                    "1.246.358",
//                    "Tinh nay rat la dep"
//                ),
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/ha_noi.jpg?alt=media&token=94e9a884-f67a-4891-8db3-bda85c93b01d",
//                    "Hà Nội",
//                    "1.246.358",
//                    "Tinh nay rat la dep"
//                ),
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/quang_tri.jpg?alt=media&token=0dcb1e81-34da-44c5-b1e4-f6f0835a4d11",
//                    "Quảng Trị",
//                    "1.246.358",
//                    "Tinh nay rat la dep"
//                ),
//                Tinh2(
//                    null,
//                    "https://firebasestorage.googleapis.com/v0/b/giua-ky-7d827.appspot.com/o/tp_hcm.jpg?alt=media&token=ebdd09b3-1029-478a-9dd1-d3c73563d070",
//                    "Hồ Chí Minh",
//                    "1.246.358",
//                    "Tinh nay rat la dep"
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
                "Ứng dụng xem thông tin tỉnh",
                Toast.LENGTH_LONG
            ).show()
        }
        return true
    }

    private fun setDataTinh() {

        fb.getAllTinh("tinh").get().addOnSuccessListener {
            for (item in it.children) {
                var student2: Student2 = Student2()
                val product = item.getValue(Student2::class.java)
                student2.id = item.key
                student2.resouerceUrl = product?.resouerceUrl.toString()
                student2.description = product?.description.toString()
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
