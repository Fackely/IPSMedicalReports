package com.servinte.axioma.generadorReporte.historiaClinica.generacionDinamicaFormularioTexto;

import org.apache.log4j.Logger;

import util.UtilidadTexto;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

public class ServicioGenerarFormularioDinamicoTexto {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(ServicioGenerarFormularioDinamicoTexto.class);
	
	
	
	
	/**
	 * 
	 */
	public ServicioGenerarFormularioDinamicoTexto() {
	}
	
	
	public String generarCampoDinamicoTexto(DtoSeccionFija seccionFija){
		String res ="\n";
		ComponenteCreacionEscala componenteCreacionEscala = new ComponenteCreacionEscala();
		ComponenteCreacionSeccion componenteCreacionSeccion = new ComponenteCreacionSeccion();
		
		
		
		if(seccionFija.getNumElementosVisibles()>0)
		{
			for(DtoElementoParam elemento:seccionFija.getElementos())
			{
				if(elemento.isVisible())
				{
					if(elemento.isComponente())
					{
						DtoComponente componente=(DtoComponente)elemento;
						
						for(int a=0;a<componente.getElementos().size();a++)
						{
							if(componente.getElementos().get(a).isVisible())
							{
								if(componente.getElementos().get(a).isEscala())
								{
									DtoEscala escala = (DtoEscala)componente.getElementos().get(a);
									res +=componenteCreacionEscala.crearSeccionEscala(escala)+"\n";;
								} else if(componente.getElementos().get(a).isSeccion()){

									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)componente.getElementos().get(a);
									if(!UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp()))
									{
										if(!seccion.getCodigo().equals("")&&!seccion.getDescripcion().equals(""))
										{
											res+="\n"+seccion.getDescripcion();
										}
										res+=componenteCreacionSeccion.pintarSecciones(seccion)+"\n";;
									}
									
									
								}
							}
						}
						
						
						
						
					}
					if(elemento.isEscala())
					{
						DtoEscala escala = (DtoEscala)elemento;
						res +=componenteCreacionEscala.crearSeccionEscala(escala)+"\n";
					}else if(elemento.isSeccion())
					{
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
						res+=componenteCreacionSeccion.pintarSecciones(seccion)+"\n";
					}
					
				}
				
			}
		}
		
		if(res.equals("\n\n")){
			res=res.replace("\n\n","");
		}
		return res;
	}
	
	
}
