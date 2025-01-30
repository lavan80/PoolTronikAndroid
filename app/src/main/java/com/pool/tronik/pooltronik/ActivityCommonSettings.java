package com.pool.tronik.pooltronik;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputLayout;
import com.pool.tronik.pooltronik.dto.ControllerEntity;
import com.pool.tronik.pooltronik.net.AbstractRequest;
import com.pool.tronik.pooltronik.net.NetConfig;
import com.pool.tronik.pooltronik.net.SetControllerIpRequest;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.ITokenCallBack;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ActivityCommonSettings extends AppCompatActivity implements View.OnClickListener, ITokenCallBack {

    private TextInputLayout tilController, tilServer;
    private String ipHintPrefix = "";
    private Button btUpdateToken, btUpdateIp;
    private ProgressBar progressBar, progressBarForIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
        tilController = findViewById(R.id.tip_ip);
        tilServer = findViewById(R.id.tip_server_ip);
        progressBar = findViewById(R.id.pb_indication);
        progressBarForIp = findViewById(R.id.pb_indication_ip);
        ipHintPrefix = getResources().getString(R.string.ip_hint) + " ";
        setCurrentIp(tilController,FileUtil.getIp(this));
        setCurrentIp(tilServer,FileUtil.getServerIp(this));
        findViewById(R.id.bt_reset_status).setOnClickListener(this);
        findViewById(R.id.bt_set_ip).setOnClickListener(this);
        findViewById(R.id.bt_set_server_ip).setOnClickListener(this);
        btUpdateToken = findViewById(R.id.bt_update_token);
        btUpdateToken.setOnClickListener(this);
        btUpdateIp = findViewById(R.id.bt_update_ip);
        btUpdateIp.setOnClickListener(this);
        if (FileUtil.isHaveToUpdateToken(this)) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
            btUpdateToken.startAnimation(animation);
        }
    }

    public void setCurrentIp(TextInputLayout til, String ip){
        String hintTxt;
        if (ip.substring(NetConfig.IP_PREFIX.length()).isEmpty()) {
            hintTxt = getString(R.string.ip_hint_error);
        }
        else {
            hintTxt = ipHintPrefix + ip.substring(NetConfig.IP_PREFIX.length());
            int index = hintTxt.indexOf(":");
            if (index > -1) {
                hintTxt = hintTxt.substring(0, index);
            }
        }
        til.setHint(hintTxt);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_outline_arrow_back_24px);
        setTitle(R.string.common_setting);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_reset_status) {//TransitionDrawable transition = (TransitionDrawable)view.getBackground();
            //transition.startTransition(500);
            ((Button) view).setText(R.string.done);
            FileUtil.resetParams(this);
            setResult(RESULT_OK);
        } else if (id == R.id.bt_set_ip) {
            String ip = tilController.getEditText().getText().toString();
            if (Patterns.IP_ADDRESS.matcher(ip).matches()) {
                tilController.setHint(ipHintPrefix + ip);
                tilController.getEditText().setText("");
            } else {
                Toast.makeText(this, R.string.ip_incorrect, Toast.LENGTH_LONG).show();
                return;
            }
            ip = NetConfig.IP_PREFIX + ip;
            FileUtil.setIp(this, ip);
            NetConfig.BASE_CONTROLLER_URL = ip;
        } else if (id == R.id.bt_set_server_ip) {
            String serverIp = tilServer.getEditText().getText().toString();
            if (Patterns.IP_ADDRESS.matcher(serverIp).matches()) {
                tilServer.setHint(ipHintPrefix + serverIp);
                tilServer.getEditText().setText("");
            } else {
                Toast.makeText(this, R.string.ip_incorrect, Toast.LENGTH_LONG).show();
                return;
            }
            serverIp = NetConfig.IP_PREFIX + serverIp + NetConfig.BASE_PORT;
            FileUtil.setServerIp(this, serverIp);
            NetConfig.BASE_SERVER_URL = serverIp;
        } else if (id == R.id.bt_update_token) {
            if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) {
                Toast.makeText(this, R.string.ip_server_error, Toast.LENGTH_LONG).show();
                return;
            }
            changeUpdateButtonVisibility(btUpdateToken, progressBar, false);
            TokenHelper tokenHelper = new TokenHelper(this);
            tokenHelper.askToken(getApplicationContext());
        } else if (id == R.id.bt_update_ip) {
            if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) {
                Toast.makeText(this, R.string.ip_server_error, Toast.LENGTH_LONG).show();
                return;
            }
            String cntrlIp = FileUtil.getIp(this);
            if (cntrlIp.equals(NetConfig.IP_PREFIX)) {
                Toast.makeText(this, R.string.ip_incorrect, Toast.LENGTH_LONG).show();
                return;
            }
            ControllerEntity controllerEntity = new ControllerEntity();
            controllerEntity.setControllerIp(cntrlIp);
            changeUpdateButtonVisibility(btUpdateIp, progressBarForIp, false);
            AbstractRequest abstractRequest = new SetControllerIpRequest(this, new UpdateIpObserver(), controllerEntity);
            abstractRequest.call();
        }
    }

    private void changeUpdateButtonVisibility(View button, View progress, boolean isButtonVisible) {
        if (isButtonVisible) {
            button.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
        else {
            button.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void success(Object o) {
        changeUpdateButtonVisibility(btUpdateToken, progressBar, true);
        Toast.makeText(this, R.string.finished_with_ok, Toast.LENGTH_LONG).show();
    }

    @Override
    public void error(Object o) {
        changeUpdateButtonVisibility(btUpdateToken, progressBar,true);
        Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
    }

    class UpdateIpObserver implements Observer<Object> {

        @Override
        public void onChanged(Object o) {
            if (o instanceof Throwable) {
                changeUpdateButtonVisibility(btUpdateIp, progressBarForIp, true);
                Toast.makeText(ActivityCommonSettings.this, R.string.error_msg, Toast.LENGTH_LONG).show();
            }
            else {
                Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) o;
                if (responseBodyResponse.isSuccessful())
                    Toast.makeText(ActivityCommonSettings.this,getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ActivityCommonSettings.this, R.string.error_msg, Toast.LENGTH_LONG).show();

                changeUpdateButtonVisibility(btUpdateIp, progressBarForIp, true);
            }
        }
    }
}
