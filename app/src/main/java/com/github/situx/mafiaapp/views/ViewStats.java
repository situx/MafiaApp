package com.github.situx.mafiaapp.views;

import android.os.Bundle;
import android.widget.ListView;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.PlayerListAdapter;
import com.github.situx.mafiaapp.util.parser.PlayerParse;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by timo on 14.01.14.
 * Class for showing a ranking of all registered players_config.
 */
public class ViewStats extends ViewUtils {
    /**
     * The instance of this class.
     */
    private static ViewStats instance;
    /**
     * PlayerParse object to get a list of players_config.
     */
    private PlayerParse players;
    /**
     * ListViewadapter to display the player ranking.
     */
    private PlayerListAdapter listviewadapter;

    /**
     * Constructor for this class.
     */
    public ViewStats() {
        if (ViewStats.instance == null) {
            this.players = new PlayerParse();
            ViewStats.instance = this;
            this.turn = false;
        } else {
            this.players = ViewStats.instance.players;
            this.listviewadapter = ViewStats.instance.listviewadapter;
            this.turn = true;
        }
    }

    @Override
    public void finish() {
        ViewStats.instance = null;
        super.finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewstats);
        ListView listview = (ListView) this.findViewById(R.id.statPersonView);
        if (!this.turn) {
            SAXParser parser;
            try {
                parser = SAXParserFactory.newInstance().newSAXParser();
                InputStream is = this.getResources().openRawResource(R.raw.players_config);
                parser.parse(is, this.players);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            this.listviewadapter = new PlayerListAdapter(this, this.players.getPlayers());
        }
        listview.setAdapter(this.listviewadapter);
    }
}
