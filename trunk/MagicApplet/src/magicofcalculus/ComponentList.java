//
// ComponentList.java
//
package magicofcalculus;

import java.util.LinkedList;

/**
 * Class extending {@link ArrayList} of type {@link Component}. Holds the
 * components used in MagicApplet's scenes.
 */
public class ComponentList extends LinkedList<Component>
{

  /**
   * Default class constructor
   */
  public ComponentList() {
      super();
  }

  /**
   * Moves the component at the specified index to the foreground, so that it is
   * on top of all the other components
   * 
   * @param index
   *          Index of the component to move to the foreground
   */
  public void bringToTopOfZOrder(int index) {
    Component comp = get(index);
    for (int i = index; i > 0; i--) {
      set(i, get(i - 1));
    }
    set(0, comp);
  }

  /**
   * Moves the specified <code>Component</code> to the foreground, so that it is
   * on top of all the other components
   * 
   * @param comp
   *          The <code>Component</code> to move to the foreground
   */
  public void bringToTopOfZOrder(Component comp) {
    int index = indexOf(comp);
    for (int i = index; i > 0; i--) {
      set(i, get(i - 1));
    }
    set(0, comp);
  }

  /**
   * Determines if a given <code>Component</code> is 'on top of' another
   * component. (Meaning it has a lower index value)
   * 
   * @param firstComp
   *          The first <code>Component</code>
   * @param secondComp
   *          The second <code>Component</code>
   * @return <code>True</code> if <code>firstComponent</code> is above (has a
   *         lower index than) <code>secondComponent</code>; otherwise
   *         <code>false</code>
   */
  public boolean isOnTopOf(Component firstComp, Component secondComp) {
    int indexOfFirstComp = indexOf(firstComp);
    int indexOfSecondComp = indexOf(secondComp);
    return (indexOfFirstComp < indexOfSecondComp);
  }

}
// -------------------------------
// -------------------------------
