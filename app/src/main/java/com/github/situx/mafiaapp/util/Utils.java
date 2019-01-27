package com.github.situx.mafiaapp.util;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Xml;

import com.github.situx.mafiaapp.cards.GameElements;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.cards.Player;

import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by timo on 21.01.14.
 */
public class Utils {

    public static String dialogtext="";

    public static String currentimgpath="";

    public static String filesdir="";

    public static final String PLAYERFILE="players_config.xml";

    public static final String DRAWABLE="drawable";

    public static final String CONFIGSUFFIX="_config";

    public static final String PRESETSUFFIX="_presets";

    public static final String CHARSSUFFIX="_chars";

    public static final String SAVEGAMESUFFIX="_savegame";

    public static final Integer NUM_FRAGS=10;

/*    public static Round2Fragment one=new Round2Fragment(),two=new Round2Fragment(),three=new Round2Fragment()
            ,four=new Round2Fragment(),five=new Round2Fragment(),six=new Round2Fragment(),seven=new Round2Fragment(),
            eight=new Round2Fragment(),nine=new Round2Fragment(),ten=new Round2Fragment();

    public static final Round2Fragment[] round2fragments=new Round2Fragment[NUM_FRAGS];

  */
    public static Integer currentnum=0;

 /*   public static void initarray(){
          round2fragments[0]=one;
          round2fragments[1]=two;
        round2fragments[2]=three;
        round2fragments[3]=four;
        round2fragments[4]=five;
        round2fragments[5]=six;
        round2fragments[6]=seven;
        round2fragments[7]=eight;
        round2fragments[8]=nine;
        round2fragments[9]=ten;
    }

    public static void addFragment(final Round2Fragment fragment){
          round2fragments[currentnum++]=fragment;
    }


    public static void removeFragment(final Integer fragid){
        Round2Fragment frag=round2fragments[fragid];
        frag=null;
    }   */

    /**
     * Converts a bitmap to grayscale.
     * Used to indicated died characters
     * @param bmpOriginal the bitmap to convert
     * @return  the converted bitmap
     */
    public static Bitmap toGrayscale(final Bitmap bmpOriginal){
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static void saveFileToInternalStorage(List<? extends GameElements> savelist,final SaveEnum saveformat,final File savefile) throws IOException {
        File outputFile=new File(savefile.getAbsolutePath());
        if(!outputFile.exists())
            outputFile.createNewFile();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savefile));
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            //OutputStream os=new FileOutputStream(filesdir+"/"+directory+"/"+file+"2.xml");

                serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            switch(saveformat){
                case PLAYER:List<Player> playerlist=(List<Player>)savelist;
                            serializer.startTag("", "players_config");
                            for(Player player:playerlist){
                                serializer.flush();
                                writer.write(player.toXML());
                            }
                            serializer.endTag("","players_config");
                            serializer.endDocument();
                            serializer.flush();
                                break;
                case CARD: List<Karte> cardlist=(List<Karte>) savelist;
                    serializer.startTag("", "data");
                    Collections.sort(cardlist, new Comparator<Karte>() {
                        public int compare(Karte s1, Karte s2) {
                            return s1.getPosition().compareTo(s2.getPosition());
                        }
                    });
                    for(Karte card:cardlist){
                        Log.e("Card: ",card.toString());
                        serializer.flush();
                        writer.write(card.toXML());
                    }
                    serializer.endTag("","data");
                    serializer.endDocument();
                    serializer.flush();
                    break;
                case GAMESET: GameSet gameSet=((List<GameSet>) savelist).get(0);
                    Collections.sort(gameSet.getCards(), new Comparator<Karte>() {
                        public int compare(Karte s1, Karte s2) {
                            return s1.getPosition().compareTo(s2.getPosition());
                        }
                    });
                    serializer.flush();
                    writer.write(gameSet.toXML());
                    Log.e("GameSet.toXML()", gameSet.toXML());
                    serializer.endDocument();
                    serializer.flush();
                    break;
                case EVENT: break;
                case ITEM: break;
                case GAME:
                case PRESET: GameElements preset=savelist.get(0);
                    serializer.flush();
                    writer.write(preset.toXML());
                    serializer.endDocument();
                    serializer.flush();
                        break;
                default:
            }
        } catch (IOException e) {

            e.printStackTrace();
        }


        out.write(writer.toString().getBytes());
        Log.e("Write it:",writer.toString());
        Log.e("Write it:",savefile.getAbsolutePath());

        out.flush();
        out.close();

    }

    /*public static void saveFileToExternalStorage(final String filepath){
        try {
            OutputStream os=new FileOutputStream(filepath+".xml");
            File filfil=new File(filepath+".xml");
            //filestring+=is.getAbsolutePath()+"\n"+filfil.exists()+"\n";
            filestring+=filfil2.getAbsolutePath()+"\n"+filfil2.exists()+"\n";
            Utils.copy(is, filfil2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        //InputStream in = new FileInputStream(src);
        //FileOutputStream out = op(dst, Context.MODE_PRIVATE);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void copy(InputStream in, File dst) throws IOException {
        //InputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Pretty formats the file name to display it in an application.
     * @param filename the filename as String
     * @return the pretty formatted filename
     */
    public static String prettyFormatFileName(String filename){
        StringBuffer stringbf = new StringBuffer();
        Matcher m = Pattern.compile("([a-z])([a-z]*)",
                Pattern.CASE_INSENSITIVE).matcher(filename.substring(filename.indexOf('_')+1).replace('_',' '));
        while (m.find()) {
            m.appendReplacement(stringbf,
                    m.group(1).toUpperCase() + m.group(2).toLowerCase());
        }
        return stringbf.toString();
    }

    public static Drawable loadDrawable(final String filesdir,final String directory,final String file){
        Log.e("LoadDrawable",filesdir+"/chars/"+ directory+"/"+file);
        return new BitmapDrawable(filesdir+"/chars/"+ directory+"/"+file);
    }

    public static void copyDrawableToDestination(Bitmap bitmap,String destination,String filename){
        File dest = new File(destination, filename);
        try {
            FileOutputStream out;
            out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Zips a given amount of files into a zip file.
     * @param input
     * @param zipFile
     * @throws IOException
     */
    public static void zipFiles(final File input,final File zipFile) throws IOException{
        byte[] buffer = new byte[1024];

        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        System.out.println("Output to Zip : " + zipFile);

        for(String file : input.list()){

            System.out.println("File Added : " + file);
            ZipEntry ze= new ZipEntry(file);
            zos.putNextEntry(ze);

            FileInputStream in =
                    new FileInputStream(input + File.separator + file);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
        }

        zos.closeEntry();
        //remember close it
        zos.close();

        System.out.println("Done");
    }

    public static void unzipFiles(final File input,final File output) throws IOException{
        byte[] buffer = new byte[1024];

        //create output directory is not exists
        File folder = output;
        if(!folder.exists()){
            folder.mkdir();
        }

        //get the zip file content
        ZipInputStream zis =
                new ZipInputStream(new FileInputStream(input));
        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while(ze!=null){

            String fileName = ze.getName();
            File newFile = new File(output + File.separator + fileName);

            System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        System.out.println("Done");

    }
}
