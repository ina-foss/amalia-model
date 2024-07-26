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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class TrackSegment extends TrackRange implements Iterable<TrackPosition> {
	private List<TrackPosition> pos;

	public TrackSegment() {
		super();
		pos = new ArrayList<TrackPosition>();
	}

	public boolean add(TrackPosition e) {
		return pos.add(e);
	}

	public boolean isEmpty() {
		return pos.isEmpty();
	}

	@Override
	public Iterator<TrackPosition> iterator() {
		return pos.iterator();
	}

	public boolean remove(TrackPosition o) {
		return pos.remove(o);
	}

	public int size() {
		return pos.size();
	}

	@Override
	public String toString() {
		return "TrackSegment [getScore()=" + getScore() + ", getTcIn()=" + getTcIn() + ", getTcOut()=" + getTcOut() + "]";
	}

	@Override
	public void wrap() {
		setScore(0);
		Collections.sort(pos);

		for (TrackPosition p : pos) {
			updateRangeAndScore(p.getTc(), p.getScore());
		}
	}

}
