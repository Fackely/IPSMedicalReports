package com.princetonsa.dto.administracion;
 
import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.servinte.axioma.orm.Ciudades;


/**
 * DTO Persona. Esta Clase hace referencia a las personas del sistemas
 * @author Axioma, Edgar 
 *
 */
@SuppressWarnings("serial")
public class DtoPersonas implements Serializable
{
	/**
	 * c&oacute;digo de la persona.
	 */
	private int codigo;
	
	/**
	 * Atributo que almacena el tipo de identificación 
	 * de la persona.
	 */
	private String tipoId;

	/**
	 * Atributo que almacena el nombre del tipo de identificación 
	 * de la persona.
	 */
	private String nombreTipoIdentificacion;

	/**
	 * Atributo que almacena el n&uacute;mero de identificaci&oacute;n 
	 * de la persona.
	 */ 
	private String numeroId;
	
	/**
	 * Atributo que almacena el primer nombre de la persona.
	 */
	private String primerNombre;
	
	/**
	 * Atributo que almacena el segundo nombre de la persona.
	 */
	private String segundoNombre;
	
	/**
	 * Atributo que almacena el primer apellido de la persona.
	 */
	private String primerApellido;
	
	/**
	 * Atributo que almacena el segundo apellido de la persona.
	 */
	private String segundoApellido;
	
	/**
	 * Atributo que almacena la fecha de nacimiento de la persona.
	 */
	private String fechaNacimiento;
	
	/**
	 * Atributo que almacena la fecha de nacimiento de la persona tipo Date.
	 */
	private Date fechaNacimientoTipoDate;
	
	/**
	 * Atributo que almacena el c&oacute;digo del pais de 
	 * nacimiento de la persona.
	 */
	private String codigoPaisNacimiento;

	/**
	 * Atributo que almacena el c&oacute;digo del departamento de 
	 * nacimiento de la persona.
	 */
	private String codigoDepartamentoNacimiento;

	/**
	 * Atributo que almacena el c&oacute;digo de la ciudad de 
	 * nacimiento de la persona.
	 */
	private String codigoCiudadNacimiento;
	
	/**
	 * Atributo que almacena el tipo de sangre de la persona.
	 */
	private String tipoSangre;
	
	/**
	 * Atributo que almacena el tipo de persona.
	 */
	private String tipoPersona;
	
	/**
	 * Atributo que almacena el codigo del tipo de persona.
	 */
	private String codTipoPersona;
	
	/**
	 * Atributo que almacena el sexo de la persona.
	 */
	private String sexo; //private Sexo sexo;
	
	/**
	 * Atributo que almacena el c&oacute;digo del 
	 * centro de atenci&oacute;n al que pertenece la persona.
	 */
	private int cAtencionDuenio;
	
	/**
	 * Atributo que almacena el c&oacute;digo del 
	 * centro de atenci&oacute;n al que ingres&oacute; la persona.
	 */
	private int cAtencionIngreso;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la instituci&oacute;n
	 * al que pertenece la persona.
	 */
	private int institucion;
	
	/**
	 * Atributo que almacena la direcci&oacute;n de la persona.
	 */
	private String direccion;
	
	/**
	 * Atributo que almacena el n&uacute;mero de tel&eacute;fono de 
	 * la persona.
	 */
	private String telefono;
	
	/**
	 * Atributo para cargar el nombre de la identificacion 
	 */
	private String nombreIdentificacion;
	
	/**
	 * Atributo que almacena el login de la persona.
	 */
	private String login;
	
	/**
	 * 
	 */
	private String nombreCompletoXLogin;
	
	/**
	 * Almacena la descripción del centro de atención duenio del paciente.
	 */
	private String descripcionCentroAtencionDuenio;
	
	/**
	 * Ciudad de residencia
	 */
	private Ciudades ciudadResidenciaPersona;
	
	
	/**
	 * Atributo que almacena el nombre completo de la persona.
	 */
	private String nombreCompletoPersona;
	
	
	/**
	 * Atributo que almacena la fecha de
	 * nacimiento de la persona.
	 */
	private Date fechaNacimientoPersona;
	
	/**
	 * Atributo que almacena el número 
	 * de teléfono fijo de la persona.
	 */
	private Integer telefonoFijo;
	
	/**
	 * Atributo que almacena el código completo de la ciudad de residencia de
	 * la persona.
	 */
	private String codigoCiudadDptoPaisRes;
	
