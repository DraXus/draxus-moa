/*
 *    HoeffdingOptionTreeNB.java
 *    Copyright (C) 2007 University of Waikato, Hamilton, New Zealand
 *    @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
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
package moa.classifiers;

import moa.options.IntOption;
import weka.core.Instance;

/**
 * Hoeffding Option Tree with naive Bayes learners at the leaves.
 *
 * <p>Parameters:</p> <ul>
 * <li>Same parameters as <code>HoeffdingOptionTree<code></li>
 * <li> -q : The number of instances a leaf should observe before
 * permitting Naive Bayes</li> </ul>
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class HoeffdingOptionTreeNB extends HoeffdingOptionTree {

    private static final long serialVersionUID = 1L;

    public IntOption nbThresholdOption = new IntOption(
            "nbThreshold",
            'q',
            "The number of instances a leaf should observe before permitting Naive Bayes.",
            0, 0, Integer.MAX_VALUE);

    public static class LearningNodeNB extends ActiveLearningNode {

        private static final long serialVersionUID = 1L;

        public LearningNodeNB(double[] initialClassObservations) {
            super(initialClassObservations);
        }

        @Override
        public double[] getClassVotes(Instance inst, HoeffdingOptionTree hot) {
            if (getWeightSeen() >= ((HoeffdingOptionTreeNB) hot).nbThresholdOption.getValue()) {
                return NaiveBayes.doNaiveBayesPrediction(inst,
                        this.observedClassDistribution,
                        this.attributeObservers);
            }
            return super.getClassVotes(inst, hot);
        }

        @Override
        public void disableAttribute(int attIndex) {
            // should not disable poor atts - they are used in NB calc
        }
    }

    public HoeffdingOptionTreeNB() {
        this.removePoorAttsOption = null;
    }

    @Override
    protected LearningNode newLearningNode(double[] initialClassObservations) {
        return new LearningNodeNB(initialClassObservations);
    }
}
