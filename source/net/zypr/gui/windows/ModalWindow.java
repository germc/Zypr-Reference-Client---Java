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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Panel;
import net.zypr.gui.panels.GenericPanel;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.WebBrowser;

import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.MattePainter;

public class ModalWindow
  extends Panel
{
  private final BorderLayout _borderLayoutPanel = new BorderLayout();
  private final Panel _panelTitleBar = new Panel();
  private final BorderLayout _borderLayoutTitleBar = new BorderLayout();
  private final Button _buttonWindowClose = new Button("button-close-window-up.png", "button-close-window-down.png");
  private final JLabel _labelWindowTitle = new JLabel("Window Title");
  private final Button _buttonDataSource = new Button();
  private Timer _timer = null;
  private String _dataSourceURL = null;
  protected Panel _panelContent = new Panel();
  private ModalWindow _discardWhenHide = null;
  private VoiceCommandState _voiceCommandState = VoiceCommandState.MAIN;
  private VoiceCommandContextState[] _voiceCommandContextStates = null;
  private ArrayList<ContextDataVO> _contextData = new ArrayList<ContextDataVO>();
  public static final int JLIST_VISIBLE_ROW_COUNT = 5;

  public ModalWindow()
  {
    super(true);
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
    this.setLayout(_borderLayoutPanel);
    AlphaPainter alphaPainter = new AlphaPainter();
    alphaPainter.setPainters(new MattePainter(new Color(0, 0, 0, 196)));
    setBackgroundPainter(alphaPainter);
    setAlpha(0f);
    setVisible(false);
    setOpaque(false);
    _panelContent.setLayout(null);
    _panelContent.setOpaque(false);
    _panelTitleBar.setSize(34, 34);
    _panelTitleBar.setPreferredSize(new Dimension(34, 34));
    _panelTitleBar.setMinimumSize(new Dimension(34, 34));
    _panelTitleBar.setBackground(Color.BLACK);
    _panelTitleBar.setLayout(_borderLayoutTitleBar);
    _panelTitleBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    _borderLayoutTitleBar.setHgap(4);
    _buttonWindowClose.setMaximumSize(new Dimension(32, 32));
    _buttonWindowClose.setMinimumSize(new Dimension(32, 32));
    _buttonWindowClose.setPreferredSize(new Dimension(32, 32));
    _buttonWindowClose.setSize(new Dimension(32, 32));
    _buttonWindowClose.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonWindowClose_actionPerformed(actionEvent);
      }
    });
    _labelWindowTitle.setForeground(Color.WHITE);
    _labelWindowTitle.setFont(new Font("Dialog", 1, 20));
    _buttonDataSource.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDataSource_actionPerformed(actionEvent);
      }
    });
    _panelTitleBar.add(_buttonWindowClose, BorderLayout.WEST);
    _panelTitleBar.add(_labelWindowTitle, BorderLayout.CENTER);
    _panelTitleBar.add(_buttonDataSource, BorderLayout.EAST);
    _buttonDataSource.setVisible(false);
    _buttonDataSource.setIconTextGap(0);
    this.add(_panelTitleBar, BorderLayout.NORTH);
    this.add(_panelContent, BorderLayout.CENTER);
    addComponentListener(new ComponentAdapter()
    {
      public void componentShown(ComponentEvent componentEvent)
      {
        _this_componentShown(componentEvent);
      }
    });
  }

  public void setParentSize(Dimension parentDimension)
  {
    setLocation((int) (parentDimension.getWidth() - getWidth()) / 2, (int) (parentDimension.getHeight() - getHeight()) / 2);
  }

  public void showWindow()
  {
    setEnabledComponents(true);
    if (_timer != null)
      {
        _timer.stop();
        _timer = null;
      }
    this.setAlpha(0f);
    this.setVisible(true);
    _timer = new Timer(50, new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            float alpha = getAlpha() + 0.15f;
            if (alpha > 1.0f)
              {
                _timer.stop();
                _timer = null;
                setAlpha(1.0f);
                windowVisible();
              }
            else
              {
                setAlpha(alpha);
              }
          }
        });
    _timer.start();
  }

  public void hideWindow()
  {
    hideWindow(false);
  }

  public void hideWindow(boolean discard)
  {
    setEnabledComponents(false);
    if (_timer != null)
      {
        _timer.stop();
        _timer = null;
      }
    this.setAlpha(1.0f);
    if (discard)
      _discardWhenHide = this;
    _timer = new Timer(50, new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            float alpha = getAlpha() - 0.15f;
            if (alpha < 0.0f)
              {
                _timer.stop();
                _timer = null;
                setAlpha(0.0f);
                setVisible(false);
                if (_discardWhenHide != null && getParentPanel() != null)
                  getParentPanel().remove(_discardWhenHide);
                windowInvisible();
              }
            else
              {
                setAlpha(alpha);
              }
          }
        });
    _timer.start();
  }

  protected void windowVisible()
  {
  }

  protected void windowInvisible()
  {
  }

  public void setEnabledComponents(boolean enabled)
  {
    Component[] components = _panelContent.getComponents();
    for (int index = 0; index < components.length; index++)
      if (components[index] instanceof Button)
        components[index].setEnabled(enabled);
    components = _panelTitleBar.getComponents();
    for (int index = 0; index < components.length; index++)
      if (components[index] instanceof Button)
        components[index].setEnabled(enabled);
  }

  public String getDataSourceURL()
  {
    return (_dataSourceURL);
  }

  public void setDataSourceURL(String dataSourceURL)
  {
    _dataSourceURL = dataSourceURL;
  }

  public void setTitle(String title)
  {
    _labelWindowTitle.setText(title);
  }

  public String getTitle()
  {
    return (_labelWindowTitle.getText());
  }

  public void setDataSourceLogo(ImageIcon iconLogo)
  {
    if (iconLogo != null)
      _buttonDataSource.setIconDefault(iconLogo);
    _buttonDataSource.setVisible((iconLogo != null));
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    hideWindow(true);
  }

  protected void _this_componentShown(ComponentEvent componentEvent)
  {
  }

  protected void _buttonDataSource_actionPerformed(ActionEvent actionEvent)
  {
    if (getDataSourceURL() != null)
      WebBrowser.openURL(getDataSourceURL());
  }

  protected void addWindowCloseButtonActionListener(ActionListener actionListener)
  {
    _buttonWindowClose.addActionListener(actionListener);
  }

  protected void setWindowCloseButtonEnabled(boolean enabled)
  {
    _buttonWindowClose.setEnabled(enabled);
  }

  protected GenericPanel getParentPanel()
  {
    try
      {
        return ((GenericPanel) getParent());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        return (null);
      }
  }

  public void setVoiceCommandState(VoiceCommandState voiceCommandState)
  {
    this._voiceCommandState = voiceCommandState;
  }

  public VoiceCommandState getVoiceCommandState()
  {
    return (_voiceCommandState);
  }

  public void setContextData(ArrayList<ContextDataVO> contextData)
  {
    this._contextData = contextData;
  }

  public ArrayList<ContextDataVO> getContextData()
  {
    return (_contextData);
  }

  public void clearContextData()
  {
    _contextData.clear();
  }

  public void addContextData(ContextDataVO contextDataVO)
  {
    _contextData.add(contextDataVO);
  }

  public void setVoiceCommandContextState(VoiceCommandContextState[] voiceCommandContextStates)
  {
    this._voiceCommandContextStates = voiceCommandContextStates;
  }

  public VoiceCommandContextState[] getVoiceCommandContextState()
  {
    return (_voiceCommandContextStates);
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    return (false);
  }
}
