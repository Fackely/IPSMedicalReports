package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseFichaInfeccionesDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaInfeccionesDao.class);
    
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichainfecciones "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaInfecciones," +
    														"codigoPaciente,"+
    														"estado,"+
    														"acronimo,"+
    														"fechaDiligenciamiento," +
    														"horaDiligenciamiento,"+
    														"codigomunprocedencia," +
    														"codigodepprocedencia," +
    														"codigomunnoti," +
    														"codigodepnoti," +
    														"codigoaseguradora," +
    														"nombreprofesionaldiligencio" +
    														")"+
    														" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?)";
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichainfecciones "+
														    "(" +
															"loginUsuario,"+
															"codigoFichaInfecciones," +
															"codigoPaciente,"+
															"estado,"+
															"acronimo,"+
															"fechaDiligenciamiento," +
															"horaDiligenciamiento,"+
															"codigomunprocedencia," +
															"codigodepprocedencia," +
															"codigomunnoti," +
															"codigodepnoti," +
															"codigoaseguradora," +
															"nombreprofesionaldiligencio," +
															
															"numeroCama, " +
														    "servicio, " +
														    "fechaIngreso, " +
														    "fechaDxIh, " +
														    "fechaEgreso, " +
														    "fechaDxHisto, " +
														    "dxHospital, " +
														    "dxIh, " +
														    "dxEgreso, " +
														    "dxHisto, " +
														    "generoMicro, " +
														    "especieMicro, " +
														    "bioTipoMicro, " +
														    "tipoMuestra1, " +
														    "locAnatomica1, " +
														    "fechaToma1, " +
														    "fechaRemision1, " +
														    "mIdentificacion1, " +
														    "pruAdicionales1, " +
														    "tipoMuestra2, " +
														    "locAnatomica2, " +
														    "fechaToma2, " +
														    "fechaRemision2, " +
														    "mIdentificacion2, " +
														    "pruAdicionales2, " +
														    "antibiotico1, " +
														    "sensibilidad1, " +
														    "tDosis1, " +
														    "fechaInicioAntibiotico1, " +
														    "fechaFinAntibiotico1, " +
														    "antibiotico2, " +
														    "sensibilidad2, " +
														    "tDosis2, " +
														    "fechaInicioAntibiotico2, " +
														    "fechaFinAntibiotico2, " +
														    "antibiotico3, " +
														    "sensibilidad3, " +
														    "tDosis3, " +
														    "fechaInicioAntibiotico3, " +
														    "fechaFinAntibiotico3, " +
														    "clasificacionCaso," +
														    "localizacionAnatomica, " +
														    "institucionAtendio," +
														    "medicos, " +
														    "activa, " +
														    "servicio2 " +
														") " +
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",21,11,21,11,?,? " +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?" +
														") ";
    
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigiFichaInfecciones " +
														"SET " +
														"estado=?, " +
														"numeroCama=?, " +
													    "servicio=?, " +
													    "fechaIngreso=?, " +
													    "fechaDxIh=?, " +
													    "fechaEgreso=?, " +
													    "fechaDxHisto=?, " +
													    "dxHospital=?, " +
													    "dxIh=?, " +
													    "dxEgreso=?, " +
													    "dxHisto=?, " +
													    "generoMicro=?, " +
													    "especieMicro=?, " +
													    "bioTipoMicro=?, " +
													    "tipoMuestra1=?, " +
													    "locAnatomica1=?, " +
													    "fechaToma1=?, " +
													    "fechaRemision1=?, " +
													    "mIdentificacion1=?, " +
													    "pruAdicionales1=?, " +
													    "tipoMuestra2=?, " +
													    "locAnatomica2=?, " +
													    "fechaToma2=?, " +
													    "fechaRemision2=?, " +
													    "mIdentificacion2=?, " +
													    "pruAdicionales2=?, " +
													    "antibiotico1=?, " +
													    "sensibilidad1=?, " +
													    "tDosis1=?, " +
													    "fechaInicioAntibiotico1=?, " +
													    "fechaFinAntibiotico1=?, " +
													    "antibiotico2=?, " +
													    "sensibilidad2=?, " +
													    "tDosis2=?, " +
													    "fechaInicioAntibiotico2=?, " +
													    "fechaFinAntibiotico2=?, " +
													    "antibiotico3=?, " +
													    "sensibilidad3=?, " +
													    "tDosis3=?, " +
													    "fechaInicioAntibiotico3=?, " +
													    "fechaFinAntibiotico3=?, " +
													    "clasificacionCaso=?," +
													    "localizacionAnatomica=?, " +
													    "institucionAtendio=?, " +
													    "medicos=?, " +
													    "servicio2=? " +
												"WHERE codigoFichaInfecciones=? ";
    
    
    
    public static final String consultarFichaStr = "SELECT " +
																	"ficha.estado," +
																	"ficha.numeroCama, " +
																    "ficha.servicio, " +
																    "ficha.fechaIngreso, " +
																    "ficha.fechaDxIh, " +
																    "ficha.fechaEgreso, " +
																    "ficha.fechaDxHisto, " +
																    "ficha.dxHospital, " +
																    "ficha.dxIh, " +
																    "ficha.dxEgreso, " +
																    "ficha.dxHisto, " +
																    "ficha.localizacionAnatomica, " +
																    "ficha.generoMicro, " +
																    "ficha.especieMicro, " +
																    "ficha.bioTipoMicro, " +
																    "ficha.tipoMuestra1, " +
																    "ficha.locAnatomica1, " +
																    "ficha.fechaToma1, " +
																    "ficha.fechaRemision1, " +
																    "ficha.mIdentificacion1, " +
																    "ficha.pruAdicionales1, " +
																    "ficha.tipoMuestra2, " +
																    "ficha.locAnatomica2, " +
																    "ficha.fechaToma2, " +
																    "ficha.fechaRemision2, " +
																    "ficha.mIdentificacion2, " +
																    "ficha.pruAdicionales2, " +
																    "ficha.antibiotico1, " +
																    "ficha.sensibilidad1, " +
																    "ficha.tDosis1, " +
																    "ficha.fechaInicioAntibiotico1, " +
																    "ficha.fechaFinAntibiotico1, " +
																    "ficha.antibiotico2, " +
																    "ficha.sensibilidad2, " +
																    "ficha.tDosis2, " +
																    "ficha.fechaInicioAntibiotico2, " +
																    "ficha.fechaFinAntibiotico2, " +
																    "ficha.antibiotico3, " +
																    "ficha.sensibilidad3, " +
																    "ficha.tDosis3, " +
																    "ficha.fechaInicioAntibiotico3, " +
																    "ficha.fechaFinAntibiotico3, " +
																    "ficha.clasificacionCaso, " +
																    "ficha.medicos, " +
																    "ficha.servicio2, " +
																	
																	"ficha.institucionAtendio AS nombreUnidad, " +
																	"ficha.nombreprofesionaldiligencio AS nombreProfesional, " +
																	"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, " +
																	
																	"per.primer_nombre," +
																	"per.segundo_nombre," +
																	"per.primer_apellido," +
																	"per.segundo_apellido," +
																	"dep.descripcion AS dep_nacimiento," +
																	"ciu.descripcion AS ciu_nacimiento," +
																	"dep2.descripcion AS dep_vivienda," +
																	"ciu2.descripcion AS ciu_vivienda," +
																	"per.direccion AS direccion_paciente," +
																	"per.telefono AS telefono_paciente," +
																	"per.fecha_nacimiento," +
																	"per.sexo," +
																	"per.estado_civil," +
																	"per.numero_identificacion, " +
																	"per.codigo_pais_nacimiento, " +
																	"per.codigo_pais_vivienda, " +
																	"per.codigo_pais_id, " +
																	
																	"bar.descripcion AS barrio, " +
																	"pac.zona_domicilio AS zonaDomicilio, " +
																	"ocup.nombre AS ocupacionPaciente, " +
																	"per.tipo_identificacion AS tipoId, " +
																	"conv.nombre AS aseguradora, " +
																	"regs.nombre AS regimenSalud, " +
																	"pac.etnia AS etnia, " +
																	"pac.esta_vivo AS estaVivo, " +
																	"ficha.desplazado AS desplazado " +
																"FROM " +
																	"epidemiologia.vigifichainfecciones ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaInfecciones = ? " +
																	
																	"AND per.codigo=ficha.codigoPaciente " +
																	"AND dep.codigo_departamento=per.codigo_departamento_nacimiento " +
																	"AND dep.codigo_pais=per.codigo_pais_nacimiento " +
																	"AND ciu.codigo_ciudad=per.codigo_ciudad_nacimiento " +
																	"AND ciu.codigo_departamento=per.codigo_departamento_nacimiento " +
																	"AND ciu.codigo_pais=per.codigo_pais_nacimiento " +
																	"AND dep2.codigo_departamento=per.codigo_departamento_vivienda " +
																	"AND dep2.codigo_pais=per.codigo_pais_vivienda " +
																	"AND ciu2.codigo_ciudad=per.codigo_ciudad_vivienda " +
																	"AND ciu2.codigo_departamento=per.codigo_departamento_vivienda " +
																	"AND ciu2.codigo_pais=per.codigo_pais_vivienda " +
																	
																	"AND ficha.loginUsuario=usu.login " +
																	"AND usu.codigo_persona=per2.codigo "+
																	"AND per.codigo_barrio_vivienda=bar.codigo " +
																	"AND per.codigo=pac.codigo_paciente " +
																	"AND pac.codigo_paciente=ficha.codigoPaciente " +
																	"AND pac.ocupacion=ocup.codigo "+
																	"AND conv.codigo=ficha.codigoAseguradora " +
																	"AND conv.tipo_regimen=regs.acronimo ";
    
    
    private static final String insertarFactorRiesgo = "INSERT INTO epidemiologia.vigiDetalleFacInfeccion(codigoFactor,codigofichainfecciones) VALUES(?,?)";
    
    
    private static final String eliminarFactoresRiesgoStr = "DELETE from epidemiologia.vigiDetalleFacInfeccion WHERE codigoFichaInfecciones = ?";
    
    
    
    public static final String consultarFactoresRiesgoStr = "SELECT " +
																		"codigofactor " +
																	"FROM " +
																		"epidemiologia.vigiDetalleFacInfeccion " +
																	"WHERE " +
																		"codigoFichaInfecciones = ? ";
    
    
    public static final String consultaDatosPacienteStr = "SELECT " +
														    "per.primer_nombre," +
															"per.segundo_nombre," +
															"per.primer_apellido," +
															"per.segundo_apellido," +
															"dep.descripcion AS dep_vivienda," +
															"ciu.descripcion AS ciu_vivienda," +
															"per.direccion AS direccion_paciente," +
															"per.telefono AS telefono_paciente," +
															"per.fecha_nacimiento," +
															"per.sexo," +
															"per.estado_civil," +
															"per.numero_identificacion, " +
															"per.codigo_pais_nacimiento, " +
															"per.codigo_pais_vivienda, " +
															"per.codigo_pais_id, " +
															
															"bar.descripcion AS barrio, " +
															"pac.zona_domicilio AS zonaDomicilio, " +
															"ocup.nombre AS ocupacionPaciente, " +
															"per.tipo_identificacion AS tipoId, " +
															"conv.nombre AS aseguradora, " +
															"regs.nombre AS regimenSalud, " +
															"pac.etnia AS etnia " +
															"FROM personas per " +
															"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_vivienda and dep.codigo_pais=per.codigo_pais_vivienda)  " +
															"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_vivienda and ciu.codigo_departamento=per.codigo_departamento_vivienda AND ciu.codigo_pais=per.codigo_pais_vivienda)  " +
															"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda)  " +
															"INNER JOIN pacientes pac on(pac.codigo_paciente=per.codigo) " +
															"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion)  " +
															"INNER JOIN cuentas c ON(c.codigo_paciente = pac.codigo_paciente) " +
															"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) " +
															"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) " +
															"INNER JOIN tipos_regimen regs ON(regs.acronimo = conv.tipo_regimen) " +
														"WHERE " +
															"per.codigo = ? " ;
    
    
    
    public static final String consultaDatosPacienteStr2 = "SELECT " +
															    "per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.descripcion AS dep_vivienda," +
																"ciu.descripcion AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																
																"bar.descripcion AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"pac.etnia AS etnia " +
															"FROM " +
																"personas per, departamentos dep, ciudades ciu, " +
																"barrios bar, pacientes pac, ocupaciones ocup " +
															"WHERE " +
																"per.codigo = ? " +
																"AND dep.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND dep.codigo_pais=per.codigo_pais_vivienda " +
																"AND ciu.codigo_ciudad=per.codigo_ciudad_vivienda " +
																"AND ciu.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND ciu.codigo_pais=per.codigo_pais_vivienda " +
																
																"AND per.codigo_barrio_vivienda=bar.codigo " +
																"AND per.codigo=pac.codigo_paciente " +
																"AND pac.ocupacion=ocup.codigo ";
    
    
    public static final String insertarFactorStr = "INSERT INTO epidemiologia.vigiFactoresInfecciones (" +
    													"codigoFicha," +
    													"factor," +
    													"clasificacion " +
    												") " +
    												"VALUES (?,?,?)";
    
    
    public static final String modificarFactorStr = "UPDATE epidemiologia.vigiFactoresInfecciones SET " +
    													"factor=?, " +
    													"clasificacion=? " +
    												"WHERE codigoficha=? AND factor=? ";
    
    
    
    public static final String eliminarFactorStr = "DELETE from epidemiologia.vigiFactoresInfecciones WHERE codigoficha=? ";
    
    
    
    public static final String consultarFactorStr = "SELECT factor, clasificacion FROM epidemiologia.vigiFactoresInfecciones WHERE codigoFicha = ?";
    
    
    
    
    public static final String insertarMicroStr = "INSERT INTO epidemiologia.vigiMicroorganismos (" +
    													"codigo," +
    													"codigoficha," +
    													"codigomicro," +
    													"especie," +
    													"biotipo" +
    												") " +
    												"VALUES (?,?,?,?,?)";
    
    
    
    public static final String modificarMicroStr = "UPDATE epidemiologia.vigiMicroorganismos SET " +
    													"codigomicro=?, " +
    													"especie=?, " +
    													"biotipo=? " +
    												"WHERE codigo = ?";
    
    
    
    public static final String eliminarMicroStr = "DELETE FROM epidemiologia.vigiMicroorganismos WHERE codigo=?";
    
    
    public static final String consultarMicroStr = "SELECT " +
    													"codigo," +
    													"codigomicro," +
    													"especie," +
    													"biotipo " +
    												"FROM " +
    													"epidemiologia.vigiMicroorganismos " +
    												"WHERE " +
    													"codigoFicha = ?";
    
    
    
    public static int insertarFicha(Connection con,
									int numeroSolicitud,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									int codigoAseguradora,
									String nombreProfesional,
								    String secuencia) 
	{
		int resultado=0;
		int codigo;
		
		try {
		
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				codigo = rs.getInt(1);
			}
			else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
			
			PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			// Inserción de los datos de la ficha
			
			insertarFicha.setInt(1,numeroSolicitud);
			insertarFicha.setString(2,login);
			insertarFicha.setInt(3,codigo);
			insertarFicha.setInt(4,codigoPaciente);
			insertarFicha.setInt(5,estado);
			insertarFicha.setString(6,codigoDiagnostico);
			insertarFicha.setInt(7,codigoAseguradora);
			insertarFicha.setString(8,nombreProfesional);
			
			resultado = insertarFicha.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			else {
			
				resultado = codigo;
			}
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaInfeccionesDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
	}
    
    
    
    public static int insertarFichaCompleta(Connection con,
											int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    String secuencia,
										    
										    int numeroCama,
										    int servicio,
										    String fechaIngreso,
										    String fechaDxIh,
										    String fechaEgreso,
										    String fechaDxHisto,
										    String dxHospital,
										    String dxIh,
										    String dxEgreso,
										    String dxHisto,
										    int localizacionAnatomica,
										    int generoMicro,
										    String especieMicro,
										    String bioTipoMicro,
										    int tipoMuestra1,
										    int locAnatomica1,
										    String fechaToma1,
										    String fechaRemision1,
										    String mIdentificacion1,
										    String pruAdicionales1,
										    int tipoMuestra2,
										    int locAnatomica2,
										    String fechaToma2,
										    String fechaRemision2,
										    String mIdentificacion2,
										    String pruAdicionales2,
										    int antibiotico1,
										    String sensibilidad1,
										    String tDosis1,
										    String fechaInicioAntibiotico1,
										    String fechaFinAntibiotico1,
										    int antibiotico2,
										    String sensibilidad2,
										    String tDosis2,
										    String fechaInicioAntibiotico2,
										    String fechaFinAntibiotico2,
										    int antibiotico3,
										    String sensibilidad3,
										    String tDosis3,
										    String fechaInicioAntibiotico3,
										    String fechaFinAntibiotico3,
										    int clasificacionCaso,
										    HashMap factoresRiesgo,
										    int estadoAnterior,
										    int unidadGeneradora,
										    String medicosTratantes,
										    boolean activa,
										    String servicio2,
										    HashMap microorganismos,
										    String secuenciaMicro
										   )
    {
    	int resultado=0;
        int codigo;
        
    	try {
    		
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
            
            PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaCompletaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
            // Inserción de los datos de la ficha
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,login,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigo),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoPaciente),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estado),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDiagnostico,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoAseguradora),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreProfesional,7,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroCama),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(servicio),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaIngreso,10,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDxIh,11,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaEgreso,12,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDxHisto,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,dxHospital,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,dxIh,15,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,dxEgreso,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,dxHisto,17,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(generoMicro),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,especieMicro,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,bioTipoMicro,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoMuestra1),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(locAnatomica1),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaToma1,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaRemision1,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,mIdentificacion1,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pruAdicionales1,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoMuestra2),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(locAnatomica2),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaToma2,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaRemision2,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,mIdentificacion2,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pruAdicionales2,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antibiotico1),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,sensibilidad1,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,tDosis1,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioAntibiotico1,36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaFinAntibiotico1,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antibiotico2),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,sensibilidad2,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,tDosis2,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioAntibiotico2,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaFinAntibiotico2,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antibiotico3),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,sensibilidad3,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,tDosis3,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioAntibiotico3,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaFinAntibiotico3,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(clasificacionCaso),48,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(localizacionAnatomica),49,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),50,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,medicosTratantes,51,Types.VARCHAR,true,false);
                        
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),52,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,servicio2,53,Types.VARCHAR,true,false);
            
            resultado = insertarFicha.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            } 
            /*
            try {
	            for (int i=1;i<factoresRiesgo.size()+1;i++) {
			    	
			    	String val = factoresRiesgo.get("factor_"+i).toString();
			    	String clasi = factoresRiesgo.get("clasificacion_"+i).toString();
			    	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarFactor =  new PreparedStatementDecorator(con.prepareStatement(insertarFactorRiesgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarFactor.setInt(1,i);
				    	insertarFactor.setInt(2,codigo);
				    	
				    	resultado = insertarFactor.executeUpdate();
				    	
				    	if(resultado<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
			    }
            }
		    catch (NullPointerException npe) {}
            
		    if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            } 
		    */
            
            
            int tamFactores = factoresRiesgo.size()/2;
            
            for (int i=0;i<tamFactores;i++) {
            	
            	String factor = factoresRiesgo.get("factor_"+i).toString();
            	String clasificacion = factoresRiesgo.get("clasificacion_"+i).toString();
            	
            	PreparedStatementDecorator insertarFactor =  new PreparedStatementDecorator(con.prepareStatement(insertarFactorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            	
            	UtilidadBD.ingresarDatoAStatement(insertarFactor,Integer.toString(codigo),1,Types.INTEGER,true,false);
            	UtilidadBD.ingresarDatoAStatement(insertarFactor,factor,2,Types.INTEGER,true,false);
            	UtilidadBD.ingresarDatoAStatement(insertarFactor, clasificacion, 3, Types.VARCHAR, true, true);
            	
            	resultado = insertarFactor.executeUpdate();
            	
            	if(resultado<1)
	            {
	                daoFactory.abortTransaction(con);
	                return -1; // Estado de error
	            }
            }
            
		    
		    int tamMicroorganismos = microorganismos.size()/3;
		    
		    for (int i=0;i<tamMicroorganismos;i++) {
		    	
		    	String micro = microorganismos.get("micro_"+i).toString();
		    	String especie = microorganismos.get("especie_"+i).toString();
		    	String biotipo = microorganismos.get("biotipo_"+i).toString();
		    	int codigoMicro;
		    	
		    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaMicro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ResultSet rs2 = statement.executeQuery();
	            
	            if (rs2.next()) {
	                codigoMicro = rs2.getInt(1);
	            }
	            else {
					logger.error("Error obteniendo el código de la secuencia de microorganismos ");
					return 0;
				}
	            
	            PreparedStatementDecorator insertarMicro =  new PreparedStatementDecorator(con.prepareStatement(insertarMicroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            UtilidadBD.ingresarDatoAStatement(insertarMicro,Integer.toString(codigoMicro),1,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarMicro,Integer.toString(codigo),2,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarMicro, micro, 3, Types.INTEGER, true, false);
	            UtilidadBD.ingresarDatoAStatement(insertarMicro, especie, 4, Types.VARCHAR, true, false);
	            UtilidadBD.ingresarDatoAStatement(insertarMicro, biotipo, 5, Types.VARCHAR, true, false);
	            
	            resultado = insertarMicro.executeUpdate();
	            
	            if(resultado<1)
	            {
	                daoFactory.abortTransaction(con);
	                return -1; // Estado de error
	            }
		    }
		    
		    
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }   
            else {
			    
			    resultado = codigo;
			}
            
            daoFactory.endTransaction(con);
            
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaInfeccionesDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaInfecciones,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
									    int estado,
									    
									    int numeroCama,
									    int servicio,
									    String fechaIngreso,
									    String fechaDxIh,
									    String fechaEgreso,
									    String fechaDxHisto,
									    String dxHospital,
									    String dxIh,
									    String dxEgreso,
									    String dxHisto,
									    int localizacionAnatomica,
									    int generoMicro,
									    String especieMicro,
									    String bioTipoMicro,
									    int tipoMuestra1,
									    int locAnatomica1,
									    String fechaToma1,
									    String fechaRemision1,
									    String mIdentificacion1,
									    String pruAdicionales1,
									    int tipoMuestra2,
									    int locAnatomica2,
									    String fechaToma2,
									    String fechaRemision2,
									    String mIdentificacion2,
									    String pruAdicionales2,
									    int antibiotico1,
									    String sensibilidad1,
									    String tDosis1,
									    String fechaInicioAntibiotico1,
									    String fechaFinAntibiotico1,
									    int antibiotico2,
									    String sensibilidad2,
									    String tDosis2,
									    String fechaInicioAntibiotico2,
									    String fechaFinAntibiotico2,
									    int antibiotico3,
									    String sensibilidad3,
									    String tDosis3,
									    String fechaInicioAntibiotico3,
									    String fechaFinAntibiotico3,
									    int clasificacionCaso,
									    HashMap factoresRiesgo,
									    int estadoAnterior,
									    int unidadGeneradora,
									    String medicos,
									    String servicio2,
									    HashMap microorganismos,
									    String secuenciaMicro
									    )
	{
		int resultado=0;
		int codigo=0;
		int codigoNot=0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
					
			//********************************************************
			// Insercion de la ficha de infecciones intrahospitalarias
			
			PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),1,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroCama),2,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(servicio),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaIngreso,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDxIh,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaEgreso,6,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDxHisto,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,dxHospital,8,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,dxIh,9,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,dxEgreso,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,dxHisto,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(generoMicro),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,especieMicro,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,bioTipoMicro,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoMuestra1),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(locAnatomica1),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaToma1,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaRemision1,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,mIdentificacion1,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,pruAdicionales1,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoMuestra2),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(locAnatomica2),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaToma2,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaRemision2,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,mIdentificacion2,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,pruAdicionales2,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antibiotico1),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,sensibilidad1,28,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tDosis1,29,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioAntibiotico1,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaFinAntibiotico1,31,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antibiotico2),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,sensibilidad2,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tDosis2,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioAntibiotico2,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaFinAntibiotico2,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antibiotico3),37,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,sensibilidad3,38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tDosis3,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioAntibiotico3,40,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaFinAntibiotico3,41,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(clasificacionCaso),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(localizacionAnatomica),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,medicos,45,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,servicio2,46,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaInfecciones),47,Types.INTEGER,true,false);
			
			
			resultado = modificarFicha.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			PreparedStatementDecorator eliminarFactoresRiesgo =  new PreparedStatementDecorator(con.prepareStatement(eliminarFactoresRiesgoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarFactoresRiesgo.setInt(1,codigoFichaInfecciones);
			int result = eliminarFactoresRiesgo.executeUpdate();
			
			if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
			
			for (int k=1;k<70;k++) {
		    	
		    	try {
			    	String val = factoresRiesgo.get("factor_"+k).toString();
			    	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarFactor =  new PreparedStatementDecorator(con.prepareStatement(insertarFactorRiesgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarFactor.setInt(1,k);
				    	insertarFactor.setInt(2,codigoFichaInfecciones);
				    	
				    	int res = insertarFactor.executeUpdate();
				    	
				    	if(res<0)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
				catch (NullPointerException npe) {}
			}
			
			
			
			
			PreparedStatementDecorator eliminarFactores =  new PreparedStatementDecorator(con.prepareStatement(eliminarFactorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarFactores.setInt(1, codigoFichaInfecciones);
			result = eliminarFactores.executeUpdate();
			
			if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
			
			

            for (int i=1;i<50;i++) {
            	
            	String factor = factoresRiesgo.get("factor_"+i).toString();
            	String clasificacion = factoresRiesgo.get("clasificacion_"+i).toString();
            	
            	PreparedStatementDecorator insertarFactor =  new PreparedStatementDecorator(con.prepareStatement(insertarFactorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            	
            	UtilidadBD.ingresarDatoAStatement(insertarFactor,Integer.toString(codigo),1,Types.INTEGER,true,false);
            	UtilidadBD.ingresarDatoAStatement(insertarFactor,factor,2,Types.INTEGER,true,false);
            	UtilidadBD.ingresarDatoAStatement(insertarFactor, clasificacion, 3, Types.VARCHAR, true, true);
            	
            	resultado = insertarFactor.executeUpdate();
            	
            	if(resultado<1)
	            {
	                daoFactory.abortTransaction(con);
	                return -1; // Estado de error
	            }
            }
            
            
            
			
			

		    PreparedStatementDecorator eliminarMicros =  new PreparedStatementDecorator(con.prepareStatement(eliminarMicroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarMicros.setInt(1,codigoFichaInfecciones);
		    result = eliminarMicros.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    
		    for (int i=1;i<20;i++) {
		    	
		    	try {
		    		String micro = microorganismos.get("micro_"+i).toString();
			    	String especie = microorganismos.get("especie_"+i).toString();
			    	String biotipo = microorganismos.get("biotipo_"+i).toString();
			    	int codigoMicro;
			    	
			    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaMicro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            ResultSet rs2 = statement.executeQuery();
		            
		            if (rs2.next()) {
		                codigoMicro = rs2.getInt(1);
		            }
		            else {
						logger.error("Error obteniendo el código de la secuencia de microorganismos ");
						return 0;
					}
		            
		            PreparedStatementDecorator insertarMicro =  new PreparedStatementDecorator(con.prepareStatement(insertarMicroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            
		            UtilidadBD.ingresarDatoAStatement(insertarMicro,Integer.toString(codigoMicro),1,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarMicro,Integer.toString(codigo),2,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarMicro, micro, 3, Types.INTEGER, true, false);
		            UtilidadBD.ingresarDatoAStatement(insertarMicro, especie, 4, Types.VARCHAR, true, false);
		            UtilidadBD.ingresarDatoAStatement(insertarMicro, biotipo, 5, Types.VARCHAR, true, false);
		            
		            result = insertarMicro.executeUpdate();
		            
		            if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    	catch (NullPointerException npe) {}
		    }
		    
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaInfeccionDao "+sqle.toString() );
			resultado=0;
		}
		
		return resultado;
	}
    
    
    
    public static ResultSet consultarTodoFichaInfecciones(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Infecciones Intrahospitalarias "+sqle);
			return null;
		}
    }
    
    
    
    
    public static ResultSet consultarFactoresRiesgo(Connection con, int codigo)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFactorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los factores de riesgo (ficha de Infecciones) "+sqle);
			return null;
    	}
    	/*
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFactoresRiesgoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los factores de riesgo (ficha de Infecciones Intrahospitalarias) "+sqle);
			return null;
        }
        */
    }
    
    
    
    
    public static ResultSet consultarDatosPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	try {
    		String consultaStr = consultaDatosPacienteStr;
    		
    		if (empezarnuevo) {
    			
    			consultaStr = consultaDatosPacienteStr2;
    		}
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigo);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaSarampionDao) "+sqle);
			return null;
    	}
    }
    
    
    
    

    public static ResultSet consultarMicros(Connection con, int codigo)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarMicroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los microorganismos aislados (ficha de Infecciones) "+sqle);
			return null;
    	}
    }
    
    
    
    

    public static ResultSet consultarFactores(Connection con, int codigo)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFactorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los factores de riesgo (ficha de Infecciones) "+sqle);
			return null;
    	}
    }
}
