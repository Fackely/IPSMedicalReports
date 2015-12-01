package com.servinte.axioma.vista.odontologia.contrato;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;


import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.dto.odontologia.DtoFirmasContOtrsiMultiEmpresa;
import com.princetonsa.mundo.Usuario;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.FirmasContOtrsiempr;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;
import com.servinte.axioma.orm.Usuarios;

/**
 * 
 * @author Edgar Carvajal
 * 
 */
public class ParametrizacionInstHelper

{

	private static final int tamanioMaximo=4;
	
	
	/**
	 * Metodo que toma la entidad contrato y arma un array de dto FirmasContOtro
	 * @author Edgar Carvajal Ruiz
	 * @param contrato
	 * @return
	 */
	public static  ArrayList<DtoFirmasContOtrosInstOdont> cargarDtoFirmas(ContratoOdontologico contrato) {
		
		ArrayList<DtoFirmasContOtrosInstOdont> listaContrato = new ArrayList<DtoFirmasContOtrosInstOdont>();
		

		if (contrato != null) 
		{
			if (contrato.getFirmasContratoOtrosiInsts().size() > 0) {

				Iterator it = contrato.getFirmasContratoOtrosiInsts().iterator();

				while (it.hasNext()) {
					DtoFirmasContOtrosInstOdont dtoFirmas = new DtoFirmasContOtrosInstOdont();
					
					FirmasContratoOtrosiInst entidadFirmas = (FirmasContratoOtrosiInst) it.next();
					
					dtoFirmas.setCodigoPk(entidadFirmas.getCodigoPk());
					dtoFirmas.setActivo(Boolean.TRUE);
					
					dtoFirmas.setAdjuntoFirma(entidadFirmas.getAdjuntoFirma());
					dtoFirmas.setCodigoContrato(entidadFirmas.getContratoOdontologico().getCodigoPk());
					dtoFirmas.setLabelDebajoFirma(entidadFirmas.getLabelDebajoFirma());
					dtoFirmas.setNumero(entidadFirmas.getNumero());
					dtoFirmas.setFirmaDigital(entidadFirmas.getFirmaDigital());
					
					
					listaContrato.add(dtoFirmas);
				}

			}

		}

		return listaContrato;
	}
	
	
	
	
	/**
	 * METODO QUE LLENA CON AL ESTRUCTURA 
	 * @author Edgar Carvajal Ruiz
	 * @param contrato
	 * @param listaFirmas
	 * @return
	 */
	public static ContratoOdontologico llenarEstructuraContratoOdontologico(ContratoOdontologico contrato , ArrayList<DtoFirmasContOtrosInstOdont> listaFirmas , Usuario usuario)
	{
		
		if(contrato!=null)
		{
			for(DtoFirmasContOtrosInstOdont dto:listaFirmas)
			{
				
				FirmasContratoOtrosiInst firmaInsticion = new FirmasContratoOtrosiInst();
				
				firmaInsticion.setCodigoPk(dto.getCodigoPk());
				firmaInsticion.setAdjuntoFirma(dto.getAdjuntoFirma());
				firmaInsticion.setLabelDebajoFirma(dto.getLabelDebajoFirma());
				firmaInsticion.setFirmaDigital(dto.getFirmaDigital());
				firmaInsticion.setNumero(dto.getNumero());
				firmaInsticion.setHoraModifica(UtilidadFecha.getHoraActual());
				firmaInsticion.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
				firmaInsticion.setUsuarios(new Usuarios());
				firmaInsticion.getUsuarios().setLogin(usuario.getLoginUsuario());
				
				
				contrato.getFirmasContratoOtrosiInsts().add(firmaInsticion);
			}
		}
		
		return contrato;
		
	}
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param lista
	 */
	public static boolean validacionTamanioFirmas(ArrayList<DtoFirmasContOtrosInstOdont> lista)
	{
		
		boolean retorno=Boolean.FALSE;
		
		int contador=0;
		
		for(DtoFirmasContOtrosInstOdont dtoFirmas: lista)
		{
			if(dtoFirmas.isActivo())
			{
				contador++;
			}
		}
		
		if(contador==tamanioMaximo)
		{
			retorno=Boolean.TRUE;
		}
		
		return retorno;
	}
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param listaFirmasEntidad
	 * @return
	 */
	public static ArrayList<DtoFirmasContOtrsiMultiEmpresa> cargarDtoFirmaMultiEmpresa(List<FirmasContOtrsiempr>  listaFirmasEntidad )
	{
		ArrayList<DtoFirmasContOtrsiMultiEmpresa> listaDtoFirmaMultiEmpresa = new ArrayList<DtoFirmasContOtrsiMultiEmpresa>();
		
		for(FirmasContOtrsiempr entidad:   listaFirmasEntidad)
		{
			
			DtoFirmasContOtrsiMultiEmpresa dto = new DtoFirmasContOtrsiMultiEmpresa();
		
			dto.setCodigoPk(entidad.getCodigoPk());
			dto.setAdjuntoFirma(entidad.getAdjuntoFirma());
			dto.setFirmaDigital(entidad.getFirmaDigital());
			dto.setLabelDebajoFirma(entidad.getLabelDebajoFirma());
			dto.setNumero(entidad.getNumero());
			
			
			
			listaDtoFirmaMultiEmpresa.add(dto);
			
		}
		
		
		
		
		
		return listaDtoFirmaMultiEmpresa;
	}
	
	
	
	/**
	 * METODO QUE VALIDA SI EXISTE POR LO MENOS UNA FIRMA 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static  boolean validarExisteUnaFirma(ArrayList<DtoFirmasContOtrosInstOdont> listaFirmas )

	{
		 
	   
        	Boolean existeFirma=Boolean.FALSE;
        	
        	
        	for(DtoFirmasContOtrosInstOdont dto: listaFirmas )
        	{
        		if( dto.isActivo() )
        		{
        			existeFirma=Boolean.TRUE;
        			break;
        		}
		   	}
        	
        	
    		
		return existeFirma;
	}
	
	
	
	/**
	 * METODO QUE RECIBE UN ARRAY LIST DTO FIRMA CONTRATO MULTIEMPRESA
	 * Y DESACTIVA TODOS LOS DTO 
	 * RETORNA LA LISTA CON LA INFORMACION RESPECTIVA
	 * 
	 * @param listaDtoContrato
	 * @return
	 */
	public static  ArrayList<DtoFirmasContOtrsiMultiEmpresa> eliminarListaFirmasContratoEmpresaInstitucion(ArrayList<DtoFirmasContOtrsiMultiEmpresa> listaDtoContrato ){

		
		for(DtoFirmasContOtrsiMultiEmpresa dto: listaDtoContrato)
		{
			dto.setActivo(Boolean.FALSE); 
		}
		
		return listaDtoContrato;
		
	}
	
	

	/**
	 *  METODO QUE RECIBE UN ARRAY LIST DTO FIRMA CONTRATO INSTITUCION
	 * Y DESACTIVA TODOS LOS DTO 
	 * RETORNA LA LISTA CON LA INFORMACION RESPECTIVA
	 * @param listaContrato
	 * @return
	 */
	public static ArrayList<DtoFirmasContOtrosInstOdont> eliminarFirmasContratoInstitucion(ArrayList<DtoFirmasContOtrosInstOdont> listaContrato )
	{
		
		for(DtoFirmasContOtrosInstOdont dto: listaContrato )
		{
			dto.setActivo(Boolean.FALSE);
		}
	
		
		return listaContrato;
	}
	
	
	
	
	
	
	

}