package com.princetonsa.mundo.facturacion;

import org.apache.log4j.Logger;

import util.InfoPorcentajeValor;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoParametrosBusquedaHonorarios;
import com.princetonsa.mundo.Medico;

/**
 * 
 * @author axioma
 *
 */
public class CalculoHonorariosPooles
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(CalculoHonorariosPooles.class);
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoPorcentajeValor obtenerHonorarioPoolDetalleFacturaNoCx(double codigoDetalleCargo)
	{
		logger.info("\n\n\n**************************BIENVENIDOS A obtenerHonorarioPoolDetalleFacturaNoCx ****************************************");
		
		//1. VERIFICAMOS SI TIENE UNA PROMOCION EL SERVICIO
		InfoPorcentajeValor porcentajeValorHonorarios= obtenerHonorariosPromocionesCargos(codigoDetalleCargo);
		if(porcentajeValorHonorarios==null)
		{
			logger.info("No encontro honorarios por las promociones entonces debemos continuar buscando x el tipo de liquidacion.......");
			DtoParametrosBusquedaHonorarios dtoBusqueda=cargarParametrosBusquedaHonorarios(codigoDetalleCargo); 
			porcentajeValorHonorarios= obtenerHonorarioPoolXTipoLiquidacion(dtoBusqueda);
		}
		return porcentajeValorHonorarios;
	}
	
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoPorcentajeValor obtenerHonorarioPoolDetalleFacturaAsociosCx(int servicio, int pool, int convenio, int esquemaTarifario, int especialidad, int centroAtencion, int codigoMedicoResponde)
	{
		logger.info("\n\n\n**************************BIENVENIDOS A obtenerHonorarioPoolDetalleFacturaAsociosCx ****************************************");
		//En cx no existen promociones x servicio, entonces ese primer punto no se valida
		DtoParametrosBusquedaHonorarios dtoBusqueda= new DtoParametrosBusquedaHonorarios();
		dtoBusqueda.setConvenio(convenio);
		dtoBusqueda.setEspecialidad(especialidad);
		dtoBusqueda.setEsquemaTarifario(esquemaTarifario);
		dtoBusqueda.setPool(pool);
		dtoBusqueda.setServicio(servicio);
		dtoBusqueda.setTipoLiquidacion(Medico.obtenerTipoLiquidacionPool(codigoMedicoResponde));
		dtoBusqueda.setCentroAtencion(centroAtencion);
		dtoBusqueda.setCodigoMedicoResponde(codigoMedicoResponde);
		return  obtenerHonorarioPoolXTipoLiquidacion(dtoBusqueda);
	}
	
	
	
	/**
	 * 
	 * @param servicio
	 * @param tipoLiquidacion
	 * @param pool
	 * @param convenio
	 * @param esquemaTarifario
	 * @param especialidad
	 * @return
	 */
	public static InfoPorcentajeValor obtenerHonorarioPoolXTipoLiquidacion (DtoParametrosBusquedaHonorarios dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalculoHonorariosPoolesDao().obtenerHonorarioPoolXTipoLiquidacion(dto);
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoPorcentajeValor obtenerHonorariosPromocionesCargos(double codigoDetalleCargo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalculoHonorariosPoolesDao().obtenerHonorariosPromocionesCargos(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static DtoParametrosBusquedaHonorarios cargarParametrosBusquedaHonorarios(double codigoDetalleCargo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalculoHonorariosPoolesDao().cargarParametrosBusquedaHonorarios(codigoDetalleCargo);
	}
}
