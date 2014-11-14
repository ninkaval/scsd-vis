package vis;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import scsd.MainPApplet;
import toxi.physics2d.VerletSpring2D;
import util.Assets;

//-----------------------------------------------------------------------------
public class SunburstCenter{
  
//  PFont fontCircle; 
  int col; 
  String content; 
  
  String line1; 
  String line2;  
  String line3;
  
  int bboxw;
  int bboxh; 
  
  boolean activeUser; 		//-------------------------------------------------center content is user info
  boolean activeDistrict; 	//-------------------------------------------------center content is district info
  boolean activeNum;  		//-------------------------------------------------center content is number of participants
  boolean activaTotalCost; 
  
  //Springing Bars for Participation (which is a nonanimated value) 
  Particle p1c1;
  Particle p2c1;
  VerletSpring2D springC1; 
  
  Particle p1c2;
  Particle p2c2;
  VerletSpring2D springC2; 
  
  
  int fontSize = 24;
  
  public SunburstCenter(){
	  col = 255;
   
	  bboxw = 100; 
	  bboxh = 100;
  
	  line1 = "";
	  line2 = "DEBUG";
	  line3 = "";
	    
	  //-----------------------------------------------------------------------
	  p1c1 = new Particle(0,0);
	  p2c1 = new Particle(fontSize,0);  
	  p1c1.lock();
	  springC1 = new VerletSpring2D(p1c1,p2c1,PApplet.dist(p1c1.x,p1c1.y,p2c1.x,p2c1.y),0.01f);
    
	  //-----------------------------------------------------------------------
	  p1c2 = new Particle(0,0);
	  p2c2 = new Particle(Assets.visCenterRadius,0);  
	  p1c2.lock();
	  springC2 = new VerletSpring2D(p1c2, p2c2, Assets.visCenterRadius, 0.005f);
	  
	  MainPApplet.mainPhysicsEngine.addParticle(p1c1);
	  MainPApplet.mainPhysicsEngine.addParticle(p2c1);
	  MainPApplet.mainPhysicsEngine.addSpring(springC1);
	  
	  MainPApplet.mainPhysicsEngine.addParticle(p1c2);
	  MainPApplet.mainPhysicsEngine.addParticle(p2c2);
	  MainPApplet.mainPhysicsEngine.addSpring(springC2);
	  
  }
 
  
  //TODO: make animation showing name and than value according to timing 
  //should fade away after some time 
  // "District NAME spent VAL" 
  void setContentDistrict(String _name, float _val){
    content = _name;
    line3 = ""+_val;
    updateSprings();
  }
  
   
  //TODO: make animation showing name and than value according to timing 
  //should fade away after some time 
  // "NAME spent VAL" 
  void setContent_USER_CONSUMPTION(String _name, float _val){
    
    line1 = "CONSUME"; 
    line2 = (int)_val + " kWh"; 
    line3 = "";
    
    updateSprings();
  }
  
  void setContent_USERMONEY(String _name, float _val){
    
    line1 = _name; 
    line2 = "GASTA"; 
    line3 = (int)_val + " " + Assets.labelCurrencySymbol;
    
    updateSprings();
  }
  
  void setContent_USER1(String _name, float _val){
    
    line1 = _name; 
    line2 = "ESTA PARTICIPANDO"; 
    line3 = "AHORA";
    updateSprings();
  }
  
