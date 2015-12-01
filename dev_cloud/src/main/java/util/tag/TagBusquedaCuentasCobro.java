
/*
 * Creado   11/04/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package util.tag;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Tag para consultar las Cuentas de Cobro  para las 
 * siguientes funcionalidades:
 * 
 * ModificaciónCuentasCobro
 * 
 * Retorna los datos de la cuenta de cobro, segun los 
 * el filtro aplicado, si no hay filtro retorna todos
 * los datos presentes en la tabla <code>cuentas_cobro</code>.
 *
 * @version 1.0, 11/04/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TagBusquedaCuentasCobro extends BodyTagSupport
{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2074525415866712317L;

	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagBusquedaServicios1.class);
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * String con el criterio de busqueda del servicio
	 */
	private String criterioBusqueda;
	
	/**
	 * Boolean que nos dice si la búsqueda es por nombre 
	 */
	private boolean esPorNombre;
	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "@";
	
	/**
	 * almacena el tipo de campo por el cual
	 * se desea realizar la busqueda
	 */
	private String filtroBusqueda;
	
	/**
	 * el nombre de la columna, por el 
	 * cual se filtra
	 */
	private String rest1nombre="";
	
	/**
	 * el valor de la columna
	 */
	private String rest1valor="";
	
	/**
	 * el nombre de la columna, por el 
	 * cual se filtra
	 */
	private String rest2nombre="";
	
	/**
	 * el valor de la columna
	 */
	private String rest2valor="";
	
	private boolean usarVector=false;
	
	
	
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
	    	funcionalidad();
			return EVAL_BODY_BUFFERED;
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
					throw new JspTagException("Error escribiendo TagBusquedaCuentasCobro : " + e.getMessage());
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
		is_separador = "@";
		this.criterioBusqueda = "";
		this.esPorNombre=false;		
		this.filtroBusqueda="";
		this.rest1nombre="";
		this.rest2nombre="";
		this.rest1valor="";
		this.rest2valor="";
	}
	
	/**
	 * Este método obtiene de la BD las Cuentas de Cobro de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{
		
		PreparedStatementDecorator psd = null;
		ResultSetDecorator rs = null;
		Vector<String> parejasResultadoList = null; 
		Vector<Vector<String>> vectorResultados = null; 		
		Vector<String> cadenaResultados = null;
		
	    try
		{
			String consulta="";
			boolean existeWhere=false;
						
			if(!this.esPorNombre) {
				consulta=	"SELECT " +
						"cc.numero_cuenta_cobro," +
						"convenio as codigoconvenio," +
						"getnombreconvenio(convenio) as nombreconvenio," +
						"estado," +
						"to_char(fecha_elaboracion,'YYYY-MM-DD') AS fecha_elaboracion, " +
						"hora_elaboracion," +
						"usuario_genera," +								
						"valor_inicial_cuenta," +								
						"to_char(fecha_inicial,'YYYY-MM-DD') AS fecha_inicial, " +
						"to_char(fecha_final,'YYYY-MM-DD') AS fecha_final,  " +							
						"getDescripcionEstadoCartera (estado), "+
						"coalesce(obs_generacion,' ') AS observaciones, " +
						"coalesce(fecha_radicacion||'',' ') AS fecharadicacion, " +
						"coalesce(usuario_radica,' ') as usuarioradica "+
						" FROM " +
						"cuentas_cobro cc ";


				if(!rest1nombre.equals("") )
				{
					if(rest2nombre.equals(""))
					{
						consulta+=" where estado = "+rest1valor+" ";							    
						existeWhere=true;	
					}
					else if(!rest2nombre.equals(""))
					{
						consulta+=" where ( estado = "+rest1valor+" ";	
						existeWhere=true;
					}
				}
				if(!rest2nombre.equals("") )
				{
					if(!rest1nombre.equals(""))
					{ 
						consulta+=" OR estado = "+rest2valor+" ) ";
					}
					else if(rest1nombre.equals(""))
					{
						consulta+=" where estado = "+rest2valor+" ";
						existeWhere=true;
					}
				}			    
				if(!criterioBusqueda.equals("") && filtroBusqueda.equals("codigo"))
				{
					if(existeWhere)
					{
						consulta+=	" AND " +
								"cc.numero_cuenta_cobro ="+criterioBusqueda+" "; 
					}
					else if(!existeWhere)
					{
						consulta+=	" where " +
								"cc.numero_cuenta_cobro ="+criterioBusqueda+" "; 
						existeWhere=true;
					}							    
				}							
				if(!criterioBusqueda.equals("-1") && filtroBusqueda.equals("convenio"))
				{
					if(existeWhere)
					{
						consulta+=	" AND " +
								"convenio ="+criterioBusqueda+" "; 
					}
					else if(!existeWhere)
					{
						consulta+=	" where " +
								"convenio="+criterioBusqueda+" "; 
						existeWhere=true;
					}						    
				}							
				if(!criterioBusqueda.equals("") && filtroBusqueda.equals("fechaElaboracion"))
				{
					if(existeWhere)
					{
						consulta+=	" AND " +
								"fecha_elaboracion ='"+UtilidadFecha.conversionFormatoFechaABD(criterioBusqueda)+"' "; 
					}
					else if(!existeWhere)
					{
						consulta+=	" where " +
								"fecha_elaboracion='"+UtilidadFecha.conversionFormatoFechaABD(criterioBusqueda)+"' "; 
						existeWhere=true;
					}						   
				}

				logger.info("consulta tag->"+consulta);

				psd = new PreparedStatementDecorator(con, consulta+" order by cc.numero_cuenta_cobro ");
				rs = new ResultSetDecorator(psd.executeQuery());

				vectorResultados = new Vector<Vector<String>>();
				cadenaResultados = new Vector<String>();
				
				String parejasResultado="";

				boolean primerEncuentro;

				while (rs.next())
				{
					parejasResultadoList = new Vector<String>();

					//No he encontrado el primer campo, en cuyo caso NO debo poner
					//guión anterior
					primerEncuentro=true;
					parejasResultado="";

					//					Si es la primera vez que lo encontramos, ponemos el valor
					//en true y si no ponemos guion anterior
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(1));
					else
						parejasResultado=parejasResultado + rs.getString(1);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado= parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(2));
					else
						parejasResultado=parejasResultado + rs.getString(2);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(3));
					else
						parejasResultado=parejasResultado + rs.getString(3);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(4));
					else
						parejasResultado=parejasResultado + rs.getString(4);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(5));
					else
						parejasResultado=parejasResultado + rs.getString(5);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(6));
					else
						parejasResultado=parejasResultado + rs.getString(6);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(7));
					else
						parejasResultado=parejasResultado + rs.getString(7);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(8));
					else
						parejasResultado=parejasResultado + rs.getString(8);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(9));
					else
						parejasResultado=parejasResultado + rs.getString(9);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(10));
					else
						parejasResultado=parejasResultado + rs.getString(10);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(11));
					else
						parejasResultado=parejasResultado + rs.getString(11);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(12));
					else
						parejasResultado=parejasResultado + rs.getString(12);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector)
						parejasResultadoList.add(rs.getString(13));
					else
						parejasResultado=parejasResultado + rs.getString(13);

					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}

					if(usarVector) {
						parejasResultadoList.add(rs.getString(14));
					} else {
						parejasResultado=parejasResultado + rs.getString(14);
					}

					if(usarVector) {
						vectorResultados.add(parejasResultadoList);
						pageContext.setAttribute("resultados" ,  vectorResultados);
					} else {
						cadenaResultados.add(parejasResultado);
						pageContext.setAttribute("resultados" ,  cadenaResultados);
					}
				}

			}
			
			if(usarVector) {
				pageContext.setAttribute("resultados" ,  vectorResultados);
			} else {
				pageContext.setAttribute("resultados" ,  cadenaResultados);
			}
			
		}	   
	    catch (java.sql.SQLException e)
		{
	    	e.printStackTrace();
			logger.warn(e.getMessage());
			throw new JspTagException("TagBusquedaCuentasCobro: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
			throw new JspTagException("TagBusquedaCuentasCobro: "+e.getMessage()+e.toString());
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, null);
		}
	}
	
	
    /**
     * @return Retorna con.
     */
    public Connection getCon() {
        return con;
    }
    /**
     * @param con Asigna con.
     */
    public void setCon(Connection con) {
        this.con = con;
    }
    /**
     * @return Retorna criterioBusqueda.
     */
    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }
    /**
     * @param criterioBusqueda Asigna criterioBusqueda.
     */
    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }
    /**
     * @return Retorna esPorNombre.
     */
    public boolean isEsPorNombre() {
        return esPorNombre;
    }
    /**
     * @param esPorNombre Asigna esPorNombre.
     */
    public void setEsPorNombre(boolean esPorNombre) {
        this.esPorNombre = esPorNombre;
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
     * @return Retorna filtroBusqueda.
     */
    public String getFiltroBusqueda() {
        return filtroBusqueda;
    }
    /**
     * @param filtroBusqueda Asigna filtroBusqueda.
     */
    public void setFiltroBusqueda(String filtroBusqueda) {
        this.filtroBusqueda = filtroBusqueda;
    }    
    /**
     * @return Retorna rest1nombre.
     */
    public String getRest1nombre() {
        return rest1nombre;
    }
    /**
     * @param rest1nombre Asigna rest1nombre.
     */
    public void setRest1nombre(String rest1nombre) {
        this.rest1nombre = rest1nombre;
    }
    /**
     * @return Retorna rest1valor.
     */
    public String getRest1valor() {
        return rest1valor;
    }
    /**
     * @param rest1valor Asigna rest1valor.
     */
    public void setRest1valor(String rest1valor) {
        this.rest1valor = rest1valor;
    }
    /**
     * @return Retorna rest2nombre.
     */
    public String getRest2nombre() {
        return rest2nombre;
    }
    /**
     * @param rest2nombre Asigna rest2nombre.
     */
    public void setRest2nombre(String rest2nombre) {
        this.rest2nombre = rest2nombre;
    }
    /**
     * @return Retorna rest2valor.
     */
    public String getRest2valor() {
        return rest2valor;
    }
    /**
     * @param rest2valor Asigna rest2valor.
     */
    public void setRest2valor(String rest2valor) {
        this.rest2valor = rest2valor;
    }

	/**
	 * @return the usarVector
	 */
	public boolean isUsarVector() {
		return usarVector;
	}

	/**
	 * @param usarVector the usarVector to set
	 */
	public void setUsarVector(boolean usarVector) {
		this.usarVector = usarVector;
	}
    
}
