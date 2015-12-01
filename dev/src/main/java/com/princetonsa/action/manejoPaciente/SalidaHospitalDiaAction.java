package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.SalidaHospitalDiaForm;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.manejoPaciente.ReingresoHospitalDia;
import com.princetonsa.mundo.manejoPaciente.SalidaHospitalDia;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SalidaHospitalDiaAction extends Action {
	
	/**
	 * Objeto para manejar los logs de la clase
	 */
	private Logger logger=Logger.getLogger(SalidaHospitalDiaAction.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof SalidaHospitalDiaForm)
			{
				SalidaHospitalDiaForm forma=(SalidaHospitalDiaForm) form;
				String estado=forma.getEstado();
				logger.info("\n\nEstado-->> "+estado);
				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				SalidaHospitalDia mundo=new SalidaHospitalDia();
				forma.setMensaje(new ResultadoBoolean(false));
				if(estado==null)
				{
					logger.warn("Estado no valido dentro del flujo de Tipod de Moneda (null)");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getSalidaHospitalDiaMap().get("numRegistros").toString()), response, request, "salidaHospitalDia.jsp",true);

				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../salidaHospitalDia/salidaHospitalDia.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con,forma,usuario,mapping);
				}
				else if(estado.equals("busqueda"))
				{
					return this.accionBusqueda(forma, mapping, con, usuario,mundo);
				}
				else if(estado.equals("ordenar"))
				{
					return this.accionOrdenarMapa(con, forma, mapping);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(con,forma,mundo,usuario,mapping,paciente,request);
				}
				else if(estado.equals("registrarSalida"))
				{
					return this.accionRegistrarSalida(con, forma, request, usuario, mapping, mundo);
				}
				else
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					logger.warn("Estado no valido dentro del flujo de Tipos de Moneda");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form Salida Hospital Dia");
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
	
	public ActionForward accionGuardar(Connection con,SalidaHospitalDiaForm forma,SalidaHospitalDia mundo,UsuarioBasico usuario,ActionMapping mapping,PersonaBasica paciente,HttpServletRequest request)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numeroSolicitud=ConstantesBD.codigoNuncaValido;
		forma.setFecha(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.setHora(UtilidadFecha.getHoraActual());
		
		//validar que no se modifique el mismo registro por varios usuarios al mismo tiempo
		if(ReingresoHospitalDia.existeReingresoOSalidaActivo(con, Integer.parseInt(forma.getSalidaHospitalDiaMap("cuenta_"+forma.getIndexSeleccionado()).toString()), ConstantesIntegridadDominio.acronimoConductaSeguirSalida,Utilidades.convertirAEntero(forma.getSalidaHospitalDiaMap("codigoreingresosalida_"+forma.getIndexSeleccionado())+"")))
        {
        	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error ya existe ingreso", "error.manejoPaciente.existeSalida", true);
        }
		
		//insertar solicitud
		numeroSolicitud=mundo.insertarSolicitud(con,forma,paciente);
		logger.info("\n\nNUMERO SOLICITUD-->>"+numeroSolicitud);
		if(numeroSolicitud>0)
		{
			logger.info("\n\nSOLICITUD GENERADA");
			//generando cargo directo evaluando cobertuda
			Cargos cargos= new Cargos();
		    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																				usuario, 
																				paciente, 
																				false/*dejarPendiente*/, 
																				numeroSolicitud, 
																				ConstantesBD.codigoTipoSolicitudEstancia /*codigoTipoSolicitudOPCIONAL*/, 
																				forma.getCuenta(), 
																				usuario.getCodigoCentroCosto()/*codigoCentroCostoEjecutaOPCIONAL*/, 
																				Utilidades.convertirAEntero(forma.getCodigoServicio())/*codigoServicioOPCIONAL*/, 
																				1/*cantidadServicioOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																				/*"" -- numeroAutorzacionOPCIONAL*/
																				""/*esPortatil*/,false,"",
																				"" /*subCuentaCoberturaOPCIONAL*/);
		   
		    //Inserción del cargo directo
		    CargosDirectos cargoDirecto = new CargosDirectos();
		    cargoDirecto.llenarMundoCargoDirecto(numeroSolicitud,usuario.getLoginUsuario(),ConstantesBD.codigoTipoRecargoSinRecargo,Utilidades.convertirAEntero(forma.getCodigoServicio()),"",true,"");
		    cargoDirecto.insertar(con);
		
		
		    //ingresando datos en reingresosalidahospitaldia
			DtoReingresoSalidaHospiDia dto=new DtoReingresoSalidaHospiDia();
			dto.setLoginUsuarioSalida(usuario.getLoginUsuario());
			dto.setFechaSalida(forma.getFecha());
			dto.setHoraSalida(forma.getHora());
			dto.setTipo(ConstantesIntegridadDominio.acronimoConductaSeguirSalida);
			dto.setObservacionesSalida(forma.getObservaciones());
			dto.setNumeroSolicitudSalida(numeroSolicitud+"");
			dto.setServicioSalida(new InfoDatosInt(Utilidades.convertirAEntero(forma.getCodigoServicio())));
			dto.setCodigo(forma.getSalidaHospitalDiaMap("codigoreingresosalida_"+forma.getIndexSeleccionado())+"");
			transaccion=mundo.actualizarSalidaHospitalDia(con, dto);
			if(transaccion)
			{
				//actualizar el estado del ingreso a cerrado por la salida de hospital dia
				if(!IngresoGeneral.actualizarEstadoIngreso(con, forma.getSalidaHospitalDiaMap("numeroingreso_"+forma.getIndexSeleccionado()).toString(), ConstantesIntegridadDominio.acronimoEstadoCerrado, usuario.getLoginUsuario()))
		    	{
		    		UtilidadBD.abortarTransaccion(con);
		    		logger.info("no actualizo el ingreso a cerrado");
		    		UtilidadBD.closeConnection(con);
		    	}
				else 
				{
					logger.info("\n\nESTADO INGRESO ACTUALIZADO A CERRADO POR LA SALIDA HOSPITAL DIA");
				}
				
				forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
				logger.info("\n\nOPERACION REALIZADA CON EXITO!!!");
				forma.reset(usuario.getCodigoInstitucionInt());
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
				logger.info("\n\nNO SE PUDO INSERTAR EL REGISTRO.");
				forma.reset(usuario.getCodigoInstitucionInt());
				UtilidadBD.abortarTransaccion(con);
			}
		}
		forma.setEstado("empezar");
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,SalidaHospitalDiaForm forma,UsuarioBasico usuario,ActionMapping mapping)
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.resetCriteriosBusqueda();
		forma.inicializarTagMap(usuario.getCodigoInstitucionInt());
		forma.setMaxPageItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusqueda(SalidaHospitalDiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario,SalidaHospitalDia mundo) 
    {
    	boolean existeSalida=false;
		HashMap criteriosBusqueda= armarCriteriosBusqueda(forma, usuario);
    	forma.setSalidaHospitalDiaMap(SalidaHospitalDia.listadoPacientesReingresoOSalida(con, criteriosBusqueda));
    	for(int i=0;i<Utilidades.convertirAEntero(forma.getSalidaHospitalDiaMap("numRegistros")+"");i++)
    	{
    		forma.setCuenta(Utilidades.convertirAEntero(forma.getSalidaHospitalDiaMap("cuenta_"+i)+""));
        	existeSalida=mundo.existeSalidaXFecha(con, forma.getCuenta(), UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
        	if(!existeSalida)
        	{
        		forma.setSalidaHospitalDiaMap("fechaultima_"+i, null);
        	}
    	}
    	forma.setNombreCentroAtencion(usuario.getCentroAtencion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	private ActionForward accionRegistrarSalida(Connection con,SalidaHospitalDiaForm forma,HttpServletRequest request,UsuarioBasico usuario, ActionMapping mapping,SalidaHospitalDia mundo)
	{
		boolean existeSalida=false;
		forma.setCuenta(Utilidades.convertirAEntero(forma.getSalidaHospitalDiaMap("cuenta_"+forma.getIndexSeleccionado())+""));
		//CARGAR EL PACIENTE EN SESION
    	PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
        persona.setCodigoPersona(Integer.parseInt(forma.getSalidaHospitalDiaMap("codigopaciente_"+forma.getIndexSeleccionado())+""));
        try 
        {
			persona.cargar(con,Integer.parseInt(forma.getSalidaHospitalDiaMap("codigopaciente_"+forma.getIndexSeleccionado())+""));
			persona.cargarPaciente(con, Integer.parseInt(forma.getSalidaHospitalDiaMap("codigopaciente_"+forma.getIndexSeleccionado())+""), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		} 
        catch (NumberFormatException e) 
		{
			e.printStackTrace();
		} 
        catch (SQLException e) 
		{
			e.printStackTrace();
		}
        
        //validar si el paciente a la fecha ya tiene salidas registradas
        existeSalida=mundo.existeSalidaXFecha(con, forma.getCuenta(), UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		if(existeSalida)
		{
			forma.setMensaje(new ResultadoBoolean(true,"¡EL PACIENTE YA TIENE REGISTRADA UNA SALIDA!"));
		}
		return mapping.findForward("registrarSalida");
	}

    /**
     * 
     * @param forma
     * @param usuario key{codigoCentroAtencion, esSalida, tipoIdentificacion, numeroIdentificacion, primerNombre, primerApellido}
     * @return
     */
	private HashMap armarCriteriosBusqueda(SalidaHospitalDiaForm forma, UsuarioBasico usuario) 
	{
		HashMap criteriosBusqueda= new HashMap();
		criteriosBusqueda.put("codigoCentroAtencion", usuario.getCodigoCentroAtencion());
		criteriosBusqueda.put("esSalida", "true");
		if(!UtilidadTexto.isEmpty(forma.getTipoIdentificacion()))
			criteriosBusqueda.put("tipoIdentificacion", forma.getTipoIdentificacion());
		if(!UtilidadTexto.isEmpty(forma.getNumeroIdentificacion()))
			criteriosBusqueda.put("numeroIdentificacion", forma.getNumeroIdentificacion());
		if(!UtilidadTexto.isEmpty(forma.getPrimerNombre()))
			criteriosBusqueda.put("primerNombre", forma.getPrimerNombre());
		if(!UtilidadTexto.isEmpty(forma.getPrimerApellido()))
			criteriosBusqueda.put("primerApellido", forma.getPrimerApellido());
		return criteriosBusqueda;
	}

	/**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @return
     */
    private ActionForward accionOrdenarMapa(Connection con, SalidaHospitalDiaForm forma, ActionMapping mapping) 
    {
    	//String[] indices= {"numeroingreso_", "fechahoraingreso_", "fechahoraingresobd_", "nombrepaciente_", "tipoid_", "numeroid_", "fechaultima_", "fechaultimabd_"};
    	String[] indices= {"codigoreingresosalida_", "numeroingreso_", "consecutivoingreso_", "fechahoraingreso_", "fechahoraingresobd_", "nombrepaciente_", "tipoid_", "numeroid_", "fechaultima_", "fechaultimabd_", "codigopaciente_", "cuenta_"};
		int numReg=Integer.parseInt(forma.getSalidaHospitalDiaMap().get("numRegistros")+"");
		forma.setSalidaHospitalDiaMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getSalidaHospitalDiaMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSalidaHospitalDiaMap("numRegistros",numReg+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
    }

}
