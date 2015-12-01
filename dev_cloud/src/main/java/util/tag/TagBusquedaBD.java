
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.Answer;
import util.InfoDatos;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Tag implementado para la busqueda de ajustes
 * empresa.
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TagBusquedaBD extends BodyTagSupport 
{
    /**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagBusquedaBD.class);	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;
	/**
	 * tipo de consulta que se ejecuta
	 */
	private String tipoConsulta;	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String separador = "@";	
	/**
	 * sentencia where del query
	 */
	private String where;
	/**
	 * sentencia and del query
	 */
	private String and;
	/**
	 * para identificar si ya existe la clausula
	 * WHERE en el query
	 */
	private boolean existeWhere;
	/**
	 * clausula inner join del query
	 */
	private String innerJoin1;	
	/**
	 * almacena objetos de tipoInfoDatos, 
	 * cada Objeto posee el nombre del campo por 
	 * el cual se va a buscar y el respectivo
	 * valor.
	 */
	private HashMap mapaTag;
	/**
	 * numero de objetos en el mapa
	 */
	private int numRegistros;
	
	
	
	
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
					throw new JspTagException("Error escribiendo TagBusquedaAjustesEmpresa : " + e.getMessage());
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
	    this.separador="@";	    	   
	    this.where=" WHERE ";
	    this.and=" AND ";
	    this.existeWhere=false;
	    this.innerJoin1="";
	    this.tipoConsulta="";
	    this.mapaTag=new HashMap();
	    this.numRegistros=0;
	}
	
	
	/**
	 * Este método obtiene de la BD las Cuentas de Cobro de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
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
			InfoDatos campo = new InfoDatos();
			if(this.tipoConsulta.equals("1"))
			{
				
			    consulta="SELECT " +
									"ae.codigo as codigo_ajuste," +
									"ae.castigo_cartera as castigo_cartera," +
									"ae.tipo_ajuste as tipo_ajuste," +
									"ae.fecha_ajuste as fecha_ajuste," +
									"ae.cuenta_cobro as cuenta_cobro," +
									"ae.concepto_ajuste as concepto_ajuste," +
									"ae.metodo_ajuste as metodo_ajuste," +
									"ae.valor_ajuste as valor_ajuste," +
									"ae.observaciones as observaciones " +
									"FROM ajustes_empresa ae ";
			    
			    if(!this.mapaTag.isEmpty())
			    {
			        for(int k=0;k<this.numRegistros;k++)
			        {
			            campo=(InfoDatos)this.mapaTag.get(k+"");
			            if(!this.existeWhere)
				        {
				            consulta+=this.where+campo.getId()+"="+campo.getValue();
				            this.existeWhere=true;
				        }
				        else
				        {
				            consulta+=this.and+campo.getId()+"="+campo.getValue();
				        }
			        }
			    }
			    
			    logger.info("consulta tag->"+consulta);					
			}
			a=tagDao.resultadoConsulta(con, consulta);
			rs  = a.getResultSet();
			con = a.getConnection();
			Vector resultados=new Vector(18,9);
			String parejasResultado="";
			boolean primerEncuentro;
			
			while (rs.next())
			{
//			  No he encontrado el primer campo, en cuyo caso NO debo poner
				//guión anterior
				primerEncuentro=true;
				parejasResultado="";
				
//				Si es la primera vez que lo encontramos, ponemos el valor
				//en true y si no ponemos guion anterior
				if (primerEncuentro==true)
				{
					  primerEncuentro=false;
				}
				else
				{
					  parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(1);   
				if (primerEncuentro==true)
				{
						primerEncuentro=false;
				}
				else
				{
						parejasResultado= parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(2);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(3);

				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(4);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(5);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(6);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(7);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(8);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + this.separador;
				}
				parejasResultado=parejasResultado + rs.getString(9);
				
				resultados.add(parejasResultado);
			}
			pageContext.setAttribute("resultados" ,  resultados);
		}
	    catch (java.sql.SQLException e)
		{
			logger.warn(e.getMessage());
			throw new JspTagException("TagBusquedaAjustesEmpresa: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
			throw new JspTagException("TagBusquedaAjustesEmpresa: "+e.getMessage()+e.toString());
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
     * @return Retorna separador.
     */
    public String getSeparador() {
        return separador;
    }
    /**
     * @param separador Asigna separador.
     */
    public void setSeparador(String separador) {
        this.separador = separador;
    }    
    /**
     * @return Retorna innerJoin1.
     */
    public String getInnerJoin1() {
        return innerJoin1;
    }
    /**
     * @param innerJoin1 Asigna innerJoin1.
     */
    public void setInnerJoin1(String innerJoin1) {
        this.innerJoin1 = innerJoin1;
    }
    /**
     * @return Retorna tipoConsulta.
     */
    public String getTipoConsulta() {
        return tipoConsulta;
    }
    /**
     * @param tipoConsulta Asigna tipoConsulta.
     */
    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }
    /**
     * @return Retorna mapaTag.
     */
    public HashMap getMapaTag() {
        return mapaTag;
    }
    /**
     * @param mapaTag Asigna mapaTag.
     */
    public void setMapaTag(HashMap mapaTag) {
        this.mapaTag = mapaTag;
    }
    /**
     * @return Retorna numRegistros.
     */
    public int getNumRegistros() {
        return numRegistros;
    }
    /**
     * @param numRegistros Asigna numRegistros.
     */
    public void setNumRegistros(int numRegistros) {
        this.numRegistros = numRegistros;
    }
}
