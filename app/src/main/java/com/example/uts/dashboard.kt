package com.example.uts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import org.json.JSONObject

class dashboard : AppCompatActivity() {

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btnlogout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("STATUS", "0")
            editor.apply()

            startActivity(Intent(this@dashboard, MainActivity::class.java))
            finish()
        }

        getdariserver()

        btnSimpan.setOnClickListener() {
            val data_nama: String = nama_penduduk.text.toString()
            val data_ttl: String = ttl.text.toString()
            val data_hp: String = nomor_hp.text.toString()
            val data_alamat: String = alamat_penduduk.text.toString()
            postkeserve(data_nama, data_ttl, data_hp, data_alamat)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    @SuppressLint("WrongConstant")
    fun getdariserver() {
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val users = ArrayList<User>()


        AndroidNetworking.get("https://utsandroidwardah.000webhostapp.com/UtsAndroid/penduduk_json.php")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("_kotlinResponse", response.toString())

                    val jsonArray = response.getJSONArray("result")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        Log.e("_kotlinTitle", jsonObject.optString("nama_penduduk"))

                        // txt1.setText(jsonObject.optString("shubuh"))
                        var isi1 = jsonObject.optString("nama_penduduk").toString()
                        var isi2 = jsonObject.optString("ttl_penduduk").toString()
                        var isi3 = jsonObject.optString("hp_penduduk").toString()
                        var isi4 = jsonObject.optString("alamat_penduduk").toString()

                        users.add(User("$isi1", "$isi2", "$isi3", "$isi4"))


                    }

                    val adapter = CustomAdapter(users)
                    recyclerView.adapter = adapter

                }

                override fun onError(anError: ANError) {
                    Log.i("_err", anError.toString())
                }
            })

    }

    fun postkeserve(data1: String, data2: String, data3: String, data4: String) {
        AndroidNetworking.post("https://utsandroidwardah.000webhostapp.com/UtsAndroid/proses_tambah_penduduk.php")
            .addBodyParameter("nama_penduduk", data1)
            .addBodyParameter("ttl_penduduk", data2)
            .addBodyParameter("hp_penduduk", data3)
            .addBodyParameter("alamat_penduduk", data4)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {

                }

                override fun onError(anError: ANError?) {

                }
            })
    }
}



