/*
 * @(#)TagBusquedaPersonasStruts.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.tag;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Collection;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * @version 1.0, Mar 31, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class TagBusquedaPersonasStruts extends BodyTagSupport
{
	/**
	 * String en donde viene con quien estoy trabajando
	 * (puede ser un médico o un paciente)
	 */
	private String tipoPersonaBuscada = null;
	
	private String numeroHistoriaClinica = "";

	private String fichaUsuarioCapitado = "";

	private String accion = "";

	/**
	 * Número de identificación buscada
	 */
	private String numeroIdentificacion = "";

	/**
	 * Código del tipo de identificación buscada
	 */
	private String codigoTipoIdentificacion = "";

	/**
	 * Primer nombre de la persona buscada.
	 */
	private String primerNombrePersona = "";

	/**
	 * Segundo nombre de la persona buscada.
	 */
	private String segundoNombrePersona = "";

	/**
	 * Primer apellido de la persona buscada.
	 */
	private String primerApellidoPersona = "";

	/**
	 * Segundo apellido de la persona buscada.
	 */
	private String segundoApellidoPersona = "";

	/**
	 * Pais de vivienda
	 */
	private String codigoPaisIdentificacion = "";
	
	/**
	 * Codigo de la ciudad en la que fue expedida la identificacion de la persona que se esta buscando que se esta buscando.
	 */
	private String codigoCiudadIdentificacion = "";

	/**
	 * Codigo del departamento en la que fue expedida la identificacion de la persona que se esta buscando.
	 */
	private String codigoDepartamentoIdentificacion = "";

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Fecha de nacimiento de la persona buscada.
	 */
	private String fechaNacimiento = null;

	/**
	 * Dirección de la persona buscada.
	 */
	private String direccion = "";

	/**
	 * Codigo pais de residencia
	 */
	private String codigoPais = "";
	
	/**
	 * Codigo de la Ciudad de residencia de la persona.
	 */
	private String codigoCiudad = "";

	/**
	 * Codigo del departamento donde reside de la persona.
	 */
	private String codigoDepartamento = "";

	/**
	 * Criterios de búsqueda elegidos
	 */
	private String criteriosBusqueda[];

	/**
	 * Email de la persona buscada.
	 */
	private String email = "";

	/**
	 * Codigo Cargo de la persona (usuario)
	 */
	private String codigoCargo; 
	/**
	 * Teléfono de la persona buscada.
	 */
	private String telefono = "";

	/**
	 * Código de la institución a la cual pertenece la persona buscada.
	 */
	private String codigoInstitucion = "";

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	* Conjunto solución de la búsqueda
	*/
	private Collection	ic_resultados = null;
	
	
	private String numeroIngreso="";
	

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException
	{
		if
		(
			tipoPersonaBuscada != null							&&
			(
				tipoPersonaBuscada.equals("medico")				||
				tipoPersonaBuscada.equals("paciente")			||
				tipoPersonaBuscada.equals("pacienteEncabezado")			||
				tipoPersonaBuscada.equals("usuario")			||
				tipoPersonaBuscada.equals("usuarioInactivo")
			)													&&
			codigoInstitucion != null							&&
			!codigoInstitucion.equals("")
		)
		{
			abrirResultSet();
	
			if(ic_resultados != null && ic_resultados.size() > 0)
				pageContext.setAttribute("resultadoBusquedaPersonas", ic_resultados);
				return EVAL_BODY_BUFFERED;
				
		}

		return SKIP_BODY;
	}

	/**
	 * Este método escribe en la página las <i>scripting variables</i>
	 * solicitadas por la página. También limpia el estado interno de los
	 * atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException
	{
		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output!=null && output.length() > 0)
		{
			try
			{
				bodyContent.getEnclosingWriter().write(output.toString() );
				clean();
			}
			catch (Exception e)
			{
				throw new JspTagException("Error escribiendo en TagBusquedaPersonasStruts.doEndTag: " + e.getMessage() );
			}
		}

		return EVAL_PAGE;
	}

	/**
	 * Este método deja en valores vacíos los atributos de este Tag. Debe
	 * llamarse en doEndTag justo antes del return de dicho método; esta es una
	 * precaución que se debe tener en caso que el container donde corra esta
	 * aplicación haga pooling de tag handlers y no "limpie" los atributos
	 * deinstancia de los tags al momento de reutilizarlos.
	 */
	private void clean() throws SQLException
	{
		codigoCiudad						= "";
		codigoCiudadIdentificacion			= "";
		codigoDepartamento					= "";
		codigoDepartamentoIdentificacion	= "";
		codigoInstitucion					= "";
		codigoTipoIdentificacion			= "";
		criteriosBusqueda					= null;
		direccion							= "";
		email								= "";
		fechaNacimiento						= null;
		numeroIdentificacion				= "";
		output								= null;
		primerApellidoPersona				= "";
		primerNombrePersona					= "";
		segundoApellidoPersona				= "";
		segundoNombrePersona				= "";
		telefono							= "";
		tipoPersonaBuscada					= null;
		fichaUsuarioCapitado = "";
		accion = "";
		numeroIngreso = "";

		if(ic_resultados != null)
		{
			ic_resultados.clear();
			ic_resultados = null;
		}
	}

	/**
	 * Este método se conecta con la Base de Datos y trae los resultados de la
	 * consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados.
	 */
	private void abrirResultSet() throws JspTagException
	{
		String		tipoBD		= (String)pageContext.getServletContext().getInitParameter("TIPOBD");
		DaoFactory	myFactory	= DaoFactory.getDaoFactory(tipoBD);
		TagDao		tagDao		= myFactory.getTagDao();

		try
		{
			
			
				ic_resultados	= 
					tagDao.consultaTagBusquedaPersonasStruts(
						con, 
						tipoPersonaBuscada, 
						criteriosBusqueda, 
						numeroIdentificacion, 
						codigoTipoIdentificacion, 
						primerNombrePersona, 
						segundoNombrePersona, 
						primerApellidoPersona, 
						segundoApellidoPersona, 
						fechaNacimiento, 
						direccion, 
						telefono, 
						codigoCiudad, 
						codigoCiudadIdentificacion, 
						codigoDepartamento, 
						codigoDepartamentoIdentificacion, 
						email, 
						codigoCargo,
						codigoInstitucion,
						numeroHistoriaClinica,
						codigoPais,
						codigoPaisIdentificacion,
						fichaUsuarioCapitado,
						accion,
						numeroIngreso
					) 
				;
			
			
		}
		catch (SQLException sqle)
		{
			throw new JspTagException("Error obteniendo los datos de la base de datos en el Tag Busqueda Personas Struts: " + sqle.getMessage() );
		}
	}

	/**
	 * Returns the codigoCiudad.
	 * @return String
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * Returns the codigoCiudadIdentificacion.
	 * @return String
	 */
	public String getCodigoCiudadIdentificacion() {
		return codigoCiudadIdentificacion;
	}

	/**
	 * Returns the codigoDepartamento.
	 * @return String
	 */
	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	/**
	 * Returns the codigoDepartamentoIdentificacion.
	 * @return String
	 */
	public String getCodigoDepartamentoIdentificacion() {
		return codigoDepartamentoIdentificacion;
	}

	/**
	 * Returns the codigoInstitucion.
	 * @return String
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Returns the codigoTipoIdentificacion.
	 * @return String
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * Returns the con.
	 * @return Connection
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Returns the criteriosBusqueda.
	 * @return String[]
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Returns the direccion.
	 * @return String
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * Returns the email.
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the fechaNacimiento.
	 * @return String
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Returns the numeroIdentificacion.
	 * @return String
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * Returns the primerApellidoPersona.
	 * @return String
	 */
	public String getPrimerApellidoPersona() {
		return primerApellidoPersona;
	}

	/**
	 * Returns the primerNombrePersona.
	 * @return String
	 */
	public String getPrimerNombrePersona() {
		return primerNombrePersona;
	}

	/**
	 * Returns the segundoApellidoPersona.
	 * @return String
	 */
	public String getSegundoApellidoPersona() {
		return segundoApellidoPersona;
	}

	/**
	 * Returns the segundoNombrePersona.
	 * @return String
	 */
	public String getSegundoNombrePersona() {
		return segundoNombrePersona;
	}

	/**
	 * Returns the telefono.
	 * @return String
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Returns the tipoPersonaBuscada.
	 * @return String
	 */
	public String getTipoPersonaBuscada() {
		return tipoPersonaBuscada;
	}

	/**
	 * Sets the codigoCiudad.
	 * @param codigoCiudad The codigoCiudad to set
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * Sets the codigoCiudadIdentificacion.
	 * @param codigoCiudadIdentificacion The codigoCiudadIdentificacion to set
	 */
	public void setCodigoCiudadIdentificacion(String codigoCiudadIdentificacion) {
		this.codigoCiudadIdentificacion = codigoCiudadIdentificacion;
	}

	/**
	 * Sets the codigoDepartamento.
	 * @param codigoDepartamento The codigoDepartamento to set
	 */
	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	/**
	 * Sets the codigoDepartamentoIdentificacion.
	 * @param codigoDepartamentoIdentificacion The codigoDepartamentoIdentificacion to set
	 */
	public void setCodigoDepartamentoIdentificacion(String codigoDepartamentoIdentificacion) {
		this.codigoDepartamentoIdentificacion =
			codigoDepartamentoIdentificacion;
	}

	/**
	 * Sets the codigoInstitucion.
	 * @param codigoInstitucion The codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Sets the codigoTipoIdentificacion.
	 * @param codigoTipoIdentificacion The codigoTipoIdentificacion to set
	 */
	public void setCodigoTipoIdentificacion(String codigoTipoIdentificacion) {
		this.codigoTipoIdentificacion = codigoTipoIdentificacion;
	}

	/**
	 * Sets the con.
	 * @param con The con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * Sets the criteriosBusqueda.
	 * @param criteriosBusqueda The criteriosBusqueda to set
	 */
	public void setCriteriosBusqueda(String[] criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}

	/**
	 * Sets the direccion.
	 * @param direccion The direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Sets the email.
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the fechaNacimiento.
	 * @param fechaNacimiento The fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * Sets the numeroIdentificacion.
	 * @param numeroIdentificacion The numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Sets the primerApellidoPersona.
	 * @param primerApellidoPersona The primerApellidoPersona to set
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona) {
		this.primerApellidoPersona = primerApellidoPersona;
	}

	/**
	 * Sets the primerNombrePersona.
	 * @param primerNombrePersona The primerNombrePersona to set
	 */
	public void setPrimerNombrePersona(String primerNombrePersona) {
		this.primerNombrePersona = primerNombrePersona;
	}

	/**
	 * Sets the segundoApellidoPersona.
	 * @param segundoApellidoPersona The segundoApellidoPersona to set
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona) {
		this.segundoApellidoPersona = segundoApellidoPersona;
	}

	/**
	 * Sets the segundoNombrePersona.
	 * @param segundoNombrePersona The segundoNombrePersona to set
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona) {
		this.segundoNombrePersona = segundoNombrePersona;
	}

	/**
	 * Sets the telefono.
	 * @param telefono The telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Sets the tipoPersonaBuscada.
	 * @param tipoPersonaBuscada The tipoPersonaBuscada to set
	 */
	public void setTipoPersonaBuscada(String tipoPersonaBuscada) {
		this.tipoPersonaBuscada = tipoPersonaBuscada;
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
	public void setCodigoPais(String codigoPais) {
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
	public void setCodigoPaisIdentificacion(String codigoPaisIdentificacion) {
		this.codigoPaisIdentificacion = codigoPaisIdentificacion;
	}


	/**
	 * @return the fichaUsuarioCapitado
	 */
	public String getFichaUsuarioCapitado() {
		return fichaUsuarioCapitado;
	}

	/**
	 * @param fichaUsuarioCapitado the fichaUsuarioCapitado to set
	 */
	public void setFichaUsuarioCapitado(String fichaUsuarioCapitado) {
		this.fichaUsuarioCapitado = fichaUsuarioCapitado;
	}

	/**
	 * @return the accion
	 */
	public String getAccion() {
		return accion;
	}

	/**
	 * @param accion the accion to set
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}	

	public String getNumeroIngreso() {
		return numeroIngreso;
	}

	public void setNumeroIngreso(String numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}

	public String getCodigoCargo() {
		return codigoCargo;
	}

	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

}
