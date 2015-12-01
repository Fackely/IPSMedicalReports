/**
 * 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.tesoreria.DtoFiltroConsultaAbonos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseAbonosYDescuentosDao 
{

	/**
	 * Carga el código de la tabla mov_abonos
	 */
	private static final String cargarCodigoPKMovimientosAbonoStr="SELECT codigo FROM movimientos_abonos WHERE tipo=? AND codigo_documento=?";
	
	/**
	 * Método para consultar los abonos disponibles del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param ingreso
	 * @return
	 */
	public static double consultarAbonosDisponibles(Connection con,int codigoPaciente, String consultarAbonosDisponiblesStr, Integer ingreso)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con, consultarAbonosDisponiblesStr);
			pst.setInt(1,codigoPaciente);
			Log4JManager.info("Consulta abonos "+pst);
			if(ingreso != null)
			{
				pst.setInt(2,ingreso);
			}
			else
			{
				pst.setNull(2,Types.INTEGER);
			}
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getDouble("abono_disponible");
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en consultarAbonosDisponibles en SqlBaseAbonosYDescuentosDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método para insertar un movimiento de abonos
	 * @param con
	 * @param cadenadelaconsulta
	 * @param codigoPaciente
	 * @param codigoDocumento
	 * @param tipoDocumento
	 * @param valor
	 * @param institucion
	 * @param ingreso
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public static int insertarMovimientoAbonos(Connection con,String insertarMovimientoAbonosStr,
												int codigoPaciente,int codigoDocumento,int tipoDocumento,double valor,int institucion, Integer ingreso, int codigoCentroAtencion)
	{
		try
		{
			String sentenciaConsultaDuenio=
						"SELECT " +
							"centro_atencion_duenio AS duenio " +
						"FROM " +
							"manejopaciente.pacientes " +
						"WHERE codigo_paciente=?";
			PreparedStatementDecorator psdConsultaDuenio=new PreparedStatementDecorator(con, sentenciaConsultaDuenio);
			psdConsultaDuenio.setInt(1, codigoPaciente);
			ResultSetDecorator resultadoDuenio=new ResultSetDecorator(psdConsultaDuenio.executeQuery());
			int duenio=0;
			if(resultadoDuenio.next())
			{
				duenio=resultadoDuenio.getInt("duenio");
			}
			UtilidadBD.cerrarObjetosPersistencia(psdConsultaDuenio, resultadoDuenio, null);
			
			int resultado=ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con, insertarMovimientoAbonosStr);
			pst.setInt(1,codigoPaciente);
			pst.setInt(2,codigoDocumento);
			pst.setInt(3,tipoDocumento);
			pst.setDouble(4,valor);
			pst.setInt(5,institucion);
			if(duenio>0)
			{
				pst.setInt(6, duenio);
			}
			else
			{
				pst.setNull(6, Types.INTEGER);
			}
			if(ingreso!=null&&ingreso>0)
			{
				pst.setInt(7, ingreso);
			}
			else
			{
				pst.setNull(7, Types.INTEGER);
			}
			
			if(codigoCentroAtencion > 0)
			{
				pst.setInt(8, codigoCentroAtencion);
			}
			else
			{
				pst.setNull(8, Types.INTEGER);
			}
			
			resultado=pst.executeUpdate();
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
			return resultado;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en insertarMovimientoAbonos de SqlBaseAbonosYDescuentosDao: ",e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	
	/**
	 * Carga el codigo de la tabla movimientos_abonos dado el codigo de la factura y el tipo de mov
	 * @param con
	 * @param tipoMovAbonos
	 * @param codigoFactura
	 * @return
	 */
	public static int cargarCodigoPKMovimientosAbono(Connection con, int tipoMovAbonos, int codigoFactura)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoPKMovimientosAbonoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tipoMovAbonos);
			pst.setInt(2, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigo");
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en cargarCodigoPKMovimientosAbono en SqlBaseAbonosYDescuentosDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método para insertar un clon de un insert de mov de abonos, diferenciado por el tipo
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @param insertarClonMovimientoAbonosDadoCodigoStr
	 * @return
	 */
	public static int insertarClonMovimientoAbonosDadoCodigo(	Connection con,int codigoMovimientoAbono, int tipoMovimientoAbono, int codigoCentroAtencion, String insertarClonMovimientoAbonosDadoCodigoStr)
	{
	    try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con, insertarClonMovimientoAbonosDadoCodigoStr);
			pst.setInt(1,tipoMovimientoAbono);
			pst.setInt(2,codigoMovimientoAbono);
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en insertarClonMovimientoAbonosDadoCodigo de SqlBaseAbonosYDescuentosDao: ",e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static ArrayList<DtoMovmimientosAbonos> obtenerAbonosAplicadosFacturas(int codigoPersona) 
	{
		String consulta="SELECT " +
		" ma.codigo AS codigo, " +
		" f.codigo AS codigo_factura," +
		" ma.valor AS valor," +
		" ef.nombre AS estado, " +
		" ma.fecha AS fecha, " +
		" ma.hora AS hora, " +
		" ing.consecutivo AS ingreso, " +
		" nma.nombre AS nombre_tipo, " +
		" tma.operacion AS operacion, " +
		" ca.descripcion AS centro_atencion, " +
		" cadue.descripcion AS centro_atencion_duenio, " +
		" f.consecutivo_factura AS consecutivoFactura, nma.orden "+
" from facturacion.facturas f " +
" INNER JOIN facturacion.estados_factura_f ef on(f.estado_facturacion=ef.codigo) " +
" INNER JOIN tesoreria.movimientos_abonos ma on (f.institucion=ma.institucion AND ma.codigo_documento=f.codigo AND ma.tipo="+ConstantesBD.tipoMovimientoAbonoFacturacion+") " +
" LEFT OUTER JOIN administracion.centro_atencion ca ON (ca.consecutivo=ma.centro_atencion) " +
" INNER JOIN tesoreria.tipos_mov_abonos tma ON(tma.codigo=ma.tipo)" +
" INNER JOIN tesoreria.nombre_mov_abono nma ON(nma.tipo_mov_abono=tma.codigo)" +
" LEFT OUTER JOIN ingresos ing on(ing.id=ma.ingreso) " +
" LEFT OUTER JOIN administracion.centro_atencion cadue ON (cadue.consecutivo=ma.centro_atencion_duenio) " +
" where cod_paciente=? and valor_abonos>0  order by fecha, hora" ;
ArrayList<DtoMovmimientosAbonos> listaMovmimientos=new ArrayList<DtoMovmimientosAbonos>();

Connection con=UtilidadBD.abrirConexion();
try
{
PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
ps.setInt(1, codigoPersona);
ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
Log4JManager.info(consulta);
while(rs.next())
{
DtoMovmimientosAbonos dtoMovmimientosAbonos=new DtoMovmimientosAbonos();
dtoMovmimientosAbonos.setCodigo(rs.getInt("codigo"));
dtoMovmimientosAbonos.setCodigoDocumento(rs.getBigDecimal("codigo_factura"));
dtoMovmimientosAbonos.setValor(rs.getDouble("valor"));
dtoMovmimientosAbonos.setFecha(rs.getDate("fecha"));
dtoMovmimientosAbonos.setHora(rs.getString("hora"));
dtoMovmimientosAbonos.setIngreso(rs.getInt("ingreso"));
dtoMovmimientosAbonos.setEstado(rs.getString("estado"));
dtoMovmimientosAbonos.setNombreTipo(rs.getString("nombre_tipo"));
dtoMovmimientosAbonos.setOperacion(rs.getString("operacion"));
dtoMovmimientosAbonos.setCentroAtencion(rs.getString("centro_atencion"));
dtoMovmimientosAbonos.setCentroAtencionDuenio(rs.getString("centro_atencion_duenio"));

listaMovmimientos.add(dtoMovmimientosAbonos);
}
rs.close();
ps.close();
}
catch (SQLException e) 
{
Log4JManager.error("error -->",e);
}
UtilidadBD.closeConnection(con);
return listaMovmimientos;
}

	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static ArrayList<DtoMovmimientosAbonos> obtenerAbonosDevolucionRC(int codigoPersona) 
	{
		String consulta=
			"SELECT " +
								" ma.codigo AS codigo, " +
								" der.codigo as devolucion, " +
								" der.consecutivo as codigo_documento, " +
								" ma.fecha AS fecha, " +
								" ma.hora As hora, " +
								" ma.valor as valor, " +
								" getIntegridadDominio(der.estado) AS estado, " +
								//" ma.ingreso AS ingreso, " +
								" ing.consecutivo AS ingreso, "+
								" nma.nombre AS nombre_tipo, " +
								" tma.operacion AS operacion, " +
								" ca.descripcion AS centro_atencion, " +
								" cadue.descripcion AS centro_atencion_duenio, " +
								" rc.consecutivo_recibo AS reciboCaja, nma.orden "+
						" FROM tesoreria.devol_recibos_caja der " +
						" INNER JOIN tesoreria.recibos_caja rc on(der.numero_rc=rc.numero_recibo_caja and der.institucion=rc.institucion)  " +
						" INNER JOIN tesoreria.detalle_conceptos_rc dc on(rc.numero_recibo_caja=dc.numero_recibo_caja and rc.institucion=dc.institucion) " +
						" INNER JOIN tesoreria.detalle_conc_rc_x_paciente dcrp ON (dcrp.detalle_concepto_rc = dc.consecutivo ) "+
						" INNER JOIN movimientos_abonos ma ON (ma.institucion=rc.institucion " +
							" AND ma.tipo = "+ConstantesBD.tipoMovimientoAbonoDevolucionReciboCaja+"" +
							" AND ma.codigo_documento=der.consecutivo) " +
							" LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo=ma.centro_atencion) " +
						" INNER JOIN tesoreria.tipos_mov_abonos tma ON(tma.codigo=ma.tipo)" +
						" INNER JOIN tesoreria.nombre_mov_abono nma ON(nma.tipo_mov_abono=tma.codigo)" +
						" LEFT OUTER JOIN ingresos ing ON(ing.id=ma.ingreso) " +						
						" LEFT OUTER JOIN administracion.centro_atencion cadue ON (cadue.consecutivo=ma.centro_atencion_duenio) " +
						" WHERE dcrp.paciente=? and der.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"'  " +
						" order by fecha, hora" ;
	
		ArrayList<DtoMovmimientosAbonos> listaMovimientos=new ArrayList<DtoMovmimientosAbonos>();
		
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consulta);
			ps.setInt(1, codigoPersona);
			Log4JManager.info(consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoMovmimientosAbonos dtoMovmimientosAbonos=new DtoMovmimientosAbonos();
				dtoMovmimientosAbonos.setCodigo(rs.getInt("codigo"));
				dtoMovmimientosAbonos.setCodigoDocumento(rs.getBigDecimal("devolucion"));
				dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("codigo_documento"));
				dtoMovmimientosAbonos.setValor(rs.getDouble("valor"));
				dtoMovmimientosAbonos.setFecha(rs.getDate("fecha"));
				dtoMovmimientosAbonos.setHora(rs.getString("hora"));
				dtoMovmimientosAbonos.setIngreso(rs.getInt("ingreso"));
				dtoMovmimientosAbonos.setEstado(rs.getString("estado"));
				dtoMovmimientosAbonos.setNombreTipo(rs.getString("nombre_tipo"));
				dtoMovmimientosAbonos.setOperacion(rs.getString("operacion"));
				dtoMovmimientosAbonos.setCentroAtencion(rs.getString("centro_atencion"));
				dtoMovmimientosAbonos.setCentroAtencionDuenio(rs.getString("centro_atencion_duenio"));
				dtoMovmimientosAbonos.setConsecutivoReciboCaja(rs.getString("reciboCaja"));
				listaMovimientos.add(dtoMovmimientosAbonos);
			}
			rs.close();
			ps.close();

		}
		catch (SQLException e) 
		{
			Log4JManager.error("error -->",e);
		}
		UtilidadBD.closeConnection(con);
		return listaMovimientos;
}

	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static ArrayList<DtoMovmimientosAbonos> obtenerAbonosRecibosCaja(int codigoPersona) 
	{
		String consulta=
		    "SELECT " +
					" ma.codigo AS codigo, " +
					" rc.consecutivo_recibo as codigo_documento," +
					" ma.fecha AS fecha, " +
					" ma.hora AS hora, " +
					" ma.valor AS valor, " +
					" rc.estado AS estado, " +
					" erc.descripcion as nomestado, " +
					" ing.consecutivo AS ingreso, " +
					" nma.nombre AS nombre_tipo, " +
					" tma.operacion AS operacion, " +
					" ca.descripcion AS centro_atencion, " +
					" cadue.descripcion AS centro_atencion_duenio, nma.orden " +
			" FROM recibos_caja rc " +
				"INNER JOIN detalle_conceptos_rc dc ON (rc.numero_recibo_caja=dc.numero_recibo_caja and rc.institucion=dc.institucion) " +						
				"INNER JOIN tesoreria.detalle_conc_rc_x_paciente dcrp ON (dcrp.detalle_concepto_rc = dc.consecutivo ) "+
				"INNER JOIN conceptos_ing_tesoreria cit ON(cit.codigo=dc.concepto and cit.institucion=dc.institucion) " +
				"INNER JOIN estados_recibos_caja erc ON(rc.estado=erc.codigo) "+ 
				"INNER JOIN movimientos_abonos ma ON " +
					"(" +
							"ma.institucion=rc.institucion " +
						"AND ma.codigo_documento || '' =rc.numero_recibo_caja " +
						"AND ma.tipo = " +ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja+
					") " +
				"INNER JOIN tesoreria.tipos_mov_abonos tma ON(tma.codigo=ma.tipo) " +
				"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo=ma.centro_atencion) " +
				"INNER JOIN tesoreria.nombre_mov_abono nma ON(nma.tipo_mov_abono=tma.codigo) " +
				"LEFT OUTER JOIN ingresos ing ON(ing.id=ma.ingreso) " +
				"LEFT OUTER JOIN administracion.centro_atencion cadue ON (cadue.consecutivo=ma.centro_atencion_duenio) " +
			    "WHERE dcrp.paciente=? and cit.codigo_tipo_ingreso=?  order by fecha, hora" ;
	
	ArrayList<DtoMovmimientosAbonos> listaMovimientos=new ArrayList<DtoMovmimientosAbonos>();
	
	Connection con=UtilidadBD.abrirConexion();
	try
	{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setInt(1, codigoPersona);
		ps.setInt(2, ConstantesBD.codigoTipoIngresoTesoreriaAbonos);
		
		Log4JManager.info(consulta);
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		while(rs.next())
		{
			DtoMovmimientosAbonos dtoMovmimientosAbonos=new DtoMovmimientosAbonos();
			dtoMovmimientosAbonos.setCodigo(rs.getInt("codigo"));
			dtoMovmimientosAbonos.setCodigoDocumento(rs.getBigDecimal("codigo_documento"));
			dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("codigo_documento"));
			dtoMovmimientosAbonos.setValor(rs.getDouble("valor"));
			dtoMovmimientosAbonos.setFecha(rs.getDate("fecha"));
			dtoMovmimientosAbonos.setHora(rs.getString("hora"));
			dtoMovmimientosAbonos.setIngreso(rs.getInt("ingreso"));
			dtoMovmimientosAbonos.setEstado(rs.getString("nomestado"));
			dtoMovmimientosAbonos.setNombreTipo(rs.getString("nombre_tipo"));
			dtoMovmimientosAbonos.setOperacion(rs.getString("operacion"));
			dtoMovmimientosAbonos.setCentroAtencion(rs.getString("centro_atencion"));
			dtoMovmimientosAbonos.setCentroAtencionDuenio(rs.getString("centro_atencion_duenio"));
			listaMovimientos.add(dtoMovmimientosAbonos);
		}
		rs.close();
		ps.close();
	}
	catch (SQLException e) 
	{
		Log4JManager.error("error -->",e);
	}
	UtilidadBD.closeConnection(con);
	return listaMovimientos;
}	
	
	/**
	 * Lista todos los abonos de los pacientes
	 * Este m&eacute;todo se debe arreglar por cada consulta espec&iacute;fica
	 * de movimientos de abonos
	 * @param codigoPersona
	 * @return {@link ArrayList}<{@link DtoMovmimientosAbonos}> Lista con los elementos retornados
	 * @author Juan David Ram&iacute;rez L&oacute;pez
	 */
	public static ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneral(int codigoPersona) 
	{
		String consulta=
		    "SELECT " +
					" ma.codigo AS codigo, " +
					" ma.codigo_documento as codigo_documento," +
					" f.consecutivo_factura AS factura," +
					" ef.nombre AS estado, " +
					" ma.fecha AS fecha, " +
					" ma.hora AS hora, " +
					" ma.valor AS valor, " +
					" ca.descripcion AS centro_atencion, " +
					" ing.consecutivo AS ingreso, " +
					" tma.codigo AS tipo_mov, " +
					" f.consecutivo_factura AS consecutivoFactura," +
					" nma.nombre AS nombre_tipo, " +
					" tma.operacion AS operacion, nma.orden " +
			"FROM " +
				"tesoreria.movimientos_abonos ma " +
			"INNER JOIN " +
				"tesoreria.tipos_mov_abonos tma " +
					"ON(tma.codigo=ma.tipo)" +
			"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo=ma.centro_atencion) " +
			"INNER JOIN " +
			"tesoreria.nombre_mov_abono nma " +
				"ON(nma.tipo_mov_abono=tma.codigo)" +	
			"LEFT OUTER JOIN facturacion.facturas f on (f.institucion=ma.institucion AND ma.codigo_documento=f.codigo ) " +
			"LEFT OUTER JOIN facturacion.estados_factura_f ef on(f.estado_facturacion=ef.codigo) " +
			"LEFT OUTER JOIN " +
				"ingresos ing " +
					"ON(ing.id=ma.ingreso) " +
			"WHERE " +
					"ma.paciente=? " +
				"AND " +
					"ma.tipo NOT IN (" +
						ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja+", " +
						ConstantesBD.tipoMovimientoAbonoAnulacionReciboCaja+", " +
						ConstantesBD.tipoMovimientoAbonoDevolucionReciboCaja+", " +
						ConstantesBD.tipoMovimientoAbonoDevolucionAbono+", " +
						ConstantesBD.tipoMovimientoAbonoFacturacion+
					")  order by fecha, hora" ;
	
	ArrayList<DtoMovmimientosAbonos> listaMovimientos=new ArrayList<DtoMovmimientosAbonos>();
	final String   ESTADO_FACTURA_ANULADA = "Anulada";
	
	Connection con=UtilidadBD.abrirConexion();
	try
	{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setInt(1, codigoPersona);
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		while(rs.next())
		{
			DtoMovmimientosAbonos dtoMovmimientosAbonos=new DtoMovmimientosAbonos();
			dtoMovmimientosAbonos.setCodigo(rs.getInt("codigo"));
			dtoMovmimientosAbonos.setTipo(rs.getInt("tipo_mov"));
			dtoMovmimientosAbonos.setCodigoDocumento(rs.getBigDecimal("codigo_documento"));
			
			if (dtoMovmimientosAbonos.getTipo() == ConstantesBD.tipoMovimientoAbonoAnulacionFactura) {
				dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("factura"));
				dtoMovmimientosAbonos.setEstado(rs.getString("estado"));
			
				if (dtoMovmimientosAbonos.getEstado() != null && dtoMovmimientosAbonos.getEstado().trim().equals(ESTADO_FACTURA_ANULADA)) {
					dtoMovmimientosAbonos.setConsecutivoFactura(rs.getString("consecutivoFactura"));
				}
			
			}else{
				dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("codigo_documento"));
			}
			
			dtoMovmimientosAbonos.setValor(rs.getDouble("valor"));
			dtoMovmimientosAbonos.setFecha(rs.getDate("fecha"));
			dtoMovmimientosAbonos.setHora(rs.getString("hora"));
			dtoMovmimientosAbonos.setIngreso(rs.getInt("ingreso"));
			dtoMovmimientosAbonos.setNombreTipo(rs.getString("nombre_tipo"));
			dtoMovmimientosAbonos.setOperacion(rs.getString("operacion"));
			dtoMovmimientosAbonos.setCentroAtencion(rs.getString("centro_atencion"));
			
			listaMovimientos.add(dtoMovmimientosAbonos);
		}
		rs.close();
		ps.close();
	}
	catch (SQLException e) 
	{
		Log4JManager.error("error -->",e);
	}
	UtilidadBD.closeConnection(con);
	return listaMovimientos;
}

	/**
	 * Lista los movimientos de los abonos relacionados a las devoluciones de los recibos de caja
	 * @param codigoPersona
	 * @return {@link ArrayList}<{@link DtoMovmimientosAbonos}> Lista con los elementos retornados
	 * @author Juan David Ramírez López
	 */
	public static ArrayList<DtoMovmimientosAbonos> obtenerAbonosAnulaciones(int codigoPersona) 
	{
		String consulta=
		    "SELECT " +
					" ma.codigo AS codigo, " +
					" arc.numero_anulacion_rc as codigo_documento," +
					" ma.fecha AS fecha, " +
					" ma.hora AS hora, " +
					" ma.valor AS valor, " +
					" rc.estado AS estado, " +
					" erc.descripcion as nomestado, " +
					" ing.consecutivo AS ingreso, " +
					" nma.nombre AS nombre_tipo, " +
					" tma.operacion AS operacion, " +
					" ca.descripcion AS centro_atencion, " +
					" cadue.descripcion AS centro_atencion_duenio, " +
					" rc.consecutivo_recibo AS recibocaja, nma.orden "+
			" FROM recibos_caja rc " +
				"INNER JOIN detalle_conceptos_rc dc ON (rc.numero_recibo_caja=dc.numero_recibo_caja and rc.institucion=dc.institucion) " +						
				"INNER JOIN tesoreria.detalle_conc_rc_x_paciente dcrp ON (dcrp.detalle_concepto_rc = dc.consecutivo ) "+
				"INNER JOIN conceptos_ing_tesoreria cit ON(cit.codigo=dc.concepto and cit.institucion=dc.institucion) " +
				"INNER JOIN estados_recibos_caja erc ON(rc.estado=erc.codigo) "+ 
				"INNER JOIN tesoreria.anulacion_recibos_caja arc ON(arc.numero_recibo_caja = rc.numero_recibo_caja) " +
				"INNER JOIN movimientos_abonos ma ON " +
					"(" +
							"ma.institucion=rc.institucion " +
						"AND ma.codigo_documento || '' =arc.numero_anulacion_rc " +
						"AND ma.tipo = " +ConstantesBD.tipoMovimientoAbonoAnulacionReciboCaja+
					") " +
				"INNER JOIN tesoreria.tipos_mov_abonos tma ON(tma.codigo=ma.tipo) " +
				"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo=ma.centro_atencion) " +
				"INNER JOIN tesoreria.nombre_mov_abono nma ON(nma.tipo_mov_abono=tma.codigo) " +
				"LEFT OUTER JOIN ingresos ing ON(ing.id=ma.ingreso) " +
				"LEFT OUTER JOIN administracion.centro_atencion cadue ON (cadue.consecutivo=ma.centro_atencion_duenio) " +
			    "WHERE dcrp.paciente=? and cit.codigo_tipo_ingreso=? order by fecha, hora" ;
	
		ArrayList<DtoMovmimientosAbonos> listaMovimientos=new ArrayList<DtoMovmimientosAbonos>();
		
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			ps.setInt(2, ConstantesBD.codigoTipoIngresoTesoreriaAbonos);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoMovmimientosAbonos dtoMovmimientosAbonos=new DtoMovmimientosAbonos();
				dtoMovmimientosAbonos.setCodigo(rs.getInt("codigo"));
				dtoMovmimientosAbonos.setCodigoDocumento(rs.getBigDecimal("codigo_documento"));
				dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("codigo_documento"));
				dtoMovmimientosAbonos.setValor(rs.getDouble("valor"));
				dtoMovmimientosAbonos.setFecha(rs.getDate("fecha"));
				dtoMovmimientosAbonos.setHora(rs.getString("hora"));
				dtoMovmimientosAbonos.setIngreso(rs.getInt("ingreso"));
				dtoMovmimientosAbonos.setNombreTipo(rs.getString("nombre_tipo"));
				dtoMovmimientosAbonos.setOperacion(rs.getString("operacion"));
				dtoMovmimientosAbonos.setCentroAtencion(rs.getString("centro_atencion"));
				dtoMovmimientosAbonos.setCentroAtencionDuenio(rs.getString("centro_atencion_duenio"));
				dtoMovmimientosAbonos.setConsecutivoReciboCaja(rs.getString("recibocaja"));
				listaMovimientos.add(dtoMovmimientosAbonos);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error -->",e);
		}
		UtilidadBD.closeConnection(con);
		return listaMovimientos;
	}

	/**
	 * Método implementado para eliminar los abonos de un documento especifico
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @return
	 */
	public static ResultadoBoolean eliminarAbonos(Connection con,BigDecimal codigoMovimientoAbono,int tipoMovimientoAbono)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String consulta = "DELETE FROM tesoreria.movimientos_abonos WHERE codigo_documento = ? and tipo = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoMovimientoAbono.intValue());
			pst.setInt(2,tipoMovimientoAbono);
			pst.executeUpdate();
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en eliminarAbonos: ",e);
			resultado.setResultado(false);
			resultado.setDescripcion("Problemas al tratar de eliminar los abonos del documento "+codigoMovimientoAbono+": "+e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static BigDecimal obtenerValorReservadoAbono(int codigoPaciente, String consulta) 
	{
		BigDecimal valor= BigDecimal.ZERO; 
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoPaciente);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				valor= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en obtenerValorReservadoAbono: ",e);
		}
		return valor;
	}

	/**
	 * 
	 * @param codigoPersona
	 * @param dtoFiltro
	 * @return
	 */
	public static ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneralAvanzada(
			int codigoPersona, DtoFiltroConsultaAbonos dtoFiltro) {
		String consulta=
		    "SELECT " +
					" ma.codigo AS codigo, " +
					" coalesce(rc.consecutivo_recibo||'',ma.codigo_documento||'') as codigo_documento," +
					" f.consecutivo_factura AS factura," +
					" ef.nombre AS estado, " +
					" ma.fecha AS fecha, " +
					" ma.hora AS hora, " +
					" ma.valor AS valor, " +
					" ing.consecutivo AS ingreso, " +
					" nma.nombre AS nombre_tipo, " +
					" ca.descripcion AS centro_atencion, " +
					" tma.codigo AS tipo_mov, " +
					" f.consecutivo_factura AS consecutivoFactura," +
					" tma.operacion AS operacion " +
			"FROM " +
				"tesoreria.movimientos_abonos ma " +
			"INNER JOIN  tesoreria.tipos_mov_abonos tma ON(tma.codigo=ma.tipo)" +
			"INNER JOIN  tesoreria.nombre_mov_abono nma ON(nma.tipo_mov_abono=tma.codigo)" +
			"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo=ma.centro_atencion) " +
			"LEFT OUTER JOIN ingresos ing ON(ing.id=ma.ingreso) " +
			"LEFT OUTER JOIN recibos_caja rc ON(ma.codigo_documento || '' =rc.numero_recibo_caja AND ma.tipo = " +ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja +")" +
			"LEFT OUTER JOIN facturacion.facturas f on (f.institucion=ma.institucion AND ma.codigo_documento=f.codigo ) " +
			"LEFT OUTER JOIN facturacion.estados_factura_f ef on(f.estado_facturacion=ef.codigo) " +
			"WHERE " +
					"ma.paciente=? " ;
	
		
		
		
	ArrayList<DtoMovmimientosAbonos> listaMovimientos=new ArrayList<DtoMovmimientosAbonos>();
	
	if(dtoFiltro.getCentroAtencionBusqueda()>0)
	{
		consulta=consulta+" and ma.centro_atencion="+dtoFiltro.getCentroAtencionBusqueda();
	}
	if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaInicialBusqueda()) && !UtilidadTexto.isEmpty(dtoFiltro.getFechaFinalBusqueda()))
	{
		consulta=consulta+" and ma.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaInicialBusqueda())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaFinalBusqueda())+"'";
	}
	if(!UtilidadTexto.isEmpty(dtoFiltro.getIngresoBusqueda()))
	{
		consulta=consulta+" and ing.consecutivo='"+dtoFiltro.getIngresoBusqueda()+"'";
	}
	if(dtoFiltro.getTipoMovimientoBusqueda()>0)
	{
		consulta=consulta+" and ma.tipo="+dtoFiltro.getTipoMovimientoBusqueda();
	}
	
	Connection con=UtilidadBD.abrirConexion();
	try
	{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consulta);
		ps.setInt(1, codigoPersona);
		Log4JManager.info("-->"+ps);
		final String   ESTADO_FACTURA_ANULADA = "Anulada";
		
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		while(rs.next())
		{
			DtoMovmimientosAbonos dtoMovmimientosAbonos=new DtoMovmimientosAbonos();
			dtoMovmimientosAbonos.setCodigo(rs.getInt("codigo"));
			dtoMovmimientosAbonos.setTipo(rs.getInt("tipo_mov"));
			dtoMovmimientosAbonos.setCodigoDocumento(rs.getBigDecimal("codigo_documento"));
			
			if (dtoMovmimientosAbonos.getTipo() == ConstantesBD.tipoMovimientoAbonoAnulacionFactura) {
				dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("factura"));
				dtoMovmimientosAbonos.setEstado(rs.getString("estado"));
			
				if (dtoMovmimientosAbonos.getEstado() != null && dtoMovmimientosAbonos.getEstado().trim().equals(ESTADO_FACTURA_ANULADA)) {
					dtoMovmimientosAbonos.setConsecutivoFactura(rs.getString("consecutivoFactura"));
				}
			
			}else{
				dtoMovmimientosAbonos.setConsecutivoDocumento(rs.getString("codigo_documento"));
			}
			
			dtoMovmimientosAbonos.setValor(rs.getDouble("valor"));
			dtoMovmimientosAbonos.setFecha(rs.getDate("fecha"));
			dtoMovmimientosAbonos.setHora(rs.getString("hora"));
			dtoMovmimientosAbonos.setIngreso(rs.getInt("ingreso"));
			dtoMovmimientosAbonos.setNombreTipo(rs.getString("nombre_tipo"));
			dtoMovmimientosAbonos.setCentroAtencion(rs.getString("centro_atencion"));
			dtoMovmimientosAbonos.setOperacion(rs.getString("operacion"));
			listaMovimientos.add(dtoMovmimientosAbonos);
		}
		rs.close();
		ps.close();
	}
	catch (SQLException e) 
	{
		Log4JManager.error("error -->",e);
	}
	UtilidadBD.closeConnection(con);
	return listaMovimientos;
	}
	

}
