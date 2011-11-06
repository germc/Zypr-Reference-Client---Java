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

import java.math.BigInteger;

import java.security.MessageDigest;

public class StringUtils
{

  public static String arrayToString(String[] array, String separator)
  {
    if (array == null || separator == null)
      return null;
    else if (array.length == 0)
      return ("");
    StringBuilder stringBuilder = new StringBuilder(array[0]);
    for (int index = 1; index < array.length; index++)
      {
        stringBuilder.append(separator);
        stringBuilder.append(array[index]);
      }
    return (stringBuilder.toString());
  }

  public static boolean isEmpty(String string)
  {
    if (string == null)
      return (true);
    string = string.trim();
    return (string.length() == 0);
  }

  public static String[] subarray(String[] array, int offset)
  {
    return (subarray(array, offset, array.length - offset));
  }

  public static String[] subarray(String[] array, int offset, int length)
  {
    String[] result = new String[length];
    System.arraycopy(array, offset, result, 0, length);
    return (result);
  }

  public static String toMD5Hash(String text)
  {
    try
      {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(text.getBytes());
        return (String.format("%032x", new BigInteger(1, messageDigest.digest())));
      }
    catch (Exception exception)
      {
        return (null);
      }
  }
}
