/*
 * @(#)TagEdita CamposDinamicos.java
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

import org.apache.log4j.Logger;



import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Esta clase implementa un tag que permite, dado el nombre de un parámetro,
 * recuperar de la Base de Datos el valor por defecto respectivo. Define
 * las <i>scripting variables</i> <b>valor</b> y <b>nombre</b>, y se apoya en la
 * clase <code>TEIValoresPorDefecto</code>.
 *
 */

public class TagEditaCamposDinamicos extends BodyTagSupport {
	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagEditaCamposDinamicos.class);
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;
	
	/**
	 * Entero que representa el id del medico,
	 * Se recibe como un entero porque inicialmente esta
	 * funcionalidad era solo para médicos, pero como ahora
	 * puede ser por centro de costo o por institución,
	 * se debe cargar el paciente (cuando haya tiempo se
	 * puede hacer un refactoring, pasando el objeto 
	 * UsuarioBasico; en ese momento no olvide hacer múltiples
	 * pruebas, porque el tag se usa en muchas partes)
	 */
	private int idMedico=0;
	
	/**
	 *
	 *String que representa la funcionalidad donde se encuentra el usuario del tag
	 */
	private String idFuncionalidad="";
	/**
	 *
	 *String que representa la seccion dentro de la funcionalidad donde se encuentra el usuario del tag
	*/
	private String idSeccion="";
	
	/**
	 * Manejo del centro de costo del paciente
	 */
	private int centroCosto=0;
		
	/*
	 * <code>Collection</code> con el <code>resultSet</code> de la consulta de
	 * las camas
	 */		  
	private Collection resultados = null;
	/*
	 * Iterador que usamos para recorrer la Collection de los resultados
	 */
	private Iterator iter = null;
	/*
	 * En este Buffer vamos guardando los resultados a 'imprimir' en el JSP
	 */
	private StringBuffer output = null;	

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexión usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

	/**
	 * Método "Set" que recibe una conexion para permitir manejar todos los tags
	 * de una misma pagina con la misma conexión
	 * @param con conexion
	 */
	public void setCon (Connection con)	{
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
	public int doStartTag() throws JspTagException {
		if (idMedico>0&& idFuncionalidad != null && !idFuncionalidad.trim().equals("")) {
			this.getData();
			if(this.resultados.size() > 0) {				
				this.iter = this.resultados.iterator();
				if (iter.hasNext()) {
					this.output = new StringBuffer();
					this.putData();
					return EVAL_BODY_BUFFERED;
				}				
			}
		}
		return SKIP_BODY;

	}
	/*
	 * Procesa el cuerpo del tag
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspTagException {
		BodyContent bodyContent = getBodyContent();
		if (bodyContent != null){
			output.append(bodyContent.getString());
			try {
				bodyContent.clear();
			}
			catch (IOException ex){
				throw new JspTagException("Error I/O Fatal limpiando el directorio del cuerpo del tag");
			}
		}
		/* Continuando mientras haya resultados por mostrar */
		if (iter.hasNext()){
			this.putData();
			return EVAL_BODY_AGAIN;
		}
		/* Si ya no hay resultados */
		else {
			return SKIP_BODY;
		}
	}
	
	/**
	 * Este método escribe en la página el valor y nombre por defecto pedidos
	 * según el parámetro, usando la presentación presente en la página. También
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output != null && output.length()>0) {

			try {
				bodyContent.getEnclosingWriter().write(output.toString());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error Escribiendo TagEditaCamposDinamicos : " + e.getMessage());
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
		this.idMedico = 0;
		this.idFuncionalidad = "";
		this.idSeccion = "";
		this.resultados = null;
		this.iter = null;
		this.output = null;
		this.centroCosto = 0;
	}

	/*
	 * Este método se conecta con la Base de Datos y trae los resultados de 
	 * la consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados
	 */
	
	private void getData() throws JspTagException {

		TagDao tagDao;

		ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		ResultSetDecorator rs;

		resultados = new LinkedList();		
		
		try {
			UsuarioBasico medico=new UsuarioBasico();
		    medico.cargarUsuarioBasico(con, idMedico);
			if(idSeccion != null && !idSeccion.trim().equals("")){
				rs=tagDao.consultaTagEditaCamposDinamicosSeccion (con, idMedico, idFuncionalidad,idSeccion, centroCosto, medico.getCodigoInstitucion()) ;
			}else{
				rs=tagDao.consultaTagEditaCamposDinamicos (con, idMedico,  idFuncionalidad, centroCosto, medico.getCodigoInstitucion()) ;

			}

			while (rs.next())	{
				Collection row = new LinkedList();
				row.add(rs.getString("tipo"));
				row.add(rs.getString("orden"));
				row.add(rs.getString("nombre"));
				row.add(rs.getString("seccion"));
				row.add(idFuncionalidad.toString());
				resultados.add(row);
			}

		}	catch (SQLException sqle) {
				sqle.printStackTrace();
				throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El TagEditaCamposDinamicosSeccion: " + sqle.getMessage());
		}

	}
	/*
	 * Este método pone en PageContext los valores de las <i>scripting
	 * variables</i> para cada iteración del tag
	 */
	 public void putData() {
		
	
		String tipoCampo = "";
		String nombreCampo = "";
		String ordenCampo = "";
		String seccionCampo="";
		String funcionalidadCampo="";
		
		
		
		Iterator i = ((Collection) this.iter.next()).iterator();
		tipoCampo=(String)i.next();
		ordenCampo=(String)i.next();
		nombreCampo=(String)i.next();
		seccionCampo=(String)i.next();
		funcionalidadCampo=(String)i.next();
						 		

		pageContext.setAttribute("tipoCampo" , tipoCampo);
		pageContext.setAttribute("nombreCampo" , nombreCampo);
		pageContext.setAttribute("ordenCampo" , ordenCampo);
		pageContext.setAttribute("seccionCampo" , seccionCampo);
		pageContext.setAttribute("funcionalidadCampo" , funcionalidadCampo);
		
		
		
				
	 }


	/**
	 * @return
	 */
	public String getIdFuncionalidad() {
		return idFuncionalidad;
	}

	/**
	 * @return
	 */
	public int getIdMedico() {
		return idMedico;
	}

	/**
	 * @return
	 */
	public String getIdSeccion() {
		return idSeccion;
	}

	/**
	 * @param string
	 */
	public void setIdFuncionalidad(String string) {
		idFuncionalidad = string;
	}

	/**
	 * @param string
	 */
	public void setIdMedico(int  idMedico) {
		this.idMedico = idMedico ;
	}

	/**
	 * @param string
	 */
	public void setIdSeccion(String string) {
		idSeccion = string;
	}

	/**
	 * @return Retorna centroCosto.
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna centroCosto.
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
	}

}