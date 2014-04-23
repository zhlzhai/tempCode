package testChip;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestChip {

	public static  void main(String[] agrs){
//		File file = new File("G:/java/workspace/testChip/src/testChip/testcontent.txt");
//		try {
//			InputStream input = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	List list = new List();	
	int i = 0 ;
	while(true){
		list.add(String.valueOf(i++).intern());
	}
	}
	
	
}
