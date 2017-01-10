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

import com.henja.liqnot.app.LiqNotApp;
import com.henja.liqnot.dummy.DummyContent;
import com.henja.liqnot.service.LiqNotService;

import bo.Notifier;
import bo.NotifierDirector;
import dao.DAOFactory;
import dao.DAONotifier;
import dao.sqlite.DAOFactorySQLite;

public class LiqNotMainActivity extends AppCompatActivity implements NotifierListFragment.OnNotifierListFragmentInteractionListener, CurrencyOperatorValueNotifierRuleFragment.OnCurrencyOperatorValueNotifierFragmentInteractionListener {

    private NotifierDirector notifierDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifier_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //The directorNotifier
        this.notifierDirector = ((LiqNotApp)getApplication()).getNotifierDirector();

        Intent intent = new Intent(this, LiqNotService.class);
        startService(intent);


        FloatingActionButton newNotifierButton = (FloatingActionButton) findViewById(R.id.newNotifierButton);
        newNotifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewNotifierAction();
            }
        });

        NotifierFragmentPagerAdapter fragmentPagerAdapter = new NotifierFragmentPagerAdapter(getSupportFragmentManager(), this.notifierDirector);

        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setAdapter(fragmentPagerAdapter);

        //loadNotifiers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadNotifiers(){
        /*ListView notifierListView = (ListView) findViewById(R.id.notifier_list_view);
        DAOFactorySQLite db = DAOFactory.getSQLiteFactory(getApplicationContext());
        DAONotifier daoNotifier = db.getNotifierDAO();

        DAOEnumeration<DAO<Notifier>,Notifier> enumeration = daoNotifier.getNotifiers(0,10);
        TextView notifierTextView;
        final ArrayList<String> list = new ArrayList<String>();

        while (enumeration.hasNext()){
            Notifier nextNotifier = enumeration.next();
            list.add("id:"+nextNotifier.getId());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        notifierListView.setAdapter(adapter);*/

    }

    public void onNewNotifierAction(){
        ViewPager notifierPager = (ViewPager) findViewById(R.id.NotifierViewPager);
        notifierPager.setCurrentItem(1);

        /*Notifier newNotifier = new Notifier("");

        DAOFactorySQLite db = DAOFactory.getSQLiteFactory(getApplicationContext());
        DAONotifier daoNotifier = db.getNotifierDAO();
        daoNotifier.insertNotifier(newNotifier);*/
    }

    @Override
    public void OnNotifierListFragmentInteractionListener(DummyContent.DummyItem item) {
        //
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
