package br.superMonitoraAgua.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.superMonitoraAgua.R;

public class SensoresFragment extends Fragment
{

    public SensoresFragment()
    {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensores, container, false);
    }
    public static SensoresFragment newInstance() {
        return new SensoresFragment();
    }
}