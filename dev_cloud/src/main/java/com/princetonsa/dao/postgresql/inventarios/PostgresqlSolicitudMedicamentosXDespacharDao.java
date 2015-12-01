package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.AjustesXInventarioFisicoDao;
import com.princetonsa.dao.inventarios.ComparativoUltimoConteoDao;
import com.princetonsa.dao.inventarios.SolicitudMedicamentosXDespacharDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseAjustesXInventarioFisicoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseComparativoUltimoConteoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseSolicitudMedicamentosXDespacharDao;
import com.princetonsa.mundo.inventarios.AjustesXInventarioFisico;
import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

/**
 * @author garias@princetonsa.com
 */
public class PostgresqlSolicitudMedicamentosXDespacharDao implements SolicitudMedicamentosXDespacharDao {
	
	/**
	 * 
	 */
	public String crearConsultaMedicamentosXDespachar(Connection con, HashMap filtros) 
	{
		String cadena = "SELECT DISTINCT " +
				"s.fecha_solicitud||' / '||s.hora_solicitud as fechahora, " +
				"s.fecha_solicitud as fecha, " +
				"s.hora_solicitud as hora, " +
				"'Médico: ' || lower(getnombrepersona(s.codigo_medico)) as medico, " +
				"'Habitación: ' || coalesce(getcamacuenta(c.id, c.via_ingreso), '') as cama, " +
				"ds.cantidad - coalesce(getcantdespachoart(a.codigo, s.numero_solicitud), 0) as cantidad, " +
				//"ds.frecuencia||' - '||ds.tipo_frecuencia as frecuencia, " +
				"ds.frecuencia ||' - '||" +
					"CASE WHEN ds.tipo_frecuencia='Horas' " +
					"THEN 'H' " +
					"ELSE " +
						"(CASE WHEN ds.tipo_frecuencia='Minutos' " +
						"THEN 'M' " +
						"ELSE " +
							"(CASE WHEN ds.tipo_frecuencia='Días' " +
							"THEN 'D' END ) " +
						"END) " +
					"END AS frecuencia, " +
				"ds.observaciones as observaciones, " +
				"ds.dosis||' - '|| uxa.unidad_medida as dosis, " +
				"ds.via as via, " +
				"lower(getdescarticulosincodigo(a.codigo)) as descripcion, " +
				//"a.unidad_medida as umedida, ";
				"CASE WHEN a.unidad_medida = 'UND' THEN '' ELSE a.unidad_medida END as umedida, " ;
		
		// codigo axioma
		if (filtros.get("tipoCodigo").toString().equals("axioma"))
		cadena += "a.codigo as codigo, ";
		
		// codigo interfaz
		if (filtros.get("tipoCodigo").toString().equals("interfaz"))
		cadena += "a.codigo_interfaz as codigo, ";
		
		// ambos
		if (filtros.get("tipoCodigo").toString().equals("ambos"))
		cadena += "getCodArticuloAxiomaInterfaz(a.codigo,'"+ConstantesIntegridadDominio.acronimoAmbos+"') AS codigo, ";
		
		cadena +=			"'Paciente: ' || lower(getnombrepersona(i.codigo_paciente)) as paciente, " +
				"'No. Ingreso: ' || i.consecutivo as ingreso " +
			"FROM " +
				"solicitudes s " +
			"INNER JOIN " +
				"cuentas c ON (c.id = s.cuenta) " +
			"INNER JOIN " +
				"detalle_solicitudes ds ON (ds.numero_solicitud=s.numero_solicitud) " +
			"INNER JOIN " +
				"articulo a ON (a.codigo=ds.articulo) " +
			"INNER JOIN " +
				"ingresos i ON (i.id=c.id_ingreso) " +
			"LEFT OUTER JOIN " +
				"unidosis_x_articulo uxa ON (uxa.codigo=ds.unidosis_articulo) " +
			"WHERE ";
		
		// Tipo de medicamentos
		cadena += "s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+" ";
		
		// Estado historia clinica - solicitado
		cadena += "AND s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" ";
				
		// Rango de tiempo
		cadena += "AND (s.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"') ";
		
		// Almacen
		cadena += "AND s.centro_costo_solicitado="+filtros.get("almacen")+" ";
		
		// Centro de costo solicitante
		if(filtros.containsKey("centroCostoSolicitante"))
		if(!filtros.get("centroCostoSolicitante").toString().equals(""))
		cadena += "AND s.centro_costo_solicitante="+filtros.get("centroCostoSolicitante")+" ";
		
		// Piso
		if(filtros.containsKey("piso"))
		if(!filtros.get("piso").toString().equals(""))
		cadena += "AND getcodigopisocuenta(c.id, c.via_ingreso)="+filtros.get("piso")+" ";
		
		cadena += "ORDER BY " +
		"paciente, " +
		"fecha, " +
		"hora ";
		
		return cadena;
	}
	
