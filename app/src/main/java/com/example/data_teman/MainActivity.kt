package com.example.data_teman

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener(this)
        save.setOnClickListener(this)
        show_data.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

    }

    override fun onClick(v: View) {
        when(v.getId()){


            R.id.save -> {
                val getUserID = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()

                val getName: String = txtnama.getText().toString()
                val getAlamat: String = txtalamat.getText().toString()
                val getNoHp: String = no_hp.getText().toString()

                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpty(getName) || isEmpty(getAlamat) || isEmpty(getNoHp)) {
                    Toast.makeText(
                        this,  "Data tidak boleh ada yang kosong",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    getReference.child("Admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getName, getAlamat, getNoHp))
                        .addOnCompleteListener(this) {

                            txtnama.setText("")
                            txtalamat.setText("")
                            no_hp.setText("")
                            Toast.makeText(this@MainActivity, "Data Tersimpan", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
            R.id.logout->{

                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        intent = Intent(this, Loginactivity::class.java)
                        startActivity(intent)
                        finish()
                    }

            }
            R.id.show_data->{
                startActivity(Intent(this@MainActivity, MyListData::class.java))
            }
        }
    }
}
