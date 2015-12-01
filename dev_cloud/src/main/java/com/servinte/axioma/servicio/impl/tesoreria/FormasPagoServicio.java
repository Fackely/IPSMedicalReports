package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.TiposDetalleFormaPago;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link IFormasPagoServicio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class FormasPagoServicio  implements IFormasPagoServicio{

	private IFormasPagoMundo formasPagoMundo;
	
	
	public FormasPagoServicio() {
		formasPagoMundo =  TesoreriaFabricaMundo.crearFormasPagoMundo();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio#findById(int)
	 */
	@Override
	public FormasPago findById(int consecutivo) {
		
		return formasPagoMundo.findById(consecutivo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio#obtenerFormasPagos()
	 */
	@Override
	public List<FormasPago> obtenerFormasPagos() {
		
		return formasPagoMundo.obtenerFormasPagos();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio#obtenerFormasPagos(com.princetonsa.dto.tesoreria.DtoFormaPago)
	 */
	@Override
	public List<DtoFormaPago> obtenerFormasPagos(DtoFormaPago formaPagoFiltros) {
		
		return formasPagoMundo.obtenerFormasPagos(formaPagoFiltros);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio#obtenerTiposDetalleFormaPago()
	 */
	@Override
	public List<TiposDetalleFormaPago> obtenerTiposDetalleFormaPago() {
		
		return formasPagoMundo.obtenerTiposDetalleFormaPago();
	}
}
