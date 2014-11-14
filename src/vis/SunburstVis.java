package vis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import scsd.MainPApplet;
import toxi.geom.Vec2D;
import util.Assets;

abstract class GenericVis {
	
	GenericVis() {
	}
	
	abstract void notifyNewUser(int _id, 
								String _name, 
								float _val1,
								float _val2, 
								float _val3);
}

public class SunburstVis extends GenericVis {

	
	//---------------hashmap: district id-pieces per district; contains arrayLists
	HashMap<Integer, ArrayList<SunburstPiece>> 	visMapPiecesPerDistrict; 
	
	//---------------hashmap: id-district
	HashMap<Integer, SunburstDistrict> 			visMapDistricts; 
														
	SunburstDistrict lastDistrict;

	float angleUser;

	//---------------current number of users that have revealed their data
	int numUsers;

	float ratioHor = 0.5f;
	float ratioVer = 1.0f / 3.0f;

	//---------------animation effects 
	boolean bBounceAll = false;
	
	//---------------separate piece for the energy of the whole city 
	SunburstPiece energyCity;

	//---------------LEGEND
	float avgCityEuro = 0.f; 
	float avgCityPixels; 
	
	int xParticipation;
	int yParticipation;
	
	int yConsumptionTitle;
	int xConsumptionTitle;
	
	int xConsumptionMadrid;
	int xConsumptionBars;

	int xMadrid;
	int yMadrid;

	int xLabels;
	int xParticipationBars;

	int yLabels;
	int yParticipationBar;
	int yConsumptionMadrid;

	int maxW_ParticipationBar;
	int maxW_ConsumptionBar;
	// --------------------------------------------------------------
	
	boolean colorYellow = false;
	boolean debug = true;

	Vec2D center;
	boolean doScale = false;

	public SunburstVis() {
		super();

		reset();

		// ------------------------set the city energy piece
		energyCity = new SunburstPiece(Assets.cityId, Assets.labelCurrentCity,
				Assets.avgCityElectro, Assets.avgCityGas, Assets.avgCityEnergy,
				2 * PConstants.PI, MainPApplet.mainPhysicsEngine);

		avgCityEuro = Assets.avgCityElectro + Assets.avgCityGas;
		float cityRad = energyCity.getRad();
		avgCityPixels = PApplet.map(avgCityEuro, 0, cityRad, 0,
				Assets.wBarConsumption);
	}
	
	public void reset(){
		if (visMapPiecesPerDistrict == null)
			visMapPiecesPerDistrict 		= new HashMap<Integer, ArrayList<SunburstPiece>>();
		else 
			visMapPiecesPerDistrict.clear();
		
		if (visMapDistricts == null)
			visMapDistricts 				= new HashMap<Integer, SunburstDistrict>();
		else 
			visMapDistricts.clear();
		
		lastDistrict = null;
		
		numUsers 	 = 0;
		angleUser    = 0.f;
		// -------------------------set angle
		calcAngle();
	}

	public void update() {
		calcAngle();
		MainPApplet.mainPhysicsEngine.update();
	}

	/** Calculates the current angle portion of a sunburst */
	void calcAngle() {
		if (numUsers == 0)
			angleUser = 0.f;
		else {
			float angleRange = PConstants.PI * 2f;
			angleUser = (angleRange - (float)Assets.visPiecesSpace * numUsers)/ numUsers;
		}
	}

