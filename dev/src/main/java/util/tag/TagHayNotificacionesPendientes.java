/*
 * Creado en 02-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import util.ConstantesBD;

/**
 * @author santiago
 *
 */
public class TagHayNotificacionesPendientes extends BodyTagSupport {

    private Connection con;    
    private String loginUsuario;
    private int institucion;
    
    public int doStartTag() throws JspTagException {
		if (loginUsuario != "") {
			funcionalidad();
			return EVAL_BODY_BUFFERED;
		}
		else {
			return SKIP_BODY;
		}
	}
    
    public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo TagHayNotificacionesPendientes : " + e.getMessage());
			}

		}

		clean();
		return EVAL_PAGE;
	}
    
    
    private void clean()
    {
        loginUsuario="";
    }
    
       
    private void funcionalidad() throws JspTagException 
	{
        TagDao tagDao;
        ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		
		ResultSetDecorator resultado;
		
		int incompletas=0,seguimiento=0,completas=0;
		
		try {
		    resultado = tagDao.consultaHayFichasPendientes(con,loginUsuario);
		    
		    while (resultado.next()) {
		        
		        int codigo=0;
		        codigo = resultado.getInt("estado");
		        
		        if (codigo==ConstantesBD.codigoEstadoFichaIncompleta) {
		            incompletas++;
		        }
		    /*    else if (codigo==ConstantesBD.codigoEstadoFichaSeguimiento) {
		            seguimiento++;
		        }
		        */
		        else if (codigo==ConstantesBD.codigoEstadoFichaCompleta) {
		            completas++;
		        }
		    }
		}
		catch (Exception e)
		{
		    throw new JspTagException(e.toString());
		}
		
		pageContext.setAttribute("incompletas",Integer.toString(incompletas));
		pageContext.setAttribute("seguimiento",Integer.toString(seguimiento));
		pageContext.setAttribute("completas",Integer.toString(completas));
	}
    
    
    /**
     * @return Returns the codigoUsuario.
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }
    /**
     * @param codigoUsuario The codigoUsuario to set.
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }
    /**
     * @return Returns the con.
     */
    public Connection getCon() {
        return con;
    }
    /**
     * @param con The con to set.
     */
    public void setCon(Connection con) {
        this.con = con;
    }
    /**
     * @return Returns the institucion.
     */
    public int getInstitucion() {
        return institucion;
    }
    /**
     * @param institucion The institucion to set.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
}