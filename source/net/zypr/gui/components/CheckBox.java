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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.zypr.gui.Resources;
import net.zypr.gui.utils.AudioUtils;
import net.zypr.gui.utils.Debug;

import org.jdesktop.swingx.JXButton;

public class CheckBox
  extends JXButton
{
  private boolean _checked = false;
  private ActionListener _actionListener = null;

  public CheckBox(String text)
  {
    super(text);
    jbInit();
  }

  public CheckBox(String text, boolean checked)
  {
    super(text);
    _checked = checked;
    jbInit();
  }

  protected void jbInit()
  {
    setFocusPainted(false);
    setBorderPainted(false);
    setFocusable(false);
    setFocusPainted(false);
    setOpaque(false);
    setContentAreaFilled(false);
    setFont(new Font("Dialog", 1, 16));
    setForeground(Color.WHITE);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setHorizontalAlignment(SwingConstants.LEFT);
    setMargin(new Insets(0, 0, 0, 0));
    this.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        this_actionPerformed(actionEvent);
      }
    });
    int width = getWidth();
    int height = getHeight();
    try
      {
        ImageIcon imageIcon = Resources.getInstance().getImageIcon("button-checkbox-" + (!_checked ? "not-" : "") + "selected-up.png");
        setIcon(imageIcon);
        if (imageIcon.getIconWidth() > width)
          width = imageIcon.getIconWidth();
        if (imageIcon.getIconHeight() > height)
          height = imageIcon.getIconHeight();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        ImageIcon imageIcon = Resources.getInstance().getImageIcon("button-checkbox-" + (!_checked ? "not-" : "") + "selected-down.png");
        setPressedIcon(imageIcon);
        if (imageIcon.getIconWidth() > width)
          width = imageIcon.getIconWidth();
        if (imageIcon.getIconHeight() > height)
          height = imageIcon.getIconHeight();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        ImageIcon imageIcon = Resources.getInstance().getImageIcon("button-checkbox-" + (!_checked ? "not-" : "") + "selected-disabled.png");
        setDisabledIcon(imageIcon);
        if (imageIcon.getIconWidth() > width)
          width = imageIcon.getIconWidth();
        if (imageIcon.getIconHeight() > height)
          height = imageIcon.getIconHeight();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    Dimension size = new Dimension((int) (getFontMetrics(getFont()).stringWidth(getText()) * 1.1) + width, height);
    setSize(size);
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    this.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent mouseEvent)
      {
        this_mousePressed(mouseEvent);
      }

      public void mouseReleased(MouseEvent mouseEvent)
      {
        this_mouseReleased(mouseEvent);
      }
    });
  }

  public void setIcons()
  {
    try
      {
        setIcon(Resources.getInstance().getImageIcon("button-checkbox-" + (!_checked ? "not-" : "") + "selected-up.png"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        setPressedIcon(Resources.getInstance().getImageIcon("button-checkbox-" + (!_checked ? "not-" : "") + "selected-down.png"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        setDisabledIcon(Resources.getInstance().getImageIcon("button-checkbox-" + (!_checked ? "not-" : "") + "selected-disabled.png"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public void setActionListener(ActionListener actionListener)
  {
    _actionListener = actionListener;
  }

  private void this_actionPerformed(ActionEvent actionEvent)
  {
    _checked = !_checked;
    setIcons();
    if (_actionListener != null)
      _actionListener.actionPerformed(actionEvent);
  }

  public void setChecked(boolean _checked)
  {
    this._checked = _checked;
    setIcons();
  }

  public boolean isChecked()
  {
    return (_checked);
  }

  private void this_mousePressed(MouseEvent mouseEvent)
  {
    if (isEnabled())
      AudioUtils.play("button-down.wav");
  }

  private void this_mouseReleased(MouseEvent mouseEvent)
  {
    if (isEnabled())
      AudioUtils.play("button-up.wav");
  }
}
