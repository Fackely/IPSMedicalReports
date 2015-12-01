package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;


import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;

@SuppressWarnings("serial")
public class DtoEmisionTarjetaCliente  implements Serializable , Cloneable {

	private double codigo;
	
	private String fechaModifica;
	private String horaModifica;
	private InfoDatosStr tipoTarjeta;
	private BigDecimal serialInicial;
	private BigDecimal serialFinal;
	private String usuarioModifica;
	private int cantidad;
	private int institucion;
	
	public DtoEmisionTarjetaCliente (){
		this.reset();
	}
	
	/**
	 * Limpiar el DTO
	 */
	public void reset(){
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.tipoTarjeta = new InfoDatosStr();
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
		this.serialInicial =  BigDecimal.ZERO;
		this.serialFinal = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.cantidad=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}
	
	/**
	 * Obtener el nombre del tipo de tarjeta cliente
	 */
	public String getNombreConvenio(){
		return this.getTipoTarjeta().getDescripcion();
	}

	/**
	 * Obtiene el valor del atributo codigo
	 *
	 * @return Retorna atributo codigo
	 */
	public double getCodigo()
	{
		return codigo;
	}

	/**
	 * Establece el valor del atributo codigo
	 *
	 * @param valor para el atributo codigo
	 */
	public void setCodigo(double codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Obtiene el valor del atributo fechaModifica
	 *
	 * @return Retorna atributo fechaModifica
	 */
	public String getFechaModifica()
	{
		return fechaModifica;
	}

	/**
	 * Establece el valor del atributo fechaModifica
	 *
	 * @param valor para el atributo fechaModifica
	 */
	public void setFechaModifica(String fechaModifica)
	{
		this.fechaModifica = fechaModifica;
	}

	/**
	 * Obtiene el valor del atributo horaModifica
	 *
	 * @return Retorna atributo horaModifica
	 */
	public String getHoraModifica()
	{
		return horaModifica;
	}

	/**
	 * Establece el valor del atributo horaModifica
	 *
	 * @param valor para el atributo horaModifica
	 */
	public void setHoraModifica(String horaModifica)
	{
		this.horaModifica = horaModifica;
	}

	/**
	 * Obtiene el valor del atributo tipoTarjeta
	 *
	 * @return Retorna atributo tipoTarjeta
	 */
	public InfoDatosStr getTipoTarjeta()
	{
		return tipoTarjeta;
	}

	/**
	 * Establece el valor del atributo tipoTarjeta
	 *
	 * @param valor para el atributo tipoTarjeta
	 */
	public void setTipoTarjeta(InfoDatosStr tipoTarjeta)
	{
		this.tipoTarjeta = tipoTarjeta;
	}

	/**
	 * Obtiene el valor del atributo serialInicial
	 *
	 * @return Retorna atributo serialInicial
	 */
	public BigDecimal getSerialInicial()
	{
		return serialInicial;
	}

	/**
	 * Establece el valor del atributo serialInicial
	 *
	 * @param valor para el atributo serialInicial
	 */
	public void setSerialInicial(BigDecimal serialInicial)
	{
		this.serialInicial = serialInicial;
	}

	/**
	 * Obtiene el valor del atributo serialFinal
	 *
	 * @return Retorna atributo serialFinal
	 */
	public BigDecimal getSerialFinal()
	{
		return serialFinal;
	}

	/**
	 * Establece el valor del atributo serialFinal
	 *
	 * @param valor para el atributo serialFinal
	 */
	public void setSerialFinal(BigDecimal serialFinal)
	{
		this.serialFinal = serialFinal;
	}

	/**
	 * Obtiene el valor del atributo usuarioModifica
	 *
	 * @return Retorna atributo usuarioModifica
	 */
	public String getUsuarioModifica()
	{
		return usuarioModifica;
	}

	/**
	 * Establece el valor del atributo usuarioModifica
	 *
	 * @param valor para el atributo usuarioModifica
	 */
	public void setUsuarioModifica(String usuarioModifica)
	{
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * Obtiene el valor del atributo cantidad
	 *
	 * @return Retorna atributo cantidad
	 */
	public int getCantidad()
	{
		return cantidad;
	}

	/**
	 * Establece el valor del atributo cantidad
	 *
	 * @param valor para el atributo cantidad
	 */
	public void setCantidad(int cantidad)
	{
		this.cantidad = cantidad;
	}

	/**
	 * Obtiene el valor del atributo institucion
	 *
	 * @return Retorna atributo institucion
	 */
	public int getInstitucion()
	{
		return institucion;
	}

	/**
	 * Establece el valor del atributo institucion
	 *
	 * @param valor para el atributo institucion
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}
	
	/**
	 * Establece el valor del atributo serialInicial
	 *
	 * @param valor para el atributo serialInicial
	 */
	public void setSerialInicialLong(Long serialInicial)
	{
		this.serialInicial = new BigDecimal(serialInicial);
	}

	/**
	 * Establece el valor del atributo serialFinal
	 *
	 * @param valor para el atributo serialFinal
	 */
	public void setSerialFinalLong(Long serialFinal)
	{
		this.serialFinal = new BigDecimal(serialFinal);
	}

}
