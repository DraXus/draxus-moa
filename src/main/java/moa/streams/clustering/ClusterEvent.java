/*
 *    ClusterEvent.java
 *    Copyright (C) 2010 RWTH Aachen University, Germany
 *    @author Jansen (moa@cs.rwth-aachen.de)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package moa.streams.clustering;

import java.util.EventObject;

public class ClusterEvent extends EventObject {

  private String type;
  private String message;
  private long timestamp;

  public ClusterEvent(Object source, long timestamp, String type, String message) {
    super(source);
    this.type = type;
    this.message = message;
    this.timestamp = timestamp;
  }

  public String getMessage(){
      return message;
  }

  public long getTimestamp(){
      return timestamp;
  }

  public String getType(){
      return type;
  }
}
