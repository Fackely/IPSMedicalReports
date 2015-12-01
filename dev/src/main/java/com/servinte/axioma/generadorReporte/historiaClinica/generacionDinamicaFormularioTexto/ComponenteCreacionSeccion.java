package com.servinte.axioma.generadorReporte.historiaClinica.generacionDinamicaFormularioTexto;

import org.apache.log4j.Logger;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

public class ComponenteCreacionSeccion {
	
	

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(ComponenteCreacionSeccion.class);
	
	
	/**
	 * 
	 */
	public ComponenteCreacionSeccion() {
	}
	
	public String pintarSecciones(DtoSeccionParametrizable seccion){
		String res="\n";
		Integer campos=0;
		ComponenteDibujarSeccion componenteDibujarSeccion = new ComponenteDibujarSeccion();
		
		for(DtoCampoParametrizable campo:seccion.getCampos())
		{
			//if(seccion.getColumnasSeccion()==campos||seccion.getColumnasSeccion()<(campos+campo.getColumnasOcupadas())||(campo.isUnicoXFila()))
			if(campos==0)
			{
				res+=seccion.getDescripcion().toUpperCase();
			}
			
			campos++;
			res+=componenteDibujarSeccion.dibujarCampoSeccion(seccion, campo);
		}
		
		res = res.replace("/\n","/");
		for(int i=0;i<seccion.getSecciones().size();i++)
		{
			DtoSeccionParametrizable subseccion2 = seccion.getSecciones().get(i);
			if(subseccion2.isVisible())
			{
				if(!subseccion2.getDescripcion().equals(""))
				{
					res+="\n"+subseccion2.getDescripcion();
				}
				for(DtoCampoParametrizable campo:subseccion2.getCampos())
				{
					res+="\n"+componenteDibujarSeccion.dibujarCampoSeccion(subseccion2,campo);
				}
				
				for(int j=0;j<subseccion2.getSecciones().size();j++)
				{
					DtoSeccionParametrizable subseccion3 = subseccion2.getSecciones().get(j);
					if(subseccion3.isVisible())
					{
						if(!subseccion3.getDescripcion().equals(""))
						{
							res+="\n"+subseccion3.getDescripcion();
						}
						for(DtoCampoParametrizable campo:subseccion3.getCampos())
						{
							res+="\n"+componenteDibujarSeccion.dibujarCampoSeccion(subseccion3,campo);
						}
					}
				}
			}
		
		}
		
		return res;
		
	}
	

}
