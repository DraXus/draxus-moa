/*
 *    FakeMethod.java
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

import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;

// Fake method to force drift detection

public class FakeMethod extends AbstractOptionHandler implements DriftDetectionMethod {

	private static final long serialVersionUID = 7126286583443853212L;

	public IntOption warningDriftOption = new IntOption("warningDrift", 'w',
            "Number of instances to force a fake warning drift.", 900, 1,
            Integer.MAX_VALUE);
	
	public IntOption fakeDriftOption = new IntOption("fakeDrift", 'f',
            "Number of instances to force a fake concept drift.", 1000, 1,
            Integer.MAX_VALUE);

	private int num_instances;

	public FakeMethod() {
		initialize();
	}

	private void initialize() {
		num_instances = 0;
	}

	@Override
	public int computeNextVal(boolean prediction) {

		num_instances++;
		
		if (num_instances>=fakeDriftOption.getValue()){
			initialize();
			return DDM_OUTCONTROL_LEVEL;
		}
		
		if (num_instances>=warningDriftOption.getValue()){
			return DDM_WARNING_LEVEL;
		}

		return DDM_INCONTROL_LEVEL;
	}

	@Override
	public void getDescription(StringBuilder sb, int indent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prepareForUseImpl(TaskMonitor monitor,
			ObjectRepository repository) {
		// TODO Auto-generated method stub
		
	}

}