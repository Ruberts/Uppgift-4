import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

public abstract class Animal implements Entity {
	
	protected int moveInterval;
	protected int moveDelay;
	protected int viewDistance;
	protected int lastX, lastY;
	protected int liveWithoutFood;
	protected int lastMealTime;
	protected boolean hasEaten;
	protected boolean alive;

	protected Pasture pasture;
	protected ImageIcon image;
	
	protected Engine engine;
	
	public Animal(Pasture pasture, int moveInterval, int viewDistance) {
		this.pasture = pasture;
		this.moveInterval = moveInterval;
		this.moveDelay = this.moveInterval;
		this.viewDistance = viewDistance;
		this.alive = true;
		this.hasEaten = false;
		this.engine = new Engine(pasture);
		this.lastMealTime = 0;
	}
	
	public void starveToDeath(int meal, int live) {
		if((meal - engine.getTime()) > live) {
			System.out.println("Sheep died of starvation!");
			kill();
		}
	
	}
	
	public void moveTheEntity() {
		if (alive) {
			moveDelay--;
		}
		if (moveDelay == 0) {	
			List<Entity> seen = pasture.getEntitiesWithinView(pasture.getEntityPosition(this), this.viewDistance);
			
			Map<Point, Double> scoredNeighbours = new HashMap<Point, Double>();
			Point here = pasture.getEntityPosition(this);
			
			for(Point neighbour : pasture.getAllNeighbours(here)) {
				Double score = 0.0;
				
				for(Entity e : seen) {
					Double distance = neighbour.distance(pasture.getEntityPosition(e));
					
					/*  */
					if(this instanceof Sheep) {
						if(e instanceof Wolf) {
							score += 100 / (1 + distance);
						}
						else if(e instanceof Plant) {
							score += 50 / (1 + distance);
						}						
					}
					else if(this instanceof Wolf) {
						if(e instanceof Sheep) {
							score += 100 / (1 + distance);
						}						
					}	
				}
				scoredNeighbours.put(neighbour, score);
			}
			
			/* Get direction */
			Map.Entry<Point, Double> maxEntry = null;
			for (Map.Entry<Point, Double> entry : scoredNeighbours.entrySet()) {
				if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			Point preferredNeighbour = maxEntry.getKey();
			
			/* If there's no interesting direction continue on the same direction */
			if(pasture.getFreeNeighbours(this).contains(preferredNeighbour) == false) {
				preferredNeighbour = new Point(
						(int) pasture.getEntityPosition(this).getX() + lastX,
						(int) pasture.getEntityPosition(this).getY() + lastY);
			}
		
			/* If last direction isn't possible, take a random direction */
			if(pasture.getFreeNeighbours(this).contains(preferredNeighbour) == false) {
				preferredNeighbour = getRandomMember(pasture.getFreeNeighbours(this));
			}
			
			/* Update the direction */
			lastX = (int)preferredNeighbour.getX() - (int)pasture.getEntityPosition(this).getX();
			lastY = (int)preferredNeighbour.getY() - (int)pasture.getEntityPosition(this).getY();
			
			/* Move the Animal */
			pasture.moveEntity(this, preferredNeighbour);
			this.moveDelay = this.moveInterval;
			
			/* Eat the other entity if on same position */
			for(Entity e : pasture.getEntitiesAt(pasture.getEntityPosition(this))){
				this.eatOtherEntity(e);
			}
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
