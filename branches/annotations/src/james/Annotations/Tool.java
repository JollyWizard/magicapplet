package james.Annotations;

import java.lang.reflect.Field;

import magicofcalculus.panels.SecantApproxPanel;

public class Tool {

    public static void main(String[] args) {
	ComponentCaller.debug = true;
	SecantApproxPanel sap = QuickInit.Build(SecantApproxPanel.class, (Object) null);
	System.out.println(sap);
	for (Field f : sap.getClass().getFields()) {
	    System.out.println(f.getName());
	    AutoCaller.m.autoCall(f, sap);
	    System.out.println();
	}
    }

}
