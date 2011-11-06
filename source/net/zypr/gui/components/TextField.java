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
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class TextField
  extends JTextField
{
  public TextField(Document document, String text, int columns)
  {
    super(document, text, columns);
    jbInit();
  }

  public TextField(String text, int columns)
  {
    super(text, columns);
    jbInit();
  }

  public TextField(int columns)
  {
    super(columns);
    jbInit();
  }

  public TextField(String text)
  {
    super(text);
    jbInit();
  }

  public TextField()
  {
    super();
    jbInit();
  }

  protected void jbInit()
  {
    setOpaque(false);
    setFont(new Font("Dialog", 1, 16));
    setCaretColor(Color.LIGHT_GRAY);
    setForeground(Color.WHITE);
    setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    this.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent focusEvent)
      {
        this_focusGained(focusEvent);
      }

      public void focusLost(FocusEvent focusEvent)
      {
        this_focusLost(focusEvent);
      }
    });
  }

  private void this_focusGained(FocusEvent focusEvent)
  {
    setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
  }

  private void this_focusLost(FocusEvent focusEvent)
  {
    setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
  }
}
