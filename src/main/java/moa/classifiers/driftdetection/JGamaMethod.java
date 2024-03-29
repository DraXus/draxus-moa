/*
 *    JGamaMethod.java
 *    Copyright (C) 2008 University of Waikato, Hamilton, New Zealand
 *    @author Manuel Baena (mbaena@lcc.uma.es)
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
import moa.tasks.TaskMonitor;

public class JGamaMethod extends AbstractOptionHandler implements DriftDetectionMethod {

	private static final long serialVersionUID = -3518369648142099719L;

	private static final int JGAMAMETHOD_MINNUMINST = 30;

	private int m_n;

	private double m_p;

	private double m_s;

	private double m_psmin;

	private double m_pmin;

	private double m_smin;

	public JGamaMethod() {
		initialize();
	}

	private void initialize() {
		m_n = 1;
		m_p = 1;
		m_s = 0;
		m_psmin = Double.MAX_VALUE;
		m_pmin = Double.MAX_VALUE;
		m_smin = Double.MAX_VALUE;
	}

	@Override
	public int computeNextVal(boolean prediction) {
		if (prediction == false) {
			m_p = m_p + (1.0 - m_p) / (double) m_n;
		} else {
			m_p = m_p - (m_p) / (double) m_n;
		}
		m_s = Math.sqrt(m_p * (1 - m_p) / (double) m_n);

		m_n++;

		// System.out.print(prediction + " " + m_n + " " + (m_p+m_s) + " ");

		if (m_n < JGAMAMETHOD_MINNUMINST) {
			return DDM_INCONTROL_LEVEL;
		}

		if (m_p + m_s <= m_psmin) {
			m_pmin = m_p;
			m_smin = m_s;
			m_psmin = m_p + m_s;
		}
		
		

		if (m_n > JGAMAMETHOD_MINNUMINST && m_p + m_s > m_pmin + 3 * m_smin) {
			System.out.println(m_p + ",D");
			initialize();
			return DDM_OUTCONTROL_LEVEL;
		} else if (m_p + m_s > m_pmin + 2 * m_smin) {
			System.out.println(m_p + ",W");
			return DDM_WARNING_LEVEL;
		} else {
			System.out.println(m_p + ",N");
			return DDM_INCONTROL_LEVEL;
		}
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