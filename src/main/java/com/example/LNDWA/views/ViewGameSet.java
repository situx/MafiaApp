package com.example.LNDWA.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.GameSetEditAdapter;
import com.example.LNDWA.cards.Event;
import com.example.LNDWA.cards.GameSet;
import com.example.LNDWA.cards.Group;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.views.edit.EditGameSet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by timo on 07.02.14.
 */
public class ViewGameSet extends ViewUtils {
    private static final int EDIT_CHAR = 0;
    private static final int EDIT_EVENT = 2;
    private static final int EDIT_GAMESET = 1;
    private static final int EDIT_GROUP = 3;
    /**The instance of this class.*/
    private static ViewGameSet instance;
    /**The list of GameSets.*/
    private List<GameSet> gamesets;
    /**The adapter to display the GameSets.*/
    private GameSetEditAdapter gameSetEditAdapter;
    /**
     * OnClickListener to add a new GameSet.
     */
    private View.OnClickListener addGameSet=new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent(ViewGameSet.this, EditGameSet.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("gameset", new GameSet());
            intent.putExtras(mBundle);
            ViewGameSet.this.startActivity(intent);
        }
    };

    /**
     * Empty constructor for ViewGameSet.
     * Initializes the instance of this class.
     */
    public ViewGameSet() {
        if(ViewGameSet.instance==null){
           this.turn=false;
           ViewGameSet.instance=this;
        }else{
            this.gamesets =ViewGameSet.instance.gamesets;
            this.gameSetEditAdapter=ViewGameSet.instance.gameSetEditAdapter;
            this.addGameSet=ViewGameSet.instance.addGameSet;
            this.turn=true;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewset);
        if(!turn){
            this.gamesets =new LinkedList<>();
            try {
                this.parseSets();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Collections.sort(this.gamesets, new Comparator<GameSet>() {
                public int compare(GameSet s1, GameSet s2) {
                    return s1.getTitle().compareTo(s2.getTitle());
                }
            });
            this.gameSetEditAdapter=new GameSetEditAdapter(this,this.gamesets);
        }
        ListView setList=(ListView) this.findViewById(R.id.setlistView);
        setList.setAdapter(this.gameSetEditAdapter);
        Button addGameSetButton=(Button) this.findViewById(R.id.addGameSetButton);
        addGameSetButton.setOnClickListener(addGameSet);
    }

    /**
     * Parses existing GameSets and
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void parseSets() throws IOException, ParserConfigurationException, SAXException {
        List<File> filelist=new ArrayList<>();
        File chardir=new File(this.getFilesDir()+"/chars/");
        for(File file:chardir.listFiles()){
            if(file.getName().endsWith("_chars.xml")){
                filelist.add(file);
                Log.e("File: ",file.getAbsolutePath()+"");
            }
        }
        for(File file:filelist){
            this.gamesets.add(new GameSet(file));
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.e("OnActivitiyResult","ViewGameSet "+requestCode);
        Integer gamesetid;
        Log.e("ResultCode",resultCode+"");
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case EDIT_CHAR:
                    Log.e("Cardlist","Case");
                    gamesetid=data.getExtras().getInt("gamesetid");
                    List<Karte> newcardlist=data.getExtras().getParcelableArrayList("cardlist");
                    this.gamesets.get(gamesetid).setCards(newcardlist);

                    break;
                case EDIT_GAMESET:
                    Log.e("Gameset","Case");
                    gamesetid=data.getExtras().getInt("gamesetid");
                    Log.e("Do GameSet stuff","yeah"+gamesetid);
                    GameSet gameSet=data.getExtras().getParcelable("gameset");
                    this.gamesets.get(gamesetid).synchronize(gameSet);
                    break;
                case EDIT_EVENT:
                    Log.e("Event","Case");
                    gamesetid=data.getExtras().getInt("gamesetid");
                    List<Event> neweventlist=data.getExtras().getParcelableArrayList("eventlist");
                    this.gamesets.get(gamesetid).setEvents(neweventlist);
                    break;
                case EDIT_GROUP:
                    Log.e("Group","Case");
                    gamesetid=data.getExtras().getInt("gamesetid");
                    List<Group> newgrouplist=data.getExtras().getParcelableArrayList("grouplist");
                    this.gamesets.get(gamesetid).setGroups(new TreeSet<Group>(newgrouplist));
                    break;
                default:                     Log.e("Default","Case");
            }
        }

    }

    @Override
    public void finish() {
        ViewGameSet.instance=null;
        super.finish();
    }
}
