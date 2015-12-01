/*
 * Creado el 29/12/2005
 * Jorge Armando OSorio Velasquez	
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * 
 * @author Jorge Armando OSorio Velasquez	
 * 
 * CopyRight Princeton S.A.
 * 29/12/2005
 */
public class SqlBaseConsultaHonorariosMedicosDao
{

	/**
	 * Cadenas para la consulta del listado de profesionales.
	 */
	private static String cadenaConsultaProfesionales=" select " +
															"m.codigo_medico as codigomedico,getnombrepersona(m.codigo_medico) as profesional,p.tipo_identificacion as tipoid,p.numero_identificacion as numeroid,m.numero_registro as registro,getocupacion(m.ocupacion_medica) as ocupacion " +
														"from " +
															"facturas f " +
														"inner join " +
															"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
														"inner join " +
															"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
														"inner join " +
															"medicos m on (s.codigo_medico_responde=m.codigo_medico) " +
														"inner join " +
															"personas p on(s.codigo_medico_responde=p.codigo) " +
														"inner join " +
															"centro_atencion ca on (ca.consecutivo=f.centro_aten) " +
														"left outer join " +
															"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
	
	private static String cadenaConsultaProfesionalescx=" select " +
															"m.codigo_medico as codigomedico,getnombrepersona(m.codigo_medico) as profesional,p.tipo_identificacion as tipoid,p.numero_identificacion as numeroid,m.numero_registro as registro,getocupacion(m.ocupacion_medica) as ocupacion " +
														"from " +
															"facturas f " +
														"inner join " +
															"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
														"inner join " +
															"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
														"inner join " +
															"centro_atencion ca on (ca.consecutivo=f.centro_aten) " +
														"inner join asocios_det_factura adf on(adf.codigo=dfs.codigo) " +
														"inner join " +
														"medicos m on (adf.codigo_medico=m.codigo_medico) " +
														"inner join " +
														"personas p on (m.codigo_medico=p.codigo) " +
														"left outer join " +
															"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";


	
	/**
	 * Cadenas para la consulta del listado de profesionales.
	 */
	private static String cadenaConsultaProfesionalesPaquetizacion=" select " +
															"m.codigo_medico as codigomedico,getnombrepersona(m.codigo_medico) as profesional,p.tipo_identificacion as tipoid,p.numero_identificacion as numeroid,m.numero_registro as registro,getocupacion(m.ocupacion_medica) as ocupacion " +
														"from " +
															"facturas f " +
														"inner join " +
															"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
														"inner join " +
															"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
														"inner join " +
															"paquetizacion_det_factura pdf on (pdf.codigo_det_fact = dfs.codigo) " +
														"inner join " +
														"medicos m on (pdf.medico_asocio=m.codigo_medico) " +
														"inner join " +
															"personas p on(pdf.medico_asocio=p.codigo) " +
														"inner join " +
																"centro_atencion ca on (ca.consecutivo=f.centro_aten) " +
														"left outer join " +
															"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
	
	
	private static String cadenaWhereConsultaProfesionales=" where s.tipo!="+ConstantesBD.codigoTipoSolicitudPaquetes+" and s.tipo!="+ConstantesBD.codigoTipoSolicitudCirugia+" and dfs.valor_pool>0 ";
	private static String cadenaWhereConsultaProfesionalesPaquetizacion=" where s.tipo="+ConstantesBD.codigoTipoSolicitudPaquetes+" and pdf.valor_pool>0 ";
	private static String cadenaWhereConsultaCX=" where s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" and adf.valor_pool>0 ";
	
	private static String cadenaGroupByProfesionales=" group by m.codigo_medico,p.tipo_identificacion,p.numero_identificacion,m.numero_registro,m.ocupacion_medica";
	
