package com.example.LNDWA.views;

import android.os.Bundle;
import android.widget.ListView;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.GameAdapter;
import com.example.LNDWA.cards.Game;

import java.util.List;

/**
 * Created by timo on 15.01.14.
 * Views GameStats calculated from already finished games.
 */
public class GameStats extends ViewUtils {

    /**
     * Constructor for this class.
     */
    public GameStats() {

    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewstats);
        this.setTitle(this.getIntent().getExtras().getString("name") + " " + this.getResources().getString(R.string.statistics));
        final List<Game> games = this.getIntent().getExtras().getParcelableArrayList("games");
        ListView listview = (ListView) this.findViewById(R.id.statPersonView);
        listview.setAdapter(new GameAdapter(this, games));

    }

}
