package magic.doclet.filesystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SiteFolder {

    String root;

    File rootFolder;
    File imageFolder;
    File stylesFolder;

    public File getRootFile() {
	return new File(root);
    }

    public SiteFolder(String rootURL) {
	root = rootURL;
	getRootFile().mkdirs();
	imageFolder = new File(getRelativePath("images"));
	imageFolder.mkdirs();
	stylesFolder = new File(getRelativePath("styles"));
	stylesFolder.mkdirs();
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

}
