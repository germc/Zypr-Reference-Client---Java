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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.enums.MessageType;
import net.zypr.api.vo.ItemVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.MPlayer;

public class RadioStationDetailsWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelRadio = new ImagePanel("placeholder-picture-radio-96.png");
  private Button _buttonSendToContact = new Button("button-station-send-to-contact-up.png", "button-station-send-to-contact-down.png", "button-station-send-to-contact-disabled.png");
  private Button _buttonLocateOnMap = new Button("button-station-locate-on-the-map-up.png", "button-station-locate-on-the-map-down.png", "button-station-locate-on-the-map-disabled.png");
  private Button _buttonPlayStop = new Button("button-station-play-up.png", "button-station-play-down.png");
  private Button _buttonAddRemoveToRoute = new Button("button-station-add-to-route-up.png", "button-station-add-to-route-down.png", "button-station-add-to-route-disabled.png");
  private Button _buttonAddToCalendar = new Button("button-station-add-to-calendar-up.png", "button-station-add-to-calendar-down.png");
  private Button _buttonAddRemoveToFavorites = new Button("button-station-add-to-favorite-up.png", "button-station-add-to-favorite-down.png");
  private Label _labelGenre = new Label();
  private Label _labelDescription = new Label();
  private ItemVO _itemVO = null;
  private boolean _allowSendMessage = true;
  private PropertyChangeListener _mplayerPropertyChangeListener = new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
      if (propertyChangeEvent.getPropertyName().equals("PlaybackStarted"))
        playbackStatusChanged((Boolean) propertyChangeEvent.getNewValue());
    }
  };

  public RadioStationDetailsWindow(ItemVO itemVO, boolean allowSendMessage)
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
    this.setSize(new Dimension(560, 170));
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon("logo-" + _itemVO.getService() + ".png"));
    setDataSourceURL("http://www." + _itemVO.getService() + ".com/");
    -*/
    _imagePanelRadio.setLocation(20, 15);
    setTitle("Details of \"" + _itemVO.getName() + "\"");
    _labelDescription.setText(_itemVO.getDescription());
    BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(_itemVO.getIconURL(), 96, 96, null);
    if (bufferedImage != null)
      _imagePanelRadio.setImage(bufferedImage);
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
    _buttonPlayStop.setLocation(270, 70);
    _buttonPlayStop.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPlayStop_actionPerformed(actionEvent);
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
    _buttonAddToCalendar.setLocation(410, 70);
    _buttonAddToCalendar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddToCalendar_actionPerformed(actionEvent);
      }
    });
    _buttonAddRemoveToFavorites.setLocation(480, 70);
    _buttonAddRemoveToFavorites.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddRemoveToFavorites_actionPerformed(actionEvent);
      }
    });
    _labelGenre.setBounds(140, 15, 455, 20);
    _labelDescription.setBounds(140, 40, 455, 20);
    _panelContent.add(_labelDescription, null);
    _panelContent.add(_labelGenre, null);
    _panelContent.add(_buttonAddRemoveToFavorites, null);
    _panelContent.add(_buttonAddToCalendar, null);
    _panelContent.add(_buttonAddRemoveToRoute, null);
    _panelContent.add(_buttonPlayStop, null);
    _panelContent.add(_buttonLocateOnMap, null);
    _panelContent.add(_buttonSendToContact, null);
    _panelContent.add(_imagePanelRadio, null);
    if (Session.getInstance().isInFavorites(_itemVO))
      _buttonAddRemoveToFavorites.setIcons("button-station-remove-from-favorite-up.png", "button-station-remove-from-favorite-down.png", "button-station-remove-from-favorite-disabled.png");
    else
      _buttonAddRemoveToFavorites.setIcons("button-station-add-to-favorite-up.png", "button-station-add-to-favorite-down.png", "button-station-add-to-favorite-disabled.png");
    MPlayer.getInstance().addPropertyChangeListener(_mplayerPropertyChangeListener);
  }

  private void playbackStatusChanged(boolean started)
  {
    if (isCurrentRadioStation())
      _buttonPlayStop.setIcons("button-media-" + (started ? "stop" : "play") + "-up.png", "button-media-" + (started ? "stop" : "play") + "-down.png", null);
  }

  private boolean isCurrentRadioStation()
  {
    try
      {
        if (MPlayer.getInstance().getCurrentRadioStationItem() == null)
          return (false);
        return (MPlayer.getInstance().getCurrentRadioStationItem().getGlobalItemID().equals(_itemVO.getGlobalItemID()));
      }
    catch (NullPointerException nullPointerException)
      {
        return (false);
      }
  }

  protected void windowVisible()
  {
    playbackStatusChanged(MPlayer.getInstance().isPlaybackStarted());
    _buttonLocateOnMap.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonAddRemoveToRoute.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonSendToContact.setEnabled(_allowSendMessage);
    MapViewPanel mapViewPanel = (MapViewPanel) getParentPanel();
    mapViewPanel.addItemVOsPOI(_itemVO);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    MPlayer.getInstance().removePropertyChangeListener(_mplayerPropertyChangeListener);
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

  private void _buttonPlayStop_actionPerformed(ActionEvent actionEvent)
  {
    if (isCurrentRadioStation())
      {
        try
          {
            if (MPlayer.getInstance().isPlaybackStarted())
              {
                MPlayer.getInstance().stopPlayback();
                playbackStatusChanged(false);
              }
            else
              {
                MPlayer.getInstance().loadPlaylist(_itemVO.getAction("play").getHandlerURI());
                playbackStatusChanged(true);
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    else
      {
        MPlayer.getInstance().setCurrentRadioStationItem(_itemVO);
        MPlayer.getInstance().loadPlaylist(_itemVO.getAction("play").getHandlerURI());
      }
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
            _buttonAddRemoveToFavorites.setIcons("button-station-add-to-favorite-up.png", "button-station-add-to-favorite-down.png", "button-station-add-to-favorite-disabled.png");
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
            _buttonAddRemoveToFavorites.setIcons("button-station-remove-from-favorite-up.png", "button-station-remove-from-favorite-down.png", "button-station-remove-from-favorite-disabled.png");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
  }
}
