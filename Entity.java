import javax.swing.ImageIcon;


/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

/**
 * This is the superclass of all entities in the pasture simulation system. This
 * interface <b>must</b> be implemented by all entities that exist in the
 * simulation of the pasture.
 */
public interface Entity {

	public void tick();

	/**
	 * ImageIcon returns the icon of this entity, to be displayed by the pasture
	 * gui.
	 */
	public ImageIcon getImage();
	
	/**
	 * Decides if the entity can be on the same position as another entity
	 */
	public boolean isCompatible(Entity otherEntity);
	
	/**
	 * "Eats" another entity if they are on the same position
	 */
	public void eatOtherEntity(Entity otherEntity);

	/**
	 * Removes the entity from the pasture
	 */
	public void kill();
	
	/**
	 * Multiplys the entity if correct conditions are fulfilled.
	 */
	public void multiplyEntity(boolean eaten, int time);
}
