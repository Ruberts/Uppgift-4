import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import javax.swing.ImageIcon;

/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

public abstract class Animal implements Entity {
	
	protected int moveInterval;
	protected int moveDelay;
	protected int viewDistance;
	protected int lastX, lastY;
	protected int liveWithoutFood;
	protected int lastMealTime;
	protected int timeToMultiply;
	protected int multiplayInterval;
	protected boolean hasEaten;
	protected boolean alive;
	protected Pasture pasture;
	protected ImageIcon image;
	protected final Engine engine;
	
	public Animal(Pasture pasture, int moveSpeed, int viewDistance) {
		this.pasture = pasture;
		this.moveInterval = moveSpeed;
		this.moveDelay = this.moveInterval;
		this.viewDistance = viewDistance;
		this.alive = true;
		this.engine = new Engine(pasture);
	}
	
	 
	/* If conditions is correct the animal is able to breed! */
	abstract Animal breed();
	
	/* Get the score of to determine which direction to move */
	abstract double getScore(Entity e, Double distance);
	
	/* Living animals has to eat */
	public void eat() {
		/* Eat the other entity if on same position */
		for (Entity e : pasture.getEntitiesAt(pasture.getEntityPosition(this))) {
			eatOtherEntity(e);
		}
	}
	
	/* Animals multiply if correct conditions are fulfilled */
	public void multiplyEntity(boolean eaten, int time) {
		if(eaten && (timeToMultiply-- <= 0)) {
			if(pasture.getFreeNeighbors(this).size() > 0) {
				pasture.addEntity(breed(), pasture.getFreeNeighbors(this).get((int) (Math.random() * pasture.getFreeNeighbors(this).size())));
						
				/* "Reset" the timer for the newly born Animal */
				timeToMultiply = multiplayInterval;
			}
		}
	}

	/* If the Animal hasn't eaten in a set amount of time, it'll die of starvation */
	public void starveToDeath(int lastMeal, int withoutFood) {
		if((pasture.getTime() - lastMeal) >= withoutFood) {
			kill();
		}
	}
	
	/* "Kill" the animal if it hasn't eaten in a set amount of time.. */
	public void kill() {
		this.alive = false;
		pasture.removeEntity(this);
	}
	
	/* Calculate next direction for movement and eat if possible */
	public void moveTheEntity() {
		moveDelay--;
		if (moveDelay == 0) {	
			List<Entity> entitisInView = pasture.getEntitiesWithinView(pasture.getEntityPosition(this), this.viewDistance);
			
			Map<Point, Double> scoredNeighbors = new HashMap<Point, Double>();
			Point entityPosition = pasture.getEntityPosition(this);
			
			for(Point neighbor : pasture.getAllNeighbors(entityPosition)) {
				Double score = 0.0;
				
				for(Entity e : entitisInView) {
					Double distance = neighbor.distance(pasture.getEntityPosition(e));
					
					score += getScore(e, distance);
				}
				scoredNeighbors.put(neighbor, score);
			}
			
			/* Get direction */
			Map.Entry<Point, Double> maxEntry = null;
			for (Map.Entry<Point, Double> entry : scoredNeighbors.entrySet()) {
				if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			Point preferredNeighbor = maxEntry.getKey();
			
			/* If there's no interesting direction continue on the same direction */
			if(pasture.getFreeNeighbors(this).contains(preferredNeighbor) == false) {
				preferredNeighbor = new Point(
						(int) pasture.getEntityPosition(this).getX() + lastX,
						(int) pasture.getEntityPosition(this).getY() + lastY);
			}
			
			/* If preferred direction isn't possible, take a random direction */
			if(pasture.getFreeNeighbors(this).contains(preferredNeighbor) == false) {
				preferredNeighbor = getRandomMember(pasture.getFreeNeighbors(this));
			}
			
			/* Update the direction */
			lastX = (int)preferredNeighbor.getX() - (int)pasture.getEntityPosition(this).getX();
			lastY = (int)preferredNeighbor.getY() - (int)pasture.getEntityPosition(this).getY();
			
			/* Move the Animal */
			pasture.moveEntity(this, preferredNeighbor);
			this.moveDelay = this.moveInterval;
		}
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
	 * A general method for grabbing a random element from a list. 
	 */
	public static <X> X getRandomMember(List<X> c) {
		if (c.size() == 0)
			return null;

		int n = (int) (Math.random() * c.size());

		return c.get(n);
	}	
}
