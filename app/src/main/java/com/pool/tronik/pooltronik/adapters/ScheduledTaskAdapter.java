package com.pool.tronik.pooltronik.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pool.tronik.pooltronik.R;
import com.pool.tronik.pooltronik.dto.PTScheduleDate;
import com.pool.tronik.pooltronik.utils.DateTimeUtils;
import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.pool.tronik.pooltronik.utils.StaticVarFile;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class ScheduledTaskAdapter extends RecyclerView.Adapter<ScheduledTaskAdapter.TaskHolder>{

    private List<PTScheduleDate> data;
    private String relayName;

    public ScheduledTaskAdapter(String relayName) {
        data = new ArrayList<>();
        this.relayName = relayName;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduled_task_layout,parent,false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {

        PTScheduleDate ptScheduleDate = data.get(position);
        holder.tvRelayName.setText(relayName);
        LocalDateTime localDateTime = DateTimeUtils.createLocalDateTime(ptScheduleDate.getStartDate());
        String date = DateTimeUtils.getDayOfWeek(localDateTime.getDayOfWeek(),holder.tvRelayName.getContext())
                + " - "+localDateTime.getHourOfDay()+":"+localDateTime.getMinuteOfHour();
        holder.tvTime.setText(date);
        if (ptScheduleDate.getDuration() == StaticVarFile.DurationStatus.ALWAYS.ordinal()) {
            holder.tvDuration.setText(holder.tvRelayName.getContext().getResources().getString(R.string.constantly));
        }
        else {
            holder.tvDuration.setText(holder.tvRelayName.getContext().getResources().getString(R.string.once));
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<PTScheduleDate> list) {
        data = list;
        notifyDataSetChanged();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView tvRelayName;
        TextView tvTime;
        TextView tvDuration;
        Button btDelete;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tvRelayName = itemView.findViewById(R.id.tv_item_relay_name);
            tvTime = itemView.findViewById(R.id.tv_item_time);
            tvDuration = itemView.findViewById(R.id.tv_item_duration);
            btDelete = itemView.findViewById(R.id.bt_item_delete);
        }
    }
}
