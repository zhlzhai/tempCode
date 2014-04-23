package designPattern.collection.copy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Test {
	public static void main(String args[]) {
		/******************************************************/
//		Vector<Object> vecFields = new Vector<Object>();
//
//		vecFields.addElement(new Field("goods_name", "aaaaa"));
//		vecFields.addElement(new Field("goods_test", "bbbbbb"));
//		vecFields.addElement("string_test_sample");
//
//		System.out.println(vecFields.size());// 显示vecFields的长度（这里为3）
//		Field field = (Field) vecFields.elementAt(0);
//		System.out.println(field.getFieldName() + ": " + field.getFieldValue());
//		field = (Field) vecFields.elementAt(1);
//		System.out.println(field.getFieldName() + ": " + field.getFieldValue());
//		System.out.println(vecFields.elementAt(2));
		
		/*********************************************************/
//		ArrayList<String> array = new ArrayList<String>();
//		array.add("hello");
//		array.add("ye");
//		array.add(1, "wo");
		/*********************************************************/
//		ArrayList<String> al = new ArrayList<String>();
//		al.add("sssssssssssssssss");
//		al.add("bbbbbbbbbbbbbbbbbbt");
//		al.add("gggggggggggggggggg");
//		
//		try {
//			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\al.tmp"));
//			oos.writeObject(al);
//			
//			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:\\al.tmp"));
//			
//			ArrayList<String> a = (ArrayList<String>)ois.readObject();
//			for(String s: a)
//			{
//				System.out.println(s);
//			}
//			oos.close();ois.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		/***************************************************************************************/
		String[] stra = new String[4];  
		stra[0] = "mmmmmmmmmm";  
		stra[2] = "nnnnnnnnnn";  
			  
			ObjectOutputStream oos;
			ObjectInputStream ois;
			try {
				oos = new ObjectOutputStream(new FileOutputStream("D:\\sa.tmp"));  
				oos.writeObject(stra);  
				  
				ois = new ObjectInputStream(new FileInputStream("D:\\sa.tmp"));  
				  
				String[] str  = (String[])ois.readObject();  
				for(String s: str)  
				{  
				    System.out.println(s);  
				}
				oos.close();ois.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			

	}
}

class Field {
	private String fieldName;
	private String fieldValue;

	public Field(String fieldName, String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}
}
