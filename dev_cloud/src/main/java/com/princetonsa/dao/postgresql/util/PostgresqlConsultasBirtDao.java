package com.princetonsa.dao.postgresql.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dao.sqlbase.util.SqlBaseConsultasBirtDao;
import com.princetonsa.dao.util.ConsultasBirtDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;

/**
 * @author Giovanny Arias
 * cgarias@princetonsa.com
 */
public class PostgresqlConsultasBirtDao implements ConsultasBirtDao {

	/**
	 * @param cuenta
	 * @return
	 */
	public String furipsAccidenteDeTransito(int ingreso, int codigoMedicoTratante, int codigoReclamacion) {
		return SqlBaseConsultasBirtDao.furipsAccidenteDeTransito(ingreso, codigoMedicoTratante,codigoReclamacion);
	}
	
	/**
	 * @param cuenta
	 * @return
	 */
	public String informeAccidenteDeTransito(int ingreso, int codigoMedicoTratante) {
		return SqlBaseConsultasBirtDao.informeAccidenteDeTransito(ingreso, codigoMedicoTratante);
	}
	
	/**
	 * @param cuenta
	 * @return
	 */
	public String furipsEventoCatastrofico(int ingreso, int codigoMedicoTratante, int codigoReclamacion) {
		return SqlBaseConsultasBirtDao.furipsEventoCatastrofico(ingreso, codigoMedicoTratante,codigoReclamacion);
	}
	
	/**
	 * @param cuenta
	 * @return
	 */
	public String informeEventoCatastrofico(int ingreso, int codigoMedicoTratante) {
		return SqlBaseConsultasBirtDao.informeEventoCatastrofico(ingreso, codigoMedicoTratante);
	}
	
	/**
	 * @param cuenta
	 * @return
	 */
	public String furpro(int ingreso, int codigoReclamacion) {
		return SqlBaseConsultasBirtDao.furpro(ingreso,codigoReclamacion);
	}
	
	/**
	 * @param cuenta
	 * @return
	 */
	public String informeFurpro(int ingreso) {
		return SqlBaseConsultasBirtDao.informeFurpro(ingreso);
	}
	
	/**
	 * @param mes en formato 'nombreMes YYYY'
	 * @return consulta SQL
	 */
	public String perfilDeFarmacoterapia(int cuenta, String mes){
		return SqlBaseConsultasBirtDao.perfilDeFarmacoterapia(cuenta, mes);
	}
	
	/**
	 * @param tipoBusqueda (codigo, descripcion, claseGrupoSubgrupo)
	 * @param almacen
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param mostrarExt
	 * @param articulo
	 * @param institucion
	 * @return consulta SQL
	 */
	public String existenciasDeInventario(String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo,int institucion){
		return SqlBaseConsultasBirtDao.existenciasDeInventario(tipoBusqueda,almacen,clase,grupo,subgrupo,mostrarExt,articulo,institucion);
	}
	
	/**
	 * @param tipoBusqueda (codigo, descripcion, claseGrupoSubgrupo)
	 * @param almacen
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param mostrarExt
	 * @param articulo
	 * @param institucion
	 * @return consulta SQL
	 */
	public String existenciasDeInventario2(String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo,int institucion){
		return SqlBaseConsultasBirtDao.existenciasDeInventario2(tipoBusqueda,almacen,clase,grupo,subgrupo,mostrarExt,articulo,institucion);
	}
	
