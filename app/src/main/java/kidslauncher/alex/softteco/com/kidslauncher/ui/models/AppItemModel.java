package kidslauncher.alex.softteco.com.kidslauncher.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AppItemModel implements Parcelable {

    public static final Parcelable.Creator<AppItemModel> CREATOR = new Parcelable.Creator<AppItemModel>() {
        @Override
        public AppItemModel createFromParcel(Parcel source) {
            return new AppItemModel(source);
        }

        @Override
        public AppItemModel[] newArray(int size) {
            return new AppItemModel[size];
        }
    };

    private String packageName;
    private String label;
    private boolean selected;

    public AppItemModel() {
    }

    public AppItemModel(String packageName, String label) {
        this.packageName = packageName;
        this.label = label;
    }

    public AppItemModel(String packageName, String label, boolean selected) {
        this.packageName = packageName;
        this.label = label;
        this.selected = selected;
    }

    protected AppItemModel(Parcel in) {
        this.packageName = in.readString();
        this.label = in.readString();
        this.selected = in.readByte() != 0;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.label);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    @Override
    public String toString() {
        return "AppItemModel{" +
                "packageName='" + packageName + '\'' +
                ", label='" + label + '\'' +
                ", selected=" + selected +
                '}';
    }
}