package vis;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import scsd.MainPApplet;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import util.Assets;

public class SunburstDistrict {

	int id;
	String name;
	String code;
	int col;
	
	boolean drawLabels 	= false; 
	boolean drawArcDeco = true; 
	
	float sumExpenses; // gas + electo from DB in EURO
	float sumConsumption; // energy from DB in HKWH

	int numUsers;
	float avgExpRad; // gas + electro in PIXELS

	Vec2D arcPos;
	float barThickness = 13.f;

	// Springing Bars for Participation (which is a nonanimated value)
	Particle p1;
	Particle p2;
	VerletSpring2D spring;

	SunburstPiece lastParticipantInfo;

	float nameThickness; // thickness of the name of the district
	float codeThickness;

	boolean high = false;

	/*
	 * NOW ITS STATIC OUT SIDE float maxExpenseMoney = 200.f; float minRadius =
	 * 150.f; float maxRadius = 600.f;
	 */

	public SunburstDistrict() {

	}

	public SunburstDistrict(int _id, String _name, String _code, int _color,
			VerletPhysics2D _worldPhysics) {
		id = _id;
		name = _name;
		code = _code;
		// col = color(0xff00ffff);

		// ------------------------------------------------------
		p1 = new Particle(0, 0);
		p2 = new Particle(numUsers + 0.01f, 0f); // just a bit more so that we
													// can start calculating
		p1.lock();
		spring = new VerletSpring2D(p1, p2,
				PApplet.dist(p1.x, p1.y, p2.x, p2.y), 0.005f);

		_worldPhysics.addParticle(p1);
		_worldPhysics.addParticle(p2);
		_worldPhysics.addSpring(spring);
		// ------------------------------------------------------
		// col = color(random(0,255), random(0,255), random(0,255));

		col = _color; // set the color in hexadecimal

		nameThickness = MainPApplet.getInstance().textWidth(name);
		codeThickness = MainPApplet.getInstance().textWidth(code);
		//System.out.println(name + " " + nameThickness);

		// col = color(_colorInt);

		// avgExpenses = 0.f;
		// avgConsumption = 0.f;

		sumExpenses = 0.f;
		sumConsumption = 0.f;

		numUsers = 0;
		arcPos = new Vec2D();
	}

	void printme() {
		System.out.println("EnergyDistrict " + id + " " + name + " " + code
				+ " " + col + " sumExp " + sumExpenses + " sumCon "
				+ sumConsumption + " numUsers " + numUsers);
	}

	// avg this district vis, that a new user has added his values
	void notifyUpdate(float _newExp, float _newConsumption) {
		numUsers++; // update numUsers
		if (numUsers <= 0)
			return; // just in case

		// Update physics-------------------------
		// float scaleFactor = (oldNumUsers == 0) ? 1 : (float)numUsers /
		// (float)oldNumUsers; // avoid zero division
		// spring.setRestLength(spring.getRestLength() * scaleFactor);
		spring.setRestLength(numUsers);

		p2.x = numUsers;

		// Sum up energy costs-------------------------
		sumExpenses += _newExp;

		// Sum up energy consumption
		sumConsumption += _newConsumption;
		
//		System.out.println("SunburstDistrict::notifyUpdate(): _newExp = " + _newExp + " _newConsumption = "
//				+ _newConsumption + " numUsers = " + numUsers);

		// printme();
	}

	/*
	 * void displayArc(float _beginAngle, float _endAngle){ noFill();
	 * stroke(col); strokeWeight(g_arcThickness); //Vec2D arcPos=new Vec2D(avg *
	 * 0.5, lastNumPieces*angleUser).toCartesian(); // the end pt of the avg
	 * arcPos.set(avgExpenses * 0.5, _endAngle).toCartesian();
	 * 
	 * line(0,0, arcPos.x, arcPos.y); arc(0,0, avgExpenses, avgExpenses,
	 * _beginAngle, _endAngle);
	 * 
	 * //_beginAngle = lastAng-lastNumPieces*angleUser //_endAngle = _endAngle
	 * 
	 * // //Vec2D arcPos=new Vec2D(avgArc * 0.5, lastAng -
	 * lastNumPieces*angleUser).toCartesian(); // the end pt of the avg //
	 * line(0,0,arcPos.x,arcPos.y); // arc(0,0,avgArc,avgArc,lastAng -
	 * lastNumPieces*angleUser, lastAng); // }
	 */

