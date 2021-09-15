package com.example.realmdb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var realm: Realm? = null
    private var output: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val config = RealmConfiguration.Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        realm = Realm.getInstance(config)
        var jobs: Job
        val insert = findViewById<Button>(R.id.insert)
        val update = findViewById<Button>(R.id.update)
        val read = findViewById<Button>(R.id.read)
        val delete = findViewById<Button>(R.id.delete)
        output = findViewById(R.id.show_data)
        insert.setOnClickListener(this)
        update.setOnClickListener(this)
        read.setOnClickListener(this)
        delete.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.insert) {
            Log.d("Insert", "Insert")
            showInsertDialog()
        }
        if (v.id == R.id.update) {
            showUpdateDialog()
            Log.d("Insert", "Insert")
        }
        if (v.id == R.id.delete) {
            Log.d("Insert", "Insert")
            showDeleteDialog()
        }
        if (v.id == R.id.read) {
            showData()
            Log.d("Insert", "Insert")
        }
    }

    private fun showUpdateDialog() {
        val al = AlertDialog.Builder(this@MainActivity)
        val view = layoutInflater.inflate(R.layout.delete_dialog, null)
        al.setView(view)
        val data_id = view.findViewById<EditText>(R.id.data_id)
        val delete = view.findViewById<Button>(R.id.delete)
        val alertDialog = al.show()
        delete.setOnClickListener {
            alertDialog.dismiss()
            val id = data_id.text.toString().toLong()
            val dataModel =
                realm!!.where(DataModel::class.java).equalTo("id", id).findFirst()
            showUpdateDialog(dataModel)
        }
    }

    private fun showDeleteDialog() {
        val al = AlertDialog.Builder(this@MainActivity)
        val view = layoutInflater.inflate(R.layout.delete_dialog, null)
        al.setView(view)
        val data_id = view.findViewById<EditText>(R.id.data_id)
        val delete = view.findViewById<Button>(R.id.delete)
        val alertDialog = al.show()
        delete.setOnClickListener {
            val id = data_id.text.toString().toLong()
            val dataModel =
                realm!!.where(DataModel::class.java).equalTo("id", id).findFirst()
            realm!!.executeTransaction {

                alertDialog.dismiss()
                dataModel!!.deleteFromRealm()


            }
        }
    }

    private fun showInsertDialog() {
        val al = AlertDialog.Builder(this@MainActivity)
        val view = layoutInflater.inflate(R.layout.data_input_dialog, null)
        al.setView(view)
        val name = view.findViewById<EditText>(R.id.name)
        val age = view.findViewById<EditText>(R.id.age)
        val gender = view.findViewById<Spinner>(R.id.gender)
        val save = view.findViewById<Button>(R.id.save)
        val alertDialog = al.show()
        save.setOnClickListener {
            alertDialog.dismiss()
            val dataModel = DataModel()
            val current_id = realm!!.where(DataModel::class.java).max("id")
            val nextId: Long
            nextId = if (current_id == null) {
                1
            } else {
                (current_id.toInt() + 1).toLong()
            }
            dataModel.id = nextId
            dataModel.age = age.text.toString().toInt()
            dataModel.name = name.text.toString()
            dataModel.gender = gender.selectedItem.toString()
            realm!!.executeTransactionAsync { realm -> realm.copyToRealm(dataModel) }
        }
    }

    private fun showUpdateDialog(dataModel: DataModel?) {
        val al = AlertDialog.Builder(this@MainActivity)
        val view = layoutInflater.inflate(R.layout.data_input_dialog, null)
        al.setView(view)
        val name = view.findViewById<EditText>(R.id.name)
        val age = view.findViewById<EditText>(R.id.age)
        val gender = view.findViewById<Spinner>(R.id.gender)
        val save = view.findViewById<Button>(R.id.save)
        val alertDialog = al.show()
        name.setText(dataModel!!.name)
        age.setText("" + dataModel.age)
        if (dataModel.gender.equals("Male", ignoreCase = true)) {
            gender.setSelection(0)
        } else {
            gender.setSelection(1)
        }
        save.setOnClickListener {
            alertDialog.dismiss()
                realm?.executeTransaction{
                    dataModel.age = age.text.toString().toInt()
                    dataModel.name = name.text.toString()
                    dataModel.gender = gender.selectedItem.toString()
                    it.copyToRealmOrUpdate(dataModel)
                }
        }
    }

    private fun showData() {
        val dataModels: List<DataModel> = realm!!.where(DataModel::class.java).findAll()
        output!!.text = ""
        for (i in dataModels.indices) {
            output!!.append(
                "ID : ${dataModels[i].id} Name : ${dataModels[i].name} Age : ${dataModels[i].age} Gender : ${dataModels[i].gender} \n "

            )
        }
    }
}