/**
 * Juan David Ramírez 30/06/2006
 * Princeton S.A.
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
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
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.capitacion.AjustesCxCForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.AjusteCxC;

/**
 * @author Juan David Ramírez
 *
 */
public class AjustesCxCAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(AjustesCxCAction.class);

	/**
	 * Control del flujo de la funcionalidad
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof AjustesCxCForm)
		{
			con=UtilidadBD.abrirConexion();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			AjustesCxCForm forma=(AjustesCxCForm)form;
			String estado=forma.getEstado();
			
			logger.warn("Estado [AjustesCxCAction]--> "+estado);
			if(estado.equalsIgnoreCase("empezar"))
			{
				return accionEmpezar(con, mapping, forma, request, usuario);
			}
			if(estado.equalsIgnoreCase("buscarAjuste"))
			{
				return accionBuscarAjuste(con, mapping, forma, usuario.getCodigoInstitucionInt());
			}
			if(estado.equalsIgnoreCase("buscarCuentaCobro"))
			{
				return accionBuscarCuentaCobro(con,mapping, forma, usuario, request);
			}
			if(estado.equalsIgnoreCase("guardar"))
			{
				return accionGuardar(con, mapping, forma, usuario, request);
			}
			if(estado.equalsIgnoreCase("anular"))
			{
				return accionAnular(con, mapping, forma, usuario, request);
			}
			if(estado.equalsIgnoreCase("guardarDetalle"))
			{
				return accionGuardarDetalle(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
			}
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.estadoInvalido", "errors.estadoInvalido", true);
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.formaTipoInvalido", "errors.formaTipoInvalido", true);
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	/**
	 * Flujo guardar el detalle de los ajustes
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(Connection con, ActionMapping mapping, AjustesCxCForm forma, HttpServletRequest request, int institucion)
	{
		HashMap detalleAjustes=forma.getDetalleCargues();
		int numRegistros=Integer.parseInt(detalleAjustes.get("numRegistros")+"");
		if(numRegistros>0)
		{
			UtilidadBD.iniciarTransaccion(con);
			AjusteCxC mundo=new AjusteCxC();
			mundo.setCodigo(forma.getCodigo());
			mundo.eliminarDetalleAjustes(con);
			for(int i=0; i<numRegistros; i++)
			{
				boolean ajustar=UtilidadTexto.getBoolean((String)detalleAjustes.get("ajustar_"+i));
				if(ajustar)
				{
					int codigo=Integer.parseInt(detalleAjustes.get("codigo_"+i)+"");
					double valor=Double.parseDouble((String)detalleAjustes.get("valor_"+i));
					String conceptoDetalle=(String)detalleAjustes.get("concepto_"+i);
					int resultado=mundo.detalleAjuste(con, codigo, valor, conceptoDetalle, institucion, forma.getModificando());
					if(resultado<1)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
					}
				}
			}
			forma.setDetalleCargues(mundo.consultarDetalleAjustes(con, forma.getCodigo(), true));
			UtilidadBD.finalizarTransaccion(con);
		}
		forma.setModificando(true);
		return hacerMapping(con, mapping, "detalle");
	}

	/**
	 * Flujo de anulación del ajuste
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionAnular(Connection con, ActionMapping mapping, AjustesCxCForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		AjusteCxC mundo=new AjusteCxC();
		int resultado=mundo.anular(con, forma.getCodigo(), forma.getMotivoAnulacion(), usuario.getLoginUsuario());
		if(resultado<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		mundo.reset();
		String resultadoAjuste=mundo.cargarAjuste(con, forma.getCodigo());
		if(UtilidadCadena.noEsVacio(resultadoAjuste))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoAjuste, resultadoAjuste, true);
		}
		llenarForm(mundo, forma);
		forma.setDetalleCargues(mundo.consultarDetalleAjustes(con, forma.getCodigo(), false));
		forma.setEstado("resumen");
		return hacerMapping(con, mapping, "resumen");
	}

	/**
	 * Método para guardar el ajuste
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, AjustesCxCForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		AjusteCxC mundo=new AjusteCxC();
		
		//--------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------------------------
		//--- Se debe verificar La Fecha de Generacion Ajuste.
		String descCierre = mundo.verificarFechaCierre(con, 0, forma.getFecha(), usuario.getCodigoInstitucionInt());
		
		
		//-- si no existe no se verfica .
		if (UtilidadCadena.noEsVacio(descCierre) && UtilidadCadena.noEsVacio(forma.getFecha()))
		{
			String []cadA =  forma.getFecha().split("/");
			String []cadC =  descCierre.split("-");
			int mesA = 0, anioA = 0, mesC = 0, anioC = 0;
			
			mesA = Integer.parseInt(cadA [1]);	anioA = Integer.parseInt(cadA [2]);
			mesC = Integer.parseInt(cadC [1]);	anioC = Integer.parseInt(cadC [0]);
			
			
			//-- Validar que la fecha de ajuste sea mayor que la fecha Cierre inicial de Cartera.
			if( 
				(anioA < anioC) ||
				( (anioA == anioC) && (mesC >= mesA) ) 
			  )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.fechaCierreMenor", "error.ajustesCxC.fechaCierreMenor", true);
			}
		}
		
		
			
		
		//--------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------------------------
		//--- Se debe verificar que no exista cierre Saldo Anual.
		descCierre = mundo.verificarFechaCierre(con, 1, forma.getFecha(), usuario.getCodigoInstitucionInt());
		
		
		//-- si no existe no se verfica .
		if (UtilidadCadena.noEsVacio(descCierre) && UtilidadCadena.noEsVacio(forma.getFecha()))
		{
			String []cadA =  forma.getFecha().split("/");
			int anioA = 0, anioC = 0;
			
			anioA = Integer.parseInt(cadA [2]);
			anioC = Integer.parseInt(descCierre);
			
			
			//-- Validar que la fecha de ajuste sea mayor que la fecha Cierre Anual
			if( anioC >=  anioA )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.fechaCierreMenor", "error.ajustesCxC.fechaCierreMenor", true);
			}
		}
		//--------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------------------------
		//--- Se debe verificar que Si la cuenta de Cobro esta en estado Radicada. La fecha de generacion de Ajuste sea Mayor o Igual.
		/*
		if ( UtilidadCadena.noEsVacio(forma.getCuentaCobro()) )
		{
			descCierre = mundo.verificarCuentaCobro(con, 2, Integer.parseInt(forma.getCuentaCobro()), usuario.getCodigoInstitucionInt());
			
			
			//-- si no existe no se verfica.
			if (UtilidadCadena.noEsVacio(descCierre) && UtilidadCadena.noEsVacio(forma.getFecha()))
			{
				String []cadA =  forma.getFecha().split("/");
				String []cadC =  descCierre.split("/");
				int mesA = 0, anioA = 0, mesC = 0, anioC = 0;
				
				mesA = Integer.parseInt(cadA [1]);	anioA = Integer.parseInt(cadA [2]);
				mesC = Integer.parseInt(cadC [1]);	anioC = Integer.parseInt(cadC [2]);
				
				
				//-- Validar que la fecha de ajuste sea mayor que la fecha Cierre inicial de Cartera.
				if( 
					(anioA < anioC) ||
					( (anioA == anioC) && (mesC >= mesA) ) 
				  )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.fechaCierreMenor", "error.ajustesCxC.fechaCierreMenor", true);
				}
			}
		}*/	
		//--------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------------------------

		
		llenarMundo(forma, mundo);
		if(forma.getModificando())
		{
			int resultado=mundo.modificar(con);
			if(resultado<1)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
			}
		}
		else
		{
			String consecutivoStr=ConstantesBD.codigoNuncaValido+"";
			if(mundo.getTipoAjuste()==ConstantesBD.codigoConceptosCarteraDebito)
			{
				consecutivoStr=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAjustesDebitoCapitacion, usuario.getCodigoInstitucionInt());
				if(Utilidades.convertirAEntero(consecutivoStr)<=0)
				{
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAjustesDebitoCapitacion, usuario.getCodigoInstitucionInt(), consecutivoStr, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.noExisteConsecutivosCapitacion", "error.ajustesCxC.noExisteConsecutivosCapitacion", true);
				}
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAjustesDebitoCapitacion, usuario.getCodigoInstitucionInt(), consecutivoStr, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			else if(mundo.getTipoAjuste()==ConstantesBD.codigoConceptosCarteraCredito)
			{
				consecutivoStr=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAjustesCreditoCapitacion, usuario.getCodigoInstitucionInt());
				if(Utilidades.convertirAEntero(consecutivoStr)<=0)
				{
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAjustesCreditoCapitacion, usuario.getCodigoInstitucionInt(), consecutivoStr, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.noExisteConsecutivosCapitacion", "error.ajustesCxC.noExisteConsecutivosCapitacion", true);
				}
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAjustesCreditoCapitacion, usuario.getCodigoInstitucionInt(), consecutivoStr, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			else
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Tipo de ajuste no definido", "Tipo de ajuste no definido", false);
			}
			
			int resultado=mundo.guardar(con, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(),Utilidades.convertirAEntero(consecutivoStr));
			if(resultado<1)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
			}
			else
			{
				forma.setCodigo(resultado);
			}
		}
		mundo.reset();
		String resultadoAjuste=mundo.cargarAjuste(con, forma.getCodigo());
		if(UtilidadCadena.noEsVacio(resultadoAjuste))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoAjuste, resultadoAjuste, true);
		}
		llenarForm(mundo, forma);
		forma.setDetalleCargues(mundo.cargarCargues(con, mundo.getCuentaCobro(), mundo.getCodigo(), usuario.getCodigoInstitucionInt()));
		return hacerMapping(con, mapping, "detalle");
	}

	/**
	 * Método para pasar las propiedades del form al mundo
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(AjustesCxCForm forma, AjusteCxC mundo)
	{
		mundo.setCodigo(forma.getCodigo());
		mundo.setConvenio(forma.getConvenio());
		mundo.setNumero(Integer.parseInt(forma.getNumero()));
		mundo.setCuentaCobro(Integer.parseInt(forma.getCuentaCobro()));
		mundo.setFecha(forma.getFecha());
		mundo.setConcepto(forma.getConcepto());
		mundo.setObservaciones(forma.getObservaciones());
		mundo.setEstado(forma.getEstadoAjuste());
		mundo.setSaldo(forma.getSaldo());
		mundo.setTipoAjuste(forma.getTipoAjuste());
		mundo.setValorAjuste(forma.getValorAjuste());
	}

	/**
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionBuscarCuentaCobro(Connection con, ActionMapping mapping, AjustesCxCForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		AjusteCxC mundo=new AjusteCxC();
		String resultadoBusqueda=mundo.cargarCuentaCobro(con, Integer.parseInt(forma.getCuentaCobro()), usuario.getCodigoInstitucionInt());
		
		//-- Verificar si la Cuenta de Cobro Tiene Un Ajuste en Estado Pendiente.
		String desAjuste = mundo.verificarCuentaCobro(con, 1, Integer.parseInt(forma.getCuentaCobro()), usuario.getCodigoInstitucionInt());

			
		forma.setModificando(false);
		if(!UtilidadCadena.noEsVacio(resultadoBusqueda) && !UtilidadCadena.noEsVacio(desAjuste) )
		{
			this.llenarForm(mundo, forma);
			
			//--Consultar la fecha de radicación. si la cuenta de cobro esta en estado radicada.
			String fa = mundo.verificarCuentaCobro(con, 2, Integer.parseInt(forma.getCuentaCobro()), usuario.getCodigoInstitucionInt());
			forma.setFechaRadicacionCc(fa);

			return hacerMapping(con, mapping, "ajustes");
		}
		else
		{
			if (!UtilidadCadena.noEsVacio(desAjuste))
			{	
				forma.setMensaje(resultadoBusqueda);
			}
			else
			{
				forma.setMensaje(desAjuste);
				//return ComunAction.accionSalirCasoError(mapping, request, con, logger, desAjuste, desAjuste, true);
			}
		}
		return hacerMapping(con, mapping, "principal");
	}

	/**
	 * Método para buscar un ajuste en BD
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param institucion 
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarAjuste(Connection con, ActionMapping mapping, AjustesCxCForm forma, int institucion)
	{
		AjusteCxC mundo=new AjusteCxC();
		String resultadoBusqueda=mundo.cargarAjuste(con, forma.getTipoAjuste(), Integer.parseInt(forma.getNumero()));
		forma.setCantidadAjustes(mundo.consultarTipos(con, 4, forma.getCodigo()).size());
		forma.setModificando(true);
	
		
		if(!UtilidadCadena.noEsVacio(resultadoBusqueda))
		{
			this.llenarForm(mundo, forma);
			//--Consultar la fecha de radicación. si la cuenta de cobro esta en estado radicada. 
			forma.setFechaRadicacionCc(mundo.verificarCuentaCobro(con, 2, Integer.parseInt(forma.getCuentaCobro()), institucion));
			return hacerMapping(con, mapping, "ajustes");
		}
		else
		{
			forma.setMensaje(resultadoBusqueda+ConstantesBD.separadorSplit+mundo.getEstado());
		}
		return hacerMapping(con, mapping, "principal");
	}

	/**
	 * Método para pasar los datos del mundo a la forma
	 * @param mundo
	 * @param forma
	 */
	private void llenarForm(AjusteCxC mundo, AjustesCxCForm forma)
	{
		forma.setCodigo(mundo.getCodigo());
		forma.setConvenio(mundo.getConvenio());
		forma.setNumero(mundo.getNumero()+"");
		forma.setCuentaCobro(mundo.getCuentaCobro()+"");
		forma.setFecha(mundo.getFecha());
		forma.setConcepto(mundo.getConcepto());
		forma.setObservaciones(mundo.getObservaciones());
		forma.setCodigoEstado(mundo.getCodigoEstado());
		forma.setEstadoAjuste(mundo.getEstado());
		forma.setSaldo(mundo.getSaldo());
		forma.setTipoAjuste(mundo.getTipoAjuste());
		forma.setValorAjuste(mundo.getValorAjuste());
		forma.setMotivoAnulacion(mundo.getMotivoAnulacion());

		//forma.setFechaRadicacionCc(mundo.getFechaRadicacionCc());
		
		forma.setModificoValorAjuste(false);
	}

	/**
	 * Manejo flujo inicio funcionalidad
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, AjustesCxCForm forma, HttpServletRequest request, UsuarioBasico usuario)
	{
		forma.reset();
		AjusteCxC mundo=new AjusteCxC();
		Collection coleccion=mundo.consultarTipos(con, 1, 0);
		if(coleccion == null || coleccion.isEmpty())
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		forma.setTiposAjuste(coleccion);
		coleccion=mundo.consultarTipos(con, 2, 0);
		if(coleccion == null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		if(coleccion.isEmpty())
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.noListadoConvenios", "error.ajustesCxC.noListadoConvenios", true);
		}
		forma.setListadoConvenios(coleccion);
		coleccion=mundo.consultarTipos(con, 3, 0);
		if(coleccion == null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		if(coleccion.isEmpty())
		{
			
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.noListadoConceptos", "error.ajustesCxC.noListadoConceptos", true);
		}
		
		//-- Verificar si faltan Definir los Consecutivos Ajustes Debito y Credito Empresa. (Administracion --> consecutivos Disponibles --> Cartera )
		String cons = UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoAjustesDebitoCapitacion, usuario.getCodigoInstitucionInt());
		String cons1 =UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoAjustesCreditoCapitacion, usuario.getCodigoInstitucionInt());

		if( (!UtilidadCadena.noEsVacio(cons) ||  cons.equals("-1")) || (!UtilidadCadena.noEsVacio(cons1) ||  cons1.equals("-1")))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.ajustesCxC.noExisteConsecutivosCapitacion", "error.ajustesCxC.noExisteConsecutivosCapitacion", true);
		}

		forma.setListadoConceptos(coleccion);
		return hacerMapping(con, mapping, "principal");
	}

	/**
	 * @param con
	 * @param string
	 * @param mapping 
	 * @return
	 */
	private ActionForward hacerMapping(Connection con, ActionMapping mapping, String forward)
	{
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión "+e);
		}
		return mapping.findForward(forward);
	}

}
