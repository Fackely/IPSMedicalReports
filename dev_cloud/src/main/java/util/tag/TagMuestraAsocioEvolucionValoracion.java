/*
 * @(#)TagMuestraAsocioEvolucionValoracion.java
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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.ConstantesBD;
import util.RespuestaValidacion;
import util.UtilidadFecha;
import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Tag que dado el código de la cuenta y el médico muestra ( define 
 * variables ) las valoraciones a las que puede estar asociado una 
 * futura evolución.
 *  
 *	@version 1.0, Oct 17, 2003
 */
public class TagMuestraAsocioEvolucionValoracion extends BodyTagSupport
{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * <code>ResultSet</code> con los resultados de la consulta de Admision
	 * Hospitalaria
	 */
	private ResultSetDecorator rs = null;

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	 * Código de la cuenta abierta del paciente
	 */
	private int idCuenta=0;
	
	/**
	 * Código de la cuenta abierta del paciente
	 */
	private int idCuentaAsociada=0;
	
	/**
	 * Médico que está llenando la posible evolución
	 */
	private UsuarioBasico medico = null;

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		//No se valida que los codigos sean 0, pues el paciente
		//puede tener la cuenta cerrada
		
		HttpSession session=pageContext.getSession();
		
		//Si el parametro pasado es nulo, toma los de la sesión actual
		if (medico==null)
		{
			medico=(UsuarioBasico)session.getAttribute("usuarioBasico");
			PersonaBasica paciente=(PersonaBasica)session.getAttribute("pacienteActivo");
			idCuenta=paciente.getCodigoCuenta();
			if(paciente.getExisteAsocio())
				this.setIdCuentaAsociada(paciente.getCodigoCuentaAsocio());
			else
				this.setIdCuentaAsociada(0);
		}
		
		if (idCuenta>0&&medico!=null) 
		{
			abrirResultSet();
			try 
			{
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
				throw new JspTagException("Error Escribiendo El TagMuestraAsocioEvolucionValoracion  : "+e.getMessage());
			}
		}
		else 
		{
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
			try 
			{
				bodyContent.clear();
			}
			catch (IOException ex) 
			{
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del TagMuestraAsocioEvolucionValoracion");
			}
		}

		try 
		{
			
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

			throw new JspTagException("Error Recorriendo El TagMuestraAsocioEvolucionValoracion : "+e.getMessage());
		}

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
				bodyContent.getEnclosingWriter().write(output.toString());
				clean();
			}
			catch (Exception e) 
			{

				throw new JspTagException("Error Escribiendo El TagMuestraAsocioEvolucionValoracion: "+e.getMessage());
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
		output = null;
		idCuenta=0;
		medico=null;
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
			this.rs=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao().consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones(con, idCuenta, idCuentaAsociada);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En TagMuestraAsocioEvolucionValoracion : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{
		String fechaValoracion, tipoValoracion, especialidad ;
		UsuarioBasico medicoResultadoValoracion;
		HttpSession session=pageContext.getSession();
		PersonaBasica paciente=(PersonaBasica)session.getAttribute("pacienteActivo");
		int numeroSolicitud;
		try 
		{
			numeroSolicitud=rs.getInt("numeroSolicitud");

			//Revisamos si este numero nos sirve, de acuerdo a los 
			//permisos
			RespuestaValidacion resp=UtilidadValidacion.validacionAccesoSolicitud(con, numeroSolicitud, ConstantesBD.solicitudValoracion, medico, paciente);
			
			if (resp.puedoSeguir)
			{
				//Ahora si preguntamos el resto

				fechaValoracion=rs.getString("fechaValoracion");
				tipoValoracion=rs.getString("tipoValoracion");

				pageContext.setAttribute("fechaValoracion",  UtilidadFecha.conversionFormatoFechaAAp(fechaValoracion));
				pageContext.setAttribute("tipoValoracion",  tipoValoracion);
				pageContext.setAttribute("numeroSolicitud",  numeroSolicitud + "");
				
				//Solo si el tipo de valoracion es interconsulta, tomamos
				//los otros datos de BD
				
				if (tipoValoracion.equals("interconsulta"))
				{
					medicoResultadoValoracion=new UsuarioBasico();
					medicoResultadoValoracion.cargarUsuarioBasico(con, rs.getString("tipoIdMedico"), rs.getString("numIdMedico"));
					//Solo lo asignamos en caso interconsulta
					pageContext.setAttribute("medicoResultadoValoracion", medicoResultadoValoracion);
					especialidad=rs.getString("especialidad");
				}
				else
				{
					especialidad="vacio";
				}
				
				pageContext.setAttribute("especialidad",  especialidad);
				pageContext.setAttribute("numeroSolicitud",  numeroSolicitud + "");
				pageContext.setAttribute("entradaValida",  "true");
				
			}
			else
			{
				//No es una entrada válida
				pageContext.setAttribute("entradaValida",  "false");
			}

		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos del  TagMuestraAsocioEvolucionValoracion En PageContext : " + sqle.getMessage());
		}

	
	}

	/**
	 * @return
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * @return
	 */
	public int getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @return
	 */
	public UsuarioBasico getMedico() {
		return medico;
	}

	/**
	 * @param connection
	 */
	public void setCon(Connection connection) {
		con = connection;
	}

	/**
	 * @param i
	 */
	public void setIdCuenta(int i) {
		idCuenta = i;
	}

	/**
	 * @param basico
	 */
	public void setMedico(UsuarioBasico basico) {
		medico = basico;
	}

	/**
	 * @return
	 */
	public int getIdCuentaAsociada()
	{
		return idCuentaAsociada;
	}

	/**
	 * @param i
	 */
	public void setIdCuentaAsociada(int i)
	{
		idCuentaAsociada= i;
	}

}
