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


package net.zypr.gui.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class RadioStationGenreListRowPanel
  extends JPanel
{
  private Label _labelLineNumber = new Label(SwingConstants.RIGHT);
  private Label _labelGenreName = new Label();
  private Separator _separatorLine = new Separator();

  public RadioStationGenreListRowPanel()
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
    this.setLayout(new BorderLayout());
    this.setBackground(Color.DARK_GRAY);
    _labelLineNumber.setOpaque(false);
    _labelLineNumber.setText("0 -");
    _labelLineNumber.setFont(new Font("Dialog", 1, 16));
    _labelGenreName.setFont(new Font("Dialog", 0, 24));
    _separatorLine.setBounds(0, 39, 320, 1);
    this.add(_separatorLine, BorderLayout.SOUTH);
    this.add(_labelLineNumber, BorderLayout.WEST);
    this.add(_labelGenreName, BorderLayout.CENTER);
  }

  public void setLineNumber(int lineNumber)
  {
    _labelLineNumber.setText(lineNumber + " -");
  }

  public void setGenreName(String contactName)
  {
    _labelGenreName.setText(contactName);
  }
}
