package com.test.aktivocoresdksample_android.supplementaryData.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aktivolabs.aktivocore.data.models.googlefit.FitSupplementaryDataPointEnum;
import com.test.aktivocoresdksample_android.R;
import com.test.aktivocoresdksample_android.supplementaryData.adapters.viewHolders.SupplementaryDataPointVH;

import java.util.List;

public class SupplementaryDataPointAdapter extends RecyclerView.Adapter<SupplementaryDataPointVH> {

    private final Context context;
    private final List<FitSupplementaryDataPointEnum> dataPointEnumList;
    private final LayoutInflater layoutInflater;
    private SupplementaryDataPointAdapterInterface supplementaryDataPointAdapterInterface;

    public SupplementaryDataPointAdapter(Context context, List<FitSupplementaryDataPointEnum> dataPointEnumList) {
        this.context = context;
        this.dataPointEnumList = dataPointEnumList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SupplementaryDataPointVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = layoutInflater.inflate(R.layout.list_item_supplementary_data_point, parent, false);
        SupplementaryDataPointVH viewHolder = new SupplementaryDataPointVH(row);
        row.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SupplementaryDataPointVH holder, int position) {
        holder.setData(dataPointEnumList.get(position), supplementaryDataPointAdapterInterface);
    }

    @Override
    public int getItemCount() {
        return dataPointEnumList.size();
    }

    public interface SupplementaryDataPointAdapterInterface {
        void onCheckChanged(boolean isChecked);
    }

    public void setSupplementaryDataPointAdapterInterface(SupplementaryDataPointAdapterInterface listener) {
        this.supplementaryDataPointAdapterInterface = listener;
    }
}
