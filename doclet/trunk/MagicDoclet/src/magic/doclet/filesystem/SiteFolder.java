package magic.doclet.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.stream.FileImageInputStream;

public class SiteFolder {

    public String root;
    public static String images = "images";
    public static String styles = "styles";

    public File getRootFile() {
	return new File(root);
    }

    public SiteFolder(String rootURL) {
	root = rootURL;
	getRootFile().mkdirs();
	new File(getRelativePath(images)).mkdirs();
	new File(getRelativePath(styles)).mkdirs();
    }

    public void writeFile(PageFile pf) {
	File f = new File(getRelativePath(pf.path));
	FileWriter fw = null;
	try {
	    fw = new FileWriter(f);
	    fw.write(pf.getContents());
	    fw.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public String getRelativePath(String s) {
	return root + "//" + s;
    }

    public void copyResource(Class sameLoader, String path, String resource) {
	try {
	    File f = new File(sameLoader.getResource(resource).toURI());
	    FileInputStream fis = new FileInputStream(f);
	    FileOutputStream fos = new FileOutputStream(new File(
		    getRelativePath(path), f.getName()));
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = fis.read(buf)) > 0) {
		fos.write(buf, 0, len);
	    }
	    fis.close();
	    fos.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
