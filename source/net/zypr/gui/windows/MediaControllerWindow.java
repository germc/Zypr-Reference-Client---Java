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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.ByteArrayInputStream;

import net.zypr.api.API;
import net.zypr.api.vo.ShopItemVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.cache.AbstractCache;
import net.zypr.gui.cache.AlbumArtDiskCache;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.WebBrowser;
import net.zypr.mmp.mplayer.MPlayer;

import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class MediaControllerWindow
  extends ModalWindow
{
  private static AbstractCache _albumArtCache = new AlbumArtDiskCache();
  private ImagePanel _imagePanelAlbumArt = new ImagePanel("placeholder-picture-album-96.png");
  private Button _buttonInfo = new Button("button-media-info-up.png", "button-media-info-down.png", "button-media-info-disabled.png");
  private Button _buttonPrevious = new Button("button-media-previous-up.png", "button-media-previous-down.png", "button-media-previous-disabled.png");
  private Button _buttonPlayStop = new Button("button-media-play-up.png", "button-media-play-down.png", "button-media-play-disabled.png");
  private Button _buttonNext = new Button("button-media-next-up.png", "button-media-next-down.png", "button-media-next-disabled.png");
  private Button _buttonVolumeDown = new Button("button-media-volume-down-up.png", "button-media-volume-down-down.png", "button-media-volume-down-disabled.png");
  private Button _buttonVolumeUp = new Button("button-media-volume-up-up.png", "button-media-volume-up-down.png", "button-media-volume-up-disabled.png");
  private Label _labelArtistBand = new Label();
  private Label _labelSong = new Label();
  private String _shoppingLink = null;
  private PropertyChangeListener _mplayerPropertyChangeListener = new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
      if (propertyChangeEvent.getPropertyName().equals("IcyStreamTitle"))
        {
          setIcyStreamTitle((String) propertyChangeEvent.getNewValue());
          setAlbumArt();
        }
      else if (propertyChangeEvent.getPropertyName().equals("PlaybackStarted"))
        {
          boolean started = (Boolean) propertyChangeEvent.getNewValue();
          _buttonPlayStop.setIcons("button-media-" + (started ? "stop" : "play") + "-up.png", "button-media-" + (started ? "stop" : "play") + "-down.png", null);
          _buttonVolumeUp.setEnabled(started);
          _buttonVolumeDown.setEnabled(started);
        }
      if (propertyChangeEvent.getPropertyName().equalsIgnoreCase("PlaybackVolume"))
        {
          int newValue = (Integer) propertyChangeEvent.getNewValue();
          _buttonVolumeUp.setEnabled(newValue < 100);
          _buttonVolumeDown.setEnabled(newValue > 0);
        }
    }
  };

  public MediaControllerWindow()
  {
    super();
    try
      {
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
    this.setTitle("Media Player");
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon("logo-" + Session.getInstance().getMediaSource() + ".png"));
    setDataSourceURL("http://www." + Session.getInstance().getMediaSource() + ".com/");
    -*/
    _imagePanelAlbumArt.setLocation(20, 15);
    _imagePanelAlbumArt.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelAlbumArt_mouseClicked(mouseEvent);
      }
    });
    _buttonInfo.setLocation(130, 70);
    _buttonInfo.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonInfo_actionPerformed(actionEvent);
      }
    });
    _buttonPrevious.setLocation(200, 70);
    _buttonPrevious.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPrevious_actionPerformed(actionEvent);
      }
    });
    _buttonPlayStop.setLocation(270, 70);
    _buttonPlayStop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPlayStop_actionPerformed(actionEvent);
      }
    });
    _buttonNext.setLocation(340, 70);
    _buttonNext.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNext_actionPerformed(actionEvent);
      }
    });
    _buttonVolumeDown.setLocation(410, 70);
    _buttonVolumeDown.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonVolumeDown_actionPerformed(actionEvent);
      }
    });
    _buttonVolumeUp.setLocation(480, 70);
    _buttonVolumeUp.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonVolumeUp_actionPerformed(actionEvent);
      }
    });
    _labelArtistBand.setBounds(140, 15, 455, 20);
    _labelArtistBand.setText("");
    _labelSong.setBounds(140, 40, 455, 20);
    _labelSong.setText("");
    _panelContent.add(_labelSong, null);
    _panelContent.add(_labelArtistBand, null);
    _panelContent.add(_buttonVolumeUp, null);
    _panelContent.add(_buttonVolumeDown, null);
    _panelContent.add(_buttonNext, null);
    _panelContent.add(_buttonPlayStop, null);
    _panelContent.add(_buttonPrevious, null);
    _panelContent.add(_buttonInfo, null);
    _panelContent.add(_imagePanelAlbumArt, null);
    if (MPlayer.getInstance().getCurrentRadioStationItem() != null)
      {
        setTitle(MPlayer.getInstance().getCurrentRadioStationItem().getName());
        if (MPlayer.getInstance().isPlaybackStarted())
          {
            _buttonPlayStop.setIcons("button-media-" + (MPlayer.getInstance().isPlaybackStarted() ? "stop" : "play") + "-up.png", "button-media-" + (MPlayer.getInstance().isPlaybackStarted() ? "stop" : "play") + "-down.png", null);
            _buttonVolumeUp.setEnabled(MPlayer.getInstance().isPlaybackStarted());
            _buttonVolumeDown.setEnabled(MPlayer.getInstance().isPlaybackStarted());
          }
      }
    MPlayer.getInstance().addPropertyChangeListener(_mplayerPropertyChangeListener);
    setIcyStreamTitle(MPlayer.getInstance().getIcyStreamTitle());
    setAlbumArt();
  }

  private void setIcyStreamTitle(String value)
  {
    if (value != null)
      {
        int index = value.indexOf(" - ");
        if (index != -1)
          {
            _labelArtistBand.setText(value.substring(0, index).trim());
            _labelSong.setText(value.substring(index + 3).trim());
          }
        else
          {
            _labelArtistBand.setText(value.trim());
            _labelSong.setText(value.trim());
          }
      }
  }

  private void setAlbumArt()
  {
    try
      {
        BufferedImage bufferedImage = null;
        _shoppingLink = null;
        if (!_labelArtistBand.getText().equals("") || !_labelSong.getText().equals(""))
          {
            ShopItemVO[] _shopItemVOs = API.getInstance().getShop().search("amazon", _labelArtistBand.getText(), _labelSong.getText(), null);
            if (_shopItemVOs != null && _shopItemVOs.length > 0 && _shopItemVOs[0].getAImageURI() != null)
              {
                _shoppingLink = _shopItemVOs[0].getUri();
                String imageURI = _shopItemVOs[0].getAImageURI();
                String cacheID = "" + imageURI.hashCode();
                bufferedImage = (BufferedImage) _albumArtCache.get(cacheID);
                if (bufferedImage == null)
                  {
                    byte[] byteImage = API.getInstance().getBytes(imageURI);
                    bufferedImage = GraphicsUtilities.loadCompatibleImage(new ByteArrayInputStream(byteImage));
                    _albumArtCache.put(cacheID, byteImage, bufferedImage);
                  }
              }
            else if (MPlayer.getInstance().getCurrentRadioStationItem() != null)
              {
                BufferedImage itemBufferedImage = ImageFetcher.getInstance().getImage(MPlayer.getInstance().getCurrentRadioStationItem().getIconURL(), 96, 96, null);
                if (itemBufferedImage != null)
                  _imagePanelAlbumArt.setImage(itemBufferedImage);
              }
          }
        if (bufferedImage != null)
          _imagePanelAlbumArt.setImage(bufferedImage);
        else if (MPlayer.getInstance().getCurrentRadioStationItem() != null)
          {
            BufferedImage itemBufferedImage = ImageFetcher.getInstance().getImage(MPlayer.getInstance().getCurrentRadioStationItem().getIconURL(), 96, 96, null);
            if (itemBufferedImage != null)
              _imagePanelAlbumArt.setImage(itemBufferedImage);
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
  }

  protected void windowVisible()
  {
    _buttonInfo.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    _buttonPrevious.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    _buttonPlayStop.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    _buttonNext.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    _buttonVolumeDown.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    _buttonVolumeUp.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    _imagePanelAlbumArt.setEnabled(MPlayer.getInstance().getCurrentRadioStationItem() != null);
    if (MPlayer.getInstance().getCurrentRadioStationItem() != null)
      {
        setTitle(MPlayer.getInstance().getCurrentRadioStationItem().getName());
        if (MPlayer.getInstance().isPlaybackStarted())
          {
            _buttonPlayStop.setIcons("button-media-" + (MPlayer.getInstance().isPlaybackStarted() ? "stop" : "play") + "-up.png", "button-media-" + (MPlayer.getInstance().isPlaybackStarted() ? "stop" : "play") + "-down.png", null);
            _buttonVolumeUp.setEnabled(MPlayer.getInstance().isPlaybackStarted());
            _buttonVolumeDown.setEnabled(MPlayer.getInstance().isPlaybackStarted());
          }
      }
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    MPlayer.getInstance().removePropertyChangeListener(_mplayerPropertyChangeListener);
    getParentPanel().discardWindow();
  }

  private void _buttonInfo_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParent()).showWindow(new RadioStationDetailsWindow(MPlayer.getInstance().getCurrentRadioStationItem(), true));
  }

  private void _buttonPrevious_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonPlayStop_actionPerformed(ActionEvent actionEvent)
  {
    try
      {
        if (MPlayer.getInstance().isPlaybackStarted())
          MPlayer.getInstance().stopPlayback();
        else
          MPlayer.getInstance().loadPlaylist(MPlayer.getInstance().getCurrentRadioStationItem().getAction("play").getHandlerURI());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void _buttonNext_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonVolumeDown_actionPerformed(ActionEvent actionEvent)
  {
    MPlayer.getInstance().setVolumeDown();
    ((MapViewPanel) getParent()).showTopBarNotification("Volume " + MPlayer.getInstance().getPlaybackVolume() + "%", false);
  }

  private void _buttonVolumeUp_actionPerformed(ActionEvent actionEvent)
  {
    MPlayer.getInstance().setVolumeUp();
    ((MapViewPanel) getParent()).showTopBarNotification("Volume " + MPlayer.getInstance().getPlaybackVolume() + "%", false);
  }

  private void _imagePanelAlbumArt_mouseClicked(MouseEvent mouseEvent)
  {
    if (_shoppingLink != null && !_shoppingLink.equals(""))
      WebBrowser.openURL(_shoppingLink);
  }
}
