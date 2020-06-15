package com.pool.tronik.pooltronik;

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
import com.pool.tronik.pooltronik.net.ControllerNetRequest;
import com.pool.tronik.pooltronik.net.GetStateRelayRequest;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.RelayConfig;
import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.pool.tronik.pooltronik.utils.RelayUtil;
import com.pool.tronik.pooltronik.utils.StringUtils;
import com.pool.tronik.pooltronik.utils.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 5432;
    private final int REQUEST_CHANGE = 5433;

    private RecyclerView recyclerView;
    private List<Integer> clickedList;
    private DrawerLayout drawerLayout;
    private ViewGroup attentionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);
        initToolbar();
        initNavigationView();
        clickedList = new ArrayList<>();
        attentionLayout = findViewById(R.id.rl_alert_container);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.rv_relay_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new RelayAdapter(this, new MClickListener()));

        //******
        getToken();
        //GetStateRelayRequest getStateRelayRequest = new GetStateRelayRequest(this, new RelayStatusCallback());
        //getStateRelayRequest.call();
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
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.nav_settings:
                                startActivityForResult(new Intent(MainActivity.this,ActivityCommonSettings.class),REQUEST_CODE);
                                break;
                            case R.id.nav_about_us:
                                String url = "http://www.jenyazla.wixsite.com/pool-tronic";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                break;
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    ((RelayAdapter)recyclerView.getAdapter()).initFromLocalFile(this);
                    ((RelayAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
                    break;
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
            switch (view.getId()) {
                case R.id.bt_on_off:
                    //prevent double click
                    if (clickedList.contains(relayStatus.getRelay())) {
                        return;
                    }
                    ((RelayAdapter)recyclerView.getAdapter()).itemChanged(relayStatus.getRelay(),RelayConfig.STATUS_PENDING);
                    clickedList.add(relayStatus.getRelay());
                    ControllerNetRequest netRequest = new ControllerNetRequest(MainActivity.this, new MCallback(relayStatus),relayStatus);
                    netRequest.call();
                    //mockRequest(relayStatus);
                    break;
                case R.id.iv_settings:
                    if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING)
                        return;
                    Intent intent = new Intent(MainActivity.this,RelaySettingActivity.class);
                    intent.putExtra(StringUtils.EXTRA_DATA, relayStatus);
                    startActivityForResult(intent,REQUEST_CHANGE);
                    break;
                case R.id.rl_alert_container:
                    Intent aIntent = new Intent(MainActivity.this,ActivityCommonSettings.class);
                    startActivity(aIntent);
                    break;
                case R.id.iv_schedule:
                    Intent sIntent = new Intent(MainActivity.this,ActivityScheduling.class);
                    sIntent.putExtra(StringUtils.EXTRA_DATA, relayStatus);
                    startActivity(sIntent);
                    break;
            }
        }
    }


    class MCallback implements Observer<Object> {
        RelayStatus relayStatus;

        MCallback(RelayStatus relayStatus){
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
                    else
                        status = relayStatus.getStatus();
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
            if (!(o instanceof Throwable)) {
                Response<String> response = (Response<String>) o;
                if (response.isSuccessful()) {
                    String s = response.body();
                    Log.d("ASDDSA","s = "+s);
                    Map<Integer,Integer> mapStatus = XmlUtil.parseXml(s);
                    RelayAdapter relayAdapter = (RelayAdapter) recyclerView.getAdapter();
                    List<RelayStatus> relayStatusList = relayAdapter.getStatusList();
                    boolean isNeedUpdate = false;
                    for (RelayStatus relayStatus : relayStatusList) {
                        if (mapStatus.containsKey(relayStatus.getRelay()+1)) {
                            int status = mapStatus.get(relayStatus.getRelay()+1);
                            if (status != relayStatus.getStatus()) {
                                relayStatus.setStatus(status);
                                //RelayUtil.setCorrectState(relayStatus, relayStatus.getRelay());
                                if (relayStatus.getRequestedStatus() == RelayConfig.STATUS_ON) {
                                    FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), true);
                                }
                                else if (relayStatus.getRequestedStatus() == RelayConfig.STATUS_OFF) {
                                    FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), false);
                                }
                                isNeedUpdate = true;
                            }
                        }
                    }
                    if (isNeedUpdate)
                        relayAdapter.notifyDataSetChanged();
                    //FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), true);
                    Log.d("ASDDSA","mapStatus = "+mapStatus);
                }
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
