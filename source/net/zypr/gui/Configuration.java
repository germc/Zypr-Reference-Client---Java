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


package net.zypr.gui;

import java.awt.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import net.zypr.gui.utils.Debug;

public class Configuration
  extends Properties
{
  private static final Configuration INSTANCE = new Configuration();
  private static final char[] HEX_CHARS =
  { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private static String _propertiesFilePath = System.getProperty("user.home") + File.separator + ".zypr" + File.separator + "zypr.xml";
  private static Cipher _cipherEncrypter;
  private static Cipher _cipherDecrypter;

  private Configuration()
  {
    super();
    _propertiesFilePath = System.getProperty("user.home") + File.separator + ".zypr" + File.separator + "zypr.xml";
    load();
    try
      {
        byte[] salt =
        { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
        char[] passPhrase =
        { (char) 42, 'R', (char) 42, 'A', (char) 42, 'N', (char) 42, 'A', (char) 42, 'C', (char) 42, 'N', (char) 42, 'A', (char) 42, 'V', (char) 42, 'I', (char) 42, 'K', (char) 42 };
        KeySpec keySpec = new PBEKeySpec(passPhrase, salt, 42);
        SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        _cipherEncrypter = Cipher.getInstance(secretKey.getAlgorithm());
        _cipherDecrypter = Cipher.getInstance(secretKey.getAlgorithm());
        AlgorithmParameterSpec algorithmParameterSpec = new PBEParameterSpec(salt, 42);
        _cipherEncrypter.init(Cipher.ENCRYPT_MODE, secretKey, algorithmParameterSpec);
        _cipherDecrypter.init(Cipher.DECRYPT_MODE, secretKey, algorithmParameterSpec);
      }
    catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException)
      {
        Debug.displayStack(this, invalidAlgorithmParameterException);
      }
    catch (InvalidKeySpecException invalidKeySpecException)
      {
        Debug.displayStack(this, invalidKeySpecException);
      }
    catch (InvalidKeyException invalidKeyException)
      {
        Debug.displayStack(this, invalidKeyException);
      }
    catch (NoSuchAlgorithmException noSuchAlgorithmException)
      {
        Debug.displayStack(this, noSuchAlgorithmException);
      }
    catch (NoSuchPaddingException noSuchPaddingException)
      {
        Debug.displayStack(this, noSuchPaddingException);
      }
  }

  public static synchronized Configuration getInstance()
  {
    return (INSTANCE);
  }

  public synchronized void load()
  {
    try
      {
        final FileInputStream fileInputStream = new FileInputStream(_propertiesFilePath);
        loadFromXML(fileInputStream);
        fileInputStream.close();
        Debug.print("CONFIGURATION LOADED FROM " + _propertiesFilePath);
      }
    catch (FileNotFoundException fileNotFoundException)
      {
        save();
        Debug.displayStack(this, fileNotFoundException);
      }
    catch (IOException ioException)
      {
        Debug.displayStack(this, ioException);
      }
  }

  public synchronized void save()
  {
    try
      {
        final FileOutputStream fileOutputStream = new FileOutputStream(_propertiesFilePath);
        storeToXML(fileOutputStream, "ZYPR Local Configuration File");
        fileOutputStream.close();
        Debug.print("CONFIGURATION SAVED INTO " + _propertiesFilePath);
      }
    catch (FileNotFoundException fileNotFoundException)
      {
        Debug.displayStack(this, fileNotFoundException);
      }
    catch (IOException ioException)
      {
        Debug.displayStack(this, ioException);
      }
  }

  public synchronized String getEncryptedProperty(String key)
  {
    return (getEncryptedProperty(key, null));
  }

  public String getEncryptedProperty(String key, String defaultValue)
  {
    String value = getProperty(key, defaultValue);
    try
      {
        if (value != null && !value.equals(defaultValue) && value.startsWith("=") && value.endsWith("="))
          value = new String(_cipherDecrypter.doFinal(hexStringToBytes(value.replaceAll("=", ""))), "UTF8");
      }
    catch (UnsupportedEncodingException unsupportedEncodingException)
      {
        Debug.displayStack(this, unsupportedEncodingException);
      }
    catch (BadPaddingException badPaddingException)
      {
        Debug.displayStack(this, badPaddingException);
      }
    catch (IllegalBlockSizeException illegalBlockSizeException)
      {
        Debug.displayStack(this, illegalBlockSizeException);
      }
    catch (IOException ioException)
      {
        Debug.displayStack(this, ioException);
      }
    return (value);
  }

  public synchronized void setEncryptedProperty(String key, String value)
  {
    try
      {
        setProperty(key, "=" + bytesToHexString(_cipherEncrypter.doFinal(value.getBytes("UTF8"))) + "=");
      }
    catch (UnsupportedEncodingException unsupportedEncodingException)
      {
        Debug.displayStack(this, unsupportedEncodingException);
      }
    catch (BadPaddingException badPaddingException)
      {
        Debug.displayStack(this, badPaddingException);
      }
    catch (IllegalBlockSizeException illegalBlockSizeException)
      {
        Debug.displayStack(this, illegalBlockSizeException);
      }
  }

  public synchronized int getIntegerProperty(String key, int defaultValue)
  {
    int value = defaultValue;
    try
      {
        value = Integer.parseInt(getProperty(key, "" + defaultValue).toString());
      }
    catch (NumberFormatException numberFormatException)
      {
        Debug.displayStack(this, numberFormatException, 1);
      }
    return (value);
  }

  public synchronized void setIntegerProperty(String key, int value)
  {
    setProperty(key, "" + value);
  }

  public synchronized float getFloatProperty(String key, float defaultValue)
  {
    float value = defaultValue;
    try
      {
        value = Float.parseFloat(getProperty(key, "" + defaultValue).toString());
      }
    catch (NumberFormatException numberFormatException)
      {
        Debug.displayStack(this, numberFormatException, 1);
      }
    return (value);
  }

  public synchronized void setIntegerProperty(String key, float value)
  {
    setProperty(key, "" + value);
  }

  public synchronized Color getColorProperty(String key, Color defaultValue)
  {
    Color value = defaultValue;
    try
      {
        String[] values = getProperty(key, "" + value.getRed() + "," + value.getGreen() + "," + value.getBlue() + "," + value.getAlpha()).split(",");
        value = new Color(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    return (value);
  }

  public synchronized void setColorProperty(String key, Color value)
  {
    setProperty(key, value.getRed() + "," + value.getGreen() + "," + value.getBlue() + "," + value.getAlpha());
  }

  public synchronized boolean getBooleanProperty(String key, boolean defaultValue)
  {
    boolean value = defaultValue;
    String strValue = getProperty(key, "" + defaultValue).toString().trim().toLowerCase();
    if (strValue.equals("true"))
      value = true;
    else if (strValue.equals("false"))
      value = false;
    return (value);
  }

  public synchronized void setBooleanProperty(String key, boolean value)
  {
    setProperty(key, "" + value);
  }

  public synchronized void setPropertiesFilePath(String propertiesFilePath)
  {
    _propertiesFilePath = propertiesFilePath;
    load();
  }

  public synchronized void setStringProperty(String key, String value)
  {
    setProperty(key, value);
  }

  public synchronized String getStringProperty(String key, String value)
  {
    String path = getProperty(key, value);
    return path;
  }

  public synchronized String getPropertiesFilePath()
  {
    return (_propertiesFilePath);
  }

  private synchronized String bytesToHexString(byte[] byteArray)
  {
    StringBuffer stringBuffer = new StringBuffer(byteArray.length * 2);
    for (int i = 0; i < byteArray.length; i++)
      {
        stringBuffer.append(HEX_CHARS[(byteArray[i] & 0xf0) >>> 4]);
        stringBuffer.append(HEX_CHARS[byteArray[i] & 0x0f]);
      }
    return (stringBuffer.toString());
  }

  private synchronized byte[] hexStringToBytes(String hexString)
  {
    byte byteArray[] = new byte[hexString.length() / 2];
    for (int index = 0; index < (hexString.length() / 2); index++)
      {
        byte firstNibble = Byte.parseByte(hexString.substring(2 * index, 2 * index + 1), 16);
        byte secondNibble = Byte.parseByte(hexString.substring(2 * index + 1, 2 * index + 2), 16);
        int finalByte = (secondNibble) | (firstNibble << 4);
        byteArray[index] = (byte) finalByte;
      }
    return (byteArray);
  }

  @Override
  public synchronized String getProperty(String key, String defaultValue)
  {
    String value = super.getProperty(key, defaultValue);
    setProperty(key, value);
    return (value);
  }
}
