package util.odontologia;

import java.io.Serializable;

import util.ConstantesBD;


import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;


/**
 * Edgar Carvajal 
 * 
 * @author axioma
 *
 *
 * Info para Cargar la informacion del ingreso del paciente con su respectivo 
 * Plan de tratamiento
 */
public class InfoIngresoPlanTratamiento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3109421782524443576L;
	
	
	/**
	 * Dto Para Cargar Plan tratamiento
	 */
	private DtoPlanTratamientoOdo dtoPlanTratamiento;
	
	/**
	 * Dto para Cargar Ingresos
	 */
	private DtoOtrosIngresosPaciente dtoIngresoPaciente;

	/**
	 * Atributo para Cargar la Via de Ingreso
	 */
	private int viaIngreso;
	
	
	
	/**
	 * 
	 */
	public InfoIngresoPlanTratamiento(){
		this.dtoIngresoPaciente = new DtoOtrosIngresosPaciente();
		this.dtoPlanTratamiento = new DtoPlanTratamientoOdo();
		this.viaIngreso=ConstantesBD.codigoNuncaValido;
	}
	
	
	
	
	
	
	
	public DtoPlanTratamientoOdo getDtoPlanTratamiento() {
		return dtoPlanTratamiento;
	}

	public DtoOtrosIngresosPaciente getDtoIngresoPaciente() {
		return dtoIngresoPaciente;
	}

	public void setDtoPlanTratamiento(DtoPlanTratamientoOdo dtoPlanTratamiento) {
		this.dtoPlanTratamiento = dtoPlanTratamiento;
	}

	public void setDtoIngresoPaciente(DtoOtrosIngresosPaciente dtoIngresoPaciente) {
		this.dtoIngresoPaciente = dtoIngresoPaciente;
	}







	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}







	public int getViaIngreso() {
		return viaIngreso;
	}
	
	
	

}
