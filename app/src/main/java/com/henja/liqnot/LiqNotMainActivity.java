package com.henja.liqnot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.henja.liqnot.dummy.DummyContent;
import com.henja.liqnot.service.LiqNotService;

import bo.Notifier;

public class LiqNotMainActivity extends AppCompatActivity implements NotifierListFragment.OnNotifierListFragmentInteractionListener, CurrencyOperatorValueNotifierRuleFragment.OnCurrencyOperatorValueNotifierFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifier_main);

        Intent intent = new Intent(getApplicationContext(), LiqNotService.class);
        startService(intent);


        FloatingActionButton newNotifierButton = (FloatingActionButton) findViewById(R.id.newNotifierButton);
        newNotifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewNotifierAction();
            }
        });

        NotifierFragmentPagerAdapter fragmentPagerAdapter = new NotifierFragmentPagerAdapter(getSupportFragmentManager());

        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onNewNotifierAction(){
        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setCurrentItem(1);
    }

    @Override
    public void OnNotifierListFragmentInteraction(DummyContent.DummyItem item) {
        //
    }

    @Override
    public void onBackPressed()
    {
        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setCurrentItem(0);
    }

    @Override
    public void OnCurrencyOperatorValueNotifierFragmentInteractionListener(Uri uri) {
        //
    }

    @Override
    public void onNotifierCreated(Notifier notifier) {
        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setCurrentItem(0);
    }
}
