package vis;

import processing.core.PApplet;
import processing.core.PGraphics;
import scsd.MainPApplet;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import util.Assets;
import util.Timer;

class Particle extends VerletParticle2D{

	public Particle(float arg0, float arg1) {
		super(arg0, arg1);
	}
	public void display(PGraphics pg) {
	    //pg.fill(255,0,0);
		pg.noStroke();
	    pg.ellipse(x,y,5,5);
	}
}

//--------------------------------------------------------------------------------------------------------
/* Piece of energy for each participant
 */
class SunburstPiece{
   float rad;  // stays the same radius (since its only one-time participation)
   float angle;   // can change depending on num of particpants as a whole
   int colorActive;   // for now yellow, might change to district color keep it open
   int colorNormal;
   
   Particle p1;  // springs for animation 
   Particle p2;
   VerletSpring2D spring; 
   Vec2D polarControlPt; // spring dir 
   
   int id; // this is the district id and should actually serve as a mapping pt for the color!!!!
   String name;  
   float electro; 
   float gas; 
   float energy; 
   
   Timer timer;
   boolean bActive = true; 
   
   /*
   float maxExpenseMoney = .f;  
   float minRadius = .f; 
   float maxRadius = .f; 
   */
   //PApplet applet; 
   
   SunburstPiece(int _id, 
		   		String _name, 
		   		float _electro, 
		   		float _gas, 
		   		float _energy, 
		   		float _angle, 
		   		VerletPhysics2D _worldPhysics){
	   
	   id 		= _id; 
       name 	= _name; 
       gas 		= _gas; 
       electro 	= _electro; 
       
       //TODO: TRICKY, document it somewhere
       //MAP values that come in EURO (from users) to values in PIXELS (for the vis)
       float sum 	= gas+electro; 
       rad 			= PApplet.map(sum, 0, Assets.visMaxUserInputValue, Assets.visMinRadius, Assets.visMaxRadius);
       
       colorActive = 255;
       colorNormal = 100;
       
       if (_id == Assets.cityId) {
         colorActive = Assets.cityColor;
       }
       
       //rad = _radius;  
       angle = _angle; 
       
       polarControlPt = new Vec2D();
       polarControlPt.set(1,angle).toCartesian();
       
       p1 = new Particle(0,0);
       p2 = new Particle(polarControlPt.x * rad * 0.5f ,polarControlPt.y * rad * 0.5f); 
       p1.lock();
       
       // Make a spring connecting both Particles
       spring = new VerletSpring2D(p1,p2,rad,0.002f);

      // Anything we make, we have to add into the physics world
      _worldPhysics.addParticle(p1);
      _worldPhysics.addParticle(p2);
      _worldPhysics.addSpring(spring);
      
      //TODO remember the logic of this if/else block (13.08.2013)
      // set a quicker timer for white-to-color change for energy piece if we are just launching the application (usually this is the researcher, no user interaction is involved) 
      
      if (MainPApplet.getInstance().frameCount < 60){
    	  timer = new Timer(1000);
      } else {
    	  timer = new Timer(Assets.visActiveUserSecs * 1000);
      }
      timer.start();
     

    } 
   
   // arc begin and end controlled from outside because can change
   // radius can be controlled from inside and animated 
   void display(PGraphics pg, float _arcBegin, float _arcEnd){
	   
	  pg.pushMatrix();
      float d = PApplet.dist(p1.x, p1.y,p2.x, p2.y);
      
      if (id == Assets.cityId){
	      pg.noFill();
	      pg.stroke(255);
	      pg.strokeWeight(Assets.visArcThickness);
	      pg.arc(0,0,d,d,_arcBegin,_arcEnd);
      } else {
    	  //------------------------------------------------set normal color (corresponding to the category / district 
    	  if (timer.isFinished()){
    		  pg.fill(colorNormal);//-----------------------TODO: make it a bit transparent (as in reveal-it original) ?  
    	  //------------------------------------------------set active color (during initial animation) FIXME: too long 
    	  } else {
    		  pg.fill(colorActive); 
    	  }
    	pg.fill(colorNormal);  //test: set always color 
        //pg.noStroke();
        pg.strokeWeight(1.0f); //test: draw strokes of pieces FIXME: does not work 
        pg.stroke(0);
        pg.arc(0,0,d,d,_arcBegin,_arcEnd);
        
        /*
        pushMatrix();
          fill(color(red(colorNormal),green(colorNormal),blue(colorNormal),200));  
          rotate(_arcEnd * 0.5);
          textFont(g_fontLastUser, g_fontSize_2 * 0.8);    
          text("TestName" + frameCount % 10, d * 0.8, 0);  
        popMatrix();
        */

        //fill(255,255,0,0);
        if (true){
          // Display the particles
          p1.display(pg);
          p2.display(pg);
        }
        if (!timer.isFinished() && (MainPApplet.getInstance().frameCount % 20 * 30 == 0)){
        //  updateSpring(1.05);  
        }
      }
      pg.popMatrix();
      
   }
   
   // _userAngle is the portion equal for each participants (changes from outside if more participants come)
   // _lastBeginAngle is..
   // THIS IS NOT REALLY WORKING WELL YET !!!!!!!!!!!!!!!!!!! I AM NOT USING IT 
   void update(float _userAngle, float _lastBeginAngle){
     float polarAngle = _lastBeginAngle + _userAngle * 0.5f;
      polarControlPt.set(1,polarAngle).toCartesian();
      p2.x =  polarControlPt.x * rad;
      p2.y =  polarControlPt.y * rad;
    }
    
    void updateSpringMouse(){
      if (MainPApplet.getInstance().mousePressed) {
        p2.lock();   
        p2.x += 35;//mouseX;
        p2.y += 35;//mouseY;
        p2.unlock();
      }
    }
    
    void updateSpring(boolean lock){
      if (lock) {
        p2.lock();   
        p2.x += 20;//mouseX;
        p2.y += 20;//mouseY;
        p2.unlock();
      }
    }
    
    void updateSpring(float scaleFactor){
        p2.lock();   
        p2.x *= scaleFactor;//mouseX;
        p2.y *= scaleFactor;//mouseY;
        p2.unlock();
    }
    
    ///*
    void scaleSpring(float scaleFactor){
      spring.setRestLength(spring.getRestLength() * scaleFactor);
      p2.y += scaleFactor;
      p2.x += scaleFactor;
    }
    //*/
    
    //float getRad() { return rad; }
    float getRad() {
      float d = PApplet.dist(p1.x, p1.y,p2.x, p2.y); 
      return d;    
    }
    
    int getColActive(){return colorActive;}
    int getColNormal(){return colorNormal;}
    void setColorNormal(int _c) { colorNormal = _c; }
    
    void printme(){
      System.out.println("EnergyPiece: angle = " + angle + " rad = " + rad + " pt2 = " + p2.x + "," + p2.y +"\n");
      System.out.println("\tangle = " + angle + " rad = " + rad + " pt2 = " + p2.x + "," + p2.y +"\n");
    }
    

}


