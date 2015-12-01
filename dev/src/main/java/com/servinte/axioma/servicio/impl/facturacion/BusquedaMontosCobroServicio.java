/**
 * 
 */
package com.servinte.axioma.servicio.impl.facturacion;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.princetonsa.dto.facturacion.DtoFiltroBusquedaMontosCobro;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.impl.facturacion.IBusquedaMontosCobroMundo;
import com.servinte.axioma.servicio.interfaz.facturacion.IBusquedaMontosCobroServicio;

/**
 * @author axioma
 *
 */
public class BusquedaMontosCobroServicio implements IBusquedaMontosCobroServicio 
{
	
	IBusquedaMontosCobroMundo busquedaMontosCobroMundo;

	public BusquedaMontosCobroServicio()
	{
		busquedaMontosCobroMundo=FacturacionFabricaMundo.crearBusquedaMontosCobroMundo();
	}
	
	@Override
	public DtoBusquedaMontosCobro consultarMontosCobro(
			DtoFiltroBusquedaMontosCobro dtoFiltro) {
		return busquedaMontosCobroMundo.consultarMontosCobro(dtoFiltro);
	}
	
	@Override
	public  DtoBusquedaMontosCobro consultarMontosCobro(
			int clasificacionSocioEconomica,int convenio,String fechaAperturaCuenta,int naturalezaPaciente,String tipoAfiliado,String tipoPaciente, int viaIngreso) 
	{
		DtoBusquedaMontosCobro resultado=new DtoBusquedaMontosCobro();
		try
		{
			DtoFiltroBusquedaMontosCobro dtoFiltro=new DtoFiltroBusquedaMontosCobro();
			dtoFiltro.setClasificacionSocioEconomica(clasificacionSocioEconomica);
			dtoFiltro.setConvenio(convenio);
			dtoFiltro.setFechaAperturaCuenta(fechaAperturaCuenta);
			dtoFiltro.setNaturalezaPaciente(naturalezaPaciente);
			dtoFiltro.setTipoAfiliado(tipoAfiliado);
			dtoFiltro.setTipoPaciente(tipoPaciente);
			dtoFiltro.setViaIngreso(viaIngreso);
			resultado= busquedaMontosCobroMundo.consultarMontosCobro(dtoFiltro);
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
		return resultado;
	}

}
