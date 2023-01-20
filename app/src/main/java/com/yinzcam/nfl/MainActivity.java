package com.yinzcam.nfl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;


import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    TextView title;

    private boolean isLinearLayoutManager = true;
    private RecyclerView.LayoutManager layoutManager;

    List<String> home_score = new ArrayList<>();
    List<String> away_score = new ArrayList<>();
    List<String> game_type = new ArrayList<>();
    List<String> opponent = new ArrayList<>();
    List<String> time = new ArrayList<>();
    List<String> day_date = new ArrayList<>();
    List<String> week = new ArrayList<>();
    List<String> team_logo = new ArrayList<>();
    List<String> opponent_logo = new ArrayList<>();
    List<String> card_data_link = new ArrayList<>();
    List<String> tv = new ArrayList<>();
    String main_team_name="",main_team_logo="";

    private ProgressBar loading;
    RequestQueue queue;
    String URL="http://files.yinzcam.com.s3.amazonaws.com/iOS/interviews/ScheduleExercise/schedule.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getSupportActionBar().setTitle("SCHEDULE");
        ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(getApplicationContext());
        LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText("SCHEDULE");
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(30);
        textview.setTypeface(null, Typeface.BOLD);
        //textview.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF09534C"));

        actionbar.setBackgroundDrawable(colorDrawable);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        title = (TextView) findViewById(R.id.title);



        //Internet connection check
        if(isOnline()) {
            queue = Volley.newRequestQueue(MainActivity.this);
            fetchJsonDatas();
        }else{
            Toast.makeText(this,"Please check your internet connection and then try again",Toast.LENGTH_LONG).show();
        }



    }

    private void fetchJsonDatas() {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println(response);


                            JSONObject team_data = response.getJSONObject("Team");
                            main_team_name=team_data.getString("Name");
                            main_team_logo=team_data.getString("TriCode");
                            Log.i("Tricode", team_data.getString("TriCode"));

                            JSONArray jsonArray = response.getJSONArray("GameSection");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject game = jsonArray.getJSONObject(i);
                                JSONArray game_array = game.getJSONArray("Game");
                            for(int j=0;j<game_array.length();j++) {
                                JSONObject details = game_array.getJSONObject(j);

                                String week_string = details.getString("Week");
                                week.add(week_string);
                                tv.add(details.getString("TV"));
                                JSONObject card_obj=details.getJSONObject("CardData");
                                String link=card_obj.getString("ClickthroughURL");
                                card_data_link.add(link);


                                if(details.getString("Type").toString().equals("F") || details.getString("Type").toString().equals("S") ) {
                                    String away = details.getString("AwayScore");
                                    away_score.add(away);
                                    String home = details.getString("HomeScore");
                                    home_score.add(home);
                                    String gametype= details.getString("Type");
                                    game_type.add(gametype);
                                    JSONObject opponent_obj=details.getJSONObject("Opponent");
                                    String tricode=opponent_obj.getString("TriCode");
                                    String opponent_name=opponent_obj.getString("Name");
                                    opponent.add(opponent_name);
                                    opponent_logo.add(tricode);


                                    JSONObject time_obj=details.getJSONObject("Date");
                                    String timestamp=time_obj.getString("Timestamp");
                                   //Converting time
                                    Instant instant = Instant.parse(timestamp);
                                    TimeZone tz = TimeZone.getDefault();
                                    ZoneId country = ZoneId.of(tz.getID());
                                    ZonedDateTime date = instant.atZone(country);
                                   Log.i("oringinal timestamp",timestamp);
                                   Log.i("date",date.toString());
                                   // java.util.Date dayAndDate = new java.util.Date( (long) timestamp * 1000);

                                    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
                                            .withLocale(Locale.getDefault());

                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                            .withLocale(Locale.getDefault());
                                    DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                                            .withLocale(Locale.getDefault());


                                    DateTimeFormatter formatter3 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                            .withLocale(Locale.getDefault());

                                    System.out.println(date.format(formatter1));
                                    System.out.println(date.format(formatter));
                                    System.out.println(date.format(formatter2));
                                    System.out.println(date.format(formatter3));

                                    //time list add
                                    String[] dates;
                                    if(date.format(formatter3).contains(",")) {
                                       dates = date.format(formatter3).split(",");
                                        time.add(dates[1]);
                                    }else{
                                         dates = date.format(formatter3).split(" ");
                                        time.add(dates[1]+" "+dates[2]);
                                    }
                                    System.out.println(dates[1]);



                                    //day and month,date add
                                    Log.i("dates[0]",dates[0]);
                                    String[] day_finding=dates[0].split("/");

                                    String[] day_month = date.format(formatter1).split(",");

                                    System.out.println(day_month[0].trim().substring(0,3)+","+day_month[1].substring(0,4));
                                    day_date.add(day_month[0].trim().substring(0,3)+","+day_month[1].substring(0,4)+" "+day_finding[0]);






                                    /*SimpleDateFormat dateFormatter
                                            = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateString
                                            = day_finding[1] + "-" +day_finding[0] + "-" + day_finding[2];
                                    Log.i("dateString",dateString);
                                    try {
                                        // Parse the String representation of date
                                        // to Date
                                        Date date_find = dateFormatter.parse(dateString);
                                      String  dayOfWeek
                                                =new SimpleDateFormat("EE").format(date_find);
                                        Log.i("dayOfWeek",dayOfWeek);
                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }*/



                                }else if(details.getString("Type").toString().equals("B")){
                                    away_score.add("");
                                    home_score.add("");

                                    opponent.add("");
                                    opponent_logo.add("");
                                    game_type.add("B");
                                    time.add("");
                                    day_date.add("");

                                }

                                Log.i("week", week_string);
                            }


                            }
                            Log.i("team name",main_team_name);
                            //Loading datas into adapter
                            ScoreAdapter mAdapter= new ScoreAdapter(main_team_name,main_team_logo,home_score,opponent,away_score,
                                    week,game_type,tv,opponent_logo,time,card_data_link,day_date,MainActivity.this);
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);

    }
    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}