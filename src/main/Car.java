package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class Car {

	GraphicPanel gp;
	KeyHandler keyH;
	
	private double x, y;
	private double forwardVel;
	private double turnVel;
	private double forwardAcc;
	private double turnAcc;
	private int width, height;
	private double angle;
	private Color color;
	
	Point[] hitboxPoints = new Point[4];
	
	Polygon hitbox;
	
	Area hitboxArea;
	
	private double centerX = x + (Math.cos(Math.toRadians(angle)) * height*.2);
	private double centerY = y - height * .2 + (Math.sin(Math.toRadians(angle)) * height*.2);
	
	public Player player;
	
	private double speedLimit;
	private double turnLimit;
	
	public Car(GraphicPanel gp, KeyHandler keyH, double x, double y, double forwardVel, double turnVel, double forwardAcc, double turnAcc, double speedLimit, double turnLimit, int width, int height, double angle, Color color, Player player) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		this.x = x;
		this.y = y;
		this.forwardVel = forwardVel;
		this.turnVel = turnVel;
		this.forwardAcc = forwardAcc;
		this.turnAcc = turnAcc;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.color = color;
		
		this.player = player;
		
		this.speedLimit = speedLimit;
		this.turnLimit = turnLimit;
		
		this.hitboxPoints[0] = new Point(gp.round(centerX + height/2),gp.round(centerY - width/2));
		this.hitboxPoints[1] = new Point(gp.round(centerX - height/2),gp.round(centerY - width/2));
		this.hitboxPoints[2] = new Point(gp.round(centerX - height/2),gp.round(centerY + width/2));
		this.hitboxPoints[3] = new Point(gp.round(centerX + height/2),gp.round(centerY + width/2));
		
		setHitbox();
	}
	
	public void draw(Graphics2D g2) {
		AffineTransform origTransform = g2.getTransform();
		
		g2.rotate(Math.toRadians(angle-90), centerX, centerY);
		
		g2.setColor(Color.darkGray);
		
		g2.drawOval(gp.round(centerX) - width, gp.round(centerY) - width, width*2, width*2);
		g2.setTransform(origTransform);
		
		g2.rotate(Math.toRadians(angle-90), x, y - height * .2);
		AffineTransform carTransform = g2.getTransform();
		
		Dimension wheel = new Dimension(4, 10);
		
		g2.rotate(Math.toRadians(turnVel*5), (x+width/2), (y+height/3));
		g2.fillRect(gp.round(x-wheel.getWidth()/2+width/2), gp.round(y-wheel.getHeight()/2+height/3), gp.round(wheel.getWidth()), gp.round(wheel.getHeight()));
		
		g2.setTransform(carTransform);
		g2.rotate(Math.toRadians(turnVel*5), (x-width/2), (y+height/3));
		g2.fillRect(gp.round(x-wheel.getWidth()/2-width/2), gp.round(y-wheel.getHeight()/2+height/3), gp.round(wheel.getWidth()), gp.round(wheel.getHeight()));
		
		g2.setTransform(carTransform);
		g2.fillRect(gp.round(x-wheel.getWidth()/2+width/2), gp.round(y-wheel.getHeight()/2-height/3), gp.round(wheel.getWidth()), gp.round(wheel.getHeight()));
		
		g2.fillRect(gp.round(x-wheel.getWidth()/2-width/2), gp.round(y-wheel.getHeight()/2-height/3), gp.round(wheel.getWidth()), gp.round(wheel.getHeight()));
		
		g2.translate(-width/2, height/2);
		
		g2.setColor(color);
		g2.fillRect(gp.round(x), gp.round(y-height), width, height);
		g2.setColor(color.darker());
		g2.fillRect(gp.round(x), gp.round(y-height*.8), width, gp.round(height*.4));
		g2.fillRect(gp.round(x - width*.15), gp.round(y-height*.35), gp.round(width*.25), gp.round(height*.08));
		g2.fillRect(gp.round(x + width - width*.1), gp.round(y-height*.35), gp.round(width*.25), gp.round(height*.08));
		g2.setColor(Color.darkGray);
		g2.fillRect(gp.round(x)+1, gp.round((y-height*.43)), width-2, gp.round(height*.2));
		g2.fillRect(gp.round(x)+1, gp.round((y-height*.92)), width-2, gp.round(height*.15));
		
		g2.setTransform(origTransform);
	}
	
	public void update() {
		
		centerX = x + (Math.cos(Math.toRadians(angle)) * height*.2);
		centerY = y - height * .2 + (Math.sin(Math.toRadians(angle)) * height*.2);
		
		if (Math.sqrt((centerX - gp.player.getX())*(centerX - gp.player.getX()) + (centerY - gp.player.getY())*(centerY - gp.player.getY())) <= width && player == null) {
			player = gp.player;
			if (player.car == null) {
				player.car = this;    
			} else {
				player = null;
			}
		}
		
		if (player != null) {
			player.setX(centerX);
			player.setY(centerY);
			updateControls();
		} else {
			forwardVel *= .997;
			turnVel *= 1 - (.1 * (forwardVel/speedLimit));
		}
		
		if (gp.round(angle) % 45 == 0) {
			if (forwardVel >= speedLimit*.5) {
				angle = gp.round(angle);
			}
		}
		
		if (turnVel > turnLimit) {
			turnVel = turnLimit;
		}
		if (turnVel < -turnLimit) {
			turnVel = -turnLimit;
		}
		
		angle += turnVel * (forwardVel/speedLimit);
		
		double netAngle = angle;
		double netForwardVel = forwardVel;
		
		x += Math.cos(Math.toRadians(netAngle)) * netForwardVel;
		y += Math.sin(Math.toRadians(netAngle)) * netForwardVel;
		
		setHitbox();
	}
	
	public void updateControls() {
		
		if (keyH.forwardPressed) {
			if (forwardVel > 0) {
				forwardVel += -Math.log(Math.abs(forwardVel/speedLimit)) * forwardAcc * .3;
			} else {
				forwardVel += forwardAcc * .75;
			}
		}
		
		if (keyH.backwardPressed) {
			if (forwardVel < 0) {
				forwardVel -= -Math.log(Math.abs(forwardVel/speedLimit)) * forwardAcc * .3;
			} else {
				forwardVel -= forwardAcc * .75;
			}
		}
		
		if (keyH.leftPressed) {
			turnVel -= turnAcc;
		}
		
		if (keyH.rightPressed) {
			turnVel += turnAcc;
		}
		
		if (!keyH.forwardPressed && !keyH.backwardPressed) {
			forwardVel *= .997;
		}
		if (!keyH.leftPressed && !keyH.rightPressed) {
			turnVel *= 1 - (.1 * (forwardVel/speedLimit));
		}
		if (keyH.brakePressed) {
			forwardVel *= .985;
		}
		if (keyH.exitPressed) {
			if (player != null) {
				player.setX(centerX + Math.cos(Math.toRadians(angle-90))*30);
				player.setY(centerY + Math.sin(Math.toRadians(angle-90))*30);
				player.car = null;
				player = null;
			}
		}
	}
	
	public void setHitbox() {
		
		Point point1 = gp.rotatePoint(centerX, centerY, hitboxPoints[0].getX() + centerX, hitboxPoints[0].getY() + centerY, angle);
		Point point2 = gp.rotatePoint(centerX, centerY, hitboxPoints[1].getX() + centerX, hitboxPoints[1].getY() + centerY, angle);
		Point point3 = gp.rotatePoint(centerX, centerY, hitboxPoints[2].getX() + centerX, hitboxPoints[2].getY() + centerY, angle);
		Point point4 = gp.rotatePoint(centerX, centerY, hitboxPoints[3].getX() + centerX, hitboxPoints[3].getY() + centerY, angle);
		
		hitbox = new Polygon(new int[] {gp.round(point1.getX()),gp.round(point2.getX()),gp.round(point3.getX()),gp.round(point4.getX())},
				 			 new int[] {gp.round(point1.getY()),gp.round(point2.getY()),gp.round(point3.getY()),gp.round(point4.getY())},4);
		
		hitboxArea = new Area(hitbox);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getForwardVel() {
		return forwardVel;
	}

	public void setForwardVel(double forwardVel) {
		this.forwardVel = forwardVel;
	}

	public double getTurnVel() {
		return turnVel;
	}

	public void setTurnVel(double turnVel) {
		this.turnVel = turnVel;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}
	
	public void setSpeedLimit(int speedLimt) {
		this.speedLimit = speedLimt;
	}

	public double getTurnLimit() {
		return turnLimit;
	}

	public void setTurnLimit(int turnLimit) {
		this.turnLimit = turnLimit;
	}

	public double getForwardAcc() {
		return forwardAcc;
	}

	public void setForwardAcc(double forwardAcc) {
		this.forwardAcc = forwardAcc;
	}

	public double getTurnAcc() {
		return turnAcc;
	}

	public void setTurnAcc(double turnAcc) {
		this.turnAcc = turnAcc;
	}
}
