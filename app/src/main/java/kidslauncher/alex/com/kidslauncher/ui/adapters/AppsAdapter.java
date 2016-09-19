package kidslauncher.alex.com.kidslauncher.ui.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private List<AppItemModel> mModels;
    private OnClickListener mClickListener;
    private Context mContext;
    private int selectedCount;

    public AppsAdapter(List<AppItemModel> mModels, OnClickListener mClickListener, Context mContext) {
        this.mModels = mModels;
        this.mClickListener = mClickListener;
        this.mContext = mContext;
    }

    public List<AppItemModel> getModels() {
        return mModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AppItemModel appItemModel = mModels.get(position);
        holder.textView.setText(appItemModel.getLabel());
        holder.selectedImage.setVisibility(appItemModel.isSelected() ? View.VISIBLE : View.GONE);
        try {
            holder.imageView.setImageDrawable(mContext.getPackageManager().getApplicationIcon(appItemModel.getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener((view) -> {
            final boolean selected = !appItemModel.isSelected();
            if (selected) {
                selectedCount++;
            } else {
                selectedCount--;
            }
            appItemModel.setSelected(selected);
            holder.selectedImage.setVisibility(selected ? View.VISIBLE : View.GONE);
            if (mClickListener != null) {
                mClickListener.onAppItemClick(selectedCount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void setModels(List<AppItemModel> models) {
        this.mModels = models;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private ImageView selectedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.label);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
            selectedImage = (ImageView) itemView.findViewById(R.id.selected_image);
        }

    }

    public interface OnClickListener {
        void onAppItemClick(int selectedCount);
    }

}