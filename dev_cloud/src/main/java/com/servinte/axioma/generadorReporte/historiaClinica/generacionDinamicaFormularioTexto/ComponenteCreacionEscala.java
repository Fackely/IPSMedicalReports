package com.servinte.axioma.generadorReporte.historiaClinica.generacionDinamicaFormularioTexto;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

public class ComponenteCreacionEscala {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(ComponenteCreacionEscala.class);
	
	
	/**
	 * Constructor 
	 */
	public ComponenteCreacionEscala() {
	}
	
	
	/**
	 * @param elemento
	 * @return cadena con las secciones y valores de la escala
	 */
	public String crearSeccionEscala(DtoEscala elemento){
		String res="\n";
		DtoEscala escala = (DtoEscala)elemento;
		ComponenteDibujarCampoEscala componenteDibujarCampoEscala = new ComponenteDibujarCampoEscala();
		
		
		
		
		res += "\n"+escala.getDescripcion().toUpperCase();
		//res = "\n"+escala.getDescripcion();
		
		for(int i=0;i<escala.getSecciones().size();i++)
		{
			DtoSeccionParametrizable seccion = escala.getSecciones().get(i);
			if(seccion.isVisible())
			{
				res+="\n"+seccion.getDescripcion();
				
				if(seccion.numeroCamposVisibles()>0)
				{
					int contadorCampos = ConstantesBD.codigoNuncaValido;
					for(int j=0;j<seccion.getCampos().size();j++)
					{
						DtoCampoParametrizable campo = seccion.getCampos().get(j);
						if(campo.isMostrar())
						{
							res+=componenteDibujarCampoEscala.dibujarEscala(campo, escala);
						}
					}
				}				
			}
		}
		
		res+="\nCLASIFICACIÓN TOTAL:"+String.valueOf(escala.getTotalEscala());
		res+="\nFACTOR PREDICCIÓN: "+escala.getNombreFactorPrediccion();
		
		
		if(!UtilidadTexto.isEmpty(escala.getObservaciones())){
			res+="\nOBSERVACIONES:"+escala.getObservaciones();
		}
		
		return res;
	}

}
