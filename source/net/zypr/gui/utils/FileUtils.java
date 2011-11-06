/**
 * 
 * Initial version of this code (c) 2009-2011 Media Tuners LLC with a full license to Pioneer Corporation.
 * 
 * Pioneer Corporation licenses this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 */


package net.zypr.gui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils
{
  public static final File USER_HOME_PATH = new File(System.getProperty("user.home"));
  public static final File SYSTEM_TEMP_PATH = new File(System.getProperty("java.io.tmpdir"));
  public static final File ZYPR_PATH = new File(USER_HOME_PATH + File.separator + ".zypr" + File.separator);
  public static final File ZYPR_CACHE_PATH = new File(USER_HOME_PATH + File.separator + ".zypr" + File.separator + "cache" + File.separator);

  public static String generateFilename(String string, String extension)
  {
    return (Integer.toHexString(string.hashCode()) + "." + extension);
  }

  public static boolean deleteAllFiles(String path)
  {
    return (deleteAllFiles(path, ""));
  }

  public static boolean deleteAllFiles(String path, String extension)
  {
    return (deleteAllFiles(new File(path), extension));
  }

  public static boolean deleteAllFiles(File path)
  {
    return (deleteAllFiles(path, ""));
  }

  public static boolean deleteAllFiles(File path, String extension)
  {
    File[] files = path.listFiles();
    if (files == null)
      return (false);
    extension = extension.toLowerCase();
    for (int index = 0; index < files.length; index++)
      if (files[index].toString().toLowerCase().endsWith("." + extension))
        files[index].delete();
    return (true);
  }

  public static void writeBytes(String file, byte[] data)
    throws IOException, FileNotFoundException
  {
    writeBytes(new File(file), data);
  }

  public static void writeBytes(File file, byte[] data)
    throws IOException, FileNotFoundException
  {
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    fileOutputStream.write(data);
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  public static String readTextFromStream(InputStream inputStream)
    throws Exception
  {
    String output = "";
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    String line = "";
    while (null != (line = bufferedReader.readLine()))
      output += line + "\n";
    if (bufferedReader != null)
      bufferedReader.close();
    if (inputStream != null)
      inputStream.close();
    return (output);
  }
}
