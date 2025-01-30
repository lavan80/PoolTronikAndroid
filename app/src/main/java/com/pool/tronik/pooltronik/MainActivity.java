package com.pool.tronik.pooltronik;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.pool.tronik.pooltronik.adapters.RelayAdapter;
import com.pool.tronik.pooltronik.net.AbstractRequest;
import com.pool.tronik.pooltronik.net.ControllerNetRequest;
import com.pool.tronik.pooltronik.net.GetStateRelayRequest;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.RelayConfig;
import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.pool.tronik.pooltronik.utils.StringUtils;
import com.pool.tronik.pooltronik.utils.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_SETTINGS = 5432;
    private final int REQUEST_CHANGE = 5433;

    private RecyclerView recyclerView;
    private List<Integer> clickedList;
    private DrawerLayout drawerLayout;
    private ViewGroup attentionLayout;
    private boolean isStateUpdated = false;
    private List<AbstractRequest> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);
        initToolbar();
        initNavigationView();
        clickedList = new ArrayList<>();
        requestList = new ArrayList<>();
        attentionLayout = findViewById(R.id.rl_alert_container);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.rv_relay_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new RelayAdapter(this, new MClickListener()));
        //******
        getToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FileUtil.isHaveToUpdateToken(this)) {
            attentionLayout.setVisibility(View.VISIBLE);
            attentionLayout.setOnClickListener(new MClickListener());
        }
        else
            attentionLayout.setVisibility(View.GONE);

        isStateUpdated = false;
        GetStateRelayRequest getStateRelayRequest = new GetStateRelayRequest(this, new RelayStatusCallback());
        getStateRelayRequest.call();
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_outline_menu_24px);
    }

    public void initNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        @SuppressLint("RestrictedApi") NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.nav_settings) {
                            startActivityForResult(new Intent(MainActivity.this, ActivityCommonSettings.class), REQUEST_SETTINGS);
                        } else if (itemId == R.id.nav_about_us) {
                            String url = "http://www.pooltronic.co.il";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SETTINGS:
                case REQUEST_CHANGE:
                    ((RelayAdapter)recyclerView.getAdapter()).initFromLocalFile(this);
                    ((RelayAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//https://medium.com/@son.rommer/fix-cleartext-traffic-error-in-android-9-pie-2f4e9e2235e6
    class MClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final RelayStatus relayStatus = (RelayStatus) view.getTag();
            int id = view.getId();
            if (id == R.id.bt_on_off) {//prevent double click
                if (clickedList.contains(relayStatus.getRelay())) {
                    return;
                }
                ((RelayAdapter) recyclerView.getAdapter()).itemChanged(relayStatus.getRelay(), RelayConfig.STATUS_PENDING);
                clickedList.add(relayStatus.getRelay());
                ControllerNetRequest netRequest = new ControllerNetRequest(MainActivity.this, new RelayOnClickCallback(relayStatus), relayStatus);
                if (!isStateUpdated) {
                    requestList.add(netRequest);
                } else
                    netRequest.call();
                //mockRequest(relayStatus);
            } else if (id == R.id.iv_settings) {
                if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING)
                    return;
                Intent intent = new Intent(MainActivity.this, RelaySettingActivity.class);
                intent.putExtra(StringUtils.EXTRA_DATA, relayStatus);
                startActivityForResult(intent, REQUEST_CHANGE);
            } else if (id == R.id.rl_alert_container) {
                Intent aIntent = new Intent(MainActivity.this, ActivityCommonSettings.class);
                startActivity(aIntent);
            } else if (id == R.id.iv_schedule) {
                Intent sIntent = new Intent(MainActivity.this, ActivityScheduling.class);
                sIntent.putExtra(StringUtils.EXTRA_DATA, relayStatus);
                startActivity(sIntent);
            }
        }
    }


    class RelayOnClickCallback implements Observer<Object> {
        RelayStatus relayStatus;

        RelayOnClickCallback(RelayStatus relayStatus){
            this.relayStatus = relayStatus;
        }
        @Override
        public void onChanged(Object o) {
            if (o instanceof Throwable) {
                onComplete();
                errorHandle();
            }
            else {
                onComplete();
                Response<String> response = (Response<String>) o;
                if(response.isSuccessful()) {
                    int status;
                    if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING && relayStatus.getRequestedStatus() == RelayConfig.STATUS_ON) {
                        status = RelayConfig.STATUS_ON;
                        FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), true);
                    }
                    else if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING && relayStatus.getRequestedStatus() == RelayConfig.STATUS_OFF) {
                        status = RelayConfig.STATUS_OFF;
                        FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), false);
                    }
                    else {
                        status = relayStatus.getRequestedStatus();
                        FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), status == (RelayConfig.STATUS_ON));
                    }
                    ((RelayAdapter)recyclerView.getAdapter()).itemChanged(relayStatus.getRelay(), status);
                }
                else {
                    errorHandle();
                }
            }
        }
        public void onComplete() {
            clickedList.remove(new Integer(relayStatus.getRelay()));
        }

        public void errorHandle() {
            if (relayStatus.getRequestedStatus() == RelayConfig.STATUS_OFF)
                ((RelayAdapter)recyclerView.getAdapter()).itemChanged(relayStatus.getRelay(), RelayConfig.STATUS_ON);
            else
                ((RelayAdapter)recyclerView.getAdapter()).itemChanged(relayStatus.getRelay(), RelayConfig.STATUS_OFF);
            Toast.makeText(MainActivity.this,getString(R.string.error_msg), Toast.LENGTH_LONG).show();
        }
    }

    class RelayStatusCallback implements Observer<Object> {

        @Override
        public void onChanged(Object o) {
            isStateUpdated = true;// even if we are get an error user can try to on/off a controller
            if (!(o instanceof Throwable)) {
                Response<String> response = (Response<String>) o;
                if (response.isSuccessful()) {
                    String s = response.body();
                    Map<Integer,Integer> mapStatus = XmlUtil.parseXml(s);
                    RelayAdapter relayAdapter = (RelayAdapter) recyclerView.getAdapter();
                    List<RelayStatus> relayStatusList = relayAdapter.getStatusList();
                    boolean isNeedUpdate = false;
                    try {
                        for (RelayStatus relayStatus : relayStatusList) {
                            if (mapStatus.containsKey(relayStatus.getRelay())) {
                                int status = mapStatus.get(relayStatus.getRelay());
                                if (status != relayStatus.getStatus()) {
                                    relayStatus.setStatus(status);
                                    if (relayStatus.getRequestedStatus() == RelayConfig.STATUS_ON) {
                                        FileUtil.setRelayStatus(getApplicationContext(), relayStatus.getCommand(), true);
                                    } else if (relayStatus.getRequestedStatus() == RelayConfig.STATUS_OFF) {
                                        FileUtil.setRelayStatus(getApplicationContext(), relayStatus.getCommand(), false);
                                    }
                                    isNeedUpdate = true;
                                }
                            }
                        }
                        if (isNeedUpdate)
                            relayAdapter.notifyDataSetChanged();
                    } catch (Exception e){}
                }
            }
            if (!requestList.isEmpty()) {
                for (AbstractRequest request : requestList) {
                    request.call();
                }
                requestList.clear();
            }
        }
    }

    public void getToken() {
        //MyFirebaseMessagingService.showSystemNotification(this,"alarm msg");
        if (TokenHelper.isNeedRefresh(getApplicationContext())) {
            TokenHelper tokenHelper = new TokenHelper();
            tokenHelper.askToken(getApplicationContext());
        }
    }
    public void mockRequest(final RelayStatus relayStatus) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clickedList.remove(new Integer(relayStatus.getRelay()));
                        int status;
                        Log.d("DRDRDRDR","Thread status="+relayStatus.getStatus()+"; RequestedStatus="+relayStatus.getRequestedStatus());
                        if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING &&
                                relayStatus.getRequestedStatus() == RelayConfig.STATUS_ON) {
                            status = RelayConfig.STATUS_ON;
                            FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), true);
                        }
                        else if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING &&
                                relayStatus.getRequestedStatus() == RelayConfig.STATUS_OFF) {
                            status = RelayConfig.STATUS_OFF;
                            FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), false);
                        }
                        else
                            status = relayStatus.getStatus();
                        ((RelayAdapter)recyclerView.getAdapter()).itemChanged(relayStatus.getRelay(), status);
                    }
                });
            }
        });
        thread.start();
    }

}
