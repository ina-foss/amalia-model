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

import java.io.InputStream;
import java.io.Reader;

import fr.ina.research.rex.model.serialize.ModelDeserializer;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public abstract class DefaultModelDeserializer<T> implements ModelDeserializer<T> {
	private Reader reader;
	private boolean readerUsed;
	private InputStream stream;
	private boolean streamUsed;

	public DefaultModelDeserializer() {
		super();
		stream = null;
		streamUsed = false;
		reader = null;
		readerUsed = false;
	}

	public Reader getReader() {
		return reader;
	}

	public InputStream getStream() {
		return stream;
	}

	public boolean isReaderUsed() {
		return readerUsed;
	}

	public boolean isStreamUsed() {
		return streamUsed;
	}

	@Override
	public void setReader(Reader param) {
		reader = param;
		if (reader == null) {
			readerUsed = false;
		} else {
			readerUsed = true;
			setStream(null);
		}

	}

	@Override
	public void setStream(InputStream param) {
		stream = param;
		if (stream == null) {
			streamUsed = false;
		} else {
			streamUsed = true;
			setReader(null);
		}
	}
}
