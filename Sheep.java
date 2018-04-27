import javax.swing.ImageIcon;

public class Sheep extends Animal {
	
	public Sheep(Pasture pasture) {
		super(pasture, 10, 20);
		this.image = new ImageIcon("resource/sheep.gif");
		this.lastX = 1;
		this.lastY = 1;
		this.liveWithoutFood = 100;
		this.timeToMultiply = 101;
	}

	@Override
	public void tick() {
		if (alive) {
			moveTheEntity();
			starveToDeath(lastMealTime, liveWithoutFood, this);
			multiplyEntity(hasEaten, timeToMultiply);
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
		if(otherEntity instanceof Wolf) {
			return true;
		}
		return false;
	}


	@Override
	public void eatOtherEntity(Entity otherEntity) {
		if(otherEntity instanceof Plant) {
			System.out.println("Sheep ate Plant!");
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
