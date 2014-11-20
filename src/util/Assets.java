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

	// hard-coded category names FIXME: quick hack for Riga due to strange signs in their alphabet 
	// which causes errors in loading them from file or DB 
	public static String[] categoryNames = new String[6];
	
	
	public static float maskStrokeWeight 	= 1.0f; 
	public static int 	maskBGColor	   		= 0;
	public static int   maskStrokeColor1	= 255;
	public static int   ColorCYAN	= 255;
	public static int   ColorRED	= 255;
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
	public static int visCategoryStates[] = {0, 0, 0, 0, 0};
	public static float visColorHue	= 0;
	
	public static float visZeroShiftX							= 0; 
	public static float visZeroShiftY							= 0; 
	
	public static int   visActiveUserSecs 				= 3; 
	public static float visScaleFactor     				= 1.0f; 
	public static float visArcThickness    				= 2.f; 

	
	public static float 	visPieceLineStrokeWidth 	= 1.0f;
	public static int 		visPieceLineColor		  	= 0;
	public static boolean 	visPieceLineDraw      		= false;
	public static float 	visPieceLineScale			= 0.5f;
	public static boolean   visPieceLineSmooth    		= false;
	public static double 	visPiecesSpace				= 0.05;
	
	public static boolean visDistrictArcDecoLineDraw 	= false;
	public static boolean visDistrictArcSmooth       	= true;
	public static float   visDistrictArcThickness    	= 3.f; 

	public static boolean 		heartStateOn 			= false;
	public static Timer			heartStateTimer 		= null;
	public static int 			heartStateSeconds 		= 3;
	
	
	//-------------------SCSD SMALLSCREEN VALUES--------------------------------
//	public static float visMaxUserInputValue  		= 3.f;
//	public static float visMinRadius        		= 2.f; 
//	public static float visMaxRadius        		= 220.f;

	//-------------------SCSD FULLSCREEN VALUES--------------------------------
	public static float visMaxUserInputValue  		= 2.f;
	public static float visMinRadius        		= 200.f; 
	public static float visMaxRadius        		= 400.f;
	public static float visCenterRadius 			= 150.f;

	//-------------------REVEAL-IT FULLSCREEN VALUES--------------------------------
//	public static float visMaxUserInputValue  		= 100.f;
//	public static float visMinRadius        		= 170.f; 
//	public static float visMaxRadius        		= 800.f;
	//------------------------------------------------------------------------------
	
	//-------------------------------------------------------------------	
	public static boolean mainDrawInfo 			= false;
	public static boolean mainDrawTitle 			= false; 	
	public static boolean mainDrawCityEnergy 		= false;
		
	public static boolean mainDrawLegend 			= false; 
	public static boolean mainDrawCategory			= true; 
	public static boolean mainDrawVisFullscreen 	= false; 
	public static boolean mainDrawFacadeMask	  	= true; 
	public static boolean mainDrawAuxLines			= false; 
	public static boolean mainDrawSmooth 			= true; 
	
	//Assets.visMaxUserInputValue, Assets.visMinRadius, Assets.visMaxRadius);
	
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

	public static boolean 	visHeartStateOn 		= false;
	public static int 		visBurstPieceAlpha  	= 200; // the amount of alpha to add to the sunburst 
	
	
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
	
	//public static PImage iconCategory0;
//	public static PImage iconCategory1;
//	public static PImage iconCategory2;
//	public static PImage iconCategory3;
//	public static PImage iconCategory4;
//	public static PImage iconCategory5;
//	public static PImage iconCategory6;
	
	public static boolean iconTestingOn = true; 
	public static int 	  iconTestingID = 0; 
	public static float   iconScale		= 0.57f; 
	public static float   iconScaleBG	= 1.41f; // scaling fact of the background of the icon (black circle) w.r.t the size of the image 
	
	public static PShape iconCategory1;
	public static PShape iconCategory2;
	public static PShape iconCategory3;
	public static PShape iconCategory4;
	public static PShape iconCategory5;
	public static PShape iconCategory6;
	
	//public static ArrayList<PImage> iconsCategory; 
	public static ArrayList<PShape> iconsCategory;
	public static ArrayList<PShape> iconsCategoryTesting;
	
	//-------------------------------------------------------------------AUXILIARY VIS
	public static int projectLegendX 		= 30; 
	public static int projectLegendShiftX 	= 0;
	public static int projectLegendShiftY 	= 0;
	
	public static int projectTitleShiftX 	= 0;
	public static int projectTitleShiftY 	= 0;
	
	public static int projectInfoShiftX 	= 0; 
	public static int projectInfoShiftY 	= 0; 
		
	
	public static String districtConfig = "/Users/nina/Documents/Eclipse/scsd-vis/data/districts.csv";
	
	public static boolean drawGUI		= true;
	public static int colorTestingID = 0;
	
	public static boolean 	debugOn		  			= false;
	
	
	
	
	
	
	
	
