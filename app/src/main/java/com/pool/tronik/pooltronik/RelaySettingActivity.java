package com.pool.tronik.pooltronik;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.pool.tronik.pooltronik.adapters.ScheduledTaskAdapter;
import com.pool.tronik.pooltronik.dto.PTScheduleDate;
import com.pool.tronik.pooltronik.net.DeleteTaskRequest;
import com.pool.tronik.pooltronik.net.GetTasksRequest;
import com.pool.tronik.pooltronik.utils.ColorUtils;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.IntentHelper;
import com.pool.tronik.pooltronik.utils.RelayConfig;
import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.pool.tronik.pooltronik.utils.StringUtils;

import java.util.List;

import retrofit2.Response;

public class RelaySettingActivity extends AppCompatActivity {

    private ImageButton ivStatus, ivStatusEditable;
    private RelayStatus relayStatus;
    private TextView tvRelayName, tvEmptyText;
    private ImageView ivSchedule;
    private TextInputLayout tilRelayName;
    private RecyclerView recyclerView;
    private ScheduledTaskAdapter scheduledTaskAdapter;
    private ProgressBar progressBar;

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
        GetTasksRequest getTasksRequest = new GetTasksRequest(this, new GetTasksObserver(), relayStatus.getRelay());

        getTasksRequest.call();
        findViewById(R.id.iv_settings).setVisibility(View.GONE);
        ivStatus = findViewById(R.id.bt_on_off);
        ivStatusEditable = findViewById(R.id.bt_on_off_editable);
        ivStatusEditable.setOnClickListener(new MClickListener());
        tvRelayName = findViewById(R.id.tv_relay_name);
        tilRelayName = findViewById(R.id.til_relay_name);
        tilRelayName.getEditText().addTextChangedListener(new MTextInputListener());
        recyclerView = findViewById(R.id.rv_task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        scheduledTaskAdapter = new ScheduledTaskAdapter(new MClickListener(), relayStatus.getName());
        recyclerView.setAdapter(scheduledTaskAdapter);
        ivSchedule = findViewById(R.id.iv_schedule);
        tvEmptyText = findViewById(R.id.tv_empty_text);
        progressBar = findViewById(R.id.pb_tasks);
        ivSchedule.setOnClickListener(new MClickListener());
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
        FileUtil.setRelayName(this,RelayConfig.RELAY_LIST_ON.get(relayStatus.getRelay()),tvRelayName.getText().toString());
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

    public void setEmptyText(boolean isEmpty) {
        if (isEmpty){
            recyclerView.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
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
                case R.id.iv_schedule:
                    startActivity(IntentHelper.getIntent(RelaySettingActivity.this, ActivityScheduling.class, relayStatus));
                    break;
                case R.id.bt_item_delete:
                    PTScheduleDate ptScheduleDate = (PTScheduleDate) view.getTag();
                    if (ptScheduleDate != null) {
                        scheduledTaskAdapter.addToInProgress(ptScheduleDate.getId());

                        DeleteTaskRequest deleteTaskRequest = new DeleteTaskRequest(RelaySettingActivity.this,
                                new DeleteObserver(ptScheduleDate.getId()),
                             ptScheduleDate.getId());
                        deleteTaskRequest.call();
                    }
                    break;
            }
        }
    }

    public void showSnackBar(String string) {
        final Snackbar snackbar = Snackbar
                .make(findViewById(R.id.ll_top_parent), string, Snackbar.LENGTH_INDEFINITE);
        try {
            View snackBarView = snackbar.getView();
            TextView snackTextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
            snackTextView.setMaxLines(3);
        } catch (Exception e){}

        snackbar.show();
        snackbar.setAction(getResources().getString(R.string.dismiss), view -> snackbar.dismiss());

        snackbar.show();
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
    }

    class DeleteObserver implements Observer<Object> {

        int id;

        DeleteObserver(int id) {
            this.id = id;
        }

        @Override
        public void onChanged(Object o) {
            if (o instanceof Throwable) {
                setEmptyText(false);
                showSnackBar(getResources().getString(R.string.error2));
            }
            else {
                scheduledTaskAdapter.deleteFromInProgress(id);
                scheduledTaskAdapter.deleteItem(id);
                scheduledTaskAdapter.notifyDataSetChanged();
            }
        }
    }

    class GetTasksObserver implements Observer<Object> {

        @Override
        public void onChanged(Object o) {
            if (o instanceof Throwable) {
                setEmptyText(false);
                showSnackBar(getResources().getString(R.string.error2));
            }
            else {
                Response<List<PTScheduleDate>> response = (Response<List<PTScheduleDate>>) o;
                if (response.isSuccessful()) {
                    List<PTScheduleDate> list = response.body();
                    if (list.isEmpty()) {
                        setEmptyText(true);
                    }
                    else {
                        setEmptyText(false);
                        scheduledTaskAdapter.setData(list);
                    }
                }
                else
                    setEmptyText(true);
            }
        }
    }
}
