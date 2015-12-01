package util;

import java.sql.SQLException;

import org.apache.struts.action.ActionMessage;

/**
 * Clase genérica para tratar errores con información en el archivo de recursos y retornarlos fácilmente como un ActionError
 * @author alejo
 *
 */
@SuppressWarnings("serial")
public class Errores extends Exception
{
	private String recurso;
	private Object[] parametros;
	private Tipo tipo;
	public enum Tipo{
		INFORMATIVO,
		ERROR
	};

	/**
	 * Mensaje de error para adicionar errores al struts
	 * @param recurso
	 */
	public Errores(String recurso)
	{
		this.recurso=recurso;
		this.parametros=new Object[0];
	}

	/**
	 * Mensaje de error para adicionar errores al struts
	 * @param recurso
	 * @param tipo de error
	 */
	public Errores(String recurso, Tipo tipo)
	{
		this.recurso=recurso;
		this.tipo=tipo;
	}

	/**
	 * Mensaje de error para adicionar errores al struts
	 * @param recurso
	 */
	public Errores(String recurso, Object[] parametros)
	{
		this.recurso=recurso;
		this.parametros=parametros;
	}

	/**
	 * Mensaje de error para adicionar errores al struts
	 * @param recurso
	 */
	public Errores(String recurso, Object[] parametros, Tipo tipo)
	{
		this.recurso=recurso;
		this.parametros=parametros;
		this.tipo=tipo;
	}

	public Errores(String mensaje, String recurso)
	{
		super(mensaje);
		this.recurso=recurso;
		this.parametros=new Object[0];
	}
	
	public Errores(String mensaje, String recurso, Object parametro0)
	{
		super(mensaje);
		this.recurso=recurso;
		this.parametros=new Object[1];
		this.parametros[0]=parametro0;
	}
	
	public Errores(String mensaje, String recurso, Object parametro0, Object parametro1)
	{
		super(mensaje);
		this.recurso=recurso;
		this.parametros=new Object[2];
		this.parametros[0]=parametro0;
		this.parametros[1]=parametro1;
	}
	
	public Errores(String mensaje, String recurso, Object parametro0, Object parametro1, Object parametro2)
	{
		super(mensaje);
		this.recurso=recurso;
		this.parametros=new Object[3];
		this.parametros[0]=parametro0;
		this.parametros[2]=parametro1;
		this.parametros[2]=parametro2;
	}
	
	public Errores(String mensaje, String recurso, Object parametro0, Object parametro1, Object parametro2, Object parametro3)
	{
		super(mensaje);
		this.recurso=recurso;
		this.parametros=new Object[4];
		this.parametros[0]=parametro0;
		this.parametros[2]=parametro1;
		this.parametros[2]=parametro2;
		this.parametros[3]=parametro3;
	}
	
	public Errores(String mensaje, String recurso, Object[] parametros)
	{
		super(mensaje);
		this.recurso=recurso;
		this.parametros=parametros;
	}
	
	public Errores(SQLException errorSQL)
	{		
		/*if(errorSQL.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente))
		{
			this("Registro existente", "")
		}
		else if(errorSQL.getSQLState().equals())*/
		this("Problemas accediendo la base de datos:"+errorSQL.getMessage(), "errors.problemasBd");
	}
	
	/**
	 * Retorna el action error basado en el recurso y los parámetros
	 * @return
	 */
	public ActionMessage getActionMessage()
	{
		return new ActionMessage(this.recurso, this.parametros);
	}

	/**
	 * Indica si ya fue o no asignado un error
	 * 
	 * @return true en caso de no existir mensaje de error
	 */
	public boolean isEmpty()
	{
		return UtilidadTexto.isEmpty(recurso);
	}

	/**
	 * Obtiene el valor del atributo recurso
	 *
	 * @return Retorna atributo recurso
	 */
	public String getRecurso()
	{
		return recurso;
	}

	/**
	 * Establece el valor del atributo recurso
	 *
	 * @param valor para el atributo recurso
	 */
	public void setRecurso(String recurso)
	{
		this.recurso = recurso;
	}

	/**
	 * Obtiene el valor del atributo parametros
	 *
	 * @return Retorna atributo parametros
	 */
	public Object[] getParametros()
	{
		return parametros;
	}

	/**
	 * Establece el valor del atributo parametros
	 *
	 * @param valor para el atributo parametros
	 */
	public void setParametros(Object[] parametros)
	{
		this.parametros = parametros;
	}

	/**
	 * Obtiene el valor del atributo tipo
	 *
	 * @return Retorna atributo tipo
	 */
	public Tipo getTipo()
	{
		return tipo;
	}

	/**
	 * Establece el valor del atributo tipo
	 *
	 * @param valor para el atributo tipo
	 */
	public void setTipo(Tipo tipo)
	{
		this.tipo = tipo;
	}
	
}
