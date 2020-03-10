package com.pool.tronik.pooltronik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pool.tronik.pooltronik.R;
import com.pool.tronik.pooltronik.dto.PTScheduleDate;
import com.pool.tronik.pooltronik.utils.DateTimeUtils;
import com.pool.tronik.pooltronik.utils.StaticVarFile;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class ScheduledTaskAdapter extends RecyclerView.Adapter<ScheduledTaskAdapter.TaskHolder>{

    private List<PTScheduleDate> data;
    private String relayName;
    private View.OnClickListener onClickListener;
    private List<Integer> inProgressList;

    public ScheduledTaskAdapter(View.OnClickListener onClickListener, String relayName) {
        data = new ArrayList<>();
        this.relayName = relayName;
        this.onClickListener = onClickListener;
        inProgressList = new ArrayList<>();
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
        Context context = holder.tvRelayName.getContext();
        holder.tvRelayName.setText(relayName);
        LocalDateTime localDateTime = DateTimeUtils.createLocalDateTime(ptScheduleDate.getStartDate());
        String date = DateTimeUtils.getDayOfWeek(localDateTime.getDayOfWeek(),context)
                + " - "+localDateTime.getHourOfDay()+":"+localDateTime.getMinuteOfHour();
        holder.tvTime.setText(date);
        String txt;
        if (ptScheduleDate.getDuration() == StaticVarFile.DurationStatus.ALWAYS.ordinal()) {
            txt = context.getResources().getString(R.string.constantly);
        }
        else {
            txt = context.getResources().getString(R.string.once);
        }

        if (ptScheduleDate.getStatus() == StaticVarFile.RELAY_STATUS.OFF.ordinal())
            txt += " ("+context.getResources().getString(R.string.off)+")";
        else if (ptScheduleDate.getStatus() == StaticVarFile.RELAY_STATUS.ON.ordinal())
            txt += " ("+context.getResources().getString(R.string.on)+")";
        holder.tvDuration.setText(txt);


        if (!inProgressList.contains(ptScheduleDate.getId())) {
            holder.btDelete.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
            holder.btDelete.setTag(ptScheduleDate);
            holder.btDelete.setOnClickListener(onClickListener);
        }
        else {
            holder.btDelete.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
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

    public void addToInProgress(int id) {
        if (!inProgressList.contains(id)) {
            inProgressList.add(id);
        }
    }

    public void deleteFromInProgress(int id) {
        if (inProgressList.contains(id))
            inProgressList.remove(Integer.valueOf(id));
    }

    public void deleteItem(int id) {
        for (PTScheduleDate ptScheduleDate : data) {
            if (ptScheduleDate.getId() == id) {
                data.remove(ptScheduleDate);
                break;
            }
        }
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView tvRelayName;
        TextView tvTime;
        TextView tvDuration;
        TextView btDelete;
        ProgressBar progressBar;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tvRelayName = itemView.findViewById(R.id.tv_item_relay_name);
            tvTime = itemView.findViewById(R.id.tv_item_time);
            tvDuration = itemView.findViewById(R.id.tv_item_duration);
            btDelete = itemView.findViewById(R.id.bt_item_delete);
            progressBar = itemView.findViewById(R.id.pb_remove);
        }
    }
}
