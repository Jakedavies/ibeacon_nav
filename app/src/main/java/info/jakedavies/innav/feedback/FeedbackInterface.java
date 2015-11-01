package info.jakedavies.innav.feedback;

/**
 * Created by jakedavies on 15-11-01.
 */
public interface FeedbackInterface {

    /**
     * instructs the user to turn left
     */
    void turnLeft();

    /**
     * instructs the user to turn right
     */
    void turnRight();

    /**
     * instructs the user to move forward
     * @param x the number of m
     */
    void straight(int x);
}
