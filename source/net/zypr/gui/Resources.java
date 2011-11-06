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

import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.InputStream;

import java.net.URL;

import java.util.Hashtable;

import javax.swing.ImageIcon;

import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.ImageUtils;

public class Resources
{
  private static final String ROOT_PATH = "/net/zypr/resources/";
  private static final Resources INSTANCE = new Resources();
  private static Hashtable _images = new Hashtable();

  private Resources()
  {
    super();
  }

  public static Resources getInstance()
  {
    return INSTANCE;
  }

  public InputStream getHTMLResourceAsStream(String htmlName)
  {
    return getClass().getResourceAsStream(ROOT_PATH + "html/" + htmlName);
  }

  public URL getImageResourceURL()
  {
    return (getImageResourceURL(""));
  }

  public URL getAudioResourceURL(String audioName)
  {
    return (getClass().getResource(ROOT_PATH + "audio/" + audioName));
  }

  public File getAudioResourceFile(String audioName)
  {
    try
      {
        return (new File(getAudioResourceURL(audioName).toURI()));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (null);
  }

  public URL getHTMLResourceURL(String htmlName)
  {
    return (getClass().getResource(ROOT_PATH + "html/" + htmlName));
  }

  public URL getImageResourceURL(String imageName)
  {
    return (getClass().getResource(ROOT_PATH + "image/" + imageName));
  }

  synchronized public ImageIcon getImageIcon(String imageName)
  {
    ImageIcon imageIcon = (ImageIcon) _images.get(imageName + "ImageIcon");
    try
      {
        if (imageIcon == null)
          {
            imageIcon = new ImageIcon(getImageResourceURL(imageName));
            _images.put(imageName + "ImageIcon", imageIcon);
          }
      }
    catch (Exception exception)
      {
        imageIcon = new ImageIcon();
      }
    return (imageIcon);
  }

  synchronized public Image getImage(String imageName)
  {
    return (getImageIcon(imageName).getImage());
  }

  synchronized public BufferedImage getBufferedImage(String imageName)
  {
    return (ImageUtils.getBufferedImage(getImage(imageName)));
  }
}
