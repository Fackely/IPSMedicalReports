/*
 * @(#)TagMuestraContratosVigentes.java
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
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.Answer;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Tag que conserva los contratos que todavía 
 * están vigentes (fecha finalización > sistema), además
 * muestra su correspondiente descripción.
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 */
public class TagMuestraContratosVigentes extends BodyTagSupport
{
	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagMuestraContratosVigentes.class);
	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "-";
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;

	/**
	 * String con el valor de la fecha actual para hacer la comparación de la fecha de vigencia del contrato
	 * restricción
	 */
	private String rest1valorFecha;
	
	/**
	 * String para hacer la restricción del convenio
	 */
	private String rest2valorCodigoConvenio;
	
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP).
	 */
	public int doStartTag() throws JspTagException
	{
			funcionalidad();
			return EVAL_BODY_BUFFERED;
	}
	
	public int doEndTag() throws JspTagException
	{
		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Muestra Contratos vigentes : " + e.getMessage()+e.toString());
			}
		}
		clean();
		return EVAL_PAGE;
	}
	
	private void funcionalidad() throws JspTagException
	{
		try
		{
			TagDao tagDao;
			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");

			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			Answer a;
			ResultSetDecorator rs = null;
			String consulta="";
			
			consulta= "SELECT codigo, numero_contrato, to_char(fecha_inicial,'dd/mm/yyyy') as fechaInicial, to_char(fecha_final,'dd/mm/yyyy') as fechaFinal from contratos WHERE convenio = "+rest2valorCodigoConvenio+ " AND fecha_final >= '"+rest1valorFecha+"' order by fechaInicial desc" ;
			
			a=tagDao.resultadoConsulta(con, consulta);

			rs  = a.getResultSet();
			con = a.getConnection();
			
			//Variables locales para no tener problemas con pools de conexiones

			Vector resultados=new Vector(15,5);
			String parejasResultado="";

			boolean primerEncuentro;
			
			while (rs.next())
			{
				//No he encontrado el primer campo, en cuyo caso NO debo poner
				//guión anterior
				primerEncuentro=true;
				parejasResultado="";
				
				//Si es la primera vez que lo encontramos, ponemos el valor
				//en true y si no ponemos guion anterior
				if (primerEncuentro==true)
				{
					  primerEncuentro=false;
				}
				else
				{
					  parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(1);
				
				if (primerEncuentro==true)
				{
						primerEncuentro=false;
				}
				else
				{
						parejasResultado= parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(2);
				
				if (primerEncuentro==true)
				{
						primerEncuentro=false;
				}
				else
				{
						parejasResultado= parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(3);
				
				if (primerEncuentro==true)
				{
						primerEncuentro=false;
				}
				else
				{
						parejasResultado= parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(4);
				
				resultados.add(parejasResultado);  
			}	
			
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
		{
			logger.warn(e.getMessage());
			throw new JspTagException("TagMuestraContratosVigentes: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
		throw new JspTagException("TagMuestraContratosVigentes: "+e.getMessage()+e.toString());
		}
	}
	
	/**
	 * resetea los valores
	 */
	private void clean()
	{
		is_separador = "-";
		this.rest1valorFecha = "";
		this.rest2valorCodigoConvenio="";
	}	
	
	/** Obtiene el separador de la cadena de resultados */
	public String getSeparador()
	{
		return is_separador;
	}
	
	/** Asigna el separador de la cadena de resultados */
	public void setSeparador(String as_separador)
	{
		if(as_separador != null && !(as_separador = as_separador.trim() ).equals("") )
			is_separador = as_separador;
	}

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon()
	{
		return con;
	}

	/**
	 * Metodo "Set" que recibe una conexion
	 * para permitir manejar todos los tags
	 * de una misma pagina con la misma
	 * conexion
	 * @param con conexion
	 */
	public void setCon(Connection connection) 
	{
		con = connection;
	}

	/**
	 * @return Returns the rest1valorFecha.
	 */
	public String getRest1valorFecha() {
		return this.rest1valorFecha;
	}
	/**
	 * @param rest1valorFecha The rest1valorFecha to set.
	 */
	public void setRest1valorFecha(String rest1valorFecha) {
		this.rest1valorFecha = rest1valorFecha;
	}
	/**
	 * @return Returns the rest2valorCodigoConvenio.
	 */
	public String getRest2valorCodigoConvenio() {
		return rest2valorCodigoConvenio;
	}
	/**
	 * @param rest2valorCodigoConvenio The rest2valorCodigoConvenio to set.
	 */
	public void setRest2valorCodigoConvenio(String rest2valorCodigoConvenio) {
		this.rest2valorCodigoConvenio = rest2valorCodigoConvenio;
	}
}
