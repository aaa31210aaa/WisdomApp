package tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.yqh.wisdomapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Enforcement extends Fragment {


    public Enforcement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enforcement, container, false);
    }

}
