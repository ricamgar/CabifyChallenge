package com.ricamgar.challenge.presentation.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricamgar.challenge.R;
import com.ricamgar.challenge.domain.model.Estimate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EstimatesAdapter extends RecyclerView.Adapter<EstimatesAdapter.ViewHolder> {

    private final Picasso picasso;
    private List<Estimate> estimates = new ArrayList<>();

    @Inject
    EstimatesAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    public void addEstimates(List<Estimate> estimates) {
        this.estimates = estimates;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estimate_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Estimate estimate = estimates.get(position);
        holder.type.setText(estimate.vehicleType.shortName);
        holder.price.setText(estimate.priceFormatted);
        picasso.load(estimate.vehicleType.icons.regular)
                .placeholder(R.drawable.vehicle_placeholder)
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return estimates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.estimate_icon)
        ImageView icon;
        @BindView(R.id.estimate_type)
        TextView type;
        @BindView(R.id.estimate_price)
        TextView price;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
