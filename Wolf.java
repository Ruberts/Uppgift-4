import javax.swing.ImageIcon;

public class Wolf extends Animal {
	
	public Wolf(Pasture pasture) {
		super(pasture, 10, 20);
		this.image = new ImageIcon("resource/wolf.gif");
		this.lastX = 1;
		this.lastY = 1;
		this.liveWithoutFood = 200;
		this.timeToMultiply = 201;
	}

	@Override
	public void tick() {
		if (alive) {
			moveTheEntity();
			starveToDeath(lastMealTime, liveWithoutFood, this);
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
		if(otherEntity instanceof Sheep) {
			return true;
		}
		return false;
	}

	@Override
	public void eatOtherEntity(Entity otherEntity) {
		if(otherEntity instanceof Sheep) {
			System.out.println("Wolf ate sheep!");
			otherEntity.kill();
			hasEaten = true;
			lastMealTime = pasture.getTime();
		}
	}

	/* remove since we have it in Animal */	
//	@Override
//	public void kill() {
//		this.alive = false;
//		pasture.removeEntity(this);
//	}
}
