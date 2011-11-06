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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import net.zypr.api.API;
import net.zypr.gui.Configuration;
import net.zypr.gui.components.Label;
import net.zypr.gui.utils.AudioUtils;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.TTSPlayer;

public class MessagePanel
  extends FlipCardPanel
{
  private Label _labelMessage = new Label(SwingConstants.CENTER);
  private String _message = null;

  public MessagePanel()
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
    this.add(_labelMessage, BorderLayout.CENTER);
    _labelMessage.setFont(new Font("Dialog", 1, 20));
    _labelMessage.setForeground(Configuration.getInstance().getColorProperty("top-bar-font-color", Color.WHITE));
  }

  public void setNotificationMessage(String message)
  {
    setNotificationMessage(message, true, null);
  }

  public void setNotificationMessage(final String message, boolean ttsEnabled, String sound)
  {
    if (this.isVisible())
      {
        _message = message;
        hidePanel();
      }
    else
      {
        _labelMessage.setText(message);
        showPanel();
      }
    if (sound != null)
      AudioUtils.play(sound);
    if (ttsEnabled)
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              TTSPlayer.getInstance().loadPlayfile(API.getInstance().getVoice().tts(message).getAudioURI());
            }
          catch (Exception exception)
            {
              Debug.displayStack(this, exception);
            }
        }
      }).start();
  }

  protected void panelVisible()
  {
  }

  protected void panelInvisible()
  {
    if (_message != null)
      {
        _labelMessage.setText(_message);
        _message = null;
        showPanel();
      }
  }
}
