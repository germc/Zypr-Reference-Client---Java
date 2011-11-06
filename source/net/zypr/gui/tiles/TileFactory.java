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

import java.awt.Dimension;
import java.awt.geom.Point2D;

import net.zypr.api.vo.GeoPositionVO;
import net.zypr.gui.utils.GeoUtil;

public abstract class TileFactory
{
  private TileFactoryInfo info;

  protected TileFactory(TileFactoryInfo info)
  {
    this.info = info;
  }

  public int getTileSize()
  {
    return getInfo().getTileSize();
  }

  public Dimension getMapSize(int zoom)
  {
    return GeoUtil.getMapSize(zoom, getInfo());
  }

  public abstract Tile getTile(int x, int y, int zoom);

  public GeoPositionVO pixelToGeo(Point2D pixelCoordinate, int zoom)
  {
    return GeoUtil.getPosition(pixelCoordinate, zoom, getInfo());
  }

  public Point2D geoToPixel(GeoPositionVO c, int zoomLevel)
  {
    return GeoUtil.getBitmapCoordinate(c, zoomLevel, getInfo());
  }

  public TileFactoryInfo getInfo()
  {
    return info;
  }

  protected abstract void startLoading(Tile tile);
}
