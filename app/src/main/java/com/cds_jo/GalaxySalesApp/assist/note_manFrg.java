package com.cds_jo.GalaxySalesApp.assist;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.GlobaleVar;
import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
import com.cds_jo.GalaxySalesApp.R;


/**
 * A simple {@link Fragment} subclass.
 */


public class note_manFrg  extends DialogFragment {
    View form ;
EditText note;
Button add,delete,btn_Cancel;
    private ExampleDialogListener listener;

  /*  @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;
        int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }*/
  @Override
  public void onStart()
  {
      super.onStart();
      if (getDialog() == null)
          return;

      int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
      int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here

      getDialog().getWindow().setLayout(dialogWidth, dialogHeight);




  }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form= inflater.inflate(R.layout.fragment_note_man_frg, container, false);

add =(Button) form.findViewById(R.id.btn_Save);
delete =(Button) form.findViewById(R.id.btn_Clear);
        btn_Cancel =(Button) form.findViewById(R.id.btn_Cancel);
note =(EditText) form.findViewById(R.id.add1);
        String oldnote = DB.GetValue(getActivity(), "Sal_invoice_Hdr", "OrderDesc", "OrderNo ='" + GlobaleVar.orderno + "' ");
note.setText(oldnote);
add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String note1 = note.getText().toString();
        listener.applyTexts(note1);
        dismiss();

    }
});
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        note.setText("");
    }
});
        return form;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface ExampleDialogListener {
        void applyTexts(String note);
        
    }
}
