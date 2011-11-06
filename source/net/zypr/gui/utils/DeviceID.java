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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class DeviceID
{
  private static final File PATH = new File(FileUtils.ZYPR_PATH, File.separator + "deviceids" + File.separator);

  public static String load(String username)
  {
    username = "" + username.hashCode();
    String deviceID = null;
    BufferedReader bufferedReader = null;
    try
      {
        bufferedReader = new BufferedReader(new FileReader(PATH.toString() + File.separator + username));
        deviceID = bufferedReader.readLine();
      }
    catch (Exception exception)
      {
        Debug.displayStack(null, exception, 1);
      }
    finally
      {
        if (bufferedReader != null)
          {
            try
              {
                bufferedReader.close();
              }
            catch (Exception exception)
              {
                Debug.displayStack(null, exception, 1);
              }
            bufferedReader = null;
          }
      }
    return (deviceID);
  }

  public static void save(String username, String deviceID)
  {
    username = "" + username.hashCode();
    PATH.mkdirs();
    BufferedWriter bufferedWriter = null;
    try
      {
        bufferedWriter = new BufferedWriter(new FileWriter(PATH.toString() + File.separator + username));
        bufferedWriter.write(deviceID);
      }
    catch (Exception exception)
      {
        Debug.displayStack(null, exception, 1);
      }
    finally
      {
        if (bufferedWriter != null)
          {
            try
              {
                bufferedWriter.close();
              }
            catch (Exception exception)
              {
                Debug.displayStack(null, exception, 1);
              }
            bufferedWriter = null;
          }
      }
  }
}
