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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.zypr.api.audio.gsm0610.DetectClick;
import net.zypr.api.audio.gsm0610.Main;
import net.zypr.gui.Configuration;
import net.zypr.gui.utils.Debug;

public class GSMOutputStream
  extends ByteArrayOutputStream
{
  // Output file objects
  private FileOutputStream fileStream;
  private DataOutputStream dataStream;
  private static Boolean isFileOpen;
  // Debug files
  private FileOutputStream CodecFileStream;
  private DataOutputStream CodecDataStream;
  private FileOutputStream GSMInFileStream;
  private DataOutputStream GSMInDataStream;
  // Buffer with vector of shorts for the GSM encoder.
  // May be partially filled if not enough bytes sent at
  // a time, so account for continue filling.
  private static final int SourceBufferSize = 160;
  private short[] SourceBuffer = new short[SourceBufferSize];
  private int SourceBufferIdx;
  // The GSM encoded buffer that is written to file.
  private byte[] GSMFrame = new byte[33];
  // If odd number of bytes sent then use this buffer to hold for next "write"
  private byte UnderFlowByte;
  private Boolean isUnderFlow;
  // GSM Encoder
  private Main GSMEncoder = new Main();
  // First packet click detection flag
  private boolean isFirstFrame = true;
  private DetectClick ClickDetector = new DetectClick();
  // Debug switches
  private static final boolean USE_CLICKDETECT = true;
  private static final boolean DEBUG_AUDIO = false;
  private static final boolean DEBUG_PRINT = false;
  // ********************************************
  // Constructor that takes path to file as parameter.
  // ********************************************

  public GSMOutputStream(String filePath)
    throws FileNotFoundException
  {
    super();
    fileStream = new FileOutputStream(filePath);
    dataStream = new DataOutputStream(fileStream);
    if (Configuration.getInstance().getBooleanProperty("add-gsm-header", false))
      {
        try
          {
            dataStream.write("#!GSM610".getBytes());
          }
        catch (IOException ioe)
          {
            // TODO: Add catch code
            ioe.printStackTrace();
          }
      }
    SourceBufferIdx = 0;
    isUnderFlow = false;
    isFileOpen = true;
    // Initialize the GSM Encoder
    GSMEncoder.Init();
    // Click detection setup
    isFirstFrame = true; // falg to remove intiial codec noise
    ClickDetector.Init(1600); // set for 0.2 seconds
    // See if debug
    if (DEBUG_AUDIO)
      {
        String dbgFilePath = filePath + "_codec.pcm";
        CodecFileStream = new FileOutputStream(dbgFilePath);
        CodecDataStream = new DataOutputStream(CodecFileStream);
        dbgFilePath = filePath + "_gsmin.pcm";
        GSMInFileStream = new FileOutputStream(dbgFilePath);
        GSMInDataStream = new DataOutputStream(GSMInFileStream);
      }
  }
  // ********************************************
  // Close the current output file
  // ********************************************

  @Override
  public void close()
    throws IOException
  {
    fileStream.close();
    isFileOpen = false;
    if (DEBUG_AUDIO)
      {
        CodecFileStream.close();
        GSMInFileStream.close();
      }
  }
  // ********************************************
  // Open new output file.
  // If one currently open, it is first closed.
  // ********************************************

  public void open(String filePath)
    throws IOException, FileNotFoundException
  {
    if (isFileOpen)
      fileStream.close();
    // OPen new file
    fileStream = new FileOutputStream(filePath);
    dataStream = new DataOutputStream(fileStream);
    // Initialize GSM encoder for new stream
    GSMEncoder.Init();
    // Reset runtime flags for new Stream
    SourceBufferIdx = 0;
    isUnderFlow = false;
    isFileOpen = true;
    isFirstFrame = true; // falg to remove intiial codec noise
    ClickDetector.Init(1600); // set for 0.2 seconds
    // See if debug
    if (DEBUG_AUDIO)
      {
        String dbgFilePath = filePath + ".pcm";
        CodecFileStream = new FileOutputStream(dbgFilePath);
        CodecDataStream = new DataOutputStream(CodecFileStream);
      }
  }
  // ********************************************
  // Get an array of bytes to encode
  // ********************************************

  @Override
  public void write(byte[] b, int off, int len)
  {
    try
      {
        encodeToGSM(b, off, len);
      }
    catch (IOException ioException)
      {
        // TODO Auto-generated catch block
        ioException.printStackTrace();
      }
  }
  // ********************************************
  // Get an single byte to encode
  // ********************************************

  @Override
  public void write(int b)
  {
    byte ba[] = new byte[1];
    ba[0] = (byte) b;
    try
      {
        encodeToGSM(ba, 0, 1);
      }
    catch (IOException ioException)
      {
        // TODO Auto-generated catch block
        ioException.printStackTrace();
      }
  }
  // ********************************************
  // Encode an array of PCM data byte-pairs
  // and write out to file. Throws an IOException
  // if file not open or error in writing data.
  // ********************************************

  private void encodeToGSM(byte[] b, int off, int len)
    throws IOException
  {
    // First check that file is open
    if (!isFileOpen)
      {
        IOException e = new IOException("GSM output file Not Open");
        throw e;
      }
    // If is First frame and "off" is zero, then
    // advance "off" by 10 packets to account for
    // observered noise in first packets from codec.
    if (isFirstFrame && (off == 0) && (len > 20))
      {
        isFirstFrame = false;
        off = 20;
        if (DEBUG_PRINT)
          Debug.print("Start Audio Detected by BSM encoder\n");
      }
    //    DEBUG: Record input from Codec
    if (DEBUG_AUDIO)
      {
        CodecDataStream.write(b, off, len);
      }
    // Encode in GSM Frames
    // It is assumed that the data will be 16bit PCM delivered as byte
    // pairs.
    // This routine accounts for case where half a 16bit PCM may be received
    // as input parameter by using the UnderFlowBuffer and isUnderFlow flag.
    while (off < len)
      {
        // see if enough data to Encode (must be exactly SourceBufferSize
        // packets in buffer)
        if (SourceBufferIdx == SourceBufferSize)
          {
            // Do click detection and wait for valid audio
            // Since sending such small samples, juse throw away the
            // entire sample until valuid audio is found.
            DetectClick.Status audioClickStatus = DetectClick.Status.VALID_AUDIO_DETECTED;
            if (USE_CLICKDETECT)
              audioClickStatus = ClickDetector.FindClick(SourceBuffer, SourceBufferSize);
            if (audioClickStatus == DetectClick.Status.VALID_AUDIO_DETECTED)
              {
                if (DEBUG_PRINT)
                  Debug.print("Good Audio Detected by GSM encoder\n");
                //    DEBUG: Record input to GSM encoder
                if (DEBUG_AUDIO)
                  {
                    for (int i = 0; i < SourceBuffer.length; i++)
                      GSMInDataStream.writeShort(SourceBuffer[i]);
                  }
                // Encode PCM into GSM
                GSMEncoder.Encode(SourceBuffer, GSMFrame);
                // Write out to File
                dataStream.write(GSMFrame);
              }
            // Reset source buffer index.
            SourceBufferIdx = 0;
          }
        // Put new audio data into the SourceBuffer
        // See if use underflow byte
        // Assume data is already in little endian order.
        if (isUnderFlow)
          {
            // Build short out of two bytes - first is underflow byte then
            // one new byte.
            SourceBuffer[SourceBufferIdx++] = (short) (((short) (UnderFlowByte) << 8) + (short) (b[off++] & 0xff));
            isUnderFlow = false;
          }
        else if ((off + 1) < len)
          {
            // Build short out of two new audio bytes
            //SourceBuffer[SourceBufferIdx++] = (short) ((b[off] << 8) | b[off + 1]);
            SourceBuffer[SourceBufferIdx++] = (short) (((short) (b[off]) << 8) + (short) (b[off + 1] & 0xff));
            off += 2;
          }
        else
          {
            // Don't have two bytes left so save last byte for next time.
            UnderFlowByte = b[off++];
            isUnderFlow = true;
          }
      }
  }
}
