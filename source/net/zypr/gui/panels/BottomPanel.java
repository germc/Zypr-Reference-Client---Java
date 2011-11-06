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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingConstants;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.gui.Configuration;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.windows.AgendaItemListWindow;
import net.zypr.gui.windows.ContactListWindow;
import net.zypr.gui.windows.FavoritesListWindow;
import net.zypr.gui.windows.MediaControllerWindow;
import net.zypr.gui.windows.MessageListWindow;
import net.zypr.gui.windows.RouteListWindow;
import net.zypr.gui.windows.SettingsWindow;
import net.zypr.gui.windows.WarningWindow;
import net.zypr.mmp.mplayer.MPlayer;

import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.MattePainter;

public class BottomPanel
  extends GenericPanel
{
  private FlowLayout _flowLayout = new FlowLayout();
  private Button _buttonCalendar = new Button("button-bottom-bar-calendar-up.png", "button-bottom-bar-calendar-down.png");
  private Button _buttonLayers = new Button("button-bottom-bar-layers-up.png", "button-bottom-bar-layers-down.png");
  private Button _buttonMediaController = new Button("button-bottom-bar-media-controller-up.png", "button-bottom-bar-media-controller-down.png");
  private Button _buttonMessages = new Button("button-bottom-bar-messages-up.png", "button-bottom-bar-messages-down.png");
  private Button _buttonMuteUnmute = new Button("button-bottom-bar-mute-up.png", "button-bottom-bar-mute-down.png");
  private Button _buttonContacts = new Button("button-bottom-bar-people-up.png", "button-bottom-bar-people-down.png");
  private Button _buttonRoute = new Button("button-bottom-bar-route-up.png", "button-bottom-bar-route-down.png");
  private Button _buttonSettings = new Button("button-bottom-bar-settings-up.png", "button-bottom-bar-settings-down.png");
  private Button _buttonFavorites = new Button("button-bottom-bar-favorites-up.png", "button-bottom-bar-favorites-down.png");
  private Button _buttonQuit = new Button("button-bottom-bar-quit-up.png", "button-bottom-bar-quit-down.png");

  public BottomPanel()
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
    setOpaque(false);
    setSize(0, 80);
    this.setLayout(_flowLayout);
    AlphaPainter alphaPainter = new AlphaPainter();
    alphaPainter.setPainters(new MattePainter(Configuration.getInstance().getColorProperty("bottom-bar-background-color", new Color(0, 0, 0, 196))));
    setBackgroundPainter(alphaPainter);
    _flowLayout.setHgap(Configuration.getInstance().getIntegerProperty("bottom-menu-horizontal-gap", 6));
    _flowLayout.setVgap(Configuration.getInstance().getIntegerProperty("bottom-menu-vertical-gap", 5));
    _buttonCalendar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCalendar_actionPerformed(actionEvent);
      }
    });
    _buttonLayers.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonLayers_actionPerformed(actionEvent);
      }
    });
    _buttonMediaController.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonMediaController_actionPerformed(actionEvent);
      }
    });
    _buttonMessages.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonMessages_actionPerformed(actionEvent);
      }
    });
    _buttonMuteUnmute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonMuteUnmute_actionPerformed(actionEvent);
      }
    });
    _buttonContacts.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonContacts_actionPerformed(actionEvent);
      }
    });
    _buttonRoute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonRoute_actionPerformed(actionEvent);
      }
    });
    _buttonSettings.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSettings_actionPerformed(actionEvent);
      }
    });
    _buttonFavorites.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonFavorites_actionPerformed(actionEvent);
      }
    });
    _buttonQuit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonQuit_actionPerformed(actionEvent);
      }
    });
    String alignment = Configuration.getInstance().getProperty("bottom-menu-alignment", "center");
    if (alignment.equalsIgnoreCase("left"))
      {
        _flowLayout.setAlignment(FlowLayout.LEFT);
      }
    else if (alignment.equalsIgnoreCase("right"))
      {
        _flowLayout.setAlignment(FlowLayout.RIGHT);
      }
    else
      {
        _flowLayout.setAlignment(FlowLayout.CENTER);
        Configuration.getInstance().setProperty("bottom-menu-alignment", "center");
      }
    String[] buttonOrder = Configuration.getInstance().getProperty("bottom-menu-button-order", "settings,messages,contacts,favorites,route,media,mute,quit").split("(,|\\s)+");
    Separator separator = new Separator(SwingConstants.VERTICAL);
    separator.setPreferredSize(new Dimension(1, 70));
    this.add(separator, null);
    for (int index = 0; index < buttonOrder.length; index++)
      {
        boolean found = false;
        if (buttonOrder[index].equalsIgnoreCase("settings"))
          {
            found = true;
            this.add(_buttonSettings, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("layers"))
          {
            found = true;
            this.add(_buttonLayers, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("calendar"))
          {
            found = true;
            this.add(_buttonCalendar, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("messages"))
          {
            found = true;
            this.add(_buttonMessages, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("contacts"))
          {
            found = true;
            this.add(_buttonContacts, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("route"))
          {
            found = true;
            this.add(_buttonRoute, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("media"))
          {
            found = true;
            this.add(_buttonMediaController, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("mute"))
          {
            found = true;
            this.add(_buttonMuteUnmute, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("favorites"))
          {
            found = true;
            this.add(_buttonFavorites, null);
          }
        else if (buttonOrder[index].equalsIgnoreCase("quit"))
          {
            found = true;
            this.add(_buttonQuit, null);
          }
        else
          {
            Debug.displayWarning(this, "Unknown \"bottom-menu-button-order\" value : " + buttonOrder[index]);
          }
        if (found)
          {
            separator = new Separator(SwingConstants.VERTICAL);
            separator.setPreferredSize(new Dimension(1, 70));
            this.add(separator, null);
          }
      }
    if (MPlayer.getInstance().isAvailable())
      {
        MPlayer.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {
          public void propertyChange(PropertyChangeEvent propertyChangeEvent)
          {
            if (propertyChangeEvent.getPropertyName().equals("PlaybackMuted"))
              {
                updateMuteButton();
              }
          }
        });
      }
  }

  private void updateMuteButton()
  {
    if (MPlayer.getInstance().isPlaybackMuted())
      _buttonMuteUnmute.setIcons("button-bottom-bar-unmute-up.png", "button-bottom-bar-unmute-down.png", null);
    else
      _buttonMuteUnmute.setIcons("button-bottom-bar-mute-up.png", "button-bottom-bar-mute-down.png", null);
  }

  private void _buttonCalendar_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParent()).discardAllWindows();
    ((MapViewPanel) getParent()).showWindow(new AgendaItemListWindow());
  }

  private void _buttonLayers_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParent()).discardAllWindows();
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonMediaController_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParent()).discardAllWindows();
    ((MapViewPanel) getParent()).showWindow(new MediaControllerWindow());
  }

  private void _buttonMessages_actionPerformed(ActionEvent actionEvent)
  {
    if (Session.getInstance().messageCount() == -1)
      {
        ((MapViewPanel) getParent()).showWindow(new WarningWindow("Message List Loading", "Message list is still loading. Please try to open message list later.", 30, "message-list-still-loading-warning.wav"));
      }
    else if (Session.getInstance().messageCount() == 0)
      {
        ((MapViewPanel) getParent()).showWindow(new WarningWindow("No Messages", "You have no messages.", 30, "message-list-no-messages-warning.wav"));
      }
    else
      {
        ((MapViewPanel) getParent()).discardAllWindows();
        ((MapViewPanel) getParent()).showWindow(new MessageListWindow());
      }
  }

  private void _buttonMuteUnmute_actionPerformed(ActionEvent actionEvent)
  {
    MPlayer.getInstance().setMute(!MPlayer.getInstance().isPlaybackMuted());
  }

  private void _buttonContacts_actionPerformed(ActionEvent actionEvent)
  {
    if (Session.getInstance().getFriendList() == null)
      {
        ((MapViewPanel) getParent()).showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
      }
    else if (Session.getInstance().getFriendList().length == 0)
      {
        ((MapViewPanel) getParent()).showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
      }
    else
      {
        ((MapViewPanel) getParent()).discardAllWindows();
        ((MapViewPanel) getParent()).showWindow(new ContactListWindow());
      }
  }

  private void _buttonRoute_actionPerformed(ActionEvent actionEvent)
  {
    if (((MapViewPanel) getParent()).getRouteVO() == null)
      {
        ((MapViewPanel) getParent()).showWindow(new WarningWindow("No Routing", "You have not created a routing yet. Please add places to route.", 30, "route-list-no-places-warning.wav"));
      }
    else
      {
        ((MapViewPanel) getParent()).discardAllWindows();
        ((MapViewPanel) getParent()).showWindow(new RouteListWindow(((MapViewPanel) getParent()).getRouteVO().getManuvers()));
      }
  }

  private void _buttonFavorites_actionPerformed(ActionEvent actionEvent)
  {
    if (!Session.getInstance().hasFavorites())
      {
        ((MapViewPanel) getParent()).showWindow(new WarningWindow("No Favorites", "You do not have favorites yet. Please add one.", 30, "favorites-list-no-favorites-warning.wav"));
      }
    else
      {
        ((MapViewPanel) getParent()).discardAllWindows();
        ((MapViewPanel) getParent()).showWindow(new FavoritesListWindow());
      }
  }

  private void _buttonSettings_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParent()).discardAllWindows();
    ((MapViewPanel) getParent()).showWindow(new SettingsWindow());
  }

  private void _buttonQuit_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParent()).saveAsImage();
    Configuration.getInstance().save();
    Settings.getInstance().save();
    try
      {
        API.getInstance().getAuth().logout();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    System.exit(0);
  }
}
