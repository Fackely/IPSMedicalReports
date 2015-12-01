package com.servinte.axioma.servicio.impl.odontologia.contrato;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.mundo.fabrica.odontologia.contrato.ContratoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IOtrosSiMundo;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio;


/**
 * @author Cristhian Murillo
 */
public class OtrosSiServicio implements IOtrosSiServicio 
{
	
	
	IOtrosSiMundo otrosSiMundo;
	
	/**
	 * Constructor de la clase
	 */
	public OtrosSiServicio() {
		otrosSiMundo = ContratoFabricaMundo.crearOtrosSiMundo();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio#guardarOtroSi(com.princetonsa.dto.odontologia.DtoOtroSi)
	 */
	@Override
	public OtrosSi guardarOtroSi(DtoOtroSi dtoOtroSi) {
		return otrosSiMundo.guardarOtroSi(dtoOtroSi);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio#guardarOtroSiPresupuesto(com.princetonsa.dto.odontologia.DtoOtroSi)
	 */
	@Override
	public boolean guardarOtroSiPresupuesto(DtoOtroSi dtoOtroSi) {
		return otrosSiMundo.guardarOtroSiPresupuesto(dtoOtroSi);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio#obtenerOtroSiOrdenadosMayorMenor(long)
	 */
	@Override
	public ArrayList<OtrosSi> obtenerOtroSiOrdenadosMayorMenor(long codPresoOdonto) {
		return otrosSiMundo.obtenerOtroSiOrdenadosMayorMenor(codPresoOdonto);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio#obtenerOtrosSiporPresupuesto(long)
	 */
	@Override
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto) {
		return otrosSiMundo.obtenerOtrosSiporPresupuesto(codPresoOdonto);
	}
	
}
