/*
 * Created on 31-may-2004
 * Usuario juanda
 *
 * juan@princetonSA.com
 */
package util.tag;

import java.sql.Connection;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.ResultadoCollectionDB;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Creado el 31-may-2004
 * @author juanda
 * 
 *	juan@princetonSA.com
 */
public class TagMuestraFuncionalidadesTercerNivel extends BodyTagSupport
{
	/**
	 * Manejo logs del Tag
	 */
	private Logger logger=Logger.getLogger(TagMuestraFuncionalidadesTercerNivel.class);
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;
	
	/**
	 * Login del Usuario para verificar los roles del mismo
	 */
	private String login;
	
	/**
	 * Si se desea dibujar todos las funcionalidades hijas
	 * de una funcionalidad padre que pertenezcan al rol
	 * del usuario 
	 */
	private int funcionalidadPadre;

	/**
	 * Cuando se desea especificar que funcionalidades
	 * hijas se deben dibujar
	 */
	private String funcionalidadesADibujar;
	
	/**
	 *	Finalización del tag 
	 */
	public int doEndTag()
	{
		BodyContent bodyContent = getBodyContent();
		
		if(bodyContent!=null)
		{
			try{
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
			catch(Exception e)
			{
				logger.error("Error al escribir en el contenedor "+e);
			}
		}
		clean();
		return EVAL_PAGE;
	}
	
	/**
	 * Inicio del tag
	 */
	public int doStartTag()
	{
		if (login != null && !login.trim().equals("") && con!=null)
		{
			if(funcionalidadPadre!=0)
			{
				getData(true);
			}
			else if(funcionalidadesADibujar!=null && !funcionalidadesADibujar.equals(""))
			{
				String tempo[]=funcionalidadesADibujar.split("-");
				int i=0;
				funcionalidadesADibujar="";
				boolean salida=true;
				while(salida)
				{
					try{
						funcionalidadesADibujar+=tempo[i];
						i++;
						if(!tempo[i].equals(""))
						{
							funcionalidadesADibujar+=",";
						}
					}
					catch(Exception e)
					{
						salida=false;
					}
				}
				getData(false);
			}
			else
			{
				logger.error("Debe utilizar el parametro funcionalidadPadre ( 2 ) ó funcionalidadesADibujar (21-34-56)");
			}
			return EVAL_BODY_BUFFERED;
		}
		else
		{
			return SKIP_BODY;
		}
	}
	
	/**
	 * @return conexión con una base de datos.
	 */
	public Connection getCon()
	{
		return con;
	}

	/**
	 * @return funcionalidades
	 * hijas se deben dibujar
	 */
	public String getFuncionalidadesADibujar()
	{
		return funcionalidadesADibujar;
	}

	/**
	 * @return funcionalidad padre
	 */
	public String getFuncionalidadPadre()
	{
		return String.valueOf(funcionalidadPadre);
	}

	/**
	 * @return login del Usuario para verificar los roles del mismo
	 */
	public String getLogin()
	{
		return login;
	}

	/**
	 * @param connection conexión con una base de datos.
	 */
	public void setCon(Connection connection)
	{
		con = connection;
	}

	/**
	 * @param funcionalidades Funcionalidades
	 * hijas se deben dibujar
	 */
	public void setFuncionalidadesADibujar(String funcionalidades)
	{
		funcionalidadesADibujar = funcionalidades;
	}

	/**
	 * @param codigoFuncionalidad funcionalidad padre
	 */
	public void setFuncionalidadPadre(String codigoFuncionalidad)
	{
		if(codigoFuncionalidad!=null && !codigoFuncionalidad.equals(""))
			funcionalidadPadre = Integer.parseInt(codigoFuncionalidad);
	}

	/**
	 * @param login login del Usuario para verificar los roles del mismo
	 */
	public void setLogin(String login)
	{
		this.login = login;
	}

	/**
	 * Obtener funcionalidades que se deben dibujar
	 */
	private void getData(boolean modo)
	{
		try
		{
			String tipoBD=(String)pageContext.getServletContext().getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			TagDao tagDao = myFactory.getTagDao();
			ResultadoCollectionDB resultado=tagDao.funcionalidadesADibujar(con, login, funcionalidadPadre, funcionalidadesADibujar, modo);
	
			if(!resultado.getFilasRespuesta().isEmpty()){
				pageContext.setAttribute("funcionalidades", resultado.getFilasRespuesta());
			}
			else
			{
				String mensajeError="Usted no tiene los permisos necesarios para acceder a este submen&uacute;";
				pageContext.removeAttribute("funcionalidades");
				pageContext.setAttribute("mensajeError",mensajeError);
			}
		}
		catch (Exception e)
		{
			UtilidadBD.abortarTransaccion(con);
			logger.error(e);
		}
		finally
		{
			UtilidadBD.finalizarTransaccion(con);
		}
	}

	private void clean()
	{
		this.funcionalidadesADibujar="";
		this.funcionalidadPadre=0;
		this.login="";
	}

}
