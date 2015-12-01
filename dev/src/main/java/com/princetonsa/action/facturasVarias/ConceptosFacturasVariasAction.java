package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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

import util.ConstantesBD;
import util.InfoDatos;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturasVarias.ConceptosFacturasVariasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.ConceptosFacturasVarias;

/**
 * @author Juan Sebastián Castaño 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * ConceptosFacturasVarias
 */
public class ConceptosFacturasVariasAction extends Action {
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConceptosFacturasVariasAction.class);
	
	HashMap<String, Object> resultBuscRegFactVar = new HashMap<String, Object>();
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{

		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof ConceptosFacturasVariasForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				ConceptosFacturasVariasForm conceptosFacVariasForm =(ConceptosFacturasVariasForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=conceptosFacVariasForm.getEstado(); 
				logger.warn("estado conceptosFacVariasAction-->"+estado);

				if(estado == null)
				{
					conceptosFacVariasForm.reset();	
					logger.warn("Estado no valido dentro del flujo de CONCEPTOS FACTURAS VARIAS (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	

				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,conceptosFacVariasForm,usuario, mapping);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,conceptosFacVariasForm,usuario,request,response);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,conceptosFacVariasForm,usuario,request,mapping);
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar (con, conceptosFacVariasForm, mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con, conceptosFacVariasForm, response, request, mapping);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar (con, conceptosFacVariasForm, mapping);
				}
				else
				{
					conceptosFacVariasForm.reset();
					logger.warn("Estado no valido dentro del flujo de Conceptos Facturas varias (null) ");
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

	private ActionForward accionOrdenar(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, ActionMapping mapping) {
		//columnas del listado
		String[] indices = (String[])conceptosFacVariasForm.getListadoFactVar("INDICES");
 
		int temp = conceptosFacVariasForm.getNumListadoFactVar();
		
		logger.info("EL INDICE PARA ORDENAR----->"+conceptosFacVariasForm.getIndice());
		
	
		conceptosFacVariasForm.setListadoFactVar(Listado.ordenarMapa(indices,
				conceptosFacVariasForm.getIndice(),
				conceptosFacVariasForm.getUltimoIndice(),
				conceptosFacVariasForm.getListadoFactVar(),
				temp));
		
		
		conceptosFacVariasForm.setListadoFactVar("numRegistros",temp+"");
		
		conceptosFacVariasForm.setUltimoIndice(conceptosFacVariasForm.getIndice());
		
		conceptosFacVariasForm.setListadoFactVar("INDICES", indices);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionRedireccion(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping) {
		
		try
		{
			UtilidadBD.cerrarConexion(con);
			response.sendRedirect(conceptosFacVariasForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ConceptosFacturasVariasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConceptoFacturasVariasAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Metodo implementado para la eliminacion de registros de conceptos de facturas varias
	 * @param con
	 * @param conceptosFacVariasForm
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, ActionMapping mapping) 
	{
		int pos = Integer.parseInt(conceptosFacVariasForm.getcodigoEliminarPos());
		
		
		//Pregunto si el registro no existe en la base de datos
		if(conceptosFacVariasForm.getListadoFactVar("consecutivo_"+pos).toString().equals(""))
		{
			for(int i=pos;i<(conceptosFacVariasForm.getNumListadoFactVar()-1);i++)
			{
				conceptosFacVariasForm.setListadoFactVar("consecutivo_"+i, conceptosFacVariasForm.getListadoFactVar("consecutivo_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("codigo_"+i, conceptosFacVariasForm.getListadoFactVar("codigo_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("descripcion_"+i, conceptosFacVariasForm.getListadoFactVar("descripcion_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("activo_"+i, conceptosFacVariasForm.getListadoFactVar("activo_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("cuentaContableDebito_"+i, conceptosFacVariasForm.getListadoFactVar("cuentaContableDebito_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("codigoCuentaContableDebito_"+i, conceptosFacVariasForm.getListadoFactVar("codigoCuentaContableDebito_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("cuentaContableCredito_"+i, conceptosFacVariasForm.getListadoFactVar("cuentaContableCredito_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("codigoCuentaContableCredito_"+i, conceptosFacVariasForm.getListadoFactVar("codigoCuentaContableCredito_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("institucion_"+i, conceptosFacVariasForm.getListadoFactVar("institucion_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("eliminar_"+i, conceptosFacVariasForm.getListadoFactVar("eliminar_"+(i+1)));
				conceptosFacVariasForm.setListadoFactVar("usuarioModifica_"+i, conceptosFacVariasForm.getListadoFactVar("usuarioModifica_"+(i+1)));
				
			}
			
			pos = conceptosFacVariasForm.getNumListadoFactVar();
			pos--;
			conceptosFacVariasForm.getListadoFactVar().remove("consecutivo_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("codigo_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("descripcion_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("activo_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("cuentaContableDebito_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("codigoCuentaContableDebito_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("cuentaContableCredito_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("codigoCuentaContableCredito_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("institucion_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("eliminar_"+pos);
			conceptosFacVariasForm.getListadoFactVar().remove("usuarioModifica_"+pos);
			
			conceptosFacVariasForm.setListadoFactVar("numRegistros", pos+"");
			
		}
		else
			//Si existe simplemente se marca el campo eliminar en S
			conceptosFacVariasForm.setListadoFactVar("eliminar_"+pos, ConstantesBD.acronimoSi);
			
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo implementado para insertar/modificar/eliminar registros de conceptos facturas varias
	 * @param con
	 * @param conceptosFacVariasForm
	 * @param usuario
	 * @param request
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		ConceptosFacturasVarias mundoConcepto = new ConceptosFacturasVarias();
		
		UtilidadBD.iniciarTransaccion(con);
		
		
		for(int i=0;i<conceptosFacVariasForm.getNumListadoFactVar();i++) //CAMBIAR EL MAPA 
		{
			
			mundoConcepto.clean();
			
			if(conceptosFacVariasForm.getListadoFactVar("consecutivo_"+i).toString().equals(""))
			{

				this.llenarMundo(conceptosFacVariasForm,mundoConcepto,i,usuario);

				//**************INSERCION DE CONCEPTO FACTURAS VARIAS*****************************
				if(!mundoConcepto.insertarConceptoFactura(con))
				{
					errores.add("",new ActionMessage("errors.ingresoDatos","el registro N° "+(i+1)+". Proceso Cancelado"));
				}
				
			}
			
			else if(UtilidadTexto.getBoolean(conceptosFacVariasForm.getListadoFactVar("eliminar_"+i).toString()))
			{
				
				this.llenarMundo(conceptosFacVariasForm,mundoConcepto,i,usuario);
				
				if(!mundoConcepto.eliminarConceptoFacturasVarias(con)){
					
					errores.add("",new ActionMessage("errors.problemasGenericos","al eliminar el registro N° "+(i+1)));
				}
				else
				{
					String temp3[] = {"consecutivo_","codigo_","descripcion_","cuentaContableDebito_","codigoCuentaContableDebito_","cuentaContableCredito_","codigoCuentaContableCredito_","activo_","institucion_","usuarioModifica_","tipoconcepto","tercero_","getDescripciontercero_","cuentaContableCredVa_","codigoCuentaContableCredVa_"};
					Utilidades.generarLogGenerico(conceptosFacVariasForm.getListadoFactVar(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logConceptosFacturasVariasCodigo,(String[])temp3);
					//Utilidades.generarLogGenerico(conceptosFacVariasForm.getListadoFactVar(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logConceptosFacturasVariasCodigo,(String[])conceptosFacVariasForm.getListadoFactVar("INDICES") );					
				}
			}
				
			else
			{
				
				//Accion de modificaion de un concepto de factura
				this.llenarMundo(conceptosFacVariasForm, mundoConcepto, i, usuario);
			
				// Comparar si el registro leido fue modificado por el usuario
				logger.info("ENTRE ACA 1");
				if (registroFueModificado ( con, conceptosFacVariasForm, i))
				{
					logger.info("ENTRE ACA 2");
					if(!mundoConcepto.modificarConceptoFacturasVarias(con))
					{
						logger.info("ENTRE ACA 3");
						errores.add("",new ActionMessage("errors.sinActualizar"));
					}
					else
					{
				
						HashMap<String, Object> temp2 = new HashMap<String, Object> (resultBuscRegFactVar);
						temp2.remove("eliminar_"+i);
						String temp3[] = {"consecutivo_","codigo_","descripcion_","cuentaContableDebito_","codigoCuentaContableDebito_","cuentaContableCredito_","codigoCuentaContableCredito_","activo_","institucion_","usuarioModifica_","tipoconcepto_","tercero_","getDescripciontercero_","codigoCuentaContableCreditoVigenciaAnterior_"};
						Utilidades.generarLogGenerico(conceptosFacVariasForm.getListadoFactVar(), temp2, usuario.getLoginUsuario(), false, i, ConstantesBD.logConceptosFacturasVariasCodigo,(String[])temp3);												
						//Utilidades.generarLogGenerico(conceptosFacVariasForm.getListadoFactVar(), resultBuscRegFactVar, usuario.getLoginUsuario(), false, i, ConstantesBD.logConceptosFacturasVariasCodigo,(String[])conceptosFacVariasForm.getListadoFactVar("INDICES"));					
					}
				}
					
			}
		}// fin for mapa
			
		
		
		if(!errores.isEmpty())
		{
			
			saveErrors(request, errores);
			conceptosFacVariasForm.setEstado("");
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			
			UtilidadBD.finalizarTransaccion(con);
			return accionEmpezar(con,conceptosFacVariasForm,usuario, mapping);
		}
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * Validar si el registro fue modificado 
	 * @param con
	 * @param conceptosFacVariasForm
	 * @param pos
	 * @return
	 */
	private boolean registroFueModificado(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, int pos) {
		
		ConceptosFacturasVarias mundoConceptoFacVar = new ConceptosFacturasVarias();
		//HashMap<String, Object> resultBuscRegFactVar = new HashMap<String, Object>();
		resultBuscRegFactVar.clear();
		
		int BanderaDeModificacion = 0;
		
		mundoConceptoFacVar.setConsecutivo(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("consecutivo_"+pos).toString()));	
		resultBuscRegFactVar = mundoConceptoFacVar.buscarConceptFactVarByConsec(con);
		
		
		if (!resultBuscRegFactVar.get("descripcion_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("descripcion_"+pos).toString()))
		{
			BanderaDeModificacion++;
		}
		if (!resultBuscRegFactVar.get("tipoconcepto_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("tipoconcepto_"+pos).toString()))
		{
			BanderaDeModificacion++;
		}
		if (!resultBuscRegFactVar.get("tercero_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("tercero_"+pos).toString()))
		{
			BanderaDeModificacion++;
		}
		
		// COMPARACION DEL CAMPO DE CUENTA CONTABLE DEBITO ANTES DE UNA ACTUALIZACION
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaContableDebito_"+pos).toString()))
		{
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaContableDebito_0").toString()))
			{
				//Ambas cuentas (La antigua y la nueva) contienen un valor se pueden comparar y asi evitar errores por null pointer
				if (!resultBuscRegFactVar.get("cuentaContableDebito_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("cuentaContableDebito_"+pos).toString()))
				{
					BanderaDeModificacion++;					
				}				
			}
			else
			{
				BanderaDeModificacion++;							
			}							
		}
		else
		{	//COMPARAR SI EL VALOR DE LA CUENTA PASO A NINGUNA
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaContableDebito_0").toString()))
			{
				BanderaDeModificacion++;		
			}				
		}
		
		
		//comparacion del campo de cuenta contable credito		
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredito_"+pos).toString()))
		{
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaContableCredito_0").toString()))
			{
				//Ambas cuentas (La antigua y la nueva) contienen un valor se pueden comparar y asi evitar errores por null pointer
				if (!resultBuscRegFactVar.get("cuentaContableCredito_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredito_"+pos).toString()))
				{					
					BanderaDeModificacion++;
				}				
			}
			else
			{
				BanderaDeModificacion++;						
			}					
		}
		else
		{
			//COMPARAR SI EL VALOR DE LA CUENTA PASO A NINGUNA
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaContableCredito_0").toString()))
			{
				BanderaDeModificacion++;				
			}			
		}
		
		//Anexo 958
		//comparacion del campo de cuenta contable credito	vigencia anterior	
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredVa_"+pos).toString()))
		{
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaContableCredVa_0").toString()))
			{
				//Ambas cuentas (La antigua y la nueva) contienen un valor se pueden comparar y asi evitar errores por null pointer
				if (!resultBuscRegFactVar.get("cuentaContableCredVa_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredVa_"+pos).toString()))
				{					
					BanderaDeModificacion++;
				}				
			}
			else
			{
				BanderaDeModificacion++;						
			}					
		}
		
	
		
		
		
		//comparacion del campo de cuenta contable credito	vigencia anterior	
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaIngrVigencia_"+pos).toString()))
		{			
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaIngrVigencia_0").toString()))
			{
				//Ambas cuentas (La antigua y la nueva) contienen un valor se pueden comparar y asi evitar errores por null pointer
				if (!resultBuscRegFactVar.get("cuentaIngrVigencia_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("cuentaIngrVigencia_"+pos).toString()))
				{					
					BanderaDeModificacion++;
				}				
			}
			else
			{
				BanderaDeModificacion++;						
			}					
		}
		else
		{
			//COMPARAR SI EL VALOR DE LA CUENTA PASO A NINGUNA
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("cuentaIngrVigencia_0").toString()))
			{
				BanderaDeModificacion++;				
			}			
		}

	
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("ajustedebitoviganterior_"+pos).toString()))
		{			
			if(UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("ajustedebitoviganterior_0").toString()))
			{
				//Ambas cuentas (La antigua y la nueva) contienen un valor se pueden comparar y asi evitar errores por null pointer
				if (!resultBuscRegFactVar.get("ajustedebitoviganterior_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("ajustedebitoviganterior_"+pos).toString()))
				{					
					BanderaDeModificacion++;
				}		
			}
			else
			{
				BanderaDeModificacion++;						
			}
		}
		else
		{
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("ajustedebitoviganterior_0").toString()))
			{
				BanderaDeModificacion++;				
			}
		}
		
		
		
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("ajustecreditoviganterior_"+pos).toString()))
		{			
			if(UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("ajustecreditoviganterior_0").toString()))
			{
				//Ambas cuentas (La antigua y la nueva) contienen un valor se pueden comparar y asi evitar errores por null pointer
				if (!resultBuscRegFactVar.get("ajustecreditoviganterior_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("ajustecreditoviganterior_"+pos).toString()))
				{					
					BanderaDeModificacion++;
				}		
			}
			else
			{
				BanderaDeModificacion++;						
			}
		}
		else
		{
			if (UtilidadCadena.noEsVacio(resultBuscRegFactVar.get("ajusteCreditoVigAnterior_0").toString()))
			{
				BanderaDeModificacion++;				
			}
		}
		
		
				
		
		if (!resultBuscRegFactVar.get("activo_0").toString().equals(conceptosFacVariasForm.getListadoFactVar("activo_"+pos).toString()))
		{
			BanderaDeModificacion++;		
		}
	
		//Verificar si el registro evaluado si ha sido modificado
		if (BanderaDeModificacion > 0)
		{
			return true;
		}
		else
		{
			return false;
			
		}
		
	}

	/**
	 * Metodo para cargar los datos de la forma al mundo
	 * @param conceptosFacVariasForm
	 * @param mundoConcepto
	 * @param pos
	 * @param usuario 
	 */
	private void llenarMundo(ConceptosFacturasVariasForm conceptosFacVariasForm, ConceptosFacturasVarias mundoConcepto, int pos, UsuarioBasico usuario) 
	{
		
		
		
		if(!conceptosFacVariasForm.getListadoFactVar("consecutivo_"+pos).toString().equals(""))
			mundoConcepto.setConsecutivo(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("consecutivo_"+pos).toString()));
		
		mundoConcepto.setCodigo(conceptosFacVariasForm.getListadoFactVar("codigo_"+pos).toString());
		mundoConcepto.setDescripcion(conceptosFacVariasForm.getListadoFactVar("descripcion_"+pos).toString());
		if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaContableDebito_"+pos).toString()))
		{
			mundoConcepto.setCodigoCuentaContableDebito(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("cuentaContableDebito_"+pos).toString()));
			mundoConcepto.getCuentaContableDebito().setNombre(conceptosFacVariasForm.getListadoFactVar("codigoCuentaContableDebito_"+pos).toString());
		}
		mundoConcepto.setActivo(conceptosFacVariasForm.getListadoFactVar("activo_"+pos).toString());
		mundoConcepto.setInstitucion(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("institucion_"+pos).toString()));
		mundoConcepto.setUsuarioModifica(usuario.getLoginUsuario());
        mundoConcepto.setTipoconcepto(conceptosFacVariasForm.getListadoFactVar("tipoconcepto_"+pos).toString());
        mundoConcepto.setTercero(conceptosFacVariasForm.getListadoFactVar("tercero_"+pos).toString());
      	if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredito_"+pos).toString()))
		{
			mundoConcepto.setCodigoCuentaContableCredito(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredito_"+pos).toString()));
			mundoConcepto.getCuentaContableCredito().setNombre(conceptosFacVariasForm.getListadoFactVar("codigoCuentaContableCredito_"+pos).toString());
		}
      	if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("ajustedebitoviganterior_"+pos).toString()))
		{
			
      		logger.info("**********************************************************************************************");
      		logger.info("**********************************************************************************************");
      		String valor=conceptosFacVariasForm.getListadoFactVar("ajusteDebitoVigAnterior_"+pos).toString();
      		logger.info("->"+valor+"<--");
      		logger.info("**********************************************************************************************");
      		
      		mundoConcepto.getAjusteDebitoVigAnterior().setCodigo(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("ajustedebitoviganterior_"+pos).toString()));
			mundoConcepto.getAjusteDebitoVigAnterior().setNombre(conceptosFacVariasForm.getListadoFactVar("codigoajustedebitoviganterior_"+pos).toString());
		}
      	if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("ajustecreditoviganterior_"+pos).toString()))
		{
			mundoConcepto.getAjusteCreditoVigAnterior().setCodigo(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("ajustecreditoviganterior_"+pos).toString()));
			mundoConcepto.getAjusteCreditoVigAnterior().setNombre(conceptosFacVariasForm.getListadoFactVar("codigoajustecreditoviganterior_"+pos).toString());
		}
      	//Anexo 958
      	if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredVa_"+pos).toString()))
		{
			mundoConcepto.setCodigoCuentaContableCreditoVigenciaAnterior(Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("cuentaContableCredVa_"+pos).toString()));
			mundoConcepto.getCuentaContableCreditoVigenciaAnterior().setNombre(conceptosFacVariasForm.getListadoFactVar("codigoCuentaContableCredVa_"+pos).toString());
			logger.info("********EL CODIGO DE LA CTA CONTABLE------>"+mundoConcepto.getCodigoCuentaContableCreditoVigenciaAnterior());
			logger.info("********EL NOMBRE DE LA CTA ------->"+mundoConcepto.getCuentaContableCreditoVigenciaAnterior().getNombre());
		}
      	
      	if(UtilidadCadena.noEsVacio(conceptosFacVariasForm.getListadoFactVar("cuentaIngrVigencia_"+pos).toString()))
      	{
      		mundoConcepto.setCuentaIngrVigencia(new InfoDatos( Integer.parseInt(conceptosFacVariasForm.getListadoFactVar("cuentaIngrVigencia_"+pos).toString()), conceptosFacVariasForm.getListadoFactVar("codigoCuentaIngrVigencia_"+pos).toString()));
      	}
      	
      	
      	
      	//Fi n anexo 958
		
      	
    }

	/***
	 * Metodo implementado para agregar un nuevo registro al mapa listadoFactVar
	 * @param con
	 * @param conceptosFacVariasForm
	 * @param usuario
	 * @param response 
	 * @param request 
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		int pos = conceptosFacVariasForm.getNumListadoFactVar();
		
		conceptosFacVariasForm.setListadoFactVar("consecutivo_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("codigo_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("descripcion_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("activo_"+pos, ConstantesBD.acronimoSi);
		conceptosFacVariasForm.setListadoFactVar("cuentaContableDebito_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("codigoCuentaContableDebito_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("cuentaContableCredito_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("codigoCuentaContableCredito_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("institucion_"+pos, usuario.getCodigoInstitucion());
		conceptosFacVariasForm.setListadoFactVar("eliminar_"+pos, ConstantesBD.acronimoNo);
		conceptosFacVariasForm.setListadoFactVar("tipoconcepto_"+pos,conceptosFacVariasForm.getTipoconcepto());
		conceptosFacVariasForm.setListadoFactVar("tercero_"+pos,conceptosFacVariasForm.getTercero());
		conceptosFacVariasForm.setListadoFactVar("descripciontercero_"+pos,conceptosFacVariasForm.getDescripciontercero());
		conceptosFacVariasForm.setListadoFactVar("usuarioModifica_"+pos, usuario.getLoginUsuario());
		conceptosFacVariasForm.setListadoFactVar("nuevoRegistro_"+pos, ConstantesBD.acronimoSi);
		conceptosFacVariasForm.setListadoFactVar("cuentaContableCredVa_"+pos, "");
		conceptosFacVariasForm.setListadoFactVar("codigoCuentaContableCredVa_"+pos, "");
		pos++;
		conceptosFacVariasForm.setListadoFactVar("numRegistros", pos+"");
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(conceptosFacVariasForm.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),conceptosFacVariasForm.getNumListadoFactVar(), response, request, "conceptoFacturasVarias.jsp",true);
	}

	/**
	 * Metodo que inicia el flujo de conceptos facturas varias
	 * @param con
	 * @param conceptosFacVariasForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConceptosFacturasVariasForm conceptosFacVariasForm, UsuarioBasico usuario, ActionMapping mapping) 
	{
		
		ConceptosFacturasVarias mundoConceptoFacVar = new ConceptosFacturasVarias();
		mundoConceptoFacVar.setInstitucion(usuario.getCodigoInstitucionInt());
		conceptosFacVariasForm.setListadoFactVar(mundoConceptoFacVar.cargar(con));
		
		Utilidades.imprimirMapa(conceptosFacVariasForm.getListadoFactVar());
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");
	}
}
