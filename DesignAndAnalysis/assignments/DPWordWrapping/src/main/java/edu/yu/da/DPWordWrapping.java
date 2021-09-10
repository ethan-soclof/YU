package edu.yu.da;

/** Skeleton class for implementing a dynamic programming solution for
 * minimizing a "word wrap penalty" function for a sequence of text.  
 *
 * @author Avraham Leff
 */

import java.util.*;

public class DPWordWrapping {

  public String[] words;
  public int lineLength;
  int[] dp;
  int [] solution;
  Map<Integer, List<String>> map;
/*
  public static void main(String[] args){
    String line = "";
    String[] words = line.split(" ");
    DPWordWrapping wordWrap = new DPWordWrapping(words, 60);
    Map<Integer, List<String>> map = wordWrap.getLayout();

    for (int i = 0; i < map.size(); i++){
      List<String> list = map.get(i);
      for (int j = 0; j < list.size(); j++){
        System.out.printf("%s ", list.get(j));
      }
      System.out.println();
    }
    System.out.printf("Penalty: %d", wordWrap.minimumPenalty());
  }
  */

  /** No-argument constructor.
   *
   * @param words a non-null, non-empty, sequence of words
   * @param lineLength a positive value that defines the line length for all
   * lines in the computed display
   */
  public DPWordWrapping(final String[] words, final int lineLength) {
    this.words = words;
    this.lineLength = lineLength;
    this.dp = new int[words.length];
    this.solution = new int[words.length];
    this.calculateMinimumPenalty();
    this.constructLayout();
  }

  public void calculateMinimumPenalty(){
    int penaltyMeta = lineLength;
    for (int i = 0; i < words.length; i++){
      int min = Integer.MAX_VALUE;
      penaltyMeta -= words[i].length();
      if (i > 0){
        penaltyMeta--;
      }
      int penaltyGlobal = penaltyMeta;
      for (int j = 0; j <= i; j++){
        int penaltyLocal;
        if (j > 0){
          penaltyGlobal += (words[j-1].length() + 1);
        }
        if (penaltyGlobal < 0){
          penaltyLocal = Integer.MAX_VALUE;
        } else {
          penaltyLocal = (int) Math.pow(penaltyGlobal, 2);
        }
        if (j == 0){
          dp[i] = penaltyLocal;
          min = penaltyLocal;
          solution[i] = 0;
          continue;
        }
        if (penaltyLocal < Integer.MAX_VALUE && dp[j-1] + penaltyLocal < min){
          min = dp[j-1] + penaltyLocal;
          dp[i] = min;
          solution[i] = j;
        }
      }
    }
  }

  public void constructLayout(){
    int i = solution.length - 1;
    List<List<String>> lines = new ArrayList<List<String>>();
    while (i >= 0){
      int firstWordOnLine = solution[i];
      List<String> line = new ArrayList<String>();
      for (int j = firstWordOnLine; j <= i; j++){
        line.add(words[j]);
      }
      lines.add(0, line);
      i = firstWordOnLine - 1;
    }
    map = new HashMap<Integer, List<String>>();
    for (int j = 0; j < lines.size(); j++){
      map.put(j, lines.get(j));
    }
  }

  /** Using the rules and constraints defined in the requirements document,
   * return the value of the optimal total penalty for the specified sequence
   * of words and constant line length parameter supplied to the constructor.
   *
   * @return the optimal total penalty to layout the words
   */
  public int minimumPenalty() {
    return dp[dp.length-1];
  }

  /** Returns the optimal layout for the specified sequence of words and
   * constant line parameter supplied to the constructor.  If no optimal total
   * penalty value can be computed per the requirements doc, the layout is undefined.
   *
   * @return layout A Map which associates with each line in the layout
   * (0..n-1), a sequence of words that conforms to the rules specified in the
   * requirements doc.
   */
  public Map<Integer, List<String>> getLayout() {
    return map;
  }
}