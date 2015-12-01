/*
 * @(#)TagBusquedaSustitutosArticulos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.Answer;
import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Tag que realiza la b�squeda de los art�culos para sustituir
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 */
public class TagBusquedaSustitutosArticulos extends BodyTagSupport
{
	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagBusquedaSustitutosArticulos.class);
	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "@";
	
	/**
	 * Objeto que representa una conexi�n con una base de datos.
	 */
	private Connection con=null;
	
	/**
	 * C�digo del art�culo principal
	 */
	private int codigoArticuloPrincipal;
	
	/**
	 * 
	 */
	private int codigoFarmacia;
	
	/**
	 * 
	 */
	private int institucion;
	
	
	private Collection articulosInsertadosBD;
	
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
					throw new JspTagException("Error escribiendo Tag Busqueda Sustitutos Articulos : " + e.getMessage()+e.toString());
			}
		}
		clean();
		return EVAL_PAGE;
	}

	private void funcionalidad() throws JspTagException
	{
		try
		{
			
			logger.info("tam col== "+articulosInsertadosBD.size());
			
			TagDao tagDao;
			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");

			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			Answer a;
			ResultSetDecorator rs = null;
			String consulta="";
			
			String restriccionAnteriores="";
			if (this.articulosInsertadosBD!=null)
			{
				Iterator it= articulosInsertadosBD.iterator();
				while (it.hasNext())
				{
					restriccionAnteriores=restriccionAnteriores+ " and codigo!= " +it.next();
				}
			}
			
			
			//String campoConsulta1="getTotalExisArticulosXAlmacen( "+codigoFarmacia+" , codigo, "+usuario.getCodigoInstitucionInt()+")";
			consulta= "SELECT " +
							" codigo, " +
							" descripcion, " +
							" concentracion, " +
							" getNomFormaFarmaceutica(forma_farmaceutica), " +
							" getNomUnidadMedida(unidad_medida), " ;
			if(codigoFarmacia>ConstantesBD.codigoNuncaValido && institucion>ConstantesBD.codigoNuncaValido)
				consulta=consulta	+			" getTotalExisArticulosXAlmacen( "+codigoFarmacia+" , codigo, "+institucion+") as existencias " ;
			else
				consulta=consulta	+			" existencias " ;
			
			
			consulta=consulta	+",naturaleza from view_articulos " +
						"where codigo in (select articulo_equivalente from equivalentes_inventario where articulo_ppal="+codigoArticuloPrincipal+") and estado="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+restriccionAnteriores;
			
			
			a=tagDao.resultadoConsulta(con, consulta);

			rs  = a.getResultSet();
			con = a.getConnection();
			
			//Variables locales para no tener problemas con pools de conexiones

			Vector resultados=new Vector(15,5);
			String parejasResultado="";

			boolean primerEncuentro;
			
			while (rs.next())
			{
				//No he encontrado el primer campo, en cuyo caso NO debo poner
				//gui�n anterior
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
						parejasResultado= parejasResultado + is_separador;
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
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(6);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(7);//naturaleza del articulo
				
				resultados.add(parejasResultado);  
			}	
			
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
		{
			logger.warn(e.getMessage());
			throw new JspTagException("TagBusquedaSustitutosArticulos: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
		throw new JspTagException("TagBusquedaSustitutosArticulos: "+e.getMessage()+e.toString());
		}
	}

	/**
	 * resetea los valores
	 */
	private void clean()
	{
		is_separador = "@";
		this.codigoArticuloPrincipal=-1;
		this.codigoFarmacia=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.articulosInsertadosBD= new ArrayList();
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
	 * @return Returns the codigoArticuloPrincipal.
	 */
	public int getCodigoArticuloPrincipal() {
		return codigoArticuloPrincipal;
	}
	
	/**
	 * @param codigoArticuloPrincipal The codigoArticuloPrincipal to set.
	 */
	public void setCodigoArticuloPrincipal(int codigoArticuloPrincipal) {
		this.codigoArticuloPrincipal = codigoArticuloPrincipal;
	}
	
	/**
	 * @return Returns the articulosInsertadosBD.
	 */
	public Collection getArticulosInsertadosBD() {
		return articulosInsertadosBD;
	}
	/**
	 * @param articulosInsertadosBD The articulosInsertadosBD to set.
	 */
	public void setArticulosInsertadosBD(Collection articulosInsertadosBD) {
		this.articulosInsertadosBD = articulosInsertadosBD;
	}

	public int getCodigoFarmacia() {
		return codigoFarmacia;
	}

	public void setCodigoFarmacia(int codigoFarmacia) {
		this.codigoFarmacia = codigoFarmacia;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
}
