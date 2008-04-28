package pkg;

import java.lang.reflect.Array;
import java.util.*;

public class Util {

	final public static int supp=2;
	final public static double conf=0.0;
	public static String sep=" ";
	static void subset ( String[] arr, int i, String s ) {
		System.out.println( s );
		for( ; i<arr.length; ++i )
			subset( arr,i+1, s + sep+ arr[i] );
	}
	
	public static String add(String itm,String str){
		String[] arr=itm.split(sep);
		String[] r=new String[arr.length+1];
		System.arraycopy(arr, 0, r, 1, arr.length);
		r[0]=str.trim();
		Arrays.sort(r);

		String result=r[0];
		for (int i = 1; i < r.length; i++) {
			result+=sep+r[i];
		}

		return result;
	}
	public static String del(String itm,String str){
		
		String[] arr=itm.split(sep);
		Arrays.sort(arr);
		if(! itm.contains(str)){
			System.out.println(" can not delete, not found");
			return null;
		}

		String result="";
		for (int i = 0; i < arr.length; i++) {
			if(arr[i] == str.trim())continue;
			result+=sep+arr[i];
		}

		return result.trim();
	}

	public static String subStr(String[] values, int index){
		String result="";
		for (int i = 0; i < values.length; i++) {
			if (i==index)continue;
			result+=sep+values[i];
		}
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str="11 59 41 ";//48 52 65 89 150 193 259 397 479 544 580 587 643 673 904 916 947 1014 1076 1084 1135 1846 1886 1991 2054 2116 2408 2515 2537 3134 3260 3462 4127 4804 4877 5020 5161 5434 6128 6437 6517 7840 7910 7999 8592 8978 9356 10939 12929 13056 14912 14913 14954 15036 15119 15436 15490 15850 16010 16011 16077 16078";
		
		String[] arr=str.split(sep);

		Arrays.sort(arr);
		
//		List<String> lst=Arrays.asList(arr);
//		Collections.sort(lst);
//		
//		arr=(String[])lst.toArray();
		
		
		System.out.println(subStr(arr, 0));
		//arr=str.split(" ");
		subset(arr, 0, "");

	}

}
