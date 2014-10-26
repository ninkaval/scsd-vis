package scsd;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import toxi.physics2d.VerletPhysics2D;
import util.Assets;
import util.EventScheduler;
import vis.EnergyUserData;
import vis.SunburstCenter;
import vis.SunburstVis;

import com.DashboardListener;
import com.DatabaseParticipant;
import com.VisDatabaseCom;

import controlP5.ControlEvent;
import facade.Dimensions;
import facade.Geometry;
import facade.Points;



public class MainPApplet extends PApplet implements DashboardListener{


	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////
	// Pseudo Singleton
	////////////////////////////////////////////////////////////////////////////////
	private static MainPApplet instance;
	
	public static MainPApplet getInstance() {
		return instance;
	}
	
	public MainPApplet() {
		super();
		instance = this;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Utility Objects 
	////////////////////////////////////////////////////////////////////////////////
	//WebCom 	mainCom; 
	EventScheduler scheduler;
	
	
	//////////////////////////////////////////////////////////////////////
	// Facade Mask Objects 
	//////////////////////////////////////////////////////////////////////
	Dimensions 	facadeDims;
	Points 		facadePts;
	Geometry 	facadeGeom; 
	PGraphics   pgTextureUnfold1 		= null; 
	PGraphics   pgTextureUnfold2 		= null; 
	PGraphics   pgFacadeUnfold			= null;
	PGraphics   pgTextureUnfoldRotated  = null;
	float 		pgScaleFact 			= 1.5f; 
	PImage 		testImage 				= null; 

	
	//-------------------------------------------------------------------
	boolean comOn 				= true    ;
	boolean comActive 			= false;
	
	//-------------------------------------------------------------------	
	boolean mainDrawInfo 			= false;
	boolean mainDrawTitle 			= false; 	
	boolean mainDrawCityEnergy 		= false;
	
	boolean mainDrawLegend 			= false; 
	boolean mainDrawCategory		= true; 
	boolean mainDrawVisFullscreen 	= false; 
	boolean mainDrawFacadeMask	  	= false; 
	boolean mainDrawAuxLines		= true; 
	boolean mainDrawSmooth 			= false; 
	//-------------------------------------------------------------------
	boolean moveLegend 			= false;
	float   scaleScreenFactor 	= 0.8f;
	boolean scaleScreen     	= false;
	boolean rotateScreen    = false; 
	boolean moveInfo       = false;
	boolean moveTitle      = false; 
	
	float rot = -HALF_PI; 
	
	
	
	//--------------------------------------------------------------------
	// Debugging
	boolean over 		= false;
	boolean move 		= false;  // If mouse down and over
	boolean recordPdf   = false;
	boolean debugScreen = false;
	int   	debugCol  = color(0,255,0);
	
	//--------------------------------------------------------------------
	
	//////////////////////////////////////////////////////////////////////
	// global variables 
	//////////////////////////////////////////////////////////////////////
	public static VerletPhysics2D mainPhysicsEngine;

	public static SunburstVis 	mainSunburstVisualization; 
	public static SunburstCenter 	mainCenterVis; 
	public static EnergyUserData 	mainUserData; 
	
	public static boolean g_bFirstLaunch = true; 

	//--------------------------------------------------------------------Database Com 
	VisDatabaseCom 			dbCom; 

	public static boolean 	g_newResults;
	public static boolean 	g_resultsPublished;
	public static String  	g_lines[];
	public static int 		g_lastTimestamp;
	public static int 		g_num_new_participants;
	public static String[]  temp;//TODO check if this needs to be a global
	
	//--------------------------------------------------------------------
	PGraphics pgVisFullscreen; 
	PGraphics pgVisSmall; 
	PGraphics pgVisLegend;
	
	/*
	public void init() {
		if (frame != null) {
			frame.removeNotify();// make the frame not displayable
			frame.setResizable(false);
			frame.setUndecorated(true);
			frame.addNotify();
		}
		super.init();
		
	}*/
	
	@Override
	public void setup() {
		//size(1800, 768, PConstants.OPENGL);
		size(1024, 768, P3D);
	   
		//hint(ENABLE_NATIVE_FONTS);
	    smooth();
	    frameRate(30);

	    Assets.load(this);
	    
		//--------------------------------------------------------------------
		scheduler 			= new EventScheduler(this);						//TODO check it  
	  	
		mainPhysicsEngine 					= new VerletPhysics2D();		//TODO: think about: pass as an argument to vis ???
		mainSunburstVisualization   		= new SunburstVis();
		mainCenterVis   					= new SunburstCenter(); 
	  
		//--------------------------------------------------------------------
		mainUserData 						= new EnergyUserData();
		mainUserData.registerVis(mainSunburstVisualization);
		
		//--------------------------------------------------------------------
//		mainCom   					= new WebCom();
//	  	g_lastTimestamp 		= 1;
//		g_resultsPublished 		= true;
		
		dbCom					= new VisDatabaseCom(); 
		dbCom.setDashboardListener(this);
		
		//--------------------------------------------------------------------
		facadeDims 				= Dimensions.getInstance();
		facadeGeom  			= new Geometry(facadeDims.getPoints());

		testImage 				= loadImage("tex3.jpg");
		if (testImage !=null)   pgScaleFact	= testImage.width / facadeDims.totalWidth;
	
		pgScaleFact = 1.0f;
		
		pgTextureUnfold1		= createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
		pgTextureUnfold2 		= createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
		pgFacadeUnfold 			= createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
		pgTextureUnfoldRotated  = createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
		
		pgVisFullscreen 		= createGraphics(width, height, P3D);
	    pgVisSmall				= createGraphics((int)facadeDims.totalWidth, (int)facadeDims.totalHeight, P3D);
		pgVisLegend				= createGraphics(width, height, JAVA2D);

		//--------------------------------------------------------------------
//     	if (comOn && !mainCom.running){ 
//     		println();
//		    println("Activate DB Communication!");
//		    mainCom.start();
//     	}
     	if (comOn && !dbCom.isRunning()){ 
     		println();
		    println("Activate DB Communication!");
		    dbCom.start();
     	}
		
	}
	
	float x 	= Assets.maskOffsetX; 
	float y		= Assets.maskOffsetY;
	float y2 	= (y - Assets.maskHeight) * 0.5f; 
	float dx 	= Assets.maskWidth + x;
	float dy2 	= Assets.maskHeight + y2;

	@Override
	public void draw(){
		frame.setLocation(0, 0);

		x 	= Assets.maskOffsetX; 
		y	= Assets.maskOffsetY;
		y2 	= (y - Assets.maskHeight) * 0.5f; 
		dx 	= Assets.maskWidth + x;
		dy2 = Assets.maskHeight + y2;

		background(Assets.appletBGColor);
			
		//--------------------------------draw GUIs
//		if (drawGUI)
//			facadeDims.cp5.show();
//		else facadeDims.cp5.hide(); 

		
		float ratioVisTexture = facadeDims.totalHeight/pgVisFullscreen.height;
		
		if (!mainDrawVisFullscreen){
			//--------------------------------RENDER / DISPLAY SUNBURST MAPPED 

			//--------------------------------render vis to smaller texture 
			renderVisSunburst(pgVisSmall, ratioVisTexture);			
			//--------------------------------display smaller texture to screen 
			pushMatrix();
				imageMode(CORNER);
				translate(x, y2);
				image(pgVisSmall, 0, 0);
				
				if (mainDrawAuxLines){
					noFill();//-------------------draw a debug rectangle on top 
					rectMode(CORNER);
					stroke(Assets.ColorCYAN);
					rect(0, 0, pgVisSmall.width, pgVisSmall.height);
				}
			popMatrix();
			
			
			//-------------------------------RENDER / DISPLAY FACADE UNFOLDINGS / MASKS 
			 
			//-------------------------------render texture-to-be-mapped cut along facade front-side borders  
			facadeGeom.drawTextureUnfold(pgTextureUnfold2, null, pgScaleFact, false, Assets.ColorCYAN, -1); 
			pushMatrix();
				translate(x+dx, y2);
				image(pgTextureUnfold2, 0, 0);
				
				if (mainDrawAuxLines){
					noFill();//-------------------draw a debug rectangle on top 
					rectMode(CORNER);
					stroke(Assets.ColorCYAN);
					rect(0, 0, pgTextureUnfold2.width, pgTextureUnfold2.height);
				}
			popMatrix();
			
			//-------------------------------render texture-to-be-mapped unfolding rotated around the lateral-side borders 
			facadeGeom.drawTextureUnfold(pgTextureUnfoldRotated, pgVisSmall, pgScaleFact, true, Assets.colorWhite, -1); 
			pushMatrix();
				translate(x, y);
				imageMode(CORNER);
				image(pgTextureUnfoldRotated, 0, 0);
				
				if (mainDrawAuxLines){
					noFill();//-------------------draw a debug rectangle on top 
					rectMode(CORNER);
					stroke(Assets.maskStrokeColor1);
					rect(0, 0, pgTextureUnfoldRotated.width, pgTextureUnfoldRotated.height);
				}
			popMatrix(); 
					
			//------------------------------render the facade geometry unfolding mask 
			facadeGeom.drawFacadeUnfold(pgFacadeUnfold, pgScaleFact, Assets.colorWhite, -1); 
			pushMatrix();
				translate(x + dx, y);
				imageMode(CORNER);
				image(pgFacadeUnfold, 0, 0);
				
				if (mainDrawAuxLines){
					noFill();//-------------------draw a debug rectangle on top 
					rectMode(CORNER);
					stroke(Assets.colorWhite);
					rect(0, 0, pgFacadeUnfold.width, pgFacadeUnfold.height);
				}
			popMatrix(); 
					
			//------------------------------------draw category icon at the center of the mapped texture 
			if (mainDrawCategory && dbCom.getActiveCategoryID() > 0){
				int iconID = dbCom.getActiveCategoryID()-1;//categories start with 1
				PImage iconImg = Assets.iconsCategory.get(iconID);
				if (iconImg!=null){
					pushMatrix();
						translate(x + pgTextureUnfoldRotated.width/2, y + pgTextureUnfoldRotated.height/2);
						imageMode(CENTER);
						image(iconImg, 0, 0);
					popMatrix(); 
				}
			}
			//------------------------------draw mask 
			if (mainDrawFacadeMask) {
				drawFacadeMask();
			}
			
			if (mainDrawAuxLines) {
				drawAuxLines(); 
			}
			//-----------------------------END RENDER / DISPLAY 	
		} 
		
		else {
			renderVisSunburst(pgVisFullscreen, 1.0f);
			image(pgVisFullscreen, 0, 0);
		}
				
		//--------------------------------draw participation legend ontop of everything 
		//if (mainDrawLegend) {
			renderVisLegend(pgVisLegend, 0.8f);
			pushMatrix();
				translate(x, y+dy2);
				image(pgVisLegend, 0, 0);
				
				if (mainDrawAuxLines){
					noFill();//-------------------draw a debug rectangle on top 
					rectMode(CORNER);
					stroke(Assets.colorWhite);
					rect(0, 0, pgVisLegend.width, pgVisLegend.height);
				}
			popMatrix();
		//}
		
		scheduler.update();
		readDB();
		
		checkFirstLaunch();
	}
	
	
	
	public void renderVisLegend(PGraphics pg, float scaleFactor){
		
		pg.beginDraw();
		pg.hint(ENABLE_NATIVE_FONTS);
		
		//--------------------fill with transparent pixels 
		pg.loadPixels();
		for (int i = 0; i < pg.pixels.length; ++i) {
		  pg.pixels[i] = pg.pixels[i] & 0xff000000;
		}
		pg.updatePixels();
		
		if (mainDrawLegend){
			pg.pushMatrix();
				pg.scale(scaleFactor);
				mainSunburstVisualization.displayLegend(pg);
	    	pg.popMatrix();
		}
		
		pg.endDraw(); 
		
	}
	
	public void renderVisSunburst(PGraphics pg, float scaleFactor) {
		pg.beginDraw();
		pg.background(0);
		
		//pg.hint(ENABLE_NATIVE_FONTS);//-------------------not needed for low-res displays? 
	    //	pg.smooth();//------------------------------------this slows down extremely 
	    
		//--------------------------------------------------FIXME: what is this about? 
		pg.pushMatrix();
			pg.rotate(frameCount/10);
			mainSunburstVisualization.update(); 
		pg.popMatrix();
	
		pg.pushMatrix();
			pg.translate(pg.width/2, pg.height/2);
			pg.scale(scaleFactor);
			mainSunburstVisualization.displaySunburst(pg);
		pg.popMatrix();
		
		pg.endDraw();
	}
	
	
	@Override
	public void keyPressed(){    
		mainSunburstVisualization.keyPressed(key);
		mainUserData.keyPressed(key);
		switch(key){
			//--------------------------------------------------------flags for facade masks and rendering     
		    case 'f': mainDrawVisFullscreen = !mainDrawVisFullscreen; 	break; 
		    case 'm': mainDrawFacadeMask 	= !mainDrawFacadeMask; 		break; 
		    //case 'g': Assets.drawGUI	= !Assets.drawGUI; 				break; //FIXME not working
		    case 'l': mainDrawAuxLines		= !mainDrawAuxLines; 		break;
		    case 'g': mainDrawSmooth		= !mainDrawSmooth; 		break;
		    
		    //--------------------------------------------------------flags for drawing auxiliary visualization info    
			//case'1': mainDrawInfo 		= !mainDrawInfo; break;
		    case '2': mainDrawLegend 		= !mainDrawLegend; break;
		    case '3': mainDrawCategory 		= !mainDrawCategory; break; 
		    
		    
		    //case'3': mainDrawTitle 		= !mainDrawTitle; break;
		    //case'4': mainDrawCityEnergy = !mainDrawCityEnergy; break;
		    
//		    case 'l': moveLegend 	= !moveLegend; /*scaleScreen = false;*/ break;
//		    case 'i': moveInfo   	= !moveInfo; moveTitle = false; break;
//		    case 't': moveTitle   	= !moveTitle; moveInfo = false;  break;
//		    
//		    case CODED: 
//		      if(moveLegend){
//		        if (keyCode == UP) 			Assets.projectLegendShiftY -= 10;
//		        else if (keyCode == DOWN) 	Assets.projectLegendShiftY += 10;
//		        else if (keyCode == LEFT) 	Assets.projectLegendShiftX -= 10;
//		        else if (keyCode == RIGHT) 	Assets.projectLegendShiftX += 10;
//		        break;
//		      }
//		      if(moveTitle){
//		        if (keyCode == UP) 			Assets.projectTitleShiftY -= 10;
//		        else if (keyCode == DOWN) 	Assets.projectTitleShiftY += 10;
//		        else if (keyCode == LEFT) 	Assets.projectTitleShiftX -= 10;
//		        else if (keyCode == RIGHT) 	Assets.projectTitleShiftX += 10;
//		        break;
//		      }
//		      else if (scaleScreen){
//		        if (keyCode == UP) scaleScreenFactor += 0.1;
//		        else if (keyCode == DOWN) scaleScreenFactor -= 0.1; 
//		        break;
//		      }
//		      else if (moveInfo){
//		        if (keyCode == UP)         	Assets.projectInfoShiftY += 10;
//		        else if (keyCode == DOWN)   Assets.projectInfoShiftY -= 10; 
//		        if (keyCode == LEFT)        Assets.projectInfoShiftX -= 10;
//		        else if (keyCode == RIGHT)  Assets.projectInfoShiftX += 10; 
//		        break;
//		      } 
//		   break;
		    
		   case 'n':
			   int 	randId         = (int)(random(0,5));
			   float randElectro  = random(20,50);
			   float randGas      = random(10,30);
			   float randEnergy   = randElectro / 0.15f + randGas / 0.05f; 
			   String entryStamp = day() + "-" + hour() + ":" + minute() + ":" + second(); 
			   entryStamp = "user"+randId;
			   mainUserData.addEnergyUser(randId, entryStamp, randElectro, randGas, randEnergy);
		   break;
		  
		   case 'p': 
		     if (moveInfo) {
		       println("yInfoShift = " + Assets.projectInfoShiftY);
		     } else if (moveLegend){
		         println("yLegendShift = " + Assets.projectLegendShiftY);
		         println("xLegendShift = " + Assets.projectLegendShiftX);
		     } else if(moveTitle){
		         println("yTitleShift = " + Assets.projectTitleShiftY);
		         println("xTitleShift = " + Assets.projectTitleShiftX);
		     } else if (scaleScreen){
		       println("scaleScreenFactor = " + scaleScreenFactor);
		     } else if (debugScreen) {
		       println ("visZeroX = " + Assets.visZeroX);
		       println ("visZeroY = " + Assets.visZeroY);
		     }
		   break;
		  }
		
		
	}

	@Override
	public void mousePressed() {
		if(over) {
			move = true;
		}
	}

	@Override
	public void mouseReleased(){
		move = false;
	}
	 
	/** Checks if there are new participants in the DB and if yes, adds them to the visualiuzation
	 * TODO: make everything pass through the communication thread class */
	public void _readDB(){
		if(g_newResults) {
		     System.out.println();
			 for (int j=3; j <= g_num_new_participants+2; j++){
			    temp = split(g_lines[j], ':');
				mainUserData.addEnergyUser(Integer.parseInt(temp[0]),
			    							temp[1],
			    							Float.parseFloat(temp[2]),
			    							Float.parseFloat(temp[3]), 
			    							Float.parseFloat(temp[4]));
			 }
			 g_newResults 			= false;
		     g_resultsPublished 	= true;
		}	
	}
	
	/** Query DB for new participants */
	public void readDB(){
		if(dbCom.areNewResults()) { 
			println("MainPApplet::readDB(): NEW PARTICIPANTS IN DB!!!");
		    int num = dbCom.getParticipants().size(); 
		    DatabaseParticipant tmpParticipant; 
		    
		    for (int j=0; j < num; j++) {
		    	tmpParticipant = dbCom.getParticipants().get(j);
		      
		      if (tmpParticipant != null) {
		    	String cardID 		= tmpParticipant.getCardID();
				int devID 			= tmpParticipant.getDevID();
				int catID			= tmpParticipant.getCatID();
				int prefID			= tmpParticipant.getPrefID();
				long time			= tmpParticipant.getTstamp();
				System.out.println("MainPApplet::readDB():  Participant: " + cardID + ":" + devID + ":" + catID + ":" + prefID + ":" + time);
				float feedbackVal = 0f; 
				switch(prefID) {
					case 0: feedbackVal = 30; break; 
					case 1: feedbackVal = 60; break; 
					case 2: feedbackVal = 90; break; 
				}
				mainUserData.addEnergyUser(devID,cardID,feedbackVal, 0, 0);
		      }
		    }
		    dbCom.setNewResults(false);
		    dbCom.setResultsPublished(true);
		  }
	}
	
	@Override
	public void categorySelected(int _catID) {
		System.out.println("MainPApplet::categorySelected("+_catID+")");
		mainSunburstVisualization.reset();
	}

	@Override
	public void sentimentSubmitted(int _prefID, String _cardID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void specialCategorySelected(int _val) {
		// TODO Auto-generated method stub
		
	}
	
	public void checkFirstLaunch(){
		if (g_bFirstLaunch){
		    println("APP first launch... #frames " + frameCount); 
		    g_bFirstLaunch = false; 
		}
	}
	
	/** Catch controlP5 controlEvents here (callback seems not accessible outside PApplet) */
	public void controlEvent(ControlEvent theEvent) {
		facadeDims.controlEvent(theEvent);
	}
	
	/** Draws the mask corresponding the facade vis mapping */
	public void drawFacadeMask(){
		pushMatrix();
			rectMode(CORNER);
			fill(0);
			noStroke();
			rect(0, 0, Assets.maskOffsetX, width);	//-------------------------------------------------------------#1
			rect(Assets.maskOffsetX, 0, width - Assets.maskOffsetX, Assets.maskOffsetY);//--------------------------------------#2
			rect(Assets.maskOffsetX + Assets.maskWidth, Assets.maskOffsetY, width - Assets.maskOffsetX - Assets.maskWidth, Assets.maskHeight);//-----#3
			rect(Assets.maskOffsetX, Assets.maskOffsetY + Assets.maskHeight, width - Assets.maskOffsetX, height - Assets.maskOffsetY - Assets.maskHeight);//---#4
			
			noFill(); 
			strokeWeight(1.f);
			stroke(0, 255, 0);
			rect(Assets.maskOffsetX, Assets.maskOffsetY, Assets.maskWidth, Assets.maskHeight);
		popMatrix(); 
	}
	
	public void drawAuxLines(){
//		x 	= Assets.maskOffsetX; 
//		y	= Assets.maskOffsetY;
//		
//		y2 	= (y - Assets.maskHeight) * 0.5f; 
//		dx 	= Assets.maskWidth + x;
//		dy2 = Assets.maskHeight + y2;
//		
//		println("Assets.maskOffsetY: " + Assets.maskOffsetY);
//		
		pushMatrix();
			noFill(); 
			strokeWeight(1.0f);
			
			//draw guides for facade mask window pos 
			stroke(Assets.maskStrokeColor1);
			line(x, 0, x, height);
			line(0, y, width, y);
			
			//draw guides for the center of the texture where the visualization is rendered 
			stroke(Assets.ColorCYAN);
			line(x, y2, dx, dy2);
			line(x, dy2, dx, y2);		
		popMatrix();
	}
	
	public static void main(String args[]) {
		//PApplet.main(new String[] { "-present", "App" });
		PApplet.main(new String[]{"scsd.MainPApplet"});
		//PApplet.main(new String[] { "--present", "scsd.MainPApplet" });

	}

	

	
		
	  
}