	void displayArc(PGraphics pg, float _beginAngle, float _endAngle, float _avgArcRadius) {
		
		pg.pushMatrix();//------------------------------------START DRAWING DISTRICT ARC 
			//pg.fill(col);
			if ((MainPApplet.getInstance()).dbCom != null && (MainPApplet.getInstance()).dbCom.getHeart() == 0){
				pg.fill(col);
			}
			else {
				pg.noFill();
				pg.stroke(col);
				//pg.stroke(255);
				pg.strokeWeight(Assets.visDistrictArcThickness);
			}
			// TRICKY: DONT DELETE THIS COMMENTS
			// -----------------------------------------------
			// MAP values that come in EURO (from users) to values in PIXELS (for the vis)
			// The MIN/MAX values for EURO/PIXELS should be the SAME as for EnergyPiece;
			// avgExpRad = map(avgExpenses, 0, maxExpenseMoney, minRadius,maxRadius);
			// ATTENTION:
			// Even though avgExpRad is eventually the same as _avgArcRadius, the input parameter is animated from the physics
			// so we use it for drawing!
			// -----------------------------------------------
			avgExpRad = _avgArcRadius;
			arcPos.set(_avgArcRadius * 0.5f, _beginAngle).toCartesian();
			
			if (Assets.visDistrictArcSmooth){
				pg.smooth();
				pg.arc(0, 0, _avgArcRadius * 1.f, _avgArcRadius * 1.f, _beginAngle,_endAngle);
				pg.noSmooth();
			}
			else 
				pg.arc(0, 0, _avgArcRadius * 1.f, _avgArcRadius * 1.f, _beginAngle,_endAngle);
			

			//------------------------------------------------draw the ending decoration of the arc 
			Vec2D endDir 	= new Vec2D();
			endDir.set(1, _endAngle).toCartesian();
			Vec2D endPos1 	= new Vec2D(endDir);
			endPos1.y 		*= _avgArcRadius * 0.5;
			
			
			
            if (Assets.visDistrictArcDecoLineDraw){
				//pg.line(endPos1.x, endPos1.y, endPos1.x + endDir.x * 50, endPos1.y + endDir.y * 50);
				
				pg.line(_avgArcRadius, 0.f, _avgArcRadius * 0.5f, 0.f);
				
			}
		pg.popMatrix(); //------------------------------------END DRAW DISTRICT ARC  

		if (drawLabels) {
			pg.pushMatrix();//--------------------------------START DRAWING LABELS 
			
			//------------------------------------------------ALIGN TEXT DRAWING ACCORDING TO THE SIDE OF THE CIRCLE 
			pg.rectMode(PConstants.CORNER);
			float deg = PApplet.degrees(_endAngle);
			if (deg > 90.0 && deg < 270.0) // left part of circle
				pg.textAlign(PConstants.RIGHT);
			else
				pg.textAlign(PConstants.LEFT); // right part of circle

			//------------------------------------------------DRAW DISTRICT NAME 
			float avg 			= getAvgExpensesEuro();
			String arcLabelTxt 	= code + ": " + PApplet.nf(avg, 2, 2) + " " + Assets.labelCurrencySymbol;
			pg.fill(col);
			pg.textFont(Assets.visFontLegend, Assets.visFontSize1);
			pg.text(arcLabelTxt, endPos1.x + endDir.x * 60, endPos1.y + endDir.y * 60);
			
			//------------------------------------------------DRAW LAST PARTICIPANT NAME 
			String tmpStr = lastParticipantInfo.name;
			if (tmpStr.length() > 0) {
				pg.pushMatrix();//----------------------------start drawing name
				pg.fill(255);
				float scaleF = 1.0f;
				if (!lastParticipantInfo.bActive) {//---------draw it big only if active
					scaleF = 0.6f;
					pg.textFont(Assets.visFontLastUser, Assets.visFontSize2 * scaleF);
					pg.text(tmpStr, endPos1.x + endDir.x * 60, endPos1.y + endDir.y * 60 + 20);
				} else {
					float expense = (lastParticipantInfo.electro + lastParticipantInfo.gas);
					// ""+ nf(,2,2);
					tmpStr = "Tu: " + tmpStr + " " + PApplet.nf(expense, 2, 2) + " " + Assets.labelCurrencySymbol;
					pg.textFont(Assets.visFontLastUser, Assets.visFontSize2 * scaleF);
					pg.text(tmpStr, endPos1.x + endDir.x * 60, endPos1.y + endDir.y * 60 + 25);
				} //------------------------------------------end if (!lastParticipantInfo.bActive)
				pg.popMatrix();//end drawing name
			}//-----------------------------------------------end if (tmpStr.length() > 0)
			
			pg.popMatrix();//---------------------------------END DRAWING LABELS
		}//---------------------------------------------------end if(drawLabels)
	}

	int getCol() {
		return col;
	}

	int getNumUsers() {
		return numUsers;
	}

	float getNumUsersPhysics() {
		return PApplet.dist(p1.x, p1.y, p2.x, p2.y);
	}

	float getAvgExpensesEuro() {
		return (numUsers > 0f) ? sumExpenses / numUsers : 0.0f;
	}

	float getAvgConsumption() {
		return (numUsers > 0f) ? sumConsumption / numUsers : 0.0f;
	}

	float getAvgExpensesPix() {
		return avgExpRad;
	}

}
