package com.nkdroid.blooddonation;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nkdroid.blooddonation.model.BloodBank;
import com.nkdroid.blooddonation.model.UserClass;

import java.util.ArrayList;
import java.util.Locale;


public class SearchResultActivity extends ActionBarActivity {

    private ListView searchRequest;
    private ArrayList<UserClass> searchUsers;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchRequest= (ListView) findViewById(R.id.searchRequest);
        searchUsers=PrefUtils.getSearchUsers(SearchResultActivity.this).searchUserList;
        postAdapter=new PostAdapter(SearchResultActivity.this,searchUsers);
        searchRequest.setAdapter(postAdapter);

    }

    public class PostAdapter extends BaseAdapter {

        Context context;
        ArrayList<UserClass> postArrayList;



        public PostAdapter(Context context, ArrayList<UserClass> postArrayList) {

            this.context = context;
            this.postArrayList = postArrayList;

        }

        public int getCount() {
            return postArrayList.size();
        }

        public Object getItem(int position) {
            return postArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }



        class ViewHolder {
            TextView txtPostTitle,mobile,address,email;

        }

        public View getView(final int position, View convertView,ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_notificationview, parent, false);
                holder = new ViewHolder();
                holder.txtPostTitle = (TextView) convertView.findViewById(R.id.textView);
                holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.email = (TextView) convertView.findViewById(R.id.email);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtPostTitle.setText(postArrayList.get(position).name);
            holder.mobile.setText(postArrayList.get(position).contact);
            holder.address.setText(postArrayList.get(position).address);
            holder.email.setText(postArrayList.get(position).email);

            return convertView;
        }

    }

}
