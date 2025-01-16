package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Player {

	GraphicPanel gp;
	KeyHandler keyH;
	
	private double x, y;
	private double forwardVel;
	private double forwardAcc;
	private double speedLimit;
	private double angle;
	private int size = 10;
	
	public Car car;
	
	public Player(GraphicPanel gp, KeyHandler keyH, double x, double y, double forwardAcc, double speedLimit, double angle) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		this.x = x;
		this.y = y;
		this.forwardAcc = forwardAcc;
		this.setSpeedLimit(speedLimit);
		this.angle = angle;
	}
	
	public void draw(Graphics2D g2) {
		if (car == null) {
			AffineTransform origTransform = g2.getTransform();
			
			g2.setColor(new Color(200, 180, 150));
			
			g2.rotate(angle-Math.PI/2, x, y);
			g2.fillRect(gp.round(x - size/2), gp.round(y - size/2), size, size);
			
			Dimension eyeSize = new Dimension(2,2);
			
			g2.setColor(Color.black);
			g2.fillRect(gp.round(x - eyeSize.width - 1), gp.round(y - eyeSize.height - 2), eyeSize.width, eyeSize.height);
			g2.fillRect(gp.round(x + 1), gp.round(y - eyeSize.height - 2), eyeSize.width, eyeSize.height);
			g2.setColor(new Color(60, 25, 15));
			g2.fillRect(gp.round(x) - size/2 - 1 , gp.round(y) - 1, size + 2, gp.round(size * .5 + 2));
			
			g2.setTransform(origTransform);
		}
	}
	
	public void update() {
		
		angle = Math.atan2(y - gp.mouseY + gp.cameraY, x - gp.mouseX + gp.cameraX);
		
		double netAngle = angle;
		
		if (keyH.leftPressed) {
			netAngle -= Math.toRadians(90);
			forwardVel += forwardAcc*.75;
		}
		
		if (keyH.rightPressed) {
			netAngle += Math.toRadians(90);
			forwardVel += forwardAcc*.75;
		}
		
		if (keyH.forwardPressed) {
			forwardVel += forwardAcc;
		}
		
		if (keyH.backwardPressed) {
			forwardVel -= forwardAcc;
		}
		
		if (!keyH.forwardPressed && !keyH.backwardPressed) {
			forwardVel *= .9;
		}
		
		if (forwardVel > speedLimit) {
			forwardVel = speedLimit;
		} else if (forwardVel < -speedLimit) {
			forwardVel = -speedLimit;
		}
		
		double netForwardVel = forwardVel;
		
		x += Math.cos(netAngle) * -netForwardVel;
		y += Math.sin(netAngle) * -netForwardVel;
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

	public double getForwardAcc() {
		return forwardAcc;
	}

	public void setForwardAcc(double forwardAcc) {
		this.forwardAcc = forwardAcc;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}
}
