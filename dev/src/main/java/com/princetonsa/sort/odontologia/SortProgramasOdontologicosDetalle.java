package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;



public class SortProgramasOdontologicosDetalle implements Comparator<DtoDetalleProgramas> {
	
	private String patronOrdenar; 
	
	public int compare(DtoDetalleProgramas dtoUno, DtoDetalleProgramas dtoDos) 
	{
		if(this.patronOrdenar.equals("orden")){
			return new Integer(dtoUno.getOrden()).compareTo(new Integer(dtoDos.getOrden()));
		}
		else if(this.patronOrdenar.equals("codigoServicio")){
			return new String(dtoUno.getCodigoCUPS()).compareTo(new String(dtoDos.getCodigoCUPS()));
		}
		else if(this.patronOrdenar.equals("nombreServicio")){
			return new String(dtoUno.getDescripcionServicio()).compareTo(new String(dtoDos.getDescripcionServicio()));
		}
		else if(this.patronOrdenar.equals("activo")){
			return new String(dtoUno.getActivo()).compareTo(new String(dtoDos.getActivo()));
		}
		else if(this.patronOrdenar.equals("orden_descendente")){
			return new Integer(dtoDos.getOrden()).compareTo(new Integer(dtoUno.getOrden()));
		}
		else if(this.patronOrdenar.equals("codigoServicio_descendente")){
			return new String(dtoDos.getCodigoCUPS()).compareTo(new String(dtoUno.getCodigoCUPS()));
		}
		else if(this.patronOrdenar.equals("nombreServicio_descendente")){
			return new String(dtoDos.getDescripcionServicio()).compareTo(new String(dtoUno.getDescripcionServicio()));
		}
		else if(this.patronOrdenar.equals("activo_descendente")){
			return new String(dtoDos.getActivo()).compareTo(new String(dtoUno.getActivo()));
		}
		else
		{
			return new Double(dtoUno.getCodigoPk()).compareTo(new Double(dtoDos.getCodigoPk()));
		}
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	
	
}