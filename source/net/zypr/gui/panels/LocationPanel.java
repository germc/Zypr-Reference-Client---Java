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


package net.zypr.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.zypr.gui.Configuration;
import net.zypr.gui.components.Panel;
import net.zypr.gui.utils.Debug;

public class LocationPanel
  extends Panel
{
  private Color _backgroundColor = Configuration.getInstance().getColorProperty("location-bar-background-color", new Color(0, 0, 0, 196));

  public LocationPanel()
  {
    super();
    setOpaque(false);
    try
      {
        Dimension size = new Dimension(400, 28);
        setMinimumSize(size);
        setSize(size);
        setPreferredSize(size);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  @Override
  protected void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);
    Graphics2D graphics2D = (Graphics2D) graphics;
    double alphaDifference = (double) _backgroundColor.getAlpha() / (double) getWidth();
    for (int x = 0; x < this.getWidth(); x++)
      {
        graphics2D.setPaint(new Color(_backgroundColor.getRed(), _backgroundColor.getGreen(), _backgroundColor.getBlue(), _backgroundColor.getAlpha() - (int) (alphaDifference * x)));
        graphics2D.fillRect(x, 0, 1, this.getHeight());
      }
  }
}
