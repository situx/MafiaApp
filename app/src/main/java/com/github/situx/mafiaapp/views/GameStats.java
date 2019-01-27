package com.github.situx.mafiaapp.views;

import android.os.Bundle;
import android.widget.ListView;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.GameAdapter;
import com.github.situx.mafiaapp.cards.Game;
import com.github.situx.mafiaapp.views.ViewUtils;

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
