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

import java.lang.reflect.Method;

import java.net.URI;

public class WebBrowser
{
  private static final String[] BROWSERS =
  { "moblin-web-browser", "firefox", "opera", "safari", "chromium-browser", "konqueror", "epiphany", "seamonkey", "galeon", "kazehakase", "mozilla", "netscape", "camino", "omniweb", "icab" };

  public static void openURL(String url)
    throws UnsupportedOperationException
  {
    if (url == null)
      return;
    try
      {
        Class desktopClass = Class.forName("java.awt.Desktop");
        Method browseMethod = desktopClass.getDeclaredMethod("browse", new Class[]
            { URI.class });
        browseMethod.invoke(null, new Object[]
            { new URI(url) });
      }
    catch (Exception desktopBrowseException)
      {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS"))
          {
            try
              {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]
                    { String.class });
                openURL.invoke(null, new Object[]
                    { url });
              }
            catch (Exception FileManagerOpenURLException)
              {
                execBrowser(url);
              }
          }
        else if (osName.startsWith("Windows"))
          {
            try
              {
                Runtime.getRuntime().exec(new String[]
                    { "rundll32", "url.dll,FileProtocolHandler", url });
              }
            catch (Exception exception)
              {
                throw new UnsupportedOperationException();
              }
          }
        else
          {
            execBrowser(url);
          }
      }
  }

  private static void execBrowser(String url)
    throws UnsupportedOperationException
  {
    for (int index = 0; index < BROWSERS.length; index++)
      {
        try
          {
            if (Runtime.getRuntime().exec(new String[]
                { "which", BROWSERS[index] }).waitFor() == 0)
              {
                Runtime.getRuntime().exec(new String[]
                    { BROWSERS[index], url });
                return;
              }
          }
        catch (Exception exception)
          {
            throw new UnsupportedOperationException();
          }
      }
    throw new UnsupportedOperationException();
  }
}
