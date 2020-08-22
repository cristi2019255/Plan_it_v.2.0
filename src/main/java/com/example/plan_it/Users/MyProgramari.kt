package com.example.plan_it.Users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.plan_it.Users.Fragments.ProgramariFutureFragment
import com.example.plan_it.Users.Fragments.ProgramariPassedFragment
import com.example.plan_it.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class MyProgramari : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_programari)

        class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
            private val fragments: java.util.ArrayList<Fragment>
            private val titles: java.util.ArrayList<String>

            init {
                this.fragments = java.util.ArrayList()
                this.titles = java.util.ArrayList()
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }

            fun addFragment(fragment: Fragment, title: String) {
                fragments.add(fragment)
                titles.add(title)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }

        val tabLayout:TabLayout = findViewById(R.id.tab_layout)
        val viewPager:ViewPager = findViewById(R.id.view_pager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        var UserId:String=FirebaseAuth.getInstance().currentUser!!.uid
        viewPagerAdapter.addFragment(ProgramariPassedFragment(UserId), "Programari Precedente")
        viewPagerAdapter.addFragment(ProgramariFutureFragment(UserId), "Programari Viitoare")
        viewPager.setAdapter(viewPagerAdapter)
        tabLayout.setupWithViewPager(viewPager)
    }
}
