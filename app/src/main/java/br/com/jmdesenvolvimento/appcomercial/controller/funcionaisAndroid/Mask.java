package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;

public class Mask {
    public static String CPF = "###.###.###-###";
    public static String CNPJ = "##.###.###/####-##";
    public static String TELEFONE = "(##)####-#####";
    public static String CEP = "#####-###";
    public static String DATA = "##/##/####";
    public String mask;

    public String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll(" ", "")
                .replaceAll(",", "");
    }

    public boolean isASign(char c) {
        if (c == '.' || c == '-' || c == '/' || c == '(' || c == ')' || c == ',' || c == ' ') {
            return true;
        } else {
            return false;
        }
    }

    public String mask(String mask, String text) {
        int i = 0;
        String mascara = "";
        for (char m : mask.toCharArray()) {
            if (m != '#') {
                mascara += m;
                continue;
            }
            try {
                mascara += text.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }

        return mascara;
    }

    public TextWatcher insert(String tipo, final TextView ediTxt) {

        mask = tipo;

        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss ="*" + ediTxt.getText().toString() + "*";
                if(ediTxt.getText().toString().length() > 0 && !ediTxt.getText().toString().equals(" ")) {
                    String str = unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    String texto = ediTxt.getText().toString();
                    if (mask.equals(CPF) &&
                            FuncoesGerais.removePontosTracos(texto).length() > 11) {
                        mask = CNPJ;
                    }

                    int index = 0;
                    for (int i = 0; i < mask.length() + 1; i++) {
                        if (i == mask.length()) {
                            continue;
                        }
                        char m = mask.charAt(i);
                        if (m != '#') {
                            if (index == str.length() && str.length() < old.length()) {
                                continue;
                            }
                            mascara += m;
                            continue;
                        }

                        try {
                            mascara += str.charAt(index);
                        } catch (Exception e) {
                            break;
                        }

                        index++;
                    }

                    if (mascara.length() > 0) {
                        char last_char = mascara.charAt(mascara.length() - 1);
                        boolean hadSign = false;
                        while (isASign(last_char) && str.length() == old.length()) {
                            mascara = mascara.substring(0, mascara.length() - 1);
                            last_char = mascara.charAt(mascara.length() - 1);
                            hadSign = true;
                        }

                        if (mascara.length() > 0 && hadSign) {
                            mascara = mascara.substring(0, mascara.length() - 1);
                        }
                    }

                    isUpdating = true;
                    ediTxt.setText(mascara);
                    Class clas = ediTxt.getClass();
                    String simpleNome = clas.getSimpleName();
                    if (simpleNome.toLowerCase().contains("edittext")) {
                        ((EditText) ediTxt).setSelection(mascara.length());
                    }
                }
//                if(ediTxt.getText().toString().length() == 0){
//                    ediTxt.setText("");
//                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
}
