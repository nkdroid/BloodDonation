package com.nkdroid.blooddonation;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class SearchActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView postListView;
    private ArrayList<String> postArrayList;
    private PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setToolbar();
        postArrayList=new ArrayList<String>();
        postArrayList.add("title 1");
        postArrayList.add("title 2");
        postArrayList.add("title 3");
        postArrayList.add("title 4");
        postArrayList.add("title 5");
        postArrayList.add("title 1");
        postArrayList.add("title 2");
        postArrayList.add("title 3");
        postArrayList.add("title 4");
        postArrayList.add("title 5");
        postArrayList.add("title 1");
        postArrayList.add("title 2");
        postArrayList.add("title 3");
        postArrayList.add("title 4");
        postArrayList.add("title 5");
        postArrayList.add("title 1");
        postArrayList.add("title 2");
        postArrayList.add("title 3");
        postArrayList.add("title 4");
        postArrayList.add("title 5");
        postListView=(ListView)findViewById(R.id.listView);
        postAdapter=new PostAdapter(SearchActivity.this,postArrayList);
        postListView.setAdapter(postAdapter);
    }

    public class PostAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> postArrayList;
        ArrayList<String> arraylist;


        public PostAdapter(Context context, ArrayList<String> postArrayList) {

            this.context = context;
            this.postArrayList = postArrayList;
            arraylist = new ArrayList<String>();
            arraylist.addAll(postArrayList);
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
            TextView txtPostTitle,txtPostDate,txtPostDescription;

        }

        public View getView(final int position, View convertView,ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_notificationview, parent, false);
                holder = new ViewHolder();
                holder.txtPostTitle = (TextView) convertView.findViewById(R.id.textView);
                holder.txtPostDate = (TextView) convertView.findViewById(R.id.textView2);
                holder.txtPostDescription = (TextView) convertView.findViewById(R.id.textView3);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtPostTitle.setText(postArrayList.get(position));
//            holder.txtPostDate.setText(postArrayList.get(position).getPost_date());
//            holder.txtPostDescription.setText(postArrayList.get(position).getDescription());


            return convertView;
        }
        // Filter Class
        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            postArrayList.clear();
            if (charText.length() == 0) {
                postArrayList.addAll(arraylist);

            } else {
                for (String movieDetails : arraylist) {
                    if (charText.length() != 0 && movieDetails.toLowerCase(Locale.getDefault()).contains(charText)) {
                        postArrayList.add(movieDetails);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Request Blood");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MainActivity.this, "called", Toast.LENGTH_SHORT).show();



                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                postAdapter.filter(searchQuery.toString().trim());
                postListView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_bar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
