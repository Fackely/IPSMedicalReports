package com.servinte.axioma.mundo.helper.odontologia;

import java.util.ArrayList;
import java.util.Iterator;

import util.UtilidadFecha;

import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;
import com.servinte.axioma.orm.Usuarios;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class ContratoOdontologicoHelper 
{
	
	
	
	/**
	 * Metodo que Retorna  un Contrato Odontologico con la lista de Firmas Odontologicas
	 * Recibe un ArrayList de DtofirmaContOtrosInstOdont y un Objeto UsuarioBasico.
	 * Toma el array de Dto y construye el set de firmas de contrato.
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ContratoOdontologico transformarContratoOdontologico(ContratoOdontologico contrato, ArrayList<DtoFirmasContOtrosInstOdont> listaFirmas, UsuarioBasico usuario) 
	{
	
		if(contrato==null)
		{
			return null;
		}
		
		
		
		Iterator it= contrato.getFirmasContratoOtrosiInsts().iterator();
			
			
		
		
		/*
		 * 1. Recorrido de la Entidad FirmaContratoIntitucion
		 */
		while(it.hasNext())
		{
			
			FirmasContratoOtrosiInst  firma=	(FirmasContratoOtrosiInst)it.next();
		
				
				/*
				 * 2. Recorrido de dto Firmas Contrato Institucion 
				 */
				for(DtoFirmasContOtrosInstOdont dto:listaFirmas)
				{
				
					
				
					
					
					/*
					 * 3. Eliminar Las Firmas 
					 */
		
					if(!dto.isActivo()  && dto.getCodigoPk()>0)
					{
						if(dto.getCodigoPk()==firma.getCodigoPk())
						{
							it.remove();
							continue;
						}
					}
					
					/*
					 * 4.Modificar las Firmas
					 */
					if(dto.isActivo() && dto.getCodigoPk()>0)
					{
						if(dto.getCodigoPk()==firma.getCodigoPk())
						{
							firma.setCodigoPk(dto.getCodigoPk());
							firma.setContratoOdontologico(contrato);
							firma.setAdjuntoFirma(dto.getAdjuntoFirma());
							firma.setLabelDebajoFirma(dto.getLabelDebajoFirma());
							firma.setFirmaDigital(dto.getFirmaDigital());
							firma.setNumero(dto.getNumero());
							firma.setHoraModifica(UtilidadFecha.getHoraActual());
							firma.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
							firma.setUsuarios(new Usuarios());
							firma.getUsuarios().setLogin(usuario.getLoginUsuario());
							
							
							continue;
						}
					}
					
					
					
					
				}//fin for
		}//fin while
		
			
			
			/*
			 * 5. Insertar  la nuevas firmas
			 */
			for(DtoFirmasContOtrosInstOdont dto:listaFirmas)
			{
				
				if(dto.isActivo() && dto.getCodigoPk()<0 )
				{

					FirmasContratoOtrosiInst firmaInstitucion = new FirmasContratoOtrosiInst();
					
				
					firmaInstitucion.setContratoOdontologico(contrato);
					firmaInstitucion.setAdjuntoFirma(dto.getAdjuntoFirma());
					firmaInstitucion.setLabelDebajoFirma(dto.getLabelDebajoFirma());
					firmaInstitucion.setFirmaDigital(dto.getFirmaDigital());
					firmaInstitucion.setNumero(dto.getNumero());
					firmaInstitucion.setHoraModifica(UtilidadFecha.getHoraActual());
					firmaInstitucion.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
					firmaInstitucion.setUsuarios(new Usuarios());
					firmaInstitucion.getUsuarios().setLogin(usuario.getLoginUsuario());
					
					
					contrato.getFirmasContratoOtrosiInsts().add(firmaInstitucion);
					
					
					
					continue;
					
				}
			
			
			}//fin for
			
			
			
			return contrato;
			

	}

}
