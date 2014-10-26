package facade;

import processing.core.PApplet;
import scsd.MainPApplet;
import util.Assets;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.Slider;

public class Dimensions {
	
	
	////////////////////////////////////////////////////////////////////////////////
	// Pseudo Singleton
	////////////////////////////////////////////////////////////////////////////////
	//public ControlP5 guiInstance 	= null;
	private static ControlP5 guiInstance;
	
	public static ControlP5 getGUIInstance() {
		return guiInstance;
	}
	
	


	ControlWindow guiWindow = null; 
	public Points points 	= null; 
	
	//////////////////////////////////////////////////////////////////////////////////////
	// Singleton 
	//////////////////////////////////////////////////////////////////////////////////////
	public static Dimensions getInstance() {
		return singleton;
	}
	
	private static Dimensions singleton = new Dimensions();
	
	private Dimensions() {
		if (guiInstance == null && Assets.drawGUI) //only once
			guiInstance = createGUI(MainPApplet.getInstance()); 
		
		if (points == null)
			points = Points.getInstance(); 
		
		update();
		points.update(this);
	}

	public Points getPoints(){
		return points; 
	}
	//////////////////////////////////////////////////////////////////////////////////////
	// Dimensions: TODO: Load from a config file 
	//////////////////////////////////////////////////////////////////////////////////////
	public float baseLeft 	= 62f;
	public float baseRight 	= 59f;
	public float baseFront 	= 93f;

	public float topLeft 		= 10f;
	public float topRight 		= 8f;
	public float topFront 		= 52f;
	
	public float totalHeight 	= 168f;
	
	public float totalWidth 	= 214f;

	float invisibleBaseLeft 	= 0;
	float invisibleBaseRight 	= 0;
	
//	public void drawGUI(){
//		if (guiInstance != null)
//			guiInstance.draw();
//		
//	//	System.out.println(baseLeft);
//	}
	
	public void update() {
		totalWidth 			= baseLeft + baseFront + baseRight;
		
		float diff   		= baseFront - topFront; 
		float diffHalfInt   = (int)(diff/2f);
		float baseFrontDev 	= diff - diffHalfInt * 2; 
		invisibleBaseLeft   = (baseLeft - topLeft) + diffHalfInt; 		
		invisibleBaseRight  = (baseRight - topRight) + diffHalfInt + baseFrontDev;

		System.out.println();
		System.out.println("Dimensions.update():");
		System.out.println("totalWidth " + totalWidth);
//		System.out.println("diff " + diff);
//		System.out.println("diffHalfInt " + diffHalfInt);
//		System.out.println("baseFrontDev " + baseFrontDev);
		System.out.println("invisibleBaseRight " + invisibleBaseRight);
		System.out.println("invisibleBaseLeft " + invisibleBaseLeft);
		System.out.println();
	}
	
	
	ControlP5 createGUI(PApplet p){
		
		ControlP5	guiInstance = new ControlP5(p);
		guiInstance.setAutoDraw(false);
		    
		int w1 = 200, w2 = 250, w3 = 350, h = 12;
		int dx = 0;
		int dy = 15;
		int x = 10, y = 10;
		
		//x+=dx, y+=dy, w, h);
		Slider slider1 = guiInstance.addSlider("base-left")
		     .setRange(0, 100)
		     .setPosition(x+=dx, y+=dy)
		     .setSize(w2, h)
		     .setValue(62)
			 .setId(1);
		
		Slider slider2 = guiInstance.addSlider("base-right")
			     .setRange(0, 100)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w2, h)
			     .setValue(59)
				 .setId(2);	
		
		Slider slider3 = guiInstance.addSlider("base-front")
			     .setRange(0, 100)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w2, h)
			     .setValue(93)
				 .setId(3);	
		
		Slider slider4 = guiInstance.addSlider("top-left")
			     .setRange(0, 100)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w1, h)
			     .setValue(10)
				 .setId(4);
			
		Slider slider5 = guiInstance.addSlider("top-right")
			     .setRange(0, 100)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w1, h)
			     .setValue(8)
				 .setId(5);	
			
		Slider slider6 = guiInstance.addSlider("top-front")
			     .setRange(0, 100)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w1, h)
			     .setValue(52)
				 .setId(6);		
		
		Slider slider7 = guiInstance.addSlider("total-height")
			     .setRange(150, 200)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w3, h)
			     .setValue(168)
				 .setId(7);	
		
		Slider slider8 = guiInstance.addSlider("mask-offsetX")
			     .setRange(0, 1024)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w3, h)
			     .setValue(Assets.maskOffsetX)
				 .setId(8);
		
		Slider slider9 = guiInstance.addSlider("mask-offsetY")
			     .setRange(0, 768)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w3, h)
			     .setValue(Assets.maskOffsetY)
				 .setId(9);
		
		Slider slider10 = guiInstance.addSlider("mask-width")
			     .setRange(0, 250)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w3, h)
			     .setValue(Assets.maskWidth)
				 .setId(10);
		
		Slider slider11 = guiInstance.addSlider("mask-height")
			     .setRange(0, 200)
			     .setPosition(x+=dx, y+=dy)
			     .setSize(w3, h)
			     .setValue(Assets.maskHeight)
				 .setId(11);
		
		guiWindow = guiInstance.addControlWindow("controlP5window", 0, 768-300+50, 1024, 300)
		    	    .hideCoordinates()
		    	    .setBackground(0)
		    	    ;
		    
		slider1.moveTo(guiWindow);		
		slider2.moveTo(guiWindow);		
		slider3.moveTo(guiWindow);		
		slider4.moveTo(guiWindow);		
		slider5.moveTo(guiWindow);		
		slider6.moveTo(guiWindow);
		slider7.moveTo(guiWindow);
		slider8.moveTo(guiWindow);
		slider9.moveTo(guiWindow);
		slider10.moveTo(guiWindow);
		slider11.moveTo(guiWindow);
		
		return guiInstance; 
	}
	
	public void controlEvent(ControlEvent theEvent) {
		  if (theEvent.isFrom(guiInstance.getController("base-left"))) {
			  baseLeft = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("base-right"))) {
			  baseRight = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("base-front"))) {
			  baseFront = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("top-left"))) {
			  topLeft = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("top-right"))) {
			  topRight = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("top-front"))) {
			  topFront = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("total-height"))) {
			  totalHeight = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("mask-offsetX"))) {
			  Assets.maskOffsetX = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("mask-offsetY"))) {
			  Assets.maskOffsetY = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("mask-width"))) {
			  Assets.maskWidth = (int)(theEvent.getController().getValue());
		  } else if (theEvent.isFrom(guiInstance.getController("mask-height"))) {
			  Assets.maskHeight = (int)(theEvent.getController().getValue());
		  } 
		  
		  update();
		  points.update(this);

//		  
//		  switch(theEvent.getController().getId()) {
//		    case(1):
//		    	
//		    	System.out.println((int)(theEvent.getController().getValue()));
//		    break;
//		    case(2):
//		    	System.out.println((int)(theEvent.getController().getValue()));
//		    break;
//		    case(3):
//		    	System.out.println((int)(theEvent.getController().getValue()));
//		    break;
//		  }
		}
	
	

	
}
