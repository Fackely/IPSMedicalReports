package com.princetonsa.mundo.inventarios;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.Errores;
import util.IdentificadoresExcepcionesSql;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MezclasDao;

public class Mezcla 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(Mezcla.class);

	private int consecutivo;
	
	private String codigo;
	
	private String nombre;
	
	private InfoDatosInt tipo;
	
	private String activo;
	
	private int codInstitucion;
	
	public Mezcla()
	{
		this.consecutivo=-1;
		this.codigo="";
		this.nombre="";
		this.tipo = new InfoDatosInt();
		this.activo="";
		this.codInstitucion=-1;
	}
	
	public int getConsecutivo()
	{
		return this.consecutivo;
	}
	
	public void setConsecutivo(int consecutivo)
	{
		this.consecutivo=consecutivo;
	}
	
	public String getCodigo()
	{
		return this.codigo;
	}
	
	public void setCodigo(String codigo)
	{
		this.codigo=codigo;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	public InfoDatosInt getTipo()
	{
		return this.tipo;
	}
	
	public void setTipo(InfoDatosInt tipo)
	{
		this.tipo = tipo;
	}
	
	public void setTipo(int codigoTipo, String nombreTipo)
	{
		this.tipo = new InfoDatosInt();
		this.tipo.setCodigo(codigoTipo);
		this.tipo.setNombre(nombreTipo);
	}
	
	public String getActivo()
	{
		return this.activo;
	}
	
	public void setActivo(String activo)
	{
		this.activo=activo;
	}
	
	public int getCodInstitucion()
	{
		return this.codInstitucion;
	}
	
	public void setCodInstitucion(int codInstitucion)
	{
		this.codInstitucion=codInstitucion;
	}
	
    private static MezclasDao mezclasDao;
    
    private static MezclasDao getMezclasDao()
    {
        if(mezclasDao==null)
        {
            String tipoBD = System.getProperty("TIPOBD" );
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            mezclasDao=myFactory.getMezclasDao();
        }
        return mezclasDao;
    }

    public ActionErrors insertar(Connection con)
	{
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        ActionErrors errores = new ActionErrors();
        
        try
        {
	        inicioTransaccion = myFactory.beginTransaction(con);
	        if(inicioTransaccion)
	        {
	        	Mezcla.getMezclasDao().insertarMezcla(con, this.getCodigo(), this.getNombre(), this.getTipo().getCodigo(), this.getActivo(), this.getCodInstitucion());
	        }
	        else
	        {
	        	logger.warn("Problemas iniciando la transacción");
	        	errores.add("", new ActionMessage("Problemas iniciando la transacción", "errors.transaccion"));
	        }

	        myFactory.endTransaction(con);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
        	try
        	{
        		if(inicioTransaccion)
        			myFactory.abortTransaction(con);

        		//if(se.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente))
        			errores.add("", new ActionMessage("error.mezclas.mezclaYaExiste", this.getCodigo()));
        	}
        	catch(SQLException se2)
        	{
            	logger.warn(se2);
        	}
        }
        return errores;
	}

    public void actualizar(Connection con) throws Errores
	{
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

        try
        {
	        inicioTransaccion = myFactory.beginTransaction(con);
	        if(inicioTransaccion)
	        {
	        	Mezcla.getMezclasDao().actualizarMezcla(con, this.getConsecutivo(), this.getNombre(), this.getTipo().getCodigo(), this.getActivo());
	        }
	        else
	        {
	        	logger.warn("Problemas iniciando la transacción");
	        	throw new Errores("Problemas iniciando la transacción", "errors.transaccion");
	        }

	        myFactory.endTransaction(con);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
        	try
        	{
        		if(inicioTransaccion)
        			myFactory.abortTransaction(con);
        	}
        	catch(SQLException se2)
        	{
            	logger.warn(se2);
            	se.setNextException(se2);
        	}
			throw new Errores(se);
        }
	}
    
    public void eliminar(Connection con) throws Errores
	{
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

        try
        {
	        inicioTransaccion = myFactory.beginTransaction(con);
	        if(inicioTransaccion)
	        {
	        	Mezcla.getMezclasDao().eliminarMezcla(con, this.getConsecutivo());
	        }
	        else
	        {
	        	logger.warn("Problemas iniciando la transacción");
	        	throw new Errores("Problemas iniciando la transacción", "errors.transaccion");
	        }

	        myFactory.endTransaction(con);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
        	try
        	{
        		if(inicioTransaccion)
        			myFactory.abortTransaction(con);

        		//if(se.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlViolacionForanea))
    				throw new Errores(
    						"La mezcla no se puede eliminar por violación de llave foránea", 
    						"error.mezclas.noSePuedeEliminar", this.getCodigo());
        	}
        	catch(SQLException se2)
        	{
            	logger.warn(se2);
            	se.setNextException(se2);
        	}
			throw new Errores(se);
        }
	}
    
    public void consultar(Connection con, int consecutivo) throws Errores
    {
    	try
    	{
	    	HashMap tempoMezcla = Mezcla.getMezclasDao().consultarMezcla(con, consecutivo);
	    	int cantidadRegistros = Integer.parseInt((String)tempoMezcla.get("numRegistros"));
	    	
	    	if(cantidadRegistros>0)
	    	{
	    		this.setConsecutivo(consecutivo);
	    		this.setCodigo((String)tempoMezcla.get("codigo_0"));
	    		this.setNombre((String)tempoMezcla.get("nombre_0"));
	    		this.setTipo( Utilidades.convertirAEntero(tempoMezcla.get("codtipo_0")+""), (String)tempoMezcla.get("nomtipo_0"));
	    		this.setActivo( tempoMezcla.get("activo_0")+"");
	    		this.setCodInstitucion( Utilidades.convertirAEntero(tempoMezcla.get("codinstitucion_0")+""));
	    	}
	    	else
	    	{
	        	logger.warn("Problemas iniciando la transacción");
	        	throw new Errores("La mezcla no existe", "errors.noExiste", "La mezcla con consecutivo"+consecutivo);
	    	}
    	}
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
    }
    
    public static HashMap consultarMezclasInstitucion(Connection con, int codInstitucion) throws SQLException
    {
       	return Mezcla.getMezclasDao().consultarMezclasInst(con, codInstitucion);
    }
}
