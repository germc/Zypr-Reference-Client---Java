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


package net.zypr.gps.enums;

public enum FixQuality
{
  INVALID(0),
  GPS_FIX_SPS(1),
  DGPS_FIX(2),
  PPS_FIX(3),
  REAL_TIME_KINEMATIC(4),
  FLOAT_RTK(5),
  ESTIMATED_DEAD_RECKONING(6),
  MANUAL_INPUT_MODE(7),
  SIMULATION_MODE(8);
  private int _value;

  FixQuality(int value)
  {
    _value = value;
  }

  public int getValue()
  {
    return (_value);
  }
}
