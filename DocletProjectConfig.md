# Table of Contents #

  1. Checkout the project
  1. Add tools.jar to your Java Platform configuration in eclipse.
  1. Add codesource of applet project to classpath.


---


### 1) Checkout the project ###


The Checkout URL for the doclet project root is:

https://magicapplet.googlecode.com/svn/doclet/trunk/MagicDoclet


---


### 2) Add tools.jar to your Java Platform configuration in eclipse ###

Goto Global Preferences Page

  1. Window -> Preferences
  1. Java   -> Installed JREs

Add Tools.jar

  1. Select JRE (most likely you only have one)
  1. Edit
  1. Add External Jar
  1. Navigate to eclipse root directory
  1. Find and select: **tools.jar**.  It will be in _{jdk\_root}\lib\_

On my system I use:

_C:\eclipse09\jdk1.6.0\lib\tools.jar_

You could also use the system JDK, instead of the eclipse one if you have it installed.

### Attaching Source & Javadocs ###

  1. Select tools.jar in the JRE jar list
  1. Source Attachment  (Button)
  1. Find **src.zip**:  _{jdk1.6.x}/src.zip_
  1. Javadoc Attachment (Button)
  1. Javadoc in Archive
  1. Use **src.zip** again.


---


### 3) Add source file paths ###

The MagicApplet Project is heavily referenced and has to be attached.  If its incorrect:

  1. Project Properties -> Java Build Path
  1. Projects (Tab)
  1. Add _{MagicApplet project}_

To run the applet, you need to add the source folder location to your run configuration, So it can find the source files to process them.

  1. Try to run PanelDoc the project once, you will get errors, but it will create the run configuration
  1. Drop down arrow next to the Play Button
  1. Run Configurations
  1. Find the Run configuration that is being used for the PanelDoc
  1. ClassPath (tab)
  1. Advanced (button)
  1. External Folder (radio)
  1. Select _{magic applet project}/src/_


---


### 4) Increasing Memory For Doclet Process ###

The Run Configuration may need to be allocated extra memory or the javadoc utility will die as it tries to cache all the class analysis.

  1. Open Run Configurations as per last step
  1. Variables (tab)
  1. VM arguments:

Add two lines:
```
-Xms128m
-Xmx512m
```

Increase the Xmx value until the program works (512 works fine for me at the time of this writing).