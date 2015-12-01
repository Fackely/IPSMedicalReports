package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RegionesCobertura;


public class ReporteCambioServiciosCitasOdontologicasForm extends ActionForm{
	
	private static final long serialVersionUID = 1L;
	
	public ReporteCambioServiciosCitasOdontologicasForm(){
		this.indicaCadenaVacia="s";
		
	}	
	
	/**
	 * Atributo que indica si la lista de tipos de 
	 * solicitud esta vacia o no.
	 */
	private String indicaCadenaVacia;
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * una ciudad-departamento y pais.
	 */
	private String ciudadDeptoPais;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * un pa&iacute;s.
	 */
	private String codigoPaisSeleccionado;
		
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * una regi&oacute;n.
	 */
	private long codigoRegionSeleccionada;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion.
	 */
	private Integer codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * un centro de atenci&oacute;n.
	 */
	private int consecutivoCentroAtencionSeleccionado;
	
	
	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina
	 */
	private String estado;
	
	
	/**
	 * Atributo que almacena el nombre completo del usuario activo en la sesion.
	 */
	private String nombreUsuario;
	
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	
	/**
	 * Atributo que almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;
	
	
	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n 
	 * del reporte generado.
	 */
	private String tipoSalida;
	

	/**
	 * Atributo que almacena el login del profesional que 
	 * valor&oacute;
	 */
	private String loginProfesional;
	
	
	/**
	 * Atributo que almacena el resultado de la consulta del primer filtro de b&uacute;squeda
	 * correspondiente a los ingresos.
	 */
	private ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaResultadoConsultaCambioServOdonto;
	
	
	/**
	 * enumeracion del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	
	/**
	 * Atributo que almacena la fecha a partir de la cual se 
	 * buscaran las solicitudes de cambio de servicios generadas.
	 */
	private String fechaInicial;
	
	
	/**
	 * Atributo que almacena la fecha hasta la cual se buscaran 
	 * las solicitudes de cambio de servicios generadas.
	 */
	private String fechaFinal;
	
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	
	/**
	 * Atributo que almacena la edad a partir de la cual 
	 * se buscaran los pacientes asociados a solicitudes de cambio de 
	 * servicio de citas Odontologicas
	 */
	private Integer rangoEdadInicialPaciente;
	
	
	/**
	 * Atributo donde se almacena la edad hasta la cual se 
	 * buscaran los pacientes asociados a solicitudes de cambio de 
	 * servicio de citas Odontologicas
	 */
	private Integer rangoEdadFinalPaciente;
	
		
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String institucionMultiempresa;
	
	
	/**
	 * Atributo que almacena los c&oacute;digos de
	 * los estados de solicitud.
	 */
	private String[] tiposEstadoSolicitud;
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * un profesional de la salud.
	 */
	private int codigoProfesional;
	
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexoPaciente;
	
	
	/**
	 * Atributo que almacena el login de
	 * usuario que confirm&oacute; o anul&oacute; 
	 * la solicitud de cambio de servicio.
	 */
	private String loginUsuario;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * servicio odontol&oacute;gico.
	 */
	private int codigoServicio=ConstantesBD.codigoNuncaValido;
	
	
	/**
	 * Atributo que almacena el nombre del
	 * servicio odontol&oacute;gico.
	 */
	private String nombreServicio="";
	
	
	/**
	 * Atributo que indica si la instituci&oacute;n utiliza programas odontol&oacute;gicos.
	 */
	private String utilizaProgramasOdonto;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * servicio odontol&oacute;gico a eliminar.
	 */
	private int indiceEliminarServicio;
	
	
	/**
	 * LISTA CODIGOS PROGRAMAS SERVICIOS
	 */
	private String listaCodigoProgramaServicios;
	
	
	/**
	 * Atributo que indica si se debe deshabilitar o no 
	 * el campo de programas odontol&oacute;gicos.
	 */
	private String deshabilitaPrograma;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * la especialidad odontol&oacute;gica.
	 */
	private int codigoEspecialidad;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * Programa odontol&oacute;gico.
	 */
	private long codigoPrograma;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	
	/**
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	
	/**
	 * 
	 */
	private int tipoReporte;
	
	
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
	 * Atributo que almacena la fecha inicial de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaInicialFormateado;
	
	
	/**
	 * Atributo que almacena la fecha final de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaFinalFormateado;

	private String nombreEspecialidad;
	private String registroSeleccionado;
	
	
	/**
	 * Atributo donde se almacenan los paises existentes en el sistema.
	 */
	private ArrayList<Paises> paises;
	
	
	/**
	 * Atributo donde se almacenan las cuidades existentes en el sistema.
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	
	/**
	 * Atributo donde se almacenan las regiones existentes en el sistema.
	 */
	private ArrayList<RegionesCobertura> regiones;
	
	
	/**
	 * Atributo que almacena el listado de empresas
	 * institucion existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	
	/**
	 * Atributo donde se almacenan los centros de atenci&oacute;n existentes en el sistema.
	 */
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	
	
