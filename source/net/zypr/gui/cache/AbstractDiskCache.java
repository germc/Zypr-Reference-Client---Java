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


package net.zypr.gui.cache;

import java.io.File;
import java.io.FileOutputStream;

import net.zypr.gui.cache.image.ImageReader;
import net.zypr.gui.utils.Debug;

public abstract class AbstractDiskCache
  extends AbstractCache
{
  private File _cachePath;

  public AbstractDiskCache(File cachePath)
  {
    _cachePath = cachePath;
    _cachePath.mkdirs();
  }

  public void setCachePath(File _cachePath)
  {
    this._cachePath = _cachePath;
    _cachePath.mkdirs();
  }

  public File getCachePath()
  {
    return _cachePath;
  }

  public void setCachePath(String cachePath)
  {
    this._cachePath = new File(cachePath);
    _cachePath.mkdirs();
  }

  public String getCachePathAsString()
  {
    return _cachePath.toString();
  }

  private String getFilePath(String id)
  {
    return (_cachePath + File.separator + id);
  }

  public long getCacheDirectorySize()
  {
    File[] files = _cachePath.listFiles();
    long size = 0;
    for (int index = 0; index < files.length; index++)
      size += (files[index].isFile() ? files[index].length() : 0L);
    return (size);
  }

  @Override
  public void emptyCache()
  {
    File[] files = _cachePath.listFiles();
    for (int index = 0; index < files.length; index++)
      files[index].delete();
  }

  @Override
  public void freeUpMemory()
  {
    System.gc();
  }

  @Override
  public void freeUpCache()
  {
    /*-
    File[] files = _cachePath.listFiles();
    long size = 0;
    for (int index = 0; index < files.length; index++)
      size += (files[index].isFile() ? files[index].length() : 0L);
    Arrays.sort(files, new Comparator<File>()
      {
        public int compare(File file1, File file2)
        {
          return Long.valueOf(file2.lastModified()).compareTo(file1.lastModified());
        }
      });
    boolean lastResult = true;
    while (size >= getMaxCacheSizeAsBytes() && files.length > 0 && lastResult)
      {
        long currentFileSize = (files[files.length - 1].isFile() ? files[files.length - 1].length() : 0L);
        lastResult = files[files.length - 1].delete();
        if (lastResult)
          {
            size -= currentFileSize;
            File[] newFiles = new File[files.length - 1];
            System.arraycopy(files, 0, newFiles, 0, newFiles.length);
            files = newFiles;
          }
      }
    -*/
  }

  @Override
  public Object get(String id)
  {
    if (id == null)
      return (null);
    id = "" + id.hashCode();
    Object bufferedImage = null;
    String filepath = getFilePath(id);
    File file = new File(filepath);
    if (file.exists())
      {
        try
          {
            bufferedImage = ImageReader.getImage(file);
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    return (bufferedImage);
  }

  @Override
  public void put(String id, byte[] byteArray, Object bufferedImage)
  {
    id = "" + id.hashCode();
    String filepath = getFilePath(id);
    try
      {
        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    freeUpCache();
  }
}
