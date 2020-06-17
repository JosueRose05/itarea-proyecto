package com.example.itarea;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModificarTareaFragment extends Fragment {

    TextView tvMostrar;
    ImageButton close;

    public ModificarTareaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View fragTarea = inflater.inflate(R.layout.fragment_modificar_tarea, container, false);
        tvMostrar = (TextView) fragTarea.findViewById(R.id.tvShowAct);
        close = (ImageButton) fragTarea.findViewById(R.id.btnCerrar);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(ModificarTareaFragment.this).commitAllowingStateLoss();
            }
        });//


        return fragTarea;
    }
}
