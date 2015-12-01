package com.princetonsa.dao.oracle.parametrizacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.princetonsa.dao.parametrizacion.TecnicaAnestesiaDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseTecnicaAnestesiaDao;
import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author wilson
 *
 */
public class OracleTecnicaAnestesiaDao implements TecnicaAnestesiaDao
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> obtenerTecnicaAnestesia(Connection con, int numeroSolicitud, int centroCosto, int institucion, Vector<String> tiposAnestInstCCNoMostrar)
	{
		return SqlBaseTecnicaAnestesiaDao.obtenerTecnicaAnestesia(con, numeroSolicitud, centroCosto, institucion, tiposAnestInstCCNoMostrar);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeTecnicaAnestesia(Connection con, int numeroSolicitud)
	{
		return SqlBaseTecnicaAnestesiaDao.existeTecnicaAnestesia(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> cargarTecnicaAnestesiaSolicitud(Connection con, int numeroSolicitud)
	{
		return SqlBaseTecnicaAnestesiaDao.cargarTecnicaAnestesiaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarTecnicaAnestesia(Connection con, int numeroSolicitud, int tipoAnestesia, int tipoAnestesiaInstCC, String loginUsuario)
    {
    	return SqlBaseTecnicaAnestesiaDao.insertarTecnicaAnestesia(con, numeroSolicitud, tipoAnestesia, tipoAnestesiaInstCC, loginUsuario);
    }
    
    /**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean eliminarTecnicaAnestesia(Connection con, int numeroSolicitud)
    {
    	return SqlBaseTecnicaAnestesiaDao.eliminarTecnicaAnestesia(con, numeroSolicitud);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoTecnicaDadoTecnicaCCInst(Connection con, int tipoAnestesiaInstCC)
	{
		return SqlBaseTecnicaAnestesiaDao.obtenerCodigoTecnicaDadoTecnicaCCInst(con, tipoAnestesiaInstCC);
	}
	
	/**
	 * Método para consultar los tipos aplicables para la hoja de anestesia según la institución
	 * @param con
	 * @param institucion
	 * @param nroConsulta
	 * @return Collection
	 */
	
	public Collection consultarTipoParametrizado (Connection con, int institucion,int nroConsulta)
	{
		return SqlBaseTecnicaAnestesiaDao.consultarTipoParametrizado(con, institucion, nroConsulta);
	}
	
	/**
     * Metodo para consultar y cargar la información de la sección Técnica de Anestesia
     * @param con
     * @param nroSolicitud
     * @return Collection -> Información de la sección técnica de anestesia
     */
    public Collection cargarTecnicaAnestesiaGeneralRegional(Connection con, int nroSolicitud)
    {
    	return SqlBaseTecnicaAnestesiaDao.cargarTecnicaAnestesiaGeneralRegional(con, nroSolicitud);
    }
    
    /**
	 * Método para insertar las técnicas de anestesia general y regional parametrizadas
	 * @param con una conexion abierta con una fuente de datos
	 * @param nroSolicitud
	 * @param tecAnestesiaInst
	 * @param valor
	 * @return tecAnestesia
	 */
	public int insertarTecnicaAnestesiaGeneralRegional (Connection con, int nroSolicitud, int tecAnestesiaInst, String valor)
	{
		return SqlBaseTecnicaAnestesiaDao.insertarTecnicaAnestesiaGeneralRegional(con, nroSolicitud, tecAnestesiaInst, valor);
	}
	
	@Override
	public List<TipoAnestesiaDto> consultarTecnicasAnestesiaXSolicitud(Connection con, int numeroSolicitud) throws BDException
	{
		return SqlBaseTecnicaAnestesiaDao.consultarTecnicasAnestesiaXSolicitud(con, numeroSolicitud);
	}
}