	/**
	 * Almacena el nombre de la ciudad de residencia.
	 */
	private String nombreCiudadRes;
	
	
	/**
	 * Almacena el nombre del departamento de residencia.
	 */
	private String nombreDptoRes;
	
	/**
	 * Almacena el nombre del país de residencia.
	 */
	private String nombrePaisRes;
	
	/**
	 * Almacena las observaciones realizadas a la persona.
	 */
	private String observaciones;
	
	
	// -- PARAMETROS DE BUSQUEDA -FUNCIONALIDAD SUBIR PACIENTE MASIVO----
	/** *  Define si se debe buscar por coincidencia exacta o con un like*/
	private boolean buscarPorLike;
	
		/**El get concatena los dos nombres*/
	private String soloNombres;
	/**El get concatena los dos apellidos*/
	private String soloApellidos;
	/**Relacion Personas seleccionadas para guardar*/
	private boolean seleccionado;
	/**Relaciona la linea leeida del archivo plano en Subir Paciente Masivo*/
	private int lineaPersonaArchivoPlano;
	private String tipoInconsistencia;
	//private boolean esCapitado;
	private String clasificacionSE; 
	
	/**Campos para guardar en caso de que no existan en usuarios capitados anexo 922*/	 
	private String email; 
	private String numeroFicha;
	private String tipoIdEmpleador;
	private String numIdEmpleador;
	private String razonSociEmpleador;
	private String tipoIdCotizante;
	private String numIdCotizante;
	private String nombresCotizante;
	private String apellidosCotizante;
	private String parentesco;
	private String centroAtencion;
	private int centroAtencionInt;
	private String tipoAfiliado;
	private String excepcionMonto; 	
	private String pais;
	private String departamento;
	private String municipio;
	private String localidad;
	private String barrio;
		
	
	/**
	 * Atributo que almacena el numero de Registro del archivo plano.
	 */
	private int numeroRegistro;
	
	
	/***
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void reset() 
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.tipoId="";
		this.numeroId="";
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.fechaNacimiento="";
		this.sexo="";
		this.codigoPaisNacimiento="";
		this.codigoCiudadNacimiento="";
		this.tipoSangre="";
		this.tipoPersona="";
		this.codTipoPersona="";
		this.cAtencionDuenio=ConstantesBD.codigoNuncaValido;
		this.cAtencionIngreso=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.direccion="";
		this.telefono="";
		this.nombreTipoIdentificacion="";
		this.nombreCompletoXLogin="";
		
		this.telefonoFijo = null;
		this.codigoCiudadDptoPaisRes = "";
		this.nombreCiudadRes = "";
		this.nombreDptoRes = "";
		this.nombrePaisRes = "";
		this.observaciones = "";
		this.setEmail("");
		this.nombreCompletoPersona = "";
		this.descripcionCentroAtencionDuenio = "";
		
		this.buscarPorLike = true;
		this.fechaNacimientoTipoDate=null;
		this.soloApellidos="";
		this.soloNombres="";
		this.seleccionado=false;
		this.lineaPersonaArchivoPlano=ConstantesBD.codigoNuncaValido;
		this.tipoInconsistencia=ConstantesIntegridadDominio.acronimoTipoNumIDNoCorresponde;
		//this.esCapitado=false;
		this.clasificacionSE="";
		
		/**Campos para guardar en caso de que no existan en usuarios capitados Anexo 922*/	 
		this.email=""; 
		this.numeroFicha="";
		this.tipoIdEmpleador="";
		this.numIdEmpleador="";
		this.razonSociEmpleador="";
		this.tipoIdCotizante="";
		this.numIdCotizante="";
		this.nombresCotizante="";
		this.apellidosCotizante="";
		this.parentesco="";
		this.centroAtencion="";
		this.centroAtencionInt=ConstantesBD.codigoNuncaValido;
		this.tipoAfiliado="";
		this.excepcionMonto=""; 	
		this.pais="";
		this.departamento="";
		this.municipio="";
		this.localidad="";
		this.barrio="";
		
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoId() {
		return tipoId;
	}

	/**
	 * @param tipoId
	 * @deprecated
	 */
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	/**
	 * Se debe estandarizar los nombres de los métodos en todo el sistema, por
	 * esto se debe utilizar <code>getNumeroIdentificacion()</code>
	 * @return Número de identificación de la persona
	 * @deprecated
	 */
	public String getNumeroId() {
		return numeroId;
	}

