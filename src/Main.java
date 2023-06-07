import java.io.*;
import java.util.*;
//work hard hard work pays you

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        String ss = "This is Love";
        char[] str = new String(ss).toCharArray();
        String temp = "";
        String result = "";
        for(int i = 0 ; i < ss.length(); i++)
        {
            temp += str[i];
            if(str[i] == ' ')
            {
                String s = reverse(temp);
                temp = "";
                result += s;
            }
        }
     String s =  reverse(temp);

        result += " " + s;
        System.out.println(result);

    }

    public static String reverse(String s){
        return new String((new StringBuffer(s)).reverse());
    }

}