	/**
	 * Cadena para consultar el detalle de honorarios
	 */
	private static String cadenaConsultaDetalleHonorariosPro=" SELECT " +
																" f.codigo as codigofactura," +
                                                                " f.estado_facturacion as codestado," +
                                                                " f.centro_aten as codigocentroatencion," +
                                                                " getNomCentroAtencion(f.centro_aten) as nombrecentroatencion, " +
                                                                " eff.nombre as descestado," +
                                                                " f.convenio as codconvenio," +
                                                                " getnombreconvenio(f.convenio) as nomconvenio," +
																" f.consecutivo_factura as factura," +
																" to_char(f.fecha, 'DD/MM/YYYY')||' '||f.hora as fechafactura," +
																" s.consecutivo_ordenes_medicas as orden," +
																" dfs.servicio as codigoservicio," +
																" ser.especialidad as codigoespecialidad," +
																" ser.especialidad||'-'||dfs.servicio as codigoaxioma," +
																" getnombreservicio(dfs.servicio,0) as nombreservicio," +
																" (dfs.valor_cargo+dfs.valor_recargo) as valorservicio," +
																" (dfs.ajustes_debito-dfs.ajustes_credito) as valorajuste," +
																" s.pool as codigopool," +
																" getdescripcionpool(s.pool) as descpool," +
																" CASE WHEN dfs.porcentaje_pool IS NULL THEN 0 ELSE dfs.porcentaje_pool END as porcentajepool," +
																" CASE WHEN dfs.porcentaje_medico IS NULL THEN 0 ELSE dfs.porcentaje_medico END as porcentajemedico," +
																" to_char(s.fecha_solicitud, 'DD/MM/YYYY')||' '||s.hora_solicitud as fechasolicitud," +
																" (dfs.ajuste_debito_medico-dfs.ajuste_credito_medico) as valorajustesmedico " +
															" from " +
																" facturas f " +
																" inner join det_factura_solicitud dfs on(f.codigo=dfs.factura) " +
																" inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
																" inner join servicios ser on (dfs.servicio = ser.codigo) " +
                                                                " inner join estados_factura_f eff on(f.estado_facturacion=eff.codigo)" +
                                                                " left outer join anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) "+
															" where " +
																"s.codigo_medico_responde=? " +
																"and f.institucion=?  " +
																//and dfs.pool is not null " + 
																"and s.codigo_medico_responde is not null " +
																"and dfs.valor_pool>0 ";
	
	/**
	 * Cadena para consultar el detalle de honorarios para el caso en el cual la solicitud es de tipo paquete
	 */
	private static String cadenaConsultaDetalleHonorariosProP=" SELECT " +
																" f.codigo as codigofactura," +
                                                                " f.estado_facturacion as codestado," +
                                                                " f.centro_aten as codigocentroatencion," +
                                                                " getNomCentroAtencion(f.centro_aten) as nombrecentroatencion, " +
                                                                " eff.nombre as descestado," +
                                                                " f.convenio as codconvenio," +
                                                                " getnombreconvenio(f.convenio) as nomconvenio," +
																" f.consecutivo_factura as factura," +
																" to_char(f.fecha, 'DD/MM/YYYY')||' '||f.hora as fechafactura," +
																" s.consecutivo_ordenes_medicas as orden," +
																" pdf.servicio as codigoservicio," +
																" ser.especialidad as codigoespecialidad," +
																" ser.especialidad||'-'||pdf.servicio as codigoaxioma," +
																" getnombreservicio(pdf.servicio,0) as nombreservicio," +
																" (pdf.valor_cargo+pdf.valor_recargo) as valorservicio," +
																" (pdf.ajustes_debito-pdf.ajustes_credito) as valorajuste," +
																" pdf.pool as codigopool," +
																" getdescripcionpool(pdf.pool) as descpool," +
																" CASE WHEN pdf.porcentaje_pool IS NULL THEN 0 ELSE pdf.porcentaje_pool END as porcentajepool," +
																" CASE WHEN pdf.porcentaje_medico IS NULL THEN 0 ELSE pdf.porcentaje_medico END as porcentajemedico," +
																" to_char(s.fecha_solicitud, 'DD/MM/YYYY')||' '||s.hora_solicitud as fechasolicitud," +
																" (pdf.ajuste_debito_medico-pdf.ajuste_credito_medico) as valorajustesmedico " +
															" from " +
																" facturas f " +
																" inner join det_factura_solicitud dfs on(f.codigo=dfs.factura) " +
																" inner join paquetizacion_det_factura pdf on (pdf.codigo_det_fact = dfs.codigo) " +
																" inner join solicitudes s on(pdf.solicitud=s.numero_solicitud) " +
																" inner join servicios ser on (pdf.servicio = ser.codigo) " +
                                                                " inner join estados_factura_f eff on(f.estado_facturacion=eff.codigo)" +
                                                                " left outer join anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) "+
															" where " +
															"pdf.medico_asocio=? " +
															"and f.institucion=?  " +
															//"and pdf.pool is not null " +
															"and pdf.medico_asocio is not null " +
															"and pdf.valor_pool>0 ";
    

