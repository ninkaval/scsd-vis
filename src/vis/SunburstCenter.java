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
  Particle p1c;
  Particle p2c;
  VerletSpring2D springC; 
  
  int fontSize = 40;
  
  public SunburstCenter(){
	  col = 255;
   
	  bboxw = 100; 
	  bboxh = 100;
  
	  line1 = "";
	  line2 = "DEBUG";
	  line3 = "";
	    
	  //-----------------------------------------------------------------------
	  p1c = new Particle(0,0);
	  p2c = new Particle(fontSize,0); // just a bit more so that we can start calculating 
	  p1c.lock();
	  springC=new VerletSpring2D(p1c,p2c,PApplet.dist(p1c.x,p1c.y,p2c.x,p2c.y),0.01f);
    
	  MainPApplet.mainPhysicsEngine.addParticle(p1c);
	  MainPApplet.mainPhysicsEngine.addParticle(p2c);
	  MainPApplet.mainPhysicsEngine.addSpring(springC);
 }
 
  
  //TODO: make animation showing name and than value according to timing 
  //should fade away after some time 
  // "District NAME spent VAL" 
  void setContentDistrict(String _name, float _val){
    content = _name;
    line3 = ""+_val;
    springText();
  }
  
   
  //TODO: make animation showing name and than value according to timing 
  //should fade away after some time 
  // "NAME spent VAL" 
  void setContent_USER_CONSUMPTION(String _name, float _val){
    
    line1 = "CONSUME"; 
    line2 = (int)_val + " kWh"; 
    line3 = "";
    
    springText();
  }
  
  void setContent_USERMONEY(String _name, float _val){
    
    line1 = _name; 
    line2 = "GASTA"; 
    line3 = (int)_val + " " + Assets.labelCurrencySymbol;
    
    springText();
  }
  
  void setContent_USER1(String _name, float _val){
    
    line1 = _name; 
    line2 = "ESTA PARTICIPANDO"; 
    line3 = "AHORA";
    springText();
  }
  
  public void setContent_VISITORS(){
    line2 = "VISITANTES"; 
    line3 = "PARTICIPAN!";
    line1 = ""+MainPApplet.mainSunburstVisualization.numUsers; 
    
  }
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_DISTRICTS(){
    int num = MainPApplet.mainSunburstVisualization.getNumParticipatingDistricts(); 
    if (num > 0){
       line1 = ""+num;
       line2 = "BARRIOS"; 
       line3 = "PARTICIPAN";
    }
    springText(); 
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_1(){
    line2 = "PARTICIPA!"; 
    line1 = line3 = "";
    springText(); 
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_3(){
    line1 = "COMPARTE"; 
    line2 = "TU GASTO";
    line3 = "ELÉCTRICO!";
    springText(); 
  }
  
  
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_2(){
    line1 = "COMPARA\nTU CONSUMO!"; 
    line2 = line3 = "";
    springText(); 
  }
    
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public void setContent_Invitation_4(){
    line1 = ""; 
    line2 = "ACÉRCATE!";
    line3 = "";
    springText(); 
  }
  
  void setContent(String txt){
    line2 = "txt";
    springText();
  }
  
  void springText(){
    p2c.lock();
    p2c.x += 10;
    p2c.unlock();
  }
  
  public void update(){
  }
  
  void display(PGraphics pg){
    pg.pushMatrix();
      
      pg.textAlign(PConstants.CENTER);
      pg.textFont(Assets.visFontCenter, PApplet.dist(p1c.x,p1c.y,p2c.x,p2c.y));
      
      float txtHeight = pg.textAscent() + pg.textDescent();
      
      pg.fill(255);
      pg.text(line1, 0f, - 0.2f * bboxh);

      pg.fill(255,255,0);

      pg.text(line2, 0f, - 0.2f * bboxh + txtHeight);  
      pg.text(line3, 0f, - 0.2f * bboxh + 2f * txtHeight);
      
    pg.popMatrix();
  }

}
