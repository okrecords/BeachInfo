package com.okre.beachinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.okre.beachinfo.databinding.ActivityMainBinding
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        viewPager = binding.viewPager
    }

    override fun onResume() {
        super.onResume()

        val rest = RetrofitOkHttpManager.beachService
        val call: Call<Beach> = rest.getBeachInfo()
        call.enqueue(object : Callback<Beach> {
            override fun onResponse(call: Call<Beach>, response: Response<Beach>) {
                if (response.isSuccessful) {
                    val adapter = ViewPagerAdapter(this@MainActivity)
                    try {
                        val jsonData = response.body()
                        Log.d("beachLog", jsonData.toString())

                        val array = ArrayList<BeachData>()
                        array.add(response.body()!!.Beach0)
                        array.add(response.body()!!.Beach1)
                        array.add(response.body()!!.Beach2)
                        array.add(response.body()!!.Beach3)
                        array.add(response.body()!!.Beach4)

                        for (item in 0 until array.size) {
                            val name = array[item].poiNm
                            val congestion = array[item].congestion.toInt()
                            adapter.appendFragment(FragmentForViewPager.newInstance(name, congestion))
                        }
                        binding.viewPager.adapter = adapter
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<Beach>, t: Throwable) {}
        })
    }
}