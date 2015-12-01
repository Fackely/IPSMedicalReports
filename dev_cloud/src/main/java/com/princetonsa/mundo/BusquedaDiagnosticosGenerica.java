/*
 * Ene 17, 2007
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.BusquedaDiagnosticosGenericaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * 
 * @author Sebastián Gómez R.
 * Clase para el manejo de la busqueda de diagnósticos generica
 */
public class BusquedaDiagnosticosGenerica 
{
	
    /**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    private static Logger logger = Logger.getLogger(BusquedaDiagnosticosGenerica.class);
    
    /**
     * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
     * @param tipoBD el tipo de base de datos que va a usar este objeto
     * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
     * son los nombres y constantes definidos en <code>DaoFactory</code>.
     * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
     */
    public static BusquedaDiagnosticosGenericaDao busquedaDao()
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaDiagnosticosGenericaDao();
    
    }
    
    /**
	 * Método implementado para consultar la fecha de una cita 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultaFechaCita (Connection con, String numeroSolicitud)
	{
		return busquedaDao().consultaFechaCita(con, numeroSolicitud);
	}
	
	/**
	 * Método que consulta el CIE vigente según fecha
	 * @param con
	 * @param fecha
	 * @return
	 */
	public static int consultaCieVigente (Connection con, String fecha)
	{
		return busquedaDao().consultaCieVigente(con, fecha);
	}
	
	/**
	 * Método que realiza la busqueda de diagnosticos
	 * @param con
	 * @param criterioBusqueda
	 * @param buscarTexto
	 * @param codigoCie
	 * @param esPrincipal
	 * @param esMuerte
	 * @param filtrarSexo
	 * @param sexo
	 * @param filtrarEdad
	 * @param edad
	 * @param tipoDiagnostico
	 * @param diagnosticosSeleccionados
	 * @param codigoFiltro 
	 * @param institucion 
	 * @return
	 */
	public static HashMap consultaDiagnosticos(Connection con, String criterioBusqueda,boolean buscarTexto,String codigoCie,boolean esPrincipal,boolean esMuerte,boolean filtrarSexo,String sexo,boolean filtrarEdad,String edad,String tipoDiagnostico,String diagnosticosSeleccionados, String codigoFiltro, String institucion)
	{
		HashMap campos = new HashMap();
		campos.put("criterioBusqueda",criterioBusqueda);
		campos.put("buscarTexto",buscarTexto+"");
		campos.put("codigoCie",codigoCie);
		campos.put("esPrincipal",esPrincipal+"");
		campos.put("esMuerte",esMuerte+"");
		campos.put("filtrarSexo",filtrarSexo+"");
		campos.put("sexo",sexo);
		campos.put("filtrarEdad",filtrarEdad+"");
		campos.put("edad",edad);
		campos.put("tipoDiagnostico",tipoDiagnostico);
		campos.put("diagnosticosSeleccionados",diagnosticosSeleccionados);
		campos.put("codigoFiltro",codigoFiltro);
		campos.put("institucion",institucion);
		
		return busquedaDao().consultaDiagnosticos(con, campos);
	}
    
     
}
