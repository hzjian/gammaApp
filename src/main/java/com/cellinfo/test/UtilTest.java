package com.cellinfo.test;

import java.util.UUID;

import org.junit.Test;

public class UtilTest {

	
	@Test
	public void testenum()
	{
		for(int i=0;i<1000;i++)
			System.out.println(generateShortUuid());
	}
	
	public void getUUID()
	{
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(generateShortUuid());
		System.out.println(generateShortUuid());
		System.out.println(generateShortUuid());
		System.out.println(generateShortUuid());
	}
	
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
            "W", "X", "Y", "Z" };  
  
	public String generateShortUuid() {  
	    StringBuffer shortBuffer = new StringBuffer();  
	    String uuid = UUID.randomUUID().toString().replace("-", "");  
	    for (int i = 0; i < 8; i++) {  
	        String str = uuid.substring(i * 4, i * 4 + 4);  
	        int x = Integer.parseInt(str, 16);  
	        shortBuffer.append(chars[x % 0x3E]);  
	    }  
	    return shortBuffer.toString();
	}
}
