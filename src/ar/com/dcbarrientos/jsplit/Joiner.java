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
 * Joiner.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 16 de ago. de 2016, 10:38:46 a. m. 
 */

package ar.com.dcbarrientos.jsplit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Joiner extends SwingWorker<Void, Integer>{
	private static final int BUFFER_SIZE = 1024;
	
	private String[] filesToJoin;
	private String outputFile;
	private boolean running;
	private boolean canceled;
	private int fileCount;
	
	private JProgressBar progressBar;
	
	/**
	 * Constructor de Joiner.
	 * @param filesToJoin Lista de archivos con sus paths a unir.
	 * @param outputFile Nombre del archivo con su path que se generará.
	 * @param progressBar JProgressBar que mostrará el progreso de este proceso.
	 */
	public Joiner(String[] filesToJoin, String outputFile, JProgressBar progressBar){
		this.filesToJoin = filesToJoin;
		this.outputFile = outputFile;
		this.progressBar = progressBar;
		
		this.running = false;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {	
		running = true;
		FileOutputStream out = new FileOutputStream(new File(outputFile));
		FileInputStream input;
		byte[] buffer = new byte[BUFFER_SIZE];
		long totalSize = getTotalSize();
		long leido = 0;
		long leidoTotal = 0;
		int progress;
		fileCount = 0;
		canceled = false;
		String fileName;
		
		while(fileCount < filesToJoin.length && !canceled){
			fileName = filesToJoin[fileCount];
			input = new FileInputStream(new File(fileName));
			while((leido = input.read(buffer)) > 0 && !canceled){
				leidoTotal += leido;
				out.write(buffer);
				progress = (int)(leidoTotal * 100 / totalSize);
				publish(progress);
			}
			input.close();

			fileCount++;
		}
		
		out.close();
		
		running = false;
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
	protected void process(List<Integer> datos){
		progressBar.setValue(datos.get(0));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done(){
		if(!canceled){
			progressBar.setValue(100);
		}
	}
	
	/**
	 * Suma los tamaños de los diferentes archivos en filesToJoin para estimar
	 * el tamaño final del archivo generado
	 * @return Tamaño del archivo que se generará.
	 */
	private long getTotalSize(){
		long size = 0;
		for(String fileName: filesToJoin){
			size += new File(fileName).length();
		}
		
		return size;
	}
	
	/**
	 * Devuelve la cantidad de archivos procesados hasta el momento.
	 * @return Cantidad de archivos procesados hasta el momento.
	 */
	public int getFilesCount(){
		return fileCount;
	}
	
	/**
	 * Devuelve un valor {@link Boolean} que determina si el proceso está corriendo o no.
	 * @return True si el proceso está corriendo.
	 */
	public boolean isRunning(){
		return running;
	}

	/**
	 * Cancela el proceso de unión.
	 */
	public void cancel(){
		canceled = true;
	}
	
	/**
	 * Devuelve un valor {@link Boolean} que determina si el proceso fue cancelado.
	 * @return True si el proceso fué cancelado.
	 */
	public boolean isCanceled(){
		return canceled;
	}
}
