import javax.swing.ImageIcon;

public class Sheep extends Animal {
	
	public Sheep(Pasture pasture) {
		super(pasture, 10, 20);
		this.image = new ImageIcon("resource/sheep.gif");
		this.lastX = 1;
		this.lastY = 1;
		this.liveWithoutFood = 100;
	}

	@Override
	public void tick() {		
		moveTheEntity();
		starveToDeath(lastMealTime, liveWithoutFood);
		System.out.println("Time since food " + lastMealTime + "!");
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
			lastMealTime = engine.getTime();;
		}	
	}

	@Override
	public void kill() {
		this.alive = false;
		pasture.removeEntity(this);
	}
}
