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

package fr.ina.research.amalia;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class holds some constant values that are set by Maven and Subversion.
 * They are thus available to the application.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class AmaliaConstants {

	public final static String AMALIA_CONSTANTS = "AmaliaConstants";
	public final static String SCM_REVISION = "SCM_REVISION";
	public final static String SCM_REVISION_DEFAULT = "000";
	public final static String PROJECT_VERSION = "PROJECT_VERSION";
	public final static String PROJECT_VERSION_DEFAULT = "0.0.0";

	private static Map<String, Properties> cache;

	public static String getConstant(Class<?> clazz, String key, String defaultValue) throws IOException {
		return getConstant(clazz.getSimpleName(), key, defaultValue);
	}

	public synchronized static String getConstant(String clazz, String key, String defaultValue) throws IOException {
		if (cache == null) {
			cache = new HashMap<String, Properties>();
		}

		if (!cache.containsKey(clazz)) {
			Properties p = new Properties();

			try {
				String file = "/" + clazz + ".properties";
				InputStream is = AmaliaConstants.class.getResourceAsStream(file);
				if (is == null) {
					throw new AmaliaException("AmaliaConstants - Unable to load " + file + " : using default values");
				}
				p.load(is);
				is.close();
			} catch (AmaliaException e) {
				System.out.println(e.getMessage());
			}

			cache.put(clazz, p);
		}

		Properties p = cache.get(clazz);
		if (p.containsKey(key)) {
			String v = p.getProperty(key);
			if (v.indexOf("${") >= 0) {
				return defaultValue;
			}
			return v;
		} else {
			return defaultValue;
		}
	}

	public static String getFullVersion(Class<?> clazz) throws IOException {
		return getFullVersion(clazz.getSimpleName());
	}

	public static String getFullVersion(String clazz) throws IOException {
		return getProjectVersion(clazz) + " (build " + getRevision(clazz) + ")";
	}

	public static String getProjectVersion() throws IOException {
		return getProjectVersion(AmaliaConstants.class);
	}

	public static String getProjectVersion(Class<?> clazz) throws IOException {
		return getProjectVersion(clazz.getSimpleName());
	}

	public static String getProjectVersion(String clazz) throws IOException {
		return getConstant(clazz, PROJECT_VERSION, PROJECT_VERSION_DEFAULT);
	}

	public static String getRevision() throws IOException {
		return getRevision(AmaliaConstants.class);
	}

	public static String getRevision(Class<?> clazz) throws IOException {
		return getRevision(clazz.getSimpleName());
	}

	public static String getRevision(String clazz) throws IOException {
		return getConstant(clazz, SCM_REVISION, SCM_REVISION_DEFAULT);
	}

	public static String getXMLSchema() throws IOException {
		return getConstant(AmaliaConstants.class, "AMALIA_XML_SCHEMA", "/amalia-model-0.2.5.xsd");
	}

}
