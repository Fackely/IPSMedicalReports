package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;
import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.princetonsa.dto.odontologia.DtoServicioHonorarios;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author axioma
 *
 */
public class ServicioHonorario 
{
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoServicioHonorarios> cargar(	DtoServicioHonorarios dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServicioHonorariosDao().cargar(dtoWhere);
	}
	
	/**
	 * @throws IPSException 
	 * 
	 * 
	 */
    public static int existenDetalles(DtoServicioHonorarios dto) throws IPSException
    {
    	int validator=0;
    	DtoDetalleServicioHonorarios dtoDetalle = new  DtoDetalleServicioHonorarios(); 
    	dtoDetalle.setCodigoHonorario(dto.getCodigo());
    	
    	DtoDetalleAgrupacionHonorarios dtoAgrupacion = new  DtoDetalleAgrupacionHonorarios(); 
    	dtoAgrupacion.setCodigoHonorario(dto.getCodigo());
    	
    	if(DetalleServicioHonorario.cargar(dtoDetalle).size() > 0  ||  DetalleAgrupacionHonorario.cargar(dtoAgrupacion).size() > 0)
    	{
    		validator=1;
    	}
    	return validator;
	}
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoServicioHonorarios dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServicioHonorariosDao().eliminar(dtoWhere);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoServicioHonorarios dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServicioHonorariosDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoServicioHonorarios dtoNuevo,DtoServicioHonorarios dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServicioHonorariosDao().modificar(dtoNuevo, dtoWhere);
	}

}