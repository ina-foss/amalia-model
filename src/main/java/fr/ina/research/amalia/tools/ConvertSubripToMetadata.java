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
package fr.ina.research.amalia.tools;

import java.io.File;
import java.util.List;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock;
import fr.ina.research.amalia.model.MetadataFactory;
import fr.ina.research.amalia.model.text.SingleSubTitle;
import fr.ina.research.amalia.model.text.SubripFileParser;

/**
 * Converts a SubRip file to the Amalia metadata format and provides either a XML or a Json file.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class ConvertSubripToMetadata {
	public static void usage() {
		System.out.println("Usage : ");
		System.out.println("java " + ConvertSubripToMetadata.class.getName() + " [srt file] [output file] [format (xml/json)] [metadata id]");
		System.out.println();
		System.out.println("Examples :");
		System.out.println("java " + ConvertSubripToMetadata.class.getName() + " myVideo.srt myVideo.xml xml ts-123");
		System.out.println("java " + ConvertSubripToMetadata.class.getName() + " myVideo.srt myVideo.json json ts-123");
		System.exit(1);
	}
	
	public static void main(String[] args) {
		if (args.length != 4) {
			usage();
		}
		
		File srtFile = new File(args[0]);
		if (!srtFile.exists()) {
			System.err.println("Unable to find the srt file : " + srtFile.getAbsolutePath());
			usage();
		}
		
		String format = args[2].toUpperCase();
		if (!(format.equals("XML") || format.equals("JSON"))) {
			System.err.println("Invalid output format : " + format + ". Should be xml or json.");
			usage();
		}
		
		File outputFile = new File(args[1]);
		File outputDirectory = outputFile.getParentFile(); 
		if (outputDirectory != null && !outputDirectory.exists() && !outputDirectory.mkdirs()) {
			System.err.println("Unable to create directories for : " + outputFile.getAbsolutePath());
			usage();
		}
		
		String id = args[3];
		
		try {
			SubripFileParser parser = new SubripFileParser();
			List<SingleSubTitle> res = parser.parse(srtFile);
			MetadataBlock metadata = parser.convertToMetadata(id, res);
			metadata.setAlgorithm(ConvertSubripToMetadata.class.getSimpleName());
			
			if (format.equals("XML")) {
				MetadataFactory.serializeToXMLFile(metadata, outputFile);
			} else if (format.equals("JSON")) {
				MetadataFactory.serializeToJsonFile(metadata, outputFile);
			}

		} catch (AmaliaException e) {
			e.printStackTrace();
		}

	}

}
