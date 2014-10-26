package util;

import processing.core.PApplet;
import scsd.MainPApplet;

//-----------------------------------------------------------------------------


public class EventScheduler{
  
  int intervalMexicanWave;
  int intervalHLparticipant;
  int intervalHLdistrict;
  int intervalSpringCircle;
  int intervalScaleCircle;
  int intervalRotateCircle;
  
  int intervalShowDistrictParticipation; 
  int intervalShowVisitorsParticipation; 
  
  int intervalDoInvitation1; 
  int intervalDoInvitation2; 
  
  int temp;
  int numUsers;
  int oldNumUsers;
  PApplet applet; 
  
  Timer	inactiveVisTimer 	= null;

  public EventScheduler(PApplet _applet) {
	  
	applet 				= _applet; 
	inactiveVisTimer 	= new Timer(30 * 1000);		
	
    //--------------------------------------------------------Update intervals in frames. I kept the 30x multiplier to understand the intervals in seconds
    intervalMexicanWave 				= 180 * 30;
    intervalHLparticipant 				= 120 * 30;
    intervalHLdistrict 					= 60 * 30;
    intervalSpringCircle 				= 10 * 30;
    intervalScaleCircle 				= 360 * 30;
    intervalRotateCircle 				= 60 * 30;
    intervalShowDistrictParticipation 	= 20 * 30; 
    intervalShowVisitorsParticipation 	= 90 * 30;
    intervalDoInvitation1 				= 60 * 30; 
    intervalDoInvitation2 				= 3600 * 30; 
    
    inactiveVisTimer.start();

  }

  public void update(){
    
    if (inactiveVisTimer.isFinished()){
        //println("g_inactiveVizTimer!!!");
        //g_vis.scaleCircle(5.0);
    }
    
    //if (numUsers - oldNumUsers >= 5) {// vis.scaleCircle(1.05); }
    
  //  if(frameCount % intervalMexicanWave == 0) MexicanWave();
  //  if(frameCount % intervalHLparticipant == 0) HighLightParticipant();
  //  if(frameCount % intervalHLdistrict == 0) HighLightDistrict();
  //  if(frameCount % intervalSpringCircle == 0) SpringCircle();
  //  if(frameCount % intervalScaleCircle == 0) ScaleCircle();
    if(applet.frameCount % intervalScaleCircle == 0)
    {
        //println("Animating...");
       // vis.scaleCircle(1.05);
    }
    if(applet.frameCount % intervalSpringCircle == 0)
    {
        //println("Animating...");
        MainPApplet.mainSunburstVisualization.springCircle();
    }
    if (applet.frameCount % intervalSpringCircle == 0) {
      //  if(frameCount % 30 == 0) vis.scaleSpringCircle(1.1);
      
      oldNumUsers = numUsers;
    }
    
    
    if (applet.frameCount % intervalShowDistrictParticipation == 0){
        MainPApplet.mainCenterVis.setContent_DISTRICTS();
    }
    
    if (applet.frameCount % intervalShowVisitorsParticipation == 0){
        MainPApplet.mainCenterVis.setContent_VISITORS();
    }
    
    if (applet.frameCount % intervalDoInvitation1 == 0){ 
      float ran = applet.random(1,10);
      if (ran%2 == 0) MainPApplet.mainCenterVis.setContent_Invitation_1(); 
      else MainPApplet.mainCenterVis.setContent_Invitation_3(); 

    }
    
    if (applet.frameCount % intervalDoInvitation2 == 0){ 
      float ran = applet.random(1,10);
      if (ran%2 == 0) MainPApplet.mainCenterVis.setContent_Invitation_2(); 
      else MainPApplet.mainCenterVis.setContent_Invitation_4(); 
    }
    
  }

  

}
