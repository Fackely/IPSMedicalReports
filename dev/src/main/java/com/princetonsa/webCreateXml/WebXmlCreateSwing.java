/*
 * @(#)WebXmlCreateSwing.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.webCreateXml;

import javax.swing.*;          

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;

/**
 * Interfaz swing para la generacion del web.xml desde la BD
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class WebXmlCreateSwing 
{
	/**
	 * frame swing
	 */
	private JFrame frameSwing = new JFrame("SwingApplication");
	
	/**
	 * Panel contenedor
	 */
	private JPanel panelContainer = new JPanel();
	
	/**
	 * path del web.xml
	 */
	private String pathWebXml="";
	
	/**
	 * Label del path web.xml
	 */
	private JLabel pathWebXmlLabel = new JLabel("Path web.xml: ");
	
	/**
	 * log con el path del web xml
	 */
	private JLabel pathWebXmlLog= new JLabel("");
	
	/**
	 * fileChooser para tomar el path del web.xml ha parsear
	 */
	private JFileChooser xPathWebXmlFileChooser = new JFileChooser();
	
	/**
	 * boton para abrir los archivos
	 */
	JButton openButton = new JButton("Abrir");
	
	/**
	 * Label JDBCDriver
	 */
	private JLabel xJDBCDriverLabel = new JLabel("JDBC Driver: ");
	
	/**
	 * Lista los JDBC Drivers
	 */
	private JComboBox xJDBCDriver= new JComboBox();
	
	/**
	 * Label dataBaseUrl
	 */
	private JLabel xDataBaseURLLabel = new JLabel("Data Base Url: ");
	
	/**
	 * campo de lectura del url de la Base de datos
	 */
	private JTextField xDataBaseURLText= new JTextField();
	
	/**
	 * Label dataBaseUrlExample
	 */
	private JLabel xDataBaseURLLabelExample = new JLabel("Ejemplos:");
	
	/**
	 * Label dataBaseUrlExample
	 */
	private JLabel xDataBaseURLLabelExample0 = new JLabel("jdbc:postgresql://<host>:<port>/<database>");
	
	/**
	 * Label dataBaseUrlExample 1
	 */
	private JLabel xDataBaseURLLabelExample1 = new JLabel("jdbc:postgresql://localhost:5432/axioma ");
	
	/**
	 * Label dataBaseUrlExample 2
	 */
	private JLabel xDataBaseURLLabelExample2 = new JLabel("jdbc:oracle:thin:@descartes:1521:axioma");
	
	/**
	 * Label UserId
	 */
	private JLabel xUserIDLabel = new JLabel("User Id: ");
	
	/**
	 * campo de lectura del user id de la BD
	 */
	private JTextField xUserIdText= new JTextField();
	
	/**
	 * Label UserId
	 */
	private JLabel xPasswordBDLabel = new JLabel("Password: ");
	
	/**
	 * campo de lectura del password BD
	 */
	private JPasswordField xPasswordBD= new JPasswordField();
	
	/**
	 * boton para generar el xml
	 */
	private JButton button = new JButton("Grabar web.xml!");
	
	/**
	 * Label objetivo
	 */
	private JLabel objectiveLabel = new JLabel(labelPrefix);
	
	/**
	 * Info del JLabel objectiveLabel
	 */
	private static String labelPrefix = "GENERA EL WEB.XML APARTIR DE LA BD - REQUERIMIENTO: BD CONSISTENTE";
  
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (Exception e) { }
		WebXmlCreateSwing app = new WebXmlCreateSwing();
		app.createFrame();
	}
	
	/**
	 * creación del frame swing
	 */
	public void createFrame()
	{
		int widthFrame=570, heightFrame=330;
		this.createPanelContainer();
		this.frameSwing.getContentPane().add(this.panelContainer, BorderLayout.CENTER);
		this.frameSwing.addWindowListener(new WindowAdapter() 
		{
          public void windowClosing(WindowEvent e) 
          {
              System.exit(0);
          }
		});
		frameSwing.pack();
		frameSwing.setSize(widthFrame, heightFrame);
		frameSwing.setTitle("::: GENERAR WEB.XML AXIOMA :::");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    frameSwing.setLocation(	screenSize.width/2 - (widthFrame/2),
	                			screenSize.height/2 - (heightFrame/2));
		frameSwing.setVisible(true);
	}
	
	/**
	 * Metodo que arma el panelContainer
	 * @return
	 */
	public void createPanelContainer() 
	{
		int top=10, 
			left=10, 
			bottom=10, 
			right=10,
			cornerY=20;
			
		
		this.actionListenerButton();
		this.panelContainer.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		this.panelContainer.setLayout(null);
		this.openButton.addActionListener(new OpenListener());
		
		for(int w=0; w<XconnectionDB.listadoDriversVector.length; w++)
			this.xJDBCDriver.addItem(XconnectionDB.listadoDriversVector[w]);
		
		this.objectiveLabel.setBounds(40, cornerY, 550, 25);
		this.panelContainer.add(this.objectiveLabel);
		cornerY+=40;
		
		cornerY=this.addToPanelInfoWebXml(cornerY);
		cornerY=this.addToPanel(this.xJDBCDriverLabel, this.xJDBCDriver, cornerY);
		cornerY=this.addToPanel(this.xDataBaseURLLabel, this.xDataBaseURLText, cornerY);
		cornerY=this.addLabelExample(this.xDataBaseURLLabelExample, cornerY);
		cornerY=this.addLabelExample(this.xDataBaseURLLabelExample0, cornerY);
		cornerY=this.addLabelExample(this.xDataBaseURLLabelExample1, cornerY);
		cornerY=this.addLabelExample(this.xDataBaseURLLabelExample2, cornerY);
		cornerY=this.addToPanel(this.xUserIDLabel, this.xUserIdText, cornerY);
		cornerY=this.addToPanel(this.xPasswordBDLabel, this.xPasswordBD, cornerY);
		
		cornerY+=10; //espacio para el boton
		this.button.setBounds(185, cornerY, 200, 25);
		this.panelContainer.add(this.button);
	}

	/**
	 * adiciona al panel toda la informacion del web xml
	 * @param cornerY
	 * @return
	 */
	public int addToPanelInfoWebXml(int cornerY)
	{
		int cornerXLabels=20,
		heightComponent=20, 
		widthLabels=95;
		
		this.pathWebXmlLabel.setBounds(cornerXLabels, cornerY, widthLabels, heightComponent);
		this.panelContainer.add(this.pathWebXmlLabel);
		
		this.openButton.setBounds(140, cornerY, 80, heightComponent);
		this.panelContainer.add(this.openButton);
		
		this.pathWebXmlLog.setBounds(230, cornerY, 340, heightComponent);
		this.panelContainer.add(this.pathWebXmlLog);
		
		
		return cornerY+=heightComponent;
	}
	
	
	/**
	 * Adiciona los elementos al panel y retorna la nueva posicion Y
	 * @param componentLabel
	 * @param componentRead
	 * @param cornerY
	 * @return
	 */
	public int addToPanel(Component componentLabel, Component componentRead, int cornerY)
	{	
		int cornerXLabels=20,
			cornerXComponentsRead=140,
			heightComponent=20, 
			widthLabels=95,
			widthComponentsRead=400;
		
		componentLabel.setBounds(cornerXLabels, cornerY, widthLabels, heightComponent);
		this.panelContainer.add(componentLabel);
		componentRead.setBounds(cornerXComponentsRead,cornerY,widthComponentsRead,heightComponent);
		this.panelContainer.add(componentRead);
		return cornerY+=heightComponent;
	}
	
	/**
	 * adiciona los label de ejemplos
	 * @param labelExample
	 * @param cornerY
	 * @return
	 */
	public int addLabelExample(JLabel labelExample, int cornerY)
	{
		int cornerX=140,
			height=20, 
			width=400;
		
		labelExample.setBounds(cornerX, cornerY, width, height);
		this.panelContainer.add(labelExample);
		return cornerY+=height;
	}
	
	/**
	 * Invoca el flowParser cuando ocurre una accion sobre el button
	 */
	private void actionListenerButton()
	{
		button.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
		              if(flowParser())
		            	  callDialog();
		           }
				});
	}	
	
	/**
	 * listener del boton abrir
	 * @author wrios
	 *
	 */
	private class OpenListener implements ActionListener 
	{
        public void actionPerformed(ActionEvent e) 
        {
            int returnVal = xPathWebXmlFileChooser.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) 
            	pathWebXml = xPathWebXmlFileChooser.getSelectedFile().getPath();
            else
                pathWebXml="";
            pathWebXmlLog.setText(pathWebXml);
        }
    }
	
	/**
	 * flujo para hacer el parser de la BD al web.xml
	 *
	 */
	public boolean  flowParser()
	{
		String xJDBCDriver=this.xJDBCDriver.getSelectedItem().toString();
		String xDatabaseURL= this.xDataBaseURLText.getText();
		String xUser= this.xUserIdText.getText();
		String xPassword="";
		if(this.xPasswordBD.getPassword()!=null)
		{	
			for(int w=0; w<this.xPasswordBD.getPassword().length; w++)
				xPassword+=this.xPasswordBD.getPassword()[w];
		}
		
		if(pathWebXml.trim().equals(""))
			JOptionPane.showMessageDialog(this.frameSwing, "El path del web.xml es requerido");
		else if(xJDBCDriver.trim().equals(""))
			JOptionPane.showMessageDialog(this.frameSwing, "El JDBC Driver es requerido");
		else if(xDatabaseURL.trim().equals(""))
			JOptionPane.showMessageDialog(this.frameSwing, "El URL Driver es requerido");
		else if(xUser.trim().equals(""))
			JOptionPane.showMessageDialog(this.frameSwing, "El User Id es requerido");
		else if(xPassword.trim().equals(""))
			JOptionPane.showMessageDialog(this.frameSwing, "El Password es requerido");
		else
		{	
			WebXmlCreate mundoWebXmlCreate= new WebXmlCreate();
			Connection con;
			
			con=XconnectionDB.getConnectionDB(xJDBCDriver, xDatabaseURL, xUser, xPassword);
			
			if(con==null)
				JOptionPane.showMessageDialog(this.frameSwing, "No pudo abrir la conexión con la BD, por favor verifique los datos de ingreso");
			else
			{
				if(!mundoWebXmlCreate.cargarDocument(pathWebXml))
					JOptionPane.showMessageDialog(this.frameSwing, "El archivo "+pathWebXml+" no es un archivo xml válido, por favor verifique");
				else if (!mundoWebXmlCreate.llenarRolesSistema(con))
					JOptionPane.showMessageDialog(this.frameSwing, "No se cargó información de los roles de la BD, por favor verifique");
				else if(!mundoWebXmlCreate.cargarInformacionRolesFuncionalidades(con))
					JOptionPane.showMessageDialog(this.frameSwing, "No se cargó información de los roles - funcionalidades de la BD, por favor verifique");
				else
				{	
					boolean parseCorrecto=mundoWebXmlCreate.crearSegmentoRolesYRolesFuncionalidades();
					if(!parseCorrecto)
					{	
						JOptionPane.showMessageDialog(this.frameSwing, "No se creó el segmento security-rol security-constraint por una inconsistencia en codigo o BD");
						XconnectionDB.closeConnectionDB(con);
						return false;
					}
					else
					{	
						XconnectionDB.closeConnectionDB(con);
						//solo retorna true si llega hasta aca
						return true;
					}
				}	
			}	
		}
		return false;
	}

	/**
	 * llamado cuando se crea el web xml
	 *
	 */
	public void callDialog()
	{
		String pathWebXmlGenerado="";
		for(int w=0; w<(pathWebXml.split(File.separator).length-1); w++)
			pathWebXmlGenerado+=File.separator+pathWebXml.split(File.separator)[w];
		pathWebXmlGenerado+=File.separator+"webGenerado.xml";
		JOptionPane.showMessageDialog(this.frameSwing, "Web.xml creado satisfactoriamente, quedó almacenado en "+pathWebXmlGenerado);
		System.exit(0);
	}
}