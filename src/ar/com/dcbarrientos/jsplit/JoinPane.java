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
 * JoinPane.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 16 de ago. de 2016, 9:14:10 a. m. 
 */

package ar.com.dcbarrientos.jsplit;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class JoinPane extends JPanel{
	private static final long serialVersionUID = 1L;

	private Principal principal;
	private ResourceBundle resource;
	private String[] filesToJoin;
	private String fileOutputName;
	Joiner join = null;
	
	private JPanel panel;
	private JButton btnJoin;
	private JButton btnCancel;
	private JLabel lblInputFile;
	private JTextField txtInputFile;
	private JButton btnSearch;
	private JLabel lblFilesList;
	private JScrollPane scrollPane;
	private JList<String> listFiles;
	private JLabel lblProgress;
	private JProgressBar progressBar;
	private JLabel lblOutput;
	private JTextField txtOutput;
	private JButton btnOutput;
	
	/**
	 * Constructor de JoinPane
	 * @param principal JFrame que lo crea.
	 * @param resource ResourceBundle que contiene los strings de la aplicación.
	 */
	public JoinPane(Principal principal, ResourceBundle resource){
		this.principal = principal;
		this.resource = resource;
		
		initComponents();
	}
	
	/**
	 * Inicializa la interfaz gráfica.
	 */
	private void initComponents(){
		setMinimumSize(new Dimension(412, 240));
		
		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		btnJoin = new JButton(resource.getString("JoinPane.btnJoin"));
		btnJoin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				join();
			}
		});
		
		btnCancel = new JButton(resource.getString("JoinPane.btnCancel"));
		btnCancel.setEnabled(false);
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				cancel();
			}
		});
		
		lblInputFile = new JLabel(resource.getString("JoinPane.lblInputFile"));
		
		txtInputFile = new JTextField();
		txtInputFile.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtInputFile.getText().length() > 0){
					setSelectedInputFile(new File(txtInputFile.getText()));
					//loadListFiles();
				}
			}
		});
		txtInputFile.setColumns(10);
		
		btnSearch = new JButton("...");
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fc = new JFileChooser();

				FileNameExtensionFilter filter = new  FileNameExtensionFilter("Split file (*.001)", "001");
				fc.addChoosableFileFilter(filter);
				fc.setFileFilter(filter);
				
				if(fc.showOpenDialog(principal) == JFileChooser.APPROVE_OPTION){
					//txtInputFile.setText(fc.getSelectedFile().getPath());
					//txtOutput.setText(fc.getSelectedFile().getParent());
					//loadListFiles();
					setSelectedInputFile(fc.getSelectedFile());
				}
			}
		});
		
		lblFilesList = new JLabel(resource.getString("JoinPane.lblFilesList"));
		
		scrollPane = new JScrollPane();
		
		lblOutput = new JLabel(resource.getString("JoinPane.lblOutput"));
		
		txtOutput = new JTextField();
		txtOutput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtOutput.getText().length() > 0 && filesToJoin != null)
					fileOutputName = txtOutput.getText() + File.separator + new File(getFileName(filesToJoin[0])).getName();
			}
		});
		txtOutput.setColumns(10);
		
		btnOutput = new JButton("...");
		btnOutput.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fc.showOpenDialog(principal) == JFileChooser.APPROVE_OPTION){
					txtOutput.setText(fc.getSelectedFile().getPath());
					if(txtOutput.getText().length() > 0 && filesToJoin != null)
						fileOutputName = txtOutput.getText() + File.separator + new File(getFileName(filesToJoin[0])).getName();
				}
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblInputFile)
								.addComponent(lblFilesList)
								.addComponent(lblOutput))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
								.addComponent(txtOutput, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
								.addComponent(txtInputFile, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnSearch)
								.addComponent(btnOutput)))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnJoin)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblInputFile)
						.addComponent(txtInputFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOutput)
						.addComponent(lblOutput))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFilesList)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnJoin))
					.addContainerGap())
		);
		
		lblProgress = new JLabel(resource.getString("JoinPane.lblProgress"));
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		
		progressBar = new JProgressBar();
		progressBar.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				lblProgress.setText(resource.getString("JoinPane.lblProgress") + " " + join.getFilesCount() + "/" + filesToJoin.length);
				if(progressBar.getValue() == 100){
					JOptionPane.showMessageDialog(null, resource.getString("JoinPane.processFinished"), resource.getString("JoinPane.title"), JOptionPane.INFORMATION_MESSAGE);
					btnCancel.setEnabled(false);
					btnJoin.setEnabled(true);
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addComponent(lblProgress, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(7)
					.addComponent(lblProgress)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		listFiles = new JList<String>();
		scrollPane.setViewportView(listFiles);
		setLayout(groupLayout);
	}
	
	/**
	 * Este proceso carga el JList con los nombres de los archivos que hay que unir.
	 * También genera un array llamado filesToJoin en el cual están los nombres de
	 * dichos archivos con sus path. Este array será pasado como parámetro a Joiner
	 * para que procese estso archivos.
	 */
	private void loadListFiles(){			
		String fileName = getFileName(txtInputFile.getText());
		DecimalFormat format = new DecimalFormat("000");
		int index = 1;
		File file = new File(txtInputFile.getText());
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		while(file.exists()){
			model.addElement(file.getName());
			index++;
			file = new File(fileName + "." + format.format(index));
		}

		filesToJoin = new String[index-1];
		model.copyInto(filesToJoin);
		for(int i = 0; i < filesToJoin.length; i++)
			filesToJoin[i] = file.getParent() + File.separator + filesToJoin[i]; 
			
		listFiles.setModel(model);
		reset();
	}
	
	/**
	 * Elimina la extensión de un archivo. En este caso, elimina la extensión .001
	 * @param fileName Nombre al cual hay que eliminarle la extensión
	 * @return Devuelve el nombre del archivo sin la extensión.
	 */
	private String getFileName(String fileName){
		int index = fileName.lastIndexOf(".");
		
		return fileName.substring(0, index);
	}
	
	/**
	 * Instancia Join y lo inicializa si no se está ejecutando. En caso contario
	 * no hace nada. Además cambia los estados de los botones Join y Cancel.
	 */
	private void join(){
		if(join == null || !join.isRunning()){
			if(validar()){
				reset();
				btnJoin.setEnabled(false);
				btnCancel.setEnabled(true);
				join = new Joiner(filesToJoin, fileOutputName, progressBar);
				join.execute();
			}
		}
	}
	
	/**
	 * Cancela el proceso de unión y cambia el estado de los botones Join y Cancel.
	 */
	private void cancel(){
		if(join.isRunning()){
			join.cancel();
			btnJoin.setEnabled(true);
			btnCancel.setEnabled(false);
		}
	}
	
	/**
	 * Verifica que los datos ingresados son válidos para poder inciar el proceso de unión.
	 * @return Verdadero si los datos son válidos.
	 */
	private boolean validar(){
		if(txtInputFile.getText().length() == 0){
			JOptionPane.showMessageDialog(null, resource.getString("JoinPane.error1"), resource.getString("JoinPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}else if(!new File(txtInputFile.getText()).exists()){
			JOptionPane.showMessageDialog(null, resource.getString("JoinPane.error2"), resource.getString("JoinPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;			
		}else if(txtOutput.getText().length() == 0){
			JOptionPane.showMessageDialog(null, resource.getString("JoinPane.error3"), resource.getString("JoinPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;			
		}else if(!new File(txtOutput.getText()).exists()){
			JOptionPane.showMessageDialog(null, resource.getString("JoinPane.error4"), resource.getString("JoinPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		
		return true;
	}
	
	/**
	 * Vuelve a su estado inicial lblProgress y progressBar. Se lo ejecuta después de que se 
	 * generó la lista de archivos a unir.
	 */
	private void reset(){
		lblProgress.setText(resource.getString("JoinPane.lblProgress") + "0/" + filesToJoin.length);
		progressBar.setValue(0);
	}
	
	private void setSelectedInputFile(File file){
		if(!file.exists()){
			JOptionPane.showMessageDialog(null, resource.getString("JoinPane.error2"), resource.getString("JoinPane.error.title"), JOptionPane.ERROR_MESSAGE);
		}else{
			txtInputFile.setText(file.getPath());
			txtOutput.setText(file.getParent());
			fileOutputName = txtOutput.getText() + File.separator + getFileName(file.getName());
			
			loadListFiles();		
		}
	}
	
}
