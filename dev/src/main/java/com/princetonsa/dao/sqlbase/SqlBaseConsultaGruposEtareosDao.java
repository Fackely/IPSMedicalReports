
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de la Consulta de Grupos Etareos por Convenio
 * @version 1.0  26 /May/ 2006
 */
public class SqlBaseConsultaGruposEtareosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaGruposEtareosDao.class);
	
	
	/**
	 * Cadena con el statement necesario para consultar los grupos etareos existentes para un convenio
	 */
	private final static String consultarGruposEtareosStr=" SELECT ge.codigo as codigogrupoetareo, " +
														  " ge.convenio as codigoconvenio, " +
														  " ge.institucion as institucion, " +
														  " ge.edad_final as edadfinal, " +
														  " ge.edad_inicial as edadinicial, " +
														  " ge.fecha_inicial as fechainicial, " +
														  " ge.fecha_final as fechafinal, " +
														  " ge.sexo as codigosexo, " +
														  " case when ge.sexo IS NULL THEN 'Ambos' ELSE s.nombre END AS nombresexo, " +
														  " ge.valor as valor, " +
														  " ge.porcentaje_pyp as pyp " +
														  " FROM grupos_etareos_x_convenio ge " +
														  " LEFT OUTER JOIN sexo s ON(ge.sexo=s.codigo) " +
														  " WHERE ge.institucion = ? " +
														  " AND ge.convenio = ? " +
														  " AND fecha_inicial >= ? " +
														  " AND fecha_final <= ? " +
														  " ORDER BY ge.edad_inicial ASC, ge.edad_final ASC, s.nombre ASC ";
	
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun unos parametros de busqueda
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarGruposEtareos (Connection con, String fechaIncial, String fechaFinal, int codigoConvenio, int institucion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarGruposEtareosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, codigoConvenio);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaIncial)));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarGruposEtareos : [SqlBaseConsultaGruposEtareosDao] "+e.toString() );
			return null;
		}	    
	}
}