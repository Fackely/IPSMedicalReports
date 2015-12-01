/*
 * @(#)TagMuestraAdmisionesHospitalarias.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.io.IOException;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.princetonsa.dao.DaoFactory;

/**
 *
 * @version 1.0, Mar 25, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */

public class TagMuestraAdmisionesHospitalarias extends BodyTagSupport {

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * El código de la institución a la cual pertenece esta persona.
	 */
	private String codigoInstitucion = "";

	/**
	 * <code>ResultSet</code> con los resultados de la consulta
	 */
	private ResultSetDecorator rs = null;

	/**
	 * Código de la admision que desea buscar
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	private String idBusqueda;
	
	/**
	 * Código del tipo de identificación del paciente que desea buscar
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	private String tipoIdBusqueda;
	
	/**
	 * Número de identificación del paciente que desea buscar
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	private String numeroIdBusqueda;

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	 * En este String vamos a recibir si se desea mostrar la información
	 * ampliada de las admisiones. Posibles modos actualmente:
	 * basico y detalleAdmisionHospitalaria
	 */
	
	private String modo="";

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		if ( (tipoIdBusqueda!=null&&numeroIdBusqueda!=null&&tipoIdBusqueda!=null&&!numeroIdBusqueda.equals("")&&!tipoIdBusqueda.equals("") ) ||(idBusqueda!=null&&!idBusqueda.equals("")) ) 
		{
			abrirResultSet();

			try {
				if (rs.next()) 
				{
					output = new StringBuffer();
					recorrerResultSet();
					return EVAL_BODY_BUFFERED;
				}
				else 
				{
					return SKIP_BODY;
				}
			}
			catch (SQLException e) 
			{
				throw new JspTagException("Error Escribiendo El Tag Muestra Admisiones Hospitalizacion : "+e.getMessage());
			}
		}
		else {
			return SKIP_BODY;
		}

	}

	/**
	 * Procesa el cuerpo del tag.
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspTagException 
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) 
		{

			output.append(bodyContent.getString());
			try {
				bodyContent.clear();
			}
			catch (IOException ex) {
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del Tag");
			}
		}

		try {
			if (rs.next()) 
			{

				recorrerResultSet();
				return EVAL_BODY_AGAIN;
			}

			else 
			{

				return SKIP_BODY;
			}
		}
		catch (SQLException e) 
		{

			throw new JspTagException("Error Recorriendo El Tag Muestra Admisiones Hospitalarias : "+e.getMessage());
		}

	}

	/**
	 * Este método escribe en la página las <i>scripting variables</i>
	 * solicitadas según el tipoPersona, usando la presentación presente en la
	 * página. También limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output!=null && output.length() > 0) 
		{

			try {
				bodyContent.getEnclosingWriter().write(output.toString());
				clean();
			}
			catch (Exception e) 
			{

				throw new JspTagException("Error Escribiendo El TagMuestraAdmisionesHospitalarias: "+e.getMessage());
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
		codigoInstitucion = "";
		idBusqueda="";
		tipoIdBusqueda="";
		numeroIdBusqueda="";
		output = null;
		if (rs!=null)
		{
			rs.close();
			rs = null;
		}

	}

	/**
	 * Este método se conecta con la Base de Datos y trae los resultados de la
	 * consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados.
	 */
	private void abrirResultSet() throws JspTagException 
	{

		try 
		{
			this.rs = (DaoFactory.getDaoFactory((String) pageContext.getServletContext().getInitParameter("TIPOBD"))).getTagDao().consultaTagMuestraAdmisionesHospitalarias (con, idBusqueda, tipoIdBusqueda, numeroIdBusqueda, codigoInstitucion);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Admisiones Hospitalizacion : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{

		String medico="", codigoCuenta= "",  codigoAdmision  = "", fechaAdmision  = "", horaAdmision  = "", origenAdmision  = "",  causaExterna    = "", diagnostico  = "", codigoDiagnostico  = "", codigoCIEDiagnostico  = "", numeroCama  = "", centroCosto = "", estadoCama  = "", tipoUsuarioCama  = "", descripcionCama  = "", numeroAutorizacion  = "", loginUsuario="", estadoAdmision="";

		try 
		{

			codigoCuenta=rs.getString("codigoCuenta");
			codigoAdmision=rs.getString("codigoAdmision");
			fechaAdmision=rs.getString("fechaAdmision");
			horaAdmision=rs.getString("horaAdmision");
			origenAdmision=rs.getString("origenAdmision");
			causaExterna=rs.getString("causaExterna");
			diagnostico=rs.getString("diagnostico");
			codigoDiagnostico=rs.getString("codigoDiagnostico");
			codigoCIEDiagnostico=rs.getString("codigoCIEDiagnostico");
			numeroCama=rs.getString("numeroCama");
			centroCosto=rs.getString("centroCosto");
			estadoCama=rs.getString("estadoCama");
			tipoUsuarioCama=rs.getString("tipoUsuarioCama");
			descripcionCama=rs.getString("descripcionCama");
			numeroAutorizacion=rs.getString("numeroAutorizacion");
			loginUsuario=rs.getString("loginUsuario");
			estadoAdmision=rs.getString("estadoAdmision");
			medico=rs.getString("primerNombreMedico") + " " + rs.getString("segundoNombreMedico") + " " + rs.getString("primerApellidoMedico") + " "+ rs.getString("segundoApellidoMedico");
		
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos En PageContext : " + sqle.getMessage());
		}

	pageContext.setAttribute("numeroCuenta",  codigoCuenta);
	pageContext.setAttribute("numeroAdmision",  codigoAdmision);
	pageContext.setAttribute("fechaAdmision",  fechaAdmision);
	pageContext.setAttribute("horaAdmision",  horaAdmision);
	if (origenAdmision!=null)
	{
		pageContext.setAttribute("origenAdmision",  origenAdmision);
	}
	else
	{	
		pageContext.setAttribute("origenAdmision",  "");
	}
	
	pageContext.setAttribute("medico",  medico);
	pageContext.setAttribute("causaExterna",  causaExterna);
	pageContext.setAttribute("diagnostico",  diagnostico);
	pageContext.setAttribute("codigoDiagnostico",  codigoDiagnostico);
	pageContext.setAttribute("codigoCIEDiagnostico",  codigoCIEDiagnostico);
	pageContext.setAttribute("numeroCama",  numeroCama);
	pageContext.setAttribute("centroCosto",  centroCosto);
	pageContext.setAttribute("estadoCama",  estadoCama);
	pageContext.setAttribute("tipoUsuarioCama",  tipoUsuarioCama);
	pageContext.setAttribute("descripcionCama",  descripcionCama);
	pageContext.setAttribute("loginUsuario",  loginUsuario);
	if (numeroAutorizacion!=null)
	{
		pageContext.setAttribute("numeroAutorizacion",  numeroAutorizacion);
	}
	else
	{
		pageContext.setAttribute("numeroAutorizacion",  "");
	}
	
	pageContext.setAttribute("estadoAdmision",  estadoAdmision);

	}

	/**
	 * Returns the codigoInstitucion.
	 * @return String
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Returns the con.
	 * @return Connection
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Returns the idBusqueda.
	 * @return String
	 */
	public String getIdBusqueda() {
		return idBusqueda;
	}

	/**
	 * Returns the numeroIdBusqueda.
	 * @return String
	 */
	public String getNumeroIdBusqueda() {
		return numeroIdBusqueda;
	}

	/**
	 * Returns the tipoIdBusqueda.
	 * @return String
	 */
	public String getTipoIdBusqueda() {
		return tipoIdBusqueda;
	}

	/**
	 * Sets the codigoInstitucion.
	 * @param codigoInstitucion The codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Sets the con.
	 * @param con The con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * Sets the idBusqueda.
	 * @param idBusqueda The idBusqueda to set
	 */
	public void setIdBusqueda(String idBusqueda) {
		this.idBusqueda = idBusqueda;
	}

	/**
	 * Sets the numeroIdBusqueda.
	 * @param numeroIdBusqueda The numeroIdBusqueda to set
	 */
	public void setNumeroIdBusqueda(String numeroIdBusqueda) {
		this.numeroIdBusqueda = numeroIdBusqueda;
	}

	/**
	 * Sets the tipoIdBusqueda.
	 * @param tipoIdBusqueda The tipoIdBusqueda to set
	 */
	public void setTipoIdBusqueda(String tipoIdBusqueda) {
		this.tipoIdBusqueda = tipoIdBusqueda;
	}

	/**
	 * Returns the modo.
	 * @return String
	 */
	public String getModo() {
		return modo;
	}

	/**
	 * Sets the modo.
	 * @param modo The modo to set
	 */
	public void setModo(String modo) {
		this.modo = modo;
	}

}