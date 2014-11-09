package util;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;

public class Assets {

	////////////////////////////////////////////////////////////////////////////////
	// Assets Facade Masks GUI 
	////////////////////////////////////////////////////////////////////////////////

	public static float maskStrokeWeight 	= 1.0f; 
	public static int 	maskBGColor	   		= 0;
	public static int   maskStrokeColor1	= 255;
	public static int   ColorCYAN	= 255;
	public static int   maskStrokeColor3	= 255;
	public static int 	maskFillColor		= 0;
	public static int   colorWhite			= 255; 
	public static int 	colorBlack			= 0; 
	
	//-----------------------------------------------------------------------------
	public static float 		maskOffsetX				= 37; 
	public static float 		maskOffsetY				= 259; 
	public static float 		maskWidth 				= 214; 
	public static float 		maskHeight 				= 168; 
	
	public static float 		facadeOffsetX			= maskOffsetX; 
	public static float 		facadeOffsetY			= maskOffsetY; 
		
	
	
	////////////////////////////////////////////////////////////////////////////////
	// Assets MainPApplet 
	////////////////////////////////////////////////////////////////////////////////
	public static int 	appletBGColor	   	= 0;
	
	////////////////////////////////////////////////////////////////////////////////
	// Assets Visualization 
	////////////////////////////////////////////////////////////////////////////////
	
	public static int visZeroX	= 0; 
	public static int visZeroY	= 0; 
	
	public static int   visActiveUserSecs 	= 0; 
	public static float visArcThickness    	= 2.f; 
	public static float visScaleFactor     	= 1.0f; 
	
	public static float visMaxUserInputValue  		= 100.f;
	public static float visMinRadius        		= 170.f; 
	public static float visMaxRadius        		= 700.f;
	
	////////////////////////////////////////////////////////////////////////////////
	// Assets Energy Visualization  
	////////////////////////////////////////////////////////////////////////////////
	
	//-----------------------------------------------------------------------------FONTS AND FONT SIZES 
	public static PFont visFontLegend;  
	public static PFont visFontLastUser;
	public static PFont visFontCenter;
	public static PFont visFontDistricts;
	public static PFont visFontTitles;
	public static PFont visFontProjectInfo;
	public static PFont visFontProjectTitle;
	
	public static final float visFontsizeMin1 = 14;
	public static final float visFontsizeMin2 = 30;
	
	public static float visFontSize1 = 14; 
	public static float visFontSize2 = 30;

	
	public static float avgCityThickness 	= 13.f; 
	public static float avgCityGas 			= 9.75f;
	public static float avgCityElectro 		= 19.8f;
	public static float avgCityEnergy 		= 163.f;
  
	public static int   cityColor 			= 255;
	public static int   cityId 				= -1;
  
	public static int 		wBarConsumption 	= 140;
	
	
	//--------------------------------------------------STRING LABELS
	public static String labelLongestDistrictName = "00 TIRSO DE MOLINA";   
	public static String labelCurrencySymbol = ""; 

	public static String labelCurrentCity    = "SAO PAULO"; 
	public static String labelParticipation  = "NEIGHBORHOOD PARTICIPANTS:";
	public static String labelConsumption    = "NEIGHBORHOOD EXPENSES IN " + labelCurrencySymbol;
    
    public static String labelProjectTitle 		= "Reveal-it!";
	public static String labelProjectSubtitle 	= "COMPARE YOUR ENERGY\nCONSUMPTION DATA";
	public static String labelProjectInfo1 		= "PARTICIPATE! \nSEND YOUR DATA VIA YOUR MOBILE PHONE";
	public static String labelProjectURL 		= "HTTP://BIT.LY/REVEAL-IT";
	
	public static String labelFrom 			= "From:";
	public static String labelDataSource 	= "Departamento de Estadistica del Ayuntamiento de Barcelona: \nhttp://www.bcn.cat/estadistica/catala/index.htm";
	
	public static PShape iconMobile;
	
	public static PImage iconCategory0;
	public static PImage iconCategory1;
	public static PImage iconCategory2;
	public static PImage iconCategory3;
	public static PImage iconCategory4;
	public static PImage iconCategory5;
	public static PImage iconCategory6;
	
	public static ArrayList<PImage> iconsCategory; 
	
