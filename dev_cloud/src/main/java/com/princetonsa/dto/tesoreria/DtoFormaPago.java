package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.ConstantesBD;

/**
 * DTO para el manejo de la información de las formas de pago
 * Se relaciona directamente con la tabla tesoreria.formas_pago
 * @author Juan David Ramírez
 * @version 1.0.2
 * @since 30 Julio 2010
 */
@SuppressWarnings("serial")
public class DtoFormaPago implements Serializable, Cloneable
{
	
	/**
	 * Llave primaria
	 */
	private int consecutivo;
	
	/**
	 * Instituci&oacute;n relacionada con la forma de pago
	 */
	private int institucion;
	
	/**
	 * C&oacute;digo de la forma de pago
	 */
	private String codigo;
	
	/**
	 * Descripci&oacute;n de la forma de pago
	 */
	private String descripcion;
	
	/**
	 * Tipo detalle DTO
	 */
	private DtoTipoDetalleFormaPago tipoDetalle;
	
	/**
	 * Indica si el registro se encuentra activo o no
	 */
	private boolean activo;
	
	/**
	 * Indica si ya fue consignado (Tersorer&iacute;a, m&oacute;dulos viejos)
	 */
	private Character indicativoConsignacion;
	
	/**
	 * Indica si ya fue trasladado (Tersorer&iacute;a, m&oacute;dulos viejos)
	 */
	private Character trasladoCajaRecaudo;
	
	/**
	 * Atributo que indica si la forma de pago
	 * requiere que se realice un traslado caja recaudo.
	 */
	private Character reqTrasladoCajaRecaudo;
	
	/**
	 * Atributo que almacena el listado con los tipos de formas de pago
	 * relacionadas en el cierre del turno de caja.
	 */
	private List<Integer> listadoTiposFormasPago;
	
	/**
	 * Constructor Vacío
	 */
	public DtoFormaPago() {
		this.tipoDetalle=new DtoTipoDetalleFormaPago();
		this.reqTrasladoCajaRecaudo = ConstantesBD.acronimoNoChar;
		this.listadoTiposFormasPago = new ArrayList<Integer>();
	}

	/**
	 * @return Retorna atributo consecutivo
	 */
	public int getConsecutivo()
	{
		return consecutivo;
	}

	/**
	 * @param consecutivo Asigna atributo consecutivo
	 */
	public void setConsecutivo(int consecutivo)
	{
		this.consecutivo = consecutivo;
	}

	/**
	 * @return Retorna atributo institucion
	 */
	public int getInstitucion()
	{
		return institucion;
	}

	/**
	 * @param institucion Asigna atributo institucion
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}

	/**
	 * @return Retorna atributo codigo
	 */
	public String getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna atributo codigo
	 */
	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Retorna atributo descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion Asigna atributo descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * @return Retorna atributo activo
	 */
	public boolean isActivo()
	{
		return activo;
	}

	/**
	 * @param activo Asigna atributo activo
	 */
	public void setActivo(boolean activo)
	{
		this.activo = activo;
	}

	/**
	 * @return Retorna atributo indicativoConsignacion
	 */
	public Character getIndicativoConsignacion()
	{
		return indicativoConsignacion;
	}

	/**
	 * @param indicativoConsignacion Asigna atributo indicativoConsignacion
	 */
	public void setIndicativoConsignacion(Character indicativoConsignacion)
	{
		this.indicativoConsignacion = indicativoConsignacion;
	}

	/**
	 * @return Retorna atributo trasladoCajaRecaudo
	 */
	public Character getTrasladoCajaRecaudo()
	{
		return trasladoCajaRecaudo;
	}

	/**
	 * @param trasladoCajaRecaudo Asigna atributo trasladoCajaRecaudo
	 */
	public void setTrasladoCajaRecaudo(Character trasladoCajaRecaudo)
	{
		this.trasladoCajaRecaudo = trasladoCajaRecaudo;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	public DtoTipoDetalleFormaPago getTipoDetalle() {
		return tipoDetalle;
	}

	public void setTipoDetalle(DtoTipoDetalleFormaPago tipoDetalle) {
		this.tipoDetalle = tipoDetalle;
	}
	
	/**
	 * Obtener el código del tipo de detalle
	 * @return codigo
	 */
	public int getCodigoTipoDetalle()
	{
		return tipoDetalle.getCodigo();
	}
	
	/**
	 * Asignar el código del tipo de detalle
	 * @param codigoTipoDetalle
	 */
	public void setCodigoTipoDetalle(int codigoTipoDetalle)
	{
		this.tipoDetalle.setCodigo(codigoTipoDetalle);
	}
	
	/**
	 * Obtener la descripción del tipo de detalle
	 * @return
	 */
	public String getDescripcionTipoDetalle()
	{
		return tipoDetalle.getDescripcion();
	}
	
	/**
	 * Asignar la descripción del tipo de detalle
	 * @param codigoTipoDetalle
	 */
	public void setDescripcionTipoDetalle(String descripcionTipoDetalle)
	{
		this.tipoDetalle.setDescripcion(descripcionTipoDetalle);
	}

	/**
	 * Obtener la prioridad del tipo de detalle
	 * @return int Prioridad
	 */
	public int getPrioridadTipoDetalle()
	{
		return this.tipoDetalle.getPrioridad();
	}
	
	/**
	 * Asignar la prioridad del tipo de detalle
	 * @param prioridadTipoDetalle prioridad
	 */
	public void setPrioridadTipoDetalle(int prioridadTipoDetalle)
	{
		this.tipoDetalle.setPrioridad(prioridadTipoDetalle);
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo reqTrasladoCajaRecaudo
	 * @return retorna la variable reqTrasladoCajaRecaudo 
	 * @author Yennifer Guerrero 
	 */
	public Character getReqTrasladoCajaRecaudo() {
		return reqTrasladoCajaRecaudo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo reqTrasladoCajaRecaudo
	 * @param valor para el atributo reqTrasladoCajaRecaudo 
	 * @author Yennifer Guerrero
	 */
	public void setReqTrasladoCajaRecaudo(Character reqTrasladoCajaRecaudo) {
		this.reqTrasladoCajaRecaudo = reqTrasladoCajaRecaudo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoTiposFormasPago
	 * @return retorna la variable listadoTiposFormasPago 
	 * @author Yennifer Guerrero 
	 */
	public List<Integer> getListadoTiposFormasPago() {
		return listadoTiposFormasPago;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoTiposFormasPago
	 * @param valor para el atributo listadoTiposFormasPago 
	 * @author Yennifer Guerrero
	 */
	public void setListadoTiposFormasPago(List<Integer> listadoTiposFormasPago) {
		this.listadoTiposFormasPago = listadoTiposFormasPago;
	}
}
