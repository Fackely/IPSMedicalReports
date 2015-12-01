package com.servinte.axioma.servicio.impl.odontologia.contrato;

import java.sql.Connection;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoFirmasContOtrsiMultiEmpresa;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.odontologia.contrato.ContratoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresaMundo;
import com.servinte.axioma.orm.FirmasContOtrsiempr;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresasServicio;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class FirmaContratoMultiEmpresasServicio implements IFirmaContratoMultiEmpresasServicio {
	
	
	
	private IFirmaContratoMultiEmpresaMundo mundoFirma;
	
	
	public  FirmaContratoMultiEmpresasServicio()
	{
		mundoFirma=ContratoFabricaMundo.crearFirmaMultiEmpresaMundo();		
	}


	@Override
	public FirmasContOtrsiempr buscarxId(Number id) {
		
		return mundoFirma.buscarxId(id);
	}


	@Override
	public List<FirmasContOtrsiempr> cargarFirmasPorInstucion(
			FirmasContOtrsiempr firmas) {
		
		return mundoFirma.cargarFirmasPorInstucion(firmas);
	}


	@Override
	public void eliminar(FirmasContOtrsiempr objeto) {
		
		mundoFirma.eliminar(objeto);
	}


	@Override
	public void insertar(FirmasContOtrsiempr objeto) 
	{
		mundoFirma.insertar(objeto);
	}


	@Override
	public void modificar(FirmasContOtrsiempr objeto) {
		
		mundoFirma.modificar(objeto);
	}


	@Override
	public void modificarFirmasMultiEmpresa(
			List<DtoFirmasContOtrsiMultiEmpresa> listaDTOFirmas,
			String codEmpresaInstitucion, UsuarioBasico usuario, Connection con) {
		
		mundoFirma.modificarFirmasMultiEmpresa(listaDTOFirmas, codEmpresaInstitucion, usuario , con);
	}
	
	
	
	
	
	
	
	
	
	

}
