/*
 * @(#)TagBusquedaSencillaServicios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Clase encargada de la busqueda de servicios, manejada
 * en todas las solicitudes
 * 
 *	@version 1.0, May 7, 2004
 */
public class TagBusquedaSencillaServicios  extends BodyTagSupport
{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * String con el criterio de busqueda del servicio
	 */
	private String criterioBusqueda;
	
	/**
	 * Entero con el código del tarifario con el que se va a hacer
	 * la búsqueda
	 */
	private int codigoTarifarioBusqueda;
	
	/**
	 * Boolean que nos dice si la búsqueda es por nombre
	 * (Si es false es por el código de axioma)
	 */
	private boolean esPorNombre;
	
	/**
	 * Boolean que nos dice si la búsqueda es por nombre
	 * (Si es false es por el código de axioma)
	 */
	private boolean buscarSoloActivos;
	
	/**
	 * 
	 */
	private boolean incluirPaquetes=true;
	
	/**
	 * 
	 */
	private boolean porCodigoCUPS;

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
		
		if (criterioBusqueda != null && !criterioBusqueda.trim().equals("")) 
		{
			//Si es por código tratamos de partir esp-codigo, si no se puede
			//es porque es el número como tal
			if (!esPorNombre)
			{
				try
				{
					String arregloTemp[]=criterioBusqueda.split("-");
					if (arregloTemp.length==2)
					{
						this.criterioBusqueda=arregloTemp[1];
					}
				}
				catch (Exception e){}
			}
			
			funcionalidad();
			return EVAL_BODY_BUFFERED;
		}
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

		if (bodyContent != null) 
		{

			try 
			{
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	
			catch (Exception e) 
			{
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
	private void clean() 
	{
		this.criterioBusqueda = "";
		this.esPorNombre=false;
		this.codigoTarifarioBusqueda=0;
		this.porCodigoCUPS=false;

	}

	/**
	 * Este método obtiene de la BD los Servicios de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{
		TagDao tagDao;

		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		tagDao = myFactory.getTagDao();
		ResultSetDecorator rs;

		try 
		{
           
			rs=tagDao.busquedaSencillaServicios (con, criterioBusqueda, codigoTarifarioBusqueda, esPorNombre, buscarSoloActivos,incluirPaquetes,porCodigoCUPS) ;

			//Variables locales para no tener problemas con pools de tags
			
			Vector codigosTemp=new Vector(8,3);
			Vector nombresTemp=new Vector(8,3);
			Vector especialidadesTemp=new Vector(8,3);
			Vector codigosEspecialidadesTemp=new Vector(8,3);
			Vector activosTemp=new Vector(8,3);
			Vector tipoServicio =new Vector(8,3);
			while (rs.next())	
			{
				codigosTemp.add(rs.getString("codigoServicio"));
				nombresTemp.add(rs.getString("nombreServicio"));
				especialidadesTemp.add(rs.getString("nombreEspecialidad"));
				codigosEspecialidadesTemp.add(rs.getString("codigoEspecialidad"));
				activosTemp.add( ("" + rs.getBoolean("activo"))  );
				tipoServicio.add(("" + rs.getBoolean("tipoServicio"))  );
			}

			pageContext.setAttribute("codigos" ,  codigosTemp);
			pageContext.setAttribute("nombres",   nombresTemp);
			pageContext.setAttribute("codigosEspecialidades",   codigosEspecialidadesTemp);
			pageContext.setAttribute("especialidades",   especialidadesTemp);
			pageContext.setAttribute("activos" ,  activosTemp);
			pageContext.setAttribute("tipoServicio" ,  tipoServicio);

		}	catch (SQLException sqle) 
		{
				sqle.printStackTrace();
				throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag de Busqueda de Servicios: " + sqle.getMessage());
		}
	}

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
	 * @return
	 */
	public boolean isBuscarSoloActivos() {
		return buscarSoloActivos;
	}

	/**
	 * @return
	 */
	public int getCodigoTarifarioBusqueda() {
		return codigoTarifarioBusqueda;
	}

	/**
	 * @return
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @return
	 */
	public boolean isEsPorNombre() {
		return esPorNombre;
	}

	/**
	 * @param b
	 */
	public void setBuscarSoloActivos(boolean b) {
		buscarSoloActivos = b;
	}

	/**
	 * @param i
	 */
	public void setCodigoTarifarioBusqueda(int i) {
		codigoTarifarioBusqueda = i;
	}

	/**
	 * @param string
	 */
	public void setCriterioBusqueda(String string) {
		criterioBusqueda = string;
	}

	/**
	 * @param b
	 */
	public void setEsPorNombre(boolean b) {
		esPorNombre = b;
	}

	/**
	 * @return the incluirPaquetes
	 */
	public boolean isIncluirPaquetes() {
		return incluirPaquetes;
	}

	/**
	 * @param incluirPaquetes the incluirPaquetes to set
	 */
	public void setIncluirPaquetes(boolean incluirPaquetes) {
		this.incluirPaquetes = incluirPaquetes;
	}
	
	public boolean isPorCodigoCUPS() {
		return porCodigoCUPS;
	}

	public void setPorCodigoCUPS(boolean porCodigoCUPS) {
		this.porCodigoCUPS = porCodigoCUPS;
	}


}
