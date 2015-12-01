/*
 * @(#)TagCargarPacientes.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.tag;


import java.sql.Connection;
import java.util.Observable;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase maneja la funcionalidad del tag que permite dado un
 * número y tipo de identificacion, cargarlo en la sesión con el
 * nombre "pacienteActivo"
 *
 * @version 1.0, Mar 31, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author 	<a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
 */

public class TagCargarPaciente extends TagSupport 
{

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(TagCargarPaciente.class); 
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;
	
	/**
	 * String con el número de Identificacion del paciente a cargar
	 */
	private String numeroIdentificacion="";

	/**
	 * String con el tipo de Identificacion del paciente a cargar
	 */
	private String tipoIdentificacion="";
	
	/**
	 * String con el código de la institución que desea cargar este paciente
	 */
	private String codigoInstitucion="";
	
	/**
	 * String del codigo del centro de atencion de la sesión
	 */
	private String codigoCentroAtencion = "";
	
	/**
	 * Codigo del Ingreso del paciente
	 * */
	private String ingreso = "";
	
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo de la ciudad y el codigo del departamento, con
	 * estos busco los codigos y nombres de los barrios existentes
	 * en estas ciudades
	 * @return la constante SKIP_BODY
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */

	public int doStartTag() throws JspException	
	{

		try	{
			HttpSession session = pageContext.getSession();
			
			logger.info("empieza a cargar el paciente");
			
			//verifico que los datos del ingreso existan, si existen se realiza la carga del paciente por 
			//el ingreso
			if(this.getIngreso()!=null && !this.getIngreso().equals(""))
			{
				PersonaBasica pac = (PersonaBasica) session.getAttribute("pacienteActivo");
				
				logger.info("ing > "+ingreso+" codigoInsti > "+codigoInstitucion+" centro aten > "+codigoCentroAtencion);
				
				pac.cargarPacienteXingreso(con, ingreso, codigoInstitucion, codigoCentroAtencion);								
				// Código necesario para registrar este paciente como Observer
				Observable observable = (Observable) pageContext.getServletContext().getAttribute("observable");
				if (observable != null) 
				{
					pac.setObservable(observable);
					// Si ya lo habíamos añadido, la siguiente línea no hace nada
					observable.addObserver(pac);
				}				
			}
			
			//Primero Reviso si tengo acceso y si existe este paciente
			else 
			{
				boolean validacion = false;
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getProduccionEnParaleloConSistemaAnterior(Utilidades.convertirAEntero(codigoInstitucion))))
				{
					ResultadoBoolean resultado;
					UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
					resultado = interfaz.verificarPacienteSistemaParalelo(tipoIdentificacion, numeroIdentificacion, codigoInstitucion);
					
					if(resultado.isTrue())
					{	
						pageContext.getRequest().setAttribute("mensaje", resultado.getDescripcion());
						validacion = false;
					}
					
					
				}
			
				if(!validacion)
				{
					if (UtilidadValidacion.puedoImprimirPaciente(con, tipoIdentificacion, numeroIdentificacion, codigoInstitucion)) 
					{
						TipoNumeroId identificacion = new TipoNumeroId(tipoIdentificacion, numeroIdentificacion);
						PersonaBasica pac = (PersonaBasica) session.getAttribute("pacienteActivo");
						pac.cargar(con, identificacion);				
						pac.cargarPaciente2(con, identificacion, codigoInstitucion, codigoCentroAtencion);				
						// Código necesario para registrar este paciente como Observer
						Observable observable = (Observable) pageContext.getServletContext().getAttribute("observable");
						if (observable != null) 
						{
							pac.setObservable(observable);
							// Si ya lo habíamos añadido, la siguiente línea no hace nada
							observable.addObserver(pac);
						}
						boolean historiaAnterior=false; 
						historiaAnterior=Utilidades.existeHistoriaSistemaAnterior(con,tipoIdentificacion, numeroIdentificacion);
						
						if(historiaAnterior)
							pageContext.getRequest().setAttribute("mensaje", "Paciente con Información de Historia en el Sistema Anterior");
						else
							if (!UtilidadCadena.noEsVacio(pageContext.getRequest().getAttribute("mensaje")+""))
								pageContext.getRequest().setAttribute("mensaje", "");
					}
					else 
					{
						pageContext.getRequest().setAttribute("mensaje", "No se pudo cargar el paciente");
					}
				}
			}

		}	catch (java.sql.SQLException e)	{
				e.printStackTrace();
				throw new JspTagException("TagCargarPaciente SQL Exception : " + e.getMessage());
		}	/*catch (Exception e)	{
				throw new JspTagException("TagCargarPaciente General Exception : e: "+e+" "+ e.getMessage());
		}*/

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
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

	/**
	 * Returns the codigoInstitucion.
	 * @return String
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Returns the numeroIdentificacion.
	 * @return String
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * Returns the tipoIdentificacion.
	 * @return String
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * Sets the codigoInstitucion.
	 * @param codigoInstitucion The codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Sets the numeroIdentificacion.
	 * @param numeroIdentificacion The numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Sets the tipoIdentificacion.
	 * @param tipoIdentificacion The tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
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
	 * @return Returns the codigoCentroAtencion.
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

}
