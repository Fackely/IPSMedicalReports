/*
 * @(#)SqlBaseActivPyPXFacturarDao
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 *
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseActivPyPXFacturarDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseActivPyPXFacturarDao.class);
	
	/**
	 * listado
	 */
	private static String listado=	"SELECT DISTINCT " +
											"oa.codigo as numeroorden, " +
											"oa.fecha as fechaordenbd, " +
											"to_char(oa.fecha, 'DD/MM/YYYY') as fechaorden, " +
											"s.consecutivo_ordenes_medicas as numerosolicitud, " +
											"oa.codigo_paciente as codigopaciente, " +
											"getnombrepersona(oa.codigo_paciente) as nombrespaciente, " +
											"getnombreservicio(getcodservordenambser(oa.codigo), "+ConstantesBD.codigoTarifarioCups+") as nombreservicio   " +
									"FROM " +
											"ordenes_amb_solicitudes oas " +
											"INNER JOIN ordenes_ambulatorias oa ON (oa.codigo=oas.orden) " +
											"INNER JOIN solicitudes s ON (s.numero_solicitud=oas.numero_solicitud)  " +
											"INNER JOIN cuentas c ON (c.id=s.cuenta) " +
											"INNER JOIN centros_costo cc ON (cc.codigo=c.area)  " +
											"INNER JOIN sub_cuentas sc on(c.id_ingreso=sc.ingreso) " +
									"WHERE " +
											"oa.pyp="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
											"and oa.estado="+ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+" " +
											"and oa.consulta_externa="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
											"and c.via_ingreso="+ConstantesBD.codigoViaIngresoConsultaExterna+" " +
											"and c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva+" " +
											"and sc.convenio=? " +
											"and cc.centro_atencion=? " +
											"and oa.institucion=? " +
									"order by " +
											"fechaordenbd ";
	
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listado(	Connection con,
									int codigoConvenio,
									int codigoCentroAtencion,
	        						int codigoInstitucion
								 )
	{
	    try
		{
	    	PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	cargarStatement.setInt(1, codigoConvenio);
	    	cargarStatement.setInt(2, codigoCentroAtencion);
			cargarStatement.setInt(3, codigoInstitucion);
			logger.info("\n\nlistado-->"+listado+ "codConvenio-->"+codigoConvenio+" centroAten-->"+codigoCentroAtencion+" cod Ins->"+codigoInstitucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			cargarStatement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado: SqlBaseActivPyPXFacturarDao "+e.toString());
			return new HashMap();
		}
	}
	
}