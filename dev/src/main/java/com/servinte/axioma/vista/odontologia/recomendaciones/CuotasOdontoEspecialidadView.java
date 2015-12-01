package com.servinte.axioma.vista.odontologia.recomendaciones;

import java.util.Iterator;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.odontologia.DtoCuotasOdontologicasEspecialidad;
import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEspClon;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class CuotasOdontoEspecialidadView 
{

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param cuotaEntidad
	 * @return
	 */
	public static  DtoCuotasOdontologicasEspecialidad armarDtoClon(CuotasOdontEspecialidad cuotaEntidad)
	{
		
		DtoCuotasOdontologicasEspecialidad dtoCuota = new DtoCuotasOdontologicasEspecialidad();
		
		 if(cuotaEntidad!=null)
		 {
			 
			 dtoCuota.setCodigoPk(cuotaEntidad.getCodigoPk());
			 dtoCuota.setCodigoEspecialidad(cuotaEntidad.getEspecialidades().getCodigo());
			 dtoCuota.setNombreEspecialidad(cuotaEntidad.getEspecialidades().getNombre());
			 dtoCuota.setCodigoInstitucion(cuotaEntidad.getInstituciones().getCodigo());
			 dtoCuota.setNombreInstitucion(cuotaEntidad.getInstituciones().getRazonSocial());
			 dtoCuota.setHoraModifica(cuotaEntidad.getHoraModifica());
			 dtoCuota.setLoginUsuario(cuotaEntidad.getUsuarios().getLogin());
			 dtoCuota.setTipoValor(cuotaEntidad.getTipoValor());
			 dtoCuota.setFechaModifica(cuotaEntidad.getFechaModifica());
			 
			 
			 if( cuotaEntidad.getDetalleCuotasOdontoEsps().size()>0)
			 {
				
				 Iterator it= cuotaEntidad.getDetalleCuotasOdontoEsps().iterator();
				 
				 while(it.hasNext())
				 {
					 DetalleCuotasOdontoEsp  entidadDetalle= (DetalleCuotasOdontoEsp) it.next();
					 
					 DtoDetalleCuotasOdontoEspClon dtoDetalle= new DtoDetalleCuotasOdontoEspClon();
					 
					 Log4JManager.info(entidadDetalle.getCodigoPk());
					 Log4JManager.info(entidadDetalle.getNroCuotas());
					 Log4JManager.info(entidadDetalle.getPorcentaje());
					 Log4JManager.info(entidadDetalle.getTipoCuota());
					 Log4JManager.info(entidadDetalle.getValor());
					 
					 dtoDetalle.setCodigoPk(entidadDetalle.getCodigoPk());
					 dtoDetalle.setNroCuotas(entidadDetalle.getNroCuotas());
					 
					 if(entidadDetalle.getPorcentaje()!=null)
					 {
						 dtoDetalle.setPorcentaje( entidadDetalle.getPorcentaje().doubleValue());
					 }
					 if(entidadDetalle.getValor()!=null)
					 {
						 dtoDetalle.setValor(entidadDetalle.getValor().doubleValue());
					 }
					 
					 dtoCuota.getListaDetallesEspecialidad().add(dtoDetalle);
				 }
				
			 }
			 
		 }
		
		
		return dtoCuota;
		
	}
}