	/**
	 * Se debe estandarizar los nombres de los métodos en todo el sistema, por
	 * esto se debe utilizar <code>setNumeroIdentificacion(...)</code>
	 * @param numeroId Número de identificación a asignar
	 * @deprecated
	 */
	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		if(UtilidadTexto.isEmpty(segundoNombre))
			segundoNombre="";				
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
		if(UtilidadTexto.isEmpty(segundoApellido))
			segundoApellido="";				
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the codigoPaisNacimiento
	 */
	public String getCodigoPaisNacimiento() {
		return codigoPaisNacimiento;
	}

	/**
	 * @param codigoPaisNacimiento the codigoPaisNacimiento to set
	 */
	public void setCodigoPaisNacimiento(String codigoPaisNacimiento) {
		this.codigoPaisNacimiento = codigoPaisNacimiento;
	}

	/**
	 * @return the codigoCiudadNacimiento
	 */
	public String getCodigoCiudadNacimiento() {
		return codigoCiudadNacimiento;
	}

	/**
	 * @param codigoCiudadNacimiento the codigoCiudadNacimiento to set
	 */
	public void setCodigoCiudadNacimiento(String codigoCiudadNacimiento) {
		this.codigoCiudadNacimiento = codigoCiudadNacimiento;
	}

	/**
	 * @return the tipoSangre
	 */
	public String getTipoSangre() {
		return tipoSangre;
	}

	/**
	 * @param tipoSangre the tipoSangre to set
	 */
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	/**
	 * @return the tipoPersona
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}

	/**
	 * @param tipoPersona the tipoPersona to set
	 */
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	/**
	 * @return the codTipoPersona
	 */
	public String getCodTipoPersona() {
		return codTipoPersona;
	}

	/**
	 * @param codTipoPersona the codTipoPersona to set
	 */
	public void setCodTipoPersona(String codTipoPersona) {
		this.codTipoPersona = codTipoPersona;
	}

	/**
	 * @param cAtencionDuenio the codTipoPersona to set
	 */
	public int getcAtencionDuenio() {
		return cAtencionDuenio;
	}

	/**
	 * @param cAtencionDuenio the codTipoPersona to set
	 */
	public void setcAtencionDuenio(int cAtencionDuenio) {
		this.cAtencionDuenio = cAtencionDuenio;
	}
	
	/**
	 * @param cAtencionDuenio the codTipoPersona to set
	 */
	public void setcAtencionDuenioInteger(Integer cAtencionDuenio) {
		
		if (cAtencionDuenio == null) {
			this.cAtencionDuenio = ConstantesBD.codigoNuncaValido;
		}else{
			this.cAtencionDuenio = cAtencionDuenio;
		}
	}

	/**
	 * @param cAtencionIngreso the codTipoPersona to set
	 */
	public int getcAtencionIngreso() {
		return cAtencionIngreso;
	}