	//-------------------------------------------------------------------AUXILIARY VIS
	public static int projectLegendX 		= 30; 
	public static int projectLegendShiftX 	= 0;
	public static int projectLegendShiftY 	= 0;
	
	public static int projectTitleShiftX 	= 0;
	public static int projectTitleShiftY 	= 0;
	
	public static int projectInfoShiftX 	= 0; 
	public static int projectInfoShiftY 	= 0; 
		
	
	public static String districtConfig = "/Users/moni/Documents/Eclipse/scsd-vis/data/districts.csv";
	
	public static boolean drawGUI		= true;


	public static void load(PApplet p) {
		
		maskBGColor	   		= p.color(0);
		maskStrokeColor1	= p.color(0, 255, 0);
		ColorCYAN	= p.color(0, 255, 255);
		maskStrokeColor3	= p.color(255, 255, 0);
		maskFillColor		= p.color(0, 255, 0, 200);
		
		visZeroX			 = (p.width)/2; 
		visZeroY 			 = (p.height)/2; 
		
//		visFontLegend        = p.loadFont("MyriadPro-Bold-14.vlw");		//0
//		visFontLastUser      = p.loadFont("ArialNarrow-Bold-40.vlw");	//1
//	    visFontCenter 		 = p.loadFont("ArialNarrow-Bold-40.vlw");	//1
//	    
//	    visFontTitles        = p.loadFont("MyriadPro-Bold-14.vlw");		//0
//	    visFontDistricts     = p.loadFont("MyriadPro-Regular-14.vlw");	//2
//	    visFontProjectInfo   = p.loadFont("MyriadPro-Bold-14.vlw");		//0
//	    visFontProjectTitle  = p.loadFont("ArialNarrow-40.vlw"); 		// BARCELONETA CIVIC CENTER     
//	    
	    
	    visFontLegend 	= p.createFont("MyriadPro-Bold", 14);
	    visFontLastUser = p.createFont("MyriadPro-Bold", 40);	//1
	    visFontCenter 	= p.createFont("MyriadPro-Bold", 40);		//1
	    
	    visFontTitles       = p.createFont("Georgia", 14);	
	    visFontDistricts     = p.createFont("Georgia", 14);	
	    visFontProjectInfo   = p.createFont("Georgia", 14);	
	    visFontProjectTitle  = p.createFont("Georgia", 40);			// BARCELONETA CIVIC CENTER     
	    
	    
//	    fontProjectInfo      = p.loadFont("MyriadPro-Bold-14.vlw"); 	// CORDOBA CULTURAL CENTER 
//	    fontProjectTitle     = p.loadFont("MyriadPro-Bold-20.vlw");    
//	    fontProjectTitle     = p.loadFont("HelveticaCY-Bold-24.vlw"); 	// VIRGEN CULTURAL CAFE 
//	    fontTitles           = P.loadFont("AndaleMono-14.vlw");
		    
	    
	   iconMobile           = p.loadShape("/Users/moni/Documents/Eclipse/scsd-vis/data/mobile.svg");

	    
	    //iconCategory0		= p.loadImage("icons/0-air.png");
		iconCategory1		= p.loadImage("/Users/moni/Documents/Eclipse/scsd-vis/data/icons/1-environment.png");
		iconCategory2		= p.loadImage("/Users/moni/Documents/Eclipse/scsd-vis/data/icons/2-mobility.png");
		iconCategory3		= p.loadImage("/Users/moni/Documents/Eclipse/scsd-vis/data/icons/3-safety.png");
		iconCategory4		= p.loadImage("/Users/moni/Documents/Eclipse/scsd-vis/data/icons/4-publicspace.png");
		iconCategory5		= p.loadImage("/Users/nina/Documents/Eclipse/scsd-vis/data//Users/moni/Documents/Eclipse/scsd-vis/data/icons/5-housing.png");
		iconCategory6		= p.loadImage("/Users/moni/Documents/Eclipse/scsd-vis/data/icons/6-quality-all.png");
		
		iconsCategory 		= new ArrayList<PImage>();
		//iconsCategory.add(iconCategory0);
		iconsCategory.add(iconCategory1);
		iconsCategory.add(iconCategory2);
		iconsCategory.add(iconCategory3);
		iconsCategory.add(iconCategory4);
		iconsCategory.add(iconCategory5);
		iconsCategory.add(iconCategory6);
	}
	
}
