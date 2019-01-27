package com.github.situx.mafiaapp.views.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Group;
import com.github.situx.mafiaapp.views.ViewUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by timo on 06.04.14.
 */
public class EditGroup extends ViewUtils {

    private static final int FILE_CHOOSER = 0;
    private Group group;

    private ImageView imageview;

    public EditGroup(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editgroup);
        this.group=(Group)this.getIntent().getExtras().getParcelable("group");
        EditText editGroupName=(EditText) this.findViewById(R.id.editGroupName);
        editGroupName.setText(this.group.getGroupname());
        editGroupName.setOnClickListener(view -> EditGroup.this.group.setGroupname(((EditText) view).getText().toString()));
        EditText editGroupIdentifier=(EditText)this.findViewById(R.id.editGroupIdentifier);
        editGroupIdentifier.setText(this.group.getGroupIdentifier());
        editGroupIdentifier.setOnClickListener(view -> EditGroup.this.group.setGroupIdentifier(((EditText) view).getText().toString()));
        EditText editGroupDescription=(EditText)this.findViewById(R.id.editGroupDescription);
        editGroupDescription.setText(this.group.getGroupdescription());
        editGroupDescription.setOnClickListener(view -> EditGroup.this.group.setGroupdescription(((EditText) view).getText().toString()));
        this.imageview=(ImageView)this.findViewById(R.id.editGroupimageView);
        if(this.group.imgexists())
            imageview.setImageResource(this.getResources().getIdentifier(this.group.getGroupIcon(),"drawable",this.getPackageName()));
        else
            imageview.setImageResource(R.drawable.title);
        imageview.setOnClickListener(view -> {
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
        });
        final CheckBox winsgameCB=(CheckBox)this.findViewById(R.id.editGroupWinsGameCB);
        winsgameCB.setChecked(this.group.getWinsgame());

        winsgameCB.setOnCheckedChangeListener((compoundButton, b) -> {
            winsgameCB.setChecked(b);
            if (b)
                EditGroup.this.group.setWinsgame(b);
        });
        final CheckBox secondaryCB=(CheckBox)this.findViewById(R.id.editGroupSecondaryCB);
        secondaryCB.setChecked(this.group.getSecondary());

        secondaryCB.setOnCheckedChangeListener((compoundButton, b) -> {
            secondaryCB.setChecked(b);
            if (b)
                EditGroup.this.group.setSecondary(b);
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
                    this.group.setGroupIcon(FilePath);
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
    public void onBackPressed() {
        Bundle conData = new Bundle();
        conData.putParcelable("group", this.group);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }


}
