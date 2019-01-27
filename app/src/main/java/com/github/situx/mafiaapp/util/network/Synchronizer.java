package com.github.situx.mafiaapp.util.network;

import android.content.Context;
import android.util.Log;

import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.cards.Player;
import com.github.situx.mafiaapp.util.parser.CharParse;
import com.github.situx.mafiaapp.util.parser.PlayerParse;
import com.github.situx.mafiaapp.util.parser.SyncParse;

import org.apache.http.conn.ClientConnectionManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by timo on 14.03.14.
 */
public class Synchronizer  {
    private NetworkHandler handler;

    private SyncParse syncparse;

    private CharParse gameSetParse;

    private String errormessage;

    public Boolean getErrorflag() {
        return errorflag;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrorflag(final Boolean errorflag) {
        this.errorflag = errorflag;
    }

    private Boolean errorflag=false;

    private ClientConnectionManager manager;

    public Synchronizer(){
        this.handler=new NetworkHandler(1000);
        this.manager=this.handler.createClientConnectionManager();
        this.syncparse=new SyncParse();
    }



    public void sendGameResults(final String game,Context context){
        Log.e("URL", "http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/gameresults");
        List<String> params=new LinkedList<>();
        params.add(game);
        SynchronizerTask task= new SynchronizerTask(handler,context);
        task.setPostparams(params);
        task.execute(new String[]{"http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/gameresults", ""});
    }

    public void getStatisticsFromServer(){

    }

    public List<Player> loadPlayers(Context context) {
        Log.e("URL", "http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/getplayers");
        String res;
        try {
            res = new SynchronizerTask(handler,context).execute(new String[]{"http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/getplayers",""})
                    .get();
            Log.e("Result",res);
            PlayerParse playerParse=new PlayerParse();
            Log.e("Players in Sync",playerParse.parsePlayer(new InputSource(res)).toString());
            if(playerParse.getError()){
                playerParse.setError(false);
                this.errorflag=true;
                this.errormessage=this.syncparse.getErrormessage();
                return new LinkedList<>();
            }
            return playerParse.parsePlayer(new InputSource(res));
        } catch (InterruptedException e) {
            this.errorflag=true;
            this.errormessage=this.syncparse.getErrormessage();
        } catch (ExecutionException e) {
            this.errorflag=true;
            this.errormessage=this.syncparse.getErrormessage();
        }
        return new LinkedList<>();
    }



    public String[] getGameSetList(String locale,Context context) {
        Log.e("URL", "http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/gamesetlist/" + locale);
        try {
            String res=new SynchronizerTask(handler,context).execute(new String[]{"http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/gamesetlist/" + locale,""})
                    .get();
            Log.e("Result",res);
            List<String> resultlist=this.syncparse.parseGameSetList(res);
            if(this.syncparse.isError()){
                this.syncparse.setError(false);
                this.errorflag=true;
                this.errormessage=this.syncparse.getErrormessage();
                return new String[]{this.syncparse.getErrormessage()};
            }
            Log.e("Result",resultlist.toString());
            return resultlist.toArray(new String[resultlist.size()]);
        } catch (ParserConfigurationException e) {
            this.errorflag=true;
            return new String[]{this.syncparse.getErrormessage()};
        } catch (SAXException e) {
            this.errorflag=true;
            return new String[]{this.syncparse.getErrormessage()};
        } catch (InterruptedException e) {
            this.errorflag=true;
            return new String[]{this.syncparse.getErrormessage()};
        } catch (ExecutionException e) {
            this.errorflag=true;
            return new String[]{this.syncparse.getErrormessage()};
        } catch (IOException e) {
            this.errorflag=true;
            return new String[]{this.syncparse.getErrormessage()};
        }
    }

    /**
     * Loads a GameSet from the REST service.
     * @param gamesetName the name of the gameset
     * @param context the application context
     * @return
     */
    public GameSet loadGameSet(final String gamesetName,String locale,final Context context) throws ExecutionException, InterruptedException, ParserConfigurationException, SAXException, IOException {
        Log.e("URL", "http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/gameset/"+locale +"/"+gamesetName.replace(" ","_").toLowerCase());
            String res=new SynchronizerTask(handler,context).execute(new String[]{"http://192.168.220.100:8080/LNDWAWeb/rest/lndwa/gameset/"+locale +"/"+gamesetName.replace(" ","_").toLowerCase(),""})
                    .get();
            Log.e("Result",res);
            GameSet result=new GameSet(res);
            return result;
    }

    public void publishGameSet(final GameSet gameSet){

    }
}
