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


package net.zypr.gps.gpx;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{

  public ObjectFactory()
  {
  }

  public Gpx.Wpt createGpxWpt()
  {
    return new Gpx.Wpt();
  }

  public BoundsType createBoundsType()
  {
    return new BoundsType();
  }

  public Gpx.Trk.Trkseg.Trkpt createGpxTrkTrksegTrkpt()
  {
    return new Gpx.Trk.Trkseg.Trkpt();
  }

  public Gpx.Rte createGpxRte()
  {
    return new Gpx.Rte();
  }

  public Gpx createGpx()
  {
    return new Gpx();
  }

  public Gpx.Rte.Rtept createGpxRteRtept()
  {
    return new Gpx.Rte.Rtept();
  }

  public Gpx.Trk createGpxTrk()
  {
    return new Gpx.Trk();
  }

  public Gpx.Trk.Trkseg createGpxTrkTrkseg()
  {
    return new Gpx.Trk.Trkseg();
  }
}