	/**
	 * 
	 */
	public String crearConsultaMedicamentosXEntregar(Connection con, HashMap filtros) 
	{
		String cadena = "SELECT DISTINCT " +
				"d.fecha||' / '||d.hora as fechahora, " +
				"d.fecha as fecha, " +
				"d.hora as hora, " +
				"getnombrealmacen(s.centro_costo_solicitado) as almacen, " +
				"'Habitación: ' || coalesce(getcamacuenta(c.id, c.via_ingreso), '') as cama, " +
				"coalesce(dd.cantidad, -1) as cantidad, " +
				"ds.frecuencia ||' - '||" +
					"CASE WHEN ds.tipo_frecuencia='Horas' " +
					"THEN 'H' " +
					"ELSE " +
						"(CASE WHEN ds.tipo_frecuencia='Minutos' " +
						"THEN 'M' " +
						"ELSE " +
							"(CASE WHEN ds.tipo_frecuencia='Días' " +
							"THEN 'D' END ) " +
						"END) " +
					"END AS frecuencia, " +
				"coalesce(ds.observaciones,'') as observaciones, " +
				"ds.dosis||' - '|| uxa.unidad_medida as dosis, " +
				"ds.via as via, " +
				"lower(getdescarticulosincodigo(a.codigo)) as descripcion, " +
				//"a.unidad_medida as umedida, ";
				"CASE WHEN a.unidad_medida = 'UND' THEN '' ELSE a.unidad_medida END as umedida, " ;
		
		// codigo axioma
		if (filtros.get("tipoCodigo").toString().equals("axioma"))
		cadena += "a.codigo as codigo, ";
		
		// codigo interfaz
		if (filtros.get("tipoCodigo").toString().equals("interfaz"))
		cadena += "a.codigo_interfaz as codigo, ";
		
		// ambos
		if (filtros.get("tipoCodigo").toString().equals("ambos"))
		cadena += "getCodArticuloAxiomaInterfaz(a.codigo,'"+ConstantesIntegridadDominio.acronimoAmbos+"') AS codigo, ";
		
		
		cadena +=			"'Paciente: ' || lower(getnombrepersona(c.codigo_paciente)) as paciente, " +
				"'No. Ingreso: ' || getconsecutivoingreso(c.id_ingreso) as ingreso " +
			"FROM " +
				"despacho d " +
			"INNER JOIN " +
				"solicitudes s ON (s.numero_solicitud=d.numero_solicitud) " +
			"INNER JOIN " +
				"cuentas c ON (c.id = s.cuenta) " +
			"INNER JOIN " +
				"detalle_despachos dd ON (dd.despacho=d.orden) " +
			"INNER JOIN " +
				"articulo a ON (a.codigo=dd.articulo) " +
			"LEFT OUTER JOIN " +
				"detalle_solicitudes ds ON (ds.numero_solicitud=s.numero_solicitud and ds.articulo = dd.articulo) " +
			"LEFT OUTER JOIN " +
				"unidosis_x_articulo uxa ON (uxa.codigo=ds.unidosis_articulo) " +
			"WHERE ";
		
		// Rango de tiempo
		cadena += "(to_char(d.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+
		"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"') ";
		
		// Tipo de medicamentos
		cadena += "AND s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+" ";
		
		// Almacen
		cadena += "AND s.centro_costo_solicitado="+filtros.get("almacen")+" ";
		
		// Cantidad diferente de cero
		cadena += "AND dd.cantidad <> 0 ";
		
		// Centro de costo solicitante
		if(filtros.containsKey("centroCostoSolicitante"))
		if(!filtros.get("centroCostoSolicitante").toString().equals(""))
		cadena += "AND s.centro_costo_solicitante="+filtros.get("centroCostoSolicitante")+" ";
		
		// Piso
		if(filtros.containsKey("piso"))
		if(!filtros.get("piso").toString().equals(""))
		cadena += "AND getcodigopisocuenta(c.id, c.via_ingreso)="+filtros.get("piso")+" ";
		
		// Cadena en la que se contemplan los despachos realizados parcialmente
		String cadena2 = cadena+"AND s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" ";
		
		// Estado historia clinica - solicitado
		cadena += "AND s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCDespachada+","+ConstantesBD.codigoEstadoHCAdministrada+") ";
		
		
		String sql = "SELECT " +
			"g.fechahora," +
			"g.fecha, " +
			"g.hora, " +
			"g.almacen, " +
			"g.cama, " +
			"g.cantidad, " +
			"g.frecuencia, " +
			"g.observaciones, " +
			"g.dosis, " +
			"g.via, " +
			"g.descripcion, " +
			"g.umedida, " +
			"g.codigo," +
			"g.paciente, " +
			"g.ingreso " +
		"FROM " +
			"(("+cadena+") UNION ("+cadena2+")) g " +
		"ORDER BY " +
			"g.paciente, g.fecha, g.hora ";	
		
		
		
		return sql;
	}
	
	/**
	 * 
	 */
	public String crearConsultaMedicamentosDespachadosXPaciente(Connection con, HashMap filtros) 
	{
		return SqlBaseSolicitudMedicamentosXDespacharDao.crearConsultaMedicamentosDespachadosXPaciente(con, filtros);
	}
	
	/**
	 * Método encargado de Obtener el codigo de un centro de atención dado su nombre
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion)
	{
		return SqlBaseSolicitudMedicamentosXDespacharDao.obtenerCodigoCentroAtencion(con, nombreCentroAtencion);
	}
} 