//	public static void heartStateOn() {
//		heartStateTimer.start();
//		System.out.println("Assets::heartStateOn " + heartStateTimer.getSavedTime() + heartStateTimer.getTotalTime());
//		
//		heartStateOn = true; 
//	}
//	
//	public static void heartStateOff() {
//		System.out.println("Assets::heartStateOff");
//		heartStateOn = false;
//	}
//	
//	public static boolean isHeartStateOver(){
//		boolean b = heartStateTimer.isFinished();
//		System.out.println("Assets::isHeartStateOver " + heartStateTimer.getSavedTime() + "----" + heartStateTimer.getTotalTime());
//
//		return b;
//	}
	
	
	public static void load(PApplet p) {

		//-------------------------------------------------
		//FIXME to be done better, now hard coded due to special letters 
		//Riga 2014
		/*
		categoryNames[0] = "ATTISTIBA";//"ATTĪSTĪBA";  
		categoryNames[1] = "TRANSPORTS";
		categoryNames[2] = "VIDE";
		categoryNames[3] = "DROŠIBA";//"DROŠĪBA";
		categoryNames[4] = "KULTURA";//"KULTŪRA";  
		categoryNames[5] = "RIGA";
		*/
		
		//LINZ / MAB'14
		categoryNames[0] = "EDUCATION";//"ATTĪSTĪBA";  
		categoryNames[1] = "IMMIGRATION";
		categoryNames[2] = "TECHNOLOGY";
		categoryNames[3] = "MOBILITY";//"DROŠĪBA";
		categoryNames[4] = "PUBLIC SPACE";//"KULTŪRA";  
		categoryNames[5] = "MAB14";
		
		
		heartStateTimer		= new Timer(1 * heartStateSeconds * 1000);
		
		maskBGColor	   		= p.color(0);
		maskStrokeColor1	= p.color(0, 255, 0);
		ColorCYAN	= p.color(0, 255, 255);
		ColorRED	= p.color(255, 0, 0);
		
		maskStrokeColor3	= p.color(255, 255, 0);
		maskFillColor		= p.color(0, 255, 0, 200);
		
		visZeroShiftX			 = 0f; 
		visZeroShiftY 			 = 0f; 
		
		visFontLegend        = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/MyriadPro-Bold-14.vlw");
		visFontLastUser      = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/ArialNarrow-Bold-40.vlw");
	    visFontCenter 		 = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/ArialNarrow-Bold-40.vlw");
	    
	    visFontTitles           = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/MyriadPro-Bold-14.vlw");
	    visFontDistricts        = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/MyriadPro-Regular-14.vlw");
	    visFontProjectInfo      = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/MyriadPro-Bold-14.vlw");
	    visFontProjectTitle     = p.loadFont("/Users/nina/Documents/Eclipse/scsd-vis/data/ArialNarrow-40.vlw"); 		// BARCELONETA CIVIC CENTER     
//	    fontProjectInfo      = p.loadFont("MyriadPro-Bold-14.vlw"); 	// CORDOBA CULTURAL CENTER 
//	    fontProjectTitle     = p.loadFont("MyriadPro-Bold-20.vlw");    
//	    fontProjectTitle     = p.loadFont("HelveticaCY-Bold-24.vlw"); 	// VIRGEN CULTURAL CAFE 
//	    fontTitles           = P.loadFont("AndaleMono-14.vlw");
		    
	    
	    iconMobile           = p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/mobile.svg");

	    
		iconCategory1		= p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/transporte01.svg");
		iconCategory2		= p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/seguranca03.svg");
		iconCategory3		= p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/espaciopublico02.svg");
		iconCategory4		= p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/habitacao01.svg");
		iconCategory5		= p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/meioambiente01.svg");
		
		iconCategory6		= p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/corazao01.svg");
		
//		iconCategory1		= p.loadImage("icons/1-environment.png");
//		iconCategory2		= p.loadImage("icons/2-transport.png");
//		iconCategory3		= p.loadImage("icons/3-safety.png");
//		iconCategory4		= p.loadImage("icons/4-publicspace.png");
//		iconCategory5		= p.loadImage("icons/5-housing.png");
//		iconCategory6		= p.loadImage("icons/6-quality-all.png");
//		
//		iconsCategory 		= new ArrayList<PImage>();
		iconsCategory 		 = new ArrayList<PShape>();
		iconsCategoryTesting = new ArrayList<PShape>();
		
		iconsCategory.add(iconCategory1);
		iconsCategory.add(iconCategory2);
		iconsCategory.add(iconCategory3);
		iconsCategory.add(iconCategory4);
		iconsCategory.add(iconCategory5);
		
		iconsCategory.add(iconCategory6);
		
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/meioambiente02.svg"));
		iconsCategoryTesting.add(p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/transporte01.svg"));
		iconsCategoryTesting.add(p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/seguranca03.svg"));

		//iconsCategoryTesting.add(p.loadShape("iconsSVG/corazao02.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/corazao03.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/espaciopublico01.svg"));
		iconsCategoryTesting.add(p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/espaciopublico022.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/espaciopublico03.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/espaciopublico04.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/seguranca01.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/seguranca02.svg"));
		
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/transporte02.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/transporte03.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/transporte04.svg"));
		
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/habitacao02.svg"));
		//iconsCategoryTesting.add(p.loadShape("iconsSVG/habitacao03.svg"));
		iconsCategoryTesting.add(p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/habitacao05.svg"));
		iconsCategoryTesting.add(p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/meioambiente01.svg"));

		iconsCategoryTesting.add(p.loadShape("/Users/nina/Documents/Eclipse/scsd-vis/data/iconsSVG/corazao01.svg"));
		
		
	}
	
}
