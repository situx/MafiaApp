package com.example.LNDWA.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.example.LNDWA.*;
import com.example.LNDWA.adapters.ResultListAdapter;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.cards.Player;
import com.example.LNDWA.util.network.Synchronizer;
import com.example.LNDWA.util.parser.PlayerParse;
import com.example.LNDWA.util.SaveEnum;
import com.example.LNDWA.util.Utils;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 25.11.13
 * Time: 18:15
 * Displays the results of a Game in a listview and allows to assign points to individual players_config.
 */
public class Results extends ViewUtils {
    private String gamesetid;
    private String language;
    /**
     * PlayerParse object to parse the list of players_config.
     */
    private PlayerParse players;
    /**
     * The list of cards.
     */
    private List<Karte> cardlist;
    /**
     * The icon of the GameSet to be used in the ActionBar.
     */
    private String icon;

    private static Results instance;
    private ArrayList<Player> winnerlist;

    private ResultListAdapter adapter;

    /**
     * Constructor for this class initializing the playerlist.
     * @throws IOException on error
     * @throws SAXException on error
     * @throws ParserConfigurationException on error
     */
    public Results() throws IOException, SAXException, ParserConfigurationException {
        if(Results.instance==null){

            this.players=new PlayerParse();
            Results.instance=this;
            this.turn=false;
        }else{
            this.players=Results.instance.players;
            this.cardlist=Results.instance.cardlist;
            this.icon=Results.instance.icon;
            this.winnerlist=Results.instance.winnerlist;
            this.turn=true;
        }

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewresults);
        Button button=(Button)this.findViewById(R.id.finishbutton);
        ListView listview=(ListView) this.findViewById(R.id.statListView);
        if(!turn){
        SAXParser parser;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
            InputStream is = this.getResources().openRawResource(R.raw.players_config);
            parser.parse(is,this.players);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Bundle bundle=this.getIntent().getExtras();
        this.setTitle(this.getResources().getString(R.string.results));
        this.language=bundle.getString("language");
        this.gamesetid=bundle.getString("gamesetid");
        File file=new File(this.getFilesDir().getAbsolutePath()+"/"+this.language+"_"+this.gamesetid+"/"+this.language+"_"+this.gamesetid+".png");
        if(file.exists()){
            this.getSupportActionBar().setIcon(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(), this.language + "_" + this.gamesetid, this.language + "_" + this.gamesetid + ".png"));
        }else{
            this.getSupportActionBar().setIcon(R.drawable.title);
        }
        this.cardlist= bundle.getParcelableArrayList("cards");
        this.winnerlist=bundle.getParcelableArrayList("winners");

        this.adapter=new ResultListAdapter(this,this.players.getPlayers(),this.cardlist,bundle.getString("winninggroup"),listview,this.winnerlist,this.language,this.gamesetid);
        }
        listview.setAdapter(this.adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    Utils.saveFileToInternalStorage(Results.this.players.getPlayers(), SaveEnum.PLAYER,new File(Results.this.getFilesDir()+"/"+Utils.PLAYERFILE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Intent intent = new Intent(Results.this,LNDWA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putBoolean("new", true);
                intent.putExtras(mBundle);
                Results.this.startActivity(intent);
                Results.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0,0,0,this.getResources().getString(R.string.about));
        menu.add(0,0,0,this.getResources().getString(R.string.sendgameresults));
        menu.add(0,0,0,this.getResources().getString(R.string.sendgameresults)+" 2");
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Utils.dialogtext=Results.this.
                        getResources().getString(R.string.copyright);
                Results.this.chooseDialogs(DialogEnum.ABOUT);
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Synchronizer sync=new Synchronizer();
                sync.sendGameResults(Results.this.gameResultsToXML(),Results.this);
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {

                String to = "test@test.de";
                String subject = Results.this.getResources().getString(R.string.gameresult)+" ";
                String message = Results.this.getResources().getString(R.string.gameresult)+" ";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                // email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                // email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                // need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                return true;
            }
        });
        return true;
    }

    /**
     * GameResults as an xml representation.
     * @return
     */
    public String gameResultsToXML(){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "GameResult");
            serializer.attribute("","gameid", UUID.randomUUID().toString());
            serializer.attribute("","players",this.cardlist.size()+"");
            serializer.attribute("","gameset","");
            serializer.attribute("","winninggroup",this.cardlist.get(0).getGroup().getGroupId());
            int i=0;
            for(Player winner:this.winnerlist){
                serializer.startTag("","game");
                serializer.attribute("", "character", this.cardlist.get(i).getName());
                serializer.attribute("","player",winner.toString());
                serializer.attribute("","points",this.cardlist.get(i).getWinningAlive().toString());
                serializer.attribute("","won",Boolean.toString(true));
                serializer.endTag("","game");
            }
            serializer.endTag("", "GameResult");
            serializer.flush();
            Log.e("GameResult: ", writer.toString());
            writer.toString();
            //this.openFileOutput("save-"+System.currentTimeMillis()+"",0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
