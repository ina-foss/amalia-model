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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock.MetadataType;
import fr.ina.research.amalia.model.jaxb.Localisation;
import fr.ina.research.amalia.model.jaxb.Metadata;
import fr.ina.research.rex.commons.tc.RexTimeCode;
import fr.ina.research.rex.model.serialize.ModelException;
import fr.ina.research.rex.model.serialize.ModelSerializer;
import fr.ina.research.rex.model.serialize.impl.JsonModelSerializer;
import fr.ina.research.rex.model.serialize.impl.XmlModelSerializer;

/**
 * This is the main class to create, manipulate and serialize metadata blocks.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class MetadataFactory {

	public static LocalisationBlock createLocalisationBlock() throws AmaliaException {
		LocalisationBlock l = new LocalisationBlock();
		return l;
	}

	public static LocalisationBlock createLocalisationBlock(RexTimeCode tc) throws AmaliaException {
		return createLocalisationBlock(tc.toString());
	}

	public static LocalisationBlock createLocalisationBlock(RexTimeCode tcin, RexTimeCode tcout) throws AmaliaException {
		return createLocalisationBlock(tcin.toString(), tcout.toString());
	}

	public static LocalisationBlock createLocalisationBlock(String tc) throws AmaliaException {
		return createLocalisationBlock().setTc(tc);
	}

	public static LocalisationBlock createLocalisationBlock(String tcin, String tcout) throws AmaliaException {
		return createLocalisationBlock().setTcin(tcin).setTcout(tcout);
	}

	public static MetadataBlock createMetadataBlock() throws AmaliaException {
		MetadataBlock w = new MetadataBlock();
		return w.setProcessedNow();
	}

	public static MetadataBlock createMetadataBlock(String id) throws AmaliaException {
		MetadataBlock w = createMetadataBlock();
		return w.setId(id);
	}

	public static MetadataBlock createMetadataBlock(String id, MetadataType type) throws AmaliaException {
		MetadataBlock w = createMetadataBlock(id);
		return w.setType(type);
	}

	public static MetadataBlock createMetadataBlock(String id, MetadataType type, RexTimeCode duration) throws AmaliaException {
		MetadataBlock w = createMetadataBlock(id, type);
		w.setRootLocalisationBlock(MetadataFactory.createLocalisationBlock(new RexTimeCode(0), duration));
		return w;
	}

	public static MetadataBlock createMetadataBlock(String id, String type) throws AmaliaException {
		MetadataBlock w = createMetadataBlock(id);
		return w.setType(type);
	}

	public static LocalisationBlock createSynchronizedTextLocalisationBlock(RexTimeCode tcin, RexTimeCode tcout, String text) throws AmaliaException {
		return createLocalisationBlock(tcin, tcout).setSynchronizedText(text);
	}

	public static LocalisationBlock createSynchronizedTextLocalisationBlock(String tcin, String tcout, String text) throws AmaliaException {
		return createLocalisationBlock(tcin, tcout).setSynchronizedText(text);
	}

	public static void serialize(MetadataBlock metadata, ModelSerializer<Metadata> serializer) throws AmaliaException {
		try {
			serializer.serialize(metadata.getInternal());
		} catch (ModelException e) {
			throw new AmaliaException(e);
		}
	}

	public static void serializeToFile(MetadataBlock metadata, File f, ModelSerializer<Metadata> serializer) throws AmaliaException {
		try {
			FileWriter w = new FileWriter(f);
			serializer.setWriter(w);
			serialize(metadata, serializer);
			w.close();
		} catch (IOException e) {
			throw new AmaliaException(e);
		}
	}

	public static void serializeToJsonFile(MetadataBlock metadata, File f) throws AmaliaException {
		JsonModelSerializer<Metadata> jsonWriter = new JsonModelSerializer<Metadata>(false, Metadata.class);
		serializeToFile(metadata, f, jsonWriter);
	}

	public static String serializeToJsonString(MetadataBlock metadata) throws AmaliaException {
		JsonModelSerializer<Metadata> jsonWriter = new JsonModelSerializer<Metadata>(false, Metadata.class);
		return serializeToString(metadata, jsonWriter);
	}

	public static String serializeToString(MetadataBlock metadata, ModelSerializer<Metadata> serializer) throws AmaliaException {
		StringWriter w = new StringWriter();
		serializer.setWriter(w);
		serialize(metadata, serializer);
		return w.toString();
	}

	public static void serializeToXMLFile(MetadataBlock metadata, File f) throws AmaliaException {
		XmlModelSerializer<Metadata> jsonWriter = new XmlModelSerializer<Metadata>(Metadata.class);
		serializeToFile(metadata, f, jsonWriter);
	}

	public static String serializeToXMLString(MetadataBlock metadata) throws AmaliaException {
		XmlModelSerializer<Metadata> jsonWriter = new XmlModelSerializer<Metadata>(Metadata.class);
		return serializeToString(metadata, jsonWriter);
	}

	public static LocalisationBlock wrap(Localisation l) throws AmaliaException {
		return new LocalisationBlock(l);
	}

	public static MetadataBlock wrap(Metadata m) throws AmaliaException {
		return new MetadataBlock(m);
	}
}
