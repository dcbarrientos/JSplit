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
 * Splitter.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 14 de ago. de 2016, 6:52:34 p. m. 
 */

package ar.com.dcbarrientos.jsplit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Splitter extends SwingWorker<Long, Long>{
	
	private File input;						//Archivo para dividir
	private long splitFileSize;				//Tamaño al que hay que dividirlo
	private JProgressBar totalProgressBar;	//Progreso total del proceso.
	private boolean isCanceled;				//Verifica si se cancela el proceso.
	private int cantidadArchivos;			//LLeva la cuenta de los archvos generados.
	private Vector<String> listaArchivos;	//Lista de archivos generados.
	private String destFolder;				//Carpeta destino.
	private boolean running;
	
	/**
	 * Constructor de Splitter.
	 * @param input 			//Archivo a separar.
	 * @param splittedFileSize	//Tamaño que tiene que tener cada archivo separado.
	 * @param fileProgressBar	//ProgressBar que lleva el progreso de cada archivo.
	 * @param totalProgressBar	//ProgressBar que lleva el progreso de cada archivo.
	 */
	public Splitter(File input, long splittedFileSize, JProgressBar totalProgressBar, String destFolder){
		this.input = input; 
		this.splitFileSize = splittedFileSize;
		this.totalProgressBar = totalProgressBar;
		this.destFolder = destFolder;
		this.running = false;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Long doInBackground() throws Exception {
		FileInputStream in = new FileInputStream(input.getPath());
		FileOutputStream out;
		
		int leido = 0; 		//Lo que se lee por vez.
		long leidoTotal = 0;	//Leido del archivo total
		int leidoParcial = 0;	//lleva la cuenta de bytes leidos, vuelve a 0 cuando se llega al max por archivo.
		cantidadArchivos = 0; 
		long maxFileProgressBar = splitFileSize;
		listaArchivos = new Vector<String>();
		running = true;
		
		DecimalFormat format = new DecimalFormat("000");
		
		byte[] buffer = new byte[1024];
		String fileName = destFolder + input.getName() + ".";
                  
		out = new FileOutputStream(fileName + format.format(cantidadArchivos+1));
		listaArchivos.add(input.getName() + "." + format.format(cantidadArchivos+1));
		while(leidoTotal < input.length() && !isCanceled){
			leido = in.read(buffer);
			out.write(buffer, 0, leido);
			
			leidoParcial += leido;
			leidoTotal += leido;
		
			publish(leidoTotal * 100 / input.length());			//Primer valor en la lista es para el total
			publish(leidoParcial * 100 / maxFileProgressBar);		//Segudo valor en la lista es para el parcial.
			
			if(leidoParcial >= splitFileSize){
				leidoParcial = 0;
				out.close();
				cantidadArchivos++;
				out = new FileOutputStream(fileName + format.format(cantidadArchivos+1));
				listaArchivos.add(input.getName() + "." + format.format(cantidadArchivos+1));
				if(input.length() - leidoTotal < splitFileSize)
					maxFileProgressBar = input.length() - leidoTotal;
			}
		}
		
		running = false;
		out.close();
		in.close();
		
		return leidoTotal;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
	protected void process(List<Long>datos){
		totalProgressBar.setValue(datos.get(0).intValue());
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override 
	public void done(){
		if(!isCanceled){
			cantidadArchivos++;
			totalProgressBar.setValue(100);
		}
	}
	
	/**
	 * Cancela el proceso de división de archivos.
	 * @param isCanceled True si cancela el proceso.
	 */
	public void cancelar(boolean isCanceled){
		this.isCanceled = isCanceled;
	}
	
	/**
	 * Devuelve la cantidad de archivos generados.
	 * @return Devuelve la cantidad de archivos generados.
	 */
	public int getCantidadArchivos(){
		return cantidadArchivos;
	}
	
	public List<String> getListaArchivos(){
		return listaArchivos;
	}
	
	public boolean isRunning(){
		return running;
	}
}
