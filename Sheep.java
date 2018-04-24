import java.awt.Point;

import javax.swing.ImageIcon;

public class Sheep extends Animal {
	
	public Sheep(Pasture pasture) {
		super(pasture, 20, 20);
		this.image = new ImageIcon("src/sheep.gif");
//		this.pasture = pasture;
		this.alive = true;
		this.eatsSheep = false;
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
