import java.awt.Point;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

public class Plant implements Entity {
	private final ImageIcon image = new ImageIcon("src/plant.gif");
	private final Pasture pasture;
	/* Interval until the plant spreads */
	static final int spreadInterval = 50;
	private int spreadingDelay;
	
	public Plant(Pasture pasture) {
		this.pasture = pasture;
		this.spreadingDelay = spreadInterval;
	}

	@Override
	public void tick() {
		if(spreadingDelay-- <= 0) {
			if(pasture.getFreeNeighbours(this).size() > 0) {
				pasture.addEntity(new Plant(pasture), pasture.getFreeNeighbours(this).get((int)(Math.random() * pasture.getFreeNeighbours(this).size())));
				
				this.spreadingDelay = spreadInterval;
			}
		}
		/* If a sheep finds a plant it eats it! */
		for(Entity e : pasture.getEntitiesAt(pasture.getEntityPosition(this))){
			this.eatenByEntity(e);
		}		

	}
	
	public void eatenBySheep(Entity otherEntity) {
		if(otherEntity instanceof Sheep) {
			pasture.removeEntity(this);
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
	
	
	/* Check if a sheep is on the same position of the plant */
	@Override
	public void eatenByEntity(Entity otherEntity) {
		if(otherEntity instanceof Sheep) {
			pasture.removeEntity(this);
		}	
	}
}
