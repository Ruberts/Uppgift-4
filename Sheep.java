import javax.swing.ImageIcon;

public class Sheep extends Animal {
	
	public Sheep(Pasture pasture) {
		super(pasture, 10, 20);
		this.image = new ImageIcon("src/sheep.gif");
		this.lastX = 1;
		this.lastY = 1;
	}

	@Override
	public void tick() {		
		moveTheEntity();
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
		}	
	}

	@Override
	public void kill() {
		this.alive = false;
		pasture.removeEntity(this);
	}
}
