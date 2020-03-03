package com.pool.tronik.pooltronik.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pool.tronik.pooltronik.R;
import com.pool.tronik.pooltronik.utils.ColorUtils;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.RelayConfig;
import com.pool.tronik.pooltronik.utils.RelayStatus;

import java.util.ArrayList;
import java.util.List;

public class RelayAdapter extends RecyclerView.Adapter<RelayAdapter.RelayViewHolder> {

    private View.OnClickListener onClickListener;
    private List<RelayStatus> statusList;

    public RelayAdapter(Context context, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        statusList = new ArrayList<>();
        initFromLocalFile(context);
    }

    public void initFromLocalFile(Context context) {
        statusList.clear();
        String defName = context.getResources().getString(R.string.def_relay_name);
        for (int i = 0; i < RelayConfig.RELAYS_SIZE; i++) {
            RelayStatus relayStatus = new RelayStatus();
            relayStatus.setRelay(i+1);
            if (FileUtil.getRelayStatus(context,RelayConfig.RELAY_LIST_ON.get(i))) {
                relayStatus.setStatus(RelayConfig.STATUS_ON);
            }
            else {
                relayStatus.setStatus(RelayConfig.STATUS_OFF);
            }
            String name = FileUtil.getRelayName(context, RelayConfig.RELAY_LIST_ON.get(i));
            if (name.equalsIgnoreCase(defName))
                name += (i+1);
            relayStatus.setName(name);
            statusList.add(relayStatus);
        }
    }

    @NonNull
    @Override
    public RelayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_relay,viewGroup,false);
        return new RelayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelayViewHolder relayViewHolder, int i) {
        RelayStatus relayStatus = statusList.get(i);
        if (relayStatus.getStatus() == RelayConfig.STATUS_ON) {
            //relayStatus.setStatus(RelayConfig.STATUS_ON);
            relayStatus.setRequestedStatus(RelayConfig.STATUS_OFF);
            relayStatus.setCommand(RelayConfig.RELAY_LIST_ON.get(i));
            relayStatus.setAvailableCommand(RelayConfig.RELAY_LIST_OFF.get(i));
            relayViewHolder.ivSwitch.setVisibility(View.VISIBLE);
            relayViewHolder.progressBar.setVisibility(View.GONE);
        }
        else if (relayStatus.getStatus() == RelayConfig.STATUS_PENDING) {
            relayViewHolder.ivSwitch.setVisibility(View.GONE);
            relayViewHolder.progressBar.setVisibility(View.VISIBLE);
            relayViewHolder.ivSwitch.setOnClickListener(null);
        }
        else {
            //relayStatus.setStatus(RelayConfig.STATUS_OFF);
            relayStatus.setRequestedStatus(RelayConfig.STATUS_ON);
            relayStatus.setCommand(RelayConfig.RELAY_LIST_ON.get(i));
            relayStatus.setAvailableCommand(RelayConfig.RELAY_LIST_ON.get(i));
            relayViewHolder.ivSwitch.setVisibility(View.VISIBLE);
            relayViewHolder.progressBar.setVisibility(View.GONE);
        }

        //changeSwitchColor(relayViewHolder.ivSwitch, relayStatus.getStatus());
        ColorUtils.setColor(relayViewHolder.ivSwitch, relayStatus.getStatus());

        //relayViewHolder.textView.setText("Relay"+(i+1)+"; Status="+relayStatus.getStatus());
        //String relayName = relayViewHolder.textView.getResources().getString(R.string.def_relay_name)+(i+1);
        relayViewHolder.textView.setText(relayStatus.getName());
        relayViewHolder.ivSwitch.setTag(relayStatus);
        relayViewHolder.ivSettings.setTag(relayStatus);
        relayViewHolder.ivSettings.setOnClickListener(onClickListener);
        relayViewHolder.ivSwitch.setOnClickListener(onClickListener);
        relayViewHolder.ivSchedule.setOnClickListener(onClickListener);
        relayViewHolder.ivSchedule.setTag(relayStatus);
    }

    public void itemChanged(int position, int status) {
        statusList.get(position).setStatus(status);
        notifyItemChanged(position);
    }

    /*private void changeSwitchColor(ImageButton imageButton, int status) {
        GradientDrawable backgroundGradient = (GradientDrawable)imageButton.getBackground();
        if (status == RelayConfig.STATUS_OFF)
             backgroundGradient.setColor(Color.parseColor(ColorUtils.COLOR_OFF));
        else if (status == RelayConfig.STATUS_ON)
            backgroundGradient.setColor(Color.parseColor(ColorUtils.COLOR_ON));
    }*/

    @Override
    public int getItemCount() {
        return RelayConfig.RELAYS_SIZE;
    }

    public class RelayViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageButton ivSwitch;
        ImageView ivSettings;
        ImageView ivSchedule;
        ProgressBar progressBar;

        public RelayViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_relay_name);
            ivSwitch = itemView.findViewById(R.id.bt_on_off);
            progressBar = itemView.findViewById(R.id.pb_indication);
            ivSettings = itemView.findViewById(R.id.iv_settings);
            ivSchedule = itemView.findViewById(R.id.iv_schedule);
        }
    }
}
