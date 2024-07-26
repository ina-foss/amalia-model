/*
 * Copyright (c) 2015-2024 Institut National de l'Audiovisuel, INA
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Redistributions of source code and compiled versions
 * must retain the above copyright notice, this list of conditions and
 * the following disclaimer.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this file. If not, see <http://www.gnu.org/licenses/>
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 */
package fr.ina.research.amalia.model.tracking;

import fr.ina.research.rex.commons.tc.RexTimeCode;

/**
 *
 * @author Nicolas HERVE - nherve@ina.fr
 */
public abstract class TrackRange {
	private RexTimeCode tcIn;
	private RexTimeCode tcOut;
	private double score;

	public TrackRange() {
		super();

		tcIn = null;
		tcOut = null;
		score = 0;
	}

	public double getLength() {
		return tcOut.getSecond() - tcIn.getSecond();
	}

	public double getScore() {
		return score;
	}

	public RexTimeCode getTcIn() {
		return tcIn;
	}

	public RexTimeCode getTcOut() {
		return tcOut;
	}

	public void setScore(double score) {
		this.score = score;
	}

	protected void updateRangeAndScore(RexTimeCode tc, double score) {
		updateRangeAndScore(tc, tc, score);
	}

	protected void updateRangeAndScore(RexTimeCode ti, RexTimeCode to, double score) {
		if (tcIn == null) {
			tcIn = new RexTimeCode(ti.getSecond());
		} else if (ti.getSecond() < tcIn.getSecond()) {
			tcIn.setSecond(ti.getSecond());
		}

		if (tcOut == null) {
			tcOut = new RexTimeCode(to.getSecond());
		} else if (to.getSecond() > tcOut.getSecond()) {
			tcOut.setSecond(to.getSecond());
		}

		this.score = Math.max(score, this.score);
	}

	public abstract void wrap();
}
