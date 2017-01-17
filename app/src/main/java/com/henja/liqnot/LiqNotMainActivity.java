package com.henja.liqnot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.henja.liqnot.dummy.DummyContent;
import com.henja.liqnot.service.LiqNotService;

import bo.Notifier;

public class LiqNotMainActivity extends AppCompatActivity implements NotifierListFragment.OnNotifierListFragmentInteractionListener, CurrencyOperatorValueNotifierRuleFragment.OnCurrencyOperatorValueNotifierFragmentInteractionListener {
    FloatingActionButton newNotifierButton;
    Toolbar toolbar;
    Notifier notifierToModify;

    public Toolbar getToolbar(){
        return this.toolbar;
    }

    public Notifier getNotifierToModify(){
        return notifierToModify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifier_main);

        Intent intent = new Intent(getApplicationContext(), LiqNotService.class);
        startService(intent);

        NotifierFragmentPagerAdapter fragmentPagerAdapter = new NotifierFragmentPagerAdapter(getSupportFragmentManager());

        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setAdapter(fragmentPagerAdapter);
        toolbar = (Toolbar) findViewById(R.id.notifier_list_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_item) {
            onNewNotifierAction();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNewNotifierAction(){
        this.notifierToModify = null;
        loadNotifierCreationFragment();
    }

    public void onModifyNotifierAction(Notifier notifier){
        this.notifierToModify = notifier;
        loadNotifierCreationFragment();
    }

    @Override
    public void OnNotifierListFragmentInteraction(DummyContent.DummyItem item) {
        //
    }

    @Override
    public void onBackPressed()
    {
        loadNotifierListFragment();
    }

    @Override
    public void OnCurrencyOperatorValueNotifierFragmentInteractionListener(Uri uri) {
        //
    }

    @Override
    public void onNotifierCreated(Notifier notifier) {
        loadNotifierListFragment();
    }

    @Override
    public void onNotifierModified(Notifier notifier) {
        loadNotifierListFragment();
    }

    public void loadNotifierCreationFragment(){
        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        ((NotifierFragmentPagerAdapter)notifierPager.getAdapter()).notifyChangeInPosition(1);
        notifierPager.getAdapter().notifyDataSetChanged();
        notifierPager.setCurrentItem(1);
        toolbar.setVisibility(View.GONE);
    }

    public void loadNotifierListFragment(){

        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setCurrentItem(0);
        toolbar.setVisibility(View.VISIBLE);

    }
}
