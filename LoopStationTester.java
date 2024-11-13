//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    P07 - Hyperloop
// Course:   CS 300 Fall 2024
//
// Author: Tomasz Sobkowicz-Oliveira 
// Email: sobkowiczoli@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
// 
// Partner Name: David Greenberg
// Partner Email: dgreenberg8@wisc.edu
// Partner Lecturer's Name: Hobbes LeGault
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   X Write-up states that pair programming is allowed for this assignment.
//   X We have both read and understand the course Pair Programming Policy.
//   X We have registered our team prior to the team registration deadline.
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons: NONE
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

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

    // Check that returned pods are in correct position
    valid &= p1.equals(ls.waitingFirst.head.getPod());
    valid &= p2.equals(ls.waitingEconomy.tail.getPod());

    return valid;
  }

  /**
   * Checks the correctness of the launchPod() method.
   * This method should:
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
      return false; // No exception was thrown
    } catch (NoSuchElementException e) {/* Method throws the correct exception */}
    catch (Exception e) {
      return false; // Any other exception is incorrect
    }

    // First class pods
    ls.createPod(4, true); // this pod should be removed
    Pod p1 = ls.createPod(3, true); // this should be the new tail
    ls.createPod(2, true);
    ls.createPod(1, true);

    // Economy pods
    ls.createPod(1, false); // this pod should be removed
    Pod p2 = ls.createPod(2, false); // this should be the new head 
    ls.createPod(3, false);
    ls.createPod(4, false);

    try {
      ls.launchPod();
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingFirst.tail.getPod().equals(p1);

    try {
      // launch the three remaining pods in first class
      ls.launchPod();
      ls.launchPod();
      ls.launchPod();
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingFirst.isEmpty(); // list should be empty

    try {
      ls.launchPod();
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingEconomy.head.getPod().equals(p2);

    try {
      // launch the three remaining pods
      ls.launchPod();
      ls.launchPod();
      ls.launchPod();
    } catch (Exception e) {
      return false;
    }

    valid &= ls.waitingEconomy.isEmpty();

    return valid;
  }

  /**
   * Checks the correctness of the clearMalfunctioning() method.
   * This method should:
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
    boolean valid = true;

    LoopStation ls = new LoopStation();
    
    // record the expected track after calling clearMalfunctioning()
    Track clearTrack = new Track();

    // create and launch 8 pods with arbitrary atributes and
    // set 3 of them to be malfunctioning
    for (int i = 0; i < 8; ++i) {
      Pod pod = ls.createPod(i + 1, (i % 2 == 0));

      try {
        ls.launchPod();
      } catch (NoSuchElementException e) {
        return false;
      }
      
      if (i == 0 || i == 5 || i == 7)
        pod.setNonFunctional();
      else
        clearTrack.add(pod);
    }

    int removedCount = ls.clearMalfunctioning();

    valid &= removedCount == 3; // verify the correct removal count
    
    // compare launched track with expectation
    for (int i = 0; i < clearTrack.size(); ++i) {
      Pod clearPod = clearTrack.get(i);
      Pod testPod;
      try {
        testPod = ls.launched.get(i);
      } catch (Exception e) {return false;}

      valid &= clearPod.equals(testPod);
    }

    return valid;
  }

  /**
   * Checks the correctness of the three getNumXXX() methods from LoopStation.
   * This will require adding Pods of various types, loading them with passengers, and launching
   * them.
   *
   * @return true if the getNumXXX() methods are all functioning correctly, false otherwise
   */
  public static boolean testGetNums() {
    boolean valid = true;

    LoopStation ls = new LoopStation();
    int passengerCount = 0;

    // create 10 pods with arbitrary atributes and number of passengers
    for (int i = 0; i < 10; ++i) {
      Pod pod = ls.createPod(2 * i + 1, i % 3 == 0);

      // add 2i + 1 passengers
      for (int j = 0; j < 2 * i + 1; ++j) {
        try { 
          pod.addPassenger(i + " " + j);
          passengerCount++;
        } catch (Exception e) {/* Ignore malfunctioning pods */}
      }
    }

    // launch 6 pods
    for (int i = 0; i < 6; ++i) {
      try {
        ls.launchPod();
      } catch (NoSuchElementException e) {
        return false;
      }
    }

    // perform the tests
    valid &= ls.getNumLaunched() == 6;
    valid &= ls.getNumPassengers() == passengerCount; 
    valid &= ls.getNumWaiting() == 4;

    return valid;
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
