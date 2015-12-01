package com.princetonsa.dao.sqlbase.facturasVarias;

import java.util.HashMap;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;



public class SqlBaseConsultaFacturasVariasDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseConsultaFacturasVariasDao.class);

	
	
	
	/**
	 * Cadena para la consulta de las Facturas Varias
	 */
	private static final String cadenaBuscarStr="SELECT " +
		"f.codigo_fac_var as codigofactura, " +
		"f.consecutivo as consecutivo, " +
		"f.fecha as fechaelaboracion, " +
		"f.deudor as deudor, " +
		"t.descripcion as descripcion, " +
		"t.numero_identificacion as numeroidentificacion, " +
		"f.concepto as concepto, " +
		"cf.descripcion as descripcionconcepto, " +
		"f.valor_factura as valorfactura, " +
		"f.estado_factura as estadofactura, " +
		"d.tipo as tipodeudor " +
		"from facturas_varias f  " +
		"inner join deudores d on(d.codigo=f.deudor) " +
		"inner join conceptos_facturas_varias cf on(cf.consecutivo=f.concepto) " +
		"left outer join terceros t on(t.codigo=d.codigo_tercero)  " +
		"where 1=1 ";
	
	/**
	 * Cadena para obtnener los datos adiconales de un paciente deudor
	 */
	private static final String cadenaDatosPacienteDeudorStr = "SELECT " +
		"p.tipo_identificacion || ' ' || p.numero_identificacion as numeroidentificacion, " +
		"p.primer_apellido || coalesce(' '||p.segundo_apellido,' ') || p.primer_nombre || coalesce(' '||p.segundo_nombre,' ') as descripcion " +
		"from deudores d " +
		"inner join personas p on(p.codigo = d.codigo_paciente) " +
		"where d.codigo = ?";
	
	/**
	 * Cadena para obtener los datos adicionales de una empresa deudor
	 */
	private static final String cadenaDatosEmpresaDeudorStr = "SELECT " +
		"coalesce (t.numero_identificacion,'') as numeroidentificacion, " +
		"coalesce (t.descripcion,'') as descripcion " +
		"FROM deudores d " +
		"inner join empresas e on(e.codigo = d.codigo_empresa) " +
		"inner join terceros t on(t.codigo = e.tercero) " +
		"WHERE d.codigo = ?";
	
	/**
	 * Cadena para obtener los datos adicionales de un tercero deudor
	 */
	private static final String cadenaDatosTerceroDeudorStr = "SELECT " +
		"coalesce (t.numero_identificacion, '') as numeroidentificacion, " +
		"coalesce (t.descripcion,'')  as descripcion " +
		"from deudores d " +
		"inner join terceros t on(t.codigo = d.codigo_tercero) " +
		"where d.codigo = ?";
	
	/**
	 * Cadena para obtener los datos adicionales de un deudor tipo otro
	 */
	private static final String cadenaDatosDeudorOtroStr = "SELECT " +
		"coalesce (d.numero_identificacion, '') as numeroidentificacion, " +
		"d.primer_nombre ||' '|| d.primer_apellido as descripcion " +
		"from deudores d " +		
		"where d.codigo = ?";
	
	
	
	/**
	 * Cadena para la consulta del detalle de las Facturas Varias Tipo Empresas
	 */
	public static String cadenaConsultaDetalleFacturasTipoEmpresas=" SELECT f.codigo_fac_var as codigofactura," +
																		" f.fecha as fechaelaboracion," +
																		" f.valor_factura as valorfactura, " +
																		" f.estado_factura as estadofactura, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then f.fecha_aprobacion||''  else '' end as fechaaprobacion, " +
																		" f.centro_atencion as centroatencion, " +
																		" getnomcentroatencion(f.centro_atencion) as nombrecentroatencion, " +
																		" f.centro_costo as centrocosto,  " +
																		" getnomcentrocosto(f.centro_costo) as nombrecentrocosto, " +
																		" f.concepto as concepto, " +
																		" cf.descripcion as descripcionconcepto, " +
																		" d.tipo as tipodeudor, " +
																		" e.razon_social as razonsocial, " +
																		" t.numero_identificacion as numeroidentificacion, " +
																		" e.direccion as direccionprincipal," +
																		" e.telefono as telefonoprincipal, " +
																		" email as email, " +
																		" e.nombre_representante as representantelegal, " +
																		" facturacion.getNombreContacto(e.codigo) as nombrecontacto, " +
																		" f.deudor as deudor, " +
																		" f.observaciones as observaciones, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then f.fecha_anulacion||'' else ''  end as fechaanulacion, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then f.usuario_anulacion else '' end  as usuarioanulacion, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then f.usuario_anulacion else '' end  as usuarioaprob, " +
																		" f.motivo_anulacion as motivo, " +
																		" f.consecutivo "+
																	"FROM facturas_varias f " +
																	"inner join deudores d on(d.codigo=f.deudor) " +
																	"left outer join empresas e on(e.codigo=d.codigo_empresa) " +
																	"left outer join terceros t on(t.codigo=e.tercero) " +
																	"left outer join conceptos_facturas_varias cf on(cf.consecutivo=f.concepto) "+
																	"WHERE f.codigo_fac_var=?";
	
	
	
	/**
	 * Cadena para la consulta del detalle de las Facturas Varias Tipo Otro Deudores
	 */
	public static String cadenaConsultaDetalleFacturasTipoOtro=" SELECT f.codigo_fac_var as codigofactura," +
																" f.fecha as fechaelaboracion, " +
																" f.valor_factura as valorfactura, " +
																" f.estado_factura as estadofactura, " +
																" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then f.fecha_aprobacion||''  else '' end as fechaaprobacion, " +
																" f.centro_atencion as centroatencion, " +
																" getnomcentroatencion(f.centro_atencion) as nombrecentroatencion, " +
																" f.centro_costo as centrocosto, " +
																" getnomcentrocosto(f.centro_costo) as nombrecentrocosto, " +
																" f.concepto as concepto, " +
																" cf.descripcion as descripcionconcepto, " +
																" d.tipo as tipodeudor, " +
																" t.descripcion as razonsocial, " +
																" t.numero_identificacion as numeroidentificacion, " +
																" d.direccion as direccionprincipal, " +
																" d.telefono as telefonoprincipal, " +
																" d.e_mail as email, " +
																" d.representante_legal as representantelegal, " +
																" d.nombre_contacto as nombrecontacto, " +
																" f.deudor as deudor, " +
																" f.observaciones as observaciones, " +
																" f.consecutivo, "+
																" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then f.fecha_anulacion||'' else ''  end as fechaanulacion, " +
																" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then f.usuario_anulacion else '' end  as usuarioanulacion, " +
																" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then f.usuario_anulacion else '' end  as usuarioaprob, " +
																" f.motivo_anulacion as motivo "+
																"FROM facturas_varias f " +
																"inner join deudores d on(d.codigo=f.deudor) " +
																"left outer join terceros t on(t.codigo=d.codigo_tercero)  " +
																"left outer join conceptos_facturas_varias cf on(cf.consecutivo=f.concepto)  " +
																//"left outer join aprob_anul_facturas_varias aa on(aa.factura_varia=f.codigo_fac_var) "+
																"WHERE f.codigo_fac_var=?";
	
	/**
	 * Cadena para la consulta del detalle de las Facturas Varias Tipo Otro Deudores
	 */
	public static String cadenaConsultaDetalleFacturasTipoPaciente=" SELECT " +
																		" f.codigo_fac_var as codigofactura," +
																		" f.fecha as fechaelaboracion, " +
																		" f.valor_factura as valorfactura, " +
																		" f.estado_factura as estadofactura, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then f.fecha_aprobacion||''  else '' end as fechaaprobacion, " +
																		" f.centro_atencion as centroatencion, " +
																		" getnomcentroatencion(f.centro_atencion) as nombrecentroatencion, " +
																		" f.centro_costo as centrocosto, " +
																		" getnomcentrocosto(f.centro_costo) as nombrecentrocosto, " +
																		" f.concepto as concepto, " +
																		" cf.descripcion as descripcionconcepto, " +
																		" d.tipo as tipodeudor, " +
																		/*" '' as razonsocial, " +
																		" '' as numeroidentificacion, " +
																		" d.direccion as direccionprincipal, " +
																		" d.telefono as telefonoprincipal, " +
																		" d.e_mail as email, " +*/
																		" coalesce(getnombrepersona2(d.codigo_paciente),'') AS razonsocial, " +
																		" p.tipo_identificacion||' '||p.numero_identificacion AS numeroidentificacion, " +
																		" p.direccion AS direccionprincipal, " +
																		" p.telefono AS telefonoprincipal, " +
																		" p.email AS email,  " +
																		" '' AS representantelegal, " +
																		" '' AS nombrecontacto,  " +
																		" d.representante_legal as representantelegal, " +
																		" d.nombre_contacto as nombrecontacto, " +
																		" f.deudor as deudor, " +
																		" f.observaciones as observaciones, " +
																		" f.consecutivo, "+
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then f.fecha_anulacion||'' else ''  end as fechaanulacion, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then f.usuario_anulacion else '' end  as usuarioanulacion, " +
																		" case when f.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then f.usuario_anulacion else '' end  as usuarioaprob, " +
																		" f.motivo_anulacion as motivo "+
																	"FROM " +
																		"facturas_varias f " +
																		"inner join deudores d on(d.codigo=f.deudor) " +
																		"left outer join personas p on(p.codigo = d.codigo_paciente)  " +
																		"left outer join conceptos_facturas_varias cf on(cf.consecutivo=f.concepto)  " +
																		//"left outer join aprob_anul_facturas_varias aa on(aa.factura_varia=f.codigo_fac_var) "+
																	"WHERE f.codigo_fac_var=?";
	
	
	//" " +
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param factura
	 * @param estadosFactura
	 * @param tipoDeudor
	 * @param deudor
	 * @return
	 */
	public static HashMap BusquedaFacturas(
			Connection con, 
			String fechaInicial,
			String fechaFinal,
			String factura,
			String estadosFactura,
			String tipoDeudor,
			String deudor) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaBuscarStr;
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND  fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
		}
		if(!factura.equals(""))
		{
			cadena+=" AND f.consecutivo='"+factura+"'";
		}
		if(!estadosFactura.equals(""))
		{
			cadena+=" AND estado_factura='"+estadosFactura+"'";
		}
		if(!tipoDeudor.equals(""))
		{
			cadena+=" AND d.tipo = '"+tipoDeudor+"' ";
		}
		if(!deudor.equals(""))
		{
			if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente) )
			{
				cadena+=" AND d.codigo_paciente = "+deudor;
			}
			else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros) )
			{
				cadena+=" AND d.codigo_tercero = "+deudor;
			}
			else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa) )
			{
				cadena+=" AND d.codigo_empresa = "+deudor;
			}
			else
			{
				cadena+=" AND deudor="+deudor;
			}
		}
		
		//cadena+="  order by codigo_fac_var";
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
			busqueda.close();
		}
		catch(SQLException e)
		{
			logger.info("Error en BusquedaFacturas: "+e);
		}
		
		logger.info("\n\n valor de la consulta cadena >> "+cadena);
		
		for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
		{
			String td = mapa.get("tipodeudor_"+i).toString();
			String cd = mapa.get("deudor_"+i).toString();
			String consulta = "";
			//************OBTENER DATOS ADICIONALES DE CADA DEUDOR DE TIPO EMPRESA********************
			if(td.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
			{				
				consulta = cadenaDatosEmpresaDeudorStr;
			}
			///************OBTENER DATOS ADICIONALES DE CADA DEUDOR DE TIPO TERCERO********************
			if(td.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
			{
				consulta = cadenaDatosTerceroDeudorStr;
			}
			///************OBTENER DATOS ADICIONALES DE CADA DEUDOR DE TIPO OTRO********************
			if(td.equals(ConstantesIntegridadDominio.acronimoOtro))
			{
				consulta = cadenaDatosDeudorOtroStr;
			}
			//************OBTENER DATOS ADICIONALES DE CADA DEUDOR DE TIPO PACIENTE********************
			if(td.equals(ConstantesIntegridadDominio.acronimoPaciente))
			{
				consulta = cadenaDatosPacienteDeudorStr;
			}
			
			try
			{			
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
				pst.setInt(1,Integer.parseInt(cd));
			
				logger.info("consul-> "+pst);				
				
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					mapa.put("numeroidentificacion_"+i, rs.getString("numeroidentificacion"));
					mapa.put("descripcion_"+i, rs.getString("descripcion"));
				}
			}
			catch(SQLException e)
			{
				logger.error("Error en BusquedaFacturas: "+e);
			}
		}
		
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param factura
	 * @param tipoDeudor
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleFactura(Connection con, int factura, String tipoDeudor) 
	{
		logger.info("entro a  consultaDetalleFactura  factura -->"+factura+"   tipodeudor -->"+tipoDeudor);
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			if(tipoDeudor.equals("DEUEMP"))
			{	
				logger.info("\n cadena  empresa -->"+cadenaConsultaDetalleFacturasTipoEmpresas);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleFacturasTipoEmpresas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(factura+""));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}
			else if (tipoDeudor.equals("DEUOTR"))
			{
				logger.info("\n cadena otro -->"+cadenaConsultaDetalleFacturasTipoOtro);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleFacturasTipoOtro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(factura+""));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}
			else if (tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente))
			{
				logger.info("\n cadena otro Paciente -->"+cadenaConsultaDetalleFacturasTipoPaciente+" factura:"+factura);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleFacturasTipoPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(factura+""));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LAS FACTUAS VARIAS "+e);
			e.printStackTrace();
		}
		return mapa;
	}

}
