package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Esta clase almacena los valores que son enviados al reporte de órdenes autorizadas a
 * entidades subcontratadas
 * @author Fabián Becerra
 */
public class DtoFiltroConsultaOrdenesAutorizAEntidadesSub implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;

	/** * Razón Social de la Institución */
	private String razonSocial;
	
	/** * Nit de la Institución */
	private String nit;
	
	/** * Fecha de Inicio */
	private Date fechaInicio;
	
	/** * Fecha Fin */
	private Date fechaFin;
	
	/** * Tipo de Consulta */
	private String tipoConsulta;
	
	/** * Estado de las autorizaciones */
	private String estadoAutorizacion;
	
	/** * Número de estados de las autorizaciones seleccionados */
	private int numeroEstados;
	
	/** * Código de la Entidad Subcontratada seleccionada */
	private int codigoEntidadSub;
	
	/** * Código del convenio seleccionado */
	private int codigoConvenio;
	
	/** * Posición donde debe ubicarse el logo */
	private String ubicacionLogo;
	
	/** * Ruta del logo de la institución */
	private String rutaLogo;
	
	/** * Nombre del usuario activo */
	private String nombreUsuario;
	
	
	//----------AGREGADOS PARA PLANO
	private String nombreEntidadSub;
	//----------------------
	
	private String[] estadosAutorizacion;
	private String tituloTipoConsulta;
	
	/**
	 * Constructor de la clase
	 */
	public DtoFiltroConsultaOrdenesAutorizAEntidadesSub() {
		this.reset();
	}
	
	/**
	 * Método que inicializa los atributos de la clase
	 */
	private void reset()
	{
		this.razonSocial		= "";
		this.nit				= "";
		this.fechaFin			= null;
		this.fechaInicio		= null;
		this.tipoConsulta		= "";
		this.estadoAutorizacion	= "";
		this.codigoEntidadSub	= ConstantesBD.codigoNuncaValido;
		this.codigoConvenio		= ConstantesBD.codigoNuncaValido;
		this.numeroEstados	= ConstantesBD.codigoNuncaValido;
	}
	/**
	 * Método que retorna el valor del atributo razonSocial
	 * @return razonSocial
	 * @author Fabián Becerra
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo razonSocial
	 * @param razonSocial valor que se va a almacenar en el atributo razonSocial
	 * @author Fabián Becerra
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	/**
	 * Método que retorna el valor del atributo nit
	 * @return nit
	 * @author Fabián Becerra
	 */
	public String getNit() {
		return nit;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo nit
	 * @param nit valor que se va a almacenar en el atributo nit
	 * @author Fabián Becerra
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}
	/**
	 * Método que retorna el valor del atributo fechaInicio
	 * @return fechaInicio
	 * @author Fabián Becerra
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo fechaInicio
	 * @param fechaInicio valor que se va a almacenar en el atributo fechaInicio
	 * @author Fabián Becerra
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * Método que retorna el valor del atributo fechaFin
	 * @return fechaFin
	 * @author Fabián Becerra
	 */
	public Date getFechaFin() {
		return fechaFin;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo fechaFin
	 * @param fechaFin valor que se va a almacenar en el atributo fechaFin
	 * @author Fabián Becerra
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	/**
	 * Método que retorna el valor del atributo tipoConsulta
	 * @return tipoConsulta
	 * @author Fabián Becerra
	 */
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo tipoConsulta
	 * @param tipoConsulta valor que se va a almacenar en el atributo tipoConsulta
	 * @author Fabián Becerra
	 */
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	/**
	 * Método que retorna el valor del atributo estadoAutorizacion
	 * @return estadoAutorizacion
	 * @author Fabián Becerra
	 */
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo estadoAutorizacion
	 * @param estadoAutorizacion valor que se va a almacenar en el atributo estadoAutorizacion
	 * @author Fabián Becerra
	 */
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	/**
	 * Método que retorna el valor del atributo codigoEntidadSub
	 * @return codigoEntidadSub
	 * @author Fabián Becerra
	 */
	public int getCodigoEntidadSub() {
		return codigoEntidadSub;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo codigoEntidadSub
	 * @param codigoEntidadSub valor que se va a almacenar en el atributo codigoEntidadSub
	 * @author Fabián Becerra
	 */
	public void setCodigoEntidadSub(int codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}
	/**
	 * Método que retorna el valor del atributo codigoConvenio
	 * @return codigoConvenio
	 * @author Fabián Becerra
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo codigoConvenio
	 * @param codigoConvenio valor que se va a almacenar en el atributo codigoConvenio
	 * @author Fabián Becerra
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * Método que retorna el valor del atributo ubicacionLogo
	 * @return ubicacionLogo
	 * @author Fabián Becerra
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo ubicacionLogo
	 * @param ubicacionLogo valor que se va a almacenar en el atributo ubicacionLogo
	 * @author Fabián Becerra
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * Método que retorna el valor del atributo rutaLogo
	 * @return rutaLogo
	 * @author Fabián Becerra
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo rutaLogo
	 * @param rutaLogo valor que se va a almacenar en el atributo rutaLogo
	 * @author Fabián Becerra
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * Método que retorna el valor del atributo nombreUsuario
	 * @return nombreUsuario
	 * @author Fabián Becerra
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nombreUsuario
	 * @param nombreUsuario valor que se va a almacenar en el atributo nombreUsuario
	 * @author Fabián Becerra
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Método que retorna el valor del atributo numeroEstados
	 * @return numeroEstados
	 * @author Fabián Becerra
	 */
	public int getNumeroEstados() {
		return numeroEstados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo numeroEstados
	 * @param numeroEstados valor que se va a almacenar en el atributo numeroEstados
	 * @author Fabián Becerra
	 */
	public void setNumeroEstados(int numeroEstados) {
		this.numeroEstados = numeroEstados;
	}

	public void setNombreEntidadSub(String nombreEntidadSub) {
		this.nombreEntidadSub = nombreEntidadSub;
	}

	public String getNombreEntidadSub() {
		return nombreEntidadSub;
	}

	public void setEstadosAutorizacion(String[] estadosAutorizacion) {
		this.estadosAutorizacion = estadosAutorizacion;
	}

	public String[] getEstadosAutorizacion() {
		return estadosAutorizacion;
	}

	public void setTituloTipoConsulta(String tituloTipoConsulta) {
		this.tituloTipoConsulta = tituloTipoConsulta;
	}

	public String getTituloTipoConsulta() {
		return tituloTipoConsulta;
	}



	
	
	
	
}
