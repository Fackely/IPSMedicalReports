
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0 09 /May/ 2006
 * Clase para las transacciones de la Consulta de LOGS de los Cupos Extras
 */
public class SqlBaseConsultaLogCuposExtraDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaLogCuposExtraDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar los datos del LOG de cupos extra
	 */
	private static final String consultarLogCuposExtraStr=" SELECT ce.fecha_proceso as fecha, " +
														  " ce.hora_proceso as hora, " +
														  " ce.usuario as usuario, " +
														  " a.fecha as fechacita, " +
														  " a.hora_inicio as horacita, " +
														  " uc.descripcion as unidadconsulta, " +
														  " administracion.getnombremedico(a.codigo_medico) as profesional, " +
														  " ce.cupos_disponibles as cuposdisponibles, " +
														  " ce.cupos_extra as cuposextra " +
														  " FROM cupos_extra ce " +
														  " INNER JOIN agenda a ON(ce.codigo_agenda=a.codigo) " +
														  " INNER JOIN unidades_consulta uc ON(a.unidad_consulta=uc.codigo) " +
														  " INNER JOIN consultorios co ON (co.codigo=a.consultorio)		" +
														  " WHERE 1=1 ";
	
	/**
	 * M�todo para consultar el LOG de cupos extras segun los parametros de busqueda definidos
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoMedico
	 * @param codigoUnidadConsulta
	 * @param centroAtencion 
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarLogCuposExtra (Connection con, String fechaIncial, String fechaFinal, int codigoMedico, int codigoUnidadConsulta, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps = null;
		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
		
			String avanzadaStr = "";
			
			if(!fechaIncial.equals("") && !fechaFinal.equals(""))
				avanzadaStr+=" AND a.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaIncial)+"' and  '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
			
			if(codigoUnidadConsulta != ConstantesBD.codigoNuncaValido)
				avanzadaStr+=" AND a.unidad_consulta="+codigoUnidadConsulta;
			else
				avanzadaStr+=" AND a.unidad_consulta IN ("+unidadesAgenda+") ";
			
			if(codigoMedico != ConstantesBD.codigoNuncaValido)
				avanzadaStr+=" AND a.codigo_medico="+codigoMedico;

			if(centroAtencion != ConstantesBD.codigoNuncaValido)
				avanzadaStr+=" AND co.centro_atencion = "+centroAtencion;
			else
				avanzadaStr+=" AND a.codigo_medico IN ("+centrosAtencion+") ";
			
			String consulta= consultarLogCuposExtraStr + avanzadaStr+" ORDER BY a.fecha desc ";
			logger.info("Consulta -> "+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarLogCuposExtra : [SqlBaseConsultaLogCuposExtraDao] "+e.toString() );
			return null;
		}	    
	}
	
	
}