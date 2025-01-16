package main;                                         

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	boolean
	forwardPressed,
	backwardPressed,
	leftPressed,
	rightPressed,
	brakePressed,
	exitPressed;
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_P) {
			forwardPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
			backwardPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_L) {
			leftPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_QUOTE) {
			rightPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			brakePressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			exitPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_P) {
			forwardPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
			backwardPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_L) {
			leftPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_QUOTE) {
			rightPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			brakePressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			exitPressed = false;
		}
	}
}
