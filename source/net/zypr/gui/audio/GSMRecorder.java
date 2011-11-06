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
// Controlling object for a GSM Recorder thread.
// Creates a new recorder thread and uses the "AU" file type
// since this format does not require a pre-determined size
// for output file header. Actual file format is GSM but the
// audio library does not have a native GSM format so use AU
// which is also a streaming format unlike WAVE which requires
// pre-determined data length. The GSMOutputStream does all the
// actual file IO so really doesn't matter except audio library
// will give error if specify a file format that needs a pre-known
// length and none is supplied even though it is not doing the
// actual file generation.
//
// The GSM encoder requires that the audio data be 16bit signed
// little endian single channel PCM. The preferred sampling rate
// is 8000.0 Khz and other rates can be used but the GSM tables
// are optimized for 8000.0 Khz.
public class GSMRecorder
  extends AudioRecorder
{
  private static GSMRecorderThread _audioRecorderThread = null;

  public GSMRecorder()
  {
    super();
    setAudioType("gsm");
  }

  public boolean isAvaiable()
  {
    try
      {
        return (new GSMRecorderThread(new AudioFormat(getSampleRate(), getSampleSizeInBits(), getSampleChannels(), isSampleSigned(), isSampleBigEndian()), AudioFileFormat.Type.AU, getFile()) != null);
      }
    catch (Throwable throwable)
      {
        // TODO Debug.displayStack(this, throwable);
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
            _audioRecorderThread = new GSMRecorderThread(new AudioFormat(getSampleRate(), getSampleSizeInBits(), getSampleChannels(), isSampleSigned(), isSampleBigEndian()), AudioFileFormat.Type.AU, getFile());
            _audioRecorderThread.start();
          }
      }
    catch (Exception exception)
      {
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
