package server;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;


public class ISpaceConfig {

	
	private static final String configFile="C://Users/Suhel/workspace/ispace/configure/ispace.properties";
	
	private static final Map<String, String> keyMap=new HashMap<String, String>();
	
	static {
		StringBuffer sb=new StringBuffer();
		try {

			BufferedReader in= new BufferedReader(new FileReader(configFile));
			//read the file lines withoud the comments to the lines list
			String s;
			while( (s=in.readLine()) != null)  {
				if(s.trim().startsWith("#") ||
						s.trim().equals(""))
					continue;
				String[] parts = s.trim().split("#");
				sb.append(parts[0].trim()+" ");
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] items=sb.toString().split("-");
		for (int i = 0; i < items.length; i++) {
			if(items[i].trim().equals(""))continue;
			String[] kv=items[i].trim().split("=");
			if(kv.length <2 )continue;
			keyMap.put(kv[0].trim(),kv[1].trim());
		}
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String get(String key){
		return keyMap.get(key).trim();
	}
	public static String[] getArray(String key){
		if(keyMap == null)new ISpaceConfig();
		String value=keyMap.get(key);
		if(value == null)return null;
		return value.split(" ");
	}
	public static List<String> getList(String key){
		return Arrays.asList(getArray(key));
	}
	
	public String set(String key, String value){
		if(keyMap == null)new ISpaceConfig();
		return keyMap.put(key, value);
	}
	
	public static String get() {
		if(keyMap == null)new ISpaceConfig();
		StringBuffer sb=new StringBuffer();
		for (Map.Entry<String, String> e : keyMap.entrySet()) {
			sb.append(e.getKey()+" : "+ e.getValue()+"\n");
		}
		return sb.toString();
	}
	
}
