package util.tag;

import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.UtilidadBD;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Vector;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

public class TagBusquedaUsuariosEpi extends BodyTagSupport {
	
	private Connection con;
    
    public int doStartTag() throws JspTagException {
    	    	    	
		if (con != null) {
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
					throw new JspTagException("Error escribiendo TagBusquedaUsuariosEpi : " + e.getMessage());
			}

		}

	//	clean();
		return EVAL_PAGE;
	}
    
    
    private void clean()
    {
     //   loginUsuario="";
    }
    
    private void funcionalidad() throws JspTagException 
	{
        TagDao tagDao;
        ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		
		ResultSetDecorator resultado;
		Vector resultados = new Vector();
		
		try {
						
			resultado = tagDao.consultaUsuariosEpidemiologia(con);
			
			while (resultado.next()) {
				 
				String elemento = resultado.getString("login")+"---"+resultado.getString("primer_nombre")+" "+resultado.getString("segundo_nombre")+" "+resultado.getString("primer_apellido");
				
				resultados.add(elemento);
			}
		}
		catch (SQLException sqle) {
			throw new JspTagException(sqle.toString());
		}
		
		pageContext.setAttribute("resultados", resultados);
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}
