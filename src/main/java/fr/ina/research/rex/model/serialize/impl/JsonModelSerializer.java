/*
 * Copyright 2012-2015 Institut National de l'Audiovisuel
 * 
 * This file is part of Rex.
 * 
 * Rex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Rex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Rex. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.ina.research.rex.model.serialize.impl;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import fr.ina.research.rex.model.serialize.ModelException;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class JsonModelSerializer<T> extends DefaultModelSerializer<T> {
	private ObjectMapper objectMapper;
	private boolean doNullValues;

	public JsonModelSerializer() {
		this(true);
	}

	public JsonModelSerializer(boolean doNullValues) {
		super();
		this.doNullValues = doNullValues;
	}

	public JsonModelSerializer(boolean doNullValues, Class<T> serializedType) {
		this(doNullValues);
		objectMapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		// make serializer use JAXB annotations (only)
		SerializationConfig sc = objectMapper.getSerializationConfig().withAnnotationIntrospector(introspector);
		sc.with(SerializationConfig.Feature.INDENT_OUTPUT);
		
		if (!doNullValues) {
			sc.without(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
			sc.without(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS);
			sc = sc.withSerializationInclusion(Inclusion.NON_EMPTY);
		}
		
		objectMapper.setSerializationConfig(sc);
//		if (!doNullValues) {
//			objectMapper.setSerializationInclusion(Inclusion.NON_EMPTY);
//		}
	}

	public JsonModelSerializer(Class<T> serializedType) {
		this(true, serializedType);
	}

	@Override
	public void serialize(T object) throws ModelException {
		try {
			if (isStreamUsed()) {
				objectMapper.writeValue(getStream(), object);
			} else if (isWriterUsed()) {
				objectMapper.writeValue(getWriter(), object);
			} else {
				throw new ModelException("You should set a stream or a reader for JsonModelSerializer");
			}
		} catch (JsonGenerationException e) {
			throw new ModelException(e);
		} catch (JsonMappingException e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		}
	}
}
