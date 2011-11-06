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


package net.zypr.gui.markers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.HashSet;
import java.util.Set;

import net.zypr.gui.panels.MapViewPanel;

import org.jdesktop.swingx.painter.AbstractPainter;

public class MarkerPainter<T extends MapViewPanel>
  extends AbstractPainter<T>
{
  private MarkerRenderer _markerRenderer = new ZYPRMarkerRenderer();
  private Set<Marker> _markers;

  public MarkerPainter()
  {
    setAntialiasing(true);
    setCacheable(false);
    _markers = new HashSet<Marker>();
  }

  public void setRenderer(MarkerRenderer markerRenderer)
  {
    _markerRenderer = markerRenderer;
  }

  public Set<Marker> getMarkers()
  {
    return (_markers);
  }

  public void setMarkers(Set<Marker> items)
  {
    _markers = items;
  }

  @Override
  protected void doPaint(Graphics2D graphics2D, T mapViewPanel, int width, int height)
  {
    if (_markerRenderer == null)
      {
        return;
      }
    Rectangle viewportBounds = mapViewPanel.getViewportBounds();
    int zoom = mapViewPanel.getZoom();
    Dimension sizeInTiles = mapViewPanel.getTileFactory().getMapSize(zoom);
    int tileSize = mapViewPanel.getTileFactory().getTileSize();
    Dimension sizeInPixels = new Dimension(sizeInTiles.width * tileSize, sizeInTiles.height * tileSize);
    double vpx = viewportBounds.getX();
    while (vpx < 0)
      {
        vpx += sizeInPixels.getWidth();
      }
    while (vpx > sizeInPixels.getWidth())
      {
        vpx -= sizeInPixels.getWidth();
      }
    Rectangle2D vp2 = new Rectangle2D.Double(vpx, viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
    Rectangle2D vp3 = new Rectangle2D.Double(vpx - sizeInPixels.getWidth(), viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
    for (Marker marker : getMarkers())
      {
        Point2D point = mapViewPanel.getTileFactory().geoToPixel(marker.getItem().getPosition(), mapViewPanel.getZoom());
        if (vp2.contains(point))
          {
            int x = (int) (point.getX() - vp2.getX());
            int y = (int) (point.getY() - vp2.getY());
            marker.setClickableRectangle(new Rectangle2D.Double(x, y, 0, 0));
            graphics2D.translate(x, y);
            paintMarker(marker, mapViewPanel, graphics2D);
            graphics2D.translate(-x, -y);
          }
        if (vp3.contains(point))
          {
            int x = (int) (point.getX() - vp3.getX());
            int y = (int) (point.getY() - vp3.getY());
            marker.setClickableRectangle(new Rectangle2D.Double(x, y, 0, 0));
            graphics2D.translate(x, y);
            paintMarker(marker, mapViewPanel, graphics2D);
            graphics2D.translate(-x, -y);
          }
      }
    for (Marker marker : getMarkers())
      {
        if (marker.isSelected())
          {
            Point2D point = mapViewPanel.getTileFactory().geoToPixel(marker.getItem().getPosition(), mapViewPanel.getZoom());
            if (vp2.contains(point))
              {
                int x = (int) (point.getX() - vp2.getX());
                int y = (int) (point.getY() - vp2.getY());
                marker.setClickableRectangle(new Rectangle2D.Double(x, y, 0, 0));
                graphics2D.translate(x, y);
                paintMarker(marker, mapViewPanel, graphics2D);
                graphics2D.translate(-x, -y);
              }
            if (vp3.contains(point))
              {
                int x = (int) (point.getX() - vp3.getX());
                int y = (int) (point.getY() - vp3.getY());
                marker.setClickableRectangle(new Rectangle2D.Double(x, y, 0, 0));
                graphics2D.translate(x, y);
                paintMarker(marker, mapViewPanel, graphics2D);
                graphics2D.translate(-x, -y);
              }
          }
      }
  }

  protected void paintMarker(final Marker marker, final T mapViewer, final Graphics2D graphics2D)
  {
    _markerRenderer.paintMarker(graphics2D, mapViewer, marker);
  }
}
