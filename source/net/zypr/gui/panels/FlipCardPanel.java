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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import net.zypr.gui.components.Panel;
import net.zypr.gui.utils.Debug;

public class FlipCardPanel
  extends Panel
{
  protected Timer _timer = null;

  public FlipCardPanel()
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
    this.setSize(512, 50);
    this.setOpaque(false);
  }

  public void showPanel()
  {
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
            float alpha = getAlpha() + 0.15f;
            if (alpha > 1.0f)
              {
                _timer.stop();
                _timer = null;
                setAlpha(1.0f);
                panelVisible();
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
            float alpha = getAlpha() - 0.15f;
            if (alpha < 0.0f)
              {
                _timer.stop();
                _timer = null;
                setAlpha(0.0f);
                setVisible(false);
                panelInvisible();
              }
            else
              {
                setAlpha(alpha);
              }
          }
        });
    _timer.start();
  }

  protected void panelVisible()
  {
  }

  protected void panelInvisible()
  {
  }
}
