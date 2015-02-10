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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock;
import fr.ina.research.amalia.model.MetadataBlock.MetadataType;
import fr.ina.research.amalia.model.MetadataFactory;
import fr.ina.research.rex.commons.tc.RexTimeCode;

/**
 * SubRip file parser. Currently only supports plain format.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/SubRip">SubRip on Wikipedia</a>
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class SubripFileParser {
	public final static String SINGLE_PATTERN = "([0-9]{2}):([0-9]{2}):([0-9]{2}),([0-9]{3})";
	public final static String FULL_PATTERN = SINGLE_PATTERN + " --> " + SINGLE_PATTERN;

	private Pattern pattern;

	public SubripFileParser() throws AmaliaException {
		super();
		try {
			pattern = Pattern.compile(FULL_PATTERN);
		} catch (PatternSyntaxException e) {
			throw new AmaliaException(e);
		}
	}

	public MetadataBlock convertToMetadata(String id, List<SingleSubTitle> srt) throws AmaliaException {
		MetadataBlock metadata = MetadataFactory.createMetadataBlock(id, MetadataType.SYNCHRONIZED_TEXT);
		metadata.setVersion(1);

		RexTimeCode tcIn = null;
		RexTimeCode tcOut = null;
		
		if (srt.isEmpty()) {
			tcIn = RexTimeCode.build(0, 0, 0, 0);
			tcOut = tcIn;
		} else {
			tcIn = srt.get(0).getTcin();
			tcOut = srt.get(srt.size() - 1).getTcout();
		}
		
		metadata.setRootLocalisationBlock(tcIn, tcOut);
		for (SingleSubTitle sst : srt) {
			metadata.addToRootLocalisationBlock(sst.convertToLocalisation());
		}

		return metadata;
	}

	public List<SingleSubTitle> parse(File f) throws AmaliaException {
		BufferedReader r = null;

		try {
			r = new BufferedReader(new FileReader(f));
			String line;
			int lineNumber = 0;

			boolean nextLineId = true;
			boolean nextLineTC = false;
			SingleSubTitle current = null;
			List<SingleSubTitle> res = new ArrayList<SingleSubTitle>();

			while ((line = r.readLine()) != null) {
				lineNumber++;
				if (line.trim().isEmpty() && !nextLineTC) {
					nextLineId = true;
					if (current != null) {
						res.add(current);
						current = null;
					}
				} else if (nextLineId) {
					try {
						int id = Integer.parseInt(line);
						current = new SingleSubTitle();
						current.setId(id);
						nextLineId = false;
						nextLineTC = true;
					} catch (NumberFormatException e) {
						throw new AmaliaException("Bad SRT file format : line " + lineNumber + ", id expected instead of <<" + line + ">>");
					}
				} else if (nextLineTC) {
					Matcher m = pattern.matcher(line);

					if (!m.matches()) {
						throw new AmaliaException("Bad SRT file format : line " + lineNumber + ", invalid timecode pattern found : <<" + line + ">>");
					}

					if (m.groupCount() != 8) {
						throw new AmaliaException("Bad SRT file format : line " + lineNumber + ", invalid timecode pattern found (" + m.groupCount() + " groups) : <<" + line + ">>");
					}

					try {
						current.setTcin(RexTimeCode.build(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
						current.setTcout(RexTimeCode.build(Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8))));
					} catch (NumberFormatException e) {
						throw new AmaliaException("Bad SRT file format : line " + lineNumber + ", invalid timecode pattern found : <<" + line + ">> | " + e.getMessage());
					} catch (AmaliaException e) {
						throw new AmaliaException("Bad SRT file format : line " + lineNumber + ", invalid timecode pattern found : <<" + line + ">> | " + e.getMessage());
					}
					nextLineTC = false;
				} else {
					current.addText(line);
				}
			}
			return res;
		} catch (IOException e1) {
			throw new AmaliaException(e1);
		} finally {
			try {
				r.close();
			} catch (IOException e) {
				throw new AmaliaException(e);
			}
		}

	}

}
