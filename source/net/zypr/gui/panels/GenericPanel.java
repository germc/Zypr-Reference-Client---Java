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

import java.awt.LayoutManager;

import java.util.NoSuchElementException;
import java.util.Stack;

import net.zypr.gui.components.Panel;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.windows.ModalWindow;
import net.zypr.gui.windows.WarningWindow;

public class GenericPanel
  extends Panel
{
  private static Stack<ModalWindow> _stackWindows = new Stack<ModalWindow>();

  public GenericPanel()
  {
    super();
    this.setLayout(null);
    this.setOpaque(false);
  }

  public GenericPanel(boolean isDoubleBuffered)
  {
    super(isDoubleBuffered);
    this.setLayout(null);
    this.setOpaque(false);
  }

  public GenericPanel(LayoutManager layoutManager)
  {
    super(layoutManager);
    this.setOpaque(false);
  }

  public GenericPanel(LayoutManager layoutManager, boolean isDoubleBuffered)
  {
    super(layoutManager, isDoubleBuffered);
    this.setOpaque(false);
  }

  public synchronized void pushWindow(ModalWindow modalWindow)
  {
    _stackWindows.push(modalWindow);
  }

  public synchronized ModalWindow popWindow()
  {
    ModalWindow modalWindow = null;
    if (hasAnyWindows())
      modalWindow = _stackWindows.pop();
    return (modalWindow);
  }

  public synchronized void discardAllWindows()
  {
    ModalWindow modalWindow = popWindow();
    while (modalWindow != null)
      {
        if (modalWindow.isVisible())
          {
            modalWindow.hideWindow(true);
          }
        else
          {
            if (modalWindow.getParent() != null)
              modalWindow.getParent().remove(modalWindow);
          }
        modalWindow = popWindow();
      }
  }

  public synchronized boolean hasAnyWindows()
  {
    return (!_stackWindows.empty());
  }

  public Stack<ModalWindow> getStackWindows()
  {
    return (_stackWindows);
  }

  public synchronized ModalWindow getLastInStack()
  {
    try
      {
        return (_stackWindows.lastElement());
      }
    catch (NoSuchElementException noSuchElementException)
      {
        return (null);
      }
  }

  public synchronized void discardAndShowWindow(ModalWindow modalWindow)
  {
    ModalWindow currentModalWindow = popWindow();
    if (currentModalWindow != null)
      {
        currentModalWindow.setEnabled(false);
        currentModalWindow.hideWindow(true);
      }
    pushWindow(modalWindow);
    add(modalWindow);
    modalWindow.setLocation((int) (getWidth() - modalWindow.getWidth()) / 2, (int) (getHeight() - modalWindow.getHeight()) / 2);
    modalWindow.showWindow();
  }

  public synchronized void showWindow(ModalWindow modalWindow)
  {
    ModalWindow currentModalWindow = getLastInStack();
    if (currentModalWindow != null)
      {
        currentModalWindow.setEnabled(false);
        currentModalWindow.hideWindow(currentModalWindow instanceof WarningWindow);
      }
    pushWindow(modalWindow);
    add(modalWindow);
    modalWindow.setLocation((int) (getWidth() - modalWindow.getWidth()) / 2, (int) (getHeight() - modalWindow.getHeight()) / 2);
    modalWindow.showWindow();
  }

  public synchronized void discardWindow()
  {
    ModalWindow currentModalWindow = popWindow();
    if (currentModalWindow != null)
      {
        currentModalWindow.setEnabled(false);
        currentModalWindow.hideWindow(true);
      }
    ModalWindow modalWindow = getLastInStack();
    if (modalWindow != null)
      {
        modalWindow.setEnabled(true);
        modalWindow.showWindow();
      }
  }

  public MainPanel getMainPanel()
  {
    try
      {
        return ((MainPanel) getParent());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        return (null);
      }
  }
}
