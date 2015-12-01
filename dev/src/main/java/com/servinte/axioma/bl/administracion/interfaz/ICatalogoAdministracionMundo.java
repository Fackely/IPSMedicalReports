package com.servinte.axioma.bl.administracion.interfaz;

import java.util.List;

import com.servinte.axioma.dto.administracion.DtoSexo;
import com.servinte.axioma.dto.administracion.EspecialidadDto;
import com.servinte.axioma.dto.administracion.FuncionalidadDto;
import com.servinte.axioma.dto.administracion.TipoAfiliadoDto;
import com.servinte.axioma.dto.administracion.TipoIdentificacionDto;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;
import com.servinte.axioma.fwk.exception.IPSException;

public interface ICatalogoAdministracionMundo {

	
	/**
	 * Servicio encargado de obtener el catalogo de Especialidades Válidas
	 * tomando como válido que el códigoPk de la especialidad es mayor a cero
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	List<EspecialidadDto> consultarEspecialidadesValidas() throws IPSException;
	
	/**
	 * Consulta todos los tipos de identificacion para usuarios
	 * 
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 8/10/2012
	 */
	List<TipoIdentificacionDto> consultarTiposIdentificacion() throws IPSException;
	
	/**
	 * Consulta todos los tipos de afiliado para usuarios
	 * 
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 8/10/2012
	 */
	List<TipoAfiliadoDto> consultarTiposAfiliado() throws IPSException;
	
	/**
	 * Servicio encargado de verificar si una entidad subcontratada ya tiene parametrización de entidades
	 * subcontratadas por centros de costo
	 * @param codigoEntidad
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeParametrizacionEntidadSubcontratadaPorCentroCosto(int codigoEntidad) throws IPSException;
	
	
	/**
	 * Consulta todos los sexos
	 * @return
	 * @throws IPSException
	 */
	List<DtoSexo> consultarSexos() throws IPSException;
	
	/**
	 * Metodo encargado de obtener el codigo de los profesionales con las especialidades asociadas
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 * @created 21/12/2012
	 */
	List<ProfesionalEspecialidadesDto> consultarProfesionalesEspecialidades(int codigoInstitucion) throws IPSException;
	
	/**
	 * Consulta todas las funcionalidades asociadas a una funcionalidad padre
	 * 
	 * @param codigoFuncionalidadPadre
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/10/2012
	 */
	List<FuncionalidadDto> consultarFuncionalidades(int codigoFuncionalidadPadre) throws IPSException;
}
