package server;

import java.util.TreeMap;
import com.oreilly.servlet.*;
import org.gjt.mm.mysql.Driver;
public class BitLab {
	public static void main(String[] args){
	
//		ISpaceConfig c=new ISpaceConfig();
//		ISpaceConfig c2=new ISpaceConfig();

		//System.out.println(c);
		System.out.println();
		System.out.println(ISpaceConfig.get());
		System.out.println();
		System.out.println(ISpaceConfig.getList("web_temp_path_upload"));
		
//		String s = null;
//		if (s == null) {
//			System.out.println("null");
//		}
//		
//		String[] parts="dfd".split("#");
//		for (int i = 0; i < parts.length; i++) {
//			if(parts[i].trim().equals(""))continue;
//			System.out.println(parts[i]);
//		}
		
	}
}
