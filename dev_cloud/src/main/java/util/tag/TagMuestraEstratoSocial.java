/*
 * @(#)TagMuestraEstratoSocial.java
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
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import util.Encoder;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;


/**
 * Esta clase maneja la funcionalidad del tag que permite 
 * mostrar todos los estratos sociales permitidos en un
 * determinado tipo de Régimen
 *
 * @version 1.0, Dec 9, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraEstratoSocial extends TagSupport{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Código del tipo de régimen.
	 */
	private String codigoTipoRegimen=null;

	/**
	 * Boolean que indica si debo restringir por activo o no
	 */
	private boolean restringirPorActivo=false;
	/**
	 * Estrato social de la madre
	 */
	private int codigoEstratoMadre=0;
	
	/**
	 * Indica si 
	 * TRUE => los estratos van encerrados en un TD
	 * FALSE => los estratos van encerrados en un TR
	 */
	private boolean sinTr;
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo del tipo de Régimen y solo muestra los estratos
	 * aceptables para este tipo de regimen
	 * Si solo hay un estrato para este tipo de regimen, imprime un
	 * hidden.
	 * @return la constante SKIP_BODY
	 */
	public int doStartTag() throws JspException
	{
	try
		{
			//Hay un caso especial, si el tipo de regimen todavï¿½a no se ha definido
			//debemos mostrar un mensaje, sin necesidad de consultas y demï¿½s
		
			if (codigoTipoRegimen==null||codigoTipoRegimen.equals(""))
			{				
				String out = "";
				if(!sinTr)
					out+="<tr bgcolor=\"#FFFFFF\"><td align=\"right\"><font color=\"#FF0000\">*</font> Clasificaci&oacute;n SocioEcon&oacute;mica:</td><td align=\"left\">" +
					"<select name=\"estrato\"><option value=\"-1\">Seleccione una Convenio</option></select></td></tr>";
				else
					out+="<td ><font color=\"#FF0000\">*</font><b> Clasificaci&oacute;n SocioEcon&oacute;mica:</b>&nbsp;" +
					"<select name=\"estrato\"><option value=\"-1\">Seleccione una Convenio</option></select></td>";

				pageContext.getOut().print(out);
				
				return SKIP_BODY;
			}

			TagDao tagDao;

			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			//Ahora vamos a definir un Vector donde almacenamos todo
			//lo que se va a imprimir, bï¿½sicamente para saber si hay
			//uno o mï¿½s resultados (Dependiendo de esto imprimimos
			//un hidden o un select)
			
			//El vector empieza en 10 posiciones y aumenta 5 posiciones
			//ya que no hay muchos estratos (en el mundo de hoy)
			
			Vector aImprimir= new Vector(10,5);
			Enumeration porRecorrer;

			ResultSetDecorator rs = null;
			int codigoEstrato=-1;
			String estrato="";
			
			rs=tagDao.consultaTagMuestraEstratoSocial (con, codigoTipoRegimen, restringirPorActivo,codigoEstratoMadre);
			
			
		//Con este while vamos a guardar en el vector todos los estratos disponibles
		while (rs.next())
		{
			codigoEstrato=rs.getInt("codigo");
			estrato=rs.getString("descripcion");
			
			if(codigoEstratoMadre==codigoEstrato)
				aImprimir.add("<option value=\"" + codigoEstrato + "-" + estrato  + "\" selected>" + Encoder.encode(estrato) + "</option>");
			else
				aImprimir.add("<option value=\"" + codigoEstrato + "-" + estrato  + "\">" + Encoder.encode(estrato) + "</option>");
		}
		String p1;
		
		
			if(!sinTr)
				p1 = 	
					"<tr bgcolor=\"#FFFFFF\"><td align=\"right\"><font color=\"#FF0000\">*</font> Clasificaci&oacute;n SocioEcon&oacute;mica:</td>" +
					"<td align=\"left\"><select name=\"estrato\" onChange=\"revisionClasificacion(this.form)\" id=\"estrato\"><option value=\"-1\">Seleccione una Clasificación</option>";
			else
				p1 = 	
					"<td ><font color=\"#FF0000\">*</font> <b>Clasificaci&oacute;n SocioEcon&oacute;mica:</b>&nbsp;" +
					"<select name=\"estrato\" onChange=\"revisionClasificacion(this.form)\" id=\"estrato\"><option value=\"-1\">Seleccione una Clasificación</option>";
		
			
			String p2 = "</select></td>";	
			
			if(!sinTr)
				p2 +="</td>";

			pageContext.getOut().print(p1);
			
			porRecorrer=aImprimir.elements();
			while (porRecorrer.hasMoreElements())
			{
				pageContext.getOut().print(porRecorrer.nextElement());
			}

			pageContext.getOut().print(p2);
		
		}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagMuestraEstratoSocial: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagMuestraEstratoSocial: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagMuestraEstratoSocial: "+e.getMessage());
		}

	return SKIP_BODY;
	}

	/**
	 * Metodo necesario al extender la clase <code>TagSupport</code>
	 * en este caso no se usa para nada mï¿½s
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
	 * Retorna el codigo del estrato.
	 * @return String con el codigo del estrato
	 */
	public String getCodigoTipoRegimen() {
		return codigoTipoRegimen;
	}

	/**
	 * Establece el codigo del estrato.
	 * @param codigoTipoRegimen El codigo del estrato a establecer
	 */
	public void setCodigoTipoRegimen(String codigoEstrato) {
		this.codigoTipoRegimen = codigoEstrato;
	}

	/**
	 * @param b
	 */
	public void setRestringirPorActivo(boolean b) {
		restringirPorActivo = b;
	}

	/**
	 * @return Retorna el codigoEstratoMadre.
	 */
	public int getCodigoEstratoMadre() {
		return codigoEstratoMadre;
	}
	/**
	 * @param codigoEstratoMadre Asigna el codigoEstratoMadre.
	 */
	public void setCodigoEstratoMadre(int codigoEstratoMadre) {
		this.codigoEstratoMadre = codigoEstratoMadre;
	}

	/**
	 * @return Returns the sinTr.
	 */
	public boolean isSinTr() {
		return sinTr;
	}

	/**
	 * @param sinTr The sinTr to set.
	 */
	public void setSinTr(boolean sinTr) {
		this.sinTr = sinTr;
	}
}