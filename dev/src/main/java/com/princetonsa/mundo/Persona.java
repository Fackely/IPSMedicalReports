/*
 * @(#)Persona.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo; 

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import util.ConstantesBD;
import util.Encoder;
import util.RespuestaInsercionPersona;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PersonaDao;
import org.apache.log4j.Logger;
/**
 * La clase abstracta persona define los atributos y operaciones comunes a las clases Paciente, Médico y Usuario.
 * debe ser extendida por alguna de dichas clases.
 *
 * @version 1.3, Sep 29, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public abstract class Persona {

	private static Logger logger = Logger.getLogger(Persona.class);

	/**
	 * Código de la persona en el sistema
	 */
	private int codigoPersona=-1;
	
	
	

	/**
	 * Numero de identificacion de la persona.
	 */
	private String numeroIdentificacion = "";
	
	/**
	 * Pais de identificacion de la persona
	 */
	private String paisId = "";
	
	/**
	 * Código del pais de identificacion de la persona
	 */
	private String codigoPaisId = "";
	
	/**
	 * Ciudad de identificacion de la persona
	 */
	private String ciudadId = "";
	
	/**
	 * Codigo de la ciudad de identificacion de la persona
	 */
	private String codigoCiudadId = "";
	
	/**
	 * Departamento de identificacion de la persona
	 */
	private String departamentoId = "";
	
	/**
	 * Código departamento de identificacion de la persona
	 */
	private String codigoDepartamentoId = "";
	
	/**
	 * Pais de nacimiento de la persona
	 */
	private String paisIdentificacion;
	
	/**
	 * Código del pais de nacimiento de la persona
	 */
	private String codigoPaisIdentificacion;
	
	/**
	 * Ciudad de nacimiento de la persona.
	 */
	private String ciudadIdentificacion = "";

	/**
	 * Codigo de la ciudad de nacimiento de la persona.
	 */
	private String codigoCiudadIdentificacion = "";

	/**
	 * Departamento de nacimiento de la persona.
	 */
	private String departamentoIdentificacion = "";

	/**
	 * Codigo del departamento de nacimiento de la persona.
	 */
	private String codigoDepartamentoIdentificacion = "";

	/**
	 * Codigo del tipo de identificacion de la persona.
	 */
	private String codigoTipoIdentificacion = "";

	/**
	 * Nombre del tipo de identificacion de la persona.
	 */
	private String tipoIdentificacion = "";

	/**
	 * Codigo del Tipo de persona (1, 2 ...)
	 */
	private String codigoTipoPersona = "";

	/**
	 * Texto del Tipo de persona (Natural, Juridica...)
	 */
	private String tipoPersona = "";

	/**
	 * Dia de nacimiento de la persona.
	 */
	private String diaNacimiento = "";

	/**
	 * Mes de nacimiento de la persona.
	 */
	private String mesNacimiento = "";

	/**
	 * Año de nacimiento de la persona.
	 */
	private String anioNacimiento = "";

	/**
	 * Edad de la persona, en años.
	 */
	private int edad = -1;

	/**
	 * Codigo del Estado civil de la persona.
	 */
	private String codigoEstadoCivil = "";

	/**
	 * Texto del Estado civil de la persona.
	 */
	private String estadoCivil = "";

	/**
	 * Codigo del sexo de la persona.
	 */
	private String codigoSexo = "";

	/**
	 * Texto del sexo de la persona.
	 */
	private String sexo = "";

	/**
	 * Primer nombre de la persona.
	 */
	private String primerNombrePersona = "";

	/**
	 * Segundo nombre de la persona.
	 */
	private String segundoNombrePersona = "";

	/**
	 * Primer apellido de la persona.
	 */
	private String primerApellidoPersona = "";

	/**
	 * Segundo apellido de la persona.
	 */
	private String segundoApellidoPersona = "";

	/**
	 * Barrio de residencia de la persona.
	 */
	private String barrio = "";

	/**
	 * Codigo del barrio de residencia de la persona.
	 */
	private String codigoBarrio = "";
	
	/**
	 * Localidad de residencia de la persona
	 */
	private String localidad = "";
	
	/**
	 * Código de localidad de residencia de la persona
	 */
	private String codigoLocalidad = "";

	/**
	 * Direccion de residencia de la persona.
	 */
	private String direccion = "";
	
	/**
	 * Código del pais de residencia de la persona
	 */
	private String codigoPais = "";
	
	/**
	 * Nombre del pais de residencia
	 */
	private String pais;

	/**
	 * Codigo de la Ciudad de residencia de la persona.
	 */
	private String codigoCiudad = "";

	/**
	 * Codigo del departamento donde reside de la persona.
	 */
	private String codigoDepartamento = "";
	

	/**
	 * Nombre de la Ciudad/Departamento de residencia de la persona.
	 */
	private String ciudad = "";

	/**
	 * Nombre del Departamento de residencia de la persona.
	 */
	private String departamento="";

	/**
	 * Telefono de la persona.
	 */
	private String telefono = "";
	
	/**
	 * Telefono Fijo 
	 */
	private String telefonoFijo = "";
	
	/**
	 * Teléfono celular de la persona
	 */
	private String telefonoCelular = "";

	/**
	 * Correo electronico de la persona.
	 */
	private String email = "";
	
	
	/**
	 * variable para el Centro de Atención
	 */
	private int centro_atencion = 0;
	
	/**
	 * Variable para el nombre del centro de atencion
	 */
	private String nombreCentroAtencion ="";

	/**
	 * variable para la Etnia
	 */
	private int etnia = 0;
	
	/**
	 * Variable para el nombre de la etnia
	 */
	private String nombreEtnia = "";
	
	/**
	 * variable para la lee_escribe
	 */
	private Boolean lee_escribe = true;
	
	/**
	 * variable para el estudio
	 */
	private int estudio = 0;
	
	/**
	 * Variable para el nombre del estudio
	 */
	private String nombreEstudio = "";
	
	/**
	 * Libreta militar
	 */
	private String libretaMilitar = "";
	
	//Agregado por anexo 958
	/**
	 * Este indicativo lo inserta por debajo la interfaz de sonria.
	 */
	private String indicativoInterfaz="";
	
	/**
	 * Este determina el contrato a parametrizar a una persona que haya sido parametrizada por itnerfaz sonria con indicativoInterfaz=S 
	 */
	private String contratoInterfaz="";
	
	//Fin anexo 958
	
	private String tipoidenAcronimo;
	

	
	/**
	 * Retorna el barrio de residencia de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el barrio donde vive la persona
	 */
	public String getBarrio(boolean encoded) {
		if (encoded) {
			return getBarrio();
		}
		else {
			return barrio;
		}
	}

	/**
	 * Retorna el barrio de residencia de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el barrio donde vive la persona
	 */
	public String getBarrio() {
		return Encoder.encode(barrio);
	}

	/**
	 * Retorna el codigo del barrio de residencia de la persona.
	 * @return el barrio donde vive la persona
	 */
	public String getCodigoBarrio() {
		return codigoBarrio;
	}

	/**
	 * Retorna el codigo de la  ciudad de residencia de la persona.
	 * @return codigo ciudad en la que reside la persona
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * Retorna el codigo del departamento de residencia de la persona.
	 * @return codigo departamento en el que reside la persona
	 */
	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	/**
	 * Retorna la ciudad de residencia de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return la ciudad de residencia de la persona
	 */
	public String getCiudad(boolean encoded) {
		if (encoded) {
			return getCiudad();
		}
		else {
			return ciudad;
		}
	}

	/**
	 * Retorna la ciudad de residencia de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return la ciudad de residencia de la persona
	 */
	public String getCiudad() {
		return Encoder.encode(ciudad);
	}

	/**
	 * Retorna el codigo de la ciudad donde fue expedida la identificacion de la persona.
	 * @return codigo de la ciudad en la que fue expedida la identificacion de la persona
	 */
	public String getCodigoCiudadIdentificacion() {
		return codigoCiudadIdentificacion;
	}

	/**
	 * Retorna la ciudad de expedicion de la identificacion de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return la ciudad donde fue expedida la identificacion de la persona
	 */
	public String getCiudadIdentificacion(boolean encoded) {
		if (encoded) {
			return getCiudadIdentificacion();
		}
		else {
			return ciudadIdentificacion;
		}
	}

	/**
	 * Retorna la ciudad de expedicion de la identificacion de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return la ciudad donde fue expedida la identificacion de la persona
	 */
	public String getCiudadIdentificacion() {
		return Encoder.encode(ciudadIdentificacion);
	}

	/**
	 * Retorna el codigo de la departamento donde fue expedida la identificacion de la persona.
	 * @return codigo de la departamento en la que fue expedida la identificacion de la persona
	 */
	public String getCodigoDepartamentoIdentificacion() {
		return codigoDepartamentoIdentificacion;
	}

	/**
	 * Retorna la departamento de expedicion de la identificacion de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return la departamento donde fue expedida la identificacion de la persona
	 */
	public String getDepartamentoIdentificacion(boolean encoded) {
		if (encoded) {
			return getDepartamentoIdentificacion();
		}
		else {
			return departamentoIdentificacion;
		}
	}

	/**
	 * Retorna la departamento de expedicion de la identificacion de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return la departamento donde fue expedida la identificacion de la persona
	 */
	public String getDepartamentoIdentificacion() {
		return Encoder.encode(departamentoIdentificacion);
	}

	/**
	 * Retorna la direccion de residencia de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return direccion donde vive la persona
	 */
	public String getDireccion(boolean encoded) {
		if (encoded) {
			return getDireccion();
		}
		else {
			return direccion;
		}
	}

	/**
	 * Retorna la direccion de residencia de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return direccion donde vive la persona
	 */
	public String getDireccion() {
		return Encoder.encode(direccion);
	}

	/**
	 * Retorna la edad de la persona.
	 * @return edad de la persona
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * Retorna el email de la persona.
	 * @return email de la persona
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Retorna el codigo del estado civil de la persona.
	 * @return estado civil de la persona
	 */
	public String getCodigoEstadoCivil() {
		return codigoEstadoCivil;
	}

	/**
	 * Retorna el estado civil de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el estado civil de la persona
	 */
	public String getEstadoCivil(boolean encoded) {
		if (encoded) {
			return getEstadoCivil();
		}
		else {
			return estadoCivil;
		}
	}

	/**
	 * Retorna el estado civil de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el estado civil de la persona
	 */
	public String getEstadoCivil() {
		return Encoder.encode(estadoCivil);
	}

	/**
	 * Retorna el dia de nacimiento de la persona.
	 * @return dia de nacimiento de la persona
	 */
	public String getDiaNacimiento() {
		return diaNacimiento;
	}

	/**
	 * Retorna el mes de nacimiento de la persona.
	 * @return mes de nacimiento de la persona
	 */
	public String getMesNacimiento() {
		return mesNacimiento;
	}

	/**
	 * Retorna el año de nacimiento de la persona.
	 * @return año de nacimiento de la persona
	 */
	public String getAnioNacimiento() {
		return anioNacimiento;
	}

	/**
	 * Retorna el numero de identificacion de la persona.
	 * @return numero de identificacion de la persona
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	* Retorna los nombres de la persona
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	* @return Los nombres de la persona
	*/
	public String getNombres(){
		return Encoder.encode(primerNombrePersona+" "+segundoNombrePersona);
	}

	/**
	* Retorna lLos nombres de la persona
	* @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	* de HTML (e.g., "á" como "&amp;aacute;"
	* @return Los nombres de la persona
	*/
	public String getNombres(boolean encoded){
		if (encoded) {
			return getNombres();
		}
		else {
			return primerNombrePersona+" "+segundoNombrePersona;
		}
	}

	/**
	* Retorna los apellidos de la persona
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	* @return Los apellidos de la persona
	*/
	public String getApellidos(){
		return Encoder.encode(primerApellidoPersona+" "+segundoApellidoPersona);
	}

	/**
	* Retorna Los apellidos de la persona
	* @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	* de HTML (e.g., "á" como "&amp;aacute;"
	* @return Los apellidos de la persona
	*/
	public String getApellidos(boolean encoded){
		if (encoded) {
			return getApellidos();
		}
		else {
			return primerApellidoPersona+" "+segundoApellidoPersona;
		}
	}

	/**
	 * Retorna el primer apellido de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el primer apellido de la persona
	 */
	public String getPrimerApellidoPersona(boolean encoded) {
		if (encoded) {
			return getPrimerApellidoPersona();
		}
		else {
			return primerApellidoPersona;
		}
	}

	/**
	 * Retorna el primer apellido de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el primer apellido de la persona
	 */
	public String getPrimerApellidoPersona() {
		return Encoder.encode(primerApellidoPersona);
	}

	/**
	 * Retorna el primer nombre de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el primer nombre de la persona
	 */
	public String getPrimerNombrePersona(boolean encoded) {
		if (encoded) {
			return getPrimerNombrePersona();
		}
		else {
			return primerNombrePersona;
		}
	}
	
	
	/**
	 * Retorna el primer nombre de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el primer nombre de la persona
	 */
	public String getPrimerNombrePersona() {
		//return Encoder.encode(primerNombrePersona);
		return primerNombrePersona;
	}

	/**
	 * Retorna el segundo apellido de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el segundo apellido de la persona
	 */
	public String getSegundoApellidoPersona(boolean encoded) {
		if (encoded) {
			return getSegundoApellidoPersona();
		}
		else {
			return segundoApellidoPersona;
		}
	}

	/**
	 * Retorna el segundo apellido de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el segundo apellido de la persona
	 */
	public String getSegundoApellidoPersona() {
		return Encoder.encode(segundoApellidoPersona);
	}

	/**
	 * Retorna el segundo nombre de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el segundo nombre de la persona
	 */
	public String getSegundoNombrePersona(boolean encoded) {
		if (encoded) {
			return getSegundoNombrePersona();
		}
		else {
			return segundoNombrePersona;
		}
	}

	/**
	 * Retorna el segundo nombre de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el segundo nombre de la persona
	 */
	public String getSegundoNombrePersona() {
		return Encoder.encode(segundoNombrePersona);
	}

	/**
	 * Retorna el codigo del sexo de la persona.
	 * @return codigo del sexo de la persona
	 */
	public String getCodigoSexo() {
		return codigoSexo;
	}

	/**
	 * Retorna el texto  del sexo de la persona.
	 * @return texto del sexo de la persona
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * Retorna el telefono de la persona.
	 * @return telefono de la persona
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Retorna codigo del tipo de identificacion de la persona.
	 * @return codigo del tipo de identificacion de la persona
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * Retorna el tipo de identificacion de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el tipo de identificacion de la persona
	 */
	public String getTipoIdentificacion(boolean encoded) {
		if (encoded) {
			return getTipoIdentificacion();
		}
		else {
			return tipoIdentificacion;
		}
	}

	/**
	 * Retorna el tipo de identificacion de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;").
	 * @return el tipo de identificacion de la persona
	 */
	public String getTipoIdentificacion() {
		return Encoder.encode(tipoIdentificacion);
	}

	/**
	 * Retorna el codigo del tipo de persona.
	 * @return codigo del tipo de persona (1 o 2)
	 */
	public String getCodigoTipoPersona() {
		return codigoTipoPersona;
	}

	/**
	 * Retorna el tipo de persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el tipo de persona
	 */
	public String getTipoPersona(boolean encoded) {
		if (encoded) {
			return getTipoPersona();
		}
		else {
			return tipoPersona;
		}
	}

	/**
	 * Retorna el tipo de persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el tipo de persona
	 */
	public String getTipoPersona() {
		return Encoder.encode(tipoPersona);
	}

	/**
	 * Establece el codigo de la ciudad.
	 * @param codigoCiudad el codigo de la ciudad que va a ser establecido
	 */
	public void setCodigoCiudad(String codigoCiudad) 
	{
		String[] vector = codigoCiudad.split(ConstantesBD.separadorSplit);
		if(vector.length>1)
		{
			this.codigoCiudad = vector[1];
			this.codigoDepartamento = vector[0];
		}
		else
			this.codigoCiudad = codigoCiudad;
	}

	/**
	 * Establece el codigo de la ciudad de expedicion de la identificacion.
	 * @param codigoCiudadIdentificacion el codigo de la ciudad de identificacion que va a ser establecido
	 */
	public void setCodigoCiudadIdentificacion(String codigoCiudadIdentificacion) 
	{
		String[] vector = codigoCiudadIdentificacion.split(ConstantesBD.separadorSplit);
		if(vector.length>1)
		{
			this.codigoCiudadIdentificacion = vector[1];
			this.codigoDepartamentoIdentificacion = vector[0];
		}
		else
			this.codigoCiudadIdentificacion = codigoCiudadIdentificacion;
	}

	/**
	 * Establece el codigo del departamento.
	 * @param codigoDepartamento el codigo del departamento que va a ser establecido
	 */
	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	/**
	 * Establece el codigo del estado civil.
	 * @param codigoEstadoCivil el codigo del estado civil que va a ser establecido
	 */
	public void setCodigoEstadoCivil(String codigoEstadoCivil) {
		this.codigoEstadoCivil = codigoEstadoCivil;
	}

	/**
	 * Establece el codigo del sexo.
	 * @param codigoSexo el codigo del sexo que va a ser establecido
	 */
	public void setCodigoSexo(String codigoSexo) {
		this.codigoSexo = codigoSexo;
	}

	/**
	 * Establece el codigo del tipo de identificacion.
	 * @param codigoTipoIdentificacion el codigo del tipo de identificacion que va a ser establecido
	 */
	public void setCodigoTipoIdentificacion(String codigoTipoIdentificacion) {
		this.codigoTipoIdentificacion = codigoTipoIdentificacion;
	}

	/**
	 * Establece el codigoTipoPersona.
	 * @param codigoTipoPersona el codigo del tipo de persona que va a ser establecido
	 */
	public void setCodigoTipoPersona(String codigoTipoPersona) {
		this.codigoTipoPersona = codigoTipoPersona;
	}

	/**
	 * Establece el texto de la ciudad.
	 * @param ciudad el texto de la ciudad que va a ser establecida
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * Establece el texto de la ciudad de expedicion de la identificacion.
	 * @param ciudadIdentificacion el texto de la ciudad de expedicion de la identificacion que va a ser establecido
	 */
	public void setCiudadIdentificacion(String textoCiudadIdentificacion) {
		this.ciudadIdentificacion = textoCiudadIdentificacion;
	}

	/**
	 * Establece el texto del estado civil.
	 * @param estadoCivil el texto del estado civil que va a ser establecido
	 */
	public void setEstadoCivil(String textoEstadoCivil) {
		this.estadoCivil = textoEstadoCivil;
	}

	/**
	 * Establece el texto del sexo.
	 * @param sexo el texto del sexo que va a ser establecido
	 */
	public void setSexo(String textoSexo) {
		this.sexo = textoSexo;
	}

	/**
	 * Establece el texto del tipo de identificacion.
	 * @param tipoIdentificacion el texto del tipo de identificacion que va a ser establecido
	 */
	public void setTipoIdentificacion(String textoTipoIdentificacion) {
		this.tipoIdentificacion = textoTipoIdentificacion;
	}

	/**
	 * Establece el texto del tipo de persona.
	 * @param tipoPersona el texto del tipo de persona que va a ser establecido
	 */
	public void setTipoPersona(String textoTipoPersona) {
		this.tipoPersona = textoTipoPersona;
	}

	/**
	 * Establece el barrio de residencia de la persona.
	 * @param barrio el barrio que va a ser establecido
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	/**
	 * Establece el codigo del barrio de residencia de la persona.
	 * @param codigoBarrio el barrio que va a ser establecido
	 */
	public void setCodigoBarrio(String codigoBarrio) {
		this.codigoBarrio = codigoBarrio;
	}

	/**
	 * Establece el departamento que aparece en el documento de identifiaciacion de la persona.
	 * @param departamentoIdentificacion el departamento en la Identificacion que va a ser establecido
	 */
	public void setDepartamentoIdentificacion(String departamentoIdentificacion) {
		this.departamentoIdentificacion = departamentoIdentificacion;
	}

	/**
	 * Establece el codigo del departamento que aparece en el documento de identificacion de la persona.
	 * @param codigoDepartamentoIdentificacion el codigo del Departamento de la Identificacion que va a ser establecido
	 */
	public void setCodigoDepartamentoIdentificacion(String codigoDepartamentoIdentificacion) {
		this.codigoDepartamentoIdentificacion = codigoDepartamentoIdentificacion;
	}

	/**
	 * Establece la direccion de residencia de la persona.
	 * @param direccion la direccion que va a ser establecida
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Establece la edad de la persona.
	 * @param edad la edad que va a ser establecida
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}

	/**
	 * Establece el email de la persona.
	 * @param email el email que va a ser establecido
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Establece el dia de nacimiento de la persona.
	 * @param diaNacimiento el dia de nacimiento que va a ser establecido
	 */
	public void setDiaNacimiento(String diaNacimiento) {
		this.diaNacimiento = diaNacimiento;
		// Si ya tengo todos los datos (dia, mes y año de nacimiento), calculo la edad de la persona
		if ( ((this.diaNacimiento).compareTo("") != 0) && ((this.mesNacimiento).compareTo("") != 0) && ((this.anioNacimiento).compareTo("") != 0)) {
			this.setEdad(calcularEdad(this.diaNacimiento, this.mesNacimiento, this.anioNacimiento));
		}
	}

	/**
	 * Establece el mes de nacimiento de la persona.
	 * @param mesNacimiento el mes de nacimiento que va a ser establecido
	 */
	public void setMesNacimiento(String mesNacimiento) {
		this.mesNacimiento = mesNacimiento;
		// Si ya tengo todos los datos (dia, mes y año de nacimiento), calculo la edad de la persona
		if (  ((this.diaNacimiento).compareTo("") != 0) && ((this.mesNacimiento).compareTo("") != 0) && ((this.anioNacimiento).compareTo("") != 0)) {
			this.setEdad(calcularEdad(this.diaNacimiento, this.mesNacimiento, this.anioNacimiento));
		}
	}

	/**
	 * Establece el año de nacimiento de la persona.
	 * @param anioNacimiento el año de nacimiento que va a ser establecido
	 */
	public void setAnioNacimiento(String anioNacimiento) {
		this.anioNacimiento = anioNacimiento;
		// Si ya tengo todos los datos (dia, mes y año de nacimiento), calculo la edad de la persona
		if (  ((this.diaNacimiento).compareTo("") != 0) && ((this.mesNacimiento).compareTo("") != 0) && ((this.anioNacimiento).compareTo("") != 0)) {
			this.setEdad(calcularEdad(this.diaNacimiento, this.mesNacimiento, this.anioNacimiento));
		}
	}

	/**
	 * Establece el numero de identificacion de la persona.
	 * @param numeroIdentificacion el numero de identificacion que va a ser establecido
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		if (numeroIdentificacion!=null)
		{
			numeroIdentificacion=numeroIdentificacion.trim();
		}
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Establece el primer apellido de la persona.
	 * @param primerApellidoPersona el primer apellido de la persona que va a ser establecido
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona) {
		this.primerApellidoPersona = primerApellidoPersona;
	}

	/**
	 * Establece el primer nombre de la persona.
	 * @param primerNombrePersona el primer nombre de la persona que va a ser establecido
	 */
	public void setPrimerNombrePersona(String primerNombrePersona) {
		this.primerNombrePersona = primerNombrePersona;
	}

	/**
	 * Establece el segundo apellido de la persona.
	 * @param segundoApellidoPersona el segundo apellido de la persona que va a ser establecido
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona) {
		this.segundoApellidoPersona = segundoApellidoPersona;
	}

	/**
	 * Establece el segundo nombre de la persona.
	 * @param segundoNombrePersona el segundo nombre de la persona que va a ser establecido
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona) {
		this.segundoNombrePersona = segundoNombrePersona;
	}

	/**
	 * Establece el telefono de la persona.
	 * @param telefono el telefono de la persona que va a ser establecido
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos de la persona.
	 */
	public void clean ()
	{
		this.codigoPersona=-1;
		this.setNumeroIdentificacion("");
		this.setPaisId("");
		this.setCodigoPaisId("");
		this.setCiudadId("");
		this.setCodigoCiudadId("");
		this.setCodigoDepartamentoId("");
		this.setDepartamentoId("");
		this.setPaisIdentificacion("");
		this.setCodigoPaisIdentificacion("");
		this.setCiudadIdentificacion("");
		this.setCodigoCiudadIdentificacion("");
		this.setCodigoTipoIdentificacion("");
		this.setTipoIdentificacion("");
		this.setCodigoTipoPersona("");
		this.setTipoPersona("");
		this.setDiaNacimiento("");
		this.setMesNacimiento("");
		this.setAnioNacimiento("");
		this.setEdad(-1);
		this.setCodigoEstadoCivil("");
		this.setEstadoCivil("");
		this.setCodigoSexo("");
		this.setSexo("");
		this.setPrimerNombrePersona("");
		this.setSegundoNombrePersona("");
		this.setPrimerApellidoPersona("");
		this.setSegundoApellidoPersona("");
		this.setBarrio("");
		this.setDireccion("");
		this.setCodigoPais("");
		this.setPais("");
		this.setCodigoCiudad("");
		this.setCodigoDepartamento("");
		this.setCiudad("");
		this.setLocalidad("");
		this.setCodigoLocalidad("");
		this.setTelefono("");
		this.setEmail("");
		this.setNombreCentroAtencion("");
		this.setNombreEstudio("");
		this.setNombreEtnia("");
		this.setTelefonoCelular("");
		this.setIndicativoInterfaz("");
		this.setContratoInterfaz("");
		this.setTelefonoFijo("");
		this.setTipoidenAcronimo("");
		

	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos de la persona, pero no
	 * limpia el valor del barrio.
	 */
	public void clean2 () {

		this.codigoPersona=-1;
		this.setNumeroIdentificacion("");
		this.setCiudadIdentificacion("");
		this.setCiudadId("");
		this.setCodigoCiudadId("");
		this.setCodigoDepartamentoId("");
		this.setDepartamentoId("");
		this.setCodigoCiudadIdentificacion("");
		this.setCodigoTipoIdentificacion("");
		this.setTipoIdentificacion("");
		this.setCodigoTipoPersona("");
		this.setTipoPersona("");
		this.setDiaNacimiento("");
		this.setMesNacimiento("");
		this.setAnioNacimiento("");
		this.setEdad(-1);
		this.setCodigoEstadoCivil("");
		this.setEstadoCivil("");
		this.setCodigoSexo("");
		this.setSexo("");
		this.setPrimerNombrePersona("");
		this.setSegundoNombrePersona("");
		this.setPrimerApellidoPersona("");
		this.setSegundoApellidoPersona("");
		this.setDireccion("");
		this.setCodigoCiudad("");
		this.setCodigoDepartamento("");
		this.setCiudad("");
		this.setCodigoLocalidad("");
		this.setTelefono("");
		this.setEmail("");
		this.setNombreCentroAtencion("");
		this.setNombreEstudio("");
		this.setNombreEtnia("");
		this.setTelefonoCelular("");
		this.setIndicativoInterfaz("");
		this.setContratoInterfaz("");
		this.setTelefonoFijo("");

	}

	/**
	 * Inicializa todas las variables de la persona que puedan venir en la forma "codigo-nombre".
	 */
	public void inicializarVariables() {

		String [] resultados;

		
		//Ahora sacamos el tipo de identificacion
		resultados=UtilidadTexto.separarNombresDeCodigos(tipoIdentificacion, 1);
		codigoTipoIdentificacion = (resultados[0].equals("")) ? codigoTipoIdentificacion : resultados[0] ;
		tipoIdentificacion = (resultados[1].equals("")) ? tipoIdentificacion : resultados[1];

		//Ahora el tipo de persona
		resultados=UtilidadTexto.separarNombresDeCodigos(tipoPersona, 1);
		codigoTipoPersona=resultados[0];
		tipoPersona=resultados[1];

		//Ahora el estado civil
		if(!UtilidadTexto.isEmpty(estadoCivil)&&estadoCivil.indexOf("-")>0)
		{
			resultados=UtilidadTexto.separarNombresDeCodigos(estadoCivil, 1);
			codigoEstadoCivil=resultados[0];
			estadoCivil=resultados[1];
		}

		//Ahora el sexo
		resultados=UtilidadTexto.separarNombresDeCodigos(sexo, 1);
		codigoSexo=resultados[0];
		sexo=resultados[1];

		

	}

	/**
	 * Dados el dia, mes y año de nacimiento de una persona, calcula su edad actual, en años.
	 * @param diaNacimiento cadena de texto con el dia en el cual nacio la persona
	 * @param mesNacimiento cadena de texto con el mes en el cual nacio la persona
	 * @param anioNacimiento cadena de texto con el año en el cual nacio la persona
	 * @return la edad de la persona, en años
	 */
	public static int calcularEdad (String diaNacimiento, String mesNacimiento, String anioNacimiento)
	{

		// Fecha actual
		GregorianCalendar fechaActual = new GregorianCalendar();
		int diaAct  = fechaActual.get(Calendar.DAY_OF_MONTH);
		int mesAct  = fechaActual.get(Calendar.MONTH) + 1; // Los meses en Java empiezan en 0 ...
		int anioAct = fechaActual.get(Calendar.YEAR);

		return UtilidadFecha.calcularEdad(diaNacimiento, mesNacimiento, anioNacimiento, diaAct, mesAct, anioAct);

	}

	/**
	 * Dados el dia, mes y año de nacimiento de una persona, calcula su edad actual,
	 * con las siguientes consideraciones :
	 * edad &lt; 1 mes : edad en días
	 * edad &lt; 1 año : edad en meses y días
	 * 1 año &lt;= edad &lt;= 5 años : edad en años y meses
	 * edad &gt; 5 años : edad en años
	 * @param anioNacimiento año en el cual nació la persona, como una cadena de texto
	 * @param mesNacimiento mes en el cual nació la persona, como una cadena de texto
	 * @param diaNacimiento día en el cual nació la persona, como una cadena de texto
	 * @return la edad de la persona, como se explicó arriba
	 */
	public static String calcularEdadDetallada(String anioNacimiento, String mesNacimiento, String diaNacimiento) {

		int diaNac=0, mesNac=0, anioNac=0;

		try {
			// Fecha de nacimiento de la persona
			diaNac  = Integer.parseInt(diaNacimiento.trim());
			mesNac  = Integer.parseInt(mesNacimiento.trim());
			anioNac = Integer.parseInt(anioNacimiento.trim());
		}
		catch (NumberFormatException nfe) {
			System.err.println("Error : dia/mes/año de nacimiento no son un número");
			nfe.printStackTrace();
		}

		return calcularEdadDetallada(anioNac, mesNac, diaNac);

	}
	public static String calcularEdadDetalladaAFecha_yMd(String anioNacimiento, String mesNacimiento, String diaNacimiento, String fecha)
	{
		int diaNac=0, mesNac=0, anioNac=0;

		try {
			// Fecha de nacimiento de la persona
			diaNac  = Integer.parseInt(diaNacimiento.trim());
			mesNac  = Integer.parseInt(mesNacimiento.trim());
			anioNac = Integer.parseInt(anioNacimiento.trim());
		}
		catch (NumberFormatException nfe) {
			System.err.println("Error : dia/mes/año de nacimiento no son un número");
			nfe.printStackTrace();
		}

		return calcularEdadDetalladaAFecha_yMd(anioNac, mesNac, diaNac, fecha);
	}

	public static String calcularEdadDetalladaAFecha_dMy(String anioNacimiento, String mesNacimiento, String diaNacimiento, String fecha)
	{
		int diaNac=0, mesNac=0, anioNac=0;

		try {
			// Fecha de nacimiento de la persona
			diaNac  = Integer.parseInt(diaNacimiento.trim());
			mesNac  = Integer.parseInt(mesNacimiento.trim());
			anioNac = Integer.parseInt(anioNacimiento.trim());
		}
		catch (NumberFormatException nfe) {
			System.err.println("Error : dia/mes/año de nacimiento no son un número");
			nfe.printStackTrace();
		}

		return calcularEdadDetalladaAFecha_dMy(anioNac, mesNac, diaNac, fecha);
	}
//	Version de calcularEdadDetallada que calcula la edad detallada del paciente hasta la fecha dada por parametro
	public static String calcularEdadDetalladaAFecha_dMy(int anioNacimiento, int mesNac, int diaNacimiento, String fecha)
	{
		if(fecha.trim().equals("")) return "";

		// Fecha actual, corresponde a la fecha dada por parametro

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		Date fechaActual = null;
		try {
			fechaActual = dateFormatter.parse(fecha);

		}	catch (java.text.ParseException e) {
				if (fechaActual!=null)
				{
					logger.warn("Error calculando edad. Fecha de referencia con formato invalido '"+fecha+"', se requiere dd/MM/yyyy y el intento de convertirla quedo " + fechaActual );


				}
				else
				{
					logger.warn("Error calculando edad. Fecha de referencia con formato invalido '"+fecha+"', se requiere dd/MM/yyyy");


				}
				return "";
		}
		return calcularEdadDetalladaAFecha(anioNacimiento, mesNac, diaNacimiento, dateFormatter.getCalendar());
	}
	public static String calcularEdadDetalladaAFecha_yMd(int anioNacimiento, int mesNac, int diaNacimiento, String fecha)
	{
		if(fecha.trim().equals("")) return "";

		// Fecha actual, corresponde a la fecha dada por parametro

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		Date fechaActual = null;
		try {
			fechaActual = dateFormatter.parse(fecha);
		}	catch (java.text.ParseException e) {
			if (fechaActual!=null)
			{
				logger.warn("Error calculando edad. Fecha de referencia con formato invalido '"+fecha+"', se requiere dd/MM/yyyy y el intento de convertirla quedo " + fechaActual );


			}
			else
			{
				logger.warn("Error calculando edad. Fecha de referencia con formato invalido '"+fecha+"', se requiere dd/MM/yyyy");

			}
				return "";

		}
		return calcularEdadDetalladaAFecha(anioNacimiento, mesNac, diaNacimiento, dateFormatter.getCalendar());
	}

		public static String calcularEdadDetalladaAFecha(int anioNacimiento, int mesNac, int diaNacimiento, Calendar fecha)
		{
			int diaActual = fecha.get(Calendar.DAY_OF_MONTH);
			int mesActual = fecha.get(Calendar.MONTH);
			int anioActual = fecha.get(Calendar.YEAR);
			return UtilidadFecha.calcularEdadDetallada(anioNacimiento, mesNac, diaNacimiento, diaActual, mesActual, anioActual);
	}
	/**
	 * Dados el dia, mes y año de nacimiento de una persona, calcula su edad actual,
	 * con las siguientes consideraciones :
	 * edad &lt; 1 mes : edad en días
	 * edad &lt; 1 año : edad en meses y días
	 * 1 año &lt;= edad &lt;= 5 años : edad en años y meses
	 * edad &gt; 5 años : edad en años
	 * @param anioNacimiento año en el cual nació la persona
	 * @param mesNacimiento mes en el cual nació la persona
	 * @param diaNacimiento día en el cual nació la persona
	 * @return la edad de la persona, como se explicó arriba
	 */
	public static String calcularEdadDetallada(int anioNacimiento, int mesNac, int diaNacimiento)
	{
		// Fecha actual
		GregorianCalendar lc_c = new GregorianCalendar();
		return UtilidadFecha.calcularEdadDetallada(anioNacimiento, mesNac, diaNacimiento, lc_c.get(Calendar.DAY_OF_MONTH), lc_c.get(Calendar.MONTH) + 1, lc_c.get(Calendar.YEAR) );
	}

	/**
	 * Inserta una persona en una fuente de datos, reutilizando una conexion existente.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD el tipo de bases de datos utilizada, según las constantes definidas en <code>DaoFactory</code>
	 * @param numeroIdentificacion numero de identificacion de la persona
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion de la persona
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion código del departamento de expedición de la identificación de la persona
	 * @param codigoCiudadIdentificacion código de la ciudad de expedición de la identificación de la persona
	 * @param codigoTipoPersona código del tipo de persona
	 * @param diaNacimiento día de naciemiento de la persona
	 * @param mesNacimiento mes de naciemiento de la persona
	 * @param anioNacimiento año de naciemiento de la persona
	 * @param codigoEstadoCivil código del estado civil de la persona
	 * @param codigoSexo código del sexo de la persona
	 * @param primerNombrePersona primer nombre de la persona
	 * @param segundoNombrePersona segundo nombre de la persona
	 * @param primerApellidoPersona primer apellido de la persona
	 * @param segundoApellidoPersona segundo apellido de la persona
	 * @param direccion dirección de residencia de la persona
	 * @param codigoDepartamento código del departamento de residencia de la persona
	 * @param codigoCiudad código del departamento de residencia de la persona
	 * @param codigoBarrio código del barrio de residencia de la persona
	 * @param telefono teléfono de contacto de la persona
	 * @param email correo electrónico de la persona
	 * @return un objeto <code>RespuestaInsercionPersona</code> con la informacion de la insercion, y los (posibles) mensajes de error
	 */
	public static RespuestaInsercionPersona insertarPersona (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String codigoLocalidad, String telefono, String email,String telefonoCelular, String tipoPersona, int codigoInstitucion, String telefonoFijo) throws SQLException 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		PersonaDao personaDao = myFactory.getPersonaDao();
		
		return personaDao.insertarPersona(con, numeroIdentificacion, codigoTipoIdentificacion,
			codigoDeptoId, codigoCiudadId, codigoPaisId, 
			codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona,
			diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo,
			primerNombrePersona, segundoNombrePersona, primerApellidoPersona,
			segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, codigoLocalidad,
			telefono, email,telefonoCelular, tipoPersona, codigoInstitucion , telefonoFijo
		);

	}

	/**
	 * Inserta una persona en una fuente de datos, reutilizando una conexion existente.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD el tipo de bases de datos utilizada, según las constantes definidas en <code>DaoFactory</code>
	 * @param numeroIdentificacion numero de identificacion de la persona
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion de la persona
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion código del departamento de expedición de la identificación de la persona
	 * @param codigoCiudadIdentificacion código de la ciudad de expedición de la identificación de la persona
	 * @param codigoTipoPersona código del tipo de persona
	 * @param fechaNacimiento día de naciemiento de la persona
	 * @param codigoEstadoCivil código del estado civil de la persona
	 * @param codigoSexo código del sexo de la persona
	 * @param primerNombrePersona primer nombre de la persona
	 * @param segundoNombrePersona segundo nombre de la persona
	 * @param primerApellidoPersona primer apellido de la persona
	 * @param segundoApellidoPersona segundo apellido de la persona
	 * @param direccion dirección de residencia de la persona
	 * @param codigoDepartamento código del departamento de residencia de la persona
	 * @param codigoCiudad código del departamento de residencia de la persona
	 * @param codigoBarrio código del barrio de residencia de la persona
	 * @param telefono teléfono de contacto de la persona
	 * @param email correo electrónico de la persona
	 * @return un objeto <code>RespuestaInsercionPersona</code> con la informacion de la insercion, y los (posibles) mensajes de error
	 */
	public static RespuestaInsercionPersona insertarPersona (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String fechaNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String codigoLocalidad, String telefono, String email,  String tipoPersona, int codigoInstitucion, String telefonoFijo) throws SQLException 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		PersonaDao personaDao = myFactory.getPersonaDao();

		return personaDao.insertarPersona(con, numeroIdentificacion, codigoTipoIdentificacion,
			codigoDeptoId, codigoCiudadId, codigoPaisId, 
			codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona,
			fechaNacimiento, codigoEstadoCivil, codigoSexo,
			primerNombrePersona, segundoNombrePersona, primerApellidoPersona,
			segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, codigoLocalidad,
			telefono, email,"", tipoPersona, codigoInstitucion, telefonoFijo 
		);

	}
	
	public static int modificarPersona(Connection con,
		String codigoTipoIdentificacion,String numeroIdentificacion,String codigoDepartamentoId, String codigoCiudadId,String codigoPaisId,
		int codigoPersona, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion,
		String codigoTipoPersona,String diaNacimiento, String mesNacimiento, String anioNacimiento,String codigoEstadoCivil,String codigoSexo,
		String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento,
		String codigoCiudad, String codigoPais, String codigoBarrio, String codigoLocalidad, String telefono, String email, String telefonoCelular, String estado,
		String tipoPersona, int codigoInstitucion, String telefonoFijo
		)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		PersonaDao personaDao = myFactory.getPersonaDao();
		
		int resp = 0;
		
		try 
		{
			resp = personaDao.modificarPersonaTransaccional(
					con,
					codigoTipoIdentificacion,
					numeroIdentificacion,
					codigoDepartamentoId,
					codigoCiudadId,
					codigoPaisId,
					codigoPersona,
					codigoDepartamentoIdentificacion,
					codigoCiudadIdentificacion,
					codigoPaisIdentificacion,
					codigoTipoPersona,
					diaNacimiento,
					mesNacimiento,
					anioNacimiento,
					codigoEstadoCivil,
					codigoSexo,
					primerNombrePersona,
					segundoNombrePersona,
					primerApellidoPersona,
					segundoApellidoPersona,
					direccion,
					codigoDepartamento,
					codigoCiudad,
					codigoPais,
					codigoBarrio,
					codigoLocalidad,
					telefono,
					email,
					telefonoCelular,
					estado,
					tipoPersona,
					codigoInstitucion,
					telefonoFijo
				);
		} 
		catch (SQLException e) 
		{
			logger.error("Error al modificar la persona: "+e);
		}
		
		return resp;
	}

	/**
	* Modifica una persona en una fuente de datos, permaneciendo dentro de un estado transaccional
	* @param ac_con 	Conexión a la base de datos
	* @param as_estado	Estado de la transacción
	* @return	1	Si la modificaciòn de la persona fue exitoso
	*			0	Error de base de datos
	*			-1	Si la combinación tipo/número de documento ya existe
	*			-2	Si la fecha de nacimiento es posterior a la actual
	*/
	public int modificarPersona(Connection ac_con, String as_estado, String tipoPersona, int codigoInstitucion)throws SQLException
	{
		PersonaDao lpd_pd;
        logger.info(" \n>>Entro al mundo de Persona ");  
		lpd_pd = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao();

		return
			lpd_pd.modificarPersonaTransaccional(
				ac_con,
				getCodigoTipoIdentificacion(),
				getNumeroIdentificacion(),
				getCodigoDepartamentoId(),
				getCodigoCiudadId(),
				getCodigoPaisId(),
				getCodigoPersona(),
				getCodigoDepartamentoIdentificacion(),
				getCodigoCiudadIdentificacion(),
				getCodigoPaisIdentificacion(),
				getCodigoTipoPersona(),
				getDiaNacimiento(),
				getMesNacimiento(),
				getAnioNacimiento(),
				getCodigoEstadoCivil(),
				getCodigoSexo(),
				getPrimerNombrePersona(false),
				getSegundoNombrePersona(false),
				getPrimerApellidoPersona(false),
				getSegundoApellidoPersona(false),
				getDireccion(false),
				getCodigoDepartamento(),
				getCodigoCiudad(),
				getCodigoPais(),
				getCodigoBarrio(),
				getCodigoLocalidad(),
				getTelefono(),
				getEmail(),
				getTelefonoCelular(),
				as_estado,
				tipoPersona,
				codigoInstitucion,
				getTelefonoFijo()
			);
	}
	/**
	 * Adición de Sebastián
	 * Método para obtener el código de una persona que ya está en el sistema
	 * a través de su número y tipo de identificación
	 * @param con
	 * @param numeroId
	 * @param codigoTipoId
	 * @return
	 */
	public static int obtenerCodigoPersona(Connection con,String numeroId, String codigoTipoId){
		PersonaDao lpd_pd;

		lpd_pd = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao();

		return lpd_pd.obtenerCodigoPersona(con,numeroId,codigoTipoId);
	}
	
	/**
	 * Adición de Sebastián
	 * Método para obtener el código de una persona que ya está en el sistema
	 * a través de su número y tipo de identificación
	 * @param con
	 * @param numeroId
	 * @param codigoTipoId
	 * @return
	 */
	public static int obtenerCodigoPersona(String numeroId, String codigoTipoId)
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		Connection con=UtilidadBD.abrirConexion();
		PersonaDao lpd_pd;

		lpd_pd = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao();

		resultado= lpd_pd.obtenerCodigoPersona(con,numeroId,codigoTipoId);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Método para obtener apellidos nombres de una persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerApellidosNombresPersona(Connection con,int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao().obtenerApellidosNombresPersona(con, codigoPersona);
	}
	
	/**
	 * Retorna el departamento donde vive la persona.
	 * @return String con el departamento donde vive la persona
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * Establece el departamento donde vive la persona.
	 * @param departamento El departamento a establecer
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}

	/**
	 * @param i
	 */
	public void setCodigoPersona(int i) {
		codigoPersona = i;
	}

	public int getCentro_atencion() {
		return centro_atencion;
	}

	public void setCentro_atencion(int centro_atencion) {
		this.centro_atencion = centro_atencion;
	}

	public int getEstudio() {
		return estudio;
	}

	public void setEstudio(int estudio) {
		this.estudio = estudio;
	}

	public int getEtnia() {
		return etnia;
	}

	public void setEtnia(int etnia) {
		this.etnia = etnia;
	}

	public Boolean getLee_escribe() {
		return lee_escribe;
	}

	public void setLee_escribe(Boolean lee_escribe) {
		this.lee_escribe = lee_escribe;
	}

	/**
	 * @return Returns the ciudadId.
	 */
	public String getCiudadId() {
		return ciudadId;
	}

	/**
	 * @param ciudadId The ciudadId to set.
	 */
	public void setCiudadId(String ciudadId) {
		this.ciudadId = ciudadId;
	}

	/**
	 * @return Returns the codigoCiudadId.
	 */
	public String getCodigoCiudadId() {
		return codigoCiudadId;
	}

	/**
	 * @param codigoCiudadId The codigoCiudadId to set.
	 */
	public void setCodigoCiudadId(String codigoCiudadId) 
	{
		String[] vector = codigoCiudadId.split(ConstantesBD.separadorSplit);
		if(vector.length>1)
		{
			this.codigoCiudadId = vector[1];
			this.codigoDepartamentoId = vector[0];
		}
		else
			this.codigoCiudadId = codigoCiudadId;
		
	}

	/**
	 * @return Returns the codigoDepartamentoId.
	 */
	public String getCodigoDepartamentoId() {
		return codigoDepartamentoId;
	}

	/**
	 * @param codigoDepartamentoId The codigoDepartamentoId to set.
	 */
	public void setCodigoDepartamentoId(String codigoDepartamentoId) {
		this.codigoDepartamentoId = codigoDepartamentoId;
	}

	/**
	 * @return Returns the departamentoId.
	 */
	public String getDepartamentoId() {
		return departamentoId;
	}

	/**
	 * @param departamentoId The departamentoId to set.
	 */
	public void setDepartamentoId(String departamentoId) {
		this.departamentoId = departamentoId;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the nombreEstudio
	 */
	public String getNombreEstudio() {
		return nombreEstudio;
	}

	/**
	 * @param nombreEstudio the nombreEstudio to set
	 */
	public void setNombreEstudio(String nombreEstudio) {
		this.nombreEstudio = nombreEstudio;
	}

	/**
	 * @return the nombreEtnia
	 */
	public String getNombreEtnia() {
		return nombreEtnia;
	}

	/**
	 * @param nombreEtnia the nombreEtnia to set
	 */
	public void setNombreEtnia(String nombreEtnia) {
		this.nombreEtnia = nombreEtnia;
	}

	/**
	 * @return the codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * @param codigoPais the codigoPais to set
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the codigoPaisId
	 */
	public String getCodigoPaisId() {
		return codigoPaisId;
	}

	/**
	 * @param codigoPaisId the codigoPaisId to set
	 */
	public void setCodigoPaisId(String codigoPaisId) {
		this.codigoPaisId = codigoPaisId;
	}

	/**
	 * @return the codigoPaisIdentificacion
	 */
	public String getCodigoPaisIdentificacion() {
		return codigoPaisIdentificacion;
	}

	/**
	 * @param codigoPaisIdentificacion the codigoPaisIdentificacion to set
	 */
	public void setCodigoPaisIdentificacion(String codigoPaisIdentificacion) {
		this.codigoPaisIdentificacion = codigoPaisIdentificacion;
	}

	/**
	 * @return the paisId
	 */
	public String getPaisId() {
		return paisId;
	}

	/**
	 * @param paisId the paisId to set
	 */
	public void setPaisId(String paisId) {
		this.paisId = paisId;
	}

	/**
	 * @return the paisIdentificacion
	 */
	public String getPaisIdentificacion() {
		return paisIdentificacion;
	}

	/**
	 * @param paisIdentificacion the paisIdentificacion to set
	 */
	public void setPaisIdentificacion(String paisIdentificacion) {
		this.paisIdentificacion = paisIdentificacion;
	}

	/**
	 * @return the codigoLocalidad
	 */
	public String getCodigoLocalidad() {
		return codigoLocalidad;
	}

	/**
	 * @param codigoLocalidad the codigoLocalidad to set
	 */
	public void setCodigoLocalidad(String codigoLocalidad) {
		this.codigoLocalidad = codigoLocalidad;
	}

	/**
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}

	/**
	 * @param localidad the localidad to set
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	/**
	 * 
	 * @param codigoPersona
	 */
	public boolean cargarPersona(String numeroId,String tipoId)
	{
		boolean resulReturn=false;
		Connection con=UtilidadBD.abrirConexion();
		PersonaDao personaDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao();
		this.codigoPersona= personaDao.obtenerCodigoPersona(con, numeroId, tipoId);
		logger.info("cod persona --> "+this.codigoPersona);
		HashMap<String, Object> resultado=personaDao.cargarPersona(con,this.codigoPersona);
		
		if(Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
		{
			cargarPaciente(resultado);
			resulReturn=true;
		}
		UtilidadBD.closeConnection(con);
		return resulReturn;
		
	}
	
	/**
	 * Método implementado para cargar los datos de la persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public void cargarPersona(Connection con,int codigoPersona)
	{
		
		HashMap<String, Object> resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao().cargarPersona(con, codigoPersona);
		logger.info("resultado ");
		Utilidades.imprimirMapa(resultado);
		cargarPaciente(resultado);
	}

	private void cargarPaciente(HashMap resultado)
	{
		if(Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
		{
			this.codigoPersona=Utilidades.convertirAEntero(resultado.get("codigopersona").toString());
			this.numeroIdentificacion = resultado.get("numeroIdentificacion").toString();
			this.codigoTipoIdentificacion = resultado.get("codigoTipoIdentificacion").toString();
			this.tipoIdentificacion = resultado.get("nombreTipoIdentificacion").toString();
			this.codigoDepartamentoIdentificacion = resultado.get("codigoDeptoNacimiento").toString();
			this.departamentoIdentificacion = resultado.get("nombreDeptoNacimiento").toString();
			this.codigoCiudadIdentificacion = resultado.get("codigoCiudadNacimiento").toString();
			this.ciudadIdentificacion = resultado.get("nombreCiudadNacimiento").toString();
			this.codigoPaisIdentificacion = resultado.get("codigoPaisNacimiento").toString();
			this.paisIdentificacion = resultado.get("nombrePaisNacimiento").toString();
			this.codigoTipoPersona = resultado.get("codigoTipoPersona").toString();
			this.tipoPersona = resultado.get("nombreTipoPersona").toString();
			String[] fechaNacimiento = resultado.get("fechaNacimiento").toString().split("/");
			this.diaNacimiento = fechaNacimiento[0];
			this.mesNacimiento = fechaNacimiento[1];
			this.anioNacimiento = fechaNacimiento[2];
			this.edad = UtilidadFecha.calcularEdad(resultado.get("fechaNacimiento").toString());
			this.codigoEstadoCivil = resultado.get("codigoEstadoCivil").toString();
			this.estadoCivil = resultado.get("nombreEstadoCivil").toString();
			this.codigoSexo = resultado.get("codigoSexo").toString();
			this.sexo = resultado.get("nombreSexo").toString();
			this.libretaMilitar = resultado.get("libretaMilitar").toString();
			this.primerNombrePersona = resultado.get("primerNombre").toString();
			this.segundoNombrePersona = resultado.get("segundoNombre").toString();
			this.primerApellidoPersona = resultado.get("primerApellido").toString();
			this.segundoApellidoPersona = resultado.get("segundoApellido").toString();
			this.direccion = resultado.get("direccion").toString();
			this.codigoDepartamento = resultado.get("codigoDeptoVivienda").toString();
			this.departamento = resultado.get("nombreDeptoVivienda").toString();
			this.codigoCiudad = resultado.get("codigoCiudadVivienda").toString();
			this.ciudad = resultado.get("nombreCiudadVivienda").toString();
			this.codigoPais = resultado.get("codigoPaisVivienda").toString();
			this.pais = resultado.get("nombrePaisVivienda").toString();
			this.codigoBarrio = resultado.get("codigoBarrioVivienda").toString();
			this.barrio = resultado.get("nombreBarrioVivienda").toString();
			this.telefono = resultado.get("telefono").toString();
			this.email = resultado.get("email").toString();
			this.codigoDepartamentoId = resultado.get("codigoDeptoId").toString();
			this.departamentoId = resultado.get("nombreDeptoId").toString();
			this.codigoCiudadId = resultado.get("codigoCiudadId").toString();
			this.ciudadId = resultado.get("nombreCiudadId").toString();
			this.codigoPaisId = resultado.get("codigoPaisId").toString();
			this.paisId = resultado.get("nombrePaisId").toString();
			this.codigoLocalidad = resultado.get("codigoLocalidad").toString();
			this.localidad = resultado.get("nombreLocalidad").toString();
			this.indicativoInterfaz=(String)resultado.get("indicativoInterfaz");
			this.contratoInterfaz=(String)resultado.get("contratoInterfaz");
			this.telefonoFijo=resultado.get("telefonofijo").toString();
			this.telefonoCelular=resultado.get("telefonocelular").toString();
		//	logger.info("LE TELEFONO FIJO----------->"+this.telefonoFijo);
		}
	}
	

	public String getLibretaMilitar() {
		return libretaMilitar;
	}

	public void setLibretaMilitar(String libretaMilitar) {
		this.libretaMilitar = libretaMilitar;
	}

	/**
	 * @return the telefonoCelular
	 */
	public String getTelefonoCelular() {
		return telefonoCelular;
	}

	/**
	 * @param telefonoCelular the telefonoCelular to set
	 */
	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	/**
	 * @return the telefonoFijo
	 */
	public String getTelefonoFijo() {
		return telefonoFijo;
	}

	/**
	 * @param telefonoFijo the telefonoFijo to set
	 */
	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	//Anexo 958
	public String getIndicativoInterfaz() {
		return indicativoInterfaz;
	}

	public void setIndicativoInterfaz(String indicativoInterfaz) {
		this.indicativoInterfaz = indicativoInterfaz;
	}

	public String getContratoInterfaz() {
		return contratoInterfaz;
	}

	public void setContratoInterfaz(String contratoInterfaz) {
		this.contratoInterfaz = contratoInterfaz;
	}
	
	//Fion Anexo 958
	
	/**
	 * Obtener el nombre completo de la persona
	 */
	public String getNombreCompleto()
	{
		String nombreCompleto=primerNombrePersona;
		if(!UtilidadTexto.isEmpty(segundoNombrePersona))
		{
			nombreCompleto+=" "+segundoNombrePersona;
		}
		nombreCompleto+=" "+primerApellidoPersona;
		if(!UtilidadTexto.isEmpty(segundoApellidoPersona))
		{
			nombreCompleto+=" "+segundoApellidoPersona;
		}
		return nombreCompleto;
	}

	public void setTipoidenAcronimo(String tipoidenAcronimo) {
		this.tipoidenAcronimo = tipoidenAcronimo;
	}

	public String getTipoidenAcronimo() {
		return tipoidenAcronimo;
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return firma de medico
	 */
	public static String obtenerFirmaDigitalMedico(Connection con,Integer codigoMedico ){
		String res = "";
		res = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaDao().obtenerFirmaDigitalMedico(con, codigoMedico);
		return res;
	}
	
}