/*
 * @(#)TagMuestraPersonasNombreApellidoStruts.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
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

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase maneja la funcionalidad del tag que permite
 * seleccionar cualquier persona (acompa�anantes, pacientes,
 * usuarios y m�dicos) viendo sus nombres y apellidos,
 * en la busqueda para modificarlos o eliminarlos.
 *
 * @version 1.0, Mar 10, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class TagMuestraPersonasNombreApellidoStruts  extends BodyTagSupport
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(Utilidades.class);
	
	/**
	 * Objeto que representa una conexi�n con una base de datos.
	 */
	private Connection con=null;
	
	/**
	 * Objeto que representa el separador de los resultados.
	 */
	private String separador="-";
	/**
	 * String en donde viene con quien estoy trabajando
	 * (puede ser para modificar , buscar un m�dico, un
	 * usuario, un acompa�ante o un paciente).
	 */
	private String tipoPersonaBuscada=null;

	/**
	 * C�digo de la instituci�n.
	 */
	private String codigoInstitucion="";
	
	/**
	 * C�digo del centro de costo
	 */
	private String codigoCentroCosto = ""; 

	/**
	 * Al trabajar con Tags siempre tenemos que definir al menos este m�todo.
	 * Siempre se ejecuta antes que doEndTag. Para los Tags hechos anteriormente
	 * se manejaba toda la funcionalidad en este m�todo, ahora lo limitamos
	 * �nicamente a validar que los datos que llegan ni sean nulos, ni sean
	 * vacios
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException
	{
		//El �nico par�metro que no revisamos es el login pues no siempre es necesario
		if (tipoPersonaBuscada != null && !tipoPersonaBuscada.trim().equals("")||codigoInstitucion!= null && !codigoInstitucion.trim().equals(""))
		{
			funcionalidad();
			return EVAL_BODY_BUFFERED;
		}
		else
		{
			return SKIP_BODY;
		}
	}

	/**
	 * Este m�todo escribe en la p�gina el valor y nombre por defecto pedidos
	 * seg�n el par�metro, usando la presentaci�n presente en la p�gina. Tambi�n
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo TagMuestraPersonaNombreApellido : " + e.getMessage());
			}

		}

		clean();
		return EVAL_PAGE;
	}

	/**
	 * Como Tomcat a partir de su versi�n 4.1.12 ? maneja un pool de Tags,
	 * tenemos que asegurarnos de limpiar los datos que recibimos, si no cuando
	 * Tomcat reutiliza este Tag nos vamos a encontrar con datos viejos /
	 * inconsistentes
	 */

	private void clean()
	{
		separador="-";
		tipoPersonaBuscada = "";
		codigoInstitucion = "";
		codigoCentroCosto = "";
		
	}

	public void funcionalidad() throws JspTagException
	{
		try
		{
			TagDao tagDao;

			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			//ResultSetDecorator rs = null;

			//String s;

			/*
			 * En este caso definimos (en el caso del m�dico) si queremos o no
			 * que salgan todos los m�dicos o todos los profesionales de la salud,
			 * si este parametro est� en false solo muestra los m�dicos, si esta en
			 * true o no lo pasa el usuario, muestra a todos los profesionales
			 */

			//rs =tagDao.consultaTagMuestraPersonasNombreApellidoStruts (con, tipoPersonaBuscada, codigoInstitucion, codigoCentroCosto, "", "");
			
			Vector<String> resultados = 
				tagDao.consultaTagMuestraPersonasNombreApellidoStruts (con, tipoPersonaBuscada, codigoInstitucion, codigoCentroCosto, "", "", this.separador);

			//logger.info("\n\n\n*********************************--------      " + resultados.toString());
			//SI SE ESTA BUSCANDO UN USUARIO, RETORNA UN STRING CON LOGIN-NOMBRE
			//SI ES OTRA PERSONA DEVUELVE CODTIPOID-NUMID-NOMBRE
			
			/*while (rs.next())
			{
				
				if (tipoPersonaBuscada.equals("MostrarUsuariosActivacion")|| tipoPersonaBuscada.equals("MostrarUsuariosDesactivacion")||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
				{
					s=rs.getString("login")  +  this.separador + rs.getString("primer_nombre") + " " + rs.getString("primer_apellido")+  this.separador+ rs.getString("codigo");
				}
				else if(tipoPersonaBuscada.equals("MostrarSoloMedicos")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacion")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas"))
				{
					s = rs.getString("tipo_identificacion") + this.separador + rs.getString("numero_identificacion") +  this.separador +rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) + ", " + rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + this.separador + rs.getString("codigo_medico");					
				}
				*//**Adicion Carlos. Esta opcion muestra todos los prefionales de la salud.
				Validacion necesaria para el modulo de consulta externa**//*
				else if(tipoPersonaBuscada.equals("TodosProfesionalesSalud") || tipoPersonaBuscada.equals("TodosProfesionalesSaludActivos"))
				{
					s = rs.getString("tipo_identificacion") + this.separador + rs.getString("numero_identificacion") +  this.separador +rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) + ", " + rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + this.separador + rs.getString("codigo_medico");					
				}
				else if(tipoPersonaBuscada.equals("MostrarUsuarios"))
				{
					s=rs.getString("login")  +  this.separador + rs.getString("primer_nombre") + this.separador + rs.getString("primer_apellido")+  this.separador+ rs.getString("codigo")+  this.separador;
					
					String activo = rs.getString("activo");
					
					if (!UtilidadTexto.isEmpty(activo)) {
						s+=activo;
					}else{
						s+= " ";
					}
					
					
				}
				else if(tipoPersonaBuscada.equals("MostrarMedicos"))
				{
					s = rs.getString("tipo_identificacion") + this.separador + rs.getString("numero_identificacion") +  this.separador +rs.getString("primer_apellido") + this.separador + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) + this.separador + rs.getString("primer_nombre") + this.separador + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + this.separador + rs.getString("codigo")+ this.separador + rs.getString("indicativo_interfaz");					
				}
				else 
				{
					s =rs.getString("tipo_identificacion") + this.separador + rs.getString("numero_identificacion")  +  this.separador +rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + " " + rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) ;
				}
				
				resultados.add(s);
			}
			rs.close();*/
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
			{
			throw new JspTagException("TagMuestraPersonasNombreApellidoStruts: "+e.getMessage());
			}
		catch (Exception e)
			{
			throw new JspTagException("TagMuestraPersonasNombreApellidoStruts: "+e.getMessage());
			}

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
	 * Establece el codigo de la institucion.
	 * @param codigoInstitucion El codigo de la institucion a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Establece el tipo de la persona buscada.
	 * @param tipoPersonaBuscada El tipo de persona buscada a establecer
	 */
	public void setTipoPersonaBuscada(String tipoPersonaBuscada) {
		this.tipoPersonaBuscada = tipoPersonaBuscada;
	}

	/**
	 * @return
	 */
	public String getSeparador() {
		return separador;
	}

	/**
	 * @param string
	 */
	public void setSeparador(String string) {
		separador = string;
	}


	/**
	 * @return Returns the codigoCentroCosto.
	 */
	public String getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(String codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
}