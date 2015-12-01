package com.servinte.axioma.generadorReporte.historiaClinica.generacionDinamicaFormularioTexto;

import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;

import org.apache.log4j.Logger;

import util.Utilidades;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;

public class ComponenteDibujarCampoEscala {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(ComponenteDibujarCampoEscala.class);
	
	
	
	/**
	 * Constructor
	 */
	public ComponenteDibujarCampoEscala() {
	}
	
	/**
	 * @param campo
	 * @param escala
	 * @return cadena de texto con la escala y valores
	 */
	public String dibujarEscala( DtoCampoParametrizable campo, DtoEscala escala) {
		String res ="\n";
		res+=campo.getEtiqueta().toUpperCase()+":";
		if(campo.getMinimo()==campo.getMaximo())
		{
			if(!campo.getValor().equals("")&&Utilidades.convertirADouble(campo.getValor())>0)
			{
				res+="X";
			}
		}else{
			res+=campo.getValor();
		}
		
		res+="\n"+campo.getObservaciones();
		
		return res;
		
	}
	

}
