package pkg;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.io.Text;


public class PkgItem {
	final static String isep=" ";
	final static String sep="-"; 
	private String[] arrKey;
	private String[] arrValue;
	private int freq;



	//1		,2 5:1
	public PkgItem(String item){
		set(item);
	}
	public PkgItem(){

	}
	public PkgItem(String[] key,String[] value,int freq){
		set(key, value, freq);
	}
	public PkgItem(String keyStr,String valueStr,int freq){
		set(keyStr,valueStr,freq);
	}

	public int kLength(){
		return arrKey.length;
	}
	public int vLength(){
		return arrValue.length;
	}
	public PkgItem set(String[] key,String[] value, int freq){
		this.arrKey=key;
		Arrays.sort(key);

		this.arrValue=value;
		Arrays.sort(value);

		this.freq=freq;

		return this;
	}
	public PkgItem set(String keyStr,String valueStr, int freq){
		if(keyStr==null)
			return set("".split(isep),valueStr.split(isep),freq);
		else
			return set(keyStr.split(isep),valueStr.split(isep),freq);
	}
	public PkgItem set(String str){
		String[] arr=str.split(sep);
		String k=arr[0].trim();
		//String v=arr[1].trim();
		String v=arr[1];
		int f=1;
		if(arr.length==3)
			f=Integer.valueOf(arr[2].trim());

		set(k,v,f);
		return this;

	}
	public boolean contains(List<String> s){
		HashSet<String> valueSet=new HashSet<String>(Arrays.asList(arrValue));
		return valueSet.containsAll(s);
	}
	public int inc(int i){
		freq+=i;
		return freq;
	}

	public double confidence(int base){
		return (double)freq/base;
	}


	public static String arrayToString(String[] arr){
		String result="";
		for (String  i : arr) {
			result+=i+isep;
		}
		return result.trim();
	}
	public String toString(){
		return id()+sep+freq;
	}
	public String id(){
		return getKey()+sep+getValue();
	}
	public String getKey(){
		return arrayToString(arrKey);
	}

	public String getValue(){
		return arrayToString(arrValue);
	}
	public int getFreq(){
		return freq;
	}

	public Text collect(String msg){
		return new Text(msg+toString());
	}
	public Text collect(double conf){
		return new Text(toString()+sep+conf);
	}
	public Text collect(){
		return collect("");
	}
	public Text collectKey(String msg){
		return new Text(msg+getKey());
	}

	public Text collectKey(){
		return collectKey("");
	}

	public Text collectValue(String msg){
		return new Text(msg+getValue());
	}
	public Text collectValue(){
		return collectValue("");
	}

	public PkgItem subItem(String vItm){
		if(vLength() <2 ){
			System.out.println("One item value");
			return null;
		}		
		return subItem(Arrays.binarySearch(arrValue, vItm.trim()));
	}
	public PkgItem subItem(int index){
		if(vLength() <2 ){
			System.out.println("One item value");
			return null;
		}
		String[] newKey=new String[arrKey.length+1];
		newKey[0]=arrValue[index];
		System.arraycopy(arrKey, 0, newKey, 1, arrKey.length);

		String[] newValue=new String[arrValue.length-1];
		if(index==0)
			System.arraycopy(arrValue, 1, newValue, 0, newValue.length);
		else if(index==arrValue.length-1)
			System.arraycopy(arrValue, 0, newValue, 0, newValue.length);
		else{
			System.arraycopy(arrValue, 0, newValue, 0, index);
			System.arraycopy(arrValue, index+1, newValue, index, newValue.length-index);
		}
		return new PkgItem(newKey,newValue,freq);
	}
	public PkgItem[] subItems(){
		return subItems(false);
	}
	
	public PkgItem[] subItems(boolean isInit){
		PkgItem[] result;
		int start=0;
		if (! isInit)
			while( arrKey[arrKey.length-1].compareToIgnoreCase(arrValue[start]) > 0)
				start++;

		result=new PkgItem[vLength()-start];
		for (int i=start; i<vLength();i++){		
			result[i-start]=subItem(i);
		}
		return result;
	}



	public static void main(String[] args){
		//System.out.println(add("1 2 6 3", "4"));
		//System.out.println(del("1 2 6 3", "2"));
		PkgItem i1=new PkgItem("4-2 6 3 7 8 2");
		//Item i2=new Item(null,"1 2 4 3",1);

		//System.out.println(i1);
		i1.inc(77);
		System.out.println(i1);
		System.out.println();


		PkgItem[] itms=i1.subItems(true);
		for (PkgItem i : itms) {
		System.out.println(i);
		}

//		System.out.println(i2.subItem(0));
//		System.out.println(Arrays.toString(i1.arrValue));
	}
}

