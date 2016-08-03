package com.example.nitish.nitishappsharing;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppList extends AppCompatActivity implements View.OnClickListener {

    private PackageManager packageManager = null;
    private ArrayList<ApplicationInfo> applist = null;
    private ApplicationAdapter applicationAdapter = null;

    private ListView listView=null;
    private EditText search;
    //private SearchView search;
   // private Button shareButton;
    private ImageView shareButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        packageManager=getPackageManager();
        search=(EditText) findViewById(R.id.search);
        listView=(ListView)findViewById(R.id.listview_for_pp);
       // shareButton=(Button) findViewById(R.id.share_button);
        shareButton=(ImageView) findViewById(R.id.share_button);
        shareButton.setOnClickListener(this);
        new LoadApplications().execute();

    }

    private ArrayList<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    @Override
    public void onClick(View view) {
        ArrayList<Integer> checked = applicationAdapter.getCheckedItemPositionArray();
        // ArrayList<ApplicationInfo> checkedApplication=new ArrayList<ApplicationInfo>();
        ArrayList<Uri> selectedApps = new ArrayList<Uri>(checked.size());
        ArrayList<ApplicationInfo> applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));

        //Log.d("Number of checked Item",String.valueOf(checked.size()));

        if(checked.size()==0) {
            Toast.makeText(getApplicationContext(),"Please Select at Least One App to Share",Toast.LENGTH_LONG).show();
            Log.d("selected list size",String.valueOf(checked.size()));
           // return;
        }
        else {
            for (int i = 0; i < checked.size(); i++) {
                // Item position in adapter
                int position = checked.get(i);
                // Add sport if it is checked i.e.) == TRUE!
                if (checked.get(i)!=-1) {

                    //File srcFile = new File(applist.get(position).publicSourceDir);
                    File srcFile = new File(applicationAdapter.getItem(position).publicSourceDir);
                    selectedApps.add(Uri.fromFile(srcFile));
                }
            }

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND_MULTIPLE);
            share.setType("application/vnd.android.package-archive");
            share.putParcelableArrayListExtra(Intent.EXTRA_STREAM,selectedApps);
            AppList.this.startActivity(Intent.createChooser(share, "Sharing"));

        }

    }


      private  class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            applicationAdapter = new ApplicationAdapter(AppList.this,
                    R.layout.sigle_row_of_list, applist);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            listView.setAdapter(applicationAdapter);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    String appName=search.getText().toString().toLowerCase(Locale.getDefault());
                    applicationAdapter.filter(appName);
                }
            });


            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AppList.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
