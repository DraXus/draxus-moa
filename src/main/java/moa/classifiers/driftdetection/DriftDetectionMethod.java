/*
 *    DriftDetectionMethod.java
 *    Copyright (C) 2011 Universidad de Granada, Granada, Spain
 *    @author Manuel Martín (manuelmartin@decsai.ugr.es)
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
package moa.classifiers.driftdetection;

import moa.options.OptionHandler;

//Interface to implements methods of drift detection

public interface DriftDetectionMethod extends OptionHandler {

	public static final int DDM_INCONTROL_LEVEL = 0;

	public static final int DDM_WARNING_LEVEL = 1;

	public static final int DDM_OUTCONTROL_LEVEL = 2;

	public int computeNextVal(boolean prediction);

	public void getDescription(StringBuilder sb, int indent);

}