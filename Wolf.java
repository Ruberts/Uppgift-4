import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public class Wolf extends Animal {
	
	public Wolf(Pasture pasture) {
		super(pasture, 10, 20);
		this.image = new ImageIcon("src/wolf.gif");
//		this.pasture = pasture;
		this.alive = true;
//		this.viewDistance = 3;
		
		this.lastX = 1;
		this.lastY = 1;
	}

	@Override
	public void tick() {
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
					
					/* Wolfs only eat Sheeps! */
					if(e instanceof Sheep) {
						score += 100 / (1 + distance);
					}
				}
				scoredNeighbours.put(neighbour, score);
			}
			
			/* Get direction */
			Map.Entry<Point, Double> maxEntry = null;
			for (Map.Entry<Point, Double> entry : scoredNeighbours.entrySet()) {
				if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					System.out.println("Wolf spotted a sheep, pursuing it!");
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
			
			/* Move the wolf */
			pasture.moveEntity(this, preferredNeighbour);
			this.moveDelay = this.moveInterval;
		}	
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public boolean isCompatible(Entity otherEntity) {
		if(otherEntity instanceof Plant) {
			return true;
		}
		return false;
	}

	@Override
	public void eatenByEntity(Entity otherEntity) {
		// TODO Auto-generated method stub
		
	}
}
