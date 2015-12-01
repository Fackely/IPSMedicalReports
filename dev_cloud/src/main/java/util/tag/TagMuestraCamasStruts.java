/*
 * @(#)TagMuestraCamasStruts.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.Encoder;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase implementa un tag que permite, dado el nombre de un parámetro,
 * recuperar de la Base de Datos el valor por defecto respectivo. Define
 * las <i>scripting variables</i> <b>valor</b> y <b>nombre</b>, y se apoya en la
 * clase <code>TEIValoresPorDefecto</code>.
 *
 * @version 1.0, Feb 24, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;pez</a>
 * @author 	<a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
 */

public class TagMuestraCamasStruts extends BodyTagSupport {

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * String con el sexo del futuro paciente
	 */

	private String sexo = "";
	
	/**
	 * String con la edad. Porqué un String?, porque en el caso general me llega
	 * esto como parámetro y es mejor hacer la conversión en la funcionalidad
	 * (java) y no en la presentación (jsp)
	 */
	private String edad = "";
	
	/**
	 * codigo de la institucion
	 */
	private String codigoInstitucion="";
	
	/**
	 * String con el codigo del centro del costo del cual se requiere obtener
	 * las camas
	 */	
	private String codigoCentroCosto= "";
	
	/**
	 * Cadena con el codigo del estado del cual se quieren obtener las camas
	 */
	private String codEstado = "";
	
	 /**
	  * Flag que indica si se van a consultar las camas de un cc específico
	  * en cuyo caso no 'imprime' el cc.
	  */
	private boolean isCCDefinido = false;
	
	/**
	 * <code>Collection</code> con el <code>resultSet</code> de la consulta de
	 * las camas
	 */		  
	private Collection resultados = null;
	
	/**
	 * Iterador que usamos para recorrer la Collection de los resultados
	 */
	private Iterator iter = null;
	
	/**
	 * En este Buffer vamos guardando los resultados a 'imprimir' en el JSP
	 */
	private StringBuffer output = null;	
	
	//******ATRIBUTOS VALIDACION CENTRO COSTO***********
	/**
	 * Código del centro atencion
	 */
	private String centroAtencion;
	/**
	 * Código de la vía de ingreso
	 */
	private String viaIngreso;

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexión usada por el tag
	 */
	public Connection getCon ()	
	{
		return con;
	}

	/**
	 * Método "Set" que recibe una conexion para permitir manejar todos los tags
	 * de una misma pagina con la misma conexión
	 * @param con conexion
	 */
	public void setCon (Connection con)	
	{
		this.con = con;
	}

