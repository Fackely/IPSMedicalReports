package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.dto.odontologia.DtoTratamientoExterno;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.UtilidadOdontologia;

/**
 * @author Víctor Hugo Gómez L.
 */

public class ComponenteAnteOdoto 
{
	private static Logger logger = Logger.getLogger(ComponenteOdontograma.class);
	private String forward;
	

	public ComponenteAnteOdoto()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.forward = "";
	}

	/**
	 * @return the forward
	 */
	public String getForward() {
		return forward;
	}

	/**
	 * @param forward the forward to set
	 */
	public void setForward(String forward) {
		this.forward = forward;
	}

	public static ActionErrors accionesEstados(InfoAntecedenteOdonto infoAnteOdo, String estado, boolean esEvolucion)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("empezar") || estado.equals("mostarPor"))
		{
			cargarComponenteAnteOdonto(infoAnteOdo,esEvolucion);  
		}else if(estado.equals("trataExter") || estado.equals("modificarTE") || estado.equals("eliminarTE"))
		{
			infoAnteOdo.setProcesoExitoTE(ConstantesBD.acronimoNo);
			// se resetea el dto auxiliar de tratamiento externo
			
			DtoTratamientoExterno dto = new DtoTratamientoExterno();
			infoAnteOdo.setTrataExterno(dto);
			
			if(estado.equals("trataExter")){
				infoAnteOdo.getTrataExterno().setEliminar(ConstantesBD.acronimoNo);
				infoAnteOdo.getTrataExterno().setModificar(ConstantesBD.acronimoNo);
				infoAnteOdo.getTrataExterno().setNuevo(ConstantesBD.acronimoSi);
				infoAnteOdo.setForward("tratamientoExt");
			}else if (estado.equals("eliminarTE")){
				infoAnteOdo.getTrataExterno().setNuevo(ConstantesBD.acronimoNo);
				infoAnteOdo.getTrataExterno().setModificar(ConstantesBD.acronimoNo);
				infoAnteOdo.getTrataExterno().setEliminar(ConstantesBD.acronimoSi);
				actualizarTratamientoExterno(infoAnteOdo);
			}else if (estado.equals("modificarTE")){
				infoAnteOdo.setTrataExterno(infoAnteOdo.getAntecedenteOdon().getTratamientosExternos().get(infoAnteOdo.getPosTratamientoExterno()));
				infoAnteOdo.getTrataExterno().setDescripcionPiezaDen(infoAnteOdo.getTrataExterno().getCodigoPiezaDen()+ConstantesBD.separadorSplit+infoAnteOdo.getTrataExterno().getDescripcionPiezaDen());
				infoAnteOdo.getTrataExterno().setDescripcionEsp(infoAnteOdo.getTrataExterno().getCodigoEspecialidad()+ConstantesBD.separadorSplit+infoAnteOdo.getTrataExterno().getDescripcionEsp());
				infoAnteOdo.getTrataExterno().setNuevo(ConstantesBD.acronimoNo);
				infoAnteOdo.getTrataExterno().setEliminar(ConstantesBD.acronimoNo);
				infoAnteOdo.getTrataExterno().setModificar(ConstantesBD.acronimoSi);
				infoAnteOdo.setForward("tratamientoExt");
			}
		}else if(estado.equals("guardarTrataExt"))
		{
			actualizarTratamientoExterno(infoAnteOdo);
		}else if(estado.equals("refresTratExt")){
			logger.info("entra esta parte");
			errores=validacionTratamientoExterno(infoAnteOdo);
		}
		return errores;
				
	}
	
	/**
	 * metodo que obtiene los servicios de los planes de tratamientos terminados 
	 * @param infoAnteOdo
	 * @return
	 */
	public static void cargarComponenteAnteOdonto(InfoAntecedenteOdonto infoAnteOdo, boolean esEvolucion)
	{
		ArrayList<InfoPlanTratamiento> array = new ArrayList<InfoPlanTratamiento>();
		PlanTratamiento planTrataOdo = new PlanTratamiento();
		// Estados del Plan de Tratamiento
		ArrayList<String> estados = new ArrayList<String>();
		estados.add(ConstantesIntegridadDominio.acronimoTerminado);
		
		if(infoAnteOdo.getAntecedenteOdon().getTratamientosExternos().size()<=0)
		{
			if(infoAnteOdo.getPorConfirmar().equals(ConstantesBD.acronimoNo))
				infoAnteOdo.setAntecedenteOdon(UtilidadOdontologia.obtenerAntecedenteOdontologico(infoAnteOdo.getCodigoPaciente()));
			else
				infoAnteOdo.setAntecedenteOdon(UtilidadOdontologia.obtenerAntecedenteOdontExistente(infoAnteOdo.getCodigoAnteOdon(),esEvolucion));
		}
		infoAnteOdo.setInfoPlaTratamiento(PlanTratamiento.obtenerPlanTratamiento(
				infoAnteOdo.getCodigoPaciente(), 
				estados, 
				ConstantesBD.acronimoNo,
				ConstantesBD.acronimoSi, 
				infoAnteOdo.getMostrarPor().equals(ConstantesIntegridadDominio.acronimoMostrarProgramas)?true:false));
		
		
		//infoAnteOdo.getAntecedenteOdon().setTratamientosExternos(UtilidadOdontologia.obtenerAnteOdoTratamientosExternos(infoAnteOdo.getCodigoPaciente()));
		
		infoAnteOdo.setForward("principal");
	}
	
	/**
	 * metodo que actualiza los tratamiento externos 
	 * @param infoAnteOdo
	 */
	public static void actualizarTratamientoExterno(InfoAntecedenteOdonto infoAnteOdo)
	{
		
		String[] aux = null;
		if(infoAnteOdo.getTrataExterno().getEliminar().equals(ConstantesBD.acronimoNo))
		{
			if(!infoAnteOdo.getTrataExterno().getDescripcionPiezaDen().equals(""))
			{
				aux = infoAnteOdo.getTrataExterno().getDescripcionPiezaDen().split(ConstantesBD.separadorSplit);
				infoAnteOdo.getTrataExterno().setCodigoPiezaDen(Utilidades.convertirAEntero(aux[0]));
				infoAnteOdo.getTrataExterno().setDescripcionPiezaDen(aux[1]);
			}
			if(!infoAnteOdo.getTrataExterno().getDescripcionEsp().equals(""))
			{
				aux = infoAnteOdo.getTrataExterno().getDescripcionEsp().split(ConstantesBD.separadorSplit);
				infoAnteOdo.getTrataExterno().setCodigoEspecialidad(Utilidades.convertirAEntero(aux[0]));
				infoAnteOdo.getTrataExterno().setDescripcionEsp(aux[1]);
			}
		}
		
		if(infoAnteOdo.getTrataExterno().getNuevo().equals(ConstantesBD.acronimoSi)){
			infoAnteOdo.getAntecedenteOdon().getTratamientosExternos().add(infoAnteOdo.getTrataExterno());
		}else if(infoAnteOdo.getTrataExterno().getEliminar().equals(ConstantesBD.acronimoSi)){
			infoAnteOdo.getAntecedenteOdon().getTratamientosExternos().remove(infoAnteOdo.getPosTratamientoExterno());
		}else if(infoAnteOdo.getTrataExterno().getModificar().equals(ConstantesBD.acronimoSi))
		{
			infoAnteOdo.getAntecedenteOdon().getTratamientosExternos().remove(infoAnteOdo.getPosTratamientoExterno());
			infoAnteOdo.getAntecedenteOdon().getTratamientosExternos().add(infoAnteOdo.getTrataExterno());
		}
		// en esta parte se debe llamar un ordenar de tratamieto externos
		
		for(DtoTratamientoExterno elem:infoAnteOdo.getAntecedenteOdon().getTratamientosExternos())
			logger.info("Nuevo: "+elem.getNuevo()+" Eliminar: "+elem.getEliminar()+" Modificar: "+elem.getModificar());
			
		logger.info("Imprimir dto: "+UtilidadLog.obtenerString(infoAnteOdo.getTrataExterno(), true));
		infoAnteOdo.setForward("principal");
	}
	
	/**
	 * metodo que valida el formulario de tratamientos externos
	 * @param infoAnteOdo
	 */
	public static ActionErrors validacionTratamientoExterno(InfoAntecedenteOdonto infoAnteOdo)
	{
		ActionErrors errores=new ActionErrors();
		if(infoAnteOdo.getTrataExterno().getFechaInicio().equals("")
				&& infoAnteOdo.getTrataExterno().getFechaFinal().equals("")
				&& infoAnteOdo.getTrataExterno().getProgramaServicio().equals("")
				&& infoAnteOdo.getTrataExterno().getDescripcionPiezaDen().equals("")
				&& infoAnteOdo.getTrataExterno().getDescripcionEsp().equals(""))
		{
			errores.add("trataexterno",new ActionMessage("errors.required", "Al menos un campo del formulario "));
		}else{
			infoAnteOdo.setProcesoExitoTE(ConstantesBD.acronimoSi);
		}
		return errores;
	}

	/**
	 * Méetodo que hace el llenado inicial de los campos de tipo seleccion
	 * @param infoAnteOdo
	 * @param con
	 */
	public static ActionErrors llenadoInicialInterfaz(InfoAntecedenteOdonto infoAnteOdo, Connection con)
	{
		ActionErrors errores = new ActionErrors();
		try
		{
			// se carga la lista de piezas dentales y de especialidades y no esta cargada ya
			if(infoAnteOdo.getPiezasDentales().size()<=0)
				infoAnteOdo.setPiezasDentales(UtilidadOdontologia.obtenerPiezasDentales());
			if(infoAnteOdo.getEspecialidadesOdonto().size()<=0)
				infoAnteOdo.setEspecialidadesOdonto(Utilidades.obtenerEspecialidadesEnArray(con, 
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			errores.add("llenadoInicial",new ActionMessage("errors.notEspecific", "Error en el cargado de los campos. "));
		}
		return errores;
	}

	
	
}
