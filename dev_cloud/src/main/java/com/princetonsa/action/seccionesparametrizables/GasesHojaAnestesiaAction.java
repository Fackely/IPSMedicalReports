package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.actionform.seccionesparametrizables.GasesHojaAnestesiaForm;
import com.princetonsa.dto.salas.DtoGases;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.GasesHojaAnestesia;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class GasesHojaAnestesiaAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(GasesHojaAnestesiaAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof GasesHojaAnestesiaForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			GasesHojaAnestesiaForm forma =(GasesHojaAnestesiaForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en GASES es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de GASES (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarGases") || estado.equals("resumen"))
    			{
    				return this.accionEmpezarGases(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardarGases(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("nuevoGas"))
    			{
    				return this.accionNuevoGas(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificarGas"))
    			{
    				return this.accionModificarGas(forma, mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de GASES");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    		}
    		return null;
    	} catch (Exception e) {
    		Log4JManager.error(e);
    		return null;
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    }

	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezarGases(GasesHojaAnestesiaForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapa( GasesHojaAnestesia.cargarGasHojaAnestesia(con, forma.getNumeroSolicitud(), forma.getCodigoGasInstCC(), forma.getCodigoGas()));
		validacionMostrarInfo(forma,forma.getMapa());
		forma.setDtoGases((DtoGases)forma.getMapa("DTOGAS"));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("gases");
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void postularFechasHorasActuales(GasesHojaAnestesiaForm forma) 
	{
		if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaInicialActualizada()))
			forma.setFechaInicialActualizada(UtilidadFecha.getFechaActual());
		if(!UtilidadFecha.validacionHora(forma.getHoraInicialActualizada()).puedoSeguir)
			forma.setHoraInicialActualizada(UtilidadFecha.getHoraActual());
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarGases(GasesHojaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		HashMap<Object, Object> mapaGas= llenarMapaInsercion(forma, usuario);
		
		logger.info("\n ESTA EN BD-->"+forma.getEstaBD()+"\n");
		
		if(!forma.getEstaBD())
			GasesHojaAnestesia.insertar(con, mapaGas);
		else
			GasesHojaAnestesia.modificar(con, mapaGas);
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		
		return this.accionEmpezarGases(forma,null, mapping, con, usuario);
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private HashMap<Object, Object> llenarMapaInsercion(GasesHojaAnestesiaForm forma, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> mapaEvento= new HashMap<Object, Object>();
		mapaEvento.put("numerosolicitud", forma.getNumeroSolicitud());
		mapaEvento.put("codigogasinstcc", forma.getCodigoGasInstCC());
		mapaEvento.put("fechainicial", forma.getFechaInicialActualizada());
		mapaEvento.put("horainicial", forma.getHoraInicialActualizada());
		
		mapaEvento.put("suspendido", forma.getSuspendidoActualizado());
		mapaEvento.put("cantidadlitros", forma.getCantidadLitrosActualizada());
		if(forma.getDtoGases().getLlevaGasAnestesico())
			mapaEvento.put("gasanestesico", forma.getGasAnestesicoActualizada());
		else
			mapaEvento.put("gasanestesico", ConstantesBD.codigoNuncaValido);
		if(forma.getDtoGases().getLlevaFio2())
			mapaEvento.put("fio2", forma.getFio2Actualizada());
		else
			mapaEvento.put("fio2", ConstantesBD.codigoNuncaValido);
		
		mapaEvento.put("graficar", forma.getGraficarActualizado());
		mapaEvento.put("loginusuario", usuario.getLoginUsuario());
		mapaEvento.put("codigogashojaanestesia", forma.getCodigoGasHojaAnestesia());
		
		mapaEvento.put("numRegistros", Integer.parseInt(forma.getMapa("numRegistros")+"")+1);
		
		return mapaEvento;
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoGas(GasesHojaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.resetNuevo();
		postularFechasHorasActuales(forma);
		forma.setEstaBD(false);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("gases");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarGas(GasesHojaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		llenarCamposModificar(forma, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("gases");
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void llenarCamposModificar(GasesHojaAnestesiaForm forma, UsuarioBasico usuario) 
	{
		forma.setFechaInicialActualizada(forma.getMapa("fechainicial_"+forma.getIndex())+"");
		forma.setHoraInicialActualizada(forma.getMapa("horainicial_"+forma.getIndex())+"");
		forma.setCantidadLitrosActualizada(forma.getMapa("cantidad_litros_"+forma.getIndex())+"");
		forma.setFio2Actualizada(forma.getMapa("fio2_"+forma.getIndex())+"");
		forma.setGasAnestesicoActualizada(Integer.parseInt(forma.getMapa("gas_anestesico_"+forma.getIndex())+""));
		forma.setSuspendidoActualizado(forma.getMapa("suspendido_"+forma.getIndex())+"");
		forma.setGraficarActualizado(forma.getMapa("graficar_"+forma.getIndex())+"");
		forma.setCodigoGasHojaAnestesia(Integer.parseInt( forma.getMapa("codigogashojaanestesia_"+forma.getIndex())+""));
		forma.setEstaBD(true);
	}
	
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param GasesHojaAnestesiaForm forma 
	 * */
	private void validacionMostrarInfo(GasesHojaAnestesiaForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Gases Hoja Anestesia");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{						
			logger.info("no paso validaciones mostrar informacion. Gases Hoja Anestesiea. mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
			return;	
		}		
	}
	
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param GasesHojaAnestesiaForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(GasesHojaAnestesiaForm forma,HttpServletRequest request)
	{
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(request.getParameter("esSinEncabezado")+"");
		else
			forma.setEsSinEncabezado(ConstantesBD.acronimoNo);
		
		if (!(request.getParameter("mostrarDatosInfoActivo")+"").equals("") && !(request.getParameter("mostrarDatosInfoActivo")+"").equals("null"))
			forma.setMostrarDatosInfoActivo(UtilidadTexto.getBoolean(request.getParameter("mostrarDatosInfoActivo")+""));
		else
			forma.setMostrarDatosInfoActivo(false);
		
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarMenu")+""));
		else
			forma.setOcultarMenu(false);
	}
}