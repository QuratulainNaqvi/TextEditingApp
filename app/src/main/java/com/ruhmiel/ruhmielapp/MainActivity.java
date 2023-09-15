package com.ruhmiel.ruhmielapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Toast;

import com.ruhmiel.ruhmielapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        String string = getString(R.string.paragraph);

        String textToUnderline = getString(R.string.text_to_underline);

        SpannableString text = new SpannableString(string);

        int[] range = getStartingAndEndOfSentence(string, textToUnderline);

        DashedUnderlineSpan dottedLineSpan = new DashedUnderlineSpan(ContextCompat.getColor(this, R.color.underline_color), textToUnderline, this);
        text.setSpan(dottedLineSpan, range[0], range[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int startIndex = string.indexOf(textToUnderline);
        int endIndex = startIndex + textToUnderline.length();


        if (startIndex >= 0) {
            // Create a ClickableSpan for the clickable text
            CustomClickableSpan clickableSpan = new CustomClickableSpan(textToUnderline,"a mark appearing above a letter",binding.paragraph,this);
            text.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set the formatted text to the TextView
            binding.paragraph.setText(text);
            binding.paragraph.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            binding.paragraph.setText(string);
        }


    }
    int[] getStartingAndEndOfSentence(String wholeString, String partOfAString) {

        int[] range = new int[2];

        String[] s1 = wholeString.split("\\s+");
        String[] s2 = partOfAString.split("\\s+");

        if (s2.length == 1) {
            String word = s2[0];
            range[0] = wholeString.indexOf(word);
            range[1] = range[0] + word.length();
        } else {
            int length = 0;
            for (int i = 0; i < s1.length; i++) {
                length = length + s1[i].length() + 1;
                if (s1[i].equals(s2[0])) {
                    if(s1[i+1].equals(s2[1])) {
                        range[0] = length - (s1[i].length() + 1);
                        range[1] = range[0] + partOfAString.length();
                        break;
                    }
                }
            }
        }
        return range;
    }
}