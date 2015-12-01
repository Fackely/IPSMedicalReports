package com.servinte.axioma.bl.administracion.facade;

import java.util.List;

import com.servinte.axioma.bl.administracion.impl.CatalogoAdministracionMundo;
import com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo;
import com.servinte.axioma.dto.administracion.DtoSexo;
import com.servinte.axioma.dto.administracion.EspecialidadDto;
import com.servinte.axioma.dto.administracion.FuncionalidadDto;
import com.servinte.axioma.dto.administracion.TipoAfiliadoDto;
import com.servinte.axioma.dto.administracion.TipoIdentificacionDto;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase Fachada que provee todos los servicios de lógica de negocio del módulo de
 * Administración a todos los Action de la Capa Web
 * 
 * @author ricruico
 * @version 1.0
 * @created 22-ago-2012 02:23:59 p.m.
 */
public class AdministracionFacade {
	
	/**
	 * Servicio encargado de obtener el catalogo de Especialidades Válidas
	 * tomando como válido que el códigoPk de la especialidad es mayor a cero
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<EspecialidadDto> consultarEspecialidadesValidas() throws IPSException{
		ICatalogoAdministracionMundo catalogoAdministracionMundo= new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.consultarEspecialidadesValidas();
	}


	/**
	 * Consulta todos los tipos de identificacion para usuarios
	 * 
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 8/10/2012
	 */
	public List<TipoIdentificacionDto> consultarTiposIdentificacion() throws IPSException{
		ICatalogoAdministracionMundo catalogoAdministracionMundo= new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.consultarTiposIdentificacion();
	}
	
	/**
	 * Consulta todos los tipos de afiliado para usuarios
	 * 
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 8/10/2012
	 */
	public List<TipoAfiliadoDto> consultarTiposAfiliados() throws IPSException{
		ICatalogoAdministracionMundo catalogoAdministracionMundo= new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.consultarTiposAfiliado();
	}
	
	/**
	 * Servicio encargado de verificar si una entidad subcontratada ya tiene parametrización de entidades
	 * subcontratadas por centros de costo
	 * @param codigoEntidad
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeParametrizacionEntidadSubcontratadaPorCentroCosto(int codigoEntidad) throws IPSException{
		ICatalogoAdministracionMundo catalogoAdministracionMundo= new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.existeParametrizacionEntidadSubcontratadaPorCentroCosto(codigoEntidad);
	}
	
	/**
	 * Consulta todos los sexos
	 * @return
	 * @throws IPSException
	 */
	public List<DtoSexo> consultarSexos() throws IPSException{
		ICatalogoAdministracionMundo catalogoAdministracionMundo= new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.consultarSexos();
	}
	
	/**
	 * Metodo encargado de obtener el codigo de los profesionales con las especialidades asociadas
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 * @created 21/12/2012
	 */
	public List<ProfesionalEspecialidadesDto> consultarProfesionalesEspecialidades(int codigoInstitucion) throws IPSException {
		ICatalogoAdministracionMundo catalogoAdministracionMundo = new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.consultarProfesionalesEspecialidades(codigoInstitucion);
		
	}
	
	/**
	 * Consulta todas las funcionalidades asociadas a una funcionalidad padre
	 * 
	 * @param codigoFuncionalidadPadre
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/10/2012
	 */
	public List<FuncionalidadDto> consultarFuncionalidades(int codigoFuncionalidadPadre) throws IPSException{
		ICatalogoAdministracionMundo catalogoAdministracionMundo= new CatalogoAdministracionMundo();
		return catalogoAdministracionMundo.consultarFuncionalidades(codigoFuncionalidadPadre);
	}
}
