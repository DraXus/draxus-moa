/*
 *    WeightedMajorityAlgorithm.java
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

import moa.core.DoubleVector;
import moa.core.Measurement;
import moa.core.ObjectRepository;
import moa.options.ClassOption;
import moa.options.FlagOption;
import moa.options.FloatOption;
import moa.options.ListOption;
import moa.options.Option;
import moa.tasks.TaskMonitor;
import weka.core.Instance;
import weka.core.Utils;

/**
 * Weighted majority algorithm  for data streams.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class WeightedMajorityAlgorithm extends AbstractClassifier {

    private static final long serialVersionUID = 1L;

    public ListOption learnerListOption = new ListOption(
            "learners",
            'l',
            "The learners to combine.",
            new ClassOption("learner", ' ', "", Classifier.class,
            "HoeffdingTree"),
            new Option[]{
                new ClassOption("", ' ', "", Classifier.class,
                "HoeffdingTree"),
                new ClassOption("", ' ', "", Classifier.class,
                "HoeffdingTreeNB"),
                new ClassOption("", ' ', "", Classifier.class,
                "HoeffdingTreeNBAdaptive"),
                new ClassOption("", ' ', "", Classifier.class, "NaiveBayes")},
            ',');

    public FloatOption betaOption = new FloatOption("beta", 'b',
            "Factor to punish mistakes by.", 0.9, 0.0, 1.0);

    public FloatOption gammaOption = new FloatOption("gamma", 'g',
            "Minimum fraction of weight per model.", 0.01, 0.0, 0.5);

    public FlagOption pruneOption = new FlagOption("prune", 'p',
            "Prune poorly performing models from ensemble.");

    protected Classifier[] ensemble;

    protected double[] ensembleWeights;

    @Override
    public void prepareForUseImpl(TaskMonitor monitor,
            ObjectRepository repository) {
        Option[] learnerOptions = this.learnerListOption.getList();
        this.ensemble = new Classifier[learnerOptions.length];
        for (int i = 0; i < learnerOptions.length; i++) {
            monitor.setCurrentActivity("Materializing learner " + (i + 1)
                    + "...", -1.0);
            this.ensemble[i] = (Classifier) ((ClassOption) learnerOptions[i]).materializeObject(monitor, repository);
            if (monitor.taskShouldAbort()) {
                return;
            }
            monitor.setCurrentActivity("Preparing learner " + (i + 1) + "...",
                    -1.0);
            this.ensemble[i].prepareForUse(monitor, repository);
            if (monitor.taskShouldAbort()) {
                return;
            }
        }
        super.prepareForUseImpl(monitor, repository);
    }

    @Override
    public void resetLearningImpl() {
        this.ensembleWeights = new double[this.ensemble.length];
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i].resetLearning();
            this.ensembleWeights[i] = 1.0;
        }
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        double totalWeight = 0.0;
        for (int i = 0; i < this.ensemble.length; i++) {
            boolean prune = false;
            if (!this.ensemble[i].correctlyClassifies(inst)) {
                if (this.ensembleWeights[i] > this.gammaOption.getValue()
                        / this.ensembleWeights.length) {
                    this.ensembleWeights[i] *= this.betaOption.getValue()
                            * inst.weight();
                } else if (this.pruneOption.isSet()) {
                    prune = true;
                    discardModel(i);
                    i--;
                }
            }
            if (!prune) {
                totalWeight += this.ensembleWeights[i];
                this.ensemble[i].trainOnInstance(inst);
            }
        }
        // normalize weights
        for (int i = 0; i < this.ensembleWeights.length; i++) {
            this.ensembleWeights[i] /= totalWeight;
        }
    }

    public double[] getVotesForInstance(Instance inst) {
        DoubleVector combinedVote = new DoubleVector();
        if (this.trainingWeightSeenByModel > 0.0) {
            for (int i = 0; i < this.ensemble.length; i++) {
                if (this.ensembleWeights[i] > 0.0) {
                    DoubleVector vote = new DoubleVector(this.ensemble[i].getVotesForInstance(inst));
                    if (vote.sumOfValues() > 0.0) {
                        vote.normalize();
                        vote.scaleValues(this.ensembleWeights[i]);
                        combinedVote.addValues(vote);
                    }
                }
            }
        }
        return combinedVote.getArrayRef();
    }

    @Override
    public void getModelDescription(StringBuilder out, int indent) {
        // TODO Auto-generated method stub
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        Measurement[] measurements = null;
        if (this.ensembleWeights != null) {
            measurements = new Measurement[this.ensembleWeights.length];
            for (int i = 0; i < this.ensembleWeights.length; i++) {
                measurements[i] = new Measurement("member weight " + (i + 1),
                        this.ensembleWeights[i]);
            }
        }
        return measurements;
    }

    @Override
    public boolean isRandomizable() {
        return false;
    }

    @Override
    public Classifier[] getSubClassifiers() {
        return this.ensemble.clone();
    }

    public void discardModel(int index) {
        Classifier[] newEnsemble = new Classifier[this.ensemble.length - 1];
        double[] newEnsembleWeights = new double[newEnsemble.length];
        int oldPos = 0;
        for (int i = 0; i < newEnsemble.length; i++) {
            if (oldPos == index) {
                oldPos++;
            }
            newEnsemble[i] = this.ensemble[oldPos];
            newEnsembleWeights[i] = this.ensembleWeights[oldPos];
            oldPos++;
        }
        this.ensemble = newEnsemble;
        this.ensembleWeights = newEnsembleWeights;
    }

    protected int removePoorestModelBytes() {
        int poorestIndex = Utils.minIndex(this.ensembleWeights);
        int byteSize = this.ensemble[poorestIndex].measureByteSize();
        discardModel(poorestIndex);
        return byteSize;
    }
}
