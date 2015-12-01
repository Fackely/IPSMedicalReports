package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoServicios;

public class DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas() {
		// TODO Auto-generated constructor stub
    	this.codigoEmpresaInstitucion = 0;
    	this.codigoRegionSeleccionada = 0;
    	this.loginUsuario = "";
    	this.loginProfesional= "";
    	
	}
    
    /**
	 * Atributo que almacena el c�digo de
	 * un pa�s.
	 */
	private String codigoPaisSeleccionado;
	
	/**
	 * Atributo que almacena el c�digo de
	 * una regi�n.
	 */
	private long codigoRegionSeleccionada;
	private Date fechaInicial;
	private Date fechaFinal;
	private String fechaInicialF;
	private String fechaFinalF;
	
	/**
	 * Atributo que almacena el c�digo de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el c�digo de
	 * un centro de atenci�n.
	 */
	private int consecutivoCentroAtencionSeleccionado;
	
	/**
	 * Atributo que almacena el c�digo de un pa�s de residencia.
	 */
	private String codigoPaisResidencia;
	
	/**
	 * Atributo que almacena el c�digo que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
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
	 * Atributo que almacena los c�digos de
	 * los tipos de cita.
	 */
	private String[] tiposCita;
	
	/**
	 * Atributo que almacena el c�digo de
	 * la especialidad odontol�gica.
	 */
	private int codigoEspecialidad;
	
	/**
	 * Atributo que almacena el nombre de la especialidad.
	 */
	private String nombreEspecialidad;
	
	/**
	 * Atributo que almacena el login del profesional 
	 */
	private String loginProfesional;
	
	/**
	 * Atributo donde se almacenan los login de los profesionales que se encuentran en el jsp
	 */
	private ArrayList<String> loginProfesionales;
	
	/**
	 * Atributo que almacena el login de
	 * usuario 
	 */
	private String loginUsuario;
	
	/**
	 * Atributo donde se almacenan los Servicios odontol�gicos 
	 */
	private ArrayList<DtoServicios> servicios;
	
	/**
	 * Atributo donde se almacena el codigo de la unidad agenda 
	 */
	private int codigoUnidadAgenda;
	
	/**
	 * Atributo que almacena el nombre del usuario activo
	 */
	private String nombreUsuario;
	
	 /**
	 * Atributo que indica si la lista de tipos de 
	 * solicitud esta vacia o no.
	 */
	private String indicaCadenaVacia;
	
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String institucionMultiempresa;
	
	/**
	 * Atributo que almacena el codigo de la institucion
	 */
	private Integer codigoInstitucion;
	
	/**
	 * Atributo que almacena la ruta del logo de la instituci�n.
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que indica la posici�n en donde 
	 * debe ser mostrado el logo.
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena la raz�n social
	 * de la instituci�n.
	 */
	private String razonSocial;
	
	/**
	 * Atributo que permite almacenar el codigo de los servicios
	 */
	private ArrayList<Integer> codigosServicios;
	
	/**
	 * Atributo donde se almacenan los login de los usuarios que se encuentran en el jsp
	 */
	private ArrayList<String> loginUsuarios;
   
	 /**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo indicaCadenaVacia
	 * 
	 * @param  valor para el atributo indicaCadenaVacia 
	 */
    public void setIndicaCadenaVacia(String indicaCadenaVacia) {
		this.indicaCadenaVacia = indicaCadenaVacia;
	}

    /**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo indicaCadenaVacia
	 * 
	 * @return  Retorna la variable indicaCadenaVacia
	 */
	public String getIndicaCadenaVacia() {
		return indicaCadenaVacia;
	}
	
	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoPaisSeleccionado
	 * 
	 * @param  valor para el atributo codigoPaisSeleccionado 
	 */
    public void setCodigoPaisSeleccionado(String codigoPaisSeleccionado) {
		this.codigoPaisSeleccionado = codigoPaisSeleccionado;
	}

    /**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisSeleccionado
	 * 
	 * @return  Retorna la variable codigoPaisSeleccionado
	 */
	public String getCodigoPaisSeleccionado() {
		return codigoPaisSeleccionado;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoEmpresaInstitucion
	 * 
	 * @param  valor para el atributo codigoEmpresaInstitucion 
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencionSeleccionado 
	 */
	public void setConsecutivoCentroAtencionSeleccionado(
			int consecutivoCentroAtencionSeleccionado) {
		this.consecutivoCentroAtencionSeleccionado = consecutivoCentroAtencionSeleccionado;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencionSeleccionado
	 */
	public int getConsecutivoCentroAtencionSeleccionado() {
		return consecutivoCentroAtencionSeleccionado;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoRegionSeleccionada
	 * 
	 * @param  valor para el atributo codigoRegionSeleccionada 
	 */
	public void setCodigoRegionSeleccionada(long codigoRegionSeleccionada) {
		this.codigoRegionSeleccionada = codigoRegionSeleccionada;
	}
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoRegionSeleccionada
	 * 
	 * @return  Retorna la variable codigoRegionSeleccionada
	 */
	public long getCodigoRegionSeleccionada() {
		return codigoRegionSeleccionada;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoPaisResidencia
	 * 
	 * @param  valor para el atributo codigoPaisResidencia 
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisResidencia
	 * 
	 * @return  Retorna la variable codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo ciudadDeptoPais
	 * 
	 * @param  valor para el atributo ciudadDeptoPais 
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo ciudadDeptoPais
	 * 
	 * @return  Retorna la variable ciudadDeptoPais
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoCiudad
	 * 
	 * @param  valor para el atributo codigoCiudad 
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoCiudad
	 * 
	 * @return  Retorna la variable codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoPais
	 * 
	 * @param  valor para el atributo codigoPais 
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPais
	 * 
	 * @return  Retorna la variable codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoDpto
	 * 
	 * @param  valor para el atributo codigoDpto 
	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoDpto
	 * 
	 * @return  Retorna la variable codigoDpto
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo tiposCita
	 * 
	 * @param  valor para el atributo tiposCita 
	 */
	public void setTiposCita(String[] tiposCita) {
		this.tiposCita = tiposCita;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo tiposCita
	 * 
	 * @return  Retorna la variable tiposCita
	 */
	public String[] getTiposCita() {
		return tiposCita;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoEspecialidad
	 * 
	 * @param  valor para el atributo codigoEspecialidad 
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @return  Retorna la variable codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo nombreEspecialidad
	 * 
	 * @param  valor para el atributo nombreEspecialidad 
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo loginProfesional
	 * 
	 * @param  valor para el atributo loginProfesional 
	 */
	public void setLoginProfesional(String loginProfesional) {
		this.loginProfesional = loginProfesional;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo loginProfesional
	 * 
	 * @return  Retorna la variable loginProfesional
	 */
	public String getLoginProfesional() {
		return loginProfesional;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo loginUsuario
	 * 
	 * @param  valor para el atributo loginUsuario 
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo loginUsuario
	 * 
	 * @return  Retorna la variable loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo servicios
	 * 
	 * @param  valor para el atributo servicios 
	 */
	public void setServicios(ArrayList<DtoServicios> servicios) {
		this.servicios = servicios;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo servicios
	 * 
	 * @return  Retorna la variable servicios
	 */
	public ArrayList<DtoServicios> getServicios() {
		return servicios;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoUnidadAgenda
	 * 
	 * @param  valor para el atributo codigoUnidadAgenda 
	 */
	public void setCodigoUnidadAgenda(int codigoUnidadAgenda) {
		this.codigoUnidadAgenda = codigoUnidadAgenda;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoUnidadAgenda
	 * 
	 * @return  Retorna la variable codigoUnidadAgenda
	 */
	public int getCodigoUnidadAgenda() {
		return codigoUnidadAgenda;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo nombreUsuario
	 * 
	 * @param  valor para el atributo nombreUsuario 
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @return  Retorna la variable nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}



	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo institucionMultiempresa
	 * 
	 * @param  valor para el atributo institucionMultiempresa 
	 */
	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @return  Retorna la variable institucionMultiempresa
	 */
	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	 * 
	 * @param  valor para el atributo codigoInstitucion 
	 */
	public void setCodigoInstitucion(Integer codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoInstitucion
	 * 
	 * @return  Retorna la variable codigoInstitucion
	 */
	public Integer getCodigoInstitucion() {
		return codigoInstitucion;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo rutaLogo
	 * 
	 * @param  valor para el atributo rutaLogo 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo rutaLogo
	 * 
	 * @return  Retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	 * 
	 * @param  valor para el atributo ubicacionLogo 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo fechaInicialF
	 * 
	 * @param  valor para el atributo fechaInicialF 
	 */
	public void setFechaInicialF(String fechaInicialF) {
		this.fechaInicialF = fechaInicialF;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaInicialF
	 * 
	 * @return  Retorna la variable fechaInicialF
	 */
	public String getFechaInicialF() {
		this.fechaInicialF=UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial);
		return fechaInicialF;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo fechaFinalF
	 * 
	 * @param  valor para el atributo fechaFinalF 
	 */
	public void setFechaFinalF(String fechaFinalF) {
		this.fechaFinalF = fechaFinalF;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaFinalF
	 * 
	 * @return  Retorna la variable fechaFinalF
	 */
	public String getFechaFinalF() {
		this.fechaFinalF=UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal);
		return fechaFinalF;
	}


	/**
	 * M�todo que se encarga de establecer el valor 
	 * del atributo codigosServicios
	 * 
	 * @param  valor para el atributo codigosServicios 
	 */
	public void setCodigosServicios(ArrayList<Integer> codigosServicios) {
		this.codigosServicios = codigosServicios;
	}


	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigosServicios
	 * 
	 * @return  Retorna la variable codigosServicios
	 */
	public ArrayList<Integer> getCodigosServicios() {
		return codigosServicios;
	}

	public void setLoginUsuarios(ArrayList<String> loginUsuarios) {
		this.loginUsuarios = loginUsuarios;
	}

	public ArrayList<String> getLoginUsuarios() {
		return loginUsuarios;
	}

	public void setLoginProfesionales(ArrayList<String> loginProfesionales) {
		this.loginProfesionales = loginProfesionales;
	}

	public ArrayList<String> getLoginProfesionales() {
		return loginProfesionales;
	}



	
}
