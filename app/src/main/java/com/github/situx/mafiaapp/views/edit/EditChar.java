package com.github.situx.mafiaapp.views.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.*;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.AbilityAdapter;
import com.github.situx.mafiaapp.adapters.ActionAdapter;
import com.github.situx.mafiaapp.adapters.GroupAdapter;
import com.github.situx.mafiaapp.cards.Ability;
import com.github.situx.mafiaapp.cards.Action;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.cards.Group;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.ViewUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 16.01.14.
 */
public class EditChar extends ViewUtils {

    private static final int ADD_ABILITY = 1;
    private static final int ADD_ACTION = 2;
    private Karte card;

    private ArrayList<Karte> cardlist;
    private ListView editActionListView;

    private ImageView imageview;

    private ListView editAbbListView;

    private static final int FILE_CHOOSER = 0;

    public EditChar(){
        final View.OnFocusChangeListener saveonfocus = (view, b) -> {
            if (!b) {
                EditChar.this.card.setName(((EditText) view).getText().toString());
            }
        };
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editchar);
        this.card=this.getIntent().getExtras().getParcelable("card");
        if(this.getIntent().hasExtra("addchar")){
            this.setTitle(R.string.addchar);
        }
        final GameSet gameset = this.getIntent().getExtras().getParcelable("gameset");
        final String sourcefile = this.getIntent().getExtras().getString("sourcefile");
        if (this.getResources().getIdentifier(gameset.getGamesetImg(), "drawable", this.getPackageName()) != 0) {
            this.getSupportActionBar().setIcon(this.getResources().getIdentifier(sourcefile.substring(0, sourcefile.lastIndexOf('_')), "drawable", this.getPackageName()));
        } else {
            this.getSupportActionBar().setIcon(R.drawable.title);
        }


        //View view=this.findViewById(R.layout.editchar);

