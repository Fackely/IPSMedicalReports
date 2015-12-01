package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

public class DtoFiltroReporteIngresosTarjetasCliente implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena la fecha inicial desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes.
	 */
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha final desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes
	 */
	private Date fechaFinal;
	
	/**
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la regi&oacute;n de cobertura.
	 */
	private long codigoRegion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el primer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoCiudad;
	
	/**
	 * Atributo que almacena el segundo valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoPais;
	
	/**
	 * Atributo que almacena el tercer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoDpto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del centro de 
	 * atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;
	
	/**
	 * Atributo que almacena le consecutivo inicial de la factura.
	 */
	private String consecutivoInicial;
	
	/**
	 * Atributo que almacena el consecutivo final de la factura.
	 */
	private String consecutivoFinal;
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	/**
	 * Atributo que almacena el nombre completo del usuario activo en la sesion.
	 */
	private String nombreUsuario;
	
	/**
	 * Atributo que almacena la clase de venta seleccionada por el usuario.
	 */
	private String claseVenta;
	
	/**
	 * Atributo que almacena el tipo de tarjeta seleccionada por el usuario.
	 */
	private int tipoTarjeta;
	
	/**
	 * Atributo que almacena el código del convenio tarifa.
	 */
	private int codigoConvenio;
	
	/**
	 * Atributo que almacena el login del usuario que realiza la venta.
	 */
	private String usuarioVendedor;
	
	/**
	 * Atributo que almacena la fecha de nacimiento desde la cual se
	 * desea realizar la b&uacute;squeda del comprador.
	 */
	private String rangoEdadInicial;
	
	/**
	 * Atributo que almacena la fecha de nacimiento hasta la cual se
	 * desea realizar la b&uacute;squeda del comprador.
	 */
	private String rangoEdadFinal;
	
	/**
	 * Edad inicial del paciente desde la cual se desea 
	 * realizar la b&uacute;squeda.
	 */
	private Integer edadInicial;
	
	/**
	 * Edad final del paciente desde la cual se desea 
	 * realizar la b&uacute;squeda.
	 */
	private Integer edadFinal;
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexoComprador;
	
	/**
	 * Atributo que almacena la fecha inicial de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaInicialFormateado;
	
	/**
	 * Atributo que almacena la fecha final de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaFinalFormateado;
	
	/**
	 * Atributo que almacena el rango de edades por el cual se realiza la consulta.
	 */
	private String rangoEdadConsultada;
	
	/**
	 * Permite almacenar el valor para ser mostrado en pantalla cuando no se
	 * ha seleccionado ningún sexo.
	 */
	private String ayudanteSexocomprador;
	
	/**
	 * Atributo que almacena la ubicaciòn en donde se encuentra almacenado 
	 * el logo de la instituciòn.
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que almacena la ubicaciòn en la que debe ser colocado el logo
	 * de la instituciòn.
	 */
	private String ubicacionLogo;
	
	
	
	public DtoFiltroReporteIngresosTarjetasCliente() {
		this.usuarioVendedor = "";
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisResidencia
	 * 
	 * @return  Retorna la variable codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPaisResidencia
	 * 
	 * @param  valor para el atributo codigoPaisResidencia 
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ciudadDeptoPais
	 * 
	 * @return  Retorna la variable ciudadDeptoPais
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ciudadDeptoPais
	 * 
	 * @param  valor para el atributo ciudadDeptoPais 
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoRegion
	 * 
	 * @return  Retorna la variable codigoRegion
	 */
	public long getCodigoRegion() {
		return codigoRegion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoRegion
	 * 
	 * @param  valor para el atributo codigoRegion 
	 */
	public void setCodigoRegion(long codigoRegion) {
		this.codigoRegion = codigoRegion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoEmpresaInstitucion
	 * 
	 * @param  valor para el atributo codigoEmpresaInstitucion 
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoCiudad
	 * 
	 * @return  Retorna la variable codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoCiudad
	 * 
	 * @param  valor para el atributo codigoCiudad 
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPais
	 * 
	 * @return  Retorna la variable codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPais
	 * 
	 * @param  valor para el atributo codigoPais 
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoDpto
	 * 
	 * @return  Retorna la variable codigoDpto
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoDpto
	 * 
	 * @param  valor para el atributo codigoDpto 
	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencion
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencion 
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoInicial
	 * 
	 * @return  Retorna la variable consecutivoInicial
	 */
	public String getConsecutivoInicial() {
		return consecutivoInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoInicial
	 * 
	 * @param  valor para el atributo consecutivoInicial 
	 */
	public void setConsecutivoInicial(String consecutivoInicial) {
		this.consecutivoInicial = consecutivoInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoFinal
	 * 
	 * @return  Retorna la variable consecutivoFinal
	 */
	public String getConsecutivoFinal() {
		return consecutivoFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoFinal
	 * 
	 * @param  valor para el atributo consecutivoFinal 
	 */
	public void setConsecutivoFinal(String consecutivoFinal) {
		this.consecutivoFinal = consecutivoFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @return  Retorna la variable nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreUsuario
	 * 
	 * @param  valor para el atributo nombreUsuario 
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo claseVenta
	 * 
	 * @return  Retorna la variable claseVenta
	 */
	public String getClaseVenta() {
		return claseVenta;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo claseVenta
	 * 
	 * @param  valor para el atributo claseVenta 
	 */
	public void setClaseVenta(String claseVenta) {
		this.claseVenta = claseVenta;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo tipoTarjeta
	 * 
	 * @return  Retorna la variable tipoTarjeta
	 */
	public int getTipoTarjeta() {
		return tipoTarjeta;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo tipoTarjeta
	 * 
	 * @param  valor para el atributo tipoTarjeta 
	 */
	public void setTipoTarjeta(int tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoConvenio
	 * 
	 * @return  Retorna la variable codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoConvenio
	 * 
	 * @param  valor para el atributo codigoConvenio 
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo usuarioVendedor
	 * 
	 * @return  Retorna la variable usuarioVendedor
	 */
	public String getUsuarioVendedor() {
		return usuarioVendedor;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo usuarioVendedor
	 * 
	 * @param  valor para el atributo usuarioVendedor 
	 */
	public void setUsuarioVendedor(String usuarioVendedor) {
		this.usuarioVendedor = usuarioVendedor;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadInicial
	 * 
	 * @return  Retorna la variable rangoEdadInicial
	 */
	public String getRangoEdadInicial() {
		return rangoEdadInicial;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rangoEdadInicial
	 * 
	 * @param  valor para el atributo rangoEdadInicial 
	 */
	public void setRangoEdadInicial(String rangoEdadInicial) {
		this.rangoEdadInicial = rangoEdadInicial;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadFinal
	 * 
	 * @return  Retorna la variable rangoEdadFinal
	 */
	public String getRangoEdadFinal() {
		return rangoEdadFinal;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rangoEdadFinal
	 * 
	 * @param  valor para el atributo rangoEdadFinal 
	 */
	public void setRangoEdadFinal(String rangoEdadFinal) {
		this.rangoEdadFinal = rangoEdadFinal;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo edadInicial
	 * 
	 * @return  Retorna la variable edadInicial
	 */
	public Integer getEdadInicial() {
		return edadInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo edadFinal
	 * 
	 * @return  Retorna la variable edadFinal
	 */
	public Integer getEdadFinal() {
		return edadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo edadInicial
	 * 
	 * @param  valor para el atributo edadInicial 
	 */
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
		
		if (edadInicial!=null) {
			rangoEdadInicial = UtilidadFecha.calcularFechaNacimiento(1, edadInicial);
		}
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo edadFinal
	 * 
	 * @param  valor para el atributo edadFinal 
	 */
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
		
		if (edadFinal!=null) {
			rangoEdadFinal = UtilidadFecha.calcularFechaNacimiento(1, edadFinal);
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicialFormateado
	 * 
	 * @return  Retorna la variable fechaInicialFormateado
	 */
	public String getFechaInicialFormateado() {
		fechaInicialFormateado = UtilidadFecha.conversionFormatoFechaAAp(fechaInicial);
		
		return fechaInicialFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicialFormateado
	 * 
	 * @param  valor para el atributo fechaInicialFormateado 
	 */
	public void setFechaInicialFormateado(String fechaInicialFormateado) {
		this.fechaInicialFormateado = fechaInicialFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinalFormateado
	 * 
	 * @return  Retorna la variable fechaFinalFormateado
	 */
	public String getFechaFinalFormateado() {
		fechaFinalFormateado = UtilidadFecha.conversionFormatoFechaAAp(fechaFinal);
		
		return fechaFinalFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinalFormateado
	 * 
	 * @param  valor para el atributo fechaFinalFormateado 
	 */
	public void setFechaFinalFormateado(String fechaFinalFormateado) {
		this.fechaFinalFormateado = fechaFinalFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexoComprador
	 * 
	 * @return  Retorna la variable sexoComprador
	 */
	public String getSexoComprador() {
		return sexoComprador;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo sexoComprador
	 * 
	 * @param  valor para el atributo sexoComprador 
	 */
	public void setSexoComprador(String sexoComprador) {
		this.sexoComprador = sexoComprador;
		
		this.ayudanteSexocomprador = this.sexoComprador;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadConsultada
	 * 
	 * @return  Retorna la variable rangoEdadConsultada
	 */
	public String getRangoEdadConsultada() {
		
		if (!UtilidadTexto.isEmpty(rangoEdadInicial)) {
			this.rangoEdadConsultada = edadInicial + " - "+ edadFinal;
		}else{
			this.rangoEdadConsultada= "Todas";
		}
		
		return rangoEdadConsultada;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rangoEdadConsultada
	 * 
	 * @param  valor para el atributo rangoEdadConsultada 
	 */
	public void setRangoEdadConsultada(String rangoEdadConsultada) {
		this.rangoEdadConsultada = rangoEdadConsultada;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ayudanteSexocomprador
	 * 
	 * @return  Retorna la variable ayudanteSexocomprador
	 */
	public String getAyudanteSexocomprador() {
		
		if (sexoComprador.trim().equals("-1")) {
			
			this.ayudanteSexocomprador = "Femenimo-Masculino";
			
		}else{
			
			try {
				
				if (this.ayudanteSexocomprador.trim().equals(ConstantesBD.codigoSexoMasculino +"")) {
					this.ayudanteSexocomprador= ConstantesIntegridadDominio.acronimoMasculino;
				}
				if (this.ayudanteSexocomprador.trim().equals(ConstantesBD.codigoSexoFemenino+"")) {
					this.ayudanteSexocomprador = ConstantesIntegridadDominio.acronimoFemenino;
				}
				
				if (this.ayudanteSexocomprador.trim().equals(ConstantesBD.codigoSexoAmbos+"")) {
					this.ayudanteSexocomprador = ConstantesIntegridadDominio.acronimoAmbos;
				}
				
				if(ValoresPorDefecto.getIntegridadDominio(sexoComprador)!=null)
				{
					this.ayudanteSexocomprador = ValoresPorDefecto.getIntegridadDominio(ayudanteSexocomprador).toString();
				}
			} catch (Exception e) {
				
					Log4JManager.info(e);
			}
		}
		
		return ayudanteSexocomprador;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ayudanteSexocomprador
	 * 
	 * @param  valor para el atributo ayudanteSexocomprador 
	 */
	public void setAyudanteSexocomprador(String ayudanteSexocomprador) {
		this.ayudanteSexocomprador = ayudanteSexocomprador;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  rutaLogo
	 *
	 * @return retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo rutaLogo
	 * @param rutaLogo es el valor para el atributo rutaLogo 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ubicacionLogo
	 *
	 * @return retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ubicacionLogo
	 * @param ubicacionLogo es el valor para el atributo ubicacionLogo 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

}
