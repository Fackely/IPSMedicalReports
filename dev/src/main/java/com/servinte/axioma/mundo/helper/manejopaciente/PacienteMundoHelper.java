package com.servinte.axioma.mundo.helper.manejopaciente;

import java.util.Iterator;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoAutorizacionConvIngPac;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.orm.AutorizacionConvIngPac;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.DetAutorizacionConvIngPac;

/**
 * 
 * @author axioma
 *
 */
public class PacienteMundoHelper {
	
	
	/**
	 * 
	 */
	private PacienteMundoHelper(){
	}
	
	
	
	/**
	 * 
	 * @param conveniosIngresoPaciente
	 * @return
	 */
	public  static DtoSeccionConvenioPaciente settearConveniosPaciente( ConveniosIngresoPaciente conveniosIngresoPaciente){
		
		/*
		 *1. Crear el Dto seccion convenio paciente  
		 */
		DtoSeccionConvenioPaciente 	dtoSeccionConvenioPaciente= new DtoSeccionConvenioPaciente();
		
		/*
		 *2. Settear la informacion del dtoSeccionConvenioPaciente 
		 */
		if(conveniosIngresoPaciente.getPorDefecto().equals(ConstantesBD.acronimoNoChar)){
			dtoSeccionConvenioPaciente.setEsEliminable(true);
			dtoSeccionConvenioPaciente.setEsModificable(true);
		}
		else{
			dtoSeccionConvenioPaciente.setEsEliminable(false);
			dtoSeccionConvenioPaciente.setEsModificable(false);
		}
		
		
		dtoSeccionConvenioPaciente.setCodigoConveniosIngresoPaciente(conveniosIngresoPaciente.getCodigoPk());//codigoPK
		dtoSeccionConvenioPaciente.setPorDefecto(conveniosIngresoPaciente.getPorDefecto());
		dtoSeccionConvenioPaciente.setActivo(conveniosIngresoPaciente.getActivo()+"");
		dtoSeccionConvenioPaciente.setCodigoContrato(conveniosIngresoPaciente.getContratos().getCodigo()+"");
		dtoSeccionConvenioPaciente.setNumeroContrato(conveniosIngresoPaciente.getContratos().getNumeroContrato());
		dtoSeccionConvenioPaciente.setCodigoConvenio(conveniosIngresoPaciente.getContratos().getConvenios().getCodigo()+"");
		dtoSeccionConvenioPaciente.setDescripcionConvenio(conveniosIngresoPaciente.getContratos().getConvenios().getNombre());
		
		return dtoSeccionConvenioPaciente;
	} 
	
	
	/**
	 * Metodo que settea la autorizacion cong ing pac
	 * @param autorizacionConvIngPac
	 * @return
	 */
	public static DtoSeccionConvenioPaciente settearAutorizacinoCongIngPac(AutorizacionConvIngPac autorizacionConvIngPac , DtoSeccionConvenioPaciente 	dtoSeccionConvenioPaciente )
	{
		
		
		if(autorizacionConvIngPac != null)
		{
			dtoSeccionConvenioPaciente.limpiarAutorizaciones();
			dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().setEstado(autorizacionConvIngPac.getEstado());
			dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().setFechaModifica(autorizacionConvIngPac.getFechaModifica());
			dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().setHoraModifica(autorizacionConvIngPac.getHoraModifica());
			
			for (Iterator iterator2 = autorizacionConvIngPac.getDetAutorizacionConvIngPacs().iterator(); iterator2.hasNext();) 
			{
				DetAutorizacionConvIngPac detAutorizacionConvIngPac = (DetAutorizacionConvIngPac) iterator2.next();
				
				DtoAutorizacionConvIngPac dtoAutorizacionConvIngPac;
				dtoAutorizacionConvIngPac = new DtoAutorizacionConvIngPac();
				dtoAutorizacionConvIngPac.setTipoMedioAuto(detAutorizacionConvIngPac.getTipoMedioAuto());
				dtoAutorizacionConvIngPac.setObservacionesCambio(detAutorizacionConvIngPac.getObservacionesCambio());
				
				dtoSeccionConvenioPaciente.getListaDtoAutorizacionConvIngPac().add(dtoAutorizacionConvIngPac);
			}
		}
		
		
		return dtoSeccionConvenioPaciente;
	}
	
	
	
	
	
	 
	

	
}
