
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;




/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 17 /Ene/ 2006
 * Clase para las transacciones de la Consulta de Presupuestos
 */
public class SqlBaseConsultaPresupuestoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaPresupuestoDao.class);
	
	
	
	/**
	 * Statement para consultar los presupuestos según los parametros de búsqueda
	 */
	private final static String consultaPresupuestosStr=" SELECT p.consecutivo as consecutivo, " +
														" to_char(p.fecha_presupuesto, '"+ConstantesBD.formatoFechaAp+"') as fechaPresupuesto, " +
														" (pp.tipo_identificacion||' '||pp.numero_identificacion) as tipoNumeroId, " +
														" getnombrepersona(p.paciente) as paciente, " +
														" getnombreconvenio(p.convenio) as convenio, " +
														" administracion.getnombremedico(p.medico_tratante) as medico," +
														" getnomcentroatencion(p.centro_atencion) as nombreCentroAtencion " +
														" FROM presupuesto_paciente p " +
														" INNER JOIN personas pp ON(p.paciente=pp.codigo) " +
														" LEFT OUTER JOIN centro_atencion ca ON (p.centro_atencion=ca.consecutivo) " +
														" WHERE 1=1 ";
	
	/**
	 * Statement para consulta todo el detalle de la Factura según el consecutivo de la factura
	 */
	private final static String consultarDetallePresupuestoStr=" SELECT p.consecutivo as presupuesto, " +
														       " to_char(p.fecha_presupuesto, '"+ConstantesBD.formatoFechaAp+"') as fechaPresupuesto, " +
														       " p.hora_presupuesto as horaPresupuesto, " +
														       " p.login_usuario as usuario, " +
														       " pp.tipo_identificacion as tipoId, " +
														       " pp.numero_identificacion as numeroId, " +
														       " pp.primer_nombre as primerNombre, " +
														       " pp.segundo_nombre as segundoNombre, " +
														       " pp.primer_apellido as primerApellido, " +
														       " pp.segundo_apellido as segundoApellido, " +
														       " to_char(pp.fecha_nacimiento, '"+ConstantesBD.formatoFechaAp+"') as fechaNacimiento, " +
														       " s.nombre as sexo, " +
														       " pp.direccion as direccion, " +
														       " pp.telefono as telefono, " +
														       " getnombreconvenio(p.convenio) as convenio, " +
															   " getnombrepersona(p.medico_tratante) as medicoTratante, "+
															   " (p.diagnost_intervencion||' - '|| getnombrediagnostico(p.diagnost_intervencion,p.tipo_cie)) as diagnostico "+
														       " FROM presupuesto_paciente p " +
														       " INNER JOIN personas pp ON(p.paciente=pp.codigo) " +
														       " INNER JOIN sexo s ON(pp.sexo=s.codigo) " +
														       " LEFT OUTER JOIN cuentas c ON(p.paciente=c.codigo_paciente) " +
														       " WHERE p.consecutivo=?";
	
	/**
	 * Cadena con el statement necesario para consultar las intervenciones de un presupuesto dado
	 */
	private final static String consultarIntenervencionesStr=" SELECT (pi.servicio_intervencion||' - '||getnombreservicio(pi.servicio_intervencion, "+ConstantesBD.codigoTarifarioCups+")) as intervencion, " +
															 " (getcodigoespecialidad(pi.servicio_intervencion)||' - '||getnomespecialidadservicio(pi.servicio_intervencion)) as especialidad " +
															 " FROM presupuesto_intervencion pi " +
															 " WHERE pi.presupuesto=? ";
	
	/**
	 * Cadena con el statement necesario para consultar los articulos relacionados a un presupuesto dado
	 */
	private final static String consultarArticulosStr=" SELECT pa.articulo as codigoArticulo, " +
													  " getdescarticulo(pa.articulo) as articulo, " +
													  " getdescripcionarticulo(pa.articulo) as nombreArticulo, "+
													  " pa.cantidad as cantidad, " +
													  " pa.valor_unitario as valorUnitario, " +
													  " (pa.cantidad*pa.valor_unitario) as valorTotal, " +
													  " sg.codigo as codigoSubGrupo, "+
													  " sg.nombre as subgrupo, " +
													  " gi.codigo as codigoGrupo,"+
													  " gi.nombre as grupo, " +
													  " ci.codigo as codigoclase, "+
													  " ci.nombre as claseInventario, " +
													  " na.es_medicamento as esMedicamento, "+
													  " pa.esquema_tarifario as esquema," +
													  "	getnombreesquematarifario(pa.esquema_tarifario) as nombreesquema "+													  
													  " FROM presupuesto_articulos pa " +
													  " INNER JOIN articulo a ON(pa.articulo=a.codigo) " +
													  " INNER JOIN subgrupo_inventario sg ON(a.subgrupo=sg.codigo) " +
													  " INNER JOIN grupo_inventario gi ON(sg.grupo=gi.codigo) " +
													  " INNER JOIN clase_inventario ci ON(gi.clase=ci.codigo) "+
													  " INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo AND a.institucion=na.institucion) "+
													  " WHERE pa.presupuesto=? ";
	 
	/**
	 * Cadena con el statement necesario para consultar los articulos relacionados a un presupuesto dado
	 */
	private final static String consultarArticulosInsumosStr=" SELECT pa.articulo as codigoArticulo, " +
															  " getdescarticulo(pa.articulo) as articulo, " +
															  " getdescripcionarticulo(pa.articulo) as nombreArticulo, "+
															  " pa.cantidad as cantidad, " +
															  " pa.valor_unitario as valorUnitario, " +
															  " (pa.cantidad*pa.valor_unitario) as valorTotal, " +
															  " sg.codigo as codigoSubGrupo, "+
															  " sg.nombre as subgrupo, " +
															  " gi.codigo as codigoGrupo,"+
															  " gi.nombre as grupo, " +
															  " ci.codigo as codigoclase, "+
															  " ci.nombre as claseInventario, " +
															  " na.es_medicamento as esMedicamento," +
															  " pa.esquema_tarifario as esquema," +
															  "	getnombreesquematarifario(pa.esquema_tarifario) as nombreesquema "+
															  " FROM presupuesto_articulos pa " +
															  " INNER JOIN articulo a ON(pa.articulo=a.codigo) " +
															  " INNER JOIN subgrupo_inventario sg ON(a.subgrupo=sg.codigo) " +
															  " INNER JOIN grupo_inventario gi ON(sg.grupo=gi.codigo) " +
															  " INNER JOIN clase_inventario ci ON(gi.clase=ci.codigo) " +
															  " INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo AND a.institucion=na.institucion) "+
															  " WHERE pa.presupuesto=? "+
															  " AND na.es_medicamento='"+ConstantesBD.acronimoNo+"'";
	
	/**
	 * Cadena con el statement necesario para consultar los servicios relacionados a un presupuesto dado
	 */
	private final static String consultarServiciosStr=" SELECT ps.servicio as codigoServicio, " +
												      " (ps.servicio||' - '||getnombreservicio(ps.servicio, "+ConstantesBD.codigoTarifarioCups+")) as servicio, " +
													  " ps.cantidad as cantidad, " +
													  " ps.valor_unitario as valorUnitario, " +
													  " (ps.cantidad*ps.valor_unitario) as valorTotal, " +
													  " gs.descripcion as nombreGrupo, "+
													  " s.grupo_servicio as codigoGrupo," +
													  " ps.esquema_tarifario as esquema," +
													  "	getnombreesquematarifario(ps.esquema_tarifario) as nombreesquema "+
													  " FROM presupuesto_servicios ps " +
													  " INNER JOIN servicios s ON(ps.servicio=s.codigo) "+
													  " INNER JOIN grupos_servicios gs ON(s.grupo_servicio=gs.codigo) "+
													  " WHERE ps.presupuesto=? ";
	
	
	/**
	 * Cadena con el statement necesario para consultar los profesionales de la salud que
	 * su ocupacion medica sea estrictamente "MEDICO"
	 */
	private final static String consultarMedicosStr=" SELECT getnombrepersona(m.codigo_medico) as medico, " +
													" m.codigo_medico as codigoMedico " +
													" FROM medicos m WHERE m.ocupacion_medica = ? OR m.ocupacion_medica = ? "+
													" ORDER BY medico ";
	
	/**
	 * Statement para consultar los formatos de impresión existentes
	 */
	private final static String consultarFormatosExistentesStr=" SELECT fip.codigo as codigo, " +
														  	   " fip.nombre_formato as nombreFormato, " +
															   " fip.titulo_formato as tituloFormato " +
															   " FROM formato_imp_presupuesto fip " +
															   " WHERE fip.institucion=? " +
															   " ORDER BY fip.nombre_formato ";
	
	/**
	 * Cadena con el statement necesario para consultar los tipos de identificacion existentes
	 */
	private final static String consultarTiposIdStr=" SELECT ti.acronimo as acronimo, " +
													" ti.nombre as descripcion " +
													" FROM tipos_identificacion ti " +
													" INNER JOIN tipos_id_institucion tii ON(ti.acronimo=tii.acronimo) " +
													" WHERE tii.es_consecutivo= "+ ValoresPorDefecto.getValorFalseParaConsultas();
	
	
	
	/**
	 * Método para consultar el detalle básico de un presupuesto
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDetallePresupuesto(Connection con, int codigoPresupuesto)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarDetallePresupuestoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPresupuesto);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle de un presupuesto : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	
	
	
	/**
	 * Método para buscar un presupuesto segun unos parametros dados
	 * @param con
	 * @param presupuestoInicial
	 * @param presupuestoFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param tipoId
	 * @param numeroId
	 * @param codigoMedico
	 * @param responsable
	 * @return
	 */
	public static HashMap busquedaPresupuestos (Connection con,int presupuestoInicial, int presupuestoFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, String tipoId, int numeroId, int codigoMedico, int responsable)  throws SQLException
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
					if(presupuestoInicial>0 && presupuestoFinal>0)
					{
						avanzadaStr+=" AND p.consecutivo between "+presupuestoInicial +" and "+presupuestoFinal;
					}
					if(!fechaElaboracionIncial.equals("") && !fechaElaboracionFinal.equals(""))
					{
						
						avanzadaStr+=" AND p.fecha_presupuesto between '"+UtilidadFecha.conversionFormatoFechaABD(fechaElaboracionIncial)+"' and  '"+UtilidadFecha.conversionFormatoFechaABD(fechaElaboracionFinal)+"' ";
					}
					if(!tipoId.equals(""))
					{
						avanzadaStr+=" AND pp.tipo_identificacion = '"+tipoId+"' ";	
					}
					if(!(numeroId+"").equals("") && !(numeroId+"").equals("0"))
					{
						avanzadaStr+=" AND pp.numero_identificacion= "+numeroId+" ";
					}
					if(codigoMedico>0)
					{
						avanzadaStr+=" AND p.medico_tratante="+codigoMedico+" ";
					}
					if(responsable!=-1)
					{	
						avanzadaStr+=" AND p.convenio="+responsable+" ";					
					}
					String consulta= consultaPresupuestosStr + avanzadaStr;
					ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
					ps.close();
					return mapaRetorno;
				}
				catch(SQLException e)
				{
					logger.warn(e+"Error en Busqueda de los presupuestos : [SqlBaseConsultaPresupuestoDao] "+e.toString() );
					return null;
				}	    
	}
	
	
	/**
	 * Método para consultar las intervenciones (servicio - especialidad) de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarIntenervenciones(Connection con, int codigoPresupuesto)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarIntenervencionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPresupuesto);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de las intervenciones de un presupuesto : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para consultar los Articulos relacionados a un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarArticulos(Connection con, int codigoPresupuesto)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPresupuesto);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los arituclos de un presupuesto : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	/**
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarArticulosMedIns(Connection con, int codigoPresupuesto, boolean dividirMedIns)  throws SQLException
	{
		String consulta = consultarArticulosStr;
		try
		{
			if(dividirMedIns)
			{
				consulta += "AND na.es_medicamento = '"+ConstantesBD.acronimoSi+"' ";
			}
			else
			{
				consulta += "AND na.es_medicamento = '"+ConstantesBD.acronimoNo+"' ";
			}
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPresupuesto);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los arituclos de un presupuesto : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para consultar los Articulos relacionados a un presupuesto dado que sean insumos
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarArticulosInsumos(Connection con, int codigoPresupuesto)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosInsumosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPresupuesto);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los arituclos de un presupuesto : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	
	
	/**
	 * Método para consultar los servicios relacionados a un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarServicios(Connection con, int codigoPresupuesto)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPresupuesto);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los servicios de un : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	/**
	 * Metodo para consultar los profesionales de la salud que
	 * su ocupacion medica sea estrictamente "MEDICO"
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarMedicos(Connection con, int codigoOcupMedicoGeneral, int codigoOcupMedicoEspecialista)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarMedicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoOcupMedicoGeneral);
			cargarStatement.setInt(2, codigoOcupMedicoEspecialista);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los medicos : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método apra consultar ñlos tipos de identificacion existentes en el sistema
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarTiposId(Connection con)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarTiposIdStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los tipos de identificacion : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para consultar los formatos de impresion existentes en el moento de descidir la impresion
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFormatosExistentes(Connection con, int institucion)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarFormatosExistentesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
	        cargarStatement.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los servicios de un : [SqlBaseConsultaPresupuestoDao] "+e.toString());
			return null;
		}
	}
}
