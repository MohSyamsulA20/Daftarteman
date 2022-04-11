package com.example.data_teman

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.FieldPosition

class MyListData : AppCompatActivity(), RecyclerViewAdapter.dataListener {

    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataTeman = ArrayList<data_teman>()
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)

        recyclerView = findViewById(R.id.dataList)
        supportActionBar!!.title = "DataTeman"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        DetData()
    }

    private fun DetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...!!", Toast.LENGTH_SHORT).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("DataTeman")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (datasnapshot.exists()) {
                        dataTeman.clear()
                        for (snapshot in datasnapshot.children) {
                            val teman = snapshot.getValue(data_teman::class.java)
                            teman?.key = snapshot.key
                            dataTeman.add(teman!!)
                        }
                        adapter = RecyclerViewAdapter(dataTeman, this@MyListData)
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Data berhasil dimuat",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data gagal dimuat",
                        Toast.LENGTH_SHORT).show()
                    Log.e("ListData", databaseError.details + " " +
                            databaseError.message)
                }
            })
    }

    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext,
            R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_teman?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        if (getReference != null) {
            getReference.child("Admin")
                .child(getUserID)
                .child("DataTeman")
                .child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MyListData, "Delete data Berhasil!!",
                        Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this@MyListData, "Reference kosong!!",
                Toast.LENGTH_SHORT).show()
        }
    }
}













