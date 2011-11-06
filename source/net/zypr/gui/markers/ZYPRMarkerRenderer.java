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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.zypr.api.enums.ItemType;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Resources;
import net.zypr.gui.panels.MapViewPanel;

public class ZYPRMarkerRenderer
  implements MarkerRenderer
{
  private static final BufferedImage CONTACT_MARKER_SELECTED = Resources.getInstance().getBufferedImage("marker-contact-selected.png");
  private static final BufferedImage CONTACT_MARKER_NOT_SELECTED = Resources.getInstance().getBufferedImage("marker-contact-not-selected.png");
  private static final BufferedImage CONTACT_PLACE_HOLDER = Resources.getInstance().getBufferedImage("placeholder-picture-contact-48.png");
  private static final BufferedImage RADIO_MARKER_SELECTED = Resources.getInstance().getBufferedImage("marker-radio-selected.png");
  private static final BufferedImage RADIO_MARKER_NOT_SELECTED = Resources.getInstance().getBufferedImage("marker-radio-not-selected.png");
  private static final BufferedImage RADIO_PLACE_HOLDER = Resources.getInstance().getBufferedImage("placeholder-picture-radio-48.png");
  private static final BufferedImage POI_MARKER_SELECTED = Resources.getInstance().getBufferedImage("marker-poi-selected.png");
  private static final BufferedImage POI_MARKER_NOT_SELECTED = Resources.getInstance().getBufferedImage("marker-poi-not-selected.png");
  private static final BufferedImage POI_PLACE_HOLDER = Resources.getInstance().getBufferedImage("placeholder-picture-poi-48.png");
  private static final Point PIN_POINT_NOT_SELECTED = new Point(-22, -66);
  private static final Point PIN_POINT_SELECTED = new Point(-32, -78);
  private static final Point IMAGE_LOCATION_NOT_SELECTED = new Point(2, 3);
  private static final Point IMAGE_LOCATION_SELECTED = new Point(7, 5);
  private static final int INDEX_TEXT_Y_NOT_SELECTED = 48;
  private static final int INDEX_TEXT_Y_SELECTED = 56;
  private static final int IMAGE_SIZE_NOT_SELECTED = 40;
  private static final int IMAGE_SIZE_SELECTED = 48;

  public ZYPRMarkerRenderer()
  {
    super();
  }

  public boolean paintMarker(Graphics2D graphics2D, MapViewPanel mapViewPanel, Marker marker)
  {
    BufferedImage markerImage = null;
    BufferedImage defaultItemImage = null;
    if (marker.getItem().getType() == ItemType.USER)
      {
        markerImage = marker.isSelected() ? CONTACT_MARKER_SELECTED : CONTACT_MARKER_NOT_SELECTED;
        defaultItemImage = CONTACT_PLACE_HOLDER;
      }
    else if (marker.getItem().getType() == ItemType.RADIO)
      {
        markerImage = marker.isSelected() ? RADIO_MARKER_SELECTED : RADIO_MARKER_NOT_SELECTED;
        defaultItemImage = RADIO_PLACE_HOLDER;
      }
    else if (marker.getItem().getType() == ItemType.POI)
      {
        markerImage = marker.isSelected() ? POI_MARKER_SELECTED : POI_MARKER_NOT_SELECTED;
        defaultItemImage = POI_PLACE_HOLDER;
      }
    else
      {
        return (false);
      }
    Point pinPoint = marker.isSelected() ? PIN_POINT_SELECTED : PIN_POINT_NOT_SELECTED;
    Point imageLocation = marker.isSelected() ? IMAGE_LOCATION_SELECTED : IMAGE_LOCATION_NOT_SELECTED;
    int imageSize = marker.isSelected() ? IMAGE_SIZE_SELECTED : IMAGE_SIZE_NOT_SELECTED;
    graphics2D.drawImage(markerImage, (int) pinPoint.getX(), (int) pinPoint.getY(), markerImage.getWidth(), markerImage.getHeight(), null);
    BufferedImage itemImage = ImageFetcher.getInstance().getImage(marker.getItem().getIconURL(), 48, 48, defaultItemImage);
    if (itemImage != null)
      {
        double ratio = 1.0;
        if (itemImage != null)
          if (itemImage.getWidth() > itemImage.getHeight())
            ratio = (double) imageSize / (double) itemImage.getWidth();
          else
            ratio = (double) imageSize / (double) itemImage.getHeight();
        int imageSizeWidth = (int) (itemImage.getWidth() * ratio);
        int imageSizeHeight = (int) (itemImage.getHeight() * ratio);
        int imageLocationX = (imageSize - imageSizeWidth) / 2;
        int imageLocationY = (imageSize - imageSizeHeight) / 2;
        marker.setClickableRectangle(new Rectangle2D.Double(marker.getClickableRectangle().getX() + (pinPoint.getX() + imageLocation.getX()), marker.getClickableRectangle().getY() + (pinPoint.getY() + imageLocation.getY()), (double) imageSize, (double) imageSize));
        graphics2D.drawImage(itemImage, (int) (pinPoint.getX() + imageLocation.getX() + imageLocationX), (int) (pinPoint.getY() + imageLocation.getY() + imageLocationY), imageSizeWidth, imageSizeHeight, null);
      }
    graphics2D.setFont(new Font("Dialog", 1, 14));
    graphics2D.setColor(Color.WHITE);
    Rectangle2D textRectangle = graphics2D.getFontMetrics().getStringBounds(marker.getIndex() + "", graphics2D);
    graphics2D.setComposite(AlphaComposite.SrcOver.derive(1.0f));
    graphics2D.drawString(marker.getIndex() + "", (int) -textRectangle.getWidth() / 2, (int) -(textRectangle.getHeight() / 2) - 2);
    int lastY = 16;
    if (marker.isSelected())
      {
        graphics2D.setPaint(Color.WHITE);
        textRectangle = graphics2D.getFontMetrics().getStringBounds(marker.getItem().getName() + "", graphics2D);
        lastY -= (int) textRectangle.getHeight();
        graphics2D.fillRoundRect((int) (-((textRectangle.getWidth() < 16 ? 16 : textRectangle.getWidth()) + 4) / 2), lastY, (int) (textRectangle.getWidth() < 16 ? 16 : textRectangle.getWidth()) + 4, (int) textRectangle.getHeight(), 5, 5);
        lastY += (int) textRectangle.getHeight();
        graphics2D.setPaint(Color.BLACK);
        graphics2D.drawString(marker.getItem().getName() + "", (int) -textRectangle.getWidth() / 2, lastY - 2);
      }
    return (false);
  }
}
