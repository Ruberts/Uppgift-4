import java.awt.Point;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

public class Plant implements Entity {
	private final ImageIcon image = new ImageIcon("resource/plant.gif");
	private final Pasture pasture;
	protected boolean alive;
	
	/* Interval until the plant spreads */
	static final int spreadInterval = 30;
	private int spreadingDelay;
	
	public Plant(Pasture pasture) {
		this.pasture = pasture;
		this.spreadingDelay = spreadInterval;
		this.alive = true;
	}

	@Override
	public void tick() {
		if (alive) {
			if (spreadingDelay-- <= 0) {
				/* Check surroundings for an empty space to grow */
				if (pasture.getFreeNeighbours(this).size() > 0) {
					pasture.addEntity(new Plant(pasture), pasture.getFreeNeighbours(this)
							.get((int) (Math.random() * pasture.getFreeNeighbours(this).size())));
					
					/* "Reset" the timer for the newly created plant */
					this.spreadingDelay = spreadInterval;
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
	public void eatOtherEntity(Entity otherEntity) {}

	@Override
	public void kill() {
		this.alive = false;
		pasture.removeEntity(this);
	}
}
