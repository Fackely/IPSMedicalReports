/*
 * Junio 26, 2009
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * 
 * @author Sebasti�n G�mez R.
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares para la Busqueda de Deudores
 *
 */
public class SqlBaseBusquedaDeudoresGenericaDao 
{
	/**
    * Objeto para manejar los logs de esta clase
    */
	private static Logger logger = Logger.getLogger(SqlBaseBusquedaDeudoresGenericaDao.class);
	
	/**
	 * Cadena para buscar deudores tipo empresa
	 */
	private static final String busquedaEmpresaStr = "SELECT " +
		"d.codigo AS codigo, " +
		"t.numero_identificacion || coalesce('-'||t.digito_verificacion,'') as identificacion, " +
		"e.razon_social as descripcion," +
		"d.tipo as tipo " +
		"FROM empresas e " +
		"INNER JOIN terceros t on (t.codigo = e.tercero) "+
		"INNER JOIN deudores d ON (d.codigo_empresa = e.codigo) " +
		"WHERE 1=1 ";
	
	/**
	 * Cadena para buscar deudores tipo empresa
	 */
	private static final String busquedaEmpresaSinDeudorStr = "SELECT " +
		"e.codigo AS codigo, " +
		"t.numero_identificacion || coalesce('-'||t.digito_verificacion,'') as identificacion, " +
		"e.razon_social as descripcion," +
		"'"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' as tipo " +
		"FROM empresas e " +
		"INNER JOIN terceros t on (t.codigo = e.tercero) " +
		"WHERE 1=1 ";
		
	
	/**
	 * Cadena para buscar deudores tipo tercero
	 */
	private static final String busquedaTerceroStr = "SELECT " +
		"d.codigo AS codigo, " +
		"t.numero_identificacion || coalesce('-'||t.digito_verificacion,'') as identificacion, " +
		"t.descripcion as descripcion, " +
		"d.tipo as tipo " +
		"FROM terceros t " +
		"INNER JOIN deudores d on (d.codigo_tercero = t.codigo) " +
		"WHERE 1=1 ";
	
	
	/**
	 * Cadena para buscar deudores tipo tercero
	 */
	private static final String busquedaOtroSinDeudorStr = "SELECT " +
		"d.codigo AS codigo, " +
		"d.tipo_identificacion || ' ' || d.numero_identificacion as identificacion, " +
		"d.primer_apellido || coalesce(' '||d.segundo_apellido||' ',' ') || d.primer_nombre || coalesce(' '||d.segundo_nombre,'')  as descripcion, " +
		"'"+ConstantesIntegridadDominio.acronimoOtro+"' as tipo " +
		" FROM deudores d  where  d.tipo ='"+ConstantesIntegridadDominio.acronimoOtro+"' ";
	
	
	/**
	 * Cadena para buscar deudores tipo tercero
	 */
	private static final String busquedaOtroStr = "SELECT " +
		"d.codigo AS codigo, " +
		"d.tipo_identificacion || ' ' || d.numero_identificacion as identificacion, " +
		"d.primer_apellido || coalesce(' '||d.segundo_apellido||' ',' ') || d.primer_nombre || coalesce(' '||d.segundo_nombre,'')  as descripcion, " +
		"d.tipo as tipo " +
		"FROM deudores d " +
		"WHERE d.tipo ='"+ConstantesIntegridadDominio.acronimoOtro+"' "; 
	
	/**
	 * Cadena para buscar deudores tipo tercero
	 */
	private static final String busquedaTerceroSinDeudorStr = "SELECT " +
		"t.codigo AS codigo, " +
		"t.numero_identificacion || coalesce('-'||t.digito_verificacion,'') as identificacion, " +
		"t.descripcion as descripcion," +
		"'"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' as tipo " +
		"FROM terceros t  " +
		"WHERE 1=1 ";
	
	/**
	 * Cadena para buscar deudores tipo paciente
	 */
	private static final String busquedaPacienteStr = "SELECT " +
		"d.codigo AS codigo, " +
		"p.tipo_identificacion || ' ' || p.numero_identificacion as identificacion, " +
		"p.primer_apellido || coalesce(' '||p.segundo_apellido||' ',' ') || p.primer_nombre || coalesce(' '||p.segundo_nombre,'')  as descripcion, " +
		"d.tipo as tipo " +
		"FROM personas p " +
		"INNER JOIN deudores d on (d.codigo_paciente = p.codigo ) " +
		"WHERE 1=1 ";
	
