package com.example.LNDWA.views.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Event;
import com.example.LNDWA.views.ViewUtils;

/**
 * Created by timo on 06.02.14.
 */
public class EditEvent extends ViewUtils {
    /**The event to edit.*
     */
    private Event event;

    /**Constructor for this class.*/
    public EditEvent(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editevent);
        if(this.getIntent().hasExtra("addevent")){
            this.setTitle(R.string.addevent);
        }else{
           this.setTitle(R.string.editevent);
        }
        this.event=this.getIntent().getExtras().getParcelable("event");
        /*The ActionBar icon.*/
        final String icon = this.getIntent().getExtras().getString("icon");
        if (this.getResources().getIdentifier(icon, "drawable", this.getPackageName()) != 0) {
            this.getSupportActionBar().setIcon(this.getResources().getIdentifier(icon, "drawable", this.getPackageName()));
        } else {
            this.getSupportActionBar().setIcon(R.drawable.title);
        }
        EditText eventName=(EditText) this.findViewById(R.id.editEventTitle);
        eventName.setText(this.event.getTitle());
        eventName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b){
                    EditEvent.this.event.setTitle(((EditText) view).getText().toString());
                }
            }
        });
        EditText eventDescription=(EditText) this.findViewById(R.id.editEventDescription);
        eventDescription.setText(this.event.getDescription());
        eventDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b){
                    EditEvent.this.event.setDescription(((EditText) view).getText().toString());
                }
            }
        });
        EditText eventProbability=(EditText) this.findViewById(R.id.editEventProb);
        eventProbability.setText(this.event.getProbability().toString());
        eventProbability.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if(!b){
                    EditEvent.this.event.setProbability(Integer.valueOf(((EditText) view).getText().toString()));
                }
            }
        });
        CheckBox checkbox=(CheckBox)this.findViewById(R.id.editEventActivecheckBox);
        checkbox.setChecked(event.getActive());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                EditEvent.this.event.setActive(b);
            }
        });
    }

  /*
    @Override
    public void onBackPressed() {
        try {
            File subdir=new File(getFilesDir(), "chars");
            File destination=new File(subdir+"/"+sourcefile+".xml");
            Utils.saveFileToInternalStorage((List<Object>) (List<?>) this.cardlist, SaveEnum.CARD, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
       super.onBackPressed();
    }*/

    @Override
    public void finish()
    {
        Bundle conData = new Bundle();
        conData.putParcelable("event", this.event);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        super.finish();
    }

}
