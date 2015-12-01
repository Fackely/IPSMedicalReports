package com.servinte.axioma.mundo.interfaz.facturacion;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.HistoDetalleMonto;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public interface IHistoDetalleMontoMundo {
	
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
	public boolean guardarDetalleMontoCobro(HistoDetalleMonto histoDetalleMonto);
	
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
			UsuarioBasico usuarioSesion);

}
