package com.pool.tronik.pooltronik;

import android.graphics.Color;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pool.tronik.pooltronik.utils.ColorUtils;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.RelayConfig;
import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.pool.tronik.pooltronik.utils.StringUtils;

public class RelaySettingActivity extends AppCompatActivity {

    private ImageButton ivStatus, ivStatusEditable;
    private RelayStatus relayStatus;
    private TextView tvRelayName;
    private TextInputLayout tilRelayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relay_setting);
        initToolbar();
        relayStatus = (RelayStatus) getIntent().getSerializableExtra(StringUtils.EXTRA_DATA);
        if (relayStatus == null) {
            finish();
            return;
        }
        findViewById(R.id.iv_settings).setVisibility(View.GONE);
        ivStatus = findViewById(R.id.bt_on_off);
        ivStatusEditable = findViewById(R.id.bt_on_off_editable);
        ivStatusEditable.setOnClickListener(new MClickListener());
        tvRelayName = findViewById(R.id.tv_relay_name);
        tilRelayName = findViewById(R.id.til_relay_name);
        tilRelayName.getEditText().addTextChangedListener(new MTextInputListener());
        setDefValue();
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_outline_arrow_back_24px);
        setTitle(R.string.relay_setting);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simple_apply, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_apply:
                proceed();
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void proceed() {
        FileUtil.setRelayName(this,RelayConfig.RELAY_LIST_ON.get(relayStatus.getRelay()-1),tvRelayName.getText().toString());
        if (relayStatus.getStatus() == RelayConfig.STATUS_ON) {
            //relayStatus.setRequestedStatus(RelayConfig.STATUS_OFF);
            FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), true);
        }
        else {
            //relayStatus.setRequestedStatus(RelayConfig.STATUS_ON);
            FileUtil.setRelayStatus(getApplicationContext(),relayStatus.getCommand(), false);
        }
        setResult(RESULT_OK);
    }

    public void setDefValue() {
        tvRelayName.setText(relayStatus.getName());
        changeStatusColor();
    }

    public void changeStatusColor() {
        ColorUtils.setColor(ivStatusEditable, relayStatus.getStatus());
        ColorUtils.setColor(ivStatus, relayStatus.getStatus());
    }

    class MClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_on_off_editable:
                    if (relayStatus.getStatus() == RelayConfig.STATUS_OFF)
                        relayStatus.setStatus(RelayConfig.STATUS_ON);
                    else
                        relayStatus.setStatus(RelayConfig.STATUS_OFF);
                    changeStatusColor();
                    break;
            }
        }
    }

    class MTextInputListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tvRelayName.setText(charSequence);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    } //addTextChangedListener(new TextWatcher()
}