	/**
	 * 
	 * @param oldQuery
	 * @param consecutivosOrdenesInsertadas
	 * @param institucion
	 * @return
	 */
	public String modificarConsultaOrdenesAmbServicios(String oldQuery,  Vector<String> consecutivosOrdenesInsertadas, int institucion)
	{
		return SqlBaseConsultasBirtDao.modificarConsultaOrdenesAmbServicios(oldQuery, consecutivosOrdenesInsertadas, institucion);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Listado de facturas auditadas
	 * @param con
	 * @param fechaAuditoriaInicial
	 * @param fechaAuditoriaFinal
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param codigoConvenio
	 * @param codigoContrato
	 * @param numeroPreGlosa
	 * @param codigoInstitucionInt
	 */
	public String listadoFacturasAuditadas(Connection con, String fechaAuditoriaInicial, String fechaAuditoriaFinal, String facturaInicial, String facturaFinal, int codigoConvenio, int codigoContrato, String numeroPreGlosa, int codigoInstitucionInt)
	{
		return SqlBaseConsultasBirtDao.listadoFacturasAuditadas(con, fechaAuditoriaInicial, fechaAuditoriaFinal, facturaInicial, facturaFinal, codigoConvenio, codigoContrato, numeroPreGlosa, codigoInstitucionInt);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Formato Suba Conciliacion Glosas
	 * @param con
	 * @param fechaAuditoriaInicial
	 * @param fechaAuditoriaFinal
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param codigoConvenio
	 * @param codigoContrato
	 * @param numeroPreGlosa
	 * @param codigoInstitucionInt
	 */
	public String formatoSubaConciliacionFacturas(Connection con, String codConciliacion)
	{
		return SqlBaseConsultasBirtDao.formatoSubaConciliacionFacturas(con,codConciliacion);
	}
	
	public String formatoSubaConciliacionEncabezado(Connection con,String codConciliacion)
	{
		return SqlBaseConsultasBirtDao.formatoSubaConciliacionEncabezado(con, codConciliacion);
	}
	
	public String formatoSubaConciliacionSolicitudes(Connection con,String codConciliacion, String codigoTarifario, int institucion,String tipo)
	{
		return SqlBaseConsultasBirtDao.formatoSubaConciliacionSolicitudes(con, codConciliacion, codigoTarifario, institucion, tipo);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de facturas pacientes para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoFacturasPacientes(Connection con, HashMap criterios)
	{
		return SqlBaseConsultasBirtDao.listadoFacturasPacientes(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de ingresos para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoIngresos(Connection con, HashMap criterios)
	{
		return SqlBaseConsultasBirtDao.listadoIngresos(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de facturas varias para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoFacturasVarias(Connection con, HashMap criterios)
	{
		return SqlBaseConsultasBirtDao.listadoFacturasVarias(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de Recibos Caja para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoRecibosCaja(Connection con, HashMap criterios)
	{
		return SqlBaseConsultasBirtDao.listadoRecibosCaja(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Impresion Glosa Factura
	 */
	
	public String ImpresionGlosaFactura(Connection con, String codigoFactura, int codigoInstitucionInt, String codigoGlosa)
	{
		return SqlBaseConsultasBirtDao.ImpresionGlosaFactura(con, codigoFactura, codigoInstitucionInt, codigoGlosa);
	}
	/**
	 * Metodo encargado de organizar la consulta
	 * de datos gnerales del egreso para la
	 * impresion de la boleta de salida
	 * @param cuenta
	 * @return
	 */
	public String consultaEgreso (String cuenta)
	{
		return SqlBaseConsultasBirtDao.consultaEgreso(cuenta);
	}
	
	/**
	 * Metodo encargado de consulta la informacion
	 * de la facturas para imprimir la boleta de salida
	 * @param ingreso
	 * @return
	 */
	public String consultaFacturas(String ingreso)
	{
		return SqlBaseConsultasBirtDao.consultaFacturas(ingreso);
	}
	
	/**
	 * Metodo encargado de consulta la informacion
	 * de la cuenta para imprimir la boleta de salida
	 * @param ingreso
	 * @return
	 */
	public String consultaSalida(String ingreso){
		
		return SqlBaseConsultasBirtDao.consultaSalida(ingreso);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion respuesta de glosa 
	 * @param ingreso
	 * @return
	 */
	public String impresionRespuestaGlosaStandar(String codigoRespuestaGlosa)
	{
		return SqlBaseConsultasBirtDao.impresionRespuestaGlosaStandar(codigoRespuestaGlosa);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion del detalle correspondiente a la factura de una glosa
	 * @param codigoGlosa
	 * @param codigoInstitucion
	 * @return
	 */
	public String consultarDetalleFacturasGlosa(String codigoGlosa, int codigoInstitucion)
	{
		return SqlBaseConsultasBirtDao.consultarDetalleFacturasGlosa(codigoGlosa,codigoInstitucion);
	}
	
	/**
	 * Metodo que retorna  la sentencia SQL para crear el reporte de la impresion del detalle de Admision
	 */
	public String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta)
	{
		String cadenaConsulta= "SELECT * FROM ( SELECT " +
		   "coalesce (respauto.numero_autorizacion ||'','') AS numeroautoriza," +
		   "1 as orden  " +
		   "FROM cuentas cu " +
		   "INNER JOIN sub_cuentas subcuen ON (cu.id_ingreso = subcuen.ingreso AND subcuen.nro_prioridad = 1) " +
		   "INNER JOIN autorizaciones aut ON (aut.sub_cuenta = subcuen.sub_cuenta AND aut.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision +"') " +
		   "INNER JOIN det_autorizaciones detaut ON (aut.codigo = detaut.autorizacion AND detaut.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado +"' AND detaut.activo = '"+ConstantesBD.acronimoSi +"') " +
		   "INNER JOIN resp_autorizaciones respauto ON (respauto.det_autorizacion = detaut.codigo) " +
		   "WHERE cu.id = "+ codigoCuenta+ "  ";
		if(codSubCuenta!=ConstantesBD.codigoNuncaValido)   		
			cadenaConsulta+=" AND  aut.sub_cuenta = "+ codSubCuenta +" ";
		cadenaConsulta+="UNION " +
		   "SELECT " +
		   "coalesce(verif.numero_verificacion,'') AS numeroautoriza, " +
		   "2 as orden " +
		   "FROM cuentas cuent " +
		   "INNER JOIN sub_cuentas subc ON (subc.ingreso = cuent.id_ingreso) " +
		   "INNER JOIN verificaciones_derechos verif ON (verif.sub_cuenta = subc.sub_cuenta )" +
		   "WHERE cuent.id =  "+ codigoCuenta + " ";
		if(codSubCuenta!=ConstantesBD.codigoNuncaValido)   		
			cadenaConsulta+= "AND subc.sub_cuenta ="+codSubCuenta + " ";
		cadenaConsulta+=") tabl ORDER BY tabl.orden limit 1 ";
		
		return cadenaConsulta;
	}
	
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion de la orden ambulatoria de medicamentos de control especial
	 * @param codigoPersona
	 * @return
	 */
	public String impresionInfoPacienteMedicamentosControlEspecial(int codigoPersona)
	{
		return SqlBaseConsultasBirtDao.impresionInfoPacienteMedicamentosControlEspecial(codigoPersona);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion de la orden ambulatoria de medicamentos de control especial
	 * @param codigoPersona
	 * @return
	 */
	public String impresionInfoOrden(String nroOrden)
	{
		return SqlBaseConsultasBirtDao.impresionInfoOrden(nroOrden);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion de la orden ambulatoria de medicamentos de control especial
	 * @param codigoPersona
	 * @return
	 */
	public String impresionInfoMedico(String idMedico)
	{
		return SqlBaseConsultasBirtDao.impresionInfoMedico(idMedico);
	}
	
	
	/**
	 * Mï¿½todo que retorna la consulta Birt para el reporte detallado por solicitud del estado de 
	 * @param estadoAction
	 * @param remite
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultaImprimirDetSolConEstadoCuenta(String estadoAction,int remite,String idSubCuenta,String numeroSolicitud)
	{
		String query = "SELECT "+
		"t.orden as orden, "+
		"t.convenio as convenio, "+
		"t.valoru as valoru, " +
		"t.usuario as usuario, " +
		"t.paquete as paquete," +
		"t.fecha_cargo as fecha_cargo, "+
		"t.profesional as profesional, "+
		"t.centro_costo_solicita as centro_costo_solicita," +
		"t.centro_costo_ejecuta as centro_costo_ejecuta, "+
		"t.codigo as codigo, "+
		"t.grupo as grupo, "+
		"t.descripcion as descripcion, "+
		"t.cantidad as cantidad, "+
		"t.total_cargo as total_cargo, "+
		"t.total_recargo as total_recargo, "+
		"t.total_dcto as total_dcto, "+ 
		"to_char(current_date, 'DD/MM/YYYY') AS fechaActual " +
			"FROM "+ 
			"( "+
				"( "+
					"SELECT "+ 
					"sol.consecutivo_ordenes_medicas As  orden," +
					" CASE WHEN c.tipo_regimen = 'P' THEN getNomDeudorIngreso(sbc.ingreso)  ELSE c.nombre  END   AS convenio, "+
					
					//Modificado por la Tarea 51069
					//"(((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)))-(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))) AS valoru, " +
					"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
					
					"dc.usuario_modifica AS usuario, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
					"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
					"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"CAST (getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")AS TEXT) "+ 					
				     "ELSE "+ 
				    "CASE WHEN c.tipo_codigo_articulos=1 THEN CAST(dc.articulo AS TEXT) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)AS TEXT) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) AS TEXT) 	END END "+ 
				    " END AS codigo, "+
				    "  CASE WHEN dc.servicio IS NOT NULL THEN facturacion.getnomtiposervicio(dc.servicio)" +
		    		" ELSE inventarios.getnomclaseinventario(inventarios.getclaseinventarioarticulo(dc.articulo)) END as grupo,"+
		    		  "CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
					"ELSE "+ 
						"UPPER(getdescripcionalternaarticulo(dc.articulo)) "+ 
					"END AS descripcion, "+
					"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
					"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
					"coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) AS total_recargo, "+
					"coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) AS total_dcto "+ 
					"FROM det_cargos dc "+ 
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"WHERE " +
					"dc.sub_cuenta = "+idSubCuenta+" AND ";
					if(estadoAction.equals("imprimirDetSolDetSol"))
						query+="dc.solicitud="+numeroSolicitud+" AND ";
			    	if(remite==2)
						query+="dc.facturado='S' AND ";
			    	if(!estadoAction.equals("imprimirDetSolDetSol"))
			    		query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
					query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
					if(!estadoAction.equals("imprimirDetSolDetSol"))
						query+="dc.valor_total_cargado > 0 AND ";
					query+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
					"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+
				") "+ 
				"UNION "+ 
				"( "+
					"SELECT "+ 
					"sol.consecutivo_ordenes_medicas As  orden, " +
					"CASE  WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)  ELSE c.nombre END AS convenio, "+
					
					//Modificado por la Tarea 51069
					//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
					"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
					
					"dc.usuario_modifica AS usuario, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
					"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
					"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
					"CAST (getcodigoespecialidad(dc.servicio_cx)AS TEXT) || '-' || CAST(dc.servicio_cx AS TEXT)  AS codigo, "+
					" facturacion.getnomtiposervicio(dc.servicio_cx) AS grupo, "+
					"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
					"1 AS cantidad, "+
					"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
					"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
					"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto "+ 
					"FROM det_cargos dc "+ 
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
					"WHERE " +
					"dc.sub_cuenta = "+idSubCuenta+" AND ";
					if(estadoAction.equals("imprimirDetSolDetSol"))
						query+="dc.solicitud="+numeroSolicitud+" AND ";
					if(remite==2)
						query+="dc.facturado='S' AND ";
					if(!estadoAction.equals("imprimirDetSolDetSol"))
			    		query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
					query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
					if(!estadoAction.equals("imprimirDetSolDetSol"))
						query+="dc.valor_total_cargado > 0 AND ";
					query+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
					"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
					"dc.articulo is null "+
					"GROUP BY sol.consecutivo_ordenes_medicas,dc.solicitud,sol.centro_costo_solicitado,dc.fecha_modifica,dc.solicitud,dc.servicio_cx,tpa.nombre_asocio,dc.det_cx_honorarios,dc.usuario_modifica,dc.cargo_padre,sol.centro_costo_solicitante,dc.valor_unitario_cargado,  dc.servicio_cx,    c.tipo_regimen,    sbc.ingreso,    c.nombre,    dc.codigo_factura,    tpa.codigo_asocio,    tpa.nombre_asocio"+
				") "+
				//Detalle de los materiales especiales
				"UNION "+ 
				"( "+
					"SELECT "+ 
					"sol.consecutivo_ordenes_medicas As  orden, " +
					"CASE   WHEN c.tipo_regimen = 'P'  THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre  END     AS convenio, "+
					
					//Modificado por la Tarea 51069
					//"(((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)))-(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))) AS valoru, " +
					"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
					
					"dc.usuario_modifica AS usuario, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"'' AS profesional, "+
					"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
					"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
					"CASE WHEN c.tipo_codigo_articulos=1 THEN CAST(dc.articulo AS TEXT) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)AS TEXT) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) AS TEXT) 	END END AS codigo,"+ 
					" inventarios.getnomclaseinventario(inventarios.getclaseinventarioarticulo(dc.articulo)) AS grupo, "+ 
					" UPPER(getdescripcionalternaarticulo(dc.articulo)) AS descripcion, "+
					"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
					"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
					"coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) AS total_recargo, "+
					"coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) AS total_dcto "+ 
					"FROM det_cargos dc "+ 
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"WHERE " +
					"dc.sub_cuenta = "+idSubCuenta+" AND ";
					if(estadoAction.equals("imprimirDetSolDetSol"))
						query+="dc.solicitud="+numeroSolicitud+" AND ";
					if(remite==2)
						query+="dc.facturado='S' AND ";
					if(!estadoAction.equals("imprimirDetSolDetSol"))
			    		query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
					query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
					if(!estadoAction.equals("imprimirDetSolDetSol"))
						query+="dc.valor_total_cargado > 0 AND ";
					query+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
					"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
					"dc.articulo is not null "+
				") "+
			") t "+ 
			"ORDER BY fecha_cargo,codigo";
		return query;
	}
	
	/**
	 * M&eacute;todo consulta Birt para el reporte detallado por solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
		
	public String consultaImprimirDetSolConEstadoCuentaEquivalente(String idSubCuenta,String numeroSolicitud)
	{
		
		String query = "SELECT "+
		"t.\"orden\" as orden, "+
		"t.\"convenio\" as convenio, "+
		"t.\"valoru\" as valoru, " +
		"t.\"usuario\" as usuario, " +
		"t.\"paquete\" as paquete," +
		"t.\"fecha_cargo\" as fecha_cargo, "+
		"t.\"profesional\" as profesional, "+
		"t.\"centro_costo_solicita\" as centro_costo_solicita," +
		"t.\"centro_costo_ejecuta\" as centro_costo_ejecuta, "+
		"t.\"codigo\" as codigo, "+
		"t.\"grupo\" as grupo, "+
		"t.\"descripcion\" as descripcion, "+
		"t.\"cantidad\" as cantidad, "+
		"t.\"total_cargo\" as total_cargo, "+
		"t.\"total_recargo\" as total_recargo, "+
		"t.\"total_dcto\" as total_dcto, "+ 
		"to_char(current_date, 'DD/MM/YYYY') AS fechaActual " +
			"FROM "+ 
			"( "+
				"( "+
					"SELECT "+ 
					"sol.consecutivo_ordenes_medicas As  orden, " +
					" CASE   WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END   AS convenio, "+					
					"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+					
					"dc.usuario_modifica AS usuario, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '' END AS paquete, " +
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
					"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
					"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"CAST (getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")AS TEXT) "+ 					
				     "ELSE "+ 
				       "CASE WHEN c.tipo_codigo_articulos=1 THEN CAST(dc.articulo AS TEXT) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)as text) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) as text) 	END END "+ 
				       " END AS codigo, "+
				       "  CASE WHEN dc.servicio IS NOT NULL THEN facturacion.getnomtiposervicio(dc.servicio)" +
			    		" ELSE inventarios.getnomclaseinventario(inventarios.getclaseinventarioarticulo(dc.articulo)) END as grupo,"+
				       "CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
					"ELSE "+ 
						"UPPER(getdescripcionalternaarticulo(dc.articulo)) "+ 
					"END AS descripcion, "+
					"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
					"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
					"coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) AS total_recargo, "+
					"coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) AS total_dcto "+ 
					"FROM det_cargos dc "+ 
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud)" +
					"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo)" +
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"WHERE " +
					"dc.sub_cuenta = "+idSubCuenta+"" +
					" AND dc.solicitud="+numeroSolicitud+"" +
					" AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
					" AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"'" +
					" AND dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+"" +
					" AND dd.cantidad > 0" +
					") "+
					"UNION "+ 
				"( "+
					"SELECT "+ 
					"sol.consecutivo_ordenes_medicas As  orden, " +
					" CASE   WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END   AS convenio, "+					
					"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+					
					"dc.usuario_modifica AS usuario, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '' END AS paquete, " +
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
					"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
					"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
					"CAST(getcodigoespecialidad(dc.servicio_cx)AS TEXT) || '-' || CAST(dc.servicio_cx AS TEXT)  AS codigo, "+
					" facturacion.getnomtiposervicio(dc.servicio_cx) AS grupo, "+
					"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
					"1 AS cantidad, "+
					"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
					"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
					"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto "+ 
					"FROM det_cargos dc "+ 
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud)" +
					"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo)" +
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
					"WHERE " +					
					" dc.sub_cuenta = "+idSubCuenta+" " +
					" AND dc.solicitud="+numeroSolicitud+" " +
					" AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
					" AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"'" +
					" AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
					" AND dd.cantidad > 0 " +
					" AND dc.articulo is null " +
					"GROUP BY sol.consecutivo_ordenes_medicas,dc.solicitud,sol.centro_costo_solicitado,dc.fecha_modifica,dc.solicitud,dc.servicio_cx,tpa.nombre_asocio,dc.det_cx_honorarios,dc.usuario_modifica,dc.cargo_padre,sol.centro_costo_solicitante,dc.valor_unitario_cargado, dc.servicio_cx,    c.tipo_regimen,    sbc.ingreso,    c.nombre,    dc.codigo_factura,    tpa.codigo_asocio,    tpa.nombre_asocio"+
				") "+
				//Detalle de los materiales especiales
				"UNION "+ 
				"( "+
					"SELECT "+ 
					"sol.consecutivo_ordenes_medicas As  orden, "+	
					" CASE   WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END   AS convenio, "+		
					"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+					
					"dc.usuario_modifica AS usuario, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '' END AS paquete, " +
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"'' AS profesional, "+
					"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
					"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
					"CASE WHEN c.tipo_codigo_articulos=1 THEN cast(dc.articulo as text) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)as text) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) as text) 	END END AS codigo"+ 
					" inventarios.getnomclaseinventario(inventarios.getclaseinventarioarticulo(dc.articulo)) AS grupo, "+ 
					" UPPER(getdescripcionalternaarticulo(dc.articulo)) AS descripcion, "+
					"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
					"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
					"coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) AS total_recargo, "+
					"coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) AS total_dcto "+ 
					"FROM det_cargos dc "+ 
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud)" +
					"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo)" +
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"WHERE " +
					
					" dc.sub_cuenta = "+idSubCuenta+" " +
					" AND dc.solicitud="+numeroSolicitud+" " +
					" AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
					" AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"'" +
					" AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
					" AND dd.cantidad > 0 " +
					" AND dc.articulo is null " +					
				") "+
			") t "+ 
		"ORDER BY \"codigo\"";
		
		return query;
	}
	
	/**
	 * Mï¿½todo que retorna la consulta de la impresion detallada por item en el estado de la cuenta
	 * @param estadoAction
	 * @param remite
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultaImprimirItemEstadoCuenta(String estadoAction,int remite,String idSubCuenta,String numeroSolicitud)
	{
		String query =   "SELECT "+ 
	    	"t.fecha_cargo, "+
	    	"t.codigo, " +
	    	"t.convenio, " +
	    	"t.es_portatil, "+
	    	"t.codigo_portatil, "+
	    	"t.codigoalterno AS codigoalterno, "+
	    	"t.descripcion, "+
	    	"t.asocio, " +
	    	"t.paquete, " +
	    	"t.ctc, " +
	    	"t.valoru, " +
	    	"sum(t.valort) AS valort, " +
	    	"t.profesional, " +
	    	"sum(t.cantidad) AS cantidad, "+
	    	"sum(t.total_cargo) AS total_cargo, "+
	    	"sum(t.total_recargo) AS total_recargo, "+
	    	"sum(t.total_dcto) AS total_dcto, "+
	    	"to_char(current_date, 'DD/MM/YYYY') AS fechaActual " +
	    	"FROM "+
	    	"( "+
	    		"( "+
	    			"SELECT " +
	    			"-1 as servicio_cx, " +
	    			"0 as es_encabezado_cx, "+ 
					"0 as solicitud, "+ //se deja vacï¿½o para acomodarse con el union de cirugias
					"'---' AS asocio, " +
					"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '---' END AS paquete, " +
					"CASE WHEN dc.servicio IS NOT NULL THEN " +
					"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"ELSE " +
					"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"END AS ctc, " +
					"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END AS profesional, "+
					"sum(coalesce(dc.valor_unitario_cargado, 0)) AS valoru, " +
					"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
					"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as es_portatil, "+
					"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN coalesce(getobtenercodigocupsserv((SELECT portatil_asociado from sol_procedimientos WHERE numero_solicitud = dc.solicitud),"+ConstantesBD.codigoTarifarioCups+"),'---') ELSE '' END as codigo_portatil, "+
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"CASE"+
				      " WHEN dc.servicio IS NOT NULL     THEN      CAST (getobtenercodigocupsserv(dc.servicio,0) AS TEXT)"+
				       "ELSE   CASE        WHEN c.tipo_codigo_articulos=1        THEN CAST(dc.articulo AS TEXT)"+
				          "ELSE  CASE WHEN c.tipo_codigo_articulos=0      THEN CAST (facturacion.getcodigocumarticulo(dc.articulo)AS TEXT)"+
				            "  ELSE CAST (facturacion.getcodigointerfaz(dc.articulo) AS   TEXT)"+
				           " END END   END AS codigo, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') "+ 
					"ELSE "+ 
						"'---' "+ 
					"END AS codigoalterno, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"coalesce(UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")),'---') "+ 
					"ELSE "+ 
						"coalesce(UPPER(getdescripcionalternaarticulo(dc.articulo)),'---') "+ 
					"END AS descripcion, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.cantidad_cargada,0)) ELSE 0 END AS cantidad, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END AS total_recargo, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END AS total_dcto "+ 
					"FROM det_cargos dc "+
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
					" INNER JOIN convenios c ON(dc.convenio  =c.codigo)" +
					"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) "+
					"WHERE " +
					"dc.sub_cuenta = "+idSubCuenta+" AND ";
	    			if(estadoAction.equals("imprimirResumidoItemDetSol"))
	    				query+="dc.solicitud="+numeroSolicitud+" AND ";
					if(remite==2)
						query+="dc.facturado='S' AND ";
					query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
					if(!estadoAction.equals("imprimirResumidoItemDetSol"))
					{
						query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						"dc.valor_total_cargado > 0 AND ";
					}
					query+="dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
					"GROUP BY servicio_cx, es_encabezado_cx, dc.fecha_modifica,dc.servicio,dc.articulo,dc.cargo_padre,dc.solicitud,sol.tipo,sol.codigo_medico_responde,dc.cargo_padre,dc.servicio,dc.articulo,dc.valor_unitario_cargado,  c.codigo, c.tipo_codigo_articulos,   sbc.ingreso,  c.nombre,  c.tipo_regimen "+ 
	    		") "+
	    		"UNION ALL "+
	    		"( " +
	    			"(" +
	    				"SELECT " +
	    					"DISTINCT " +
	    					"coalesce(dc.servicio_cx,-1) as servicio_cx, " +
	    					"1 as es_encabezado_cx, "+
	    					"dc.solicitud, " +
	    					"'----' AS asocio, " +
	    					"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
	    					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '---' END AS paquete, " +
	    					"'----' AS ctc, " +
	    					"CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END AS profesional, " +
	    					"-1 AS valoru, " +
	    					"getValorCxCompleta(dc.solicitud, dc.sub_cuenta, dc.servicio_cx, dc.facturado, dc.paquetizado,"+ConstantesBD.codigoEstadoFCargada+") as valort, " +
	    					"'"+ConstantesBD.acronimoNo+"' as es_portatil, " +
	    					"'' as codigo_portatil, " +
	    					"to_char(dc.fecha_modifica,'DD/MM/YYYY') AS fecha_cargo, " +
	    					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+"),'---') AS codigo, " +
	    					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioSoat+"),'---') AS codigoalterno, " +
	    					"UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")) AS descripcion, " +
	    					"1 AS cantidad, " +
	    					"0 AS total_cargo, " +
	    					"0 AS total_recargo, " +
	    					"0 AS total_dcto " +
	    				"FROM " +
	    					"det_cargos dc " +
	    					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
	    					" INNER JOIN convenios c  ON(dc.convenio             =c.codigo)  INNER JOIN sub_cuentas sbc  ON(dc.sub_cuenta=sbc.sub_cuenta) " +
	        			"WHERE " +
	    					"dc.sub_cuenta = "+idSubCuenta+" AND ";
	    					if(remite==2)
	    						query+="dc.facturado='"+ConstantesBD.acronimoSi+"' AND ";
	    					if(!estadoAction.equals("imprimirResumidoItemDetSol"))
	    						query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
	    					query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
	    					query+="dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
	    					"dc.articulo is null "+
	            	") " +
	            	"UNION ALL " +
	            	"(" +
	            		"SELECT "+
	            			"coalesce(dc.servicio_cx,-1) as servicio_cx, " +
	            			"0 as es_encabezado_cx, "+
	            			"dc.solicitud, "+
	            			"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
	            			"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
	            			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '---' END AS paquete, " +
	            			"CASE WHEN dc.servicio IS NOT NULL THEN " +
	            			"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
	            			"ELSE " +
	            			"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
	            			"END AS ctc, " +
	            			"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'---') AS profesional, "+
	            			"sum(coalesce(dc.valor_unitario_cargado, 0)) AS valoru, " +
	            			"-1 AS valort, " +
	            			"'"+ConstantesBD.acronimoNo+"' as es_portatil, " +
	            			"'' as codigo_portatil, "+
	            			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
	            			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') AS codigo, "+
	            			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') AS codigoalterno, "+
	            			"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28) AS descripcion, "+
	            			"1 AS cantidad, "+
	            			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
	            			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo,0)) ELSE 0 END AS total_recargo, "+
	            			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto,0)) ELSE 0 END AS total_dcto "+ 
	            		"FROM " +
	            			"det_cargos dc "+
	            			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
	            			"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
	            			" INNER JOIN convenios c  ON(dc.convenio             =c.codigo)  INNER JOIN sub_cuentas sbc  ON(dc.sub_cuenta=sbc.sub_cuenta) " +
	            		"WHERE " +
	            		"dc.sub_cuenta = "+idSubCuenta+" AND ";
	    					if(estadoAction.equals("imprimirResumidoItemDetSol"))
	    						query+="dc.solicitud="+numeroSolicitud+" AND ";
	    					if(remite==2)
	    						query+="dc.facturado='S' AND ";
	    					if(!estadoAction.equals("imprimirResumidoItemDetSol"))
	    						query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
	    					query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
	    					if(!estadoAction.equals("imprimirResumidoItemDetSol"))
	    						query+="dc.valor_total_cargado > 0 AND ";
	    					query+="dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
	    					"dc.articulo is null "+
	    				"GROUP BY servicio_cx, es_encabezado_cx, dc.fecha_modifica,dc.solicitud,dc.servicio_cx,tpa.codigo_asocio,tpa.nombre_asocio,dc.cargo_padre,dc.servicio,dc.articulo,dc.det_cx_honorarios,dc.valor_unitario_cargado,  c.codigo, c.tipo_codigo_articulos,   sbc.ingreso,  c.nombre,  c.tipo_regimen " +
	    			")"+ 
	    		") "+
	    		//La consulta de los materiales especiales
	    		"UNION ALL "+
	    		"( "+
	    			"SELECT "+
	    			"coalesce(dc.servicio_cx,-1) as servicio_cx, " +
	    			"0 as es_encabezado_cx, "+
	    			"dc.solicitud, "+
	    			"'---' AS asocio, " +
	    			"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '---' END AS paquete, " +
	    			"CASE WHEN dc.servicio IS NOT NULL THEN " +
					"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"ELSE " +
					"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"END AS ctc, " +
					"'---' AS profesional, "+
					"sum(coalesce(dc.valor_unitario_cargado, 0)) AS valoru, " +
					"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
					"'"+ConstantesBD.acronimoNo+"' as es_portatil, " +
					"'' as codigo_portatil, "+
	    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
	    			"'---' AS codigo, "+
	    			"'---' AS codigoalterno, "+
	    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
	    			"sum(dc.cantidad_cargada) AS cantidad, "+
	    			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
	    			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END AS total_recargo, "+
	    			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END AS total_dcto "+ 
	    			"FROM det_cargos dc "+ 
	    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
	    			" INNER JOIN convenios c  ON(dc.convenio             =c.codigo)  INNER JOIN sub_cuentas sbc  ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"WHERE " +
	    			"dc.sub_cuenta = "+idSubCuenta+" AND ";
	    			if(estadoAction.equals("imprimirResumidoItemDetSol"))
	    				query+="dc.solicitud="+numeroSolicitud+" AND ";
	    			if(remite==2)
						query+="dc.facturado='S' AND ";
	    			if(!estadoAction.equals("imprimirResumidoItemDetSol"))
	    				query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
	    			query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
	    			if(!estadoAction.equals("imprimirResumidoItemDetSol"))
	    				query+="dc.valor_total_cargado > 0 AND ";
	    			query+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
	    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
	    			"dc.articulo is not null "+
	    			"GROUP BY  servicio_cx, es_encabezado_cx, dc.fecha_modifica,dc.solicitud,dc.cargo_padre,dc.servicio,dc.articulo,dc.valor_unitario_cargado,  c.codigo, c.tipo_codigo_articulos,   sbc.ingreso,  c.nombre,  c.tipo_regimen "+ 
	    		") "+
	    	") t "+
	    "GROUP BY t.servicio_cx, t.es_encabezado_cx, t.fecha_cargo,t.solicitud,t.codigo,t.convenio,t.codigoalterno,t.descripcion,t.asocio,t.paquete,t.ctc,t.valoru,t.valort,t.profesional,t.es_portatil,t.codigo_portatil "+
	    "ORDER BY fecha_cargo ASC, t.solicitud ASC, t.servicio_cx ASC,  es_encabezado_cx desc  ";
	    return query;
	}
	
	/**
	 * M&eacute;todo consulta Birt para el reporte detallado por item con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	
	public String consultaImprimirItemEstadoCuentaEquivalente(String idSubCuenta,String numeroSolicitud)
	{
		String query =   "SELECT "+ 
	    	"t.\"fecha_cargo\" as fecha_cargo, "+
	    	"t.\"codigo\" as codigo, " +
	    	"t.\"convenio\" as convenio, " +
	    	"t.\"es_portatil\" as es_portatil, "+
	    	"t.\"codigo_portatil\" as codigo_portatil, "+
	    	"t.\"codigoalterno\" AS codigoalterno, "+
	    	"t.\"descripcion\" as descripcion, "+
	    	"t.\"asocio\" as asocio, " +
	    	"t.\"paquete\" as paquete, " +
	    	"t.\"ctc\" as ctc, " +
	    	"t.\"valoru\" as valoru, " +
	    	"sum(t.\"valort\") AS valort, " +
	    	"t.\"profesional\" as profesional, " +
	    	"sum(t.\"cantidad\") AS cantidad, "+
	    	"sum(t.\"total_cargo\") AS total_cargo, "+
	    	"sum(t.\"total_recargo\") AS total_recargo, "+
	    	"sum(t.\"total_dcto\") AS total_dcto, "+
	    	"to_char(current_date, 'DD/MM/YYYY') AS fechaActual " +
	    	"FROM "+
	    	"( "+
	    		"( "+
	    			"SELECT " +
	    			"-1 as servicio_cx, " +
	    			"0 as es_encabezado_cx, "+ 
					"0 as solicitud, "+ //se deja vacï¿½o para acomodarse con el union de cirugias
					"'---' AS asocio, " +
					"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '---' END AS paquete, " +
					"CASE WHEN dc.servicio IS NOT NULL THEN " +
					"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"ELSE " +
					"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"END AS ctc, " +
					"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END AS profesional, "+
					"sum(coalesce(dc.valor_unitario_cargado, 0)) AS valoru, " +
					"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
					"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as es_portatil, "+
					"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN coalesce(getobtenercodigocupsserv((SELECT portatil_asociado from sol_procedimientos WHERE numero_solicitud = dc.solicitud),"+ConstantesBD.codigoTarifarioCups+"),'---') ELSE '' END as codigo_portatil, "+
					"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
					"CASE"+
				      " WHEN dc.servicio IS NOT NULL     THEN      CAST (getobtenercodigocupsserv(dc.servicio,0) AS TEXT)"+
				       "ELSE   CASE        WHEN c.tipo_codigo_articulos=1        THEN CAST(dc.articulo AS TEXT)"+
				          "ELSE  CASE WHEN c.tipo_codigo_articulos=0      THEN CAST (facturacion.getcodigocumarticulo(dc.articulo)AS TEXT)"+
				            "  ELSE CAST (facturacion.getcodigointerfaz(dc.articulo) AS   TEXT)"+
				           " END END   END AS codigo, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') "+ 
					"ELSE "+ 
						"'---' "+ 
					"END AS codigoalterno, "+
					"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"coalesce(UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")),'---') "+ 
					"ELSE "+ 
						"coalesce(UPPER(getdescripcionalternaarticulo(dc.articulo)),'---') "+ 
					"END AS descripcion, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.cantidad_cargada,0)) ELSE 0 END AS cantidad, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END AS total_recargo, "+
					"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END AS total_dcto "+ 
					"FROM det_cargos dc " +
					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
					"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud) " +
					"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden AND dd.articulo = dc.articulo) " +
					"INNER JOIN convenios c on (c.codigo = dc.convenio ) " +
					" INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) "+
					"WHERE dc.sub_cuenta = "+idSubCuenta+" " +
					"AND dc.solicitud="+numeroSolicitud+" " +
					"AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
					"AND dd.cantidad > 0 " +
					"AND dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" " +					
					"GROUP BY dc.fecha_modifica,dc.servicio,dc.articulo,dc.cargo_padre,dc.solicitud,sol.tipo,sol.codigo_medico_responde,dc.cargo_padre,dc.servicio,dc.articulo,dc.valor_unitario_cargado, c.codigo, c.tipo_codigo_articulos,   sbc.ingreso,  c.nombre,  c.tipo_regimen"+						
			    	") "+
	    		"UNION ALL "+
	    		"( " +
	    			"(" +
	    				"SELECT " +
	    					"DISTINCT " +
	    					"coalesce(dc.servicio_cx,-1) as servicio_cx, " +
	    					"1 as es_encabezado_cx, "+
	    					"dc.solicitud, " +
	    					"'----' AS asocio, " +
	    					"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
	    					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '---' END AS paquete, " +
	    					"'----' AS ctc, " +
	    					"CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END AS profesional, " +
	    					"-1 AS valoru, " +
	    					"getValorCxCompleta(dc.solicitud, dc.sub_cuenta, dc.servicio_cx, dc.facturado, dc.paquetizado,"+ConstantesBD.codigoEstadoFCargada+") as valort, " +
	    					"'"+ConstantesBD.acronimoNo+"' as es_portatil, " +
	    					"'' as codigo_portatil, " +
	    					"to_char(dc.fecha_modifica,'DD/MM/YYYY') AS fecha_cargo, " +
	    					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+"),'---') AS codigo, " +
	    					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioSoat+"),'---') AS codigoalterno, " +
	    					"UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")) AS descripcion, " +
	    					"1 AS cantidad, " +
	    					"0 AS total_cargo, " +
	    					"0 AS total_recargo, " +
	    					"0 AS total_dcto " +
	    				"FROM " +
	    					"det_cargos dc " +
	    					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
	    					"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud)" +
	    					"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo) " +
	    					" INNER JOIN convenios c  ON(dc.convenio             =c.codigo)  INNER JOIN sub_cuentas sbc  ON(dc.sub_cuenta=sbc.sub_cuenta) " +
	        			"WHERE " +
	    				"dc.sub_cuenta = "+idSubCuenta+" " +
	    				"AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
	    				"AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
	    				"AND dd.cantidad > 0 " +
	    				"AND dc.articulo is null " +
	    				") " +
	            	"UNION ALL " +
	            	"(" +
	            		"SELECT "+
	            			"coalesce(dc.servicio_cx,-1) as servicio_cx, " +
	            			"0 as es_encabezado_cx, "+
	            			"dc.solicitud, "+
	            			"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
	            			"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
	            			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '---' END AS paquete, " +
	            			"CASE WHEN dc.servicio IS NOT NULL THEN " +
	            			"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
	            			"ELSE " +
	            			"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
	            			"END AS ctc, " +
	            			"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'---') AS profesional, "+
	            			"sum(coalesce(dc.valor_unitario_cargado, 0)) AS valoru, " +
	            			"-1 AS valort, " +
	            			"'"+ConstantesBD.acronimoNo+"' as es_portatil, " +
	            			"'' as codigo_portatil, "+
	            			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
	            			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') AS codigo, "+
	            			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') AS codigoalterno, "+
	            			"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28) AS descripcion, "+
	            			"1 AS cantidad, "+
	            			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
	            			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo,0)) ELSE 0 END AS total_recargo, "+
	            			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto,0)) ELSE 0 END AS total_dcto "+ 
	            		"FROM " +
	            			"det_cargos dc "+
	            			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
	            			"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud) " +
	            			"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo)" +
	            			"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
	            			" INNER JOIN convenios c ON(dc.convenio  =c.codigo)" +
	    					" INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) "+
	    				"WHERE " +
	            		"dc.sub_cuenta = "+idSubCuenta+" " +
	            		"AND dc.solicitud="+numeroSolicitud+" " +
	            		"AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
	            		"AND dc.tipo_solicitud ="+ConstantesBD.codigoTipoSolicitudCirugia+" " +
	            		"AND dd.cantidad > 0 " +
	            		"AND dc.articulo is null " +
	            		"GROUP BY dc.servicio_cx, dc.fecha_modifica,dc.solicitud,dc.servicio_cx,tpa.codigo_asocio,tpa.nombre_asocio,dc.cargo_padre,dc.servicio,dc.articulo,dc.det_cx_honorarios,dc.valor_unitario_cargado, c.codigo, c.tipo_codigo_articulos,   sbc.ingreso,  c.nombre,  c.tipo_regimen " +
	    			")"+ 
	    		") "+
	    		//La consulta de los materiales especiales
	    		"UNION ALL "+
	    		"( "+
	    			"SELECT "+
	    			"coalesce(dc.servicio_cx,-1) as servicio_cx, " +
	    			"0 as es_encabezado_cx, "+
	    			"dc.solicitud, "+
	    			"'---' AS asocio, " +
	    			"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
	    			"CASE     WHEN c.tipo_regimen = 'P'   THEN getNomDeudorIngreso(sbc.ingreso)    ELSE c.nombre   END          AS convenio, " +
	    			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre) ELSE '---' END AS paquete, " +
	    			"CASE WHEN dc.servicio IS NOT NULL THEN " +
					"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"ELSE " +
					"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
					"END AS ctc, " +
					"'---' AS profesional, "+
					"sum(coalesce(dc.valor_unitario_cargado, 0)) AS valoru, " +
					"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
					"'"+ConstantesBD.acronimoNo+"' as es_portatil, " +
					"'' as codigo_portatil, "+
	    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
	    			"'---' AS codigo, "+
	    			"'---' AS codigoalterno, "+
	    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
	    			"sum(dc.cantidad_cargada) AS cantidad, "+
	    			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
	    			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END AS total_recargo, "+
	    			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END AS total_dcto "+ 
	    			"FROM det_cargos dc "+ 
	    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
	    			"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud)" +
	    			"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo)" +
	    			"WHERE " +
	    			"dc.sub_cuenta = "+idSubCuenta+" " +
	    			"AND dc.solicitud="+numeroSolicitud+" " +
	    			"AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
	    			"AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"' " +
	    			"AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
	    			"AND dd.cantidad > 0 " +
	    			"AND dc.articulo is not null " +	    			   			  
	    		"GROUP BY  dc.servicio_cx, dc.fecha_modifica,dc.solicitud,dc.cargo_padre,dc.servicio,dc.articulo,dc.valor_unitario_cargado,  c.codigo, c.tipo_codigo_articulos,   sbc.ingreso,  c.nombre,  c.tipo_regimen "+ 
	    		") "+
	    	") t "+
	    "GROUP BY t.\"servicio_cx\", t.\"es_encabezado_cx\", t.\"fecha_cargo\",t.\"solicitud\",t.\"codigo\",t.\"convenio\",t.\"codigoalterno\",t.\"descripcion\",t.\"asocio\",t.\"paquete\",t.\"ctc\",t.\"valoru\",t.\"valort\",t.\"profesional\",t.\"es_portatil\",t.\"codigo_portatil\" "+
	    "ORDER BY t.\"solicitud\" ASC, t.\"servicio_cx\" ASC,  t.\"es_encabezado_cx\" desc  ";
	    return query;
	}
	
	/**
	 * Mï¿½todo para obtener la consulta de la consulta de impresion por item del estado de la cuenta
	 * @param idSubCuenta
	 * @return
	 */
	public String consultaImprimirItem2EstadoCuenta(String idSubCuenta)
	{
		String query="SELECT tabla.tipom AS tipomp, " +
				"tabla.nomtipoc AS nomtipocp, " +
				"sum(tabla.valori) AS valorip, " +
				"sum(tabla.valori2) AS valori2p " +
		"FROM " +
			"(SELECT tp.nombre AS tipom, " +
					"ctc.nombre AS nomtipoc, " +
					"sum(coalesce((f.valor_bruto_pac-f.val_desc_pac),0)) AS valori, " +
					"CASE WHEN f.estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+" THEN sum(coalesce((f.valor_bruto_pac-f.val_desc_pac),0)) ELSE sum(f.valor_abonos) END AS valori2 " +
			"FROM det_cargos dc " +
					"LEFT OUTER JOIN facturas f ON (dc.codigo_factura=f.codigo) " +
					"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
					"LEFT OUTER JOIN convenios c ON (dc.convenio=c.codigo) " +
					"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) " +
					"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) " +
			"WHERE dc.sub_cuenta="+idSubCuenta+" AND dc.codigo_factura IS NOT NULL AND ctc.codigo<>"+ConstantesBD.codigoClasificacionTipoConvenioParticular+" " +
			"GROUP BY tp.nombre,ctc.nombre,f.estado_paciente,f.codigo) tabla GROUP BY tabla.tipom,tabla.nomtipoc";
		return query;
	}
	
	
	/**
	 * Mï¿½todo que retorna la consulta del estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirResSolConEstadoCuenta(String idIngreso,int remite)
	{
		String query =  "SELECT "+ 
		    	"t.fecha_cargo, "+
		    	"t.convenio, " +
		    	"t.asocio, " +
		    	"t.centro_costo_solicitado, " +
		    	"t.nomcc, " +
		    	"t.codigo_manual, " +
		    	"t.profesional, " +
		    	"t.valoru, " +
				"t.valort, " +
		    	"t.codigo, "+
		    	"t.descripcion, "+
		    	"sum(t.cantidad) As cantidad, "+
		    	"sum(t.total_cargo) AS total_cargo, "+
		    	"sum(t.total_recargo) AS total_recargo, "+
		    	"sum(t.total_dcto) AS total_dcto, "+
		    	"to_char(current_date, 'DD/MM/YYYY') AS fechaActual," +
		    	"t.esquema_tarifario," +
		    	"t.identificadorcc  " +
		    	"FROM "+
		    	"( "+
		    		"( "+
		    			"SELECT "+ 
						"0 as solicitud, "+ //se deja vacï¿½o para acomodarse con el union de cirugias
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"'' AS asocio, " +
						"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
						"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
						"cc.nombre AS nomcc, " +
						"c.tipo_codigo AS codigo_manual, " +
						"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
						
						//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
						
						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"cast (getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS text)"+ 
						"ELSE "+ 
						"CASE WHEN c.tipo_codigo_articulos=1 THEN CAST(dc.articulo AS TEXT) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)AS TEXT) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) AS TEXT) 	END END "+ 
			    		"END AS codigo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
						"ELSE "+ 
							"UPPER(getdescripcionarticulo(dc.articulo)) "+ 
						"END AS descripcion, "+
						"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
						"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
						"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
						"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto," +
						"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+							
						"FROM det_cargos dc "+
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
						"WHERE " +
						"sbc.ingreso = "+idIngreso+" AND " +
						"dc.cargo_padre IS NULL AND ";
				        if(remite==2)
							query+="dc.facturado='S' AND ";					        
				        query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
						"dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						"dc.valor_total_cargado > 0 AND " +
						"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
						"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
						"GROUP BY 2,3,4,5,dc.fecha_modifica,dc.servicio,dc.articulo,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_codigo_articulos,c.tipo_regimen,sbc.ingreso,cc.nombre,sol.codigo_medico_responde,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc "+
		    		") "+
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
		    			"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
		    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
						"cc.nombre AS nomcc, " +
		    			"c.tipo_codigo AS codigo_manual, " +
		    			"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
		    			
		    			//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
		    			
						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			//"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
		    			" cast( getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS TEXT) As codigo,"+
		    			"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
		    			"1 AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto," +
		    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+			    			
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
		    			"WHERE " +
		    			"sbc.ingreso = "+idIngreso+" AND ";
		    			if(remite==2)
							query+="dc.facturado='S' AND ";
		    			query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
		    			"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
		    			"dc.valor_total_cargado > 0 AND " +
		    			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,tpa.codigo_asocio,tpa.nombre_asocio,sol.centro_costo_solicitado,c.tipo_codigo,dc.det_cx_honorarios,dc.servicio_cx,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc,dc.servicio " +
		    		") "+
		    		//La consulta de los materiales especiales
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"'' AS asocio, " +
		    			"sol.centro_costo_solicitado AS centro_costo_solicitado," +
		    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc, " +
						"cc.nombre AS nomcc, " +
		    			"c.tipo_codigo AS codigo_manual, " +
		    			"'' AS profesional, " +
		    			
		    			//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
		    			
		    			"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"null  AS codigo, "+
		    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
		    			"sum(dc.cantidad_cargada) AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto," +
		    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario   "+
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
		    			"WHERE " +
		    			"sbc.ingreso = "+idIngreso+" AND ";
		    			if(remite==2)
							query+="dc.facturado='S' AND ";
		    			query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
		    			"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
		    			"dc.valor_total_cargado > 0 AND " +
		    			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is not null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado, dc.esquema_tarifario,identificadorcc"+ 
		    		") "+
		    	") t "+
		    "GROUP BY t.fecha_cargo,t.codigo,t.descripcion,t.convenio,t.asocio,t.centro_costo_solicitado,t.codigo_manual,t.profesional,t.valoru,t.valort,t.nomcc,t.esquema_tarifario,t.identificadorcc "+
		    "ORDER BY convenio, fecha_cargo, codigo"; 
		
		
		return query;
			
			
	}
	
	/**
	 * M&eacute;todo para realizar la consulta Birt para el reporte resumido por solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idIngreso
	 * @param remite
	 * @return
	 */	
	
	public String consultaImprimirResSolConEstadoCuentaEquivalente(String idIngreso,int remite)
	{
		String query =  "SELECT "+ 
		    	"t.\"fecha_cargo\" as fecha_cargo, "+
		    	"t.\"convenio\" as convenio, " +
		    	"t.\"asocio\" as asocio, " +
		    	"t.\"centro_costo_solicitado\" as centro_costo_solicitado, " +
		    	"t.\"nomcc\" as nomcc, " +
		    	"t.\"codigo_manual\" as codigo_manual, " +
		    	"t.\"profesional\" as profesional, " +
		    	"t.\"valoru\" as valoru, " +
				"t.\"valort\" as valort, " +
		    	"t.\"codigo\" as codigo, "+
		    	"t.\"descripcion\" as descripcion, "+
		    	"sum(t.\"cantidad\") As cantidad, "+
		    	"sum(t.\"total_cargo\") AS total_cargo, "+
		    	"sum(t.\"total_recargo\") AS total_recargo, "+
		    	"sum(t.\"total_dcto\") AS total_dcto, "+
		    	"to_char(current_date, '"+ConstantesBD.formatoFechaAp+"') AS fechaActual," +
		    	"t.\"esquema_tarifario\" as esquema_tarifario," +
		    	"t.\"identificadorcc\" as identificadorcc  " +
		    	"FROM "+
		    	"( "+
		    		"( "+
		    			"SELECT "+ 
						"0 as solicitud, "+ //se deja vacï¿½o para acomodarse con el union de cirugias
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"'' AS asocio, " +
						"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
						"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
						"cc.nombre AS nomcc, " +
						"c.tipo_codigo AS codigo_manual, " +
						"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+						
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+						
						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"cast(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") as text)"+ 
						"ELSE "+ 
						"CASE WHEN c.tipo_codigo_articulos=1 THEN CAST(dc.articulo AS TEXT) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)AS TEXT) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) AS TEXT) 	END END "+ 
			    		"END AS codigo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
						"ELSE "+ 
							"UPPER(getdescripcionarticulo(dc.articulo)) "+ 
						"END AS descripcion, "+
						"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
						"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
						"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
						"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto," +
						"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+							
						"FROM det_cargos dc "+
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
						"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud) " +
						"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden AND dd.articulo = dc.articulo) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
						"WHERE " +
						"sbc.ingreso = "+idIngreso+" AND " +
						"dc.cargo_padre IS NULL AND ";
				        if(remite==2)
							query+="dc.facturado='S' AND ";					        
				        query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
						"dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						"dc.valor_total_cargado > 0 AND  " +
						"dd.cantidad > 0 AND " +
						"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
						"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
						"GROUP BY dc.fecha_modifica,dc.servicio,dc.articulo,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_codigo_articulos,c.tipo_regimen,c.nombre,sbc.ingreso,cc.nombre,sol.codigo_medico_responde,dc.valor_unitario_cargado,dc.esquema_tarifario "+
		    		") "+
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
		    			"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
		    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
						"cc.nombre AS nomcc, " +
		    			"c.tipo_codigo AS codigo_manual, " +
		    			"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
		    			
		    			//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
		    			
						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			//"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
		    			" cast( getcodigocups(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS TEXT) As codigo,"+
		    			"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
		    			"1 AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto," +
		    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+			    			
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud) " +
						"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
		    			"WHERE " +
		    			"sbc.ingreso = "+idIngreso+" AND ";
		    			if(remite==2)
							query+="dc.facturado='S' AND ";
		    			query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
		    			"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
		    			"dc.valor_total_cargado > 0 AND " +
		    			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dd.cantidad > 0 AND " +
		    			"dc.articulo is null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,tpa.codigo_asocio,tpa.nombre_asocio,sol.centro_costo_solicitado,c.tipo_codigo,dc.det_cx_honorarios,dc.servicio_cx,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado,dc.esquema_tarifario,dc.servicio " +
		    		") "+
		    		//La consulta de los materiales especiales
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"'' AS asocio, " +
		    			"sol.centro_costo_solicitado AS centro_costo_solicitado," +
		    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc, " +
						"cc.nombre AS nomcc, " +
		    			"c.tipo_codigo AS codigo_manual, " +
		    			"'' AS profesional, " +
		    			
		    			//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
		    			
		    			"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"null  AS codigo, "+
		    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
		    			"sum(dc.cantidad_cargada) AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto," +
		    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario   "+
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud)  " +
		    			"INNER JOIN despacho d on (dc.solicitud = d.numero_solicitud) " +
		    			"INNER JOIN detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo) " +		    			
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
		    			"WHERE " +
		    			"sbc.ingreso = "+idIngreso+" AND ";
		    			if(remite==2)
							query+="dc.facturado='S' AND ";
		    			query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
		    			"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
		    			"dc.valor_total_cargado > 0 AND " +
		    			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
		    			"dd.cantidad > 0 AND " +
		    			"dc.articulo is not null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado, dc.esquema_tarifario "+ 
		    		") "+
		    	") t "+
		    "GROUP BY t.\"fecha_cargo\",t.\"codigo\",t.\"descripcion\",t.\"convenio\",t.\"asocio\",t.\"centro_costo_solicitado\",t.\"codigo_manual\",t.\"profesional\",t.\"valoru\",t.\"valort\",t.\"nomcc\",t.\"esquema_tarifario\",t.\"identificadorcc\" "+
		    "ORDER BY t.\"convenio\", t.\"fecha_cargo\", t.\"codigo\""; 
		
		
		return query;
			
			
	}

	
	
	/**
	 * Mï¿½todo para retornar la consulta de la impresion de estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirDetsolCueEstadoCuenta(String idIngreso,int remite)
	{
		String query = "SELECT "+
				"t.orden, "+
				"t.convenio, " +
				"t.factura, " +
				"t.asocio, " +
				"t.valoru, " +
				"t.valort, " +
				"t.usuario, " +
				"t.paquete," +
				"t.fecha_cargo, "+
				"t.profesional, "+
				"t.centro_costo_solicita, " +
				"t.centro_costo_ejecuta, "+
				"t.codigo, "+
				"t.grupo, "+
				"t.descripcion, "+
				"t.cantidad, "+
				"t.total_cargo, "+
				"t.total_recargo, "+
				"t.total_dcto, "+
				"to_char(current_date, '"+ConstantesBD.formatoFechaAp+"') AS fechaActual " +
				"FROM "+ 
				"( "+
					"( "+
						"SELECT "+ 
						"sol.consecutivo_ordenes_medicas As  orden, "+
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"dc.codigo_factura AS factura, " +
						"'' AS asocio, " +
						
						//Modificado por la Tarea 51069
						//"(((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo,0)))-(coalesce(dc.valor_unitario_dcto,0))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
						
						"((((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)))-(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)))) AS valort, " +
						"dc.usuario_modifica AS usuario, " +
						"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
						"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
						"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"CAST (getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") as text) "+ 
						"ELSE "+ 
						"CASE WHEN c.tipo_codigo_articulos=1 THEN cast(dc.articulo as text) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)as text) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) as text) 	END END"+ 
						" END AS codigo, "+
						 "  CASE WHEN dc.servicio IS NOT NULL THEN facturacion.getnomtiposervicio(dc.servicio)" +
					      "  ELSE inventarios.getnomclaseinventario(inventarios.getclaseinventarioarticulo(dc.articulo)) END as grupo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
						"ELSE "+ 
							"UPPER(getdescripcionalternaarticulo(dc.articulo)) "+ 
						"END AS descripcion, "+
						"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN coalesce(dc.valor_total_cargado,0) ELSE 0 END AS total_cargo, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) ELSE 0 END AS total_recargo, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) ELSE 0 END AS total_dcto "+ 
						"FROM det_cargos dc "+ 
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(sbc.convenio=c.codigo) " +
						"WHERE " +
						"sbc.ingreso = "+idIngreso+" AND ";
						if(remite==2)
							query+="dc.facturado='S' AND ";
						query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND "+
						"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
						"dc.valor_total_cargado > 0 AND " +
						"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+
					") "+ 
					"UNION "+ 
					"( "+
						"SELECT "+ 
						"sol.consecutivo_ordenes_medicas As  orden, "+
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"dc.codigo_factura AS factura, " +
						"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
						
						//Modificado por la Tarea 51069
						//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
						
						"((((sum(coalesce(dc.valor_unitario_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
						"dc.usuario_modifica AS usuario, " +
						"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
						"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
						"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
						"CAST (getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as text) AS codigo, "+
						" facturacion.getnomtiposervicio(dc.servicio_cx) AS grupo, "+
						"UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")) AS descripcion, "+
						"1 AS cantidad, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END AS total_cargo, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo,0)) ELSE 0 END AS total_recargo, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto,0)) ELSE 0 END AS total_dcto "+ 
						"FROM det_cargos dc "+ 
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(sbc.convenio=c.codigo) " +
						"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
						"WHERE " +
						"sbc.ingreso = "+idIngreso+" AND ";
						if(remite==2)
							query+="dc.facturado='S' AND ";
						query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
						"dc.valor_total_cargado > 0 AND " +
						"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
						"dc.articulo is null "+
						"GROUP BY 1,2,3,4,7,8,9,10,11,12,13,14,15,dc.cargo_padre,dc.valor_unitario_cargado "+
					") "+
					//Detalle de los materiales especiales
					"UNION "+ 
					"( "+
						"SELECT "+ 
						"sol.consecutivo_ordenes_medicas As  orden, "+
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"dc.codigo_factura AS factura, " +
						"'' AS asocio, " +
						
						//Modificado por la Tarea 51069
						//"(((coalesce(dc.valor_unitario_cargado,0))+(coalesce(dc.valor_unitario_recargo,0)))-(coalesce(dc.valor_unitario_dcto,0))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
						
						"((((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)))-(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)))) AS valort, " +
						"dc.usuario_modifica AS usuario, " +
						"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"'' AS profesional, "+
						"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
						"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
						"CASE WHEN c.tipo_codigo_articulos=1 THEN cast(dc.articulo as text) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)as text) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) as text) 	END END AS codigo,"+ 
						" inventarios.getnomclaseinventario(inventarios.getclaseinventarioarticulo(dc.articulo)) AS grupo, "+ 
						"UPPER(getdescripcionalternaarticulo(dc.articulo)) AS descripcion, "+
						"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN coalesce(dc.valor_total_cargado,0) ELSE 0 END AS total_cargo, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) ELSE 0 END AS total_recargo, "+
						"CASE WHEN dc.cargo_padre IS NULL THEN coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) ELSE 0 END AS total_dcto "+ 
						"FROM det_cargos dc "+ 
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(sbc.convenio=c.codigo) " +
						"WHERE " +
						"sbc.ingreso = "+idIngreso+" AND ";
						if(remite==2)
							query+="dc.facturado='S' AND ";
						query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
						"dc.valor_total_cargado > 0 AND " +
						"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
						"dc.articulo is not null "+
					") "+
				") t "+ 
			"ORDER BY convenio,fecha_cargo,codigo";
				
		return query;
	}
	
	/**
	 * Mï¿½todo que consulta la impresion del estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirResSolCueEstadoCuenta(String idIngreso,int remite)
	{
		String  query = "SELECT "+ 
		    	"t.fecha_cargo, "+
		    	"t.convenio, " +
		    	"t.asocio, " +
		    	"t.centro_costo_solicitado, " +
		    	"t.nomcc, " +
		    	"t.codigo_manual, " +
		    	"t.profesional, " +
		    	"t.valoru, " +
				"t.valort, " +
		    	"t.codigo, "+
		    	"t.descripcion, "+
		    	"sum(t.cantidad) As cantidad, "+
		    	"sum(t.total_cargo) AS total_cargo, "+
		    	"sum(t.total_recargo) AS total_recargo, "+
		    	"sum(t.total_dcto) AS total_dcto, "+
		    	"to_char(current_date, 'DD/MM/YYYY') AS fechaActual," +
		    	"t.esquema_tarifario," +
		    	"t.identificadorcc  " +
		    	"FROM "+
		    	"( "+
		    		"( "+
		    			"SELECT "+ 
						"0 as solicitud, "+ //se deja vacï¿½o para acomodarse con el union de cirugias
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"'' AS asocio, " +
						"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
						"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
						"cc.nombre AS nomcc, " +
						"c.tipo_codigo AS codigo_manual, " +
						"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
						
						//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
						
						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))))*1) AS valort, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"CAST (getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") as text) "+ 
						"ELSE "+ 
						"CASE WHEN c.tipo_codigo_articulos=1 THEN cast(dc.articulo as text) ELSE CASE WHEN c.tipo_codigo_articulos=0 THEN  cast (facturacion.getcodigocumarticulo(dc.articulo)as text) ELSE cast (facturacion.getcodigointerfaz(dc.articulo) as text) 	END END "+ 
						"END AS codigo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
						"ELSE "+ 
							"UPPER(getdescripcionarticulo(dc.articulo)) "+ 
						"END AS descripcion, "+
						"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
						"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
						"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
						"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto," +
						"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+							
						"FROM det_cargos dc "+
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
						"WHERE " +
						"sbc.ingreso = "+idIngreso+" AND " +
						"dc.cargo_padre IS NULL AND ";
				        if(remite==2)
							query+="dc.facturado='S' AND ";					        
				        query+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
						"dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						"dc.valor_total_cargado > 0 AND " +
						"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
						"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
						"GROUP BY 2,3,4,5,dc.fecha_modifica,dc.servicio,dc.articulo,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_codigo_articulos,c.tipo_regimen,sbc.ingreso,cc.nombre,sol.codigo_medico_responde,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc "+
		    		") "+
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
		    			"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
		    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
						"cc.nombre AS nomcc, " +
		    			"c.tipo_codigo AS codigo_manual, " +
		    			"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
		    			
		    			//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
		    			
						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))))*1) AS valort, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			//"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
		    			" CAST (getcodigocups(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") as text) As codigo,"+
		    			"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
		    			"1 AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto," +
		    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+			    			
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
		    			"WHERE " +
		    			"sbc.ingreso = "+idIngreso+" AND ";
		    			if(remite==2)
							query+="dc.facturado='S' AND ";
		    			query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
		    			"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
		    			"dc.valor_total_cargado > 0 AND " +
		    			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,tpa.codigo_asocio,tpa.nombre_asocio,sol.centro_costo_solicitado,c.tipo_codigo,dc.det_cx_honorarios,dc.servicio_cx,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc,dc.servicio " +
		    		") "+
		    		//La consulta de los materiales especiales
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"'' AS asocio, " +
		    			"sol.centro_costo_solicitado AS centro_costo_solicitado," +
		    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc, " +
						"cc.nombre AS nomcc, " +
		    			"c.tipo_codigo AS codigo_manual, " +
		    			"'' AS profesional, " +
		    			
		    			//Modificado por la Tarea 51069
						//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
						"coalesce(dc.valor_unitario_cargado, 0) AS valoru, "+
		    			
		    			"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))))*1) AS valort, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"null  AS codigo, "+
		    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
		    			"sum(dc.cantidad_cargada) AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto," +
		    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario   "+
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
		    			"WHERE " +
		    			"sbc.ingreso = "+idIngreso+" AND ";
		    			if(remite==2)
							query+="dc.facturado='S' AND ";
		    			query+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
		    			"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
		    			"dc.valor_total_cargado > 0 AND " +
		    			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is not null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado, dc.esquema_tarifario,identificadorcc"+ 
		    		") "+
		    	") t "+
		    "GROUP BY t.fecha_cargo,t.codigo,t.descripcion,t.convenio,t.asocio,t.centro_costo_solicitado,t.codigo_manual,t.profesional,t.valoru,t.valort,t.nomcc,t.esquema_tarifario,t.identificadorcc "+
		    "ORDER BY convenio, fecha_cargo, codigo"; 
		  return query;
	}
	
	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de consultaInventarioFisicoArticulos1
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public String articulosConInventarioFisico(Connection con, String articulos,int almacen, String patronOrdenar){
		String sql = "SELECT " +
							"t.descripcion, " +
							"t.codigo, " +
							"getunidadmedidaarticulo(t.codigo) as unidad_medida, " +
							"coalesce(t.codigo_interfaz,'') as codigo_interfaz, " +
							"coalesce(t.lote,'N/A') as lote , " +
							"coalesce(t.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
							"t.codigo_lote, " +
							"max (getExistenciasArticulo(t.codigo,t.ultima_preparacion)) as existencia, " +
							"max (getFechaTomaPreparacion(t.codigo,t.ultima_preparacion)) as fecha_toma, " +
							"max (getUsuarioModificaPreparacion(t.codigo,t.ultima_preparacion)) as usuario_modifica, " +
							"max (t.ajuste) as ajuste, " +
							"max (axif.codigo) as codajuste, " +
							"SUM (getCantRegistroConteo(t.ultima_preparacion, t.numero_conteo)) AS cant_conteo, " +
							"getubicacionarticulo(t.codigo, t.almacen) AS ubicacion " +
						"FROM " +
							"( " +
								"SELECT " +
									"getdescarticulosincodigo(f.codigo) AS descripcion, " +
									"f.codigo, " +
									"f.unidad_medida, " +
									"f.codigo_interfaz, " +
									"f.lote, " +
									"f.fecha_vencimiento || '' as fecha_vencimiento, " +
									"f.codigo_lote, " +
									"f.numero_conteo, " +
									"f.codigo_dapa, " +
									"max (f.ajuste) as ajuste, " +
									"max (getExistenciasArticulo(f.codigo,f.codigo_preparacion)) as existencia, " +
									"max (getFechaTomaPreparacion(f.codigo,f.codigo_preparacion)) as fecha_toma, " +
									"max (getUsuarioModificaPreparacion(f.codigo,f.codigo_preparacion)) as usuario_modifica, " +
									"max (getCodRegistroConteoInv(f.numero_conteo,f.codigo_preparacion)) as codregistroconteo, " +
									"max (f.codigo_preparacion) as ultima_preparacion, " +
									"f.almacen " +
								"FROM " +
									"( " +
										"(	" +
											"SELECT " +
												"a.descripcion, " +
												"a.codigo, " +
												"a.unidad_medida, " +
												"a.codigo_interfaz, " +
												"l.lote, " +
												"l.codigo as codigo_lote, " +
												"to_char(l.fecha_vencimiento, 'dd/mm/yyyy') as fecha_vencimiento, " +
												"a.concentracion, " +
												"a.forma_farmaceutica, " +
												"a.naturaleza, " +
												"a.subgrupo, " +
												"max (rci.codigo_preparacion) as codigo_preparacion, " +
												"pti.codigo_dapa, " +
												"max (rci.codigo_ajuste) as ajuste, " +
												"max (rci.numero_conteo) AS numero_conteo, " +
												"l.almacen AS almacen " +
											"FROM " +
												"articulo a, " +
												"articulo_almacen_x_lote l " +
											"INNER JOIN " +
												"preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote) " +
											"INNER JOIN " +
												"registro_conteo_inventario rci ON (rci.articulo=l.articulo and rci.codigo_preparacion=pti.codigo) " +
											"WHERE " +
												"a.codigo=l.articulo " +
												"and a.maneja_lotes='S' " +
												"AND rci.almacen ="+almacen+"  " +
												"AND rci.articulo IN ("+articulos+") "+
												"and pti.estado='FIN' " +
											"GROUP BY " +
												"a.descripcion, " +
												"a.codigo, " +
												"a.unidad_medida, " +
												"a.codigo_interfaz, " +
												"l.lote, l.codigo, " +
												"l.fecha_vencimiento, " +
												"a.concentracion, " +
												"a.forma_farmaceutica, " +
												"a.naturaleza, " +
												"a.subgrupo, " +
												"pti.codigo_dapa, " +
												"l.almacen " +
										") " +
										"UNION " +
										"( " +
											"SELECT " +
												"a.descripcion, " +
												"a.codigo, " +
												"a.unidad_medida, " +
												"a.codigo_interfaz, " +
												"'N/A' as lote, " +
												"-1 as codigo_lote, " +
												"'N/A' as fecha_vencimiento, " +
												"a.concentracion, " +
												"a.forma_farmaceutica, " +
												"a.naturaleza, " +
												"a.subgrupo, " +
												"max (rci.codigo_preparacion) as codigo_preparacion, " +
												"pti.codigo_dapa, " +
												"max (rci.codigo_ajuste) as ajuste, " +
												"max (rci.numero_conteo) AS numero_conteo, " +
												"ex.almacen AS almacen " +
											"FROM " +
												"articulos_almacen ex " +
											"INNER JOIN " +
												"articulo a ON (a.codigo=ex.articulo) " +
											"INNER JOIN " +
												"preparacion_toma_inventario pti ON (pti.articulo=a.codigo) " +
											"INNER JOIN " +
												"registro_conteo_inventario rci ON (rci.articulo=pti.articulo and rci.codigo_preparacion=pti.codigo) " +
											"WHERE " +
												"a.maneja_lotes = 'N' " +
												"AND rci.almacen ="+almacen+"  " +
												"AND rci.articulo IN ("+articulos+") " +
												"AND pti.estado='FIN' " +
											"GROUP BY " +
												"a.descripcion, " +
												"a.codigo, " +
												"a.unidad_medida, " +
												"a.codigo_interfaz, " +
												"a.concentracion, " +
												"a.forma_farmaceutica, " +
												"a.naturaleza, " +
												"a.subgrupo, " +
												"pti.codigo_dapa, " +
												"ex.almacen " +
										") " +
									") f " +
								"GROUP BY " +
									"f.descripcion, " +
									"f.concentracion, " +
									"f.forma_farmaceutica, " +
									"f.naturaleza, " +
									"f.codigo, " +
									"f.unidad_medida, " +
									"f.codigo_interfaz, " +
									"f.lote, " +
									"f.fecha_vencimiento, " +
									"f.codigo_lote, " +
									"f.numero_conteo, " +
									"f.codigo_dapa, " +
									"f.almacen " +
								")t " +
							"INNER JOIN " +
								"ajustes_x_inv_fisico axif ON (axif.codigo=t.ajuste) " +
							"GROUP BY " +
								"t.descripcion, " +
								"t.codigo, " +
								"t.unidad_medida, " +
								"t.codigo_interfaz, " +
								"t.lote, " +
								"t.fecha_vencimiento, " +
								"t.codigo_lote, " +
								"t.almacen " +
							"ORDER BY " + patronOrdenar;
		return sql;
	}
	
	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de consultaInventarioFisicoArticulos
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public String articulosSinInventarioFisico(Connection con, String articulos,int almacen, String patronOrdenar){
		String sql = "SELECT " +
						"getdescarticulosincodigo(f.codigo) AS descripcion, " +
						"f.codigo as codigo, " +
						"getunidadmedidaarticulo(f.codigo) as unidad_medida, " +
						"f.codigo_interfaz as codigo_interfaz, " +
						"coalesce(f.lote,'N/A') as lote, " +
						"coalesce(f.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
						"f.codigo_lote as codigo_lote, " +
						"f.fecha_toma as fecha_toma, " +
						"f.existencia as existencia, " +
						"f.ultima_preparacion as ultima_preparacion, " +
						"f.usuario_modifica as usuario_modifica " +
					"FROM " +
						"( " +
							"( " +
								"SELECT " +
									"a.descripcion, " +
									"a.codigo, " +
									"a.unidad_medida, " +
									"a.codigo_interfaz, " +
									"ex.lote, " +
									"ex.codigo as codigo_lote, " +
									"to_char(ex.fecha_vencimiento, 'dd/mm/yyyy')||'' as fecha_vencimiento, " +
									"a.concentracion, " +
									"a.forma_farmaceutica, " +
									"a.naturaleza, " +
									"a.subgrupo, " +
									"' ' AS cod_prep, " +
									"' ' AS fecha_toma, " +
									"' ' as existencia, " +
									"' ' as usuario_modifica, " +
									"' ' as numero_conteo, " +
									"' ' AS ultima_preparacion " +
								"FROM " +
									"articulo a, " +
									"articulo_almacen_x_lote ex " +
								"LEFT JOIN " +
									"det_articulos_por_almacen dapa ON (dapa.articulo = ex.articulo) " +
								"LEFT JOIN " +
									"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
								"WHERE 	" +
									"a.codigo=ex.articulo " +
									"AND a.maneja_lotes='S' " +
									"AND ex.almacen = " + almacen +" "+
									"AND ex.articulo NOT IN ( select articulo from preparacion_toma_inventario ex where ex.almacen="+almacen+" ) " +
									"AND ex.articulo IN ("+articulos+") " +
							") " +
							"UNION " +
							"( " +
								"SELECT " +
									"a.descripcion, " +
									"a.codigo, " +
									"a.unidad_medida, " +
									"a.codigo_interfaz, " +
									"'N/A' as lote, " +
									"-1 as codigo_lote, " +
									"'N/A' as fecha_vencimiento, " +
									"a.concentracion, " +
									"a.forma_farmaceutica, " +
									"a.naturaleza, " +
									"a.subgrupo, " +
									"' ' AS cod_prep, " +
									"' ' as fecha_toma, " +
									"' ' as existencia, " +
									"' ' as usuario_modifica, " +
									"' ' as numero_conteo, " +
									"' ' as  ultima_preparacion " +
								"FROM " +
									"articulos_almacen ex " +
								"INNER JOIN " +
									"articulo a ON ( a.codigo = ex.articulo) " +
								"LEFT JOIN " +
									"det_articulos_por_almacen dapa ON ( dapa.articulo = ex.articulo) " +
								"LEFT JOIN  " +
									"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
								"WHERE " +
									"a.maneja_lotes = 'N' " +
									"AND ex.almacen = " + almacen +" "+
									"AND ex.articulo NOT IN (select articulo from preparacion_toma_inventario ex where ex.almacen="+almacen+" ) " +
									"AND ex.articulo IN ("+articulos+") " +
							") " +
						") f " +
					"LEFT JOIN  " +
						"det_articulos_por_almacen dapa ON ( dapa.articulo = f.codigo) " +
					"LEFT JOIN " +
						"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
					"GROUP BY " +
						"f.descripcion, " +
						"f.codigo, " +
						"f.unidad_medida, " +
						"f.codigo_interfaz, " +
						"f.lote, " +
						"f.fecha_vencimiento, " +
						"f.codigo_lote, " +
						"f.fecha_toma, " +
						"f.existencia, " +
						"f.ultima_preparacion, " +
						"f.usuario_modifica, " +
						"f.concentracion, " +
						"f.forma_farmaceutica, " +
						"f.naturaleza " +
					"ORDER BY " +patronOrdenar;
		return sql;
	}
	
	public String consultarPazYSalvo(String codPyS)
	{
		return SqlBaseConsultasBirtDao.consultarPazYSalvo(codPyS);
	}
	
	public String consultarExtractosDeudor(DtoDeudor dto)
	{
		return SqlBaseConsultasBirtDao.consultarExtractosDeudor(dto);
	}
	
	public String reportePagosCarteraPacientePagos(int codigoPk,String fechaIni, String fechaFin,
			int anioIni, int anioFin, 
			String tipoPeriodo) 
	{
		return SqlBaseConsultasBirtDao.reportePagosCarteraPacientePagos(codigoPk, fechaIni, fechaFin, anioIni, anioFin, tipoPeriodo);
	}

	@Override
	public String reportePagosCarteraPaciente(String fechaIni, String fechaFin,
			int anioIni, int anioFin, int centroAtencion, String tipoDoc,
			String tipoPeriodo) {
		
		return null;
	}
	
	public String consultarDocumentosCarteraPaciente(DtoDocumentosGarantia dto)
	{
		return SqlBaseConsultasBirtDao.consultarDocumentosCarteraPaciente(dto);
	}
	
	public String reporteEstadoCarteraYGlosas(String fechaCorte,String tipoConvenio, int convenio, int institucionInt, int consulta)
	{
		String validacion="<>";
		return SqlBaseConsultasBirtDao.reporteEstadoCarteraYGlosas(fechaCorte,tipoConvenio,convenio,institucionInt,consulta, validacion);
	}
	
	public String reporteFacturasReiteradas(String fechaCte,String tipoconvenio, int convenio, int institucion)
	{
		return SqlBaseConsultasBirtDao.reporteFacturasReiteradas(fechaCte, tipoconvenio, convenio, institucion);
	}
	
	public String reporteFacturacionEventoRadicar(String centroAtencion,String convenio, String fechaElabIni, String fechaElabFin,String factIni, String factFin, String viaIngreso, int consulta)
	{
		String validacion="<>";
		return SqlBaseConsultasBirtDao.reporteFacturacionEventoRadicar(centroAtencion, convenio, fechaElabIni, fechaElabFin, factIni, factFin, viaIngreso, consulta, validacion);
	}
	
	public String EdadGlosaFechaRadicacion(HashMap criterios)
	{
		String validacion="<>"; 
		return SqlBaseConsultasBirtDao.EdadGlosaFechaRadicacion(criterios, validacion);
	}
	
	public String ReporteFacturasVencidasNoObjetadas(HashMap criterios)
	{
		return SqlBaseConsultasBirtDao.ReporteFacturasVencidasNoObjetadas(criterios);
	}
	
	/**
	 * Método para obtener la consulta de la carta de instrucciones de garantía
	 * @param idIngreso
	 * @return
	 */
	public String cartaInstruccionesGarantia(String idIngreso)
	{
		return SqlBaseConsultasBirtDao.cartaInstruccionesGarantia(idIngreso);
	}
	
	/**
	 * Método para obtener las consultas de cada DATESET del reporte 
	 * docgarantiaActaCompromiso.rptdesign
	 * @param parametros
	 * @return
	 */
	public ArrayList<String> actaCompromisoCondicionesIngreso(HashMap parametros)
	{
		return SqlBaseConsultasBirtDao.actaCompromisoCondicionesIngreso(parametros);
	}
	
	/**
	 * Método para obtener la consulta del DATaset del reporte docGarantiaPaghaere1.rptdesign
	 * @param parametros
	 * @return
	 */
	public String docGarantiaPagare1(HashMap parametros)
	{
		return SqlBaseConsultasBirtDao.docGarantiaPagare1(parametros);
	}
	
	/**
	 * Método para obtener la cosnulta del DATASET de docGarantiaCheque1.rptdesign 
	 * @param parametros
	 * @return
	 */
	public String docGarantiaCheque1(HashMap parametros)
	{
		return SqlBaseConsultasBirtDao.docGarantiaCheque1(parametros);
	}
	
	/**
	 * 
	 */
	public String impresionConceptosReciboCaja(HashMap parametros)
	{
		return SqlBaseConsultasBirtDao.impresionConceptosReciboCaja(parametros);
	}
	
	/**
	 * 
	 */
	public String impresionTotalesReciboCaja(HashMap parametros)
	{
		return SqlBaseConsultasBirtDao.impresionTotalesReciboCaja(parametros);
	}

	@Override
	public String honorariosMedicosValFacturaValHonorario(HashMap filtros) {
		
		return SqlBaseConsultasBirtDao.honorariosMedicosValFacturaValHonorario(filtros);
	}

	@Override
	public String honorariosMedicosValHonorario(HashMap filtros) {
		
		return SqlBaseConsultasBirtDao.honorariosMedicosValHonorario(filtros);
	}

	@Override
	public String consultarNotasPorCodigo(int codigo) {
		return SqlBaseConsultasBirtDao.consultarNotasPorCodigo(codigo);
	}
	
	@Override
	public String impresionFacturaVaria(int institucion, int consecutivofacvar,
			boolean manejaMultiInstitucion) {
		return SqlBaseConsultasBirtDao.impresionFacturaVaria(institucion, consecutivofacvar,
				manejaMultiInstitucion);
	}
	
	@Override
	public String consultaUltimosSignosRE(String idCuenta) {
		return SqlBaseConsultasBirtDao.consultaUltimosSignosRE(idCuenta);
	}
	
	@Override
	public String consultarRompimientoPiso() {
		return SqlBaseConsultasBirtDao.consultarRompimientoPiso();
	}

	@Override
	public String consultarRompimientoPiso1() {
		return SqlBaseConsultasBirtDao.consultarRompimientoPiso1();
	}

	@Override
	public String consultarRompimientoCentroCosto() {
		return SqlBaseConsultasBirtDao.consultarRompimientoCentroCosto();
	}

	@Override
	public String consultarRompimientoCentroCosto1() {
		return SqlBaseConsultasBirtDao.consultarRompimientoCentroCosto1();
	}

	@Override
	public String consultarRompimientoEstadoJus() {
		return SqlBaseConsultasBirtDao.consultarRompimientoEstadoJus();
	}

	@Override
	public String consultarRompimientoEstadoJus1() {
		return SqlBaseConsultasBirtDao.consultarRompimientoEstadoJus1();
	}
}