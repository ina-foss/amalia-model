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
package fr.ina.research.rex.commons.tc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fr.ina.research.amalia.AmaliaException;

/**
 *
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class RexTimeCode implements Comparable<RexTimeCode> {
	public final static double DEFAULT_FPS = 25d;
	public final static String DEFAULT_PATTERN = "([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{4})";
	public final static String DEFAULT_FORMAT = "%02d:%02d:%02d.%04d";
	public final static double MS = 10000d;

	private static Pattern pattern;

	public static RexTimeCode build(int h, int m, int s, int ms) throws AmaliaException {
		if (h < 0) {
			throw new AmaliaException("h must be >= 0");
		}
		if (m < 0) {
			throw new AmaliaException("m must be >= 0");
		}
		if (s < 0) {
			throw new AmaliaException("s must be >= 0");
		}
		if (ms < 0) {
			throw new AmaliaException("ms must be >= 0");
		}
		return new RexTimeCode((h * 3600d) + (m * 60d) + s + (ms / MS));
	}

	public static synchronized void initPattern() throws AmaliaException {
		if (pattern == null) {
			initPattern(DEFAULT_PATTERN);
		}
	}

	public static synchronized void initPattern(String str) throws AmaliaException {
		try {
			pattern = Pattern.compile(str);
		} catch (PatternSyntaxException e) {
			throw new AmaliaException(e);
		}
	}

	public static RexTimeCode parse(String tc) throws AmaliaException {
		initPattern();
		Matcher m = pattern.matcher(tc);

		if (!m.matches()) {
			throw new AmaliaException("Invalid pattern for RexTimeCode : " + tc);
		}

		if (m.groupCount() != 4) {
			throw new AmaliaException("Invalid pattern for RexTimeCode (" + m.groupCount() + ")");
		}

		try {
			return build(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
		} catch (NumberFormatException e) {
			throw new AmaliaException(e);
		}

	}

	public static boolean validate(String tc) throws AmaliaException {
		initPattern();
		return pattern.matcher(tc).matches();
	}

	private double second;

	private RexTimeCode() {
		super();
	}
	
	public RexTimeCode(double second) {
		this();
		this.second = second;
	}

	public RexTimeCode(long frame) {
		this(frame, DEFAULT_FPS);
	}

	public RexTimeCode(long frame, double fps) {
		super();
		setFrame(frame, fps);
	}

	public void add(double s) throws AmaliaException {
		if (s < 0) {
			throw new AmaliaException("Use remove() for negative values");
		}
		second += s;
	}

	public void add(long frame, double fps) throws AmaliaException {
		add(frame / fps);
	}

	public void add(RexTimeCode o) throws AmaliaException {
		add(o.getSecond());
	}

	@Override
	public int compareTo(RexTimeCode o) {
		return (int) Math.signum(getSecond() - o.getSecond());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RexTimeCode other = (RexTimeCode) obj;
		if (Double.doubleToLongBits(second) != Double.doubleToLongBits(other.second)) {
			return false;
		}
		return true;
	}

	public long getFrame(double fps) {
		return Math.round(second * fps);
	}

	public double getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(second);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public void remove(double s) throws AmaliaException {
		second -= s;
		if (second < 0) {
			throw new AmaliaException("Negative timecodes not allowed yet");
		}
	}

	public void remove(long frame, double fps) throws AmaliaException {
		remove(frame / fps);
	}

	public void remove(RexTimeCode o) throws AmaliaException {
		remove(o.getSecond());
	}

	public void setFrame(long frame, double fps) {
		setSecond(frame / fps);
	}

	public void setSecond(double second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return toString(DEFAULT_FORMAT);
	}

	public String toString(String format) {
		int h = (int) (second / 3600);
		double left = second - (h * 3600);
		int m = (int) (left / 60);
		left = left - (m * 60);
		int s = (int) Math.floor(left);
		left = left - s;
		int ms = (int) Math.round(left * MS);
		return String.format(format, h, m, s, ms);
	}
}
