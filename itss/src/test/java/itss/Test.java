package itss;

import java.util.List;

import com.google.common.collect.Lists;

public class Test {

	public static void main(String[] args) {
		List<String> l = Lists.newArrayList();
		l = null;
		
		for(String s : l){
			System.out.println(s);
		}

	}

}
