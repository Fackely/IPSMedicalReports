package com.sysmedica.dao.sqlbase;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadBD;
import util.Utilidades;

import java.util.Collection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlBaseBusquedaNotificacionesDao {
	
	/**
     * Objeto que maneja los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseBusquedaFichasDao.class);
    
    
    private static final String consultaNotificacionesParte1Str = 			"vigienfnotificables.nombre AS diagnostico, " +
    																		"viginotificacion.fecha AS fechaNotificacion," +
    																		"viginotificacion.hora AS horaNotificacion," +
    																		"vigitiponotificacion.nombre as tipoNotificacion," +
    																		"personas.primer_nombre AS primerNombreProfesional," +
    																		"personas.segundo_nombre AS segundoNombreProfesional," +
    																		"personas.primer_apellido AS primerApellidoProfesional," +
    																		"personas.segundo_apellido AS segundoApellidoProfesional," +
    																		"diagnosticos.codigoenfermedadesnotificables AS codigoenfnot ";
    private static final String consultaNotificacionesParte2Str = "FROM " +
    														      			"viginotificacion," +
    														      			"usuarios," +
    														      			"personas," +
    														      			"diagnosticos," +
    														      			"vigienfnotificables," +
    														      			"vigitiponotificacion";
    
    
    private static final String consultaNotificacionesParte3Str = "WHERE " +
    																		"viginotificacion.login=usuarios.login " +
    																		"AND usuarios.codigo_persona=personas.codigo " +
    																		"AND vigienfnotificables.codigoenfermedadesnotificables=diagnosticos.codigoenfermedadesnotificables " +
    																		"AND viginotificacion.tipo=vigitiponotificacion.codigo ";
    
    private static final String tablaFichasStr = ", (SELECT " +
														"codigoNotificacion," +
														"acronimo," +
														"codigoFichaRabia as codigoficha " +
													"FROM vigificharabia " +
													"UNION " +
													"SELECT " +
														"codigoNotificacion," +
														"acronimo," +
														"codigoFichaSarampion as codigoficha " +
													"FROM vigifichasarampion) tablaFichas ";
    
    /**
     * Condicion opcinal para realizar la busqueda teniendo en cuenta
     * el usuario quien diligencio la notificacion
     */
    private static final String consultaNotificacionCondicionUsuario= "AND viginotificacion.login=? ";
    
    
    public static Collection consultaNotificaciones(Connection con,
    												String fechaInicial,
    												String fechaFinal,
    												String diagnostico,
    												String loginUsuario,
    												String loginUsuarioBusqueda,
    												int tipoNotificacion) {
    	
    	try {
    		
    		ResultSet rs;
	        Collection coleccion;
	        String stringConsulta="";
	        String consultaNotificacionesParte0Str = "";
	        String consultaNotificacionesParte4Str = "";
	        String condicionUsuarios = "";
	        String condicionTemporal = "";
	        String condicionTipo = "";
	        String condicionOrden = "";
	        
	        
	        if (!Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
	        	
	        	condicionUsuarios = consultaNotificacionCondicionUsuario;
	        }
	        else {
	        	
	        	if (!loginUsuarioBusqueda.equals("@@")) {
	        		
	        		condicionUsuarios = consultaNotificacionCondicionUsuario;
	        	}
	        }
	        
	        if (fechaInicial.length()>0&&fechaFinal.length()>0) {
	            
	            condicionTemporal = " AND viginotificacion.fecha>="+"'"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"'"+" AND viginotificacion.fecha<="+"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
	        }
	        
	        if (tipoNotificacion==1||tipoNotificacion==2) {
	        	
	        	condicionTipo = " AND viginotificacion.tipo="+tipoNotificacion;
	        }
	        
	        	        
	        if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRabia)))
	        {
	        	consultaNotificacionesParte0Str = "SELECT DISTINCT ON (vigiFichaRabia.codigoNotificacion) ";
	        	
	        	String otrosCamposAConsultar = ",vigificharabia.codigoficharabia AS codigoficha, vigificharabia.codigonotificacion AS codigoNotificacion ";
	        	
	        	String otrasTablasAConsultar = ", vigificharabia ";
	        	
	        	consultaNotificacionesParte4Str = " AND	vigificharabia.codigonotificacion=viginotificacion.codigonotificacion " +
	        									  " AND vigificharabia.acronimo=diagnosticos.acronimo ";
	        	
	        	condicionOrden = " ORDER BY vigiFichaRabia.codigoNotificacion ASC";
	            
	        	stringConsulta = consultaNotificacionesParte0Str 
	        					+ consultaNotificacionesParte1Str 
	        					+ otrosCamposAConsultar 
	        					+ consultaNotificacionesParte2Str 
	        					+ otrasTablasAConsultar 
	        					+ consultaNotificacionesParte3Str 
	        					+ consultaNotificacionesParte4Str
	        					+ condicionUsuarios
	        					+ condicionTemporal
	        					+ condicionTipo
	        					+ condicionOrden;
	        }
	        
	        // Para consultar en las fichas de todas las enfermedades
	        else if (diagnostico.equals("0")) {
	        	
	        	consultaNotificacionesParte0Str = "SELECT DISTINCT ON (tablafichas.codigoNotificacion) ";
	        	
	        	String otrosCamposAConsultar = ",tablaFichas.codigoNotificacion, tablaFichas.codigoFicha ";
	        		      
	        	consultaNotificacionesParte4Str = " AND tablaFichas.codigoNotificacion = viginotificacion.codigoNotificacion " +
	        									   "AND tablaFichas.acronimo = diagnosticos.acronimo ";
	        	
	        	condicionOrden = " ORDER BY tablafichas.codigonotificacion ASC";
	        	
	        	stringConsulta =  consultaNotificacionesParte0Str 
	        					+ consultaNotificacionesParte1Str
	        					+ otrosCamposAConsultar
	        					+ consultaNotificacionesParte2Str
	        					+ tablaFichasStr
	        					+ consultaNotificacionesParte3Str
	        					+ consultaNotificacionesParte4Str
	        					+ condicionUsuarios
	        					+ condicionTemporal
	        					+ condicionTipo
	        					+ condicionOrden;
	        }
	        
	        
	        
	         
	        PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        logger.info(stringConsulta);
	        
	        if (!loginUsuarioBusqueda.equals("@@")) {
	        	
	        	if (Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
	        		
	        		consulta.setString(1,loginUsuarioBusqueda);
	        	}
	        	else {
	        		
	        		consulta.setString(1,loginUsuario);
	        	}
	        }
	        if (!Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
	        	
	        	consulta.setString(1,loginUsuario);
	        }
	        
	        rs = consulta.executeQuery();
	        
	        coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
	        	        
	        return coleccion;
	        
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando las notificaciones "+sqle);
			return null;
    	}
    }
}
