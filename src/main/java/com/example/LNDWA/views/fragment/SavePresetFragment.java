package com.example.LNDWA.views.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.GameSet;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.cards.Preset;
import com.example.LNDWA.util.SaveEnum;
import com.example.LNDWA.util.Utils;
import com.example.LNDWA.views.ChooseChars;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by timo on 01.05.14.
 */
public class SavePresetFragment extends DialogFragment {

    GameSet gameset;

    public SavePresetFragment(GameSet gameset){
        this.gameset=gameset;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final EditText input = new EditText(this.getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder=new AlertDialog.Builder(this.getActivity());
        builder.setIcon(this.getResources().getDrawable(R.drawable.title)).setTitle(this.getResources().getString(R.string.presetname)
        ).setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int buttonid) {
                if (SavePresetFragment.this.savePreset()) {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(SavePresetFragment.this.getActivity(), SavePresetFragment.this.getActivity().getResources().getString(R.string.presetsaved), duration);
                    toast.show();
                } else {
                    Utils.dialogtext = SavePresetFragment.this.getResources().getString(R.string.nopresetsave);
                    SavePresetFragment.this.getActivity().showDialog(0);
                }
            }
        }).setNegativeButton(this.getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                dialogInterface.cancel();
            }
        }).setView(input);
        return builder.create();
    }

    /**
     * Saves a preset to the internal storage.
     *
     * @return a success indicator
     */
    private boolean savePreset() {
        Map<String, Integer> cardmap = new TreeMap<>();
        List<Karte> presetcardlist = ((ChooseChars)this.getActivity()).generateRoundCardList();
        if (presetcardlist == null) {
            this.getActivity().showDialog(0);
            return false;
        }
        Integer players = 0;
        for (Karte card : presetcardlist) {
            players += card.getCurrentamount();
            cardmap.put(card.getCardid(), card.getCurrentamount());
        }
        Preset preset = new Preset(cardmap, this.gameset.getGamesetid(), players, this.gameset.getTitle());
        List<Preset> presetList = new LinkedList<>();
        presetList.add(preset);
        try {
            Utils.saveFileToInternalStorage(presetList, SaveEnum.PRESET, new File(this.getActivity().getFilesDir() + "/presets/" + preset.getPresetName()));
        } catch (IOException e) {
            Utils.dialogtext = e.getMessage();
            this.getActivity().showDialog(0);
            return false;
        }
        Log.e("Preset: ", preset.toXML());
        return true;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();

    }
}