	/**
	 * @param cAtencionIngreso the codTipoPersona to set
	 */
	public void setcAtencionIngreso(int cAtencionIngreso) {
		this.cAtencionIngreso = cAtencionIngreso;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	/**
	 * Obtener el tipo de identificación de la persona
	 */
	public String getTipoIdentificacion()
	{
		return tipoId;
	}

	/**
	 * Asignar el tipo de identificación de la persona
	 * @param tipoIdentificacion
	 */
	public void setTipoIdentificacion(String tipoIdentificacion)
	{
		this.tipoId=tipoIdentificacion;
	}

	/**
	 * Obtiene el valor del atributo numeroIdentificacion
	 *
	 * @return Retorna atributo numeroIdentificacion
	 */
	public String getNumeroIdentificacion()
	{
		return numeroId;
	}

	/**
	 * Establece el valor del atributo numeroIdentificacion
	 *
	 * @param valor para el atributo numeroIdentificacion
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion)
	{
		this.numeroId = numeroIdentificacion;
	}

	/**
	 * Obtiene el valor del atributo nombreTipoIdentificacion
	 *
	 * @return Retorna atributo nombreTipoIdentificacion
	 */
	public String getNombreTipoIdentificacion()
	{
		return nombreTipoIdentificacion;
	}

	/**
	 * Establece el valor del atributo nombreTipoIdentificacion
	 *
	 * @param valor para el atributo nombreTipoIdentificacion
	 */
	public void setNombreTipoIdentificacion(String nombreTipoIdentificacion)
	{
		this.nombreTipoIdentificacion = nombreTipoIdentificacion;
	}

	public void setNombreIdentificacion(String nombreIdentificacion) {
		this.nombreIdentificacion = nombreIdentificacion;
	}

	public String getNombreIdentificacion() {
		return nombreIdentificacion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo login
	 * 
	 * @return  Retorna la variable login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo login
	 * 
	 * @param  valor para el atributo login 
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * Obtener el nombre completo de la persona
	 * @return retorna nombre completo
	 */
	public String getNombreCompleto()
	{
		String nombreCompleto = "";
		
		if (!UtilidadTexto.isEmpty(this.primerApellido)) {
			nombreCompleto +=  this.primerApellido + " ";
		}
		
		if (!UtilidadTexto.isEmpty(this.segundoApellido)) {
			nombreCompleto +=  this.segundoApellido + " ";
		}
		
		if (!UtilidadTexto.isEmpty(this.primerNombre)) {
			nombreCompleto +=  this.primerNombre + " ";
		}
		
		if (!UtilidadTexto.isEmpty(this.segundoNombre)) {
			nombreCompleto +=  this.segundoNombre + " ";
		}
		
		return nombreCompleto;
	}	
		

	/**
	 * M&eacute;todo que se encarga de establecer
	 * el Nombre Completo por Login
	 * @param nombreCompletoXLogin
	 */
	public void setNombreCompletoXLogin(String nombreCompletoXLogin) {
		this.nombreCompletoXLogin = nombreCompletoXLogin;
	}
	
	/**
	 *  M&eacute;todo que se encarga de obtener
	 * el Nombre Completo por Login
	 * @return
	 */

	public String getNombreCompletoXLogin() {
		
		this.nombreCompletoXLogin= Utilidades.obtenerNombreUsuarioXLogin(login, true); 
		
		return nombreCompletoXLogin;
		
		
	}
	/**
	 * campo para 
	 * @return
	 */
	public boolean isBuscarPorLike() {
		return buscarPorLike;
	}

	public void setBuscarPorLike(boolean buscarPorLike) {
		this.buscarPorLike = buscarPorLike;
	}

	public void setFechaNacimientoTipoDate(Date fechaNacimientoTipoDate) {
		this.fechaNacimientoTipoDate = fechaNacimientoTipoDate;
	}

	public Date getFechaNacimientoTipoDate() {
		return fechaNacimientoTipoDate;
	}
	
	
	public String getSoloNombres()
	{
		soloNombres=primerNombre;
		if(!UtilidadTexto.isEmpty(segundoNombre))
			soloNombres+=" "+segundoNombre;
				
		return soloNombres;
	}
	
	public String getSoloApellidos()
	{
		soloApellidos=primerApellido;
		if(!UtilidadTexto.isEmpty(segundoApellido))
			soloApellidos+=" "+segundoApellido;
				
		return soloApellidos;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setLineaPersonaArchivoPlano(int lineaPersonaArchivoPlano) {
		this.lineaPersonaArchivoPlano = lineaPersonaArchivoPlano;
	}

	public int getLineaPersonaArchivoPlano() {
		return lineaPersonaArchivoPlano;
	}

	public void setTipoInconsistencia(String tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}

	public String getTipoInconsistencia() {
		return tipoInconsistencia;
	}

	/*public void setEsCapitado(boolean esCapitado) {
		this.esCapitado = esCapitado;
	}

	public boolean isEsCapitado() {
		return esCapitado;
	}*/

	public void setClasificacionSE(String clasificacionSE) {
		this.clasificacionSE = clasificacionSE;
	}

	public String getClasificacionSE() {
		return clasificacionSE;
	}

	public String getNumeroFicha() {
		return numeroFicha;
	}

	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}

	public String getTipoIdEmpleador() {
		return tipoIdEmpleador;
	}

	public void setTipoIdEmpleador(String tipoIdEmpleador) {
		this.tipoIdEmpleador = tipoIdEmpleador;
	}

	public String getNumIdEmpleador() {
		return numIdEmpleador;
	}

	public void setNumIdEmpleador(String numIdEmpleador) {
		this.numIdEmpleador = numIdEmpleador;
	}

	public String getRazonSociEmpleador() {
		return razonSociEmpleador;
	}

	public void setRazonSociEmpleador(String razonSociEmpleador) {
		this.razonSociEmpleador = razonSociEmpleador;
	}

	public String getTipoIdCotizante() {
		return tipoIdCotizante;
	}

	public void setTipoIdCotizante(String tipoIdCotizante) {
		this.tipoIdCotizante = tipoIdCotizante;
	}

	public String getNumIdCotizante() {
		return numIdCotizante;
	}

	public void setNumIdCotizante(String numIdCotizante) {
		this.numIdCotizante = numIdCotizante;
	}

	public String getNombresCotizante() {
		return nombresCotizante;
	}

	public void setNombresCotizante(String nombresCotizante) {
		this.nombresCotizante = nombresCotizante;
	}

	public String getApellidosCotizante() {
		return apellidosCotizante;
	}

	public void setApellidosCotizante(String apellidosCotizante) {
		this.apellidosCotizante = apellidosCotizante;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public int getCentroAtencionInt() {
		return centroAtencionInt;
	}

	public void setCentroAtencionInt(int centroAtencionInt) {
		this.centroAtencionInt = centroAtencionInt;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public String getExcepcionMonto() {
		return excepcionMonto;
	}

	public void setExcepcionMonto(String excepcionMonto) {
		this.excepcionMonto = excepcionMonto;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public void setSoloNombres(String soloNombres) {
		this.soloNombres = soloNombres;
	}

	public void setSoloApellidos(String soloApellidos) {
		this.soloApellidos = soloApellidos;
	}


	/**
	 * Obtiene el valor del atributo ciudadResidenciaPaciente
	 *
	 * @return Retorna atributo ciudadResidenciaPaciente
	 */
	public Ciudades getCiudadResidenciaPersona()
	{
		return ciudadResidenciaPersona;
	}

	/**
	 * Establece el valor del atributo ciudadResidencia
	 *
	 * @param valor para el atributo ciudadResidenciaPaciente
	 */
	public void setCiudadResidenciaPersona(Ciudades ciudadResidenciaPaciente)
	{
		this.ciudadResidenciaPersona = ciudadResidenciaPaciente;
	}

	/**
	 * Obtiene el valor del atributo codigoDepartamentoNacimiento
	 *
	 * @return Retorna atributo codigoDepartamentoNacimiento
	 */
	public String getCodigoDepartamentoNacimiento()
	{
		return codigoDepartamentoNacimiento;
	}

	/**
	 * Establece el valor del atributo codigoDepartamentoNacimiento
	 *
	 * @param valor para el atributo codigoDepartamentoNacimiento
	 */
	public void setCodigoDepartamentoNacimiento(String codigoDepartamentoNacimiento)
	{
		this.codigoDepartamentoNacimiento = codigoDepartamentoNacimiento;
	}

	/**
	 * M&eacute;todo que establece el valor del atributo email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * M&eacute;todo que Obtiene el valor del atributo email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCompletoPersona
	 * @return retorna la variable nombreCompletoPersona 
	 * @author Yennifer Guerrero 
	 */
	public String getNombreCompletoPersona() {
		return nombreCompletoPersona;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCompletoPersona
	 * @param valor para el atributo nombreCompletoPersona 
	 * @author Yennifer Guerrero
	 */
	public void setNombreCompletoPersona(String nombreCompletoPersona) {
		this.nombreCompletoPersona = nombreCompletoPersona;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaNacimientoPersona
	
	 * @return retorna la variable fechaNacimientoPersona 
	 * @author Yennifer Guerrero 
	 */
	public Date getFechaNacimientoPersona() {
		return fechaNacimientoPersona;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaNacimientoPersona
	
	 * @param valor para el atributo fechaNacimientoPersona 
	 * @author Yennifer Guerrero
	 */
	public void setFechaNacimientoPersona(Date fechaNacimientoPersona) {
		
		this.fechaNacimientoPersona = fechaNacimientoPersona;
		this.fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(this.fechaNacimientoPersona);
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo telefonoFijo
	
	 * @return retorna la variable telefonoFijo 
	 * @author Yennifer Guerrero 
	 */
	public String getTelefonoFijo() {
		String telefono=String.valueOf(telefonoFijo);
			if(UtilidadTexto.isEmpty(telefono))
				telefono="";	
			return telefono;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo telefonoFijo
	
	 * @param valor para el atributo telefonoFijo 
	 * @author Yennifer Guerrero
	 */
	public void setTelefonoFijo(Integer telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoCiudadDptoPaisRes
	
	 * @return retorna la variable codigoCiudadDptoPaisRes 
	 * @author Yennifer Guerrero 
	 */
	public String getCodigoCiudadDptoPaisRes() {
		return codigoCiudadDptoPaisRes;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoCiudadDptoPaisRes
	
	 * @param valor para el atributo codigoCiudadDptoPaisRes 
	 * @author Yennifer Guerrero
	 */
	public void setCodigoCiudadDptoPaisRes(String codigoCiudadDptoPaisRes) {
		this.codigoCiudadDptoPaisRes = codigoCiudadDptoPaisRes;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCiudadRes
	
	 * @return retorna la variable nombreCiudadRes 
	 * @author Yennifer Guerrero 
	 */
	public String getNombreCiudadRes() {
		return nombreCiudadRes;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCiudadRes
	
	 * @param valor para el atributo nombreCiudadRes 
	 * @author Yennifer Guerrero
	 */
	public void setNombreCiudadRes(String nombreCiudadRes) {
		this.nombreCiudadRes = nombreCiudadRes;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreDptoRes
	
	 * @return retorna la variable nombreDptoRes 
	 * @author Yennifer Guerrero 
	 */
	public String getNombreDptoRes() {
		return nombreDptoRes;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreDptoRes
	
	 * @param valor para el atributo nombreDptoRes 
	 * @author Yennifer Guerrero
	 */
	public void setNombreDptoRes(String nombreDptoRes) {
		this.nombreDptoRes = nombreDptoRes;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePaisRes
	
	 * @return retorna la variable nombrePaisRes 
	 * @author Yennifer Guerrero 
	 */
	public String getNombrePaisRes() {
		return nombrePaisRes;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePaisRes
	
	 * @param valor para el atributo nombrePaisRes 
	 * @author Yennifer Guerrero
	 */
	public void setNombrePaisRes(String nombrePaisRes) {
		this.nombrePaisRes = nombrePaisRes;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo observaciones
	
	 * @return retorna la variable observaciones 
	 * @author Yennifer Guerrero 
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo observaciones
	
	 * @param valor para el atributo observaciones 
	 * @author Yennifer Guerrero
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * Este Método se encarga de obtener la razón social según
	 * el esquema :  tipo y número de identificación, los apellidos y nombres del paciente.
	 * 
	 * @return retorna la razon social 
	 */
	public String getRazonSocial() {
	
		StringBuilder razonSocial = new StringBuilder();
		razonSocial.append(this.getTipoIdentificacion()).append(" ");
		razonSocial.append(this.getNumeroIdentificacion()).append(" - ");
		razonSocial.append(this.getPrimerApellido()).append(" ");
		razonSocial.append(this.getSegundoApellido()).append(" ");
		razonSocial.append(this.getPrimerNombre()).append(" ");
		razonSocial.append(this.getSegundoNombre()).append(" ");		
		
		return  razonSocial.toString();
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionCentroAtencionDuenio
	
	 * @return retorna la variable descripcionCentroAtencionDuenio 
	 * @author Yennifer Guerrero 
	 */
	public String getDescripcionCentroAtencionDuenio() {
		return descripcionCentroAtencionDuenio;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionCentroAtencionDuenio
	
	 * @param valor para el atributo descripcionCentroAtencionDuenio 
	 * @author Yennifer Guerrero
	 */
	public void setDescripcionCentroAtencionDuenio(
			String descripcionCentroAtencionDuenio) {
		
		if (descripcionCentroAtencionDuenio == null) {
			this.descripcionCentroAtencionDuenio = "";
		}else{
			this.descripcionCentroAtencionDuenio = descripcionCentroAtencionDuenio;
		}
	}
	
	

	/**
	 * Establece el valor del atributo codigoDepartamentoNacimiento
	 *
	 * @param valor para el atributo codigoDepartamentoNacimiento
	 */
	public void setNumeroRegistro(int numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * @return valor de centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion el centroAtencion para asignar
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return valor de numeroRegistro
	 */
	public int getNumeroRegistro() {
		return numeroRegistro;
	}
}

