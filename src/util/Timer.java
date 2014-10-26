package util;
// Learning Processing
// Daniel Shiffman
// http://www.learningprocessing.com

// Example 10-5: Object-oriented timer

public class Timer {
 
  long savedTime; // When Timer started
  long totalTime; // How long Timer should last
  
  public Timer(int tempTotalTime) {
    totalTime = tempTotalTime;
  }
  
  // Starting the timer
  public void start() {
    // When the timer starts it stores the current time in milliseconds.
    savedTime = System.currentTimeMillis();
  }
  
  public void reset() {
      savedTime = System.currentTimeMillis();
  }
      
  // The function isFinished() returns true if 5,000 ms have passed. 
  // The work of the timer is farmed out to this method.
  public boolean isFinished() { 
    // Check how much time has passed
    float passedTime = System.currentTimeMillis() - savedTime;
    if (passedTime > totalTime) {
      return true;
    } else {
      return false;
    }
  }
}
