package vis;

import processing.core.PApplet;
import processing.core.PConstants;
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
   
   Timer activeUsertimer;
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
       //REVEALIT: MAP values that come in EURO (from users) to values in PIXELS (for the vis)
       //SCSD: MAP values that come in YES/NO (1/2) (from users) to values in PIXELS (for the vis)
       float sum 	= gas+electro; 
       //rad 			= PApplet.map(sum, 0, Assets.visMaxUserInputValue, Assets.visMinRadius, Assets.visMaxRadius);
       
       //SCSD: value starts at 1 //TODO think of a more sophisticated way 
       rad 			= PApplet.map(sum, 1, Assets.visMaxUserInputValue, Assets.visMinRadius, Assets.visMaxRadius);
           
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
       //p2 = new Particle(polarControlPt.x * rad * 1.2f ,polarControlPt.y * rad * 1.2f); 
       p2 = new Particle(polarControlPt.x * rad * 0.5f ,polarControlPt.y * rad * 0.5f); 
       p1.lock();
       
       // Make a spring connecting both Particles
       spring = new VerletSpring2D(p1,p2,rad,0.0005f);
       //spring.setRestLength(0.2f);

      // Anything we make, we have to add into the physics world
      _worldPhysics.addParticle(p1);
      _worldPhysics.addParticle(p2);
      _worldPhysics.addSpring(spring);
      
      //TODO remember the logic of this if/else block (13.08.2013)
      // set a quicker timer for white-to-color change for energy piece if we are just launching the application (usually this is the researcher, no user interaction is involved) 
      
//      if (MainPApplet.getInstance().frameCount < 60){
//    	  timer = new Timer(1000);
//      } else {
//    	  timer = new Timer(Assets.visActiveUserSecs * 1000);
//      }
      activeUsertimer = new Timer(Assets.visActiveUserSecs * 1000);
      activeUsertimer.start();
     

    } 
   
   // arc begin and end controlled from outside because can change
   // radius can be controlled from inside and animated 
   void display(PGraphics pg, float _arcBegin, float _arcEnd){
	 // if (true) return;
	  
	  pg.pushMatrix();
      float d = PApplet.dist(p1.x, p1.y,p2.x, p2.y);
      
      if (id == Assets.cityId){
	      pg.noFill();
	      pg.stroke(255);
	      pg.strokeWeight(Assets.visDistrictArcThickness);
	      pg.arc(0,0,d,d,_arcBegin,_arcEnd);
      } else {
    	  //------------------------------------------------set normal color (corresponding to the category / district 
    	  if (activeUsertimer.isFinished()){
    		  // draw the piece a bit transparent (as in reveal-it original) ?  
    		  pg.fill(colorNormal, Assets.visBurstPieceAlpha);
    	  //------------------------------------------------set active color (during initial animation) 
    	  //FIXME: too long 
    	  } else {
    		  // draw the piece white while still active 
    		  pg.fill(255); 
    	  }
 
    	  pg.arc(0,0,d,d,_arcBegin,_arcEnd);

    	// Display the particles that drive the springing effect 
    	if (Assets.debugOn){
          p1.display(pg);
          p2.display(pg);
        }
        if (!activeUsertimer.isFinished() && (MainPApplet.getInstance().frameCount % 20 * 30 == 0)){
        //  updateSpring(1.05);  
        }
      }
      
      //--------------draw a line to mark the beginning of the pie piece  
      if (Assets.visPieceLineDraw){
	      pg.pushMatrix();
	      		if (Assets.visPieceLineSmooth)
	      			pg.smooth();
	      		
	      		pg.strokeWeight(Assets.visPieceLineStrokeWidth); 
	      		//pg.colorMode(PConstants.HSB, 360, 100, 100);
		        //pg.stroke(MainPApplet.getInstance().color(Assets.visPieceLineColor, 100, 100));
	      		
	      		pg.stroke(Assets.visPieceLineColor);
		        pg.rotate(_arcBegin);
		        pg.line(0, 0, d * Assets.visPieceLineScale, 0);
		        
		        pg.strokeWeight(1.0f); 
		        //pg.colorMode(PConstants.ARGB);
		        pg.noStroke();

	      		if (Assets.visPieceLineSmooth)
	      			pg.noSmooth();
	      pg.popMatrix();
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


