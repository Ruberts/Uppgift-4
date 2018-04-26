import java.util.*;
import java.awt.Point;

/**
 * A pasture contains sheep, wolves, fences, plants, and possibly other
 * entities. These entities move around in the pasture and try to find food,
 * other entities of the same kind and run away from possible enimies.
 */
public class Pasture {

	private final int width = 35;
	private final int height = 24;

	private final int wolves = 0;//10;
	private final int sheep = 2;//20;
	private final int plants = 0;//40;

	private final Set<Entity> world = new HashSet<Entity>();
	private final Map<Point, List<Entity>> grid = new HashMap<Point, List<Entity>>();
	private final Map<Entity, Point> point = new HashMap<Entity, Point>();

	private final PastureGUI gui;

	/**
	 * Creates a new instance of this class and places the entities in it on random
	 * positions.
	 */
	public Pasture() {

		Engine engine = new Engine(this);
		gui = new PastureGUI(width, height, engine);

		/*
		 * The pasture is surrounded by a fence. Replace Dummy for Fence when you have
		 * created that class
		 */
		for (int i = 0; i < width; i++) {
			addEntity(new Fence(this), new Point(i, 0));
			addEntity(new Fence(this), new Point(i, height - 1));
		}
		for (int i = 1; i < height - 1; i++) {
			addEntity(new Fence(this), new Point(0, i));
			addEntity(new Fence(this), new Point(width - 1, i));
		}

		/*
		 * Now insert the right number of different entities in the pasture.
		 */
		for (int i = 0; i < wolves; i++) {
			Wolf w1 = new Wolf(this);
			addEntity(w1, w1.getFreePosition(width, height));
		}

		for (int i = 0; i < sheep; i++) {
			Sheep s1 = new Sheep(this);
			addEntity(s1, s1.getFreePosition(width, height));
		}
		
		for (int i = 0; i < plants; i++) {
			Plant p1 = new Plant(this);
			addEntity(p1, p1.getFreePosition(width, height));
		}

		gui.update();
	}
	
	public void refresh() {
		gui.update();
	}

	/**
	 * Add a new entity to the pasture.
	 */
	public void addEntity(Entity entity, Point pos) {

		world.add(entity);

		List<Entity> l = grid.get(pos);
		if (l == null) {
			l = new ArrayList<Entity>();
			grid.put(pos, l);
		}
		l.add(entity);

		point.put(entity, pos);

		gui.addEntity(entity, pos);
	}

	public void moveEntity(Entity e, Point newPos) {

		Point oldPos = point.get(e);
		List<Entity> l = grid.get(oldPos);
		if (!l.remove(e))
			throw new IllegalStateException("Inconsistent stat in Pasture");
		/*
		 * We expect the entity to be at its old position, before we move, right?
		 */

		l = grid.get(newPos);
		if (l == null) {
			l = new ArrayList<Entity>();
			grid.put(newPos, l);
		}
		l.add(e);

		point.put(e, newPos);

		gui.moveEntity(e, oldPos, newPos);
	}

	/**
	 * Remove the specified entity from this pasture.
	 */
	public void removeEntity(Entity entity) {

		Point p = point.get(entity);
		world.remove(entity);
		grid.get(p).remove(entity);
		point.remove(entity);
		gui.removeEntity(entity, p);

	}

	/**
	 * Various methods for getting information about the pasture
	 */
	public List<Entity> getEntities() {
		return new ArrayList<Entity>(world);
	}

	public Collection<Entity> getEntitiesAt(Point lookAt) {
		Collection<Entity> l = grid.get(lookAt);

		if (l == null) {
			return null;
		} else {
			return new ArrayList<Entity>(l);
		}
	}

	public List<Point> getFreeNeighbours(Entity entity) {
		List<Point> free = new ArrayList<Point>();

		int entityX = getEntityPosition(entity).x;
		int entityY = getEntityPosition(entity).y;

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				Point p = new Point(entityX + x, entityY + y);
				if (freeSpace(p, entity))
					free.add(p);
			}
		}
		return free;
	}

	private boolean freeSpace(Point p, Entity e) {

		List<Entity> l = grid.get(p);
		if (l == null)
			return true;
		for (Entity old : l)
			if (!old.isCompatible(e))
				return false;
		return true;
	}

	public Point getEntityPosition(Entity entity) {
		return point.get(entity);
	}
	
	public List<Entity> getEntitiesWithinView(Point p, int viewDistance){
		List<Entity> found = new ArrayList<Entity>();
		
		for(Entity e : world) {
			double entityX = this.getEntityPosition(e).getX();
			double entityY = this.getEntityPosition(e).getY();
			if(p.distance(entityX, entityY) <= viewDistance) {
				found.add(e);
			}
		}
		return found;
	}
	
    // Do we need this? getFreeNeighbours should be used?
    public List<Point> getAllNeighbours(Point origin) {
        List<Point> surrounding = new ArrayList<Point>();

        int originX = origin.x;
        int originY = origin.y;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Point p = new Point(originX + x, originY + y);
                surrounding.add(p);
            }
        }
        return surrounding;
    }
   
	/** The method for the JVM to run. */
	public static void main(String[] args) {

		new Pasture();
	}
}
