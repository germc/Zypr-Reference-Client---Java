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


package net.zypr.gui.audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;

import net.zypr.gui.utils.Debug;

public class AudioPlayer
{
  private static final AudioPlayer INSTANCE = new AudioPlayer();
  private PlayerThread _thread = null;

  private AudioPlayer()
  {
    super();
    setThread(new PlayerThread());
  }

  public static AudioPlayer getInstance()
  {
    return (INSTANCE);
  }

  public void startPlayback(String filename)
  {
    INSTANCE.getThread().startPlayback(new File(filename));
  }

  public void startPlayback(File file)
  {
    INSTANCE.getThread().startPlayback(file);
  }

  public void stopPlayback()
  {
    INSTANCE.getThread().stopPlayback();
  }

  public void addLineListener(LineListener listener)
  {
    INSTANCE.getThread().addLineListener(listener);
  }

  public void removeLineListener(LineListener listener)
  {
    INSTANCE.getThread().removeLineListener(listener);
  }

  public boolean isPlaying()
  {
    return (INSTANCE.getThread().isPlaying());
  }

  protected void setThread(AudioPlayer.PlayerThread thread)
  {
    _thread = thread;
  }

  protected AudioPlayer.PlayerThread getThread()
  {
    return (_thread);
  }

  private class PlayerThread
    extends Thread
  {
    private Clip _clip = null;
    private File _file = null;

    public PlayerThread()
    {
      super();
      if (_clip == null)
        try
          {
            _clip = AudioSystem.getClip();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
    }

    public void addLineListener(LineListener listener)
    {
      if (_clip != null)
        _clip.addLineListener(listener);
    }

    public void removeLineListener(LineListener listener)
    {
      if (_clip != null)
        _clip.removeLineListener(listener);
    }

    public void startPlayback(File file)
    {
      _file = file;
      stopPlayback();
      run();
    }

    public void stopPlayback()
    {
      if (_clip != null && _clip.isRunning())
        _clip.stop();
      _clip.close();
    }

    public boolean isPlaying()
    {
      return (_clip.isRunning());
    }

    @Override
    public void run()
    {
      super.run();
      if (_clip != null)
        try
          {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(_file);
            _clip.open(audioInputStream);
            _clip.start();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception, 1);
          }
    }

    public void setFile(File file)
    {
      this._file = file;
    }

    public File getFile()
    {
      return (_file);
    }
  }
}
