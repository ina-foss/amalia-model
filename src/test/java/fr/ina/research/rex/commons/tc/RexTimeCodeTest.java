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
package fr.ina.research.rex.commons.tc;

import org.junit.Test;

import fr.ina.research.amalia.AmaliaException;
import junit.framework.Assert;

/**
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class RexTimeCodeTest {

	@Test
	public void testBuild() {
		try {
			Assert.assertNotNull(RexTimeCode.build(0, 0, 0, 0));
			Assert.assertNotNull(RexTimeCode.build(0, 0, 1, 0));
			Assert.assertNotNull(RexTimeCode.build(0, 1, 0, 0));
			Assert.assertNotNull(RexTimeCode.build(1, 0, 0, 0));
			Assert.assertNotNull(RexTimeCode.build(0, 0, 0, 0));
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}

		try {
			RexTimeCode.build(0, 0, 0, -1);
			Assert.fail("No exception launched");
		} catch (AmaliaException e) {
		}

		try {
			RexTimeCode tc = RexTimeCode.build(0, 0, 0, 0);
			tc.setFrame(25l, RexTimeCode.DEFAULT_FPS);
			Assert.assertEquals(1d, tc.getSecond());
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}
	}

	@Test
	public void testParse() {
		try {
			String[] strings = new String[] { "00:00:00.0000", "00:00:01.0000", "00:01:00.0000", "01:00:00.0000" };
			long[] frames23 = new long[] { 0l, 24l, 1439l, 86314l };
			long[] frames24 = new long[] { 0l, 24l, 1440l, 86400l };
			long[] frames25 = new long[] { 0l, 25l, 1500l, 90000l };
			double[] seconds = new double[] { 0d, 1d, 60d, 3600d };

			for (int i = 0; i < strings.length; i++) {
				Assert.assertTrue(RexTimeCode.validate(strings[i]));

				RexTimeCode tc = RexTimeCode.parse(strings[i]);
				Assert.assertNotNull(tc);
				Assert.assertEquals(frames25[i], tc.getFrame(25d));
				Assert.assertEquals(seconds[i], tc.getSecond());

				tc = RexTimeCode.parse(strings[i]);
				Assert.assertNotNull(tc);
				Assert.assertEquals(frames24[i], tc.getFrame(24d));
				Assert.assertEquals(seconds[i], tc.getSecond());

				tc = RexTimeCode.parse(strings[i]);
				Assert.assertNotNull(tc);
				Assert.assertEquals(frames23[i], tc.getFrame(23.976d));
				Assert.assertEquals(seconds[i], tc.getSecond());
			}

			String[] fail = new String[] { "00:00:00", "00:01.0000", "0", "0100:000000", "", "azerty" };
			for (String s : fail) {
				Assert.assertFalse(RexTimeCode.validate(s));
			}
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}
	}

	@Test
	public void testParse2() {
		try {
			String[] strings = new String[] { "00:00:00.4400", "00:00:04.1200", "00:00:09.5600", "00:00:13.1600" };
			double[] seconds = new double[] { 0.44d, 4.12d, 9.56d, 13.16d };

			for (int i = 0; i < strings.length; i++) {
				Assert.assertTrue(RexTimeCode.validate(strings[i]));
				RexTimeCode tc = RexTimeCode.parse(strings[i]);
				Assert.assertNotNull(tc);
				Assert.assertEquals(seconds[i], tc.getSecond());
			}
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}
	}

	@Test
	public void testAddRemove() {
		try {
			RexTimeCode tc1 = RexTimeCode.parse("00:00:00.0000");
			tc1.add(25l, RexTimeCode.DEFAULT_FPS);
			Assert.assertEquals(25, tc1.getFrame(RexTimeCode.DEFAULT_FPS));

			RexTimeCode tc2 = RexTimeCode.parse("00:00:01.0000");
			tc1.add(tc2);
			Assert.assertEquals(50, tc1.getFrame(RexTimeCode.DEFAULT_FPS));
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}
	}

	@Test
	public void testComparison() {
		try {
			RexTimeCode tc1 = RexTimeCode.parse("00:00:00.0000");
			RexTimeCode tc2 = RexTimeCode.parse("00:00:01.0000");
			RexTimeCode tc3 = RexTimeCode.parse("00:00:01.0000");

			Assert.assertTrue(tc1.equals(tc1));

			Assert.assertFalse(tc1.equals(tc2));
			Assert.assertFalse(tc2.equals(tc1));
			Assert.assertTrue(tc1.compareTo(tc2) < 0);
			Assert.assertTrue(tc2.compareTo(tc1) > 0);

			Assert.assertTrue(tc2.equals(tc3));
			Assert.assertTrue(tc3.equals(tc2));
			Assert.assertTrue(tc2.compareTo(tc3) == 0);
			Assert.assertTrue(tc3.compareTo(tc2) == 0);
			
			Assert.assertTrue(tc1.isBefore(tc2));
			Assert.assertTrue(tc2.isAfter(tc1));
			Assert.assertFalse(tc2.isAfter(tc3));
			
			Assert.assertTrue(tc1.isBeforeOrEqual(tc2));
			Assert.assertTrue(tc2.isAfterOrEqual(tc1));
			Assert.assertTrue(tc2.isAfterOrEqual(tc3));
			
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}
	}

	@Test
	public void testFormat() {
		try {
			String[] strings = new String[] { "00:00:00.0000", "00:00:01.0000", "00:01:00.0000", "01:00:00.0000", "00:00:13.1600", "12:53:01.0476" };
			for (String s : strings) {
				RexTimeCode tc = RexTimeCode.parse(s);
				Assert.assertEquals(s, tc.toString());
			}
		} catch (AmaliaException e) {
			Assert.fail("AmaliaException : " + e.getMessage());
		}
	}

}
