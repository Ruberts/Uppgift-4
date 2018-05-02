import java.util.*;
import java.util.List;

import javax.swing.*;

import java.awt.*;

/**
 * A pasture contains sheep, wolves, fences, plants, and possibly other
 * entities. These entities move around in the pasture and try to find food,
 * other entities of the same kind and run away from possible enemies.
 */
public class Pasture {

	private final int width = 35;
	private final int height = 24;

	private final int wolves = 10;//10
	private final int sheep = 20;//20
	private final int plants = 40;//40
	private final int fences = 40;

	private final Set<Entity> world = new HashSet<Entity>();
	private final Map<Point, List<Entity>> grid = new HashMap<Point, List<Entity>>();
	private final Map<Entity, Point> point = new HashMap<Entity, Point>();

	private final PastureGUI gui;
	private final Engine engine;

	/**
	 * Creates a new instance of this class and places the entities in it on random
	 * positions.
	 */
	public Pasture(int wolfSpeed, int wolfView, int sheepSpeed, int sheepView) {

		engine = new Engine(this);
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
		
		for(int i = 0; i < fences; i++) {
			Fence f1 = new Fence(this);
			addEntity(f1, f1.getFreePosition(width, height));			
		}

		/*
		 * Now insert the right number of different entities in the pasture.
		 */
		for (int i = 0; i < wolves; i++) {
			Wolf w1 = new Wolf(this, wolfSpeed, wolfView);
			addEntity(w1, w1.getFreePosition(width, height));
		}

		for (int i = 0; i < sheep; i++) {
			Sheep s1 = new Sheep(this, sheepSpeed, sheepView);
			addEntity(s1, s1.getFreePosition(width, height));
		}
		
		for (int i = 0; i < plants; i++) {
			Plant p1 = new Plant(this);
			addEntity(p1, p1.getFreePosition(width, height));
		}

		gui.update();
	}
	
	public int getTime() {
		return engine.getTime();
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
	
	/* Get all entities within the view of the entity*/
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
		JFrame f = new JFrame();
		InitialParameters p = new InitialParameters();
		p.add("wolfSpeed", "Speed of the wolf?", "20");
		p.add("wolfView", "View distance of the wolf?", "3");
		p.add("sheepSpeed", "Speed of the sheep?", "20");
		p.add("sheepView", "View distance of the sheep?", "5");
		p.add("plantSpread", "How fast should the plants spread?", "30");

		JButton klar = new JButton("Klar!");

		p.add(klar);
		klar.addActionListener(p);

		f.add(p);
		f.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(screenSize.width / 2 - f.getWidth() / 2, screenSize.height / 2 - f.getHeight() / 2);

		f.setVisible(true);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Map<String, JTextField> m = p.getMap();

		new Pasture(
				Integer.parseInt(m.get("wolfSpeed").getText()),
				Integer.parseInt(m.get("wolfView").getText()),
				Integer.parseInt(m.get("sheepSpeed").getText()),
				Integer.parseInt(m.get("sheepView").getText()));
	}
}