	/**
	 * Cadena para consultar el detalle de honorarios para el caso en el cual la solicitud es de tipo paquete
	 */
	private static String cadenaConsultaDetalleHonorariosCX=" SELECT " +
																" f.codigo as codigofactura," +
                                                                " f.estado_facturacion as codestado," +
                                                                " f.centro_aten as codigocentroatencion," +
                                                                " getNomCentroAtencion(f.centro_aten) as nombrecentroatencion, " +
                                                                " eff.nombre as descestado," +
                                                                " f.convenio as codconvenio," +
                                                                " getnombreconvenio(f.convenio) as nomconvenio," +
																" f.consecutivo_factura as factura," +
																" to_char(f.fecha, 'DD/MM/YYYY')||' '||f.hora as fechafactura," +
																" s.consecutivo_ordenes_medicas as orden," +
																" adf.servicio_asocio as codigoservicio," +
																" ser.especialidad as codigoespecialidad," +
																" ser.especialidad||'-'||adf.servicio_asocio as codigoaxioma," +
																" getnombreservicio(adf.servicio_asocio,0) as nombreservicio," +
																" (adf.valor_cargo+adf.valor_recargo) as valorservicio," +
																" (adf.ajustes_debito-adf.ajustes_credito) as valorajuste," +
																" adf.pool as codigopool," +
																" getdescripcionpool(adf.pool) as descpool," +
																" CASE WHEN adf.porcentaje_pool IS NULL THEN 0 ELSE adf.porcentaje_pool END as porcentajepool," +
																" CASE WHEN adf.porcentaje_medico IS NULL THEN 0 ELSE adf.porcentaje_medico END as porcentajemedico," +
																" to_char(s.fecha_solicitud, 'DD/MM/YYYY')||' '||s.hora_solicitud as fechasolicitud," +
																" (adf.ajuste_debito_medico-adf.ajuste_credito_medico) as valorajustesmedico " +
															" from " +
																" facturas f " +
																" inner join det_factura_solicitud dfs on(f.codigo=dfs.factura) " +
																" inner join asocios_det_factura adf on (adf.codigo = dfs.codigo) " +
																" inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
																" inner join servicios ser on (adf.servicio_asocio = ser.codigo) " +
                                                                " inner join estados_factura_f eff on(f.estado_facturacion=eff.codigo)" +
                                                                " left outer join anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) "+
															" where " +
																"adf.codigo_medico=? " +
																"and f.institucion=?  " +
																"and adf.pool is not null " +
																"and adf.codigo_medico is not null " +
																"and adf.valor_pool>0 ";
    

	
    /**
     * Cadena para ordenar los honorarios
     */
    private static String cadenaOrderDetalleHonorarios=" order by factura,orden,codigoaxioma ";
	
