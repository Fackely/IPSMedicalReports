/*
 * 
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseConsultaAjustesEmpresaDao
{

	/*
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseConsultaAjustesEmpresaDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsultaGeneralAjustes="SELECT DISTINCT " +
																	" ae.codigo as codigo_ajuste, " +
																	" ae.consecutivo_ajuste as consecutivo_ajuste, " +
																	" ae.castigo_cartera as castigo_cartera, " +
																	" ae.tipo_ajuste as tipo_ajuste, " +
																	" getNombreTipoAjuste(ae.tipo_ajuste) as nombre_tipo_ajuste, " +
																	" to_char(ae.fecha_ajuste,'yyyy-mm-dd') as fecha_ajuste, " +
																	" ae.cuenta_cobro as cuenta_cobro, " +
																	" case when cuenta_cobro is null then f.consecutivo_factura||'' else '' end as consecutivo_factura ," +
																	" case when cuenta_cobro is null then getnombreconvenio(f.convenio) else getnombreconvenio(cc.convenio) end as nombre_convenio, " +
																	" ae.concepto_ajuste as concepto_ajuste, " +
																	" getNombreConceptoAjuste(ae.concepto_ajuste) as nombre_concepto_ajuste, " +
																	" ae.metodo_ajuste as metodo_ajuste, " +
																	" getNombreMetodoAjuste(ae.metodo_ajuste) as nombre_metodo_ajuste, " +
																	" ae.valor_ajuste as valor_ajuste, " +
																	" ae.observaciones as observaciones, " +
																	" to_char(ae.fecha_elaboracion,'yyyy-mm-dd') as fecha_elaboracion, " +
																	" ae.hora_elaboracion as hora_elaboracion, " +
																	" ae.usuario as usuario_elaboracion, " +
																	" ae.estado as codigo_estado, " +
																	" getdescripcionestadocartera(ae.estado) as nombre_estado, " +
																	" getDescempresainstitucion(empresa_institucion) as nombreempresa,"+
																	" case when ae.ajuste_reversado ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'R' else '' end as reversado,  " +
																	" case when ae.cod_ajuste_reversado is null then '' else ae.cod_ajuste_reversado||'' end as codajustereversado " +
													" from ajustes_empresa ae " +
													" left outer join ajus_fact_empresa afe on (afe.codigo=ae.codigo) " +
													" left outer join cuentas_cobro cc on (cc.numero_cuenta_cobro=ae.cuenta_cobro and cc.institucion=ae.institucion)  " +
													" left outer join facturas f on (f.codigo=afe.factura) " +
													" where ae.institucion=2 ";
	
	/**
	 * 
	 */
	private static String cadenaConsultaDetalleAjustes=" select " +
																"afe.factura as factura," +
																"afe.metodo_ajuste as metodo_ajuste," +
																"getnombremetodoajuste(afe.metodo_ajuste) as nombre_metodo_ajuste," +
																"getNombreConceptoAjuste(afe.concepto_ajuste) as concepto_ajuste," +
																"afe.valor_ajuste as valor_ajuste," +
																"f.consecutivo_factura as consecutivo_factura," +
																"f.tipo_factura_sistema as tipo_factura_sistema," +
																"f.convenio as convenio," +
																"getnombreconvenio(f.convenio) as nombre_convenio," +
																"f.centro_aten as codigocentroatencion," +
																"getDescempresainstitucion(empresa_institucion) as nombreempresa," +
																"getnomcentroatencion(f.centro_aten) as nombrecentroatencion " +
														" FROM ajus_fact_empresa afe " +
														" INNER JOIN facturas f on (f.codigo=afe.factura) " +
														" WHERE afe.codigo=? " + 
														" AND afe.valor_ajuste > 0 "+
														" ORDER BY f.consecutivo_factura ";	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static ResultSetDecorator busquedaGeneralAjustes(Connection con, HashMap vo)
	{

	    String cadenaWhere="";
	    if(Utilidades.convertirAEntero(vo.get("codTipoAjuste")+"")!=ConstantesBD.codigoNuncaValido)
	    {
	        if(Utilidades.convertirAEntero(vo.get("codTipoAjuste")+"")==ConstantesBD.codigoConceptosCarteraCredito)
	        {
	        	cadenaWhere+=" AND ae.tipo_ajuste in ("+ConstantesBD.codigoAjusteCreditoCuentaCobro+","+ConstantesBD.codigoAjusteCreditoFactura+")";
	        }
	        else if(Utilidades.convertirAEntero(vo.get("codTipoAjuste")+"")==ConstantesBD.codigoConceptosCarteraDebito)
	        {
	        	cadenaWhere+=" AND ae.tipo_ajuste in ("+ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoAjusteDebitoFactura+")";
	        }
	    }
	    
	    if(Utilidades.convertirAEntero(vo.get("estado")+"")!=ConstantesBD.codigoNuncaValido)
	    {
	    	cadenaWhere+=" AND ae.estado="+vo.get("estado");
	    }
	    if(Utilidades.convertirAEntero(vo.get("codTipoAjuste")+"")!=ConstantesBD.codigoNuncaValido&&!UtilidadTexto.isEmpty(vo.get("ajusteInicial")+"")&&!UtilidadTexto.isEmpty(vo.get("ajusteFinal")+""))
	    {
	    	cadenaWhere+=" AND ae.consecutivo_ajuste BETWEEN '"+vo.get("ajusteInicial")+"' AND '"+vo.get("ajusteFinal")+"'";
	    }
	    if(!UtilidadTexto.isEmpty(vo.get("fechaInicial")+"")&&!UtilidadTexto.isEmpty(vo.get("fechaFinal")+""))
	    {
	    	cadenaWhere+=" AND to_char(ae.fecha_elaboracion,'yyyy-mm-dd')  BETWEEN '"+vo.get("fechaInicial")+"' AND '"+vo.get("fechaFinal")+"'";
	    }
	    if(!(vo.get("concepto")+"").trim().equals("")&&Utilidades.convertirAEntero(vo.get("concepto")+"")!=ConstantesBD.codigoNuncaValido)
	    {
	    	cadenaWhere+=" AND ae.concepto_ajuste='"+vo.get("concepto")+"'";
	    }
	    if(Utilidades.convertirAEntero(vo.get("factura")+"")!=ConstantesBD.codigoNuncaValido)
	    {
	    	cadenaWhere+=" AND ae.cuenta_cobro is null and  f.consecutivo_factura ='"+vo.get("factura")+"'";
	    }
	    if(Utilidades.convertirAEntero(vo.get("convenio")+"")!=ConstantesBD.codigoNuncaValido)
	    {
	    	cadenaWhere+=" AND (ae.cuenta_cobro is not null and cc.convenio='"+vo.get("convenio")+"' OR (ae.cuenta_cobro is null and  f.convenio ='"+vo.get("convenio")+"')) ";
	    }
	    cadenaWhere+= " AND ae.valor_ajuste> 0 ";
	    
	    cadenaWhere+=" ORDER BY ae.tipo_ajuste,ae.consecutivo_ajuste ";
		
	    String cadena=cadenaConsultaGeneralAjustes+cadenaWhere;
	    //logger.info(cadena);
	    
	    try
	    {
	    	PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
	    	return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	    	logger.error("ERROR EN busquedaGeneralAjustes \n"+e.getMessage());
	    }
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static ResultSetDecorator consltarDetalleAjuste(Connection con, double codigoAjuste)
	{
		 try
		    {
		    	PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleAjustes));
		    	ps.setDouble(1, codigoAjuste);
		    	return new ResultSetDecorator(ps.executeQuery());
		    }
		    catch(SQLException e)
		    {
		    	logger.error("ERROR EN consltarDetalleAjuste \n"+e.getMessage());
		    }
			return null;
	}
}