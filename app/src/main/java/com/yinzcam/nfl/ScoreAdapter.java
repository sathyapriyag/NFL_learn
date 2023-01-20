package com.yinzcam.nfl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    public Context con;
    LinearLayout score_container;

    public TextView team_name, opponent_team, date,time,week,tv,home_score,away_score,bye;
    public View layout;
    public ImageView team_logo,opponent_logo,attchment;
    public String main_team_name,main_team_logo;

    List<String> home_score_list = new ArrayList<>();
    List<String> away_score_list = new ArrayList<>();
    List<String> game_type_list = new ArrayList<>();
    List<String> opponent_list = new ArrayList<>();
    List<String> time_list = new ArrayList<>();
    List<String> date_list = new ArrayList<>();
    List<String> week_list = new ArrayList<>();
    List<String> team_logo_list = new ArrayList<>();
    List<String> opponent_logo_list = new ArrayList<>();
    List<String> card_data_link_list = new ArrayList<>();
    List<String> tv_list = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
            layout = v;
            team_name = (TextView) v.findViewById(R.id.team_name);
            opponent_team = (TextView) v.findViewById(R.id.opponent_team);
            date = (TextView) v.findViewById(R.id.date);
            time = (TextView) v.findViewById(R.id.time);
            week = (TextView) v.findViewById(R.id.season);
            tv = (TextView) v.findViewById(R.id.tv);
            home_score = (TextView) v.findViewById(R.id.home_score);
            away_score = (TextView) v.findViewById(R.id.away_score);

            team_logo = (ImageView) v.findViewById(R.id.team_logo);
            opponent_logo = (ImageView) v.findViewById(R.id.opponent_logo);
            attchment = (ImageView) v.findViewById(R.id.card_link);

            score_container=(LinearLayout) v.findViewById(R.id.score_container);
            bye = (TextView) v.findViewById(R.id.bye);



        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ScoreAdapter(String main_team,String main_logo,List<String> home,List<String> opponent, List<String> away,List<String> week,List<String> gametype,List<String> tv,
                        List<String> opponent_logo,List<String> time,List<String> card_data,List<String> day_date, Context main) {

        con = main;
        main_team_name=main_team;
        opponent_list=opponent;
        home_score_list=home;
        away_score_list=away;
        week_list=week;
        game_type_list=gametype;
        tv_list=tv;
        main_team_logo=main_logo;
        opponent_logo_list=opponent_logo;
        time_list=time;
        card_data_link_list=card_data;
        date_list=day_date;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.score_container, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewhold = new ViewHolder(v);
        return viewhold;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Shows BYE text
        if(game_type_list.get(position).equals("B")){
            score_container.setVisibility(View.GONE);
            bye.setVisibility(View.VISIBLE);
        }
        else
        {
            //Setting values for final and scheduled game
            time.setText("Final");
            score_container.setVisibility(View.VISIBLE);
            bye.setVisibility(View.GONE);
            team_name.setText(main_team_name);
            opponent_team.setText(opponent_list.get(position));
            week.setText(week_list.get(position));
            Log.i("tv value",tv_list.get(position));
            tv.setText(tv_list.get(position));
            Log.i("tv value after",tv.getText().toString());
            home_score.setText(home_score_list.get(position));
            away_score.setText(away_score_list.get(position));
            date.setText(date_list.get(position));
            home_score.setTextColor(con.getResources().getColor(R.color.black));
            home_score.setTextSize(32);
            home_score.setTypeface(null, Typeface.BOLD);
            away_score.setTextColor(con.getResources().getColor(R.color.black));
            away_score.setTextSize(32);
            away_score.setTypeface(null, Typeface.BOLD);
            if(game_type_list.get(position).equals("S"))
            {
                home_score.setText("0-0");
                away_score.setText("0-0");
                home_score.setTextColor(con.getResources().getColor(R.color.grey));
                home_score.setTextSize(14);
                home_score.setTypeface(null, Typeface.NORMAL);
                away_score.setTextColor(con.getResources().getColor(R.color.grey));
                away_score.setTextSize(14);
                away_score.setTypeface(null, Typeface.NORMAL);
                time.setText(time_list.get(position));

            }/*else{

                home_score.setTextColor(con.getResources().getColor(R.color.black));
                home_score.setTextSize(32);
                home_score.setTypeface(null, Typeface.BOLD);
                away_score.setTextColor(con.getResources().getColor(R.color.black));
                away_score.setTextSize(32);
                away_score.setTypeface(null, Typeface.BOLD);
            }*/

            attchment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    {
                        Log.i("card", card_data_link_list.get(holder.getAdapterPosition()));
                         Uri uri = Uri.parse(card_data_link_list.get(holder.getAdapterPosition()));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(con.getPackageManager()) != null) {
                               con.startActivity(intent);
                        }
                    }

                }
            });

            Picasso.get()
                    .load("https://s3.amazonaws.com/yc-app-resources/nfl/logos/nfl_"+main_team_logo.toLowerCase()+"_light.png")
                    .into(team_logo);
            Picasso.get()
                    .load("https://s3.amazonaws.com/yc-app-resources/nfl/logos/nfl_"+opponent_logo_list.get(position).toLowerCase()+"_light.png")
                    .into(opponent_logo);

        }









    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return game_type_list.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

}

