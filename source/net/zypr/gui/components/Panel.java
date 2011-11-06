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

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.jdesktop.swingx.JXPanel;

public class Panel
  extends JXPanel
{
  private Timer _timer = null;

  public Panel(LayoutManager layoutManager, boolean isDoubleBuffered)
  {
    super(layoutManager, isDoubleBuffered);
  }

  public Panel(LayoutManager layoutManager)
  {
    super(layoutManager);
  }

  public Panel(boolean isDoubleBuffered)
  {
    super(isDoubleBuffered);
  }

  public Panel()
  {
    super();
  }

  public void showPanel()
  {
    setEnabledComponents(true);
    if (_timer != null)
      {
        _timer.stop();
        _timer = null;
      }
    this.setAlpha(0f);
    this.setVisible(true);
    _timer = new Timer(50, new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            float alpha = getAlpha() + 0.1f;
            if (alpha > 1.0f)
              {
                _timer.stop();
                _timer = null;
                setAlpha(1.0f);
              }
            else
              {
                setAlpha(alpha);
              }
          }
        });
    _timer.start();
  }

  public void hidePanel()
  {
    setEnabledComponents(false);
    if (_timer != null)
      {
        _timer.stop();
        _timer = null;
      }
    this.setAlpha(1.0f);
    _timer = new Timer(50, new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            float alpha = getAlpha() - 0.1f;
            if (alpha < 0.0f)
              {
                _timer.stop();
                _timer = null;
                setAlpha(0.0f);
                setVisible(false);
              }
            else
              {
                setAlpha(alpha);
              }
          }
        });
    _timer.start();
  }

  public void setEnabledComponents(boolean enabled)
  {
    Component[] components = getComponents();
    for (int index = 0; index < components.length; index++)
      if (components[index] instanceof Button)
        components[index].setEnabled(enabled);
  }
}
