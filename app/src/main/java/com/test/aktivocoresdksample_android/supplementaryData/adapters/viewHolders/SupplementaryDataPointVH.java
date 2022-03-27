package com.test.aktivocoresdksample_android.supplementaryData.adapters.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aktivolabs.aktivocore.data.models.googlefit.FitSupplementaryDataPointEnum;
import com.test.aktivocoresdksample_android.R;
import com.test.aktivocoresdksample_android.supplementaryData.adapters.SupplementaryDataPointAdapter;

public class SupplementaryDataPointVH extends RecyclerView.ViewHolder {

    private CheckBox dataPointCB;

    public SupplementaryDataPointVH(@NonNull View itemView) {
        super(itemView);
        dataPointCB = itemView.findViewById(R.id.dataPointCB);
    }

    public void setData(FitSupplementaryDataPointEnum dataPointEnum, SupplementaryDataPointAdapter.SupplementaryDataPointAdapterInterface dataPointAdapterInterface) {
        if (dataPointEnum != null) {
            dataPointCB.setText(dataPointEnum.name());

            dataPointCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dataPointAdapterInterface.onCheckChanged(isChecked);
                }
            });
        }
    }
}
