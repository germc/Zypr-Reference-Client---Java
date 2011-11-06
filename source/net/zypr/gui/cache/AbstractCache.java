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

public abstract class AbstractCache
{
  private long _maxCacheSize = 262144000;

  public abstract void emptyCache();

  public abstract void freeUpCache();

  public abstract void freeUpMemory();

  public abstract void put(String id, byte[] byteArray, Object bufferedImage);

  public abstract Object get(String id);

  public void setMaxCacheSizeAsBytes(long _maxCacheSize)
  {
    this._maxCacheSize = _maxCacheSize;
  }

  public long getMaxCacheSizeAsBytes()
  {
    return _maxCacheSize;
  }

  public void setMaxCacheSizeAsKilobytes(long _maxCacheSize)
  {
    this._maxCacheSize = _maxCacheSize * 1024;
  }

  public long getMaxCacheSizeAsKilobytes()
  {
    return _maxCacheSize / 1024;
  }

  public void setMaxCacheSizeAsMegabytes(long _maxCacheSize)
  {
    this._maxCacheSize = _maxCacheSize * 1048576;
  }

  public long getMaxCacheSizeAsMegabytes()
  {
    return _maxCacheSize / 1048576;
  }
}
