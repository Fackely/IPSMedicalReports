
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;


public class DtoInformeInconsisenBD implements Serializable{

	private static Logger logger = Logger.getLogger(DtoInformeInconsisenBD.class);
	
	/**
	 * 
	 * */
	private int codigoPk;
	
	/**
	 * 
	 * */
	private int codigoConvenio;
	
	/**
	 * 
	 */
	private int subcuenta;
    
	/**
	 * 
	 */
	private String  nombreSubcuenta;
	
	/**
	 * 
	 */
	private int tipoInconsistencia;

	/**
	 * 
	 */
	private String descripcionTipoInconsistencia;
	
	/**
	 * 
	 * */
	private String descripcionConvenio;
	
	
	/**
	 * 
	 */
	private String primerNombre;
	
	/**
	 * 
	 */
	private String segundoNombre;
	
	/**
	 * 
	 */
	private String primerApellido;
	
	/**
	 * 
	 */
	private String segundoApellido;
	
	/**
	 * 
	 * */
	private String nombrePersona;
	
	
	/**
	 * 
	 */
	private String tipoIdentificacion;
	
	/**
	 * 
	 * */
	private String idPersona;
	
	/**
	 * 
	 */
	private String fechaNacimiento;
	
	/**
	 * 
	 */
	private String direccionResidencia;
	
	/**
	 * 
	 */
	private String telefono;
	
	/**
	 * 
	 */
	 private String departamento;
	 
	 /**
	 * 
	 */
	 private String codigoDepartamento;
	 
	/**
	 * 
	 */
	 private String municipio;
	
	 /**
	  * 
     */
	private String codigoMunicipio;
		 
	/**
	 *  
	 */
	private int codCobertura;
	
	/**
	 * 
	 */
	private String cobertura;	
	
	/**
	 * 
	 */
	private String observaciones;
	 	
	/**
	 * 
	 * */
	private String consecutivoIngreso;
	
	/**
	 * 
	 * */
	private String anioConsecutivoIngreso;
	
	/**
	 * 
	 * */
	private int idIngreso;
	
	/**
	 * 
	 * */
	private int codigoCuenta;
	
	/**
	 * 
	 * */
	private String descripcionCuenta;
	
	/**
	 * 
	 * */
	private String estadoInforme;
	
	/**
	 * 
	 * */
	private String fechaGeneracion;
	
	/**
	 * 
	 * */
	private String horaGeneracion;
	
	/**
	 * 
	 */
	private String usuarioGeneracion;
	
	
	/**
	 * 
	 * */
	private String fechaModificacion;
	
	/**
	 * 
	 * */
	private String horaModificacion;
	
	/**
	 * 
	 * */
	private String usuarioModificacion;
	
	/**
	 * 
	 * */
	private int codigoPaciente;
	
	/**
	 * 
	 * */
	private ArrayList< DtoRegistroEnvioInformInconsisenBD> historialEnvios;

	/**
	 * 
	 * */
	private String nombreCentroAtencion;
	
	/**
	 * 
	 * */
	private ArrayList<InfoDatosString> conveniosPaciente;
		
	/**
	 * 
	 */
	private int codigoViaIngreso;
	
	/**
	 * 
	 * */
	private String descripcionViaIngreso;
	
	/**
	 * 
	 * */
	private String [] variablesInco;
	
	/**
	 * 
	 */
	private String entidad;
	
	/**
	 * 
	 */
	private int codigoEntidad;
	
	/**
	 * 
	 */
	private String medioEnvio;
	
	/**
	 * 
	 */
    private String filtroVariable;
	
	/**
	 * 
	 */
	public ArrayList<DtoVariablesIncorrectasenBD> variablesIncorrectas; 
	
   
	//******************************************************
	/**
	 * 
	 */
	private static InfoDatosString convenio;
	
	/**
	 * 
	 */
	private static InfoDatosInt tipoInconsistenciaInfo;
	
	/**
	 * 
	 */
	private static String telefonoPer;
	/**
	 * 
	 */
	private static String telefonoCelularPer;
	/**
	 * 
	 */
	private static String cargo;
	/**
	 * 
	 */
	private String archivoInconGenerado;
	/**
	 * 
	 */
	private String pathArchivoIncoXml;
	
	
	//******************************************************
	public DtoInformeInconsisenBD()
	{
		reset();		
	}
	
