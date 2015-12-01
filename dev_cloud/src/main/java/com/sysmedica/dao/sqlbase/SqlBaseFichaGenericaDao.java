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

public class SqlBaseFichaGenericaDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaGenericaDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichagenerica "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaGenerica," +
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
    														"nombreprofesionaldiligencio)" +
    												" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?)";
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichagenerica "+
															"(" +
															"loginUsuario,"+
															"codigoFichaGenerica," +
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
															
															"sire, " +
															
															"fechaConsultaGeneral, " +
															"fechaInicioSintomasGeneral, " +
															"tipoCaso, " +
															"hospitalizadoGeneral, " +
															"fechaHospitalizacionGeneral, " +
															"estaVivoGeneral, " +
															"fechaDefuncion, " +
															"institucionAtendio," +
															"activa, " +
															"pais, " +
															"areaProcedencia " +
														") " +
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
    
    
    private static final String modificarFichaStr = "UPDATE epidemiologia.vigifichagenerica " +
														"SET " +
															"sire=?, " +
															"loginUsuario=?, " +
															"estado=?, " +
															
															"codigoDepProcedencia=?, " +
															"codigoMunProcedencia=?, " +
															"fechaConsultaGeneral=?, " +
															"fechaInicioSintomasGeneral=?, " +
															"tipoCaso=?, " +
															"hospitalizadoGeneral=?, " +
															"fechaHospitalizacionGeneral=?, " +
															"estaVivoGeneral=?, " +
															"fechaDefuncion=?, " +
															"codigoDepNoti=?, " +
															"codigoMunNoti=?, " +
															"institucionAtendio=?, " +
															"pais=?, " +
															"areaProcedencia=? " +
														"WHERE codigoFichaGenerica = ? ";
    
    public static final String consultarFichaGenericaStr =  "SELECT " +
																"ficha.sire, " +
																
																"ficha.codigoDepProcedencia AS departamentoProcedencia, " +
    															"ficha.codigoMunProcedencia AS municipioProcedencia, " +
    															"ficha.pais AS pais, " +
    															"ficha.areaProcedencia AS areaProcedencia, " +
    															"ficha.fechaconsultageneral AS fechaConsultaGeneral, " +
    															"ficha.fechainiciosintomasgeneral AS fechaInicioSintomas, " +
    															"ficha.tipocaso AS tipoCaso, " +
    															"ficha.hospitalizadogeneral AS hospitalizado, " +
    															"ficha.fechahospitalizaciongeneral AS fechaHospitalizacion, " +
    															"ficha.estavivogeneral AS condicionFinal, " +
    															"ficha.fechadefuncion AS fechaDefuncion, " +
    															"ficha.nombreProfesionalDiligencio AS nombreProfesional, " +
    															"ficha.codigoDepNoti AS departamentoNotifica, " +
    															"ficha.codigoMunNoti AS municipioNotifica, " +
    															"ficha.institucionAtendio AS nombreUnidad, " +
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
    		    											//	"ficha.desplazado AS desplazado " +
    		    												"pac.grupo_poblacional as grupoPoblacional " +
    		    												"FROM epidemiologia.vigifichagenerica ficha " +
				    											"INNER JOIN personas per ON(ficha.codigoPaciente=per.codigo) " +
				    											"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_nacimiento AND dep.codigo_pais=per.codigo_pais_nacimiento) " +
				    											"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_nacimiento AND ciu.codigo_departamento=per.codigo_departamento_nacimiento AND ciu.codigo_pais=per.codigo_pais_nacimiento)  " +
				    											"INNER JOIN departamentos dep2 ON(dep2.codigo_departamento=per.codigo_departamento_vivienda AND dep2.codigo_pais=per.codigo_pais_vivienda) " +
				    											"INNER JOIN ciudades ciu2 ON(ciu2.codigo_ciudad=per.codigo_ciudad_vivienda AND ciu2.codigo_departamento=per.codigo_departamento_vivienda AND ciu2.codigo_pais=per.codigo_pais_vivienda) " +
				    											"INNER JOIN usuarios usu ON(usu.login=ficha.loginUsuario)  " +
				    											"INNER JOIN personas per2 ON(per2.codigo=usu.codigo_persona)  " +
				    											"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda) " +
				    											"INNER JOIN pacientes pac ON(pac.codigo_paciente=per.codigo) " +
				    											"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion) " +
																"INNER JOIN convenios conv ON(conv.codigo=ficha.codigoAseguradora) " +
																"INNER JOIN tipos_regimen regs ON(conv.tipo_regimen=regs.acronimo) " +
																"WHERE " +
																"ficha.codigoFichaGenerica = ? " ;
    
    
    /*
    public static final String consultaDatosPacienteStr = "SELECT " +
															    "per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.nombre AS dep_vivienda," +
																"ciu.nombre AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																
																"bar.nombre AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"conv.nombre AS aseguradora, " +
																"regs.nombre AS regimenSalud, " +
																"pac.etnia AS etnia " +
															"FROM " +
																"personas per, departamentos dep, ciudades ciu, " +
																"barrios bar, pacientes pac, ocupaciones ocup, " +
																"convenios conv, tipos_regimen regs, cuentas " +
															"WHERE " +
																"per.codigo = ? " +
																"AND dep.codigo=per.codigo_departamento_vivienda " +
																"AND ciu.codigo_ciudad=per.codigo_ciudad_vivienda " +
																"AND ciu.codigo_departamento=per.codigo_departamento_vivienda " +
																
																"AND per.codigo_departamento_vivienda=bar.codigo_departamento " +
																"AND per.codigo_ciudad_vivienda=bar.codigo_ciudad " +
																"AND per.codigo_barrio_vivienda=bar.codigo_barrio " +
																"AND per.codigo=pac.codigo_paciente " +
																"AND pac.ocupacion=ocup.codigo " +
																"AND cuentas.codigo_paciente=per.codigo " +
																"AND cuentas.convenio_por_defecto=conv.codigo " +
																"AND cuentas.codigo_paciente=pac.codigo_paciente "+
																"AND conv.tipo_regimen=regs.acronimo ";
    */
    
    
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
															"pac.etnia AS etnia, " +
															"pac.grupo_poblacional as grupoPoblacional " +
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
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
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
    
															
    
    
    public static final String consultaDatosAdicionalesPacienteStr = "SELECT " +
																	    "conv.nombre AS aseguradora, " +
																		"tr.nombre AS regimenSalud " +
																	"FROM cuentas c " +
																	"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) " +
																	"INNER JOIN convenios conv ON(conv.codigo = sc.convenio) " +
																	"INNER JOIN tipos_regimen tr ON(tr.acronimo=conv.tipo_regimen) " +
																	"WHERE " +
																		"c.codigo_paciente=? " ;
    
    
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
				logger.error("Error obteniendo el c?digo de la secuencia ");
				return 0;
			}
			
			PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			// Inserci?n de los datos de la ficha
			
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
			logger.warn(sqle+" Error en la inserci?n de datos: SqlBaseFichaGenericaDao "+sqle.toString() );
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
										    
										    String sire,
										    String lugarProcedencia,
										    String fechaConsultaGeneral,
										    String fechaInicioSintomasGeneral,
										    int tipoCaso,
										    boolean hospitalizadoGeneral,
										    String fechaHospitalizacionGeneral,
										    boolean estaVivoGeneral,
										    String fechaDefuncion,
										    String lugarNoti,
										    int unidadGeneradora,
										    boolean activa,
										    String pais,
										    int areaProcedencia
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
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,login,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigo),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoPaciente),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estado),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDiagnostico,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunProcedencia,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepProcedencia,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunNoti,8,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepNoti,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoAseguradora),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreProfesional,11,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,sire,12,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),16,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,17,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),20,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),21,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),23,Types.INTEGER,true,false);
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaGenericaDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaGenerica,
									    int estado,
									    
										String lugarProcedencia,
									    String fechaConsultaGeneral,
									    String fechaInicioSintomasGeneral,
									    int tipoCaso,
									    boolean hospitalizadoGeneral,
									    String fechaHospitalizacionGeneral,
									    boolean estaVivoGeneral,
									    String fechaDefuncion,
									    String lugarNoti,
									    int unidadGeneradora,
									    String pais,
									    int areaProcedencia
										)
	{
		int result=0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			String codigoMunProcedencia = lugarProcedencia.split("-")[0];
			String codigoDepProcedencia = lugarProcedencia.split("-")[1];
			
			String codigoMunNoti = lugarNoti.split("-")[0];
			String codigoDepNoti = lugarNoti.split("-")[1];
			
			PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,loginUsuario,2,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),3,Types.INTEGER,true,false);
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,6,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),9,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),11,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,12,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),17,Types.INTEGER,true,false);
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaGenerica),18,Types.INTEGER,true,false);
			
			result = modificarFicha.executeUpdate();
			
			if(result<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaGenericaDao "+sqle.toString() );
			result=0;
		}
		
		return result;
	}
    
    
    
    
    
    public static ResultSet consultarTodoFichaGenerica(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaGenericaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha Generica "+sqle);
			return null;
		}
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaGenericaDao) "+sqle);
			return null;
    	}
    }
    
    
    
    public static ResultSet consultarDatosAdicionalesPaciente(Connection con, int codigo) {
    	
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosAdicionalesPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigo);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		logger.error("Error consultando los datos adicionales del paciente (SqlBaseFichaGenericaDao) "+sqle);
			return null;
    	}
    }
}
