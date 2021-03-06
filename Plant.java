import java.awt.Point;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

public class Plant implements Entity {
	private final ImageIcon image = new ImageIcon("resource/plant.gif");
	private final Pasture pasture;
	protected boolean alive;
	private int spreadDelay, spreadInterval;
	
	public Plant(Pasture pasture, int spreadInterval) {
		this.pasture = pasture;
		this.spreadDelay = spreadInterval;
		this.spreadInterval = spreadInterval;
		this.alive = true;
	}

	@Override
	public void tick() {
		if (alive) {
			if (spreadDelay-- <= 0) {
				/* Check surroundings for an empty space to grow */
				if (pasture.getFreeNeighbors(this).size() > 0) {
					pasture.addEntity(new Plant(pasture, this.spreadInterval), pasture.getFreeNeighbors(this)
							.get((int) (Math.random() * pasture.getFreeNeighbors(this).size())));
					
					/* "Reset" the timer for the newly created plant */
					this.spreadDelay = spreadInterval;
				}
			}
		}
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public boolean isCompatible(Entity otherEntity) {
		/* Make sure all "Animals" in the pasture can step on plants! */
		if(otherEntity instanceof Animal) {
			return true;
		}
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
	public void eatOtherEntity(Entity otherEntity) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void kill() {
		this.alive = false;
		pasture.removeEntity(this);
	}

	@Override
	public void multiplyEntity(boolean eaten, int time) {
		// TODO Auto-generated method stub
	}
}
