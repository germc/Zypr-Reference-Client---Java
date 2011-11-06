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


package net.zypr.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;

import net.zypr.gui.Resources;
import net.zypr.gui.utils.Debug;

public class ImagePanel
  extends Panel
{
  transient private BufferedImage _bufferedImage;

  public ImagePanel()
  {
  }

  public ImagePanel(BufferedImage bufferedImage)
  {
    this(bufferedImage, true);
  }

  public ImagePanel(String imageFilename)
  {
    this(imageFilename, true);
  }

  public ImagePanel(String imageFilename, boolean border)
  {
    super();
    setImage(imageFilename);
    showBorder(border);
    resizePanel();
  }

  public ImagePanel(BufferedImage bufferedImage, boolean border)
  {
    super();
    setImage(bufferedImage);
    showBorder(border);
    resizePanel();
  }

  private void resizePanel()
  {
    try
      {
        Dimension size = new Dimension(_bufferedImage.getWidth(), _bufferedImage.getHeight());
        setMinimumSize(size);
        setSize(size);
        setPreferredSize(size);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void showBorder(boolean visible)
  {
    if (visible)
      setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    setOpaque(visible);
  }

  public void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);
    if (_bufferedImage != null)
      {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        double scale = Math.min((double) this.getWidth() / _bufferedImage.getWidth(), (double) this.getHeight() / _bufferedImage.getHeight());
        AffineTransform affineTransform = AffineTransform.getTranslateInstance((this.getWidth() - (scale * _bufferedImage.getWidth())) / 2, (this.getHeight() - (scale * _bufferedImage.getHeight())) / 2);
        affineTransform.scale(scale, scale);
        graphics2D.drawRenderedImage(_bufferedImage, affineTransform);
      }
  }

  public void setImage(String imageFilename)
  {
    try
      {
        if (imageFilename != null)
          _bufferedImage = Resources.getInstance().getBufferedImage(imageFilename);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public void setImage(BufferedImage bufferedImage)
  {
    _bufferedImage = bufferedImage;
  }

  public BufferedImage getImage()
  {
    return (_bufferedImage);
  }
}
