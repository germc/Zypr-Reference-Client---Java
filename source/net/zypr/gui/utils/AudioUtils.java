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


package net.zypr.gui.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.zypr.gui.Configuration;
import net.zypr.gui.Resources;

public class AudioUtils
{
  public static synchronized void play(final String audioName)
  {
    if (Configuration.getInstance().getBooleanProperty("gui-sound-effects-enabled", true))
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              Clip clip = AudioSystem.getClip();
              AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Resources.getInstance().getAudioResourceURL(audioName));
              clip.open(audioInputStream);
              clip.start();
              Thread.sleep((clip.getMicrosecondLength() / 100) + 250);
              clip.stop();
              audioInputStream.close();
              clip.close();
            }
          catch (Exception exception)
            {
              Debug.displayStack(this, exception, 1);
            }
        }
      }).start();
  }
}
