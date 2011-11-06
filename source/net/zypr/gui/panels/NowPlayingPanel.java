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


package net.zypr.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingConstants;

import net.zypr.gui.Configuration;
import net.zypr.gui.components.Label;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.MPlayer;

public class NowPlayingPanel
  extends FlipCardPanel
{
  private Label _labelSource = new Label(SwingConstants.RIGHT);
  private Label _labelArtistBand = new Label(SwingConstants.RIGHT);
  private Label _labelSong = new Label(SwingConstants.RIGHT);
  private GridLayout gridLayout = new GridLayout();

  public NowPlayingPanel()
  {
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
    this.setLayout(gridLayout);
    gridLayout.setRows(3);
    gridLayout.setColumns(1);
    _labelSource.setFont(new Font("Dialog", 0, 14));
    _labelSource.setText("");
    _labelArtistBand.setFont(new Font("Dialog", 0, 14));
    _labelArtistBand.setText("");
    _labelSong.setFont(new Font("Dialog", 0, 14));
    _labelSong.setText("");
    _labelSource.setForeground(Configuration.getInstance().getColorProperty("top-bar-font-color", Color.WHITE));
    _labelArtistBand.setForeground(Configuration.getInstance().getColorProperty("top-bar-font-color", Color.WHITE));
    _labelSong.setForeground(Configuration.getInstance().getColorProperty("top-bar-font-color", Color.WHITE));
    this.add(_labelSource, null);
    this.add(_labelArtistBand, null);
    this.add(_labelSong, null);
    MPlayer.getInstance().addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent)
      {
        if (propertyChangeEvent.getPropertyName().equals("IcyStreamTitle"))
          {
            if (propertyChangeEvent.getNewValue() != null)
              {
                String value = propertyChangeEvent.getNewValue().toString();
                int index = value.indexOf(" - ");
                if (index != -1)
                  {
                    setArtistBand(value.substring(0, index).trim());
                    setSong(value.substring(index + 3).trim());
                  }
                else
                  {
                    setArtistBand(value.trim());
                    setSong(value.trim());
                  }
              }
          }
        else if (propertyChangeEvent.getPropertyName().equals("Name"))
          {
            if (propertyChangeEvent.getNewValue() != null)
              {
                if (MPlayer.getInstance().getCurrentRadioStationItem() != null)
                  setSource(MPlayer.getInstance().getCurrentRadioStationItem().getName().trim());
              }
          }
      }
    });
  }

  public void setSource(String source)
  {
    _labelSource.setText(source);
  }

  public String getSource()
  {
    return (_labelSource.getText());
  }

  public void setArtistBand(String artistBand)
  {
    _labelArtistBand.setText(artistBand);
  }

  public String getLabelArtistBand()
  {
    return (_labelArtistBand.getText());
  }

  public void setSong(String song)
  {
    _labelSong.setText(song);
  }

  public String getSong()
  {
    return (_labelSong.getText());
  }

  public void setInfo(String source, String artistBand, String song)
  {
    setSource(source);
    setArtistBand(artistBand);
    setSong(song);
  }
}
