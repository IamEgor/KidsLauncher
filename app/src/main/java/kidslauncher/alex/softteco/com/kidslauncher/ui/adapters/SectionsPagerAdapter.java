package kidslauncher.alex.softteco.com.kidslauncher.ui.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import kidslauncher.alex.softteco.com.kidslauncher.ui.fragments.PlaceholderFragment;
import kidslauncher.alex.softteco.com.kidslauncher.ui.models.AppItemModel;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int ELEMENTS_COUNT = 12;
    private final int tabs_count;

    private List<AppItemModel> mModels;

    public SectionsPagerAdapter(FragmentManager fm, List<AppItemModel> models) {
        super(fm);
        this.mModels = models;
        tabs_count = (int) Math.ceil(1. * models.size() / ELEMENTS_COUNT);
    }

    @Override
    public Fragment getItem(int position) {
        int elementsCount = mModels.size();

        int start = position * ELEMENTS_COUNT;
        int finish = start + ELEMENTS_COUNT < elementsCount ? start + ELEMENTS_COUNT : elementsCount;

        return PlaceholderFragment.newInstance(new ArrayList<>(mModels.subList(start, finish)));
    }

    @Override
    public int getCount() {
        return tabs_count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}