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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.AddressVO;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.GeoBoundsVO;
import net.zypr.api.vo.GeoPointVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.RouteVO;
import net.zypr.api.vo.VoiceCommandDataVO;
import net.zypr.api.vo.WeatherCurrentVO;
import net.zypr.gps.GPSDevice;
import net.zypr.gui.Application;
import net.zypr.gui.Configuration;
import net.zypr.gui.Resources;
import net.zypr.gui.Settings;
import net.zypr.gui.TextToSpeech;
import net.zypr.gui.VoiceCommand;
import net.zypr.gui.audio.AudioPlayer;
import net.zypr.gui.audio.AudioRecorder;
import net.zypr.gui.audio.AudioRecorderException;
import net.zypr.gui.audio.GSMRecorder;
import net.zypr.gui.audio.WAVERecorder;
import net.zypr.gui.cache.AbstractCache;
import net.zypr.gui.cache.MarkerDiskCache;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.EmbossLabel;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.ToggleButton;
import net.zypr.gui.markers.Marker;
import net.zypr.gui.markers.MarkerPainter;
import net.zypr.gui.tiles.Tile;
import net.zypr.gui.tiles.TileFactory;
import net.zypr.gui.tiles.TileFactoryInfo;
import net.zypr.gui.tiles.ZYPRTileFactory;
import net.zypr.gui.utils.Converter;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.GeoUtil;
import net.zypr.gui.windows.CompanyNameWindow;
import net.zypr.gui.windows.ContactDetailsWindow;
import net.zypr.gui.windows.ModalWindow;
import net.zypr.gui.windows.POIDetailsWindow;
import net.zypr.gui.windows.POIListWindow;
import net.zypr.gui.windows.RadioStationDetailsWindow;
import net.zypr.gui.windows.RouteListWindow;
import net.zypr.gui.windows.WeatherCurrentWindow;
import net.zypr.mmp.mplayer.MPlayer;
import net.zypr.mmp.mplayer.TTSPlayer;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;

