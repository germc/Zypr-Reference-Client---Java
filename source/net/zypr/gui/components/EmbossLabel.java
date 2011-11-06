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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.SwingConstants;

public class EmbossLabel
  extends Label
{
  private int _tracking = 0;
  private int _leftX = 1;
  private int _leftY = 1;
  private int _rightX = 1;
  private int _rightY = 1;
  private Color _colorLeft = Color.BLACK;
  private Color _colorRight = Color.BLACK;

  public EmbossLabel(String text, int tracking)
  {
    super(text);
    _tracking = tracking;
  }

  public EmbossLabel(String string)
  {
    super(string);
  }

  public EmbossLabel(int horizontalAlignment, String text)
  {
    super(text, horizontalAlignment);
  }

  public EmbossLabel(int horizontalAlignment)
  {
    super(horizontalAlignment);
  }

  public EmbossLabel()
  {
    super();
  }

  public void setLeftShadow(int x, int y, Color color)
  {
    _leftX = x;
    _leftY = y;
    _colorLeft = color;
  }

  public void setRightShadow(int x, int y, Color color)
  {
    _rightX = x;
    _rightY = y;
    _colorRight = color;
  }

  public Dimension getPreferredSize()
  {
    String text = getText();
    FontMetrics fm = this.getFontMetrics(getFont());
    int width = fm.stringWidth(text);
    width += (text.length() - 1) * _tracking;
    width += _leftX + _rightX;
    int height = fm.getHeight();
    height += _leftY + _rightY;
    return new Dimension(width, height);
  }

  public void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);
    ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    char[] chars = getText().toCharArray();
    FontMetrics fontMetrics = this.getFontMetrics(getFont());
    int height = fontMetrics.getAscent();
    graphics.setFont(getFont());
    int x = 0;
    if (getHorizontalAlignment() == SwingConstants.RIGHT)
      x = getWidth() - fontMetrics.stringWidth(getText());
    for (int index = 0; index < chars.length; index++)
      {
        char ch = chars[index];
        int width = fontMetrics.charWidth(ch) + _tracking;
        graphics.setColor(_colorLeft);
        graphics.drawString("" + chars[index], x - _leftX, height - _leftY);
        graphics.setColor(_colorRight);
        graphics.drawString("" + chars[index], x + _rightX, height + _rightY);
        graphics.setColor(getForeground());
        graphics.drawString("" + chars[index], x, height);
        x += width;
      }
  }
}
