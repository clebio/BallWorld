package CSHBall;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.Observable;
import java.util.Observer;

public class Ball implements Observer {
	protected Point2D loc; // location, the position of the ball
	protected Point2D vel; // velocity
	protected float rad; // radius
	protected Color col; // color of the ball
	private static BallObservable ballObservable; // class member

	protected class BallObservable extends Observable {
		void setChangedAndNotify(Object arg) {
			setChanged();
			notifyObservers(arg);
		}
	}

	public void update(Observable o, Object b) {
		// TODO	
	}

	public Ball(Point2D loc, Point2D vel, float rad, Color col) {
		super();
		this.loc = loc;
		this.vel = vel;
		this.rad = rad;
		this.col = col;
		
		ballObservable = new BallObservable();
	    ballObservable.addObserver(this);
	}

   public void draw(Graphics g) {
	      g.setColor(col);
	      g.fillOval( (int)(loc.getX()-rad), (int)(loc.getY()-rad), (int)(2*rad), (int)(2*rad) );
   }

   public double getAngle() {
	   return Math.tan(vel.getY()/vel.getX());
   }

   /*
    * Move the ball by one time unit.  The ball moves in its current
    * direction for a number of pixels equal to its current speed.
    * That is, speed is given in terms of pixels per time unit.
    * Note:  The ball won't move at all if the width or height
    * of the rectangle is smaller than the ball's diameter.
    */
   public void travel() {
      travel(1.0);
   }
   
   /**
    * Move the ball for the specified number of time units.
    * The ball is restricted to the specified rectangle.
    * Note:  The ball won't move at all if the width or height
    * of the rectangle is smaller than the ball's diameter.
    */
   public void travel(double time) {
      double newx = loc.getX() + vel.getX()*time;
      double newy = loc.getY() + vel.getY()*time;
      
      /* We have the new values for x and y. */
      setLoc(new Point2D.Double(newx, newy));

      ballObservable.setChangedAndNotify(this);
   } // end travel()
	

   /**
    * Adjust the direction of motion of the ball so that it is
    * headed towards the point (a,b).  If the ball happens to
    * lie exactly at the point (a,b) already, this operation is
    * undefined, so nothing is done.
    */
   public void headTowards(double a, double b) {
      double vx = a - loc.getX();
      double vy = b - loc.getY();
      double dist = Math.sqrt(vx*vx + vy*vy);
      double dx = vel.getX();
	  double dy = vel.getY();
      if (dist > 0) {
          double speed = Math.sqrt(dx*dx + dy*dy);
         dx = vx / dist * speed;
         dy = vy / dist * speed;
      }
      this.setVel(new Point2D.Double(dx, dy));
   }
   
   public Rectangle2D getBounds() {
	   return new Rectangle2D.Float((float)loc.getX()-rad, (float)loc.getY()-rad, 2*rad, 2*rad);
   }
   
	public Point2D getLoc() {
		return loc;
	}

	public void setLoc(Point2D loc) {
		this.loc = loc;
	}

	public Point2D getVel() {
		return vel;
	}

	public void setVel(Point2D vel) {
		this.vel = vel;
	}

	public float getRad() {
		return rad;
	}

	public void setRad(float rad) {
		this.rad = rad;
	}

	public Color getCol() {
		return col;
	}

	public void setCol(Color col) {
		this.col = col;
	}
	
}
