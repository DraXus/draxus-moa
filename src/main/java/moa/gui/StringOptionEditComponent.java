/*
 *    StringOptionEditComponent.java
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

import javax.swing.JTextField;

import moa.options.Option;

/**
 * An OptionEditComponent that lets the user edit a string option.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class StringOptionEditComponent extends JTextField implements
        OptionEditComponent {

    private static final long serialVersionUID = 1L;

    protected Option editedOption;

    public StringOptionEditComponent(Option option) {
        this.editedOption = option;
        setEditState(this.editedOption.getValueAsCLIString());
    }

    @Override
    public Option getEditedOption() {
        return this.editedOption;
    }

    @Override
    public void setEditState(String cliString) {
        setText(cliString);
    }

    @Override
    public void applyState() {
        this.editedOption.setValueViaCLIString(getText().length() > 0 ? getText() : null);
    }
}
