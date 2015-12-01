package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;


public class SqlBaseSolicitudMedicamentosXDespacharDao {
	
	// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @return
	 */
	public static String crearConsultaMedicamentosDespachadosXPaciente(Connection con, HashMap filtros){
		
		String cadena = "SELECT DISTINCT " +
							"d.fecha||' / '||d.hora as fechaHora, " +
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
		
		
		cadena +=			"lower(getnombrepersona(c.codigo_paciente)) as paciente, " +
							"getconsecutivoingreso(c.id_ingreso) as ingreso " +
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
		cadena += "( to_char(d.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"') ";
		
		// Tipo de medicamentos
		cadena += "AND s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+" ";
		
		// Estado historia clinica - solicitado
		cadena += "AND s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCDespachada+","+ConstantesBD.codigoEstadoHCAdministrada+")";
		
		// Almacen
		cadena += "AND s.centro_costo_solicitado="+filtros.get("almacen")+" ";
		
		// Cuenta
		cadena += "AND c.id = "+filtros.get("cuenta")+" ";
		
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
		
		cadena += "ORDER BY " +
						"fecha, " +
						"hora ";
		
		return cadena;
	}
	
	/**
	 * Método encargado de Obtener el código de un centro de atención dado su nombre
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public static int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion)
	{
		logger.info("===> Entré a obtenerCodigoCentroAtencion, nombre centro de atención = "+nombreCentroAtencion);
		String consulta = "SELECT codigo FROM centro_atencion WHERE descripcion = '"+nombreCentroAtencion+"'";
		logger.info("===> La consulta es : "+consulta);

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getInt(1);
			
		}
		catch (SQLException e) 
		{
			logger.error("===> Se produjeron errores al consultar "+e);
			
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
}