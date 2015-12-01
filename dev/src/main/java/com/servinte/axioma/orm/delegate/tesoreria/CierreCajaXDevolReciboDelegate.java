package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.HashSet;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.orm.CierreCajaXDevolRecibo;
import com.servinte.axioma.orm.CierreCajaXDevolReciboHome;
import com.servinte.axioma.orm.DevolRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto CierreCajaXDevolReciboCaja.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class CierreCajaXDevolReciboDelegate extends CierreCajaXDevolReciboHome{

	/**
	 
	 * M&eacute;todo que asocia las devoluciones de recibos de caja con un movimiento de cierre espec&iacute;fico
	 * 
	 * @param listaRecibosCajaTurno
	 * @param movimientosCaja
	 * @return
	 */
	public boolean asociarDevolucionesCierreCaja(List<DtoReciboDevolucion> listaDevolRecibosCajaTurno,MovimientosCaja movimientosCaja) {
	
		boolean update =true;
		
		if (listaDevolRecibosCajaTurno!=null && listaDevolRecibosCajaTurno.size()>0){

			for (DtoReciboDevolucion dtoDevolucion : listaDevolRecibosCajaTurno) {						
				
				Log4JManager.info("/////////////////////////////// entre a actualizar la devolucion //////////////////////// " + dtoDevolucion.getCodigoDevolucion());
				
				HashSet<CierreCajaXDevolRecibo> setCierreCajaXDevolRecibo = new HashSet<CierreCajaXDevolRecibo>();
				DevolRecibosCaja devolRecibosCaja = new DevolRecibosCaja();
				devolRecibosCaja.setCodigo(dtoDevolucion.getCodigoDevolucion());
				
				CierreCajaXDevolRecibo cierreCajaXDevolRecibo = new CierreCajaXDevolRecibo();
				cierreCajaXDevolRecibo.setMovimientosCaja(movimientosCaja);
				cierreCajaXDevolRecibo.setDevolRecibosCaja(devolRecibosCaja);
				
				devolRecibosCaja.setCierreCajaXDevolRecibos(setCierreCajaXDevolRecibo);
				movimientosCaja.setCierreCajaXDevolRecibos(setCierreCajaXDevolRecibo);
				
				super.persist(cierreCajaXDevolRecibo);
			}
		}else{
			
			update = false;
		}
		
		return update;
	}
}
