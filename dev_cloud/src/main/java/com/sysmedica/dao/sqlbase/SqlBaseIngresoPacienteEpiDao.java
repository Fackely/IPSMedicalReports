package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import java.sql.Types;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseIngresoPacienteEpiDao {

	/**
     * Objeto que maneja los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseIngresoPacienteEpiDao.class);
    
	
    private static final String consultaPacienteStr = "SELECT " +
    														"per.codigo as codigo," +
														    "per.primer_nombre," +
															"per.segundo_nombre," +
															"per.primer_apellido," +
															"per.segundo_apellido," +
															"per.codigo_departamento_vivienda AS dep_vivienda," +
															"per.codigo_ciudad_vivienda AS ciu_vivienda," +
															"per.codigo_departamento_nacimiento AS dep_nacimiento," +
															"per.codigo_ciudad_nacimiento AS ciu_nacimiento," +
															"per.direccion AS direccion_paciente," +
															"per.telefono AS telefono_paciente," +
															"per.fecha_nacimiento," +
															"per.sexo," +
															"per.estado_civil," +
															"per.numero_identificacion, " +
															"per.codigo_depto_id AS depIdentifica, " +
															"per.codigo_ciudad_id AS ciuIdentifica, " +
															"per.codigo_pais_id, " +
															"per.codigo_pais_nacimiento, " +
															"per.codigo_pais_vivienda, " +
															
															"per.codigo_barrio_vivienda AS barrio, " +
															"pac.zona_domicilio AS zonaDomicilio, " +
															"pac.ocupacion AS ocupacion, " +
															"pac.esta_vivo AS estaVivo, " +
															"per.tipo_identificacion AS tipoId, "+
															"pac.etnia AS etnia, " +
															"pac.tipo_sangre as tipoSangre, " +
															"pac.grupo_poblacional as grupoPoblacional " +
														
														"FROM " +
														//	"personas per, departamentos dep, ciudades ciu, " +
															"personas per, "+
															"pacientes pac " +
														"WHERE " +
															"per.numero_identificacion=? " +
														"AND per.tipo_identificacion=? " +
														"AND per.codigo=pac.codigo_paciente ";
    
    
	
    private static final String ingresarPersonaStr = "INSERT INTO personas " +
    													"(" +
    													"codigo," +
    													"primer_nombre," +
    													"segundo_nombre," +
    													"primer_apellido," +
    													"segundo_apellido," +
    													"codigo_ciudad_vivienda," +
    													"codigo_departamento_vivienda," +
    													"codigo_barrio_vivienda," +
    													"direccion," +
    													"telefono," +
    													"fecha_nacimiento," +
    													"sexo," +
    													"numero_identificacion," +
    													"tipo_identificacion," +
    													"estado_civil," +
    													"tipo_persona," +
    													"codigo_ciudad_nacimiento," +
    													"codigo_departamento_nacimiento," +
    													"codigo_depto_id," +
    													"codigo_ciudad_id," +
    													"codigo_pais_id, " +
    													"codigo_pais_nacimiento, " +
    													"codigo_pais_vivienda) " +
    												"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,?,?,?,?,?)";
    
    
	
    private static final String modificarPersonaStr = "UPDATE personas " +
    														"SET " +
    															"primer_nombre=?," +
    															"segundo_nombre=?," +
    															"primer_apellido=?," +
    															"segundo_apellido=?," +
    															"codigo_departamento_vivienda=?," +
    															"codigo_ciudad_vivienda=?," +
    															"codigo_barrio_vivienda=?," +
    															"direccion=?," +
    															"telefono=?," +
    															"fecha_nacimiento=?," +
    															"sexo=?,"+
    															"numero_identificacion=?," +
    															"tipo_identificacion=?, " +
    															"estado_civil=?, " +
    	    													"codigo_ciudad_nacimiento=?, " +
    	    													"codigo_departamento_nacimiento=?, " +
    	    													"codigo_depto_id=?, " +
    	    													"codigo_ciudad_id=?, " +
    	    													"codigo_pais_id=?, " +
    	    													"codigo_pais_nacimiento=?, " +
    	    													"codigo_pais_residencia=? " +
    														"WHERE " +
    															"numero_identificacion=? " +
    														"AND tipo_identificacion=? ";
    
    
    
    
    private static final String modificarPacienteStr = "UPDATE pacientes " +
    														"SET " +
    															"zona_domicilio=?," +
    															"ocupacion=?," +
    															"etnia=?, " +
    															"esta_vivo=?, " +
    															"tipo_sangre=?," +
    															"grupo_poblacional=? " +
    														"WHERE " +
    															"codigo_paciente=?";
    											
    
    
	
    private static final String ingresarPacienteStr = "INSERT INTO pacientes " +
    														"(" +
    														"codigo_paciente," +
    														"zona_domicilio," +
    														"ocupacion," +
    														"etnia," +
    														"esta_vivo," +
    														"tipo_sangre," +
    														"grupo_poblacional" +
    														") " +
    													"VALUES (?,?,?,?,?,?,?)";
    
    
    private static final String ingresarHistoriaClinicaStr = "INSERT INTO historias_clinicas " +
    															"(" +
    															"codigo_paciente," +
    															"fecha_apertura" +
    															") " +
    														"VALUES (?,CURRENT_DATE)";
    
    
    
    private static final String ingresarPacientesInstituciones = "INSERT INTO pacientes_instituciones " +
    																	"(" +
    																		"codigo_paciente," +
    																		"codigo_institucion" +
    																	") " +
    																"VALUES (?,?)";
    
    
    
    private static final String ingresarPacientesInstituciones2 = "INSERT INTO pacientes_instituciones2 " +
																		"(" +
																			"codigo_paciente," +
																			"codigo_institucion_duena," +
																			"codigo_institucion_permitida" +
																		") " +
																	"VALUES (?,?,?)";
    
    
    
    private static final String consultarConvenioPacienteExisteStr = "SELECT " +
	    																"convenio_por_defecto " +
	    															  "FROM " +
	    															  	"cuentas," +
	    															  	"personas " +
	    															  "WHERE " +
	    															  	"cuentas.codigo_paciente=personas.codigo " +
	    															  "AND personas.codigo = ?";
    
    
    public static int ingresarPaciente(Connection con,
    									String secuencia,
    									String primerApellido,
										String segundoApellido,
										String primerNombre,
										String segundoNombre,
										String fechaNacimiento,
										int genero,
										String municipioResidencia,
										String departamentoResidencia,
										String lugarResidencia,
										int codigoBarrioResidencia,
										String municipioNacimiento,
										String departamentoNacimiento,
										String lugarNacimiento,
										String estadoCivil,
										boolean estaVivo,
										String direccion,
										String zonaDomicilio,
										String telefono,
										int ocupacion,
										String tipoRegimen,
										int aseguradora,
										int etnia,
										String numeroIdentificacion,
										String tipoIdentificacion,
										String municipioIdentifica,
										String departamentoIdentifica,
										String lugarIdentifica,
										int tipoSangre,
										String grupoPoblacional,
										int codigoInstitucion,
										String paisExpedicion,
										String paisNacimiento,
										String paisResidencia)
    {
    	int resultado=0;
		int codigo=0;
		
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
			
			PreparedStatementDecorator ingresarPersona =  new PreparedStatementDecorator(con.prepareStatement(ingresarPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			String codigoMunResidencia = lugarResidencia.split("-")[1];
	        String codigoDepResidencia = lugarResidencia.split("-")[0];
	        String codigoMunNacimiento = lugarNacimiento.split("-")[0];
	        String codigoDepNacimiento = lugarNacimiento.split("-")[1];
	        String codigoMunIdentifica = lugarIdentifica.split("-")[0];
	        String codigoDepIdentifica = lugarIdentifica.split("-")[1];
	        
			ingresarPersona.setInt(1,codigo);
			ingresarPersona.setString(2,primerNombre);
			ingresarPersona.setString(3,segundoNombre);
			ingresarPersona.setString(4,primerApellido);
			ingresarPersona.setString(5,segundoApellido);
			ingresarPersona.setString(6,codigoMunResidencia);
			ingresarPersona.setString(7,codigoDepResidencia);
			ingresarPersona.setInt(8,codigoBarrioResidencia);
			ingresarPersona.setString(9,direccion);
			ingresarPersona.setString(10,telefono);
			ingresarPersona.setString(11,UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento));
			ingresarPersona.setInt(12,genero);
			ingresarPersona.setString(13,numeroIdentificacion);
			ingresarPersona.setString(14,tipoIdentificacion);
			ingresarPersona.setString(15,estadoCivil);
			ingresarPersona.setString(16,codigoMunNacimiento);
			ingresarPersona.setString(17,codigoDepNacimiento);
			ingresarPersona.setString(18,codigoDepIdentifica);
			ingresarPersona.setString(19,codigoMunIdentifica);
			ingresarPersona.setString(20,paisExpedicion);
			ingresarPersona.setString(21,paisNacimiento);
			ingresarPersona.setString(22,paisResidencia);
			
			
			resultado = ingresarPersona.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			PreparedStatementDecorator ingresarPaciente =  new PreparedStatementDecorator(con.prepareStatement(ingresarPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ingresarPaciente.setInt(1,codigo);
			ingresarPaciente.setString(2,zonaDomicilio);
			ingresarPaciente.setInt(3,ocupacion);
			ingresarPaciente.setInt(4,etnia);
			ingresarPaciente.setBoolean(5,estaVivo);
			ingresarPaciente.setInt(6,tipoSangre);
			ingresarPaciente.setString(7,grupoPoblacional);			
			
			resultado = ingresarPaciente.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			PreparedStatementDecorator ingresarHistoria =  new PreparedStatementDecorator(con.prepareStatement(ingresarHistoriaClinicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ingresarHistoria.setInt(1,codigo);
			
			resultado = ingresarHistoria.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			
			// Insercion a la tabla pacientes_instituciones
			PreparedStatementDecorator ingresarPacientesInst =  new PreparedStatementDecorator(con.prepareStatement(ingresarPacientesInstituciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ingresarPacientesInst.setInt(1,codigo);
			ingresarPacientesInst.setInt(2,codigoInstitucion);
			
			
			resultado = ingresarPacientesInst.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			
			
			
			// Insercion a la tabla pacientes_instituciones
			PreparedStatementDecorator ingresarPacientesInst2 =  new PreparedStatementDecorator(con.prepareStatement(ingresarPacientesInstituciones2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ingresarPacientesInst2.setInt(1,codigo);
			ingresarPacientesInst2.setInt(2,codigoInstitucion);
			ingresarPacientesInst2.setInt(3,codigoInstitucion);
			
			
			resultado = ingresarPacientesInst2.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de Paciente desde Epidemiologia: SqlBaseIngresoPacienteEpiDao "+sqle.toString() );
			resultado=0;			
		}
		
		return codigo;
    }
    
    
    
    public static ResultSet consultarPaciente(Connection con, String numeroIdentificacion, String tipoIdentificacion)
    {
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setString(1,numeroIdentificacion);
			consulta.setString(2,tipoIdentificacion);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando el paciente desde Epidemiologia "+sqle);
			return null;
		}
    }
    
    
    
    public static int modificarPaciente(Connection con,
    									int codigoPaciente,
										String primerApellido,
										String segundoApellido,
										String primerNombre,
										String segundoNombre,
										String fechaNacimiento,
										int genero,
										String municipioResidencia,
										String departamentoResidencia,
										String lugarResidencia,
										int codigoBarrioResidencia,
										String municipioNacimiento,
										String departamentoNacimiento,
										String lugarNacimiento,
										String estadoCivil,
										boolean estaVivo,
										String direccion,
										String zonaDomicilio,
										String telefono,
										int ocupacion,
										String tipoRegimen,
										int aseguradora,
										int etnia,
										String numeroIdentificacion,
										String tipoIdentificacion,
										String nuevoNumeroIdentificacion,
										String nuevoTipoIdentificacion,
										String municipioIdentifica,
										String departamentoIdentifica,
										String lugarIdentifica,
										int tipoSangre,
										String grupoPoblacional,
										String paisExpedicion,
										String paisNacimiento,
										String paisResidencia
										)
    {
    	int result = 0;
    	
    	try {
	    	DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	        daoFactory.beginTransaction(con);
	        
	        String codigoMunResidencia = lugarResidencia.split("-")[1];
	        String codigoDepResidencia = lugarResidencia.split("-")[0];
	        String codigoMunNacimiento = lugarNacimiento.split("-")[0];
	        String codigoDepNacimiento = lugarNacimiento.split("-")[1];
	        String codigoMunIdentifica = lugarIdentifica.split("-")[0];
	        String codigoDepIdentifica = lugarIdentifica.split("-")[1];
	        
	        PreparedStatementDecorator modificarPersona =  new PreparedStatementDecorator(con.prepareStatement(modificarPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,primerNombre,1,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,segundoNombre,2,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,primerApellido,3,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,segundoApellido,4,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,codigoDepResidencia,5,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,codigoMunResidencia,6,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,Integer.toString(codigoBarrioResidencia),7,Types.INTEGER,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,direccion,8,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,telefono,9,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento),10,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,Integer.toString(genero),11,Types.INTEGER,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,numeroIdentificacion,12,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,tipoIdentificacion,13,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,estadoCivil,14,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,codigoMunNacimiento,15,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,codigoDepNacimiento,16,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,codigoDepIdentifica,17,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,codigoMunIdentifica,18,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,paisExpedicion,19,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,paisNacimiento,20,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,paisResidencia,21,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,numeroIdentificacion,22,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPersona,tipoIdentificacion,23,Types.VARCHAR,true,false);
	        
	        
	        
	        result = modificarPersona.executeUpdate();
	        
	        if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
	        
	        PreparedStatementDecorator modificarPaciente =  new PreparedStatementDecorator(con.prepareStatement(modificarPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,zonaDomicilio,1,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,Integer.toString(ocupacion),2,Types.INTEGER,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,Integer.toString(etnia),3,Types.INTEGER,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,Boolean.toString(estaVivo),4,Types.BOOLEAN,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,Integer.toString(tipoSangre),5,Types.INTEGER,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,grupoPoblacional,6,Types.VARCHAR,true,false);
	        UtilidadBD.ingresarDatoAStatement(modificarPaciente,Integer.toString(codigoPaciente),7,Types.INTEGER,true,false);
	        
        	result = modificarPaciente.executeUpdate();
	        
	        if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
	        
	        daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de paciente: SqlBaseIngresoPacienteEpiDao "+sqle.toString() );
		    result=0;
        }
    	
    	return result;
    }
    
    
    public static ResultSet consultarConvenioPaciente(Connection con, int codigoPaciente)
    {
    	ResultSet result;
    	
    	try {
	    	PreparedStatementDecorator consultarConvenio =  new PreparedStatementDecorator(con.prepareStatement(consultarConvenioPacienteExisteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	
	    	consultarConvenio.setInt(1,codigoPaciente);
	    	
	    	result = consultarConvenio.executeQuery();
	    	
	    	return result;
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    		
    		return null;
    	}
    	
    	
    }
}

