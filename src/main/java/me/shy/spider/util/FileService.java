/**
 * @Since: 2020-03-27 21:48:50
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class FileService {

	public static void writeObjectToFile(Object object, File file)throws FileNotFoundException, IOException{
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file, false));
		outputStream.writeObject(object);
		outputStream.flush();
		outputStream.close();
	}

	public static Object readObjectFromFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
		Object object = inputStream.readObject();
		inputStream.close();
		return object;
	}
}

