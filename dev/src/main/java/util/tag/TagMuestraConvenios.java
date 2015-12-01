/*
 * @(#)TagMuestraConvenios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Esta clase maneja la funcionalidad del tag que permite 
 * mostrar todos los convenios existentes en un determinado 
 * tipo de Régimen
 *
 * @version 1.0, Dec 9, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraConvenios extends TagSupport{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;
	
	/**
	 * Fecha para validar los contratos vigentes
	 */
	private String fecha = "";
	
	/**
	 * Si el usuario quiere especificar un código para
	 * que esta opción aparezca seleccionada por defecto
	 */
	private int elementoSeleccionado=0;
	
	/**
	 * Si el usuario quiere que dentro del value
	 * del option solo salga el código, basta con
	 * que ponga este atributo en true
	 */
	private boolean soloCodigo=false;
	
	/**
	 * Si se desea consultar solo convenios capitados
	 */
	private String capitado = "";
	
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo del tipo de Règimen y solo muestra los estratos
	 * aceptables para este tipo de regimen
	 * Si solo hay un estrato para este tipo de regimen, imprime un
	 * hidden.
	 * @return la constante SKIP_BODY
	 */
	public int doStartTag() throws JspException
	{
	try
		{
			TagDao tagDao;

			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();
			
			UsuarioBasico usuario=(UsuarioBasico)pageContext.getSession().getAttribute("usuarioBasico");

			//El vector empieza en 10 posiciones y aumenta 5 posiciones
			//ya que no hay muchos estratos (en el mundo de hoy)

			ResultSetDecorator rs = null;
			String textoSelected="";
			rs=tagDao.consultaTagMuestraConvenios(con, usuario.getCodigoInstitucion(),fecha,capitado);
			while (rs.next())
			{
			    if (rs.getInt("codigo")==this.elementoSeleccionado)
			    {
			        textoSelected=" SELECTED ";
			    }
			    else
			    {
			        textoSelected="";
			    }
			    if (!this.soloCodigo)
			    {
					pageContext.getOut().print("<option value=\"" +  rs.getString("codigo") +"-" + rs.getString("tipo_regimen")  +"-" + rs.getString("nombre")  + "\" "+   textoSelected +" >" + rs.getString("nombre") +(rs.getBoolean("pyp")?"(PYP)":"") + "</option>");
			    }
			    else
			    {
			        pageContext.getOut().print("<option value=\"" +  rs.getString("codigo") + "\" "+   textoSelected +" >" + rs.getString("nombre") +(rs.getBoolean("pyp")?"(PYP)":"") + "</option>");
			    }
			}
			
		
		}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagMuestraConvenios: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagMuestraConvenios: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagMuestraConvenios: "+e.getMessage());
		}

	return SKIP_BODY;
	}

	/**
	 * Metodo necesario al extender la clase <code>TagSupport</code>
	 * en este caso no se usa para nada más
	 * @return la constante EVAL_PAGE
	 */
	public int doEndTag() {
		return EVAL_PAGE;
	}

	/**
	 * Metodo "Set" que recibe una conexion
	 * para permitir manejar todos los tags
	 * de una misma pagina con la misma
	 * conexion
	 * @param con conexion
	 */
	public void setCon (Connection con)	{
		this.con=con;
	}
	
	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

    /**
     * @return Retorna el/la elementoSeleccionado.
     */
    public int getElementoSeleccionado() {
        return elementoSeleccionado;
    }
    /**
     * El/La elementoSeleccionado a establecer.
     * @param elementoSeleccionado 
     */
    public void setElementoSeleccionado(int elementoSeleccionado) {
        this.elementoSeleccionado = elementoSeleccionado;
    }

    /**
     * @return Retorna el/la soloCodigo.
     */
    public boolean getSoloCodigo() {
        return soloCodigo;
    }
    /**
     * El/La soloCodigo a establecer.
     * @param soloCodigo 
     */
    public void setSoloCodigo(boolean soloCodigo) {
        this.soloCodigo = soloCodigo;
    }

	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return Returns the capitado.
	 */
	public String getCapitado() {
		return capitado;
	}

	/**
	 * @param capitado The capitado to set.
	 */
	public void setCapitado(String capitado) {
		this.capitado = capitado;
	}
}