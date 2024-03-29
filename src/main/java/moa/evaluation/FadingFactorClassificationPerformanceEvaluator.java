/*
 *    FadingFactorClassificationPerformanceEvaluator.java
 *    Copyright (C) 2007 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
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
package moa.evaluation;

import moa.core.Measurement;
import moa.core.ObjectRepository;
import moa.options.FloatOption;
import moa.options.AbstractOptionHandler;
import moa.tasks.TaskMonitor;

import weka.core.Utils;
import weka.core.Instance;

/**
 * Classification evaluator that updates evaluation results using a fading factor.
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class FadingFactorClassificationPerformanceEvaluator extends AbstractOptionHandler
        implements ClassificationPerformanceEvaluator {

    private static final long serialVersionUID = 1L;

    protected double TotalweightObserved;

    public FloatOption alphaOption = new FloatOption("alpha",
            'a', "Fading factor or exponential smoothing factor", .01);

    protected Estimator weightCorrect;

    protected class Estimator {

        protected double alpha;

        protected double estimation;

        protected double b;

        public Estimator(double a) {
            alpha = a;
            estimation = 0.0;
            b = 0.0;
        }

        public void add(double value) {
            estimation = alpha * estimation + value;
            b = alpha * b + 1.0;
        }

        public double estimation() {
            return b > 0.0 ? estimation / b : 0;
        }
    }

    /*   public void setalpha(double a) {
    this.alpha = a;
    reset();
    }*/

    @Override
    public void reset() {
        weightCorrect = new Estimator(this.alphaOption.getValue());
    }

    /*public void addClassificationAttempt(int trueClass, double[] classVotes,
    double weight) {
    if (weight > 0.0) {
    this.TotalweightObserved += weight;
    if (Utils.maxIndex(classVotes) == trueClass) {
    this.weightCorrect.add(1);
    } else
    this.weightCorrect.add(0);
    }
    }*/
    @Override
    public void addResult(Instance inst, double[] classVotes) {
        double weight = inst.weight();
        int trueClass = (int) inst.classValue();
        if (weight > 0.0) {
            this.TotalweightObserved += weight;
            if (Utils.maxIndex(classVotes) == trueClass) {
                this.weightCorrect.add(1);
            } else {
                this.weightCorrect.add(0);
            }
        }
    }

    @Override
    public Measurement[] getPerformanceMeasurements() {
        return new Measurement[]{
                    new Measurement("classified instances",
                    this.TotalweightObserved),
                    new Measurement("classifications correct (percent)",
                    getFractionCorrectlyClassified() * 100.0)};
    }

    public double getTotalWeightObserved() {
        return this.TotalweightObserved;
    }

    public double getFractionCorrectlyClassified() {
        return this.weightCorrect.estimation();
    }

    public double getFractionIncorrectlyClassified() {
        return 1.0 - getFractionCorrectlyClassified();
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
        Measurement.getMeasurementsDescription(getPerformanceMeasurements(),
                sb, indent);
    }

    @Override
    public void prepareForUseImpl(TaskMonitor monitor,
            ObjectRepository repository) {
        reset();
    }
}
