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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.text.SimpleDateFormat;

import javax.swing.SwingConstants;

import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.vo.WeatherForecastVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Settings;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.utils.Converter;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.WebBrowser;

public class WeatherForecastWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelForecast1Day = new ImagePanel();
  private Label _labelForecast1DayDate = new Label();
  private Label _labelForecast1DayTemp = new Label();
  private Label _labelForecast1NightTemp = new Label();
  private Label _labelForecast1NightDate = new Label();
  private ImagePanel _imagePanelForecast1Night = new ImagePanel();
  private ImagePanel _imagePanelForecast2Night = new ImagePanel();
  private Label _labelForecast2NightDate = new Label();
  private Label _labelForecast2NightTemp = new Label();
  private Label _labelForecast2DayTemp = new Label();
  private Label _labelForecast2DayDate = new Label();
  private ImagePanel _imagePanelForecast2Day = new ImagePanel();
  private Label _labelForecast3NightTemp = new Label();
  private Label _labelForecast3NightDate = new Label();
  private ImagePanel _imagePanelForecast3Night = new ImagePanel();
  private Label _labelForecast3DayTemp = new Label();
  private Label _labelForecast3DayDate = new Label();
  private ImagePanel _imagePanelForecast3Day = new ImagePanel();
  private ImagePanel _imagePanelForecast4Night = new ImagePanel();
  private Label _labelForecast4NightDate = new Label();
  private Label _labelForecast4NightTemp = new Label();
  private Label _labelForecast4DayTemp = new Label();
  private Label _labelForecast4DayDate = new Label();
  private ImagePanel _imagePanelForecast4Day = new ImagePanel();
  private ImagePanel _imagePanelForecast5Night = new ImagePanel();
  private Label _labelForecast5NightDate = new Label();
  private Label _labelForecast5NightTemp = new Label();
  private Label _labelForecast5DayTemp = new Label();
  private Label _labelForecast5DayDate = new Label();
  private ImagePanel _imagePanelForecast5Day = new ImagePanel();
  private WeatherForecastVO[] _weatherForecastVOs = null;
  private String _placename = null;

  public WeatherForecastWindow(WeatherForecastVO[] weatherForecastVOs, String placename)
  {
    super();
    try
      {
        _weatherForecastVOs = weatherForecastVOs;
        _placename = placename;
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
    setTitle("5 Days Forecast");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Settings.getInstance().getDTO().getDateFormatPattern().getPattern());
    TemperatureScale temperatureScale = Settings.getInstance().getDTO().getTemperatureScale();
    BufferedImage bufferedImage = null;
    _imagePanelForecast1Day.setBounds(new Rectangle(20, 10, 80, 80));
    _imagePanelForecast1Day.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast1Day_mouseClicked(mouseEvent);
      }
    });
    _labelForecast1DayDate.setBounds(new Rectangle(5, 90, 110, 20));
    _labelForecast1DayDate.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast1DayTemp.setBounds(new Rectangle(5, 110, 110, 20));
    _labelForecast1DayTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast1NightTemp.setBounds(new Rectangle(5, 235, 110, 20));
    _labelForecast1NightTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast1NightDate.setBounds(new Rectangle(5, 215, 110, 20));
    _labelForecast1NightDate.setHorizontalAlignment(SwingConstants.CENTER);
    _imagePanelForecast1Night.setBounds(new Rectangle(20, 130, 80, 80));
    _imagePanelForecast1Night.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast1Night_mouseClicked(mouseEvent);
      }
    });
    _imagePanelForecast2Night.setBounds(new Rectangle(130, 130, 80, 80));
    _imagePanelForecast2Night.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast2Night_mouseClicked(mouseEvent);
      }
    });
    _labelForecast2NightDate.setBounds(new Rectangle(115, 215, 110, 20));
    _labelForecast2NightDate.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast2NightTemp.setBounds(new Rectangle(115, 235, 110, 20));
    _labelForecast2NightTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast2DayTemp.setBounds(new Rectangle(115, 110, 110, 20));
    _labelForecast2DayTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast2DayDate.setBounds(new Rectangle(115, 90, 110, 20));
    _labelForecast2DayDate.setHorizontalAlignment(SwingConstants.CENTER);
    _imagePanelForecast2Day.setBounds(new Rectangle(130, 10, 80, 80));
    _imagePanelForecast2Day.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast2Day_mouseClicked(mouseEvent);
      }
    });
    _labelForecast3NightTemp.setBounds(new Rectangle(225, 235, 110, 20));
    _labelForecast3NightTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast3NightDate.setBounds(new Rectangle(225, 215, 110, 20));
    _labelForecast3NightDate.setHorizontalAlignment(SwingConstants.CENTER);
    _imagePanelForecast3Night.setBounds(new Rectangle(240, 130, 80, 80));
    _imagePanelForecast3Night.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast3Night_mouseClicked(mouseEvent);
      }
    });
    _labelForecast3DayTemp.setBounds(new Rectangle(225, 110, 110, 20));
    _labelForecast3DayTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast3DayDate.setBounds(new Rectangle(225, 90, 110, 20));
    _labelForecast3DayDate.setHorizontalAlignment(SwingConstants.CENTER);
    _imagePanelForecast3Day.setBounds(new Rectangle(240, 10, 80, 80));
    _imagePanelForecast3Day.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast3Day_mouseClicked(mouseEvent);
      }
    });
    _imagePanelForecast4Night.setBounds(new Rectangle(350, 130, 80, 80));
    _imagePanelForecast4Night.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast4Night_mouseClicked(mouseEvent);
      }
    });
    _labelForecast4NightDate.setBounds(new Rectangle(335, 215, 110, 20));
    _labelForecast4NightDate.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast4NightTemp.setBounds(new Rectangle(335, 235, 110, 20));
    _labelForecast4NightTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast4DayTemp.setBounds(new Rectangle(335, 110, 110, 20));
    _labelForecast4DayTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast4DayDate.setBounds(new Rectangle(335, 90, 110, 20));
    _labelForecast4DayDate.setHorizontalAlignment(SwingConstants.CENTER);
    _imagePanelForecast4Day.setBounds(new Rectangle(350, 10, 80, 80));
    _imagePanelForecast4Day.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast4Day_mouseClicked(mouseEvent);
      }
    });
    _imagePanelForecast5Night.setBounds(new Rectangle(460, 130, 80, 80));
    _imagePanelForecast5Night.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast5Night_mouseClicked(mouseEvent);
      }
    });
    _labelForecast5NightDate.setBounds(new Rectangle(445, 215, 110, 20));
    _labelForecast5NightDate.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast5NightTemp.setBounds(new Rectangle(445, 235, 110, 20));
    _labelForecast5NightTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast5DayTemp.setBounds(new Rectangle(445, 110, 110, 20));
    _labelForecast5DayTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _labelForecast5DayDate.setBounds(new Rectangle(445, 90, 110, 20));
    _labelForecast5DayDate.setHorizontalAlignment(SwingConstants.CENTER);
    _imagePanelForecast5Day.setBounds(new Rectangle(460, 10, 80, 80));
    _imagePanelForecast5Day.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelForecast5Day_mouseClicked(mouseEvent);
      }
    });
    _panelContent.add(_imagePanelForecast5Day, null);
    _panelContent.add(_labelForecast5DayDate, null);
    _panelContent.add(_labelForecast5DayTemp, null);
    _panelContent.add(_labelForecast5NightTemp, null);
    _panelContent.add(_labelForecast5NightDate, null);
    _panelContent.add(_imagePanelForecast5Night, null);
    _panelContent.add(_imagePanelForecast4Day, null);
    _panelContent.add(_labelForecast4DayDate, null);
    _panelContent.add(_labelForecast4DayTemp, null);
    _panelContent.add(_labelForecast4NightTemp, null);
    _panelContent.add(_labelForecast4NightDate, null);
    _panelContent.add(_imagePanelForecast4Night, null);
    _panelContent.add(_imagePanelForecast3Day, null);
    _panelContent.add(_labelForecast3DayDate, null);
    _panelContent.add(_labelForecast3DayTemp, null);
    _panelContent.add(_imagePanelForecast3Night, null);
    _panelContent.add(_labelForecast3NightDate, null);
    _panelContent.add(_labelForecast3NightTemp, null);
    _panelContent.add(_imagePanelForecast2Day, null);
    _panelContent.add(_labelForecast2DayDate, null);
    _panelContent.add(_labelForecast2DayTemp, null);
    _panelContent.add(_labelForecast2NightTemp, null);
    _panelContent.add(_labelForecast2NightDate, null);
    _panelContent.add(_imagePanelForecast2Night, null);
    _panelContent.add(_imagePanelForecast1Night, null);
    _panelContent.add(_labelForecast1NightDate, null);
    _panelContent.add(_labelForecast1NightTemp, null);
    _panelContent.add(_labelForecast1DayTemp, null);
    _panelContent.add(_labelForecast1DayDate, null);
    _panelContent.add(_imagePanelForecast1Day, null);
    _labelForecast1DayDate.setText(simpleDateFormat.format(_weatherForecastVOs[0].getDate()));
    _labelForecast1DayTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[0].getScale(), temperatureScale, _weatherForecastVOs[0].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[0].getScale(), temperatureScale, _weatherForecastVOs[0].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[0].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast1Day.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast1NightDate.setText(simpleDateFormat.format(_weatherForecastVOs[1].getDate()));
    _labelForecast1NightTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[1].getScale(), temperatureScale, _weatherForecastVOs[1].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[1].getScale(), temperatureScale, _weatherForecastVOs[1].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[1].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast1Night.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast2DayDate.setText(simpleDateFormat.format(_weatherForecastVOs[2].getDate()));
    _labelForecast2DayTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[2].getScale(), temperatureScale, _weatherForecastVOs[2].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[2].getScale(), temperatureScale, _weatherForecastVOs[2].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[2].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast2Day.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast2NightDate.setText(simpleDateFormat.format(_weatherForecastVOs[3].getDate()));
    _labelForecast2NightTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[3].getScale(), temperatureScale, _weatherForecastVOs[3].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[3].getScale(), temperatureScale, _weatherForecastVOs[3].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[3].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast2Night.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast3DayDate.setText(simpleDateFormat.format(_weatherForecastVOs[4].getDate()));
    _labelForecast3DayTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[4].getScale(), temperatureScale, _weatherForecastVOs[4].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[4].getScale(), temperatureScale, _weatherForecastVOs[4].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[4].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast3Day.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast3NightDate.setText(simpleDateFormat.format(_weatherForecastVOs[5].getDate()));
    _labelForecast3NightTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[5].getScale(), temperatureScale, _weatherForecastVOs[5].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[5].getScale(), temperatureScale, _weatherForecastVOs[5].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[5].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast3Night.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast4DayDate.setText(simpleDateFormat.format(_weatherForecastVOs[6].getDate()));
    _labelForecast4DayTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[6].getScale(), temperatureScale, _weatherForecastVOs[6].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[6].getScale(), temperatureScale, _weatherForecastVOs[6].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[6].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast4Day.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast4NightDate.setText(simpleDateFormat.format(_weatherForecastVOs[7].getDate()));
    _labelForecast4NightTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[7].getScale(), temperatureScale, _weatherForecastVOs[7].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[7].getScale(), temperatureScale, _weatherForecastVOs[7].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[7].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast4Night.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast5DayDate.setText(simpleDateFormat.format(_weatherForecastVOs[8].getDate()));
    _labelForecast5DayTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[8].getScale(), temperatureScale, _weatherForecastVOs[8].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[8].getScale(), temperatureScale, _weatherForecastVOs[8].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[8].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast5Day.setImage(bufferedImage);
    bufferedImage = null;
    _labelForecast5NightDate.setText(simpleDateFormat.format(_weatherForecastVOs[9].getDate()));
    _labelForecast5NightTemp.setText(((int) Converter.convertTemperatures(_weatherForecastVOs[9].getScale(), temperatureScale, _weatherForecastVOs[9].getHighTemperature())) + " / " + ((int) Converter.convertTemperatures(_weatherForecastVOs[9].getScale(), temperatureScale, _weatherForecastVOs[9].getLowTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherForecastVOs[9].getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelForecast5Night.setImage(bufferedImage);
    bufferedImage = null;
  }

  private void _imagePanelForecast1Day_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[0].getDetailsURI());
  }

  private void _imagePanelForecast1Night_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[1].getDetailsURI());
  }

  private void _imagePanelForecast2Day_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[2].getDetailsURI());
  }

  private void _imagePanelForecast2Night_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[3].getDetailsURI());
  }

  private void _imagePanelForecast3Day_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[4].getDetailsURI());
  }

  private void _imagePanelForecast3Night_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[5].getDetailsURI());
  }

  private void _imagePanelForecast4Day_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[6].getDetailsURI());
  }

  private void _imagePanelForecast4Night_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[7].getDetailsURI());
  }

  private void _imagePanelForecast5Day_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[8].getDetailsURI());
  }

  private void _imagePanelForecast5Night_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherForecastVOs[9].getDetailsURI());
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }
}
