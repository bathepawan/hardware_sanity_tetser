package com.pawanbathe.hardwaresanitytester;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pbathe on 2/4/16.
 */
public class AboutFragment extends Fragment {

    View viewAboutFragment=null;
    public AboutFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewAboutFragment = inflater.inflate(R.layout.fragment_about, container, false);
        TextView textView =(TextView)viewAboutFragment.findViewById(R.id.web);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "For Data Interpretation: <a href=\"http://hst.pawanbathe.com\">http://hst.pawanbathe.com</a>";
        textView.setText(Html.fromHtml(text));
        return viewAboutFragment;
    }

}
