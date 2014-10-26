package facade;


import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import util.Assets;

public class Geometry {
	
	Points points; 

	static boolean printed 	= false; 
    
	public Geometry(Points points) {
		this.points = points; 
	}
	
			
	/** Draws the polygon pieces, which represent the unfoldiing of the FIESP building sides.
	 * The polygon points are determined by the explicit points class, which in turn is defined by the dimensions of the facade 
	 * TODO: make each piece a different object and draw it separately 
	 * @param pgScaleFact */
	public void drawFacadeUnfold(PGraphics pg, float pgScaleFact, int strokeColor, int fillColor) {
		if (points == null) {
			System.out.println("Invalid points");
			return; 
		}
		if (pg == null) {
			System.out.println("Invalid pgraphics");
			return; 	
		}	
		boolean drawLeft	= true; 
		boolean drawFront	= true; 
		boolean drawRight	= true; 
		boolean drawPGRect = true; 
		
		pg.beginDraw();//always start with beginDraw when dealing with pg's 
		pg.background(Assets.maskBGColor);
		pg.hint(PConstants.ENABLE_DEPTH_TEST);
		
		
				
		//----------------draw polygons 
		pg.pushMatrix(); 
		
		//----------------------slightly scale-down so we avoid z-fighting btw. the polygons and the borders of the pgraphics tex
		//FIXME scaleFact should be a field 
		float deltaWidth 	= pg.width * ((pgScaleFact - 1)/pgScaleFact);		
		float deltaHeight 	= pg.height * ((pgScaleFact - 1)/pgScaleFact);
//		float halfWidth 	= pg.width * 0.5f; 
//		float halfHeight	= pg.height * 0.5f; 
		
//		pg.translate(halfWidth, halfHeight); 		//move to center
//		pg.scale(1/pgScaleFact, 1/pgScaleFact);	 	//scale down
//		pg.translate(-halfWidth, -halfHeight); 		//move back to corner
		pg.translate(deltaWidth * 0.5f, deltaHeight * 0.5f);
		//-------------------------------------------------------------------------------------------------
				
		
		//----------------------look&feel
		if (fillColor == -1)
			pg.noFill();
		else pg.fill(fillColor);
		
		if (strokeColor == -1)
			pg.noStroke();
		else {
			pg.strokeWeight(Assets.maskStrokeWeight);
			pg.stroke(strokeColor);
		}
		//----------------draw LEFT: (p1, p2, p9, p10, p1)
		if (drawLeft){
			pg.beginShape(); 
		      pg.vertex((float)points.p1.x, (float)points.p1.y, 0.f);
		      pg.vertex((float)points.p2.x, (float)points.p2.y, 0.f);
		      pg.vertex((float)points.p9.x, (float)points.p9.y, 0.f);
		      pg.vertex((float)points.p10.x, (float)points.p10.y, 0.f);
		      pg.vertex((float)points.p1.x, (float)points.p1.y, 0.f);
		    pg.endShape(); 
		}

	    //----------------draw FRONT: (p2, p3, p7, p8, p2) /same as for the textureUnfold 
	    if (drawFront){
		    pg.beginShape();
		      pg.vertex((float)points.p2.x, (float)points.p2.y, 0f);
		      pg.vertex((float)points.p3.x, (float)points.p3.y, 0f);
		      pg.vertex((float)points.p7.x, (float)points.p7.y, 0f);
		      pg.vertex((float)points.p8.x, (float)points.p8.y, 0f);
		      pg.vertex((float)points.p2.x, (float)points.p2.y, 0f);
		    pg.endShape();
	    }

	    //----------------draw RIGHT: (p3, p4, p5, p6, p3)
	    if (drawRight){
		  	pg.beginShape();
		  	  pg.vertex((float)points.p3.x, (float)points.p3.y, 0f);
		      pg.vertex((float)points.p4.x, (float)points.p4.y, 0f);
		      pg.vertex((float)points.p5.x, (float)points.p5.y, 0f);
		      pg.vertex((float)points.p6.x, (float)points.p6.y, 0f);
		      pg.vertex((float)points.p3.x, (float)points.p3.y, 0f);
		    pg.endShape();
//		    if (!printed) {
//		    	System.out.println((float)points.p3.x +","+ (float)points.p3.y);
//		    	System.out.println((float)points.p4.x +","+ (float)points.p4.y);
//		    	printed = true; 
//		    }
	    }

		pg.popMatrix(); 
		
		//----------------draw a rect with the size of the pg for debugging 
		if (drawPGRect){
			pg.noFill();
			pg.stroke(strokeColor);
			pg.rect(0, 0, pg.width, pg.height);
		}
				
		pg.hint(PConstants.DISABLE_DEPTH_TEST);
	    pg.endDraw();
		
	}
	
