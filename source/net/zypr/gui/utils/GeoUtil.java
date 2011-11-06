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


package net.zypr.gui.utils;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import net.zypr.api.enums.CardinalDirection;
import net.zypr.api.vo.GeoBoundsVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.tiles.TileFactory;
import net.zypr.gui.tiles.TileFactoryInfo;

public final class GeoUtil
{

  public static Dimension getMapSize(int zoom, TileFactoryInfo info)
  {
    return new Dimension(info.getMapWidthInTilesAtZoom(zoom), info.getMapWidthInTilesAtZoom(zoom));
  }

  public static boolean isValidTile(int x, int y, int zoomLevel, TileFactoryInfo info)
  {
    if (x < 0 || y < 0)
      {
        return false;
      }
    if (info.getMapCenterInPixelsAtZoom(zoomLevel).getX() * 2 <= x * info.getTileSize())
      {
        return false;
      }
    if (info.getMapCenterInPixelsAtZoom(zoomLevel).getY() * 2 <= y * info.getTileSize())
      {
        return false;
      }
    if (zoomLevel < info.getMinimumZoomLevel() || zoomLevel > info.getMaximumZoomLevel())
      {
        return false;
      }
    return true;
  }

  public static Point2D getBitmapCoordinate(GeoPositionVO c, int zoomLevel, TileFactoryInfo info)
  {
    return getBitmapCoordinate(c.getLatitude(), c.getLongitude(), zoomLevel, info);
  }

  public static Point2D getBitmapCoordinate(double latitude, double longitude, int zoomLevel, TileFactoryInfo info)
  {
    double x = info.getMapCenterInPixelsAtZoom(zoomLevel).getX() + longitude * info.getLongitudeDegreeWidthInPixels(zoomLevel);
    double e = Math.sin(latitude * (Math.PI / 180.0));
    if (e > 0.9999)
      {
        e = 0.9999;
      }
    if (e < -0.9999)
      {
        e = -0.9999;
      }
    double y = info.getMapCenterInPixelsAtZoom(zoomLevel).getY() + 0.5 * Math.log((1 + e) / (1 - e)) * -1 * (info.getLongitudeRadianWidthInPixels(zoomLevel));
    return new Point2D.Double(x, y);
  }

  public static GeoPositionVO getPosition(Point2D pixelCoordinate, int zoom, TileFactoryInfo info)
  {
    double x = pixelCoordinate.getX();
    double y = pixelCoordinate.getY();
    double longitude = (x - info.getMapCenterInPixelsAtZoom(zoom).getX()) / info.getLongitudeDegreeWidthInPixels(zoom);
    double latitude = (y - info.getMapCenterInPixelsAtZoom(zoom).getY()) / (-1 * info.getLongitudeRadianWidthInPixels(zoom));
    latitude = (2 * Math.atan(Math.exp(latitude)) - Math.PI / 2) / (Math.PI / 180.0);
    return (new GeoPositionVO(latitude, longitude));
  }

  public static GeoBoundsVO getMapBounds(MapViewPanel mapViewPanel)
  {
    TileFactory tileFactory = mapViewPanel.getTileFactory();
    int zoom = mapViewPanel.getZoom();
    Rectangle2D bounds = mapViewPanel.getViewportBounds();
    GeoPositionVO nwGeoPosition = tileFactory.pixelToGeo(new Point2D.Double(bounds.getX(), bounds.getY()), zoom);
    GeoPositionVO seGeoPosition = tileFactory.pixelToGeo(new Point2D.Double(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight()), zoom);
    return (new GeoBoundsVO(nwGeoPosition, CardinalDirection.NW, seGeoPosition, CardinalDirection.SE));
  }

  public static double getGeoMetersPerPixel(int zoom)
  {
    return ((40075016.685578 / Math.pow(2.0, 17 - zoom)) / 256);
  }
}
