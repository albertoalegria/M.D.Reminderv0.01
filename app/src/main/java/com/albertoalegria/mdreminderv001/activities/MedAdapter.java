package com.albertoalegria.mdreminderv001.activities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albertoalegria.mdreminderv001.R;
import com.albertoalegria.mdreminderv001.model.Med;
import com.albertoalegria.mdreminderv001.utils.Constants;

import java.util.ArrayList;

/**
 * Created by alberto on 05/03/17.
 */

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.ViewHolder> {

    private ArrayList<Med> meds;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMedImage;
        public TextView tvMedName;
        public TextView tvMedRepeat;
        public TextView tvMedQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ivMedImage = (ImageView) itemView.findViewById(R.id.ivMedPhoto);
            tvMedName = (TextView) itemView.findViewById(R.id.tv_MedName);
            tvMedRepeat = (TextView) itemView.findViewById(R.id.tv_Repeat);
            tvMedQuantity = (TextView) itemView.findViewById(R.id.tv_MedQuantity);
        }
    }



    public MedAdapter(Context context, ArrayList<Med> meds) {
        this.context = context;
        this.meds = meds;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View medView = inflater.inflate(R.layout.card_view_med, parent, false);



        return new ViewHolder(medView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Med med = meds.get(position);

        ImageView ivMedType = holder.ivMedImage;
        TextView tvMedName = holder.tvMedName;
        TextView tvRepeat = holder.tvMedRepeat;
        TextView tvMedQuantity = holder.tvMedQuantity;

        int repeatInterval = 24 / med.getHours().size();
        String repeat = context.getString(R.string.repeat) + " " + repeatInterval + " " + context.getString(R.string.hours);
        /*String[] quantities = String.valueOf(med.getQuantity()).split(".");
        String quantity;

        if (quantities[1].equals("0")) {
            quantity = quantities[0];
        } else {
            quantity = String.valueOf(med.getQuantity());
        }*/

        if (med.getFirstImagePath().equals(CreateMed.NO_IMAGE)) {
            ivMedType.setImageResource(getResource(med.getType()));
        } else {
            ivMedType.setImageBitmap(BitmapFactory.decodeFile(med.getFirstImagePath()));
        }

        tvMedName.setText(med.getName());
        tvRepeat.setText(repeat);
        //tvMedType.setText(getType(med.getType()));
        tvMedQuantity.setText(String.valueOf(med.getQuantity()) + " " + getMeasure(med.getType()));

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Yo! " + med.getName(), Toast.LENGTH_SHORT).show();
            }
        });*/

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Yo! " + med.getName(), Toast.LENGTH_SHORT).show();

                PopupMenu popup = new PopupMenu(context, holder.itemView);
                popup.inflate(R.menu.med_options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit_med:
                                Toast.makeText(context, "Editing!", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.menu_delete_med:
                                Toast.makeText(context, "Deleting!", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });

                popup.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return meds.size();
    }

    private String getType(int type) {
        switch (type) {
            case Constants.Types.INJECTION:
                return context.getString(R.string.injection);
            case Constants.Types.PILL:
                return context.getString(R.string.pill);
            case Constants.Types.SYRUP:
                return context.getString(R.string.syrup);
            default:
                return "NO_TYPE";
        }
    }

    private String getMeasure(int type) {
        switch (type) {
            case Constants.Types.INJECTION:
                return context.getString(R.string.milliliters);
            case Constants.Types.PILL:
                return context.getString(R.string.pills);
            case Constants.Types.SYRUP:
                return context.getString(R.string.milliliters);
            default:
                return "NO_TYPE";
        }
    }

    private int getResource(int type) {
        switch (type) {
            case Constants.Types.INJECTION:
                return R.drawable.ic_injection;
            case Constants.Types.PILL:
                return R.drawable.ic_pill;
            case Constants.Types.SYRUP:
                return R.drawable.ic_syrup;
            default:
                return R.drawable.ic_pill;
        }
    }
}
