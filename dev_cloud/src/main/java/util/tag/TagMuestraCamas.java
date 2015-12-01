/*
 * @(#)TagMuestraCamas.java
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

import util.Encoder;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase implementa un tag que permite mostrar las camas que cumplan
 * ciertos requerimientos especificados en las restricciones. Define un conjunto
 * de <i>scripting variables</i> <b>valor</b> y <b>nombre</b>, y se apoya en la
 * clase <code>TEIValoresPorDefecto</code>.
 *
 * @version 1.0, Feb 24, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author 	<a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
 */

public class TagMuestraCamas extends BodyTagSupport {

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * String con el sexo del futuro paciente
	 */

	private String sexo;
	
	/**
	 * String con la edad. Porqué un String?, porque en el caso general me llega
	 * esto como parámetro y es mejor hacer la conversión en la funcionalidad
	 * (java) y no en la presentación (jsp)
	 */
	private String edad;

	/**
	 * String que me dice si no se deben mostrar las camas de urgencias
	 */
	
	private String sinUrgencias;
	
	/**
	 * Boolean que dice si hay que mostrar las camas en 
	 * desinfección o no
	 */
	private boolean conDesinfeccion=false;
	
	/**
	 * codigo de la institucion a la cual pertenecen las camas
	 */
	private String codigoInstitucion;
	
	/**
	 * fecha del mov
	 */
	private String fechaMovimiento;
	
	/**
	 * hora del mov
	 */
	private String horaMovimiento;
	
	//*******ATRIBUTOS ADICIONALES HOSPITALIZACION***********
	/**
	 * Centro de atención del usuario
	 */
	private String centroAtencion = "";
	/**
	 * Vía de ingreso que valida camas
	 */
	private String viaIngreso = "";
	
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
		if (sexo != null && !sexo.trim().equals("")||edad!= null && !edad.trim().equals("")) {
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
					throw new JspTagException("Error escribiendo TagMuestraCamas : " + e.getMessage());
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
		this.conDesinfeccion=false;
		this.codigoInstitucion="";
		this.fechaMovimiento="";
		this.horaMovimiento="";
		
		//atributos de la hospitalizacion
		this.centroAtencion = "";
		this.viaIngreso = "";
	}

	/**
	 * Este método obtiene de la BD los datos de "valor" y "nombre" asociados al
	 * "parametro" pasado a este tag, y los pone en el contexto de la página
	 * como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{

		TagDao tagDao;

		ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		ResultSetDecorator rs;

		try {

			rs=tagDao.consultaTagMuestraCamas (con, sexo, this.sinUrgencias, conDesinfeccion, codigoInstitucion, this.fechaMovimiento, this.horaMovimiento,this.centroAtencion,this.viaIngreso); 
			
			//Variables locales para no tener problemas con pools de conexiones
			
			Vector codigosTemp=new Vector(8,3);
			Vector nombresTemp=new Vector(8,3);
			Vector centrosCostoTemp=new Vector(8,3);
			Vector estadosCamaTemp=new Vector(8,3);
			Vector nombresCentroCostoTemp=new Vector(8,3);
			Vector tiposUsuarioTemp=new Vector(8,3);			
			Vector descripcionesTemp=new Vector(8,3);
			Vector habitacionesTemp= new Vector(8,3);
			Vector esUciTemp= new Vector(8,3);
			Vector tipoMonitoreoTemp= new Vector(8,3);
			
			while (rs.next())	{
				codigosTemp.add(rs.getString("codigo"));
				nombresTemp.add(Encoder.encode(rs.getString("numeroCama")));
				centrosCostoTemp.add(rs.getString("centroCosto"));

				estadosCamaTemp.add(rs.getString("estadoCama"));
				nombresCentroCostoTemp.add(rs.getString("nombreCentroCosto"));
				tiposUsuarioTemp.add(rs.getString("tipoUsuario"));
				descripcionesTemp.add(rs.getString("descripcion"));
				
				habitacionesTemp.add(rs.getString("habitacion"));
				esUciTemp.add(rs.getString("esUci"));
				tipoMonitoreoTemp.add(rs.getString("tipoMonitoreo"));
				
			}

			pageContext.setAttribute("codigos" ,  codigosTemp);
			pageContext.setAttribute("nombres",   nombresTemp);
			pageContext.setAttribute("centrosCosto", centrosCostoTemp);


			pageContext.setAttribute("estadosCama" ,  estadosCamaTemp);
			pageContext.setAttribute("nombresCentroCosto", nombresCentroCostoTemp);
			pageContext.setAttribute("tiposUsuario", tiposUsuarioTemp);
			pageContext.setAttribute("descripciones", descripcionesTemp);
			
			pageContext.setAttribute("habitaciones", habitacionesTemp);
			pageContext.setAttribute("esUci", esUciTemp);
			pageContext.setAttribute("tipoMonitoreo", tipoMonitoreoTemp);

		}	catch (SQLException sqle) {
				sqle.printStackTrace();
				throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Camas : " + sqle.getMessage());
		}


	}


	/**
	 * Establece la edad del futuro paciente.
	 * @param edad La edad a establecer
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * Establece el sexo del futuro paciente.
	 * @param sexo El sexo a establecer
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * Returns the edad.
	 * @return String
	 */
	public String getEdad() {
		return edad;
	}

    /**
     * @return Returns the fechaMovimiento.
     */
    public String getFechaMovimiento() {
        return fechaMovimiento;
    }
    /**
     * @param fechaMovimiento The fechaMovimiento to set.
     */
    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
	/**
	 * Returns the sexo.
	 * @return String
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * Returns the sinUrgencias.
	 * @return String
	 */
	public String getSinUrgencias() {
		return sinUrgencias;
	}

	/**
	 * Sets the sinUrgencias.
	 * @param sinUrgencias The sinUrgencias to set
	 */
	public void setSinUrgencias(String sinUrgencias) {
		this.sinUrgencias = sinUrgencias;
	}

	/**
	 * @return
	 */
	public boolean setConDesinfeccion() {
		return conDesinfeccion;
	}

	/**
	 * @param b
	 */
	public void setConDesinfeccion(boolean b) {
		conDesinfeccion = b;
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
     * @return Returns the horaMovimiento.
     */
    public String getHoraMovimiento() {
        return horaMovimiento;
    }
    /**
     * @param horaMovimiento The horaMovimiento to set.
     */
    public void setHoraMovimiento(String horaMovimiento) {
        this.horaMovimiento = horaMovimiento;
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