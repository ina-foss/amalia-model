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
package fr.ina.research.amalia.model.text;

import java.util.ArrayList;
import java.util.List;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.LocalisationBlock;
import fr.ina.research.amalia.model.MetadataFactory;
import fr.ina.research.rex.commons.tc.RexTimeCode;

/**
 * A single text sample from a SubRip file.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class SingleSubTitle {
	private int id;
	private RexTimeCode tcin;
	private RexTimeCode tcout;
	private List<String> texts;

	public SingleSubTitle() {
		super();
		texts = new ArrayList<String>();
	}

	public boolean addText(String e) {
		return texts.add(e);
	}

	public int getId() {
		return id;
	}

	public RexTimeCode getTcin() {
		return tcin;
	}

	public RexTimeCode getTcout() {
		return tcout;
	}

	public List<String> getTexts() {
		return texts;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTcin(RexTimeCode tcin) {
		this.tcin = tcin;
	}

	public void setTcout(RexTimeCode tcout) {
		this.tcout = tcout;
	}

	public void setTexts(List<String> texts) {
		this.texts = texts;
	}

	public LocalisationBlock convertToLocalisation() throws AmaliaException {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : texts) {
			if (first) {
				first = false;
			} else {
				sb.append("<br/>");
			}
			sb.append(s.trim());
		}
		return MetadataFactory.createSynchronizedTextLocalisationBlock(tcin, tcout, sb.toString());
	}
}