        //this.setTitle(R.string.editchar);
        EditText editName=(EditText)this.findViewById(R.id.editCharName);
        editName.setText(this.card.getName());
        editName.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setName(((EditText)view).getText().toString());
            }
        });
        Spinner editGroup=(Spinner)this.findViewById(R.id.groupspinner);
        final List<Group> grouplist=new LinkedList<Group>(gameset.getGroups());
        editGroup.setAdapter(new GroupAdapter(this,grouplist,false));
        editGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                EditChar.this.card.setGroup(grouplist.get(i));
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        });
        /*EditText editGroup=(EditText)this.findViewById(R.id.editCharGroup);
        editGroup.setText(this.card.getGroup());
        editGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b){
                    EditChar.this.card.setGroup(((EditText) view).getText().toString());
                }
            }
        });*/
        EditText editPosition=(EditText)this.findViewById(R.id.editCharPosition);
        editPosition.setText(this.card.getPosition().toString());
        editPosition.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setPosition(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText editMinAmount=(EditText)this.findViewById(R.id.editCharMinAmount);
        editMinAmount.setText(this.card.getMinAmount().toString());
        editMinAmount.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setMinamount(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText editMaxAmount=(EditText)this.findViewById(R.id.editCharMaxAmount);
        editMaxAmount.setText(this.card.getMaxAmount().toString());
        editMaxAmount.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setMaxamount(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText editExtra=(EditText)this.findViewById(R.id.editCharExtra);
        editExtra.setText(this.card.getExtra().toString());
        editExtra.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setExtra(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText editBalance=(EditText)this.findViewById(R.id.editCharBalanceValue);
        editBalance.setText(this.card.getBalancevalue().toString());
        editBalance.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setBalancevalue(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText editWinningAlive=(EditText)this.findViewById(R.id.editCharWinningAlive);
        editWinningAlive.setText(this.card.getWinningAlive().toString());
        editWinningAlive.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setWinningAlive(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText editWinningDead=(EditText)this.findViewById(R.id.editCharWinningDead);
        editWinningDead.setText(this.card.getWinningDead().toString());
        editWinningDead.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setWinningDead(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });

        EditText editDescriptionText=(EditText)this.findViewById(R.id.editCharDescription);
        editDescriptionText.setText(Html.fromHtml(this.card.getDescription()));
        editDescriptionText.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditChar.this.card.setDescription(((EditText) view).getText().toString());
            }
        });
        CheckBox callEveryoneBox=(CheckBox)this.findViewById(R.id.editCharCallEveryoneBox);
        callEveryoneBox.setChecked(this.card.getCalleveryone());
        callEveryoneBox.setOnCheckedChangeListener((compoundButton, b) -> EditChar.this.card.setCalleveryone(b));
        CheckBox nopointsBox=(CheckBox)this.findViewById(R.id.editCharNopointsBox);
        nopointsBox.setChecked(this.card.getNopoints());
        nopointsBox.setOnCheckedChangeListener((compoundButton, b) -> EditChar.this.card.setNopoints(b));
        CheckBox deadcharsBox=(CheckBox)this.findViewById(R.id.editCharDeadcharBox);
        deadcharsBox.setChecked(this.card.getDeadchars());
        deadcharsBox.setOnCheckedChangeListener((compoundButton, b) -> EditChar.this.card.setDeadchars(b));
        CheckBox winsaloneBox=(CheckBox)this.findViewById(R.id.editCharWinsaloneBox);
        winsaloneBox.setChecked(this.card.getWinsalone());
        winsaloneBox.setOnCheckedChangeListener((compoundButton, b) -> EditChar.this.card.setWinsalone(b));
        CheckBox fixedDeathBox=(CheckBox)this.findViewById(R.id.editCharFixedDeathBox);
        fixedDeathBox.setChecked(this.card.getFixeddeath()!=0);
        final EditText fixedDeathtext=(EditText)this.findViewById(R.id.editCharFixedDeath);
        if(this.card.getFixeddeath()==0){
            fixedDeathtext.setEnabled(false);
        }else{

            fixedDeathtext.setText(this.card.getFixeddeath().toString());
            fixedDeathtext.setEnabled(true);
        }
        fixedDeathBox.setOnCheckedChangeListener((compoundButton, b) -> {
             fixedDeathtext.setEnabled(b);
            if(!b){
                EditChar.this.card.setFixeddeath(0);
            }
        });
        this.imageview=(ImageView)this.findViewById(R.id.editCharImgView);
        if(this.card.imgexists())
            imageview.setImageDrawable(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),gameset.getLanguage()+"_"+gameset.getGamesetid(), card.getImg() + ".png"));
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
        editAbbListView=(ListView)this.findViewById(R.id.editAbbListView);
        editAbbListView.setAdapter(new AbilityAdapter(this,this.card.getabblist(),R.layout.item_ability));
        editActionListView=(ListView)this.findViewById(R.id.editActionListView);
        editActionListView.setAdapter(new ActionAdapter(this.card.getActionlist(),this));
        Integer round=this.card.getRound();
        final EditText xRoundEditText=(EditText)this.findViewById(R.id.editCharXRound);
        final EditText everyXRoundEditText=(EditText)this.findViewById(R.id.editCharEveryXRound);
        RadioButton everyRound=(RadioButton)this.findViewById(R.id.editCharEveryRound);
        everyRound.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                EditChar.this.card.setRound(0);
            }
        });
        RadioButton noRound=(RadioButton)this.findViewById(R.id.editCharNoRoundRB);
        noRound.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                EditChar.this.card.setRound(-1);
            }
        });
        RadioButton xRound=(RadioButton)this.findViewById(R.id.editCharXRoundRB);
        xRound.setOnCheckedChangeListener((compoundButton, b) -> xRoundEditText.setEnabled(b));
        xRoundEditText.setOnFocusChangeListener((view, b) -> {
            if(!b)
            EditChar.this.card.setRound(Integer.valueOf(((EditText)view).getText().toString()));
        });
        RadioButton everyXRound=(RadioButton)this.findViewById(R.id.editCharEveryXRoundRB);
        everyXRound.setOnCheckedChangeListener((compoundButton, b) -> everyXRoundEditText.setEnabled(b));
        everyXRoundEditText.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditChar.this.card.setRound(Integer.valueOf(((EditText)view).getText().toString())*-1);
        });
        if(round==0){
            everyRound.setChecked(true);
        }else if(round==-1){
            noRound.setChecked(true);
        }else if(round>0){
            xRound.setChecked(true);
            xRoundEditText.setText(this.card.getRound().toString());
            xRoundEditText.setEnabled(true);
        }else if(round<-1){
            everyXRound.setChecked(true);
            everyXRoundEditText.setText(Integer.valueOf(this.card.getRound() * -1).toString());
            everyXRoundEditText.setEnabled(true);
        }
        Button button=(Button)this.findViewById(R.id.addAbilityButton);
        button.setOnClickListener(view -> {
            final Intent intent = new Intent(EditChar.this,EditAbility.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("ability", new Ability());
            mBundle.putBoolean("addabb", true);
            intent.putExtras(mBundle);
            EditChar.this.startActivityForResult(intent, ADD_ABILITY);
        });
        Button button3=(Button)this.findViewById(R.id.addActionButton);
        button3.setOnClickListener(view -> {
            final Intent intent = new Intent(EditChar.this,EditAction.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("action", new Action());
            mBundle.putBoolean("addaction", true);
            intent.putExtras(mBundle);
            EditChar.this.startActivityForResult(intent, ADD_ACTION);
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
                    this.card.setImg(FilePath);
                }
                break;
            case ADD_ABILITY:
                if(resultCode==RESULT_OK){
                    this.card.getabblist().add((Ability)data.getExtras().getParcelable("ability"));
                    this.editAbbListView.invalidateViews();
                } break;
            case ADD_ACTION:
                if(resultCode==RESULT_OK){
                    this.card.getActionlist().put(((Action)data.getExtras().getParcelable("action")).getActionid(),((Action)data.getExtras().getParcelable("action")));
                    this.editActionListView.invalidateViews();
                }
        }
    }

    @Override
    public void onBackPressed() {
        Bundle conData = new Bundle();
        conData.putParcelable("card", this.card);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }


}
