//package com.servinte.axioma.orm.delegate.tesoreria;
//
//import java.util.ArrayList;
//
//import com.servinte.axioma.orm.CierreCajaXReciboCaja;
//import com.servinte.axioma.orm.CierreCajaXReciboCajaHome;
//
///**
// * Delegado para el acceso a la BD
// * @author Juan David Ram&iacute;rez
// * @since 06 Jul 2010
// *
// */
//public class CierreCajaXReciboCajaDelegate extends CierreCajaXReciboCajaHome
//{
//
//	
//	/**
//	 * M&etodo que asocia los recibos de caja con el movimiento de cierre pasado
//	 * como par&aacute;metro
//	 * 
//	 * @param listaCierreCajaXReciboCajas
//	 */
////	public void asociarReciboCajaConCierreTurno(ArrayList<CierreCajaXReciboCaja> listaCierreCajaXReciboCajas) {
////		
////		for (CierreCajaXReciboCaja cierreCajaXReciboCaja : listaCierreCajaXReciboCajas) {
////			
////			persist(cierreCajaXReciboCaja);
////		}
////	}
//
//}

package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.HashSet;
import java.util.List;

import com.servinte.axioma.orm.CierreCajaXReciboCaja;
import com.servinte.axioma.orm.CierreCajaXReciboCajaHome;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;


/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto CierreCajaXReciboCaja.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class CierreCajaXReciboCajaDelegate extends CierreCajaXReciboCajaHome{
	
	
	/**
	 * M&eacute;todo que asocia los recibos de caja con un movimiento de cierre espec&iacute;fico
	 * 
	 * @param listaRecibosCajaTurno
	 * @param movimientosCaja 
	 */
	public boolean asociarReciboCajaConCierreTurno(List<RecibosCaja> listaRecibosCajaTurno, MovimientosCaja movimientosCaja) {
		
		boolean update =true;
		
		if (listaRecibosCajaTurno!=null && listaRecibosCajaTurno.size()>0){

			for (RecibosCaja reciboCaja : listaRecibosCajaTurno) {						
				
				HashSet<CierreCajaXReciboCaja> setCierreCajaXReciboCaja = new HashSet<CierreCajaXReciboCaja>();
				
				CierreCajaXReciboCaja cierreCajaXReciboCaja = new CierreCajaXReciboCaja();
				cierreCajaXReciboCaja.setMovimientosCaja(movimientosCaja);
				cierreCajaXReciboCaja.setRecibosCaja(reciboCaja);
				
				reciboCaja.setCierreCajaXReciboCajas(setCierreCajaXReciboCaja);
				movimientosCaja.setCierreCajaXReciboCajas(setCierreCajaXReciboCaja);
				
				super.persist(cierreCajaXReciboCaja);
			}
		}else{
			
			update = false;
		}
		
		return update;
	}

}