public class MapViewPanel
  extends GenericPanel
{
  public static boolean gpsPaused = false;
  private TopPanel _panelTop = new TopPanel();
  private BottomPanel _panelBottom = new BottomPanel();
  private ImagePanel _mapCenterIcon = new ImagePanel("icon-map-center.png", false);
  private ToggleButton _toggleButtonGPSLocation = new ToggleButton("button-go-to-gps-location-up.png", "button-go-to-gps-location-down.png", Configuration.getInstance().getFloatProperty("compass-button-alpha", 1.0f));
  private Button _buttonZoomIn = new Button("button-map-zoom-in-up.png", "button-map-zoom-in-down.png", Configuration.getInstance().getFloatProperty("zoom-in-button-alpha", 1.0f));
  private Button _buttonZoomOut = new Button("button-map-zoom-out-up.png", "button-map-zoom-out-down.png", Configuration.getInstance().getFloatProperty("zoom-out-button-alpha", 1.0f));
  private Button _buttonVoice = new Button("button-voice-command-button-up.png", "button-voice-command-button-down.png", "button-voice-command-button-disabled.png", Configuration.getInstance().getFloatProperty("voice-command-button-alpha", 1.0f));
  private EmbossLabel _labelAddress = new EmbossLabel();
  private EmbossLabel _labelWeather = new EmbossLabel(SwingConstants.RIGHT);
  private LocationPanel _locationPanel = new LocationPanel();
  private WeatherPanel _weatherPanel = new WeatherPanel();
  private int _currentZoom = 5;
  private Point2D _mapCenter = new Point2D.Double(0, 0);
  private TileFactory _tileFactory = new ZYPRTileFactory();
  private boolean _panEnabled = true;
  private boolean _zoomEnabled = true;
  private boolean _recenterOnDoubleClick = true;
  private boolean _zoomInOnDoubleClick = true;
  private MarkerPainter _markerPainter = new MarkerPainter();
  private Image _loadingImage = Resources.getInstance().getImage("tile-map-loading.png");
  private Image _positionMarkerImage = Resources.getInstance().getImage("icon-gps-location-shared.png");
  private boolean _restrictOutsidePanning = false;
  private boolean _horizontalWrapped = true;
  private AbstractCache _markerCache = new MarkerDiskCache();
  private ItemType[] _displayedItemTypes = new ItemType[ItemType.values().length];
  private TileLoadListener _tileLoadListener = new TileLoadListener();
  private Point _mouseDragStartPoint;
  private Timer _timerBottomBar = null;
  private Timer _timerMapCursor = null;
  private Timer _timerReverseGeocode = null;
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  private boolean _speechButtonDown = false;
  private int _speechVolume = 0;
  private AudioRecorder _audioRecorder = null;
  private AddressVO _userAddress = null;
  private AddressVO _reverseGeocodedAddress = null;
  private Timer _timerPressAndHold = new Timer(1000, new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _timerPressAndHold_actionPerformed(actionEvent);
      }
    });
  private MouseEvent _mouseEventPressAndHold = null;
  private RouteVO _routeVO = null;
  private GeoPointVO[] _geoPointsRoute = new GeoPointVO[0];
  private ItemVO[] _itemVOsISR = new ItemVO[0];
  private ItemVO[] _itemVOsPOI = new ItemVO[0];
  private ContextDataVO[] _genericContextData = new ContextDataVO[0];
  private JTextField _textFieldTextToSpeech = new JTextField();
  private JButton _buttonTextToSpeech = new JButton();

  public MapViewPanel()
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
    Session.getInstance().addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent)
      {
        sessionUpdate(propertyChangeEvent);
      }
    });
    _textFieldTextToSpeech.setBounds(new Rectangle(0, 350, 165, 20));
    _textFieldTextToSpeech.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _textFieldTextToSpeech_keyPressed(keyEvent);
      }
    });
    _buttonTextToSpeech.setText("*");
    _buttonTextToSpeech.setBounds(new Rectangle(180, 350, 20, 20));
    _buttonTextToSpeech.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonTextToSpeech_actionPerformed(actionEvent);
      }
    });
    if (Configuration.getInstance().getBooleanProperty("fake-voice-input-visible", false))
      {
        this.add(_buttonTextToSpeech, null);
        this.add(_textFieldTextToSpeech, null);
      }
    _labelAddress.setForeground(Configuration.getInstance().getColorProperty("location-bar-font-color", Color.WHITE));
    _labelWeather.setForeground(Configuration.getInstance().getColorProperty("weather-bar-font-color", Color.WHITE));
    _labelWeather.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _labelWeather_mouseClicked(mouseEvent);
      }
    });
    this.add(_toggleButtonGPSLocation, null);
    this.add(_labelAddress, null);
    this.add(_labelWeather, null);
    this.add(_locationPanel, null);
    this.add(_weatherPanel, null);
    this.add(_buttonVoice, null);
    this.add(_buttonZoomIn, null);
    this.add(_buttonZoomOut, null);
    this.add(_panelTop, null);
    this.add(_panelBottom, null);
    this.add(_mapCenterIcon, null);
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    _mapCenterIcon.setVisible(false);
    _panelBottom.setVisible(false);
    _buttonVoice.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent mouseEvent)
      {
        _buttonVoice_mousePressed(mouseEvent);
      }

      public void mouseReleased(MouseEvent mouseEvent)
      {
        _buttonVoice_mouseReleased(mouseEvent);
      }
    });
    _buttonZoomIn.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonZoomIn_actionPerformed(actionEvent);
      }
    });
    _buttonZoomOut.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonZoomOut_actionPerformed(actionEvent);
      }
    });
    _toggleButtonGPSLocation.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _toggleButtonGPSLocation_actionPerformed(actionEvent);
      }
    });
    _toggleButtonGPSLocation.addLongClickActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _toggleButtonGPSLocation_longClickActionPerformed(actionEvent);
      }
    });
    this.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent componentEvent)
      {
        this_componentResized(componentEvent);
      }

      public void componentShown(ComponentEvent componentEvent)
      {
        this_componentShown(componentEvent);
      }
    });
    this.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        this_mouseClicked(mouseEvent);
      }

      public void mouseEntered(MouseEvent mouseEvent)
      {
        this_mouseEntered(mouseEvent);
      }

      public void mouseExited(MouseEvent mouseEvent)
      {
        this_mouseExited(mouseEvent);
      }

      public void mousePressed(MouseEvent mouseEvent)
      {
        this_mousePressed(mouseEvent);
      }

      public void mouseReleased(MouseEvent mouseEvent)
      {
        this_mouseReleased(mouseEvent);
      }
    });
    this.addMouseMotionListener(new MouseMotionAdapter()
    {
      public void mouseDragged(MouseEvent mouseEvent)
      {
        this_mouseDragged(mouseEvent);
      }

      public void mouseMoved(MouseEvent mouseEvent)
      {
        this_mouseMoved(mouseEvent);
      }
    });
    this.addMouseWheelListener(new MouseWheelListener()
    {
      public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent)
      {
        this_mouseWheelMoved(mouseWheelEvent);
      }
    });
    this.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        super.keyPressed(keyEvent);
        this_keyPressed(keyEvent);
      }

      @Override
      public void keyReleased(KeyEvent keyEvent)
      {
        super.keyReleased(keyEvent);
        this_keyReleased(keyEvent);
      }

      @Override
      public void keyTyped(KeyEvent keyEvent)
      {
        super.keyTyped(keyEvent);
        this_keyTyped(keyEvent);
      }
    });
    this.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent focusEvent)
      {
        this_focusGained(focusEvent);
      }

      public void focusLost(FocusEvent focusEvent)
      {
        this_focusLost(focusEvent);
      }
    });
    setBackgroundPainter(new AbstractPainter<JXPanel>()
    {
      protected void doPaint(Graphics2D graphics2D, JXPanel component, int width, int height)
      {
        doPaintComponent(graphics2D);
      }
    });
    setMapCenter(Session.getInstance().getPosition());
    Session.getInstance().setGeoBounds(GeoUtil.getMapBounds(this));
    GPSDevice.getInstance().addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent)
      {
        gpsDeviceUpdate(propertyChangeEvent);
      }
    });
    buildMarkers();
    Application.splashFrame.setVisible(false);
  }

  public void saveAsImage()
  {
    try
      {
        BufferedImage originalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = originalImage.createGraphics();
        this.paint(graphics2D);
        BufferedImage blurImage = new BufferedImage(originalImage.getWidth(this), originalImage.getHeight(this), BufferedImage.TYPE_INT_RGB);
        BufferedImage bwImage = new BufferedImage(originalImage.getWidth(this), originalImage.getHeight(this), Configuration.getInstance().getBooleanProperty("grayscale-saved-startup-screen", true) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);
        float[] matrix =
        { 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, /*- -*/
          1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, /*- -*/
          1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, /*- -*/
          1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, /*- -*/
          1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, /*- -*/
          1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f, 1.0f / 36.0f };
        Kernel kernel = new Kernel(6, 6, matrix);
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        if (Configuration.getInstance().getBooleanProperty("blur-saved-startup-screen", true))
          convolve.filter(originalImage, blurImage);
        else
          blurImage = originalImage;
        Graphics graphics = bwImage.getGraphics();
        graphics.drawImage(blurImage, 0, 0, null);
        ImageIO.write(bwImage, "jpeg", new File(System.getProperty("user.home") + File.separator + ".zypr" + File.separator + "screenshot.jpg"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void _buttonTextToSpeech_actionPerformed(ActionEvent actionEvent)
  {
    try
      {
        _textFieldTextToSpeech.setText(_textFieldTextToSpeech.getText().trim());
        File audioFile = TextToSpeech.convert(_textFieldTextToSpeech.getText());
        final MapViewPanel mapViewPanel = this;
        new Thread(new Runnable()
        {
          public void run()
          {
            try
              {
                GeoPointVO geoPointVO = null;
                if (getUserAddress() != null)
                  geoPointVO = new GeoPointVO(getUserAddress());
                else
                  geoPointVO = new GeoPointVO(getMapCenterAsPosition());
                VoiceCommandDataVO voiceCommandDataVO = new VoiceCommandDataVO(geoPointVO, getVoiceCommandState());
                voiceCommandDataVO.setAudioFormat("wav");
                voiceCommandDataVO.setContextStates(getContextStates());
                voiceCommandDataVO.setContextData(getContextData());
                VoiceCommand.Process(mapViewPanel, API.getInstance().getVoice().parse(voiceCommandDataVO));
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
            finally
              {
                _buttonVoice.setEnabled(true);
              }
          }
        }).start();
        AudioPlayer.getInstance().startPlayback(audioFile);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
  }

  private void _textFieldTextToSpeech_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == 10)
      _buttonTextToSpeech_actionPerformed(null);
  }

  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
  }

  public VoiceCommandContextState[] getContextStates()
  {
    Set<VoiceCommandContextState> setContextStates = new HashSet<VoiceCommandContextState>();
    if (MPlayer.getInstance().isPlaybackStarted())
      {
        setContextStates.add(VoiceCommandContextState.MEDIA_CONTROL);
        setContextStates.add(VoiceCommandContextState.MEDIA_MESSAGING);
        setContextStates.add(VoiceCommandContextState.MEDIA_PLAYING);
        setContextStates.add(VoiceCommandContextState.AUDIO_CONTROL);
      }
    if (_routeVO != null)
      setContextStates.add(VoiceCommandContextState.ROUTE_OPEN);
    ModalWindow modalWindow = getLastInStack();
    if (modalWindow != null && modalWindow.getVoiceCommandContextState() != null)
      for (int index = 0; index < modalWindow.getVoiceCommandContextState().length; index++)
        setContextStates.add(modalWindow.getVoiceCommandContextState()[index]);
    if (Session.getInstance().messageCount() > 0)
      setContextStates.add(VoiceCommandContextState.MESSAGE_LIST);
    return ((VoiceCommandContextState[]) setContextStates.toArray(new VoiceCommandContextState[0]));
  }

  private void showBottomBar()
  {
    if (_timerBottomBar != null && _timerBottomBar.isRunning())
      {
        _timerBottomBar.restart();
      }
    else
      {
        _panelBottom.showPanel();
        _timerBottomBar = new Timer(Configuration.getInstance().getIntegerProperty("milliseconds-for-bottom-bar-fadeout", 5000), new ActionListener()
            {
              public void actionPerformed(ActionEvent actionEvent)
              {
                _timerBottomBar.stop();
                _timerBottomBar = null;
                _panelBottom.hidePanel();
              }
            });
        _timerBottomBar.start();
      }
  }

  private void showMapCursor()
  {
    if (hasAnyWindows())
      return;
    if (_timerMapCursor != null && _timerMapCursor.isRunning())
      {
        _timerMapCursor.restart();
      }
    else
      {
        _mapCenterIcon.showPanel();
        _timerMapCursor = new Timer(Configuration.getInstance().getIntegerProperty("milliseconds-for-map-cursor-fadeout", 5000), new ActionListener()
            {
              public void actionPerformed(ActionEvent actionEvent)
              {
                _timerMapCursor.stop();
                _timerMapCursor = null;
                _mapCenterIcon.hidePanel();
              }
            });
        _timerMapCursor.start();
      }
    reverseGeocode();
  }

  public void updateCurrentWeather()
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            if (!_labelAddress.getText().trim().equals(""))
              {
                WeatherCurrentVO weatherCurrentVO = API.getInstance().getWeather().current(_labelAddress.getText());
                TemperatureScale temperatureScale = Settings.getInstance().getDTO().getTemperatureScale();
                _labelWeather.setText(((int) Converter.convertTemperatures(weatherCurrentVO.getScale(), temperatureScale, weatherCurrentVO.getCurrentTemperature())) + " " + temperatureScale.getSymbol() + " - " + weatherCurrentVO.getDescription());
              }
            else
              {
                _labelWeather.setText("");
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  private void reverseGeocode()
  {
    if (_timerReverseGeocode == null || !_timerReverseGeocode.isRunning())
      {
        _timerReverseGeocode = null;
        _timerReverseGeocode = new Timer(Configuration.getInstance().getIntegerProperty("reverse-geocode-update-interval-as-milliseconds", 3000), new ActionListener()
            {
              public void actionPerformed(ActionEvent actionEvent)
              {
                _timerReverseGeocode.stop();
                _timerReverseGeocode = null;
                new Thread(new Runnable()
                {
                  public void run()
                  {
                    try
                      {
                        setReverseGeocodedAddress(API.getInstance().getMap().reverseGeocode(getMapCenterAsPosition()));
                      }
                    catch (Exception exception)
                      {
                        setReverseGeocodedAddress(null);
                        Debug.displayStack(this, exception, 1);
                      }
                  }
                }).start();
              }
            });
        _timerReverseGeocode.start();
      }
  }

  private void doPaintComponent(Graphics2D graphics2D)
  {
    if (isVisible())
      {
        Rectangle viewportBounds = getViewportBounds();
        drawMapTiles(graphics2D, _currentZoom, viewportBounds);
        if (_markerPainter != null)
          _markerPainter.paint(graphics2D, this, getWidth(), getHeight());
        drawPosition(graphics2D, _currentZoom, viewportBounds);
        if (_routeVO != null)
          drawRoute(graphics2D, _currentZoom, viewportBounds);
      }
    super.paintBorder(graphics2D);
  }

  protected void drawMapTiles(final Graphics graphics, final int zoom, Rectangle viewportBounds)
  {
    int tileSize = getTileFactory().getTileSize();
    Dimension mapSize = getTileFactory().getMapSize(zoom);
    int numberOfTilesHorizontally = viewportBounds.width / tileSize + 2;
    int numberOfTilesVertically = viewportBounds.height / tileSize + 2;
    int tilePieceX = (int) Math.floor(viewportBounds.getX() / tileSize);
    int tilePieceY = (int) Math.floor(viewportBounds.getY() / tileSize);
    for (int horizontalTileIndex = 0; horizontalTileIndex <= numberOfTilesHorizontally; horizontalTileIndex++)
      for (int verticalTileIndex = 0; verticalTileIndex <= numberOfTilesVertically; verticalTileIndex++)
        {
          int itpx = horizontalTileIndex + tilePieceX;
          int itpy = verticalTileIndex + tilePieceY;
          if (graphics.getClipBounds().intersects(new Rectangle(itpx * tileSize - viewportBounds.x, itpy * tileSize - viewportBounds.y, tileSize, tileSize)))
            {
              Tile tile = getTileFactory().getTile(itpx, itpy, zoom);
              tile.addUniquePropertyChangeListener("Loaded", _tileLoadListener);
              int tileOnScreenX = ((itpx * getTileFactory().getTileSize()) - viewportBounds.x);
              int tileOnScreenY = ((itpy * getTileFactory().getTileSize()) - viewportBounds.y);
              if (!isTileOnMap(itpx, itpy, mapSize) && tile.isLoaded())
                {
                  graphics.drawImage(tile.getBufferedImage(), tileOnScreenX, tileOnScreenY, null);
                }
              else
                {
                  int imageX = (getTileFactory().getTileSize() - getLoadingImage().getWidth(null)) / 2;
                  int imageY = (getTileFactory().getTileSize() - getLoadingImage().getHeight(null)) / 2;
                  graphics.setColor(Color.BLACK);
                  graphics.fillRect(tileOnScreenX, tileOnScreenY, tileSize, tileSize);
                  graphics.drawImage(getLoadingImage(), tileOnScreenX + imageX, tileOnScreenY + imageY, null);
                }
            }
        }
  }

  protected void drawRoute(final Graphics2D graphics2D, final int zoom, Rectangle viewportBounds)
  {
    Dimension sizeInTiles = getTileFactory().getMapSize(zoom);
    int tileSize = getTileFactory().getTileSize();
    Dimension sizeInPixels = new Dimension(sizeInTiles.width * tileSize, sizeInTiles.height * tileSize);
    double viewportX = viewportBounds.getX();
    while (viewportX < 0)
      {
        viewportX += sizeInPixels.getWidth();
      }
    while (viewportX > sizeInPixels.getWidth())
      {
        viewportX -= sizeInPixels.getWidth();
      }
    Rectangle2D viewport2 = new Rectangle2D.Double(viewportX, viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
    int[] xPoints = new int[_routeVO.getGeoPositions().length];
    int[] yPoints = new int[_routeVO.getGeoPositions().length];
    for (int index = 0; index < _routeVO.getGeoPositions().length; index++)
      {
        Point2D point = getTileFactory().geoToPixel(_routeVO.getGeoPositions()[index], zoom);
        xPoints[index] = (int) (point.getX() - viewport2.getX());
        yPoints[index] = (int) (point.getY() - viewport2.getY());
      }
    graphics2D.setPaint(Color.BLACK);
    graphics2D.setStroke(new BasicStroke(9.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    graphics2D.setComposite(AlphaComposite.SrcOver.derive(0.5f));
    graphics2D.drawPolyline(xPoints, yPoints, _routeVO.getGeoPositions().length);
    graphics2D.setPaint(Color.GREEN);
    graphics2D.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    graphics2D.setComposite(AlphaComposite.SrcOver.derive(0.5f));
    graphics2D.drawPolyline(xPoints, yPoints, _routeVO.getGeoPositions().length);
  }

  protected void drawPosition(final Graphics2D graphics, final int zoom, Rectangle viewportBounds)
  {
    if (Session.getInstance().getPosition() != null)
      {
        Dimension sizeInTiles = getTileFactory().getMapSize(zoom);
        int tileSize = getTileFactory().getTileSize();
        Dimension sizeInPixels = new Dimension(sizeInTiles.width * tileSize, sizeInTiles.height * tileSize);
        double viewportX = viewportBounds.getX();
        while (viewportX < 0)
          viewportX += sizeInPixels.getWidth();
        while (viewportX > sizeInPixels.getWidth())
          viewportX -= sizeInPixels.getWidth();
        Rectangle2D viewport2 = new Rectangle2D.Double(viewportX, viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
        Rectangle2D viewport3 = new Rectangle2D.Double(viewportX - sizeInPixels.getWidth(), viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
        Point2D point = getTileFactory().geoToPixel(Session.getInstance().getPosition(), zoom);
        double pixelMeter = GeoUtil.getGeoMetersPerPixel(zoom);
        double halfRadius = Session.getInstance().getPositionAccuracy() / pixelMeter;
        if (viewport2.contains(point))
          {
            int x = (int) (point.getX() - viewport2.getX());
            int y = (int) (point.getY() - viewport2.getY());
            graphics.setPaint(Color.black);
            graphics.drawImage(_positionMarkerImage, x - (_positionMarkerImage.getWidth(this) / 2), y - (_positionMarkerImage.getHeight(this) / 2), this);
            graphics.setPaint(Color.cyan);
            graphics.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            graphics.fill(new Ellipse2D.Double(x - halfRadius, y - halfRadius, halfRadius * 2, halfRadius * 2));
            graphics.setComposite(AlphaComposite.SrcOver.derive(1.0f));
          }
        if (viewport3.contains(point))
          {
            int x = (int) (point.getX() - viewport3.getX());
            int y = (int) (point.getY() - viewport3.getY());
            graphics.setPaint(Color.black);
            graphics.drawImage(_positionMarkerImage, x - (_positionMarkerImage.getWidth(this) / 2), y - (_positionMarkerImage.getHeight(this) / 2), this);
            graphics.setPaint(Color.cyan);
            graphics.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            graphics.fill(new Ellipse2D.Double(x - halfRadius, y - halfRadius, halfRadius * 2, halfRadius * 2));
            graphics.setComposite(AlphaComposite.SrcOver.derive(1.0f));
          }
      }
  }

  private void sessionUpdate(PropertyChangeEvent propertyChangeEvent)
  {
    if (propertyChangeEvent.getPropertyName().equals("FriendList"))
      {
        buildMarkers();
      }
    else if (propertyChangeEvent.getPropertyName().equals("Messages"))
      {
        showTopBarNotification(Session.getInstance().getMessage(0).getName(), Settings.getInstance().getDTO().isTTSMessageHeader(), "notification-message-new-received.wav");
      }
    else if (propertyChangeEvent.getPropertyName().equals("UserInfo"))
      {
        if (Session.getInstance().getUserInfo().isLocationIsVisible())
          _positionMarkerImage = Resources.getInstance().getImage("icon-gps-location-shared.png");
        else
          _positionMarkerImage = Resources.getInstance().getImage("icon-gps-location-not-shared.png");
      }
  }

  public ItemVO findMarkerByNumber(int number)
  {
    for (Marker marker : (Set<Marker>) _markerPainter.getMarkers())
      if (marker.getIndex() == number)
        return (marker.getItem());
    return (null);
  }

  public ItemVO findMarkerByName(String name)
  {
    for (Marker marker : (Set<Marker>) _markerPainter.getMarkers())
      if (marker.getName().equalsIgnoreCase(name))
        return (marker.getItem());
    return (null);
  }

  public ItemVO findMarkerByService(String serviceID, String serviceName)
  {
    for (Marker marker : (Set<Marker>) _markerPainter.getMarkers())
      if (marker.getItem().getServiceItemID().equalsIgnoreCase(serviceID) && marker.getItem().getService().equalsIgnoreCase(serviceName))
        return (marker.getItem());
    return (null);
  }

  public void buildMarkers()
  {
    Set setMarkers = Collections.synchronizedSet(new HashSet());
    int count = 0;
    if (Session.getInstance().getFriendList() != null)
      for (int index = 0; index < Session.getInstance().getFriendList().length; index++)
        if (Session.getInstance().getFriendList()[index].getDisplay() == ItemDisplay.GEO && !Settings.getInstance().getDTO().isHiddenContact(Session.getInstance().getFriendList()[index].getServiceItemID(), Session.getInstance().getFriendList()[index].getService()))
          setMarkers.add(new Marker(Session.getInstance().getFriendList()[index], ++count));
    if (Session.getInstance().getFavorites() != null)
      for (int index = 0; index < Session.getInstance().getFavorites().length; index++)
        if (Session.getInstance().getFavorites()[index].getDisplay() == ItemDisplay.GEO)
          setMarkers.add(new Marker(Session.getInstance().getFavorites()[index], ++count));
    if (_itemVOsISR != null)
      for (int index = 0; index < _itemVOsISR.length; index++)
        if (_itemVOsISR[index].getDisplay() == ItemDisplay.GEO)
          setMarkers.add(new Marker(_itemVOsISR[index], ++count));
    if (_itemVOsPOI != null)
      for (int index = 0; index < _itemVOsPOI.length; index++)
        if (_itemVOsPOI[index].getDisplay() == ItemDisplay.GEO)
          setMarkers.add(new Marker(_itemVOsPOI[index], ++count));
    _markerPainter.setMarkers(setMarkers);
    repaint();
  }

  private void gpsDeviceUpdate(PropertyChangeEvent propertyChangeEvent)
  {
    if (propertyChangeEvent.getPropertyName().equals("Position"))
      {
        if (Session.getInstance().getPosition() == null)
          {
            Session.getInstance().setPosition((GeoPositionVO) propertyChangeEvent.getNewValue());
            setMapCenter(Session.getInstance().getPosition());
          }
        else
          {
            Session.getInstance().setPosition((GeoPositionVO) propertyChangeEvent.getNewValue());
            if (_toggleButtonGPSLocation.isSelected())
              setMapCenter(Session.getInstance().getPosition());
          }
      }
    else if (propertyChangeEvent.getPropertyName().equals("PositionDilutionOfPrecision"))
      {
        Session.getInstance().setPositionAccuracy((Double) propertyChangeEvent.getNewValue());
      }
    if (isVisible())
      repaint();
  }

  private boolean isTileOnMap(int x, int y, Dimension mapSize)
  {
    return (y < 0 || y >= mapSize.getHeight());
  }

  private Rectangle calculateViewportBounds(Point2D center)
  {
    Insets insets = getInsets();
    int viewportWidth = getWidth() - insets.left - insets.right;
    int viewportHeight = getHeight() - insets.top - insets.bottom;
    double viewportX = (center.getX() - viewportWidth / 2);
    double viewportY = (center.getY() - viewportHeight / 2);
    return (new Rectangle((int) viewportX, (int) viewportY, viewportWidth, viewportHeight));
  }

  private void this_componentResized(ComponentEvent componentEvent)
  {
    _panelTop.setSize(getWidth(), _panelTop.getHeight());
    _panelBottom.setSize(getWidth(), _panelBottom.getHeight());
    _panelTop.setLocation(0, 0);
    _panelBottom.setLocation(0, getHeight() - _panelBottom.getHeight());
    _mapCenterIcon.setLocation((getWidth() - _mapCenterIcon.getWidth()) / 2, (getHeight() - _mapCenterIcon.getHeight()) / 2);
    _buttonZoomOut.setLocation(getWidth() - _buttonZoomOut.getWidth() - 20, _panelBottom.getY() - _buttonZoomOut.getHeight() * 2);
    _buttonZoomIn.setLocation(getWidth() - _buttonZoomIn.getWidth() - 20, _buttonZoomOut.getY() - _buttonZoomIn.getHeight() * 2);
    _buttonVoice.setLocation(20, _panelBottom.getY() - _buttonVoice.getHeight() * 2);
    _toggleButtonGPSLocation.setLocation(20, _buttonVoice.getY() - _toggleButtonGPSLocation.getHeight() * 2);
    _labelAddress.setBounds(20, _panelBottom.getHeight() - 20, getWidth() - 20, 20);
    _labelWeather.setBounds(getWidth() / 2, _panelBottom.getHeight() - 20, (getWidth() / 2) - 20, 20);
    _locationPanel.setLocation(0, _panelBottom.getHeight() - 24);
    _weatherPanel.setLocation(getWidth() - _weatherPanel.getWidth(), _panelBottom.getHeight() - 24);
  }

  public Rectangle getViewportBounds()
  {
    return (calculateViewportBounds(_mapCenter));
  }

  private void this_componentShown(ComponentEvent componentEvent)
  {
    /*- Implement it -*/
    Debug.print(componentEvent);
  }

  private void this_focusGained(FocusEvent focusEvent)
  {
    /*- Implement it -*/
    Debug.print(focusEvent);
  }

  private void this_focusLost(FocusEvent focusEvent)
  {
    /*- Implement it -*/
    Debug.print(focusEvent);
  }

  private void _labelWeather_mouseClicked(MouseEvent mouseEvent)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            showWindow(new WeatherCurrentWindow(API.getInstance().getWeather().current("default", _labelAddress.getText()), _labelAddress.getText()));
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  private void this_mouseClicked(MouseEvent mouseEvent)
  {
    showBottomBar();
    if (mouseEvent.getClickCount() == 2)
      {
        if (isRecenterOnDoubleClick())
          {
            Rectangle viewportBounds = getViewportBounds();
            setMapCenter(new Point2D.Double(viewportBounds.getX() + mouseEvent.getX(), viewportBounds.getY() + mouseEvent.getY()));
          }
        if (isZoomInOnDoubleClick())
          _buttonZoomIn_actionPerformed(null);
      }
    else
      {
        Set<Marker> markerSet = null;
        if (getMarkerPainter() != null)
          markerSet = ((MarkerPainter) getMarkerPainter()).getMarkers();
        if (markerSet != null)
          {
            boolean found = false;
            for (Marker marker : markerSet)
              {
                if (marker.getClickableRectangle() != null && marker.getClickableRectangle().contains(mouseEvent.getX(), mouseEvent.getY()))
                  {
                    if (!found)
                      {
                        found = true;
                        marker.setSelected(true);
                      }
                    else
                      {
                        marker.setSelected(false);
                      }
                  }
                else
                  {
                    marker.setSelected(false);
                  }
              }
            if (found)
              repaint();
          }
      }
  }

  private void this_mouseDragged(MouseEvent mouseEvent)
  {
    _timerPressAndHold.stop();
    _mouseEventPressAndHold = null;
    unsetLockToGPS();
    showBottomBar();
    if (isPanEnabled())
      {
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        showMapCursor();
        Point currentMousePoint = mouseEvent.getPoint();
        double newMapCenterX = getMapCenterAsPoint().getX() - (currentMousePoint.x - _mouseDragStartPoint.x);
        double newMapCenterY = getMapCenterAsPoint().getY() - (currentMousePoint.y - _mouseDragStartPoint.y);
        if (newMapCenterY < 0)
          newMapCenterY = 0;
        int maxHeight = (int) (getTileFactory().getMapSize(getZoom()).getHeight() * getTileFactory().getTileSize());
        if (newMapCenterY > maxHeight)
          newMapCenterY = maxHeight;
        _mouseDragStartPoint = currentMousePoint;
        setMapCenter(new Point2D.Double(newMapCenterX, newMapCenterY));
      }
  }

  private void this_mouseEntered(MouseEvent mouseEvent)
  {
    /*- Implement it -*/
    Debug.print(mouseEvent);
  }

  private void this_mouseExited(MouseEvent mouseEvent)
  {
    /*- Implement it -*/
    Debug.print(mouseEvent);
  }

  private void this_mouseMoved(MouseEvent mouseEvent)
  {
    /*- Implement it -*/
    Debug.print(mouseEvent);
  }

  private void this_mousePressed(MouseEvent mouseEvent)
  {
    showBottomBar();
    _mouseDragStartPoint = mouseEvent.getPoint();
    if (!_timerPressAndHold.isRunning())
      _timerPressAndHold.start();
    _mouseEventPressAndHold = mouseEvent;
  }

  private void this_mouseReleased(MouseEvent mouseEvent)
  {
    showBottomBar();
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    _timerPressAndHold.stop();
    _mouseEventPressAndHold = null;
  }

  private void this_mouseWheelMoved(MouseWheelEvent mouseWheelEvent)
  {
    /*- Implement it -*/
    Debug.print(mouseWheelEvent);
  }

  private void this_keyPressed(KeyEvent keyEvent)
  {
    /*- Implement it -*/
    Debug.print(keyEvent);
  }

  private void this_keyReleased(KeyEvent keyEvent)
  {
    /*- Implement it -*/
    Debug.print(keyEvent);
  }

  private void this_keyTyped(KeyEvent keyEvent)
  {
    /*- Implement it -*/
    Debug.print(keyEvent);
  }

  public void setMarkerSelected(String globalItemID)
  {
    Set<Marker> markerSet = null;
    if (getMarkerPainter() != null)
      markerSet = ((MarkerPainter) getMarkerPainter()).getMarkers();
    if (markerSet != null)
      {
        boolean found = false;
        for (Marker marker : markerSet)
          {
            if (!found && marker.getItem().getGlobalItemID().equals(globalItemID))
              {
                found = true;
                marker.setSelected(true);
              }
          }
        if (found)
          repaint();
      }
  }

  private void _timerPressAndHold_actionPerformed(ActionEvent actionEvent)
  {
    Set<Marker> markerSet = null;
    if (getMarkerPainter() != null)
      markerSet = ((MarkerPainter) getMarkerPainter()).getMarkers();
    if (markerSet != null)
      {
        boolean found = false;
        for (Marker marker : markerSet)
          {
            if (marker.getClickableRectangle() != null && marker.getClickableRectangle().contains(_mouseEventPressAndHold.getX(), _mouseEventPressAndHold.getY()))
              {
                if (!found)
                  {
                    _timerPressAndHold.stop();
                    found = true;
                    marker.setSelected(true);
                    final ItemVO itemVO = (ItemVO) marker.getItem();
                    if (itemVO.getType() == ItemType.USER)
                      {
                        new Thread(new Runnable()
                        {
                          public void run()
                          {
                            try
                              {
                                showWindow(new ContactDetailsWindow(itemVO, (InfoContactTypeVO) API.getInstance().getInfoVO(itemVO.getAction("details").getHandlerURI()), true));
                              }
                            catch (Exception exception)
                              {
                                Debug.displayStack(this, exception);
                              }
                          }
                        }).start();
                      }
                    else if (itemVO.getType() == ItemType.RADIO)
                      {
                        showWindow(new RadioStationDetailsWindow(itemVO, true));
                      }
                    else if (itemVO.getType() == ItemType.POI)
                      {
                        showWindow(new POIDetailsWindow(itemVO, true));
                      }
                  }
                else
                  {
                    marker.setSelected(false);
                  }
              }
            else
              {
                marker.setSelected(false);
              }
          }
        if (found)
          repaint();
      }
  }

  public void zoomIn()
  {
    setZoom(getZoom() - 1);
  }

  private void _buttonZoomIn_actionPerformed(ActionEvent actionEvent)
  {
    zoomIn();
  }

  public void zoomOut()
  {
    setZoom(getZoom() + 1);
  }

  private void _buttonZoomOut_actionPerformed(ActionEvent actionEvent)
  {
    zoomOut();
  }

  public void setLocationToMapCenter()
  {
    if (!GPSDevice.getInstance().isAvailable())
      {
        Session.getInstance().setPosition(getMapCenterAsPosition());
        unsetLockToGPS();
        new Thread(new Runnable()
        {
          public void run()
          {
            try
              {
                InfoUserTypeVO newInfoUserTypeVO = new InfoUserTypeVO();
                newInfoUserTypeVO.setCurrentPosition(Session.getInstance().getPosition());
                API.getInstance().getUser().infoSet(Session.getInstance().getUserInfo(), newInfoUserTypeVO);
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception, 1);
              }
          }
        }).start();
        repaint();
      }
  }

  private void _toggleButtonGPSLocation_longClickActionPerformed(ActionEvent actionEvent)
  {
    setLocationToMapCenter();
  }

  private void _toggleButtonGPSLocation_actionPerformed(ActionEvent actionEvent)
  {
    if (_toggleButtonGPSLocation.isSelected())
      {
        setMapCenter(Session.getInstance().getPosition());
        if (!GPSDevice.getInstance().isAvailable())
          unsetLockToGPS();
      }
  }

  public boolean isLockToGPS()
  {
    return (_toggleButtonGPSLocation.isSelected());
  }

  public void setLockToGPS(boolean locked)
  {
    if (!_toggleButtonGPSLocation.isSelected() && locked)
      {
        setMapCenter(Session.getInstance().getPosition());
        if (!GPSDevice.getInstance().isAvailable())
          unsetLockToGPS();
      }
    else if (_toggleButtonGPSLocation.isSelected() && !locked)
      {
        unsetLockToGPS();
      }
  }

  public void unsetLockToGPS()
  {
    _toggleButtonGPSLocation.setSelected(false);
  }

  public void setZoomToBoundingBox(GeoBoundsVO geoBounds)
  {
    setZoomToBoundingBoxAndCenter(geoBounds, false);
  }

  public void setZoomToBoundingBoxAndCenter(GeoBoundsVO geoBounds)
  {
    setZoomToBoundingBoxAndCenter(geoBounds, true);
  }

  public void setZoomToBoundingBoxAndCenter(GeoBoundsVO geoBounds, boolean center)
  {
    int zoom = 0;
    boolean nwPointOnTheMap = false;
    boolean sePointOnTheMap = false;
    for (zoom = getTileFactory().getInfo().getMinimumZoomLevel(); zoom <= getTileFactory().getInfo().getMaximumZoomLevel() && !nwPointOnTheMap && !sePointOnTheMap; zoom++)
      {
        Rectangle viewportBounds = calculateViewportBounds(getTileFactory().geoToPixel(geoBounds.getCenter(), zoom));
        Dimension sizeInTiles = getTileFactory().getMapSize(zoom);
        int tileSize = getTileFactory().getTileSize();
        Dimension sizeInPixels = new Dimension(sizeInTiles.width * tileSize, sizeInTiles.height * tileSize);
        double viewportX = viewportBounds.getX();
        while (viewportX < 0)
          viewportX += sizeInPixels.getWidth();
        while (viewportX > sizeInPixels.getWidth())
          viewportX -= sizeInPixels.getWidth();
        Rectangle2D viewport2 = new Rectangle2D.Double(viewportX, viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
        Rectangle2D viewport3 = new Rectangle2D.Double(viewportX - sizeInPixels.getWidth(), viewportBounds.getY(), viewportBounds.getWidth(), viewportBounds.getHeight());
        Point2D nwPoint = getTileFactory().geoToPixel(geoBounds.getNorthWest(), zoom);
        Point2D sePoint = getTileFactory().geoToPixel(geoBounds.getSouthEast(), zoom);
        if (viewport2.contains(nwPoint) || viewport3.contains(nwPoint))
          nwPointOnTheMap = true;
        if (viewport2.contains(sePoint) || viewport3.contains(sePoint))
          sePointOnTheMap = true;
      }
    if (center)
      setMapCenter(geoBounds.getCenter());
    setZoom(zoom);
  }

  public void setZoom(int newZoom)
  {
    if (_currentZoom == newZoom)
      return;
    TileFactoryInfo tileFactoryInfo = getTileFactory().getInfo();
    if (tileFactoryInfo != null && (newZoom < tileFactoryInfo.getMinimumZoomLevel() || newZoom > tileFactoryInfo.getMaximumZoomLevel()))
      return;
    int oldCurrentZoomLevel = _currentZoom;
    Point2D oldMapCenter = getMapCenterAsPoint();
    Dimension oldMapSize = getTileFactory().getMapSize(oldCurrentZoomLevel);
    this._currentZoom = newZoom;
    propertyChangeSupport.firePropertyChange("CurrentZoomLevel", oldCurrentZoomLevel, newZoom);
    Dimension mapSize = getTileFactory().getMapSize(newZoom);
    setMapCenter(new Point2D.Double(oldMapCenter.getX() * (mapSize.getWidth() / oldMapSize.getWidth()), oldMapCenter.getY() * (mapSize.getHeight() / oldMapSize.getHeight())));
  }

  public void setZoomAsPercent(float percent)
  {
    if (percent < 0)
      percent = 0;
    else if (percent > 100)
      percent = 100;
    TileFactoryInfo tileFactoryInfo = getTileFactory().getInfo();
    setZoom((int) ((percent * (tileFactoryInfo.getMaximumZoomLevel() - tileFactoryInfo.getMinimumZoomLevel())) / 100.0) + tileFactoryInfo.getMinimumZoomLevel());
  }

  public int getZoom()
  {
    return (_currentZoom);
  }

  public int getZoomAsPercent()
  {
    TileFactoryInfo tileFactoryInfo = getTileFactory().getInfo();
    return ((int) ((_currentZoom * 100.0) / (tileFactoryInfo.getMaximumZoomLevel() - tileFactoryInfo.getMinimumZoomLevel())));
  }

  public void panMapLeft(boolean fast)
  {
    panMap((int) (getTileFactory().getTileSize() / 8) * -1, 0, fast);
  }

  public void panMapRight(boolean fast)
  {
    panMap((int) (getTileFactory().getTileSize() / 8), 0, fast);
  }

  public void panMapDown(boolean fast)
  {
    panMap(0, (int) (getTileFactory().getTileSize() / 8) * -1, fast);
  }

  public void panMapUp(boolean fast)
  {
    panMap(0, (int) (getTileFactory().getTileSize() / 8), fast);
  }

  public void panMapLeft()
  {
    panMap((int) (getTileFactory().getTileSize() / 8) * -1, 0, true);
  }

  public void panMapRight()
  {
    panMap((int) (getTileFactory().getTileSize() / 8), 0, true);
  }

  public void panMapDown()
  {
    panMap(0, (int) (getTileFactory().getTileSize() / 8) * -1, true);
  }

  public void panMapUp()
  {
    panMap(0, (int) (getTileFactory().getTileSize() / 8), true);
  }

  public void panMapLeft(int pixel)
  {
    panMap(Math.abs(pixel) * -1, 0, true);
  }

  public void panMapRight(int pixel)
  {
    panMap(Math.abs(pixel), 0, true);
  }

  public void panMapDown(int pixel)
  {
    panMap(0, Math.abs(pixel) * -1, true);
  }

  public void panMapUp(int pixel)
  {
    panMap(0, Math.abs(pixel), true);
  }

  public void panMapLeft(int pixel, boolean fast)
  {
    panMap(Math.abs(pixel) * -1, 0, fast);
  }

  public void panMapRight(int pixel, boolean fast)
  {
    panMap(Math.abs(pixel), 0, fast);
  }

  public void panMapDown(int pixel, boolean fast)
  {
    panMap(0, Math.abs(pixel) * -1, fast);
  }

  public void panMapUp(int pixel, boolean fast)
  {
    panMap(0, Math.abs(pixel), fast);
  }

  public void panMap(int xPixel, int yPixel, boolean fast)
  {
    unsetLockToGPS();
    double mapCenterX = getMapCenterAsPoint().getX() - xPixel;
    double mapCenterY = getMapCenterAsPoint().getY() - yPixel;
    if (mapCenterY < 0)
      mapCenterY = 0;
    int maxHeight = (int) (getTileFactory().getMapSize(getZoom()).getHeight() * getTileFactory().getTileSize());
    if (mapCenterY > maxHeight)
      mapCenterY = maxHeight;
    final double newMapCenterX = mapCenterX;
    final double newMapCenterY = mapCenterY;
    if (fast)
      {
        setMapCenter(new Point2D.Double(mapCenterX, mapCenterY));
      }
    else
      {
        new Thread(new Runnable()
        {
          public void run()
          {
            double xDifference = (getMapCenterAsPoint().getX() - newMapCenterX) / 8;
            double yDifference = (getMapCenterAsPoint().getY() - newMapCenterY) / 8;
            for (int x = 0; x < 8; x++)
              for (int y = 0; y < 8; y++)
                try
                  {
                    setMapCenter(new Point2D.Double(getMapCenterAsPoint().getX() + xDifference, getMapCenterAsPoint().getY() + yDifference), true);
                    Thread.sleep(25);
                  }
                catch (Exception exception)
                  {
                    Debug.displayStack(this, exception);
                  }
          }
        }).start();
      }
  }

  public void setMapCenter(Point2D mapCenter)
  {
    setMapCenter(mapCenter, false);
  }

  private void setMapCenter(Point2D mapCenter, boolean adjusting)
  {
    if (!adjusting)
      reverseGeocode();
    Point2D oldMapCenter = _mapCenter;
    this._mapCenter = mapCenter;
    propertyChangeSupport.firePropertyChange("MapCenter", oldMapCenter, mapCenter);
    Session.getInstance().setGeoBounds(GeoUtil.getMapBounds(this));
    repaint();
  }

  public Point2D getMapCenterAsPoint()
  {
    return (_mapCenter);
  }

  public void setMapCenter(GeoPositionVO geoPosition)
  {
    reverseGeocode();
    GeoPositionVO oldGeoPosition = getMapCenterAsPosition();
    setMapCenter(getTileFactory().geoToPixel(geoPosition, _currentZoom));
    firePropertyChange("centerPosition", oldGeoPosition, getMapCenterAsPosition());
    Session.getInstance().setGeoBounds(GeoUtil.getMapBounds(this));
    repaint();
  }

  public GeoPositionVO getMapCenterAsPosition()
  {
    return getTileFactory().pixelToGeo(_mapCenter, _currentZoom);
  }

  public void setTileFactory(TileFactory tileFactory)
  {
    TileFactory oldTileFactory = _tileFactory;
    this._tileFactory = tileFactory;
    propertyChangeSupport.firePropertyChange("TileFactory", oldTileFactory, tileFactory);
  }

  public TileFactory getTileFactory()
  {
    return (_tileFactory);
  }

  public void setPanEnabled(boolean panEnabled)
  {
    boolean oldPanEnabled = _panEnabled;
    this._panEnabled = panEnabled;
    propertyChangeSupport.firePropertyChange("PanEnabled", oldPanEnabled, panEnabled);
  }

  public boolean isPanEnabled()
  {
    return (_panEnabled);
  }

  public void setZoomEnabled(boolean zoomEnabled)
  {
    boolean oldZoomEnabled = _zoomEnabled;
    this._zoomEnabled = zoomEnabled;
    propertyChangeSupport.firePropertyChange("ZoomEnabled", oldZoomEnabled, zoomEnabled);
  }

  public boolean isZoomEnabled()
  {
    return (_zoomEnabled);
  }

  public void setRecenterOnDoubleClick(boolean recenterOnDoubleClick)
  {
    boolean oldRecenterOnDoubleClick = _recenterOnDoubleClick;
    this._recenterOnDoubleClick = recenterOnDoubleClick;
    propertyChangeSupport.firePropertyChange("RecenterOnDoubleClick", oldRecenterOnDoubleClick, recenterOnDoubleClick);
  }

  public boolean isRecenterOnDoubleClick()
  {
    return (_recenterOnDoubleClick);
  }

  public void setMarkerPainter(MarkerPainter _markerPainter)
  {
    MarkerPainter oldMarkerPainter = _markerPainter;
    this._markerPainter = _markerPainter;
    propertyChangeSupport.firePropertyChange("MarkerPainter", oldMarkerPainter, _markerPainter);
  }

  public MarkerPainter getMarkerPainter()
  {
    return (_markerPainter);
  }

  public void setLoadingImage(Image loadingImage)
  {
    Image oldLoadingImage = _loadingImage;
    this._loadingImage = loadingImage;
    propertyChangeSupport.firePropertyChange("LoadingImage", oldLoadingImage, loadingImage);
  }

  public Image getLoadingImage()
  {
    return (_loadingImage);
  }

  public void setRestrictOutsidePanning(boolean restrictOutsidePanning)
  {
    boolean oldRestrictOutsidePanning = _restrictOutsidePanning;
    this._restrictOutsidePanning = restrictOutsidePanning;
    propertyChangeSupport.firePropertyChange("RestrictOutsidePanning", oldRestrictOutsidePanning, restrictOutsidePanning);
  }

  public boolean isRestrictOutsidePanning()
  {
    return (_restrictOutsidePanning);
  }

  public void setHorizontalWrapped(boolean horizontalWrapped)
  {
    boolean oldHorizontalWrapped = _horizontalWrapped;
    this._horizontalWrapped = horizontalWrapped;
    propertyChangeSupport.firePropertyChange("HorizontalWrapped", oldHorizontalWrapped, horizontalWrapped);
  }

  public boolean isHorizontalWrapped()
  {
    return (_horizontalWrapped);
  }

  public void setMarkerCache(AbstractCache markerCache)
  {
    AbstractCache oldMarkerCache = _markerCache;
    this._markerCache = markerCache;
    propertyChangeSupport.firePropertyChange("MarkerCache", oldMarkerCache, markerCache);
  }

  public AbstractCache getMarkerCache()
  {
    return (_markerCache);
  }

  public void setDisplayedItemTypes(ItemType[] displayedItemTypes)
  {
    ItemType[] oldDisplayedItemTypes = _displayedItemTypes;
    this._displayedItemTypes = displayedItemTypes;
    propertyChangeSupport.firePropertyChange("DisplayedItemTypes", oldDisplayedItemTypes, displayedItemTypes);
  }

  public ItemType[] getDisplayedItemTypes()
  {
    return (_displayedItemTypes);
  }

  public void setTileLoadListener(MapViewPanel.TileLoadListener tileLoadListener)
  {
    MapViewPanel.TileLoadListener oldTileLoadListener = _tileLoadListener;
    this._tileLoadListener = tileLoadListener;
    propertyChangeSupport.firePropertyChange("TileLoadListener", oldTileLoadListener, tileLoadListener);
  }

  public MapViewPanel.TileLoadListener getTileLoadListener()
  {
    return (_tileLoadListener);
  }

  public void setZoomInOnDoubleClick(boolean zoomInOnDoubleClick)
  {
    boolean oldZoomInOnDoubleClick = _zoomInOnDoubleClick;
    this._zoomInOnDoubleClick = zoomInOnDoubleClick;
    propertyChangeSupport.firePropertyChange("ZoomInOnDoubleClick", oldZoomInOnDoubleClick, zoomInOnDoubleClick);
  }

  public boolean isZoomInOnDoubleClick()
  {
    return (_zoomInOnDoubleClick);
  }

  public ArrayList<ContextDataVO> getContextData()
  {
    ModalWindow modalWindow = getLastInStack();
    if (modalWindow == null || modalWindow instanceof CompanyNameWindow)
      {
        ArrayList<ContextDataVO> contextData = new ArrayList<ContextDataVO>();
        for (Marker marker : (Set<Marker>) _markerPainter.getMarkers())
          contextData.add(new ContextDataVO(marker.getName(), "" + marker.getIndex()));
        return (contextData);
      }
    else
      {
        return (modalWindow.getContextData());
      }
  }

  public VoiceCommandState getVoiceCommandState()
  {
    ModalWindow modalWindow = getLastInStack();
    if (modalWindow == null)
      return (VoiceCommandState.MAIN);
    else
      return (modalWindow.getVoiceCommandState());
  }

  public void setUserAddress(AddressVO userAddress)
  {
    AddressVO oldUserAddress = _userAddress;
    this._userAddress = userAddress;
    propertyChangeSupport.firePropertyChange("UserAddress", oldUserAddress, userAddress);
  }

  public AddressVO getUserAddress()
  {
    return (_userAddress);
  }

  private void _buttonVoice_mousePressed(MouseEvent mouseEvent)
  {
    if (Configuration.getInstance().getBooleanProperty("voice-action-toggle-button-enabled", false))
      {
        _speechButtonDown = !_speechButtonDown;
        if (!_speechButtonDown)
          return;
      }
    TTSPlayer.getInstance().stopPlayback();
    _speechVolume = MPlayer.getInstance().getPlaybackVolume();
    if (MPlayer.getInstance().isPlaybackStarted() && !MPlayer.getInstance().isPlaybackMuted() && MPlayer.getInstance().getPlaybackVolume() > Configuration.getInstance().getIntegerProperty("playback-volume-during-voice-command", 5))
      MPlayer.getInstance().setVolume(Configuration.getInstance().getIntegerProperty("playback-volume-during-voice-command", 5));
    if (_audioRecorder == null)
      {
        String encoderName = Configuration.getInstance().getProperty("voice-command-encoder", "gsm");
        if (encoderName.equalsIgnoreCase("gsm"))
          _audioRecorder = new GSMRecorder();
        else
          _audioRecorder = new WAVERecorder();
      }
    try
      {
        _audioRecorder.startRecording();
      }
    catch (AudioRecorderException audioRecorderException)
      {
        Debug.displayStack(this, audioRecorderException);
      }
  }

  private void _buttonVoice_mouseReleased(MouseEvent mouseEvent)
  {
    if (Configuration.getInstance().getBooleanProperty("voice-action-toggle-button-enabled", false) && _speechButtonDown)
      {
        _buttonVoice.setIcons("button-voice-command-button-down.png", "button-voice-command-button-down.png", "button-voice-command-button-disabled.png");
        return;
      }
    else
      {
        _buttonVoice.setIcons("button-voice-command-button-up.png", "button-voice-command-button-down.png", "button-voice-command-button-disabled.png");
      }
    _audioRecorder.stopRecording();
    _buttonVoice.setEnabled(false);
    final MapViewPanel mapViewPanel = this;
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            GeoPointVO geoPointVO = null;
            if (getUserAddress() != null)
              geoPointVO = new GeoPointVO(getUserAddress());
            else
              geoPointVO = new GeoPointVO(getMapCenterAsPosition());
            VoiceCommandDataVO voiceCommandDataVO = new VoiceCommandDataVO(geoPointVO, getVoiceCommandState());
            voiceCommandDataVO.setAudioFormat(_audioRecorder.getAudioType());
            voiceCommandDataVO.setContextStates(getContextStates());
            voiceCommandDataVO.setContextData(getContextData());
            VoiceCommand.Process(mapViewPanel, API.getInstance().getVoice().parse(voiceCommandDataVO));
          }
        catch (APICommunicationException apiCommunicationException)
          {
            Debug.displayStack(this, apiCommunicationException);
          }
        catch (APIServerErrorException apiServerErrorException)
          {
            Debug.displayStack(this, apiServerErrorException);
          }
        catch (APIProtocolException apiProtocolException)
          {
            Debug.displayStack(this, apiProtocolException);
          }
        finally
          {
            _buttonVoice.setEnabled(true);
          }
      }
    }).start();
    if (MPlayer.getInstance().isPlaybackStarted() && !MPlayer.getInstance().isPlaybackMuted())
      {
        MPlayer.getInstance().setVolume(_speechVolume);
      }
  }

  @Override
  public synchronized void showWindow(ModalWindow modalWindow)
  {
    super.showWindow(modalWindow);
    if (_timerMapCursor != null && _timerMapCursor.isRunning())
      {
        _timerMapCursor.stop();
        _timerMapCursor = null;
        _mapCenterIcon.hidePanel();
      }
  }

  @Override
  public synchronized void discardAndShowWindow(ModalWindow modalWindow)
  {
    super.discardAndShowWindow(modalWindow);
    if (_timerMapCursor != null && _timerMapCursor.isRunning())
      {
        _timerMapCursor.stop();
        _timerMapCursor = null;
        _mapCenterIcon.hidePanel();
      }
  }

  public void addRoute(final ItemVO itemVO)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            GeoPointVO[] newGeoPointsRoute = new GeoPointVO[_geoPointsRoute.length + 1];
            System.arraycopy(_geoPointsRoute, 0, newGeoPointsRoute, 0, _geoPointsRoute.length);
            newGeoPointsRoute[newGeoPointsRoute.length - 1] = new GeoPointVO(itemVO.getPosition());
            _geoPointsRoute = newGeoPointsRoute;
            GeoPointVO[] geoPoints = new GeoPointVO[_geoPointsRoute.length + 1];
            System.arraycopy(_geoPointsRoute, 0, geoPoints, 1, _geoPointsRoute.length);
            geoPoints[0] = new GeoPointVO(Session.getInstance().getPosition());
            setRouteVO(API.getInstance().getNav().routeGet(geoPoints));
            if (!(getLastInStack() instanceof RouteListWindow))
              discardAndShowWindow(new RouteListWindow((getRouteVO().getManuvers())));
            else
              showWindow(new RouteListWindow((getRouteVO().getManuvers())));
            setZoomToBoundingBoxAndCenter(getRouteVO().getGeoBounds());
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  public void clearRoute()
  {
    setRouteVO(null);
    _geoPointsRoute = new GeoPointVO[0];
  }

  public void setRouteVO(RouteVO routeVO)
  {
    RouteVO oldRouteVO = _routeVO;
    this._routeVO = routeVO;
    propertyChangeSupport.firePropertyChange("RouteVO", oldRouteVO, routeVO);
  }

  public RouteVO getRouteVO()
  {
    return (_routeVO);
  }

  public void setItemVOsISR(ItemVO[] itemVOsISR)
  {
    ItemVO[] oldItemVOsISR = _itemVOsISR;
    this._itemVOsISR = itemVOsISR;
    propertyChangeSupport.firePropertyChange("ItemVOsISR", oldItemVOsISR, itemVOsISR);
  }

  public void addItemVOsISR(ItemVO itemVO)
  {
    for (int index = 0; index < _itemVOsISR.length; index++)
      if (itemVO.getGlobalItemID().equals(_itemVOsISR[index].getGlobalItemID()))
        return;
    ItemVO[] oldItemVOsISR = _itemVOsISR;
    this._itemVOsISR = new ItemVO[oldItemVOsISR.length + 1];
    this._itemVOsISR[this._itemVOsISR.length - 1] = itemVO;
    System.arraycopy(oldItemVOsISR, 0, this._itemVOsISR, 0, oldItemVOsISR.length);
    propertyChangeSupport.firePropertyChange("ItemVOsISR", oldItemVOsISR, this._itemVOsISR);
    buildMarkers();
  }

  public ItemVO[] getItemVOsISR()
  {
    return (_itemVOsISR);
  }

  public void setItemVOsPOI(ItemVO[] itemVOsPOI)
  {
    setItemVOsPOI(itemVOsPOI, false);
  }

  public void setItemVOsPOI(ItemVO[] itemVOsPOI, boolean discard)
  {
    ItemVO[] oldItemVOsPOI = _itemVOsPOI;
    this._itemVOsPOI = itemVOsPOI;
    propertyChangeSupport.firePropertyChange("ItemVOsPOI", oldItemVOsPOI, itemVOsPOI);
    if (discard)
      discardAndShowWindow(new POIListWindow(itemVOsPOI, null));
    else
      showWindow(new POIListWindow(itemVOsPOI, null));
  }

  public void setItemVOsPOI(ItemVO[] itemVOsPOI, String keyword)
  {
    ItemVO[] oldItemVOsPOI = _itemVOsPOI;
    this._itemVOsPOI = itemVOsPOI;
    propertyChangeSupport.firePropertyChange("ItemVOsPOI", oldItemVOsPOI, itemVOsPOI);
    showWindow(new POIListWindow(itemVOsPOI, keyword));
  }

  public void addItemVOsPOI(ItemVO itemVO)
  {
    for (int index = 0; index < _itemVOsPOI.length; index++)
      if (itemVO.getGlobalItemID().equals(_itemVOsPOI[index].getGlobalItemID()))
        return;
    ItemVO[] oldItemVOsPOI = _itemVOsPOI;
    this._itemVOsPOI = new ItemVO[oldItemVOsPOI.length + 1];
    this._itemVOsPOI[this._itemVOsPOI.length - 1] = itemVO;
    System.arraycopy(oldItemVOsPOI, 0, this._itemVOsPOI, 0, oldItemVOsPOI.length);
    propertyChangeSupport.firePropertyChange("ItemVOsPOI", oldItemVOsPOI, this._itemVOsPOI);
    buildMarkers();
  }

  public ItemVO[] getItemVOsPOI()
  {
    return (_itemVOsPOI);
  }

  public void setReverseGeocodedAddress(AddressVO reverseGeocodedAddress)
  {
    AddressVO oldReverseGeocodedAddress = _reverseGeocodedAddress;
    _reverseGeocodedAddress = reverseGeocodedAddress;
    if (reverseGeocodedAddress != null)
      {
        String text = reverseGeocodedAddress.toCityStateCountry();
        if (oldReverseGeocodedAddress == null || !oldReverseGeocodedAddress.toCityStateCountry().equals(text))
          updateCurrentWeather();
        _labelAddress.setText(text);
      }
    else
      {
        _labelAddress.setText("");
        _labelWeather.setText("");
      }
    propertyChangeSupport.firePropertyChange("ReverseGeocodedAddress", oldReverseGeocodedAddress, reverseGeocodedAddress);
  }

  public AddressVO getReverseGeocodedAddress()
  {
    return (_reverseGeocodedAddress);
  }

  private final class TileLoadListener
    implements PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
      if ("Loaded".equals(propertyChangeEvent.getPropertyName()) && Boolean.TRUE.equals(propertyChangeEvent.getNewValue()))
        if (((Tile) propertyChangeEvent.getSource()).getZoom() == getZoom())
          repaint();
    }
  }

  public void showTopBarNotification(String message)
  {
    showTopBarNotification(message, true, null);
  }

  public void showTopBarNotification(final String message, boolean ttsEnabled)
  {
    _panelTop.setNotificationMessage(message, ttsEnabled, null);
  }

  public void showTopBarNotification(final String message, boolean ttsEnabled, String sound)
  {
    _panelTop.setNotificationMessage(message, ttsEnabled, sound);
  }
}
