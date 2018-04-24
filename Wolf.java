import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public class Wolf extends Animal {
	
	public Wolf(Pasture pasture) {
		super(pasture, 10, 10);
		this.image = new ImageIcon("src/wolf.gif");
//		this.pasture = pasture;
		this.alive = true;
		this.eatsSheep = true;
	}

	@Override
	public void tick() {
		if (alive) {
			moveDelay--;
		}
		if (moveDelay == 0) {
			Point neighbour = getRandomMember(pasture.getFreeNeighbours(this));

			if (neighbour != null) {
				pasture.moveEntity(this, neighbour);
			}
			moveDelay = 10;
		}
		
//		if(this.moveDelay-- <= 0) {
//			/* Get all entities within the view distance */
//			List<Entity> view = pasture.getEntitiesOfView(pasture.getPosition(this), viewDistance);
//			
//			Map<Point, Double> prio = new HashMap<Point, Double>();
//			Point pos = pasture.getPosition(this);
//			
//			for (Point neighbour : pasture.get
//				Double prio1 = 0.0;
//				
//				
//			}
//		}
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public boolean isCompatible(Entity otherEntity) {
		return false;
	}
}
