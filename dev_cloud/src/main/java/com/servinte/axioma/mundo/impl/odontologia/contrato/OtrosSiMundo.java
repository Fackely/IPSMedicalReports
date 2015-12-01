package com.servinte.axioma.mundo.impl.odontologia.contrato;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.dao.fabrica.odontologia.contrato.ContratoOdontologicoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IOtrosSiDAO;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IOtrosSiMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo;
import com.servinte.axioma.orm.ExclusionesPresupuesto;
import com.servinte.axioma.orm.ExclusionesPresupuestoHome;
import com.servinte.axioma.orm.InclusionesPresupuesto;
import com.servinte.axioma.orm.InclusionesPresupuestoHome;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiInclusiones;


/**
 * @author Cristhian Murillo
 */
public class OtrosSiMundo implements IOtrosSiMundo {
	
	
	/**
	 * Atributos de la clase
	 */
	private IOtrosSiDAO otrosSiDAO;
	private ICentroAtencionMundo centroAtencionMundo;
	private IUsuariosMundo usuariosMundo;
	private IPresupuestoOdontologicoMundo presupuestoOdontologicoMundo;
	
	
	
	/**
	 *  Constructor
	 */
	public  OtrosSiMundo()
	{
		usuariosMundo					= AdministracionFabricaMundo.crearUsuariosMundo();
		otrosSiDAO 						= ContratoOdontologicoFabricaDAO.crearOtrosSiDao();
		centroAtencionMundo 			= AdministracionFabricaMundo.crearCentroAtencionMundo();
		presupuestoOdontologicoMundo	= PresupuestoFabricaMundo.crearPresupuestoOdontologicoMundo();
	}



	@Override
	public ArrayList<OtrosSi> obtenerOtroSiOrdenadosMayorMenor(long codPresoOdonto) {
		return otrosSiDAO.obtenerOtroSiOrdenadosMayorMenor(codPresoOdonto);
	}



	@Override
	public boolean guardarOtroSiPresupuesto(DtoOtroSi dtoOtroSi) 
	{
		boolean exito = false;
		
		if(dtoOtroSi.getPresupuesto() > 0)
		{
			try 
			{
				OtrosSi otroSi;
				otroSi = new OtrosSi();
				long consecutivoAsignar = 0;
				
				otroSi.setCentroAtencion(centroAtencionMundo.findById(dtoOtroSi.getCentroAtencion()));
				otroSi.setUsuarios(usuariosMundo.buscarPorLogin(dtoOtroSi.getUsuario()));
				otroSi.setPresupuestoOdontologico(presupuestoOdontologicoMundo.buscarPresupuestoId(dtoOtroSi.getPresupuesto()));
				otroSi.setFechaGeneracion(UtilidadFecha.getFechaActualTipoBD());
				otroSi.setHoraGeneracion(UtilidadFecha.getHoraActual());
				
				ArrayList<OtrosSi> listaOtrosSi = obtenerOtroSiOrdenadosMayorMenor(dtoOtroSi.getPresupuesto());
				consecutivoAsignar = listaOtrosSi.size() + 1;
				
				otroSi.setConsecutivo(consecutivoAsignar);
				
				otrosSiDAO.persist(otroSi);
				
				
				
				for (DtoCheckBox inclusion_exclusion : dtoOtroSi.getListaInclusionesExclusiones()) 
				{
					//long codigoInclusionExclusion = inclusion_exclusion.getCodigo().l
					Double d=Double.parseDouble(inclusion_exclusion.getCodigo().trim());
					long codigoInclusionExclusion = d.longValue();
					
					if(inclusion_exclusion.getNombre().equals(ConstantesIntegridadDominio.acronimoExcluido))
					{
						OtrosSiExclusiones exclusiones; exclusiones = new OtrosSiExclusiones();
						exclusiones.setOtrosSi(otroSi);
						// Se crea la exclusion y se le asigna la llave primaria que corresponde
						ExclusionesPresupuesto exclusionesPresupuesto;	exclusionesPresupuesto = new ExclusionesPresupuesto();
						exclusionesPresupuesto = new ExclusionesPresupuestoHome().findById(codigoInclusionExclusion);
						exclusiones.setExclusionesPresupuesto(exclusionesPresupuesto); 
						persistOtrosSiExclusiones(exclusiones);
					}
					else if(inclusion_exclusion.getNombre().equals(ConstantesIntegridadDominio.acronimoInclusion))
					{
						OtrosSiInclusiones inclusiones;	inclusiones = new OtrosSiInclusiones();
						inclusiones.setOtrosSi(otroSi);
						// Se crea la inclusion y se le asigna la llave primaria que corresponde
						InclusionesPresupuesto inclusionesPresupuesto; inclusionesPresupuesto = new InclusionesPresupuesto();
						inclusionesPresupuesto = new InclusionesPresupuestoHome().findById(codigoInclusionExclusion);
						inclusiones.setInclusionesPresupuesto(inclusionesPresupuesto);
						persistOtrosSiInclusiones(inclusiones);
					}
				}
				
				exito = true;
				
			} catch (Exception e) {
				exito = false;
				Log4JManager.error("error guardando el otro si", e);
			}
		}
		
		return exito;
	}


	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.contrato.IOtrosSiMundo#guardarOtroSi(com.princetonsa.dto.odontologia.DtoOtroSi)
	 */
	@Override
	public OtrosSi guardarOtroSi (DtoOtroSi dtoOtroSi)
	{
		if(dtoOtroSi.getPresupuesto() > 0)
		{
			try 
			{
				OtrosSi otroSi;
				otroSi = new OtrosSi();
				long consecutivoAsignar = 0;
				
				otroSi.setCentroAtencion(centroAtencionMundo.findById(dtoOtroSi.getCentroAtencion()));
				otroSi.setUsuarios(usuariosMundo.buscarPorLogin(dtoOtroSi.getUsuario()));
				otroSi.setPresupuestoOdontologico(presupuestoOdontologicoMundo.buscarPresupuestoId(dtoOtroSi.getPresupuesto()));
				otroSi.setFechaGeneracion(UtilidadFecha.getFechaActualTipoBD());
				otroSi.setHoraGeneracion(UtilidadFecha.getHoraActual());
				
				ArrayList<OtrosSi> listaOtrosSi = obtenerOtroSiOrdenadosMayorMenor(dtoOtroSi.getPresupuesto());
				consecutivoAsignar = listaOtrosSi.size() + 1;
				
				otroSi.setConsecutivo(consecutivoAsignar);
				
//				OtrosSiHome otroSiHome = new OtrosSiHome();
//				otroSiHome.attachDirty(otroSi);
				
				
				otrosSiDAO.persist(otroSi);
	
				return otroSi;
				
			} catch (Exception e) {
				
				return null;
			}
		}

		return null;
	}
	
	
	
	@Override
	public void persist(OtrosSi transientInstance) {
		otrosSiDAO.persist(transientInstance);
	}



	@Override
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance) {
		otrosSiDAO.persistOtrosSiExclusiones(transientInstance);
	}


	@Override
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance) {
		otrosSiDAO.persistOtrosSiInclusiones(transientInstance);
	}



	
	@Override
	public void attachDirty(OtrosSi instance) {
		otrosSiDAO.attachDirty(instance);
	}


	@Override
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto) {
		return otrosSiDAO.obtenerOtrosSiporPresupuesto(codPresoOdonto);
	}
	

}
