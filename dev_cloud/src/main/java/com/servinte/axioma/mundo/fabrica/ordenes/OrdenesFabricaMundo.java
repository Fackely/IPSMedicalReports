package com.servinte.axioma.mundo.fabrica.ordenes;

import com.servinte.axioma.mundo.impl.ordenes.OrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.impl.ordenes.SolicitudesMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.ISolicitudesMundo;

/**
 * 
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo Órdenes
 * 
 * @author Fabián Becerra
 * @since 30 Marzo 2011
 */
public abstract class OrdenesFabricaMundo {

		
		/**
		 * Método constructor de la clase
		 */
		private OrdenesFabricaMundo(){ }	
		
		
		
		/**
		 * Método que retorna una instancia de {@link OrdenesAmbulatoriasMundo}
		 * 
		 * @return IOrdenesAmbulatoriasMundo
		 * @author Camilo Gómez
		 */
		public static IOrdenesAmbulatoriasMundo crearOrdenesAmbulatoriasMundo(){
			return new OrdenesAmbulatoriasMundo();
		}
		
	/**
	 * Método que retorna una de instancia de {@link SolicitudesMundo}
	 * 
	 * @author Fabián Becerra
	 * @return {@link ISolicitudesMundo}
	 */
	public static ISolicitudesMundo crearSolicitudesMundo(){
		return new SolicitudesMundo();				
	}
}
