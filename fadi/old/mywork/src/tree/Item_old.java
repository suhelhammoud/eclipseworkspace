package tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;

import utils.MapTools;

public class Item_old{
	static String FS=" ";
	static String IS="-";
	
	static int SUPPORT;
	static float CONFIDENCE;

	TreeSet<String > key;
	List<Integer> lines;

	
	public Item_old(String[] arrKey){
		key=new TreeSet<String>(Arrays.asList(arrKey));
	}
	
	public static Item_old getItem(String strkey,String strLines){
		Item_old result=getItem(strkey);
		result.addAllLines(strLines);
		return result;
	}
	
	public static Item_old getItem(String strKey){
		String[] arrKey=strKey.split(FS);
		return new Item_old(arrKey);
	}
	public Item_old(TreeSet<String> stKey){
		this.key=stKey;
	}
	
	public boolean addLine(Integer line){
		return lines.add(line);
	}
	public void addAllLines(String strLines){
		String[] arrLines=strLines.split(FS);
		for (String ln : arrLines) {
			lines.add(Integer.valueOf(ln));
		}
	}

	public String toString(){
		return MapTools.join(FS, key);
	}
	public String lines(){
		return MapTools.join(FS, lines);
	}
	public int length(){
		return key.size();
	}
	public int numOfLines(){
		return lines.size();
	}

	public boolean isFreq(){
		if(lines.size() > SUPPORT)
			return true;
		else
			return false;
	}
	public Item_old merg(Item_old itm){
		// TODO comment them later
		//if(! isFreq() || ! itm.isFreq())return null;
		//if (length()!=itm.length())return null;
		
		TreeSet<String> nkey=new TreeSet<String>(key);
		nkey.addAll(itm.key);
		if(nkey.size() != itm.length()+1 )return null;						
		return new Item_old(nkey);
	}
	public void outputMapper(OutputCollector<IntWritable, Text> output)
	throws IOException{
		String id=toString();
		for (Integer iter : lines) {
			output.collect(new IntWritable(iter), new Text(id));
		}
	}
	
	public void outputReducer(OutputCollector<Text, Text> output)
	throws IOException{
		output.collect(new Text(toString()), new Text(lines()));
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Item_old i1=getItem("1 2 3 4");
		Item_old i2=getItem("1 2 3 5");
		
		Item_old i3=i1.merg(i2);
		System.out.println(i3);
	}
	
	                   

}
