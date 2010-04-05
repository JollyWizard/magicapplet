package james.annotations.scenes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The scene descriptor annotation. A Act/panel should get several of these at
 * the class level in a @{@link Scenes} Holds the descriptor information, index,
 * and booleans indicating whether or not to jump between panels when finished
 * (in forward progressions and reverse).
 * <p>
 * Progressions are assumed when exiting final(forward) and first(reverse)
 * scenes. {@link #next()} and {@link #prev()} are only for intermediary scenes
 * or unusual situations, such as return to previous panel in either case.
 * 
 * @author James Arlow
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Scene {

    /**
     * The index of the scene
     */
    int index();

    /**
     * The description of the scene
     */
    String description() default DEFAULT;

    /**
     * Indicates that when the scene is finished the next act should be called
     */
    boolean next() default false;

    /**
     * Indicates that when reversing through scenes, the previous Act should be
     * called after this one. By default scene 0 will get this functionality.
     */
    boolean prev() default false;

    /**
     * The default value for Strings, to differentiate from intended empty
     * strings when processing
     */
    public static final String DEFAULT = "!_DEFAULT";
    
    public Class<? extends Action> action = Action.class;
    
    public static interface Action<T> {
	public void performAction(T option);
    }
}
