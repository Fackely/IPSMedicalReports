package com.servinte.axioma.generadorReporte.historiaClinica.generacionDinamicaFormularioTexto;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadTexto;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

public class ComponenteDibujarSeccion {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(ComponenteDibujarSeccion.class);
	
	
	
	/**
	 *Constructor 
	 */
	public ComponenteDibujarSeccion() {
	}
	
	/**
	 * @param seccion
	 * @param campo
	 * @return cadena con seccion dibujada
	 */
	public String dibujarCampoSeccion(DtoSeccionParametrizable seccion, DtoCampoParametrizable campo){
		String res="\n";

		if(campo.isMostrar())
		{
			if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoTextArea))
			{
				if(!UtilidadTexto.isEmpty(campo.getEtiqueta())){
					res+="\n"+campo.getEtiqueta()+"  "+campo.getValor();
				}else{
					res+="\n"+campo.getValor().toUpperCase();
				}
				
			}
			else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox))
			{
				if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
				{
					res+="\n"+campo.getEtiqueta();
				}
				for(int i=0;i<campo.getOpciones().size();i++)
				{
					if(UtilidadTexto.getBoolean(campo.getOpciones().get(i).getSeleccionado()))
					{
						String tempo=""+campo.getOpciones().get(i).getValor();
						if(campo.tieneValoresOpcionesCampo())
						{
							tempo=tempo+"    "+campo.getOpciones().get(i).getValoresOpcionRegistrado();
						}
						res+="\n"+tempo;
					}
				}
			}
			else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoRadio))
			{
				if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
				{
					res+="\n"+campo.getEtiqueta();
				}
				String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
				if(!campo.getValoresOpcion().equals(""))
				{
					tempo=tempo+"    "+campo.getValoresOpcion();
				}
				res+="\n"+tempo;
			}
			else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect))
			{
				res+=campo.getEtiqueta();
				if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect) &&campo.getOpciones().size() > 0)
				{
					for(int i=0;i<campo.getOpciones().size();i++)
					{
						if(campo.getOpciones().get(i).getSeleccionado().equals(ConstantesBD.acronimoSi))
						{
							res+="\n"+campo.getOpciones().get(i).getValor();
						}

					}

				}else{
					String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
					if(!campo.getValoresOpcion().equals(""))
					{
						tempo=tempo+"    "+campo.getValoresOpcion();
					}
					res+="\n"+tempo;
				}
			}
			else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoText))
			{
				
				if(!UtilidadTexto.isEmpty(campo.getEtiqueta())){
					res+="\n"+campo.getEtiqueta();
					String tempo=campo.getValor()+(!campo.getNombreUnidad().equals("")&&!campo.getValor().equals("")?" "+campo.getNombreUnidad():"");
					res+=":"+tempo;
				}else{
					String tempo=campo.getValor()+(!campo.getNombreUnidad().equals("")&&!campo.getValor().equals("")?" "+campo.getNombreUnidad():"");
					res+=tempo;
				}
			}
			else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoLabel))
			{
				if(campo.getEtiqueta().equals("/") && campo.getEtiqueta().length()==1){
					res=campo.getEtiqueta();
				}else{
					res+="\n"+campo.getEtiqueta();
				}
			}
		}

		return res;
	}
	
}
