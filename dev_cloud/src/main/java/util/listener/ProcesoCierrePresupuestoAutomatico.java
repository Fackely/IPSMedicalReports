package util.listener;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCierrePresupuestoMundo;


public class ProcesoCierrePresupuestoAutomatico implements Runnable {

	private int institucion;

	/**
	 * @return valor de institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion el institucion para asignar
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	} 
	
	@Override
	public void run() {
		IProcesoCierrePresupuestoMundo procesoCierrePresupuestoMundo = CapitacionFabricaMundo.crearProcesoCierrePresupuestoMundo();
		DtoProcesoPresupuestoCapitado presupuestoCapitado = new DtoProcesoPresupuestoCapitado();
		presupuestoCapitado.setInstitucion(getInstitucion());
		procesoCierrePresupuestoMundo.generarCierrePresupuestoAutomatico(presupuestoCapitado);
	}
}
