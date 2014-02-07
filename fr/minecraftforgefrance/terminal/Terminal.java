package fr.minecraftforgefrance.terminal;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class Terminal extends JFrame{
	
	public Terminal() {
		try {
			super.setIconImage(ImageIO.read(getClass().getResource("/fr/minecraftforgefrance/terminal/icon/icon.jpg")));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	@Override
	public void setTitle(String title) {
		super.setTitle("Minecraft Forge France Terminal : "+title);
	}
	
	@Override
	public void setIconImage(Image icon) {}
	
}
