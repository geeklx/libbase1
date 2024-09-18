package com.geek.libfacerecognition

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ttv.face.FaceEngine


class UserActivity : AppCompatActivity() {
    private var mydb: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mydb = DBHelper(this)

        val adapter = UsersAdapter(this, FaceMainActivity.userLists)
        val listView: ListView = findViewById<View>(R.id.userList) as ListView
        listView.setAdapter(adapter)

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@UserActivity)
            alertDialog.setTitle("Delete User")
            val items = arrayOf("Delete", "Delete All")
            alertDialog.setSingleChoiceItems(items, -1,
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> {

                            mydb!!.deleteUser(FaceMainActivity.userLists.get(i).userName)
                            FaceEngine.getInstance(applicationContext)
                                .removeFaceFeature(FaceMainActivity.userLists.get(i).user_id)
                            FaceMainActivity.userLists.removeAt(i)

                            adapter.notifyDataSetChanged()
                            dialog.cancel()
                        }
                        1 -> {
                            mydb!!.deleteAllUser()
                            FaceMainActivity.userLists.clear()
                            for (user in FaceMainActivity.userLists) {
                                FaceEngine.getInstance(applicationContext)
                                    .removeFaceFeature(user.user_id)
                            }

                            adapter.notifyDataSetChanged()
                            dialog.cancel()
                        }
                    }
                })

            val alert: AlertDialog = alertDialog.create()
            alert.show()
        }
    }
}