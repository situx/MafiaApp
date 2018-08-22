package com.example.LNDWA.views.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Ability;
import com.example.LNDWA.views.ViewUtils;

/**
 * Created by timo on 28.01.14.
 * Edit Ability dialog for editing abilities.
 */
public class EditAbility extends ViewUtils {
    /**File chooser constant.*/
    private static final int FILE_CHOOSER = 0;
    /**The ability to edit.*/
    private Ability ability;
    /**The imageview of this ability.*/
    private ImageView abilityImageView;

    /**
     * Constructor for this class.
     */
    public EditAbility(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editability);
        if(this.getIntent().hasExtra("addabb")){
            this.setTitle(R.string.addAbility);
        }
        this.ability=this.getIntent().getExtras().getParcelable("ability");
        EditText abilityDescription=(EditText)this.findViewById(R.id.editAbilityDescription);
        abilityDescription.setText(ability.getDescription());
        abilityDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                ability.setDescription(((EditText)view).getText().toString());
            }
        });
        EditText abilityAmount=(EditText)this.findViewById(R.id.editAbilityAmount);
        abilityAmount.setText(ability.getOriginalamount().toString());
        abilityAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                ability.setOriginalamount(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        CheckBox checkBox=(CheckBox)this.findViewById(R.id.editAbilityActiveCB);
        checkBox.setChecked(ability.getActive());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setActive(b);
            }
        });
        CheckBox checkBox2=(CheckBox)this.findViewById(R.id.editAbilityMustUseCB);
        checkBox2.setChecked(ability.getMustuse());
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setMustuse(b);
            }
        });
        CheckBox onDeadCheckBox=(CheckBox)this.findViewById(R.id.editAbilityOnDead);
        onDeadCheckBox.setChecked(ability.getOndead());
        onDeadCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setOndead(b);
            }
        });
        CheckBox killingcheckbox=(CheckBox)this.findViewById(R.id.editAbilityKillingCheckBox);
        killingcheckbox.setChecked(ability.getKilling());
        killingcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setKilling(b);
            }
        });
        CheckBox selfusecheckbox=(CheckBox)this.findViewById(R.id.editAbilitySelfUse);
        selfusecheckbox.setChecked(ability.getSelf());
        selfusecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setSelf(b);
            }
        });
        CheckBox counterKillingCheckbox=(CheckBox)this.findViewById(R.id.editAbilityCounterKillingCheckBox);
        counterKillingCheckbox.setChecked(ability.getCounterKilling());
        counterKillingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setCounterKilling(b);
            }
        });
        CheckBox switchCharCheckbox=(CheckBox)this.findViewById(R.id.editAbilitySwitchChar);
        switchCharCheckbox.setChecked(ability.getSwitchchar());
        switchCharCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditAbility.this.ability.setSwitchchar(b);
            }
        });
        EditText availableFromText=(EditText) this.findViewById(R.id.editAbilityAvailableFrom);
        availableFromText.setText(ability.getAvailableFrom().toString());
        availableFromText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b)
                    EditAbility.this.ability.setAvailableFrom(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText availableUntilText=(EditText)this.findViewById(R.id.editAbilityAvailableUntil);
        availableUntilText.setText(ability.getAvailableUntil().toString());
        availableUntilText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b)
                EditAbility.this.ability.setAvailableUntil(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText probabilityText=(EditText)this.findViewById(R.id.editAbilityProb);
        probabilityText.setText(ability.getProbability().toString());
        probabilityText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b)
                    EditAbility.this.ability.setProbability(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        Button saveAbilityButton=(Button)this.findViewById(R.id.saveAbilityButton);
        saveAbilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditAbility.this.finish();
            }
        });
        this.abilityImageView=(ImageView) this.findViewById(R.id.editAbilityImgView);
        this.abilityImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        fileIntent.setType("file/*"); // intent type to filter application based on your requirement
                        startActivityForResult(fileIntent, FILE_CHOOSER);
                    }
        });
        if(!"".equals(this.ability.getImage()))
            abilityImageView.setImageResource(this.getResources().getIdentifier(this.ability.getImage(),"drawable",this.getPackageName()));
        else
            abilityImageView.setImageResource(R.drawable.title);
        /*Spinner editGroup=(Spinner)this.findViewById(R.id.groupspinner);
        final List<Group> grouplist=new LinkedList<Group>(this.gameset.getGroups());
        editGroup.setAdapter(new GroupAdapter(this,grouplist));
        editGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                EditAbility.this.ability.setChangeGroup(grouplist.get(i));
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        });
        /*EditText abilityChangeGroupEditText=(EditText)this.findViewById(R.id.editAbilityChangeGroup);
        abilityChangeGroupEditText.setText(this.ability.getChangeGroup());
        abilityChangeGroupEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b)
                    EditAbility.this.ability.setChangeGroup(((EditText)view).getText().toString());
            }
        });*/
        EditText abilityConcernsEditText=(EditText)this.findViewById(R.id.editAbilityConcerns);
        abilityConcernsEditText.setText(ability.getConcerns().toString());
        abilityConcernsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b)
                    EditAbility.this.ability.setConcerns(Integer.valueOf(((EditText) view).getText().toString()));
            }
        });
        EditText abilityDurationEditText=(EditText)this.findViewById(R.id.editAbilityDuration);
        abilityDurationEditText.setText(this.ability.getDuration().toString());
        abilityDurationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b)
                    EditAbility.this.ability.setDuration(Integer.valueOf(((EditText)view).getText().toString()));
            }
        });
        EditText abilityDelayEditText=(EditText)this.findViewById(R.id.editAbilityDelay);
        abilityDelayEditText.setText(this.ability.getDelay().toString());
        abilityDelayEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b)
                    EditAbility.this.ability.setDelay(Integer.valueOf(((EditText)view).getText().toString()));
            }
        });
        EditText abilitySwitchNewCharEditText=(EditText)this.findViewById(R.id.editAbilitySwitchNewChar);
        abilitySwitchNewCharEditText.setText(this.ability.getSwitchnewchar());
        abilitySwitchNewCharEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b)
                    EditAbility.this.ability.setSwitchnewchar(((EditText)view).getText().toString());
            }
        });

    }

    @Override
    public void onBackPressed() {

        Bundle conData = new Bundle();
        conData.putParcelable("ability", this.ability);
        Intent intent = this.getIntent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                switch(requestCode){
                    case FILE_CHOOSER:
                        if(resultCode==RESULT_OK){
                            String FilePath = data.getData().getPath();
                            Bitmap bmp = BitmapFactory.decodeFile(FilePath);
                            this.abilityImageView.setImageBitmap(bmp);
                            this.ability.setImage(FilePath);
                        }
                        break;
                }
            }
}
