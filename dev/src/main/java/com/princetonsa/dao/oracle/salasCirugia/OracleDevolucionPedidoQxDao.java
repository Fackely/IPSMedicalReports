package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.princetonsa.dao.salasCirugia.DevolucionPedidoQxDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseDevolucionPedidoQxDao;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class OracleDevolucionPedidoQxDao implements DevolucionPedidoQxDao
{
	
	/**
	 * Cadena para consultar las solicitudes de cirugia Por Rangos
	 */

	private static String strConPeticionesPorRangos= "SELECT t.peticion AS peticion, " +
				"TO_CHAR(t.fechapeticion,'DD/MM/YYYY') AS fechapeticion, " +
				"t.horapeticion AS horapeticion, " +
				"t.estadomedico AS estadomedico, " +
				"t.profesionalsolicita AS profesionalsolicita, " +
				"t.solicitud AS solicitud, " +
				"t.ordenmedica AS ordenmedica, " +
				"t.codigopaciente AS codigopaciente, " +
				"t.nombrepaciente AS nombrepaciente, " +
				"t.idpaciente AS idpaciente, " +
				"t.fechanacimiento AS fechanacimiento, " +
				"t.convenio AS convenio, " +
				"t.centrocostosolicita AS centrocostosolicita, " +
				"t.nombrecentrocostosolicita AS nombrecentrocostosolicita, " +
				"t.centrocostosolicitado	AS centrocostosolicitado, " +
				"t.nombrecentrocostosolicitado AS nombrecentrocostosolicitado " +	
			 "FROM ( " +
				"SELECT DISTINCT pq.codigo AS peticion, " +
					"pq.fecha_peticion AS fechapeticion, " +
					"pq.hora_peticion AS horapeticion, " +
					"'' AS estadomedico, " +
					"getnombrepersona(pq.solicitante) AS profesionalsolicita, " +
					"'' AS solicitud, " +
					"'' AS ordenmedica, " +
					"pq.paciente AS codigopaciente, " +
					"getnombrepersona(pq.paciente) AS nombrepaciente, " +
					"getidpaciente(pq.paciente) AS idpaciente, " +
					"getfechanacimientopaciente(pq.paciente) AS fechanacimiento, " +
					"getnombreconvenio(getconvenioxingreso(pq.ingreso)) AS convenio, " +
					"pq.ingreso AS ingresopaciente, " +
					"p.centro_costo_solicitante AS centrocostosolicita, " +
					"getnomcentrocosto(p.centro_costo_solicitante) AS nombrecentrocostosolicita, " +
					"'' AS centrocostosolicitado, " +
					"'' AS nombrecentrocostosolicitado, " +
					"i.centro_atencion AS centroatencion " +
				"FROM pedidos_peticiones_qx ppq " +
				"INNER JOIN peticion_qx pq ON(pq.codigo=ppq.peticion) " +
				"INNER JOIN ingresos i ON(i.id=pq.ingreso) " +
				"INNER JOIN cuentas cu ON(cu.id_ingreso=i.id) " +
				"INNER JOIN pedido p ON(p.codigo=ppq.pedido) " +
				"WHERE pq.codigo NOT IN (SELECT codigo_peticion FROM solicitudes_cirugia WHERE codigo_peticion IS NOT NULL) " +
				"AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
				"AND (cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") " +
				"OR (cu.estado_cuenta   = "+ConstantesBD.codigoEstadoCuentaAsociada+" " +
				"AND (SELECT cuenta_final FROM asocios_cuenta WHERE cuenta_inicial = cu.id AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY fecha DESC, hora DESC "+ValoresPorDefecto.getValorLimit1()+" 1) IS NULL)) " +
		"UNION " +
			"SELECT DISTINCT pq.codigo AS peticion, " +
				"pq.fecha_peticion AS fechapeticion, " +
				"pq.hora_peticion  AS horapeticion, " +
				"getestadosolhis(s.estado_historia_clinica) AS estadomedico, " +
				"getnombrepersona(pq.solicitante) AS profesionalsolicita, " +
				"s.numero_solicitud ||'' AS solicitud, " +
				"s.consecutivo_ordenes_medicas ||'' AS ordenmedica, " +
				"cu.codigo_paciente AS codigopaciente, " +
				"getnombrepersona(cu.codigo_paciente) AS nombrepaciente, " +
				"getidpaciente(cu.codigo_paciente) AS idpaciente, " +
				"getfechanacimientopaciente(cu.codigo_paciente) AS fechanacimiento, " +
				"getnombreconvenio(getconvenioxingreso(cu.id_ingreso)) AS convenio, " +
				"cu.id_ingreso AS ingresopaciente, " +
				"s.centro_costo_solicitante AS centrocostosolicita, " +
				"getnomcentrocosto(s.centro_costo_solicitante) AS nombrecentrocostosolicita, " +
				"s.centro_costo_solicitado||'' AS centrocostosolicitado, " +
				"getnomcentrocosto(s.centro_costo_solicitado) AS nombrecentrocostosolicitado, " +
				"i.centro_atencion AS centroatencion " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes s ON(s.cuenta = cu.id) " +
				"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = s.numero_solicitud) " +
				"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=sc.codigo_peticion) " +
				"INNER JOIN pedido pe ON(pe.codigo=ppq.pedido) " +
				"INNER JOIN peticion_qx pq ON(pq.codigo = sc.codigo_peticion) " +
				"INNER JOIN materiales_qx mat ON (mat.numero_solicitud = sc.numero_solicitud) " +
				"INNER JOIN personas p ON(p.codigo=pq.solicitante) " +
				"INNER JOIN ingresos i ON (cu.id_ingreso= i.id) " +
				"WHERE  " +
					"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
					"AND (cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") " +
					"OR (cu.estado_cuenta   = "+ConstantesBD.codigoEstadoCuentaAsociada+" " +
					"AND (SELECT cuenta_final FROM asocios_cuenta WHERE cuenta_inicial = cu.id AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY fecha DESC, hora DESC "+ValoresPorDefecto.getValorLimit1()+" 1) IS NULL)) " +
					"AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"') " +
				"AND mat.finalizado  = '"+ConstantesBD.acronimoSi+"' " +
				"AND s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
				"AND s.estado_historia_clinica<>4 " +
			") t WHERE t.centroatencion = ? " ;


	/**
	 * 
	 */
	public HashMap listadoSolicitudes(Connection con, int codigoCuenta)
    {
        return SqlBaseDevolucionPedidoQxDao.listadoSolicitudes(con, codigoCuenta);
    }
	
	/**
	 * 
	 */
	public String validarPeticionDevolucionesPendientes(Connection con, int codigoSolicitud)
	{
		String consulta = "SELECT getTienePeticDevolucPendient(?) from dual";
		return SqlBaseDevolucionPedidoQxDao.validarPeticionDevolucionesPendientes(con, codigoSolicitud,consulta);
	}
	
	/**
	 * 
	 */
	public HashMap<String,Object> detallePeticion(Connection con, int codigoPeticion)
    {
        return SqlBaseDevolucionPedidoQxDao.detallePeticion(con, codigoPeticion);
    }
	
	/**
	 * 
	 */
	public HashMap detalleSolicitudArticulos(Connection con, int codigoSolicitud, boolean validacionFactura)
    {
        return SqlBaseDevolucionPedidoQxDao.detalleSolicitudArticulos(con, codigoSolicitud, validacionFactura);
    }
	
	/**
	 * 
	 */
	public HashMap<String,Object> listadoPeticionesPorRangos(Connection con, HashMap vo)
    {
		return SqlBaseDevolucionPedidoQxDao.listadoPeticionesPorRangos(con, vo,strConPeticionesPorRangos);
    }
	
	/**
	 * 
	 */
	public int devolucionPedidoQx(Connection con, HashMap vo)
    {
        return SqlBaseDevolucionPedidoQxDao.devolucionPedidoQx(con, vo);
    }
	
	/**
	 * 
	 */
	public boolean devolverDetallePedidoQx(Connection con, HashMap vo, int codigoDevolucion)
    {
        return SqlBaseDevolucionPedidoQxDao.devolverDetallePedidoQx(con, vo, codigoDevolucion);
    }
	
	/**
	 * 
	 */
	public HashMap<String,Object> consultarAlmacenes(Connection con, int codigoPeticion, int codigoArticulo)
    {
        return SqlBaseDevolucionPedidoQxDao.consultarAlmacenes(con, codigoPeticion, codigoArticulo);
    }
	
	/**
	 * 
	 */
	public String consultarEstadoDevolucion(Connection con, String codigoDevolucion)
    {
        return SqlBaseDevolucionPedidoQxDao.consultarEstadoDevolucion(con, codigoDevolucion);
    }
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public Vector<String> obtenerPedidosPeticionesQx(Connection con, int numeroSolicitud)
	{
		return SqlBaseDevolucionPedidoQxDao.obtenerPedidosPeticionesQx(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	@Override
	public HashMap<String, Object> listadoPeticiones(Connection con,
			int codigoIngreso, int codigoInstitucion) {
		return SqlBaseDevolucionPedidoQxDao.listadoPeticiones(con, codigoIngreso, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	@Override
	public HashMap<String, Object> detallePeticionArticulos(Connection con,
			int codigoPeticion) {
		return SqlBaseDevolucionPedidoQxDao.detallePeticionArticulos(con, codigoPeticion);
	}
}