	/**
	 * Cadena para buscar deudores tipo paciente
	 */
	private static final String busquedaPacienteSinDeudorStr = "SELECT " +
		"p.codigo AS codigo, " +
		"p.tipo_identificacion || ' ' || p.numero_identificacion as identificacion, " +
		"p.primer_apellido || coalesce(' '||p.segundo_apellido||' ',' ') || p.primer_nombre || coalesce(' '||p.segundo_nombre,'')  as descripcion, " +
		"'"+ConstantesIntegridadDominio.acronimoPaciente+"' as tipo " +
		"FROM personas p " +
		"INNER JOIN pacientes pac ON(pac.codigo_paciente = p.codigo) " +
		"WHERE 1=1 " ;
	
	/**
	 * M�todo que realiza la busqueda de deudores
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> busqueda(Connection con,HashMap<String, Object> campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		try
		{
			//*****************SE TOMAN LOS PAR�METROS**********************************
			String tipoDeudor = campos.get("tipoDeudor").toString();
			String codigoTipoIdentificacion = campos.get("codigoTipoIdentificacion").toString();
			String numeroIdentificacion = campos.get("numeroIdentificacion").toString();
			String primerApellido = campos.get("primerApellido").toString();
			String primerNombre = campos.get("primerNombre").toString();
			String descripcion = campos.get("descripcion").toString();
			//Par�metro para saber si la busqueda se hace sobre la tabla deudores o no
			//Si es nuevoDEudor quiere decir que apenas se buscar crearlo por lo tanto no se debe tomar en cuenta la tabla deudores
			boolean nuevoDeudor = UtilidadTexto.getBoolean(campos.get("nuevoDeudor").toString());
			//Par�metro usado para filtrar solo los deudores activos
			boolean activo = UtilidadTexto.getBoolean(campos.get("activo").toString());
			//***************************************************************************
			
			String consulta = "";
			String where = "";
			String where2 = "";
			
			if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
			{
				consulta = nuevoDeudor?busquedaEmpresaSinDeudorStr:busquedaEmpresaStr;
				if(!numeroIdentificacion.equals(""))
				{
					where += " AND  t.numero_identificacion = '"+numeroIdentificacion+"' ";
				}
				if(!descripcion.equals(""))
				{
					where += " AND  upper(e.razon_social) like upper('%"+descripcion+"%') ";
				}
				if(nuevoDeudor)
				{
					where += " AND  e.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
				}
			}
			else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
			{
				consulta = nuevoDeudor?busquedaTerceroSinDeudorStr:busquedaTerceroStr;
				if(!numeroIdentificacion.equals(""))
				{
					where += " AND  t.numero_identificacion = '"+numeroIdentificacion+"' ";
				}
				if(!descripcion.equals(""))
				{
					where += " AND  upper(t.descripcion) like upper('%"+descripcion+"%') ";
				}
				if(nuevoDeudor)
				{
					where += " AND  t.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
				}
			}
			else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoOtro))
			{
				consulta = nuevoDeudor?busquedaOtroSinDeudorStr:busquedaOtroStr;
								
				if(!codigoTipoIdentificacion.equals(""))
				{
					where2 += " AND  d.tipo_identificacion = '"+codigoTipoIdentificacion+"' ";
				}
				if(!numeroIdentificacion.equals(""))
				{
					where2 += " AND  d.numero_identificacion = '"+numeroIdentificacion+"' ";
				}
				if(!primerApellido.equals(""))
				{
					where2 +=  "AND   upper(d.primer_apellido) like upper('%"+primerApellido+"%') ";
				}
				if(!primerNombre.equals(""))
				{
					where2 += " AND  upper(d.primer_nombre) like upper('%"+primerApellido+"%') ";
				}
				
				consulta += where2;
				
			}
			
			else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente))
			{
				consulta = nuevoDeudor?busquedaPacienteSinDeudorStr:busquedaPacienteStr;
				if(!codigoTipoIdentificacion.equals(""))
				{
					where +=  " AND p.tipo_identificacion = '"+codigoTipoIdentificacion+"' ";
				}
				if(!numeroIdentificacion.equals(""))
				{
					where +=  " AND p.numero_identificacion = '"+numeroIdentificacion+"' ";
				}
				if(!primerApellido.equals(""))
				{
					where +=  " AND upper(p.primer_apellido) like upper('%"+primerApellido+"%') ";
				}
				if(!primerNombre.equals(""))
				{
					where += " AND  upper(p.primer_nombre) like upper('%"+primerNombre+"%') ";
				}
			}
			
			if(activo)
			{
				where += " AND d.activo = '"+ConstantesBD.acronimoSi+"' ";
			}
			
			consulta = consulta  + where + " ORDER BY descripcion, identificacion ";
			logger.info("\n CONSULTA DEUDORES --> "+consulta);
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en busqueda: "+e);
			resultados.put("numRegistros", "0");
		}
		return resultados;
	}
		
}
