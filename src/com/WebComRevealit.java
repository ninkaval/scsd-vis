package com;

import processing.core.PApplet;
import scsd.MainPApplet;

public class WebComRevealit extends Thread {
  public boolean running;    // Is the thread running?  Yes or no?
  boolean available;  // Are there new tweets available?
  
  int query_status;
  
  //String urlPrefix = "http://localhost/~jp/revealit/get_user_update.php?last=";
  //String urlPrefix = "http://www.jpcarrascal.com/revealit/test/get_user_update.php?last=";
  
  //String host = "localhost";
  String host = "193.145.50.221/revealit";
  
  String urlPrefix = "http://" + host + "/get_user_update.php?last=";
  //String urlPrefix = "http://localhost/~jp/revealit/get_user_update.php?last=";
   
  public WebComRevealit(){ 
  }
  
  public boolean available() {
    return available;
  }
  
  @Override
public void start(){
	  running = true;
	  super.start();  
  }
  
  @Override
public void run(){
	  while (running){
		  try{
			  if(MainPApplet.g_resultsPublished){
		    	  MainPApplet.g_lines = MainPApplet.getInstance().loadStrings(urlPrefix + MainPApplet.g_lastTimestamp);
		    	  String[] stringtemp = PApplet.split(MainPApplet.g_lines[0], ':');
		    	  query_status = Integer.parseInt(stringtemp[1]);

		    	  if (query_status > 0){
					
		    		  stringtemp = PApplet.split(MainPApplet.g_lines[1], ':');
		    		  MainPApplet.g_num_new_participants = Integer.parseInt(stringtemp[1]);

		    		  if(MainPApplet.g_num_new_participants > 0) {
		    			  MainPApplet.g_newResults = true;
		    			  stringtemp = PApplet.split(MainPApplet.g_lines[2], ':');
		    			  MainPApplet.g_lastTimestamp = Integer.parseInt(stringtemp[1]);
		    			  MainPApplet.g_resultsPublished = false;
		    		  }
		    	  }
		      }
		  }catch (Exception e) {
		      System.out.println("Communication error");
		      e.printStackTrace();
		      return;
		  }
	  }
  }
  
  void quit() {
    System.out.println("Quitting."); 
    running = false;  // Setting running to false ends the loop in run()
    // In case the thread is waiting. . .
    interrupt();
  }
  

}
