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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

public class PressAndHoldButton
  extends Button
{
  private Timer _timerHold = new Timer(2000, new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _timerHold_actionPerformed(actionEvent);
      }
    });

  public PressAndHoldButton(String iconDefault, String iconPressed, String iconDisabled)
  {
    super(iconDefault, iconPressed, iconDisabled);
  }

  public PressAndHoldButton(String iconDefault, String iconPressed)
  {
    super(iconDefault, iconPressed);
  }

  public PressAndHoldButton(String iconDefault)
  {
    super(iconDefault);
  }

  public PressAndHoldButton()
  {
    super();
  }

  protected void jbInit(String iconDefault, String iconPressed, String iconDisabled, float alpha)
  {
    super.jbInit(iconDefault, iconPressed, iconDisabled, alpha);
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

  private void this_mousePressed(MouseEvent mouseEvent)
  {
    _timerHold.start();
  }

  private void this_mouseReleased(MouseEvent mouseEvent)
  {
    _timerHold.stop();
  }

  private void _timerHold_actionPerformed(ActionEvent actionEvent)
  {
    _timerHold.stop();
  }

  @Override
  public void addActionListener(ActionListener actionListener)
  {
    _timerHold.addActionListener(actionListener);
  }
}
