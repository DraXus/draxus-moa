/*
 *    IntOptionEditComponent.java
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
package moa.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import moa.options.IntOption;
import moa.options.Option;

/**
 * An OptionEditComponent that lets the user edit an integer option.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class IntOptionEditComponent extends JPanel implements
        OptionEditComponent {

    private static final long serialVersionUID = 1L;

    protected IntOption editedOption;

    protected JSpinner spinner;

    protected JSlider slider;

    public IntOptionEditComponent(IntOption option) {
        this.editedOption = option;
        int minVal = option.getMinValue();
        int maxVal = option.getMaxValue();
        setLayout(new GridLayout(1, 0));
        this.spinner = new JSpinner(new SpinnerNumberModel(option.getValue(),
                minVal, maxVal, 1));
        add(this.spinner);
        if ((minVal > Integer.MIN_VALUE) && (maxVal < Integer.MAX_VALUE)) {
            this.slider = new JSlider(minVal, maxVal, option.getValue());
            add(this.slider);
            this.slider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    IntOptionEditComponent.this.spinner.setValue(IntOptionEditComponent.this.slider.getValue());
                }
            });
            this.spinner.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    IntOptionEditComponent.this.slider.setValue(((Integer) IntOptionEditComponent.this.spinner.getValue()).intValue());
                }
            });
        }
    }

    @Override
    public void applyState() {
        this.editedOption.setValue(((Integer) this.spinner.getValue()).intValue());
    }

    @Override
    public Option getEditedOption() {
        return this.editedOption;
    }

    @Override
    public void setEditState(String cliString) {
        this.spinner.setValue(IntOption.cliStringToInt(cliString));
    }
}
