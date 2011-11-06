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

import java.awt.geom.Point2D;

public class TileFactoryInfo
{
  private int _minimumZoomLevel;
  private int _maximumZoomLevel;
  private int _totalMapZoom;
  private int _tileSize = 256;
  private int[] _mapWidthInTilesAtZoom;
  private Point2D[] _mapCenterInPixelsAtZoom;
  private double[] _longitudeDegreeWidthInPixels;
  private double[] _longitudeRadianWidthInPixels;
  private boolean _xr2l = true;
  private boolean _yt2b = true;
  private int _defaultZoomLevel;
  private String _service;

  public TileFactoryInfo(int minimumZoomLevel, int maximumZoomLevel, int totalMapZoom, int tileSize, boolean _xr2l, boolean _yt2b)
  {
    this("default", minimumZoomLevel, maximumZoomLevel, totalMapZoom, tileSize, _xr2l, _yt2b);
  }

  public TileFactoryInfo(String service, int minimumZoomLevel, int maximumZoomLevel, int totalMapZoom, int tileSize, boolean xr2l, boolean yt2b)
  {
    _service = service;
    _minimumZoomLevel = minimumZoomLevel;
    _maximumZoomLevel = maximumZoomLevel;
    _totalMapZoom = totalMapZoom;
    this.setXr2l(xr2l);
    this.setYt2b(yt2b);
    _tileSize = tileSize;
    int tilesize = this.getTileSize();
    _longitudeDegreeWidthInPixels = new double[totalMapZoom + 1];
    _longitudeRadianWidthInPixels = new double[totalMapZoom + 1];
    _mapCenterInPixelsAtZoom = new Point2D.Double[totalMapZoom + 1];
    _mapWidthInTilesAtZoom = new int[totalMapZoom + 1];
    for (int z = totalMapZoom; z >= 0; --z)
      {
        _longitudeDegreeWidthInPixels[z] = (double) tilesize / 360;
        _longitudeRadianWidthInPixels[z] = (double) tilesize / (2.0 * Math.PI);
        int t2 = tilesize / 2;
        _mapCenterInPixelsAtZoom[z] = new Point2D.Double(t2, t2);
        _mapWidthInTilesAtZoom[z] = tilesize / this.getTileSize();
        tilesize *= 2;
      }
  }

  public int getMinimumZoomLevel()
  {
    return (_minimumZoomLevel);
  }

  public int getMaximumZoomLevel()
  {
    return (_maximumZoomLevel);
  }

  public int getTotalMapZoom()
  {
    return _totalMapZoom;
  }

  public int getMapWidthInTilesAtZoom(int zoom)
  {
    return _mapWidthInTilesAtZoom[zoom];
  }

  public Point2D getMapCenterInPixelsAtZoom(int zoom)
  {
    return _mapCenterInPixelsAtZoom[zoom];
  }

  public int getTileSize()
  {
    return (_tileSize);
  }

  public double getLongitudeDegreeWidthInPixels(int zoom)
  {
    return _longitudeDegreeWidthInPixels[zoom];
  }

  public double getLongitudeRadianWidthInPixels(int zoom)
  {
    return _longitudeRadianWidthInPixels[zoom];
  }

  public boolean isXr2l()
  {
    return _xr2l;
  }

  public void setXr2l(boolean xr2l)
  {
    _xr2l = xr2l;
  }

  public boolean isYt2b()
  {
    return _yt2b;
  }

  public void setYt2b(boolean yt2b)
  {
    _yt2b = yt2b;
  }

  public int getDefaultZoomLevel()
  {
    return _defaultZoomLevel;
  }

  public void setDefaultZoomLevel(int defaultZoomLevel)
  {
    _defaultZoomLevel = defaultZoomLevel;
  }

  public String getService()
  {
    return _service;
  }
}
