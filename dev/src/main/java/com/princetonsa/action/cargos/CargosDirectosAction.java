/*
 * @(#)CargosDirectosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.cargos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifaVigente;
import util.facturacion.InfoTarifaYExcepcion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cargos.CargosDirectosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.servinte.axioma.bl.inventario.facade.InventarioFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Action, controla todas las opciones dentro de Cargos Directos 
 * incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Junio 20, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 * 
 * Modificado -> 25 Marzo 2008. Anexo 550. Jose Eduardo Arias Doncel.
 */
public class CargosDirectosAction extends Action    
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CargosDirectosAction.class);

	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
	{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof CargosDirectosForm)
		{
			Connection con=null;
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			if(paciente.getExisteAsocio() && paciente.getCodigoCuentaAsocio()<=0)
				paciente.setExisteAsocio(false);
			
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			ActionErrors errores = new ActionErrors();
			CargosDirectosForm cargosForm =(CargosDirectosForm)form;
            cargosForm.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			String estado=cargosForm.getEstado();
			logger.warn("El estado en los cargos directos es------->"+estado);
			
			if(estado.equals("empezar") || estado.equals("listarAlmacenes"))
			{    
				ActionForward validacionesGenerales = this.validacionesAccesoUsuario(con, paciente,  mapping, request, cargosForm, usuario);
				if (validacionesGenerales != null)
				{
					UtilidadBD.cerrarConexion(con);
					return validacionesGenerales ;
				}
			}	
			
			if(estado == null)
			{
				cargosForm.reset();	
                cargosForm.resetMensajes();
				logger.warn("Estado no valido dentro del flujo de Cargos Directos (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
	            UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			//estado donde se realizaran las validaciones de los estados de la cuenta
			else if (estado.equals("empezar"))
			{
				return this.accionEmpezar(cargosForm,mapping, con,paciente, usuario);
			}
			else if(estado.equals("empezarContinuar"))
			{
				cargarProfesionalesEjecutan(cargosForm, usuario);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("agregarArticulo"))
			{
				calcularValorCargoArticulo(con, cargosForm, paciente, usuario);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardarArticulos"))
			{
			    return this.flujoInsertarMedicamentos(con, request, mapping, cargosForm, Integer.parseInt(cargosForm.getIdCuenta()), paciente, usuario);
			}
			else if(estado.equals("principal"))
			{
			    return this.accionEmpezarCaso2(con, mapping, cargosForm, paciente, usuario);
			}
			else if(estado.equals("guardarServicios"))
			{
				errores = validacionesGuardarInfoRIPS(con,cargosForm,usuario,paciente.getFechaIngreso());
				
				if(!errores.isEmpty())
				{
					UtilidadBD.closeConnection(con);
					saveErrors(request,errores);
					return mapping.findForward("principal");
				}
				else			
					return this.flujoInsertarServicios(con, request, mapping, cargosForm, Integer.parseInt(cargosForm.getIdCuenta()), paciente, usuario);
			}
            else if(estado.equals("listarAlmacenes"))
            {
                return this.accionListarAlmacenes(cargosForm, usuario, mapping, con, paciente, request);
            }			
			//Requerimiento Anexo 550.*********************************
			else if(estado.equals("nuevoServicio"))
			{
				
				logger.info("\n\n**********************************************************************************************");
				logger.info("NUMERO SERVICIOS-->"+cargosForm.getNumeroFilasMapaCasoServicios());
				logger.info("MAPA--->"+cargosForm.getMapaUtilitarioMap());
				logger.info("\n\nMAPA serv--->"+cargosForm.getServiciosArticulosMap());
				logger.info("**********************************************************************************************\n\n");
				calcularValorCargoServicio(con, cargosForm, paciente, usuario);
				cargosForm.setIndice(cargosForm.getNumeroFilasMapaCasoServicios()-1);
				ArrayList<InfoDatosInt> centroCostos= UtilidadesOrdenesMedicas.obtenerCentrosCostoEjecuta(Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoServicio_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1))+""), usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), cargosForm.getCodigoCentroCostoSolicita());
				cargosForm.getCentrosCostoEjecutanArray().add(centroCostos);
				
				if(centroCostos.size()==1)
				{
					cargosForm.setServiciosArticulosMap("codigoCentroCostoEjecuta_"+cargosForm.getIndice(), centroCostos.get(0).getCodigo());
				}
				cargarProfesionalesEjecutan(cargosForm, usuario);
				
				validacionesServicioXManejoRips(con,cargosForm,usuario);
			    UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("eliminarServicio"))
			{
				//metodoEstadoEliminarServicio(cargosForm,usuario);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			// Cambio Segun Anexo 809
			else if(estado.equals("filtrarEspMedicoResponde"))
			{
				return accionFiltrarEspProfResponde(con,cargosForm,response);
			}
			// Fin Cambio Segun Anexo 809
			//******************************************************			
			else
			{
				cargosForm.reset();	
                cargosForm.resetMensajes();
				logger.warn("Estado no valido dentro del flujo de Cargos Directos (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}	
		return null;	
	}			
	
	/**
	 * 
	 * @param cargosForm
	 */
	private void cargarProfesionalesEjecutan(CargosDirectosForm cargosForm, UsuarioBasico usuario) 
	{
		if(cargosForm.getIndice()>ConstantesBD.codigoNuncaValido)
		{
			ArrayList<InfoDatosInt> arrayVacio= new ArrayList<InfoDatosInt>();
			arrayVacio.add(new InfoDatosInt(ConstantesBD.codigoNuncaValido, "Seleccione"));
			int servicio= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoServicio_"+cargosForm.getIndice())+"");
	        int centroCostoEjecuta= Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoCentroCostoEjecuta_"+cargosForm.getIndice())+"");
	        
	        if(cargosForm.getIndice()>=cargosForm.getProfesionalesEjecutanArray().size()){
        		cargosForm.getProfesionalesEjecutanArray().add(cargosForm.getIndice(), arrayVacio);
        	}
        	if(cargosForm.getIndice()>=cargosForm.getEspecialidadProfesionalesEjecutanArray().size()){
        		cargosForm.getEspecialidadProfesionalesEjecutanArray().add(cargosForm.getIndice(),arrayVacio);
        	}
        	
	        if(centroCostoEjecuta>0)
	        {	
	        	cargosForm.getProfesionalesEjecutanArray().set(cargosForm.getIndice(),UtilidadesOrdenesMedicas.obtenerProfesionalesEjecutan(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), centroCostoEjecuta, cargosForm.getCodigoCentroCostoSolicita(), servicio));
	        	cargosForm.getEspecialidadProfesionalesEjecutanArray().set(cargosForm.getIndice(),arrayVacio);
	        }	
	        else
	        {
				cargosForm.getProfesionalesEjecutanArray().set(cargosForm.getIndice(), arrayVacio);
				cargosForm.getEspecialidadProfesionalesEjecutanArray().set(cargosForm.getIndice(),arrayVacio);
	        }
	    }
		cargosForm.setIndice(ConstantesBD.codigoNuncaValido);
	}

	/**
	 * 
	 * @param con
	 * @param cargosForm
	 */
	private void calcularValorCargoArticulo(Connection con,	CargosDirectosForm cargosForm, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		cargosForm.setNumeroFilasMapa(cargosForm.getNumeroFilasMapa()+1);
		logger.info("\n\n\ncuenta--->"+cargosForm.getIdCuenta());
		int codigoArticulo=Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoArticulo_"+(cargosForm.getNumeroFilasMapa()-1))+"");
		Cuenta cuenta= new Cuenta();
    	cuenta.cargarCuenta(con, cargosForm.getIdCuenta());
    	
    	String tipoPaciente= UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, cargosForm.getIdCuenta()).getAcronimo();
	    InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaArticulo(con, cuenta.getCodigoIngreso()+"", Utilidades.convertirAEntero(cuenta.getCodigoViaIngreso()),tipoPaciente, codigoArticulo, usuario.getCodigoInstitucionInt(),false);
	    int codigoTipoComplejidad=  Cuenta.obtenerTipoComplejidad(con, cargosForm.getIdCuenta()+"");
	    int codigoEsquemaTarifario= Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"", infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo, false,cargosForm.getFecha(), usuario.getCodigoCentroAtencion());
	    //@FIXME TARIFAS CENTRO ATENCION
	    InfoTarifaYExcepcion infoTarifaYExcepcion=Cargos.obtenerValorTarifaYExcepcion(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo(), infoResponsableCobertura.getDtoSubCuenta().getContrato(), codigoEsquemaTarifario, usuario.getCodigoInstitucionInt(), codigoArticulo, false, tipoPaciente,  Utilidades.convertirAEntero(cuenta.getCodigoViaIngreso()), codigoTipoComplejidad, cargosForm.getFecha(),usuario.getCodigoCentroAtencion());
	    
	    logger.info("valorTarifaTotal-->"+infoTarifaYExcepcion.getValorTarifaTotalConExcepcion()+" tarifa base->"+infoTarifaYExcepcion.getValorTarifaBase());
	    cargosForm.setServiciosArticulosMap("codigoContrato_"+(cargosForm.getNumeroFilasMapa()-1),infoResponsableCobertura.getDtoSubCuenta().getNumeroContrato());
	    cargosForm.setServiciosArticulosMap("valorUnitarioOriginal_"+(cargosForm.getNumeroFilasMapa()-1), infoTarifaYExcepcion.getValorTarifaTotalConExcepcion()+"");
	    cargosForm.setServiciosArticulosMap("valorUnitario_"+(cargosForm.getNumeroFilasMapa()-1), infoTarifaYExcepcion.getValorTarifaTotalConExcepcion()+"");
	    if(infoTarifaYExcepcion.getExisteTarifaBase())
	    	cargosForm.setServiciosArticulosMap("existeTarifa_"+(cargosForm.getNumeroFilasMapa()-1), "si");
	}

	/**
	 * Metodo que evalua el valor del cargo del servicio 
	 * @param con
	 * @param cargosForm
	 */
    private void calcularValorCargoServicio(Connection con, CargosDirectosForm cargosForm, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
    {
    	logger.info("\n\n\ncuenta--->"+cargosForm.getIdCuenta());
    	int codigoServicio=Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoServicio_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1))+"");
    	Cuenta cuenta= new Cuenta();
    	cuenta.cargarCuenta(con, cargosForm.getIdCuenta());
    	
    	String tipoPaciente= UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, cargosForm.getIdCuenta()).getAcronimo();
    	InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, cuenta.getCodigoIngreso()+"", Utilidades.convertirAEntero(cuenta.getCodigoViaIngreso()), tipoPaciente, codigoServicio, usuario.getCodigoInstitucionInt(), false, "");
    	int codigoTipoComplejidad=  Cuenta.obtenerTipoComplejidad(con, cargosForm.getIdCuenta()+"");
    	int codigoEsquemaTarifario= Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"", infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicio, true,cargosForm.getFechaEjeccuion() ,usuario.getCodigoCentroAtencion());
    	int tipoTarifario=Utilidades.obtenertipoTarifarioEsquema(con,codigoEsquemaTarifario);
    	
    	InfoTarifaVigente infoTarifaVigente=Cargos.obtenerTarifaBaseServicio(con, tipoTarifario, codigoServicio, codigoEsquemaTarifario, cargosForm.getFechaEjeccuion() /*fechaVigencia*/);
		boolean error=false;
		InfoTarifaYExcepcion infoTarifaYExcepcion= new InfoTarifaYExcepcion(0,0); 
		
		if(infoTarifaVigente.getTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatGrupo || infoTarifaVigente.getTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUvr)
		{
			cargosForm.getServiciosArticulosMap().put("fueEliminadoServicio_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1), "true");
			cargosForm.getServiciosArticulosMap().put("error_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1),"La tarifa actual del servicio "+cargosForm.getServiciosArticulosMap("descripcionServicio_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1))+" tiene tipo de liquidación grupo o uvr, por tal motivo no puede ser seleccionado.");
			error=true;
		}
		else
		{	
			if(infoTarifaVigente.isLiquidarAsocios())
			{
				cargosForm.getServiciosArticulosMap().put("fueEliminadoServicio_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1), "true");
				cargosForm.getServiciosArticulosMap().put("error_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1),"La tarifa actual del servicio "+cargosForm.getServiciosArticulosMap("descripcionServicio_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1))+" es base de asocios, por tal motivo no puede ser seleccionado.");
				error=true;
			}
			else
			{
				//@FIXME TARIFAS CENTRO ATENCION
				infoTarifaYExcepcion=Cargos.obtenerValorTarifaYExcepcion(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo(), infoResponsableCobertura.getDtoSubCuenta().getContrato(), codigoEsquemaTarifario, usuario.getCodigoInstitucionInt(), codigoServicio, true, tipoPaciente,  Utilidades.convertirAEntero(cuenta.getCodigoViaIngreso()), codigoTipoComplejidad, cargosForm.getFechaEjeccuion(),usuario.getCodigoCentroAtencion());
				if(infoTarifaYExcepcion.getExisteTarifaBase())
				{	
					cargosForm.setServiciosArticulosMap("existeTarifa_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1), "si");
				}	
			}	
		}	
		
    	logger.info("infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()-->"+infoResponsableCobertura.getDtoSubCuenta().getSubCuenta());
		logger.info("infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()-->"+infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		logger.info("valorTarifaTotal-->"+infoTarifaYExcepcion.getValorTarifaTotalConExcepcion()+" tarifa base->"+infoTarifaYExcepcion.getValorTarifaBase());
		
		if(!error)
		{	cargosForm.setServiciosArticulosMap("codigoContrato_"+(cargosForm.getNumeroFilasMapa()-1),infoResponsableCobertura.getDtoSubCuenta().getContrato());
			cargosForm.setServiciosArticulosMap("valorUnitarioConExcepcionOriginal_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1), infoTarifaYExcepcion.getValorTarifaTotalConExcepcion()+"");
			cargosForm.setServiciosArticulosMap("valorUnitarioConExcepcion_"+(cargosForm.getNumeroFilasMapaCasoServicios()-1), infoTarifaYExcepcion.getValorTarifaTotalConExcepcion()+"");
		}	
	}

	/**
     * estado donde se listan los almacenes 
     * @param cargosForm
     * @param usuario
     * @param mapping
     * @param con
     * @param paciente
     * @param req
     * @return
     * @throws SQLException
     */
    private ActionForward accionListarAlmacenes (   CargosDirectosForm cargosForm, 
                                                                            UsuarioBasico usuario, 
                                                                            ActionMapping mapping, 
                                                                            Connection con, 
                                                                            PersonaBasica paciente, 
                                                                            HttpServletRequest req)throws SQLException, IPSException
    {
        //Limpiamos lo que venga del form
        cargosForm.reset();
        cargosForm.resetMensajes();
        ArrayList<String> aux = new ArrayList<String>();
        //se valida que el paciente es de entidad subcontratada
	    if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
	    {
	    	aux.add("Ingreso paciente por entidad subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
	    	cargosForm.setMensajes(aux);
	    }
	    
	    // CAPTURO EL NUMERO DE CONVENIOS PLAN ESPECIAL POR INGRESO
	    cargosForm.setNumConveniosPlanEspecial(Cargos.conveniosPlanEspecial(con, paciente.getCodigoIngreso()));
        
        logger.info("Cantidad Convenios Plan Especial por Ingreso -> "+cargosForm.getNumConveniosPlanEspecial());
	    
	    
	    boolean planEspecial;
        // PREGUNTO SI LOS CONVENIOS SON MAYORES A CERO PARA MOSTRAR LOS ALMACENES
        if(cargosForm.getNumConveniosPlanEspecial()>0)
        {
        	logger.info("SI TIENE CONVENIOS PLAN ESPECIAL");
        	planEspecial=true;
        	
        	logger.info("Numero de Almacenes Parametrizados como Plan Especial --->"+UtilidadInventarios.existeAlmacenPlanEspecial(con, usuario.getCodigoCentroAtencion()));
        	if(UtilidadInventarios.existeAlmacenPlanEspecial(con, usuario.getCodigoCentroAtencion()) < 1)
    	    {
        		ActionErrors errores = new ActionErrors(); 
            	errores.add("No hay definido almacenes Plan Especial", new ActionMessage("error.inventarios.parametrosAlmacenPlanEspecial"));
            	saveErrors(req, errores);
                UtilidadBD.cerrarConexion(con);
                return mapping.findForward("paginaErroresActionErrors");
    	    }
        }
        else
        {
        	logger.info("NO TIENE CONVENIOS PLAN ESPECIAL");
        	planEspecial=false;
        }	
	    
	    
	    /*
	     * cambio por tarea 36141
	     */////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*gosForm.setListadoAlmacenesMap(	UtilidadInventarios.listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(
							        		usuario.getCodigoInstitucionInt(), 
											usuario.getLoginUsuario(),
											ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(),true),"", ConstantesBD.codigoNuncaValido, planEspecial));
       */
        
        cargosForm.setListadoAlmacenesMap(UtilidadInventarios.listadoAlmacenes(usuario.getCodigoInstitucionInt(), paciente.getCodigoArea() , ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(),true), "", planEspecial));
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        // si es cero entonces sacar el error
        if(cargosForm.getListadoAlmacenesMap("numRegistros").equals("0"))
        {
            //error.inventarios.noExistenAlmacenesPermitidos
        	ActionErrors errores = new ActionErrors(); 
        	errores.add("usuario no definido almacen", new ActionMessage("error.inventarios.UsuarioNoDefinidoEnAlmacen"));
        	//errores.add("no hay almacenes permitidos", new ActionMessage("error.inventarios.noExistenAlmacenesPermitidos","SOLICITUDES DE ARTICULOS"));
            saveErrors(req, errores);
            UtilidadBD.cerrarConexion(con);
            return mapping.findForward("paginaErroresActionErrors");

        }
        //si el numero de registros es uno entonces pasar a la pagina siguiente
        else if(cargosForm.getListadoAlmacenesMap("numRegistros").equals("1"))
        {
            cargosForm.setCodigoFarmacia(Integer.parseInt(cargosForm.getListadoAlmacenesMap("codigo_0").toString()));
            cargosForm.setNombreFarmacia(cargosForm.getListadoAlmacenesMap("nombre_0").toString());
            cargosForm.setEstado("empezar");
            return accionEmpezar(cargosForm, mapping, con, paciente, usuario);
        }
        else
        {
            UtilidadBD.cerrarConexion(con);
            return mapping.findForward("listadoAlmacenes");
        }
    }
    
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * Método usado para evaluar la pantalla inicial según el estado de la cuenta
	 * @param cargosForm CargosDirectosForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param paciente
	 * @return ActionForward a la página principal "menuCargosDirectos.jsp"
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionEmpezar(	CargosDirectosForm cargosForm, 
																ActionMapping mapping, 
																Connection con, PersonaBasica paciente,
																UsuarioBasico user) throws SQLException, IPSException
	{
		ArrayList<String> aux = new ArrayList<String>();
	    
		//Limpiamos lo que venga del form
	    if(cargosForm.getEstado().equals("empezar") && cargosForm.getEsCargosArticulosOServicios().equals("servicios"))
	    {    
	        cargosForm.reset();
            cargosForm.resetMensajes();
	    }
	    
	    //se valida que el paciente es de entidad subcontratada
	    if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
	    {
	    	aux.add("Ingreso paciente por entidad subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
	    	cargosForm.setMensajes(aux);
	    }
	    
	    //Cambio Anexo 550.****************************************************************
	    //Inicializa el Mapa Utilitario 
	    cargosForm.getMapaUtilitarioMap().clear();
	    cargosForm.setMapaUtilitarioMap(new HashMap());
	    cargosForm.setMapaUtilitarioMap("esPosibleIngresarServicio",ConstantesBD.acronimoSi);
	    cargosForm.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul",ConstantesBD.acronimoNo);
	    cargosForm.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced",ConstantesBD.acronimoNo);
	    cargosForm.setMapaUtilitarioMap("esPermitidoModificarCantidad",ConstantesBD.acronimoSi);
	    //*********************************************************************************	    
	    		
	    //cargar la informacion de la cuenta principal
		Cuenta cuenta = new Cuenta();
		cuenta.cargarCuenta(con,((CuentasPaciente)paciente.getCuentasPacienteArray(0)).getCodigoCuenta());		
		
		//CASO 1: CUENTA ABIERTA O ASOCIADA SIN CUENTA FINAL
		if((cuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaActiva+"") 
			|| (cuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaAsociada+""))
			|| (cuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial+""))
			)&&!paciente.getExisteAsocio())
		{
		    cargosForm.setCasoCuenta("CASO 1");
		    
			this.llenarForm(con, cargosForm, cuenta, paciente, user, Integer.parseInt(cuenta.getCodigoConvenio()));
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		
		//CASO 2: CUENTA ABIERTA CON ASOCIO 
		else if((cuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaActiva+"") 
				|| (cuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaAsociada+""))
				|| (cuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial+""))
				)&&paciente.getExisteAsocio())
		{
			cargosForm.setCasoCuenta("CASO 2");
			
			UtilidadBD.cerrarConexion(con);
			//página para la selección del responsable entre las cuentas asociadas
			return mapping.findForward("seleccionConvenio");
		}
	
		return null;
	}
	
	/**
	 * Método que carga la informacion en la forma
	 * @param con
	 * @param cargosForm
	 * @param cuenta
	 * @param paciente
	 * @param user
	 * @throws SQLException
	 */
	private void llenarForm(		Connection con, 
    								CargosDirectosForm cargosForm, 
    								Cuenta cuentaPpalOAsociada, 
    								PersonaBasica paciente,
									UsuarioBasico user,
									int codigoConvenioCuentaOSubcuenta) throws SQLException, IPSException
	{
	    Convenio objectConvenio= new Convenio();
	    cargosForm.setCodigoConvenioCuentaOSubcuentaSeleccionada(codigoConvenioCuentaOSubcuenta);
		objectConvenio.cargarResumen(con, codigoConvenioCuentaOSubcuenta);
		cargosForm.setAcronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada(objectConvenio.getTipoRegimen());
		cargosForm.setCodigoCentroCostoSolicita(paciente.getCodigoArea());
		cargosForm.setNombreCentroCostoSolicita(UtilidadValidacion.getNombreCentroCosto(con, cargosForm.getCodigoCentroCostoSolicita()));
		
		if(cargosForm.getFecha().equals(""))
		    cargosForm.setFecha(UtilidadFecha.getFechaActual());
		if(cargosForm.getHora().equals(""))
		    cargosForm.setHora(UtilidadFecha.getHoraActual());
		
		logger.info("cuenta--->"+cuentaPpalOAsociada.getIdCuenta());
		cargosForm.setIdCuenta(""+cuentaPpalOAsociada.getIdCuenta());
		
		if((cuentaPpalOAsociada.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoAmbulatorios+""))|| 
		    (cuentaPpalOAsociada.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoConsultaExterna+"")))
		{
		    cargosForm.setFechaAdmisionOApertura(UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.obtenerFechaAperturaCuenta(con, paciente.getCodigoCuenta())));
		    cargosForm.setHoraAdmisionOApertura(UtilidadFecha.convertirHoraACincoCaracteres(cuentaPpalOAsociada.getHoraApertura()));
		    cargosForm.setComentario("de la apertura de la cuenta");
		}
		else
		{    
		    if(cuentaPpalOAsociada.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+"") || 
		            cuentaPpalOAsociada.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
		    {
		        cargosForm.setFechaAdmisionOApertura(UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.getFechaAdmision(con, Integer.parseInt(cuentaPpalOAsociada.getIdCuenta()))));
		        cargosForm.setHoraAdmisionOApertura(UtilidadFecha.convertirHoraACincoCaracteres(UtilidadValidacion.getHoraAdmision(con, Integer.parseInt(cuentaPpalOAsociada.getIdCuenta()))));
		        cargosForm.setComentario("de la admisión");
		    }
		}	
	}
	
	/**
	 * Método que empiza el proceso de cargos directos cuando ya se ha
	 * seleccionado la cuenta o subcuenta
	 * @param con
	 * @param mapping
	 * @param cargosForm
	 * @param paciente
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarCaso2(Connection con, 
											ActionMapping mapping,
											CargosDirectosForm cargosForm,
											PersonaBasica paciente,
											UsuarioBasico user
											) throws SQLException, IPSException
	{
		Cuenta cuenta = new Cuenta();	
		
	    if(cargosForm.getCasoCuenta().equals("CASO 2"))
	    {	    	
	    	CuentasPaciente cuentasPaciente = (CuentasPaciente)paciente.getCuentasPacienteArray().get(Integer.parseInt(cargosForm.getIndicadorPosCuentas()));	    		    
	    	cuenta.cargarCuenta(con,cuentasPaciente.getCodigoCuenta());
	    	
	        if(cargosForm.getEsLaAsociada())
	        {
	        	logger.info("\n cuentaAsociada->"+cuenta.getIdCuenta()+"\n");
	            this.llenarForm(con, cargosForm, cuenta, paciente, user, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada());
	        }
	        else
	        {
	        	logger.info("\n cuenta->"+cuenta.getIdCuenta()+"\n");
	            this.llenarForm(con, cargosForm, cuenta, paciente, user, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada());
	        }
	    }
	   	UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que tiene todos los inserts necesarios para la generacion de cargos directos 
	 * @param con
	 * @param request
	 * @param mapping
	 * @param cargosForm
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws IPSException 
	 */
	@SuppressWarnings("deprecation")
	private ActionForward flujoInsertarServicios( Connection con, HttpServletRequest request, ActionMapping mapping,
			CargosDirectosForm cargosForm, int codigoCuenta, PersonaBasica paciente, UsuarioBasico user) throws SQLException, IPSException
	{
		ActionErrors errores = null;
		List<InfoResponsableCobertura>listaCoberturaCargo = null;
		try{    
			listaCoberturaCargo = new ArrayList<InfoResponsableCobertura>();
		    errores = new ActionErrors();
	    
		    for (int i=0; i<cargosForm.getNumeroFilasMapaCasoServicios(); i ++)
		    {    
		        if(!cargosForm.getServiciosArticulosMap("fueEliminadoServicio_"+i).toString().equals("true"))
		        {
		        	logger.info("\nCANTIDAD : "+cargosForm.getServiciosArticulosMap("cantidadServicios_"+i));
		        	
		        	/*
		        	 *  Modificado x Tarea 136593
		        	 *  Cuando la entidad maneja Rips, un procedimiento puede tener cantidad mayor a uno
		        	 *	y para cada uno se debe crear una solicitud  	        	 
		        	 */
		        	if (ValoresPorDefecto.getEntidadManejaRips(user.getCodigoInstitucionInt()).toString().equals(ConstantesBD.acronimoSi)){
		        		for(int g=0; g<Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("cantidadServicios_"+i)+""); g++){
		        			insertarSolicitudServicio(con, cargosForm, i,errores, codigoCuenta, paciente, user, true,listaCoberturaCargo);
		        		}
		        	}else{
		        		insertarSolicitudServicio(con, cargosForm, i, errores, codigoCuenta, paciente, user, false,listaCoberturaCargo);
		        	}
		        }
		    }  
			cargosForm.setInfoCoberturaCargoDirecto(listaCoberturaCargo);
		    
		    if(!errores.isEmpty())
		    {
		    	saveErrors(request, errores);
		    	return mapping.findForward("paginaErroresActionErrors");
		    }
		    
		    Solicitud sol= new Solicitud();
		    //sol.cargar(con, numeroSolicitud);
		    sol.cargar(con, Utilidades.convertirAEntero(cargosForm.getNumeroSolicitudGenerado()));
		    cargosForm.setConsecutivoOrdenMedica(sol.getConsecutivoOrdenesMedicas()+"");
		    
		    for (Integer numeroSolicitud: cargosForm.getListaNumerosSolicitud()){
			    sol.cargar(con, Utilidades.convertirAEntero(numeroSolicitud+""));
			    for(InfoResponsableCobertura info: listaCoberturaCargo){
			    	if(info.getDtoSubCuenta().getSolicitudesSubcuenta().get(0).getNumeroSolicitud().equals(numeroSolicitud.toString())){
			    		info.getDtoSubCuenta().getSolicitudesSubcuenta().get(0).setConsecutivoSolicitud(sol.getConsecutivoOrdenesMedicas()+"");
			    		info.getDtoSubCuenta().getSolicitudesSubcuenta().get(0).getCentroCostoEjecuta().setCodigo(sol.getCentroCostoSolicitado().getCodigo());
			    		
			    	}
			    }
		    }
		    cargosForm.setInfoCoberturaCargoDirecto(listaCoberturaCargo);
			    
		    //si llega a este punto entonces se inserta el log
		   	this.generarLogTarifasServicios(con, cargosForm, user);
		   
		    //si todo salio bien entonces 
		    this.terminarTransaccion(con);
		    //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		    
			    //Se captura la excepcion para no bloquear el flujo
			try {
				//VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
				cargarInfoVerificarGeneracionAutorizacionServicios(con,cargosForm, user, paciente, errores);
				saveErrors(request, errores);
			}catch (IPSException e) {
				Log4JManager.error(e);
				ActionMessages mensajeError = new ActionMessages();
				mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
				saveErrors(request, mensajeError);
	     	}
		     			
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("resumen");
	}

	/**
	 * 
	 * @param con 
	 * @param cargosForm
	 * @param i
	 * @param errores 
	 * @param user 
	 * @param paciente 
	 * @param codigoCuenta 
	 * @param cantidadUno 
	 */
	private void insertarSolicitudServicio(Connection con, CargosDirectosForm cargosForm, int i, ActionErrors errores,
			int codigoCuenta, PersonaBasica paciente, UsuarioBasico user, boolean cantidadUno,
			List<InfoResponsableCobertura>listaCoberturaCargo) throws IPSException  
	{
		
		int numeroSolicitud=ConstantesBD.codigoNuncaValido;
		boolean inserto=false;
		
		int temporalCodigoServicio=ConstantesBD.codigoNuncaValido;
	    int temporalCantidadServicio=ConstantesBD.codigoNuncaValido;
	    //String temporalNumeroAutorizacion="";
	    int temporalCodigoCentroCostoEjecuta=ConstantesBD.codigoNuncaValido;
	    int temporalCodigoMedicoResponde=ConstantesBD.codigoNuncaValido;
	    int temporalCodigoTipoRecargo= ConstantesBD.codigoNuncaValido;
	    int temporalCodigoPool= ConstantesBD.codigoNuncaValido;
	    int temporalCodigoEspMedResponde = ConstantesBD.codigoNuncaValido; 
		
		temporalCodigoServicio= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoServicio_"+i)+"");
		
		if(cantidadUno)
			temporalCantidadServicio=1;
		else	
			temporalCantidadServicio= Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadServicios_"+i)+"");
        
        //temporalNumeroAutorizacion=cargosForm.getServiciosArticulosMap("autorizacionServicio_"+i)+"";
        temporalCodigoCentroCostoEjecuta= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoCentroCostoEjecuta_"+i)+"");
        temporalCodigoMedicoResponde= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoMedicoResponde_"+i)+"");
        temporalCodigoTipoRecargo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoRecargo_"+i)+"");
        temporalCodigoPool= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoPoolMedico_"+i)+"");
        temporalCodigoEspMedResponde =  Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoEspMedicoResponde_"+i)+"");
        
        logger.info("codigo especialidad medico responde >>>>>>>> "+temporalCodigoEspMedResponde);
        
	    //PRIMERO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
	    try
	    { 
	        numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con, cargosForm, codigoCuenta, paciente, user, ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, temporalCodigoCentroCostoEjecuta, temporalCodigoEspMedResponde);
	        cargosForm.setNumeroSolicitudGenerado(numeroSolicitud+"");
	        
	        /*
	         * Cambio 1.50 Anexo Cargos Directos de Servicios 
	         * Se adiciona las N solicitudes generadas
	         * para entregarselas al proceso de 
	         * autorizaci�n poblaci�n capitada 
	         * @author Diana Carolina G 
	         */
	        cargosForm.getListaNumerosSolicitud().add(numeroSolicitud);
	        
	    	if(numeroSolicitud<=0)
	    	{
	    	    UtilidadBD.abortarTransaccion(con);
	    	    errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
	    	    //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	    	    //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    	}
	    }
	    catch(SQLException sqle)
	    {
	    	UtilidadBD.abortarTransaccion(con);
	        //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	        errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
	        //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    }
	    
	    try {
		    if(temporalCodigoPool>=0)
		    {    
		        inserto = this.actualizarPoolXSolicitud(con, numeroSolicitud, temporalCodigoPool);
		        if(!inserto)
		    	{
		        	UtilidadBD.abortarTransaccion(con);
		    	    //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		    	    errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
		    	    //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar el pool x medico (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    	}
		    }
	    }
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
    	    //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
    	    errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
    	    //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar el pool x medico (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		}   
	    
	    // se hace una actulizacion del medico que responde
	    try
	    {
	       inserto = this.actaulizarMedicoRespondeTransaccional(con, numeroSolicitud, temporalCodigoMedicoResponde);
	       if(!inserto)
	    	{
	    	   UtilidadBD.abortarTransaccion(con);
	    	    //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	    	    errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
	    	    //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar medico responde (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    	}
	    }
	    catch(SQLException sqle)
	    {
	    	UtilidadBD.abortarTransaccion(con);
	        //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	        errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
	        //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar medico responde (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    }
	    
	    //SE INSERTA EL USUARIO QUE HIZO EL CARGO DIR Y EL TIPO DE RECARGO
	    CargosDirectos cargo = new CargosDirectos();
	    
	    //Anexo 550*************************************************************
	    if(cargosForm.getMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul_"+i).toString().equals(ConstantesBD.acronimoSi) || 
	    		cargosForm.getMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced_"+i).toString().equals(ConstantesBD.acronimoSi))
	    	cargo.llenarMundoCargoDirectoHC(con, 
	    			user, 
	    			temporalCodigoServicio, 
	    			cargosForm.getServiciosArticulosMap("tipoServicio_"+i).toString(), 
	    			cargosForm.getMapaUtilitarioMap(),
	    			i);			    
	    //**********************************************************************
	    
	    try {
	    	inserto = this.insertarInfoCargosDirectosTransaccional(con,cargo, numeroSolicitud, user.getLoginUsuario(), temporalCodigoTipoRecargo, temporalCodigoServicio,cargosForm.getFechaEjeccuion());
		    if(!inserto)
		    {
		    	UtilidadBD.abortarTransaccion(con);
		        //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
		        //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el insertar cargos dir (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		} catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
	        errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
		}
	    
	    
	    // en caso de que sea la distibuida toca hacer el insert en las solicitudes_subcuenta
	    // esto con el fin de que las solicitudes generadas queden asociadas en la distribucion de la cuenta hija
	    //del responsable seleccionado
	    /*
	     * desaparece.
	     *
	    if(cargosForm.getEsLaDistribuida())
	    {
	        try
	        {
		        if(Integer.parseInt(cargosForm.getIdSubCuenta())>0)
		        {
		            SubCuenta subCuenta = new SubCuenta();
		            int inserciones=subCuenta.insertarSolicitudesSubcuentas(con, Integer.parseInt(cargosForm.getIdSubCuenta()), numeroSolicitud, temporalCodigoServicio,user.getCodigoInstitucion());
		            if(inserciones<=0)
		            {
		                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		    	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar las solicitudes_subcuentas (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		            }
		        }
	        }
	        catch(NumberFormatException ne)
	        {
	            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el parseInt del id de la Subcuenta (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	        }
	    }
	    */
	    inserto=this.cambiarEstadoMedicoSolicitudTransaccional(con, numeroSolicitud/*, temporalNumeroAutorizacion*/);
	    if(!inserto)
	    {
	    	UtilidadBD.abortarTransaccion(con);
	        //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	        errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
	        //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al cambiar el estado medico de la solicitud (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    }
	    
	    /*se asigna el valor de la tarifa*/
	    double valorTarifaOpcional=Double.parseDouble(cargosForm.getServiciosArticulosMap("valorUnitarioConExcepcion_"+i).toString());;
	    
	    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
	    Cargos cargos= new Cargos();
	    logger.info("***************TARIFA OPCIONAL--------->"+valorTarifaOpcional);
	    inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
	    																			user, 
	    																			paciente, 
	    																			false/*dejarPendiente*/, 
	    																			numeroSolicitud, 
	    																			ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
	    																			codigoCuenta, 
	    																			temporalCodigoCentroCostoEjecuta/*codigoCentroCostoEjecutaOPCIONAL*/, 
	    																			temporalCodigoServicio/*codigoServicioOPCIONAL*/, 
	    																			temporalCantidadServicio/*cantidadServicioOPCIONAL*/, 
	    																			valorTarifaOpcional/*valorTarifaOPCIONAL*/, 
	    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
	    																			/* "" -- numeroAutorizacionOPCIONAL*/
	    																			""/*esPortatil*/,false,
	    																			cargosForm.getFechaEjeccuion(),
	    																			"" /*subCuentaCoberturaOPCIONAL*/);
	   	
	    /**Se adiciona a cada Servicio la informacion correspondiente de la cobertura para 
	     * evaluacion posterior en la autorizacion de Capitacion sub*/
	    DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta=new DtoSolicitudesSubCuenta();
	    dtoSolicitudesSubCuenta.getServicio().setCodigo(temporalCodigoServicio+"");
	    dtoSolicitudesSubCuenta.setNumeroSolicitud(numeroSolicitud+"");
	    //dtoSolicitudesSubCuenta.setConsecutivoSolicitud(solicitud.getConsecutivoOrdenesMedicas()+"");
	    //dtoSolicitudesSubCuenta.setUrgenteSolicitud(cargosForm.geUrgente());
	    //dtoSolicitudesSubCuenta.setFinalidadSolicitud();
	    cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
	    listaCoberturaCargo.add(cargos.getInfoResponsableCoberturaGeneral());	   	
	    
	    if(!inserto)
	    {
	    	UtilidadBD.abortarTransaccion(con);
	        //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	        errores.add("error.cargosDirectos.medicamentos", new ActionMessage("error.cargosDirectos.medicamentos"));
	        //return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de generar el cargo (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    }
	    else
	    {
	    	this.validarInsertarJustificacionS(con, user, numeroSolicitud, cargosForm, temporalCodigoServicio, temporalCantidadServicio);
	    }
	    cargosForm.setServiciosArticulosMap("solicitud_"+i, numeroSolicitud);
	}

	/**
	 * Metodo que tiene todo el flujo del insert básico de la solicitud general haciendo todos los inserts
	 * (sol medicamentos - despachos) hasta llegar a la funcionalidad generacion cargos 
	 * @param con
	 * @param request
	 * @param mapping
	 * @param cargosForm
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	private ActionForward flujoInsertarMedicamentos( Connection con, HttpServletRequest request, 
			ActionMapping mapping, CargosDirectosForm cargosForm, int codigoCuenta,	PersonaBasica paciente,
																				UsuarioBasico user ) throws Exception
	{
		ActionErrors errores = null;
		try{
			errores = new ActionErrors();
		    int numeroSolicitud=ConstantesBD.codigoNuncaValido;
		    boolean inserto=false;
		    int codigoDespacho= ConstantesBD.codigoNuncaValido; 
		    String codigoCentroCostoPrincipal="";
		    
		    //PRIMERO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
		    try
		    {
		        numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con, cargosForm, codigoCuenta, paciente, user, ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos, cargosForm.getCodigoFarmacia(),ConstantesBD.codigoNuncaValido);
		        cargosForm.setNumeroSolicitudGenerado(numeroSolicitud+"");
		    	if(numeroSolicitud<=0)
		    	{
		    	    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    	}
		    }
		    catch(SQLException sqle)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    
		    if(!AlmacenParametros.manejaExistenciasNegativas(con, cargosForm.getCodigoFarmacia(), user.getCodigoInstitucionInt()))
	        {
		    	for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
		        {
		    		if(!(cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i)+"").equals("true"))
			        {
		    			//EL BLOQUEO SE HACE EN EL MOMENTO DE INSERTAR EN EL SQL BASE
			    		/*ArrayList filtro=new ArrayList();
				        filtro.add(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString());
				        filtro.add(cargosForm.getCodigoFarmacia()+"");
				        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);*/
				        
				        if(!Articulo.articuloManejaLote(con, Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()), cargosForm.getCodigoInstitucion()))
	                	{
				        	int exArticulo=Integer.parseInt(UtilidadInventarios.getExistenciasXArticulo(Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()),cargosForm.getCodigoFarmacia(),user.getCodigoInstitucionInt())+"");
					        if(Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"")>exArticulo)
				    		{
				                errores.add("error.inventarios.existenciasInsuficientes", 
				                        new ActionMessage("error.inventarios.existenciasInsuficientes", 
				                        		cargosForm.getServiciosArticulosMap("descripcionArticulo_"+i).toString(), 
				                        		cargosForm.getServiciosArticulosMap("existenciaXAlmacen_"+i).toString(), 
				                        		cargosForm.getNombreFarmacia() ));
				                cargosForm.setServiciosArticulosMap("existenciaXAlmacen_"+i,exArticulo);
				    		}
					    }
				        else
				        {
				        	if(Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"")>Integer.parseInt(cargosForm.getServiciosArticulosMap("existenciaXLote_"+i)+""))
		                    {
		                        errores.add("error.inventarios.existenciasInsuficientesLote", 
		                                new ActionMessage("error.inventarios.existenciasInsuficientesLote", 
		                                                          cargosForm.getServiciosArticulosMap("descripcionArticulo_"+i).toString(),
		                                                          cargosForm.getServiciosArticulosMap("existenciaXLote_"+i).toString(),
		                                                          cargosForm.getNombreFarmacia(),
		                                                          cargosForm.getServiciosArticulosMap("lote_"+i).toString()));
		                    }
				        }
		    		}
		        }
		    	if(!errores.isEmpty())
		    	{
		    		saveErrors(request,errores);
		    		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		    		UtilidadBD.closeConnection(con);
		    		return mapping.findForward("principal");
		    	}
	        }
		    
		    // en caso de que sea la distibuida toca hacer el insert en las solicitudes_subcuenta
		    // esto con el fin de que las solicitudes generadas queden asociadas en la distribucion de la cuenta hija
		    //del responsable seleccionado
		    /*
		     * 
		     * Esto desaparece
		    if(cargosForm.getEsLaDistribuida())
		    {
		        try
		        {
			        if(Integer.parseInt(cargosForm.getIdSubCuenta())>0)
			        {
			            SubCuenta subCuenta = new SubCuenta();
			            int inserciones=subCuenta.insertarSolicitudesSubcuentas(con, Integer.parseInt(cargosForm.getIdSubCuenta()), numeroSolicitud, Integer.parseInt(ValoresPorDefecto.getCodigoServicioFarmacia(user.getCodigoInstitucionInt())),user.getCodigoInstitucion());
			            if(inserciones<=0)
			            {
			                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			    	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar las solicitudes_subcuentas (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
			            }
			        }
		        }
		        catch(NumberFormatException ne)
		        {
		            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el parseInt del id de la Subcuenta (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		        }
		    }
		    */
		    
		    //SEGUNDO SE HACE UNA SOLICITUD DE MEDICAMENTOS BASICA
		    /*Cambios Anexo 662. Consultamos el centro de costo principal de la farmacia
		    con la intencion de guardarla en la solicitud de medicamentos*/
		    codigoCentroCostoPrincipal = Utilidades.obtenerCentroCostoPrincipal(con, cargosForm.getCodigoFarmacia()+"", user.getCodigoInstitucion());
		    logger.info("===>Centro de Costo Principal de la farmacia "+cargosForm.getCodigoFarmacia()+": "+codigoCentroCostoPrincipal);
		    inserto = this.insertarSolicitudMedicamentosTransaccional(con, numeroSolicitud, codigoCentroCostoPrincipal);
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud de medicamentos básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    
		    //TERCERO SE HACE EL INSERT DEL DETALLE DE LA SOLICITUD DE MEDICAMENTOS
		    inserto=this.insertarDetalleSolicitudMedicamentosTransaccional(con, cargosForm, numeroSolicitud, user.getCodigoInstitucionInt());
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar el detalle de la solicitud de medicamentos básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    
		    /*
		     * se suprimo el metodo insertarDescAtributosSolicitud
		    inserto= this.insertarDescAtributosSolicitud(con, numeroSolicitud, cargosForm, user);
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la desc de los atributos de la solicitud (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    */
		    
		    //CUARTO SE HACE EL INSERT DEL DESPACHO BASICO
		    codigoDespacho=this.insertarDespachoBasicoTransaccional(con, user, numeroSolicitud);
		    if(codigoDespacho<=0)
	    	{
	    	    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar el despacho básico (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    	}
		    
		    //QUINTO SE HACE EL INSERT DEL DETALLE DEL DESPACHO, aca tambien se inserta el valor costo promedio
		    inserto=this.insertarDetalleDespachoTransaccional(con, cargosForm, codigoDespacho, user, paciente);
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar el detalle del despacho básico (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    
		    //SEXTO SE HACE EL CAMBIO DEL ESTADO DE LA SOLICITUD
		    inserto=this.cambiarEstadoMedicoSolicitudTransaccional(con, numeroSolicitud/*, ""*/);
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al cambiar el estado medico de la solicitud (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    
		    //SEPTIMO SE TRATA DE GENERAR EL CARGO
		    inserto=this.generarInfoSubCuentaCargoMedicamentos(con, user, paciente, numeroSolicitud, cargosForm, codigoCuenta+"");
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de generar el cargo (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    else
		    {
		    	this.validarInsertarJustificacion(con, user, numeroSolicitud, cargosForm);
		    }
		    
		    //SE INSERTA EL USUARIO QUE HIZO EL CARGO DIRECTO Y SE LE PONE RECARGO -1
		    inserto=this.insertarInfoCargosDirectosTransaccional(con,new CargosDirectos(), numeroSolicitud, user.getLoginUsuario(), -1, -1,"");
		    if(!inserto)
		    {
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de almacenar el usuario que hizo el cargo directo (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
		    }
		    
	        //SE ACTUALIZAN LAS EXISTENCIAS DE ARTICULOS X ALMACEN
	        inserto=this.actualizarExistenciasArticulosAlmacen(con, cargosForm);
	        if(!inserto)
	        {
	            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de actualizarExistenciasArticulosAlmacen (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	        }
	        
	        //SE VERIFICA SI EL ARTICULO TIENE EXCEPCIONES DE FARMACIA, EN CASO DE SER TRUE ENTONCES
	        //SE ACTUALIZA LA EXCEPCIONES_FARMACIA_GEN (excepciones de farmacia generadas)
	        // También se debe verificar el parámetro "Generar excepciones de farmacia automáticamente"
	        
	        /// XPLANNER 37023 SEPTIEMBRE 1/08
	        /*if(UtilidadTexto.getBoolean(ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt())))
	        {
	        	inserto=this.insertarExcepcionesFarmaciaGeneradas(con, cargosForm, user, numeroSolicitud);
	            if(!inserto)
	            {
	                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de insertarExcepcionesFarmaciaGeneradas (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	            }
	        }*/
	        
	        //se generan los correspondientes logs 
	        this.generarLogTarifasArticulos(con, cargosForm, user);
	        //si todo salio bien entonces 
		    this.terminarTransaccion(con);
	        //ahora se cargan los mensajes con las existencias negativas en caso que aplique.
	        //en  el metodo se evalua que el valor por defecto de existencias negativas este en true de lo contrario llena el mapa con 
	        //el key numregostros para no sacar error en el jsp
	        this.cargarMensajesExistenciasNegativas(cargosForm);
	        this.cargarMensajesStockMaximoMinimo(cargosForm);
	        //DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	        
		        //Se captura la excepcion para no bloquear el flujo
			try {
				//VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
				cargarInfoVerificarGeneracionAutorizacionArticulos(cargosForm, user, paciente, errores);
		     	saveErrors(request, errores);
			}catch (IPSException e) {
				Log4JManager.error(e);
				ActionMessages mensajeError = new ActionMessages();
				mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
				saveErrors(request, mensajeError);
		    }
        
        }catch (Exception e) {
        	HibernateUtil.abortTransaction();
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
			
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("resumen");
	} 
			
			
	/**
	 * Método privado que inserta la solicitud básica en una transaccion, retorna el numeroSolicitud
	 * @param con
	 * @param cargosForm
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @return numeroSolicitud
	 * @throws SQLException
	 */
	private int insertarSolicitudBasicaTransaccional(	Connection con, 
			        														CargosDirectosForm cargosForm,
			        														int codigoCuenta,
			        														PersonaBasica paciente,
			        														UsuarioBasico user,
			        														int tipoSolicitud,
			        														int centroCostoSolicitado,
			        														int codigoEspMedResponde
			        													   ) throws SQLException
	{
	    Solicitud objectSolicitud= new Solicitud();
	    int numeroSolicitudInsertado=0;
	    objectSolicitud.clean();
	    objectSolicitud.setFechaSolicitud(cargosForm.getFecha());
	    objectSolicitud.setHoraSolicitud(cargosForm.getHora());
	    objectSolicitud.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
	    objectSolicitud.setEspecialidadSolicitadaOrdAmbulatorias(codigoEspMedResponde);
	    objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
	    objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
	   // objectSolicitud.setEspecialidadSolicitante(int1);

	    objectSolicitud.setCodigoMedicoResponde(Utilidades.convertirAEntero(cargosForm.getCodigoMedicoResponde()));
	    
	    //objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(UtilidadValidacion.getCodigoCentroCostoTratante(con,paciente, user.getCodigoInstitucionInt())));
	    objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
	    
	    objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(centroCostoSolicitado));
	    
	    logger.info("\n\nCuenta A insertar--->"+codigoCuenta+"**********************************************************************************\n");
	    objectSolicitud.setCodigoCuenta(codigoCuenta);
	    objectSolicitud.setCobrable(true);
	    objectSolicitud.setVaAEpicrisis(false);
	    objectSolicitud.setUrgente(false);
	    //primero lo inserto como pendiente, pero si mas adelante es exitoso el cargo entonces le hago un update a  cargada
	    objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCCargoDirecto));
	    
	  //Se agrega diagn�stico a la solicitud por Anexo Cargos Directos Articulos-1059 V 1.52 
        DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
        
        //MT5429 hermorhu
        // Validacion en caso de que el diagnostico sea el primero ingresado
        if(dtoDiagnostico != null && !dtoDiagnostico.getAcronimoDiagnostico().isEmpty() && !dtoDiagnostico.getTipoCieDiagnostico().isEmpty()) {
        	objectSolicitud.setDtoDiagnostico(dtoDiagnostico);
        	cargosForm.setDtoDiagnostico(dtoDiagnostico);
        } else {
        	if(cargosForm.getMapaUtilitarioMap("dxPrincipal_0") != null ) {
		        if(!cargosForm.getMapaUtilitarioMap("dxPrincipal_0").toString().isEmpty()) {	
		        	String[] diagnostico = cargosForm.getMapaUtilitarioMap("dxPrincipal_0").toString().split("@@@@@");
		        	DtoDiagnostico diagnosticoPrimerVez = new DtoDiagnostico();
		        	diagnosticoPrimerVez.setAcronimoDiagnostico(diagnostico[0]);
		        	diagnosticoPrimerVez.setTipoCieDiagnostico(diagnostico[1]);
		        	objectSolicitud.setDtoDiagnostico(diagnosticoPrimerVez);
		        	cargosForm.setDtoDiagnostico(diagnosticoPrimerVez);
		        }
        	}
        }
        
	    try
	    { 
	        numeroSolicitudInsertado=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.inicioTransaccion);
	    }
	    catch(SQLException sqle)
	    {
	    	sqle.printStackTrace();
	        logger.warn("Error en la transaccion del insert en la solicitud básica");
			return 0;
	    }
	    return numeroSolicitudInsertado;
	}
	
	/**
	 * Metodo que inserta la solcitud de medicamentos básica en una transacción, recibe como parámetro principal
	 * el numeroSolicitud 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private boolean insertarSolicitudMedicamentosTransaccional(Connection con, int numeroSolicitud, String codigoCentroCostoPrincipal)
	{
	    SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
	    objetoSolicitudMedicamentos.setNumeroSolicitud(numeroSolicitud);
	    objetoSolicitudMedicamentos.setObservacionesGenerales("");
	    objetoSolicitudMedicamentos.setCentroCostoPrincipal(codigoCentroCostoPrincipal);
	    int resultado=objetoSolicitudMedicamentos.insertarUnicamenteSolMedicamentosTransaccional(con);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	
   /**
	 * metodo que actualiza el medico que responde la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoMedico
	 * @return
	 * @throws SQLException
	 */
	private boolean actaulizarMedicoRespondeTransaccional(Connection con, int numeroSolicitud, int codigoMedico) throws SQLException
	{
	    Solicitud objetoSol= new Solicitud();
	    UsuarioBasico medico= new UsuarioBasico();
	    medico.cargarUsuarioBasico(con, codigoMedico);
	    int resultado = objetoSol.actualizarMedicoRespondeTransaccional(con, numeroSolicitud, medico, ConstantesBD.continuarTransaccion);
	    
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	
	/**
	 * método que inserta la info de los cargos directos
	 * @param con
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @param tipoRecargo
	 * @param fechaEjecucion 
	 * @return
	 * @throws SQLException
	 */
	private boolean insertarInfoCargosDirectosTransaccional(
			Connection con, 
			CargosDirectos cargo,
			int numeroSolicitud, 
			String loginUsuario, 
			int tipoRecargo, 
			int codigoServicioSolicitado, String fechaEjecucion) throws SQLException
	{
	    cargo.llenarMundoCargoDirecto(numeroSolicitud,loginUsuario,tipoRecargo,codigoServicioSolicitado,"",true,fechaEjecucion);
	    int resultado=cargo.insertar(con);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}

    /**
     * Inserta las excepciones de farmacia gen 
     * @param con
     * @param cargosForm
     * @param user
     * @param numeroSolicitud
     * @return
     */
	/// XPLANNER 37023 SEPTIEMBRE 1/08
	
	/*private boolean insertarExcepcionesFarmaciaGeneradas(Connection con, CargosDirectosForm cargosForm, UsuarioBasico user, int numeroSolicitud)
    {
	    GeneracionExcepcionesFarmacia mundoGEF= new GeneracionExcepcionesFarmacia();
        int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
        float porcentajeNoCubierto= 0;
        int resp=ConstantesBD.codigoNuncaValido;
        for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
        {    
            porcentajeNoCubierto=0;
            if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
            {
                try
                {
                    temporalCodigoArticulo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+"");
                }
                catch(NumberFormatException e)
                {
                    logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                    return false;
                }
                
                try
                {
                    ResultSetDecorator rs=mundoGEF.consultaExcepcionesFarmacia(con, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada(), cargosForm.getCodigoCentroCostoSolicita(), temporalCodigoArticulo, false, true);
                    if(rs.next())
                    {
                        porcentajeNoCubierto=rs.getFloat("no_cubreEF");
                        mundoGEF.setNumeroSolicitud(numeroSolicitud);
                        mundoGEF.setCodigoArticulo(temporalCodigoArticulo);
                        mundoGEF.setPorcentajeNoCubierto(porcentajeNoCubierto);
                        resp=mundoGEF.insertarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion, user.getLoginUsuario());
                        if(resp<=0)
                        {
                            logger.warn("Error en el insercion de las  excepciones de farmacia gen con el indice "+i );
                            return false;
                        }
                    }
                }
                catch(SQLException sqle)
                {
                    logger.warn("Error en el consulta de las excepciones de farmacia con el indice "+i +sqle);
                    return false;
                }
            }
        } 
        return true;
    }*/
     
    /**
	 * Método que inserta el detallle de la solicitud de medicamentos en una transaccion
	 * @param con
	 * @param cargosForm
	 * @param numeroSolicitud
	 * @return
	 */
	private boolean insertarDetalleSolicitudMedicamentosTransaccional(	Connection con, 
	        																								CargosDirectosForm cargosForm, 
	        																								int numeroSolicitud, int codigoInstitucion)
	{
	    SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
	    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
	    int cantidadDespachadaArticulo=ConstantesBD.codigoNuncaValido;
	    int resp=ConstantesBD.codigoNuncaValido; 
	    int i=0;
	    for (i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
	    {    
	        if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
	        {
	            try
	            {
	                temporalCodigoArticulo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+"");
	                cantidadDespachadaArticulo=Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"");
	            }
	            catch(NumberFormatException e)
	            {
	                logger.warn("Error en el parseInt del codigo del articulo o Cantidad con indice ="+i +"   error-->"+e);
	                return false;
	            }
	            resp=objetoSolicitudMedicamentos.insertarUnicamenteDetalleSolicitudMedicamentos(con, numeroSolicitud, temporalCodigoArticulo, ValoresPorDefecto.getNumDiasTratamientoMedicamentos(codigoInstitucion), cantidadDespachadaArticulo);
	            if (resp<1)
				{
				    logger.warn("Error en el insert del detalle de solicitud medicamentos del codigo del articulo con indice ="+i );
					return false;
				}
	        }
	    }  
	    return true;
	}
	
	/**
	 * Metodo que inserta el despacho básico en una transaccion, este metodo retorna el codigo del despacho
	 * @param con
	 * @param user
	 * @param numeroSolicitud
	 * @return codigoDespacho
	 */
	private int insertarDespachoBasicoTransaccional(Connection con, UsuarioBasico user, int numeroSolicitud)
	{
	    int codigoDespacho=ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho = new DespachoMedicamentos();
	    despacho.setUsuario(user.getLoginUsuario());
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    //este se puso en true para que pudiera generar el cargo, pero en realidad debia ser false, 
	    //esto se acordo con Margarita y Nury el 2005-07-01
	    despacho.setEsDirecto(true);
	    try
	    {
	        codigoDespacho=despacho.insertarDespachoBasicoUnicamenteTransaccional(con, ConstantesBD.continuarTransaccion);
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del despacho básico" );
	        return ConstantesBD.codigoNuncaValido;
	    }
	    return codigoDespacho;
	}
	
	/**
	 * Inserta el detalle del despacho en una transacción
	 * @param con
	 * @param cargosForm
	 * @param codigoDespacho
	 * @param user
	 * @param paciente
	 * @return
	 */
	private boolean insertarDetalleDespachoTransaccional(	Connection con, 
	        																			CargosDirectosForm cargosForm, 
	        																			int codigoDespacho, 
	        																			UsuarioBasico user, 
	        																			PersonaBasica paciente) throws IPSException
	{
	    int i=0, temporalCodigoArticulo=0, temporalCantidad;
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    int resp=ConstantesBD.codigoNuncaValido;
	    boolean insertoBienExcepcionFarmacia=false;
	    CargosEntidadesSubcontratadas cargosEntidadesSubcontratadas = new CargosEntidadesSubcontratadas();
	    for (i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
	    {    
	        if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
	        {
	            try
	            {
	                temporalCodigoArticulo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+"");
	                temporalCantidad= Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"");
	                cargosEntidadesSubcontratadas.generarCargoArticulo(con, cargosForm.getCodigoFarmacia(), Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+""), ConstantesBD.codigoNuncaValido, cargosForm.getNumeroSolicitudGenerado(), "", cargosForm.getFecha(), cargosForm.getHora(), false, user,"","");
	                resp=despacho.insertarDetalleDespachoUnicamenteTransaccional(		con, ConstantesBD.continuarTransaccion, 
	                        															temporalCodigoArticulo, 
	                        															temporalCodigoArticulo, 
	                        															codigoDespacho, 
	                        															temporalCantidad, 
	                        															cargosForm.getServiciosArticulosMap("lote_"+i).toString(),
	                        															cargosForm.getServiciosArticulosMap("fechaVencimientoLote_"+i).toString(),
	                        															"",
	                        															"",
	                        															"",
	                        															"");
	                if (resp<1)
	    			{
	    			    logger.warn("Error en el insert del detalle de despacho con indice ="+i);
	    				return false;
	    			}
	                /// XPLANNER 37023 SEPTIEMBRE 1/08
	                /*String valorPorDefecto= ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt());
	                if(valorPorDefecto.trim().equals("true"))
	                {    
		                insertoBienExcepcionFarmacia= despacho.ingresarExcepcionesFarmaciaXConvenio(con,user,paciente,temporalCodigoArticulo);
						if(!insertoBienExcepcionFarmacia)
						{
			                logger.warn("Error en la insercion de la expcepcion de farmacia con indice="+i);
			                return false;
						}
	                }*/	
	            }
	            catch(NumberFormatException e)
	            {
	                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
	                return false;
	            }
	            catch(SQLException sqle)
	            {
	                logger.warn("Error en el insert del detalle de despocho con indice ="+i +"   error-->"+sqle);
	                return false;
	            }
	        }
	    }  
	   	return true; 
	}
	
	/**
	 * Metodo que cambia el estado medico de la solicitud en una transaccion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private boolean cambiarEstadoMedicoSolicitudTransaccional(Connection con, int numeroSolicitud/*, String numeroAutorizacion*/)
	{
	    int i=0;
	    int inserto= ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    try
	    {
	       inserto =despacho.cambiarEstadoMedicoSolicitudTransaccional(con, ConstantesBD.continuarTransaccion, ConstantesBD.codigoEstadoHCCargoDirecto/*, numeroAutorizacion*/); 
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del cambiar estado de la solicitud transaccional con indice ="+i +"   error-->"+sqle);
	        return false;
	    }
	    if (inserto<1)
	    {
				return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	private boolean validarInsertarJustificacion(Connection con, UsuarioBasico user, int numeroSolicitud, CargosDirectosForm cargosForm) throws IPSException
	{
		logger.info("*********************** entro a validarInsertarJustificacion");
		int codigoArticulo=ConstantesBD.codigoNuncaValido;
		cargosForm.setJustificacionNoPosMap("numRegistros", 0);
		int advertenciasJustiNoPos=0;
		for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
	    {    
			//logger.info("*********************** 1");
	        if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
	        {
	        //	logger.info("*********************** 2");
	            try
	            {
	                codigoArticulo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+"");
	                if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoArticulo, true, false, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada()))
	        		{
	                	double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, codigoArticulo, numeroSolicitud, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada(), true);
	          //      	logger.info("*********************** 3");
	                	String tipoJus = ConstantesIntegridadDominio.acronimoInsumo;
	                	if(UtilidadInventarios.esMedicamento(con, codigoArticulo, user.getCodigoInstitucionInt()))
	                		tipoJus = ConstantesIntegridadDominio.acronimoMedicamento;
	                	
	                	if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoArticulo, Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+""), user.getLoginUsuario(), true, false, Utilidades.convertirAEntero(subcuenta+""),tipoJus))
	        			{
	        	//			logger.info("*********************** 4");
	                		advertenciasJustiNoPos++;
	        				cargosForm.setJustificacionNoPosMap("mensaje_"+i, "ARTÍCULO ["+codigoArticulo+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIÓN NO POS");
	        			}
	        		}
	            }
	            catch(NumberFormatException e)
	            {
	                logger.warn("Error no se pudo validar e insertar justificacion pendiente del codigo del articulo "+codigoArticulo+" con indice ="+i +"   error-->"+e);
	                return false;
	            }
	        }
	    }
		//cargosForm.setJustificacionNoPosMap("numRegistros", cargosForm.getNumeroFilasMapa());
		cargosForm.setJustificacionNoPosMap("numRegistros", advertenciasJustiNoPos);
		
		return true;
	}
	
	/**
	 * @param temporalCantidadServicio 
	 * 
	 */
	private boolean validarInsertarJustificacionS(Connection con, UsuarioBasico user, int numeroSolicitud, CargosDirectosForm cargosForm, int codigoServicio, int cantidadServicio) throws IPSException
	{
		if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoServicio, false, false, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada()))
		{
			double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, codigoServicio, numeroSolicitud, cargosForm.getCodigoConvenioCuentaOSubcuentaSeleccionada(), false);
			if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoServicio, cantidadServicio, user.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),ConstantesIntegridadDominio.acronimoServicio))
			{
				cargosForm.setJustificacionNoPosMap("mensaje_0", "SERVICIO [ "+codigoServicio+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACION NO POS.");
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param numeroSolicitud
	 * @param cargosForm
	 * @param codigoCuenta
	 * @return
	 */
	private boolean generarInfoSubCuentaCargoMedicamentos(Connection con, UsuarioBasico usuario, PersonaBasica paciente,
			int numeroSolicitud, CargosDirectosForm cargosForm, String codigoCuenta) throws IPSException
	{
	    boolean generoCargo=false;
	    int codigoArticulo=ConstantesBD.codigoNuncaValido;
	    int cantidadArticulo=ConstantesBD.codigoNuncaValido;
	    float valorTarifa=ConstantesBD.codigoNuncaValido;
	    float valorTarifaOriginal=ConstantesBD.codigoNuncaValido;
	    boolean tarifaNoModificada = false;
	    List<InfoResponsableCobertura> infoCoberturaMedicamento=new ArrayList<InfoResponsableCobertura>();
	    
	    for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
	    {    
	        if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
	        {
	            try
	            {
	            	Utilidades.imprimirMapa(cargosForm.getServiciosArticulosMap());
	                codigoArticulo= Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+"");
	                valorTarifa= Float.parseFloat(cargosForm.getServiciosArticulosMap("valorUnitario_"+i)+"");
	                valorTarifaOriginal= Float.parseFloat(cargosForm.getServiciosArticulosMap("valorUnitarioOriginal_"+i)+"");
	                Log4JManager.info("Valor forma: " + valorTarifa + " - Valor calculado: " + valorTarifaOriginal );
	                cantidadArticulo= Utilidades.convertirAEntero(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i).toString());
	                //generoCargoExitoso=despacho.generarCargoDespachoMedicamentosDadoValorTarifa(con,usuario, paciente, numeroSolicitud, codigoArticulo, valorTarifa);
	        	    tarifaNoModificada = (valorTarifa == valorTarifaOriginal) ? true : false; /*Evalua si la tarifa fue modificada, si no fue modificada envia true, 
	        	    de lo contrario envia false; si envia false calcula nuevamente el % para sacar el valor de la tarifa total del articulo*/
	        	    Cargos cargoArticulos= new Cargos();
	        	    
	        	    generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		con, 
																	usuario, 
																	paciente, 
																	numeroSolicitud, 
																	codigoArticulo, 
																	cantidadArticulo, 
																	false/*dejarPendiente*/, 
																	ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos/*codigoTipoSolicitudOPCIONAL*/, 
																	Utilidades.convertirAEntero(codigoCuenta)/*codigoCuentaOPCIONAL*/, 
																	cargosForm.getCodigoCentroCostoSolicita(), 
																	valorTarifa /*valorTarifaOPCIONAL*/,
																	false,cargosForm.getFecha(), tarifaNoModificada);
					/**Se adiciona a cada articulo la informacion correspondiente de la cobertura para 
					* evaluacion posterior en la autorizacion de Capitacion*/
					DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta=new DtoSolicitudesSubCuenta();
					dtoSolicitudesSubCuenta.getArticulo().setCodigo(codigoArticulo+"");
					dtoSolicitudesSubCuenta.setNumeroSolicitud(numeroSolicitud+"");
					//dtoSolicitudesSubCuenta.setConsecutivoSolicitud();
					cargoArticulos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
					infoCoberturaMedicamento.add(cargoArticulos.getInfoResponsableCoberturaGeneral());
					/**------------------------------------------------------------------------------------*/
	        	    if(!generoCargo)
	        		{
	        			logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud +" y articulo ->"+ codigoArticulo+" y valorUnitario-> "+valorTarifa);
	        			return false;
	        		}
	        	    
	        	}
	            catch(NumberFormatException e)
	            {
	                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
	                return false;
	            }
	        } 
	    }    
	    cargosForm.setInfoCoberturaCargoDirecto(infoCoberturaMedicamento);
	    return true;
	}
	
    /**
     * metodo que actualiza las existencias articulos almacen 
     * @param con
     * @param cargosForm
     * @return
     */
    private boolean actualizarExistenciasArticulosAlmacen (Connection con, CargosDirectosForm cargosForm)
    {
        boolean insertoBien= false;
        for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
        {    
            if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
            {
                try
                {
                	
                	if(!Articulo.articuloManejaLote(con, Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()), cargosForm.getCodigoInstitucion()))
                	{
	                    insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(  con, 
	                                                                                                                                                Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()),  
	                                                                                                                                                cargosForm.getCodigoFarmacia(), 
	                                                                                                                                                false, 
	                                                                                                                                                Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i).toString()), 
	                                                                                                                                                cargosForm.getCodigoInstitucion(), 
	                                                                                                                                                ConstantesBD.continuarTransaccion );
                	}
                	else
                	{
	                    insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(  con, 
                                Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()),  
                                cargosForm.getCodigoFarmacia(), 
                                false, 
                                Integer.parseInt(cargosForm.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i).toString()), 
                                cargosForm.getCodigoInstitucion(), 
                                ConstantesBD.continuarTransaccion,
                                cargosForm.getServiciosArticulosMap("lote_"+i).toString(),
                                UtilidadFecha.conversionFormatoFechaABD(cargosForm.getServiciosArticulosMap("fechaVencimientoLote_"+i).toString())
                                );
                		
                	}
                    if(!insertoBien)
                    {    
                        logger.warn("Error en el insertar el valor existencias del articulo==="+cargosForm.getServiciosArticulosMap("codigoArticulo_"+i));
                        return false;
                    }   
                }  
                catch(NumberFormatException e)
                {
                    logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                    return false;
                }
                catch(SQLException sqle)
                {
                    logger.warn("Error en el insert del valor existencias articulos solicitud con indice ="+i +"   error-->"+sqle);
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * metodo que carga el mapa para mostrar los mensajes con las existencias negativas
     * @param cargosForm
     * @return
     */
    private boolean cargarMensajesExistenciasNegativas (CargosDirectosForm cargosForm)
    {
        int numeroExistencias= ConstantesBD.codigoNuncaValido;
        String codigosArticulos="";
        String valoresExistenciasXArticulo="";
        int centinela=0;
        if(AlmacenParametros.manejaExistenciasNegativas(cargosForm.getCodigoFarmacia(), cargosForm.getCodigoInstitucion()))
        {
            for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
            {    
                if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
                {
                    try
                    {
                        numeroExistencias=UtilidadInventarios.existenciasArticuloAlmacen(Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()), cargosForm.getCodigoFarmacia(), cargosForm.getCodigoInstitucion());
                        if(numeroExistencias<0)
                        {
                        	codigosArticulos+= cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()+",";
                        	valoresExistenciasXArticulo+= numeroExistencias+",";
                            //cargosForm.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULO  ["+cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()+"] QUEDA CON EXISTENCIA NEGATIVA ["+numeroExistencias+"]");
                            //index++
                        	centinela=1;
                        }
                    }  
                    catch(NumberFormatException e)
                    {
                        logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                        return false;
                    }
                }
            }
        }
        //error.inventarios.articuloQuedaConExistenciaNegativa
        if(centinela>0)
        	cargosForm.setMensajesAdvertenciaMap("mensaje_0", "ARTICULO(S)  ["+codigosArticulos+"] QUEDA(N) CON EXISTENCIA(S) NEGATIVA(S) ["+valoresExistenciasXArticulo+"] [bz-14]");
        cargosForm.setMensajesAdvertenciaMap("numRegistros", centinela+"");
        return true;
    }
    
    
    /**
     * metodo que carga el mapa para mostrar los mensajes de los articulos 
     * que no cumplen con el stock maxiom - minimo y punto pedido
     * @param cargosForm
     * @return
     */
    private boolean cargarMensajesStockMaximoMinimo (CargosDirectosForm cargosForm)
    {
        int index=Integer.parseInt(cargosForm.getMensajesAdvertenciaMap("numRegistros")+"");
        String descripcionesArticulosStockMin="", descripcionesArticulosStockMax="", descripcionesArticulosPuntoPedido="";
        for (int i=0; i<cargosForm.getNumeroFilasMapa(); i ++)
        {    
            if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
            {
                try
                {
                    if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()), cargosForm.getCodigoInstitucion()))
                        descripcionesArticulosStockMin+= cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()+", ";
                    if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()), cargosForm.getCodigoInstitucion()))
                        descripcionesArticulosStockMax+= cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()+", ";
                    if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()), cargosForm.getCodigoInstitucion()) )
                        descripcionesArticulosPuntoPedido+= cargosForm.getServiciosArticulosMap("codigoArticulo_"+i).toString()+", ";
                }  
                catch(NumberFormatException e)
                {
                    logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                    return false;
                }
            }
        } 
        //error.inventarios.quedanConCantidad
        if(!descripcionesArticulosStockMin.equals(""))
        {    
            cargosForm.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULOS [ "+descripcionesArticulosStockMin+"] QUEDAN CON CANTIDAD MENOR AL STOCK MINIMO [bz-16]");
            index++;
        }
        //error.inventarios.quedanConCantidad
        if(!descripcionesArticulosStockMax.equals(""))
        {    
            cargosForm.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULOS [ "+descripcionesArticulosStockMax+"] QUEDAN CON CANTIDAD MAYOR AL STOCK MAXIMO [bz-16]");
            index++;
        }
        //error.inventarios.quedanConCantidad
        if(!descripcionesArticulosPuntoPedido.equals(""))
        {    
            cargosForm.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULOS [ "+descripcionesArticulosPuntoPedido+"] QUEDAN CON CANTIDAD MENOR AL PUNTO PEDIDO [bz-16]");
            index++;
        }
        cargosForm.setMensajesAdvertenciaMap("numRegistros", index+"");
        return true;
    }
    
	/**
	 * inserta la desc de los atributos de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param cargosForm
	 * @param user
	 * @return
	 */
    /*
	private boolean insertarDescAtributosSolicitud(Connection con, int numeroSolicitud, CargosDirectosForm cargosForm, UsuarioBasico user)
	{
	    SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
	    String temporalNumAutorizacion="";
	    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
	    int resultado=ConstantesBD.codigoNuncaValido;
	    int codigoAtributoNumeroAutorizacion=UtilidadValidacion.getCodNumeroAutorizacionAtributosSolicitud(con, user.getCodigoInstitucionInt());
	    
	    for (int i=0; i<cargosForm.getNumeroFilasMapa(); i++)
	    {
	        if (!(cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+i)+"").equals("true"))
	        {
	            temporalNumAutorizacion= cargosForm.getServiciosArticulosMap("autorizacionArticulo_"+i)+"";
	            temporalCodigoArticulo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoArticulo_"+i)+"");
	            try
	            {
	            	if(temporalNumAutorizacion!=null&&!temporalNumAutorizacion.equals(""))
	            	{
		                resultado=objetoSolicitudMedicamentos.insertarUnicamenteAtributoSolicitudMedicamentos(con, numeroSolicitud,temporalCodigoArticulo,  codigoAtributoNumeroAutorizacion, temporalNumAutorizacion);
		                if(resultado<=0)
		 			       return false;
	            	}
	            }
	            catch (Exception e)
	            {
	                logger.warn("Error insertando la desc_atribuots_solicitud en la solicitud= "+ numeroSolicitud);
	                return false;
	            }
	        }
	    }    
	    return true;
	}
	*/    
	/**
	 * Método que valida el acceso a la funcionalidad
	 * @param con
	 * @param paciente
	 * @param map
	 * @param req
	 * @return
	 */
	protected ActionForward validacionesAccesoUsuario( Connection con, PersonaBasica paciente, 
                                                                                    ActionMapping map, HttpServletRequest req,
                                                                                    CargosDirectosForm cargosForm, UsuarioBasico user)
	{
        try
	    {
	    	//Se verifica que el paciente se encuentre cargado en sesión
	        if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
			{
				logger.warn("Paciente no válido (null)");			
				req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
				return map.findForward("paginaError");
			}
	        
	        /*if(cargosForm.getEsCargosArticulosOServicios().equals("articulos"))
	        {	
				if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
				{
					return ComunAction.accionSalirCasoError(map, req, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
				}
	        }*/	
			
			
	        //**************************	VALIDACIONES DE LA CUENTA ACTUAL 
	        //**************************	SI ESTA EN ESTADO DE FACTURACION
	      	//**************************	o SI ESTA EN ESTADO DE DISTRICION
	        //primero se evalua si la cuenta actual esta en proceso de facturacion o en proceso de distribcion
	        Cuenta objectCuentaActual= new Cuenta();
			objectCuentaActual.cargarCuenta(con, paciente.getCodigoCuenta()+"");
			// se revisa que la cuenta se encuentre en proceso de facturación
			
			if(objectCuentaActual.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaProcesoFacturacion+""))
			{
			    logger.warn("Esta cuenta esta siendo facturada");						
				req.setAttribute("codigoDescripcionError", "error.facturacion.cuentaEnProcesoFact1");
				return map.findForward("paginaError");
			}
			// se revisa que la cuenta se encuentre en proceso de distribucion
	        else if(objectCuentaActual.getCodigoEstadoCuenta().equals(""+ConstantesBD.codigoEstadoCuentaProcesoDistribucion))
	        {
	            logger.warn("Esta cuenta esta siendo distribuida");			
	            req.setAttribute("codigoDescripcionError", "error.distribucion.cuentaEnProcesoDistricion");
	            return map.findForward("paginaError");
	        }
	        /*else if(paciente.getCodigoCuenta()==0)
	        {
	        	 logger.warn("El paciente no tiene cuenta abierta");			
		         req.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoAbierta");
		         return map.findForward("paginaError");
	        	
	        }*/
			
			logger.info("cuenta->"+paciente.getCodigoCuenta()+" cuenta asocio-->"+paciente.getCodigoCuentaAsocio());
			if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
			{
				return ComunAction.accionSalirCasoError(map, req, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
			}
			
	        /** Se revisa que la cuenta principal se encuentre cargada en sesión
	        else if(objectCuentaActual.getCodigoEstadoCuenta().equals(""))
	        {
	        	 logger.warn("El paciente no tiene cuenta cargada en sesión");			
		         req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
		         return map.findForward("paginaError");
		    }**/
	        
			//SE EVALUA EN UN SOLO PUNTO SI ALGUNA DE LAS CUENTAS CUMPLE LOS REQUISITOS DE LOS ESTADOS
			
			boolean cumpleRequisitoCuentaPpal=cuentaCumpleRequisitosEstado(objectCuentaActual.getCodigoEstadoCuenta());
			logger.warn("El estado de la cuenta ppal --> estado="+objectCuentaActual.getCodigoEstadoCuenta());
			boolean cumpleRequisitoCuentaAsocio=false;
			
			//**************************	VALIDACIONES DE LA CUENTA ASOCIADA (SIEXISTE) 
	        //**************************	SI ESTA EN ESTADO DE FACTURACION
	      	//**************************	o SI ESTA EN ESTADO DE DISTRICION
	       //Se verifica que si el paciente tiene asocio 
		    if(paciente.getExisteAsocio() && paciente.getCodigoCuentaAsocio()>0)
		    {
		        //primero se evalua si la cuenta asociada esta en proceso de facturacion o en proceso de distribcion
				Cuenta objectCuentaAsocio= new Cuenta();
		        objectCuentaAsocio.cargarCuenta(con, paciente.getCodigoCuentaAsocio()+"");
		        //La de urgencias esta en estado de facturacion
		        if(objectCuentaAsocio.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaProcesoFacturacion+""))
		        {
		            logger.warn("Esta cuenta esta siendo facturada");			
		            req.setAttribute("codigoDescripcionError", "error.facturacion.cuentaEnProcesoFact1");
		            return map.findForward("paginaError");
		        }
		        //La de urgencias está en proceso de distribucion
		        else if(objectCuentaAsocio.getCodigoEstadoCuenta().equals(""+ConstantesBD.codigoEstadoCuentaProcesoDistribucion))
		        {
		        	logger.warn("Esta cuenta esta siendo distribuida");			
		            req.setAttribute("codigoDescripcionError", "error.distribucion.cuentaEnProcesoDistricion");
		            return map.findForward("paginaError");
		        }
		        // Se revisa que la cuenta principal se encuentre cargada en sesión
				else if(objectCuentaAsocio.getCodigoEstadoCuenta().equals(""))
		        {
		        	 logger.warn("El paciente no tiene cuenta cargada en sesión");			
			         req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			         return map.findForward("paginaError");
			    }
				
		        cumpleRequisitoCuentaAsocio=cuentaCumpleRequisitosEstado(objectCuentaAsocio.getCodigoEstadoCuenta());
		        logger.warn("El estado de la cuenta asociada --> estado="+objectCuentaAsocio.getCodigoEstadoCuenta());
				/*if(!cumpleRequisitoCuentaAsocio)
				{
				    logger.warn("El estado de la cuenta asociada --> estado="+objectCuentaAsocio.getCodigoEstadoCuenta()+" no es un estado para ingresar a esta funcionalidad");			
			        req.setAttribute("codigoDescripcionError", "error.cargosDirectos.estadoInvalido");
			        return map.findForward("paginaError");
				}*/
		    }
            
		    if(!cumpleRequisitoCuentaPpal && !cumpleRequisitoCuentaAsocio)
			{
		    	logger.warn("El estado de la cuenta no es un estado para ingresar a esta funcionalidad");			
		        req.setAttribute("codigoDescripcionError", "error.cargosDirectos.estadoInvalido");
		        return map.findForward("paginaError");
			}
		    
		    
            // se valida que el area (centro de costo) en el cual esta ubicado el paciente cargado en sesion,
            //se encuentre definido en la parametrizacion de la funcionalidad transacciones validas por centro costo
            if(cargosForm.getEsCargosArticulosOServicios().equals("articulos"))
            {    
                if(ValoresPorDefecto.getCodigoTransSoliPacientes(user.getCodigoInstitucionInt(), true).trim().equals(""))
                {
                    ActionErrors errores=new ActionErrors();
                    errores.add("error.inventarios.sinDefinirTipoTransaccion", new ActionMessage("error.inventarios.sinDefinirTipoTransaccion", "SOLICITUDES PACIENTE"));
                    logger.warn("entra al error de [error.inventarios.sinDefinirTipoTransaccion] ");
                    saveErrors(req, errores);
                    UtilidadBD.closeConnection(con);
                    return map.findForward("paginaErroresActionErrors");
                }
                else
                {    
                    Vector restricciones= new Vector();
                    restricciones.add(0, ValoresPorDefecto.getCodigoTransSoliPacientes(user.getCodigoInstitucionInt(), true));
                    HashMap transaccionesValidasCCMap=UtilidadInventarios.transaccionesValidasCentroCosto( user.getCodigoInstitucionInt(), paciente.getCodigoArea(), restricciones, true);
                    if(Integer.parseInt(transaccionesValidasCCMap.get("numRegistros").toString())<=0) 
                    {
                        ActionErrors errores=new ActionErrors();
                        errores.add("error.inventarios.transaccionNoValidaCentroCosto", new ActionMessage("error.inventarios.transaccionNoValidaCentroCosto", paciente.getArea()));
                        logger.warn("entra al error de [error.inventarios.transaccionNoValidaCentroCosto] Area-->"+paciente.getCodigoArea());
                        saveErrors(req, errores);
                        UtilidadBD.closeConnection(con);
                        return map.findForward("paginaErroresActionErrors");
                    }
                }    
            }   
 	    }
	    catch(Exception e)
	    {
	        logger.warn("Error en las validaciones de acceso al usuario "+e.toString());
	        req.setAttribute("codigoDescripcionError", "error.facturacion.cuentaEnProcesoFact");
			return map.findForward("paginaError");
	    }
		return null;
	}
	
	/**
	 * metodo que indica si la cuenta cargada en sesion si puede acceder
	 * @param codigoCuenta
	 * @return
	 */
	private boolean cuentaCumpleRequisitosEstado(String codigoCuenta)
	{
	    if(	codigoCuenta.equals(ConstantesBD.codigoEstadoCuentaActiva+"")
		        || codigoCuenta.equals(ConstantesBD.codigoEstadoCuentaAsociada+"")
				|| codigoCuenta.equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial+""))
	    {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Método que termina la transaccion
	 * @param con
	 * @throws SQLException
	 */
	public void terminarTransaccion(Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    myFactory.endTransaction(con);
	}
	
	/**
	 * Método que actualiza el pool x solciitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPool
	 * @return
	 * @throws SQLException
	 */
	private boolean actualizarPoolXSolicitud(Connection con, int numeroSolicitud, int codigoPool) throws SQLException
	{
	    Solicitud solciitud= new Solicitud();
	    int resultado= solciitud.actualizarPoolSolicitud(con, numeroSolicitud, codigoPool);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
    
	/**
	 * Método para la generacion de los logs cuando se modifica la tarifa
	 * @param cargosForm
	 * @param usuario
	 * @param indice
	 */
	private void generarLogTarifasServicios(Connection con, CargosDirectosForm cargosForm, UsuarioBasico usuario)
    {
		String log;
		if(!UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con, usuario.getLoginUsuario(), 377).trim().equals(""))
	    {
			for (int indice=0; indice<cargosForm.getNumeroFilasMapaCasoServicios(); indice ++)
		    {    
		        if(!cargosForm.getServiciosArticulosMap("fueEliminadoServicio_"+indice).toString().equals("true"))
		        {
					if(!cargosForm.getServiciosArticulosMap("valorUnitarioConExcepcionOriginal_"+indice).toString().equals(cargosForm.getServiciosArticulosMap("valorUnitarioConExcepcion_"+indice).toString()))
					{	
				        log="\n           ======INFORMACION TARIFA CARGOS DIRECTOS SERVICIOS===== " +
				        "\n*  Número Solicitud [" +cargosForm.getNumeroSolicitudGenerado()+"] " +
				        "\n*  Servicio ["+cargosForm.getServiciosArticulosMap("codigoServicio_"+indice)+"] " +
				        "\n*  Tarifa Inicial ["+cargosForm.getServiciosArticulosMap("valorUnitarioConExcepcionOriginal_"+indice)+"]" +
				        "\n*  Tarifa Modificada ["+cargosForm.getServiciosArticulosMap("valorUnitarioConExcepcion_"+indice)+"]"+
				        "\n*  Institución ["+usuario.getCodigoInstitucion()+"]";
				        log+="\n========================================================\n\n\n " ;
				        LogsAxioma.enviarLog(ConstantesBD.logCargosDirectosServiciosCodigo, log, ConstantesBD.tipoRegistroLogInsercion,usuario.getInformacionGeneralPersonalSalud());
					}
		        }
		    }   
	    }	
    }
	
	/**
	 * Método para la generacion de los logs cuando se modifica la tarifa
	 * @param cargosForm
	 * @param usuario
	 * @param indice
	 */
	private void generarLogTarifasArticulos(Connection con, CargosDirectosForm cargosForm, UsuarioBasico usuario)
    {
		String log;
		if(!UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con, usuario.getLoginUsuario(), 378).trim().equals(""))
	    {
			for (int indice=0; indice<cargosForm.getNumeroFilasMapa(); indice ++)
		    {    
		        if(!cargosForm.getServiciosArticulosMap("fueEliminadoArticulo_"+indice).toString().equals("true"))
		        {
					if(!cargosForm.getServiciosArticulosMap("valorUnitarioOriginal_"+indice).toString().equals(cargosForm.getServiciosArticulosMap("valorUnitario_"+indice).toString()))
					{	
				        log="\n           ======INFORMACION TARIFA CARGOS DIRECTOS ARTICULOS===== " +
				        "\n*  Número Solicitud [" +cargosForm.getNumeroSolicitudGenerado()+"] " +
				        "\n*  Artículo ["+cargosForm.getServiciosArticulosMap("codigoArticulo_"+indice)+"] " +
				        "\n*  Tarifa Inicial ["+cargosForm.getServiciosArticulosMap("valorUnitarioOriginal_"+indice)+"]" +
				        "\n*  Tarifa Modificada ["+cargosForm.getServiciosArticulosMap("valorUnitario_"+indice)+"]"+
				        "\n*  Institución ["+usuario.getCodigoInstitucion()+"]";
				        log+="\n========================================================\n\n\n " ;
				        LogsAxioma.enviarLog(ConstantesBD.logCargosDirectosArticulosCodigo, log, ConstantesBD.tipoRegistroLogInsercion,usuario.getInformacionGeneralPersonalSalud());
					}
		        }
		    }   
	    }	
    }
	
	
	//***************************************************************************************************************************************
	//Requerimientos Anexo 550.**************************************************************************************************************
	
	/**
	 * Validaciones para los Servicios
	 * @param Connection con
	 * @param CargosDirectosForm cargosForm
	 * @param UsuarioBasico usuario
	 * */
	private void validacionesServicioXManejoRips(Connection con,CargosDirectosForm forma,UsuarioBasico usuario)
	{
		//Recorre los servicios
		int i = forma.getNumeroFilasMapaCasoServicios()-1;
		logger.info("INDICE ERROR-->"+i);
		
		
		//se puede ingresar mas de un servicio por solicitud
		forma.setMapaUtilitarioMap("esPosibleIngresarServicio",ConstantesBD.acronimoSi);
		forma.getArrayUtilitario1().add(i,new ArrayList());
		forma.getArrayUtilitario2().add(i,new ArrayList());
		
		forma.setMapaUtilitarioMap("esPermitidoModificarCantidad_"+i,ConstantesBD.acronimoSi);
		forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul_"+i,ConstantesBD.acronimoNo);
		forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced_"+i,ConstantesBD.acronimoNo);
		forma.setMapaUtilitarioMap("esRequeridaInfoRips",ConstantesBD.acronimoNo);
		
		//Valida que el indicador de Entidad Maneja Rips se encuentre en SI
		if(ValoresPorDefecto.getEntidadManejaRips(usuario.getCodigoInstitucionInt()).toString().equals(ConstantesBD.acronimoSi))
		{					
			//Inicializa los indicadores		
			forma.setMapaUtilitarioMap("esRequeridaInfoRips",UtilidadTexto.getBoolean(ValoresPorDefecto.getRequeridaInfoRipsCagosDirectos(usuario.getCodigoInstitucionInt()))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						
			if(!UtilidadTexto.getBoolean(forma.getServiciosArticulosMap("fueEliminadoServicio_"+i).toString()))
			{		
				if(!forma.getMapaUtilitarioMap().containsKey("seccionrips_"+i))
					forma.setMapaUtilitarioMap("seccionrips_"+i,ConstantesBD.acronimoNo);					
				
				//Evalua si el tipo de Servicio es Consulta					
				if(forma.getServiciosArticulosMap("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioInterconsulta+""))
				{
					forma.setMapaUtilitarioMap("esPermitidoModificarCantidad_"+i,ConstantesBD.acronimoNo);
					forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul_"+i,ConstantesBD.acronimoSi);
					forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced_"+i,ConstantesBD.acronimoNo);						
						
					//Actualiza la cantidad del servicio					
					if(forma.getServiciosArticulosMap("cantidadServicios_"+i).toString().equals(""))
					{
						forma.setServiciosArticulosMap("cantidadServicios_"+i,"1");
						forma.setMapaUtilitarioMap("esPermitidoModificarCantidad_"+i,ConstantesBD.acronimoNo);
					}
						
					inicializarDatosInfoRips(con, forma,1,usuario.getCodigoInstitucionInt(),i);
					
					//Carga la información del parametro por defecto para las causas externas
					forma.setMapaUtilitarioMap("codigoCausaExterna_"+i,ValoresPorDefecto.getCausaExterna(usuario.getCodigoInstitucionInt()));					
					//Carga la información del parametro por defecto para la finalidad de la consulta
					forma.setMapaUtilitarioMap("codigoFinalidadConsulta_"+i,ValoresPorDefecto.getFinalidad(usuario.getCodigoInstitucionInt()));
					forma.setMapaUtilitarioMap("codigoTipoDiagnosticoPrincipal_"+i,"");					
				}
					
				//Evalua si el tipo de Servicio es Procedimiento
				else if (forma.getServiciosArticulosMap("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioProcedimiento+""))
				{
					forma.setMapaUtilitarioMap("esPermitidoModificarCantidad_"+i,ConstantesBD.acronimoSi);
					forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced_"+i,ConstantesBD.acronimoSi);
					forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul_"+i,ConstantesBD.acronimoNo);
					
					//Actualiza la cantidad del servicio					
					if(forma.getServiciosArticulosMap("cantidadServicios_"+i).toString().equals(""))
					{
						forma.setServiciosArticulosMap("cantidadServicios_"+i,"1");
						forma.setMapaUtilitarioMap("esPermitidoModificarCantidad_"+i,ConstantesBD.acronimoSi);
					}
					
					inicializarDatosInfoRips(con,forma,2,usuario.getCodigoInstitucionInt(),i);
				}
			}					
		}
	}	
	
	//****************************************************************************************************************************
	
	/**
	 * Inicializa la información para mostrar el formulario de datos de RIPS
	 * @param Connection con
	 * @param CargosDirectosForm forma
	 * @param String tipoServicio
	 * @param int institucion
	 * */
	private void inicializarDatosInfoRips(Connection con,CargosDirectosForm forma,int tipoServicio,int institucion,int pos) 
	{
		if(forma.getArrayUtilitario1().size() <= 0)		
			forma.setArrayUtilitario1(new ArrayList(20));
		
		if(forma.getArrayUtilitario2().size() <= 0)
			forma.setArrayUtilitario2(new ArrayList(20));

		switch (tipoServicio) 
		{
			case 1: //Consultas
				
				//Carga los datos para el formulario de la informacion de RIPS					
				forma.getArrayUtilitario1().add(pos,UtilidadesHistoriaClinica.obtenerCausasExternas(con,false));	
				forma.getArrayUtilitario2().add(pos,UtilidadesHistoriaClinica.obtenerFinalidadesConsulta(con));
				
				forma.setMapaUtilitarioMap("fechaCP_"+pos,forma.getFecha());				
				forma.setMapaUtilitarioMap("codigoCausaExterna_"+pos,ValoresPorDefecto.getCausaExterna(institucion));
				forma.setMapaUtilitarioMap("codigoFinalidadConsulta_"+pos,ValoresPorDefecto.getFinalidad(institucion));					
				
				if(!forma.getMapaUtilitarioMap().containsKey("numDiagnosticosDefinitivos_"+pos) || 
						forma.getMapaUtilitarioMap("numDiagnosticosDefinitivos_"+pos).toString().equals(""))
				{
					forma.setMapaUtilitarioMap("numDiagnosticosRelacionados_"+pos,"0");
					forma.setMapaUtilitarioMap("numDiagnosticosDefinitivos_"+pos,"0");
				}
				
			break;
			
			case 2 : //Procedimientos				
				forma.getArrayUtilitario1().add(pos,Utilidades.obtenerFinalidadesServicio(
						con,
						Integer.parseInt(forma.getServiciosArticulosMap("codigoServicio_"+pos).toString()), 
						institucion));
				
				forma.setMapaUtilitarioMap("fechaCP_"+pos,forma.getFecha());
				forma.setMapaUtilitarioMap("codigoFinalidadProcedimiento_"+pos,forma.getFecha());				
				
				if(!forma.getMapaUtilitarioMap().containsKey("numDiagnosticosDefinitivos_"+pos) || 
						forma.getMapaUtilitarioMap("numDiagnosticosDefinitivos_"+pos).toString().equals(""))
				{
					forma.setMapaUtilitarioMap("numDiagnosticosRelacionados_"+pos,"0");
					forma.setMapaUtilitarioMap("numDiagnosticosDefinitivos_"+pos,"0");				
				}
				
			break;	
		}
	}
	
	//****************************************************************************************************************************
	/**
	 * Reinicia los valores para el servicio
	 * @param CargosDirectosForm forma
	 * **/
	private void metodoEstadoEliminarServicio(CargosDirectosForm forma,UsuarioBasico usuario)
	{
		//Valida que el indicador de Entidad Maneja Rips se encuentre en SI
		if(ValoresPorDefecto.getEntidadManejaRips(usuario.getCodigoInstitucionInt()).toString().equals(ConstantesBD.acronimoSi))
		{	
			//Inicializa los indicadores			
			forma.setMapaUtilitarioMap(new HashMap());
			forma.setMapaUtilitarioMap("esPosibleIngresarServicio",ConstantesBD.acronimoSi);
			forma.setMapaUtilitarioMap("esPermitidoModificarCantidad",ConstantesBD.acronimoSi);
			forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul",ConstantesBD.acronimoNo);
			forma.setMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced",ConstantesBD.acronimoNo);
			forma.setMapaUtilitarioMap("numDiagnosticosRelacionados","0");
			forma.setMapaUtilitarioMap("numDiagnosticosDefinitivos","0");
		}
	}
	//****************************************************************************************************************************
	
	/**
	 * Realiza validaciones sobre los datos al guardar servicios de Procedimientos y Consultas 
	 * @param Connection con
	 * @param CargosDirectosForm forma
	 * @param UsuarioBasico usuario
	 * @param String fechaIngresoPaciente
	 * */
	private ActionErrors validacionesGuardarInfoRIPS(
			Connection con,
			CargosDirectosForm forma,
			UsuarioBasico usuario,
			String fechaIngresoPaciente)
	{
		ActionErrors errores = new ActionErrors();
		boolean validarRequerido = false;
		String temp = "";
		
		//Utilidades.imprimirMapa(forma.getServiciosArticulosMap());
		//Utilidades.imprimirMapa(forma.getMapaUtilitarioMap());
		
		//Valida que el indicador de Entidad Maneja Rips se encuentre en SI
		if(ValoresPorDefecto.getEntidadManejaRips(usuario.getCodigoInstitucionInt()).toString().equals(ConstantesBD.acronimoSi))				
		{
			validarRequerido = UtilidadTexto.getBoolean(ValoresPorDefecto.getRequeridaInfoRipsCagosDirectos(usuario.getCodigoInstitucionInt()));			
			
			for (int i=0; i<forma.getNumeroFilasMapaCasoServicios(); i++)
		    { 					
				 if(!forma.getServiciosArticulosMap("fueEliminadoServicio_"+i).toString().equals("true"))
				 {				
					//Evalua que tipo de información se solicito dependiendo del tipo de servicio
					if(forma.getMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul_"+i).toString().equals(ConstantesBD.acronimoSi))
					{		
						temp = "Consulta";												
						
						//Validación de la Causa Externa
						if((forma.getMapaUtilitarioMap("codigoCausaExterna_"+i).toString().equals("") || 
								forma.getMapaUtilitarioMap("codigoCausaExterna_"+i).toString().equals("0")) &&
									validarRequerido)				
							errores.add("descripcion",new ActionMessage("errors.required","La Causa Externa del Registros Nro. "+(i+1)));
						
						//Validación de la Finalidad de la Consulta
						if(forma.getMapaUtilitarioMap("codigoFinalidadConsulta_"+i).toString().equals("")	&& validarRequerido)				
							errores.add("descripcion",new ActionMessage("errors.required","La Finalidad de la Consulta del Registro Nro. "+(i+1)));
						
						//Validación del diagnostico principal
						if(forma.getMapaUtilitarioMap("dxPrincipal_"+i).toString().equals("") && validarRequerido)				
							errores.add("descripcion",new ActionMessage("errors.required","El Diagnostico Principal del Registro Nro. "+(i+1)));
						
						//Validación del Tipo de Diagnostico Principal
						if(forma.getMapaUtilitarioMap("codigoTipoDiagnosticoPrincipal_"+i).toString().equals("") && validarRequerido)				
							errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Diagnostico Principal del Registro Nro. "+(i+1)));
					}
					else if (forma.getMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced_"+i).toString().equals(ConstantesBD.acronimoSi))
					{
						temp = "Procedimiento";
						
						//Validación de la Finalidad del Procedimiento
						if(forma.getMapaUtilitarioMap("codigoFinalidadProcedimiento_"+i).toString().equals("") && validarRequerido)				
							errores.add("descripcion",new ActionMessage("errors.required","La Finalidad del Procedimiento del Registro Nro. "+(i+1)));
					}			
					
					if(forma.getMapaUtilitarioMap("esPermitidoMostrarInfoRipsConsul_"+i).toString().equals(ConstantesBD.acronimoSi) || 
							forma.getMapaUtilitarioMap("esPermitidoMostrarInfoRipsProced_"+i).toString().equals(ConstantesBD.acronimoSi))
					{
					
						//Validación de la fecha de Consulta
						if(forma.getMapaUtilitarioMap("fechaCP_"+i).toString().equals("") && validarRequerido)				
							errores.add("descripcion",new ActionMessage("errors.required","Fecha de "+temp+" del Registro Nro. "+(i+1)));
			
						//Validación del formato de la fecha de Consulta				
						if(!forma.getMapaUtilitarioMap("fechaCP_"+i).toString().equals("") 
								&& !UtilidadFecha.validarFecha(forma.getMapaUtilitarioMap("fechaCP_"+i).toString()))				
							errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de "+temp+" "+forma.getMapaUtilitarioMap("fechaCP_"+i).toString()+" del Registro Nro. "+(i+1)));
						else
						{	
							//Valida que la fecha sea menor igual que la fecha del sistema				
							if(UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",forma.getMapaUtilitarioMap("fechaCP_"+i).toString(),"00:00").isTrue())
							{
								//Valida que la fecha sea menor igual a la fecha del ingreso del paciente
								if(!UtilidadFecha.compararFechas(forma.getMapaUtilitarioMap("fechaCP_"+i).toString(),"00:00",fechaIngresoPaciente,"00:00").isTrue())
								{
									errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," de "+temp+" "+forma.getMapaUtilitarioMap("fechaCP_"+i).toString()+" del Registro Nro. "+(i+1),"de Ingreso del Paciente "+fechaIngresoPaciente));
								}
							}					
							else
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," de "+temp+" "+forma.getMapaUtilitarioMap("fechaCP_"+i).toString()+" del Registro Nro. "+(i+1)," Actual "+UtilidadFecha.getFechaActual()));
						}
					}
				 }
		    }
		}
		
		return errores;
	}
	//***************************************************************************************************************************************
	//***************************************************************************************************************************************
	
	
	// Cambios Segun Anexo 809
	private ActionForward accionFiltrarEspProfResponde(Connection con,CargosDirectosForm cargosForm,HttpServletResponse response) 
	{
		logger.info(" >>>  ENTRO A FILTRAR ESPECIALIDADES MEDICO RESPONDE ");
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoEspMedicoResponde_"+cargosForm.getIndice()+"</id-select>" +
				"<id-arreglo>especialidades</id-arreglo>" +
			"</infoid>" ;
		
		//consultar especialidades medicos
		logger.info("indice >>>>"+cargosForm.getIndice());
		if(cargosForm.getEspecialidadProfesionalesEjecutanArray().get(cargosForm.getIndice())!=null)
		{
			cargosForm.getEspecialidadProfesionalesEjecutanArray().set(cargosForm.getIndice(),
					UtilidadesOrdenesMedicas.obtenerEspecialidadProfesionalEjecutan(
							Utilidades.convertirAEntero(cargosForm.getCodigoMedicoResponde())));
		}
		else
		{
			cargosForm.getEspecialidadProfesionalesEjecutanArray().add(cargosForm.getIndice(),
				UtilidadesOrdenesMedicas.obtenerEspecialidadProfesionalEjecutan(
						Utilidades.convertirAEntero(cargosForm.getCodigoMedicoResponde())));
		}
		
		/*resultado += "<especialidades>";
		resultado += "<codigo></codigo>";
		resultado += "<descripcion>Seleccione</descripcion>";
		resultado += "</especialidades>";	
		*/
		for(int i=0; i< cargosForm.getEspecialidadProfesionalesEjecutanArray().get(cargosForm.getIndice()).size();i++)
		{
				resultado += "<especialidades>";
				resultado += "<codigo>"+cargosForm.getEspecialidadProfesionalesEjecutanArray().get(cargosForm.getIndice()).get(i).getCodigo()+"</codigo>";
				resultado += "<descripcion>"+cargosForm.getEspecialidadProfesionalesEjecutanArray().get(cargosForm.getIndice()).get(i).getNombre()+"</descripcion>";
			    resultado += "</especialidades>";	
			
		}
		resultado += "</respuesta>";
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.reset();
			response.resetBuffer();
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().flush();			
	        response.getWriter().write(resultado);
	        response.getWriter().close();
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarUnidadesAgenda: "+e);
		}
		return null;
	}
	// Fin Cambios Segun Anexo 809
	
	/**
	 * M�todo que se encarga de validar si se generar autorizacion para el articulo de la orden.
	 * DCU 1059 - Cargos Directos de Articulos v2.1
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param citaForm
	 * @param usuario
	 * @param paciente
	 * @param listaDtoAdministrarSolicitudesAutorizar
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacionArticulos(CargosDirectosForm forma,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		InventarioFacade inventarioFacade 	= null;
		ClaseInventarioDto claseInventarioDto	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
			inventarioFacade		= new InventarioFacade();
			
			if(forma.getInfoCoberturaCargoDirecto()!=null && !forma.getInfoCoberturaCargoDirecto().isEmpty()){
				
				dtoSubCuenta = forma.getInfoCoberturaCargoDirecto().get(0).getDtoSubCuenta();
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(dtoSubCuenta.getConvenio().getCodigo());
				convenioDto.setNombre(dtoSubCuenta.getConvenio().getNombre());
				if(dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(dtoSubCuenta.getContrato());
				contratoDto.setNumero(dtoSubCuenta.getNumeroContrato());
				
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(forma.getNumeroSolicitudGenerado()+""));
				ordenAutorizacionDto.setConsecutivoOrden(dtoSubCuenta.getSolicitudesSubcuenta().get(0).getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(forma.getCodigoFarmacia());
				//ordenAutorizacionDto.setEsPyp(forma.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenCargoDirecto);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos);
				listaArticulosPorAutorizar	= null;
				listaArticulosPorAutorizar  = ordenesFacade.obtenerMedicamentosInsumosPorAutorizar(Utilidades.convertirAEntero(forma.getNumeroSolicitudGenerado()));
				for (MedicamentoInsumoAutorizacionOrdenDto articulo : listaArticulosPorAutorizar) {
					articulo.setAutorizar(true);
					claseInventarioDto	= inventarioFacade.obtenerClaseInventarioPorSubGrupo(articulo.getSubGrupoInventario());
					if(claseInventarioDto!=null){
						articulo.setClaseInventario(claseInventarioDto.getCodigo());
						articulo.setGrupoInventario(claseInventarioDto.getCodigoGrupo());
					}
				}
				ordenAutorizacionDto.setEsUrgente(false);
				ordenAutorizacionDto.setMedicamentosInsumosPorAutorizar(listaArticulosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
				
				boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;
				
				datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
				datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
				datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
				datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
				datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
				datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
				datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
				datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
				datosPacienteAutorizar.setCuentaAbierta(true);
				datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
				datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
				
				montoCobroAutorizacion	= new MontoCobroDto();
				montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
				montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
				montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
				montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
				montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
				montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
				
				autorizacionCapitacionDto = new AutorizacionCapitacionDto();
				autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
				autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
				autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
				autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
				autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
				autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
				autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
	    		
				//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
				manejoPacienteFacade = new ManejoPacienteFacade();
				listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
			}
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * M�todo que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 134 - Cargos Directos de Servicios v2.1
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacionServicios(Connection con,CargosDirectosForm cargosForm,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		DtoSolicitudesSubCuenta dtoSolicitudSubCuenta = null; 
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
						
			dtoSubCuenta =  cargosForm.getInfoCoberturaCargoDirecto().get(0).getDtoSubCuenta();
			
			for(InfoResponsableCobertura infoCoberturaSer : cargosForm.getInfoCoberturaCargoDirecto()){
				
				dtoSolicitudSubCuenta = infoCoberturaSer.getDtoSubCuenta().getSolicitudesSubcuenta().get(0);
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(infoCoberturaSer.getDtoSubCuenta().getConvenio().getCodigo());
				convenioDto.setNombre(infoCoberturaSer.getDtoSubCuenta().getConvenio().getNombre());
				if(infoCoberturaSer.getDtoSubCuenta().getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						infoCoberturaSer.getDtoSubCuenta().getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(infoCoberturaSer.getDtoSubCuenta().getContrato());
				contratoDto.setNumero(infoCoberturaSer.getDtoSubCuenta().getNumeroContrato());
				
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(dtoSolicitudSubCuenta.getNumeroSolicitud()+""));
				ordenAutorizacionDto.setConsecutivoOrden(dtoSolicitudSubCuenta.getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(dtoSolicitudSubCuenta.getCentroCostoEjecuta().getCodigo());
				//ordenAutorizacionDto.setEsPyp(cargosForm.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenCargoDirecto);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudCargosDirectosServicios);
				
				//Se consultan datos del servicio
				listaServiciosPorAutorizar = null;
				listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
						ordenAutorizacionDto.getClaseOrden(), ordenAutorizacionDto.getTipoOrden());
				listaServiciosPorAutorizar.get(0).setFinalidad(dtoSolicitudSubCuenta.getFinalidadSolicitud());
				long cantidad = 1;
				listaServiciosPorAutorizar.get(0).setCantidad(cantidad);
				listaServiciosPorAutorizar.get(0).setAutorizar(true);
				if(dtoSolicitudSubCuenta.isUrgenteSolicitud()){
					ordenAutorizacionDto.setEsUrgente(true);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
				}else{
					ordenAutorizacionDto.setEsUrgente(false);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
				}
				
				ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
			}	
			boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;
			
			datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
			datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
			datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
			datosPacienteAutorizar.setCuentaAbierta(true);
			datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
			datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
			
			montoCobroAutorizacion	= new MontoCobroDto();
			montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
			montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
			montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
			montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
			montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
			montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
			
			autorizacionCapitacionDto = new AutorizacionCapitacionDto();
			autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
			autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
			autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
			autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
			autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
			autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
			autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
			
			//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty()){//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			UtilidadBD.finalizarTransaccion(con);
		}catch (IPSException ipsme) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
}