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


package net.zypr.gps;

import java.util.Iterator;
import java.util.List;

import net.zypr.api.utils.Debug;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.gps.gpx.Gpx;
import net.zypr.gui.Configuration;
import net.zypr.gui.panels.MapViewPanel;

public class GPXThread
  extends Thread
{
  private static final long SLEEP_MULTIPLIER = Configuration.getInstance().getIntegerProperty("gps-file-speed-multiplier", 1000);
  private GPSData _gpsData;
  private List<Gpx.Trk.Trkseg.Trkpt> _gpxData = null;
  private boolean _running = true;
  private long _previousTime = 0;

  public GPXThread(GPSData gpsData, List<Gpx.Trk.Trkseg.Trkpt> gpxData)
  {
    super();
    _gpsData = gpsData;
    _gpxData = gpxData;
  }

  @Override
  public void run()
  {
    super.run();
    Iterator<Gpx.Trk.Trkseg.Trkpt> iterator = _gpxData.iterator();
    while (iterator.hasNext())
      {
        Gpx.Trk.Trkseg.Trkpt trkpt = iterator.next();
        _gpsData.setPosition(new GeoPositionVO(trkpt.getLat().doubleValue(), trkpt.getLon().doubleValue()));
        if (_previousTime == 0)
          _previousTime = trkpt.getTime().toGregorianCalendar().getTimeInMillis();
        double timeDifference = (trkpt.getTime().toGregorianCalendar().getTimeInMillis() - _previousTime) / 1000.0;
        _previousTime = trkpt.getTime().toGregorianCalendar().getTimeInMillis();
        try
          {
            sleep((long) (timeDifference * SLEEP_MULTIPLIER));
          }
        catch (InterruptedException interruptedException)
          {
            Debug.displayStack(this, interruptedException);
          }
        while (MapViewPanel.gpsPaused)
          try
            {
              sleep(500);
            }
          catch (InterruptedException interruptedException)
            {
              Debug.displayStack(this, interruptedException);
            }
      }
    _running = false;
  }

  public void setRunning(boolean running)
  {
    this._running = running;
  }

  public boolean isRunning()
  {
    return (_running);
  }
}
