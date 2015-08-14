/*
 * Copyright (c) 2015 Institut National de l'Audiovisuel, INA
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
public class TrackPosition implements Comparable<TrackPosition> {
	private double xc, yc, hw, hh, o;
	private double score;
	private RexTimeCode tc;

	public TrackPosition() {
		super();
	}

	@Override
	public int compareTo(TrackPosition o) {
		return tc.compareTo(o.tc);
	}

	public double getHh() {
		return hh;
	}

	public double getHw() {
		return hw;
	}

	public double getO() {
		return o;
	}

	public double getScore() {
		return score;
	}

	public double getSurface() {
		return 4 * hw * hh;
	}

	public RexTimeCode getTc() {
		return tc;
	}

	public double getXc() {
		return xc;
	}

	public double getYc() {
		return yc;
	}

	public void setHh(double hh) {
		this.hh = hh;
	}

	public void setHw(double hw) {
		this.hw = hw;
	}

	public void setO(double o) {
		this.o = o;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void setTc(RexTimeCode tc) {
		this.tc = tc;
	}

	public void setXc(double xc) {
		this.xc = xc;
	}

	public void setYc(double yc) {
		this.yc = yc;
	}
}
