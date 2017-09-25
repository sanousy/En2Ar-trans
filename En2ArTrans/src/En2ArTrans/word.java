/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package En2ArTrans;

/**
 *
 * @author Sanousy
 */
public class word
{

     public enum GENDER
    {

        male(1),
        female(2),
        unknown(0);

        private final int value;

        private GENDER(int value)
        {
            this.value = value;
        }

        public int toInt()
        {
            return value;
        }
        
        public static GENDER val(int x)
        {
            for (GENDER i : GENDER.values()) {
                if (x == i.toInt()) {
                    return i;
                }
            }
            return unknown;
        }
    }

    public enum PLURALITY
    {

        one(1),
        two(2),
        several(4),
        unknown(0);

        private final int value;

        private PLURALITY(int value)
        {
            this.value = value;
        }

        public int toInt()
        {
            return value;
        }
        public static PLURALITY val(int x)
        {
            for (PLURALITY i : PLURALITY.values()) {
                if (x == i.toInt()) {
                    return i;
                }
            }
            return unknown;
        }
    }

    public enum ACTIVENESS
    {

        active(1),
        passive(2),
        unknown(0);
        private final int value;

        private ACTIVENESS(int value)
        {
            this.value = value;
        }

        public static ACTIVENESS val(int x)
        {
            for (ACTIVENESS i : ACTIVENESS.values()) {
                if (x == i.toInt()) {
                    return i;
                }
            }
            return unknown;
        }

        public int toInt()
        {
            return value;
        }

    }

    public enum TENSE
    {

        past(1),
        present(2),
        command(4),
        unknown(0);

        private final int value;

        private TENSE(int value)
        {
            this.value = value;
        }

        public int toInt()
        {
            return value;
        }
        public static TENSE val(int x)
        {
            for (TENSE i : TENSE.values()) {
                if (x == i.toInt()) {
                    return i;
                }
            }
            return unknown;
        }
    }

    public String word;
    public GENDER gender;
    public String root;
    public PLURALITY plurality;
    public ACTIVENESS activeness;
    public TENSE tense;
    public String Arabic;
    public boolean isNoun;
    public boolean isVerb;
    public boolean isAdj;
    public boolean isAdv;
    public boolean isDif;
    public boolean isAux;
    

    public word(String s)
    {
        this.word = s;
        gender = GENDER.unknown;
        root = "";
        plurality = PLURALITY.unknown;
        activeness = ACTIVENESS.unknown;
        Arabic = "";
        tense = TENSE.unknown;
        isNoun = false;
        isVerb = false;
        isAdj = false;
        isAdv = false;
        isDif = false;
        isAux = false;
    }

    public String GetArabicMeaning()
    {

        return Arabic;
    }

    public boolean isAlpha()
    {
        return Character.isAlphabetic(word.charAt(0));
    }

    public boolean isDigits()
    {
        return Character.isDigit(word.charAt(0));
    }

    public boolean isPunct()
    {
        return !(isDigits() || isAlpha());
    }
}
