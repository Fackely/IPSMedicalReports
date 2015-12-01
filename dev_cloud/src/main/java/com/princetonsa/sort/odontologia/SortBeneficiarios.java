package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */
public class SortBeneficiarios implements Comparator<DtoBeneficiarioPaciente> 
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(DtoBeneficiarioPaciente one, DtoBeneficiarioPaciente two) 
	{
		String nombreOne = one.getPrimerNombre()+" "+one.getSegundoNombre()+" "+one.getPrimerApellido()+" "+one.getSegundoApellido();
		String nombretwo = two.getPrimerNombre()+" "+two.getSegundoNombre()+" "+two.getPrimerApellido()+" "+two.getSegundoApellido();
		String tipoOcupacionOne = one.getTipoOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado)?"Empleado":"Independiente";
		String tipoOcupacionTwo = two.getTipoOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado)?"Empleado":"Independiente";
		
		if(this.patronOrdenar.equals("identificacion")){
			return new String(one.getNumeroId()+" "+one.getTipoId()).compareTo(new String(two.getNumeroId()+" "+two.getTipoId()));
		}else if(this.patronOrdenar.equals("nombre")){
			return new String(nombreOne).compareTo(new String(nombretwo));
		}else if(this.patronOrdenar.equals("parentezco")){
			return new String(one.getParentezco()).compareTo(new String(two.getParentezco()));
		}else if(this.patronOrdenar.equals("ocupacion")){
			return new String(one.getOcupacion()).compareTo(new String(two.getOcupacion()));
		}else if(this.patronOrdenar.equals("tipoocupacion")){
			return new String(tipoOcupacionOne).compareTo(new String(tipoOcupacionTwo));
		}else if(this.patronOrdenar.equals("estudios")){
			return new Integer(one.getEstudio()).compareTo(new Integer(two.getEstudio()));
		}
		
		
		if(this.patronOrdenar.equals("identificacion_descendente")){
			return new String(two.getNumeroId()+" "+two.getTipoId()).compareTo(new String(one.getNumeroId()+" "+one.getTipoId()));
		}else if(this.patronOrdenar.equals("nombre_descendente")){
			return new String(nombretwo).compareTo(new String(nombreOne));
		}else if(this.patronOrdenar.equals("parentezco_descendente")){
			return new String(two.getParentezco()).compareTo(new String(one.getParentezco()));
		}else if(this.patronOrdenar.equals("ocupacion_descendente")){
			return new String(two.getOcupacion()).compareTo(new String(one.getOcupacion()));
		}else if(this.patronOrdenar.equals("tipoocupacion_descendente")){
			return new String(tipoOcupacionTwo).compareTo(new String(tipoOcupacionOne));
		}else if(this.patronOrdenar.equals("estudios_descendente")){
			return new Integer(two.getEstudio()).compareTo(new Integer(one.getEstudio()));
		}
		
		
		else{
			return new String(one.getNumeroId()+" "+one.getTipoId()).compareTo(new String(two.getNumeroId()+" "+two.getTipoId()));
		}
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
}
