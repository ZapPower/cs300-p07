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
 * This class models a LoopStation, which manages three tracks:
 * waitingFirst - newly created first-class Pods are added here, ordered from most recently-created
 * to least recently-created
 * waitingEconomy - newly created economy-class Pods are added here, ordered from least
 * recently-created to most recently-created
 * launched - both first- and economy-class Pods which have been launched into the Hyperloop
 * Launching a Pod is done in order of creation, with first-class Pods launching before
 * economy-class Pods.
 */
public class LoopStation {
  /**
   * A track containing all launched Pods.
   */
  protected Track launched;

  /**
   * A track containing all newly-created, unlaunched economy-class Pods.
   */
  protected Track waitingEconomy;

  /**
   * A track containing all newly-created, unlaunched first-class Pods.
   */
  protected Track waitingFirst;

  /**
   * Creates a new LoopStation with empty tracks
   */
  public LoopStation() {
    this.launched = new Track();
    this.waitingEconomy = new Track();
    this.waitingFirst = new Track();
  }

  /**
   * Creates a new Pod of the appropriate class and loads it onto the correct waiting track.
   * This method also returns a reference to this newly-created Pod so that passengers may board
   * 
   * @param capacity the capacity of the new Pod to create
   * @param isFirstClass whether the new Pod is a first class Pod
   * @return a reference to the newly-created Pod
   */
  public Pod createPod(int capacity, boolean isFirstClass) {
    int podClass = isFirstClass ? Pod.FIRST : Pod.ECONOMY;
    Pod newPod = new Pod(capacity, podClass);

    if (isFirstClass) {
      this.waitingFirst.add(newPod);
    } else {
      this.waitingEconomy.add(newPod);
    }

    return newPod;
  }

  /**
   * Launches the highest-priority, least-recently-created Pod that is currently waiting:
   *  - FIRST class Pods are launched first
   *  - Then ECONOMY class Pods are launched
   * Remember that new FIRST class Pods are added to the beginning of the FIRST class, while new
   * ECONOMY class Pods are added to the end.
   * The least-recently created Pod of the correct class should always be launched first.
   * If a Pod to be launched is nonfunctional, it should still be removed from its waiting Track.
   *
   * @throws NoSuchElementException if no Pods are waiting to launch
   */
  public void launchPod() {
    if (!this.waitingFirst.isEmpty()) {
      // add least-recent pod which is at the end
      this.launched.add(this.waitingFirst.remove(this.waitingFirst.size() - 1));
      return;
    } else if (!this.waitingEconomy.isEmpty()) {
      // add least recent pod which is at the start
      this.launched.add(this.waitingEconomy.remove(0));
      return;
    }

    throw new NoSuchElementException("No Pods Waiting for Launch");
  }

  /**
   * Finds and removes any malfunctioning Pods from the launched track
   * 
   * @return the total number of pods which were removed
   */
  public int clearMalfunctioning() {
    int numRemoved = 0;
    int currIdx = this.launched.findFirstNonFunctional();

    while (currIdx != -1) {
      // remove found malfunctioning pod, and count it
      this.launched.remove(currIdx);
      numRemoved++;

      // get next malfuctioning pod
      currIdx = this.launched.findFirstNonFunctional();
    }

    return numRemoved;
  }

  /**
   * Reports the total number of first and economy class Pods which are waiting to be launched
   * 
   * @return the total number of unlaunched pods at this LoopStation
   */
  public int getNumWaiting() {
    return this.waitingEconomy.size() + this.waitingFirst.size();
  }

  /**
   * Reports the total number of Pods, functional or non-functional, which are currently running
   * on the launched track
   * 
   * @return the total number of Pods on the launched track
   */
  public int getNumLaunched() {
    return this.launched.size();
  }

  /**
   * Reports the total number of passengers in functional Pods across all tracks, waiting and
   * launched
   * 
   * @return the total number of passengers in functional Pods currently being served by this
   * LoopStation
   */
  public int getNumPassengers() {
    int numPassengers = 0;

    numPassengers += countNumPassengers(this.waitingEconomy);
    numPassengers += countNumPassengers(this.waitingFirst);
    numPassengers += countNumPassengers(this.launched);

    return numPassengers;
  }

  /**
   * Private helper method for counting the number of passengers on a track
   * 
   * @param t Track to count
   * @return Number of passengers on given track
   */
  private int countNumPassengers(Track t) {
    LinkedNode curr = t.head;
    int numPassengers = 0;

    while (curr != null) {
      try {
        numPassengers += curr.getPod().getNumPassengers();
      } catch (MalfunctioningPodException e) {/* Skip counting in  malfuctioning pods */}

      curr = curr.getNext();
    }

    return numPassengers;
  }
}
