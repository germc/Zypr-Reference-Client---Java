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

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import net.zypr.gui.utils.Debug;

public class TranslucentPanel
  extends JPanel
{
  private BufferedImage _image = null;
  private float _alpha = 0.5f;

  public TranslucentPanel()
  {
    try
      {
        jbInit();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void jbInit()
    throws Exception
  {
    this.setLayout(null);
  }

  @Override
  public void paint(Graphics graphics)
  {
    if (_image == null || _image.getWidth() != getWidth() || _image.getHeight() != getHeight())
      _image = (BufferedImage) createImage(getWidth(), getHeight());
    Graphics2D graphics2D = _image.createGraphics();
    super.paint(graphics2D);
    graphics2D.setClip(graphics.getClip());
    graphics2D.dispose();
    graphics2D = (Graphics2D) graphics.create();
    graphics2D.setComposite(AlphaComposite.SrcOver.derive(_alpha));
    graphics2D.drawImage(_image, 0, 0, null);
  }

  public void setAlpha(float alpha)
  {
    _alpha = alpha;
  }

  public float getAlpha()
  {
    return (_alpha);
  }
}
