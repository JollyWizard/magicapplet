package james.annotations.labels;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import james.ComponentCaller;
import magicofcalculus.components.Label;

/**
 * Contains a configuration information about label images
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

    /**
     * Whether or not the image is to be drawn. Assumedly, initially.
     */
    public boolean showImage() default true;

    public static class SetLabel extends ComponentCaller<Image, Label> {

	@Override
	public void call(Image annote, Label c) {
	    c.setImage(annote.value());
	    c.setDisplayImage(annote.showImage());
	}

	@Override
	public String[] getProperties(Label c) {
	    return new String[] {};
	}
    }
}
