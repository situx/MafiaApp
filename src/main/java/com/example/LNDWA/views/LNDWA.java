package com.example.LNDWA.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.SetAdapter;
import com.example.LNDWA.cards.Ability;
import com.example.LNDWA.cards.GameSet;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.cards.Preset;
import com.example.LNDWA.util.SaveEnum;
import com.example.LNDWA.util.Tuple;
import com.example.LNDWA.util.network.Synchronizer;
import com.example.LNDWA.util.parser.PresetParse;
import com.example.LNDWA.util.Utils;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Main View of this project.
 */
public class LNDWA extends ViewUtils {
    /**
     * The instance of this class.
     */
    private static LNDWA instance;
    /**
     * The currently selected entry of the spinner.
     */
    private Tuple<String,String> currentselection;
    /**
     * The currentposition in the spinner to remember.
     */
    private Integer currentpos;
    /**
     * The adapter for displaying sets in a spinner.
     */
    private SetAdapter adapter;
    /**
     * List of gameset files available.
     */
    private List<String> filelist;
    /**
     * Pretty formatted list of gameset files.
     */
    private List<Tuple<String,String>> prettyfilelist;
    /**Constructor for this class.

     */
    public LNDWA() {
        if (LNDWA.instance == null) {
            this.filelist = new ArrayList<>();
            this.prettyfilelist = new ArrayList<>();
            this.turn = false;
        } else {
            this.filelist = LNDWA.instance.filelist;
            this.prettyfilelist = LNDWA.instance.prettyfilelist;
            this.turn = true;
        }
    }

    @Override
    public void finish() {
        LNDWA.instance = null;
        super.finish();
    }

    /**
     * Gets save games by scanning the save game folder.
     *
     * @return the list of savegames
     */
    public String[] getSaveGames() {
        File savegamefolder = new File(this.getFilesDir() + "/savegames");
        String[] filelists = new String[savegamefolder.listFiles().length];
        File[] listfiles = savegamefolder.listFiles();
        Log.e("listfiles", filelists[0] + "");
        for (int i = 0; i < listfiles.length; i++) {
            filelists[i] = listfiles[i].getName();
        }
        return filelists;
    }



