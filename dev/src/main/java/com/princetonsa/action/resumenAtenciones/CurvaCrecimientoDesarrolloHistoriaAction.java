package com.princetonsa.action.resumenAtenciones;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;

import com.princetonsa.actionform.resumenAtenciones.CurvaCrecimientoDesarrolloHistoriaForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Juan Camilo Gaviria Acosta
 */
public class CurvaCrecimientoDesarrolloHistoriaAction extends DispatchAction
{	
	/**
	 * empezar
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward empezar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		try 
		{
			CurvaCrecimientoDesarrolloHistoriaForm forma = (CurvaCrecimientoDesarrolloHistoriaForm) form;
			forma.reset();
			
			PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			forma.setPaciente(paciente);
			
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			forma.setUsuario(usuario);
			
			HistoriaClinicaFacade hcf = new HistoriaClinicaFacade();
			
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date fechaCorte = null;
			fechaCorte = formatoDelTexto.parse(System.getProperty("FECHACORTECURVASCRECIMIENTO") + " " + System.getProperty("HORACORTECURVASCRECIMIENTO"));

			
			forma.setHayCurvasAnteriores(hcf.existeCurvasAnteriores(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt(), fechaCorte));
			
			forma.setDtoHistoricoImagenPlantilla(new ArrayList<HistoricoImagenPlantillaDto>());
			List<HistoricoImagenPlantillaDto> dtohipTemp = hcf.valoracionesYevolucionesPorpacienteConCurva(paciente.getCodigoPersona());
			
			if(dtohipTemp.size() > 0)
			{
				TreeMap<String , HistoricoImagenPlantillaDto> dtohip = new TreeMap<String, HistoricoImagenPlantillaDto>();
				
				dtohip.put(dtohipTemp.get(1).getTituloGrafica() + dtohipTemp.get(1).getEdadInicial() + dtohipTemp.get(1).getEdadFinal(), dtohipTemp.get(1));
									
				for (int i = 0; i < dtohipTemp.size(); i++)
					if(!dtohip.containsKey(dtohipTemp.get(i).getTituloGrafica() + dtohipTemp.get(i).getEdadInicial() + dtohipTemp.get(i).getEdadFinal()))
						dtohip.put(dtohipTemp.get(i).getTituloGrafica() + dtohipTemp.get(i).getEdadInicial() + dtohipTemp.get(i).getEdadFinal(), dtohipTemp.get(i));
				
				forma.setDtoHistoricoImagenPlantilla(new ArrayList<HistoricoImagenPlantillaDto>(dtohip.values()));
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (IPSException e) 
		{
			e.printStackTrace();
		}
		catch (ClassCastException e)
		{
			Log4JManager.error(e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return mapping.findForward("empezar");
	}
	
	/**
	 * mostrarDetalles
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward mostrarDetalles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		try
		{
			CurvaCrecimientoDesarrolloHistoriaForm forma = (CurvaCrecimientoDesarrolloHistoriaForm) form;
			forma.setCurvaSeleccionada(forma.getDtoHistoricoImagenPlantilla().get(forma.getIndiceCurvaSeleccionada()));

			Calendar cIngreso = Calendar.getInstance();
			cIngreso.setTime(forma.getCurvaSeleccionada().getFecha());

			int[] vEdad=UtilidadFecha.calcularVectorEdad
					(
						Integer.valueOf(forma.getPaciente().getAnioNacimiento()),
						Integer.valueOf(forma.getPaciente().getMesNacimiento()),
						Integer.valueOf(forma.getPaciente().getDiaNacimiento()),
						cIngreso.get(Calendar.DAY_OF_MONTH),
						cIngreso.get(Calendar.MONTH) + 1, //enero es el mes CERO
						cIngreso.get(Calendar.YEAR)
					);
			
			String edad = "";
			
			if(vEdad[0] == 1)
				edad = edad + vEdad[0] + " año ";
			else
				edad = edad + vEdad[0] + " años ";
			
			if(vEdad[1] == 1)
				edad = edad + vEdad[1] + " mes ";
			else
				edad = edad + vEdad[1] + " meses ";
			
			if(vEdad[2] == 1)
				edad = edad + vEdad[2] + " dia ";
			else
				edad = edad + vEdad[2] + " dias ";
					
			forma.setEdadCalculada(edad);
			forma.setMostrarDetalles(true);
		}
		catch (ClassCastException e)
		{
			Log4JManager.error(e);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return mapping.findForward("empezar");
	}
	
	
//	public ActionForward empezarPorIngreso(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
//		try 
//		{
//			CurvaCrecimientoDesarrolloHistoriaForm forma = (CurvaCrecimientoDesarrolloHistoriaForm) form;
//			forma.reset();
//			
//			PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
//			forma.setPaciente(paciente);
//						
//			DtoHistoricoImagenPlantilla dtohip = new HistoriaClinicaFacade().evolucionesPorId(Integer.valueOf(forma.getCodigoValEvol()));
//			forma.setCurvaSeleccionada(dtohip);
//			forma.setMostrarDetalles(true);
//		}
//		catch (IPSException e) 
//		{
//			e.printStackTrace();
//		}
//		return null;
//	}
}