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

public abstract class AudioRecorder
{
  private String _audioType = "gsm";
  private float _sampleRate = 8000.0F;
  private int _sampleSizeInBits = 16;
  private int _sampleChannels = 1;
  private boolean _sampleSigned = true;
  private boolean _sampleBigEndian = false;
  private int _frameSizeInBits = 520;
  private double _frameRate = 1625.0;
  private File _file;

  public AudioRecorder()
  {
    super();
    _file = new File(System.getProperty("user.home") + File.separator + ".zypr" + File.separator + "zypr.audio");
    _file.deleteOnExit();
  }

  public void setAudioType(String audioType)
  {
    _audioType = audioType;
  }

  public String getAudioType()
  {
    return _audioType;
  }

  public void setSampleRate(float sampleRate)
  {
    _sampleRate = sampleRate;
  }

  public float getSampleRate()
  {
    return _sampleRate;
  }

  public void setSampleSizeInBits(int sampleSizeInBits)
  {
    _sampleSizeInBits = sampleSizeInBits;
  }

  public int getSampleSizeInBits()
  {
    return _sampleSizeInBits;
  }

  public void setSampleChannels(int sampleChannels)
  {
    _sampleChannels = sampleChannels;
  }

  public int getSampleChannels()
  {
    return _sampleChannels;
  }

  public void setSampleSigned(boolean sampleSigned)
  {
    _sampleSigned = sampleSigned;
  }

  public boolean isSampleSigned()
  {
    return _sampleSigned;
  }

  public void setSampleBigEndian(boolean sampleBigEndian)
  {
    _sampleBigEndian = sampleBigEndian;
  }

  public boolean isSampleBigEndian()
  {
    return _sampleBigEndian;
  }

  public void setFrameSizeInBits(int frameSizeInBits)
  {
    _frameSizeInBits = frameSizeInBits;
  }

  public int getFrameSizeInBits()
  {
    return _frameSizeInBits;
  }

  public void setFrameRate(double frameRate)
  {
    _frameRate = frameRate;
  }

  public double getFrameRate()
  {
    return _frameRate;
  }

  public void setFilename(String filename)
  {
    _file = new File(filename);
    _file.deleteOnExit();
  }

  public String getFilename()
  {
    return _file.getAbsolutePath();
  }

  public void setFile(File file)
  {
    _file = file;
    _file.deleteOnExit();
  }

  public File getFile()
  {
    return _file;
  }

  public abstract void startRecording()
    throws AudioRecorderException;

  public abstract void stopRecording();

  public abstract boolean isAvaiable();
}