	/**
	 * Metodo que realiza la consulta de los profesionales de la salud.
	 * @param con
	 * @param vo
	 * @return
	 */
    public static HashMap consultarProfesionalesSalud(Connection con, HashMap vo)
	{
		//String condiciones=cadenaWhereConsultaProfesionales;
		String condicionesGenerales="";
		
		String condicionesEspecificas =		//" and dfs.pool is not null " + OJO, verificar antes de cambiar, es posible que el pool llegue null cuando no se maneja liquidación por pool, Juan David
											" and s.codigo_medico_responde is not null ";
		
		String condicionesPaquetizacion = 	" and pdf.pool is not null " +
				" and pdf.medico_asocio is not null ";
		
		String condicionescx = 	" and adf.pool is not null " +
								" and adf.codigo_medico is not null ";
						
		if (vo.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoNo))
			condicionesGenerales=condicionesGenerales+" and f.estado_facturacion != "+ConstantesBD.codigoEstadoFacturacionAnulada+" ";
		
		if(!(vo.get("pool")+"").equals("-1") || Utilidades.convertirAEntero(vo.get("pool"+"")+"")!=ConstantesBD.codigoNuncaValido){
			condicionesEspecificas=condicionesEspecificas+" and s.pool="+vo.get("pool")+" ";
			condicionesPaquetizacion=condicionesPaquetizacion+" and pdf.pool="+vo.get("pool")+" ";
			condicionescx=condicionescx+" and adf.pool="+vo.get("pool")+" ";
		}
		
		if(!(vo.get("especialidad")+"").equals("-1")) 
			condicionesGenerales=condicionesGenerales+" and s.especialidad_solicitada="+vo.get("especialidad")+" ";
		
		if(!(vo.get("profesional")+"").equals("-1")){
			condicionesEspecificas=condicionesEspecificas+" and s.codigo_medico_responde="+vo.get("profesional")+" ";
			condicionesPaquetizacion=condicionesPaquetizacion+" and pdf.medico_asocio="+vo.get("profesional")+" ";
			condicionescx=condicionescx+" and adf.codigo_medico="+vo.get("profesional")+" ";
		}
			
		if(!(vo.get("centroAtencion")+"").equals("-1"))
			condicionesGenerales=condicionesGenerales+" and f.centro_aten="+vo.get("centroAtencion")+" ";
		
