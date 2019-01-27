package com.github.situx.mafiaapp.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 16.03.14.
 */
public class SynchronizerTask extends AsyncTask<String, Void, String> {

    private final NetworkHandler handler;
    private final Context context;

    private List<String> postparams;

    public SynchronizerTask(NetworkHandler handler, Context context){
        this.handler=handler;
        this.context=context;
        this.postparams=new LinkedList<>();
    }

    public List<String> getPostparams() {
        return postparams;
    }

    public void setPostparams(List<String> params) {
        this.postparams=params;
    }


    @Override
    protected String doInBackground(String... urls) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    String result=this.handler.getData(urls[0],urls[1].length()==0,postparams);

                    return result;
                } catch (IOException e) {
                    return "<?xml version=\"1.0\"?><error>"+e.getMessage()+"</error>";
                } finally{
                    this.postparams.clear();
                }
            } else {
                return "<?xml version=\"1.0\"?><error>No Network Connection!</error>";
            }

        }
}
