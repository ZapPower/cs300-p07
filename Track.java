/**
 * This class models Track objects as a doubly-linked list for the CS300 Hyperloop project.
 */
public class Track implements ListADT<Pod> {
    
    /**
     * A reference to the node containing the first Pod on the track, may be null if the track is empty
     */
    protected LinkedNode head;

    /**
     * The number of Pods currently on the track
     */
    private int size;

    /**
     * A reference to the node containing the last Pod on the track, may be null if the track is empty
     */
    protected LinkedNode tail;

    /**
     * Adds a passenger to the first available seat in a Pod which matches their class designation.
     * 
     * @param name the name of the passenger to add
     * @param isFirstClass whether this passenger is first class
     * @return true if they were successfully added to an available seat of their corresponding class, false if there were no seats or Pods available for their class
     */
    public boolean addPassenger(String name, boolean isFirstClass) {
        LinkedNode curr = this.head;
        Pod currPod = null;
        while (curr != null) {
            currPod = curr.getPod();
            try {
                if (currPod.getPodClass() == Pod.FIRST && isFirstClass) {
                    if (!currPod.isFull()) {
                        this.size++;
                        currPod.addPassenger(name);
                        return true;
                    }
                } else {
                    if (!currPod.isFull()) {
                        this.size++;
                        currPod.addPassenger(name);
                        return true;
                    }
                }
            } catch (Exception e) {}
            
            curr = curr.getNext();
        }
        return false;
    }

    /**
     * Searches all Pods in the track to find the given passenger
     * 
     * @param name the name of the passenger to find
     * @return the index of the Pod this passenger was located in, or -1 if they were not found (or the Track is currently empty)
     */
    public int findPassenger(String name) {
        int currIdx = 0;
        LinkedNode curr = this.head;
        Pod currPod = null;

        while (curr != null) {
            currPod = curr.getPod();
            try {
                if (currPod.containsPassenger(name)) {
                    return currIdx;
                }
            } catch (Exception e) {}

            currIdx++;
            curr = curr.getNext();
        }

        return -1;
    }

    /**
     * Finds the index of the first non-functional Pod on the track. If all Pods are functioning, returns -1
     * 
     * @return the lowest index of a non-functional Pod on the track, or -1 if all Pods are currently functioning (or the Track is currently empty)
     */
    public int findFirstNonFunctional() {
        LinkedNode curr = this.head;
        Pod currPod = null;
        int idx = 0;

        while (curr != null) {
            currPod = curr.getPod();

            try {
                currPod.getPodClass();
            } catch (MalfunctioningPodException e) {
                return idx;
            }

            idx++;
            curr = curr.getNext();
        }

        return -1;
    }

    /**
     * Reports whether the track is currently empty
     * @return true if the track is currently empty, false otherwise
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Reports the current number of Pods currently on this track. This number includes both functional and non-functional Pods.
     * @return the number of Pods on this track
     */
    public int size() {
        return this.size;
    }

    /**
     * Removes ALL Pods from this track
     */
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Adds a Pod to the track in the correct location. FIRST class Pods should be added to the front of the list; ECONOMY class Pods should be added to the back of the list.
     * 
     * If the Pod is not functional, do NOT add it to the track, but also do NOT allow any exception to be thrown. Attempting to add a non-functional Pod should simply not cause the list to change.
     * 
     * @param newElement the Pod to add to this track
     */
    public void add(Pod newElement) {
        int podClass = 0;

        // test if pod is functional
        try {
            podClass = newElement.getPodClass();
        } catch(MalfunctioningPodException e) {return;}

        LinkedNode newNode = new LinkedNode(newElement);

        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;
        }

        if (podClass == Pod.ECONOMY) {
            this.tail.setNext(newNode);
            newNode.setPrev(this.tail);
            this.tail = newNode;
        } else {
            newNode.setNext(this.head);
            this.head.setPrev(newNode);
            this.head = newNode;
        }
    }

    /**
     * Accesses the Pod at a given index
     * 
     * @param index the index of the Pod to access
     * @return a reference to the Pod at a given index in the track
     * 
     * @throws IndexOutOfBoundsException if the given index is invalid
     */
    public Pod get(int index) {
        if (index >= this.size || index < 0) {
            throw new IndexOutOfBoundsException("Pod index out of bounds");
        }

        LinkedNode curr = this.head;
        int currIdx = 0;

        while (curr != null) {
            if (currIdx == index) {
                return curr.getPod();
            }
            currIdx++;
            curr = curr.getNext();
        }

        throw new IndexOutOfBoundsException("Pod index invalid");
    }

    /**
     * Determines whether a particular Pod is contained in the track
     * 
     * @param toFind the Pod to search for in the track
     * @return true if the Pod is contained in the track, false otherwise
     */
    public boolean contains(Pod toFind) {
        LinkedNode curr = this.head;

        while (curr != null) {
            if (curr.getPod().equals(toFind)) {
                return true;
            }
            curr = curr.getNext();
        }
        return false;
    }

    /**
     * Removes a Pod at a given index from the track
     * 
     * @param index the index of the Pod to remove
     * @return a reference to the Pod removed from the track
     * @throws IndexOutOfBoundsException if the given index is invalid
     */
    public Pod remove(int index) {
        if (index >= this.size || index < 0) {
            throw new IndexOutOfBoundsException("Pod index out of bounds");
        }

        LinkedNode curr = this.head;
        int currIdx = 0;

        while (curr != null) {
            if (currIdx == index) {
                if (this.size == 1) {
                    Pod p = this.head.getPod();
                    this.head = null;
                    this.tail = null;
                    this.size = 0;
                    return p;
                }

                Pod p = curr.getPod();
                LinkedNode prev = curr.getPrev();
                LinkedNode next = curr.getNext();
                prev.setNext(next);
                next.setPrev(prev);
                return p;
            }
            currIdx++;
            curr = curr.getNext();
        }

        throw new IndexOutOfBoundsException("Pod index invalid");
    }

    /**
     * Returns a String representation of the entire contents of the track (OUTPUT NOT GRADED). This method traverses the entire track and includes a String representation of each Pod, which you may wish to use for testing purposes.
     * 
     * @return a String representation of all Pods currently on the track
     */
    @Override
    public String toString() {
        LinkedNode curr = this.head;
        StringBuilder sb = new StringBuilder();
        while (curr != null) {
            sb.append(curr.getPod().toString());
            curr = curr.getNext();
        }
        return sb.toString();
    }
}
