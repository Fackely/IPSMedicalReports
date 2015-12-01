/*
 * @(#)BusquedaPersonaBean.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util;

import java.util.Collection;
import java.util.ArrayList;

import java.util.HashMap;

/**
 * Esta clase es necesaria para capturar la información seleccionada
 * en la pagina de busqueda por personas
 *
 * @version 1.0, Nov 12, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
*/

public class BusquedaPersonaBean {

	private ArrayList lal_al = new ArrayList();
	
	private String numeroHistoriaClinica;

	/**
	* Variable usada par la busqueda de usuarios capitados
	**/
	private String fichaUsuarioCapitado;
	
	/**
	 * Número de identificación de la persona.
	 */
	private String numeroIdentificacion="";

	/**
	 * Tipo de identificación de la persona.
	 */
	private String tipoIdentificacion="";

	/**
	 * Código del tipo de identificación de la persona.
	 */
	private String codigoTipoIdentificacion="";

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
	 * Ciudad y departamento en la que fue expedida la identificacion de la persona.
	 */
	private String ciudadIdentificacion = "";

	/**
	 * Codigo pais de nacimiento
	 */
	private String codigoPaisIdentificacion = "";
	
	/**
	 * Pais de identificacion
	 */
	private String paisIdentificacion = "";
	
	/**
	 * Codigo de la ciudad en la que fue expedida la identificacion de la persona.
	 */
	private String codigoCiudadIdentificacion = "";

	/**
	 * Codigo del departamento en la que fue expedida la identificacion de la persona.
	 */
	private String codigoDepartamentoIdentificacion = "";

	/**
	 * Fecha de nacimiento de la persona
	 */
	private String fechaNacimiento="";

	/**
	 * Estado civil de la persona.
	 */
	private String estadoCivil = "";

	/**
	 * Codigo del Estado civil de la persona.
	 */
	private String codigoEstadoCivil = "";

	/**
	 * Codigo del sexo de la persona.
	 */
	private String codigoSexo = "";

	/**
	 * Sexo de la persona.
	 */
	private String sexo = "";

	/**
	 * Dirección de residencia de la persona.
	 */
	private String direccion = "";

	/**
	 * Ciudad de residencia de la persona.
	 */
	private String ciudadVivienda = "";

	/**
	 * Codigo pais de residencia
	 */
	private String codigoPais = "";
	
	/**
	 * pais de residencia
	 */
	private String pais = "";
	
	/**
	 * Codigo de la Ciudad de residencia de la persona.
	 */
	private String codigoCiudad = "";

	/**
	 * Codigo del departamento donde reside de la persona.
	 */
	private String codigoDepartamento = "";

	/**
	 * Criterio(s) de búsqueda elegido(s).
	 */
	private String criteriosBusqueda[];

	/**
	 * Nombre de la Ciudad/Departamento de residencia de la persona.
	 */
	private String ciudad = "";

	/**
	 * Telefono de la persona.
	 */
	private String telefono = "";

	/**
	 * Correo electronico de la persona.
	 */
	private String email = "";
	
	
	private String codigoCargo="";

	private String numeroIngreso="";
	
	public String getNumeroIngreso() {
		return numeroIngreso;
	}

	public void setNumeroIngreso(String numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}

	/**
	 * Retorna la ciudad de identificacion.
	 * @return la ciudad de identificacion
	 */
	public String getCiudadIdentificacion() {
		return ciudadIdentificacion;
	}

	/**
	 * Retorna la ciudad de vivienda.
	 * @return la ciudad de vivienda
	 */
	public String getCiudadVivienda() {
		return ciudadVivienda;
	}

	/**
	 * Retorna los criterios de busqueda.
	 * @return los criterios de busqueda
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Retorna la direccion.
	 * @return la direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * Retorna el email.
	 * @return el email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Retorna el estado civil.
	 * @return el estado civil
	 */
	public String getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * Retorna el primer apellido de la persona.
	 * @return el primer apellido de la persona
	 */
	public String getPrimerApellidoPersona() {
		return primerApellidoPersona;
	}

	/**
	 * Retorna el primer nombre de la persona.
	 * @return el primer nombre de la persona
	 */
	public String getPrimerNombrePersona() {
		return primerNombrePersona;
	}

	/**
	 * Retorna el segundo apellido de la persona.
	 * @return el segundo apellido de la persona
	 */
	public String getSegundoApellidoPersona() {
		return segundoApellidoPersona;
	}

	/**
	 * Retorna el segundo nombre de la persona.
	 * @return el segundo nombre de la persona
	 */
	public String getSegundoNombrePersona() {
		return segundoNombrePersona;
	}

	/**
	 * Retorna el sexo.
	 * @return el sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * Retorna el telefono.
	 * @return el telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Retorna la ciudad.
	 * @return la ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * Retorna el código de la ciudad.
	 * @return el código de la ciudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * Retorna el código de la ciudad de identificación.
	 * @return el código de la ciudad de identificación
	 */
	public String getCodigoCiudadIdentificacion() {
		return codigoCiudadIdentificacion;
	}

