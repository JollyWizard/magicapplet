# Introduction #

The `ScribbleComponent` will be a class extending `Component` that allows the user to 'scribble' inside of the component with the mouse/pointing device. It should function like Paint does in Microsoft Windows.


## Specifications ##
  * Extend `Component`
  * If possible, incorporate the existing `PolyLine` class
  * Size defined by a `Rectangle`
  * Implement two possible states of functionality- single and multiple strokes
  * Differentiate between drawing input and dragging input (Recognize mouse button down, button up, and movement)


Example enum:
```
/**
   * Enumeration for the modes under which the {@link ScribbleComponent} can operate.
   * <ul>
   * <li><b>Single stroke mode</b> - <code>{@link ScribbleComponent.ScribbleMode#SingleStroke}</code></li>
   * <li><b>Multi-stroke mode</b> - <code>{@link ScribbleComponent.ScribbleMode#MultiStroke}</code></li>
   * </ul>
   * @author WadeVH
   *
   */
  public enum ScribbleMode
  {
    /**
     * Mode for the <code>ScribbleComponent</code> in which a single stroke can
     * be made.
     */
    SingleStroke,
    /**
     * Mode for the <code>ScribbleComponent</code> in which multiple strokes can
     * be made until committed.
     */
    MultiStroke
  }
```

`ScribbleComponent` will also need to override the `draw` method from `Component`
```
 public void draw(Graphics g){
  
// rendering code
// probably using g.drawPolyLine(...);
  }
```

_It will probably not be necessary to store the 'scribble' as points, since the Graphics class is raster based._