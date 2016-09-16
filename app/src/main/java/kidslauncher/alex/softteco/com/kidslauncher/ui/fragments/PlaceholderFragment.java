package kidslauncher.alex.softteco.com.kidslauncher.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.percent.PercentFrameLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kidslauncher.alex.softteco.com.kidslauncher.R;
import kidslauncher.alex.softteco.com.kidslauncher.ui.models.AppItemModel;

public class PlaceholderFragment extends Fragment implements
        View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_models";

    private int[] ids = {
            R.id.percent1, R.id.percent2, R.id.percent3,
            R.id.percent4, R.id.percent5, R.id.percent6,
            R.id.percent7, R.id.percent8, R.id.percent9,
            R.id.percent10, R.id.percent11, R.id.percent12
    };

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(ArrayList<AppItemModel> models) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SECTION_NUMBER, models);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        PercentFrameLayout rootView = (PercentFrameLayout) inflater.inflate(R.layout.fragment_main22, container, false);

        Adapter adapter = new Adapter(getArguments().getParcelableArrayList(ARG_SECTION_NUMBER), ids, rootView);


        return rootView;
    }

    static class Adapter {

        private ArrayList<AppItemModel> models;
        private int[] ids;
        private ViewGroup container;
        private Context context;

        public Adapter(ArrayList<AppItemModel> models, int[] ids, ViewGroup container) {
            this.models = models;
            this.ids = ids;
            this.container = container;
            this.context = container.getContext();
            init();
        }

        private void init() {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (ids.length != models.size()) {
//                throw new IllegalArgumentException();
//            }

            for (int position = 0; position < models.size(); position++) {
                final AppItemModel appItemModel = models.get(position);

                View item = inflater.inflate(R.layout.item_app_card, container, false);
                TextView label = (TextView) item.findViewById(R.id.label);
                ImageView icon = (ImageView) item.findViewById(R.id.icon);

                label.setText(appItemModel.getLabel());
                try {
                    icon.setImageDrawable(context.getPackageManager().getApplicationIcon(appItemModel.getPackageName()));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                item.setOnClickListener((view) -> {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(appItemModel.getPackageName());
                    context.startActivity(intent);
                });
                ((FrameLayout) container.findViewById(ids[position])).addView(item);
            }

        }

    }

    @Override
    public void onClick(View view) {

    }
}
