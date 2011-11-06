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


package net.zypr.gui.tiles;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;

import java.lang.ref.SoftReference;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;

import javax.swing.SwingUtilities;

import net.zypr.api.API;
import net.zypr.gui.Configuration;
import net.zypr.gui.cache.AbstractCache;
import net.zypr.gui.cache.TileDiskCache;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.GeoUtil;

import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class ZYPRTileFactory
  extends TileFactory
{
  private static final int MAX_ZOOM = 17;
  private static final int MIN_ZOOM = 0;
  private static boolean _eagerTileFetchingEnabled = Configuration.getInstance().getBooleanProperty("eager-tile-fetching-enabled", false);
  private int _threadPoolSize = Configuration.getInstance().getIntegerProperty("number-of-tiles-fetch-simultaneously", 16);
  private ExecutorService _executorService;
  private Map<String, Tile> _tileMap = new HashMap<String, Tile>();
  private AbstractCache _tileCache = new TileDiskCache();
  private static BlockingQueue<Tile> _tileQueue = new PriorityBlockingQueue<Tile>(Configuration.getInstance().getIntegerProperty("number-of-tiles-fetch-simultaneously", 16), new Comparator<Tile>()
    {
      public int compare(Tile tile1, Tile tile2)
      {
        if (tile1.getPriority() == TileFetchPriority.LOW && tile2.getPriority() == TileFetchPriority.HIGH)
          return 1;
        else if (tile1.getPriority() == TileFetchPriority.HIGH && tile2.getPriority() == TileFetchPriority.LOW)
          return -1;
        return 0;
      }

      @Override
      public boolean equals(Object object)
      {
        return (object == this);
      }
    });

  public ZYPRTileFactory()
  {
    super(new TileFactoryInfo(MIN_ZOOM, MAX_ZOOM - MIN_ZOOM - 1, MAX_ZOOM, 256, true, true));
  }

  public Tile getTile(int x, int y, int zoom)
  {
    return getTile(x, y, zoom, true);
  }

  private Tile getTile(int x, int y, int zoom, boolean fetchEagerly)
  {
    int tileX = x;
    int tileY = y;
    int numTilesWide = (int) getMapSize(zoom).getWidth();
    if (tileX < 0)
      tileX = numTilesWide - (Math.abs(tileX) % numTilesWide);
    tileX = tileX % numTilesWide;
    Tile tile = null;
    if (!_tileMap.containsKey(x + "" + y + "" + zoom))
      {
        if (!GeoUtil.isValidTile(tileX, tileY, zoom, getInfo()))
          {
            tile = new Tile(tileX, tileY, zoom);
          }
        else
          {
            tile = new Tile(tileX, tileY, zoom, fetchEagerly ? TileFetchPriority.HIGH : TileFetchPriority.LOW, this);
            startLoading(tile);
          }
        _tileMap.put(x + "" + y + "" + zoom, tile);
      }
    else
      {
        tile = _tileMap.get(x + "" + y + "" + zoom);
        if (tile.getPriority() == TileFetchPriority.LOW && fetchEagerly && !tile.isLoaded())
          promote(tile);
      }
    if (fetchEagerly && _eagerTileFetchingEnabled && zoom > 0)
      {
        eagerlyLoad(tileX * 2, tileY * 2, zoom - 1);
        eagerlyLoad(tileX * 2 + 1, tileY * 2, zoom - 1);
        eagerlyLoad(tileX * 2, tileY * 2 + 1, zoom - 1);
        eagerlyLoad(tileX * 2 + 1, tileY * 2 + 1, zoom - 1);
      }
    return tile;
  }

  private void eagerlyLoad(int x, int y, int zoom)
  {
    if (!isLoaded(x, y, zoom))
      getTile(x, y, zoom, false);
  }

  private boolean isLoaded(int x, int y, int zoom)
  {
    return _tileMap.containsKey(x + "" + y + "" + zoom);
  }

  public AbstractCache getTileCache()
  {
    return _tileCache;
  }

  public void setTileCache(AbstractCache cache)
  {
    this._tileCache = cache;
  }

  protected synchronized ExecutorService getService()
  {
    if (_executorService == null)
      {
        _executorService = Executors.newFixedThreadPool(_threadPoolSize, new ThreadFactory()
            {
              private int count = 0;

              public Thread newThread(Runnable r)
              {
                Thread t = new Thread(r, "tile-pool-" + count++);
                t.setPriority(Thread.MIN_PRIORITY);
                t.setDaemon(true);
                return t;
              }
            });
      }
    return _executorService;
  }

  public void setThreadPoolSize(int size)
  {
    if (size <= 0)
      {
        throw new IllegalArgumentException("size invalid: " + size + ". The size of the threadpool must be greater than 0.");
      }
    _threadPoolSize = size;
  }

  @SuppressWarnings("unchecked")
  protected synchronized void startLoading(Tile tile)
  {
    if (tile.isLoading())
      {
        Debug.print("already loading. bailing");
        return;
      }
    tile.setLoading(true);
    try
      {
        _tileQueue.put(tile);
        getService().submit(createTileRunner(tile));
      }
    catch (Exception ex)
      {
        ex.printStackTrace();
      }
  }

  protected Runnable createTileRunner(Tile tile)
  {
    return new TileRunner();
  }

  public synchronized void promote(Tile tile)
  {
    if (_tileQueue.contains(tile))
      {
        try
          {
            _tileQueue.remove(tile);
            tile.setPriority(TileFetchPriority.HIGH);
            _tileQueue.put(tile);
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
  }

  private class TileRunner
    implements Runnable
  {
    public void run()
    {
      final Tile tile = _tileQueue.remove();
      int trys = 3;
      while (!tile.isLoaded() && trys > 0)
        {
          try
            {
              BufferedImage bufferedImage = (BufferedImage) _tileCache.get(tile.getID());
              if (bufferedImage == null)
                {
                  byte[] byteImage = API.getInstance().getMap().get(tile.getX(), tile.getY(), getInfo().getTotalMapZoom() - tile.getZoom(), null, getInfo().getService(), null);
                  bufferedImage = GraphicsUtilities.loadCompatibleImage(new ByteArrayInputStream(byteImage));
                  _tileCache.put(tile.getID(), byteImage, bufferedImage);
                }
              if (bufferedImage == null)
                {
                  Debug.displayWarning(this, "Unable to load tile " + tile.getX() + "x" + tile.getY() + " zoom = " + (getInfo().getTotalMapZoom() - tile.getZoom()));
                  trys--;
                }
              else
                {
                  final BufferedImage i = bufferedImage;
                  SwingUtilities.invokeAndWait(new Runnable()
                  {
                    public void run()
                    {
                      tile.setBufferedImage(new SoftReference<BufferedImage>(i));
                      tile.setLoaded(true);
                    }
                  });
                }
            }
          catch (OutOfMemoryError outOfMemoryError)
            {
              Debug.displayStack(this, outOfMemoryError);
              _tileCache.freeUpMemory();
            }
          catch (Throwable throwable)
            {
              Debug.displayStack(this, throwable, 1);
              try
                {
                  Thread.sleep(10000);
                }
              catch (InterruptedException interruptedException)
                {
                  Debug.displayStack(this, interruptedException);
                }
              Object oldError = tile.getError();
              tile.setError(throwable);
              tile.firePropertyChangeOnEDT("loadingError", oldError, throwable);
              if (trys == 0)
                tile.firePropertyChangeOnEDT("unrecoverableError", null, throwable);
              else
                trys--;
            }
        }
      tile.setLoading(false);
    }
  }
}
