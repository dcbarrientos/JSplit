/*
 *  Copyright (C) 2016 Diego Barrientos <dc_barrientos@yahoo.com.ar>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/** 
 * Constantes.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 12 de ago. de 2016, 5:17:05 p. m. 
 */

package ar.com.dcbarrientos.jsplit;

import java.text.DecimalFormat;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Constantes {
	public static final int BYTES = 0;
	public static final int KB = 1;
	public static final int MB = 2;
	public static final int GB = 3;
	
	
	/**
	 * Cuando se le ingresa un valor en bytes devuelve su equivalente en la unidad más
	 * grande posible. 1024 bytes devolvería 1 kb.
	 * @param nro Valor en bytes para calcular su equivalente.
	 * @return Devuelve el equibalente más grande posible.
	 */	
	public static String getExtendedSize(double nro){
		double size = nro;
		int cons = 1024;
		int unidad = 0;
		
		while(size > cons){
			size /= cons;
			unidad++;
		}
	
		DecimalFormat dec = new DecimalFormat("0.00");
		if(unidad == BYTES)
			return dec.format(size).concat(" B.");
		else if(unidad == KB)
			return dec.format(size).concat(" Kb.");
		else if(unidad == MB)
			return dec.format(size).concat(" Mb.");
	
		return dec.format(size).concat(" Gb.");
	}

	/**
	 * @param size1 Tamaño en bytes del divisor
	 * @param unid1 Unidad del divisor.
	 * @param size2 Tamaño en bytes del dividendo
	 * @param unid2 Unidad del dividendo.
	 * @param unid3 Unidad del resultado
	 * @return
	 */
	public static int fileSizeDivider(long size1, int unid1, long size2, int unid2){
		int res;
		for(int i = 0; i < unid1; i ++)
			size1 *= 1024;

		for(int i = 0; i < unid2; i ++)
			size2 *= 1024;
		
		res = (int)(size1 / size2);
		
		if((size1 % size2) > 0)
			res++;
		
		return res;
		
	}
	
	/**
	 * Lleva cualquier unidad mayor a bytes a bytes.
	 * @param size	//Tamaño a convertir.
	 * @param unid	//Unidad que hay que convertir a bytes.
	 * @return
	 */
	public static long toBytes(long size, int unid){
		long res = size;
		
		for(int i = 0; i < unid; i++){
			res *= 1024;
		}
		
		return res;
	}
	
	/**
	 * Verifica si el sistema operativo es Windows.
	 * @return True Si es Windows.
	 */
	public static boolean isWindows(){
		if(System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0)
			return true;
		return false;
	}

	/**
	 * Verifica si el sistema operativo es Linux
	 * @return True si es Linux.
	 */
	public static boolean isLinux(){
		if(System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0)
			return true;
		return false;
	}
}
