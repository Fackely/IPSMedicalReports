package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.Errores;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.ContratoCargueDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseContratoCargueDao;

public class ContratosCargue
{
	private ArrayList contratosCargue;
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private static Logger	logger	= Logger.getLogger(ContratosCargue.class);

	/**
	 * 
	 */
	private static ContratoCargueDao contratoCargueDao;

	/**
	 * 
	 * @return
	 */
	private static ContratoCargueDao getContratoCargueDao()
    {
        if(contratoCargueDao==null)
        {
            String tipoBD = System.getProperty("TIPOBD" );
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            contratoCargueDao=myFactory.getContratoCargueDao();
        }
        return contratoCargueDao;
    }

	/**
	 * 
	 */
	public ContratosCargue()
	{
		this.contratosCargue=new ArrayList();
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public ContratoCargue getContratoCargue(int i)
	{
		return (ContratoCargue)this.contratosCargue.get(i);
	}
	
	/**
	 * 
	 * @param contratoCargue
	 */
	public void addContratoCargue(ContratoCargue contratoCargue)
	{
		this.contratosCargue.add(contratoCargue);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getContratosCargueSize()
	{
		return this.contratosCargue.size();
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @throws Errores
	 */
	public void guardar(Connection con, int codigoInstitucion) throws Errores
	{
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

        try
        {
	        inicioTransaccion = myFactory.beginTransaction(con);
	        if(inicioTransaccion)
	        {
	        	for(int i=0; i<contratosCargue.size(); i++)
	        	{
	        		this.getContratoCargue(i).guardar(con, codigoInstitucion);
	        	}
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
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 * @throws Errores
	 */
	public static HashMap buscarContratosCargue(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws Errores
	{
        try
        {
    		return ContratosCargue.getContratoCargueDao().buscarContratosCargue(con, fechaInicial, fechaFinal, codigoConvenio);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws Errores
	 */
	public static HashMap consultarCarguesGrupoEtareoContrato(Connection con, int codigoContratoCargue) throws Errores
	{
        try
        {
    		return ContratosCargue.getContratoCargueDao().consultarCarguesGrupoEtareoContrato(con, codigoContratoCargue);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
	}

	/**
	 * Busca los logs de subir paciente según los parametros especificados
	 * @param con
	 * @param convenio que correspondan al convenio especificado
	 * @param fechaInicial cuya fecha de cargue sea mayor o igual a esta fecha inicial
	 * @param fechaFinal cuya fecha de cargue sea menor o igual a esta fecha final
	 * @param usuario que corresponda al usuario especificado
	 * @return
	 * @throws SQLException
	 * @author Alejandro Diaz Betancourt
	 */
	public static Collection buscarLogsSubirPacientes(Connection con, String convenio, String fechaInicial, String fechaFinal, String usuario) throws Errores
	{
        try
        {
    		return ContratosCargue.getContratoCargueDao().buscarLogsSubirPacientes(con, convenio, fechaInicial, fechaFinal, usuario);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 * @throws Errores
	 */
	public static boolean existenContratosCargueConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws Errores
	{
        try
        {
    		return ContratosCargue.getContratoCargueDao().existenContratosCargueConvenioPeriodo(con, fechaInicial, fechaFinal, codigoConvenio);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 * @throws Errores
	 */
	public static boolean existenCuentasCobroConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws Errores
	{
        try
        {
    		return ContratosCargue.getContratoCargueDao().existenCuentasCobroConvenioPeriodo(con, fechaInicial, fechaFinal, codigoConvenio);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoId
	 * @param numeroId
	 * @param nombre
	 * @param apellido
	 * @param numeroFicha
	 * @return
	 * @throws Errores
	 */
	public static HashMap consultarUsuariosCargados(Connection con, int codigoContrato, String fechaInicial, String fechaFinal, String tipoId, String numeroId, String nombre, String apellido, String numeroFicha) throws Errores 
	{
        try
        {
    		return ContratosCargue.getContratoCargueDao().consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, tipoId, numeroId, nombre, apellido,numeroFicha);
        }
        catch(SQLException se)
        {
        	logger.warn(se);
			throw new Errores(se);
        }
	}

	/**
	 * 
	 * @param con
	 * @param contratosCargueEliminados
	 */
	public void eliminarContratos(Connection con, HashMap contratosCargueEliminados) 
	{
		 ContratosCargue.getContratoCargueDao().eliminarContratos(con, contratosCargueEliminados);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param activo
	 * @return
	 */
	public static boolean inactivarUsuarios(Connection con, String consecutivo, String activo) 
	{
		return ContratosCargue.getContratoCargueDao().inactivarUsuarios(con, consecutivo, activo);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param activo
	 * @return
	 */
	public static boolean inactivarUsuariosActivos(Connection con, String consecutivo, String activo) 
	{
		return ContratosCargue.getContratoCargueDao().inactivarUsuariosActivos(con, consecutivo, activo);
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public static boolean eliminarUsuarios(Connection con, String consecutivo) 
	{
		return ContratosCargue.getContratoCargueDao().eliminarUsuarios(con, consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap consultarDatosEliminado(Connection con, String consecutivo) 
	{
		return ContratosCargue.getContratoCargueDao().consultarDatosEliminado(con, consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarLogEliminacion(Connection con, HashMap vo) 
	{
		return ContratosCargue.getContratoCargueDao().insertarLogEliminacion(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public static HashMap consultarDaotosInactivar(Connection con, String consecutivo) 
	{
		return ContratosCargue.getContratoCargueDao().consultarDaotosInactivar(con, consecutivo);
	}
	
	
}