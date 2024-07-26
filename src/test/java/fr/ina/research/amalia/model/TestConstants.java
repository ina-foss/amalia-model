/*
 * Copyright (c) 2015-2024 Institut National de l'Audiovisuel, INA
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

import java.io.IOException;

import fr.ina.research.amalia.AmaliaConstants;


/**
 * Test constants reading.
 * 
 * @author Nicolas HERVE - nherve@ina.fr
 */
public class TestConstants {

	public static void main(String[] args) {
		try {
			System.out.println("XML Schema      : " + AmaliaConstants.getXMLSchema());
			System.out.println("Project version : " + AmaliaConstants.getProjectVersion());
			System.out.println("Revision        : " + AmaliaConstants.getRevision());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
