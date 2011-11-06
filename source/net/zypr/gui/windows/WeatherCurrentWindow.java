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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.text.SimpleDateFormat;

import javax.swing.SwingConstants;

import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.vo.WeatherCurrentVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Settings;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.utils.Converter;
import net.zypr.gui.utils.WebBrowser;

public class WeatherCurrentWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelCurrent = new ImagePanel();
  private Label _labelCurrentDate = new Label();
  private Label _labelCurrentDescription = new Label();
  private Label _labelCurrentTemp = new Label();
  private WeatherCurrentVO _weatherCurrentVO = null;
  private String _placename = null;

  public WeatherCurrentWindow(WeatherCurrentVO weatherCurrentVO, String placename)
  {
    try
      {
        _weatherCurrentVO = weatherCurrentVO;
        _placename = placename;
        jbInit();
      }
    catch (Exception e)
      {
        e.printStackTrace();
      }
  }

  private void jbInit()
    throws Exception
  {
    this.setSize(new Dimension(560, 293));
    setTitle("Current Weather Condition");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Settings.getInstance().getDTO().getDateFormatPattern().getPattern() + " " + Settings.getInstance().getDTO().getTimeFormatPattern().getPattern());
    TemperatureScale temperatureScale = Settings.getInstance().getDTO().getTemperatureScale();
    BufferedImage bufferedImage = null;
    _imagePanelCurrent.setBounds(new Rectangle(20, 10, 240, 240));
    _imagePanelCurrent.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent mouseEvent)
      {
        _imagePanelCurrent_mouseClicked(mouseEvent);
      }
    });
    _labelCurrentDate.setBounds(new Rectangle(290, 25, 240, 20));
    _labelCurrentDate.setHorizontalAlignment(SwingConstants.CENTER);
    _labelCurrentDescription.setBounds(new Rectangle(270, 155, 275, 100));
    _labelCurrentDescription.setHorizontalAlignment(SwingConstants.LEFT);
    _labelCurrentDescription.setVerticalTextPosition(SwingConstants.TOP);
    _labelCurrentDescription.setVerticalAlignment(SwingConstants.TOP);
    _labelCurrentDescription.setHorizontalTextPosition(SwingConstants.LEFT);
    _labelCurrentTemp.setBounds(new Rectangle(335, 60, 140, 95));
    _labelCurrentTemp.setFont(new Font("Dialog", 0, 24));
    _labelCurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
    _panelContent.add(_labelCurrentTemp, null);
    _panelContent.add(_labelCurrentDescription, null);
    _panelContent.add(_labelCurrentDate, null);
    _panelContent.add(_imagePanelCurrent, null);
    _labelCurrentDescription.setText("<html>" + _weatherCurrentVO.getDescriptionLong() + "</html>");
    _labelCurrentDate.setText(simpleDateFormat.format(_weatherCurrentVO.getDate()));
    _labelCurrentTemp.setText(((int) Converter.convertTemperatures(_weatherCurrentVO.getScale(), temperatureScale, _weatherCurrentVO.getCurrentTemperature())) + " " + temperatureScale.getSymbol());
    while (bufferedImage == null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_weatherCurrentVO.getIconURI(), 80, 80, null);
        Thread.sleep(500);
      }
    _imagePanelCurrent.setImage(bufferedImage);
  }

  private void _imagePanelCurrent_mouseClicked(MouseEvent mouseEvent)
  {
    WebBrowser.openURL(_weatherCurrentVO.getDetailsURI());
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }
}
