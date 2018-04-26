import javax.swing.ImageIcon;

public class Fence implements Entity{
	
	/** The icon of this entity. */
	private final ImageIcon image = new ImageIcon("resource/fence.gif");
	private final Pasture pasture;
	
	public Fence(Pasture pasture) {
		this.pasture = pasture;
	}
	
	@Override
	public void tick() {}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public boolean isCompatible(Entity otherEntity) {
		return false;
	}

	@Override
	public void eatOtherEntity(Entity otherEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}
}
