import javax.swing.ImageIcon;

/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

public class Wolf extends Animal {
	
	final int withoutFood = 200;
	final int breedTimer = 201;
	
	public Wolf(Pasture pasture, int moveSpeed, int viewDistance) {
		super(pasture, moveSpeed, viewDistance);
		this.image = new ImageIcon("resource/wolf.gif");
		this.lastX = 1;
		this.lastY = 1;
		this.liveWithoutFood = withoutFood;
		this.timeToMultiply = breedTimer;
		this.multiplayInterval = breedTimer;
		this.hasEaten = false;
		this.lastMealTime = pasture.getTime();
	}

	@Override
	public void tick() {
		if(alive) {
			moveTheEntity();
			eat();
			multiplyEntity(hasEaten, timeToMultiply);
			starveToDeath(lastMealTime, liveWithoutFood);

		}
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public boolean isCompatible(Entity otherEntity) {
		if (otherEntity instanceof Plant) {
			return true;
		}
		if (otherEntity instanceof Sheep) {
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
		}
	}

	@Override
	Animal breed() {
		return new Wolf(pasture, moveInterval, viewDistance);
	}

	@Override
	double getScore(Entity e, Double distance) {
		if(e instanceof Sheep) {
			return 100 / (1 + distance);
		}
		else {
			return 0.0;			
		}
	}
}
