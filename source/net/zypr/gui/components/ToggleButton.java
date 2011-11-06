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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import net.zypr.gui.Resources;
import net.zypr.gui.utils.AudioUtils;
import net.zypr.gui.utils.Debug;

public class ToggleButton
  extends JToggleButton
{
  private float _alpha = 1.0f;
  private Timer _timerHold = new Timer(2000, new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _timerHold_actionPerformed(actionEvent);
      }
    });

  public ToggleButton(String iconDefault, String iconSelected, String iconDisabled, float alpha)
  {
    super();
    jbInit(iconDefault, iconSelected, iconDisabled, alpha);
  }

  public ToggleButton(String iconDefault, String iconSelected, float alpha)
  {
    super();
    jbInit(iconDefault, iconSelected, null, alpha);
  }

  public ToggleButton(String iconDefault, float alpha)
  {
    super();
    jbInit(iconDefault, null, null, alpha);
  }

  public ToggleButton(String iconDefault, String iconSelected, String iconDisabled, boolean selected, float alpha)
  {
    super("", selected);
    jbInit(iconDefault, iconSelected, iconDisabled, alpha);
  }

  public ToggleButton(String iconDefault, String iconSelected, boolean selected, float alpha)
  {
    super("", selected);
    jbInit(iconDefault, iconSelected, null, alpha);
  }

  public ToggleButton(String iconDefault, boolean selected, float alpha)
  {
    super("", selected);
    jbInit(iconDefault, null, null, alpha);
  }

  public ToggleButton(float alpha)
  {
    super();
    jbInit(null, null, null, alpha);
  }

  public ToggleButton(String iconDefault, String iconSelected, String iconDisabled)
  {
    super();
    jbInit(iconDefault, iconSelected, iconDisabled, 1.0f);
  }

  public ToggleButton(String iconDefault, String iconSelected)
  {
    super();
    jbInit(iconDefault, iconSelected, null, 1.0f);
  }

  public ToggleButton(String iconDefault)
  {
    super();
    jbInit(iconDefault, null, null, 1.0f);
  }

  public ToggleButton(String iconDefault, String iconSelected, String iconDisabled, boolean selected)
  {
    super("", selected);
    jbInit(iconDefault, iconSelected, iconDisabled, 1.0f);
  }

  public ToggleButton(String iconDefault, String iconSelected, boolean selected)
  {
    super("", selected);
    jbInit(iconDefault, iconSelected, null, 1.0f);
  }

  public ToggleButton(String iconDefault, boolean selected)
  {
    super("", selected);
    jbInit(iconDefault, null, null, 1.0f);
  }

  public ToggleButton()
  {
    super();
    jbInit(null, null, null, 1.0f);
  }

  protected void jbInit(String iconDefault, String iconSelected, String iconDisabled, float alpha)
  {
    setSize(0, 0);
    setMargin(new Insets(0, 0, 0, 0));
    setIcons(iconDefault, iconSelected, iconDisabled);
    setText("");
    setFocusPainted(false);
    setBorderPainted(false);
    setFocusable(false);
    setFocusPainted(false);
    setOpaque(false);
    setContentAreaFilled(false);
    setAlpha(alpha);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    this.addMouseListener(new MouseAdapter()
    {
      public void mouseSelected(MouseEvent mouseEvent)
      {
        this_mouseSelected(mouseEvent);
      }

      public void mouseReleased(MouseEvent mouseEvent)
      {
        this_mouseReleased(mouseEvent);
      }

      public void mousePressed(MouseEvent mouseEvent)
      {
        this_mousePressed(mouseEvent);
      }
    });
  }

  public void setIcons(String iconDefault, String iconSelected, String iconDisabled)
  {
    ImageIcon imageIconDefault = null;
    ImageIcon imageIconSelected = null;
    ImageIcon imageIconDisabled = null;
    try
      {
        if (iconDefault != null)
          imageIconDefault = Resources.getInstance().getImageIcon(iconDefault);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (iconSelected != null)
          imageIconSelected = Resources.getInstance().getImageIcon(iconSelected);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (iconDisabled != null)
          imageIconDisabled = Resources.getInstance().getImageIcon(iconDisabled);
        else if (iconDefault != null)
          imageIconDisabled = Resources.getInstance().getImageIcon(iconDefault);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    setIcons(imageIconDefault, imageIconSelected, imageIconDisabled);
  }

  public void setIcons(ImageIcon imageIconDefault, ImageIcon imageIconSelected, ImageIcon imageIconDisabled)
  {
    int width = getWidth();
    int height = getHeight();
    if (imageIconDefault != null)
      {
        setIcon(imageIconDefault);
        if (imageIconDefault.getIconWidth() > width)
          width = imageIconDefault.getIconWidth();
        if (imageIconDefault.getIconHeight() > height)
          height = imageIconDefault.getIconHeight();
      }
    if (imageIconSelected != null)
      {
        setSelectedIcon(imageIconSelected);
        if (imageIconSelected.getIconWidth() > width)
          width = imageIconSelected.getIconWidth();
        if (imageIconSelected.getIconHeight() > height)
          height = imageIconSelected.getIconHeight();
      }
    if (imageIconDisabled != null)
      {
        setDisabledIcon(imageIconDisabled);
        if (imageIconDisabled.getIconWidth() > width)
          width = imageIconDisabled.getIconWidth();
        if (imageIconDisabled.getIconHeight() > height)
          height = imageIconDisabled.getIconHeight();
      }
    else if (imageIconDefault != null)
      {
        setDisabledIcon(imageIconDefault);
      }
    Dimension size = new Dimension(width, height);
    setSize(size);
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
  }

  public void setIconDefault(ImageIcon iconDefault)
  {
    setIcons(iconDefault, null, null);
  }

  public void setIconSelected(ImageIcon iconSelected)
  {
    setIcons(null, iconSelected, null);
  }

  public void setIconDisabled(ImageIcon iconDisabled)
  {
    setIcons(null, null, iconDisabled);
  }

  private void this_mouseSelected(MouseEvent mouseEvent)
  {
    if (isEnabled())
      AudioUtils.play("button-down.wav");
  }

  private void this_mouseReleased(MouseEvent mouseEvent)
  {
    _timerHold.stop();
    if (isEnabled())
      AudioUtils.play("button-up.wav");
  }

  private void this_mousePressed(MouseEvent mouseEvent)
  {
    _timerHold.start();
  }

  private void _timerHold_actionPerformed(ActionEvent actionEvent)
  {
    _timerHold.stop();
  }

  public void addLongClickActionListener(ActionListener actionListener)
  {
    _timerHold.addActionListener(actionListener);
  }

  @Override
  public void paint(Graphics graphics)
  {
    if (_alpha != 1.0f)
      {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _alpha));
        super.paint(graphics2D);
        graphics2D.dispose();
      }
    else
      {
        super.paint(graphics);
      }
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
