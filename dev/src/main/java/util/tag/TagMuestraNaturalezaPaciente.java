/*
 * @(#)TagMuestraNaturalezaPaciente.java
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
 * mostrar la naturaleza de un paciente dada su edad, su 
 * sexo y el tipo de R&eacute;gimen al que pertenece
 *
 * @version 1.0, Dec 9, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraNaturalezaPaciente extends TagSupport{

	/**
	 * Objeto que representa una conexin con una base de datos.
	 */
	private Connection con = null;
	
	/**
	 * Cdigo del tipo de régimen.
	 */
	private String codigoTipoRegimen=null;
	
	/**
	 * Edad del paciente.
	 */
	private int edad;
	
	/**
	 * Sexo del paciente.
	 */
	private String sexo=null;
	
	/**
	 * Codigo de la naturaleza del paciente que viene de una cuenta (NO requerido)
	 */
	private String naturalezaPacienteCuenta=null;
	
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
			//Hay un caso especial, si el tipo de regimen todavía no se ha definido
			//debemos mostrar un mensaje, sin necesidad de consultas y demás
			if (codigoTipoRegimen==null||codigoTipoRegimen.equals(""))
			{
				String out = "";
				if(!sinTr)
					out +="<tr bgcolor=\"#FFFFFF\"><td align=\"right\">Naturaleza del Paciente:</font></td><td align=\"left\">" +
					"<select name=\"naturalezaPaciente\" id=\"naturalezaPaciente\"><option value=\"-1\">Seleccione una Naturaleza</option></select>"+
					"</td></tr>";
				else
					out +="<td ><b>Naturaleza del Paciente:</b>&nbsp;" +
					"<select name=\"naturalezaPaciente\" id=\"naturalezaPaciente\"><option value=\"-1\">Seleccione una Naturaleza</option></select>"+
					"</td>";

				pageContext.getOut().print(out);
				return SKIP_BODY;
			}
			TagDao tagDao;

			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			//Ahora vamos a definir un Vector donde almacenamos todo
			//lo que se va a imprimir, básicamente para saber si hay
			//uno o más resultados (Dependiendo de esto imprimimos
			//un hidden o un select)
			
			//El vector empieza en 10 posiciones y aumenta 5 posiciones
			//ya que no hay muchos estratos (en el mundo de hoy)
			
			//Para el caso de los regimenes contributivo, particular
			//y otro utilizamos 
			if (codigoTipoRegimen.equals("C")||codigoTipoRegimen.equals("P")||codigoTipoRegimen.equals("O"))
			{
				pageContext.getOut().print("<INPUT TYPE=\"HIDDEN\" NAME=\"naturalezaPaciente\" VALUE=\"0-Ninguno\">");
				return SKIP_BODY;
			}

			Vector aImprimir= new Vector(10,5);
			Enumeration porRecorrer;

			ResultSetDecorator rs = null;
			rs=tagDao.consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (con, sexo);
			 
			//Con este while vamos a guardar en el vector todos los estratos disponibles
			
			while (rs.next())
			{
				String selected = "";
				if(this.naturalezaPacienteCuenta!=null){
					if(rs.getString("codigo").equals(this.naturalezaPacienteCuenta)){
						selected = "selected"; 
					}
				}
				aImprimir.add("<option value=\"" + rs.getString("codigo") + "-"+ rs.getString("nombre")  + "\" "+ selected +">" + Encoder.encode(rs.getString("nombre") ) + "</option>");
			}
			String p1 = "";
			if(!sinTr)
				p1 += "<tr bgcolor=\"#FFFFFF\"><td align=\"right\">Naturaleza del Paciente:</font></td>" +
					"<td align=\"left\"><select name=\"naturalezaPaciente\" id=\"naturalezaPaciente\">";
			else
				p1 += "<td ><b>Naturaleza del Paciente:</b>&nbsp;" +
				"<select name=\"naturalezaPaciente\" id=\"naturalezaPaciente\">";
	
	
	
			String p2 = "</select></td>";
			if(!sinTr)
				p2 += "</tr>";
	
			//Ahora preguntamos por el tamao, si este es mayor que 1, lo imprimimos como
			//un select, si no lo imprimimos como un hidden
			pageContext.getOut().print(p1);
			porRecorrer=aImprimir.elements();
	
			//Aunque ya tenemos en aImprimir todos los elementos, ahora utilizamos otras
			//consultas para manejar las preferencias que son:
	
			//-Si es de mas de 65 años, poner los datos de la opción 3
			//-Si es menor de un ao, poner la opción 2
			//-Si es mujer mayor de 12 aos, poner la opción 1
	
			//Como se quería cablear lo menos posible, se hace una búsqueda
			//por código, de tal manera que si en el futuro se cambian los cdigos
			//el tag no mostrará la opción por defecto actual, pero mostraría una
			//váida en la base de datos
	
			//La primera consulta que vamos a hacer es la consulta para
			//mayores de 65 años
			if (edad>65)
			{
					rs =tagDao.consultaTagMuestraNaturalezaPaciente_Mayores65 (con);
	
				//Con este while vamos a guardar en el vector todos los estratos disponibles
				while (rs.next())
				{
					pageContext.getOut().print("<option value=\"" + rs.getString("codigo") + "-"+ rs.getString("nombre")  + "\">" + Encoder.encode(rs.getString("nombre") ) + "</option>");
				}
			}
			else if (edad<1)
			{
					rs=tagDao.consultaTagMuestraNaturalezaPaciente_Bebe (con) ;
	
				//Con este while vamos a guardar en el vector todos los estratos disponibles
				while (rs.next())
				{
					pageContext.getOut().print("<option value=\"" + rs.getString("codigo") + "-"+ rs.getString("nombre")  + "\">" + Encoder.encode(rs.getString("nombre") ) + "</option>");
				}
			}
			/*Ya no se maneja el caso de embarazada
			else if (edad>12&&sexo.equals("2"))
			{
				rs=tagDao.consultaTagMuestraNaturalezaPaciente_Naturaleza1 (con) ;
	
				//Con este while vamos a guardar en el vector todos los estratos disponibles
				while (rs.next())
				{
					pageContext.getOut().print("<option value=\"" + rs.getString("codigo") + "-"+ rs.getString("nombre")  + "\">" + Encoder.encode(rs.getString("nombre") ) + "</option>");
				}
			}*/
			//Si no ocurre ninguno de los casos mencionados aparece la opción ninguno
			//que el usuario ingrese la opcin
			/*
			else
			{
				rs=tagDao.consultaTagMuestraNaturalezaPaciente_Naturaleza0 (con) ;
	
				//Con este while vamos a guardar en el vector todos los estratos disponibles
				while (rs.next())
				{
					pageContext.getOut().print("<option value=\"" + rs.getString("codigo") + "-"+ rs.getString("nombre")  + "\">" + Encoder.encode(rs.getString("nombre") ) + "</option>");
				}
			}*/
	
			while (porRecorrer.hasMoreElements())
			{
				pageContext.getOut().print( porRecorrer.nextElement() );
			}
	
			pageContext.getOut().print(p2);
	

		}
		catch (java.io.IOException e)
		{
			throw new JspTagException("TagMuestraNaturalezaPacientes: "+e.getMessage());
		}
		catch (java.sql.SQLException e)
		{
			throw new JspTagException("TagMuestraNaturalezaPacientes: "+e.getMessage());
		}
		catch (Exception e)
		{
			throw new JspTagException("TagMuestraNaturalezaPacientes: "+e.getMessage());
		}

		return SKIP_BODY;
	}

	/**
	 * Metodo necesario al extender la clase <code>TagSupport</code>
	 * en este caso no se usa para nada mï¿½
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
	 * Retorna la edad.
	 * @return int con la edad
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * Retorna el sexo.
	 * @return String con el sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * Establece la edad.
	 * @param edad La edad a establecer
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}

	/**
	 * Establece el sexo.
	 * @param sexo El sexo a establecer
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public void setNaturalezaPacienteCuenta(String naturalezaPacienteCuenta) {
		this.naturalezaPacienteCuenta = naturalezaPacienteCuenta;
	}

	public String getNaturalezaPacienteCuenta() {
		return naturalezaPacienteCuenta;
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
