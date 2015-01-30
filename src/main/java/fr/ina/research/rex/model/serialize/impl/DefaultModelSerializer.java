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
import java.io.OutputStream;
import java.io.Writer;

import fr.ina.research.rex.model.serialize.ModelSerializer;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public abstract class DefaultModelSerializer<T> implements ModelSerializer<T> {
	static class SettableOutputStream extends OutputStream {

		private OutputStream currentOutput;

		public SettableOutputStream() {
			super();
		}

		@Override
		public void close() throws IOException {
			currentOutput.close();
		}

		@Override
		public void flush() throws IOException {
			currentOutput.flush();
		}

		public void setCurrentOutput(OutputStream output) {
			if (currentOutput != null) {
				try {
					currentOutput.flush();
				} catch (IOException ioe) {
				}
			}
			currentOutput = output;
		}

		@Override
		public void write(byte[] b) throws IOException {
			currentOutput.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			currentOutput.write(b, off, len);
		}

		@Override
		public void write(int b) throws IOException {
			currentOutput.write(b);
		}

	}

	static class SettableWriter extends Writer {

		private Writer internal;

		public SettableWriter() {
			super();
		}

		@Override
		public void close() throws IOException {
			internal.close();
		}

		@Override
		public void flush() throws IOException {
			internal.flush();
		}

		public void setCurrentWriter(Writer writer) {
			if (internal != null) {
				try {
					internal.flush();
				} catch (IOException ioe) {
				}
			}
			internal = writer;
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			internal.write(cbuf, off, len);
		}
	}

	private SettableOutputStream stream;
	private boolean streamUsed;
	private SettableWriter writer;
	private boolean writerUsed;

	public DefaultModelSerializer() {
		super();
		stream = null;
		streamUsed = false;
		writer = null;
		writerUsed = false;
	}

	public SettableOutputStream getStream() {
		return stream;
	}

	public SettableWriter getWriter() {
		return writer;
	}

	public boolean isStreamUsed() {
		return streamUsed;
	}

	public boolean isWriterUsed() {
		return writerUsed;
	}

	@Override
	public void setStream(OutputStream param) {
		if (param == null) {
			if (stream != null) {
				stream.setCurrentOutput(null);
			}
			streamUsed = false;
		} else {
			if (stream == null) {
				stream = new SettableOutputStream();
			}
			stream.setCurrentOutput(param);
			streamUsed = true;
			setWriter(null);
		}
	}

	@Override
	public void setWriter(Writer param) {
		if (param == null) {
			if (writer != null) {
				writer.setCurrentWriter(null);
			}
			writerUsed = false;
		} else {
			if (writer == null) {
				writer = new SettableWriter();
			}
			writer.setCurrentWriter(param);
			writerUsed = true;
			setStream(null);
		}
	}
}
