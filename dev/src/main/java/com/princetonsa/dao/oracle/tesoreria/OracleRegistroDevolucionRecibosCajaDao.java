/**
 * 
 */
package com.princetonsa.dao.oracle.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseRegistroDevolucionRecibosCajaDao;
import com.princetonsa.dao.tesoreria.RegistroDevolucionRecibosCajaDao;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoProcesoDevolucionRC;

/**
 * @author axioma
 *
 */
public class OracleRegistroDevolucionRecibosCajaDao implements RegistroDevolucionRecibosCajaDao {

	public HashMap<String, Object> consultarRecibosCaja(HashMap<String, Object> vo) 
	{
		String cadenaConsultaBusquedaRecibosCaja="SELECT " +
		" rc.numero_recibo_caja as numerorc, " +
		" rc.consecutivo_recibo as consecutivorc, " +
		" to_char(rc.fecha,'dd/MM/yyyy')||' '||to_char(rc.hora,'HH:MI') as fechahoraelaboracion ,  " +
		" rc.fecha as fechaoriginal,  " +
		/*//" sum(dpr.valor) - getValoDevolAproRC(rc.numero_recibo_caja,rc.institucion) as valor,  " +
		//no es necesario hacer el sum ya que solo se permite un forma de pago efectivo.
		" dpr.valor - tesoreria.getValoDevolAproRC(rc.numero_recibo_caja,rc.institucion) as valor,  " +*/
		" tesoreria.GETVALORTOTALRC(rc.numero_recibo_caja) - getValoDevolAproRC(rc.numero_recibo_caja,rc.institucion) as valor,  " +
		" dcrc.nombre_beneficiario ||' - '|| coalesce(dcrc.tipo_id_beneficiario||': ','')||dcrc.numero_id_beneficiario as beneficiario,  " +
		" dcrc.tipo_id_beneficiario as idbeneficiario,  " +
		" dcrc.numero_id_beneficiario as numidbeneficiario, " +
		" rc.estado as codigoestado, " +
		" erc.descripcion as descestado,  " +
		" dcrc.doc_soporte as docsoporte, " +
		" dcrc.concepto as concepto," +
		//*******************************************************************
		// cambio anexo 762
		" case " +
		"   when cit.codigo_tipo_ingreso = "+ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+"  then " +
		"    ( " +
		"      select " + 
		"      case when count(apcp.codigo_pk) > 0 then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as is_elem " +
		"      from carterapaciente.aplicac_pagos_cartera_pac apcp " +
		"      where apcp.numero_documento = rc.numero_recibo_caja " +
		"      and apcp.tipo_documento = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" " +
		"    ) " + 
		"  else  " +
		"    '"+ConstantesBD.acronimoSi+"'  " +
		"  end as iseliminarrc " +
		// fin cambio anexo 762
		//*******************************************************************
        " from recibos_caja rc  " +
        " inner join detalle_conceptos_rc dcrc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja and rc.institucion=dcrc.institucion) " +
        " INNER JOIN tesoreria.conceptos_ing_tesoreria cit ON (cit.codigo = dcrc.concepto) " +
        //" inner join detalle_pagos_rc dpr on(dpr.numero_recibo_caja=rc.numero_recibo_caja and rc.institucion=dpr.institucion and dpr.forma_pago='"+ValoresPorDefecto.getFormaPagoEfectivo(Utilidades.convertirAEntero(vo.get("institucion")+""))+"') " +
        //" INNER JOIN tesoreria.formas_pago fg ON (fg.consecutivo = dpr.forma_pago ) " +
        " inner join estados_recibos_caja erc on(rc.estado=erc.codigo)  ";
		
		
		
		return SqlBaseRegistroDevolucionRecibosCajaDao.consultarRecibosCaja(vo,cadenaConsultaBusquedaRecibosCaja);
	}

	/**
	 * 
	 */
	public boolean guardarDevolucion(HashMap<String, Object> vo) 
	{
		return SqlBaseRegistroDevolucionRecibosCajaDao.guardarDevolucion(vo);
	}
	
	/**
	 * se obtiene el valor del concepto de ingreso de tesoreria
	 * @param parametros
	 * @return String 
	 */
	public String getValorConceptoIngTesoreria(HashMap parametros)
	{
		return SqlBaseRegistroDevolucionRecibosCajaDao.getValorConceptoIngTesoreria(parametros);
	}

	@Override
	public ArrayList<DtoVentaTarjetasCliente> consultaFacturas(
			String numeroRecibo) {
		
		return SqlBaseRegistroDevolucionRecibosCajaDao.consultaFacturas(numeroRecibo);
	}

	@Override
	public boolean actualizarEstadoArqueoCierreDevol(Connection con,
			String arqueo, String cierreCaja, ArrayList<Integer> codigosPkDevol)
	{
		return SqlBaseRegistroDevolucionRecibosCajaDao.actualizarEstadoArqueoCierreDevol(con, arqueo, cierreCaja, codigosPkDevol); 
	}
	
	/**
	 * 
	 */
	public DtoProcesoDevolucionRC estaEnProcesoDevolucion(String nroRC,int institucion, String idSesionOPCIONAL, boolean igualSession)
	{
		return SqlBaseRegistroDevolucionRecibosCajaDao.estaEnProcesoDevolucion(nroRC, institucion, idSesionOPCIONAL, igualSession);
	}
	
	/**
	 * 
	 */
	public boolean empezarBloqueoDevolucion(String nroRC,int institucion,String loginUsuario,String idSesion)
	{
		return SqlBaseRegistroDevolucionRecibosCajaDao.empezarBloqueoDevolucion(nroRC, institucion, loginUsuario, idSesion);
	}
	
	/**
	 * 
	 */
	public boolean cancelarBloqueoDevolucion(String nroRC, int institucion, String idSesion)
	{
		return SqlBaseRegistroDevolucionRecibosCajaDao.cancelarBloqueoDevolucion(nroRC, institucion, idSesion);
	}

	
}
