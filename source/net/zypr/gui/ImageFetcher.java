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


package net.zypr.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import net.zypr.api.API;
import net.zypr.api.utils.Debug;
import net.zypr.gui.cache.AbstractDiskCache;
import net.zypr.gui.cache.MarkerDiskCache;

import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class ImageFetcher
{
  private static final ImageFetcher INSTANCE = new ImageFetcher();
  private ExecutorService _executorService;
  private AbstractDiskCache _diskCache = new MarkerDiskCache();
  private int _threadPoolSize = Configuration.getInstance().getIntegerProperty("number-of-markers-fetch-simultaneously", 16);
  private static BlockingQueue<String> _queue = new ArrayBlockingQueue<String>(Configuration.getInstance().getIntegerProperty("number-of-markers-fetch-simultaneously", 16));

  private ImageFetcher()
  {
    super();
  }

  public static ImageFetcher getInstance()
  {
    return (INSTANCE);
  }

  protected synchronized ExecutorService getService()
  {
    if (_executorService == null)
      {
        _executorService = Executors.newFixedThreadPool(_threadPoolSize, new ThreadFactory()
            {
              private int count = 0;

              public Thread newThread(Runnable runnable)
              {
                Thread thread = new Thread(runnable, "image-pool-" + count++);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.setDaemon(true);
                return (thread);
              }
            });
      }
    return (_executorService);
  }

  public void setCachePath(String cachePath)
  {
    _diskCache.setCachePath(cachePath);
  }

  public BufferedImage getImageImmediately(String imageURL)
  {
    return (getImageImmediately(imageURL, 0, 0, null));
  }

  public BufferedImage getImageImmediately(String imageURL, BufferedImage placeholderImage)
  {
    return (getImageImmediately(imageURL, 0, 0, placeholderImage));
  }

  public BufferedImage getImageImmediately(String imageURL, int width, int height)
  {
    return (getImageImmediately(imageURL, width, height, null));
  }

  public BufferedImage getImageImmediately(String imageURL, int width, int height, BufferedImage placeholderImage)
  {
    BufferedImage bufferedImage = (BufferedImage) _diskCache.get(imageURL);
    if (bufferedImage == null)
      try
        {
          _diskCache.put(imageURL, API.getInstance().getBytes(imageURL), null);
          bufferedImage = (BufferedImage) _diskCache.get(imageURL);
        }
      catch (Exception exception)
        {
          Debug.displayStack(this, exception);
        }
    if (bufferedImage == null)
      return (placeholderImage);
    if (width > 0 && height > 0)
      {
        int imageSize = (width > height ? width : height);
        double ratio = 1.0;
        if (bufferedImage.getWidth() > bufferedImage.getHeight())
          ratio = (double) imageSize / (double) bufferedImage.getWidth();
        else
          ratio = (double) imageSize / (double) bufferedImage.getHeight();
        int imageSizeWidth = (int) (bufferedImage.getWidth() * ratio);
        int imageSizeHeight = (int) (bufferedImage.getHeight() * ratio);
        if (ratio < 1.0)
          bufferedImage = GraphicsUtilities.createThumbnail(bufferedImage, imageSizeWidth, imageSizeHeight);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawImage(bufferedImage, width - imageSizeHeight / 2, height - imageSizeHeight / 2, imageSizeWidth, imageSizeHeight, null);
        graphics2D.dispose();
      }
    return (bufferedImage);
  }

  public BufferedImage getImage(String imageURL)
  {
    return (getImage(imageURL, 0, 0, null));
  }

  public BufferedImage getImage(String imageURL, BufferedImage placeholderImage)
  {
    return (getImage(imageURL, 0, 0, placeholderImage));
  }

  public BufferedImage getImage(String imageURL, int width, int height)
  {
    return (getImage(imageURL, width, height, null));
  }

  public BufferedImage getImage(String imageURL, int width, int height, BufferedImage placeholderImage)
  {
    BufferedImage bufferedImage = (BufferedImage) _diskCache.get(imageURL);
    if (bufferedImage == null)
      {
        try
          {
            if (imageURL != null && imageURL.startsWith("http"))
              {
                if (!_queue.contains(imageURL))
                  {
                    _queue.put(imageURL);
                    getService().submit(new ImageRunner());
                  }
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        bufferedImage = placeholderImage;
      }
    else if (width > 0 && height > 0)
      {
        int imageSize = (width > height ? width : height);
        double ratio = 1.0;
        if (bufferedImage.getWidth() > bufferedImage.getHeight())
          ratio = (double) imageSize / (double) bufferedImage.getWidth();
        else
          ratio = (double) imageSize / (double) bufferedImage.getHeight();
        int imageSizeWidth = (int) (bufferedImage.getWidth() * ratio);
        int imageSizeHeight = (int) (bufferedImage.getHeight() * ratio);
        if (ratio < 1.0)
          bufferedImage = GraphicsUtilities.createThumbnail(bufferedImage, imageSizeWidth, imageSizeHeight);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawImage(bufferedImage, width - imageSizeHeight / 2, height - imageSizeHeight / 2, imageSizeWidth, imageSizeHeight, null);
        graphics2D.dispose();
      }
    return (bufferedImage);
  }

  private class ImageRunner
    implements Runnable
  {
    public void run()
    {
      try
        {
          final String imageURL = _queue.remove();
          _diskCache.put(imageURL, API.getInstance().getBytes(imageURL), null);
        }
      catch (Exception exception)
        {
          Debug.displayStack(this, exception);
        }
    }
  }
}
