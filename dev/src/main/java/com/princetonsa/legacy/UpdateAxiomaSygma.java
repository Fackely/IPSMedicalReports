package com.princetonsa.legacy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

import util.ConstantesBD;


/**
 * Esta Clase permite cargar los datos de pacientes de Sygma a Axioma a partir
 * de un archivo
 *@author dramirez <dramirez@princetonsa.com>
 */
public class UpdateAxiomaSygma {

	private static Logger logger = Logger.getLogger(UpdateAxiomaSygma.class);
	private static Connection con = null;
	private static Statement s = null;

	public UpdateAxiomaSygma() {
		try {
			Class.forName("org.postgresql.Driver");
			con =
				DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/axioma",
					"axioma",
					"");
		}
		catch (ClassNotFoundException e) {
			logger.warn("Error en la instanciacin del driver");
		}
		catch (SQLException e) {
			logger.warn("Error en la conexin");
		}
	}

	/**
	 * Constructor for UpdateAxiomaSygma.
	 */
	public UpdateAxiomaSygma(String aArch) throws Exception{
		this();
		Update u = getUpdate(aArch);
		if(u!=null)
			u.parseFile(aArch);
		else
			throw new Exception("El archivo no corresponde a ningun mdulo actualizable");
	}

	public void update(InputStream in){

		getUpdate(in);
	}
	private Update getUpdate(String aArch){
			if(aArch.equals("pacientes")){
				return new UpdatePacientes();
			}
			return null;
	}

	private void getUpdate(InputStream input){
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			String line;
			Update u = null;
			try {
				line = in.readLine();

				if(line.equals("pacientes")){
					u= new UpdatePacientes();
				}
				else if(line.startsWith("-1")){

				}	
				else if(line.startsWith("-3")){

				}	
			}
			catch (IOException e) {
				logger.warn("Error en la lectura del Stream");
			}
			if(u!=null){
				u.parseStream(in);
			}
	}	
	
	public static void adicionarQuery(String query) {



		try {
			if (s == null){			
				s = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			}
			s.addBatch(query);
		}
		catch (SQLException e) {
			logger.warn("Error en la creacin del Statement");
		}
	}

	public static void ejecutarQuery(String query) {



		try {
			if (s == null){			
				s = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			}
			s.execute(query);
		}
		catch (SQLException e) {
			logger.warn("Error en la ejecucion de la consulta  \n"+ query);
		}
	}

	public static void ejecutarQueries() {
		if (s != null) {
			try {

					s.executeBatch();
			}
			catch (SQLException e) {
				logger.warn("Error en la ejecucin de las consultas");
			 	logger.warn(e.getLocalizedMessage());
				logger.warn("Estado SQL=>" +e.getNextException().getSQLState());
			}
		}
	}
	public static void main(String[] args) {
		if (args.length != 1) {
			System.exit(0);
		}
		try {
			UpdateAxiomaSygma ua = new UpdateAxiomaSygma(args[0]);
			if (ua != null);
		}
		catch (Exception e) {
			logger.warn(e);
		}

	}
}