  public void setContent_VISITORS(){
    //line2 = "VISITANTES"; 
    //line3 = "PARTICIPAN!";
	  line3 = "PARTICIPANTS";
	  line2 = ""+MainPApplet.mainSunburstVisualization.numUsers; 
    updateSprings();  
  }
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_DISTRICTS(){
    int num = MainPApplet.mainSunburstVisualization.getNumParticipatingDistricts(); 
    if (num > 0){
       line1 = ""+num;
       line2 = "DISTRICTS"; 
       line3 = "PARTICIPATE";
    }
    updateSprings(); 
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_1(){
    line2 = "PARTICIPATE!"; 
    line1 = line3 = "";
    updateSprings(); 
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_3(){
    line1 = "SHARE"; 
    line2 = "YOUR";
    line3 = "SENTIMENT!";
    updateSprings(); 
  }
  
  
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_2(){
    line1 = "COMPARA\nTU CONSUMO!"; 
    line2 = line3 = "";
    updateSprings(); 
  }
    
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_4(){
    line1 = ""; 
    line2 = "COME CLOSER!";
    line3 = "";
    updateSprings(); 
  }
  
  void setContent(String txt){
    line2 = "txt";
    updateSprings();
  }
  
  void updateSprings(){
    p2c1.lock();
    p2c1.x += 10;
    p2c1.unlock();
    
    p2c2.lock();
    p2c2.x += 10;
    p2c2.unlock();
  }
  
  public void update(){
  }
  
  public void display(PGraphics pg){
	  //updateSprings();
	  
	  pg.pushMatrix();
      
    	//	pg.translate(pg.width/2 + Assets.visZeroShiftX, pg.height/2 + Assets.visZeroShiftY);
 			//pg.scale(scaleFactor);
    
    // draw black circle background 
		float tmpRad = PApplet.dist(p1c2.x,p1c2.y,p2c2.x,p2c2.y); 
		pg.fill(0);
		pg.noStroke();
		pg.ellipse(0, 0, tmpRad, tmpRad);
 			
 	// draw circle text content 
      pg.textAlign(PConstants.CENTER);
      pg.textFont(Assets.visFontCenter, PApplet.dist(p1c1.x,p1c1.y,p2c1.x,p2c1.y));
      
      //--------Draw string along circle-----
      String message = line3; 
      tmpRad = (0.5f * tmpRad) * 0.6f; // draw a bit inside of circle
      // calc overall arclength of word 
      float wordArcLength = 0;
      for (int i = 0; i < message.length(); i++ ) {
        char currentChar = message.charAt(i);
        float w = pg.textWidth(currentChar); 
        wordArcLength += w;
      }
      float halfWordAngle = (wordArcLength * 0.5f) / tmpRad;
      float wordRotAngle = 1.5f * PConstants.PI - halfWordAngle;
      
      // We must keep track of our position along the curve
      float arclength = 0;
      
      // For every "char box" 
      for (int i = 0; i < message.length(); i ++ ) {
        
        // The character and its width
        char currentChar = message.charAt(i);
        // Instead of a constant width, we check the width of each character.
        float w = pg.textWidth(currentChar); 
        // Each box is centered so we move half the width
        arclength += w/2;
        
        // Angle in radians is the arclength divided by the radius
        // Starting always so the word is centerd along the vertical axis through the mid. of the circle
        float theta = wordRotAngle + arclength / tmpRad;
        
        pg.pushMatrix();
        
        // Polar to Cartesian conversion allows us to find the point along the curve. See Chapter 13 for a review of this concept.
        pg.translate(tmpRad * PApplet.cos(theta), tmpRad* PApplet.sin(theta)); 
        // Rotate the box (rotation is offset by 90 degrees)
        pg.rotate(theta + PConstants.PI/2); 
        
        // Display the character
        pg.fill(255);
        pg.text(currentChar,0,0);
        
        pg.popMatrix();
        
        // Move halfway again
        arclength += w/2;
      }
      
      
      float txtHeight = pg.textAscent() + pg.textDescent();
      
      // Draw line1 content (white) 
//      pg.fill(255);
//      pg.text(line1, 0f, - 0.2f * bboxh);

      // Draw line2 and 3 content (white) 
      pg.fill(255,255,0);
      pg.text(line2, 0f, - 0.2f * bboxh + txtHeight);  
//      pg.text(line3, 0f, - 0.2f * bboxh + 2f * txtHeight);
      
      

      
    pg.popMatrix();
  }

  
  
}
