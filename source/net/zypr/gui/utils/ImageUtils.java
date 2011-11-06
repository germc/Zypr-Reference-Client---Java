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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageUtils
{

  public static BufferedImage scale(BufferedImage image, int width, int height)
  {
    BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = scaledImage.createGraphics();
    graphics2D.drawImage(image, 0, 0, width, height, null);
    graphics2D.dispose();
    return (scaledImage);
  }

  public static BufferedImage getBufferedImage(Image image)
  {
    BufferedImage bufferedImage = null;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try
      {
        GraphicsDevice graphicsDevice = ge.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        bufferedImage = graphicsConfiguration.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT);
      }
    catch (Exception exception)
      {
        Debug.displayStack(null, exception);
      }
    if (bufferedImage == null)
      bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    Graphics graphics = bufferedImage.createGraphics();
    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();
    return (bufferedImage);
  }
}