	/**
	 * Al trabajar con Tags siempre tenemos que definir al menos este método.
	 * Siempre se ejecuta antes que doEndTag. Para los Tags hechos anteriormente
	 * se manejaba toda la funcionalidad en este método, ahora lo limitamos
	 * únicamente a validar que los datos que llegan ni sean nulos, ni sean
	 * vacios
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		if( sexo != null && !sexo.trim().equals("") || 
			edad!= null && !edad.trim().equals("") || 
			codigoInstitucion!= null && !codigoInstitucion.trim().equals("") || 
			codigoCentroCosto != null && !codigoCentroCosto.trim().equals("") || 
			codEstado != null && !codEstado.trim().equals("") ) 
		{
			this.getData();
			if(this.resultados.size() > 0) 
			{				
				this.iter = this.resultados.iterator();
				if (iter.hasNext()) 
				{
					this.output = new StringBuffer();
					this.putData();
					return EVAL_BODY_BUFFERED;
				}				
			}
		}
		return SKIP_BODY;

	}
	
	/**
	 * Procesa el cuerpo del tag
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
				throw new JspTagException("Error I/O Fatal limpiando el directorio del cuerpo del tag");
			}
		}
		/* Continuando mientras haya resultados por mostrar */
		if (iter.hasNext())
		{
			this.putData();
			return EVAL_BODY_AGAIN;
		}
		 //Si ya no hay resultados 
		else 
		{
			return SKIP_BODY;
		}
	}
	
	/**
	 * Este método escribe en la página el valor y nombre por defecto pedidos
	 * según el parámetro, usando la presentación presente en la página. También
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException 
	{
		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output != null && output.length()>0)
		{

			try 
			{
				bodyContent.getEnclosingWriter().write(output.toString());
			}	
			catch (Exception e) 
			{
				e.printStackTrace();
				throw new JspTagException("Error Escribiendo TagMuestraCamasStruts : " + e.getMessage());
			}
		}
		clean();
		return EVAL_PAGE;
	}

	/**
	 * Como Tomcat a partir de su versión 4.1.12 ? maneja un pool de Tags,
	 * tenemos que asegurarnos de limpiar los datos que recibimos, si no cuando
	 * Tomcat reutiliza este Tag nos vamos a encontrar con datos viejos /
	 * inconsistentes
	 */
	private void clean() 
	{
		this.sexo = "";
		this.edad = "";
		this.codigoInstitucion="";
		this.codigoCentroCosto = "";
		this.codEstado = "";
		this.resultados = null;
		this.iter = null;
		this.output = null;
		this.centroAtencion = "";
		this.viaIngreso = "";
	}

	/**
	 * Este método se conecta con la Base de Datos y trae los resultados de 
	 * la consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados
	 */	
	private void getData() throws JspTagException 
	{
		TagDao tagDao;

		ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		ResultSetDecorator rs;

		resultados = new LinkedList();		
		
		if (!this.codigoCentroCosto.equals(""))
		{
			isCCDefinido = true;			
		}

		try 
		{
			rs=tagDao.consultaTagMuestraCamasStruts (con, sexo, this.codigoCentroCosto, this.codEstado, this.codigoInstitucion, this.centroAtencion, this.viaIngreso) ;

			while (rs.next())	
			{
				Collection row = new LinkedList();
				row.add(rs.getString("codigo"));
				row.add(Encoder.encode(rs.getString("numeroCama")));
				if(!isCCDefinido)
				{
					row.add(rs.getString("centroCosto"));
					row.add(rs.getString("nombreCentroCosto"));					
				}
				row.add(rs.getString("estadoCama"));
				row.add(Encoder.encode(rs.getString("tipoUsuario")));
				row.add(rs.getString("descripcion"));
								
				row.add(rs.getString("habitacion"));
				resultados.add(row);
			}

		}	
		catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Camas Struts: " + sqle.getMessage());
		}
	}
	
	/**
	 * Este método pone en PageContext los valores de las <i>scripting
	 * variables</i> para cada iteración del tag
	 */
	 public void putData() 
	 {
	 	String codigo = "";
	 	String nombre = "";
	 	String codigoCentroCosto = "";
	 	String estado = "";
	 	String nombreCentroCosto = "";
	 	String tipoUsuario = "";
	 	String descripcion = "";
	 	String habitacion="";
	 	Iterator i = ((Collection) this.iter.next()).iterator();
	 	codigo = (String)i.next();
	 	nombre = (String)i.next();	 	
	 	
	 	if(!this.isCCDefinido)
	 	{
	 		codigoCentroCosto = (String)i.next();
	 		nombreCentroCosto = (String)i.next();
	 	}
		estado = (String)i.next();	 	
		tipoUsuario = (String)i.next();
		descripcion = (String)i.next();
		habitacion=(String)i.next();
		
		pageContext.setAttribute("codigo" ,  codigo);
		pageContext.setAttribute("nombre",   nombre);
		pageContext.setAttribute("codigoCentroCosto", codigoCentroCosto);
		pageContext.setAttribute("nombreCentroCosto", nombreCentroCosto);
		pageContext.setAttribute("estado", estado);		
		pageContext.setAttribute("tipoUsuario", tipoUsuario);
		pageContext.setAttribute("descripcion", descripcion);								
		pageContext.setAttribute("habitacion", habitacion);
	 }
	 
	/**
	 * Establece la edad del futuro paciente.
	 * @param edad La edad a establecer
	 */
	public void setEdad(String edad) 
	{
		this.edad = edad;
	}

	/**
	 * Establece el sexo del futuro paciente.
	 * @param sexo El sexo a establecer
	 */
	public void setSexo(String sexo) 
	{
		this.sexo = sexo;
	}

	/**
	 * Sets the codigoCentroCosto.
	 * @param codigoCentroCosto The codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(String codigoCentroCosto) 
	{
		this.codigoCentroCosto = codigoCentroCosto;
	}
	
	/**
	 * Returns the codigoCentroCosto.
	 * @return String
	 */
	public String getCodigoCentroCosto() 
	{
		return codigoCentroCosto;
	}
	
	/**
	 * Retorna la cadena con el codigo del estado del cual se quieren obtener
	 * las camas
	 * @return String
	 */
	public String getCodEstado()
	{
		return codEstado;
	}

	/**
	 * Asigna la cadena con el codigo del estado del cual se quieren obtener las
	 * camas
	 * @param codEstado The codEstado to set
	 */
	public void setCodEstado(String codEstado)
	{
		this.codEstado = codEstado;
	}

    /**
     * @return Returns the codigoInstitucion.
     */
    public String getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion The codigoInstitucion to set.
     */
    public void setCodigoInstitucion(String codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
    }

	/**
	 * @return Returns the centroAtencion.
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Returns the viaIngreso.
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
}