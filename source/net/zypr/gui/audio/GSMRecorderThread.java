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
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import net.zypr.gui.utils.Debug;

public class GSMRecorderThread
  extends Thread
{
  private TargetDataLine _targetDataLine;
  private AudioFileFormat.Type _audioFileFormatType;
  private AudioInputStream _audioInputStream;
  private GSMOutputStream _outputStream;

  public GSMRecorderThread(AudioFormat _audioFormat, AudioFileFormat.Type _audioFileFormatType, File _file)
    throws LineUnavailableException, FileNotFoundException
  {
    super();
    DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, _audioFormat);
    this._targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
    this._targetDataLine.open(_audioFormat);
    this._audioFileFormatType = _audioFileFormatType;
    this._audioInputStream = new AudioInputStream(_targetDataLine);
    this._outputStream = new GSMOutputStream(_file.getPath());
  }

  @Override
  public void run()
  {
    super.run();
    try
      {
        AudioSystem.write(_audioInputStream, _audioFileFormatType, _outputStream);
      }
    catch (IOException ioException)
      {
        Debug.displayStack(this, ioException);
      }
  }

  @Override
  public synchronized void start()
  {
    _targetDataLine.start();
    super.start();
  }

  public void stopRecording()
  {
    _targetDataLine.stop();
    _targetDataLine.close();
    _targetDataLine = null;
    try
      {
        _audioInputStream.close();
      }
    catch (IOException ioException)
      {
        Debug.displayStack(this, ioException);
      }
    finally
      {
        _audioInputStream = null;
      }
  }
}
