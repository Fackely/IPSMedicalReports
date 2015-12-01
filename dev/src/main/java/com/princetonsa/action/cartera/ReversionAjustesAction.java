/*
 * Created on 2/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cartera.ReversionAjustesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AjustesEmpresa;
import com.princetonsa.mundo.cartera.AprobacionAjustesEmpresa;
import com.princetonsa.pdf.AjustesPdf;

/**
 * @author artotor
 *
 * Clase para manejar el flujo de la funcionalidad
 */
public class ReversionAjustesAction extends Action 
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Servicios
	 */
	private Logger logger = Logger.getLogger(ReversionAjustesAction.class); 
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
	{
		Connection con = null;
		try{
		if(form instanceof ReversionAjustesForm)
		{
			
		    
			con = openDBConnection(con);
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			ReversionAjustesForm reversionAjustesForm=(ReversionAjustesForm) form;
			AjustesEmpresa mundo=new AjustesEmpresa();
			HttpSession sesion = request.getSession();
			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			
			String estado = reversionAjustesForm.getEstado();
			logger.warn("[ReversionajustesAction]-->Estado: "+estado);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo estado is NULL");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				reversionAjustesForm.reset();
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("buscarPorAjuste"))
			{
				accionBuscarAjustePorCodigo(con,reversionAjustesForm,mundo);
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("buscarPorFecha"))
			{
				this.accionBuscarAjustePorFecha(con,reversionAjustesForm,mundo);
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("cargarAjusteSeleccionado"))
			{
				reversionAjustesForm.setTipoAjusteBusqueda(reversionAjustesForm.getAjustes("tipoajuste_"+reversionAjustesForm.getRegSeleccionado())+"");
				reversionAjustesForm.setNumeroAjusteBusquedaStr(reversionAjustesForm.getAjustes("consecutivoajuste_"+reversionAjustesForm.getRegSeleccionado())+"");
				this.accionBuscarAjustePorCodigo(con,reversionAjustesForm,mundo);
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("detalleAjuste"))
			{
				cerrarConexion(con);
				return mapping.findForward("detalleAjuste");
			}
			else if(estado.equals("confimacionReversar"))
			{
				reversionAjustesForm.setConfimacionReversion(true);
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("volverPrincipal"))
			{
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("imprimir"))
			{
				return this.imprimirDetalleAjuste(con,reversionAjustesForm,mapping,request,usuario);   
			}
			else if(estado.equals("reversar"))
			{
				return this.accionReversarAjuste(con,reversionAjustesForm,mundo,mapping,request,usuario);
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible");
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
	
	private ActionForward imprimirDetalleAjuste(Connection con, ReversionAjustesForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		AjustesPdf.imprimirDetalleAjusteConsulta(con,ValoresPorDefecto.getFilePath()+ nombreArchivo, forma.getCodigoAjuste()+"", usuario, request);
		UtilidadBD.closeConnection(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Reversion Ajustes");
		return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Metodo que realiza la busqueda de los ajustes aprobados en una fecha y los carga en un mapa.
	 * @param con
	 * @param reversionAjustesForm
	 * @param mundo
	 */
	private void accionBuscarAjustePorFecha(Connection con, ReversionAjustesForm reversionAjustesForm, AjustesEmpresa mundo) 
	{
		logger.info("\n entre a accionBuscarAjustePorFecha");
		mundo.setFechaAjuste(reversionAjustesForm.getFechaAjusteBusqueda());
		mundo.setEstado(ConstantesBD.codigoEstadoCarteraAprobado);
		reversionAjustesForm.setAjustes(mundo.buscarAjustesAprobadosPorFechaParaReversion(con));
		if(Integer.parseInt(reversionAjustesForm.getAjustes("numeroregistros")+"")==1)
		{
			reversionAjustesForm.setNumeroAjusteBusquedaStr(reversionAjustesForm.getAjustes("consecutivoajuste_0")+"");
			reversionAjustesForm.setTipoAjusteBusqueda(reversionAjustesForm.getAjustes("tipoajuste_0")+"");
			this.accionBuscarAjustePorCodigo(con,reversionAjustesForm,mundo);
		}
	}

	/**
	 * Metodo que genera la reversion de un aujuste.
	 * @param con
	 * @param reversionAjustesForm
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionReversarAjuste(Connection con, ReversionAjustesForm reversionAjustesForm, AjustesEmpresa mundo, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		try
		{
			boolean enTransaccion=false;
			//enTransaccion=UtilidadBD.iniciarTransaccion(con);
			{
				ArrayList filtro = new ArrayList();
				filtro.add(reversionAjustesForm.getCodigoAjuste()+"");
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteGeneralDeterminado,filtro);
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteFacturaDeterminado,filtro);
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteDetFacturaDeterminado,filtro);
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteAsocioDetFacturaDeterminado,filtro);
				String[] estadoAjuste=Utilidades.obtenerestadoAjuste(reversionAjustesForm.getCodigoAjuste()).split(ConstantesBD.separadorSplit);
				if(Integer.parseInt(estadoAjuste[0])!=ConstantesBD.codigoEstadoCarteraAprobado)
				{
					ActionErrors errores=new ActionErrors();
					errores.add("AJUSTE DIFERENTE GENERADO",new ActionMessage("error.cartera.ajustes.ajusteNoSeEncuentraGenerado"));
					saveErrors(request, errores);
			    	UtilidadBD.abortarTransaccion(con);
			    	UtilidadBD.closeConnection(con);	   	        
			    	return mapping.findForward("paginaErroresActionErrors");  
				}
				enTransaccion=accionGenerarAjuste(con,reversionAjustesForm,mundo,usuario);
				AprobacionAjustesEmpresa mundoAprob=new AprobacionAjustesEmpresa();
				mundoAprob.setInstitucion(reversionAjustesForm.getInstitucion());
				mundoAprob.setNumAjuste(mundo.getCodigo());//el codigo del ajuste
				mundoAprob.setFechaAprobacion(UtilidadFecha.conversionFormatoFechaABD(reversionAjustesForm.getFechaReversion()));
				mundoAprob.setUsuario(usuario.getLoginUsuario());
				mundoAprob.consultaAjustePorCodigo(con);     
				enTransaccion=mundoAprob.generarAprobacionTrans(con,true);
			}
			//cargar resumen desde la BD para acergurar que todo quedo bien, cuando se implemente la impresion, aqu debe cargarse la informacion a imprimir.
			if(enTransaccion)
				UtilidadBD.finalizarTransaccion(con);
			else
				UtilidadBD.abortarTransaccion(con);
			cerrarConexion(con);
			if(enTransaccion)
				return mapping.findForward("ajusteReversado");
			else
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error al reversar el ajuste, CAUSA: El Ajuste ya fué reversado",  "Error al reversar el ajuste, CAUSA: El Ajuste ya fué reversado", false);
		}
		catch(Exception e)
		{
			//otras cosas pueden generar esta excepcion
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error al reversar el ajuste, CAUSA: El Ajuste ya fué reversado",  "Error al reversar el ajuste, CAUSA: El Ajuste ya fué reversado", false);
		}
			
	}

	/**
	 * Metodo que genera un ajuste. teniendo cargada en reversionForm la informacion
	 * del encabezado del ajuste y un mapa con la informacion de las factura del ajuste.
	 * el metodo consulta el ajuste a nivel de servicio para realizar su reversion.
	 * @param con
	 * @param reversionAjustesForm
	 * @param mundo
	 * @param usuario
	 * @param request 
	 */
	private boolean accionGenerarAjuste(Connection con, ReversionAjustesForm reversionAjustesForm, AjustesEmpresa mundo, UsuarioBasico usuario) 
	{
		boolean enTransaccion=false;
		if(reversionAjustesForm.getTipoAjuste().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
		{
			reversionAjustesForm.setTipoAjusteNuevo(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo());
			reversionAjustesForm.setNombreConsecutivo(ConstantesBD.nombreConsecutivoAjustesDebito);
			reversionAjustesForm.setNomTipoAjusteNuevo(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getNombre());
		}
		else
		{
			reversionAjustesForm.setTipoAjusteNuevo(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo());
			reversionAjustesForm.setNombreConsecutivo(ConstantesBD.nombreConsecutivoAjustesCredito);
			reversionAjustesForm.setNomTipoAjusteNuevo(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getNombre());
		}
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(reversionAjustesForm.getNombreConsecutivo(),reversionAjustesForm.getInstitucion());
		reversionAjustesForm.setConsecutivoAjusteNuevo(consecutivo);
		enTransaccion=(!reversionAjustesForm.getConsecutivoAjusteNuevo().equals(ConstantesBD.codigoNuncaValido+""));
		if(enTransaccion)
		{
			reversionAjustesForm.setFechaElaboracion(UtilidadFecha.getFechaActual());
			reversionAjustesForm.setHoraElaboracion(UtilidadFecha.getHoraActual());
			reversionAjustesForm.setEstadoAjuste(ConstantesBD.codigoEstadoCarteraGenerado);
			mundo.setNombreConsecutivo(reversionAjustesForm.getNombreConsecutivo());
			mundo.setConsecutivoAjuste(reversionAjustesForm.getConsecutivoAjusteNuevo());
			this.cargarAjusteGeneralToMundo(reversionAjustesForm,mundo,usuario);
			enTransaccion=mundo.cambiarAtributoReversion(con,true);
			mundo.setInstitucion(usuario.getCodigoInstitucionInt());
			enTransaccion=mundo.insertarAjusteReversion(con);
		}
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,reversionAjustesForm.getNombreConsecutivo(), reversionAjustesForm.getInstitucion(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		return enTransaccion;
	}
	
	/**
	 * Metodo para pasar la informacion general de un ajuste al mundo.
	 * @param ajustesForm
	 * @param mundo
	 */
	private void cargarAjusteGeneralToMundo(ReversionAjustesForm reversionAjustesForm, AjustesEmpresa mundo,UsuarioBasico usuario)
	{
		if(reversionAjustesForm.getTipoAjusteNuevo().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
		{
			if(Utilidades.obtenerEstadoCuentaCobro(Integer.parseInt(reversionAjustesForm.getCuentaCobroStr()),reversionAjustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada)
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteCreditoCuentaCobro);
			}
			else
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteCreditoFactura);
			}
		}
		else if(reversionAjustesForm.getTipoAjusteNuevo().equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
		{
			if(Utilidades.obtenerEstadoCuentaCobro(Integer.parseInt(reversionAjustesForm.getCuentaCobroStr()),reversionAjustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada)
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteDebitoCuentaCobro);
			}
			else
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteDebitoFactura);
			}			
		}
		mundo.setFechaAjuste(reversionAjustesForm.getFechaReversion());
		mundo.setFechaElaboracion(reversionAjustesForm.getFechaElaboracion());
		mundo.setHoraElaboracion(reversionAjustesForm.getHoraElaboracion());
		mundo.setUsuario(usuario.getLoginUsuario());
		mundo.setObservaciones("Reversion Ajuste "+(mundo.getTipoAjuste()==ConstantesBD.codigoAjusteDebitoFactura?"Credito ":"Debito ")+reversionAjustesForm.getConsecutivoAjuste()+"\n"+reversionAjustesForm.getObservacionesReversion());
		mundo.setEstado(reversionAjustesForm.getEstadoAjuste());
		mundo.setAjusteResversado(false);
		mundo.setCodAjusteReversado(reversionAjustesForm.getCodigoAjuste());
	}


	/**
	 * Metodo que carga un ajuste.
	 * se debe tener cargado en el form el consecutivo del ajuste, el tipo de ajuste y la institucion.
	 * @param con
	 * @param reversionAjustesForm
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private void accionBuscarAjustePorCodigo(Connection con, ReversionAjustesForm reversionAjustesForm, AjustesEmpresa mundo) 
	{
		mundo.setConsecutivoAjuste(reversionAjustesForm.getNumeroAjusteBusquedaStr());
		mundo.setTipoAjusteStr(reversionAjustesForm.getTipoAjusteBusqueda());
		mundo.setInstitucion(reversionAjustesForm.getInstitucion());
		this.buscarYCargarAjuste(con,reversionAjustesForm,mundo);
	}
	
	/**
	 * Metodo que busca el encabezado de un ajuste APROBADO y lo carga en el formo.
	 * Es requerido que se tenga cargado en el mundo el numero de ajuste (consecutivo) 
	 * tipoAjuste(debito-credito) y la institicuion
	 * @param con
	 * @param reversionAjustesForm
	 * @param mundo
	 */
	private void buscarYCargarAjuste(Connection con, ReversionAjustesForm reversionAjustesForm, AjustesEmpresa mundo) 
	{
		logger.info("\n entre a buscarYCargarAjuste");
		mundo.setEstado(ConstantesBD.codigoEstadoCarteraAprobado);
		mundo.cargarEncabezadoAjuste(con);
		if(mundo.getCodigo()!=ConstantesBD.codigoNuncaValido)
		{
			reversionAjustesForm.setInformacionCargada(true);
			reversionAjustesForm.setCodigoAjuste(mundo.getCodigo());
			reversionAjustesForm.setConsecutivoAjuste(reversionAjustesForm.getNumeroAjusteBusquedaStr());
			reversionAjustesForm.setTipoAjuste(reversionAjustesForm.getTipoAjusteBusqueda());
			reversionAjustesForm.setCastigoCartera(mundo.isCastigoCartera());
			reversionAjustesForm.setConceptoCastigoCartera(mundo.getConceptoCastigoCartera()==null?ConstantesBD.codigoNuncaValido+"":mundo.getConceptoCastigoCartera());
			reversionAjustesForm.setFechaAjuste(mundo.getFechaAjuste());
			reversionAjustesForm.setCuentaCobro(mundo.getCuentaCobro());
			if(reversionAjustesForm.getCuentaCobro()>0)
			{
				reversionAjustesForm.setAjusteCuentaCobro(true);
				reversionAjustesForm.setConvenio(Utilidades.obtenerConvenioCuentaCobro(con,reversionAjustesForm.getCuentaCobro(),reversionAjustesForm.getInstitucion()));
				//carga el mapa con las facutras del ajuste
				reversionAjustesForm.setFacturas(mundo.cargarDistribucionAjusteFacturasCuentaCobro(con,reversionAjustesForm.getCodigoAjuste()));
			}
			else
			{
				reversionAjustesForm.setAjusteCuentaCobro(false);
				reversionAjustesForm.setCodigoFactura(Utilidades.obtenerFacturaAjuste(con,reversionAjustesForm.getCodigoAjuste()));
				reversionAjustesForm.setConsecutivoFactura(Utilidades.obtenerConsecutivoFactura(con,reversionAjustesForm.getCodigoFactura()));
				reversionAjustesForm.setConvenio(Utilidades.obtenerConvenioFactura(con,reversionAjustesForm.getCodigoFactura()));
				reversionAjustesForm.setSaldo(Utilidades.obtenerSaldoFactura(con,reversionAjustesForm.getCodigoFactura()));
				String[] temp=Utilidades.obtenerCentroAtencionFactura(con,reversionAjustesForm.getCodigoFactura());
				reversionAjustesForm.setCodigoCentroAtencion(Integer.parseInt(temp[0]));
				reversionAjustesForm.setNombreCentroAtencion(temp[1]);
			}
			reversionAjustesForm.setConceptoAjuste(Utilidades.obtenerDescripcionConceptoAjuste(con,mundo.getConceptoAjuste(),reversionAjustesForm.getInstitucion()));
			reversionAjustesForm.setMetodoAjuste(Utilidades.obtenerDescripcionMetodoAjuste(con,mundo.getMetodoAjuste()));
			reversionAjustesForm.setValorAjuste(mundo.getValorAjuste());
			reversionAjustesForm.setObservaciones(mundo.getObservaciones());
			reversionAjustesForm.setFechaAprobacionAjuste(mundo.getFechaAprobacionAjuste());
		}
	}



	/**
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
		return con;
					
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			}
			catch(Exception e)
			{
			    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
				return null;
			}
					
			return con;
	}
		 
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);
	        }
	    }
	    catch(Exception e){
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
