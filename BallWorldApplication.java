import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import CSHBall.Ball;

public class BallWorldApplication extends JPanel {

	private static int BALL_COUNT = 17;
	private static int win_width = 1000;
	private static int win_height = 800;
	private static float BALL_MAX_SPEED = 10;
	private static int BALL_MAX_RADIUS = 37;
	private static ArrayList<Ball> balls;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Ball b : balls)
			b.draw(g);
	}

	public static void main(String[] args) {
		BallWorldApplication content = new BallWorldApplication();
		content.addMouseListener(new RepaintOnClick());
		content.addMouseMotionListener(new RepaintOnClick());

		JFrame window = new JFrame("Badunka Dunk Funka Funk");
		window.addComponentListener(new WindowListener());
		window.addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent we) {
			    System.exit(0);
			  }
			});
		window.setContentPane(content);
		window.setSize(win_width, win_height);
		window.setLocation(100, 100);
		window.setVisible(true);
	}

	private int randSign() {
		if (Math.random() > 0.5) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public BallWorldApplication() {
		super();
		
		this.setBounds(new Rectangle(0, 0, win_width, win_height));
		setBackground(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));

		balls = new ArrayList<Ball>();
		for (int i = 0; i < BALL_COUNT; i++) {
			balls.add(new Ball(
					new Point2D.Double(this.getWidth() * Math.random(), this.getHeight() * Math.random()), 
					new Point2D.Double(randSign() * BALL_MAX_SPEED * Math.random(), randSign() * BALL_MAX_SPEED * Math.random()), 
					BALL_MAX_RADIUS * (float)Math.random(), 
					new Color((float)Math.random(), (float)Math.random(), (float)Math.random())));
		}

		RepaintAction action = new RepaintAction();
		Timer timer = new Timer(50, action);
		timer.start();
	}

	private class RepaintAction implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			for (Ball b : balls)
				b.travel();
			repaint();
		}
	}

	private static class RepaintOnClick implements MouseListener,
			MouseMotionListener {
		private Point2D startP = new Point2D.Float(0,0); // the start position of the dragging event
		private Point2D currentP = new Point2D.Float(0,0); // the current position of the dragging event
		private Point2D delta = new Point2D.Float(0, 0); // the dx, dy delta from dragging the mouse
				
		public void mouseClicked(MouseEvent e) {			
			for (Ball b : balls)
				b.headTowards(e.getX(), e.getY());
			Component source = (Component) e.getSource();
			source.repaint();
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			startP.setLocation(e.getPoint());
		}

		public void mouseReleased(MouseEvent e) {
			Point2D p = e.getPoint();
			delta.setLocation(p.getX()-startP.getX(), p.getY()-startP.getY());
			double MIN_DRAW_DIST = 10;
			double distance = Math.sqrt(delta.getX()*delta.getX() + delta.getY()*delta.getY());
			
			if (distance > MIN_DRAW_DIST) {
			balls.add(new Ball(
					e.getPoint(),
					new Point2D.Double(Math.signum(delta.getX()) * BALL_MAX_SPEED % delta.getX(), 
							Math.signum(delta.getY()) * BALL_MAX_SPEED % delta.getY()),
					BALL_MAX_RADIUS * (float)Math.random(), 
					new Color((float)Math.random(), (float)Math.random(), (float)Math.random())));
			}
// System.out.printf("dx,dy = %3.0f,%3.0f\n", delta.getX(), delta.getY());
		}

		public void mouseDragged(MouseEvent e) {
/*			for (Ball b : balls)
				b.headTowards(e.getX(), e.getY());
*/		
			for (Ball b : balls)
				if (b.getBounds().contains(e.getPoint()))
					b.setLoc(e.getPoint());
						
			currentP.setLocation(e.getX(), e.getY());
			Component source = (Component) e.getSource();	
			source.repaint();
			
		}

		public void mouseMoved(MouseEvent e) {
		}
	}

	public static class WindowListener implements ComponentListener {
		public void componentHidden(ComponentEvent arg0) {
		}

		public void componentMoved(ComponentEvent arg0) {
		}

		public void componentResized(ComponentEvent e) {
			// TODO deal with component resizing
		}

		public void componentShown(ComponentEvent arg0) {
		}
	}
}
