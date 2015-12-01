/*
 * @author artotor
 */
package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.PacientesConEgresoPorFacturarForm;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.PacientesConEgresoPorFacturar;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaEgresoPorFacturar;


/**
 * 
 * @author artotor
 *
 */
public class PacientesConEgresoPorFacturarAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PacientesConEgresoPorFacturarAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{
			if(form instanceof PacientesConEgresoPorFacturarForm)
			{

				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PacientesConEgresoPorFacturarForm forma = (PacientesConEgresoPorFacturarForm) form;
				PacientesConEgresoPorFacturar mundo= new PacientesConEgresoPorFacturar();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado = forma.getEstado();
				logger.warn("estado [PacientesConEgresoPorFacturarAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					ValoresPorDefecto.cargarValoresIniciales(con);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					mundo.cargarPacientesConEgresoPorFacturar(con,usuario);
					PropertyUtils.copyProperties(forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("busquedaAvanzada"))
				{
					forma.setDtoFiltro(new DtoFiltroBusquedaAvanzadaEgresoPorFacturar());
					forma.getDtoFiltro().setCentroCosto(usuario.getCodigoCentroCosto());
					forma.getDtoFiltro().setFechaEgresoFinal(UtilidadFecha.getFechaActual(con));
					forma.getDtoFiltro().setFechaEgresoInicial(UtilidadFecha.incrementarDiasAFecha(forma.getDtoFiltro().getFechaEgresoFinal(), -1, false));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaBusquedaAvanzada");
				}
				else if (estado.equals("buscar"))
				{
					ValoresPorDefecto.cargarValoresIniciales(con);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					mundo.cargarPacientesConEgresoPorFacturarAvanzado(con,usuario,forma.getDtoFiltro());
					PropertyUtils.copyProperties(forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("generarFacturar"))
				{

					UtilidadSesion.notificarCambiosObserver(Integer.parseInt(forma.getPacientes("codigopaciente_"+forma.getIndex())+""), getServlet().getServletContext());

					paciente.setCodigoPersona((Integer.parseInt(forma.getPacientes("codigopaciente_"+forma.getIndex())+"")));
					paciente.cargar(con,(Integer.parseInt(forma.getPacientes("codigopaciente_"+forma.getIndex())+"")));
					//paciente.cargarPaciente(con, (Integer.parseInt(forma.getPacientes("codigopaciente_"+forma.getIndex())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					paciente.cargarPacienteXingreso(con, forma.getPacientes("codigoingreso_"+forma.getIndex())+"",usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					setObservable(paciente);
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect("../facturacion/facturacion.do?estado=empezar");
					return null;
				}	
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de PacientesConEgresoPorFacturarForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
	
	/**
	 * 
	 */
	private void accionOrdenar(PacientesConEgresoPorFacturarForm forma) 
	{
		
		int numeroElementos=Integer.parseInt(forma.getPacientes("numRegistros")==null?"0":forma.getPacientes("numRegistros")+"");
		SimpleDateFormat dateFormatter = new SimpleDateFormat( "dd/MM/yyyy");
		
		if(forma.getPatronOrdenar().equals("fechahoraadmision_")||forma.getPatronOrdenar().equals("fechahoraengreso_")){
			
			for (int k = 0; k < numeroElementos; k ++)
			{
				String separoFechaHora[]=forma.getPacientes().get(forma.getPatronOrdenar()+k).toString().split(" ");
				if (UtilidadFecha.validarFecha(separoFechaHora[0]))
	        	{
	        		java.util.Date theDate;
					try {
						theDate = dateFormatter.parse(separoFechaHora[0]);
						//SE CAMBIA AL FORMATO ANO, MES , DIA
						String fecFormatoBD = UtilidadFecha.conversionFormatoFechaABD(theDate);
						forma.getPacientes().put(forma.getPatronOrdenar()+k,fecFormatoBD+" "+separoFechaHora[1]); 
					}catch (ParseException e) {
						e.printStackTrace();
					}
				}
				      
			}
		}
		
		String[] indices={"admision_","fechahoraadmision_","viaingreso_","fechahoraengreso_","area_","nombrepaciente_","tipoid_","numeroid_","nombremedico_","codigopaciente_"};
		HashMap pacientes=Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getPacientes(),numeroElementos);
		
		if(forma.getPatronOrdenar().equals("fechahoraadmision_")||forma.getPatronOrdenar().equals("fechahoraengreso_")){
			
			SimpleDateFormat dateFormatterGuion = new SimpleDateFormat( "yyyy-MM-dd");
			
			for (int k = 0; k < numeroElementos; k ++)
			{
				String separoFechaHora[]=pacientes.get(forma.getPatronOrdenar()+k).toString().split(" ");
					java.util.Date theDate;
					try {
						theDate = dateFormatterGuion.parse(separoFechaHora[0]);
						//SE CAMBIA AL FORMATO DIA MES AÑO
						String fecFormatoBD = UtilidadFecha.conversionFormatoFechaAAp(theDate);
						pacientes.put(forma.getPatronOrdenar()+k,fecFormatoBD+" "+separoFechaHora[1]); 
					}catch (ParseException e) {
						e.printStackTrace();
					}
			}
		}
		
		
		forma.setPacientes(pacientes);
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPacientes("numRegistros",numeroElementos+"");
		
		
	}
	
}
