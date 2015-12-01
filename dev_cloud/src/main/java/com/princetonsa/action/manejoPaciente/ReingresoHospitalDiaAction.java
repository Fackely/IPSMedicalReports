package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ReingresoHospitalDiaForm;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ReingresoHospitalDia;

/**
 * 
 * @author wilson
 *
 */
public class ReingresoHospitalDiaAction extends Action  
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ReingresoHospitalDiaAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try{
    		if (response==null); 
    		if(form instanceof ReingresoHospitalDiaForm)
    		{

    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    			ReingresoHospitalDiaForm forma =(ReingresoHospitalDiaForm)form;
    			String estado=forma.getEstado();
    			logger.warn("\nEl estado en Reingreso Salida es------->"+estado+"\n");

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt()); 
    				logger.warn("Estado no valido dentro del flujo de Reingreso Hospital Dia  (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{
    				return this.accionEmpezar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("busqueda"))
    			{
    				return this.accionBusqueda(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("ordenar"))
    			{
    				return this.accionOrdenarMapa(con, forma, mapping);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardar(forma, mapping, con, usuario, request);
    			}
    			else
    			{
    				forma.reset(usuario.getCodigoInstitucionInt()); 
    				logger.warn("Estado no valido dentro del flujo de Consultorios -> "+estado);
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
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
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
    private ActionForward accionGuardar(ReingresoHospitalDiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
    {
    	logger.info("observaciones->"+forma.getObservaciones());
    	//1 CARGAMOS EL PACIENTE EN SESION
    	PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
        persona.setCodigoPersona(Integer.parseInt(forma.getMapa("codigopaciente_"+forma.getIndexSeleccionado())+""));
        try 
        {
			persona.cargar(con,Integer.parseInt(forma.getMapa("codigopaciente_"+forma.getIndexSeleccionado())+""));
			persona.cargarPaciente(con, Integer.parseInt(forma.getMapa("codigopaciente_"+forma.getIndexSeleccionado())+""), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		} 
        catch (NumberFormatException e) 
		{
			e.printStackTrace();
		} 
        catch (SQLException e) 
		{
			e.printStackTrace();
		}
        
        if(ReingresoHospitalDia.existeReingresoOSalidaActivo(con, Integer.parseInt(forma.getMapa("cuenta_"+forma.getIndexSeleccionado()).toString()), ConstantesIntegridadDominio.acronimoReingreso,ConstantesBD.codigoNuncaValido))
        {
        	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error ya existe ingreso", "error.manejoPaciente.existeReingreso", true);
        }
        	
        //2. INICIAMOS LA TRANSACCION
    	UtilidadBD.iniciarTransaccion(con);
    	
    	//2. SE MODIFICA EL ESTADO DEL INGRESO A ABIERTO
    	if(!IngresoGeneral.actualizarEstadoIngreso(con, forma.getMapa("numeroingreso_"+forma.getIndexSeleccionado()).toString(), ConstantesIntegridadDominio.acronimoEstadoAbierto, usuario.getLoginUsuario()))
    	{
    		UtilidadBD.abortarTransaccion(con);
    		logger.info("no actualizo el ingreso a cerrado");
    		UtilidadBD.closeConnection(con);
    		return mapping.findForward("listadoReingreso");
    	}
    	
    	//3. SE INSERTA LA INFORMACION 
    	DtoReingresoSalidaHospiDia dto= cargarDto(forma, usuario);
    	
    	if(!ReingresoHospitalDia.insertar(con, dto))
    	{
    		UtilidadBD.abortarTransaccion(con);
    		logger.info("no actualizo el ingreso a cerrado");
    		UtilidadBD.closeConnection(con);
    		return mapping.findForward("listadoReingreso");
    	}
    	
    	UtilidadBD.finalizarTransaccion(con);
    	logger.info("inserto 100% la transaccion de Reingreso Hospital Dia");
    	
    	HashMap criteriosBusqueda= armarCriteriosBusqueda(forma, usuario); 
    	forma.setMapa(ReingresoHospitalDia.listadoPacientesReingresoOSalida(con, criteriosBusqueda));
    	
    	//return null;
        UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoReingreso");
    	
	}

    /**
     * 
     * @param forma
     * @param usuario
     * @return
     */
	private DtoReingresoSalidaHospiDia cargarDto(ReingresoHospitalDiaForm forma, UsuarioBasico usuario) 
	{
		DtoReingresoSalidaHospiDia dto= new DtoReingresoSalidaHospiDia();
		dto.setCuenta(forma.getMapa("cuenta_"+forma.getIndexSeleccionado())+"");
		dto.setFechaIngreso(UtilidadFecha.getFechaActual());
		dto.setHoraIngreso(UtilidadFecha.getHoraActual());
		dto.setIngreso(forma.getMapa("numeroingreso_"+forma.getIndexSeleccionado())+"");
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		dto.setLoginUsuarioIngreso(usuario.getLoginUsuario());
		logger.info("observaciones action->"+forma.getObservaciones());
		dto.setObservacionesIngreso(forma.getObservaciones());
		dto.setPaciente(new InfoDatosInt(Integer.parseInt(forma.getMapa("codigopaciente_"+forma.getIndexSeleccionado())+""), forma.getMapa("nombrepaciente_"+forma.getIndexSeleccionado())+""));
		dto.setTipo(ConstantesIntegridadDominio.acronimoReingreso);
		return dto;
	}

	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
    private ActionForward accionBusqueda(ReingresoHospitalDiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
    {
    	HashMap criteriosBusqueda= armarCriteriosBusqueda(forma, usuario);
    	forma.setMapa(ReingresoHospitalDia.listadoPacientesReingresoOSalida(con, criteriosBusqueda));
    	logger.info("sale del listado "+new Date());
		UtilidadBD.closeConnection(con);
    	logger.info("cierra la conexión "+new Date());
		return mapping.findForward("listadoReingreso");
	}

    /**
     * 
     * @param forma
     * @param usuario key{codigoCentroAtencion, esSalida, tipoIdentificacion, numeroIdentificacion, primerNombre, primerApellido}
     * @return
     */
	private HashMap armarCriteriosBusqueda(ReingresoHospitalDiaForm forma, UsuarioBasico usuario) 
	{
		HashMap criteriosBusqueda= new HashMap();
		criteriosBusqueda.put("codigoCentroAtencion", usuario.getCodigoCentroAtencion());
		criteriosBusqueda.put("esSalida", "false");
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
    private ActionForward accionOrdenarMapa(Connection con, ReingresoHospitalDiaForm forma, ActionMapping mapping) 
    {
    	logger.info("\n Mapa Antes Ordenar->"+forma.getMapa()+"\n");
    	String[] indices= {"codigoreingresosalida_", "numeroingreso_", "consecutivoingreso_", "fechahoraingreso_", "fechahoraingresobd_", "nombrepaciente_", "tipoid_", "numeroid_", "fechaultima_", "fechaultimabd_", "codigopaciente_", "cuenta_"};
		int numReg=Integer.parseInt(forma.getMapa("numRegistros")+"");
		forma.setMapa(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapa(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapa("numRegistros",numReg+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoReingreso");
	}

	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(ReingresoHospitalDiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.inicializarTagMap(usuario.getCodigoInstitucionInt());
		//forma.setMapa(ReingresoHospitalDia.listadoPacientesReingresoOSalida(con, false/*esSalida*/, usuario.getCodigoCentroAtencion()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoReingreso");
	}
    
}