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
 * SplitPane.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 12 de ago. de 2016, 4:07:48 p. m. 
 */

package ar.com.dcbarrientos.jsplit;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class SplitPane extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel lblInputFile;
	private JTextField txtInputFile;
	private JLabel lblOutputFile;
	private JTextField txtOutput;
	private JTextField txtDividerSize;
	private JLabel lblSize;
	private JComboBox<String> cbUnidad;
	private JButton btnSearchInput;
	private JButton btnSearchOutput;
	private JButton btnCancel;
	private JButton btnSplit;
	private JLabel lblInputFileSize;
	private JLabel lblInputFileSizeDesc;
	private JLabel lblPossibleNumberOf;
	private JLabel lblCantidadArchivos;

	private Principal principal;
	private File input;
	private int cantidadArchivos;
	private Splitter task;
	private long splittedFileSize;
	String destFolder;
	
	private JPanel panel;
	private JLabel lblFilesProgress;
	//private JLabel lblTotalProgress;
	private JProgressBar pbTotalProgress;
	private JCheckBox chckbxCreateBatchFile;
	private ResourceBundle resource;
	
	/**
	 * Constructor de SplitPane 
	 * @param principal Padre de este panel.
	 */
	public SplitPane(Principal principal, ResourceBundle resource){
		this.principal = principal;
		this.resource = resource;
		
		initComponents();
	}
	
	/**
	 * Inicializa la interfaz gráfica.
	 */
	private void initComponents(){
		
		
		lblInputFile = new JLabel(resource.getString("SplitPane.lblInputFile"));
		
		txtInputFile = new JTextField();
		txtInputFile.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				inputSelected(new File(txtInputFile.getText()));
			}
		});
		txtInputFile.setColumns(10);
		
		lblOutputFile = new JLabel(resource.getString("SplitPane.lblOutputFile"));
		
		txtOutput = new JTextField();
		txtOutput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				destFolder = txtOutput.getText();
			}
		});
		txtOutput.setColumns(10);
		
		txtDividerSize = new JTextField();
		txtDividerSize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcular();
			}
		});
		txtDividerSize.setColumns(10);
		
		lblSize = new JLabel(resource.getString("SplitPane.lblSize"));
		
		cbUnidad = new JComboBox<String>();
		cbUnidad.setModel(new DefaultComboBoxModel<String>(new String[] {"Bytes", "Kb.", "Mb.", "Gb."}));
		cbUnidad.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				calcular();
			}
		});
		
		btnSearchInput = new JButton("...");
		btnSearchInput.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(fc.showOpenDialog(principal) == JFileChooser.APPROVE_OPTION){
					inputSelected(fc.getSelectedFile());
				}				
			}
		});
		
		btnSearchOutput = new JButton("...");
		btnSearchOutput.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fc.showOpenDialog(principal) == JFileChooser.APPROVE_OPTION)
					txtOutput.setText(fc.getSelectedFile().getPath());
			}
		});
		
		btnCancel = new JButton(resource.getString("SplitPane.btnCancel"));
		btnCancel.setEnabled(false);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cancelar();
			}
		});
		
		btnSplit = new JButton(resource.getString("SplitPane.btnSplit"));
		btnSplit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				split();
			}
		});
		
		lblInputFileSize = new JLabel(resource.getString("SplitPane.lblInputFileSize"));
		
		lblInputFileSizeDesc = new JLabel("");
		
		lblPossibleNumberOf = new JLabel(resource.getString("SplitPane.lblPossibleNumberOf"));
		
		lblCantidadArchivos = new JLabel("");
		
		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		chckbxCreateBatchFile = new JCheckBox(resource.getString("SplitPane.chckbxCreateBatchFile"));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblOutputFile)
								.addComponent(lblSize)
								.addComponent(lblInputFile))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(txtDividerSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbUnidad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(txtOutput, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
										.addComponent(txtInputFile, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnSearchOutput)
										.addComponent(btnSearchInput)))))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(chckbxCreateBatchFile)
							.addPreferredGap(ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblInputFileSize)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblInputFileSizeDesc))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblPossibleNumberOf)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblCantidadArchivos))))
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnSplit)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblInputFile)
						.addComponent(txtInputFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchInput))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOutputFile)
						.addComponent(txtOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchOutput))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSize)
						.addComponent(txtDividerSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbUnidad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblInputFileSize)
								.addComponent(lblInputFileSizeDesc))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPossibleNumberOf)
								.addComponent(lblCantidadArchivos)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(11)
							.addComponent(chckbxCreateBatchFile)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnSplit))
					.addContainerGap(45, Short.MAX_VALUE))
		);
		
		lblFilesProgress = new JLabel(resource.getString("SplitPane.lblFilesProgress"));
		lblFilesProgress.setHorizontalAlignment(SwingConstants.CENTER);
		
		pbTotalProgress = new JProgressBar();
		pbTotalProgress.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				actualizarFileProgress();
				if(pbTotalProgress.getValue() == 100){
					if(chckbxCreateBatchFile.isSelected())
						crearBatch();
					btnCancel.setEnabled(false);
					JOptionPane.showMessageDialog(null, resource.getString("SplitPane.processFinished"), resource.getString("Principal.TabSplit.title"), JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFilesProgress, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
						.addComponent(pbTotalProgress, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblFilesProgress)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pbTotalProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
	}
	
	private void inputSelected(File inputFile){
		input = inputFile;
		lblInputFileSizeDesc.setText(Constantes.getExtendedSize(input.length()));
		txtInputFile.setText(inputFile.getPath());
		
		if(txtOutput.getText().length() == 0){
			txtOutput.setText(input.getParent());
			destFolder = txtOutput.getText();
		}
		
		calcular();		
	}
	
	/**
	 * Calcula la cnatidad de archivos generados y actualiza las JProgressBar.
	 */
	private void calcular(){
		if(txtInputFile.getText().length() > 0 && txtDividerSize.getText().length() > 0){
			cantidadArchivos = Constantes.fileSizeDivider(input.length(), Constantes.BYTES, Long.parseLong(txtDividerSize.getText()), cbUnidad.getSelectedIndex()); 
			lblCantidadArchivos.setText(Integer.toString(cantidadArchivos, 10));
			splittedFileSize = Long.parseLong(txtDividerSize.getText());
			
			actualizarFileProgress();
		}
	}
	
	/**
	 * Actualiza la etiqueta que muestra la cantidad de archvios generados.
	 */
	private void actualizarFileProgress(){
		if(task == null)	
			lblFilesProgress.setText(resource.getString("SplitPane.lblFilesProgress") + " 0/" + cantidadArchivos);
		else
			lblFilesProgress.setText(resource.getString("SplitPane.lblFilesProgress") + task.getCantidadArchivos() + "/" + cantidadArchivos);
			
	}
	
	/**
	 * Cancela el proceso.
	 */
	private void cancelar(){
		task.cancelar(true);
		btnCancel.setEnabled(false);
	}
	
	/**
	 * Inicia el proceso de división de archivos.
	 */
	private void split(){
		if(task == null || !task.isRunning()){
			if(validar()){
				actualizarFileProgress();
			
				destFolder = txtOutput.getText().endsWith(File.separator)?txtOutput.getText():txtOutput.getText()+File.separator;
				task = new Splitter(input, Constantes.toBytes(splittedFileSize, cbUnidad.getSelectedIndex()), pbTotalProgress, destFolder);
	
				task.execute();
				btnCancel.setEnabled(true);
			}
		}
	}
	
	private boolean validar(){
		if(input == null){
			JOptionPane.showMessageDialog(null, resource.getString("SplitPane.error1"), resource.getString("SplitPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}else if(!input.exists()){
			JOptionPane.showMessageDialog(null, resource.getString("SplitPane.error2"), resource.getString("SplitPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}else if(destFolder == null || destFolder.length() == 0){
			JOptionPane.showMessageDialog(null, resource.getString("SplitPane.error3"), resource.getString("SplitPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;									
		}else if(!new File(destFolder).exists()){
			JOptionPane.showMessageDialog(null, resource.getString("SplitPane.error4"), resource.getString("SplitPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;						
		}else if(txtDividerSize.getText().length() == 0){
			JOptionPane.showMessageDialog(null, resource.getString("SplitPane.error5"), resource.getString("SplitPane.error.title"), JOptionPane.ERROR_MESSAGE);
			return false;									
		}
			
		return true;
	}
	
	/**
	 * Crea el batch que permitirá unir los archivos separados.
	 */
	private void crearBatch(){
		String linea = "";
		FileWriter writer = null;
		
		if(Constantes.isWindows()){
			try {
				writer = new FileWriter(destFolder + "join.bat");
				linea = "copy /b ";
				writer.write(linea);
				
				int i = 1;
				try{
					for(String nombre: task.getListaArchivos()){
						writer.write(nombre);
						if(i < task.getListaArchivos().size())
							writer.write("+");
						else
							writer.write(" ");
						i++;
					}
					writer.write(input.getName());
					writer.close();
				}catch(IOException e){
					e.printStackTrace();
					return;
				}

			} catch (IOException e) {
				e.printStackTrace();
				return;
			}			
		}else if(Constantes.isLinux()){
			try {
				writer = new FileWriter(destFolder + "join.sh");
				writer.write("#!/bin/bash\n");
				writer.write("cat " + input.getName() + ".* >" + input.getName());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}			
		}
		
	}
	
}
