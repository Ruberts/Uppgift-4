import java.awt.Point;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

public class Fence implements Entity{	
	private final ImageIcon image = new ImageIcon("resource/fence.gif");
	private final Pasture pasture;
	
	public Fence(Pasture pasture) {
		this.pasture = pasture;
	}
	
	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public boolean isCompatible(Entity otherEntity) {
		/* No other entity can be on the same position as a fence */
		return false;
	}

	public Point getFreePosition(int width, int height) throws MissingResourceException {
		Point position = new Point((int) (Math.random() * width), (int) (Math.random() * height));
		
		int p = position.x + position.y * width;
		int m = height * width;
		int q = 97; // any large prime will do
		
		for (int i = 0; i < m; i++) {
			int j = (p + i * q) % m;
			int x = j % width;
			int y = j / width;
			
			position = new Point(x, y);
			boolean free = true;
			
			Collection<Entity> c = pasture.getEntitiesAt(position);
			if (c != null) {
				for (Entity thisThing : c) {
					if (!isCompatible(thisThing)) {
						free = false;
						break;
					}
				}
			}
			if (free) {
				return position;
			}
		}
		throw new MissingResourceException("There is no free space" + " left in the pasture", "Pasture", "");
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void eatOtherEntity(Entity otherEntity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
	}

	@Override
	public void multiplyEntity(boolean eaten, int time) {
		// TODO Auto-generated method stub		
	}
}