    /**
     * Installs file to internal storage on first installation of the program.
     * @param suffix the suffix of the files to install
     * @param directory the directory to install into
     */
    public void installFiles(final String suffix,final File directory){
        this.filelist.clear();
        Field[] fields = R.raw.class.getFields();
        String filestring = "";
        for (final Field field : fields) {
            if (field.getName().endsWith(suffix)) {
                filelist.add(field.getName().substring(0, field.getName().lastIndexOf('_')));
                Log.i("Raw Asset: ", field.getName().substring(0, field.getName().lastIndexOf('_')));
            }
        }
        directory.mkdir();
        for (String filename : filelist) {
            Log.e("Filename",filename);
            InputStream is = this.getResources().openRawResource(getResources().getIdentifier("raw/" + filename+suffix
                    , "raw", getPackageName()));
            try {
                if(filename.charAt(0)=='_'){
                    filename=filename.substring(1);
                    filename=filename.replaceAll("_","-");
                }else{
                    filename=filename.replaceAll("_","-").replaceFirst("-","_");
                }
                File file = new File(directory + "/" + filename+suffix + ".xml");
                filestring += file.getAbsolutePath() + "\n" + file.exists() + "\n";
                Utils.copy(is, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a list of GameSets out of already existing GameSets.
     * @param suffix the suffix for the GameSets
     */
    public void createGameSetList(final String suffix){
        Field[] fields = R.raw.class.getFields();
        for (final Field field : fields) {
            if (field.getName().endsWith(suffix)) {
                filelist.add(field.getName().substring(0, field.getName().lastIndexOf('_')));
                Log.i("Raw Asset: ", field.getName());
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Utils.filesdir=this.getFilesDir().getAbsolutePath();
        //TextView startView=(TextView)this.findViewById(R.id.startView);
        /*
      The spinner element.
     */

        if (!turn) {
            final Spinner spinner = (Spinner) LNDWA.this.findViewById(R.id.gamespinner);
            final ProgressDialog pd=new ProgressDialog(this);
            AsyncTask task = new AsyncTask() {

                @Override
                protected Object doInBackground(final Object[] objects) {
                    LNDWA.this.createFolders();
                    Collections.sort(filelist);
                    for (String file : filelist) {
                        LNDWA.this.createImgFoldersForGameSet(file);
                        try {
                            prettyfilelist.add(LNDWA.this.getGameSetName(file.replaceAll("_","-").replaceFirst("-","_")));
                        } catch (FileNotFoundException e) {
                            prettyfilelist.add(new Tuple<String,String>(file.replaceAll("_","-").replaceFirst("-","_"),file.replaceAll("_","-").replaceFirst("-","_")));
                        } catch (XmlPullParserException e) {
                            prettyfilelist.add(new Tuple<String,String>(file.replaceAll("_","-").replaceFirst("-","_"),file.replaceAll("_","-").replaceFirst("-","_")));
                        } catch (IOException e) {
                            prettyfilelist.add(new Tuple<String,String>(file.replaceAll("_","-").replaceFirst("-","_"),file.replaceAll("_","-").replaceFirst("-","_")));
                        }
                    }
                    Collections.sort(prettyfilelist, new Comparator<Tuple<String,String>>() {
                        public int compare(Tuple<String,String> s1, Tuple<String,String> s2) {
                            return s1.getOne().compareTo(s2.getOne());
                        }
                    });
                    LNDWA.this.adapter = new SetAdapter(LNDWA.this, LNDWA.this.prettyfilelist);
                    LNDWA.this.currentselection = prettyfilelist.get(0);
                    LNDWA.this.currentpos = 0;


                    return null;
                }

                @Override
                protected void onPreExecute() {
                    pd.setTitle(LNDWA.this.getResources().getString(R.string.loadinggamedialog));
                    pd.setMessage(LNDWA.this.getResources().getString(R.string.pleasewait));
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    pd.show();
                }

                @Override
                protected void onPostExecute(final Object o) {
                    if (pd!=null) {
                        spinner.setAdapter(LNDWA.this.adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long l) {
                                LNDWA.this.currentselection = prettyfilelist.get(pos);
                                LNDWA.this.currentpos = pos;
                            }

                            @Override
                            public void onNothingSelected(final AdapterView<?> adapterView) {

                            }
                        });
                        spinner.setSelection(LNDWA.this.currentpos);
                        pd.dismiss();
                    }
                }

            };
            task.execute((Void[])null);

        }
    }

    public Tuple<String,String> getGameSetName(String file) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput( new FileReader(this.getFilesDir()+"/chars/"+file+Utils.CHARSSUFFIX+".xml"));
        if(file.contains("/"))
            file=file.substring(0,file.lastIndexOf("/"))+file.substring(file.lastIndexOf("/")+1).replaceFirst("-","_");
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("gameset")) {
                 return new Tuple<String,String>(xpp.getAttributeValue(null, "title"),xpp.getAttributeValue(null, "language")+"_"+xpp.getAttributeValue(null,"gamesetid").replaceAll("_","-"));
            }
            eventType = xpp.next();
        }
        return new Tuple<String,String>("","");
    }

    /**
     * Creates folders to for storing presets and savegames if needed.
     */
    public void createFolders(){
        File subdir = new File(getFilesDir(), "savegames");
        if(!subdir.exists()){
            this.installFiles(Utils.SAVEGAMESUFFIX,subdir);
        }
        subdir=new File(getFilesDir(),"presets");
        if(!subdir.exists()){
            this.installFiles(Utils.PRESETSUFFIX,subdir);
        }
        subdir=new File(getFilesDir(),"config");
        if(!subdir.exists()){
            this.installFiles(Utils.CONFIGSUFFIX,subdir);
        }else{
            this.createGameSetList(Utils.CONFIGSUFFIX);
        }
        subdir=new File(getFilesDir(),"chars");
        if(!subdir.exists()){
            this.installFiles(Utils.CHARSSUFFIX,subdir);
        }else{
            this.createGameSetList(Utils.CHARSSUFFIX);
        }
    }

    public void createImgFoldersForGameSet(String gameSet){
         File subdir=new File(getFilesDir(),"chars/"+gameSet.replaceAll("_","-").replaceFirst("-","_")+"/");
         if(!subdir.exists()){
             subdir.mkdir();
             new File(subdir+"/overlay/").mkdir();
             try {
                 Log.e("Open GameSet:",new File(getFilesDir()+"/chars/"+gameSet.replaceAll("_","-").replaceFirst("-","_")+Utils.CHARSSUFFIX+".xml").getAbsolutePath());
                 GameSet gameset=new GameSet(new File(getFilesDir()+"/chars/"+gameSet.replaceAll("_","-").replaceFirst("-","_")+Utils.CHARSSUFFIX+".xml"));
                 int test=0;
                 Bitmap bitmap;
                 if(gameset.getLanguage()!=null && gameset.getGamesetid()!=null){
                 String gamesetfilestr=gameset.getLanguage()+"_"+gameset.getGamesetid().replaceAll("-","_");
                 test = getResources().getIdentifier(gamesetfilestr, "drawable", this.getPackageName());
                 if(test!=0) {
                     bitmap = BitmapFactory.decodeResource(getResources(), this.getResources()
                             .getIdentifier(gamesetfilestr, "drawable", this.getPackageName()));

                     gamesetfilestr = gamesetfilestr.replaceAll("_", "-").replaceFirst("-", "_") + ".png";
                     Log.e("Now copying ", bitmap.toString() + " to " + subdir.getAbsolutePath() + "/" + gamesetfilestr);
                     Utils.copyDrawableToDestination(bitmap, subdir.getAbsolutePath(), gamesetfilestr);
                 }
                 }
                 for(Karte card:gameset.getCards()){
                     String cardid=card.getCardid().replaceAll("-", "_");
                     if(Character.isDigit(cardid.charAt(0))){
                         cardid="_"+cardid;
                     }
                     test = getResources().getIdentifier(cardid, "drawable", this.getPackageName());
                     if(test!=0){
                         bitmap = BitmapFactory.decodeResource(getResources(), this.getResources()
                                 .getIdentifier(cardid, "drawable", this.getPackageName()));
                         String newid=cardid.substring(0,1).equals("_")?cardid.substring(1):card.getCardid();
                         newid=newid.replaceAll("_","-");
                         Log.e("Now copying ",newid+".png"+" to "+subdir.getAbsolutePath()+"/"+newid);
                         Utils.copyDrawableToDestination(bitmap,subdir.getAbsolutePath(),newid+".png");
                     }
                     Log.e("Ability Check","NOW");
                     for(Ability abb:card.getabblist()){
                         Log.e("Card ","has Abbs!"+card.getabblist().size());
                         String abid=abb.getAbilityId().replaceAll("-", "_");
                         if(Character.isDigit(abid.charAt(0))){
                             abid="_"+abid;
                         }

                         test = getResources().getIdentifier(abid, "drawable", this.getPackageName());
                         Log.e("Abid",abid+" "+test);
                         if(test!=0){
                             bitmap = BitmapFactory.decodeResource(getResources(), this.getResources()
                                     .getIdentifier(abid, "drawable", this.getPackageName()));
                             String newid=abid.substring(0,1).equals("_")?abid.substring(1):abb.getAbilityId();
                             newid=newid.replaceAll("_","-");
                             Log.e("Now copying ",newid+".png"+" to "+subdir.getAbsolutePath()+"/overlay/"+newid);
                             Utils.copyDrawableToDestination(bitmap,subdir.getAbsolutePath()+"/overlay/",newid+".png");
                         }
                     }
;
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (SAXException e) {
                 e.printStackTrace();
             } catch (ParserConfigurationException e) {
                 e.printStackTrace();
             }
             Log.e("Created",subdir.getAbsolutePath());
         }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 0:
                builder.setMessage(Utils.dialogtext)
                        .setCancelable(true)
                        .setIcon(this.getResources().getDrawable(R.drawable.title))
                        .setTitle(this.getResources().getString(R.string.about))
                        .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int buttonid) {
                                dialog.cancel();

                            }
                        });
                break;
            case 1:
                builder.setItems(this.getSaveGames(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        final Intent intent = new Intent(LNDWA.this, Round2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        Bundle mBundle = new Bundle();
                        mBundle.putBoolean("loadgame",true);
                        mBundle.putString("savegamepath",LNDWA.this.getFilesDir()+"/savegames/"+LNDWA.this.getSaveGames()[i]);
                        intent.putExtras(mBundle);
                        LNDWA.this.startActivity(intent);

                    }
                }).setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {

                    }
                }).setTitle(R.string.loadgame);break;
            case 2:
                Synchronizer sync=new Synchronizer();
                final String[] items=sync.getGameSetList(this.getResources().getConfiguration().locale.getCountry().toLowerCase(), this);
                if(sync.getErrorflag()){
                    builder.setMessage(items[0])
                            .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, final int i) {

                                }
                            }).setTitle(R.string.downloadgameset);
                }else{
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {
                            LNDWA.this.downloadGameSet(items[i]);

                        }
                    }).setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {

                        }
                    }).setTitle(R.string.downloadgameset);
                }

            default:
        }
        return builder.create();
    }

    /**
     * Downloads a game set.
     * @param name the name of the gameset
     * @return the GameSet
     */
    public String downloadGameSet(final String name){
        Synchronizer sync=new Synchronizer();
        GameSet gameset;
        try {
            gameset = sync.loadGameSet(name,this.getResources().getConfiguration().locale.getCountry().toLowerCase(), this);
            List<GameSet> gslist=new LinkedList<>();
            gslist.add(gameset);
            try {
                Utils.saveFileToInternalStorage( gslist, SaveEnum.GAMESET,new File(this.getFilesDir()+"/chars/"+name+".xml2"));
                return "Gameset Saved successfully";
            } catch (IOException e) {
                return e.toString();
            }
        } catch (ExecutionException e) {
            return e.toString();
        } catch (InterruptedException e) {
            return e.toString();
        } catch (ParserConfigurationException e) {
            return e.toString();
        } catch (SAXException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0, 0, 0, this.getResources().getString(R.string.about));
        menu.add(0, 0, 0, this.getResources().getString(R.string.options));
        menu.add(0, 0, 0, this.getResources().getString(R.string.playerview));
        //menu.add(0,0,0,this.getResources().getString(R.string.itemview));
        menu.add(0, 0, 0, this.getResources().getString(R.string.setview));
        menu.add(0, 0, 0, this.getResources().getString(R.string.statistics));
        menu.add(0, 0, 0, this.getResources().getString(R.string.loadgame));
        menu.add(0, 0, 0, this.getResources().getString(R.string.downloadgameset));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Utils.dialogtext = LNDWA.this.
                        getResources().getString(R.string.copyright);
                LNDWA.this.chooseDialogs(DialogEnum.ABOUT);
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                final Intent intent = new Intent(LNDWA.this, ViewPlayers.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                LNDWA.this.startActivity(intent);
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                final Intent intent = new Intent(LNDWA.this, Options.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                LNDWA.this.startActivity(intent);
                return true;
            }
        });
        /*menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                final Intent intent = new Intent(LNDWA.this, ViewItems.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                LNDWA.this.startActivity(intent);
                return true;
            }
        });*/
        menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                final Intent intent = new Intent(LNDWA.this, ViewGameSet.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                LNDWA.this.startActivity(intent);
                return true;
            }
        });
        menu.getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                final Intent intent = new Intent(LNDWA.this, ViewStats.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                LNDWA.this.startActivity(intent);
                return true;
            }
        });
        menu.getItem(5).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                LNDWA.this.showDialog(1);
                return true;
            }
        });
        menu.getItem(6).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                LNDWA.this.showDialog(2);
                return true;
            }
        });

        return true;
    }

    /**
     * Initiates the game using the given GameSet.
     *
     * @throws IOException                  on error
     * @throws ParserConfigurationException on error
     * @throws SAXException                 on error
     */
    public void startGame(View view) throws IOException, ParserConfigurationException, SAXException {
        final Intent intent = new Intent(this, ChooseChars.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle mBundle = new Bundle();
        Log.e("GameSetFile: ",new File(this.getFilesDir() + "/chars/" + this.currentselection.getTwo()+Utils.CHARSSUFFIX + ".xml").getAbsolutePath());
        GameSet gameset=new GameSet(new File(this.getFilesDir() + "/chars/" + this.currentselection.getTwo() +Utils.CHARSSUFFIX+ ".xml"));

        File presetfile=new File(this.getFilesDir()+"/presets/"+this.currentselection.getTwo().substring(this.currentselection.getTwo().indexOf("_")+1)+Utils.PRESETSUFFIX+".xml");
        Log.e("Presetfile",presetfile.getAbsolutePath()+" Exists? "+presetfile.exists());
        if(presetfile.exists()){
           gameset.setPresets(new ArrayList<>(new PresetParse().parsePreset(presetfile)));
        }else{
            gameset.setPresets(new ArrayList<Preset>());
        }
        Log.e("Presets?", gameset.getPresets().toString());
        mBundle.putParcelable("gameset", gameset);
        mBundle.putInt("new", 1);
        intent.putExtras(mBundle);
        this.startActivity(intent);
    }
}
