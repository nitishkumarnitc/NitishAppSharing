package com.example.nitish.nitishappsharing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nitish on 08-07-2016.
 */

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {
    private List<ApplicationInfo> appsList = null;
    private ArrayList<ApplicationInfo> tempAppsList=null;
    private Context context;
    private PackageManager packageManager;
    ArrayList<Integer> checkedItemPosition=new ArrayList<Integer>();

    public ArrayList<Integer> getCheckedItemPositionArray(){
        return  checkedItemPosition;
    }

    public ApplicationAdapter(Context context, int textViewResourceId,
                              ArrayList<ApplicationInfo> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        this.tempAppsList=new ArrayList<ApplicationInfo>();
        this.tempAppsList.addAll(appsList);
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {

            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.sigle_row_of_list, null);
        }

        final ApplicationInfo applicationInfo = appsList.get(position);

        if (null != applicationInfo) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            //TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
            final CheckBox checkBox=(CheckBox) view.findViewById(R.id.select_button);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()) checkedItemPosition.add(position);
                    else if(checkBox.isChecked()==false) {
                        checkedItemPosition.remove(Integer.valueOf(position));
                    }




                }
            });

            appName.setText(applicationInfo.loadLabel(packageManager));



            // packageName.setText(applicationInfo.packageName);
            iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));


        }


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        appsList.clear();
        if (charText.length() == 0) {
            appsList.addAll(tempAppsList);
        }
        else
        {
            for (ApplicationInfo appLicationInfo : tempAppsList)
            {

                if (appLicationInfo.loadLabel(packageManager).toString().toLowerCase().contains(charText)){
                    appsList.add(appLicationInfo);
                }
            }
        }
        notifyDataSetChanged();
    }
}