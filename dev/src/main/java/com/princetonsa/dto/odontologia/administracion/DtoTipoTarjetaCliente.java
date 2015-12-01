/**
 * 
 */
package com.princetonsa.dto.odontologia.administracion;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto para el manejo de la información de la venta de tarjeta cliente
 * @author Juan David Ramírez
 * @version 1.0
 * @since 09 Septiembre 2010
 */
@SuppressWarnings("serial")
public class DtoTipoTarjetaCliente implements Serializable
{
	/**
	 * Llave primaria
	 */
	private Long codigoPk;
	
	/**
	 * Código ingresado por el usuario
	 */
	private String codigoTipoTarjeta;
	
	/**
	 * Institución a la que pertenece
	 */
	private int institucion;
	
	/**
	 * Nombre del tipo de la tarjeta
	 */
	private String nombre;
	
	/**
	 * Es aliado 'S' o 'N'
	 */
	private String aliado;
	
	/**
	 * Convenio de la tarjeta
	 */
	private int convenio;
	
	/**
	 * Última fecha modificación registro
	 */
	private Date fechaModifica;
	
	/**
	 * Última modificación registro
	 */
	private String horaModifica;
	
	/**
	 * Último usuario que modificó el registro
	 */
	private Integer usuarioModifica;
	
	/**
	 * Código del servicio para la clase de venta personal
	 */
	private Integer servicioPersonal;
	
	/**
	 * Código del servicio para la clase de venta empresarial
	 */
	private Integer servicioEmpresarial;
	
	/**
	 * Código del servicio para la clase de venta Familiar
	 */
	private Integer servicioFamiliar;
	
	/**
	 * Número de beneficiarios para la clase de venta familiar
	 */
	private int numeroBeneficiariosFamiliar;
	
	/**
	 * Valor actual del serial para la venta 
	 */
	private double consecutivoSerial;

	/**
	 * Obtiene el valor del atributo codigoPk
	 *
	 * @return Retorna atributo codigoPk
	 */
	public Long getCodigoPk()
	{
		return codigoPk;
	}

	/**
	 * Establece el valor del atributo codigoPk
	 *
	 * @param valor para el atributo codigoPk
	 */
	public void setCodigoPk(Long codigoPk)
	{
		this.codigoPk = codigoPk;
	}

	/**
	 * Obtiene el valor del atributo codigoTipoTarjeta
	 *
	 * @return Retorna atributo codigoTipoTarjeta
	 */
	public String getCodigoTipoTarjeta()
	{
		return codigoTipoTarjeta;
	}

	/**
	 * Establece el valor del atributo codigoTipoTarjeta
	 *
	 * @param valor para el atributo codigoTipoTarjeta
	 */
	public void setCodigoTipoTarjeta(String codigoTipoTarjeta)
	{
		this.codigoTipoTarjeta = codigoTipoTarjeta;
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
	 * Obtiene el valor del atributo nombre
	 *
	 * @return Retorna atributo nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Establece el valor del atributo nombre
	 *
	 * @param valor para el atributo nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Obtiene el valor del atributo aliado
	 *
	 * @return Retorna atributo aliado
	 */
	public String getAliado()
	{
		return aliado;
	}

	/**
	 * Establece el valor del atributo aliado
	 *
	 * @param valor para el atributo aliado
	 */
	public void setAliado(String aliado)
	{
		this.aliado = aliado;
	}

	/**
	 * Obtiene el valor del atributo convenio
	 *
	 * @return Retorna atributo convenio
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * Establece el valor del atributo convenio
	 *
	 * @param valor para el atributo convenio
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * Obtiene el valor del atributo fechaModifica
	 *
	 * @return Retorna atributo fechaModifica
	 */
	public Date getFechaModifica()
	{
		return fechaModifica;
	}

	/**
	 * Establece el valor del atributo fechaModifica
	 *
	 * @param valor para el atributo fechaModifica
	 */
	public void setFechaModifica(Date fechaModifica)
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
	 * Obtiene el valor del atributo usuarioModifica
	 *
	 * @return Retorna atributo usuarioModifica
	 */
	public int getUsuarioModifica()
	{
		return usuarioModifica;
	}

	/**
	 * Establece el valor del atributo usuarioModifica
	 *
	 * @param valor para el atributo usuarioModifica
	 */
	public void setUsuarioModifica(int usuarioModifica)
	{
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * Obtiene el valor del atributo servicioPersonal
	 *
	 * @return Retorna atributo servicioPersonal
	 */
	public Integer getServicioPersonal()
	{
		return servicioPersonal;
	}

	/**
	 * Establece el valor del atributo servicioPersonal
	 *
	 * @param valor para el atributo servicioPersonal
	 */
	public void setServicioPersonal(Integer servicioPersonal)
	{
		this.servicioPersonal = servicioPersonal;
	}

	/**
	 * Obtiene el valor del atributo servicioEmpresarial
	 *
	 * @return Retorna atributo servicioEmpresarial
	 */
	public Integer getServicioEmpresarial()
	{
		return servicioEmpresarial;
	}

	/**
	 * Establece el valor del atributo servicioEmpresarial
	 *
	 * @param valor para el atributo servicioEmpresarial
	 */
	public void setServicioEmpresarial(Integer servicioEmpresarial)
	{
		this.servicioEmpresarial = servicioEmpresarial;
	}

	/**
	 * Obtiene el valor del atributo servicioFamiliar
	 *
	 * @return Retorna atributo servicioFamiliar
	 */
	public Integer getServicioFamiliar()
	{
		return servicioFamiliar;
	}

	/**
	 * Establece el valor del atributo servicioFamiliar
	 *
	 * @param valor para el atributo servicioFamiliar
	 */
	public void setServicioFamiliar(Integer servicioFamiliar)
	{
		this.servicioFamiliar = servicioFamiliar;
	}

	/**
	 * Obtiene el valor del atributo numeroBeneficiariosFamiliar
	 *
	 * @return Retorna atributo numeroBeneficiariosFamiliar
	 */
	public int getNumeroBeneficiariosFamiliar()
	{
		return numeroBeneficiariosFamiliar;
	}

	/**
	 * Establece el valor del atributo numeroBeneficiariosFamiliar
	 *
	 * @param valor para el atributo numeroBeneficiariosFamiliar
	 */
	public void setNumeroBeneficiariosFamiliar(int numeroBeneficiariosFamiliar)
	{
		this.numeroBeneficiariosFamiliar = numeroBeneficiariosFamiliar;
	}

	/**
	 * Obtiene el valor del atributo consecutivoSerial
	 *
	 * @return Retorna atributo consecutivoSerial
	 */
	public double getConsecutivoSerial()
	{
		return consecutivoSerial;
	}

	/**
	 * Establece el valor del atributo consecutivoSerial
	 *
	 * @param valor para el atributo consecutivoSerial
	 */
	public void setConsecutivoSerial(double consecutivoSerial)
	{
		this.consecutivoSerial = consecutivoSerial;
	} 

}
