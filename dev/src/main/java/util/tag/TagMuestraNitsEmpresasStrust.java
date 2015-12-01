/*
 * @(#)TagMuestraNitsEmpresas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.tag;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Tag que conserva los nits de la tabla terceros que NO 
 *  han sido seleccionados en la tabla empresas, además
 *  muestra su correspondiente descripción.
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 */
public class TagMuestraNitsEmpresasStrust extends BodyTagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagMuestraNitsEmpresasStrust.class);
	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "-";
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;
	
	/**
	 * Variable que se utiliza para capturar el valor
	 * del "OR" en la consulta, esto se hizo con el fin de
	 * poder mostrar en el tag el dato correspondiente al que
	 * se quiere modificar de la tabla empresa 
	 */	
	private String campoOr="";
	
	private String tipoConsulta="";
	
	/**
	 * codigo de la institucion
	 */
	private String codigoInstitucion="";

	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP).
	 */
	public int doStartTag() throws JspTagException
	{
			funcionalidad();
			return EVAL_BODY_BUFFERED;
	}
	
	public int doEndTag() throws JspTagException
	{
		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Muestra Opciones Nits Empresas Struts : " + e.getMessage()+e.toString());
			}
		}
		clean();
		return EVAL_PAGE;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void funcionalidad() throws JspTagException
	{

		String consulta="";
		PreparedStatementDecorator psd = null;
		ResultSetDecorator rs = null;
		
		try
		{
			//TagDao tagDao;
			//ServletContext sc=pageContext.getServletContext();
			//String tipoBD=(String)sc.getInitParameter("TIPOBD");

			//DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			//tagDao = myFactory.getTagDao();

			//Answer a;
			//ResultSetDecorator rs = null;
			
			
			if(tipoConsulta.equals("empresa"))
				consulta= "SELECT codigo, numero_identificacion, descripcion, direccion, telefono FROM terceros  WHERE institucion='"+codigoInstitucion+"' AND codigo<>ALL(SELECT e.tercero FROM empresas e, terceros t WHERE e.tercero=t.codigo AND t.institucion='"+codigoInstitucion+"' ) AND codigo<>0 AND activo= " +ValoresPorDefecto.getValorTrueParaConsultas();
			
			if(tipoConsulta.equals("convenio"))
				consulta= "SELECT codigo, razon_social, telefono FROM empresas WHERE institucion='"+codigoInstitucion+"' AND codigo<>ALL(SELECT c.empresa FROM convenios c, empresas e, terceros t WHERE c.empresa=e.codigo AND e.tercero=t.codigo AND t.institucion='"+codigoInstitucion+"') AND codigo<>0 AND activo=" +ValoresPorDefecto.getValorTrueParaConsultas();
			
			if(!campoOr.equals(""))
				consulta = consulta + " or codigo = " +campoOr;
			
			if(tipoConsulta.equals("empresa"))
			    consulta= consulta +" ORDER BY descripcion ";
			
			if(tipoConsulta.equals("convenio"))
			    consulta= consulta +" ORDER BY razon_social ";
			
			
			//a=tagDao.resultadoConsulta(con, consulta);

			psd = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(psd.executeQuery());	
			
			//rs  = a.getResultSet();
			//con = a.getConnection();
			
			//Variables locales para no tener problemas con pools de conexiones

			Vector resultados=new Vector(15,5);
			String parejasResultado="";

			boolean primerEncuentro;
			while (rs.next())
			{
				//No he encontrado el primer campo, en cuyo caso NO debo poner
				//guión anterior
				primerEncuentro=true;
				parejasResultado="";
				
				//Si es la primera vez que lo encontramos, ponemos el valor
				//en true y si no ponemos guion anterior
				if (primerEncuentro==true)
				{
					  primerEncuentro=false;
				}
				else
				{
					  parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(1);
				
				if (primerEncuentro==true)
				{
						primerEncuentro=false;
				}
				else
				{
						parejasResultado= parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(2);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(3);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(4);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(5);
				
				resultados.add(parejasResultado);  
			}	
			
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
		{
			logger.info("Consulta: " + consulta + " \n " + " -> Código institución: " +codigoInstitucion);
			logger.error(e.getMessage());
			throw new JspTagException("TagMuestraNitsEmpresasStruts: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		throw new JspTagException("TagMuestraNitsEmpresasStruts: "+e.getMessage()+e.toString());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(psd != null) {
					psd.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * resetea los valores
	 */
	private void clean()
	{
		is_separador = "-";
		this.campoOr = "";
		this.tipoConsulta="";
		this.codigoInstitucion="";
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
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon()
	{
		return con;
	}

	/**
	 * Metodo "Set" que recibe una conexion
	 * para permitir manejar todos los tags
	 * de una misma pagina con la misma
	 * conexion
	 * @param con conexion
	 */
	public void setCon(Connection connection) 
	{
		con = connection;
	}

	/**
	 * Método que retorna el valor del "OR" en la consulta
	 * @return
	 */
	public String getCampoOr ()
	{
		return this.campoOr;
	}
	
	/**
	 * Método que asigna el valor del "OR" en la consulta
	 * @param campoOr
	 */
	public void setCampoOr (String campoOr)
	{
		this.campoOr=campoOr;
	}

	/**
	 * Método que retorna si la subconsulta es de 
	 * empresas, convenios o contratos
	 * @return
	 */
	public String getTipoConsulta() {
		return this.tipoConsulta;
	}

	/**
	 * Método que asigna si la subconsulta es de 
	 * empresas, convenios o contratos
	 * @param i
	 */
	public void setTipoConsulta(String tipo) {
		this.tipoConsulta = tipo;
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
}
