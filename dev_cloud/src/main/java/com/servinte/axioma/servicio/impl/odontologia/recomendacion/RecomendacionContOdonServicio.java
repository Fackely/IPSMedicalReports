package com.servinte.axioma.servicio.impl.odontologia.recomendacion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.VerificacionIngresoUnicosException;
import com.servinte.axioma.mundo.fabrica.odontologia.recomendacion.RecomendacionesFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesContratoMundo;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.servicio.excepcion.ServicioException;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionesContOdontoServicio;


/**
 * CLASE QUE BRINDA LO SERVICIO DE RECOMENDACIONES 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionContOdonServicio implements IRecomendacionesContOdontoServicio 
{

	/**
	 * INTERFAZ MUNDO
	 */
	private IRecomendacionesContratoMundo recomendacionesI;
	
	
	
	/**
	 * CONSTRUTOR 
	 */
	public RecomendacionContOdonServicio()
	{
		recomendacionesI=RecomendacionesFabrica.crearRecomentadacionMundo();
		
	}
	
	
	
	@Override
	public void eliminarRecomendaciones( RecomendacionesContOdonto dtoRecomendacion) throws ServicioException
	{
		try{
		recomendacionesI.eliminarRecomendaciones(dtoRecomendacion);
		}
		catch (MundoRuntimeExcepcion e) {
			throw e;
		}
	}

	@Override
	public void guardarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws ServicioException
	{
		try
		{
			recomendacionesI.guardarRecomendaciones(dtoRecomendacion);
		}
		catch (VerificacionIngresoUnicosException e) 
		{
			throw e;
		}
	}

	
	
	@Override
	public void modificarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion)  throws ServicioException
	{
		try{
			recomendacionesI.modificarRecomendaciones(dtoRecomendacion);
		}
		catch (VerificacionIngresoUnicosException e) 
		{
			throw e;
		}
	}

	
	
	@Override
	public List<RecomendacionesContOdonto> obtenerRecomendacionesContrato(RecomendacionesContOdonto dtoRecomendaciones , int institucion) 
	{
		return recomendacionesI.obtenerRecomendacionesContrato(dtoRecomendaciones, institucion);
	}



	
	@Override
	public List<DtoRecomendaciones> listarRecomendaciones(	RecomendacionesContOdonto dtoRecomen,ArrayList<Integer> listaCodigos) 
	{
		return recomendacionesI.listarRecomendaciones(dtoRecomen, listaCodigos);
	}



	@Override
	public ArrayList<DtoRecomendaciones> obtenerRecomendacionesPresuOdonto(
			long codPresoOdonto) {
		return recomendacionesI.obtenerRecomendacionesPresuOdonto(codPresoOdonto);
	}
	

}
