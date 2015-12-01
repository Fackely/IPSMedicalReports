/*
 * @(#)TagBusquedaPersonas.java
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

/**
 * Esta clase maneja la funcionalidad del tag que permite 
 * desarrollar la busqueda de todas las entidades que hereden
 * de personas y que manejen como llave primaria en la BD
 * el codigo y el tipo de la identificacion (Actualmente
 * Medico y Paciente)
 *
 * @version 1.0, Nov 13, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagBusquedaPersonas extends TagSupport {

	/**
	 * String en donde viene con quien estoy trabajando
	 * (puede ser un médico o un paciente)
	 */
	private String tipoPersonaBuscada = null;

	/**
	 * Número de identificación buscada
	 */
	private String numeroIdentificacion = "";

	/**
	 * Código del tipo de identificación buscada
	 */
	private String codigoTipoIdentificacion = "";

	/**
	 * Primer nombre de la persona buscada.
	 */
	private String primerNombrePersona = "";

	/**
	 * Segundo nombre de la persona buscada.
	 */
	private String segundoNombrePersona = "";

	/**
	 * Primer apellido de la persona buscada.
	 */
	private String primerApellidoPersona = "";

	/**
	 * Segundo apellido de la persona buscada.
	 */
	private String segundoApellidoPersona = "";

	/**
	 * Codigo de la ciudad en la que fue expedida la identificacion de la persona que se esta buscando que se esta buscando.
	 */
	private String codigoCiudadIdentificacion = "";

	/**
	 * Codigo del departamento en la que fue expedida la identificacion de la persona que se esta buscando.
	 */
	private String codigoDepartamentoIdentificacion = "";

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Fecha de nacimiento de la persona buscada.
	 */
	private String fechaNacimiento = null;

	/**
	 * Dirección de la persona buscada.
	 */
	private String direccion = "";

	/**
	 * Codigo de la Ciudad de residencia de la persona.
	 */
	private String codigoCiudad = "";

	/**
	 * Codigo del departamento donde reside de la persona.
	 */
	private String codigoDepartamento = "";

	/**
	 * Criterios de búsqueda elegidos
	 */
	private String criteriosBusqueda[];

	/**
	 * Email de la persona buscada.
	 */
	private String email = "";
	
	/**
	 * Codigo del cargo de la persona (usuario) 
	 */
	private String codigoCargo="";

	/**
	 * Descripcion del cargo de la persona (usuario)
	 */
	private String nombreCargo="";
	

	/**
	 * Teléfono de la persona buscada.
	 */
	private String telefono = "";

	/**
	 * Código de la institución a la cual pertenece la persona buscada.
	 */
	private String codigoInstitucion = "";

	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a traves del <code>jsp:param</code>
	 * el codigo de la ciudad y el codigo del departamento, con
	 * estos busco los codigos y nombres de los barrios existentes
	 * en estas ciudades
	 * @return la constante SKIP_BODY
	 */
	public int doStartTag() throws JspException {
		try {
			//Primero reviso que hayan llamado para
			//un propósito específico, si no me salgo
			//y no hago un gasto de recursos innecesario

			if (tipoPersonaBuscada == null || tipoPersonaBuscada.equals("") || codigoInstitucion == null || codigoInstitucion.equals(""))
				return SKIP_BODY;

			TagDao tagDao;

			ServletContext sc = pageContext.getServletContext();
			String tipoBD = (String) sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			ResultSetDecorator rs = null;
			String s;
			String paginaAnalisis = "";

			String numeroIdentificacionRespuesta = "", tipoIdentificacionRespuesta = "", primerNombreRespuesta = "", segundoNombreRespuesta = "", primerApellidoRespuesta = "", segundoApellidoRespuesta = "";

			//Este tag nos permite agrupar la funcionalidad para todo lo que
			//se relaciona con pacientes, desafortunadamente la manera como
			//se esperan los resultados varia entre funcionalidades 

			//En este caso vamos a entrar a la opcion donde se genera un
			//ingreso de paciente (creacion de cuentas y demás)

			if (tipoPersonaBuscada.equals("IngresoPaciente")) {

				paginaAnalisis = "ingresoPaciente.do?estado=decisionIngresoPacienteSistema";

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "&numeroIdentificacion=" + numeroIdentificacionRespuesta + "&tipoIdentificacion=" + tipoIdentificacionRespuesta + "-LK&Submit=1\">" + "<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta + " " +primerNombreRespuesta + " " + segundoNombreRespuesta + "(" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</font></A></TD></TR>";
					pageContext.getOut().print(s);
				}
			}
			else if (tipoPersonaBuscada.equals("ModificarPaciente")) {

				paginaAnalisis = "modificarPaciente.jsp";

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "?identificacion=" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + "&Submit=1\">" + "<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta +" "  + primerNombreRespuesta + " " + segundoNombreRespuesta + "  (" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</font></A></TD></TR>";
					pageContext.getOut().print(s);
				}
			}
			else if (tipoPersonaBuscada.equals("MostrarPacienteClinica")||tipoPersonaBuscada.equals("MostrarPacienteFacturacion")) {

				if (tipoPersonaBuscada.equals("MostrarPacienteClinica"))
					paginaAnalisis = "mostrarPacienteClinica.jsp";
				else if (tipoPersonaBuscada.equals("MostrarPacienteFacturacion"))
					paginaAnalisis = "mostrarPacienteFacturacion.jsp";

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "?identificacion=" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + "&Submit=1\">" + "<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta +" "  + primerNombreRespuesta + " " + segundoNombreRespuesta + "  (" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</font></A></TD></TR>";
					pageContext.getOut().print(s);
				}
			}
			else if (tipoPersonaBuscada.equals("solicitudLaboratorio")) {

				paginaAnalisis = "limpiezaIdIngreso.jsp";

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "?identificacion=" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + "&Submit=1\">" + "<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta +" "  + primerNombreRespuesta + " " + segundoNombreRespuesta + "  (" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</font></A></TD></TR>";
					pageContext.getOut().print(s);
				}
			}

			else if (tipoPersonaBuscada.equals("solicitudProcedimiento")) 
			{

				paginaAnalisis = "limpiezaIdIngreso.jsp";

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "?identificacion=" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + "&Submit=1\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta +" "  + primerNombreRespuesta + " " + segundoNombreRespuesta + "  (" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</A></TD></TR>";
					pageContext.getOut().print(s);
				}
			}

			else if (tipoPersonaBuscada.equals("ModificarMedico")) {
				paginaAnalisis = "modificarMedico.jsp";

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "?identificacion=" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + "&Submit=1\">" + "<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta +" "  + primerNombreRespuesta + " " + segundoNombreRespuesta + "  (" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</font></A></TD></TR>";
					pageContext.getOut().print(s);
				}

			}
			else if (tipoPersonaBuscada.equals("ModificarUsuario")||tipoPersonaBuscada.equals("MostrarUsuario")||tipoPersonaBuscada.equals("MostrarUsuariosInactivos")) {
				
				if (tipoPersonaBuscada.equals("ModificarUsuario"))
				{
					paginaAnalisis = "modificarUsuario.jsp";
				}
				else if (tipoPersonaBuscada.equals("MostrarUsuario")||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
				{
					paginaAnalisis = "mostrarUsuario.jsp";
				}

				rs=tagDao.consultaTagBusquedaPersonas(con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email,codigoCargo, codigoInstitucion);

				while (rs.next()) {
					numeroIdentificacionRespuesta = rs.getString("numeroIdentificacionPersona");
					tipoIdentificacionRespuesta = rs.getString("tipoIdentificacionPersona");

					primerNombreRespuesta = rs.getString("primerNombrePersona");
					segundoNombreRespuesta = rs.getString("segundoNombrePersona");
					primerApellidoRespuesta = rs.getString("primerApellidoPersona");
					segundoApellidoRespuesta = rs.getString("segundoApellidoPersona");

					//En Oracle no existe diferencia entre el 
					//concepto de campo vacío y nulo, por eso
					//dejamos un campo vacío en caso de ser nulo
					if (segundoNombreRespuesta==null)
					{
					    segundoNombreRespuesta="";
					}
					if (segundoApellidoRespuesta==null)
					{
					    segundoApellidoRespuesta="";
					}

					//En este caso la pagina del ingreso espera dos atributos,
					//el número de identificación del paciente y el tipo de 
					//identificacion del mismo (Cod - Nombre para tipo)

					
					ResultSetDecorator rsb = tagDao.consultaTagBusquedaPersonas_Login(con, numeroIdentificacionRespuesta, tipoIdentificacionRespuesta);
					rsb.next();

					s = "<tr bgcolor=\"#FFFFFF\"><TD><A HREF=\"" + paginaAnalisis + "?loginSeleccionado=" + rsb.getString("login") + "&Submit=1\">" + "<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" + primerApellidoRespuesta + " " + segundoApellidoRespuesta + " " + primerNombreRespuesta + " " + segundoNombreRespuesta + "  (" + tipoIdentificacionRespuesta + "-" + numeroIdentificacionRespuesta + ")</font></A></TD></TR>";
					pageContext.getOut().print(s);
				}

			}
			else {
				s = "<tr bgcolor=\"#FFFFFF\"><TD>No ha seleccionada ninguna implementacion de Persona (TagBusquedaPersonas)</TD></TR>";
				pageContext.getOut().print(s);
			}
		}
		catch (java.io.IOException e) {
			throw new JspTagException("TagBusquedaPersonas: " + e.getMessage());
		}
		catch (java.sql.SQLException e) {
			throw new JspTagException("TagBusquedaPersonas: " + e.getMessage());
		}
		catch (Exception e) {
			throw new JspTagException("TagBusquedaPersonas: " + e.getMessage());
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
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Establece el codigoCiudad.
	 * @param codigoCiudad The codigoCiudad a establecer
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * Establece el codigoCiudadIdentificacion.
	 * @param codigoCiudadIdentificacion The codigoCiudadIdentificacion a establecer
	 */
	public void setCodigoCiudadIdentificacion(String codigoCiudadIdentificacion) {
		this.codigoCiudadIdentificacion = codigoCiudadIdentificacion;
	}

	/**
	 * Establece el codigoDepartamento.
	 * @param codigoDepartamento The codigoDepartamento a establecer
	 */
	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	/**
	 * Establece el codigoDepartamentoIdentificacion.
	 * @param codigoDepartamentoIdentificacion The codigoDepartamentoIdentificacion a establecer
	 */
	public void setCodigoDepartamentoIdentificacion(String codigoDepartamentoIdentificacion) {
		this.codigoDepartamentoIdentificacion = codigoDepartamentoIdentificacion;
	}

	/**
	 * Establece el criteriosBusqueda.
	 * @param criteriosBusqueda The criteriosBusqueda a establecer
	 */
	public void setCriteriosBusqueda(String[] criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}

	/**
	 * Establece el direccion.
	 * @param direccion The direccion a establecer
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Establece el fechaNacimiento.
	 * @param fechaNacimiento The fechaNacimiento a establecer
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * Establece el primerApellidoPersona.
	 * @param primerApellidoPersona El primer apellido de la persona a establecer
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona) {
		this.primerApellidoPersona = primerApellidoPersona;
	}

	/**
	 * Establece el primerNombrePersona.
	 * @param primerNombrePersona El primer nombre de la persona a establecer
	 */
	public void setPrimerNombrePersona(String primerNombrePersona) {
		this.primerNombrePersona = primerNombrePersona;
	}

	/**
	 * Establece el segundoApellidoPersona.
	 * @param segundoApellidoPersona El segundo apellido de la persona a establecer
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona) {
		this.segundoApellidoPersona = segundoApellidoPersona;
	}

	/**
	 * Establece el segundoNombrePersona.
	 * @param segundoNombrePersona El segundo nombre de la persona a establecer
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona) {
		this.segundoNombrePersona = segundoNombrePersona;
	}

	/**
	 * Establece el tipoPersonaBuscada.
	 * @param tipoPersonaBuscada El tipo de persona buscada. a establecer
	 */
	public void setTipoPersonaBuscada(String tipoPersonaBuscada) {
		this.tipoPersonaBuscada = tipoPersonaBuscada;
	}

	/**
	 * Retorna el correo electrónico.
	 * @return String con el correo electrónico
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Retorna el telefono.
	 * @return String con el telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Establece el email.
	 * @param email El email a establecer
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Establece el telefono.
	 * @param telefono El telefono a establecer
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Establece el codigo de la institucion.
	 * @param codigoInstitucion El codigo de la institucion a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Establece el numero de identificación.
	 * @param numeroIdentificacion El numero de identificación a establecer
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Establece el tipo de identificación
	 * @param codigoTipoIdentificacion El tipo de identificación a establecer
	 */
	public void setCodigoTipoIdentificacion(String tipoIdentificacion) {
		this.codigoTipoIdentificacion = tipoIdentificacion;
	}
    /**
     * Retorna el codigo del cargo
     * @return
     */
	public String getCodigoCargo() {
		return codigoCargo;
	}
    
	 /**
	  * Establece el codigo del cargo de la persona (usuario)
	  * @param codigoCargo
	  */
	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}
	
	/**
	 * Retorna la descripcion o nombre del cargo de la persona (usario)
	 * @return
	 */
	public String getNombreCargo() {
		return nombreCargo;
	}
 
	/**
	 * Establece el nombre o descripcion del cargo de la persona (usuario)
	 * @param nombreCargo
	 */
	public void setNombreCargo(String nombreCargo) {
		this.nombreCargo = nombreCargo;
	}

}