/*
 * Created on 23/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MotDevolucionInventariosDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class MotDevolucionInventarios
{
	
	
	private static Logger logger=Logger.getLogger(MotDevolucionInventarios.class);
	
    /**
	 * Mapa para manejar los motivo
	 */
	private HashMap motivos;
	
	/**
	 * DAO de este objeto, para trabajar con ConceptosPagoCartera
	 * en la fuente de datos
	 */    
    private static MotDevolucionInventariosDao motivosDao;
	
	public void reset ()
	{
	    this.motivos=new HashMap();
	}
	 
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	*/
	public boolean init(String tipoBD)
	{
		if ( motivosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			motivosDao= myFactory.getMotDevolucionInventariosDao();
			if( motivosDao!= null )
				return true;
		}
			return false;
	}
		
		/**
		 * Constructor
		 *
		 */
	public MotDevolucionInventarios ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Metodo que consulta todos los motivos de devolucion que tiene parametrizado una institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public void consultarMotivosDevolucionInventario(Connection con,int institucion)
	{
	    this.motivos=motivosDao.consultarMotivosDevolucionInventarios(con,institucion);
	}

    /**
     * @return Returns the motivos.
     */
    public HashMap getMotivos()
    {
        return motivos;
    }
    /**
     * @param motivos The motivos to set.
     */
    public void setMotivos(HashMap motivos)
    {
        this.motivos = motivos;
    }

    
    /**
     * @param con
     * @param institucion
     * @param usuario
     * @param map
     */
    public void guardarMotivosDevolucionInventarios(Connection con, HashMap mapa, int institucion, UsuarioBasico usuario)
    {
        boolean enTransaccion=true;
        enTransaccion=UtilidadBD.iniciarTransaccion(con);
        int numReg=Integer.parseInt(mapa.get("numRegistros")==null?"0":mapa.get("numRegistros")+"");
        for(int i=0;i<numReg;i++)
        {
            if(enTransaccion)
            {
                //eliminacion
            	if(UtilidadTexto.getBoolean(mapa.get("eliminado_"+i)+"")&&UtilidadTexto.getBoolean(mapa.get("bd_"+i)+""))
                {
                    //@todo mirar si esta siendo untilizado en otras funcionalidades,por ahora no.
                    this.generarLog(con,mapa,usuario,true, i,institucion);
                    enTransaccion=motivosDao.eliminarRegistro(con,mapa.get("codigo_"+i)+"",institucion);
                }
                else
                {
                    //es modificacion o insercion
                    if(UtilidadTexto.getBoolean(mapa.get("bd_"+i)+""))
                    {
                        if(this.existeModificacion(con,mapa,i,institucion))
                        {
                        	this.generarLog(con,mapa,usuario,false, i,institucion);
                        	enTransaccion=motivosDao.actualizarRegistrio(con,mapa.get("descripcion_"+i)+"",UtilidadTexto.getBoolean(mapa.get("activo_"+i)+""),mapa.get("codigo_"+i)+"",institucion);
                        }
                    }
                    else if(!UtilidadTexto.getBoolean(mapa.get("eliminado_"+i)+""))
                    {
                        enTransaccion=motivosDao.insertarRegistro(con,mapa.get("descripcion_"+i)+"",UtilidadTexto.getBoolean(mapa.get("activo_"+i)+""),mapa.get("codigo_"+i)+"",institucion);
                    }
                }
            }
        }
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
    }

    
    /**
     * Método implementado para consultar la descripcion del motivo
     * de devolucion
     * @param con
     * @param codigo
     * @param institucion
     * @return
     */
    public String consultarDescripcion(Connection con,String codigo,int institucion)
    {
    	String descripcion = "";
    	HashMap mapaConsulta=new HashMap();
        mapaConsulta=motivosDao.consultarMotivoDevolucionInvLLave(con,codigo,institucion);
        
        if(Integer.parseInt(mapaConsulta.get("numRegistros")+"")>0)
        	descripcion = mapaConsulta.get("descripcion_0") + "";
        
        return descripcion;
        
    }
    
    /**
     * @param con
     * @param mapa
     * @param usuario
     * @param institucion
     * @param b
     * @param i
     */
    private void generarLog(Connection con, HashMap mapa, UsuarioBasico usuario, boolean isEliminacion, int pos, int institucion)
    {
        HashMap mapaConsulta=new HashMap();
        mapaConsulta=motivosDao.consultarMotivoDevolucionInvLLave(con,mapa.get("codigo_"+pos)+"",institucion);
        String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			"\n*  Código [" +mapaConsulta.get("codigo_0")+""+"] "+
			 "\n*  Descripción ["+mapaConsulta.get("descripcion_0")+""+"] " +
			 "\n*  Activo ["+mapaConsulta.get("activo_0")+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mapaConsulta.get("codigo_0")+""+"] "+
			 "\n*  Descripción ["+mapaConsulta.get("descripcion_0")+""+"] " +
			 "\n*  Activo ["+mapaConsulta.get("activo_0")+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +mapa.get("codigo_"+pos)+""+"] "+
			 "\n*  Descripción ["+mapa.get("descripcion_"+pos)+""+"] " +
			 "\n*  Activo ["+mapa.get("activo_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logMotivoDevolucionInvCodigo,log,tipoLog,usuario.getLoginUsuario());
    }

    /**
     * @param con
     * @param mapa
     * @param institucion
     * @param i
     * @return
     */
    private boolean existeModificacion(Connection con, HashMap mapa, int pos, int institucion)
    {
        HashMap mapaConsulta=new HashMap();
        mapaConsulta=motivosDao.consultarMotivoDevolucionInvLLave(con,mapa.get("codigo_"+pos)+"",institucion);
        if(((mapa.get("descripcion_"+pos)+"").equals(mapaConsulta.get("descripcion_0")+""))&&((mapa.get("activo_"+pos)+"").equals(mapaConsulta.get("activo_0")+"")))
            return false;
        return true;
    }
}
