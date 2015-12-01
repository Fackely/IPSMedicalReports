package com.princetonsa.dao.postgresql.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoInteger;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.facturacion.FacturaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseFacturaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoAsociosDetalleFactura;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlFacturaDao implements FacturaDao
{
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap obtenerSubCuentasAFacturar(Connection con, String idIngreso)
	{
		return SqlBaseFacturaDao.obtenerSubCuentasAFacturar(con, idIngreso);
	}
	
	/**
	 * metodo para obtener el cargo total a facturar x responsable
	 * @param con
	 * @param cuentas
	 * @param codigoConvenios
	 * @return
	 */
	public double obtenerValorCargoTotalAFacturarXSubCuenta(Connection con, Vector cuentas, double subCuenta)
	{
		return SqlBaseFacturaDao.obtenerValorCargoTotalAFacturarXSubCuenta(con, cuentas, subCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public ArrayList<DtoDetalleFactura> proponerDetalleFacturaDesdeCargos(Connection con, DtoFactura dtoFactura, int estadoCargo)
	{
		return SqlBaseFacturaDao.proponerDetalleFacturaDesdeCargos(con, dtoFactura, estadoCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public ResultadoInteger insertar(Connection con, DtoFactura dtoFactura)
	{
		return SqlBaseFacturaDao.insertar(con, dtoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @param estado
	 * @return
	 */
	public int empezarProcesoFacturacionTransaccional(	Connection con,	int idCuenta, 
	        											String loginUsuario, String estado, String idSesion)
	{
		String hora= ValoresPorDefecto.getSentenciaHoraActualBDTipoTime();
		return SqlBaseFacturaDao.empezarProcesoFacturacionTransaccional(con, idCuenta, loginUsuario, estado, idSesion, hora);
	}
	
	/**
	 * Método para terminar el proceso de facturación
	 * @param con
	 * @param idCuenta
	 * @return true si se finalizó correctamente
	 */
	public int finalizarProcesoFacturacionTransaccional(Connection con, int idCuenta, String estado, String idSesion)
	{
		return SqlBaseFacturaDao.finalizarProcesoFacturaciontransaccional(con, idCuenta, estado, idSesion);
	}
	
	/**
	 * Método para cancelar el proceso de facturación (transaccional)
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return numero mayor que cero (0) si se realizó correctamente la cancelación
	 */
	public int cancelarProcesoFacturacionTransaccional(Connection con, int idCuenta, String estado, String idSesion)
	{
		return SqlBaseFacturaDao.cancelarProcesoFacturacionTransaccional(con, idCuenta, estado, idSesion);
	}
	
	/**
	 * metodo para cargar los detalles de la prefactura
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public HashMap proponerPreFactura(Connection con, DtoFactura dtoFactura)
	{
		/*
		//PARTE DE SERVICIOS
		String consulta="(SELECT " +
							"s.centro_costo_solicitado as centrocosto, " +
							"getnomcentrocosto(s.centro_costo_solicitado) as nombrecentrocosto, "+
							"dc.servicio as codarticuloservicio, " +
							"getnombreservicio(dc.servicio, "+ConstantesBD.codigoTarifarioCups+") as descarticuloservicio, " +
							"coalesce(sum(dc.cantidad_cargada),0) as cantidadcargo, " +
							"sum((coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,1)) as valortotal " +
						"FROM " +
							"solicitudes_subcuenta ssc " +
							"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
							"INNER JOIN det_cargos dc on(dc.cod_sol_subcuenta=ssc.codigo) " +
							"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
						"WHERE " +
							"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
							"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
							"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
							"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
							"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
							"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " +///con esto solo cargamos el encabezado de ese paquete
							"AND dc.articulo is null " + 
							"AND dc.servicio_cx is null " +
							"AND dc.cantidad_cargada>0 "+
							"group by centrocosto, nombrecentrocosto, codarticuloservicio " +
							"order by nombrecentrocosto, descarticuloservicio) ";
		//PARTE DE ARTICULOS					
		consulta+=		"UNION " +
						"(SELECT " +
							"s.centro_costo_solicitado as centrocosto, " +
							"getnomcentrocosto(s.centro_costo_solicitado) as nombrecentrocosto, "+
							"dc.articulo as codarticuloservicio, " +
							"getdescarticulo(dc.articulo) as descarticuloservicio, " +
							"coalesce(sum(dc.cantidad_cargada), 0) as cantidadcargo, " +
							"sum((coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,1)) as valortotal " +
						"FROM " +
							"solicitudes_subcuenta ssc " +
							"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
							"INNER JOIN det_cargos dc on(dc.cod_sol_subcuenta=ssc.codigo) " +
							"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
						"WHERE " +
							"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
							"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
							"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
							"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
							"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
							"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " + ///con esto solo cargamos el encabezado de ese paquete
							"AND dc.servicio is null " + 
							"AND dc.servicio_cx is null " +
							"AND dc.cantidad_cargada>0 "+
							"group by centrocosto, nombrecentrocosto, codarticuloservicio " +
							"order by nombrecentrocosto, descarticuloservicio) ";
		
		//TENEMOS QUE UNIRLE LA SUMA DE TODOS LOS ASOCIO DE LAS CX, SIMULACION CX
		consulta+= 	"UNION "+
					"(SELECT " +
						"s.centro_costo_solicitado as centrocosto, " +
						"getnomcentrocosto(s.centro_costo_solicitado) as nombrecentrocosto, "+
						"dc.servicio_cx as codarticuloservicio, " +
						"getnombreservicio(dc.servicio_cx, "+ConstantesBD.codigoTarifarioCups+")||' ('|| getNomTipoServicio(dc.servicio_cx)||') ' as descarticuloservicio, " +
						
						// **** Campo cantidadcargo
						"(SELECT " +
							"SUM(tabla.cantidad) " +
						"FROM " +
							"(SELECT " +
								"dc.cantidad_cargada as cantidad " +
							"FROM " +
								"det_cargos dc " +
							"INNER JOIN " +
								"sub_cuentas sc ON (sc.sub_cuenta=dc.sub_cuenta) " +
							"INNER JOIN " +
								"solicitudes_subcuenta ssc on (dc.cod_sol_subcuenta=ssc.codigo)  " +
							"WHERE " +
								"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
								"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
								"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
								"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
								"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " +///con esto solo cargamos el encabezado de ese paquete
								"AND dc.articulo is null " + 
								"AND dc.servicio_cx is not null " +
								"AND dc.cantidad_cargada>0 "+
								"AND dc.valor_unitario_cargado >0 "+
							"GROUP BY " +
								"dc.solicitud, dc.cantidad_cargada" +
						") tabla ) as cantidadcargo, " +
						// ****
						
						"getValorCxCompleta(ssc.solicitud, dc.sub_cuenta, dc.servicio_cx, '"+ConstantesBD.acronimoNo+"', '"+ConstantesBD.acronimoNo+"',"+ConstantesBD.codigoEstadoFCargada+") as valortotal " +
					"FROM " +
						"solicitudes_subcuenta ssc " +
						"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
						"INNER JOIN det_cargos dc on(dc.cod_sol_subcuenta=ssc.codigo) " +
						"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
					"WHERE " +
						"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
						"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
						"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
						"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
						"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
						"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " +///con esto solo cargamos el encabezado de ese paquete
						"AND dc.articulo is null " + 
						"AND dc.servicio_cx is not null " +
						"AND dc.cantidad_cargada>0 "+
						"AND dc.valor_unitario_cargado >0 "+
						"group by centrocosto, nombrecentrocosto, codarticuloservicio, valortotal " +
						"order by nombrecentrocosto, descarticuloservicio) ";
		*/
		/**
		* Tipo Modificacion: Segun incidencia 5837
		* Autor: Alejandro Aguirre Luna
		* usuario: aleagulu
		* Fecha: 21/02/2013
		* Descripcion: Consulta que reemplaza a la anteriormente comentada.
		* 			   Esta consulta trae el mismo numero de tuplas pero ahora en 
		* 			   caso que el servicio sea prestado por SOAT, sale el codigo 
		* 			   correspondiente. 
		**/
		String consulta =
				"SELECT * FROM(" +
				" (SELECT 1 AS tipo, " +
				"s.centro_costo_solicitado AS centrocosto," +
				" 	getnomcentrocosto(s.centro_costo_solicitado) AS nombrecentrocosto," +
				" 	dc.servicio AS codarticuloservicio," +
				"  getnombreservicio(dc.servicio, "+ConstantesBD.codigoTarifarioCups+") as descarticuloservicio, "+ 
				/*" 	getnombreservicio(dc.servicio, 0) AS descarticuloservicio," +*/
				" 	COALESCE(SUM(dc.cantidad_cargada),0) AS cantidadcargo," +
				" 	sum((COALESCE(dc.valor_unitario_cargado,0) + COALESCE(dc.valor_unitario_recargo,0) - COALESCE(dc.valor_unitario_dcto,0)) *" +
				" 	COALESCE(dc.cantidad_cargada,1)) AS valortotal" +
				" 	FROM solicitudes_subcuenta ssc" +
				" 		INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta)" +
				" 		INNER JOIN det_cargos dc ON(dc.cod_sol_subcuenta=ssc.codigo)" +
				" 		INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud)" +
				" WHERE dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" "  +
				"	AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+")" +
				"	AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
				"	AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
				"	AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
				"	AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " +
				"	AND dc.articulo is null" +
				"	AND dc.servicio_cx is null" +
				"	AND dc.cantidad_cargada>0" +
				" GROUP BY s.centro_costo_solicitado, getnomcentrocosto(s.centro_costo_solicitado), dc.servicio)" +
				" UNION ALL " +
				" (SELECT 2 AS tipo, " +
				" s.centro_costo_solicitado AS centrocosto, " +
				"	getnomcentrocosto(s.centro_costo_solicitado) AS nombrecentrocosto," +
				"	dc.articulo AS codarticuloservicio," +
				"	getdescarticulo(dc.articulo) AS descarticuloservicio," +
				"	COALESCE(SUM(dc.cantidad_cargada), 0) AS cantidadcargo," +
				"	SUM((COALESCE(dc.valor_unitario_cargado,0) + COALESCE(dc.valor_unitario_recargo,0) - COALESCE(dc.valor_unitario_dcto,0)) *" +
				"		COALESCE(dc.cantidad_cargada,1)) AS valortotal" +
				"	FROM solicitudes_subcuenta ssc " +
				" INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta)" +
				" INNER JOIN det_cargos dc ON(dc.cod_sol_subcuenta=ssc.codigo) " +
				" INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
				" WHERE " +
				" 	dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
				" 	AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
				" 	AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
				"	AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
				" 	AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
				" 	AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " +
				" 	AND dc.servicio IS NULL " +
				" 	AND dc.servicio_cx IS NULL" +
				" 	AND dc.cantidad_cargada>0 " +
				" GROUP BY s.centro_costo_solicitado,getnomcentrocosto(s.centro_costo_solicitado) ,dc.articulo)" +
				" UNION ALL (" +
				" SELECT DISTINCT 1 AS tipo," +
				" s.centro_costo_solicitado as centrocosto," +
				" getnomcentrocosto(s.centro_costo_solicitado) as nombrecentrocosto," +
				" dc.servicio_cx as codarticuloservicio, " +
				" getnombreservicio(dc.servicio_cx, "+ConstantesBD.codigoTarifarioCups+") as descarticuloservicio, "+ 
			    /*" getnombreservicio(dc.servicio_cx, 0)||' ('|| getNomTipoServicio(dc.servicio_cx)||') ' as descarticuloservicio," +*/
				" (SELECT" +
				" SUM(tabla.cantidad)" +
				" FROM " +
				"	(SELECT DISTINCT" +
				"	dca.cantidad_cargada as cantidad, " +
				"	dca.solicitud as sol, " +
				"	dca.servicio_cx as serv " +
				" FROM" +
				"	det_cargos dca" +
				" INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=dca.sub_cuenta)" +
				" INNER JOIN solicitudes_subcuenta ssc on (dca.cod_sol_subcuenta=ssc.codigo)" +
				" WHERE" +
				" 	dca.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
				"	AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
				"	AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
				"	AND dca.estado="+ConstantesBD.codigoEstadoFCargada+" " +
				"	AND dca.facturado='"+ConstantesBD.acronimoNo+"' "  +
				"	AND dca.paquetizado='"+ConstantesBD.acronimoNo+"' " +
				"	AND dca.articulo is null  " +
				"	AND dca.servicio_cx is not null " +
				"	AND dca.cantidad_cargada>0" +
				"	AND dca.valor_unitario_cargado >0) tabla" +
				" WHERE tabla.serv=dc.servicio_cx) as cantidadcargo," +
				"	getValorCxCompleta(ssc.solicitud, dc.sub_cuenta, dc.servicio_cx,'N','N',3) as valortotal" +
				" FROM" +
				" 	solicitudes_subcuenta ssc" +
				"	INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta)" +
				"	INNER JOIN det_cargos dc on(dc.cod_sol_subcuenta=ssc.codigo)" +
				"	INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud)" +
				" WHERE " +
				"	dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
				"	AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+")" +
				"	AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
				"	AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
				"	AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
				"	AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' "  +
				"	AND dc.articulo is null " +
				"	AND dc.servicio_cx is not null " +
				"	AND dc.cantidad_cargada>0" +
				" 	AND dc.valor_unitario_cargado >0" +
				" GROUP BY s.centro_costo_solicitado, " +
				"	getnomcentrocosto(s.centro_costo_solicitado), " +
				"	dc.servicio_cx, " +
				"	ssc.solicitud, dc.sub_cuenta)) tabla " +
				" ORDER BY tabla.nombrecentrocosto, tabla.descarticuloservicio";
		Log4JManager.info(consulta);
		return SqlBaseFacturaDao.proponerPreFactura(con, dtoFactura, consulta);
	}
	
	/**
	 *  metodo para la actualización del estado de facturacion y el estado del paciente de una factura dada
	 * @param con
	 * @param estadoFacturacion
	 * @param estadoPaciente
	 * @param codigoFactura
	 * @return
	 */
	public boolean actualizarEstadosFactura(Connection con, int estadoFacturacion, int estadoPaciente, int codigoFactura)
	{
		return SqlBaseFacturaDao.actualizarEstadosFactura(con, estadoFacturacion, estadoPaciente, codigoFactura);
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosInt obtenerCentroAtencionFactura( Connection con, String consecutivoFactura, int codigoInstitucion)
	{
		return SqlBaseFacturaDao.obtenerCentroAtencionFactura(con, consecutivoFactura, codigoInstitucion);
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosInt obtenerCentroAtencionFactura( Connection con, int codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerCentroAtencionFactura(con, codigoFactura);
	}
	
	/**
	 * Adición Sebastián
	 * Método que consulta el número de cuenta de cobro teniendo como referencia el
	 * consecutivo de la factura
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return numero de cuenta de cobro
	 */
	public double obtenerCuentaCobro(Connection con, int consecutivoFactura, int institucion)
	{
		return SqlBaseFacturaDao.obtenerCuentaCobro(con, consecutivoFactura, institucion);
	}
	
	
	/**
	 * Método que consulta el número de cuenta de cobro teniendo como referencia el codigo factura
	 * @param con
	 * @param codigoFactura
	 * @return numero de cuenta de cobro
	 */
	public double obtenerCuentaCobro(Connection con, int codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerCuentaCobro(con, codigoFactura);
	}
	
	/**
	 * Metodo que realiza la busqueda de una factura por su consecutivo y cod de institucion
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaPorConsecutivoDianEInstitucion(	Connection con,
																String consecutivoFactura,
																int codigoInstitucion, String restricciones)
	{
	    return SqlBaseFacturaDao.busquedaPorConsecutivoDianEInstitucion(con, consecutivoFactura, codigoInstitucion, restricciones);
	}
	
	/**
	 * obtiene el valor neto paciente
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public double obtenerValorNetoPaciente(Connection con, String consecutivoFactura, int codigoInstitucion)
	{
		return SqlBaseFacturaDao.obtenerValorNetoPaciente(con, consecutivoFactura, codigoInstitucion);
	}
	
	/**
	 * Método que cancela todos los procesos de facturación en proceso
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @return numero de cancelaciones
	 */
	public int cancelarTodosLosProcesosDeFacturacion(Connection con)
	{
		return SqlBaseFacturaDao.cancelarTodosLosProcesosDeFacturacion(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerContratoFactura(Connection con, int codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerContratoFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param cargarDetalles
	 * @return
	 */
	public DtoFactura cargarFactura( Connection con, String codigoFactura, boolean cargarDetalles)
	{
		return SqlBaseFacturaDao.cargarFactura(con, codigoFactura, cargarDetalles);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double obtenerValorConvenioFactura(Connection con, String codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerValorConvenioFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public  int obtenerCodigoPacienteFactura(Connection con, String codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerCodigoPacienteFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerIdIngresoFactura( Connection con, String codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerIdIngresoFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerFormatoFacturaXCodigoFact(Connection con, String codigoFactura)
	{
		return SqlBaseFacturaDao.obtenerFormatoFacturaXCodigoFact(con, codigoFactura);
	}
	
	/**
	 * actualiza el numero de la cuenta de cobro  en la factura
	 * @param con
	 * @param numeroCuentaCobroCapitada
	 * @param codigoFactura
	 * @return
	 */
	public boolean updateNumeroCuentaCobroCapitadaEnFactura(Connection con, String numeroCuentaCobroCapitada, String codigoFactura)
	{
		return SqlBaseFacturaDao.updateNumeroCuentaCobroCapitadaEnFactura(con, numeroCuentaCobroCapitada, codigoFactura);
	}
	
	/**
	 * Adición Sebastián Método usado para desasignar el numero de cuenta de
	 * cobro de una factura en el caso de que se haya hehco una inactivación de
	 * factura
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param institucion
	 * @return
	 */
	public int desasignarCuentaCobro(Connection con, String codigoFactura,int institucion)
	{
		return SqlBaseFacturaDao.desasignarCuentaCobro(con, codigoFactura, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean insertarHistoricoSubCuenta(Connection con, double subCuenta, double codigoFactura)
	{
		return SqlBaseFacturaDao.insertarHistoricoSubCuenta(con, subCuenta, codigoFactura);
	}

	/**
	 * 
	 */
	public ResultSetDecorator busquedaPorCodigo(Connection con, int codigoFactura, String restricciones) 
	{
		return SqlBaseFacturaDao.busquedaPorCodigo(con, codigoFactura, restricciones);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ArrayList<DtoAsociosDetalleFactura> cargarAsociosDetalleFactura(Connection con, int codigoDetalle)
	{
		return SqlBaseFacturaDao.cargarAsociosDetalleFactura(con, codigoDetalle);
	}

	/**
	 * 
	 */
	public boolean actualizarConsecutivoFactura(Connection con, int codigoFactura, String consecutivo) 
	{
		return SqlBaseFacturaDao.actualizarConsecutivoFactura(con,codigoFactura,consecutivo);
	}
	
	@Override
	public boolean eliminarCuentaProcesoFacturacion(Connection con,
			BigDecimal cuenta, String idSesion) {
		return SqlBaseFacturaDao.eliminarCuentaProcesoFacturacion(con, cuenta, idSesion);
	}
	
	
	/**
	 * @see com.princetonsa.dao.facturacion.FacturaDao#tieneSolicitudesSinAutorizar(java.sql.Connection, java.lang.Integer)
	 */
	public  Boolean  tieneSolicitudesSinAutorizar(Connection con, Integer numeroSolicitud){
		return SqlBaseFacturaDao.tieneSolicitudesSinAutorizar(con, numeroSolicitud);
	}
}