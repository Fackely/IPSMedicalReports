/*
 * Ene 17, 2007
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Sebastián Gómez R.
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares para la Busqueda de Diagnósticos
 *
 */
public class SqlBaseBusquedaDiagnosticosGenericaDao 
{
	/**
    * Objeto para manejar los logs de esta clase
    */
	private static Logger logger = Logger.getLogger(SqlBaseBusquedaDiagnosticosGenericaDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la fecha de la agenda, para una cita, dado el número de la
	 * solicitud
	 */
	private static final String consultaFechaCitaStr=" SELECT " +
		"to_char(ag.fecha, 'YYYY-MM-DD') as fecha " +
		"from agenda ag " +
		"INNER JOIN cita ci ON (ag.codigo=ci.codigo_agenda) " +
		"where ci.numero_solicitud=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del tipo de cie válido para una fecha dada
	 */
	private static final String consultaCieStr="SELECT codigo from tipos_cie where vigencia<=? order by vigencia desc";
	
	/**
	 * Secccion SELECT Cadena que consulta los diagnosticos
	 */
	private static final String consultaDiagnosticosSELECT_Str = "SELECT " +
	 "d.acronimo, " +
	 "d.tipo_cie, " +
	 "d.nombre, " +
	 "CASE WHEN d.sexo IS NULL THEN 'Sin Restricción' ELSE s.nombre END AS nombre_sexo, " +
	 "d.edad_inicial AS edad_inicial, " +
	 "d.edad_final AS edad_final, " +
	 "CASE WHEN d.es_principal = '"+ConstantesBD.acronimoSi+"' THEN 'SI' ELSE 'NO' END AS es_principal, " +
	 "CASE WHEN d.es_muerte = '"+ConstantesBD.acronimoSi+"' THEN 'SI' ELSE 'NO' END AS es_muerte, " +
	 "d.codigoenfermedadesnotificables AS codigo_enfermedad " +
	 "FROM diagnosticos d " +
	 "LEFT OUTER JOIN sexo s ON (s.codigo=d.sexo) ";
	
	/**
	 * Seccion WHERE Cadena que consulta los diagnosticos
	 */
	private static final String  consultaDiagnosticosWHERE_Str = " WHERE d.tipo_cie = ? and d.activo = " + ValoresPorDefecto.getValorTrueParaConsultas();
	
	/**
	 * Método implementado para consultar la fecha de una cita 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultaFechaCita (Connection con, String numeroSolicitud) 
	{
		try
		{
			String fecha = "";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaFechaCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, numeroSolicitud);
	    

		    ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
		    if (rs.next())
		        fecha=rs.getString("fecha");
		    
		    return fecha;

		} 
		catch (SQLException e) 
		{
			logger.error("Error en consultaFechaCita : "+e);
			return "";
		}

	}
	
	
	/**
	 * Método que consulta el CIE vigente según fecha
	 * @param con
	 * @param fecha
	 * @return
	 */
	public static int consultaCieVigente (Connection con, String fecha) 
	{
		try
		{
			
			logger.info("FECHA---->>>>>>> "+fecha);
			int cie = ConstantesBD.codigoNuncaValido;
		    PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaCieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    pst.setString(1, fecha);
		    ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
		    if (rs.next())
		        cie=rs.getInt("codigo");
		     
		    return cie;
		    
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaCieVigente : "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	
	/**
	 * Método que realiza la busqueda de diagnosticos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultaDiagnosticos(Connection con, HashMap campos) 
	{
		try
		{
			//Se toman los parámetros del mapa*******************************
			String criterioBusqueda = campos.get("criterioBusqueda").toString();
			boolean buscarTexto = UtilidadTexto.getBoolean(campos.get("buscarTexto").toString());
			int codigoCie = Utilidades.convertirAEntero(campos.get("codigoCie")+"");
			boolean esPrincipal = UtilidadTexto.getBoolean(campos.get("esPrincipal").toString());
			boolean esMuerte = UtilidadTexto.getBoolean(campos.get("esMuerte").toString());
			boolean filtrarSexo = UtilidadTexto.getBoolean(campos.get("filtrarSexo").toString());
			String sexo = filtrarSexo?campos.get("sexo").toString():"";
			boolean filtrarEdad = UtilidadTexto.getBoolean(campos.get("filtrarEdad").toString());
			String edad = filtrarEdad?campos.get("edad").toString():"";
			int tipoDiagnostico = Integer.parseInt(campos.get("tipoDiagnostico").toString());
			String codigoFiltro = campos.get("codigoFiltro").toString();
			String diagnosticosSeleccionados = campos.get("diagnosticosSeleccionados").toString();
			String institucion = campos.get("institucion").toString();
			//****************************************************************
			
			String consulta = consultaDiagnosticosSELECT_Str;
			
			/**
			 * SE DEJA PENDIENTE
			 * if(!codigoFiltro.equals(""))
				consulta += "INNER JOIN filtro_diagnosticos f ON(f.diagnostico=d.acronimo AND f.tipo_cie=d.tipo_cie AND f.institucion="+institucion+" AND f.tipo_componente IN ("+codigoFiltro+")) ";**/
			consulta += consultaDiagnosticosWHERE_Str;
			
			
			//Se filtra según el criterio de busqueda-----------------------------------------------
			if(!criterioBusqueda.equals(""))
			{
				if(buscarTexto)
					consulta += " AND UPPER(d.nombre) LIKE UPPER('%"+criterioBusqueda+"%') ";
				else
					consulta += " AND UPPER(d.acronimo) LIKE UPPER('%"+criterioBusqueda+"%') ";
			}
			
			//se filtra solo principales -----------------------------------------------------------
			if(esPrincipal)
				consulta += " AND d.es_principal = '"+ConstantesBD.acronimoSi+"'";
			
			//se filtra solo muerte ---------------------------------------------------------------
			if(esMuerte)
				consulta += " AND d.es_muerte = '"+ConstantesBD.acronimoSi+"'";
			
			//se filtra sexo ---------------------------------------------------------------------
			if(filtrarSexo)
				consulta += " AND (d.sexo IS NULL OR d.sexo = "+sexo+")";
			
			//se filtra edad ------------------------------------------------------------------
			if(filtrarEdad)
				consulta += " AND ("+edad+" BETWEEN d.edad_inicial AND d.edad_final)";
			
			//Validacion adicional para los diagnosticos relacionados
			if((tipoDiagnostico==ConstantesBD.codigoDxRelacionado || tipoDiagnostico==ConstantesBD.codigoDxPreoperatorioRelacionado || tipoDiagnostico==ConstantesBD.codigoDxMuerte)&&!esMuerte&&!diagnosticosSeleccionados.equals(""))
				consulta += " AND d.acronimo NOT IN ("+diagnosticosSeleccionados+")";
			
			//se aplicar ordenacion
			if(buscarTexto)
				consulta += " ORDER BY d.nombre ";
			
			logger.info("\n\n CONSULTA DIAGNOSTICOS GENERICA-->"+consulta+ " CIE->"+codigoCie);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCie);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaDiagnosticos: "+e);
			return null;
		}

	}
	    
}
