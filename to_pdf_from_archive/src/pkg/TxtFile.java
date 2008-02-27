package pkg;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TxtFile extends FileFilter {

	@Override
	public boolean accept(File f) {
	       if (f.isDirectory()) {
	            return true;
	        }

	        String extension = getExtension(f).toLowerCase();
	        if (extension != null) {
	            if (extension.equals("txt")) {
	                    return true;
	            } else {
	                return false;
	            }
	        }

	        return false;	}

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Only TXT files";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
