package com.servinte.axioma.mundo.impl.odontologia.contrato;

import java.util.ArrayList;
import java.util.Iterator;

import util.UtilidadFecha;

import com.princetonsa.dao.tesoreria.PresuFirmasContratoHibernateDAO;
import com.princetonsa.dao.tesoreria.PresupuestoOdontologicoHibernateDAO;
import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;
import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.odontologia.contrato.ContratoOdontologicoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IContratoOdontologicoDAO;
import com.servinte.axioma.mundo.helper.odontologia.ContratoOdontologicoHelper;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IContratoOdontologicoMundo;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.PresuFirmasContrato;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.recomendacion.RecomendacionSERVICIOFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionesContOdontoServicio;



/**
 * 
 * @author axioma
 *
 */
public class ContratoOdontologicoMundo implements IContratoOdontologicoMundo {
	
	
	private IContratoOdontologicoDAO contratoDAO;
	
	
	
	/**
	 * 
	 */
	public  ContratoOdontologicoMundo()
	{
		contratoDAO = ContratoOdontologicoFabricaDAO.crearContratoDAO();
	}
	
	
	

	@Override
	public ContratoOdontologico consultarAvanzadaContratoOdon(	ContratoOdontologico dtoContrato) 
	{
		ContratoOdontologico dtoTmp =null;
	
		try{
		
		UtilidadTransaccion.getTransaccion().begin();
		dtoTmp=contratoDAO.consultarAvanzadaContratoOdon(dtoContrato);
		UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) 
		{
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		return dtoTmp;
	}




	@Override
	public ContratoOdontologico buscarxId(Number id) {
		
		return contratoDAO.buscarxId(id);
		
	}




	@Override
	public void eliminar(ContratoOdontologico objeto) 
	{
		try{
			UtilidadTransaccion.getTransaccion().begin();
			contratoDAO.eliminar(objeto);
			UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		
	}




	@Override
	public void insertar(ContratoOdontologico objeto) 
	{
		try{
		UtilidadTransaccion.getTransaccion().begin();
		contratoDAO.insertar(objeto);
		UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) 
		{
			UtilidadTransaccion.getTransaccion().rollback();
		}
	
	}


	
	/**
	 *	INSERTAR MODIFICAR ELIMINAR CONTRATOS  
	 * @author Edgar Carvajal Ruiz
	 * @param contrato
	 * @param listaContratos
	 */
	public void insertarModificarEliminar(ContratoOdontologico contrato, ArrayList<DtoFirmasContOtrosInstOdont> listaFirmas, UsuarioBasico usuario)
	{
		
		try
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			/*
			 * INSERTAR O MODIFICAR CONTRATO
			 */
			
			contrato.setUsuarios(new Usuarios());
			contrato.getUsuarios().setLogin(usuario.getLoginUsuario());
			contrato.setInstituciones(new Instituciones());
			contrato.getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
			contrato.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
			contrato.setHoraModifica(UtilidadFecha.getHoraActual());
	
			
			ContratoOdontologico entidadContrato=  ContratoOdontologicoHelper.transformarContratoOdontologico(contrato, listaFirmas, usuario);
			
			contratoDAO.insertar(entidadContrato);
	
			/*
			 * FIN TRANACCION
			 */
			UtilidadTransaccion.getTransaccion().commit();
			
		}
		
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		
	}
	
	
	
	

	@Override
	public void modificar(ContratoOdontologico objeto) {
	
		contratoDAO.modificar(objeto);
	}
	
	
	
	
	/**
	 * 	CONSULTA FIRMA CONTRATO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoFirmasContOtrosInstOdont> consultaDtoFirmasContrato(ContratoOdontologico contratoOdontologico)
	{
	
		ArrayList<DtoFirmasContOtrosInstOdont> listaDtoFirmasContrato = new ArrayList<DtoFirmasContOtrosInstOdont>();
		
		contratoOdontologico=contratoDAO.consultarAvanzadaContratoOdon(contratoOdontologico);
		
		if(contratoOdontologico.getFirmasContratoOtrosiInsts()!=null && contratoOdontologico.getFirmasContratoOtrosiInsts().size()>0)
		{
		
			Iterator it = contratoOdontologico.getFirmasContratoOtrosiInsts().iterator();
			
			
			while(it.hasNext())
			{
				FirmasContratoOtrosiInst firmas = (FirmasContratoOtrosiInst) it.next();
				DtoFirmasContOtrosInstOdont dtoFirmas = new DtoFirmasContOtrosInstOdont();
				
				
				dtoFirmas.setCodigoPk(firmas.getCodigoPk());
				dtoFirmas.setActivo(Boolean.TRUE);
				dtoFirmas.setAdjuntoFirma(firmas.getAdjuntoFirma());
				dtoFirmas.setLabelDebajoFirma(firmas.getLabelDebajoFirma());
				dtoFirmas.setNumero(firmas.getNumero());
				dtoFirmas.setCodigoContrato(firmas.getContratoOdontologico().getCodigoPk());
				
				listaDtoFirmasContrato.add(dtoFirmas);
				
			}
		}
		
		return listaDtoFirmasContrato;
	}


	
	
	@Override
	public DtoFormatoImpresionContratoOdontologico generarImpresionContratoOdontologico
				(DtoFormatoImpresionContratoOdontologico dtoFormatoImpresionContratoOdontologico)
	{
		PresupuestoOdontologicoHibernateDAO presupuestoOdontologicoHibernateDAO = new PresupuestoOdontologicoHibernateDAO();
		DtoFormatoImpresionContratoOdontologico dtoImpresionContrato = new DtoFormatoImpresionContratoOdontologico();
		dtoImpresionContrato = presupuestoOdontologicoHibernateDAO.obtenerContratoOdontologico(dtoFormatoImpresionContratoOdontologico);
		dtoImpresionContrato.setValorTotalPresupuesto(dtoFormatoImpresionContratoOdontologico.getValorTotalPresupuesto());
		dtoImpresionContrato.setListaAnexosImpresos(dtoFormatoImpresionContratoOdontologico.getListaAnexosImpresos());
		
		//GeneradorReporteCotnratoPresupuestoOdontologico reporteContratoOdonto = new GeneradorReporteCotnratoPresupuestoOdontologico();
		//reporteContratoOdonto.generarReporte(dtoImpresionContrato);
		
		PresuFirmasContratoHibernateDAO presuFirmasContratoHibernateDAO = new PresuFirmasContratoHibernateDAO();
		ArrayList<PresuFirmasContrato> listaPResuFirmas = new ArrayList<PresuFirmasContrato>();
		
		listaPResuFirmas = presuFirmasContratoHibernateDAO.obtenerFirmasPorPresuOdonto(dtoFormatoImpresionContratoOdontologico.getCodigoPkPresupuesto());
		dtoImpresionContrato.setListaPresuFirmasContrato(listaPResuFirmas);
		
		return dtoImpresionContrato;
		
	}
	
	
	
	@Override
	public DtoFormatoImpresionContratoOdontologico generarImpresionRecomendacionesOdontologico
				(DtoFormatoImpresionContratoOdontologico dtoFormatoImpresionRecomendacionesOdontologico)
	{
		PresupuestoOdontologicoHibernateDAO presupuestoOdontologicoHibernateDAO = new PresupuestoOdontologicoHibernateDAO();
		DtoFormatoImpresionContratoOdontologico dtoImpresionRecomendaciones = new DtoFormatoImpresionContratoOdontologico();
		
		dtoImpresionRecomendaciones = presupuestoOdontologicoHibernateDAO.obtenerContratoOdontologico(dtoFormatoImpresionRecomendacionesOdontologico);
		dtoImpresionRecomendaciones.setValorTotalPresupuesto(dtoFormatoImpresionRecomendacionesOdontologico.getValorTotalPresupuesto());
		dtoImpresionRecomendaciones.setListaAnexosImpresos(dtoFormatoImpresionRecomendacionesOdontologico.getListaAnexosImpresos());
		
		
		IRecomendacionesContOdontoServicio recomendacionesContOdontoServicio = RecomendacionSERVICIOFabrica.crearRecomendacionesCont();
		ArrayList<DtoRecomendaciones> listaRecomendaciones = new ArrayList<DtoRecomendaciones>();
		
		listaRecomendaciones = recomendacionesContOdontoServicio.obtenerRecomendacionesPresuOdonto(
				dtoFormatoImpresionRecomendacionesOdontologico.getCodigoPkPresupuesto());
		
		dtoImpresionRecomendaciones.setListaRecomendaciones(listaRecomendaciones);
		
		return dtoImpresionRecomendaciones;
	}
	

}
