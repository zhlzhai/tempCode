package testChip;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** 
 * @project: Test 
 * @author chenssy
 * @date 2013-7-28 
 * @Description: �ļ�ѹ��������
 * 				  ��ָ���ļ�/�ļ���ѹ����zip��rarѹ���ļ�
 */
public class CompressedFileUtil {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public CompressedFileUtil(){
		
	}

	/**
	 * @desc ��Դ�ļ�/�ļ�������ָ����ʽ��ѹ���ļ�,��ʽzip
	 * @param resourePath Դ�ļ�/�ļ���
	 * @param targetPath  Ŀ��ѹ���ļ�����·��
	 * @return void
	 * @throws Exception 
	 */
	public void compressedFile(String resourcesPath,String targetPath) throws Exception{
		File resourcesFile = new File(resourcesPath);     //Դ�ļ�
		File targetFile = new File(targetPath);           //Ŀ��
		//���Ŀ��·�������ڣ����½�
		if(!targetFile.exists()){     
			targetFile.mkdirs();  
		}
		
		String targetName = resourcesFile.getName()+".zip";   //Ŀ��ѹ���ļ���
		FileOutputStream outputStream = new FileOutputStream(targetPath+"\\"+targetName);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));
		
		createCompressedFile(out, resourcesFile, "");
		
		out.close();  
	}
	
	/**
	 * @desc ����ѹ���ļ���
	 * 	             ������ļ��У���ʹ�õݹ飬�����ļ�������ѹ��
	 *       ������ļ���ֱ��ѹ��
	 * @param out  �����
	 * @param file  Ŀ���ļ�
	 * @return void
	 * @throws Exception 
	 */
	public void createCompressedFile(ZipOutputStream out,File file,String dir) throws Exception{
		//�����ǰ�����ļ��У�����н�һ������
		if(file.isDirectory()){
			//�õ��ļ��б���Ϣ
			File[] files = file.listFiles();
			//���ļ�����ӵ���һ�����Ŀ¼
			out.putNextEntry(new ZipEntry(dir+"/"));
			
			dir = dir.length() == 0 ? "" : dir +"/";
			
			//ѭ�����ļ����е��ļ����
			for(int i = 0 ; i < files.length ; i++){
				createCompressedFile(out, files[i], dir + files[i].getName());         //�ݹ鴦��
			}
		}
		else{   //��ǰ�����ļ����������
			//�ļ�������
			FileInputStream fis = new FileInputStream(file);
			
			out.putNextEntry(new ZipEntry(dir));
			//����д����
			int j =  0;
			byte[] buffer = new byte[1024];
			while((j = fis.read(buffer)) > 0){
				out.write(buffer,0,j);
			}
			//�ر�������
			fis.close();
		}
	}
	
	public static void main(String[] args){
		CompressedFileUtil compressedFileUtil = new CompressedFileUtil();
		
		try {
			compressedFileUtil.compressedFile("G:\\xbrl.zip", "F:\\zip");
			System.out.println("ѹ���ļ��Ѿ�����...");
		} catch (Exception e) {
			System.out.println("ѹ���ļ�����ʧ��...");
			e.printStackTrace();
		}
	}
}