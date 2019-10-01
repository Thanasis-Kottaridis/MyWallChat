package com.unipi.kottarido.mywallchat.mywallchat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogClass extends AppCompatDialogFragment {

    private EditText CustomDialogText;
    private DialogListener listener;
    private String Title;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //kali ton builder gia na ftia3ei ena alertDialog
        //kai tou pernaei san context to activity apo to opoio klithike
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //kanei inflate to view pou ftia3ame gia to custom dialog ??????
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog,null);

        //vriskei kai to textbox apo to view pou ftia3ame meso tou id tou
        CustomDialogText = view.findViewById(R.id.CustomDialogText);
        //vazei ston bulder to view tou custom dialog
        //orizei ton titlo tou
        //kai ti tha kanei sto patima kathe koumpio tou (Send = Positive) (Cancel = negative)
        builder.setView(view).
                setTitle(Title)
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //den tha kanei tipota apla klinei to dialog
            }
        })
        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String UserText = CustomDialogText.getText().toString();
                listener.applyText(UserText);
            }
        });

        return builder.create();
    }

    //elenxos gia to an i class pou kalei tin auti ti class exei kanei implement to interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
           throw new ClassCastException(context.toString() + "Must implement ExampleDialogListener");
        }
    }

    //ftiaxnw auti ti methodo gt den mporw na pira3w eukola ton constructor
    public void setTitle(String title) {
        Title = title;
    }

    //ftiaxnw ena functional interface to opoio periexei mia methodo
    // i opoia dexete san orisma to text pou pliktologise o xristis
    //sto custom fialog
    public interface DialogListener{
        void applyText(String Answer);
    }
}