	@Override
	void notifyNewUser(int _id, String _name, float _electro, float _gas, float _energy) {

		if (_name.equals("")) {
			_name = "You";
		}

		if (debug) {
//			System.out.println("SunburstVis::notifyNewUser: (" + _id + ","
//															   + _name + "," 
//															   + _electro + "," 
//															   + _gas + ","
//															   + _energy
//															   + ") numUsers = " + numUsers);
		}

		//MainPApplet.mainCenterVis.setContent_USER1(_name, _gas + _electro);
		//MainPApplet.mainCenterVis.setContent_VISITORS();
		
		// ---------------------first of all: calculate new piece angle
		numUsers++;
		calcAngle();

		SunburstPiece incomingParticipantPiece = new SunburstPiece(_id, 
															  _name, 
															  _electro,
															  _gas,
															  _energy,
															  angleUser,
															  MainPApplet.mainPhysicsEngine);
		
		SunburstDistrict district;		
		// ---------------------this district is already in the map of energy pieces
		if (visMapPiecesPerDistrict.containsKey(_id)) {
			
			//------------------add a new participant in the list of participants for this district 
			ArrayList<SunburstPiece> participantList = (ArrayList<SunburstPiece>) visMapPiecesPerDistrict.get(_id);
			participantList.add(incomingParticipantPiece);
			
			//------------------get the district and notify it about the change
			district = (SunburstDistrict) visMapDistricts.get(_id);
			district.notifyUpdate(_electro + _gas, _energy);
		} else {
			if (debug)
				System.out.println("notifyNewUser: user from a new district!");
			
			//------------------otherwise create a new list (for a new district) 
			ArrayList<SunburstPiece> participantList = new ArrayList<SunburstPiece>();
			//------------------and add this piece to it; 
			participantList.add(incomingParticipantPiece);
			//------------------update the map of participants per district 
			visMapPiecesPerDistrict.put(_id, participantList);

			//------------------create a new district visualization and 
			String dName 	= MainPApplet.mainUserData.getDistrictName(_id);
			String dCode 	= MainPApplet.mainUserData.getDistrictCode(_id);
			int dColor 		= MainPApplet.mainUserData.getDistrictColor(_id);

			district = new SunburstDistrict(_id, dName, 
												 dCode, 
												 dColor,
												 MainPApplet.mainPhysicsEngine);
			
			//------------------update it with its first value 
			district.notifyUpdate(_electro + _gas, _energy);
			
			//------------------save this new district in the map of districts 
			visMapDistricts.put(_id, district);
		}
		//----------------------save information about this last interaction (user, district) 
		district.lastParticipantInfo 	= incomingParticipantPiece; 
		lastDistrict 					= district;
	}


