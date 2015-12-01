package com.servinte.axioma.servicio.impl.facturacion;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoMundo;
import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoDetalleMontoServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoServicio implements IHistoDetalleMontoServicio {
	
	IHistoDetalleMontoMundo mundo = FacturacionFabricaMundo.crearHistoDetalleMontoMundo();
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico del detalle
	 * de un monto de cobro
	 * 
	 * @param HistoDetalleMonto
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetalleMonto histoDetalleMonto){
		return mundo.guardarDetalleMontoCobro(histoDetalleMonto);
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los historicos
	 * de los detalles de un monto de cobro
	 * 
	 * @param DetalleMonto, ArrayList<DTOBusquedaMontoAgrupacionArticulo>listaAgrupacionArticulo
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarHistoricosMontoCobro(String accion, int detalleCodigo,
			UsuarioBasico usuarioSesion){
		return mundo.guardarHistoricosMontoCobro(accion,detalleCodigo,usuarioSesion);
	}

}
