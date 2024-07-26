/*
 * Copyright 2012-2024 Institut National de l'Audiovisuel
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
public class RexDefaultLogger implements RexLogger {

	@Override
	public void logDebug(String m) {
		System.out.println("[DEBUG] " + m);
	}

	@Override
	public void logDebug(String m, Throwable t) {
		logDebug(m);
		logException(t);
	}

	@Override
	public void logError(String m) {
		System.out.println("[ERROR] " + m);
	}

	@Override
	public void logError(String m, Throwable t) {
		logError(m);
		logException(t);
	}

	@Override
	public void logException(Throwable t) {
		t.printStackTrace();
	}

	@Override
	public void logInfo(String m) {
		System.out.println("[INFO ] " + m);
	}

	@Override
	public void logInfo(String m, Throwable t) {
		logInfo(m);
		logException(t);
	}

	@Override
	public void logTrace(String m) {
		System.out.println("[TRACE] " + m);
	}

	@Override
	public void logTrace(String m, Throwable t) {
		logTrace(m);
		logException(t);
	}

	@Override
	public void logWarn(String m) {
		System.out.println("[WARN ] " + m);
	}

	@Override
	public void logWarn(String m, Throwable t) {
		logWarn(m);
		logException(t);
	}

}
