package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author wilson
 *
 */
public interface TecnicaAnestesiaDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> obtenerTecnicaAnestesia(Connection con, int numeroSolicitud, int centroCosto, int institucion, Vector<String> tiposAnestInstCCNoMostrar);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeTecnicaAnestesia(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> cargarTecnicaAnestesiaSolicitud(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarTecnicaAnestesia(Connection con, int numeroSolicitud, int tipoAnestesia, int tipoAnestesiaInstCC, String loginUsuario);
    
    /**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean eliminarTecnicaAnestesia(Connection con, int numeroSolicitud);
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoTecnicaDadoTecnicaCCInst(Connection con, int tipoAnestesiaInstCC);
	
	/**
	 * Método para consultar los tipos aplicables para la hoja de anestesia según la institución
	 * @param con
	 * @param institucion
	 * @param nroConsulta
	 * @return Collection
	 */
	public Collection consultarTipoParametrizado (Connection con, int institucion,int nroConsulta);
	
	/**
     * Metodo para consultar y cargar la información de la sección Técnica de Anestesia
     * @param con
     * @param nroSolicitud
     * @return Collection -> Información de la sección técnica de anestesia
     */
    public Collection cargarTecnicaAnestesiaGeneralRegional(Connection con, int nroSolicitud);
    
    /**
	 * Método para insertar las técnicas de anestesia general y regional parametrizadas
	 * @param con una conexion abierta con una fuente de datos
	 * @param nroSolicitud
	 * @param tecAnestesiaInst
	 * @param valor
	 * @return tecAnestesia
	 */
	public int insertarTecnicaAnestesiaGeneralRegional (Connection con, int nroSolicitud, int tecAnestesiaInst, String valor);

	/**
	 * Consulta las tecnicas de anestesia (tipos de anestesia) registrados en la hoja de anestesia
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return tipos de anestesia de la hoja de anestesia
	 * @throws BDException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	List<TipoAnestesiaDto> consultarTecnicasAnestesiaXSolicitud(Connection con, int numeroSolicitud) throws BDException;
}
