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
 * Principal.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 12 de ago. de 2016, 4:05:20 p. m. 
 */

package ar.com.dcbarrientos.jsplit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Principal extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final String APP_NAME="JSplit";
	private static final String VERSION="1.0";

	private JTabbedPane tabbedPane;
	private SplitPane jSplitPane;
	private JoinPane joinPane;
	
	
	/**
	 * Constructor de Principal.
	 */
	public Principal(){
		initComponents();
	}
	
	/**
	 * Inicializa la interfaz gráfica.
	 */
	private void initComponents(){
		ResourceBundle resource = getResource();
		
		setMinimumSize(new Dimension(412, 240));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		jSplitPane = new SplitPane(this, resource);		
		tabbedPane.addTab(resource.getString("SplitPane.title"), null, jSplitPane, null);
		
		joinPane = new JoinPane(this, resource);
		tabbedPane.addTab(resource.getString("JoinPane.title"), null, joinPane, null);
		
		setTitle(getVersion());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		pack();
	}
	
	public String getVersion(){
		return APP_NAME + " " + VERSION;
	}

	private ResourceBundle getResource(){
		Locale locale = Locale.getDefault();
		ResourceBundle resource = ResourceBundle.getBundle("ar.com.dcbarrientos.jsplit.resource.JSplit", locale);
		
		return resource;
	}
}
