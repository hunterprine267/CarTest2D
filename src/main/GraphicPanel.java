package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public int screenWidth = 1024;//1920;
	public int screenHeight = 768;//1080;
	
	public int FPS = 60;
	public double activeFPS;
	
	public int mouseX;
	public int mouseY;
	
	Thread gThread;
	
	public KeyHandler keyH = new KeyHandler();
	
	ArrayList<Car> cars = new ArrayList<Car>();
	Player player = new Player(this, keyH, 0, 0, 2, 2, -90);
	
	public double cameraX = screenWidth/2;
	public double cameraY = screenHeight/2;
	
	public GraphicPanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		this.addKeyListener(keyH);
		
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		
		cars.add(new Car(this, keyH, -100, 0, 0, 0, .1, .1, 9, 8, 26, 48, -90, new Color(150,130,90), null));
		cars.add(new Car(this, keyH, 100, 0, 0, 0, .2, .1, 9, 4.5, 26, 48, -90, new Color(130,150,90), null));
	}
	
	public void startGThread() {
		
		gThread = new Thread(this);
		gThread.start();
	}
	
	@Override @SuppressWarnings("unused")
	public void run() {

		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		long drawCount = 0;
		
		while (gThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				activeFPS = drawCount;
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	public void update() {
		
		for (Car car : cars) {
			car.update();
		}
		
		player.update();
		
		cameraX -= (cameraX + player.getX() - screenWidth/2)/25;
		cameraY -= (cameraY + player.getY() - screenHeight/2)/25;
		
		//cameraX = -player.getX() + screenWidth/2;
		//nscameraY = -player.getY() + screenHeight/2;
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform origTransform = g2.getTransform();
		
		g2.setColor(Color.black);
		g2.fillRect(0, 0, screenWidth, screenHeight);
		g2.translate(cameraX, cameraY);
		
		for (int x = -500; x < 500; x++) {
			for (int y = -1000; y < 1000; y++) {
				if (x*30 > cameraX-screenWidth-10 && x*30 < cameraX+10 && y*30 > cameraY-screenHeight-10 && y*30 < cameraY+10) {
					if (y % 2 == 0 && x % 2 == 0) {
						g2.setColor(Color.gray);
					} else {
						g2.setColor(Color.darkGray);
					}
					
					g2.fillRect(-x*30-1, -y*30-1, 2, 2);
				}
			}
		}
		
		for (Car car : cars) {
			car.draw(g2);
		}
		
		player.draw(g2);

		g2.setTransform(origTransform);
		
		int drawX = screenWidth - 120;
		int drawY = screenHeight - 20;
		
		if (player.car != null) {
			g2.setColor(Color.red);
			g2.drawLine(drawX, drawY, round(drawX + Math.cos(Math.toRadians(Math.abs(player.car.getForwardVel())/player.car.getSpeedLimit() * 135 - 157.5)) * 90),
									  round(drawY + Math.sin(Math.toRadians(Math.abs(player.car.getForwardVel())/player.car.getSpeedLimit() * 135 - 157.5)) * 90));

			g2.setColor(Color.gray);
			g2.drawLine(drawX, drawY, round(drawX + Math.cos(Math.toRadians(-160)) * 100),
					  				  round(drawY + Math.sin(Math.toRadians(-160)) * 100));
			g2.drawLine(drawX, drawY, round(drawX + Math.cos(Math.toRadians(-21)) * 100),
									  round(drawY + Math.sin(Math.toRadians(-21)) * 100));
			
			g2.drawArc(drawX - 100, drawY - 100, 200, 200, 160, -138);
		}
		
		g2.setColor(Color.white);
		g2.drawString(String.valueOf(activeFPS), 1, 11);
		
		g2.dispose();
	}
	
	public int round(double num) {
		return (int) Math.round(num);
	}
	
	public Point rotatePoint(double centerX, double centerY, double pointX, double pointY, double angle) {
		
		angle = Math.toRadians(angle);
		
		int newX = round(centerX + (pointX-centerX)*Math.cos(angle) - (pointY-centerY)*Math.sin(angle));
		int newY = round(centerY + (pointX-centerX)*Math.sin(angle) + (pointY-centerY)*Math.cos(angle));
		
		return new Point(newX, newY);
	}
}

