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
package fr.ina.research.amalia.model;

import java.math.BigDecimal;

import org.apache.commons.codec.binary.Base64;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.jaxb.Clazzref;
import fr.ina.research.amalia.model.jaxb.Data;
import fr.ina.research.amalia.model.jaxb.Histogram;

/**
 * Additional data block.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class DataBlock {
	private Data internal;

	DataBlock(Data toWrap) {
		super();

		this.internal = toWrap;
	}

	public DataBlock addClassReference(String id, BigDecimal score) throws AmaliaException {
		Clazzref ref = new Clazzref();
		ref.setId(id);
		ref.setScore(score);
		internal.getClazzref().add(ref);
		return this;
	}
	
	public DataBlock addHistogram(int[] pos, int[] neg) throws AmaliaException {
		if (pos == null) {
			throw new AmaliaException("Can't create an histogramm with null value array");
		}

		if ((neg != null) && (pos.length != neg.length)) {
			throw new AmaliaException("Pos and neg value array dimensions don't match");
		}

		Histogram h = new Histogram();
		h.setNbbins(pos.length);

		h.setPosbins(renderData(pos));
		h.setPosmax(getMaxValue(pos));

		if (neg != null) {
			h.setNegbins(renderData(neg));
			h.setNegmax(getMaxValue(neg));
		} else {
			h.setNegbins("");
			h.setNegmax(0);
		}

		internal.getHistogram().add(h);
		
		return this;
	}

	private String base64Encode(int[] data) {
		byte[] bData = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			bData[i] = (byte) data[i];
		}
		byte[] encoded = Base64.encodeBase64(bData);
		return new String(encoded);
	}

	public int getMaxValue(int[] d) {
		int mx = -1;
		for (int v : d) {
			if (v > mx) {
				mx = v;
			}
		}
		return mx;
	}

	public String renderData(int[] d) {
		return base64Encode(d);
	}
}
