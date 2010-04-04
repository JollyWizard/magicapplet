package james.annot.labels;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import james.ComponentCaller;
import magicofcalculus.components.Label;

/**
 * Contains a url string for a labels image
 * 
 * @author James Arlow
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Text {

    /**
     * The label's image url string
     * 
     * @return
     */
    public String value();

    public static class SetLabel extends ComponentCaller<Text, Label> {

	@Override
	public void call(Text annote, Label c) {
	    c.setText(annote.value());
	}

	@Override
	public String[] getProperties(Label c) {
	    return new String[] {/* Expose label text for debugging info */};
	}
    }
}
