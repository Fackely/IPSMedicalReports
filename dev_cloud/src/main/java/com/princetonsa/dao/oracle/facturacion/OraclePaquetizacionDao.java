/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dao.oracle.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.facturacion.PaquetizacionDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBasePaquetizacionDao;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class OraclePaquetizacionDao implements PaquetizacionDao {

	public static String cadenaConsultaSolSubcuenta="SELECT " +
														" to_char(getFechaSolicitud(ss.solicitud),'dd/mm/yyyy') as fechasolicitud," +
														" ss.solicitud as solicitud," +
														" ss.sub_cuenta as subcuenta," +
														" ss.servicio as servicio," +
														" getnombreservicio(ss.servicio,"+ConstantesBD.codigoTarifarioCups+") as nomservicio," +
														" getcodigocups(ss.servicio,"+ConstantesBD.codigoTarifarioCups+") as codigocups," +
														" ss.articulo as articulo," +
														" getConsecutivoSolicitud(ss.solicitud) as consecutivosolicitud," +
														" getdescripcionarticulo(ss.articulo) as nomarticulo," +
														" ss.cuenta as cuenta," +
														" ss.tipo_solicitud as tiposolicitud," +
														" getnomtiposolicitud(ss.tipo_solicitud) as nomtiposolicitud," +
														" ss.servicio_cx as serviciocx," +
														" getnombreservicio(ss.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as nomserviciocx," +
														" getcodigocups(ss.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as codigocupscx," +
														" ss.tipo_asocio as tipoasocio," +
														" dc.facturado as facturado," +
														" getnombretipoasocio(ss.tipo_asocio) as nomtipoasocio, " +
														" getCodigoCCSolicita(ss.solicitud) as codccsolicita," +
														" getnomcentrocosto(getCodigoCCSolicita(ss.solicitud)) as nomccsolicita," +
														" getCodigoCCEjecuta(ss.solicitud) as codccejecuta," +
														" getnomcentrocosto(getCodigoCCEjecuta(ss.solicitud)) as nomccejecuta," +
														" getNumRespFacSolServicio(ss.solicitud,ss.servicio) as numresfactservicio," +
														" getNumRespFacSolArticulo(ss.solicitud,ss.articulo) as numresfactarticulo," +
														" getNumRespSolServinclufac(ss.solicitud,ss.servicio,'"+ConstantesBD.acronimoSi+"') as numresservicio," +
														" getNumRespSolArtinclufac(ss.solicitud,ss.articulo,'"+ConstantesBD.acronimoSi+"') as numresarticulo," +
														" coalesce(dc.det_cx_honorarios,-1) as detcxhonorarios," +
														" coalesce(dc.det_asocio_cx_salas_mat,-1) as detasicxsalasmat," +
														" dc.estado as estado," +
														" getestadosolfac(dc.estado) as descestado," +
														" case when ss.servicio is null then getcanttotalservartresp(ss.solicitud,ss.articulo,'"+ConstantesBD.acronimoSi+"',ss.servicio_cx,dc.facturado,ss.tipo_asocio,dc.sub_cuenta,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat) else getcanttotalservartresp(ss.solicitud,ss.servicio,'"+ConstantesBD.acronimoSi+"',ss.servicio_cx,dc.facturado,ss.tipo_asocio,dc.sub_cuenta,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat) end as cantidadcargada " +
													" from solicitudes_subcuenta ss " +
													" inner join det_cargos dc on (dc.cod_sol_subcuenta=ss.codigo) " +
													" where ss.sub_cuenta=? " +
															" and (paquetizado='N' or(paquetizado='S' and ss.sub_cuenta=(SELECT DISTINCT p.sub_cuenta from paquetizacion p inner join detalle_paquetizacion dp on (dp.codigo_paquetizacion=p.codigo) where dp.numero_solicitud=ss.solicitud and ((dp.servicio=ss.servicio and dp.articulo is null) or (dp.articulo=ss.articulo and dp.servicio is null))))) " +
															" and dc.estado in('"+ConstantesBD.codigoEstadoFExento+"','"+ConstantesBD.codigoEstadoFCargada+"') and ss.tipo_solicitud<>"+ConstantesBD.codigoTipoSolicitudPaquetes+" AND ss.eliminado='"+ConstantesBD.acronimoNo+"' " +
													" GROUP BY ss.solicitud,ss.sub_cuenta,ss.servicio,ss.articulo,ss.cuenta,ss.tipo_solicitud,ss.servicio_cx,ss.tipo_asocio,dc.estado,dc.facturado,dc.sub_cuenta,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat";//traer todos los servicios, menos los paquetes.

	
	/**
	 * 
	 */
	public HashMap consultarPaquetesAsocioadosResponsableSubcuenta(Connection con,String subCuenta) 
	{
		return SqlBasePaquetizacionDao.consultarPaquetesAsocioadosResponsableSubcuenta(con,subCuenta);
	}

	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarServiciosPaquetes(Connection con, String[] codigoPaquetizaciones) 
	{
		return SqlBasePaquetizacionDao.consultarServiciosPaquetes(con,codigoPaquetizaciones);
	}
	

	/**
	 * 
	 */
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaPaquetizar(Connection con, String subCuenta) 
	{
		return SqlBasePaquetizacionDao.obtenerSolicitudesSubCuentaPaquetizar(con,subCuenta, cadenaConsultaSolSubcuenta);
	}


	/**
	 * 
	 */
	public HashMap obtenerParametrosServicio(Connection con, String codServicio) 
	{
		return SqlBasePaquetizacionDao.obtenerParametrosServicio(con,codServicio);
	}
	


	/**
	 * 
	 */
	public HashMap obtenerParametrosArticulos(Connection con, String codArticulo) 
	{
		return SqlBasePaquetizacionDao.obtenerParametrosArticulos(con, codArticulo);
	}
	
	/**
	 * 
	 */
	public int insertarEncabezadoPaquetizacion(Connection con, HashMap vo) 
	{
		String cadena="select seq_paquetizacion.nextval from dual";
		int codigo=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigo=rs.getInt(1);
			}
				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		if(codigo>0)
			return SqlBasePaquetizacionDao.insertarEncabezadoPaquetizacion(con, vo,codigo);
		return codigo;
	}


	/**
	 * 
	 */
	public boolean eliminarPaquetizacion(Connection con, String codPaquetizacion,String numeroSolicitud,boolean eliminarEncabezado) 
	{
		return SqlBasePaquetizacionDao.eliminarPaquetizacion(con, codPaquetizacion,numeroSolicitud,eliminarEncabezado);
	}


	/**
	 * 
	 */
	public boolean guardarDetallePaquete(Connection con, HashMap vo) 
	{
		String cadenaInsertDetallePaquetizacion="INSERT INTO detalle_paquetizacion (" +
													" codigo," +
													" codigo_paquetizacion," +
													" numero_solicitud," +
													" servicio," +
													" articulo," +
													" cantidad," +
													" servicio_cx," +
													" tipo_asocio," +
													" usuario_modifica," +
													" fecha_modifica," +
													" hora_modifica," +
													" principal," +
													" det_cx_honorarios," +
													" det_asocio_cx_salas_mat" +
													
													") " +
													" values (seq_det_paquetizacion.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		return SqlBasePaquetizacionDao.guardarDetallePaquete(con, vo,cadenaInsertDetallePaquetizacion);

	}
	

	/**
	 * 
	 */
	public HashMap consultarLiquidacionPaquete(Connection con, String subCuenta) 
	{
		return SqlBasePaquetizacionDao.consultarLiquidacionPaquete(con, subCuenta);
	}
	

	/**
	 * 
	 */
	public boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt, int codigoDetPadre, boolean esServicio,String facturado) 
	{
		return SqlBasePaquetizacionDao.actualizarCantidadDetCargo(con, cantidad,subCuenta,numeroSolicitud,codServArt,codigoDetPadre,esServicio,facturado);
	}


	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona,boolean aplicarValidaciones)
	{
		return SqlBasePaquetizacionDao.consultarIngresos(con,codigoPersona,aplicarValidaciones);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cantidad
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param servicio
	 * @param servicioCX
	 * @param tipoAsocio
	 * @param codigoDetPadre
	 * @param facturado
	 * @return
	 */
	public boolean actualizarCantidadDetCargoServicioCx(Connection con,int cantidad, String subCuenta, String numeroSolicitud,String servicio, String servicioCX, int tipoAsocio,int codigoDetPadre, String facturado,int detcxhonorarios,int detasicxsalasmat)
	{
		return SqlBasePaquetizacionDao.actualizarCantidadDetCargoServicioCx(con, cantidad,subCuenta,numeroSolicitud,servicio,servicioCX,tipoAsocio,codigoDetPadre,facturado,detcxhonorarios,detasicxsalasmat);

	}
}
