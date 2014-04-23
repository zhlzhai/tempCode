package designPattern.clientIo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;



/**
 * @author zhailzh
 * */
public class IOUtil {

	// 缓存的大小
	static final int DEFAULT_BUFFER_SIZE = 1024;

	public static final Charset UTF8_CHARSET = Charset.forName("utf-8");

	// 1. 首先是关闭，流分类字节流和字符流，两个类别同时有两个类型输出和输入，一共四个类型

	/**
	 * 关闭输入流。与{@link java.io.InputStream#close()}一样，除了不抛出IO异常。
	 * 
	 * @param input
	 *            输入流
	 */
	public static void close(InputStream input) {
		if (input == null) {
			return;
		}
		try {
			input.close();
		} catch (IOException ioException) {
		}
	}

	/**
	 * 关闭输出流。与{@link java.io.OutputStream#close()}一样，除了不抛出IO异常。
	 * 
	 * @param output
	 *            输出流
	 */
	public static void close(OutputStream output) {
		if (output == null) {
			return;
		}
		try {
			output.close();
		} catch (IOException ioException) {
		}
	}

	/**
	 * 无条件关闭输入流。与{@link java.io.Reader#close()}一样，除了不抛出IO异常。
	 * 
	 * @param input
	 *            输入流
	 */
	public static void close(Reader input) {
		if (input == null) {
			return;
		}
		try {
			input.close();
		} catch (IOException ioe) {
		}
	}

	/**
	 * 无条件关闭输出流。与{@link java.io.Writer#close()}一样，除了不抛出IO异常。
	 * 
	 * @param output
	 *            输出流
	 */
	public static void close(Writer output) {
		if (output == null) {
			return;
		}
		try {
			output.close();
		} catch (IOException ioe) {
		}
	}

	// 2. 输入流到输出流

	public static long copy(final InputStream input, final OutputStream output)
			throws IOException {

		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public static void copy(InputStream input, Writer output)
			throws IOException {
		InputStreamReader in = new InputStreamReader(input);
		copy(in, output);

	}

	public static void copy(InputStream input, Writer output, String encoding)
			throws IOException {
		if (encoding == null) {
			copy(input, output);
		} else {
			InputStreamReader in = new InputStreamReader(input, encoding);
			copy(in, output);
		}
	}

	public static void copy(Reader input, OutputStream output)
			throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(output);
		copy(input, out);
		// XXX Unless anyone is planning on rewriting OutputStreamWriter, we
		// have to flush here.
		out.flush();

	}

	public static void copy(Reader input, OutputStream output, String encoding)
			throws IOException {
		if (encoding == null) {
			copy(input, output);
		} else {
			OutputStreamWriter out = new OutputStreamWriter(output, encoding);
			copy(input, out);
			// XXX Unless anyone is planning on rewriting OutputStreamWriter,
			// we have to flush here.
			out.flush();
		}
	}

	public static long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return count;
	}

	// 3.读取操作
	/**
	 * 从文件中得到字节流
	 */
	public static ByteArrayOutputStream readByteStreamFromFile(File newFile)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream stream = new FileInputStream(newFile);
		copy(stream, out);
		close(stream);
		return out;
	}

	/**
	 * 从文件路径中得到字节流
	 * */
	public static ByteArrayOutputStream readByteStreamFromFile(String filePath)
			throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			return readByteStreamFromFile(file);
		} else {
			throw new IOException(filePath + " not exist an file ");
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * 将输入流的内容转换为字节数组
	 */
	public static byte[] toByteArray(Reader input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static byte[] toByteArray(Reader input, String encoding)
			throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output, encoding);
		return output.toByteArray();
	}

	/**
	 * 将字符串转换为字节数组。
	 * 
	 * @deprecated
	 */
	public static byte[] toByteArray(String input) throws IOException {
		return input.getBytes();
	}

	/**
	 * 字符串转化为输入流
	 * */
	public static InputStream toInputStream(String input) {
		byte[] bytes = input.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * 字符串转化为输入流
	 * */
	public static InputStream toInputStream(String input, String encoding)
			throws IOException {
		byte[] bytes = encoding != null ? input.getBytes(encoding) : input
				.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	@Deprecated
	public static String toString(byte[] input, String encoding)
			throws IOException {
		if (encoding == null) {
			return new String(input);
		} else {
			return new String(input, encoding);
		}
	}

	/**
	 * 将输入流转化为字符串
	 * */
	public static String toString(InputStream in) throws IOException {

		StringWriter sw = new StringWriter();
		copy(in, sw);
		return sw.toString();
	}

	/**
	 * 将输入流的内容按指定编码格式转换为字符串。
	 */
	public static String toString(InputStream input, String encoding)
			throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	/**
	 * 将输入流的内容转换为字符串
	 */
	public static String toString(Reader input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}

	/**
	 * 写入字节数组
	 * */
	public static void write(byte[] data, OutputStream output)
			throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * 写入字节数组
	 * */
	public static void write(byte[] data, Writer output) throws IOException {
		if (data != null) {
			output.write(new String(data));
		}
	}

	/**
	 * 写入字节数组
	 * */
	public static void write(byte[] data, Writer output, String encoding)
			throws IOException {
		if (data != null) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(new String(data, encoding));
			}
		}
	}

	/**
	 * 写入字符数组
	 */
	public static void write(char[] data, Writer output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * 写入字符数组
	 */
	public static void write(char[] data, OutputStream output)
			throws IOException {
		if (data != null) {
			output.write(new String(data).getBytes());
		}
	}

	/**
	 * 写入字符数组
	 */
	public static void write(char[] data, OutputStream output, String encoding)
			throws IOException {
		if (data != null) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(new String(data).getBytes(encoding));
			}
		}
	}

	
	public static void write(CharSequence data, Writer output)
			throws IOException {
		if (data != null) {
			write(data.toString(), output);
		}
	}

	
	public static void write(CharSequence data, OutputStream output)
			throws IOException {
		if (data != null) {
			write(data.toString(), output);
		}
	}

	
	public static void write(CharSequence data, OutputStream output,
			String encoding) throws IOException {
		if (data != null) {
			write(data.toString(), output, encoding);
		}
	}

	
	public static void write(String data, Writer output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}
}
