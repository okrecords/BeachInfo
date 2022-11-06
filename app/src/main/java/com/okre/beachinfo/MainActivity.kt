package com.okre.beachinfo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.okre.beachinfo.databinding.ActivityMainBinding
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
                    val viewPagerAdapter = ViewPagerAdapter(this@MainActivity)
                    try {
                        val responseData = response.body()!!
                        Log.d("beachLog", responseData.toString())

                        val array = ArrayList<BeachData>()
                        array.add(responseData.Beach0)
                        array.add(responseData.Beach1)
                        array.add(responseData.Beach2)
                        array.add(responseData.Beach3)
                        array.add(responseData.Beach4)

                        for (item in 0 until array.size) {
                            val name = array[item].poiNm
                            //val congestion = array[item].congestion.toInt()
                            // 겨울해수욕장 혼잡도 보통만 출력되므로 임의 숫자 배정
                            val congestion = (1..3).random()
                            viewPagerAdapter.appendFragment(FragmentForViewPager.newInstance(name, congestion))
                        }
                        viewPager.adapter = viewPagerAdapter
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<Beach>, t: Throwable) {}
        })
    }
}