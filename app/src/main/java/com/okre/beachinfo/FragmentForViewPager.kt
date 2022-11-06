package com.okre.beachinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.okre.beachinfo.databinding.ViewpagerItemBinding

class FragmentForViewPager : Fragment() {

    private lateinit var binding: ViewpagerItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewpagerItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = arguments
        val name = bundle?.getString("name")
        binding.nameTv.text = name
        when (bundle?.getInt("congestion", -1)) {
            1 -> binding.congestion3.setImageResource(R.color.congestion_green)
            2 -> binding.congestion2.setImageResource(R.color.congestion_yellow)
            3 -> binding.congestion1.setImageResource(R.color.congestion_red)
            else -> {}
        }

        binding.nameTv.setOnClickListener {
            with(Intent(Intent.ACTION_VIEW)){
                this.data = Uri.parse(String.format("geo://37,127?q=%s", Uri.encode(name)))
                setPackage("com.google.android.apps.maps")
                startActivity(this)
            }
        }
    }

    companion object {
        fun newInstance(name: String, congestion: Int) : FragmentForViewPager {
            val fragment = FragmentForViewPager()
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putInt("congestion", congestion)
            fragment.arguments = bundle
            return fragment
        }
    }
}