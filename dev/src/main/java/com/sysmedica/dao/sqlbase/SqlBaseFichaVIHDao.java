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

public class SqlBaseFichaVIHDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaVIHDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichavih "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaVIH," +
    														"codigoPaciente,"+
    														"estado,"+
    														"acronimo,"+
    														"fechaDiligenciamiento," +
    														"horaDiligenciamiento," +
    														"codigomunprocedencia," +
    														"codigodepprocedencia," +
    														"codigomunnoti," +
    														"codigodepnoti," +
    														"codigoaseguradora," +
    														"nombreprofesionaldiligencio)" +
    														" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?)";
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichavih " +
    													"SET " +
    													"sire=?, " +
    													"estado=?, " +
    													"tipoMuestra=?, " +
    													"tipoPrueba=?, " +
    													"resultado=?, " +
    													"fechaResultado=?, " +
    													"valor=?, " +
    													"estadioClinico=?," +
    													"numeroHijos=?," +
    													"numeroHijas=?," +
    													"embarazo=?," +
    													"numeroSemanas=?, " +
    													
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
    												"WHERE codigoFichaVih=? ";
    
    
    private static final String eliminarEnfAsociadas = "DELETE from epidemiologia.vigiDetalleEnfAsociada WHERE codigoFichaVih = ?";
    
    private static final String eliminarMecTransmision = "DELETE from epidemiologia.vigiDetalleMecanismoTran WHERE codigoFichaVih = ?";
    
    private static final String insertarMecTransmision = "INSERT INTO epidemiologia.vigiDetalleMecanismoTran(codigomecanismo,codigofichavih) VALUES(?,?)";
    
    private static final String insertarEnfAsociadas = "INSERT INTO epidemiologia.vigiDetalleEnfAsociada(codigoenfermedad,codigofichavih) VALUES(?,?)";
    
    
    private static final String consultarFichaVIHStr = "SELECT " +
    														"ficha.sire," +
    														"ficha.estado," +
    														"ficha.tipoMuestra," +
    														"ficha.tipoPrueba," +
    														"ficha.resultado," +
    														"ficha.fechaResultado," +
    														"ficha.valor," +
    														"ficha.estadioClinico," +
    														"ficha.numeroHijos," +
    														"ficha.numeroHijas," +
    														"ficha.embarazo," +
    														"ficha.numeroSemanas," +
    														"ficha.pais," +
    														"ficha.areaProcedencia, " +
    														
    														"ficha.codigoDepProcedencia AS departamentoProcedencia, " +
															"ficha.codigoMunProcedencia AS municipioProcedencia, " +
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
															"ficha.nombreprofesionaldiligencio AS nombreProfesional, " +
															"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, "+
    														
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
		
		    												"FROM epidemiologia.vigifichavih ficha " +
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
															"ficha.codigoFichaVIH = ? " ;
    
    
    
    public static final String consultaMecanismosTransmisionStr = "SELECT " +
    																	"codigoMecanismo " +
    																"FROM " +
    																	"epidemiologia.vigiDetalleMecanismoTran " +
    																"WHERE " +
    																	"codigoFichaVIH = ? ";
    
    
    public static final String consultaEnfermedadesAsociadasStr = "SELECT " +
																		"codigoEnfermedad " +
																	"FROM " +
																		"epidemiologia.vigiDetalleEnfAsociada " +
																	"WHERE " +
																		"codigoFichaVIH = ? ";
																	
																	
    /**
     * String con el statement para insertar una notificacion
     */
    private static final String insertarNotificacionStr = "INSERT INTO epidemiologia.vigiNotificacion "+
    															"("+
    															"codigoNotificacion,"+
    															"login,"+
    															"fecha,"+
    															"hora,"+
    															"tipo"+
    															") "+
    														"VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
    
    
    
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
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
																"FROM personas per " +
																"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_vivienda and dep.codigo_pais=per.codigo_pais_vivienda)  " +
																"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_vivienda and ciu.codigo_departamento=per.codigo_departamento_vivienda AND ciu.codigo_pais=per.codigo_pais_vivienda)  " +
																"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda)  " +
																"INNER JOIN pacientes pac on(pac.codigo_paciente=per.codigo) " +
																"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion)  " +
																"INNER JOIN cuentas c ON(c.codigo_paciente = pac.codigo_paciente) " +
																"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) " +
																"INNER JOIN convenios conv ON (conv.codigo = sc.convenio) " +
																"INNER JOIN tipos_regimen regs ON(regs.acronimo = conv.tipo_regimen) " +
															"WHERE " +
																"per.codigo = ? ";
    
    
    
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
   
    
    
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichavih "+
															"(" +
															"loginUsuario,"+
															"codigoFichaVIH," +
															"codigoPaciente,"+
															"estado,"+
															"acronimo,"+
															"fechaDiligenciamiento," +
															"horaDiligenciamiento," +
															"codigomunprocedencia," +
															"codigodepprocedencia," +
															"codigomunnoti," +
															"codigodepnoti," +
															"codigoaseguradora," +
															"nombreprofesionaldiligencio, " +
															
															"sire, " +
															"tipoMuestra, " +
															"tipoPrueba, " +
															"resultado, " +
															"fechaResultado, " +
															"valor, " +
															"estadioClinico," +
															"numeroHijos," +
															"numeroHijas," +
															"embarazo," +
															"numeroSemanas, " +
															
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
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
    
   
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaVIHDao "+sqle.toString() );
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
											boolean notificar,
											
											int codigoFichaVIH,										    
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
										    
										    HashMap mecanismosTransmision,
										    int tipoMuestra,
										    int tipoPrueba,
										    int resultado,
										    String fechaResultado,
										    String valor,
										    int estadioClinico,
										    int numeroHijos,
										    int numeroHijas,
										    int embarazo,
										    int numeroSemanas,
										    HashMap enfermedadesAsociadas,
										    int estadoAnterior,
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   )
    {
    	int result=0;
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoMuestra),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoPrueba),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(resultado),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaResultado,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,valor,17,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estadioClinico),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroHijos),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroHijas),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(embarazo),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroSemanas),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),26,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),28,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),30,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),31,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),33,Types.INTEGER,true,false);
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            for (int i=1;i<mecanismosTransmision.size()+1;i++) {
		    	
		    	String val = mecanismosTransmision.get("mecanismo_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarMecanismos =  new PreparedStatementDecorator(con.prepareStatement(insertarMecTransmision,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarMecanismos.setInt(1,i);
			    	insertarMecanismos.setInt(2,codigo);
			    	
			    	result = insertarMecanismos.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            for (int i=1;i<enfermedadesAsociadas.size()+1;i++) {
		    	
		    	String val = enfermedadesAsociadas.get("enfermedad_"+i).toString();
		    	
		    	if (val.equals("true")) {
		    		
		    		PreparedStatementDecorator insertarEnfermedades =  new PreparedStatementDecorator(con.prepareStatement(insertarEnfAsociadas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    		
		    		insertarEnfermedades.setInt(1,i);
		    		insertarEnfermedades.setInt(2,codigo);
		    		
		    		result = insertarEnfermedades.executeUpdate();
		    		
		    		if (result<1)
		    		{
		    			daoFactory.abortTransaction(con);
		    			return -1;
		    		}
		    		else {
		    			result = codigo;
		    		}
		    	}
		    }
            
            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSarampionDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return result;
    }
    
    
    
    public static int modificarFicha(Connection con,
    									String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaVIH,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
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
									    
									    HashMap mecanismosTransmision,
									    int tipoMuestra,
									    int tipoPrueba,
									    int resultado,
									    String fechaResultado,
									    String valor,
									    int estadioClinico,
									    int numeroHijos,
									    int numeroHijas,
									    int embarazo,
									    int numeroSemanas,
									    HashMap enfermedadesAsociadas,
									    String secuenciaNotificaciones,
									    int estadoAnterior,
									    String pais,
									    int areaProcedencia
									    )
    {
    	int result=0;
        int codigo=0;
        int codigoNot=0;
        
        try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            // Insercion de la Notificacion
        /*    if (notificar&&estado!=ConstantesBD.codigoEstadoFichaSeguimiento) {
                
                PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuenciaNotificaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    codigo = rs.getInt(1);
                    codigoNot=codigo;
                }
                else {
    				logger.error("Error obteniendo el código de la secuencia de la notificacion ");
    				return 0;
    			}
                PreparedStatementDecorator insertarNotificacion =  new PreparedStatementDecorator(con.prepareStatement(insertarNotificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
               
                // Inserción de los datos de la notificacion
                
                insertarNotificacion.setInt(1,codigo);
                insertarNotificacion.setString(2,loginUsuario);
                insertarNotificacion.setString(3,Integer.toString(ConstantesBD.codigoTipoNotificacionIndividual));
                
                result = insertarNotificacion.executeUpdate();
                
                if(result<1)
                {
                    daoFactory.abortTransaction(con);
                    return -1; // Estado de error
                }
            }
            
      */
            
            //********************************************************
            // Insercion de la ficha de VIH
            
        //    System.out.println("LUGAR PROCEDENCIA 0 : "+lugarProcedencia.split("-")[0].toString());
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoMuestra),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoPrueba),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(resultado),5,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaResultado,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,valor,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estadioClinico),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroHijos),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroHijas),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(embarazo),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroSemanas),12,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,15,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),20,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,21,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),26,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaVIH),27,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    PreparedStatementDecorator eliminarMecanismos =  new PreparedStatementDecorator(con.prepareStatement(eliminarMecTransmision,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarMecanismos.setInt(1,codigoFichaVIH);
		    result = eliminarMecanismos.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		   
		    	
			//    for (int i=1;i<mecanismosTransmision.size()+1;i++) {
		    	for (int i=1;i<20;i++) {
			    	
			    	try {
				    	String val = mecanismosTransmision.get("mecanismo_"+i).toString();
		
				    	if (val.equals("true")) {
					    	PreparedStatementDecorator insertarMecanismos =  new PreparedStatementDecorator(con.prepareStatement(insertarMecTransmision,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					    	
					    	insertarMecanismos.setInt(1,i);
					    	insertarMecanismos.setInt(2,codigoFichaVIH);
					    	
					    	result = insertarMecanismos.executeUpdate();
					    	
					    	if(result<1)
				            {
				                daoFactory.abortTransaction(con);
				                return -1; // Estado de error
				            }
				    	}
			    	}
				    catch (NullPointerException npe) {}
			    }
		    
		    
		    PreparedStatementDecorator eliminarEnfermedades =  new PreparedStatementDecorator(con.prepareStatement(eliminarEnfAsociadas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarEnfermedades.setInt(1,codigoFichaVIH);
		    result = eliminarEnfermedades.executeUpdate();
		    
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
           
		    
		    
		    	
			//    for (int i=1;i<enfermedadesAsociadas.size()+1;i++) {
		    	for (int i=1;i<40;i++) {
			    	
			    	try {
				    	String val = enfermedadesAsociadas.get("enfermedad_"+i).toString();
				    	
				    	if (val.equals("true")) {
				    		
				    		PreparedStatementDecorator insertarEnfermedades =  new PreparedStatementDecorator(con.prepareStatement(insertarEnfAsociadas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    		
				    		insertarEnfermedades.setInt(1,i);
				    		insertarEnfermedades.setInt(2,codigoFichaVIH);
				    		
				    		result = insertarEnfermedades.executeUpdate();
				    		
				    		if (result<1)
				    		{
				    			daoFactory.abortTransaction(con);
				    			return -1;
				    		}
				    	}
			    	}
				    catch (NullPointerException npe) {}
			    }
		    
		    
		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaVIHDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
         
    }
    
    
    
    public static ResultSet consultarTodoFichaVIH(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaVIHStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de VIH "+sqle);
			return null;
		}
    }
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarMecanismosTransmision(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaMecanismosTransmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los mecanismos de transmision (ficha de VIH) "+sqle);
			return null;
        }
    }
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarEnfermedadesAsociadas(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaEnfermedadesAsociadasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las enfermedades asociadas (ficha de VIH) "+sqle);
			return null;
        }
    }
    
    
    
    
    public static ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo) {
    	
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
}
