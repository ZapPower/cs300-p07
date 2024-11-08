// TODO complete file header

import java.util.NoSuchElementException;

/**
 * This class tests the LoopStation class, and by extension, the Track class
 */
public class LoopStationTester {
  
  /**
   * Checks the correctness of the createPod() method. This method should:
   * - create a Pod with the given capacity and podClass
   * - add it to the correct end of the correct Track in the LoopStation
   * - return a reference (shallow copy) to that Pod
   * Note that the tracks in LoopStation are protected, so you may access them directly for testing
   * purposes
   * @return true if createPod() is functioning correctly, false otherwise
   */
  public static boolean testCreatePod() {
    LoopStation ls = new LoopStation();

    // Fill with dummy pods
    ls.createPod(1, true);
    ls.createPod(1, true);
    ls.createPod(1, false);
    ls.createPod(1, false);

    // Add tester pods
    Pod p1 = ls.createPod(5, true);
    Pod p2 = ls.createPod(5, false);

    boolean valid = true;

    // Check that returned pod is in correct position
    valid &= p1.equals(ls.waitingFirst.head.getPod());
    valid &= p2.equals(ls.waitingEconomy.tail.getPod());

    return valid;
  }
  
  /**
   * Checks the correctness of the launchPod() method. This method should:
   * - throw a NoSuchElementException if no pods are waiting to launch
   * - launch first class pods from the END of the waitingFirst track
   * - launch economy class pods from the BEGINNING of the waitingEconomy track
   * - launch ALL first class pods before launching ANY economy class pods
   * Note that the tracks in LoopStation are protected, so you may access them directly for testing
   * purposes
   * @return true if launchPod() is functioning correctly, false otherwise
   */
  public static boolean testLaunchPod() {
    boolean valid = true;

    LoopStation ls = new LoopStation();
    
    try {
      ls.launchPod();
      valid = false;
    } catch (NoSuchElementException e) {}
    catch (Exception e) {
      return false;
    }
    
    ls.createPod(1, true);
    // Get second to first pod to check removal
    Pod p1 = ls.createPod(2, true);
    ls.createPod(1, true);

    ls.createPod(1, false);
    Pod p2 = ls.createPod(1, false);
    ls.createPod(1, false);
    
    try {
      p1.addPassenger("Dave");
      p1.addPassenger("John");
    } catch (MalfunctioningPodException e) {
      System.out.println(e);
    }

    try {
      ls.launchPod();
      
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingFirst.head.getPod().equals(p1);

    try {
      for (int i = 0; i < 2; i++) {
        ls.launchPod();
      }
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingFirst.isEmpty();


    try {
      ls.launchPod();
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingEconomy.tail.getPod().equals(p2);

    try {
      for (int i = 0; i < 2; i++) {
        ls.launchPod();
      }
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingEconomy.isEmpty();

    return valid;
  }
  
  /**
   * Checks the correctness of the clearMalfunctioning() method. This method should:
   * - repeatedly check the launched track for malfunctioning pods
   * - remove those pods correctly
   * - report the number of pods it removed once there are no longer any malfunctioning pods
   * 
   * Things to consider when you are testing:
   * 
   * - there is a protected setNonFunctional() method you may use for testing purposes to ensure
   *   that at least one pod is non-functional
   *   
   * - calling isFunctional() on a Pod may cause it to malfunction! You should come up with an
   *   alternate way to check whether a Pod is functional, if you have not already.
   *   
   * - verify that the difference in number of pods from before the method was called and after
   *   the method was called is equal to the number that it reported
   *   
   * @return true if clearMalfunctioning() is functioning correctly, false otherwise
   */
  public static boolean testClearMalfunctioning() {
    return false;
  }
  
  /**
   * Checks the correctness of the three getNumXXX() methods from LoopStation. This will require
   * adding Pods of various types, loading them with passengers, and launching them.
   * @return true if the getNumXXX() methods are all functioning correctly, false otherwise
   */
  public static boolean testGetNums() {
    return false;
  }

  public static void main(String[] args) {
    boolean test1 = testCreatePod();
    System.out.println("testCreatePod: "+(test1?"PASS":"fail"));
    
    boolean test2 = testLaunchPod();
    System.out.println("testLaunchPod: "+(test2?"PASS":"fail"));
    
    boolean test3 = testClearMalfunctioning();
    System.out.println("testClearMalfunctioning: "+(test3?"PASS":"fail"));
    
    boolean test4 = testGetNums();
    System.out.println("testGetNums: "+(test4?"PASS":"fail"));
    
    System.out.println("ALL TESTS: "+((test1&&test2&&test3&&test4)?"PASS":"fail"));
  }

}
