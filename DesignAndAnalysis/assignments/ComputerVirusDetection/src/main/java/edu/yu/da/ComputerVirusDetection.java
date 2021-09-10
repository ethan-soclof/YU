package edu.yu.da;

import java.util.Objects;

/** A skeleton class to structure implementations of solutions to the "Computer
 * Virus Detection" problem.
 *
 * @author Avraham Leff
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComputerVirusDetection {
  public final static int NO_VIRUS_QUALIFIES = -1;

  // Implement this interface any way that you like: I will test
  // your code with my implementation
  public interface VirusChecker {
    /** Returns true iff one Virus is "equal" to another Virus
     */
    boolean areEqual(final Virus virus1, final Virus virus2);
  }

  public static class MP {
    public int index;
    public int amount;

    public MP(int index, int amount){
      this.index = index;
      this.amount = amount;
    }
  }


  // You may not change this inner class in any way!
  public static class Virus {
    /** Constructor
     *
     * @param code a String containing the code for the virus
     */
    public Virus(final String code, final VirusChecker virusChecker) {
      Objects.requireNonNull(code, "code can't be null");
      if (code.isEmpty()) {
        throw new IllegalArgumentException("Empty 'code' parameter");
      }
      Objects.requireNonNull(virusChecker, "Virus checker can't be null");

      this.code = code;
      this.virusChecker = virusChecker;
    }

    public String getCode() { return code; }

    @Override
    public String toString() {
      return "Virus[code="+code+"]";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof Virus)) {
        return false;
      }

      final Virus that = (Virus) o;
      return virusChecker.areEqual(this, that);
    }

    @Override
    public int hashCode() {
      // Every virus has its own hashCode value, regardless of whether or not
      // it's equivalent to another
      return System.identityHashCode(code);
    }

    private final String code;
    private final VirusChecker virusChecker;
  }

  /** Returns the index of any virus instance that is a member of the "most
   * prevalent virus class" iff one exists, NO_VIRUS_QUALIFIES if none
   * qualifies.
   *
   * @param viruses Array of viruses to be evaluated
   * @param checker Implements the VirusChecker interface
   * @return index of a virus that, with respect to the specified VirusChecker,
   * determines a "most prevalent virus" equivalence class; or
   * NO_VIRUS_QUALIFIES if no virus set in the input qualifies as a "most
   * prevalent virus".
   *
   * Note: assuming the a "most prevalent virus" exists, multiple indices
   * typically qualify as the return value.
   */
  public static int mostPrevalent(final Virus[] viruses, final VirusChecker checker)
  {
    MP mp = mostPrevalent(viruses, checker, 0, viruses.length - 1);
    if (mp == null){
      return NO_VIRUS_QUALIFIES;
    }
    return mp.index;
  }

  private static MP mostPrevalent(final Virus[] viruses, final VirusChecker checker, int start, int end){
    if (end - start == 0){
      MP mp = new MP(start, 1);
      return mp;
    }
    if (end - start == 1){
      if (checker.areEqual(viruses[start], viruses[end])){
        MP mp = new MP(start, 2);
        return mp;
      }
      return null;
    }
    if (end - start <= 2){
      if (checker.areEqual(viruses[start], viruses[end]) && checker.areEqual(viruses[start], viruses[end-1])){
        MP mp = new MP(start, 3);
        return mp;
      }
      if (checker.areEqual(viruses[start], viruses[end]) || checker.areEqual(viruses[start], viruses[end-1])){
        MP mp = new MP(start, 2);
        return mp;
      }
      if (checker.areEqual(viruses[start+1], viruses[end])){
        MP mp = new MP(start + 1, 2);
        return mp;
      }
    }

    int mid = start + (end - start)/2;
    MP mp1 = mostPrevalent(viruses, checker, start, mid);
    MP mp2 = mostPrevalent(viruses, checker, mid + 1, end);

    if (mp1 != null){
      int amount = mp1.amount;
      for (int i = mid + 1; i <= end; i++){
        if (checker.areEqual(viruses[mp1.index], viruses[i])){
          amount++;
        }
      }
      if (amount > (end - start + 1)/2){
        MP mp = new MP(mp1.index, amount);
        return mp;
      }
    }
    if (mp2 != null){
      int amount = mp2.amount;
      for (int i = start; i <= mid; i++){
        if (checker.areEqual(viruses[mp2.index], viruses[i])){
          amount++;
        }
      }
      if (amount > (end - start + 1)/2){
        MP mp = new MP(mp2.index, amount);
        return mp;
      }
    }
    return null;
  }
/*
  public static void main(String[] args) {
    VirusChecker vc = new VC();
    Virus v1 = new Virus("1", vc);
    Virus v2 = new Virus("2", vc);
    Virus v3 = new Virus("3", vc);
    Virus v4 = new Virus("4", vc);
    Virus v5 = new Virus("5", vc);
    Virus v6 = new Virus("6", vc);
    Virus[] viruses = {v4, v4, v4, v5, v2, v1, v2, v3, v4, v4};
    System.out.println(viruses[mostPrevalent(viruses, vc)].getCode());
  }

 */

  private final static Logger logger = LogManager.getLogger(ComputerVirusDetection.class);
}
