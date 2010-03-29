package james.Annotations.labels;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import james.Annotations.ComponentCaller;
import magicofcalculus.components.Label;

/**
 * Contains a url string for a labels image
 * 
 * @author James Arlow
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Image {

    /**
     * The label's image url string
     * 
     * @return
     */
    public String value();

    public static class SetLabel extends ComponentCaller<Image, Label> {

	@Override
	public void call(Image annote, Label c) {
	    c.setImage(annote.value());
	}

	@Override
	public String[] getProperties(Label c) {
	    return new String[] { };
	}
    }
}
