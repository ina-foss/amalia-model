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

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock.MetadataType;
import fr.ina.research.rex.commons.tc.RexTimeCode;

public class TestMetadataFactory {

	public static void main(String[] args) {
		try {
			MetadataBlock metadata = MetadataFactory.createMetadataBlock("test-123456", MetadataType.DETECTION, RexTimeCode.build(0, 1, 0, 0));
			metadata.setAlgorithm(TestMetadataFactory.class.getSimpleName());
			metadata.setProcessor("Ina Research Department - N. HERVE");
			metadata.addToRootLocalisationBlock(RexTimeCode.build(0, 0, 30, 0)).setLabel("A label at 30 sec");
			
			System.out.println("<!-- XML serialization -->");
			System.out.println(MetadataFactory.serializeToXMLString(metadata));
			System.out.println();
			System.out.println("<!-- Json serialization -->");
			String asJson = MetadataFactory.serializeToJsonString(metadata);
			System.out.println(asJson);
			
			System.out.println("<!-- Json deserialization -->");
			MetadataBlock metadata2 = MetadataFactory.deserializeFromJsonString(asJson);
			System.out.println(metadata2);
			
		} catch (AmaliaException e) {
			e.printStackTrace();
		}

	}

}