	/**
	 * Retorna el código del departamento.
	 * @return el código del departamento
	 */
	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	/**
	 * Retorna el código del departamento de la identificación.
	 * @return el código del departamento de la identificación
	 */
	public String getCodigoDepartamentoIdentificacion() {
		return codigoDepartamentoIdentificacion;
	}

	/**
	 * Retorna el código del estado civil.
	 * @return el código del estado civil
	 */
	public String getCodigoEstadoCivil() {
		return codigoEstadoCivil;
	}

	/**
	 * Retorna el código del sexo.
	 * @return el código del sexo
	 */
	public String getCodigoSexo() {
		return codigoSexo;
	}

	/**
	 * Retorna el código del tipo de identificación.
	 * @return el código del tipo de identificación
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * Retorna el número de la identificación.
	 * @return el número de la identificación
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * Retorna el tipo de identificación.
	 * @return el tipo de identificación
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * Establece la ciudad de vivienda.
	 * @param ciudadVivienda La ciudad de vivienda a establecer
	 */
	public void setCiudadVivienda(String ciudadVivienda) {
		this.ciudadVivienda = ciudadVivienda;
	}

	/**
	 * Establece el criteriosBusqueda.
	 * @param criteriosBusqueda Los criterios de busqueda a establecer
	 */
	public void setCriteriosBusqueda(String[] criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}

	/**
	 * Establece la direccion.
	 * @param direccion La direccion a establecer
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Establece el email.
	 * @param email El email a establecer
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Establece el estado civil.
	 * @param estadoCivil El estado civil a establecer
	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * Establece la ciudad de identificacion.
	 * @param ciudadIdentificacion La ciudad de identificacion a establecer
	 */
	public void setCiudadIdentificacion(String ciudadIdentificacion) {
		this.ciudadIdentificacion = ciudadIdentificacion;
	}

	/**
	 * Establece el primer apellido de la persona.
	 * @param primerApellidoPersona El primerApellidoPersona a establecer
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona) {
		this.primerApellidoPersona = primerApellidoPersona;
	}

	/**
	 * Establece el primer nombre de la persona.
	 * @param primerNombrePersona El primer nombre de la persona a establecer
	 */
	public void setPrimerNombrePersona(String primerNombrePersona) {
		this.primerNombrePersona = primerNombrePersona;
	}

	/**
	 * Establece el segundo apellido de la persona.
	 * @param segundoApellidoPersona El segundo apellido de la persona a
	 * establecer
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona) {
		this.segundoApellidoPersona = segundoApellidoPersona;
	}

	/**
	 * Establece el segundo nombre de la persona.
	 * @param segundoNombrePersona El segundo nombre de la persona a establecer
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona) {
		this.segundoNombrePersona = segundoNombrePersona;
	}

	/**
	 * Establece el sexo de la persona.
	 * @param sexo El sexo a establecer
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * Establece el telefono de la persona.
	 * @param telefono El telefono a establecer
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Establece el número de identificación.
	 * @param numeroIdentificacion el número de identificacion a establecer
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Establece el tipo de identificación.
	 * @param tipoIdentificacion el tipo de identificación a establecer
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * Establece la ciudad.
	 * @param ciudad la ciudad a establecer
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * Inicializa los atributos de este objeto en valores vacíos, mas no nulos.
	 */
	public void clean ()
	{
		this.numeroHistoriaClinica = "";
		this.fichaUsuarioCapitado = "";
		primerNombrePersona = "";
		segundoNombrePersona = "";
		primerApellidoPersona = "";
		segundoApellidoPersona = "";
		ciudadIdentificacion = "";
		codigoCiudadIdentificacion = "";
		codigoDepartamentoIdentificacion = "";
		this.fechaNacimiento="";
		estadoCivil = "";
		codigoEstadoCivil = "";
		codigoSexo = "";
		sexo = "";
		direccion = "";
		ciudadVivienda = "";
		codigoCiudad = "";
		codigoDepartamento = "";
		ciudad = "";
		telefono = "";
		email = "";
		codigoCargo="";
		numeroIngreso="";
		numeroIdentificacion="";
	}

