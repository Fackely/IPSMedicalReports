package com.princetonsa.action.facturasVarias;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.actionform.facturasVarias.ConsultaImpresionAjustesFacturasVariasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.ConsultaImpresionAjustesFacturasVarias;

/**
 * Clase implementada para el manejo de la
 * consulta e impresion de ajustes de facturas
 * varias
 * @author Jhony Alexander Duque A.
 *
 */

public class ConsultaImpresionAjustesFacturasVariasAction extends Action 
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		Logger logger = Logger.getLogger(ConsultaImpresionAjustesFacturasVariasAction.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ------------------------------------------------*/
	 
	 
      //-----Se instancian los indices del mundo----------------------------------------------
		
		String [] indicesAjustesFacturasVarias = ConsultaImpresionAjustesFacturasVarias.indicesAjustesFacturasVarias;
		String [] indicesCriterios = ConsultaImpresionAjustesFacturasVarias.indicesCriteriosBusqueda;
		
/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, HttpServletRequest request,
								 HttpServletResponse response) throws Exception
								 {

		Connection connection = null;
		try{
			if (form instanceof ConsultaImpresionAjustesFacturasVariasForm) 
			{


				//se habre la conexion a la BD
				connection = UtilidadBD.abrirConexion();


				//se verifica si la conexion esta nula
				if (connection == null)
				{
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//optenemos el valor de la forma.
				ConsultaImpresionAjustesFacturasVariasForm forma = (ConsultaImpresionAjustesFacturasVariasForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				//se instancia el mundo
				ConsultaImpresionAjustesFacturasVarias mundo = new ConsultaImpresionAjustesFacturasVarias();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE ConsultaImpresionAjustesFacturasVariasForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*-------------------------------------------------------------------
				 * 	ESTADOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
			 -------------------------------------------------------------------*/


				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
				if (estado == null)
				{
					//resetiamos la forma.
					forma.reset();

					//se saca un log con el siguiente texto.
					logger.warn("Estado no Valido dentro del Flujo de Generacion modificacion ajustes facturas varias (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  INICIAL
			 ---------------------------------------------*/
				else
					if (estado.equals("empezar"))
					{ 
						//se inicializan los valores de la forma.
						forma.reset();
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("listado");
					}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  BUSCAR
			 ---------------------------------------------*/
					else
						if (estado.equals("buscar"))
						{ 

							//se inicializan los valores de la forma.
							forma.resetBusqueda();

							errores= validarCriterios(connection, forma);
							if(!errores.isEmpty())
							{
								saveErrors(request, errores);
								forma.setEstado("initBusqueda");
								return mapping.findForward("listado");

							}	

							forma.getFiltrosBusqueda().put("codigoDeudor", forma.getCodigoDeudor());
							forma.getFiltrosBusqueda().put(indicesCriterios[6], forma.getCodigoDeudor());
							forma.getFiltrosBusqueda().put("descripDeudor", forma.getDescripDeudor());
							forma.setListado(mundo.consultarAjustesFacturasVarias(connection, forma.getFiltrosBusqueda(), usuario, true));

							if (Integer.parseInt(forma.getListado("numRegistros")+"")==1)
							{
								forma.setIndex("0");
								mundo.initConsultImpreAjustesFacturasVarias(connection, forma, usuario);
								UtilidadBD.cerrarConexion(connection);
								return mapping.findForward("principal");
							}

							UtilidadBD.cerrarConexion(connection);

							return mapping.findForward("listado");
						}

				/*----------------------------------------------
				 * ESTADO =================>>>>>  CARGARAJUSTE
			 ---------------------------------------------*/
						else
							if (estado.equals("cargarAjuste"))
							{ 

								mundo.initConsultImpreAjustesFacturasVarias(connection, forma, usuario);
								UtilidadBD.cerrarConexion(connection);
								return mapping.findForward("principal");
							}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  CARGARAJUSTE
			 ---------------------------------------------*/
							else
								if (estado.equals("imprimir"))
								{ 
									return mundo.generarReporte(connection, usuario, forma, request, mapping);
								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>>  ORDENAR
					 ---------------------------------------------*/
									if (estado.equals("ordenar"))
									{
										UtilidadBD.closeConnection(connection);
										forma.setListado(mundo.accionOrdenarMapa(forma.getListado(),forma));
										return mapping.findForward("listado");	
									}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
								 }
	
	
	
	public ActionErrors validarCriterios (Connection connection, ConsultaImpresionAjustesFacturasVariasForm forma)
	{
		ActionErrors errores = new ActionErrors();
		logger.info("\n entre a validarCriterios "+forma.getFiltrosBusqueda(indicesCriterios[5])+"--"+forma.getFiltrosBusqueda(indicesCriterios[6])+"");
		
	
				
		//se valida que la fecha inicial no venga vacia
		if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null"))
		{
			//se valida que la fecha final si este
			if ((forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","La fecha Final"));
		
			//se valida que la fecha incial sea menor o igual a la actual
			if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[2])+"",UtilidadFecha.getFechaActual()))
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[2])+"", "Actual "+UtilidadFecha.getFechaActual()));
		}
			//se valida que la fecha final no venga vacia
			if (!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))
			{	
				//se valida que la fecha inicial si este
				if ((forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null"))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial"));
				}
				//se valida que la fecha final sea mayor o igual  a la fecha inicial
				else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[2])+"",forma.getFiltrosBusqueda(indicesCriterios[3])+""))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final "+forma.getFiltrosBusqueda(indicesCriterios[3])+"", "Inicial "+forma.getFiltrosBusqueda(indicesCriterios[2])+""));
				}
				else//se valida que la fecha final sea menor o igual a la fecha del sistema
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[3])+"",UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+forma.getFiltrosBusqueda(indicesCriterios[3])+"", "Actual "+UtilidadFecha.getFechaActual()));
				
			}
			
			// se pregunta si las fechas vienen sin errores; de ser asi se evalua
			//si la cantidad de dias entre fechas supera 180.
			if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null") && 
				!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))	
				if(UtilidadFecha.numeroMesesEntreFechasExacta(forma.getFiltrosBusqueda(indicesCriterios[2])+"", forma.getFiltrosBusqueda(indicesCriterios[3])+"")>6)
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "180 dias"));
			
			if (!UtilidadCadena.noEsVacio(forma.getCodigoDeudor()+"") &&
					UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[5])+""))
				errores.add("", new ActionMessage("error.facturasVarias.faltaDefinirDeudor"));
			
			boolean ban=false;
			if (
					!UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[0])+"") && //ajuste
					!UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[1])+"") && //factura varia
					!UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[2])+"") &&//fecha inicial
					!UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[3])+"") && //fecha final
					(!UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[12])+"") || (forma.getFiltrosBusqueda(indicesCriterios[12])+"").equals(ConstantesBD.codigoNuncaValido+"")) && //estado
					(!UtilidadCadena.noEsVacio(forma.getCodigoDeudor()+"") && //deudor
					!UtilidadCadena.noEsVacio(forma.getFiltrosBusqueda(indicesCriterios[5])+""))   //tipo deudor
				)
					ban=true;
			
			if (ban)
				errores.add("", new ActionMessage("error.facturacion.ConsultaTarifasServicios.camposBusquedaNoDefinidos"));
			
			
		return errores;
	}
}