	public void displaySunburst(PGraphics pg) {
		
		if (pg == null) {
			System.out.println("EnergyCircle::displaySunburst() PGraphics null! return...");
			return;
		}
		
		pg.pushMatrix();
		pg.rotate(PConstants.PI / 2 + angleUser*0.5f);
		
		try {
			Iterator<ArrayList<SunburstPiece>> i = visMapPiecesPerDistrict.values()
					.iterator();
			Iterator<SunburstDistrict> i2 = visMapDistricts.values().iterator();

			float lastAng = 0.f;
			float avgArc = 0.f;

			while (i.hasNext() && i2.hasNext()) {

				ArrayList<SunburstPiece> energyPieces = (ArrayList<SunburstPiece>) i
						.next();
				SunburstDistrict tmpDistrict = (SunburstDistrict) i2.next();
				int disColor = tmpDistrict.getCol();

				// loop users in district
				for (int p = 0; p < energyPieces.size(); p++) {
					SunburstPiece tmpEnergyPiece = (SunburstPiece)energyPieces.get(p);

					if (!colorYellow)
						tmpEnergyPiece.setColorNormal(disColor);
					else {
						// epa.setCol1(color(255,255,0));
					}
					
					//-----paint the single bursts only if we are NOT in the HEART MODE 
					if ((MainPApplet.getInstance()).dbCom != null && (MainPApplet.getInstance()).dbCom.getHeart() != 0){
						tmpEnergyPiece.display(pg, lastAng, lastAng + angleUser);
						tmpEnergyPiece.updateSpring(bBounceAll);
						bBounceAll = false;
					}	
					
					avgArc += tmpEnergyPiece.getRad();
					lastAng += angleUser + Assets.visPiecesSpace;
				}// ------------------------------------------------------------------------end
					// loop for users in district

				// -------------------------------------------------------------------------calc
				// arc for the district
				int lastNumPieces = energyPieces.size();
				if (lastNumPieces > 0) {
					avgArc /= lastNumPieces;
				}
				// -------------------------------------------------------------------------draw
				// arc for the district
				
				if ((MainPApplet.getInstance()).dbCom != null && (MainPApplet.getInstance()).dbCom.getHeart() != 0){
					tmpDistrict.displayArc(pg, lastAng - lastNumPieces * (angleUser + (float)Assets.visPiecesSpace), lastAng, avgArc);
				}
				else {
					tmpDistrict.displayArc(pg, lastAng - lastNumPieces * (angleUser) - lastNumPieces * (float)Assets.visPiecesSpace, 
											   lastAng - (float)Assets.visPiecesSpace, 
											   avgArc);
				}
				// -------------------------------------------------------------------------reset
				// avgArc calculation for next district
				avgArc = 0;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		pg.popMatrix();
		
	}

	public void displayLegend(PGraphics pg) {
		// pg.textAlign(LEFT);
		
		int yShiftCityConstant_MADRID = 25;

		// -----------------------------------------------
		Iterator<SunburstDistrict> it = visMapDistricts.values().iterator();

		pg.pushMatrix();
		pg.textFont(Assets.visFontTitles, 14);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);

		int xTitle = 0;
		int yTitle = 0;

		int xTitleShift = 235;
		int yCityShift = 170;
		int xCityShift = -15;

		pg.fill(255);
		pg.textFont(Assets.visFontTitles, 14);
		pg.text(numUsers, xTitle - 4, yTitle - 1);
		//pg.textFont(Assets.visFontTitles, 14);

		pg.fill(255);
		pg.text(numUsers + " " + Assets.labelParticipation, xTitle, yTitle - 10);
		pg.text(Assets.labelConsumption, xTitle + xTitleShift, yTitle - 10);
		
		pg.textFont(Assets.visFontDistricts, 14);
		int dotSize = 6; // size of the dots of the things connecting
							// participation bars with consumption bars

		// Start drawing bars etc.
		// ---------------------------------------------------
		int i = 0;
		int yLegendShift = 40;
		int yLegend = yTitle + yLegendShift;
		int yLabel = yLegend - 25; // magic value was "invented" manually :)

		int yLabelsShift = 8; // vertical text shift
		int yBarsShift = 3; // vertical bar shift

		int wDeco = 10;
		int wLabels = (int) pg.textWidth(Assets.labelLongestDistrictName); // bbox
																		// width
																		// of
																		// labels
																		// (longest
																		// district
																		// word:
																		// ARGANZUELA)
		int xBarShift = wLabels + wDeco; // start of participation bars

		int wBarParticipation = xTitleShift - wLabels - wDeco;

		/*
		 * DRAW AUX RECTANGLES TO SEE THE DIFFERENTES AREAS if (debug){
		 * pg.fill(255,100); pg.rect(0,yLegend, wLabels, 114);
		 * pg.fill(255,0,0,100); pg.rect(xBarShift, yLegend, wBarParticipation,
		 * 114); }
		 */

		while (it.hasNext()) {
			SunburstDistrict d = (SunburstDistrict) it.next();
			pg.textFont(Assets.visFontLegend);

			// move down for each district labels
			pg.fill(d.getCol());

			pg.pushMatrix();
			pg.translate(xTitle, yLabel);
			// pg.text("0" + d.id + " " + d.name, xTitle, yLabel);
			pg.text(d.name, xTitle, yLabel);
			pg.popMatrix();

			// TRICKY the MAX for range1 in the case of consumption is the same
			// as
			float valAvg = PApplet.map(d.getAvgExpensesPix(), 0,
					Assets.visMaxRadius, 0, Assets.wBarConsumption);
			float valParticip = PApplet.map(d.getNumUsersPhysics(), 0,
					wBarParticipation * 7 / 30, 0, wBarParticipation);

			int yBars = (int) (yLegend - 10 + i * (d.barThickness + yBarsShift));

			// draw bars of participation
			pg.pushMatrix();
			pg.translate(xBarShift, yBars);
			pg.rect(0, 0, valParticip, d.barThickness);
			pg.noStroke();
			pg.popMatrix();

			// draw bars of avg consumption
			pg.pushMatrix();
			pg.translate(xTitle + xTitleShift, yBars);
			pg.noStroke();
			pg.rect(0, 0, valAvg, d.barThickness);
			pg.popMatrix();

			pg.pushMatrix();
			// 1. draw the STRING value of participation
			// y-coordinate sames as the label of the district
			pg.translate(xBarShift + valParticip, yLabel);
			String txt = "" + d.getNumUsers();
			pg.text(txt, xTitle, yLabel);

			// draw element: o--------------------------- |
			// x-coordinate depends on position of participation number
			// y-coordinate is the same as the bars of participation
			// go up
			pg.translate(0, -yLabel);
			pg.translate(pg.textWidth(txt), yBars);
			float dotShift = dotSize * 0.75f;
			pg.ellipse(dotShift, dotShift, dotSize, dotSize);

			int wLine = (int) (xTitle + xTitleShift - (xBarShift + valParticip + dotShift));
			pg.rect(dotShift, dotSize * 0.7f, wLine, 3f);
			pg.popMatrix();

			// draw the value strings of the avg consumption //has the same Y
			// coord as the label of the district!
			pg.pushMatrix();
			pg.textFont(Assets.visFontTitles, 14);
			// pg.translate(xBarShift + val, yLabel);
			pg.translate(xTitle + xTitleShift + valAvg, yLabel);
			pg.text("" + (int) d.getAvgExpensesEuro()
					+ Assets.labelCurrencySymbol, xTitle + 2, yLabel);
			pg.popMatrix();

			yLabel += yLabelsShift;
			i++;

		}
		// ----------------------------- Draw City Stuff
		pg.fill(255);
		pg.textFont(Assets.visFontTitles, 14);

		int xCity = xTitle + xCityShift;
		// int yCity = yTitle + yCityShift + yShiftCityConstant_CORDOBA;
		int yCity = yTitle + yCityShift + yShiftCityConstant_MADRID;

		pg.text(Assets.labelCurrentCity, xCity, yCity);
		float txtWidth = pg.textWidth(Assets.labelCurrentCity);// FIXME check
																// wether to
																// call the
																// function
																// textWidth on
																// PGraphics or
																// PApplet

		float dotShift = dotSize * 0.80f;
		pg.ellipse(xCity + txtWidth + dotShift, yCity + dotShift, dotSize,
				dotSize);
		int wLine = (int) (xTitle + xTitleShift - (xCity + dotShift + txtWidth));
		pg.rect(xCity + txtWidth + dotShift, yCity + dotSize * 0.75f, wLine, 3f);

		int xCityBar = xTitle + xTitleShift;
		int xCityText = (int) (xCityBar + avgCityPixels);
		pg.rect(xCityBar, yCity, avgCityPixels, Assets.avgCityThickness);
		pg.text(avgCityEuro + " " + Assets.labelCurrencySymbol, xCityText + 2,
				yCity);
		// strokeWeight(2.0);
		// VERTICAL LINE
		pg.rect(xCityText, yCity - 150, 2, 170);
		pg.popMatrix();

	}

	void displayProjectInfo(PGraphics pg) {// FIXME port to java
	// int xInfo = 0;
	// int yInfo = 0;
	// int xShift = 44;
	// pushMatrix();
	// textAlign(LEFT,TOP);
	// // shape(iconMobile, xInfo, yInfo);
	// fill(255);
	// textFont(fontProjectInfo);
	// text(projectInfo1,xInfo + xShift,yInfo - 15);
	// fill(255,255,0);
	// text(projectInfo2,xInfo + xShift,yInfo + 25);
	//
	// popMatrix();
	}

	void displayMobileIcon(PGraphics pg) {
		// int xInfo = 0;
		// int yInfo = -16; //CORDOBA
		// int xShift = 44;
		// pushMatrix();
		// shape(iconMobile, xInfo, yInfo);
		// popMatrix();
	}

	void displayProjectTitle(PGraphics pg) {
		// int xTitle = 20;
		// int yTitle = 20;
		// pushMatrix();
		// textFont(fontProjectTitle);
		// textAlign(RIGHT,TOP);
		// fill(255,255,0);
		// text(projectTitle,xTitle,yTitle);
		// textFont(fontProjectInfo);
		// fill(255);
		// text(projectSubtitle,xTitle,yTitle+50);
		// popMatrix();
	}

	void displayCityEnergy(PGraphics pg) {
		// pushMatrix();
		// colorMode(RGB,255);
		// energyCity.display(0, 2*PI);
		// float y = energyCity.getRad() * 0.5;
		// int l = 160;
		// line(0, y, 0, y + l);
		// fill(255);
		// textFont(fontTitles,g_fontSize_1);
		//
		// textAlign(LEFT);
		// // text(currentCity + " consuma: " +
		// nf(EnergyConstants.avgCityEnergy,3,2) + " kWh", 0, y + l + 15);
		// // text("Gasto: " + nf(avgCityEuro,2,2) +
		// " "+EnergyConstants.sCurrencySymbol, 0, y + l + 35);
		// //
		// text(currentCity + ": " + nf(avgCityEuro,2,2) +
		// " "+EnergyConstants.sCurrencySymbol, 0, y + l + 15);
		// text(nf(EnergyConstants.avgCityEnergy,3,2) + " kWh", 0, y + l + 35);
		// text(dataSourceTxt, 0, y + l + 95);
		//
		//
		// fill(255,255,0);
		// text(dataSourceLabel, 0, y + l + 75);
		//
		//
		// //text(currentCity + ": " + EnergyConstants.avgCityEnergy, 0, y + l +
		// 15);
		// popMatrix();

	}

	int getNumParticipatingDistricts() {
		return visMapDistricts.size();

	}

	void scaleCircle(float _factor) {
		// System.out.println("EnergyCircle: scaleCircle() " + _factor);

		Iterator<ArrayList<SunburstPiece>> i = visMapPiecesPerDistrict.values().iterator();
		while (i.hasNext()) {
			ArrayList<?> energyPieces = (ArrayList<?>) i.next();
			// -----------------------------------------------------Look at each
			// user in the district participating
			for (int p = 0; p < energyPieces.size(); p++) {
				SunburstPiece epa = (SunburstPiece) energyPieces.get(p);
				epa.scaleSpring(_factor);
			}
		}
		energyCity.scaleSpring(_factor);

		Assets.visMinRadius *= _factor;
		Assets.visMaxRadius *= _factor;

	}

	void highlightSpringCircle(int _id) {

	}

	void highlightEnergyPiece() {

	}

	// ----------------------------
	// Bounce existing energy piece with specified name
	void bounceEnergyPiece(String _name) {

	}

	// ----------------------------
	void bounceEnergyDistrict() {
	}

	// ----------------------------
	public void springCircle() {
		// println("EnergyCircle: springCircle()");

		Iterator<ArrayList<SunburstPiece>> i = visMapPiecesPerDistrict.values().iterator();
		while (i.hasNext()) {
			ArrayList<?> energyPieces = (ArrayList<?>) i.next();
			// Look at each user in the district participating
			for (int p = 0; p < energyPieces.size(); p++) {
				SunburstPiece epa = (SunburstPiece) energyPieces.get(p);
				epa.updateSpring(true);
			}
		}

		// static float m = millis();
		// println("bounceCircle() " + bBounceAll);
		// bBounceAll = true;
	}

	void toggleColor() {
		colorYellow = !colorYellow;
	}

	boolean isYellow() {
		return colorYellow;
	}

	void printme() {
		MainPApplet.mainUserData.printme();
	}

	public void keyPressed(int _key) {

		switch (_key) {
		case 'y':
			toggleColor();
			break;

		case 'd':
			debug = !debug;
			break;

		case 's': // SMALLER
			Assets.visScaleFactor = 0.9f;
			scaleCircle(Assets.visScaleFactor);

			// scale down only if does not go below MIN
			// FIXME move this code snippet to keyPressed of App
			if (Assets.visFontSize1 * Assets.visScaleFactor > Assets.visFontsizeMin1)
				Assets.visFontSize1 *= Assets.visScaleFactor;
			if (Assets.visFontSize2 * Assets.visScaleFactor > Assets.visFontsizeMin2)
				Assets.visFontSize2 *= Assets.visScaleFactor;
			break;

		case 'b': // BIGGER
			Assets.visScaleFactor = 1.1f;
			scaleCircle(Assets.visScaleFactor);

			Assets.visFontSize1 *= Assets.visScaleFactor;
			Assets.visFontSize2 *= Assets.visScaleFactor;

			break;

		case '2':
			springCircle();
			break;
		}
	}
}
// -----------------------------------------------------------------