	/**
	 * Inicializa las variables que puedan venir de la forma código-valor.
	 */
	public void inicializarVariables() {

		String [] resultados;

		//La siguiente linea dejara el codigo del departamento, el codigo de la ciudad y el nombre de la ciudad/dep
		//en un arreglo de Strings, para luego asignarlos a las variables respectivas (tine dos codigos)
		/**
		 * MT 6804
		 * @author LuiFerJa
		 * Se agrega if para validara que el campo ciudadIdentificacion no llegue vacio por que  al tratar de ejecutar
		 * el metodo separarNombresDeCodigos(ciudadIdentificacion, 2);No se produzca NullPointerException.
		 * El atributo llega vacio cuando no se selecciona campo Ciudad de Nacimiento del formulario /src/main/webapp/common/buscarPersona.jsp
		 *  
		 */
		if(!ciudadIdentificacion.isEmpty()){			
			resultados=UtilidadTexto.separarNombresDeCodigos(ciudadIdentificacion, 2);
			ciudadIdentificacion=resultados[2];
			codigoCiudadIdentificacion=resultados[1];
			codigoDepartamentoIdentificacion=resultados[0];
		}
	/**
	 * FIN MT 6804
	 */
		

		//Ahora el estado civil . Temporalmente desactivado porque se quito este criterio
		//de busqueda, junto al sexo
		/*if (estadoCivil!=null)
		{

			resultados=UtilidadTexto.separarNombresDeCodigos(estadoCivil, 1);
			codigoEstadoCivil=resultados[0];
			estadoCivil=resultados[1];
		}

		//Ahora el sexo
		if (sexo!=null)
		{
			resultados=UtilidadTexto.separarNombresDeCodigos(sexo, 1);
			codigoSexo=resultados[0];
			sexo=resultados[1];
		}*/

		//Ahora la ciudad de residencia
		/**
		 * MT 6804
		 * @author LuiFerJa
		 * Se agrega if para validar que el campo ciudad de  nacimiento  no llegue vacio por que  al tratar de ejecutar
		 * el metodo separarNombresDeCodigos(ciudad, 2); no se produzca NullPointerException.
		 * El atributo llega vacio cuando no se selecciona campo Ciudad de Residencia del formulario /src/main/webapp/common/buscarPersona.jsp
		 */
		if(!ciudad.isEmpty()){	
			resultados=UtilidadTexto.separarNombresDeCodigos(ciudad, 2);
			codigoDepartamento=resultados[0];
			codigoCiudad=resultados[1];
			ciudad=resultados[2];
		}
		/**
		 * FIN MT 6804
		 */
		
		//Ahora el tipo de documento
		if(!tipoIdentificacion.equals(""))
		{
			resultados=UtilidadTexto.separarNombresDeCodigos(tipoIdentificacion,1);
			codigoTipoIdentificacion=resultados[0];
			tipoIdentificacion=resultados[1];
		}
		else
		{
			codigoTipoIdentificacion="";
			tipoIdentificacion="";
		}


	}

	/**
	 * Inicializa las fechas en una fecha válida.
	 */
	public void volverAFechaValida () 
	{
		if (!UtilidadFecha.esFechaValidaSegunAp(this.fechaNacimiento))
		{
			fechaNacimiento="01/01/1310";
		}
	}

	public void setItems(Collection ac_c)
	{
		if(ac_c != null)
			lal_al = new ArrayList(ac_c);
		else
			lal_al = new ArrayList(0);
	}

	public Collection getItems()
	{
		return lal_al;
	}
	
	public int size()
	{
		return lal_al == null ? 0 : lal_al.size();
	}

	public HashMap get(int i)
	{
		HashMap info = null;

		try
		{
			info = (HashMap)lal_al.get(i);
		}
		catch (IndexOutOfBoundsException e)
		{
			return info;
		}
		finally
		{
			return info;
		}
	}

	/**
	 * @return
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param string
	 */
	public void setFechaNacimiento(String string) {
		fechaNacimiento = string;
	}

	/**
	 * @return the numeroHistoriaClinica
	 */
	public String getNumeroHistoriaClinica() {
		return numeroHistoriaClinica;
	}

	/**
	 * @param numeroHistoriaClinica the numeroHistoriaClinica to set
	 */
	public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
		this.numeroHistoriaClinica = numeroHistoriaClinica;
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
	public void setCodigoPais(String codigoPais) 
	{
		String[] vector = codigoPais.split(ConstantesBD.separadorSplit);
		if(vector.length>1)
		{
			this.pais = vector[1];
			this.codigoPais = vector[0];
		}
		else
			this.codigoPais = codigoPais;
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
	public void setCodigoPaisIdentificacion(String codigoPaisIdentificacion) 
	{
		
		String[] vector = codigoPaisIdentificacion.split(ConstantesBD.separadorSplit);
		if(vector.length>1)
		{
			this.paisIdentificacion = vector[1];
			this.codigoPaisIdentificacion = vector[0];
		}
		else
			this.codigoPaisIdentificacion = codigoPaisIdentificacion;
		
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
	 * @return La ficha del usuario capitado
	 */
	public String getFichaUsuarioCapitado() {
		return this.fichaUsuarioCapitado;
	}

	/**
	 * @param asigna la ficha del usuario capitado
	 */
	public void setFichaUsuarioCapitado(String fichaUsuarioCapitado) {
		this.fichaUsuarioCapitado = fichaUsuarioCapitado;
	}

	public String getCodigoCargo() {
		return codigoCargo;
	}

	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

}
