package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticuloCatalogoDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.fwk.exception.IPSException;

public class SqlBaseConsultaTarifasDao 
{
	
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger=Logger.getLogger(SqlBaseArticuloCatalogoDao.class);
	
	
	/**
	 * Cadena para la Busqueda del Servicio
	 */
	private static final String cadenaBuscarStr="SELECT " +
				"s.codigo as codigoservicio," +
				"rs.codigo_propietario as codigo, " +
				"e.nombre as especialidad, " +
				"tp.nombre as tipoServicio, "+
				"gs.descripcion as grupo, " +
				"rs.descripcion as descripcion "+
			"FROM servicios s " +
			"INNER JOIN referencias_servicio rs on(s.codigo=rs.servicio and tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") " +
			"INNER JOIN especialidades e on(s.especialidad=e.codigo) " +
			"INNER JOIN tipos_servicio tp on(s.tipo_servicio=tp.acronimo) " +
			"INNER JOIN grupos_servicios gs on(s.grupo_servicio=gs.codigo) " +
			"WHERE rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"'";
	
	
	/**
	 * Cadena para la Busqueda del Articulo
	 */
	private static final String cadenaBuscarArticuloStr="SELECT " +
															" va.codigo as codigoArticulo, " +
															" va.descripcion as descripcionArticulo, " +
															" va.descripcionclase as clase, "+
															" va.descripciongrupo as grupoArticulo, " +
															" va.descripcionsubgrupo as subgrupo, " +
															" na.nombre as naturaleza "+
														"FROM view_articulos va INNER JOIN naturaleza_articulo na on(va.naturaleza=na.acronimo) WHERE 1=1 ";
	
	
	/**
	 * Cadena para la Busqueda del Articulo
	 */
	private static final String cadenaConsultaServicioStr="SELECT " +
			" et.nombre as esquemaTarifario," +
			" ma.nombre as tipoRedondeo," +
			" getnombreservicio(ts.servicio,0) as servicio," +
			" ts.fecha_modifica as fechaAsignacion," +
			" tl.nombre as tipoLiquidacion, "+
			" ts.valor_tarifa as valorTarifa," +
			" ts.usuario_modifica as usuarioAsigna, " +
			" ts.actualiza_automatica as tipoCambio, " +
			" ts.liq_asocios AS liquidarAsocios "+
			" FROM log_tarifas_servicios ts" +
			" INNER JOIN esquemas_tarifarios et on(ts.esquema_tarifario=et.codigo) " +
			" INNER JOIN metodos_ajuste ma on(ma.acronimo=et.metodo_ajuste) " +
			" INNER JOIN tipos_liquidacion_soat tl on(ts.tipo_liquidacion=tl.codigo)" +
			" WHERE ts.servicio=? AND et.tarifario_oficial=?  and et.codigo=?";
	
	
	/**
	 * Cadena para la Busqueda del cadenaConsultaServicioTarifaFinalStr
	 */
	private static final String cadenaConsultaServicioTarifaFinalIssStr="SELECT " +
			" et.nombre as esquemaTarifario," +
			" ma.nombre as tipoRedondeo," +
			" rs.descripcion as servicio," +
			" ti.fecha_modifica as fechaAsignacion," +
			" tl.nombre as tipoLiquidacion, "+
			" ti.valor_tarifa as valorTarifa," +
			" getnombreusuario(ti.usuario_modifica) as usuarioAsigna, " +
			" getintegridaddominio(ti.actualiza_automatica) as tipoCambio, " +
			" ti.liq_asocios AS liquidarAsocios "+
			" FROM tarifas_iss ti" +
			" INNER JOIN esquemas_tarifarios et on(ti.esquema_tarifario=et.codigo) " +
			" INNER JOIN metodos_ajuste ma on(ma.acronimo=et.metodo_ajuste) " +
			" INNER JOIN referencias_servicio rs on(ti.servicio=rs.servicio) " +
			" INNER JOIN tipos_liquidacion_soat tl on(ti.tipo_liquidacion=tl.codigo)" +
			" WHERE rs.servicio=? AND rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"' and et.codigo=?";
	
	
	/**
	 * Cadena para la Busqueda del cadenaConsultaServicioTarifaFinalStr
	 */
	private static final String cadenaConsultaServicioTarifaFinalSoatStr="SELECT " +
			" et.nombre as esquemaTarifario," +
			" ma.nombre as tipoRedondeo," +
			" rs.descripcion as servicio," +
			" ti.fecha_modifica as fechaAsignacion," +
			" tl.nombre as tipoLiquidacion, "+
			" ti.valor_tarifa as valorTarifa," +
			" getnombreusuario(ti.usuario_modifica) as usuarioAsigna, " +
			" getintegridaddominio(ti.actualiza_automatica) as tipoCambio, " +
			" ti.liq_asocios AS liquidarAsocios "+
			" FROM tarifas_soat ti" +
			" INNER JOIN esquemas_tarifarios et on(ti.esquema_tarifario=et.codigo) " +
			" INNER JOIN metodos_ajuste ma on(ma.acronimo=et.metodo_ajuste) " +
			" INNER JOIN referencias_servicio rs on(ti.servicio=rs.servicio) " +
			" INNER JOIN tipos_liquidacion_soat tl on(ti.tipo_liquidacion=tl.codigo)" +
			" WHERE rs.servicio=? AND rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"' and et.codigo=?";
	
	
	
	
	
	/**
	 * Cadena para la Busqueda del Articulo
	 */
	private static final String cadenaConsultaArticuloStr="SELECT " +
																" et.nombre as esquemaTarifario, " +
																" ma.nombre as tipoRedondeo, " +
																" va.descripcion||' - '||va.descripcionnaturaleza||' - '||va.descripcionff||' - '||va.concentracion||' - '||va.descripcionum as articulo, " +
																" ti.fecha as fechaAsignacion, " +
																" ti.tipo_tarifa as tipoTarifa, "+
																" ti.porcentaje as porcentaje, " +
																" ti.valor_tarifa as valorTarifa, " +
																" ti.usuario as usuarioAsigna, " +
																" ti.actualiz_automatic as tipoCambio "+
															" FROM log_tarifas_inventario ti " +
															" INNER JOIN esquemas_tarifarios et on(ti.esquema_tarifario=et.codigo) " +
															" INNER JOIN metodos_ajuste ma on(ma.acronimo=et.metodo_ajuste) " +
															" INNER JOIN view_articulos va on(ti.articulo=va.codigo) " +
															" WHERE va.codigo=? ";
	
	
	/**
	 * Cadena para la Busqueda del Articulo
	 */
	private static final String cadenaConsultaArticuloTarifaFinalStr=" SELECT " +
																" et.nombre as esquemaTarifario, " +
																" coalesce(ma.nombre,'') as tipoRedondeo, " +
																" coalesce(va.descripcion,'')||' - '||coalesce(va.descripcionnaturaleza,'')||' - '||coalesce(va.descripcionff,'')||' - '||coalesce(va.concentracion,'')||' - '||coalesce(va.descripcionum,'') as articulo, " +
																" coalesce(ti.fecha_modifica||'','') as fechaAsignacion, " +
																" ti.tipo_tarifa as tipoTarifa, "+
																" ti.porcentaje as porcentaje, " +
																" ti.valor_tarifa as valorTarifa, " +
																" coalesce(ti.usuario_modifica||'','') as usuarioAsigna, " +
																" coalesce(ti.actualiz_automatic||'','') as tipoCambio "+
															" FROM tarifas_inventario ti " +
															" INNER JOIN esquemas_tarifarios et on(ti.esquema_tarifario=et.codigo) " +
															" INNER JOIN metodos_ajuste ma on(ma.acronimo=et.metodo_ajuste) " +
															" INNER JOIN view_articulos va on(ti.articulo=va.codigo) " +
															" WHERE va.codigo=? and et.codigo=?";
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param especialidad
	 * @param tipoServicio
	 * @param grupo
	 * @return
	 */
	public static HashMap buscar(Connection con, String codigo, String descripcion, String especialidad, String tipoServicio, String grupo,int codigoEsquemaTarifario) throws IPSException 
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
	
		String cadena=cadenaBuscarStr;
		if(!codigo.equals(""))
		{
			cadena+="AND rs.codigo_propietario='"+codigo+"'";
		}
		if(!descripcion.equals(""))
		{
			cadena+="AND UPPER(rs.descripcion) LIKE UPPER('%"+descripcion+"%')";
		}
		if(!especialidad.equals(""))
		{
			cadena+=" AND UPPER(e.nombre) LIKE UPPER ('%"+especialidad+"%')";
		}
		if(!tipoServicio.equals(""))
		{
			cadena+=" AND UPPER(tp.nombre) LIKE UPPER('%"+tipoServicio+"%')";
		}
		if(!grupo.equals(""))
		{
			cadena+=" AND UPPER(gs.descripcion) LIKE UPPER('%"+grupo+"%')";
		}
		
		try
		{
			logger.info("-->"+cadena);
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int i=0;
			ResultSetDecorator rs=new ResultSetDecorator(busqueda.executeQuery());
			while(rs.next())
			{
				mapa.put("codigo_"+i, rs.getObject("codigo")+"");
				mapa.put("codigoservicio_"+i, rs.getObject("codigoservicio")+"");
				mapa.put("especialidad_"+i, rs.getObject("especialidad")+"");
				mapa.put("tiposervicio_"+i, rs.getObject("tiposervicio")+"");
				mapa.put("grupo_"+i, rs.getObject("grupo")+"");
				mapa.put("descripcion_"+i, rs.getObject("descripcion")+"");
				int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
				
				InfoTarifaVigente infoTarifaVigente=Cargos.obtenerTarifaBaseServicio(con, codigoTipoTarifario, Utilidades.convertirAEntero(rs.getObject("codigoservicio")+""), codigoEsquemaTarifario, ""/*fechaVigencia*/);
				double valorTarifaBase=infoTarifaVigente.getValorTarifa();
				mapa.put("tarifabase_"+i, infoTarifaVigente.isExiste()?valorTarifaBase+"":"");
				i++;
			}
			mapa.put("numRegistros", i+"");
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
		
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param descripcionArticulo
	 * @param clase
	 * @param grupoArticulo
	 * @param subgrupo
	 * @param naturaleza
	 * @return
	 */
	public static HashMap buscarArticulo(Connection con, String codigoArticulo, String descripcionArticulo, String clase, String grupoArticulo, String subgrupo, String naturaleza,int esquemaTarifario) throws IPSException 
	{
	
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaBuscarArticuloStr;
		if(!codigoArticulo.equals(""))
		{
			cadena+="AND va.codigo="+codigoArticulo;
		}
		if(!descripcionArticulo.equals(""))
		{
			cadena+="AND UPPER(va.descripcion) LIKE UPPER('%"+descripcionArticulo+"%')";
		}
		if(!clase.equals(""))
		{
			cadena+=" AND UPPER(va.descripcionclase) LIKE UPPER('%"+clase+"%')";
		}
		if(!grupoArticulo.equals(""))
		{
			cadena+=" AND UPPER(va.descripciongrupo) LIKE UPPER('%"+grupoArticulo+"%')";
		}
		if(!subgrupo.equals(""))
		{
			cadena+=" AND UPPER(va.descripcionsubgrupo) LIKE UPPER('%"+subgrupo+"%')";
		}
		if(!naturaleza.equals(""))
		{
			cadena+=" AND UPPER(na.nombre) LIKE UPPER('%"+naturaleza+"%')";
		}
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int i=0;
			ResultSetDecorator rs=new ResultSetDecorator(busqueda.executeQuery());
			while(rs.next())
			{
				mapa.put("codigoarticulo_"+i, rs.getObject("codigoArticulo")+"");
				mapa.put("descripcionarticulo_"+i, rs.getObject("descripcionArticulo")+"");
				mapa.put("clase_"+i, rs.getObject("clase")+"");
				mapa.put("grupoarticulo_"+i, rs.getObject("grupoArticulo")+"");
				mapa.put("subgrupo_"+i, rs.getObject("subgrupo")+"");
				mapa.put("naturaleza_"+i, rs.getObject("naturaleza")+"");
				int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, esquemaTarifario);
				double valorTarifaBase=Cargos.obtenerTarifaBaseArticulo(con, Utilidades.convertirAEntero(rs.getObject("codigoArticulo")+""), esquemaTarifario, "" /*fechaVigencia*/);
				mapa.put("tarifabase_"+i, valorTarifaBase>=0?valorTarifaBase+"":"");
				i++;
			}
			mapa.put("numRegistros", i+"");
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
		
	}
		
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultaServicio(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaServicioStr;
		
		try
		{
			cadena+=" ORDER BY ts.fecha_modifica ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoServicio").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("tarifarioOficial").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("esquematarifario").toString()));
			logger.info("cadena -->"+cadena+"   -->"+vo.get("codigoServicio")+"  -->"+vo.get("tarifarioOficial")+"  -->"+vo.get("esquematarifario"));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
		
		
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultaArticulo(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaArticuloStr;
		try
		{
			cadena+=" ORDER BY ti.fecha ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoArti").toString()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
		
	}

	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param esServicio
	 * @return
	 */
	public static HashMap<String,Object> consultarEsquemasVigenciasContrato(Connection con, int contrato, boolean esServicio) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		String cadena="";
		if(esServicio)
		{	
			cadena="SELECT codigo,contrato,grupo_servicio as gruposervicio,getnombregruposervicio(grupo_servicio) as nombregruposervicio,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nomesquematarifario,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia, centro_atencion as codcentroatencion, getnomcentroatencion(centro_atencion) as nombrecentroatencion from esq_tar_procedimiento_contrato where contrato=?";
		}	
		else
		{	
			cadena="SELECT codigo,contrato,clase_inventario as claseinventario,getnombreclaseinventario(clase_inventario) as nombreclaseinventario,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nomesquematarifario,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia, centro_atencion as codcentroatencion, getnomcentroatencion(centro_atencion) as nombrecentroatencion from esq_tar_inventarios_contrato where contrato=?";
		}	
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info(" CADENA DE CONSULTA DE ESQUEMA TARIFARIO >>>>>> "+cadena+" CONTRATO -> "+contrato);
			
			ps.setInt(1, contrato);
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}



	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tarifarioOficial
	 * @return
	 */
	public static HashMap consultaServicioTarifaFinal(Connection con, int codigoServicio, int tarifarioOficial,int esquemaTarifario,int convenio,int contrato,int institucion,String fechaVigencia, int grupoServicio) throws IPSException 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= "";
		if(tarifarioOficial==ConstantesBD.codigoTarifarioISS)
			cadena=cadenaConsultaServicioTarifaFinalIssStr;
		else if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
			cadena=cadenaConsultaServicioTarifaFinalSoatStr;

		String cadenaViasIngreso="SELECT via_ingreso as viaingreso,getnombreviaingreso(via_ingreso) as nomviaingreso,tipo_paciente as tipopaciente,getnombretipopaciente(tipo_paciente) as nomtipopaciente from tip_pac_via_ingreso order by via_ingreso";
		
		String cadenaTiposComplejidad="SELECT codigo,descripcion from tipos_complejidad";

		try
		{
			String fechaFinalVigencia="";
			if(grupoServicio>0)
			{
				String cadenaSiguienteFechaVigencia="SELECT fecha_vigencia from esq_tar_procedimiento_contrato where contrato="+contrato+" and grupo_servicio="+grupoServicio+"  and fecha_vigencia > '"+UtilidadFecha.conversionFormatoFechaABD(fechaVigencia)+"'";
				PreparedStatementDecorator psF= new PreparedStatementDecorator(con.prepareStatement(cadenaSiguienteFechaVigencia));
				ResultSetDecorator rsF=new ResultSetDecorator(psF.executeQuery());
				if(rsF.next())
				{
					fechaFinalVigencia=rsF.getObject(1)+"";
				}
			}
			else
			{
				String cadenaSiguienteFechaVigencia="SELECT fecha_vigencia from esq_tar_procedimiento_contrato where contrato="+contrato+" and grupo_servicio is null  and fecha_vigencia > '"+UtilidadFecha.conversionFormatoFechaABD(fechaVigencia)+"'";
				PreparedStatementDecorator psF= new PreparedStatementDecorator(con.prepareStatement(cadenaSiguienteFechaVigencia));
				ResultSetDecorator rsF=new ResultSetDecorator(psF.executeQuery());
				if(rsF.next())
				{
					fechaFinalVigencia=rsF.getObject(1)+"";
				}
			}
			if(UtilidadCadena.noEsVacio(cadena))
			{
				cadena+=" ORDER BY fecha_modifica ";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoServicio);
				ps.setInt(2, esquemaTarifario);
				logger.info("cadena -->"+cadena+"   -->"+codigoServicio+"  -->"+esquemaTarifario);
				int i=0;
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				PreparedStatementDecorator psV= new PreparedStatementDecorator(con.prepareStatement(cadenaViasIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rsV=new ResultSetDecorator(psV.executeQuery());
				
				PreparedStatementDecorator psTC= new PreparedStatementDecorator(con.prepareStatement(cadenaTiposComplejidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rsTC=new ResultSetDecorator(psTC.executeQuery());
				
				String tiposComplejidad=ConstantesBD.codigoNuncaValido+"";
				int contadorTC=0;
				while(rsTC.next())
				{
					tiposComplejidad=tiposComplejidad+ConstantesBD.separadorSplit+rsTC.getObject("codigo");
					mapa.put("codigoTipoComplejidad_"+contadorTC, rsTC.getObject("codigo")+"");
					mapa.put("descripcionTipoComplejidad_"+contadorTC, rsTC.getObject("descripcion")+"");
					contadorTC++;
				}
				if(contadorTC==0)
				{
					mapa.put("codigoTipoComplejidad_"+contadorTC, ConstantesBD.codigoNuncaValido+"");
					mapa.put("descripcionTipoComplejidad_"+contadorTC, "");
					contadorTC++;
				}
				mapa.put("numRegistrosTipoComplejidad", contadorTC+"");
				
				String viasIngreso="";
				int contadorVI=0;
				while(rsV.next())
				{
					if(contadorVI>0)
						viasIngreso=viasIngreso+ConstantesBD.separadorSplit;
					viasIngreso=viasIngreso+rsV.getObject("viaingreso")+"_"+rsV.getObject("tipopaciente")+"";
					mapa.put("viaIngreso_"+contadorVI, rsV.getObject("viaingreso")+"");
					mapa.put("nombreViaIngreso_"+contadorVI, rsV.getObject("nomviaingreso")+"");
					mapa.put("tipoPaciente_"+contadorVI, rsV.getObject("tipopaciente")+"");
					mapa.put("nombreTipoPaciente_"+contadorVI, rsV.getObject("nomtipopaciente")+"");
					contadorVI++;
				}
				mapa.put("numRegistrosViasIngreso", contadorVI+"");
					
				
				String[] vecTC=tiposComplejidad.split(ConstantesBD.separadorSplit);
				String[] vecVI=viasIngreso.split(ConstantesBD.separadorSplit);

				if(rs.next())
				{
					mapa.put("esquematarifario", rs.getObject("esquematarifario")+"");
					mapa.put("tiporedondeo", rs.getObject("tiporedondeo")+"");
					mapa.put("servicio", rs.getObject("servicio")+"");

				}
				else
				{
					mapa.put("esquematarifario", "");
					mapa.put("tiporedondeo", "");
					mapa.put("servicio", "");
				}
				for(int tc=0;tc<vecTC.length;tc++)
				{
					for(int vi=0;vi<vecVI.length;vi++)
					{
						i=0;
						rs.beforeFirst();
						String[] viTipo=vecVI[vi].split("_");
						while(rs.next())
						{
							mapa.put("fechaasignacion_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("fechaAsignacion")+"");
							mapa.put("tipoliquidacion_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("tipoLiquidacion")+"");
							mapa.put("valortarifa_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("valorTarifa")+"");
							mapa.put("usuarioasigna_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("usuarioAsigna")+"");
							mapa.put("tipocambio_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("tipoCambio")+"");
							mapa.put("liquidarasocios_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("liquidarAsocios")+"");
							//@FIXME TARIFAS CENTRO ATENCION
							mapa.put("tarifafinal_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, Cargos.obtenerValorTarifaYExcepcion(con, convenio, contrato, esquemaTarifario, institucion, codigoServicio, true, viTipo[1] , Utilidades.convertirAEntero(viTipo[0]), Utilidades.convertirAEntero(vecTC[tc]), fechaVigencia, ConstantesBD.codigoNuncaValido)+"");
							if(UtilidadTexto.isEmpty(fechaFinalVigencia))
							{
								//@FIXME TARIFAS CENTRO ATENCION
								mapa.put("excepciones_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerExcepcionesTarifasServicio(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1] ,Utilidades.convertirAEntero(vecTC[tc]) , contrato, codigoServicio, institucion, fechaVigencia, ConstantesBD.codigoNuncaValido));
								mapa.put("descuentos_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1], contrato, codigoServicio, institucion, fechaVigencia));
							}
							else
							{
								//@FIXME TARIFAS CENTRO ATENCION
								mapa.put("excepciones_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerExcepcionesTarifasServicio(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1] ,Utilidades.convertirAEntero(vecTC[tc]) , contrato, codigoServicio, institucion, fechaVigencia,fechaFinalVigencia, ConstantesBD.codigoNuncaValido));
								mapa.put("descuentos_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1], contrato, codigoServicio, institucion, fechaVigencia,fechaFinalVigencia));
							}
							i++;
						}
						mapa.put("numRegistros_"+vecTC[tc]+"_"+vecVI[vi], i+"");
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		Utilidades.imprimirMapa(mapa);
		return mapa;
	}



	/**
	 * 
	 * @param con
	 * @param codigoArti
	 * @param esquemaTarifario
	 * @param convenio
	 * @param contrato
	 * @param institucion
	 * @param fechaVigencia
	 * @return
	 */
	public static HashMap consultaArticulosTarifaFinal(Connection con, int codigoArti, int esquemaTarifario, int convenio, int contrato, int institucion, String fechaVigencia,int claseInventario) throws IPSException 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaArticuloTarifaFinalStr;
		
		String cadenaViasIngreso="SELECT via_ingreso as viaingreso,getnombreviaingreso(via_ingreso) as nomviaingreso,tipo_paciente as tipopaciente,getnombretipopaciente(tipo_paciente) as nomtipopaciente from tip_pac_via_ingreso order by via_ingreso";
		
		String cadenaTiposComplejidad="SELECT codigo,descripcion from tipos_complejidad";

		try
		{
			if(UtilidadCadena.noEsVacio(cadena))
			{
				String fechaFinalVigencia="";
				if(claseInventario>0)
				{
					String cadenaSiguienteFechaVigencia="SELECT fecha_vigencia from esq_tar_inventarios_contrato where contrato="+contrato+" and clase_inventario="+claseInventario+"  and fecha_vigencia > '"+UtilidadFecha.conversionFormatoFechaABD(fechaVigencia)+"'";
					PreparedStatementDecorator psF= new PreparedStatementDecorator(con.prepareStatement(cadenaSiguienteFechaVigencia));
					ResultSetDecorator rsF=new ResultSetDecorator(psF.executeQuery());
					if(rsF.next())
					{
						fechaFinalVigencia=rsF.getObject(1)+"";
					}
				}
				else
				{
					String cadenaSiguienteFechaVigencia="SELECT fecha_vigencia from esq_tar_inventarios_contrato where contrato="+contrato+" and clase_inventario is null  and fecha_vigencia > '"+UtilidadFecha.conversionFormatoFechaABD(fechaVigencia)+"'";
					PreparedStatementDecorator psF= new PreparedStatementDecorator(con.prepareStatement(cadenaSiguienteFechaVigencia));
					ResultSetDecorator rsF=new ResultSetDecorator(psF.executeQuery());
					if(rsF.next())
					{
						fechaFinalVigencia=rsF.getObject(1)+"";
					}
				}
				
				
				cadena+=" ORDER BY fecha_modifica ";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoArti);
				ps.setInt(2, esquemaTarifario);
				logger.info("cadena -->"+cadena+"   -->"+codigoArti+"  -->"+esquemaTarifario);
				int i=0;
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				PreparedStatementDecorator psV= new PreparedStatementDecorator(con.prepareStatement(cadenaViasIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rsV=new ResultSetDecorator(psV.executeQuery());
				
				PreparedStatementDecorator psTC= new PreparedStatementDecorator(con.prepareStatement(cadenaTiposComplejidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rsTC=new ResultSetDecorator(psTC.executeQuery());
				
				String tiposComplejidad=ConstantesBD.codigoNuncaValido+"";
				int contadorTC=0;
				while(rsTC.next())
				{
					tiposComplejidad=tiposComplejidad+ConstantesBD.separadorSplit+rsTC.getObject("codigo");
					mapa.put("codigoTipoComplejidad_"+contadorTC, rsTC.getObject("codigo")+"");
					mapa.put("descripcionTipoComplejidad_"+contadorTC, rsTC.getObject("descripcion")+"");
					contadorTC++;
				}
				if(contadorTC==0)
				{
					mapa.put("codigoTipoComplejidad_"+contadorTC, ConstantesBD.codigoNuncaValido+"");
					mapa.put("descripcionTipoComplejidad_"+contadorTC, "");
					contadorTC++;
				}
				mapa.put("numRegistrosTipoComplejidad", contadorTC+"");
				
				String viasIngreso="";
				int contadorVI=0;
				while(rsV.next())
				{
					if(contadorVI>0)
						viasIngreso=viasIngreso+ConstantesBD.separadorSplit;
					viasIngreso=viasIngreso+rsV.getObject("viaingreso")+"_"+rsV.getObject("tipopaciente")+"";
					mapa.put("viaIngreso_"+contadorVI, rsV.getObject("viaingreso")+"");
					mapa.put("nombreViaIngreso_"+contadorVI, rsV.getObject("nomviaingreso")+"");
					mapa.put("tipoPaciente_"+contadorVI, rsV.getObject("tipopaciente")+"");
					mapa.put("nombreTipoPaciente_"+contadorVI, rsV.getObject("nomtipopaciente")+"");
					contadorVI++;
				}
				mapa.put("numRegistrosViasIngreso", contadorVI+"");
					
				
				String[] vecTC=tiposComplejidad.split(ConstantesBD.separadorSplit);
				String[] vecVI=viasIngreso.split(ConstantesBD.separadorSplit);

				if(rs.next())
				{
					mapa.put("esquematarifario", rs.getObject("esquematarifario")+"");
					mapa.put("tiporedondeo", rs.getObject("tiporedondeo")+"");
					mapa.put("articulo", rs.getObject("articulo")+"");

				}
				for(int tc=0;tc<vecTC.length;tc++)
				{
					for(int vi=0;vi<vecVI.length;vi++)
					{
						i=0;
						rs.beforeFirst();
						String[] viTipo=vecVI[vi].split("_");
						while(rs.next())
						{
							mapa.put("fechaasignacion_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("fechaAsignacion")+"");
							mapa.put("tipotarifa_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("tipoTarifa")+"");
							mapa.put("valortarifa_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("valorTarifa")+"");
							mapa.put("usuarioasigna_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("usuarioAsigna")+"");
							mapa.put("tipocambio_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("tipoCambio")+"");
							mapa.put("porcentaje_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, rs.getObject("porcentaje")+"");
							//@FIXME TARIFAS CENTRO ATENCION
							mapa.put("tarifafinal_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, Cargos.obtenerValorTarifaYExcepcion(con, convenio, contrato, esquemaTarifario, institucion, codigoArti, false, viTipo[1] , Utilidades.convertirAEntero(viTipo[0]), Utilidades.convertirAEntero(vecTC[tc]), fechaVigencia, ConstantesBD.codigoNuncaValido)+"");
							if(UtilidadTexto.isEmpty(fechaFinalVigencia))
							{
								//@FIXME TARIFAS CENTRO ATENCION
								mapa.put("excepciones_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerExcepcionesTarifasArticulo(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1] ,Utilidades.convertirAEntero(vecTC[tc]) , contrato, codigoArti, institucion,esquemaTarifario, fechaVigencia, ConstantesBD.codigoNuncaValido));
								mapa.put("descuentos_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerDescuentoComercialXConvenioArticulo(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1], contrato, codigoArti, institucion, fechaVigencia));
							}
							else
							{
								//@FIXME TARIFAS CENTRO ATENCION
								mapa.put("excepciones_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerExcepcionesTarifasArticulo(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1] ,Utilidades.convertirAEntero(vecTC[tc]) , contrato, codigoArti, institucion,esquemaTarifario, fechaVigencia,fechaFinalVigencia, ConstantesBD.codigoNuncaValido));
								mapa.put("descuentos_"+vecTC[tc]+"_"+vecVI[vi]+"_"+i, SqlBaseCargosDao.obtenerDescuentoComercialXConvenioArticulo(con, Utilidades.convertirAEntero(viTipo[0]),viTipo[1], contrato, codigoArti, institucion, fechaVigencia,fechaFinalVigencia));
							}
							i++;
						}
						mapa.put("numRegistros_"+vecTC[tc]+"_"+vecVI[vi], i+"");
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		Utilidades.imprimirMapa(mapa);
		return mapa;
	}

}
