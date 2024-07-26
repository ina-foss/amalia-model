/*
 * Copyright 2012-2024 Institut National de l'Audiovisuel
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
import java.io.InputStream;
import java.io.Reader;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import fr.ina.research.rex.model.serialize.ModelException;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class JsonModelDeserializer<T> extends DefaultModelDeserializer<T> {
	private Class<T> deserializedObjectType;
	private JsonParser jsonParser;
	private ObjectMapper objectMapper;
	private String objectTypeXmlTagName;

	public JsonModelDeserializer(Class<T> deserializedObjectType) {
		super();
		this.deserializedObjectType = deserializedObjectType;
		objectMapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		// make serializer use JAXB annotations (only)
		objectMapper.setDeserializationConfig(objectMapper.getDeserializationConfig().withAnnotationIntrospector(introspector));
		objectTypeXmlTagName = deserializedObjectType.getAnnotation(XmlRootElement.class).name();

	}

	@Override
	public T deserializeNext() throws ModelException {
		try {
			if (jsonParser == null) {
				if (isReaderUsed()) {
					jsonParser = objectMapper.getJsonFactory().createJsonParser(getReader());
				} else if (isStreamUsed()) {
					jsonParser = objectMapper.getJsonFactory().createJsonParser(getStream());
				} else {
					throw new ModelException("You should set a stream or a reader for JsonModelDeserializer");
				}
			}
			JsonToken token = jsonParser.nextValue();

			while (token != null) {
				String fieldName = jsonParser.getCurrentName();

				// System.out.println(fieldName == null ? "null "+token.name() :
				// fieldName+" "+token.name());

				if ((token == JsonToken.START_OBJECT) && ((fieldName == null) || fieldName.equals(objectTypeXmlTagName))) {
					try {
						T object = objectMapper.readValue(jsonParser, deserializedObjectType);
						return object;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				token = jsonParser.nextValue();
			}
			return null;
		} catch (JsonParseException e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		}
	}

	@Override
	public void setReader(Reader reader) {
		super.setReader(reader);
		jsonParser = null;
	}

	@Override
	public void setStream(InputStream stream) {
		super.setStream(stream);
		jsonParser = null;
	}

}
