/*
 * Created on 04/05/2006
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Lenguaje		:Java
 *
 */
package util;

import java.sql.SQLException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;


/**
 * @version 1.0, 04/05/2006
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ManejoExcepciones 
{
	public static ActionErrors manejoGenericoExcepcionesSql(SQLException e)
	{
		ActionErrors error=new ActionErrors();
		while (e != null) 
		{
			error.add("ERROR "+e.getSQLState(), new ActionMessage("errors.excepcionSQL.generico."+e.getSQLState()));
            e = e.getNextException();
		}
        return error;
	}

}
