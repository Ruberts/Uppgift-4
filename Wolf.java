import javax.swing.ImageIcon;

public class Wolf extends Animal {
	
	public Wolf(Pasture pasture) {
		super(pasture, 20, 20);
		this.image = new ImageIcon("src/wolf.gif");
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
		return false;
	}

	@Override
	public void eatOtherEntity(Entity otherEntity) {
		if(otherEntity instanceof Sheep) {
			System.out.println("Wolf ate sheep!");
			otherEntity.kill();
		}
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
	}
}
