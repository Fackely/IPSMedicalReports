/*
 * @(#)TagBusquedaDiagnosticos.java
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
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.ConstantesBD;
import util.Encoder;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase implementa un tag que permite, dado el nombre , parte del nombre,
 * código o parte del código recuperar de la Base de Datos los diagnosticos que cumplan 
 * con el criterio dado. Define las <i>scripting variables</i> <b>codigos</b> y 
 * <b>nombres</b>, y se apoya en la clase <code>TEIBusquedaDiagnosticos</code>.
 * 
 *
 * @version 1.0, Mar 7, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class TagBusquedaDiagnosticos extends BodyTagSupport {

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * String con el criterioBusquedaDiagnostico del futuro paciente
	 */

	private String criterioBusquedaDiagnostico;
	
	/**
	 * Atributo para filtrar los diagnósticos depoendiendo de la especialidad
	 * de la valoración Ej: Oftalmología
	 */
	private int codigoFiltro;
	
	/**
	 * String con la accion. Porqué un String?, porque en el caso general me llega
	 * esto como parámetro y es mejor hacer la conversión en la funcionalidad
	 * (java) y no en la presentación (jsp)
	 */
	private String accion;
	
	private int codigoInstitucion;
	
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
		if (criterioBusquedaDiagnostico != null && !criterioBusquedaDiagnostico.trim().equals("")||accion!= null && !accion.trim().equals("")) {
			funcionalidad();
			return EVAL_BODY_BUFFERED;
		}
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

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo TagBusquedaProcedimientosClinicos : " + e.getMessage());
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
	private void clean() {
		this.criterioBusquedaDiagnostico = "";
		this.accion = "";
		this.codigoFiltro=ConstantesBD.codigoRespuestaGeneral;
		this.codigoInstitucion=0;
	}

	/**
	 * Este método obtiene de la BD los diagnosticos de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{
		TagDao tagDao;
		int tipoCieValido=ConstantesBD.codigoNuncaValido;
		ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		
		try
		{
			String fechaAValidar="";
			PersonaBasica paciente=(PersonaBasica )pageContext.getSession().getAttribute("pacienteActivo");
			
			if (paciente == null || paciente.getCodigoCuenta()<=0)
			{
				//Caso admisión Hospitalización
				fechaAValidar=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			}
			else if (paciente.getCodigoAdmision()>0)
			{
				//En este punto buscamos la fecha de la 
				//admisión, a través de la cual vamos
				//a buscar el tipo de Cie Válido
				fechaAValidar=UtilidadValidacion.getFechaAdmision(con, paciente.getCodigoCuenta());
			}
			else if( (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna))
			{
				//Si no tiene admisión, nos encontramos
				//en consulta externa, en cuyo caso debemos
				//buscar la cita
				String numSolString=(String)pageContext.getRequest().getParameter("numeroSolicitud");
				if(numSolString==null)
				{
					fechaAValidar  = Utilidades.getFechaAperturaCuenta(con, paciente.getCodigoCuenta()); 
				}
				else
				{
					int numeroSolicitudCasoCita=Integer.parseInt(numSolString);
					fechaAValidar=tagDao.consultaTagBusquedaDiagnosticos_FechaCita(con, numeroSolicitudCasoCita);

					//-Esta Condicion se hizo para que ha paciente de consulta externa 
					//-y ambulatorios la busqueda de servicios del JSP le sanga correctamente
					//-debido a que no pueden tener una cita registrada en la agenda ( Tarea: Xplanner 2, Proyecto : Enero 13 2006 Tarea : 8495)
					if ( !UtilidadCadena.noEsVacio(fechaAValidar) )   
					{
						if (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
						{
							fechaAValidar = Utilidades.getFechaSolicitud(con, numeroSolicitudCasoCita); 
						}
						
						if (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
						{
							fechaAValidar  = Utilidades.getFechaAperturaCuenta(con, paciente.getCodigoCuenta()); 
						}
					}
				}
			}
			else
			{
				//Caso en el cual se le dió egreso a un paciente de urgencias o de hospitalización
				fechaAValidar=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			}
			if (fechaAValidar!=null&&!fechaAValidar.equals(""))
			{
				tipoCieValido=tagDao.consultaTagBusquedaDiagnosticos_Cie(con, fechaAValidar);
			}
		}
	    catch (Exception e)
	    {
	        throw new JspTagException(e.toString());
	    }
		ResultSetDecorator rs;

		Vector codigosTemp=new Vector(8,3);
		Vector nombresTemp=new Vector(8,3);
		///////
		Vector codigosFichasTemp=new Vector(8,3);
		ResultSetDecorator rsConsultaCodigoFicha;
		String acronimo="";
		///////
		
		if (tipoCieValido>ConstantesBD.codigoCieDiagnosticoNoSeleccionado)
		{
			try {
				rs=tagDao.consultaTagBusquedaDiagnosticos(con, criterioBusquedaDiagnostico, accion.equals("buscarTexto"), tipoCieValido, codigoFiltro, codigoInstitucion);
				//Variables locales para no tener problemas con pools de conexiones
				
				while (rs!= null && rs.next())	{
				    acronimo = rs.getString("acronimo");
					codigosTemp.add(rs.getString("acronimo") + "-"+ rs.getString("tipo_cie"));
					nombresTemp.add(Encoder.encode(rs.getString("nombre")));
					
					rsConsultaCodigoFicha = tagDao.consultaTagDiagnosticoSaludPublica(con,acronimo);
					
					while (rsConsultaCodigoFicha.next()) {
					    
					    String valor = Integer.toString(rsConsultaCodigoFicha.getInt("codigoEnfermedadesNotificables"));
					    codigosFichasTemp.add(valor);
					}
				}

            
             
			}	catch (SQLException sqle) {
					sqle.printStackTrace();
					
					throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag de Busqueda de Diagnosticos: " + sqle.getMessage());
			}
			
			pageContext.setAttribute("errorCie" ,  "false");
		}
		else
		{
		    pageContext.setAttribute("errorCie" ,  "true");
		}

		pageContext.setAttribute("codigos" ,  codigosTemp);
		pageContext.setAttribute("nombres",   nombresTemp);

		pageContext.setAttribute("fichas", codigosFichasTemp);
	}


	/**
	 * Establece la accion del futuro paciente.
	 * @param accion La accion a establecer
	 */
	public void setAccion(String edad) {
		this.accion = edad;
	}

	/**
	 * Establece el criterioBusquedaDiagnostico del futuro paciente.
	 * @param criterioBusquedaDiagnostico El criterioBusquedaDiagnostico a establecer
	 */
	public void setCriterioBusquedaDiagnostico(String sexo) {
		this.criterioBusquedaDiagnostico = sexo;
	}

	/**
	 * @return Retorna codigoFiltro.
	 */
	public int getCodigoFiltro()
	{
		return codigoFiltro;
	}

	/**
	 * @param codigoFiltro Asigna codigoFiltro.
	 */
	public void setCodigoFiltro(int codigoFiltro)
	{
		this.codigoFiltro = codigoFiltro;
	}

	/**
	 * @return Retorna cosigoInstitucion.
	 */
	public int getCodigoInstitucion()
	{
		return codigoInstitucion;
	}

	/**
	 * @param cosigoInstitucion Asigna cosigoInstitucion.
	 */
	public void setCodigoInstitucion(int codigoInstitucion)
	{
		this.codigoInstitucion = codigoInstitucion;
	}

}