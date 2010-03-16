//
// DragMaster.java
//
package magicofcalculus;

/**
 * Interface used for component dragging
 * 
 * @deprecated
 * @author T Johnson
 * 
 */
public interface DragMaster {
	/**
	 * Determines if the specified destination is valid
	 * 
	 * @param mousePoint
	 *            {@link DPoint} containing the position of the mouse click
	 * @param dragDestination
	 *            <code>DPoint</code> containing the destination component was
	 *            dragged to
	 * @return <code>True</code> if the destination is a valid location,
	 *         otherwise <code>false</code>
	 * @deprecated
	 */
	public boolean getDragDestination(DPoint mousePoint, DPoint dragDestination);

}
// ---------------------------------
// ---------------------------------
