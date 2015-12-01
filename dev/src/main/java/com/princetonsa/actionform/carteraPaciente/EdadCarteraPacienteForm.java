package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.InfoDatosInt;

import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoReporteEdadCarteraPaciente;

public class EdadCarteraPacienteForm extends ValidatorForm
{
	public String estado;
	
	public DtoReporteEdadCarteraPaciente dtoEdad; 
	
	public HashMap mapaCentrosAtencion;
	
	public void reset()
	{	
		dtoEdad = new DtoReporteEdadCarteraPaciente();
		mapaCentrosAtencion = new HashMap();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public HashMap getMapaCentrosAtencion() {
		return mapaCentrosAtencion;
	}

	public void setMapaCentrosAtencion(HashMap mapaCentrosAtencion) {
		this.mapaCentrosAtencion = mapaCentrosAtencion;
	}

	public DtoReporteEdadCarteraPaciente getDtoEdad() {
		return dtoEdad;
	}

	public void setDtoEdad(DtoReporteEdadCarteraPaciente dtoEdad) {
		this.dtoEdad = dtoEdad;
	}
}