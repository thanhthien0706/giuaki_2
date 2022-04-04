package com.example.giuaki2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StudentAdapter(private val context: Context, private val mListStudent: MutableList<Student2>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgStudent: ImageView = itemView.findViewById(R.id.img_student)
        val tvNameStudent: TextView = itemView.findViewById(R.id.tv_name_student)
        val layoutItemStudent: CardView = itemView.findViewById(R.id.layout_item_student)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student: Student2 = mListStudent[position]

        if (student == null) {
            return
        }

        Glide.with(context).load(student.resouerceUrl).into(holder.imgStudent)
        holder.tvNameStudent.setText(student.name)

        holder.layoutItemStudent.setOnClickListener {
            onShowTinh(student)
        }

    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
        setHasStableIds(true)
    }

    private fun onShowTinh(student: Student2) {
        val intent: Intent = Intent(context, DetailActivity::class.java)
        val bundle: Bundle = Bundle()
        bundle.putSerializable("object_student", student)

        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        if (mListStudent != null) {
            return mListStudent.size
        }
        return 0
    }
}