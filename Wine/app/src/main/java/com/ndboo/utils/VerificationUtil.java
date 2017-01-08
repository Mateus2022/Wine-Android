package com.ndboo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Li on 2016/10/24.
 * 一些验证
 */

public class VerificationUtil {

    /**
     * 无法输入空格的EditText
     * @param editText  处理的编辑框
     */
    public static void editTextNoSpace(final EditText editText){


        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editText.setText(str1);

                    editText.setSelection(start);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
