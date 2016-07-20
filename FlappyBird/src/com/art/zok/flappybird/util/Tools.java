package com.art.zok.flappybird.util;

public class Tools {
	public static int[] splitInteger(int i) {
		char[] chars = Integer.toString(i).toCharArray();
		int[] result = new int[chars.length]; 
		for(int j = 0; j < chars.length; j++) {
			result[j] = chars[j]- 48;
		}
		return result;
	}
}
