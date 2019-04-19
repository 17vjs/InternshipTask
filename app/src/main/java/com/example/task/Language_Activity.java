package com.example.task;

import android.database.Cursor;
import android.icu.util.Currency;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class Language_Activity extends AppCompatActivity {
  private   Spinner lang,curr;
    private DatabaseHelper myDb;
   private Button save;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        curr =findViewById(R.id.eTcurrency);
        lang =findViewById(R.id.eTlanguage);
        save=findViewById(R.id.btnSave);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> languages = new ArrayList<String>();
        String language;
        for( Locale loc : locale ){
            language = loc.getDisplayLanguage();
            if( language.length() > 0 && !languages.contains(language) ){
                languages.add( language );
            }
        }
        Collections.sort(languages, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, languages);
        lang.setAdapter(adapter);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Currency currency=Currency.getInstance(Locale.getDefault());
        Set<Currency> set= currency.getAvailableCurrencies();
        ArrayList<String> currencies = new ArrayList<String>(set.size());
        for (Currency x : set)
            currencies.add(x.getDisplayName());
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.spinner_item, currencies);
        curr.setAdapter(adapter1);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        myDb=new DatabaseHelper(this);
        Cursor res1=myDb.getCurrency();
        Cursor res2=myDb.getLanguage();
        if(res1 !=null && res1.getCount()>0) {
            while (res1.moveToNext()) {
                curr.setSelection(adapter1.getPosition(res1.getString(0)));
            }
        }
        if(res2 !=null && res2.getCount()>0) {
            while (res2.moveToNext()) {
                lang.setSelection(adapter.getPosition(res2.getString(0)));

            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deletePreferences();
                myDb.setPreferences_TABLE("Language",lang.getSelectedItem().toString());
                myDb.setPreferences_TABLE("Currency",curr.getSelectedItem().toString());
                onBackPressed();
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }
}
