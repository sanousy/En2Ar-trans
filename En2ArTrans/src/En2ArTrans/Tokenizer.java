/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package En2ArTrans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sanousy
 */
public class Tokenizer
{

    private Document doc;
    private List<String> Strl = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private char[] cha = null;

    public Tokenizer()
    {
        this.doc = new Document();
    }

    public Document Tokenize(String Str)
    {
        cha = Str.toCharArray();
        CurrentSnt = new Sentence();
        doc.add(CurrentSnt);
        for (int i = 0; i < cha.length - 1; i++) {
            checkAddChar(i);
        }
        AddToken();
        return doc;
    }
    word CurrentWord = null;
    Sentence CurrentSnt = null;

    void checkAddChar(int i)
    {
        char ch = cha[i];
        if (Character.isAlphabetic(ch)) {
            sb.append(ch);
        } else if (Character.isDigit(ch)) {
            sb.append(ch);
        } else if (ch == '-' || ch == '\'' || ch == '’') {
            if (ch == '’') {
                ch = '\'';
            }
            if (i > 0) {
                if (Character.isAlphabetic(cha[i - 1]) && Character.isAlphabetic(cha[i + 1])) {
                    sb.append(ch);
                } else {
                    AddToken();
                    sb.append(ch);
                    AddToken();
                }
            } else {
                sb.append(ch);
            }

        } else if (ch == ',' || ch == '\'' || ch == '`') {
            if (i > 0) {
                if ((Character.isDigit(cha[i - 1]) && (Character.isDigit(cha[i + 1])))) {
                    sb.append(ch);
                } else {
                    AddToken();
                    sb.append(ch);
                    AddToken();
                }
            } else {
                sb.append(ch);
            }

        } else if (ch == '.') {
            if (i > 0) {
                if ((Character.isDigit(cha[i - 1]) || Character.isAlphabetic(cha[i - 1]))
                        && (Character.isDigit(cha[i + 1]) || Character.isAlphabetic(cha[i + 1]))) {
                    sb.append(ch);
                } else {
                    AddToken();
                    CurrentSnt = new Sentence();
                    doc.add(CurrentSnt);
                }
            } else {
                sb.append(ch);
            }

        } else if (ch == '\n' || ch == ' ' || ch == '\t' || ch == '\r' ) {
            AddToken();
            if (ch == '\n' || ch == '\r') {
                CurrentSnt.isEndParagraph = true;
                AddToken();
                CurrentSnt = new Sentence();
                doc.add(CurrentSnt);

            }
        } else {
            AddToken();
            sb.append(ch);
            AddToken();
        }
    }

    void AddToken()
    {
        if (sb.length() > 0) {
            CurrentSnt.add(new word(sb.toString().toUpperCase()));
            sb = new StringBuilder();
        }
    }

}
