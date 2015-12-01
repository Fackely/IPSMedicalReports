package com.servinte.axioma.mundo.fabrica.ordenes;

import com.servinte.axioma.mundo.impl.ordenes.OrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.impl.ordenes.SolicitudesMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.ISolicitudesMundo;

/**
 * 
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del m�dulo �rdenes
 * 
 * @author Fabi�n Becerra
 * @since 30 Marzo 2011
 */
public abstract class OrdenesFabricaMundo {

		
		/**
		 * M�todo constructor de la clase
		 */
		private OrdenesFabricaMundo(){ }	
		
		
		
		/**
		 * M�todo que retorna una instancia de {@link OrdenesAmbulatoriasMundo}
		 * 
		 * @return IOrdenesAmbulatoriasMundo
		 * @author Camilo G�mez
		 */
		public static IOrdenesAmbulatoriasMundo crearOrdenesAmbulatoriasMundo(){
			return new OrdenesAmbulatoriasMundo();
		}
		
	/**
	 * M�todo que retorna una de instancia de {@link SolicitudesMundo}
	 * 
	 * @author Fabi�n Becerra
	 * @return {@link ISolicitudesMundo}
	 */
	public static ISolicitudesMundo crearSolicitudesMundo(){
		return new SolicitudesMundo();				
	}
}
