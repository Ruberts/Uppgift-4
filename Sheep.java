import javax.swing.ImageIcon;

/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

public class Sheep extends Animal {
	
	final int withoutFood = 100;
	final int breedTimer = 101;
	
	public Sheep(Pasture pasture, int moveSpeed, int viewDistance) {
		super(pasture, moveSpeed, viewDistance);
		this.image = new ImageIcon("resource/sheep.gif");
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
		// if(otherEntity instanceof Plant) {
		// return true;
		// }
		// if(otherEntity instanceof Wolf) {
		// return true;
		// }
		if (otherEntity instanceof Fence) {
			return false;
			
		}
		return true;

	}


	@Override
	public void eatOtherEntity(Entity otherEntity) {
		if(otherEntity instanceof Plant) {
			otherEntity.kill();
			hasEaten = true;
			lastMealTime = pasture.getTime();
		}	
	}

	@Override
	Animal breed() {
		return new Sheep(pasture, moveInterval, viewDistance);
	}

	@Override
	double getScore(Entity e, Double distance) {
		if(e instanceof Plant) {
			return 100 / (1 + distance);
		}
		else if(e instanceof Wolf) {
			return (100 * distance) - distance;
		}
		else {
			return 0.0;
		}
	}
}
