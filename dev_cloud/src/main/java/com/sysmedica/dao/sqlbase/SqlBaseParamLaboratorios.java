package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;


import com.princetonsa.dao.DaoFactory;

public class SqlBaseParamLaboratorios {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseParamLaboratorios.class);
    
    private static final String insertarMuestraStr="INSERT INTO epidemiologia.vigiDetalleMuestra " +
    													"(" +
    													"codigoMuestra," +
    													"codigoEnfNotificable" +
    													") " +
    												"VALUES (?,?) ";
    
    private static final String insertarPruebaStr="INSERT INTO epidemiologia.vigiDetallePrueba " +
														"(" +
														"codigoPrueba," +
														"codigoEnfNotificable," +
														"tipoSolicitud," +
														"servicio" +
														") " +
													"VALUES (?,?,?,?) ";
    
    private static final String insertarAgenteStr="INSERT INTO epidemiologia.vigiDetalleAgente " +
														"(" +
														"codigoAgente," +
														"codigoEnfNotificable" +
														") " +
													"VALUES (?,?) ";
    
    private static final String insertarResultadoStr="INSERT INTO epidemiologia.vigiDetalleResultado " +
														"(" +
														"codigoResultado," +
														"codigoEnfNotificable" +
														") " +
													"VALUES (?,?) ";
    
    
    
    private static final String eliminarMuestrasStr="DELETE FROM epidemiologia.vigiDetalleMuestra WHERE codigoEnfNotificable = ?";
    
    private static final String eliminarPruebasStr="DELETE FROM epidemiologia.vigiDetallePrueba WHERE codigoEnfNotificable = ?";
    
    private static final String eliminarAgentesStr="DELETE FROM epidemiologia.vigiDetalleAgente WHERE codigoEnfNotificable = ?";
    
    private static final String eliminarResultadosStr="DELETE FROM epidemiologia.vigiDetalleResultado WHERE codigoEnfNotificable = ?";
    
    
    
    
    
    private static final String consultarMuestrasStr="SELECT codigoMuestra FROM epidemiologia.vigiDetalleMuestra WHERE codigoEnfNotificable = ?";
    
    private static final String consultarPruebasStr="SELECT codigoPrueba,tipoSolicitud,servicio FROM epidemiologia.vigiDetallePrueba WHERE codigoEnfNotificable = ?";
    
    private static final String consultarAgentesStr="SELECT codigoAgente FROM epidemiologia.vigiDetalleAgente WHERE codigoEnfNotificable = ?";
    
    private static final String consultarResultadosStr="SELECT codigoResultado FROM epidemiologia.vigiDetalleResultado WHERE codigoEnfNotificable = ?";
    
    
    
    private static final String insertarTipoSolicitud="UPDATE epidemiologia.vigiEnfNotificables SET tipoSolicitud=? WHERE codigoEnfermedadesNotificables = ?";
    
    private static final String consultarTipoSolicitudStr="SELECT tipoSolicitud FROM epidemiologia.vigiEnfNotificables WHERE codigoEnfermedadesNotificables = ?";
    
    
    
    
    private static final String consultarServiciosParametrizadosStr="SELECT " +
	    																"serv.codigo," +
	    																"serv.especialidad," +
	    																"refs.descripcion," +
	    																"det.tiposolicitud," +
	    																"det.activo " +
	    															"FROM " +
	    																"servicios serv," +
	    																"referencias_servicio refs," +
	    																"epidemiologia.vigidetalleservicios det " +
	    															"WHERE " +
	    																"det.codigoenfnotificable = ? " +
	    															"AND " +
	    																"serv.codigo = det.codigoservicio " +
	    															"AND " +
	    																"serv.codigo = refs.servicio " +
	    															"AND " +
	    																"serv.activo = 'true' " +
	    															"AND " +
	    																"refs.tipo_tarifario=0 ";
    
    
    
    private static final String consultaRapidaServiciosStr="SELECT " +
															    "serv.codigo," +
																"serv.especialidad," +
																"refs.descripcion," +
																"serv.tipo_servicio " +
															"FROM " +
																"servicios serv," +
																"referencias_servicio refs " +
															"WHERE " +
																"refs.codigo_propietario = ? " +
															"AND " +
																"serv.codigo = refs.servicio " +
															"AND " +
																"refs.tipo_tarifario=0 " +
															"AND " +
																"serv.tipo_servicio='P' " +
															"AND " +
																"serv.activo='true'";
    
    
    
    private static final String insertarServicioStr="INSERT INTO epidemiologia.vigiDetalleServicios " +
    													"(" +
    														"codigoservicio," +
    														"codigoenfnotificable," +
    														"tiposolicitud," +
    														"activo," +
    														"institucion" +
    													") " +
    													"VALUES (?,?,?,?,?) ";
    
    
    private static final String eliminarServiciosStr="DELETE FROM epidemiologia.vigiDetalleServicios WHERE codigoenfnotificable=? ";
    
    private static final String eliminarServicioStr="DELETE FROM epidemiologia.vigiDetalleServicios WHERE codigoenfnotificable=? AND codigoservicio=? ";
    
    
    public static int actualizarTipoSolicitud(Connection con, int codigoEnfNotificable, int tipoSolicitud)
    {
    	int resultado;
    	
    	try {
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator insertarTipo =  new PreparedStatementDecorator(con.prepareStatement(insertarTipoSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarTipo.setInt(1,tipoSolicitud);
			insertarTipo.setInt(2,codigoEnfNotificable);
			
			resultado = insertarTipo.executeUpdate();
			
			daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
    		logger.warn(sqle+" Error en la inserción de datos: SqlBaseParamLaboratorios (insercion de tipo de solicitud) "+sqle.toString() );
			resultado=0;
    	}
    	
    	return resultado;
    }
    
    
    
    public static int consultarTipoSolicitud(Connection con, int codigoEnfNotificables)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarTipoSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificables);
    		
    		ResultSet rs = consulta.executeQuery();
    		int resultado = -1;
    		
    		if (rs.next()) {
    			
    			resultado = rs.getInt("tipoSolicitud");
    		}
    		
    		return resultado;
    	//	return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando el tipo de solicitud"+sqle);
			return -1;
		}
    }
    
    
    
    public static int insertarMuestra(Connection con, HashMap codigoMuestra, int codigoEnfNotificable) 
    {
    	int resultado;
    	
    	try {
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarMuestras =  new PreparedStatementDecorator(con.prepareStatement(eliminarMuestrasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarMuestras.setInt(1,codigoEnfNotificable);
			
			resultado = eliminarMuestras.executeUpdate();
			
			/*
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			*/
			
			for (int i=1;i<codigoMuestra.size()+1;i++) {
				
				String val = codigoMuestra.get("muestra_"+i).toString();
				
				if (val.equals("true")) {
					
					PreparedStatementDecorator insertarMuestra =  new PreparedStatementDecorator(con.prepareStatement(insertarMuestraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					insertarMuestra.setInt(1,i);
		    		insertarMuestra.setInt(2,codigoEnfNotificable);
		    		
		    		resultado = insertarMuestra.executeUpdate();
		    		
		    		if(resultado<1)
					{
						daoFactory.abortTransaction(con);
						return -1; // Estado de error
					}
				}
			}
    					
			daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseParamLaboratorios (insercion de elementos de Muestra) "+sqle.toString() );
			resultado=0;			
		}
    	
    	return resultado;
    }
    
    
    
    
    public static int insertarPrueba(Connection con, HashMap codigoPrueba, int codigoEnfNotificable) 
    {
    	int resultado;
    	
    	try {
    		
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarPruebas =  new PreparedStatementDecorator(con.prepareStatement(eliminarPruebasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarPruebas.setInt(1,codigoEnfNotificable);
			
			resultado = eliminarPruebas.executeUpdate();
			
			/*
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			*/
			
			int limiteCiclo = codigoPrueba.size()/3;
			
			for (int i=1;i<limiteCiclo+1;i++) {
				
				String val = codigoPrueba.get("prueba_"+i).toString();
				
				int solicitud = Integer.parseInt(codigoPrueba.get("solicitud_"+i).toString());
				
				int servicio = Integer.parseInt(codigoPrueba.get("servicio_"+i).toString());
				
				if (val.equals("true")) {
					
					
					PreparedStatementDecorator insertarPrueba =  new PreparedStatementDecorator(con.prepareStatement(insertarPruebaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
										
					insertarPrueba.setInt(1,i);
					insertarPrueba.setInt(2,codigoEnfNotificable);
					insertarPrueba.setInt(3,solicitud);
					insertarPrueba.setInt(4,servicio);
					
		    		resultado = insertarPrueba.executeUpdate();
		    		
		    		if(resultado<1)
					{
						daoFactory.abortTransaction(con);
						return -1; // Estado de error
					}
				}
				
			}
    					
			daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseParamLaboratorios (insercion de elementos de Prueba) "+sqle.toString() );
			resultado=0;			
		}
    	
    	return resultado;
    }
    
    
    
    
    
    public static int insertarAgente(Connection con, HashMap codigoAgente, int codigoEnfNotificable) 
    {
    	int resultado;
    	
    	try {
    		
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarAgentes =  new PreparedStatementDecorator(con.prepareStatement(eliminarAgentesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarAgentes.setInt(1,codigoEnfNotificable);
			
			resultado = eliminarAgentes.executeUpdate();
			
			/*
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			*/
			
			for (int i=1;i<codigoAgente.size()+1;i++) {
				
				String val = codigoAgente.get("agente_"+i).toString();
				
				if (val.equals("true")) {
					
					PreparedStatementDecorator insertarAgente =  new PreparedStatementDecorator(con.prepareStatement(insertarAgenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					insertarAgente.setInt(1,i);
					insertarAgente.setInt(2,codigoEnfNotificable);
		    		
		    		resultado = insertarAgente.executeUpdate();
		    		
		    		if(resultado<1)
					{
						daoFactory.abortTransaction(con);
						return -1; // Estado de error
					}
				}
			}
    					
			daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseParamLaboratorios (insercion de elementos de Agente) "+sqle.toString() );
			resultado=0;			
		}
    	
    	return resultado;
    }
    
    
    
    public static int insertarResultado(Connection con, HashMap codigoResultado, int codigoEnfNotificable) 
    {
    	int resultado;
    	
    	try {
    		
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarResultados =  new PreparedStatementDecorator(con.prepareStatement(eliminarResultadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarResultados.setInt(1,codigoEnfNotificable);
			
			resultado = eliminarResultados.executeUpdate();
			/*
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			*/
			
			for (int i=1;i<codigoResultado.size()+1;i++) {
				
				String val = codigoResultado.get("resultado_"+i).toString();
				
				if (val.equals("true")) {
					
					PreparedStatementDecorator insertarResultado =  new PreparedStatementDecorator(con.prepareStatement(insertarResultadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					insertarResultado.setInt(1,i);
					insertarResultado.setInt(2,codigoEnfNotificable);
		    		
		    		resultado = insertarResultado.executeUpdate();
		    		
		    		if(resultado<1)
					{
						daoFactory.abortTransaction(con);
						return -1; // Estado de error
					}
				}
			}
    					
			daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseParamLaboratorios (insercion de elementos de Resultado) "+sqle.toString() );
			resultado=0;			
		}
    	
    	return resultado;
    }
    
    
    
    
    public static ResultSet consultarMuestras(Connection con, int codigoEnfNotificables)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarMuestrasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificables);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando las Opciones de Muestras"+sqle);
			return null;
		}
    }
    
    
    
    public static ResultSet consultarPruebas(Connection con, int codigoEnfNotificables)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarPruebasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificables);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando las Opciones de Pruebas"+sqle);
			return null;
		}
    }
    
    
    
    public static ResultSet consultarAgentes(Connection con, int codigoEnfNotificables)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarAgentesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificables);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando las Opciones de Agentes"+sqle);
			return null;
		}
    }
    
    
    
    public static ResultSet consultarResultados(Connection con, int codigoEnfNotificables)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarResultadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificables);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando las Opciones de Resultados"+sqle);
			return null;
		}
    }
    
    
    
    
    public static ResultSet consultarServiciosParametrizados(Connection con, int codigoEnfNotificables)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarServiciosParametrizadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificables);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("error consultando los servicios parametrizados "+sqle);
    		return null;
    	}
    }
    
    
    
    public static ResultSet consultaRapidaServicios(Connection con,int codigoCups)
    {
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaRapidaServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoCups);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("error consultando los servicios parametrizados "+sqle);
    		return null;
    	}
    }
    
    
    
    public static int insertarServicios(Connection con, HashMap mapaServicios, int codigoEnfNotificable, int codigoInstitucion)
    {
    	int resultado;
    	
    	try {
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarServicios =  new PreparedStatementDecorator(con.prepareStatement(eliminarServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarServicios.setInt(1,codigoEnfNotificable);
			
			resultado = eliminarServicios.executeUpdate();
			
			int numeroElementos = mapaServicios.size()/4;
			
			for (int i=0;i<numeroElementos;i++) {
				
				String valorCodEsp = mapaServicios.get("codigo_"+i).toString();
				
				String valorCodigo = valorCodEsp.split("-")[1];
				String valorTipoSolicitud = mapaServicios.get("tiposolicitud_"+i).toString();
				String valorActivo = mapaServicios.get("activo_"+i).toString();
				
				PreparedStatementDecorator insertarServicio =  new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				insertarServicio.setInt(1,Integer.parseInt(valorCodigo));
				insertarServicio.setInt(2,codigoEnfNotificable);
				insertarServicio.setInt(3,Integer.parseInt(valorTipoSolicitud));
				insertarServicio.setInt(4,Integer.parseInt(valorActivo));
				insertarServicio.setInt(5,codigoInstitucion);
				
				resultado = insertarServicio.executeUpdate();
				
				if (resultado<1) {
					
					daoFactory.abortTransaction(con);
					return -1;
				}
			}
    					
			daoFactory.endTransaction(con);
			
			return resultado;
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("error insertando los servicios parametrizados "+sqle);
    		return -1;
    	}
    }
    
    
    
    
    public static int eliminarServicio(Connection con,int codigoEnfNotificable,int codigoServicio)
    {
    	int resultado;
    	
    	try {
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarServicio =  new PreparedStatementDecorator(con.prepareStatement(eliminarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarServicio.setInt(1,codigoEnfNotificable);
			eliminarServicio.setInt(2,codigoServicio);
			
			resultado = eliminarServicio.executeUpdate();
			
			daoFactory.endTransaction(con);
			
			return resultado;
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("error eliminando el servicio parametrizado "+sqle);
    		return -1;
    	}
			
    }
}

