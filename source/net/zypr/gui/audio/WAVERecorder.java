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

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import net.zypr.gui.utils.Debug;

public class WAVERecorder
  extends AudioRecorder
{
  private static WAVERecorderThread _audioRecorderThread = null;

  public WAVERecorder()
  {
    super();
    setAudioType("wav");
  }

  public boolean isAvaiable()
  {
    try
      {
        return (new WAVERecorderThread(new AudioFormat(getSampleRate(), getSampleSizeInBits(), getSampleChannels(), isSampleSigned(), isSampleBigEndian()), AudioFileFormat.Type.WAVE, getFile()) != null);
      }
    catch (Throwable throwable)
      {
        Debug.displayStack(this, throwable);
      }
    return (false);
  }

  public void startRecording()
    throws AudioRecorderException
  {
    try
      {
        if (_audioRecorderThread == null)
          {
            _audioRecorderThread = new WAVERecorderThread(new AudioFormat(getSampleRate(), getSampleSizeInBits(), getSampleChannels(), isSampleSigned(), isSampleBigEndian()), AudioFileFormat.Type.WAVE, getFile());
            _audioRecorderThread.start();
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        throw new AudioRecorderException(exception);
      }
  }

  public void stopRecording()
  {
    if (_audioRecorderThread != null)
      _audioRecorderThread.stopRecording();
    _audioRecorderThread = null;
  }
}
