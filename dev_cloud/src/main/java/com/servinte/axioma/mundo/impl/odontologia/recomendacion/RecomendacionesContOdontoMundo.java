package com.servinte.axioma.mundo.impl.odontologia.recomendacion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.dao.fabrica.odontologia.recomendacion.RecomendacionesDAOFabrica;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionesContratoDAO;
import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.VerificacionIngresoUnicosException;
import com.servinte.axioma.mundo.helper.odontologia.RecomendacionesHelper;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesContratoMundo;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * CLASE EN DONDE SE LLEVA LA LOGICA DE NEGOCIO DE LAS RECOMENDACIONES CONTRATO 
 * @author Edgar Carvajal 
 *
 */
public class RecomendacionesContOdontoMundo implements IRecomendacionesContratoMundo {

	
	/**
	 *INTERFAZ RECOMENDACION DAO 
	 */
	private IRecomendacionesContratoDAO daoRecomendacion;
	
	
	public RecomendacionesContOdontoMundo()
	{
		daoRecomendacion =  RecomendacionesDAOFabrica.crearRecomendacionesDao();
	}
	
	
	
	@Override
	public void eliminarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion)  throws MundoRuntimeExcepcion
	{
	
		
			
			UtilidadTransaccion.getTransaccion().begin();
			 
			RecomendacionesContOdonto recomTmp= daoRecomendacion.buscarRecomenacionxId(dtoRecomendacion);
			
			
			if( RecomendacionesHelper.existeRecomendacionxRecomendacionSerProg(recomTmp))
			{
				throw new MundoRuntimeExcepcion(IRecomendacionesContratoMundo.errorEliminar);
			}
			else
			{
				daoRecomendacion.eliminarRecomendaciones(recomTmp);
			}
			
			UtilidadTransaccion.getTransaccion().commit();
		
		
	}
	
	

	@Override
	public void guardarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws VerificacionIngresoUnicosException 
	{
		
		UtilidadTransaccion.getTransaccion().begin(); 
		
		if(daoRecomendacion.busquedaRecomendaciones(dtoRecomendacion, Boolean.FALSE).size()>0) // validar existencia
		{
			throw new VerificacionIngresoUnicosException(IRecomendacionesContratoMundo.error); // adicionar el error 
		}
		else
		{
			daoRecomendacion.guardarRecomendaciones(dtoRecomendacion); // guardar RECOMENDACIONES  
		}
	
		
		UtilidadTransaccion.getTransaccion().commit();
	}

	
	
	@Override
	public void modificarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws VerificacionIngresoUnicosException 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		if(daoRecomendacion.busquedaRecomendaciones(dtoRecomendacion, Boolean.TRUE).size()>0) // validar existencia
		{
			throw new VerificacionIngresoUnicosException(IRecomendacionesContratoMundo.error); // adicionar el error
		}
		else
		{
			daoRecomendacion.modificarRecomendaciones(dtoRecomendacion);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	

	@Override
	public List<RecomendacionesContOdonto> obtenerRecomendacionesContrato(RecomendacionesContOdonto dtoRecomendaciones, int institucion) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		List<RecomendacionesContOdonto> lisDaoRecomen= daoRecomendacion.listaRecomendaciones(dtoRecomendaciones, institucion);
		UtilidadTransaccion.getTransaccion().commit();
		
		return lisDaoRecomen;
	}


	

	@Override
	public List<DtoRecomendaciones> listarRecomendaciones( RecomendacionesContOdonto dtoRecomen,ArrayList<Integer> listaCodigos) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		List<DtoRecomendaciones> listTmp= daoRecomendacion.listarRecomendaciones(dtoRecomen, listaCodigos);
		UtilidadTransaccion.getTransaccion().commit();
		
		
		return listTmp;
	}



	@Override
	public ArrayList<DtoRecomendaciones> obtenerRecomendacionesPresuOdonto(
			long codPresoOdonto) {
		return daoRecomendacion.obtenerRecomendacionesPresuOdonto(codPresoOdonto);
	}


}
