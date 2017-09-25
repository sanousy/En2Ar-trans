/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package En2ArTrans;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Sanousy
 */
public class Sentence
{

    private final List<word> snt;
    int index = 0;
    int length = 0;
    private Document doc;
    public boolean isEndParagraph = false;

    public Sentence()
    {
        this.snt = new ArrayList<>();
    }

    public void add(word w)
    {
        snt.add(w);
        length++;
    }

    public void add(int index, word w)
    {
        snt.add(index, w);
        length++;
    }

    public List<word> getWords()
    {
        return snt;
    }

    public word getNextWord()
    {
        return snt.get(index++);
    }

    public word itemAt(int i)
    {
        if (i > 0 && i < length) {
            return snt.get(i);
        } else {
            return null;
        }
    }
    
    
    public String word(int i)
    {
        if (i >= 0 && i < length) {
            return snt.get(i).word;
        } else {
            return "";
        }
    }

    public void Remove(int i)
    {
        snt.remove(i);
        length--;
    }

    public void swapWords(int i, int j)
    {
        if (i == j) {
            return;
        }
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        Stack<word> st = new Stack<>();
        for (int x = i; x <= j; x++) {
            try {
                st.push(snt.get(i));
                snt.remove(i);
            } catch (Exception exs) {

            }

        }
        while (!st.empty()) {
            snt.add(i++, st.pop());
        }
    }

    public void goToStart()
    {
        index = 0;
    }
}