		if(vo.get("fechaInicial")!=null&&!(vo.get("fechaInicial")+"").equals("null")&&!(vo.get("fechaInicial")+"").equals("")){
			condicionesGenerales=condicionesGenerales+"  and (to_char(f.fecha, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' ";
			
			if (vo.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoSi))
				condicionesGenerales+=" OR to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') ";
			else
				condicionesGenerales+=") ";
		}
			
		if(vo.get("facturaInicial")!=null&&!(vo.get("facturaInicial")+"").equals("null")&&!(vo.get("facturaInicial")+"").equals(""))
			condicionesGenerales=condicionesGenerales+"  and f.consecutivo_factura between "+vo.get("facturaInicial")+" and "+vo.get("facturaFinal")+" ";
		
		if(vo.get("institucionSel")!=null&&!(vo.get("institucionSel")+"").equals("null")&&!(vo.get("institucionSel")+"").equals("")&&!(vo.get("institucionSel")+"").equals("-1"))
			condicionesGenerales=condicionesGenerales+"  and f.institucion= "+vo.get("institucionSel")+" ";
		
		if(!(vo.get("nroOrden")+"").equals("-1") && !(vo.get("nroOrden")+"").equals(""))
			condicionesGenerales=condicionesGenerales+" and s.consecutivo_ordenes_medicas="+vo.get("nroOrden")+" ";
		
		if(!(vo.get("nroOrden")+"").equals("-1") && !(vo.get("nroOrden")+"").equals(""))
			condicionesGenerales=condicionesGenerales+" and s.consecutivo_ordenes_medicas="+vo.get("nroOrden")+" ";
		
		if(!vo.get("ciudad").toString().equals(""))
			condicionesGenerales=condicionesGenerales+" and ca.ciudad='"+vo.get("ciudad")+"' ";
		
		if(Utilidades.convertirAEntero(vo.get("pais")+"")>0 && !(vo.get("pais")+"").equals(""))
			condicionesGenerales=condicionesGenerales+" and ca.pais='"+vo.get("pais")+"' ";
		
		String cadena= "SELECT " +
							"* " +
						"FROM (" +
							cadenaConsultaProfesionales+cadenaWhereConsultaProfesionales+condicionesGenerales+condicionesEspecificas+cadenaGroupByProfesionales + 
						" UNION " +
							cadenaConsultaProfesionalescx+cadenaWhereConsultaCX+condicionesGenerales+condicionescx+cadenaGroupByProfesionales +
						" UNION " +
							cadenaConsultaProfesionalesPaquetizacion+cadenaWhereConsultaProfesionalesPaquetizacion+condicionesGenerales+condicionesPaquetizacion+cadenaGroupByProfesionales +
						") tabla order by profesional";	
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			Log4JManager.info(cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		//return (HashMap)mapa.clone();
		Utilidades.imprimirMapa(mapa);
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param profesional
	 * @param restricciones 
	 * @return
	 */
	public static HashMap consultarHonorariosProfesional(Connection con, int institucion, int profesional, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String sql="";
		String condiciones=" ";
		String condicionesEspecificas=" ";
		String condicionesPaquetizacion=" ";
		String condicionescx="";
        
        if (vo.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoNo))
			condiciones=condiciones+" and f.estado_facturacion != "+ConstantesBD.codigoEstadoFacturacionAnulada+" ";
        
        if(!(vo.get("pool")+"").equals("-1")){
        	condicionesEspecificas=condicionesEspecificas+" and s.pool="+vo.get("pool")+" "; 
        	condicionesPaquetizacion=condicionesPaquetizacion+" and pdf.pool="+vo.get("pool")+" ";
        	condicionescx=condicionescx+" and adf.pool="+vo.get("pool")+" ";
        }
            
        if(!(vo.get("especialidad")+"").equals("-1")) 
        	condiciones=condiciones+" and s.especialidad_solicitada="+vo.get("especialidad")+" ";
        
		if(!(vo.get("centroAtencion")+"").equals("-1"))
			condiciones=condiciones+" and f.centro_aten="+vo.get("centroAtencion")+" ";
		
        if(vo.get("fechaInicial")!=null&&!(vo.get("fechaInicial")+"").equals("null")&&!(vo.get("fechaInicial")+"").equals(""))
            condiciones=condiciones+"  and (f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' OR to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') ";
			
        if(vo.get("facturaInicial")!=null&&!(vo.get("facturaInicial")+"").equals("null")&&!(vo.get("facturaInicial")+"").equals(""))
            condiciones=condiciones+"  and f.consecutivo_factura between "+vo.get("facturaInicial")+" and "+vo.get("facturaFinal")+" ";
		
		try
		{
			sql = 	"SELECT * FROM ( " +
						" ( "+
							cadenaConsultaDetalleHonorariosPro+condiciones+condicionesEspecificas+
						" ) UNION ( "+
							cadenaConsultaDetalleHonorariosCX+condiciones+condicionescx+
						" ) UNION ( "+
							cadenaConsultaDetalleHonorariosProP+condiciones+condicionesPaquetizacion+
						" ) "+
					") tabla " + cadenaOrderDetalleHonorarios;
			Log4JManager.info(sql+" \nprofesional: "+profesional+"\ninstitucion: "+institucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, sql);
			ps.setInt(1,profesional);
			ps.setInt(2,institucion);
			ps.setInt(3,profesional);
			ps.setInt(4,institucion);
			ps.setInt(5,profesional);
			ps.setInt(6,institucion);
			mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		//return (HashMap)mapa.clone();
		Utilidades.imprimirMapa(mapa);
		return mapa;
	}
}