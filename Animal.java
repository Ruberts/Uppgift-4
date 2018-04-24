import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

public abstract class Animal implements Entity {
	
	protected int moveInterval;
	protected int moveDelay;
	protected int viewDistance;
	protected int lastX, lastY;
	protected boolean alive;

	protected Pasture pasture;
	protected ImageIcon image;
	
	public Animal(Pasture pasture, int moveInterval, int viewDistance) {
		this.pasture = pasture;
		this.moveInterval = moveInterval;
		this.moveDelay = this.moveInterval;
		this.viewDistance = viewDistance;
	}
	

	
	
	
	/**
	 * Returns a random free position in the pasture if there exists one.
	 * 
	 * If the first random position turns out to be occupied, the rest of the board
	 * is searched to find a free position.
	 */
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
	
	/**
	 * A general method for grabbing a random element from a list. Does it belong in
	 * this class?
	 */
	public static <X> X getRandomMember(List<X> c) {
		if (c.size() == 0)
			return null;

		int n = (int) (Math.random() * c.size());

		return c.get(n);
	}
}
