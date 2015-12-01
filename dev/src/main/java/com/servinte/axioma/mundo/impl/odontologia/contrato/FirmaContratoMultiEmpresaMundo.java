package com.servinte.axioma.mundo.impl.odontologia.contrato;

import java.sql.Connection;
import java.util.List;

import org.axioma.util.fechas.UtilidadesFecha;
import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.odontologia.DtoFirmasContOtrsiMultiEmpresa;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.odontologia.contrato.ContratoOdontologicoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresaDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresaMundo;
import com.servinte.axioma.orm.CodificacionImpArticulo;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.FirmasContOtrsiempr;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class FirmaContratoMultiEmpresaMundo implements IFirmaContratoMultiEmpresaMundo {

	
	
	
	/**
	 * 
	 */
	private IFirmaContratoMultiEmpresaDAO daoFirma;
	

	/**
	 * 
	 */
	public FirmaContratoMultiEmpresaMundo()
	{
		daoFirma= ContratoOdontologicoFabricaDAO.crearFirmasMultiEmpresa();
	}
	

	@Override
	public FirmasContOtrsiempr buscarxId(Number id) 
	{
		return daoFirma.buscarxId(id);
	}


	@Override
	public List<FirmasContOtrsiempr> cargarFirmasPorInstucion(
			FirmasContOtrsiempr firmas) {

		UtilidadTransaccion.getTransaccion().begin();
		List<FirmasContOtrsiempr> listTmp = daoFirma.cargarFirmasPorInstucion(firmas);
		UtilidadTransaccion.getTransaccion().commit();
		return listTmp;
	}


	@Override
	public void eliminar(FirmasContOtrsiempr objeto) 
	{
		daoFirma.eliminar(objeto);
		UtilidadTransaccion.getTransaccion().begin();
		
	}


	@Override
	public void insertar(FirmasContOtrsiempr objeto) {
		UtilidadTransaccion.getTransaccion().begin();
		daoFirma.insertar(objeto);
		UtilidadTransaccion.getTransaccion().commit();
		
	}


	
	@Override
	public void modificar(FirmasContOtrsiempr objeto) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		daoFirma.modificar(objeto);
		UtilidadTransaccion.getTransaccion().commit();
	}

	
	
	
	
	
	@Override
	public void modificarFirmasMultiEmpresa(List<DtoFirmasContOtrsiMultiEmpresa> listaDTOFirmas, 
											String codEmpresaInstitucion, 
											UsuarioBasico usuario, Connection con)
	{
		
		
		try {
		
			UtilidadTransaccion.getTransaccion().begin();
			
			
			
			for(DtoFirmasContOtrsiMultiEmpresa dto: listaDTOFirmas)
			{
				
				if( dto.isActivo() && dto.getCodigoPk()>0)
				{
					FirmasContOtrsiempr entidadFirma = new FirmasContOtrsiempr();
					entidadFirma.setCodigoPk(dto.getCodigoPk());
					entidadFirma.setAdjuntoFirma(dto.getAdjuntoFirma());
					entidadFirma.setEmpresasInstitucion(new EmpresasInstitucion());
					entidadFirma.getEmpresasInstitucion().setCodigo(Utilidades.convertirAEntero(codEmpresaInstitucion) );
					entidadFirma.setFirmaDigital(dto.getFirmaDigital());
					entidadFirma.setHoraModifica(UtilidadFecha.getHoraActual());
					entidadFirma.setFechaModificar(UtilidadFecha.getFechaActualTipoBD());
					entidadFirma.setNumero(dto.getNumero());
					entidadFirma.setLabelDebajoFirma(dto.getLabelDebajoFirma());
					entidadFirma.setUsuarios(new Usuarios());
					entidadFirma.getUsuarios().setLogin(usuario.getLoginUsuario());
					
					daoFirma.modificar(entidadFirma);
				
					continue;
				}
				
				
				if( !dto.isActivo() && dto.getCodigoPk()>0)
				{
					FirmasContOtrsiempr entidadFirma = new FirmasContOtrsiempr();
					entidadFirma.setCodigoPk(dto.getCodigoPk());
					entidadFirma.setAdjuntoFirma(dto.getAdjuntoFirma());
					entidadFirma.setEmpresasInstitucion(new EmpresasInstitucion());
					entidadFirma.getEmpresasInstitucion().setCodigo(Utilidades.convertirAEntero(codEmpresaInstitucion) );
					entidadFirma.setFirmaDigital(dto.getFirmaDigital());
					entidadFirma.setHoraModifica(UtilidadFecha.getHoraActual());
					entidadFirma.setFechaModificar(UtilidadFecha.getFechaActualTipoBD());
					entidadFirma.setNumero(dto.getNumero());
					entidadFirma.setLabelDebajoFirma(dto.getLabelDebajoFirma());
					entidadFirma.setUsuarios(new Usuarios());
					entidadFirma.getUsuarios().setLogin(usuario.getLoginUsuario());
					
					daoFirma.eliminar(entidadFirma);
				
					continue;
				}
				
				
				if( dto.isActivo() && dto.getCodigoPk()<=0)
				{
					
					FirmasContOtrsiempr entidadFirma = new FirmasContOtrsiempr();
					entidadFirma.setAdjuntoFirma(dto.getAdjuntoFirma());
					entidadFirma.setEmpresasInstitucion(new EmpresasInstitucion());
					entidadFirma.getEmpresasInstitucion().setCodigo(Utilidades.convertirAEntero(codEmpresaInstitucion) );
					entidadFirma.setFirmaDigital(dto.getFirmaDigital());
					entidadFirma.setHoraModifica(UtilidadFecha.getHoraActual());
					entidadFirma.setFechaModificar(UtilidadFecha.getFechaActualTipoBD());
					entidadFirma.setNumero(dto.getNumero());
					entidadFirma.setLabelDebajoFirma(dto.getLabelDebajoFirma());
					entidadFirma.setUsuarios(new Usuarios());
					entidadFirma.getUsuarios().setLogin(usuario.getLoginUsuario());
					
					daoFirma.insertar(entidadFirma);
					
				}
				
				
				
			}
		
			
			UtilidadTransaccion.getTransaccion().commit();
			
		}
		catch(Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			
			Log4JManager.info(e.getMessage());
			Log4JManager.error(e);
		}
		
	
	}
	




	
	
	
}