	/** Draws the polygon pieces, along which the texture (animation) projected onto the FIESP building has to be cut.
	 * The polygon points are determined by the explicit points class, which in turn is defined by the dimensions of the facade 
	 * TODO: make each piece a different object and draw it separately 
	 * @param pgScaleFact */
	public void drawTextureUnfold(PGraphics pg, PImage tex, float pgScaleFact, boolean rotateSides, int strokeColor, int fillColor) {
		if (points == null) {
			System.out.println("Invalid points");
			return; 
		}
		if (pg == null) {
			System.out.println("Invalid pgraphics");
			return; 	
		}
		double mapWidth 	= 0;
		double mapHeight 	= 0;
		double mapTopFront  = 0;
		double mapBaseLeft  = 0;
		double mapBaseFront = 0;
		double mapTopLeft   = 0;
		
		//declare the (u,v) tex coordinates which correspond to the points (same numbering)  
		double u1 = 0, v1 = 0, u2 = 0, v2 = 0, u3 = 0, v3 = 0, u4 = 0, v4 = 0, u5 = 0, v5 = 0, u7 = 0, v7 = 0, u8 = 0, v8 = 0, u10 = 0, v10 = 0; 
		
		if (tex != null) {
			mapWidth 	= points.p1.distance(points.p4);
			mapHeight 	= points.p1.distance(points.p10);
			mapTopFront  = points.p8.distance(points.p7);
			mapBaseLeft  = points.p1.distance(points.p2);
			mapBaseFront = points.p2.distance(points.p3);
			mapTopLeft   = points.p10.distance(points.p8);
			//--------------calc u-coordinates 
			u1 = (tex.width - mapWidth) / 2.0;
			u2 = u1 + mapBaseLeft; 
			u3 = u2 + mapBaseFront; 
			u4 = u1 + mapWidth; 
			u10 = u1; 
			u8  = u10 + mapTopLeft;   
			u7  = u8 + mapTopFront;
			u5  = u4;
			//--------------calc v-coordinates 
			v10 = (tex.height - mapHeight) / 2.0; 
			v8  = v10; 
			v7  = v10; 
			v5  = v10; 	
			
			v1 = v10 + mapHeight; 
			v2 = v1; 
			v3 = v1; 
			v4 = v1;
		}
		
		if (!printed && tex !=null){
			System.out.println("mapWidth " + mapWidth);
			System.out.println("mapHeight " + mapHeight);
			System.out.println("mapBaseLeft " + mapBaseLeft);
			System.out.println("mapBaseFront " + mapBaseFront);
			System.out.println("mapTopLeft " + mapTopLeft);
			System.out.println("tex.height " + tex.height);
			System.out.println("v1: " + v1 + " v10: " + v10);
			printed = true; 
		}
		
		
		boolean drawLeft	= true; 
		boolean drawFront	= true; 
		boolean drawRight	= true; 
		boolean drawPGRect = false; 
		
		pg.beginDraw();//always start with beginDraw when dealing with pg's 
		pg.background(Assets.maskBGColor);
		//pg.hint(PConstants.ENABLE_DEPTH_TEST);

		//----------------draw a rect with the size of the pg for debugging 
		if (drawPGRect){
			pg.fill(240, 100);
			pg.noStroke();
			pg.rect(0, 0, pg.width, pg.height);
		}
		
		//----------------start drawing polygons 
		pg.pushMatrix();
		
		//----------------------slightly scale-down so we avoid z-fighting btw. the polygons and the borders of the pgraphics tex
		float deltaWidth 	= pg.width * ((pgScaleFact - 1)/pgScaleFact);		
		float deltaHeight 	= pg.height * ((pgScaleFact - 1)/pgScaleFact);
//		float halfWidth 	= pg.width * 0.5f; 
//		float halfHeight	= pg.height * 0.5f; 
		
//		pg.translate(halfWidth, halfHeight); //move to center
//		pg.scale(1/pgScaleFact, 1/pgScaleFact);	 //scale down
//		pg.translate(-halfWidth, -halfHeight); //move back to corner
		pg.translate(deltaWidth * 0.5f, deltaHeight * 0.5f);
		//-------------------------------------------------------------------------------------------------
		
		if (fillColor == -1)
			pg.noFill();
		else pg.fill(fillColor);
		
		if (strokeColor == -1){
			pg.noStroke();
		} else {
			pg.strokeWeight(Assets.maskStrokeWeight);
			pg.stroke(strokeColor);
		}
		//----------------draw LEFT: (p1, p2, p8, p10, p1)
		if (drawLeft){
			pg.pushMatrix();
			if (rotateSides){
				pg.translate((float)points.p2.x, (float)points.p2.y);
				pg.rotate((float)points.angleLeft);
				pg.translate(-(float)points.p2.x, -(float)points.p2.y);
			}
			if (tex != null){
				pg.beginShape(); 
				  pg.texture(tex);
				  pg.vertex((float)points.p1.x, (float)points.p1.y, 0.1f, (float)u1, (float)v1);
			      pg.vertex((float)points.p2.x, (float)points.p2.y, 0.1f, (float)u2, (float)v2);
			      pg.vertex((float)points.p8.x, (float)points.p8.y, 0.1f, (float)u8, (float)v8);
			      pg.vertex((float)points.p10.x, (float)points.p10.y, 0.1f, (float)u10, (float)v10);
			      pg.vertex((float)points.p1.x, (float)points.p1.y, 0.1f, (float)u1, (float)v1);
			    pg.endShape(); 
			} else {
				pg.beginShape(); 
			      pg.vertex((float)points.p1.x, (float)points.p1.y, 0.1f);
			      pg.vertex((float)points.p2.x, (float)points.p2.y, 0.1f);
			      pg.vertex((float)points.p8.x, (float)points.p8.y, 0.1f);
			      pg.vertex((float)points.p10.x, (float)points.p10.y, 0.1f);
			      pg.vertex((float)points.p1.x, (float)points.p1.y, 0.1f);
			    pg.endShape();
			}
		    
		    pg.popMatrix();
		}

	    //----------------draw FRONT: (p2, p3, p7, p8, p2)
	    if (drawFront){
	    	if(tex != null){
			    pg.beginShape();
			      pg.texture(tex);
			      pg.vertex((float)points.p2.x, (float)points.p2.y, 0f, (float)u2, (float)v2);
			      pg.vertex((float)points.p3.x, (float)points.p3.y, 0f, (float)u3, (float)v3);
			      pg.vertex((float)points.p7.x, (float)points.p7.y, 0f, (float)u7, (float)v7);
			      pg.vertex((float)points.p8.x, (float)points.p8.y, 0f, (float)u8, (float)v8);
			      pg.vertex((float)points.p2.x, (float)points.p2.y, 0f, (float)u2, (float)v2);
			    pg.endShape();
	    	} else {
	    		pg.beginShape();
			      pg.vertex((float)points.p2.x, (float)points.p2.y, 0f);
			      pg.vertex((float)points.p3.x, (float)points.p3.y, 0f);
			      pg.vertex((float)points.p7.x, (float)points.p7.y, 0f);
			      pg.vertex((float)points.p8.x, (float)points.p8.y, 0f);
			      pg.vertex((float)points.p2.x, (float)points.p2.y, 0f);
			    pg.endShape();
	    	}
	    }

	    //----------------draw RIGHT: (p3, p4, p5, p7, p3)
	    if (drawRight){
	    	pg.pushMatrix();
	    	if (rotateSides){
				pg.translate((float)points.p3.x, (float)points.p3.y);
				pg.rotate(-(float)points.angleRight);
				pg.translate(-(float)points.p3.x, -(float)points.p3.y);
			}
	    	if (tex != null){
			  	pg.beginShape();
			  	  pg.texture(tex);
			  	  pg.vertex((float)points.p3.x, (float)points.p3.y, 0f, (float)u3, (float)v3);
			      pg.vertex((float)points.p4.x, (float)points.p4.y, 0f, (float)u4, (float)v4);
			      pg.vertex((float)points.p5.x, (float)points.p5.y, 0f, (float)u5, (float)v5);
			      pg.vertex((float)points.p7.x, (float)points.p7.y, 0f, (float)u7, (float)v7);
			      pg.vertex((float)points.p3.x, (float)points.p3.y, 0f, (float)u3, (float)v3);
			    pg.endShape();
	    	} else {
	    		pg.beginShape();
			  	  pg.vertex((float)points.p3.x, (float)points.p3.y, 0f);
			      pg.vertex((float)points.p4.x, (float)points.p4.y, 0f);
			      pg.vertex((float)points.p5.x, (float)points.p5.y, 0f);
			      pg.vertex((float)points.p7.x, (float)points.p7.y, 0f);
			      pg.vertex((float)points.p3.x, (float)points.p3.y, 0f);
			    pg.endShape();
	    	}
//		    if (!printed) {
//		    	System.out.println((float)points.p3.x +","+ (float)points.p3.y);
//		    	System.out.println((float)points.p4.x +","+ (float)points.p4.y);
//		    	printed = true; 
//		    }
		    pg.popMatrix();
	    }

	    pg.popMatrix();
	    
		//pg.hint(PConstants.DISABLE_DEPTH_TEST);
	    pg.endDraw();
	}

	


	private void drawTemplate(PGraphics pg){
		if (points == null) {
			System.out.println("Invalid points");
			return; 
		}
		if (pg == null) {
			System.out.println("Invalid pgraphics");
			return; 	
		}	
		boolean drawLeft	= true; 
		boolean drawFront	= true; 
		boolean drawRight	= true; 
		boolean drawPGRect = true; 
		
		pg.beginDraw();//always start with beginDraw when dealing with pg's 
		pg.hint(PConstants.ENABLE_DEPTH_TEST);
		
		//----------------draw a rect with the size of the pg for debugging 
		if (drawPGRect){
			pg.fill(240);
			pg.noStroke();
			pg.rect(0, 0, pg.width, pg.height);
		}
				
		pg.pushMatrix(); 
		pg.popMatrix(); 
		
		pg.hint(PConstants.DISABLE_DEPTH_TEST);
	    pg.endDraw();
	}
	 
}
