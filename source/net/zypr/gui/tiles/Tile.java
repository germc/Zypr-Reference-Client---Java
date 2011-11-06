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

import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import java.beans.PropertyChangeListener;

import java.lang.ref.SoftReference;

import javax.swing.SwingUtilities;

import net.zypr.api.enums.MapFormat;
import net.zypr.api.enums.MapType;

import org.jdesktop.beans.AbstractBean;

public class Tile
  extends AbstractBean
{
  private TileFetchPriority _priority = TileFetchPriority.HIGH;
  private boolean _loading = false;
  private boolean _loaded = false;
  private Throwable _error;
  private int _x;
  private int _y;
  private int _zoom;
  private MapType _type;
  private String _service;
  private MapFormat _format;
  private SoftReference<BufferedImage> _bufferedImage = new SoftReference<BufferedImage>(null);
  private TileFactory _tileFactory;
  private PropertyChangeListener _uniqueListener = null;

  public void addUniquePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    if (_uniqueListener != null && _uniqueListener != listener)
      removePropertyChangeListener(propertyName, _uniqueListener);
    if (_uniqueListener != listener)
      {
        _uniqueListener = listener;
        addPropertyChangeListener(propertyName, _uniqueListener);
      }
  }

  void firePropertyChangeOnEDT(final String propertyName, final Object oldValue, final Object newValue)
  {
    if (!EventQueue.isDispatchThread())
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            firePropertyChange(propertyName, oldValue, newValue);
          }
        });
      }
  }

  public Tile(int x, int y, int zoom)
  {
    _loaded = false;
    _x = x;
    _y = y;
    _zoom = zoom;
  }

  public Tile(int x, int y, int zoom, TileFetchPriority priority, TileFactory tileFactory)
  {
    _loaded = false;
    _x = x;
    _y = y;
    _zoom = zoom;
    _priority = priority;
    _tileFactory = tileFactory;
  }

  public void setPriority(TileFetchPriority priority)
  {
    TileFetchPriority oldPriority = _priority;
    this._priority = priority;
    firePropertyChange("Priority", oldPriority, priority);
  }

  public TileFetchPriority getPriority()
  {
    return _priority;
  }

  public void setLoading(boolean loading)
  {
    boolean oldLoading = _loading;
    this._loading = loading;
    firePropertyChange("Loading", oldLoading, loading);
  }

  public boolean isLoading()
  {
    return _loading;
  }

  public synchronized void setLoaded(boolean loaded)
  {
    boolean oldLoaded = _loaded;
    this._loaded = loaded;
    firePropertyChange("Loaded", oldLoaded, loaded);
  }

  public synchronized boolean isLoaded()
  {
    return _loaded;
  }

  public void setError(Throwable error)
  {
    Throwable oldError = _error;
    this._error = error;
    firePropertyChange("Error", oldError, error);
  }

  public Throwable getError()
  {
    return _error;
  }

  public void setX(int x)
  {
    int oldX = _x;
    this._x = x;
    firePropertyChange("X", oldX, x);
  }

  public int getX()
  {
    return _x;
  }

  public void setY(int y)
  {
    int oldY = _y;
    this._y = y;
    firePropertyChange("Y", oldY, y);
  }

  public int getY()
  {
    return _y;
  }

  public void setZoom(int zoom)
  {
    int oldZoom = _zoom;
    this._zoom = zoom;
    firePropertyChange("Zoom", oldZoom, zoom);
  }

  public int getZoom()
  {
    return _zoom;
  }

  public void setType(MapType type)
  {
    MapType oldType = _type;
    this._type = type;
    firePropertyChange("Type", oldType, type);
  }

  public MapType getType()
  {
    return _type;
  }

  public void setService(String service)
  {
    String oldService = _service;
    this._service = service;
    firePropertyChange("Service", oldService, service);
  }

  public String getService()
  {
    return _service;
  }

  public void setFormat(MapFormat format)
  {
    MapFormat oldFormat = _format;
    this._format = format;
    firePropertyChange("Format", oldFormat, format);
  }

  public MapFormat getFormat()
  {
    return _format;
  }

  public void setBufferedImage(SoftReference<BufferedImage> bufferedImage)
  {
    SoftReference<BufferedImage> oldBufferedImage = _bufferedImage;
    this._bufferedImage = bufferedImage;
    firePropertyChange("BufferedImage", oldBufferedImage, bufferedImage);
  }

  public BufferedImage getBufferedImage()
  {
    BufferedImage bufferedImage = _bufferedImage.get();
    if (bufferedImage == null)
      {
        setLoaded(false);
        _tileFactory.startLoading(this);
      }
    return (bufferedImage);
  }

  public void setTileFactory(TileFactory tileFactory)
  {
    TileFactory oldTileFactory = _tileFactory;
    this._tileFactory = tileFactory;
    firePropertyChange("TileFactory", oldTileFactory, tileFactory);
  }

  public TileFactory getTileFactory()
  {
    return _tileFactory;
  }

  public String getID()
  {
    return (_x + "" + _y + "" + _zoom);
  }

  @Override
  public boolean equals(Object object)
  {
    if (this == object)
      return true;
    else if (!(object instanceof Tile))
      return false;
    final Tile other = (Tile) object;
    return (_x == other.getX() && _y == other.getY() && _zoom == other.getZoom() && (_type == null ? other.getType() == null : _type == other.getType()) && (_service == null ? other.getService() == null : _service.equals(other.getService())) && (_format == null ? other.getFormat() == null : _format == other.getFormat()));
  }

  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();
    result = PRIME * result + _x;
    result = PRIME * result + _y;
    result = PRIME * result + _zoom;
    result = PRIME * result + ((_type == null) ? 0 : _type.hashCode());
    result = PRIME * result + ((_service == null) ? 0 : _service.hashCode());
    result = PRIME * result + ((_format == null) ? 0 : _format.hashCode());
    return result;
  }
}
