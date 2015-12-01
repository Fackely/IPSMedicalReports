/*
 * @author Jorge Armando Osorio.
 */
package util;

/**
 * Interface para almacenar los diferentes bloqueos de concurrencia que se tienen y sus descroopmes
 * @author artotor
 *
 */
public interface BloqueosConcurrencia 
{
	
	
	/**
	 * Cdena para bloquear una factura determinada
	 */
	public static final String bloqueoFacturaDeterminada = "SELECT * FROM facturas WHERE codigo=? FOR UPDATE";
	/**
	 * Cdena para bloquear un ajuste determinada
	 */
	public static final String bloqueAjusteGeneralDeterminado = "SELECT ae.* from ajustes_empresa ae where ae.codigo=? FOR UPDATE";
	public static final String bloqueAjusteFacturaDeterminado = "SELECT afe.* from ajus_fact_empresa afe where afe.codigo=? FOR UPDATE";
	public static final String bloqueAjusteDetFacturaDeterminado = "SELECT adfe.* from ajus_det_fact_empresa adfe where adfe.codigo=? FOR UPDATE";
	public static final String bloqueAjusteAsocioDetFacturaDeterminado = "SELECT aafe.* from ajus_asocios_fact_empresa aafe where aafe.codigo_ajuste=? FOR UPDATE";
	
	
	/**
	 * Cadena para bloquear una cuenta.
	 */
	public static final String bloqueoCuentaDeterminada = "SELECT * FROM cuentas WHERE id=? FOR UPDATE";
	
	/**
	 * Cadena para bloquear la cama
	 */
	public static final String bloqueoCama = "SELECT * FROM camas1 WHERE codigo=? FOR UPDATE";
	
	/**
	 * Cadea para bloquear los antecedentes transfucionales
	 */
	public static final String bloquearAntecedentesTransfusionales = "SELECT * from antece_transfusionales where codigo_paciente=? for update";
	
	/**
	 * Cadena para bloqueas los articulos por almacen.
	 */
	public static final String bloquearArticulosAlmacen = "SELECT * from articulos_almacen where articulo = ? and almacen=? FOR UPDATE";
	
	/**
	 * Cadena para bloqueas los articulos por almacen Lotes.
	 */
	public static final String bloquearArticulosAlmacenLotes = " SELECT * FROM articulos_almacen aa inner join articulo_almacen_x_lote aal on (aal.almacen=aa.almacen and aal.articulo=aa.articulo) where aal.articulo=? and aal.almacen=? and aal.lote=? and aal.fecha_vencimiento=? for update";

	/**
	 * Cadena para bloqueas las solicitudes.
	 */
	
		public static final String bloquearSolicitud = "SELECT * from solicitudes where  numero_solicitud=? FOR UPDATE";
	
	/**
	 * Cadena para bloquear las admisiones de urgencias
	 */
	public static final String bloquearAdmisionUrgencias = "SELECT * FROM admisiones_urgencias WHERE codigo = ? FOR UPDATE";
	
	
	/**
	 * cadena para bloquear una cuenta de cobro, y las facturas asociadas a ella.
	 */
	public static final String bloqueoCuentaCobroDeterminada = "SELECT * from cuentas_cobro cc inner join facturas f on(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) where cc.numero_cuenta_cobro=? FOR UPDATE";
	
	/**
	 * Cadena para bloquear un recibo de caja
	 */
	public static final String bloqueoReciboCaja="SELECT * from recibos_caja where numero_recibo_caja = ? and institucion =? FOR UPDATE NOWAIT";
	
	/**
	 * Bloqueo de Registro de Transacciones.
	 */
	public static final String bloqueoRegistroTransacciones = "SELECT * from transacciones_x_almacen txa  inner join det_trans_x_almacen dtxa on (txa.codigo=dtxa.transaccion) where txa.codigo=? FOR UPDATE";

	/**
	 * 
	 */
	public static final String bloqueoConsecutivoInventariosAlmacen = "SELECT * from consecutivos_inventarios where tipo_transaccion=? AND institucion=? AND (almacen IS NULL OR almacen=?) FOR UPDATE";
	
	/**
	 * 
	 */
	public static final String bloquearSolicitudTraslado = "SELECT * from solicitud_traslado_almacen where numero_traslado=? FOR UPDATE";
	
	/**
	 * requiere fecha_cierre, usuario, caja-ppal , institucion
	 */
	public static final String bloquearCierresCajaParaTrasladoCaja = "SELECT * from cierres_cajas where fecha_cierre=? and usuario=? and caja_ppal=? and traslado_caja is null and institucion=? FOR UPDATE";
	
	public static final String estaRCBloqueado="select * from recibos_caja where numero_recibo_caja =";
}
