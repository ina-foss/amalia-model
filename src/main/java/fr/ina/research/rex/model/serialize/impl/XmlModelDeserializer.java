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
import java.io.InputStream;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.XMLStreamReader2;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.stax.WstxInputFactory;

import fr.ina.research.rex.model.serialize.ModelException;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class XmlModelDeserializer<T> extends DefaultModelDeserializer<T> {
	private Class<T> deserializedObjectType;
	private String objectTypeXmlTagName;
	private Unmarshaller unmarshaller;
	private XMLStreamReader xmlReader;

	/**
	 * Create a deserializer from the specified input.
	 * 
	 * @param input
	 */
	public XmlModelDeserializer(Class<T> deserializedObjectType) {
		super();
		this.deserializedObjectType = deserializedObjectType;
		this.objectTypeXmlTagName = deserializedObjectType.getAnnotation(XmlRootElement.class).name();
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(deserializedObjectType);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;

	}

	private XMLStreamReader createXMLReader(boolean fragmentMarshaller) throws XMLStreamException, IOException, ModelException {
		XMLInputFactory xif = new WstxInputFactory();
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

		if (fragmentMarshaller) {
			xif.setProperty(WstxInputProperties.P_INPUT_PARSING_MODE, WstxInputProperties.PARSING_MODE_FRAGMENT);
		}

		XMLStreamReader2 xmlReader = null;
		if (isReaderUsed()) {
			xmlReader = (XMLStreamReader2) xif.createXMLStreamReader(getReader());
		} else if (isStreamUsed()) {
			xmlReader = (XMLStreamReader2) xif.createXMLStreamReader(getStream());
		} else {
			throw new IOException("You should set a stream or a reader for XmlModelDeserializer");
		}

		return xmlReader;
	}

	/**
	 * Deserialize the next object
	 * 
	 * @return the next object or null if we reach the end of the input.
	 */
	@Override
	public T deserializeNext() throws ModelException {
		try {
			if (unmarshaller == null) {
				unmarshaller = createUnmarshaller();
			}

			if (xmlReader == null) {
				xmlReader = createXMLReader(true);
			}

			while (xmlReader.hasNext()) {
				int event = xmlReader.next();

				if ((event == XMLStreamConstants.START_ELEMENT) && xmlReader.getLocalName().equals(objectTypeXmlTagName)) {
					T object = unmarshaller.unmarshal(xmlReader, deserializedObjectType).getValue();
					return object;
				}
			}
			return null;
		} catch (JAXBException e) {
			throw new ModelException(e);
		} catch (XMLStreamException e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		}

	}

	@Override
	public void setReader(Reader param) {
		super.setReader(param);
		xmlReader = null;
	}

	@Override
	public void setStream(InputStream param) {
		super.setStream(param);
		xmlReader = null;
	}

}
