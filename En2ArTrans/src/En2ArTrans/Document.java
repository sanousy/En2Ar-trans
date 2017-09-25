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
public class Document
{

    private final List<Sentence> doc;
    int index = 0;
    int length = 0;

    public Document()
    {
        this.doc = new ArrayList<>();

    }

    public void add(Sentence s)
    {
        length++;
        doc.add(s);
    }

    public List<Sentence> getSentences()
    {
        return doc;
    }

    public Sentence getNextSentence()
    {
        if (index < length - 1) {
            return doc.get(index++);
        }

        return null;
    }

    public void goToStart()
    {
        index = 0;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        for (Sentence S : doc) {

            for (word W : S.getWords()) {
                if (W.Arabic.trim().length() > 0) {
                    result.append(W.Arabic).append(" ");
                    //System.out.println(W.Arabic);
                } else {
                    result.append(W.word).append(" ");
                }

                //   System.out.println(W.word);
            }
            if (S.isEndParagraph) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
