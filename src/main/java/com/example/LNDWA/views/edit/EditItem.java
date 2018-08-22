package com.example.LNDWA.views.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Item;
import com.example.LNDWA.views.ViewUtils;

/**
 * Created by timo on 06.02.14.
 */
public class EditItem extends ViewUtils {

    private static final int FILE_CHOOSER = 0;
    private Item item;

    private ImageView imageview;

    public EditItem(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edititem);
        this.item=this.getIntent().getExtras().getParcelable("item");
        EditText editItemName=(EditText) this.findViewById(R.id.editItemTitle);
        editItemName.setText(this.item.getName());
        editItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditItem.this.item.setName(((EditText)view).getText().toString());
            }
        });
        EditText editItemDescription=(EditText)this.findViewById(R.id.editItemDescription);
        editItemDescription.setText(this.item.getDescription());
        editItemDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditItem.this.item.setDescription(((EditText)view).getText().toString());
            }
        });
        EditText editItemProbability=(EditText)this.findViewById(R.id.editItemProb);
        editItemProbability.setText(this.item.getProbability());
        editItemProbability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditItem.this.item.setProbability(Integer.valueOf(((EditText)view).getText().toString()));
            }
        });
        this.imageview=(ImageView)this.findViewById(R.id.editCharImgView);
        if(this.item.imgexists())
            imageview.setImageResource(this.getResources().getIdentifier(this.item.getImage(),"drawable",this.getPackageName()));
        else
            imageview.setImageResource(R.drawable.title);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("file/*"); // intent type to filter application based on your requirement
                startActivityForResult(fileIntent, FILE_CHOOSER);
                /*Intent intent = new Intent(this, FileChooser.class);
                ArrayList<String> extensions = new ArrayList<String>();
                extensions.add(".pdf");
                extensions.add(".xls");
                extensions.add(".xlsx");
                intent.putStringArrayListExtra("filterFileExtension", extensions);
                startActivityForResult(intent, FILE_CHOOSER);*/
            }
        });
        final RadioButton permanentButton=(RadioButton)this.findViewById(R.id.editItemPermanentRadioButton);
        final RadioButton oneTimeRadioButton=(RadioButton)this.findViewById(R.id.editItemOneTimeRadioButton);
        if(this.item.getRound()==0){
            permanentButton.setChecked(true);
        }
        if(this.item.getRound()>0){
            oneTimeRadioButton.setChecked(true);
        }
        permanentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                permanentButton.setChecked(b);
                if(b)
                    EditItem.this.item.setRound(0);
            }
        });
        oneTimeRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                oneTimeRadioButton.setChecked(b);
                if(b)
                    EditItem.this.item.setRound(1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case FILE_CHOOSER:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    Bitmap bmp = BitmapFactory.decodeFile(FilePath);
                    imageview.setImageBitmap(bmp);
                    this.item.setImage(FilePath);
                }
                break;
        }
    }
 /*
    @Override
    public void onBackPressed() {
        try {
            File subdir=new File(getFilesDir(), "chars");
            File destination=new File(subdir+"/"+sourcefile+".xml");
            Utils.saveFileToInternalStorage((List<Object>) (List<?>) this.itemlist, SaveEnum.CARD, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }
     */


    @Override
    public void finish()
    {
        Bundle conData = new Bundle();
        conData.putParcelable("item", this.item);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
