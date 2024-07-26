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
package fr.ina.research.amalia.model;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock.MetadataType;
import fr.ina.research.rex.commons.tc.RexTimeCode;

/**
 * Tutorial : creation of an audio segmentation metadata block.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class TestSegmentation {

	public static void main(String[] args) {
		try {
			RexTimeCode previous = new RexTimeCode(0);
			RexTimeCode duration = RexTimeCode.build(0, 10, 0, 0);
			
			MetadataBlock metadata = MetadataFactory.createMetadataBlock("test-audio-seg", MetadataType.AUDIO_SEGMENTATION, duration);
			metadata.setAlgorithm(TestSegmentation.class.getSimpleName());
			metadata.setProcessor("Ina Research Department - N. HERVE");
			
			LocalisationBlock speechBlock = metadata.addToRootLocalisationBlock(previous, duration).setLabel("Speech");
			LocalisationBlock musicBlock = metadata.addToRootLocalisationBlock(previous, duration).setLabel("Music");
			
			RexTimeCode current = RexTimeCode.build(0, 1, 0, 0);
			musicBlock.addLocalisationBlock(previous, current).setLabel("TV show introduction");
			previous = new RexTimeCode(current.getSecond());
			
			current.add(120);
			speechBlock.addLocalisationBlock(previous, current).setLabel("Main speaker introduces the show");
			previous = new RexTimeCode(current.getSecond());
			
			current.add(60);
			speechBlock.addLocalisationBlock(previous, current).setLabel("Singer is presenting its new song");
			previous = new RexTimeCode(current.getSecond());
			
			current.add(180);
			musicBlock.addLocalisationBlock(previous, current).setLabel("Singer is singing");
			previous = new RexTimeCode(current.getSecond());
			
			current.add(60);
			speechBlock.addLocalisationBlock(previous, current).setLabel("Singer is presenting its future work");
			previous = new RexTimeCode(current.getSecond());
			
			current.add(60);
			speechBlock.addLocalisationBlock(previous, current).setLabel("Main speaker is closing the show");
			previous = new RexTimeCode(current.getSecond());
			
			current.add(60);
			musicBlock.addLocalisationBlock(previous, current).setLabel("TV show credits");
			previous = new RexTimeCode(current.getSecond());
			
			System.out.println("<!-- XML serialization -->");
			System.out.println(MetadataFactory.serializeToXMLString(metadata));
			System.out.println();
			System.out.println("<!-- Json serialization -->");
			System.out.println(MetadataFactory.serializeToJsonString(metadata));
			
		} catch (AmaliaException e) {
			e.printStackTrace();
		}

	}

}
