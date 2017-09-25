/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package En2ArTrans;

import java.util.*;
import java.io.*;
import static java.lang.Math.sqrt;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;

/**
 *
 * @author Sanousy
 */
public class Translate extends Thread
{

    ResultSet rs = null;
    Connection c = null;
    Statement stmt = null;
    String dbName = "jdbc:sqlite:context.db";

    public List<String> unknownWords;
    Document arb = new Document();

    public Translate()
    {

    }

    private Document doc;
    // List<String> MMB = new ArrayList<>();
    ArrayList<String> stopWords;

    void connectDB()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(dbName);
            c.setAutoCommit(false);
            stmt = c.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void closeDB()
    {
        try {
            rs.close();
            stmt.close();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    JLabel status;

    public String text(String E, JLabel lblStatus)
    {
        this.status = lblStatus;
        this.unknownWords = new ArrayList<>();
        status.setText("قراءة الكلمات");
        doc = new Tokenizer().Tokenize(E);
        analyze1();
        //   translate1();
        //   analyze2();
        return sb.toString();
    }
    StringBuilder sb = new StringBuilder();

    class nominatedMeaning
    {

        public String meaning, phrase;
        public int MatchLevel;
        public int words2skip;
    }
    List<nominatedMeaning> NominatedMeanings = new ArrayList<>();

    private void analyze1()
    {

        connectDB();
        int sc = 0;
        String phrase = "";
        String meaning = "";
        String contextA = "";
        String contextB = "";
        String location = "";

        int skipWords = 1;
        String[] words = null;
        boolean found = false;
        sb = new StringBuilder();
        for (Sentence S : doc.getSentences()) {
            sc++;
            String lbl = "جاري التحليل : جملة رقم" + sc + "/" + doc.length;
            status.setText(lbl);
            String subSql = "";
            String subSqlA = "";
            String subSqlB = "";
            String sql = "";

            for (int i = 0; i < S.length; i += skipWords) {
                found = false;

                if (Character.isAlphabetic(S.word(i).charAt(0))) {
                    String conj = "";

//                    if (i < S.length -1)
//                    {
//                                                sql = "Select * from context where phrase like \"" + S.word(i) + " " + S.word(i+1) + " %\"  ";
//
//                    }
//                    else
                    {
                        sql = "Select * from context where ( phrase like \"" + S.word(i) + " %\" or  phrase like \"" + S.word(i) + "\")  ";

                    }

//                    for (int j = i - 2; j < i; j++) {
//                        if (S.word(j).length() > 1) {
//                            subSqlB += conj + " contextB like\"%" + S.word(j) + "%\" ";
//                            conj = " or ";
//                        }
//                    }
//
//                    for (int j = i + 1; j < i + 2; j++) {
//                        if (S.word(j).length() > 1) {
//                            subSqlB += conj + " contextA like\"%" + S.word(j) + "%\" ";
//                            conj = " or ";
//                        }
//                    }
//TODO:  Disabled for sake of creating Check best fit functinoality that compare context conditions
//                    if (subSqlB.length() > 0) {
//                        sql += " and (" + subSqlB + ") ";
//                    }
//                    if (i == 0) {
//                        sql += " and ( location = '^' or location = '*') ";
//                    }
//                    if (i == S.length - 1) {
//                        sql += " and ( location = '$' or location = '*') ";
//                    }
                    sql += "  order by length(phrase) desc, LENGTH(CONTEXTB) DESC, LENGTH(CONTEXTA) DESC;";
                    //System.out.println(sql);
                    try {
                        rs = stmt.executeQuery(sql);
                        ResultSetMetaData md = rs.getMetaData();
                        int columns = md.getColumnCount();

                        meaning = "";
                        while (rs.next()) {
                            phrase = rs.getString("phrase");
                            meaning = rs.getString("meaning");
                            contextA = rs.getString("ContextA");
                            contextB = rs.getString("ContextB");
                            location = rs.getString("location");

                            System.out.println(phrase + "  " + contextA + " " + contextB);
                            words = phrase.split(" ");
                            int cw = 0;
                            found = true;
                            for (String wd : words) {
                                if (wd.compareToIgnoreCase(S.word(i + cw)) != 0) {
                                    found = false;
                                }
                                cw++;
                            }

                            if (found) {
                                nominatedMeaning nom = new nominatedMeaning();
                                nom.phrase = phrase;
                                nom.meaning = meaning;
                                nom.words2skip = words.length;
                                nom.MatchLevel = (int) sqrt(10000 - words.length * words.length);
                                int ix = 0;
                                int Depth = 7;
                                int rad = Depth;
                                int Effect = 0;
                                for (int j = 1; j < Depth; j++) {
                                    Effect = (int) (Math.pow((double) rad, 2.0));
                                    ix = i - j;
                                    if (S.word(ix).length() > 1 && contextB != null) {
                                        if (contextB.contains(S.word(ix))) {
                                            System.out.println("context before match: " + S.word(ix) + "closer by: " + Effect);
                                            nom.MatchLevel -= Effect;
                                        } else {
                                            nom.MatchLevel += Effect;
                                            System.out.println("context before mismatch: " + S.word(ix) + "farter by: " + Effect);

                                        }
                                        ix = i + j;
                                        if (S.word(ix).length() > 1 && contextA != null) {
                                            if (contextA.contains(S.word(ix))) {
                                                nom.MatchLevel -= Effect;
                                                System.out.println("context before match: " + S.word(ix) + "closer by: " + Effect);
                                            } else {
                                                nom.MatchLevel += Effect;
                                                System.out.println("context before mismatch: " + S.word(ix) + "farter by: " + Effect);
                                            }
                                        }
                                    }
                                    rad--;
                                }
                                System.out.println(nom.MatchLevel);
                                NominatedMeanings.add(nom);

                                found = false;
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (NominatedMeanings.size() > 0) {
                    int HiMatch = Integer.MAX_VALUE;
                    int HMIx = 0;
                    for (int xx = 0; xx < NominatedMeanings.size(); xx++) {
                        nominatedMeaning nm = NominatedMeanings.get(xx);
                        System.out.println("Found:" + nm.phrase + " ==> (" + nm.meaning + ") Score = " + nm.MatchLevel);
                        if (nm.MatchLevel < HiMatch) {
                            HiMatch = nm.MatchLevel;
                            HMIx = xx;
                        }
                    }
                        String sp = " ";
                        if (NominatedMeanings.get(HMIx).meaning.endsWith(" ال") || NominatedMeanings.get(HMIx).meaning.compareTo("ال") == 0) {
                            sp = "";
                        }
                        sb.append(NominatedMeanings.get(HMIx).meaning + sp);
                        skipWords = NominatedMeanings.get(HMIx).words2skip;
                        NominatedMeanings.clear();
                } else {
                    sb.append(S.word(i)).append(" ");
                    skipWords = 1;
                }
            }
        }
        closeDB();
        PrintWriter out = null;
        try {
            out = new PrintWriter("Missing Words.csv");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        }

        String txt = "operation,word,root,gender,plurality,activeness,arabicRoot,isVerb,isNoun,isAdj,isAdv,isDif,tense,isAux";
        out.println(txt);

        for (String mw : unknownWords) {
            String st = mw.toUpperCase();
            txt = "A," + st + "," + st + ",0,0,0,,0,0,0,0,0,0,0";
            out.println(txt);
        }
        out.flush();
        out.close();
    }

    private void analyze2()
    {

        int sc = 0;
        for (Sentence S : doc.getSentences()) {
            sc++;
            String lbl = "جاري التحليل2 : جملة رقم" + sc + "/" + doc.length;
            status.setText(lbl);
            System.out.print("(");
            for (int i = 0; i < S.length; i++) {
                if (S.itemAt(i).isAdv) {
                    System.out.print(") " + S.itemAt(i).word + "(");
                } else if (eq(S.itemAt(i).word, "TO")) {
                    if (i + 1 < S.length) {
                        if (!S.itemAt(i + 1).isVerb) {
                            System.out.print(" " + S.itemAt(i).word + "(");
                        }
                    }
                } else if (S.itemAt(i).isVerb) {
                    if (i > 0) {
                        if (S.itemAt(i - 1).isAdj) {
                            System.out.print(" " + S.itemAt(i).word);
                        } else if (eq(S.itemAt(i - 1).word, "TO")) {
                            System.out.print(" " + S.itemAt(i).word + "ing");
                        } else {
                            System.out.print(") <==" + S.itemAt(i).word + "(");
                        }
                    } else {
                        System.out.print(" " + S.itemAt(i).word + "(");
                    }
                } else if (S.itemAt(i).isPunct()) {
                    System.out.print(") " + S.itemAt(i).word + "(");
                } else if (S.itemAt(i).isDif) {
                    System.out.print(" " + S.itemAt(i).word + "(");
                } else {
                    System.out.print(" " + S.itemAt(i).word);
                }
            }
            System.out.print(")+");
            System.out.println();
        }
    }

    boolean eq(String a, String b
    )
    {
        return (a.compareTo(b) == 0);
    }

    private void translate1()
    {

        // here in this translation module we have only to identify the actors and the verbs
        // actors must carry "the" , "a" , Adjectives along with them, this means to split the scentence into actors and actions ( which is somehow similar to object oriented methods)
        // this method will tell us where to find the object, and its adjectives, later we find what he does ( usually one of the important parts is to keep track of the tense).
        // ing clauses are somehow complicated but could be simplified to the following rules:
        // 1. if comes in the start of the scenentce, this means they are nouns
        // if they come after a "Verb to be " this means they are verbs.
        connectDB();
        int sc = 0;
        for (Sentence S : doc.getSentences()) {
            sc++;
            String lbl = "جاري الترجمة : جملة رقم" + sc + "/" + doc.length;
            status.setText(lbl);
            for (int i = 0; i < S.length; i++) {
                if (S.itemAt(i).isAdj || S.itemAt(i).isNoun || S.itemAt(i).isVerb) {
                    int j = i;
                    while (S.itemAt(i).isAdj || S.itemAt(i).isNoun) {
                        i++;
                        if (i >= S.length) {
                            break;
                        }
                    }
                    S.swapWords(j, i - 1);
                }
            }
        }
    }

}
