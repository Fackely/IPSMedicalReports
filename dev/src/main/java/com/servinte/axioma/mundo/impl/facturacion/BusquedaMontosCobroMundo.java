/**
 * 
 */
package com.servinte.axioma.mundo.impl.facturacion;

import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.princetonsa.dto.facturacion.DtoFiltroBusquedaMontosCobro;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.delegate.facturacion.MontosCobroDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConvenioDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author axioma
 *
 */
public class BusquedaMontosCobroMundo implements IBusquedaMontosCobroMundo {

	/**
	 * 
	 */
	@Override
	public DtoBusquedaMontosCobro consultarMontosCobro(
			DtoFiltroBusquedaMontosCobro dtoFiltro) 
	{
		DtoBusquedaMontosCobro dtoResultado=new DtoBusquedaMontosCobro();
		try{
			UtilidadTransaccion.getTransaccion().begin();
			MontosCobroDelegate dao=new MontosCobroDelegate();
			ConvenioDelegate convenioDao=new ConvenioDelegate();
			Convenios convenio=convenioDao.findById(dtoFiltro.getConvenio());
			if(convenio!=null)
			{
				if(UtilidadTexto.getBoolean(convenio.getManejaMontos()))
				{
					dtoResultado=dao.obtenerMontosCobroBusqueda(dtoFiltro);
				}
				else
				{
					dtoResultado.setResultado(new ResultadoBoolean(true,"Convenio no maneja montos."));
				}
			}
			else
			{
				dtoResultado.setResultado(new ResultadoBoolean(true,"Seleccione Convenio."));
			}
			UtilidadTransaccion.getTransaccion().commit();
		}catch (Exception e) {
			Log4JManager.error("Error busqueda montos:  " + e);
			UtilidadTransaccion.getTransaccion().rollback();
		}
		return dtoResultado;
	}
}