	/**
	 * 
	 * */
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.subcuenta= ConstantesBD.codigoNuncaValido;
		this.codigoEntidad=ConstantesBD.codigoNuncaValido;
        this.nombreSubcuenta= new String("");
		this.tipoInconsistencia=ConstantesBD.codigoNuncaValido;
		this.descripcionTipoInconsistencia=new String("");
		this.descripcionConvenio = new String("");
		this.primerNombre= new String("");
		this.primerApellido= new String("");
		this.segundoNombre= new String("");
		this.segundoApellido= new String("");
		this.nombrePersona = new String("");
		this.idPersona = new String("");
		this.tipoIdentificacion=new String("");
		this.fechaNacimiento=new String("");
		this.direccionResidencia=new String("");
		this.telefono=new String("");
		this.departamento=new String("");
		this.codigoDepartamento=new String("");
		this.municipio=new String("");
		this.codigoMunicipio=new String("");
		this.codCobertura=ConstantesBD.codigoNuncaValido;
		this.cobertura=new String("");
		this.observaciones=new String("");
		this.consecutivoIngreso = new String("");
		this.anioConsecutivoIngreso = new String("");	
		this.idIngreso = ConstantesBD.codigoNuncaValido;
		this.codigoCuenta = ConstantesBD.codigoNuncaValido;
		this.descripcionCuenta = new String("");
		this.estadoInforme = new String("");
		this.fechaGeneracion = new String("");
		this.horaGeneracion = new String("");
		this.usuarioGeneracion=new String("");
		this.fechaModificacion=new String("");
		this.horaModificacion=new String("");
		this.usuarioModificacion=new String("");
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.historialEnvios = new ArrayList<DtoRegistroEnvioInformInconsisenBD>();
		this.variablesIncorrectas= new ArrayList<DtoVariablesIncorrectasenBD>();
		
		
		this.nombreCentroAtencion = new String("");
		this.conveniosPaciente = new ArrayList<InfoDatosString>();		
		this.descripcionViaIngreso = new String("");
		this.variablesInco = new String [100];
		this.entidad=new String("");
		this.medioEnvio=new String("");
		this.filtroVariable=new String("");
		
		//**********************************************
		this.convenio = new InfoDatosString("","");
		this.tipoInconsistenciaInfo = new InfoDatosInt(0,"");
		this.telefonoPer = ""; 
		this.telefonoCelularPer = "";
		this.cargo = "";
		//**********************************************
		
