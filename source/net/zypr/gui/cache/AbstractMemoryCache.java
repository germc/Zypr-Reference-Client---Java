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

import java.io.ByteArrayInputStream;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.zypr.gui.cache.image.ImageReader;
import net.zypr.gui.utils.Debug;

public class AbstractMemoryCache
  extends AbstractCache
{
  private Map<String, Object> imageMap = new HashMap<String, Object>();
  private LinkedList<String> imageMapAccessQueue = new LinkedList<String>();
  private int imageSize = 0;
  private Map<String, byte[]> byteMap = new HashMap<String, byte[]>();
  private LinkedList<String> byteMapAccessQueue = new LinkedList<String>();
  private int byteSize = 0;

  public AbstractMemoryCache()
  {
    super();
    setMaxCacheSizeAsMegabytes(64);
  }

  @Override
  public void emptyCache()
  {
    imageMap.clear();
    imageMapAccessQueue.clear();
    imageSize = 0;
    byteMap.clear();
    byteMapAccessQueue.clear();
    byteSize = 0;
  }

  @Override
  public void freeUpCache()
  {
    imageMap.clear();
    System.gc();
  }

  @Override
  public void freeUpMemory()
  {
    imageMap.clear();
    System.gc();
  }

  @Override
  public Object get(String id)
  {
    id = "" + id.hashCode();
    synchronized (imageMap)
      {
        if (imageMap.containsKey(id))
          {
            imageMapAccessQueue.remove(id);
            imageMapAccessQueue.addLast(id);
            return imageMap.get(id);
          }
      }
    synchronized (byteMap)
      {
        if (byteMap.containsKey(id))
          {
            Object bufferedImage = null;
            try
              {
                byteMapAccessQueue.remove(id);
                byteMapAccessQueue.addLast(id);
                bufferedImage = ImageReader.getImage(new ByteArrayInputStream(byteMap.get(id)));
                addToImageCache(id, bufferedImage);
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
            return bufferedImage;
          }
      }
    return null;
  }

  @Override
  public void put(String id, byte[] byteArray, Object bufferedImage)
  {
    id = "" + id.hashCode();
    synchronized (byteMap)
      {
        while (byteSize >= getMaxCacheSizeAsBytes())
          {
            String oldTileID = byteMapAccessQueue.removeFirst();
            byte[] oldByteImgage = byteMap.remove(oldTileID);
            byteSize -= oldByteImgage.length;
          }
        byteMap.put(id, byteArray);
        byteSize += byteArray.length;
        byteMapAccessQueue.addLast(id);
      }
    addToImageCache(id, bufferedImage);
  }

  private void addToImageCache(final String id, final Object img)
  {
    synchronized (imageMap)
      {
        while (imageSize >= getMaxCacheSizeAsBytes())
          {
            String oldTileID = imageMapAccessQueue.removeFirst();
            imageMap.remove(oldTileID);
            /*-
            Object oldImage = imageMap.remove(oldTileID);
            imageSize -= oldImage.getWidth() * oldImage.getHeight() * 4;
            -*/
          }
        imageMap.put(id, img);
        /*-
        imageSize += img.getWidth() * img.getHeight() * 4;
        -*/
        imageMapAccessQueue.addLast(id);
      }
  }
}
