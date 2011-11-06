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


package net.zypr.gui;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.zypr.api.Session;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.FileUtils;

public class Settings
{
  private static final Settings INSTANCE = new Settings();
  private static SettingsDTO _dto = null;

  public Settings()
  {
    super();
  }

  public synchronized static Settings getInstance()
  {
    return (INSTANCE);
  }

  public synchronized SettingsDTO getDTO()
  {
    if (_dto == null)
      load();
    return (_dto);
  }

  public synchronized void load()
  {
    File file = new File(FileUtils.ZYPR_PATH, File.separator + "settings" + File.separator);
    XMLDecoder xmlDecoder = null;
    try
      {
        xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(new File(file, "" + Session.getInstance().getUsername().hashCode()))));
        _dto = (SettingsDTO) xmlDecoder.readObject();
        Debug.print("CONFIGURATION LOADED FROM " + (new File(file, "" + Session.getInstance().getUsername().hashCode())));
        if (_dto == null)
          _dto = new SettingsDTO();
      }
    catch (Exception exception)
      {
        _dto = new SettingsDTO();
        Debug.displayStack(this, exception, 1);
      }
    finally
      {
        if (xmlDecoder != null)
          xmlDecoder.close();
        xmlDecoder = null;
      }
  }

  public synchronized void save()
  {
    File file = new File(FileUtils.ZYPR_PATH, File.separator + "settings" + File.separator);
    file.mkdirs();
    XMLEncoder xmlEncoder = null;
    try
      {
        xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(new File(file, "" + Session.getInstance().getUsername().hashCode()))));
        xmlEncoder.writeObject(_dto);
        Debug.print("SETTINGS SAVED INTO " + (new File(file, "" + Session.getInstance().getUsername().hashCode())));
      }
    catch (Exception exception)
      {
        Debug.displayStack(null, exception, 1);
      }
    finally
      {
        if (xmlEncoder != null)
          xmlEncoder.close();
        xmlEncoder = null;
      }
  }

  public synchronized void clear()
  {
    _dto = null;
  }
}
