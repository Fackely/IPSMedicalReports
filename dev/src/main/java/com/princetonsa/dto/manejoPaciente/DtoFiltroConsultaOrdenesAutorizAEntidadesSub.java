package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Esta clase almacena los valores que son enviados al reporte de �rdenes autorizadas a
 * entidades subcontratadas
 * @author Fabi�n Becerra
 */
public class DtoFiltroConsultaOrdenesAutorizAEntidadesSub implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;

	/** * Raz�n Social de la Instituci�n */
	private String razonSocial;
	
	/** * Nit de la Instituci�n */
	private String nit;
	
	/** * Fecha de Inicio */
	private Date fechaInicio;
	
	/** * Fecha Fin */
	private Date fechaFin;
	
	/** * Tipo de Consulta */
	private String tipoConsulta;
	
	/** * Estado de las autorizaciones */
	private String estadoAutorizacion;
	
	/** * N�mero de estados de las autorizaciones seleccionados */
	private int numeroEstados;
	
	/** * C�digo de la Entidad Subcontratada seleccionada */
	private int codigoEntidadSub;
	
	/** * C�digo del convenio seleccionado */
	private int codigoConvenio;
	
	/** * Posici�n donde debe ubicarse el logo */
	private String ubicacionLogo;
	
	/** * Ruta del logo de la instituci�n */
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
	 * M�todo que inicializa los atributos de la clase
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
	 * M�todo que retorna el valor del atributo razonSocial
	 * @return razonSocial
	 * @author Fabi�n Becerra
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo razonSocial
	 * @param razonSocial valor que se va a almacenar en el atributo razonSocial
	 * @author Fabi�n Becerra
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	/**
	 * M�todo que retorna el valor del atributo nit
	 * @return nit
	 * @author Fabi�n Becerra
	 */
	public String getNit() {
		return nit;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo nit
	 * @param nit valor que se va a almacenar en el atributo nit
	 * @author Fabi�n Becerra
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}
	/**
	 * M�todo que retorna el valor del atributo fechaInicio
	 * @return fechaInicio
	 * @author Fabi�n Becerra
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo fechaInicio
	 * @param fechaInicio valor que se va a almacenar en el atributo fechaInicio
	 * @author Fabi�n Becerra
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * M�todo que retorna el valor del atributo fechaFin
	 * @return fechaFin
	 * @author Fabi�n Becerra
	 */
	public Date getFechaFin() {
		return fechaFin;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo fechaFin
	 * @param fechaFin valor que se va a almacenar en el atributo fechaFin
	 * @author Fabi�n Becerra
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	/**
	 * M�todo que retorna el valor del atributo tipoConsulta
	 * @return tipoConsulta
	 * @author Fabi�n Becerra
	 */
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo tipoConsulta
	 * @param tipoConsulta valor que se va a almacenar en el atributo tipoConsulta
	 * @author Fabi�n Becerra
	 */
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	/**
	 * M�todo que retorna el valor del atributo estadoAutorizacion
	 * @return estadoAutorizacion
	 * @author Fabi�n Becerra
	 */
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo estadoAutorizacion
	 * @param estadoAutorizacion valor que se va a almacenar en el atributo estadoAutorizacion
	 * @author Fabi�n Becerra
	 */
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	/**
	 * M�todo que retorna el valor del atributo codigoEntidadSub
	 * @return codigoEntidadSub
	 * @author Fabi�n Becerra
	 */
	public int getCodigoEntidadSub() {
		return codigoEntidadSub;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoEntidadSub
	 * @param codigoEntidadSub valor que se va a almacenar en el atributo codigoEntidadSub
	 * @author Fabi�n Becerra
	 */
	public void setCodigoEntidadSub(int codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}
	/**
	 * M�todo que retorna el valor del atributo codigoConvenio
	 * @return codigoConvenio
	 * @author Fabi�n Becerra
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoConvenio
	 * @param codigoConvenio valor que se va a almacenar en el atributo codigoConvenio
	 * @author Fabi�n Becerra
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * M�todo que retorna el valor del atributo ubicacionLogo
	 * @return ubicacionLogo
	 * @author Fabi�n Becerra
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo ubicacionLogo
	 * @param ubicacionLogo valor que se va a almacenar en el atributo ubicacionLogo
	 * @author Fabi�n Becerra
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * M�todo que retorna el valor del atributo rutaLogo
	 * @return rutaLogo
	 * @author Fabi�n Becerra
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo rutaLogo
	 * @param rutaLogo valor que se va a almacenar en el atributo rutaLogo
	 * @author Fabi�n Becerra
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * M�todo que retorna el valor del atributo nombreUsuario
	 * @return nombreUsuario
	 * @author Fabi�n Becerra
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreUsuario
	 * @param nombreUsuario valor que se va a almacenar en el atributo nombreUsuario
	 * @author Fabi�n Becerra
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * M�todo que retorna el valor del atributo numeroEstados
	 * @return numeroEstados
	 * @author Fabi�n Becerra
	 */
	public int getNumeroEstados() {
		return numeroEstados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo numeroEstados
	 * @param numeroEstados valor que se va a almacenar en el atributo numeroEstados
	 * @author Fabi�n Becerra
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
