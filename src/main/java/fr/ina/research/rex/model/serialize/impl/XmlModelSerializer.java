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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import com.ctc.wstx.api.InvalidCharHandler;
import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;

import fr.ina.research.rex.model.serialize.ModelException;
import fr.ina.research.rex.model.serialize.impl.DefaultModelSerializer;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class XmlModelSerializer<T> extends DefaultModelSerializer<T> {

	private ByteArrayOutputStream bufferOutput;
	private Marshaller marshaller;
	private Class<T> serializedObjectType;
	private Transformer transformer;
	private XMLStreamWriter xmlWriter;
	private XMLInputFactory xmlInputTransFactory;

	/**
	 * Create a serializer to the specified output.
	 * 
	 * @param output
	 */
	public XmlModelSerializer(Class<T> serializedObjectType) {
		super();
		this.serializedObjectType = serializedObjectType;
	}

	private Marshaller createMarshaller(boolean fragmentMarshaller) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(serializedObjectType);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, fragmentMarshaller);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		return marshaller;
	}

	private StAXSource createTransSource(InputStream input) throws XMLStreamException {
		if (xmlInputTransFactory == null) {
			xmlInputTransFactory = new WstxInputFactory();
			xmlInputTransFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			xmlInputTransFactory.setProperty(WstxInputProperties.P_INPUT_PARSING_MODE, WstxInputProperties.PARSING_MODE_FRAGMENT);
		}

		XMLStreamReader xmlReader = xmlInputTransFactory.createXMLStreamReader(input, "UTF-8");
		return new StAXSource(xmlReader);
	}

	private XMLStreamWriter createXMLWriter(boolean fragmentMarshaller) throws XMLStreamException, IOException, TransformerException {

		// String cdataElements = "data text syncTextChunk";

		TransformerFactory tf = TransformerFactory.newInstance();

		transformer = tf.newTransformer();
		// transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS,
		// cdataElements);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		bufferOutput = new ByteArrayOutputStream();

		XMLOutputFactory xof = new WstxOutputFactory();
		xof.setProperty(WstxOutputProperties.P_OUTPUT_VALIDATE_CONTENT, true);
		xof.setProperty(WstxOutputProperties.P_OUTPUT_VALIDATE_STRUCTURE, true);
		xof.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, false);
		xof.setProperty(WstxOutputProperties.P_OUTPUT_INVALID_CHAR_HANDLER, new InvalidCharHandler.ReplacingHandler(' '));
		XMLStreamWriter xmlWriter = xof.createXMLStreamWriter(bufferOutput, "UTF-8");
		return xmlWriter;
	}

	/**
	 * Serialize the specified object to the output.
	 * 
	 * @param object
	 */
	@Override
	public void serialize(T object) throws ModelException {

		try {
			if (marshaller == null) {
				marshaller = createMarshaller(true);
			}

			if (xmlWriter == null) {
				xmlWriter = createXMLWriter(true);
			}

			// Marshall in memory
			bufferOutput.reset();
			marshaller.marshal(object, xmlWriter);
			xmlWriter.flush();
			// Indent and transform data value form text to cdata
			StreamResult sr = null;
			if (isStreamUsed()) {
				sr = new StreamResult(getStream());
			} else if (isWriterUsed()) {
				sr = new StreamResult(getWriter());
			} else {
				throw new IOException("You should set a stream or a reader for XmlModelSerializer");
			}

			StAXSource transSource = createTransSource(new ByteArrayInputStream(bufferOutput.toByteArray()));
			transformer.transform(transSource, sr);
		} catch (JAXBException e) {
			throw new ModelException(e);
		} catch (XMLStreamException e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		} catch (TransformerException e) {
			throw new ModelException(e);
		}
	}

}
