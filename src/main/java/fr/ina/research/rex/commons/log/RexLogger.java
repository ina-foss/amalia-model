/*
 * Copyright 2012-2015 Institut National de l'Audiovisuel
 * 
 * This file is part of Rex.
 * 
 * Rex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Rex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Rex. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.ina.research.rex.commons.log;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public interface RexLogger {
	void logDebug(String m);

	void logDebug(String m, Throwable t);

	void logError(String m);

	void logError(String m, Throwable t);

	void logException(Throwable t);

	void logInfo(String m);

	void logInfo(String m, Throwable t);

	void logTrace(String m);

	void logTrace(String m, Throwable t);

	void logWarn(String m);

	void logWarn(String m, Throwable t);
}
