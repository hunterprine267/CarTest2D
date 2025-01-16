package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Car Test");
		
		GraphicPanel gPanel = new GraphicPanel();
		
		window.setSize(gPanel.screenWidth, gPanel.screenHeight);
		//window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//window.setUndecorated(true);
		window.add(gPanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gPanel.startGThread();
	}
	
	public BufferedImage getImage(String path) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
}
