package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.SectionsPageAdapter;

import header.Header_Frag;

public class CustomerSummaryAct extends FragmentActivity {
    TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_summary);


        Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

    }
    private void setupViewPager(ViewPager viewPager) {

        try {
            SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
            adapter.addFragment(new CustomerInformationFragment(), "معلومات العميل");
            adapter.addFragment(new CustomerLocationFrag(), "موقع العميل");
            adapter.addFragment(new CustomerAccReportFrag(), "كشف الحساب");
            adapter.addFragment(new CustomerCatchFraq(), "القبوضات");
            adapter.addFragment(new CustomerBillFrag(), "الفواتير");
            adapter.addFragment(new CustomerSalesPayoffReportFrag(), "المرتجعات");
            adapter.addFragment(new CustomerSellingRequestFraq(), "طلب البيع");
            adapter.addFragment(new CustomerCollectionsReportFrag(), "التحصيلات");
            adapter.addFragment(new CustomerManVisitFraq(), "الزيارات");
            viewPager.setAdapter(adapter);
        }catch ( Exception sd){
            Toast.makeText(this,sd.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }
}
