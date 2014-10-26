package facade;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

//import javax.vecmath.Point2D.Double;

public class Points {

	//////////////////////////////////////////////////////////////////////////////////////
	// Singleton 
	//////////////////////////////////////////////////////////////////////////////////////
	public static Points getInstance() {
		return singleton;
	}
	
	private static Points singleton = new Points();

	private Points() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	public Point2D.Double p1 = new Point2D.Double();
	public Point2D.Double p2 = new Point2D.Double();
	public Point2D.Double p3 = new Point2D.Double();
	public Point2D.Double p4 = new Point2D.Double();
	public Point2D.Double p5 = new Point2D.Double();
	public Point2D.Double p6 = new Point2D.Double();
	public Point2D.Double p7 = new Point2D.Double();
	public Point2D.Double p8 = new Point2D.Double();
	public Point2D.Double p9 = new Point2D.Double();
	public Point2D.Double p10 = new Point2D.Double();

	Line2D.Double leftLine1 = new Line2D.Double();   
	Line2D.Double leftLine2 = new Line2D.Double();     
	
	Line2D.Double rightLine1= new Line2D.Double();      
	Line2D.Double rightLine2= new Line2D.Double();      
	
	public double angleLeft  = 0.0; 
	public double angleRight = 0.0; 
	
	//////////////////////////////////////////////////////////////////////////////////////
	//Texture coordinates 
	public Point2D.Double p1tex  = new Point2D.Double();
	public Point2D.Double p2tex  = new Point2D.Double();
	public Point2D.Double p3tex  = new Point2D.Double();
	public Point2D.Double p4tex  = new Point2D.Double();
	public Point2D.Double p5tex  = new Point2D.Double();
	public Point2D.Double p6tex  = new Point2D.Double();
	public Point2D.Double p7tex  = new Point2D.Double();
	public Point2D.Double p8tex  = new Point2D.Double();
	public Point2D.Double p9tex  = new Point2D.Double();
	public Point2D.Double p10tex = new Point2D.Double();
	
	
	/** The pts are calculated according to the given dimension (measures) of the facade 
	 *  Each change in the dimensions evokes an update and re-calculation of the pts */
	public void update(Dimensions fd) {
		p1.x = 0;
		p1.y = fd.totalHeight;
		
		p2.x = fd.baseLeft;
		p2.y = fd.totalHeight;
		
		p3.x = fd.baseLeft + fd.baseFront;
		p3.y = fd.totalHeight;
		
		p4.x = fd.totalWidth;
		p4.y = fd.totalHeight;
		
		p5.x = fd.totalWidth;
		p5.y = 0;
		p6.x = fd.totalWidth - fd.topRight;
		p6.y = 0;
		p7.x = fd.totalWidth - fd.topRight - fd.invisibleBaseRight;
		p7.y = 0;
		p8.x = fd.topLeft + fd.invisibleBaseLeft;
		p8.y = 0;
		p9.x = fd.topLeft;
		p9.y = 0;
		p10.x = 0;
		p10.y = 0;
		
		
		//----------------update lines to calc angles 
		leftLine1.setLine(p2, p9);
		leftLine2.setLine(p2, p8);
		
		rightLine1.setLine(p3, p7);
		rightLine2.setLine(p3, p6);
		
		//----------------calc angles btw lines 
		angleLeft  = Points.angleBetween2Lines(leftLine1, leftLine2);
		angleRight = Points.angleBetween2Lines(rightLine1, rightLine2);
		
		System.out.println("angleLeft " + angleLeft + ", angleRight " + angleRight);
		System.out.println("angleLeft " + angleLeft + ", angleRight " + angleRight);
		
	}
	
	 public static double angleBetween2Lines(Line2D line1, Line2D line2){
	        double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
	                                   line1.getX1() - line1.getX2());
	        double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
	                                   line2.getX1() - line2.getX2());
	        return angle1-angle2;
	 }
}
