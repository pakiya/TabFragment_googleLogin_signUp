package com.maanondev.e2softdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by pankaj on 10/5/2017.
 */

public class ExerciseFragment  extends Fragment {


    View view;
    public ExerciseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.exercise,container,false);

        ArrayList<String> data=new ArrayList<>();
        data.add("Squat");
        data.add("Leg press");
        data.add("Deadlift");
        data.add("Leg extension");
        data.add("Wall sit");
        data.add("Leg curl");
        data.add("Snatch");
        data.add("Standing calf raise");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,data);

        ListView lvData=view.findViewById(R.id.lv_exercise);
        lvData.setAdapter(adapter);
        return view;

    }
}
