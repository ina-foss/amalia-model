/*
 * Copyright (c) 2024 Institut National de l'Audiovisuel, INA
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
import fr.ina.research.amalia.model.jaxb.DirectionType;
import fr.ina.research.amalia.model.jaxb.Relation;
import fr.ina.research.rex.commons.tc.RexTimeCode;

/**
 * Tutorial : creation of a span metadata block.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class TestSpanRelations {

	public static void main(String[] args) {
		try {
			String trs = "Emmanuel Macron évoque les gens qui réussissent et les gens qui ne sont rien";
			String[] words = trs.split(" ");
			
			RexTimeCode previous = new RexTimeCode(0);
			RexTimeCode duration = RexTimeCode.build(0, words.length, 0, 0);
			
			MetadataBlock transcription = MetadataFactory.createMetadataBlock("test-transcription", MetadataType.TRANSCRIPTION, duration);
			transcription.setAlgorithm(TestSpanRelations.class.getSimpleName());
			transcription.setProcessor("Ina Research Department - N. HERVE");
			transcription.setAlgorithm("Fake");
			transcription.setVersion(1);
			LocalisationBlock tb = MetadataFactory.createSynchronizedTextLocalisationBlock(previous, duration, trs);
			transcription.addToRootLocalisationBlock(tb);
			
			MetadataBlock spans = MetadataFactory.createMetadataBlock("test-span", MetadataType.SPAN, duration);
			spans.setAlgorithm(TestSpanRelations.class.getSimpleName());
			spans.setProcessor("Ina Research Department - N. HERVE");
			spans.setAlgorithm("Fake");
			spans.setVersion(1);
			
			RexTimeCode current = null;
			
			for (int i = 0; i < words.length; i++) {
				current = RexTimeCode.build(0, i + 1, 0, 0);
				LocalisationBlock w = tb.addLocalisationBlock(previous, current);
				w.setSynchronizedText(words[i]);
				previous = new RexTimeCode(current.getSecond());
			}
			
			LocalisationBlock span01 = MetadataFactory.createLocalisationBlock(RexTimeCode.build(0, 0, 0, 0),  RexTimeCode.build(0, 2, 0, 0));
			span01.setId("span01");
			span01.addProperty("entityType", "Person");
			spans.addToRootLocalisationBlock(span01);
			
			LocalisationBlock span02 = MetadataFactory.createLocalisationBlock(RexTimeCode.build(0, 2, 0, 0),  RexTimeCode.build(0, 3, 0, 0));
			span02.setId("span02");
			span02.addProperty("entityType", "Cue");
			spans.addToRootLocalisationBlock(span02);
			
			LocalisationBlock span03 = MetadataFactory.createLocalisationBlock(RexTimeCode.build(0, 3, 0, 0),  RexTimeCode.build(0, 14, 0, 0));
			span03.setId("span03");
			span03.addProperty("entityType", "Quote");
			spans.addToRootLocalisationBlock(span03);
			
			spans.addRelation(span01.getInternal().getId(), span03.getInternal().getId(), DirectionType.FROM_TO, "relationType", "Quoted_in");
			spans.addRelation(span02.getInternal().getId(), span03.getInternal().getId(), DirectionType.FROM_TO, "relationType", "Indicates");
			
			boolean indent = false;
			System.out.println(MetadataFactory.serializeToJsonString(transcription, indent));
			System.out.println();
			System.out.println("--------------------------------------------------------------");
			System.out.println();
			System.out.println(MetadataFactory.serializeToJsonString(spans, indent));
			
		} catch (AmaliaException e) {
			e.printStackTrace();
		}

	}

}