		this.archivoInconGenerado=ConstantesBD.acronimoNo;
		this.pathArchivoIncoXml = "";
		
	}

	
	
	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public int getIdIngreso() {
		return idIngreso;
	}

	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	public String getAnioConsecutivoIngreso() {
		return anioConsecutivoIngreso;
	}

	public void setAnioConsecutivoIngreso(String anioConsecutivoIngreso) {
		this.anioConsecutivoIngreso = anioConsecutivoIngreso;
	}

	public int getCodigoCuenta() {
		return codigoCuenta;
	}

	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	public String getEstadoInforme() {
		return estadoInforme;
	}

	public void setEstadoInforme(String estadoInforme) {
		this.estadoInforme = estadoInforme;
	}

	public ArrayList< DtoRegistroEnvioInformInconsisenBD> getHistorialEnvios() {
		return historialEnvios;
	}

	public void setHistorialEnvios(ArrayList< DtoRegistroEnvioInformInconsisenBD> historialEnvios) {
		this.historialEnvios = historialEnvios;
	}

	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public String getDescripcionConvenio() {
		return descripcionConvenio;
	}

	public void setDescripcionConvenio(String descripcionConvenio) {
		this.descripcionConvenio = descripcionConvenio;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public ArrayList<InfoDatosString> getConveniosPaciente() {
		return conveniosPaciente;
	}

	public void setConveniosPaciente(ArrayList<InfoDatosString> conveniosPaciente) {
		this.conveniosPaciente = conveniosPaciente;
	}

	public String getDescripcionViaIngreso() {
		return descripcionViaIngreso;
	}

	public void setDescripcionViaIngreso(String descripcionViaIngreso) {
		this.descripcionViaIngreso = descripcionViaIngreso;
	}

	

	public ArrayList<DtoVariablesIncorrectasenBD> getVariablesIncorrectas() {
		return variablesIncorrectas;
	}

	public void setVariablesIncorrectas(
			ArrayList<DtoVariablesIncorrectasenBD> variablesIncorrectas) {
		this.variablesIncorrectas = variablesIncorrectas;
	}

	public int getSubcuenta() {
		return subcuenta;
	}

	public void setSubcuenta(int subcuenta) {
		this.subcuenta = subcuenta;
	}

	public String getNombreSubcuenta() {
		return nombreSubcuenta;
	}

	public void setNombreSubcuenta(String nombreSubcuenta) {
		this.nombreSubcuenta = nombreSubcuenta;
	}

	public int getTipoInconsistencia() {
		return tipoInconsistencia;
	}

	public void setTipoInconsistencia(int tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}

	public String getDescripcionTipoInconsistencia() {
		return descripcionTipoInconsistencia;
	}

	public void setDescripcionTipoInconsistencia(
			String descripcionTipoInconsistencia) {
		this.descripcionTipoInconsistencia = descripcionTipoInconsistencia;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	public void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = direccionResidencia;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public int getCodCobertura() {
		return codCobertura;
	}

	public void setCodCobertura(int codCobertura) {
		this.codCobertura = codCobertura;
	}

	public String getCobertura() {
		return cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getDescripcionCuenta() {
		return descripcionCuenta;
	}

	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public String getUsuarioGeneracion() {
		return usuarioGeneracion;
	}

	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getHoraModificacion() {
		return horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String[] getVariablesInco() {
		return variablesInco;
	}

	public void setVariablesInco(String[] variablesInco) {
		this.variablesInco = variablesInco;
	}
  
	public String getNombreVariableInconsistencia(int pos)
	{
		String cadena= variablesInco[pos];
        String[] nombre=cadena.split(ConstantesBD.separadorSplit);
		
        return nombre[1];
	}
	
	
	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getMedioEnvio() {
		return medioEnvio;
	}

	public void setMedioEnvio(String medioEnvio) {
		this.medioEnvio = medioEnvio;
	}

	public String getFiltroVariable() {
		return filtroVariable;
	}

	public void setFiltroVariable(String filtroVariable) {
		this.filtroVariable = filtroVariable;
	}

	public int getCodigoEntidad() {
		return codigoEntidad;
	}

	public void setCodigoEntidad(int codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	//**************************************** para el informe de inconsistecias segun anexo 746 
	public static InfoDatosString getConvenio() {
		return convenio;
	}
	
	public static void setConvenio(InfoDatosString convenio) {
		DtoInformeInconsisenBD.convenio = convenio;
	}

	public static InfoDatosInt getTipoInconsistenciaInfo() {
		return tipoInconsistenciaInfo;
	}

	public static void setTipoInconsistenciaInfo(InfoDatosInt tipoInconsistenciaInfo) {
		DtoInformeInconsisenBD.tipoInconsistenciaInfo = tipoInconsistenciaInfo;
	}

	public static String getTelefonoPer() {
		return telefonoPer;
	}

	public static void setTelefonoPer(String telefonoPer) {
		DtoInformeInconsisenBD.telefonoPer = telefonoPer;
	}

	public static String getTelefonoCelularPer() {
		return telefonoCelularPer;
	}

	public static void setTelefonoCelularPer(String telefonoCelularPer) {
		DtoInformeInconsisenBD.telefonoCelularPer = telefonoCelularPer;
	}

	public static String getCargo() {
		return cargo;
	}

	public static void setCargo(String cargo) {
		DtoInformeInconsisenBD.cargo = cargo;
	}
	/**
	 * @return the archivoInconGenerado
	 */
	public String getArchivoInconGenerado() {
		return archivoInconGenerado;
	}
	/**
	 * @param archivoInconGenerado the archivoInconGenerado to set
	 */
	public void setArchivoInconGenerado(String archivoInconGenerado) {
		this.archivoInconGenerado = archivoInconGenerado;
	}

	/**
	 * @return the pathArchivoIncoXml
	 */
	public String getPathArchivoIncoXml() {
		return pathArchivoIncoXml;
	}

	/**
	 * @param pathArchivoIncoXml the pathArchivoIncoXml to set
	 */
	public void setPathArchivoIncoXml(String pathArchivoIncoXml) {
		this.pathArchivoIncoXml = pathArchivoIncoXml;
	}

	//****************************************
}	
