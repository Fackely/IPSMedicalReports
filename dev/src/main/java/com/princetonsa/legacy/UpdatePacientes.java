package com.princetonsa.legacy;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * @author dramirez
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UpdatePacientes implements Update{

	private int NUMCOL = 18;
	static final int TIPO_ID = 0;
	static final int SEXO = 1;
	static final int FECHA = 2;
	static final int CIUDAD =3;
	private Logger logger = Logger.getLogger(UpdatePacientes.class);
	/**
	 * Constructor for updatePacientes.
	 */
	public UpdatePacientes() {
		super();
	}

	private void parsePaciente(final String aPaciente) {
		StringTokenizer tok = new StringTokenizer(aPaciente, "|");
		if(tok.countTokens()==0)return; // En caso de que exista una linea vacia
		String tpaciente = null;
		String npaciente = null;
		String query = null;
		for (int i = 0; i < NUMCOL; i++) {
			if (i >= 0 && i < 14) { // Datos de la tabla de personas
				if (i == 0) {
					query =
						"insert into personas (tipo_persona,primer_apellido, segundo_apellido,"
							+ "tipo_identificacion,numero_identificacion,"
							+ "direccion,fecha_nacimiento,sexo,"
							+ "codigo_departamento_vivienda,codigo_departamento_nacimiento ,"
							+ "codigo_ciudad_vivienda, codigo_ciudad_nacimiento,"
							+ "primer_nombre,segundo_nombre,telefono,"
							+ "estado_civil,codigo_barrio_vivienda) values (1,'"
							+ tok.nextToken().trim()+ "'";
					continue;
				}
				else {
					String token = tok.nextToken().trim();
					if (i == 2) {
						token = tpaciente = sygmaToAxioma(TIPO_ID, token);
					}
					else if (i == 3) {
						npaciente = token;
					}
					else if (i == 5) {
						token = sygmaToAxioma(FECHA, token);
					}
					else if (i == 6) {
						token = sygmaToAxioma(SEXO, token);
					}
					else if(i==7){
						token = sygmaToAxioma(-1,token);
						query += ", " + token ;
					} 
					else if( i==8){ //  Ciudades
						token = sygmaToAxioma(CIUDAD,token);
						query += ", " + token ;
					}
					else{
						token = sygmaToAxioma(-1,token);
					}

					query += ", " + token ;
				}
			}
			else if (i >= 14 && i < 16) { // Datos de la tabla de pacientes
				if (i == 14) {
					query += ")";
					UpdateAxiomaSygma.ejecutarQuery(query);
					query =
						"insert into pacientes (tipo_sangre,numero_identificacion,tipo_identificacion, zona_domicilio,"
							+ "ocupacion) values (9,'"+ npaciente+ "',"+ tpaciente+ ",'"+ tok.nextToken().trim()+ "'";
				}
				else {
					String token = sygmaToAxioma(-1,tok.nextToken().trim());
					query += "," + token ;
				}
			}
			else { // Datos de historias clinicas
				if (i == 16) {
					query += ")";
					UpdateAxiomaSygma.ejecutarQuery(query);
					query =
						"insert into historias_clinicas (numero_identificacion_paciente,tipo_identificacion_paciente,"
							+ "historia_clinica_anterior,fecha_apertura) "
							+ "values ( '"+ npaciente+ "',"+ tpaciente+ ",'"+ tok.nextToken().trim()+ "'";
				}
				else {
					String token = tok.nextToken().trim();
					if (i == 17) {
						token = sygmaToAxioma(FECHA, token);
					}
					else{
						token = sygmaToAxioma(-1,tok.nextToken().trim());
					}
					query += ", " + token ;
				}
			}
		}
		query += ")";
		UpdateAxiomaSygma.ejecutarQuery(query);
		query =
			"insert into ingresos(id,numero_identificacion_paciente,tipo_identificacion_paciente,institucion) values "
				+ "(nextval('seq_ingresos'),'"+ npaciente+ "',"+ tpaciente+ ",2)";

		UpdateAxiomaSygma.ejecutarQuery(query);

		query =
			"insert into pacientes_instituciones (numero_identificacion_paciente,tipo_identificacion_paciente,codigo_institucion) values "
				+ "('"+ npaciente+ "',"	+ tpaciente+ ",2)";

		UpdateAxiomaSygma.ejecutarQuery(query);

		query =
			"insert into pacientes_instituciones2 (numero_identificacion_paciente,tipo_identificacion_paciente,codigo_institucion_duena,codigo_institucion_permitida) values "
				+ "('"+ npaciente+ "',"+ tpaciente+ ",2,2)";

		UpdateAxiomaSygma.ejecutarQuery(query);

//		UpdateAxiomaSygma.ejecutarQueries(); En el caso de que s quiera hacer ejecucion de consultas en batch

	}

	public String sygmaToAxioma(int key, String value) {

		switch (key) {
			case TIPO_ID :
				if (value.equals("0")) {
					return "'CC'";
				}
				else if (value.equals("1")) {
					return "'TI'";
				}
				else if (value.equals("2")) {
					return "'NU'";
				}
				else if (value.equals("3")) {
					return "'CE'";
				}
				else if (value.equals("4")) {
					return "'PA'";
				}
				else if (value.equals("7")) {
					return "'AS'";
				}
				else if (value.equals("8")) {
					return "'RC'";
				}
				else if (value.equals("9")) {
					return "'MS'";
				}

				break;

			case SEXO :

				if (value.equals("M")) {
					return "1";
				}
				else if (value.equals("F")) {
					return "2";
				}

				break;

			case FECHA :
				return "to_date('" + value + "','DD/MM/YYYY')";
			case CIUDAD:
				return parseCiudad(value);
			
			default:
				return " '"+ value +"'";
		}

		return value;
	}
	
	public String parseCiudad(String aValue){
		int v = Integer.parseInt(aValue);
		if(v >=1 && v <=21){
			return "21";
		}
		else
			return aValue;
	}
	
	public void parseStream(BufferedReader in){
		try {
			String line = in.readLine();
			while (line != null) {
				parsePaciente(line);
				line = in.readLine();
			}
		}
		catch (FileNotFoundException e) {
			logger.warn("No se pudo cargar el archivo");
		}
		catch (IOException e) {
			logger.warn("Error en la lectura del paciente");
		}
	}
	/**
	 * @see Update#parseStream(java.io.InputStream)
	 */
	public void parseStream(InputStream input) {
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			parseStream(in);
	}
	/**
	 * @see Update#parseFile(java.lang.String)
	 */
	public void parseFile(String arch) {
			FileInputStream fin;
			try {
				fin = new FileInputStream(arch);
				parseStream(fin);		
			}
			catch (FileNotFoundException e) {
			}
	}

}
