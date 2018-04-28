import javax.swing.ImageIcon;

public class Wolf extends Animal {
	
	public Wolf(Pasture pasture) {
		super(pasture, 20, 3);
		this.image = new ImageIcon("resource/wolf.gif");
		this.lastX = 1;
		this.lastY = 1;
		this.liveWithoutFood = 200;
		this.timeToMultiply = 201;
		this.hasEaten = false;
		this.lastMealTime = pasture.getTime();
	}

	@Override
	public void tick() {
		if (alive) {
			moveTheEntity();
			starveToDeath(lastMealTime, liveWithoutFood, this);
			multiplyEntity(hasEaten, liveWithoutFood, this);
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
			otherEntity.kill();
			hasEaten = true;
			lastMealTime = pasture.getTime();
			System.out.println("Wolf ate a sheep at " + lastMealTime);
		}
	}
}
