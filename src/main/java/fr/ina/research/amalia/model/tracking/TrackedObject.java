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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ina.research.amalia.model.jaxb.ShapeType;

/**
 *
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class TrackedObject extends TrackRange implements Iterable<TrackSegment> {
	private List<TrackSegment> seg;
	private ShapeType shapeType;

	public TrackedObject() {
		super();
		seg = new ArrayList<TrackSegment>();
		shapeType = ShapeType.RECTANGLE;
	}

	public boolean add(TrackSegment e) {
		return seg.add(e);
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public boolean isEmpty() {
		return seg.isEmpty();
	}

	@Override
	public Iterator<TrackSegment> iterator() {
		return seg.iterator();
	}

	public boolean remove(TrackSegment o) {
		return seg.remove(o);
	}

	public void setShapeType(ShapeType shapeType) {
		this.shapeType = shapeType;
	}

	public int size() {
		return seg.size();
	}

	@Override
	public void wrap() {
		setScore(0);
		for (TrackSegment s : seg) {
			s.wrap();
			updateRangeAndScore(s.getTcIn(), s.getTcOut(), s.getScore());
		}
	}

}
