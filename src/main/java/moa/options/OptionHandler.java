/*
 *    OptionHandler.java
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
package moa.options;

import moa.MOAObject;
import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

/**
 * Interface representing an object that handles options or parameters. 
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $ 
 */
public interface OptionHandler extends MOAObject {

    /**
     * Gets the purpose of this object
     *
     * @return the string with the purpose of this object
     */
    public String getPurposeString();

    /**
     * Gets the options of this object
     *
     * @return the options of this object
     */
    public Options getOptions();

    /**
     * This method prepares this object for use.
     *
     */
    public void prepareForUse();

    /**
     * This method prepares this object for use.
     *
     * @param monitor the TaskMonitor to use
     * @param repository  the ObjectRepository to use
     */
    public void prepareForUse(TaskMonitor monitor, ObjectRepository repository);

    /**
     * This method produces a copy of this object.
     *
     * @return a copy of this object
     */
    public OptionHandler copy();

    /**
     * Gets the Command Line Interface text to create the object
     *
     * @return the Command Line Interface text to create the object
     */
    public String getCLICreationString(Class<?> expectedType);
}
