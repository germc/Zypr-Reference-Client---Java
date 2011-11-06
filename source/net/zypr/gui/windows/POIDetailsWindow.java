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


package net.zypr.gui.windows;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.enums.MessageType;
import net.zypr.api.vo.InfoPOITypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ReviewVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.HTMLPane;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.NumberUtils;
import net.zypr.gui.utils.WebBrowser;

import org.apache.commons.codec.binary.Base64;

public class POIDetailsWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelPicture = new ImagePanel("placeholder-picture-poi-96.png");
  private Button _buttonSendToContact = new Button("button-poi-send-to-contact-up.png", "button-poi-send-to-contact-down.png", "button-poi-send-to-contact-disabled.png");
  private Button _buttonLocateOnMap = new Button("button-poi-locate-on-the-map-up.png", "button-poi-locate-on-the-map-down.png", "button-poi-locate-on-the-map-disabled.png");
  private Button _buttonMakeCall = new Button("button-poi-make-call-up.png", "button-poi-make-call-down.png");
  private Button _buttonAddRemoveToRoute = new Button("button-poi-add-to-route-up.png", "button-poi-add-to-route-down.png", "button-poi-add-to-route-disabled.png");
  /*-
  private Button _buttonAddToCalendar = new Button("button-poi-add-to-calendar-up.png", "button-poi-add-to-calendar-down.png");
  -*/
  private Button _buttonAddRemoveToFavorites = new Button("button-poi-add-to-favorite-up.png", "button-poi-add-to-favorite-down.png");
  private ScrollPane _scrollPanelReviews = new ScrollPane();
  private HTMLPane _htmlPaneReviews = new HTMLPane();
  private Label _labelAddress = new Label();
  private Label _labelPhoneNumber = new Label();
  private Label _labelRatings = new Label();
  private ImagePanel _imagePanelStar1 = new ImagePanel("star-icon-0.png", false);
  private ImagePanel _imagePanelStar2 = new ImagePanel("star-icon-0.png", false);
  private ImagePanel _imagePanelStar3 = new ImagePanel("star-icon-0.png", false);
  private ImagePanel _imagePanelStar4 = new ImagePanel("star-icon-0.png", false);
  private ImagePanel _imagePanelStar5 = new ImagePanel("star-icon-0.png", false);
  private boolean _allowSendMessage = true;
  private ItemVO _itemVO = null;

  public POIDetailsWindow(ItemVO itemVO, boolean allowSendMessage)
  {
    super();
    try
      {
        _allowSendMessage = allowSendMessage;
        _itemVO = itemVO;
        jbInit();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void jbInit()
    throws Exception
  {
    this.setSize(new Dimension(560, 293));
    _imagePanelPicture.setLocation(20, 15);
    _buttonSendToContact.setLocation(130, 70);
    _buttonSendToContact.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSendToContact_actionPerformed(actionEvent);
      }
    });
    _buttonLocateOnMap.setLocation(200, 70);
    _buttonLocateOnMap.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonLocateOnMap_actionPerformed(actionEvent);
      }
    });
    _buttonMakeCall.setLocation(270, 70);
    _buttonMakeCall.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonMakeCall_actionPerformed(actionEvent);
      }
    });
    _buttonAddRemoveToRoute.setLocation(340, 70);
    _buttonAddRemoveToRoute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddRemoveToRoute_actionPerformed(actionEvent);
      }
    });
    /*-
    _buttonAddToCalendar.setLocation(410, 70);
    _buttonAddToCalendar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddToCalendar_actionPerformed(actionEvent);
      }
    });
    -*/
    _buttonAddRemoveToFavorites.setLocation(410, 70);
    _buttonAddRemoveToFavorites.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddRemoveToFavorites_actionPerformed(actionEvent);
      }
    });
    _labelAddress.setBounds(140, 15, 455, 20);
    _labelPhoneNumber.setBounds(140, 40, 160, 20);
    _scrollPanelReviews.setBounds(15, 130, 530, 113);
    _scrollPanelReviews.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPanelReviews.scrollDown(_scrollPanelReviews.getHeight() / 4);
      }
    });
    _scrollPanelReviews.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPanelReviews.scrollUp(_scrollPanelReviews.getHeight() / 4);
      }
    });
    _htmlPaneReviews.addHyperlinkListener(new HyperlinkListener()
    {
      public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
      {
        _htmlPaneReviews_hyperlinkUpdate(hyperlinkEvent);
      }
    });
    _scrollPanelReviews.addToViewport(_htmlPaneReviews);
    _labelRatings.setBounds(240, 40, 160, 20);
    _labelRatings.setFont(new Font("Dialog", 0, 12));
    _labelRatings.setHorizontalAlignment(SwingConstants.RIGHT);
    _labelRatings.setHorizontalTextPosition(SwingConstants.RIGHT);
    _imagePanelStar1.setLocation(410, 35);
    _imagePanelStar2.setLocation(435, 35);
    _imagePanelStar3.setLocation(460, 35);
    _imagePanelStar4.setLocation(485, 35);
    _imagePanelStar5.setLocation(510, 35);
    _panelContent.add(_imagePanelStar5, null);
    _panelContent.add(_imagePanelStar4, null);
    _panelContent.add(_imagePanelStar3, null);
    _panelContent.add(_imagePanelStar2, null);
    _panelContent.add(_imagePanelStar1, null);
    _panelContent.add(_labelRatings, null);
    _panelContent.add(_labelPhoneNumber, null);
    _panelContent.add(_labelAddress, null);
    _panelContent.add(_buttonAddRemoveToFavorites, null);
    /*-
    _panelContent.add(_buttonAddToCalendar, null);
    -*/
    _panelContent.add(_buttonAddRemoveToRoute, null);
    _panelContent.add(_buttonMakeCall, null);
    _panelContent.add(_buttonLocateOnMap, null);
    _panelContent.add(_buttonSendToContact, null);
    _panelContent.add(_imagePanelPicture, null);
    _panelContent.add(_scrollPanelReviews, null);
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon(API.getInstance().getPOI().getServiceDetails(_itemVO.getService().toLowerCase()).getServiceIconURI()));
    setDataSourceURL("http://www." + _itemVO.getService().toLowerCase() + ".com/");
    -*/
    setRatingStars(_itemVO.getRating());
    setTitle(_itemVO.getName());
    if (Session.getInstance().isInFavorites(_itemVO))
      _buttonAddRemoveToFavorites.setIcons("button-poi-remove-from-favorite-up.png", "button-poi-remove-from-favorite-down.png", "button-poi-remove-from-favorite-disabled.png");
    else
      _buttonAddRemoveToFavorites.setIcons("button-poi-add-to-favorite-up.png", "button-poi-add-to-favorite-down.png", "button-poi-add-to-favorite-disabled.png");
    new Thread(new Runnable()
    {
      public void run()
      {
        ItemVO detailsItemVO = _itemVO;
        try
          {
            detailsItemVO = API.getInstance().getItemVO(_itemVO.getAction("details").getHandlerURI());
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(detailsItemVO.getIconURL(), 96, 96, null);
        if (bufferedImage != null)
          _imagePanelPicture.setImage(bufferedImage);
        try
          {
            InfoPOITypeVO infoPOITypeVO = (InfoPOITypeVO) detailsItemVO.getInfo();
            if (infoPOITypeVO.getPhoneNumbers() != null)
              for (int index = 0; index < infoPOITypeVO.getPhoneNumbers().length; index++)
                _labelPhoneNumber.setText((index >= 1 ? ", " : "") + infoPOITypeVO.getPhoneNumbers()[index].getValue());
            else
              _labelPhoneNumber.setText("");
            _labelAddress.setText(infoPOITypeVO.getAddress().toAddressString());
            setDescription(detailsItemVO.getDescription(), infoPOITypeVO.getReviews());
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  protected void windowVisible()
  {
    _buttonLocateOnMap.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonAddRemoveToRoute.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonSendToContact.setEnabled(_allowSendMessage);
    MapViewPanel mapViewPanel = (MapViewPanel) getParentPanel();
    mapViewPanel.addItemVOsPOI(_itemVO);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _buttonSendToContact_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SendToContactListWindow(_itemVO, MessageType.TEXT));
  }

  private void _buttonLocateOnMap_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParentPanel()).setMapCenter(_itemVO.getPosition());
    getParentPanel().discardAllWindows();
  }

  private void _buttonMakeCall_actionPerformed(ActionEvent actionEvent)
  {
    WebBrowser.openURL("data:text/html;base64," + (new Base64()).encodeBase64URLSafeString("<h1>Dialing 123...</h1>".getBytes()));
  }

  private void _buttonAddRemoveToRoute_actionPerformed(ActionEvent actionEvent)
  {
    _buttonAddRemoveToRoute.setEnabled(false);
    ((MapViewPanel) getParentPanel()).addRoute(_itemVO);
  }

  private void _buttonAddToCalendar_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonAddRemoveToFavorites_actionPerformed(ActionEvent actionEvent)
  {
    if (Session.getInstance().isInFavorites(_itemVO))
      {
        try
          {
            API.getInstance().getUser().favoriteDelete(_itemVO.getGlobalItemID());
            _buttonAddRemoveToFavorites.setIcons("button-poi-add-to-favorite-up.png", "button-poi-add-to-favorite-down.png", "button-poi-add-to-favorite-disabled.png");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    else
      {
        try
          {
            API.getInstance().getUser().favoriteSet(_itemVO);
            _buttonAddRemoveToFavorites.setIcons("button-poi-remove-from-favorite-up.png", "button-poi-remove-from-favorite-down.png", "button-poi-remove-from-favorite-disabled.png");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
  }

  private void _htmlPaneReviews_hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
  {
    try
      {
        if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
          {
            WebBrowser.openURL(hyperlinkEvent.getURL().toString());
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void setRatingStars(double rating)
  {
    if (rating < 0.5)
      {
        _imagePanelStar1.setVisible(false);
        _imagePanelStar2.setVisible(false);
        _imagePanelStar3.setVisible(false);
        _imagePanelStar4.setVisible(false);
        _imagePanelStar5.setVisible(false);
        _labelRatings.setVisible(false);
      }
    if (rating < 1.0 && rating >= 0.5)
      _imagePanelStar1.setImage("star-icon-1.png");
    if (rating >= 1.0)
      _imagePanelStar1.setImage("star-icon-2.png");
    if (rating < 2.0 && rating >= 1.5)
      _imagePanelStar2.setImage("star-icon-1.png");
    if (rating >= 2.0)
      _imagePanelStar2.setImage("star-icon-2.png");
    if (rating < 3.0 && rating >= 2.5)
      _imagePanelStar3.setImage("star-icon-1.png");
    if (rating >= 3.0)
      _imagePanelStar3.setImage("star-icon-2.png");
    if (rating < 4.0 && rating >= 3.5)
      _imagePanelStar4.setImage("star-icon-1.png");
    if (rating >= 4.0)
      _imagePanelStar4.setImage("star-icon-2.png");
    if (rating < 5.0 && rating >= 4.5)
      _imagePanelStar5.setImage("star-icon-1.png");
    if (rating >= 5.0)
      _imagePanelStar5.setImage("star-icon-2.png");
    _labelRatings.setText(NumberUtils.roundToDecimals(rating, 2) + "");
  }

  private void setDescription(String description, ReviewVO[] reviewVOs)
  {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("<html><body alink=\"#FF0000\" link=\"#00FF00\" text=\"#FFFFFF\" vlink=\"#0000FF\">");
    if (!description.trim().equals(""))
      {
        stringBuffer.append("<p>");
        stringBuffer.append(description);
        stringBuffer.append("</p>");
      }
    if (reviewVOs != null)
      for (int index = 0; index < reviewVOs.length; index++)
        {
          stringBuffer.append("<p>");
          stringBuffer.append("<a href=\"");
          stringBuffer.append(reviewVOs[index].getSourceURI());
          stringBuffer.append("\"/>");
          stringBuffer.append("<img src=\"");
          stringBuffer.append(reviewVOs[index].getReviewerIconURI());
          stringBuffer.append("\" border=\"0\" align=\"left\" width=\"48\"/></a>&nbsp;<b>");
          stringBuffer.append(reviewVOs[index].getReviewerName());
          stringBuffer.append("</b> : ");
          stringBuffer.append(reviewVOs[index].getDescription());
          stringBuffer.append(" (&nbsp;<a style=\"color: #3BB9FF;\" href=\"");
          stringBuffer.append(reviewVOs[index].getSourceURI());
          stringBuffer.append("\"/>More</a>&nbsp;)");
          stringBuffer.append("</p>");
        }
    stringBuffer.append("</body></html>");
    _htmlPaneReviews.setText(stringBuffer.toString());
    _scrollPanelReviews.scrollToStart();
  }
}
