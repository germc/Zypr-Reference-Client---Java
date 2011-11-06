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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

public class ScrollPane
  extends Panel
{
  private Panel _panelScrollBar = new Panel();
  private JScrollPane _scrollPane = new JScrollPane();
  private Button _buttonScrollDown = new Button("button-page-down-window-up.png", "button-page-down-window-down.png");
  private Button _buttonScrollUp = new Button("button-page-up-window-up.png", "button-page-up-window-down.png");

  public ScrollPane()
  {
    super();
    jbInit();
  }

  private void jbInit()
  {
    _panelScrollBar.setPreferredSize(new Dimension(_buttonScrollDown.getWidth(), _buttonScrollDown.getHeight() * 2));
    this.setLayout(new BorderLayout());
    this.setOpaque(false);
    _panelScrollBar.setLayout(new BorderLayout());
    _panelScrollBar.setOpaque(false);
    _scrollPane.setOpaque(false);
    _scrollPane.getViewport().setOpaque(false);
    _scrollPane.setWheelScrollingEnabled(false);
    _scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    _scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    _scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    _panelScrollBar.add(_buttonScrollUp, BorderLayout.NORTH);
    _panelScrollBar.add(_buttonScrollDown, BorderLayout.SOUTH);
    this.add(_panelScrollBar, BorderLayout.EAST);
    this.add(_scrollPane, BorderLayout.CENTER);
  }

  public void setScrollBarVisible(boolean visible)
  {
    _panelScrollBar.setVisible(visible);
  }

  public boolean isScrollBarVisible()
  {
    return (_panelScrollBar.isVisible());
  }

  public void addScrollUpButtonActionListener(ActionListener actionListener)
  {
    _buttonScrollUp.addActionListener(actionListener);
  }

  public void addScrollDownButtonActionListener(ActionListener actionListener)
  {
    _buttonScrollDown.addActionListener(actionListener);
  }

  public void setScrollAreaBorder(Border border)
  {
    _scrollPane.setBorder(border);
  }

  public void setSize(int width, int height)
  {
    if (isScrollBarVisible() && height < _buttonScrollDown.getHeight() * 2)
      height = _buttonScrollDown.getHeight() * 2;
    this.setSize(width, height);
  }

  public Component addToViewport(Component component)
  {
    return (_scrollPane.getViewport().add(component));
  }

  private int scroll(int value)
  {
    _scrollPane.getVerticalScrollBar().setValue(_scrollPane.getVerticalScrollBar().getValue() + value);
    return (_scrollPane.getVerticalScrollBar().getValue());
  }

  public void scrollToStart()
  {
    _scrollPane.getVerticalScrollBar().setValue(0);
  }

  public void scrollToEnd()
  {
    _scrollPane.getVerticalScrollBar().setValue(_scrollPane.getVerticalScrollBar().getMaximum());
  }

  public int scrollDown(int value)
  {
    return (scroll(value));
  }

  public int scrollUp(int value)
  {
    return (scroll(value * -1));
  }

  public void setBounds(int x, int y, int width, int height)
  {
    if (isScrollBarVisible() && height < _buttonScrollDown.getHeight() * 2)
      height = _buttonScrollDown.getHeight() * 2;
    super.setBounds(x, y, width, height);
  }
}