	/**
	 * Atributo donde se almacenan los profesionales de la salud existentes en el sistema.
	 */
	private ArrayList<DtoPersonas> profesionales;
	
	
	/**
	 * Atributo donde se almacenan los usuarios existentes en el sistema.
	 */
	private ArrayList<DtoUsuarioPersona> usuarios;
	
	
	/**
	 * Atributo donde se almacenan los Servicios odontol&oacute;gicos existentes en el sistema.
	 */
	private ArrayList<DtoServicios> servicios;
	
	
	/**
	 * Atributo donde se almacenan las especialidades odontol&oacute;gicas existentes en el sistema.
	 */
	private List<Especialidades> especialidades;
	
	
	/**
	 * Atributo que almacena el listado con las opciones
	 * femenino y masculino.
	 */
	private ArrayList<DtoIntegridadDominio> listaSexoPaciente;
	
	
	/**
	 * DTO RECOMENDACIONES PROG SER PROG SER
	 */	
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
	
	
	/**
	 * Atributo que almacena codigo estandar para la busqueda de servicios.
	 */
	private int codigoManualEstandarBusquedaServicios;
	
	
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
	 *  del atributo codigoCiudad
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
	 *  del atributo codigoPais
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
	 *  del atributo codigoDpto
	 * 
	 * @param  valor para el atributo codigoDpto
	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaEmpresaInstitucion
	 * 
	 * @return  Retorna la variable listaEmpresaInstitucion
	 */
	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo listaEmpresaInstitucion
	 * 
	 * @param  valor para el atributo listaEmpresaInstitucion
	 */
	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucion
	 */
	public Integer getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @param  valor para el atributo codigoEmpresaInstitucion
	 */
	public void setCodigoEmpresaInstitucion(Integer codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo deshabilitaCiudad
	 * 
	 * @return  Retorna la variable deshabilitaCiudad
	 */
	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo deshabilitaCiudad
	 * 
	 * @param  valor para el atributo deshabilitaCiudad
	 */
	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
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
	 *  del atributo codigoPaisResidencia
	 * 
	 * @param  valor para el atributo codigoPaisResidencia
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo tiposEstadoSolicitud depende del atributo
	 *  indicaCadenaVacia 
	 * 
	 * @return  Retorna la variable tiposEstadoSolicitud
	 */
	public String[] getTiposEstadoSolicitud() {
		if(indicaCadenaVacia.equals("s")){
			this.tiposEstadoSolicitud= new String[]{};
		}
		return tiposEstadoSolicitud;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo tiposEstadoSolicitud
	 * 
	 * @param  valor para el atributo tiposEstadoSolicitud
	 */
	public void setTiposEstadoSolicitud(String[] tiposEstadoSolicitud) {
		this.tiposEstadoSolicitud = tiposEstadoSolicitud;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaSexoPaciente
	 * 
	 * @return  Retorna la variable listaSexoPaciente
	 */
	public ArrayList<DtoIntegridadDominio> getListaSexoPaciente() {
		return listaSexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo listaSexoPaciente
	 * 
	 * @param  valor para el atributo listaSexoPaciente
	 */
	public void setListaSexoPaciente(
			ArrayList<DtoIntegridadDominio> listaSexoPaciente) {
		this.listaSexoPaciente = listaSexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @param  valor para el atributo sexoPaciente
	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPrograma
	 * 
	 * @return  Retorna la variable codigoPrograma
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoPrograma
	 * 
	 * @param  valor para el atributo codigoPrograma
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @return  Retorna la variable codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @param  valor para el atributo codigoEspecialidad
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo especialidades
	 * 
	 * @return  Retorna la variable especialidades
	 */
	public List<Especialidades> getEspecialidades() {
		return especialidades;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo especialidades
	 * 
	 * @param  valor para el atributo especialidades
	 */
	public void setEspecialidades(List<Especialidades> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo deshabilitaPrograma
	 * 
	 * @return  Retorna la variable deshabilitaPrograma
	 */
	public String getDeshabilitaPrograma() {
		return deshabilitaPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo deshabilitaPrograma
	 * 
	 * @param  valor para el atributo deshabilitaPrograma
	 */
	public void setDeshabilitaPrograma(String deshabilitaPrograma) {
		this.deshabilitaPrograma = deshabilitaPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCodigoProgramaServicios
	 * 
	 * @return  Retorna la variable listaCodigoProgramaServicios
	 */
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo listaCodigoProgramaServicios
	 * 
	 * @param  valor para el atributo listaCodigoProgramaServicios
	 */
	public void setListaCodigoProgramaServicios(String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}

	/**
	 *  M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dtoSerProSerPro y establecer el codigo
	 *  del programa seleccionado
	 * 
	 * @return  Retorna la variable dtoSerProSerPro
	 */
	public RecomSerproSerpro getDtoSerProSerPro() {
		setCodigoPrograma(dtoSerProSerPro.getProgramas().getCodigo());
		return dtoSerProSerPro;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo dtoSerProSerPro
	 * 
	 * @param  valor para el atributo dtoSerProSerPro
	 */
	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		
		this.dtoSerProSerPro = dtoSerProSerPro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo utilizaProgramasOdonto
	 * 
	 * @return  Retorna la variable utilizaProgramasOdonto
	 */
	public String getUtilizaProgramasOdonto() {
		return utilizaProgramasOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo utilizaProgramasOdonto
	 * 
	 * @param  valor para el atributo utilizaProgramasOdonto
	 */
	public void setUtilizaProgramasOdonto(String utilizaProgramasOdonto) {
		this.utilizaProgramasOdonto = utilizaProgramasOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoServicio
	 * 
	 * @return  Retorna la variable codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoServicio
	 * 
	 * @param  valor para el atributo codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreServicio
	 * 
	 * @return  Retorna la variable nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo nombreServicio
	 * 
	 * @param  valor para el atributo nombreServicio
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo indiceEliminarServicio
	 * 
	 * @return  Retorna la variable indiceEliminarServicio
	 */
	public int getIndiceEliminarServicio() {
		return indiceEliminarServicio;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo indiceEliminarServicio
	 * 
	 * @param  valor para el atributo indiceEliminarServicio
	 */
	public void setIndiceEliminarServicio(int indiceEliminarServicio) {
		this.indiceEliminarServicio = indiceEliminarServicio;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisSeleccionado
	 * 
	 * @return  Retorna la variable codigoPaisSeleccionado
	 */
	public String getCodigoPaisSeleccionado() {
		return codigoPaisSeleccionado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoPaisSeleccionado
	 * 
	 * @param  valor para el atributo codigoPaisSeleccionado
	 */
	public void setCodigoPaisSeleccionado(String codigoPaisSeleccionado) {
		this.codigoPaisSeleccionado = codigoPaisSeleccionado;
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
	 *  del atributo ciudadDeptoPais
	 * 
	 * @param  valor para el atributo ciudadDeptoPais
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoRegionSeleccionada
	 * 
	 * @return  Retorna la variable codigoRegionSeleccionada
	 */
	public long getCodigoRegionSeleccionada() {
		return codigoRegionSeleccionada;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoRegionSeleccionada
	 * 
	 * @param  valor para el atributo codigoRegionSeleccionada
	 */
	public void setCodigoRegionSeleccionada(long codigoRegionSeleccionada) {
		this.codigoRegionSeleccionada = codigoRegionSeleccionada;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencionSeleccionado
	 */
	public int getConsecutivoCentroAtencionSeleccionado() {
		return consecutivoCentroAtencionSeleccionado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencionSeleccionado
	 */
	public void setConsecutivoCentroAtencionSeleccionado(
			int consecutivoCentroAtencionSeleccionado) {
		this.consecutivoCentroAtencionSeleccionado = consecutivoCentroAtencionSeleccionado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo tipoReporte
	 * 
	 * @return  Retorna la variable tipoReporte
	 */
	public int getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo tipoReporte
	 * 
	 * @param  valor para el atributo tipoReporte
	 */
	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo servicios
	 * 
	 * @return  Retorna la variable servicios
	 */
	public ArrayList<DtoServicios> getServicios() {
		return servicios;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo servicios
	 * 
	 * @param  valor para el atributo servicios
	 */
	public void setServicios(ArrayList<DtoServicios> servicios) {
		this.servicios = servicios;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @return  Retorna la variable institucionMultiempresa
	 */
	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @param  valor para el atributo institucionMultiempresa
	 */
	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo paises
	 * 
	 * @return  Retorna la variable paises
	 */
	public ArrayList<Paises> getPaises() {
		return paises;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo paises
	 * 
	 * @param  valor para el atributo paises
	 */
	public void setPaises(ArrayList<Paises> paises) {
		this.paises = paises;
	}

	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCiudades
	 * 
	 * @return  Retorna la variable listaCiudades
	 */
	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo listaCiudades
	 * 
	 * @param  valor para el atributo listaCiudades
	 */
	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo regiones
	 * 
	 * @return  Retorna la variable regiones
	 */
	public ArrayList<RegionesCobertura> getRegiones() {
		return regiones;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo regiones
	 * 
	 * @param  valor para el atributo regiones
	 */
	public void setRegiones(ArrayList<RegionesCobertura> regiones) {
		this.regiones = regiones;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo centrosAtencion
	 * 
	 * @return  Retorna la variable centrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo centrosAtencion
	 * 
	 * @param  valor para el atributo centrosAtencion
	 */
	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo loginUsuario
	 * 
	 * @return  Retorna la variable loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo loginUsuario
	 * 
	 * @param  valor para el atributo loginUsuario
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo usuarios
	 * 
	 * @return  Retorna la variable usuarios
	 */
	public ArrayList<DtoUsuarioPersona> getUsuarios() {
		return usuarios;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo usuarios
	 * 
	 * @param  valor para el atributo usuarios
	 */
	public void setUsuarios(ArrayList<DtoUsuarioPersona> usuarios) {
		this.usuarios = usuarios;
	}

	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo profesionales
	 * 
	 * @return  Retorna la variable profesionales
	 */
	public ArrayList<DtoPersonas> getProfesionales() {
		return profesionales;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo profesionales
	 * 
	 * @param  valor para el atributo profesionales
	 */
	public void setProfesionales(ArrayList<DtoPersonas> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoProfesional
	 * 
	 * @return  Retorna la variable codigoProfesional
	 */
	public int getCodigoProfesional() {
		return codigoProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoProfesional
	 * 
	 * @param  valor para el atributo codigoProfesional
	 */
	public void setCodigoProfesional(int codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadInicialPaciente
	 * 
	 * @return  Retorna la variable rangoEdadInicialPaciente
	 */
	public Integer getRangoEdadInicialPaciente() {
		return rangoEdadInicialPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo rangoEdadInicialPaciente
	 * 
	 * @param  valor para el atributo rangoEdadInicialPaciente
	 */
	public void setRangoEdadInicialPaciente(Integer rangoEdadInicialPaciente) {
		this.rangoEdadInicialPaciente = rangoEdadInicialPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadFinalPaciente
	 * 
	 * @return  Retorna la variable rangoEdadFinalPaciente
	 */
	public Integer getRangoEdadFinalPaciente() {
		return rangoEdadFinalPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo rangoEdadFinalPaciente
	 * 
	 * @param  valor para el atributo rangoEdadFinalPaciente
	 */
	public void setRangoEdadFinalPaciente(Integer rangoEdadFinalPaciente) {
		this.rangoEdadFinalPaciente = rangoEdadFinalPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo estado
	 * 
	 * @param  valor para el atributo estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo path
	 * 
	 * @param  valor para el atributo path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo nombreArchivoGenerado
	 * 
	 * @param  valor para el atributo nombreArchivoGenerado
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreArchivoGenerado
	 * 
	 * @return  Retorna la variable nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo tipoSalida
	 * 
	 * @param  valor para el atributo tipoSalida
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo tipoSalida
	 * 
	 * @return  Retorna la variable tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo enumTipoSalida
	 * 
	 * @return  Retorna la variable enumTipoSalida
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo enumTipoSalida
	 * 
	 * @param  valor para el atributo enumTipoSalida
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo registroSeleccionado
	 * 
	 * @param  valor para el atributo registroSeleccionado
	 */
	public void setRegistroSeleccionado(String registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo registroSeleccionado
	 * 
	 * @return  Retorna la variable registroSeleccionado
	 */
	public String getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @param  valor para el atributo nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
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
	 *  del atributo fechaInicialFormateado
	 * 
	 * @param  valor para el atributo fechaInicialFormateado
	 */
	public void setFechaInicialFormateado(String fechaInicialFormateado) {
		this.fechaInicialFormateado = fechaInicialFormateado;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicialFormateado convirtiendo
	 *  la fechaInicial de tipo yyyy-mm-dd
	 * 
	 * @return  Retorna la variable fechaInicialFormateado
	 */
	public String getFechaInicialFormateado() {
		fechaInicialFormateado = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		return fechaInicialFormateado;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo fechaFinalFormateado
	 * 
	 * @param  valor para el atributo fechaFinalFormateado
	 */
	public void setFechaFinalFormateado(String fechaFinalFormateado) {
		this.fechaFinalFormateado = fechaFinalFormateado;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinalFormateado convirtiendo
	 *  la fechaFinal de tipo yyyy-mm-dd
	 * 
	 * @return  Retorna la variable fechaFinalFormateado
	 */
	public String getFechaFinalFormateado() {
		this.fechaFinalFormateado = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		return fechaFinalFormateado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo listaResultadoConsultaCambioServOdonto
	 * 
	 * @param  valor para el atributo listaResultadoConsultaCambioServOdonto
	 */
	public void setListaResultadoConsultaCambioServOdonto(
			ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaResultadoConsultaCambioServOdonto) {
		this.listaResultadoConsultaCambioServOdonto = listaResultadoConsultaCambioServOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaResultadoConsultaCambioServOdonto
	 * 
	 * @return  Retorna la variable listaResultadoConsultaCambioServOdonto
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> getListaResultadoConsultaCambioServOdonto() {
		return listaResultadoConsultaCambioServOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo loginProfesional
	 * 
	 * @param  valor para el atributo loginProfesional
	 */
	public void setLoginProfesional(String loginProfesional) {
		this.loginProfesional = loginProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo loginProfesional
	 * 
	 * @return  Retorna la variable loginProfesional
	 */
	public String getLoginProfesional() {
		return loginProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @param  valor para el atributo nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
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
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Fabian Becerra
	 *  @author Wilson Gomez
	 *  @author Javier Gonzalez
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1){
		// TODO Auto-generated method stub
		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("generarReporte")){
			
			//validaciones rango de fechas
			if(UtilidadTexto.isEmpty(getFechaInicial())){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				this.tipoSalida=null;
				
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
					 this.tipoSalida=null;
					
				}
			}
			if(UtilidadTexto.isEmpty(getFechaFinal())){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				this.tipoSalida=null;
			
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
					this.tipoSalida=null;
		
				}
				if((getFechaInicial()!=null) && 
						(!(getFechaInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
						this.tipoSalida=null;
					
					}
				}
			}

			
			
			
			
			//validaciones rangos de edades
			if(getRangoEdadInicialPaciente()!=null && getRangoEdadFinalPaciente()==null){
				errores.add("EL RANGO DE EDAD FINAL ES REQUERIDO.", new ActionMessage(
				 "errors.rangoEdadFinalRequerido"));
				this.tipoSalida=null;
			}
			else 
				if(getRangoEdadFinalPaciente()!=null&&getRangoEdadInicialPaciente()!=null){
					  if (getRangoEdadInicialPaciente() >= getRangoEdadFinalPaciente()){
							
							errores.add("RANGO EDAD FINAL MENOR QUE EDAD INICIAL.", new ActionMessage(
									 "errors.edadInicialMayorIgualEdadFinal"," Inicial "
									 +getRangoEdadInicialPaciente()," Final "
									 +getRangoEdadFinalPaciente()));
							this.tipoSalida=null;
						
					  }
			  }
				
			if (UtilidadTexto.isEmpty(getCodigoPaisSeleccionado())
					|| getCodigoPaisSeleccionado().equals(ConstantesBD.codigoNuncaValido+"")) {
				
				errores.add("PAIS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
				this.tipoSalida=null;
			}
			
		}
			
	      return errores;
	}
	
	
	/**
	 * Este método se encarga de inicializar todos los valores de la forma.
	 *  @author Fabian Becerra
	 *  @author Wilson Gomez
	 *  @author Javier Gonzalez
	 */
	public void reset()
	{
		this.sexoPaciente=ConstantesBD.codigoNuncaValido+"";
		this.fechaFinal="";
		this.fechaInicial="";
		this.codigoProfesional=ConstantesBD.codigoNuncaValido;
		this.loginProfesional=ConstantesBD.codigoNuncaValido+"";
		this.loginUsuario=ConstantesBD.codigoNuncaValido+"";
		this.consecutivoCentroAtencionSeleccionado=ConstantesBD.codigoNuncaValido;
		this.ciudadDeptoPais=ConstantesBD.codigoNuncaValido+"";
		this.codigoRegionSeleccionada=ConstantesBD.codigoNuncaValido;
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValido;
		this.codigoPaisSeleccionado=ConstantesBD.codigoNuncaValido+"";
		this.dtoSerProSerPro = 					new RecomSerproSerpro();
		this.dtoSerProSerPro.setProgramas(		new Programas());
		this.dtoSerProSerPro.setCodigoPk(ConstantesBD.codigoNuncaValido);
		this.deshabilitaPrograma = "false";
		this.tiposEstadoSolicitud= new String[]{};
		this.tipoSalida= 						null;
		this.enumTipoSalida= 					null;
		this.nombreArchivoGenerado = 			"";
		this.setListaResultadoConsultaCambioServOdonto(new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>());
		this.especialidades =		new ArrayList<Especialidades>();
		this.setCodigoEspecialidad(ConstantesBD.codigoNuncaValido);
		this.rangoEdadInicialPaciente=null;
		this.rangoEdadFinalPaciente=null;
		
			
	}

	public void setIndicaCadenaVacia(String indicaCadenaVacia) {
		this.indicaCadenaVacia = indicaCadenaVacia;
	}

	public String getIndicaCadenaVacia() {
		return indicaCadenaVacia;
	}

	public void setCodigoManualEstandarBusquedaServicios(
			int codigoManualEstandarBusquedaServicios) {
		this.codigoManualEstandarBusquedaServicios = codigoManualEstandarBusquedaServicios;
	}

	public int getCodigoManualEstandarBusquedaServicios() {
		return codigoManualEstandarBusquedaServicios;
	}

	

	
}
