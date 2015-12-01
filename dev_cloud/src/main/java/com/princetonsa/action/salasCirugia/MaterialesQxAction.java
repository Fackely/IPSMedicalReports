/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.action.salasCirugia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pedidos.PedidosInsumosForm;
import com.princetonsa.actionform.salasCirugia.MaterialesQxForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.pedidos.PedidosInsumos;
import com.princetonsa.mundo.salasCirugia.HojaGastos;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.salasCirugia.MaterialesQx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.salasCirugia.Peticiones;
import com.princetonsa.pdf.PedidosInsumosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Sebastián Gómez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Materiales Quirúrgicos
 */
@SuppressWarnings("all")
public class MaterialesQxAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MaterialesQxAction.class);
	
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
		
		if (response==null); //Para evitar que salga el warning
		if(form instanceof MaterialesQxForm)
		{
			
						
			//OBJETOS A USAR ****************************************************************
			MaterialesQxForm materialesForm =(MaterialesQxForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			String estado=materialesForm.getEstado(); 
			logger.warn("estado Materiales Qx-->"+estado);
			//*************************************************************************************
			
			//SE REALIZAN LAS VALIDACIONES************************************************************
			if(!estado.equals("resumen"))
			{
				ActionForward validaciones=this.validacionesUsuarios(paciente,materialesForm,mapping,request,logger,usuario);
				if(validaciones!=null)
					return validaciones;
			}
			//*****************************************************************************************
			
			
			if(estado == null)
			{
				materialesForm.reset(false);	
				logger.warn("Estado no valido dentro del flujo de Materiales Quirúgicos (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			//*******ESTADOS DE INGRESAR/MODIFICAR MATERIALES QUIRURGICOS**********************
			else if (estado.equals("empezar"))
			{
				return accionEmpezar(materialesForm,mapping,paciente,usuario,request);
			}
			else if(estado.equals("materiales"))
			{
				return accionMateriales(materialesForm,mapping,usuario,request);
			}
			else if(estado.equals("ordenarOrdenes"))
			{
				return accionOrdenarOrdenes(materialesForm,mapping);
			}
			else if(estado.equals("guardarFrameConsumo"))
			{
				return accionGuardarFrameConsumo(materialesForm,mapping,request);
			}
			else if(estado.equals("guardarMateriales"))
			{
				return accionGuardarMateriales(materialesForm,mapping,usuario,request);
			}
			else if (estado.equals("volverMateriales")) //estado para volver al detalle del consumo
			{
				materialesForm.setValidacionConfirmacionConsumoFinalizacion(false);
				return mapping.findForward("principal");
			}
			else if(estado.equals("redireccion")) //empleado para la paginación de las cantidades de cada cirugía
			{
				return accionRedireccion(materialesForm,response,mapping,request);
			}
			else if(estado.equals("resumen"))
			{
				return accionResumen(materialesForm,mapping,usuario);
			}
			else if (estado.equals("eliminarArticulo"))
			{
				return accionEliminarArticulo(materialesForm,mapping);
			}
			//Estados para la reversión del consumo de materiales ------------------------------------------------------------------
			else if (estado.equals("reversarFinalizacion"))
			{
				return accionReversarFinalizacion(materialesForm,mapping,usuario,request);
			}
			else if (estado.equals("continuarMateriales")) //llamado cuando se decide no finalizar
			{
				return mapping.findForward("resumenMateriales");
			}
			//---------------------------------------------------------------------------------------------------
			else if(estado.equals("imprimirMateriales"))
			{
				return accionImprimirMateriales(materialesForm,mapping,usuario,request);
			}
			//Estados parta el manejo de los paquetes---------------------------------------------------
			else if (estado.equals("agregarPaqueteConsumo"))
			{
				return accionAgregarPaqueteConsumo(materialesForm,mapping);
			}
			else if (estado.equals("eliminarPaqueteConsumo"))
			{
				return accionEliminarPaqueteConsumo(materialesForm,mapping);
			}
			else if (estado.equals("eliminarPaqueteConsumoBusqueda")) //usado desde la busqueda genérica de paquetes
			{
				return accionEliminarPaqueteConsumoBusqueda(materialesForm,mapping);
			}
			//---------------------------------------------------------------------------------------
			//*******************************************************************************
			//*******ESTADOS DE PEDIDO QX. DE MATERIALES QX. ********************************
			//Estado inicial donde se listan las peticiones del paciente 
			else if (estado.equals("listadoPeticion"))
			{
				return accionListadoPeticion(materialesForm,mapping,paciente,usuario,request);
			}
			//Estado para la selección del almacen
			else if(estado.equals("empezarPedido")) 
			{
				return accionSeleccionAlmacen(materialesForm, mapping, usuario, request);
			}
			//estado usado al seleccionar otro centro de costo
			else if (estado.equals("nuevoCentroCosto"))
			{
				return this.accionNuevoCentroCosto(mapping,materialesForm,usuario,request);
			}
			//Estado para mostrar el formulario del ingreso/modificacion del pedido
			else if(estado.equals("iniciarPedido"))
			{
				return accionEmpezarPedido(materialesForm,mapping,usuario);
			}
			else if(estado.equals("agregar")) //estado usado para ingresar un nuevo artículo en el listado
			{
				return accionAgregar(materialesForm,mapping,request);
			}
			//estado usado para ingresar un nuevo paquete al listado de articulos
			else if (estado.equals("agregarPaquete"))
			{
				return accionAgregarPaquete(materialesForm,mapping);
			}
			//Estado usado para deseleccionar un paquete
			else if (estado.equals("eliminarPaquete"))
			{
				return accionEliminarPaquete(materialesForm,mapping);
			}
			//Primer Submit que se hace para cargar los datos de los articulos a la forma
			else if (estado.equals("guardarFrame"))
			{
				return mapping.findForward("listadoArticulos");
				
			}
			//Segundo submit que es el guardar definitivo de todo el pedido
			else if(estado.equals("guardarPedido")) //estado para registrar el pedido de la peticion
			{
				return accionGuardarPedido(materialesForm,mapping,usuario,request);
			}
			else if(estado.equals("imprimirPedido"))
			{
				return accionImprimirPedido(materialesForm,mapping,usuario,request,paciente);
			}
			else if(estado.equals("ordenarPeticiones"))
			{
				return accionOrdenarPeticiones(materialesForm,mapping);
			}
			else if(estado.equals("empezarPedidoFunOut"))
			{
				//Inicializa los valores de la forma excepto los valores pasados por el request en el llamado
				//de la funcionalidad externa
				materialesForm.reset(true);
				return accionSeleccionAlmacen(materialesForm, mapping, usuario, request);
			}
			else if(estado.equals("irProgramacionCx"))
			{
				accionVolverFuncProgramacionCx(materialesForm,response);
			}
			//*****************************************************************************
			
			else
			{
				materialesForm.reset(false);
				logger.warn("Estado no valido dentro del flujo de Materiales Quirúgicos (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}			
		return null;	 
	}
	
	/**
	 * Método implementado para eliminar los articulos de un paquete ya seleccionado desde la busqueda genérica de paquetes
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarPaqueteConsumoBusqueda(MaterialesQxForm materialesForm, ActionMapping mapping) {
		
		Connection con = UtilidadBD.abrirConexion();
		//Se consultan los artículos del paquetes
		HashMap artPaquetes = HojaGastos.consultarArticulosXConsecutivo(con, materialesForm.getConsecutivoPaquete());
		int cantidadActual = 0, cantidadPaquete = 0, sumaCantidadActual = 0;
		ArrayList<Integer> codigosEliminar = new ArrayList<Integer>();
		String[] consecutivosPaquetesInsertados = materialesForm.getConsecutivosPaquetesInsertados().split(",");
		String nuevosConsecutivosInsertados = "";
		boolean encontro = false;
				
		
		//Se reeditan los consecutivos insertados
		for(int i=0;i<consecutivosPaquetesInsertados.length;i++)
		{
			logger.info("consecutivo paquete opUP:*"+materialesForm.getConsecutivoPaquete()+"*");
			logger.info("consecutivo paquete vector["+i+"]:*"+consecutivosPaquetesInsertados[i]+"*");
			logger.info("encontro:*"+encontro+"*");
			if(!consecutivosPaquetesInsertados[i].equals(materialesForm.getConsecutivoPaquete()) || encontro)
			{
				if(!nuevosConsecutivosInsertados.equals(""))
					nuevosConsecutivosInsertados += ",";
				nuevosConsecutivosInsertados += consecutivosPaquetesInsertados[i];
			}
			
			if(consecutivosPaquetesInsertados[i].equals(materialesForm.getConsecutivoPaquete()))
				encontro = true;
			logger.info("nuevosConsecutivosInsertados: "+nuevosConsecutivosInsertados);
		}
		materialesForm.setConsecutivosPaquetesInsertados(nuevosConsecutivosInsertados);
		
		
		
		
		
		//Iteracion de los articulos del listado del consumo
		for(int i=0;i<materialesForm.getNumArticulos();i++)
		{
			//Iteracion de los articulos del paquete a eliminar
			for(int j=0;j<Integer.parseInt(artPaquetes.get("numRegistros").toString());j++)
			{
				//Se verifica si el articulo del listado concuerda con el articulo del paquete
				//logger.info("codigo articulo listado ["+i+"]: "+materialesForm.getArticulos("codigoArticulo_"+i));
				//logger.info("codigo articulo paquete ["+j+"]: "+artPaquetes.get("codigoArticulo_"+j));
				if(Integer.parseInt(materialesForm.getArticulos("codigoArticulo_"+i).toString())==Integer.parseInt(artPaquetes.get("codigoArticulo_"+j).toString()))
				{
					//Se toma la cantidad del paquete
					cantidadPaquete = Integer.parseInt(artPaquetes.get("cantidad_"+j).toString());
					sumaCantidadActual = 0;
					for(int k=0;k<materialesForm.getNumCirugias();k++)
					{
						//Se toma la cantidad de una cirugia del consumo
						cantidadActual = Utilidades.convertirAEntero(materialesForm.getArticulos("consumoActual"+materialesForm.getCirugias("consecutivo_"+k)+"_"+i).toString(),true);
						//Si la cantidad del paquete sigue siendo mayor a 0, se continua descontando la cantidad del articulo en el consumo
						if(cantidadPaquete>0)
						{
							cantidadActual = cantidadActual - cantidadPaquete;
							if(cantidadActual<0)
							{
								cantidadPaquete = cantidadActual*-1;
								cantidadActual = 0;
							}
							else
								cantidadPaquete = 0;
							materialesForm.setArticulos("consumoActual"+materialesForm.getCirugias("consecutivo_"+k)+"_"+i, cantidadActual);
							
							
						}
						sumaCantidadActual+=cantidadActual;
					}
					
					//Si el total de la cantidad dió 0 y el articulo no está registrado en la base de datos se elimina de una vez
					if(sumaCantidadActual==0&&!UtilidadTexto.getBoolean(materialesForm.getArticulos("estaBd_"+i).toString()))
						codigosEliminar.add(Integer.parseInt(materialesForm.getArticulos("codigoArticulo_"+i).toString()));
					
					//1) Se recalcula el consumo actual del articulo (la cantidad que se ha puesto + la cantidad del paquete)
					int total = sumaCantidadActual, totalConsumos = 0, difPedCons = 0;
					materialesForm.setArticulos("total_"+i, sumaCantidadActual+"");
					
					//2) Se recalcula el consumo total
					totalConsumos = total + Integer.parseInt(materialesForm.getArticulos("totalConsAnt_"+i).toString());
					materialesForm.setArticulos("totalConsumos_"+i, totalConsumos+"");
					
					//3) Se recalcula la diferencia en pedido y consumo
					difPedCons = Integer.parseInt(materialesForm.getArticulos("totalPedidos_"+i).toString()) - totalConsumos;
					materialesForm.setArticulos("difPedCons_"+i, difPedCons+"");
						
				} //Fin IF
			} //Fin FOR articulos paquete
		} //Fin FOR articulos listado consumo
		
		
		//Se revisa el arreglo de los articulos que se deben eliminar
		for(Integer codigoArticulo:codigosEliminar)
		{
			int posicionArticulo = ConstantesBD.codigoNuncaValido;
			for(int i=0;i<materialesForm.getNumArticulos();i++)
				if(Integer.parseInt(materialesForm.getArticulos("codigoArticulo_"+i).toString())==codigoArticulo.intValue())
					posicionArticulo = i;
			
			if(posicionArticulo!=ConstantesBD.codigoNuncaValido)
			{
				materialesForm.setArticulos(
					MaterialesQx.eliminarRegistroListadoArticulosConsumo(
						materialesForm.getArticulos(), 
						materialesForm.getNumArticulos(), 
						posicionArticulo,
						materialesForm.isPorActo(), 
						materialesForm.getNumPaquetes(), 
						materialesForm.getNumCirugias(), 
						materialesForm.getCirugias()
					)
				);
				materialesForm.setNumArticulos(Integer.parseInt(materialesForm.getArticulos("numRegistros").toString()));
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoArticuloXCirugia");
	}

	/**
	 * Método usado para eliminar un artículo del listado
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarArticulo(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		
		materialesForm.setArticulos(
			MaterialesQx.eliminarRegistroListadoArticulosConsumo(
				materialesForm.getArticulos(), 
				materialesForm.getNumArticulos(), 
				materialesForm.getPosicionArticulo(),
				materialesForm.isPorActo(), 
				materialesForm.getNumPaquetes(), 
				materialesForm.getNumCirugias(), 
				materialesForm.getCirugias()
			)
		);
		materialesForm.setNumArticulos(Integer.parseInt(materialesForm.getArticulos("numRegistros").toString()));
		
		
		
		
		if(materialesForm.isPorActo())
			return mapping.findForward("listadoArticuloXActo");
		else
			return mapping.findForward("listadoArticuloXCirugia");
	}

	/**
	 * Método usado para eliminar un paquete del listado de pedido
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarPaquete(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		//Se toma la posicion del paquete que se desea eliminar
		int pos = materialesForm.getPosPaquete();
		
		logger.info("POSICION DEL PAQUETE=> "+pos);
		Utilidades.imprimirMapa(materialesForm.getArticulos());
		
		//Se elimna la cantidad del paquete
		for(int i=0;i<materialesForm.getNumArticulos();i++)
		{
			for(int j=pos;j<(materialesForm.getNumPaquetes()-1);j++)
				materialesForm.setArticulos("cantidadPaquete_"+i+"_"+j, materialesForm.getArticulos("cantidadPaquete_"+i+"_"+(j+1)));
			materialesForm.getArticulos().remove("cantidadPaquete_"+i+"_"+(materialesForm.getNumPaquetes()-1));
		}
		
		//Se elimina el paquete del mapa de paquetes
		int numPaquetes = materialesForm.getNumPaquetes();
		for(int i=pos;i<(numPaquetes-1);i++)
		{
			materialesForm.setPaquetes("codigo_"+i, materialesForm.getPaquetes("codigo_"+(i+1)));
			materialesForm.setPaquetes("consecutivo_"+i, materialesForm.getPaquetes("consecutivo_"+(i+1)));
			materialesForm.setPaquetes("descripcion_"+i, materialesForm.getPaquetes("descripcion_"+(i+1)));
		}
		numPaquetes--;
		materialesForm.getPaquetes().remove("codigo_"+numPaquetes);
		materialesForm.getPaquetes().remove("consecutivo_"+numPaquetes);
		materialesForm.getPaquetes().remove("descripcion_"+numPaquetes);
		materialesForm.setPaquetes("numRegistros", numPaquetes);
		materialesForm.setNumPaquetes(numPaquetes);
		
		//Se revisan los artículos existentes
		for(int i=0;i<materialesForm.getNumArticulos();i++)
		{
			//Se recalcula el pedido actual del articulo (la cantidad que se ha puesto + la cantidad del paquete a eliminar)
			//logger.info("cantidadPaquete_"+i+"_"+pos+": "+materialesForm.getArticulos("cantidadPaquete_"+i+"_"+pos));
			int cantidadPaquete = 0;
			for(int j=0;j<materialesForm.getNumPaquetes();j++)
				cantidadPaquete += Utilidades.convertirAEntero(materialesForm.getArticulos("cantidadPaquete_"+i+"_"+j)+"", true);
			int cantidad = cantidadPaquete;
			int total = cantidad + Utilidades.convertirAEntero(materialesForm.getArticulos("totalPedidosAnt_"+i).toString(),true);
			materialesForm.setArticulos("total_"+i, total);
			materialesForm.setArticulos("cantidad_"+i, cantidad);
			
		}
		
		//Se reeditan los paquetes insertados
		String codigosPaquetesInsertados = "";
		for(int i=0;i<materialesForm.getNumPaquetes();i++)
			codigosPaquetesInsertados += (codigosPaquetesInsertados.equals("")?"":",") + materialesForm.getPaquetes("consecutivo_"+i);
		materialesForm.setConsecutivosPaquetesInsertados(codigosPaquetesInsertados);
		
		
		return mapping.findForward("listadoArticulos");
	}

	/**
	 * Método que realiza la reversión de la finalización del consumo
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionReversarFinalizacion(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		MaterialesQx materiales = new MaterialesQx();
		materiales.setUsuario(usuario.getLoginUsuario());
		materiales.setNumeroSolicitud(materialesForm.getOrden());
		
		Connection con = UtilidadBD.abrirConexion();
		if(materiales.reversionFinalizacionConsumo(con)>0)
		{
			UtilidadBD.closeConnection(con);
			return accionMateriales(materialesForm, mapping, usuario, request);
		}
		else
		{
			UtilidadBD.closeConnection(con);
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.ingresoDatos","la reversión de la finalización del consumo"));
			return mapping.findForward("modificarFinalizacion");
		}
		
	}

	/**
	 * Método implementado para carga a la forma el detalle del consumo y hacer validaciones adicionales
	 * @param materialesForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarFrameConsumo(MaterialesQxForm materialesForm, ActionMapping mapping, HttpServletRequest request) 
	{
		if(!materialesForm.isPorActo()&&!materialesForm.isValidacionArticulosServicio())
		{
			ActionErrors errores = new ActionErrors();
			materialesForm.setValidacionArticulosServicio(true);
			String consCx = "";
			int contador = 0;
			
			//***********SE VERIFICA QUE A CADA SERVICIO SE LE HAYA ASIGNADO SU RESPECTIVA CANTIDAD*************
			for(int i=0;i<materialesForm.getNumCirugias();i++)
			{
				consCx = materialesForm.getCirugias("consecutivo_"+i).toString();
				contador = 0;
				
				for(int j=0;j<materialesForm.getNumArticulos();j++)
					contador += Utilidades.convertirAEntero(materialesForm.getArticulos("consumoActual"+consCx+"_"+j)+"", true);
				
				if(contador<=0)
					errores.add("",new ActionMessage("errors.notEspecific","No se ha registrado consumo para el Serv. "+consCx+". ¿Desea continuar?"));
			}
			
			if(!errores.isEmpty())
			{
				materialesForm.setEstado(""); //se borra el estado para que no efectúe ninguna accion
				saveErrors(request, errores);
			}
			//**************************************************************************************************
		}
		
		
		if(materialesForm.isPorActo())
			return mapping.findForward("listadoArticuloXActo");
		else
			return mapping.findForward("listadoArticuloXCirugia");
	}

	/**
	 * Método implementado para eliminar un paquete del consumo
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarPaqueteConsumo(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		int pos = materialesForm.getPosPaquete();
		String[] consecutivosPaquetesInsertados = materialesForm.getConsecutivosPaquetesInsertados().split(",");
		String nuevosConsecutivosInsertados = "";
		int total = 0, totalConsumos = 0, difPedCons = 0; 
		boolean encontro = false;
		ArrayList<Integer> codigosEliminar = new ArrayList<Integer>();
		
		materialesForm.setPaquetes("eliminado_"+pos, ConstantesBD.acronimoSi);
		
		
		//Se reeditan los consecutivos insertados
		for(int i=0;i<consecutivosPaquetesInsertados.length;i++)
		{
			if(!consecutivosPaquetesInsertados[i].equals(materialesForm.getPaquetes("consecutivo_"+pos).toString()) || encontro)
			{
				if(!nuevosConsecutivosInsertados.equals(""))
					nuevosConsecutivosInsertados += ",";
				nuevosConsecutivosInsertados += consecutivosPaquetesInsertados[i];
			}
			
			if(consecutivosPaquetesInsertados[i].equals(materialesForm.getPaquetes("consecutivo_"+pos).toString()))
				encontro = true;
		}
		materialesForm.setConsecutivosPaquetesInsertados(nuevosConsecutivosInsertados);
		
		//Se recalculan los totales de todos los artículos
		for(int i=0;i<materialesForm.getNumArticulos();i++)
		{
			//Se toma el total historial
			total = Integer.parseInt(materialesForm.getArticulos("totalHistorico_"+i).toString());
			
			for(int j=0;j<materialesForm.getNumPaquetes();j++)
				if(!UtilidadTexto.getBoolean(materialesForm.getPaquetes("eliminado_"+j).toString()))
					total += Utilidades.convertirAEntero(materialesForm.getArticulos("consumoActual"+j+"_"+i)+"",true);
			
			//1) Se recalcula el consumo actual
			materialesForm.setArticulos("total_"+i, total+"");
			
			//2) Se recalcula el consumo total
			totalConsumos = Integer.parseInt(materialesForm.getArticulos("totalConsAnt_"+i).toString()) + total;
			materialesForm.setArticulos("totalConsumos_"+i, totalConsumos+"");
			
			//3) Se recalcula la diferencia en pedido y consumo
			difPedCons = Integer.parseInt(materialesForm.getArticulos("totalPedidos_"+i).toString()) - totalConsumos;
			materialesForm.setArticulos("difPedCons_"+i, difPedCons+"");
			
			if(total==0&&!UtilidadTexto.getBoolean(materialesForm.getArticulos("estaBd_"+i).toString()))
				codigosEliminar.add(Integer.parseInt(materialesForm.getArticulos("codigoArticulo_"+i).toString()));
		}
		
		logger.info("codigos a eliminar encontrados: "+codigosEliminar.size());
		for(Integer codigoArticulo:codigosEliminar)
		{
			int posicionArticulo = ConstantesBD.codigoNuncaValido;
			for(int i=0;i<materialesForm.getNumArticulos();i++)
				if(Integer.parseInt(materialesForm.getArticulos("codigoArticulo_"+i).toString())==codigoArticulo.intValue())
					posicionArticulo = i;
			
			if(posicionArticulo!=ConstantesBD.codigoNuncaValido)
			{
				logger.info("Se eliminará articulo en la posicion: "+posicionArticulo);
				materialesForm.setArticulos(
						MaterialesQx.eliminarRegistroListadoArticulosConsumo(
							materialesForm.getArticulos(), 
							materialesForm.getNumArticulos(), 
							posicionArticulo,
							materialesForm.isPorActo(), 
							materialesForm.getNumPaquetes(), 
							materialesForm.getNumCirugias(), 
							materialesForm.getCirugias()
						)
					);
				materialesForm.setNumArticulos(Integer.parseInt(materialesForm.getArticulos("numRegistros").toString()));
			}
		}
		
		/**
		 * ESTA OPCION POR EL MOMENTO SOLO APLICA PARA CONSUMO POR ACTO,
		 * POR LO TANTO SE REDIRECCIONA DIRECTAMENTE AL FRAME DE ARTICULOS X ACTO
		 */
		
		return mapping.findForward("listadoArticuloXActo");
	}

	/**
	 * Mètodo para añadir los articulos de un paquete al listado del consumo de materiales
	 * @param materialesForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionAgregarPaqueteConsumo(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		Connection con = UtilidadBD.abrirConexion();
		int pos = materialesForm.getNumPaquetes();
		int numArticulos = materialesForm.getNumArticulos();
		
		
		//Se consultan los artículos del paquetes
		HashMap artPaquetes = HojaGastos.consultarArticulosXConsecutivo(con, materialesForm.getPaquetes("consecutivo_"+pos).toString());
		
		
		//****************************************POR ACTO*****************************************************************************
		if(materialesForm.isPorActo())
		{
			
			//Se agrega el paquete como insertado 
			String consecutivosPaquetesInsertados = materialesForm.getConsecutivosPaquetesInsertados();
			if(!consecutivosPaquetesInsertados.equals(""))
				consecutivosPaquetesInsertados += ","; 
			consecutivosPaquetesInsertados += materialesForm.getPaquetes("consecutivo_"+pos);
			materialesForm.setConsecutivosPaquetesInsertados(consecutivosPaquetesInsertados);
			
			
			//Se revisan los artículos existentes
			for(int i=0;i<numArticulos;i++)
			{
				for(int j=0;j<Integer.parseInt(artPaquetes.get("numRegistros").toString());j++)
				{
					//Si algun articulo del listado del paquete ya se encuentra en el listado del pedido simplemente se agrega la cantidad al registro
					//
					if(Integer.parseInt(materialesForm.getArticulos("articulo_"+i).toString().split("-")[0])==Integer.parseInt(artPaquetes.get("codigoArticulo_"+j).toString()))
					{
						materialesForm.setArticulos("consumoActual"+pos+"_"+i, artPaquetes.get("cantidad_"+j));
						artPaquetes.put("insertado_"+j,ConstantesBD.acronimoSi);
						
						//1) Se recalcula el consumo actual del articulo (la cantidad que se ha puesto + la cantidad del paquete)
						String totalString = materialesForm.getArticulos("total_"+i).toString();
						int total = 0, totalConsumos = 0, difPedCons = 0;
						if(!totalString.equals(""))
							total = Integer.parseInt(totalString) + Integer.parseInt(artPaquetes.get("cantidad_"+j).toString());
						else
							total = Integer.parseInt(artPaquetes.get("cantidad_"+j).toString());
						materialesForm.setArticulos("total_"+i, total+"");
						
						//2) Se recalcula el consumo total
						totalConsumos = total + Integer.parseInt(materialesForm.getArticulos("totalConsAnt_"+i).toString());
						materialesForm.setArticulos("totalConsumos_"+i, totalConsumos+"");
						
						//3) Se recalcula la diferencia en pedido y consumo
						difPedCons = Integer.parseInt(materialesForm.getArticulos("totalPedidos_"+i).toString()) - totalConsumos;
						materialesForm.setArticulos("difPedCons_"+i, difPedCons+"");
						
					}
				}
			}
			
			//Se agregan al listado de artículos los artículos del paquete que no estaban
			for(int i=0;i<Integer.parseInt(artPaquetes.get("numRegistros").toString());i++)
			{
				//Si el artículo no ha sido insertado se agrega como artículo nuevo al listado de artículos
				if(!UtilidadTexto.getBoolean(artPaquetes.get("insertado_"+i)+""))
				{
					
					materialesForm.setArticulos("articulo_"+numArticulos, artPaquetes.get("codigoArticulo_"+i)+"-"+artPaquetes.get("descripcionArticulo_"+i));
					materialesForm.setArticulos("unidadMedida_"+numArticulos, artPaquetes.get("unidadMedidaArticulo_"+i));
					materialesForm.setArticulos("codigoArticulo_"+numArticulos, artPaquetes.get("codigoArticulo_"+i));
					materialesForm.setArticulos("total_"+numArticulos, artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("consumoActual"+pos+"_"+numArticulos, artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("totalHistorico_"+numArticulos, "0");
					materialesForm.setArticulos("totalConsAnt_"+numArticulos, "0");
					materialesForm.setArticulos("totalPedidos_"+numArticulos, "0");
					materialesForm.setArticulos("totalConsumos_"+numArticulos, artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("difPedCons_"+numArticulos, "-"+artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("estaBd_"+numArticulos, ConstantesBD.acronimoNo);
					
					//se almacena el artículo como artículo insertado
					String auxS0 = materialesForm.getCodigosArticulosInsertados();
					auxS0 += artPaquetes.get("codigoArticulo_"+i) + ",";
					materialesForm.setCodigosArticulosInsertados(auxS0);
					
					numArticulos++;
				}
			}
			
			materialesForm.setPaquetes("eliminado_"+pos, ConstantesBD.acronimoNo);
			pos++;
			materialesForm.setNumPaquetes(pos);
			materialesForm.setPaquetes("numRegistros", pos+"");
			
			materialesForm.setNumArticulos(numArticulos);
			materialesForm.setArticulos("numRegistros", numArticulos+"");
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoArticuloXActo");
		}
		//***********************************************************************************************************************
		//****************************************POR CIRUGIA*********************************************************************
		else
		{
			//Se verifica si solo hay una ciguia
			if(materialesForm.getNumCirugias()==1)
				materialesForm.setConsecutivoCirugia(materialesForm.getCirugias("consecutivo_0").toString());
			
			//Si no hay consecutivo cirugia se debe solicitar
			if(materialesForm.getConsecutivoCirugia().equals(""))
			{
				UtilidadBD.closeConnection(con);
				materialesForm.setEstado("ingresoConsecutivo");
				return mapping.findForward("listadoArticuloXCirugia");
			}
			//Si hay consecutivo cirugia se postulan los articulos para el servicio indicado
			else
			{
				//Se agrega el paquete como 
				String consecutivosPaquetesInsertados = materialesForm.getConsecutivosPaquetesInsertados();
				if(!consecutivosPaquetesInsertados.equals(""))
					consecutivosPaquetesInsertados += ","; 
				consecutivosPaquetesInsertados += materialesForm.getPaquetes("consecutivo_"+pos);
				materialesForm.setConsecutivosPaquetesInsertados(consecutivosPaquetesInsertados);
				
				
				///Se revisan los artículos existentes
				for(int i=0;i<numArticulos;i++)
				{
					for(int j=0;j<Integer.parseInt(artPaquetes.get("numRegistros").toString());j++)
					{
						//Si algun articulo del listado del paquete ya se encuentra en el listado del pedido simplemente se agrega la cantidad al registro
						//
						if(Integer.parseInt(materialesForm.getArticulos("articulo_"+i).toString().split("-")[0])==Integer.parseInt(artPaquetes.get("codigoArticulo_"+j).toString()))
						{
							//logger.info("llave=> consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+i+": "+materialesForm.getArticulos("consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+i));
							materialesForm.setArticulos("consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+i, Utilidades.convertirAEntero(materialesForm.getArticulos("consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+i)+"",true)+Integer.parseInt(artPaquetes.get("cantidad_"+j)+""));
							//logger.info("llave=> consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+i+": "+materialesForm.getArticulos("consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+i));
							artPaquetes.put("insertado_"+j,ConstantesBD.acronimoSi);
							
							//se agrega el valor total consumo actual
							materialesForm.setArticulos("total_"+i, Integer.parseInt(artPaquetes.get("cantidad_"+j)+"")+ Integer.parseInt(materialesForm.getArticulos("total_"+i)+""));
							
							//se agrega el total de consumos
							materialesForm.setArticulos("totalConsumos_"+i, Integer.parseInt(materialesForm.getArticulos("totalConsAnt_"+i)+"")+ Integer.parseInt(materialesForm.getArticulos("total_"+i)+""));
							
							//se agrega la diferencia en pedido y consumo total
							materialesForm.setArticulos("difPedCons_"+i, Integer.parseInt(materialesForm.getArticulos("totalPedidos_"+i)+"")- Integer.parseInt(materialesForm.getArticulos("totalConsumos_"+i)+""));
							
						}
					}
				}
				
				//Se agregan al listado de artículos los artículos del paquete que no estaban
				for(int i=0;i<Integer.parseInt(artPaquetes.get("numRegistros").toString());i++)
				{
					//Si el artículo no ha sido insertado se agrega como artículo nuevo al listado de artículos
					if(!UtilidadTexto.getBoolean(artPaquetes.get("insertado_"+i)+""))
					{
						
						
						materialesForm.setArticulos("articulo_"+numArticulos,artPaquetes.get("codigoArticulo_"+i)+"-"+artPaquetes.get("descripcionArticulo_"+i));
						materialesForm.setArticulos("unidadMedida_"+numArticulos,artPaquetes.get("unidadMedidaArticulo_"+i));
						materialesForm.setArticulos("codigoArticulo_"+numArticulos,artPaquetes.get("codigoArticulo_"+i));
						materialesForm.setArticulos("totalPedidos_"+numArticulos,"0");
						materialesForm.setArticulos("totalConsAnt_"+numArticulos,"0");
						materialesForm.setArticulos("total_"+numArticulos,artPaquetes.get("cantidad_"+i));
						materialesForm.setArticulos("estaBd_"+numArticulos, ConstantesBD.acronimoNo);
						materialesForm.setArticulos("consumoActual"+materialesForm.getConsecutivoCirugia()+"_"+numArticulos, artPaquetes.get("cantidad_"+i));
						materialesForm.setArticulos("totalConsumos_"+numArticulos,artPaquetes.get("cantidad_"+i));
						materialesForm.setArticulos("difPedCons_"+numArticulos,-Integer.parseInt(artPaquetes.get("cantidad_"+i)+""));
						//se almacena el artículo como artículo insertado
						String auxS0 = materialesForm.getCodigosArticulosInsertados();
						auxS0 += artPaquetes.get("codigoArticulo_"+i) + ",";
						materialesForm.setCodigosArticulosInsertados(auxS0);
						
						numArticulos++;
					}
				}
				
				pos++;
				materialesForm.setNumPaquetes(pos);
				materialesForm.setPaquetes("numRegistros", pos+"");
				
				materialesForm.setNumArticulos(numArticulos);
				materialesForm.setArticulos("numRegistros", numArticulos+"");
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoArticuloXCirugia");
				
				
			}
		}
		//**********************************************************************************************************************
		
	}

	/**
	 * Método que agrega un paquete al listado de artículos
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAgregarPaquete(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		Connection con = UtilidadBD.abrirConexion();
		int pos = materialesForm.getNumPaquetes();
		int numArticulos = materialesForm.getNumArticulos();
		
		//Se verifica que el consecutivo del paquete llegue en buenas condiciones
		if(materialesForm.getPaquetes("consecutivo_"+pos)!=null)
		{
		
			//Se consultan los artículos del paquetes
			HashMap artPaquetes = HojaGastos.consultarArticulosXConsecutivo(con, materialesForm.getPaquetes("consecutivo_"+pos).toString());
			
			//Se agrega el paquete como 
			String consecutivosPaquetesInsertados = materialesForm.getConsecutivosPaquetesInsertados();
			if(!consecutivosPaquetesInsertados.equals(""))
				consecutivosPaquetesInsertados += ","; 
			consecutivosPaquetesInsertados += materialesForm.getPaquetes("consecutivo_"+pos);
			materialesForm.setConsecutivosPaquetesInsertados(consecutivosPaquetesInsertados);
			
			//Se revisan los artículos existentes
			for(int i=0;i<numArticulos;i++)
			{
				for(int j=0;j<Integer.parseInt(artPaquetes.get("numRegistros").toString());j++)
				{
					//Si algun articulo del listado del paquete ya se encuentra en el listado del pedido simplemente se agrega la cantidad al registro
					//
					if(Integer.parseInt(materialesForm.getArticulos("codigoArticulo_"+i).toString())==Integer.parseInt(artPaquetes.get("codigoArticulo_"+j).toString()))
					{
						materialesForm.setArticulos("cantidadPaquete_"+i+"_"+pos, artPaquetes.get("cantidad_"+j));
						artPaquetes.put("insertado_"+j,ConstantesBD.acronimoSi);
						
						//Se recalcula la cantidad del articulo (la cantidad que se ha puesto + la cantidad del paquete)
						//Nota * Se usa utilidade.convertirAEntero porque pueden que las cantidades vengan vacías
						int cantidad = Utilidades.convertirAEntero(materialesForm.getArticulos("cantidad_"+i).toString(),true) + Utilidades.convertirAEntero(artPaquetes.get("cantidad_"+j).toString(),true);
						int total = cantidad + Utilidades.convertirAEntero(materialesForm.getArticulos("totalPedidosAnt_"+i).toString(),true);
						
						materialesForm.setArticulos("cantidad_"+i, cantidad+"");
						materialesForm.setArticulos("total_"+i, total+"");
					}
				}
			}
			
			//Se agregan al listado de artículos los artículos del paquete que no estaban
			for(int i=0;i<Integer.parseInt(artPaquetes.get("numRegistros").toString());i++)
			{
				//Si el artículo no ha sido insertado se agrega como artículo nuevo al listado de artículos
				if(!UtilidadTexto.getBoolean(artPaquetes.get("insertado_"+i)+""))
				{
					materialesForm.setArticulos("codigoArticulo_"+numArticulos, artPaquetes.get("codigoArticulo_"+i));
					materialesForm.setArticulos("articulo_"+numArticulos, artPaquetes.get("codigoArticulo_"+i)+"-"+artPaquetes.get("descripcionArticulo_"+i));
					materialesForm.setArticulos("unidadMedidaArticulo_"+numArticulos, artPaquetes.get("unidadMedidaArticulo_"+i));
					materialesForm.setArticulos("cantidadPaquete_"+numArticulos+"_"+pos, artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("totalPedidosAnt_"+numArticulos, "0");
					materialesForm.setArticulos("total_"+numArticulos, artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("cantidad_"+numArticulos, artPaquetes.get("cantidad_"+i));
					materialesForm.setArticulos("existeBd_"+numArticulos, ConstantesBD.acronimoNo);
					
					//se almacena el artículo como artículo insertado
					String auxS0 = materialesForm.getCodigosArticulosInsertados();
					auxS0 += materialesForm.getArticulos("codigoArticulo_"+numArticulos) + ",";
					materialesForm.setCodigosArticulosInsertados(auxS0);
					
					numArticulos++;
				}
			}
			
			pos++;
			materialesForm.setNumPaquetes(pos);
			materialesForm.setPaquetes("numRegistros", pos+"");
			
			materialesForm.setNumArticulos(numArticulos);
			materialesForm.setArticulos("numRegistros", numArticulos+"");
		}
	
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoArticulos");
	}

	/**
	 * Método implementado para realizar la impresión de materiales
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionImprimirMateriales(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		Connection con = UtilidadBD.abrirConexion();
		
		///se toma el rptdesign maestro
		String nombreRptDesignMaestro="ingresoConsumoMateriales.rptdesign";
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE
        DesignEngineApi comp; 
        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"salasCirugia/",nombreRptDesignMaestro);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,0, "INGRESO CONSUMO MATERIALES QX.");
        
       //SE INSERTA EL DETALLE DE LOS ARTICULOS****************************************************
        v = new Vector();
        int numCirugias = materialesForm.getNumCirugias();
        int numCols = 3 + numCirugias;
        int numRows = 0;
        
        //1) Edicion de los encabezados
        v.add("Artículo");
        v.add("Cantidad");
        for(int i=0;i<numCirugias;i++)
        {
        	v.add("Cx "+materialesForm.getCirugias("consecutivo_"+i));
        }
        v.add("Total");
        numRows ++;
        
        //2) Edicion del cuerpo
        for(int i=0;i<materialesForm.getNumArticulos();i++)
        {
        	//descripcion del artículo
        	v.add(materialesForm.getArticulos("articulo_"+i)+
        		" Conc:"+materialesForm.getArticulos("concentracion_"+i)+
        		" F.F:"+materialesForm.getArticulos("formaFarmaceutica_"+i)+
        		" U.M:"+materialesForm.getArticulos("unidadMedida_"+i)+
        		" Nat:"+materialesForm.getArticulos("naturaleza_"+i));
        	//cantidad del articulo
        	v.add(materialesForm.getArticulos("cantidad_"+i));
        	//Cantidades de las cirugias
        	for(int j=0;j<numCirugias;j++)
            {
            	v.add(materialesForm.getArticulos("cantidadDespacho"+materialesForm.getCirugias("consecutivo_"+j)+"_"+i));
            }
        	//Total
        	v.add(materialesForm.getArticulos("total_"+i));
        	numRows++;
        }
        
        //3) Insertar grilla con los datos 
        // se crea la grilla con las dimensiones
        comp.insertTableInBodyPageWithName(numCols,numRows,"tablaArticulos");
        // se llena la grilla con los datos
        comp.insertLabelInTableOfBodyPage(v,"tablaArticulos");
        //se ponde negrilla a la primera fila de la grilla
        comp.fontWeightRowTableBodyPage(0,DesignChoiceConstants.FONT_WEIGHT_BOLD,"tablaArticulos");
        //Se define el ancho de la primera columna (en pulgadas)
        comp.colWidthTableBodyPage(0,"3.5","tablaArticulos");
        comp.borderTableBodyPage(DesignChoiceConstants.LINE_WIDTH_THIN,"tablaArticulos");
        /**comp.colWidthGridBodyPage(5,0,1,"15");
        comp.colWidthGridBodyPage(5,0,2,"15");
        comp.colWidthGridBodyPage(5,0,3,"15");
        comp.colWidthGridBodyPage(5,0,4,"15");**/
        
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        newPathReport += "&numeroSolicitud="+materialesForm.getOrden();
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        this.cerrarConexion(con);
        return mapping.findForward("resumenMateriales");
	}

	/**
	 * Método implementado para consultar los almacenes al seleccionar otro centro
	 * de costo en la vista de listadoAlmacenes
	 * @param mapping
	 * @param materialesForm
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionNuevoCentroCosto(ActionMapping mapping, MaterialesQxForm materialesForm, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//se reinician datos del listado de subalmacenes
		materialesForm.setNumAlmacenes(0);
		materialesForm.setListaAlmacenes(new HashMap());
		
		//se verifica que sea centro costo válido
		if(materialesForm.getCentroCosto()>0)
		{
			Connection con = UtilidadBD.abrirConexion();
			materialesForm.setNombreCentroCosto(Utilidades.obtenerNombreCentroCosto(con, materialesForm.getCentroCosto(), usuario.getCodigoInstitucionInt()));
			UtilidadBD.closeConnection(con);
			//se carga la nueva lista de subalmacenes
			ActionErrors errores = this.cargarListaAlmacenes(materialesForm,usuario,materialesForm.getCentroCosto(),new ActionErrors());
			if(!errores.isEmpty())
				saveErrors(request, errores);
			
		}
		String ruta = request.getRequestURI();
		
		
		//según el path se define cual forward lanzar según funcionalidad
		if(ruta.endsWith("ingresarModificar.do"))
			return mapping.findForward("confirmacionConsumo");
		else
			return mapping.findForward("listadoAlmacenes");
	}

	/**
	 * Método que postula la vista inicial de pedido Qx. para la selección
	 * de un centro de costo y de un almacen válidos
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionSeleccionAlmacen(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
    	int auxI0 = 0;
    	
    	//se limpia formulario;
    	materialesForm.setInstitucion(usuario.getCodigoInstitucion());
    	materialesForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
    	
    	materialesForm.setTipoTransaccionPedido(ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(), true));
    	
    	
    	
    	
    	///Se consultan si ya existen pedidos para la peticion para saltar esta parte
    	Connection con=UtilidadBD.abrirConexion();
    	
    	HashMap centrosCostoPedido = MaterialesQx.consultarCentrosCostoPedidosPeticion(con, materialesForm.getNumeroPeticion());
    	
    	if(Integer.parseInt(centrosCostoPedido.get("numRegistros").toString())>0)
    	{
    		materialesForm.setTienePedido(true);
    		
    		materialesForm.setCentroCosto(Integer.parseInt(centrosCostoPedido.get("centroCosto").toString()));
    		materialesForm.setNombreCentroCosto(centrosCostoPedido.get("nombreCentroCosto").toString());
    		materialesForm.setFarmacia(Integer.parseInt(centrosCostoPedido.get("farmacia").toString()));
    		materialesForm.setNombreFarmacia(centrosCostoPedido.get("nombreFarmacia").toString());
    	}
    	else
    	{
    		materialesForm.setFarmacia(ConstantesBD.codigoNuncaValido);
    		materialesForm.setCentroCosto(usuario.getCodigoCentroCosto());
    		materialesForm.setNombreCentroCosto(Utilidades.obtenerNombreCentroCosto(con, materialesForm.getCentroCosto(), usuario.getCodigoInstitucionInt()));
    		materialesForm.setTienePedido(false);
    	}
    	
    	UtilidadBD.closeConnection(con);
    	
    	//Si la peticion ya tenía pedidos entonces se prosigue con la misma farmacia de los pedidos anteriores
    	/**if(materialesForm.isTienePedido()&&materialesForm.getFarmacia()>0)
    		return accionEmpezarPedido(materialesForm, mapping, usuario); **/
    	
    	logger.info("codigo farmacia encontrada=> "+materialesForm.getFarmacia());
    	
    	//Se verifica que el tipo de transaccion pedido se encuentre
    	//definida en parametros generales
    	if(!materialesForm.getTipoTransaccionPedido().equals(""))
    	{
	    	//***SE VERIFICA SI HAY CENTROS DE COSTO VALIDOS PARA SOLICITUD PACIENTES**********
	    	Vector restricciones= new Vector();
	        restricciones.add(0, materialesForm.getTipoTransaccionPedido());
	        //se carga el registro de la transaccion válidad para todos los centros costo
	        //y con el tipo transaccion SOLICITUD PACIENTES
	        HashMap mapaTransValidas=UtilidadInventarios.transaccionesValidasCentroCosto( 
	        		usuario.getCodigoInstitucionInt(), 
					materialesForm.getCentroCosto(), 
					restricciones, true);
	        //se asigna tamaño del mapa
	        auxI0 = Integer.parseInt(mapaTransValidas.get("numRegistros").toString());
	        materialesForm.setNumCentrosCosto(auxI0);
	        if(auxI0<=0)
	        {
	        	ActionErrors errores = new ActionErrors(); 
	        	errores.add("", new ActionMessage("error.inventarios.centroCostoSinTransVal",materialesForm.getNombreCentroCosto(),"solicitudes de pacientes"));
	            saveErrors(request, errores);
	        }
			else
	        {
	        	///***SE VERIFICA SI EL CENTRO DE COSTO DEL USUARIO TIENE TRANSACCION VALIDA****
	        	ActionErrors errores = this.cargarListaAlmacenes(materialesForm,usuario,materialesForm.getCentroCosto(),new ActionErrors());
	        	if(!errores.isEmpty())
	        		saveErrors(request, errores);
	            //*************************************************************************************************	
	            
	        }
    	}
    	else
    	{
    		ActionErrors errores = new ActionErrors(); 
            errores.add("sin definir tipo transaccion", new ActionMessage("error.inventarios.sinDefinirTipoTransaccion","DE SOLICITUD PACIENTES"));
            saveErrors(request, errores);
    		
    	}
        
    	
    	return mapping.findForward("listadoAlmacenes");
	}

	/**
	 * Método implementado para cargar la lista de almacenes
	 * @param materialesForm
	 * @param usuario
	 * @param centroCosto
	 * @param request
	 */
	private ActionErrors cargarListaAlmacenes(MaterialesQxForm materialesForm, UsuarioBasico usuario, int centroCosto, ActionErrors errores) 
	{
       materialesForm.setListaAlmacenes(UtilidadInventarios.listadoAlmacenes(usuario.getCodigoInstitucionInt(), materialesForm.getCentroCosto(), materialesForm.getTipoTransaccionPedido(), "", false));
       //se asigna tamaño del listado
        materialesForm.setNumAlmacenes(Integer.parseInt(materialesForm.getListaAlmacenes("numRegistros")+""));
       
       ///se verifica si hay almacenes
    	if(materialesForm.getNumAlmacenes()<=0)
    		errores.add("", new ActionMessage("error.inventarios.centroCostoSinFarmacia",materialesForm.getNombreCentroCosto(),""));
    	
        
        
		return errores;
	}

	/**
	 * Método implementado para mostrar el resumen del consumo de materiales
	 * desde otras funcionalidades
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionResumen(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		
		
		//se toma el parámetro general **************************************************************
		
		materialesForm.setPorActo(UtilidadTexto.getBoolean(ValoresPorDefecto.getMaterialesPorActo(usuario.getCodigoInstitucionInt())));
		
		//se preparan los datos********************************************************************
		MaterialesQx materiales= new MaterialesQx();
		materiales.setNumeroSolicitud(materialesForm.getOrden());
		
		Connection con = UtilidadBD.abrirConexion();
		
		//se carga la información de Materiales Qx *********************************
		this.cargarInformacionMateriales(con,materiales,materialesForm,usuario,true);
		
		
		this.cerrarConexion(con);
		
		return mapping.findForward("resumenMateriales");
	}

	/**
	 * Método implementado para hacer la paginación de las cantidades de
	 * los articulos en cada cirugia
	 * @param materialesForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(MaterialesQxForm materialesForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			response.sendRedirect(materialesForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.warn("Error en la paginación de cantidades por cirugia");
			materialesForm.reset(false);
			request.setAttribute("codigoDescripcionError", "errors.problemasDatos");
			return mapping.findForward("paginaError");
		}
	}

	/**
	 * Método implementado para ingresar a la BD la información de consumo
	 * de materiales Qx.
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarMateriales(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws IPSException 
	{
		//se instancia objeto de materiales Qx**********
		ActionErrors errores = new ActionErrors();
		MaterialesQx materiales = new MaterialesQx();
		MaterialesQx materialesAntiguos = new MaterialesQx();
		
		
		
		this.llenarMundoMateriales(materialesForm,materiales,usuario);
		materialesForm.setExistenCantidadesPendientes(false);
		Connection con = UtilidadBD.abrirConexion();
		
		//*****************VALIDACIONES AL FINALIZAR***************************************************
		/**if(UtilidadTexto.getBoolean(materialesForm.getFinalizado()))
		{
			materialesForm.setGenerarPedidoXConsumo(ConstantesBD.acronimoSi);
		}**/
		
		
		
		
		if(UtilidadTexto.getBoolean(materialesForm.getFinalizado())&&!materialesForm.isValidacionConfirmacionConsumoFinalizacion())
		{
			materialesForm.setValidacionConfirmacionConsumoFinalizacion(true);
			
			//Se verifica si se debe realizar pedido qx por cantidades que faltan por pedir
			materialesForm.setExistenCantidadesPendientes(existenCantidadesPendientesParaPedido(materialesForm));
			
			//Se verifica si ya se finalizó la hoja de anestesia
			materialesForm.setExisteHojaAnesSinFinalizar(materiales.existeHojaAnestesiaSinFinalizar(con));
			
			//Se activa la generacion de pedido x consumo
			materialesForm.setGenerarPedidoXConsumo(ConstantesBD.acronimoSi);
			
			//Si cumple alguna de las validaciones se manda al forward de confirmacion
			if(materialesForm.isExisteHojaAnesSinFinalizar())
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("confirmacionConsumo");
			}
				
		}
		
		//Validación de generación de pedido
		if(
			(
				(!UtilidadTexto.getBoolean(materialesForm.getFinalizado())&&UtilidadTexto.getBoolean(materialesForm.getGenerarPedidoXConsumo())&&UtilidadTexto.getBoolean(materialesForm.getTerminado()))
				||
				(UtilidadTexto.getBoolean(materialesForm.getFinalizado())&&materialesForm.isExistenCantidadesPendientes())
				
			)
			&&
			materialesForm.getFarmacia()==ConstantesBD.codigoNuncaValido)
		{
			errores.add("", new ActionMessage("errors.required","Debido a que se solicita generar pedido, la farmacia"));
			saveErrors(request, errores);
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		//***********************************************************************************************************
		
		
		//SE CARGAN DATOS ANTIGUOS *****************************************
		///si el ingreso de materiales ya existía quiere decir que hay modificación
		//por lo tanto, se cargan los datos antiguos para el LOG
		if(materialesForm.isExiste())
		{
			materialesAntiguos.setNumeroSolicitud(materialesForm.getOrden());
			//se carga encabezado
			materialesAntiguos.cargarDatosGenerales(con);
			//se cargan las cirugias
			materialesAntiguos.cargarCirugiasPorActo(con);
			//se cargan los articulos anteriores
			//se verifica si es Por Acto o Por Cirugia para cargar los artículos desde materiales
			if(materialesAntiguos.isEsActo())
				//se cargan artículos en el caso de que ya se encuentren parametrizados
				materialesAntiguos.cargarArticulosMaterialesQx(con);
			else
				//se cargan artículos ya existente en materiales Qx por Cirugia
				materialesAntiguos.cargarArticulosPorCirugia(con);
		}
		
		
		
		//SE CARGAN LAS CIRUGIAS**********************************
		materiales.cargarCirugiasPorActo(con);
		
		//INSERCIÓN DE ENCABEZADO ********************************
		int resp1=0;
		if(materialesForm.isExiste())
			//si ya existe se modifica
			resp1=materiales.modificarEncabezadoMaterialesQx(con,ConstantesBD.inicioTransaccion,usuario);
		else
			//si no existe se inserta uno nuevo
			resp1=materiales.insertarEncabezadoMaterialesQx(con,ConstantesBD.inicioTransaccion,usuario);
		
		if(resp1<=0)
			errores.add("",new ActionMessage("errors.ingresoDatos","la información del encabezado del consumo"));
		
		
		
		//REALIZACION DEL PEDIDO ******************************************
		//Se limpia la llave numeros_pedidos
		for(int j=0;j<materialesForm.getNumArticulos();j++)
    	{
    		//SE reinicia el numero de pedidos de cada articulo
    		materialesForm.setArticulos("numeros_pedidos_"+j,"");
    	}
		//Se verifica si se debe generar el pedido
		if(
				(!UtilidadTexto.getBoolean(materialesForm.getFinalizado())&&UtilidadTexto.getBoolean(materialesForm.getGenerarPedidoXConsumo())&&UtilidadTexto.getBoolean(materialesForm.getTerminado()))
				||
				(UtilidadTexto.getBoolean(materialesForm.getFinalizado())&&materialesForm.isExistenCantidadesPendientes())
			)
		{
			errores = generacionPedidoQxEnConsumo(con,materialesForm,errores,usuario);
		}
		
		///INSERCIÓN DE ARTÍCULOS ********************************
		//Mt 5915 Se agrega nuevo parametro para enviar almacen
		/*errores = materiales.guardarArticulosConsumo(con, errores,materialesForm.getListaAlmacenes());*/
		
		/**
		 * MT 6832
		 * @author javrammo
		 * Ya no es necesario enviar los almacenes. Para mAs detalle
		 * @see SqlBaseMaterialesQxDao#guardarArticulosConsumo(Connection con,HashMap campos) 
		 */
		errores = materiales.guardarArticulosConsumo(con, errores);
		
		//***********se verifica resultado de la transacción***************************************************
        if(errores.isEmpty())
        {
        	UtilidadBD.finalizarTransaccion(con);
        	//REALIZACIÓN DE LA LIQUIDACIÓN AUTOMÁTICA *************************************************************
			if(UtilidadTexto.getBoolean(materialesForm.getFinalizado()))
			{
				UtilidadBD.iniciarTransaccion(con);
				LiquidacionServicios mundoLiquidacionServicios = new LiquidacionServicios();
				mundoLiquidacionServicios.realizarLiquidacion(con, materiales.getNumeroSolicitud()+"", usuario, true);
				
				//Se muestran las advertencias del proceso de liquidación si las hay
	        	errores = mundoLiquidacionServicios.llenarMensajesError(errores);
	        	if(!errores.isEmpty())
	        		UtilidadBD.abortarTransaccion(con);
	        	else
	        		UtilidadBD.finalizarTransaccion(con);
			}
			//**************************************************************************************************	
        	
			////*************************** JUSTIFICACION NO POS ************************************
        	errores=validarInsertarJustificacion(con, usuario, materiales.getNumeroSolicitud(), materialesForm, errores);
        	//************************FIN JUSTIFICACION NO POS *****************************************
        	//*******************MENSAJE PARA SOLICITAR EL DESPACHO DEL PEDIDO QX*********************
        	if(UtilidadTexto.getBoolean(materialesForm.getFinalizado())||(UtilidadTexto.getBoolean(materialesForm.getGenerarPedidoXConsumo())&&UtilidadTexto.getBoolean(materialesForm.getTerminado())))
        	{
        		if(UtilidadTexto.getBoolean(materialesForm.getFinalizado()))
        		{
        			String obtenerFarmacias = materiales.obtenerFarmaciasPedidosPeticion(con, materialesForm.getPeticion());
        			if(!obtenerFarmacias.equals(""))
        			{
        				errores.add("",new ActionMessage("errors.notEspecific","FAVOR SOLICITAR EL DESPACHO DEL PEDIDO QUIRURGICO A LAS FARMACIAS: "+obtenerFarmacias));
        			}
        		}
        		else
        		{
        			errores.add("",new ActionMessage("errors.notEspecific","FAVOR SOLICITAR EL DESPACHO DEL PEDIDO QUIRURGICO A LA FARMACIA: "+(materialesForm.getNombreFarmacia().equals("")?Utilidades.obtenerNombreCentroCosto(con, materialesForm.getFarmacia(), usuario.getCodigoInstitucionInt()):materialesForm.getNombreFarmacia())));
        		}
        		
        	}
        	//****************************************************************************************
        	
        	if(!errores.isEmpty())
        	{
        		saveErrors(request, errores);
        	}
        	
        	//se limpia la informaciòn conservando los datos
        	boolean esPorActo = materialesForm.isPorActo();
        	boolean existe = materialesForm.isExiste();
        	boolean conMenu = materialesForm.isConMenu();
        	String peticion = materialesForm.getPeticion();
        	String estadoPeticion = materialesForm.getEstadoPeticion();
        	String fechaPeticion = materialesForm.getFechaPeticion();
        	String horaPeticion = materialesForm.getHoraPeticion();
        	
        	materialesForm.reset(false);
        	materialesForm.setPeticion(peticion);
        	materialesForm.setEstadoPeticion(estadoPeticion);
        	materialesForm.setFechaPeticion(fechaPeticion);
        	materialesForm.setHoraPeticion(horaPeticion);
        	materialesForm.setPorActo(esPorActo);
        	materialesForm.setConMenu(conMenu);
        	materialesForm.setOrden(materiales.getNumeroSolicitud());
        	
        	//se carga de nuevo la informacion
        	this.cargarInformacionMateriales(con,materiales,materialesForm,usuario,false);
        	
        	
        	///se verifica si el registro de materiales ya existe para
        	//generar el LOG de modificacion
        	if(existe)
        		this.generarLog(materiales,materialesAntiguos);
        	
        	
        	
			this.cerrarConexion(con);
        	return mapping.findForward("resumenMateriales");
        }
        else
        {
        	UtilidadBD.abortarTransaccion(con);
        	saveErrors(request, errores);
			this.cerrarConexion(con);
			return mapping.findForward("principal");
        }
	}

	
	
	/**
	 * 
	 * @param con
	 * @param user
	 * @param numeroSolicitud
	 * @param cargosForm
	 * @param errores 
	 * @return
	 */
	private ActionErrors validarInsertarJustificacion(Connection con, UsuarioBasico user, int numeroSolicitud, MaterialesQxForm forma, ActionErrors errores) throws IPSException
	{
		
		int codigoArticulo = ConstantesBD.codigoNuncaValido;
		int cantidad = ConstantesBD.codigoNuncaValido;
		int codigoConvenio = ConstantesBD.codigoNuncaValido;
		
		for (int i=0; i<Integer.parseInt(forma.getArticulos().get("numRegistros").toString()); i ++)
	    {    
		  try
            {
                codigoArticulo= Integer.parseInt(forma.getArticulos().get("codigoArticulo_"+i)+"");
                cantidad = Integer.parseInt(forma.getArticulos().get("totalConsumos_"+i)+"");
                logger.info(" \n\n\n\n INFO - ARTICULO JUS PENDIENTE");
                logger.info(" Articulo-> "+codigoArticulo);
                logger.info(" Cantidad-> "+cantidad);
                logger.info(" Solicitud-> "+numeroSolicitud);
                /*Cambio incluido por la Tarea 59745; para la cual necesitamos tomar 
                el codigo del convenio para poder validar la justificación NO POS*/ 
                codigoConvenio = Cargos.obtenerCodigoConvenioDetalleCargo(con, codigoArticulo, numeroSolicitud, true);
                if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoArticulo, true, true, codigoConvenio))
        		{
                	double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, codigoArticulo, numeroSolicitud, codigoConvenio, true);
                	if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoArticulo, cantidad, user.getLoginUsuario(), true, true, Utilidades.convertirAEntero(subcuenta+""),""))
        				errores.add("ARTICULO NO POS", new ActionMessage("errors.required","ARTÍCULO ["+forma.getArticulos("articulo_"+i)+"] DE NATURALEZA NO POS REQUIERE JUSTIFICACIÓN"));
        		}
            }
            catch(NumberFormatException e)
            {
                logger.warn("Error no se pudo validar e insertar justificacion pendiente del codigo del articulo "+codigoArticulo+" con indice ="+i +"   error-->"+e);
                return errores;
            }
	    }
		forma.setJustificacionNoPosMap("numRegistros",Integer.parseInt(forma.getArticulos().get("numRegistros").toString()));
		return errores;
	}
	
	
	
	
	/**
	 * Método implementado para la generación del pedido en la finalización del consumo
	 * @param con
	 * @param materialesForm
	 * @param errores
	 * @param usuario 
	 * @return
	 */
	private ActionErrors generacionPedidoQxEnConsumo(Connection con, MaterialesQxForm materialesForm, ActionErrors errores, UsuarioBasico usuario) 
	{
		//********objetos que se van a usar****************************************************************
		PedidosInsumos pedidos= new PedidosInsumos();
        
      
        
        int contador=0;
        
        //*****VALIDACION DE CIERRE DE INVENTARIOS*****************
        if(UtilidadInventarios.existeCierreInventarioParaFecha(materialesForm.getFecha(),usuario.getCodigoInstitucionInt()))
        {
        	//si existe cierre hay error!!! 
           errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",materialesForm.getFecha()));
           
        }
        else
        {
		
	        //*************se inserta el pedido con su detalle ******************************************************************
        	//*******verificación de los artículos *************************************************************
        	//CASO 1) CUANDO SE FINALIZA EL CONSUMO Y EXISTEN CANTIDADES PENDIENTES
        	if(UtilidadTexto.getBoolean(materialesForm.getFinalizado())&&materialesForm.isExistenCantidadesPendientes())
        	{
        		ArrayList arrayListCodigos = new ArrayList (); 
                ArrayList arrayListCantidad = new ArrayList ();
				//se toman los articulos cuya diferencia de TOTAL CONSUMOS - TOTAL PEDIDOS es > 0
		        for(int i=0;i<materialesForm.getNumArticulos();i++)
		        {
		        	int cantidad=0;
		        	try
					{
		        		cantidad = Integer.parseInt(materialesForm.getArticulos("totalConsumos_"+i)+"") - Integer.parseInt(materialesForm.getArticulos("totalPedidos_"+i)+"") -  Integer.parseInt(materialesForm.getArticulos("total_"+i)+"");
					}
		        	catch(Exception e)
					{
		        		cantidad=0;
					}
		        	if(cantidad>0)
		        	{
		                arrayListCodigos.add(contador,materialesForm.getArticulos("articulo_"+i).toString().split("-")[0]);
		                arrayListCantidad.add(contador,cantidad);
		                contador++;
		        	}
		        }
		        
		        if(arrayListCodigos.size()>0)
		        {
			        errores = insertarPedido(con,pedidos,materialesForm,arrayListCodigos,arrayListCantidad,errores,usuario,"el pedido quirúrgico en la finalización del consumo");
			        
			        
		        }
        	}
        	//CASO 2) CUANDO SE REGISTRA UN CONSUMO TERMINADO
        	contador = 0;
    		ArrayList arrayListCodigos = new ArrayList (); 
            ArrayList arrayListCantidad = new ArrayList ();
    		//Se toma el consumo actual de los articulos
    		for(int i=0;i<materialesForm.getNumArticulos();i++)
	        {
	        	int cantidad=0;
	        	try
				{
	        		cantidad = Integer.parseInt(materialesForm.getArticulos("total_"+i)+"");
				}
	        	catch(Exception e)
				{
	        		cantidad=0;
				}
	        	if(cantidad>0)
	        	{
	                arrayListCodigos.add(contador,materialesForm.getArticulos("articulo_"+i).toString().split("-")[0]);
	                arrayListCantidad.add(contador,cantidad);
	                contador++;
	        	}
	        	
	        }
    		errores = insertarPedido(con,pedidos,materialesForm,arrayListCodigos,arrayListCantidad,errores,usuario,"el pedido quirúrgico para el consumo actual");
	        
	        //*************************************************************************************
	        
	        
        }
        
        return errores;
        
	}



	/**
	 * Método implementado para insertar un pedido quirurgico
	 * @param con
	 * @param pedidos
	 * @param materialesForm
	 * @param arrayListCodigos
	 * @param arrayListCantidad
	 * @param errores
	 * @param usuario
	 * @param mensaje
	 * @return
	 */
	private ActionErrors insertarPedido(Connection con, PedidosInsumos pedidos,
			MaterialesQxForm materialesForm, ArrayList arrayListCodigos,
			ArrayList arrayListCantidad, ActionErrors errores, UsuarioBasico usuario,String mensaje) 
	{
		int resp = 0;
		int resp1 = 0;
		int resp2 = 0;
		boolean esUrgente=false;
        boolean esTerminado=true;
		
		try
		{
	        resp=pedidos.insertarPedidoYDetalle(con,
		               materialesForm.getCentroCosto(),
		               materialesForm.getFarmacia(),
		               esUrgente,
		               "",
		               usuario,
		               esTerminado,
		               arrayListCodigos,
		               arrayListCantidad,
		               ConstantesBD.continuarTransaccion,
		               UtilidadFecha.getFechaActual(con),
		               UtilidadFecha.getHoraActual(con),
		               ConstantesBD.acronimoSi, ConstantesBD.acronimoNo); //es quirurgico ?
	        //se toma el número del pedido ********************************************
	       
	    	pedidos.setNumeroPedido(pedidos.siguientePedido(con));
	    	materialesForm.setNumeroPedido(pedidos.getNumeroPedido());
	    	
			
			//se inserta el pedido de la petición
			Peticion peticion = new Peticion();
			peticion.setNumeroPeticion(Integer.parseInt(materialesForm.getPeticion()));
			peticion.setNumeroPedido(materialesForm.getNumeroPedido());
			peticion.setUsuario(usuario.getLoginUsuario());
			resp2=peticion.insertarPedidoPeticion(con);
			
			
	        
	        // se verifica estado de la operación **************************************
	        if(resp<=0||resp1<=0&&resp2<=0)
	        	errores.add("",new ActionMessage("errors.ingresoDatos","el pedido quirúrgico en la finalización del consumo"));
	        else
	        {
	        	
	        	
	        	//SE asigna el número de pedido a los artículos que se les generó pedido
		        for(int i=0;i<arrayListCodigos.size();i++)
		        {
		        	String codigo = (String)arrayListCodigos.get(i);
		        	String numerosPedidos = "";
		        	for(int j=0;j<materialesForm.getNumArticulos();j++)
		        	{
		        		if(codigo.equals(materialesForm.getArticulos("articulo_"+j).toString().split("-")[0]))
		        		{
		        			numerosPedidos = materialesForm.getArticulos("numeros_pedidos_"+j).toString();
		        			numerosPedidos += (numerosPedidos.equals("")?"":",") + materialesForm.getNumeroPedido();
		        			materialesForm.setArticulos("numeros_pedidos_"+j, numerosPedidos);
		        		}
		        	}
		        }
	        }
		}
        catch(SQLException e)
		{
        	resp=0;
        	logger.error("Error en generacionPedidoQxEnConsumo de MaterialesQxAction: "+e);
		}
        
        return errores;
	}

	/**
	 * Método que verifica si existen cantidades pendientes por pedir
	 * @param materialesForm
	 * @return
	 */
	private boolean existenCantidadesPendientesParaPedido(MaterialesQxForm materialesForm) 
	{
		boolean existen = false;
		
		for(int i=0;i<materialesForm.getNumArticulos();i++)
			if(Utilidades.convertirAEntero(materialesForm.getArticulos("difPedCons_"+i)+"", true)<0)
				existen = true;
		
		return existen;
	}

	

	/**
	 * Método que genera el LOG de la modificación de materiales
	 * @param materiales
	 * @param materialesAntiguos
	 */
	private void generarLog(MaterialesQx materiales, MaterialesQx materialesAntiguos) 
	{
		String log="";
	    //***********************************************************
		
		//***************************************************
	    log="\n            ====INFORMACION ORIGINAL DEL INGRESO CONSUMO MATERIALES QX.===== " +
		"\n*  Fecha - Hora [" +materialesAntiguos.getFecha()+" - "+materialesAntiguos.getHora()+"] "+
		"\n*  Centro de Costo ["+materialesAntiguos.getNombreCentroCosto().trim()+"] " +
		"\n*  Usuario ["+materialesAntiguos.getUsuario()+"] " +
		"\n\n   Artículos:";
	    
	   //se iteran los artículos de materiales
    	for(int i=0;i<Integer.parseInt(materialesAntiguos.getArticulos().get("numRegistros")+"");i++)
    	{
    		log+="\n\n*  "+(materialesAntiguos.getArticulos().get("articulo_"+i)+"").trim();
    		
    		//si es Por Acto o si es Por Cirugia
    		
    		log+="\n     Total Pedidos:"+materialesAntiguos.getArticulos().get("totalPedidos_"+i);
    		log+="\n     Total Consumo Anterior:"+materialesAntiguos.getArticulos().get("totalConsAnt_"+i);
    		
    		if(materialesAntiguos.isEsActo())
    			log+="\n     Consumo Actual:"+materialesAntiguos.getArticulos().get("total_"+i);
    		else
    			for(int j=0;j<Integer.parseInt(materialesAntiguos.getCirugias().get("numRegistros")+"");j++)
    				if(!(materialesAntiguos.getArticulos().get("consumoActual"+materialesAntiguos.getCirugias().get("consecutivo_"+j)+"_"+i)+"").equals("")&&
    	    		   !(materialesAntiguos.getArticulos().get("consumoActual"+materialesAntiguos.getCirugias().get("consecutivo_"+j)+"_"+i)+"").equals("0"))
	    				log+="\n    Cx "+materialesAntiguos.getCirugias().get("consecutivo_"+j)+" ["+materialesAntiguos.getCirugias().get("descripcion_"+j)+"]   " +
	    				" ["+materialesAntiguos.getArticulos().get("consumoActual"+materialesAntiguos.getCirugias().get("consecutivo_"+j)+"_"+i)+"]";
    		log+="\n     Total Consumo:"+materialesAntiguos.getArticulos().get("totalConsumos_"+i);
    		
    	}

		//***************************************************
	    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL INGRESO CONSUMO MATERIALES QX.===== " +
	    "\n*  Fecha - Hora [" +materiales.getFecha()+" - "+materiales.getHora()+"] "+
		"\n*  Centro de Costo ["+materiales.getNombreCentroCosto().trim()+"] " +
		"\n*  Usuario ["+materiales.getUsuario()+"] " +
		"\n\n   Artículos:";
	    
	    //se iteran los artículos de materiales
    	for(int i=0;i<Integer.parseInt(materiales.getArticulos().get("numRegistros")+"");i++)
    	{
    		log+="\n\n*  "+(materiales.getArticulos().get("articulo_"+i)+"").trim();
    		
    		//si es Por Acto o si es Por Cirugia
    		log+="\n     Total Pedidos:"+materiales.getArticulos().get("totalPedidos_"+i);
    		log+="\n     Total Consumo Anterior:"+materiales.getArticulos().get("totalConsAnt_"+i);
    		
    		if(materiales.isEsActo())
    			log+="\n     Consumo Actual:"+materiales.getArticulos().get("total_"+i);
    		else
    			for(int j=0;j<Integer.parseInt(materiales.getCirugias().get("numRegistros")+"");j++)
    				if(!(materiales.getArticulos().get("consumoActual"+materiales.getCirugias().get("consecutivo_"+j)+"_"+i)+"").equals("")&&
    				   !(materiales.getArticulos().get("consumoActual"+materiales.getCirugias().get("consecutivo_"+j)+"_"+i)+"").equals("0"))
	    				log+="\n    Cx "+materiales.getCirugias().get("consecutivo_"+j)+" ["+materiales.getCirugias().get("descripcion_"+j)+"]   " +
	    				" ["+materiales.getArticulos().get("consumoActual"+materiales.getCirugias().get("consecutivo_"+j)+"_"+i)+"]";
    		log+="\n     Total Consumo:"+materiales.getArticulos().get("totalConsumo_"+i);
    		
    	}
		
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logIngresoConsumoMaterialesQxCodigo, log, ConstantesBD.tipoRegistroLogModificacion,materiales.getUsuario());
		
	}

	/**
	 * Método implementado para llenar el mundo de materiales con los
	 * datos de la forma
	 * @param materialesForm
	 * @param materiales
	 * @param usuario
	 */
	private void llenarMundoMateriales(MaterialesQxForm materialesForm, MaterialesQx materiales, UsuarioBasico usuario) 
	{
		materiales.setNumeroSolicitud(materialesForm.getOrden());
		materiales.setCentroCosto(materialesForm.getCentroCosto());
		materiales.setFarmacia(materialesForm.getFarmacia());
		materiales.setUsuario(usuario.getLoginUsuario());
		materiales.setFecha(materialesForm.getFecha());
		materiales.setHora(materialesForm.getHora());
		materiales.setEsActo(materialesForm.isPorActo());
		materiales.setArticulos(materialesForm.getArticulos());
		materiales.setCirugias(materialesForm.getCirugias());
		materiales.setTerminado(materialesForm.getTerminado());
		materiales.setFinalizado(materialesForm.getFinalizado());
		materiales.setTipoTransaccionPedido(materialesForm.getTipoTransaccionPedido());
		materiales.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		materiales.setGenerarPedidoXConsumo(UtilidadTexto.getBoolean(materialesForm.getGenerarPedidoXConsumo()));
	}

	/**
	 * Método implementado para ordenar el listado de ordenes médicas en la opcion de
	 * ingreso consumo materiales
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarOrdenes(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"responsable_",
				"numero_solicitud",
				"fecha_orden_",
				"orden_",
				"medico_solicita_",
				"fecha_cirugia_",
				"medico_responde_",
				"estado_pedido_"
			};
		
		int numeroElementos=Integer.parseInt(materialesForm.getOrdenes("numRegistros")+"");
		
		//se pasan las fechas a Formato BD
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (materialesForm.getOrdenes("fecha_orden_"+i)+"").split(" ");
			materialesForm.setOrdenes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaABD(fecha[0])+" "+fecha[1]);
			materialesForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(materialesForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		
		
		materialesForm.setOrdenes(Listado.ordenarMapa(indices,
				materialesForm.getIndice(),
				materialesForm.getUltimoIndice(),
				materialesForm.getOrdenes(),
				numeroElementos));
		
		materialesForm.setOrdenes("numRegistros",numeroElementos+"");
		
		//se pasan las fechas a formato aplicacion
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (materialesForm.getOrdenes("fecha_orden_"+i)+"").split(" ");
			materialesForm.setOrdenes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaAAp(fecha[0])+" "+fecha[1]);
			materialesForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaAAp(materialesForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		materialesForm.setUltimoIndice(materialesForm.getIndice());
		materialesForm.setEstado("empezar");
		
		return mapping.findForward("listadoOrdenes");
	}

	/**
	 * Método implementado para ordenar las peticiones
	 * @param materialesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarPeticiones(MaterialesQxForm materialesForm, ActionMapping mapping) 
	{
		//***se instancia objeto peticiones*********
		Peticiones peticiones = new Peticiones();
		peticiones.setPeticiones(materialesForm.getPeticiones());
		peticiones.setNumPeticiones(materialesForm.getNumPeticiones());
		
		//*se realiza la ordenación de registros****************
		materialesForm.setUltimoIndice(peticiones.ordenarPeticiones(materialesForm.getIndice(),materialesForm.getUltimoIndice()));
		
		return mapping.findForward("listadoPeticion");
	}

	/**
	 * Método implementado para consultar las peticiones de Qx del Paciente
	 * @param materialesForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionListadoPeticion(MaterialesQxForm materialesForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request)
	{
		
		materialesForm.reset(false);
		
		// se consultan las peticiones
		Peticiones peticiones = new Peticiones();
		Connection con = UtilidadBD.abrirConexion(); 
		peticiones.cargarPeticiones(con,paciente.getCodigoPersona());
		this.cerrarConexion(con);
		materialesForm.setPeticiones(peticiones.getPeticiones());
		materialesForm.setNumPeticiones(peticiones.getNumPeticiones());
		
		//se verifica si el numero de peticiones fue 1
		if(materialesForm.getNumPeticiones()==1)
		{
			//se asigna el numero de la única petición
			materialesForm.setNumeroPeticion(materialesForm.getPeticiones()[0].getNumeroPeticion());
			materialesForm.setFechaPeticion(materialesForm.getPeticiones()[0].getFecha());
			materialesForm.setHoraPeticion(materialesForm.getPeticiones()[0].getHora());
			materialesForm.setEstadoPeticion(materialesForm.getPeticiones()[0].getNombreEstado());
			
			//Se envía ala interfaz de la selección del almacen
			return accionSeleccionAlmacen(materialesForm, mapping, usuario, request);
		}
		
		//si existen más peticiones se listan
		return mapping.findForward("listadoPeticion");
	}

	/**
	 * Método implementado para imprimir un pedidoQx. después de ser
	 * ingresado
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionImprimirPedido(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) 
	{
		//se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/pedidoQx" + r.nextInt()  +".pdf";
    	
    	//se instancia formulario de pedidos de insumos
    	PedidosInsumosForm pedidosForm = new PedidosInsumosForm();
    	
    	//se carga el formulario con los datos del pedido Qx.
    	this.llenarForma(materialesForm,pedidosForm);
    	
    	HashMap datosPeticion = new HashMap();
    	datosPeticion.put("paciente", paciente.getApellidosNombresPersona(false));
    	datosPeticion.put("numeroIngreso", materialesForm.getNumeroIngreso());
    	datosPeticion.put("numeroPeticion", materialesForm.getNumeroPeticion());
    	datosPeticion.put("numeroIngreso", materialesForm.getNumeroIngreso());
    	datosPeticion.put("cirugias", materialesForm.getCirugias());
    	datosPeticion.put("numCirugias", materialesForm.getNumCirugias()+"");
    	
    	PedidosInsumosPdf.pdfPedidosInsumos(ValoresPorDefecto.getFilePath() + nombreArchivo, pedidosForm, usuario,datosPeticion, request);
    	
    	request.setAttribute("nombreArchivo", nombreArchivo);
    	request.setAttribute("nombreVentana", "Pedidos Insumos");
    	return mapping.findForward("abrirPdf");
	}

	/**
	 * Método usado para cargar la forma de Pedidos Insumos con los datos de la
	 * forma de Pedidos Qx.
	 * @param materialesForm
	 * @param pedidosForm
	 */
	private void llenarForma(MaterialesQxForm materialesForm, PedidosInsumosForm pedidosForm) 
	{
		pedidosForm.reset();
		pedidosForm.setNumeroPedido(materialesForm.getNumeroPedido());
		pedidosForm.setCheckTerminarPedido(UtilidadTexto.getBoolean(materialesForm.getTerminado())?"on":"off");
		if(materialesForm.getUrgente().equals("Sí"))
			pedidosForm.setCheckPrioridadPedido("on");
		else
			pedidosForm.setCheckPrioridadPedido("off");
		pedidosForm.setFechaHoraPedido(materialesForm.getFecha()+" - "+materialesForm.getHora());
		pedidosForm.setPedidosMap(materialesForm.getArticulos());
		
		//se adicionan llaves del mapa para que sean compatibles con la impresión
		pedidosForm.setPedidosMap("fechaHoraGrabacion_0",materialesForm.getFechaGrabacion()+" - "+materialesForm.getHoraGrabacion());
		
		
		pedidosForm.setNombreCentroCosto(materialesForm.getNombreCentroCosto());
		pedidosForm.setNombreFarmacia(materialesForm.getNombreFarmacia());
		pedidosForm.setNumeroIngresos(materialesForm.getNumArticulos());
		pedidosForm.setObservacionesGenerales("");
		
	}

	/**
	 * Método usado para almacenar los datos del pedido con su detalle de artículos en la BD
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarPedido(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//********objetos que se van a usar****************************************************************
		PedidosInsumos pedidos= new PedidosInsumos();
        ArrayList arrayListCodigos = new ArrayList (); 
        ArrayList arrayListCantidad = new ArrayList ();
        HashMap detallePedido = new HashMap();
        HashMap detalleEliminacion = new HashMap();
        boolean esUrgente=false;
        boolean esTerminado=UtilidadTexto.getBoolean(materialesForm.getTerminado());
        int resp=0,resp1=0,resp2=0,contador=0;
        
        //Informacion que se debe recuperar
        int numeroPeticion = materialesForm.getNumeroPeticion();
        int numeroPedido = materialesForm.getNumeroPedido();
        String fechaPeticion = materialesForm.getFechaPeticion();
        String horaPeticion = materialesForm.getHoraPeticion();
        String estadoPeticion = materialesForm.getEstadoPeticion();
        String numeroIngreso = materialesForm.getNumeroIngreso();
        String terminado = materialesForm.getTerminado();
        
        //*****VALIDACION DE CIERRE DE INVENTARIOS*****************
        if(UtilidadInventarios.existeCierreInventarioParaFecha(materialesForm.getFecha(),usuario.getCodigoInstitucionInt()))
        {
        	//si existe cierre hay error!!!
        	ActionErrors errores = new ActionErrors(); 
           errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",materialesForm.getFecha()));
           saveErrors(request, errores);
           
           return mapping.findForward("ingresoPedido");
        }
        //*********************************************************+
       
        //**************************se asignan datos generales*****************************************************
        if(materialesForm.getUrgente().equals("true"))
        	esUrgente=true;
        
        //*************se inserta el pedido con su detalle ******************************************************************
        Connection con = UtilidadBD.abrirConexion();
        UtilidadBD.iniciarTransaccion(con);
       
        //si el pedido ya existía se realiza modificación, de lo contrario se realiza inserción
        if(materialesForm.getNumeroPedido()>0)
        {
        	//*****************MODIFICACION*****************************************************
        	//*******verificación de los artículos *************************************************************
        	int numDetallePedido = 0;
        	int numDetalleEliminacion = 0;
			//se verifica que a los artículos se les haya ingresado cantidad > 0, de lo contrario no se registran
        	//se verifica si ya existian en la bd para definir su eliminacion en el mapa detalleEliminacion
	        for(int i=0;i<materialesForm.getNumArticulos();i++)
	        {
	        	int cantidad=0;
	        	try
				{
	        		cantidad=Integer.parseInt(materialesForm.getArticulos("cantidad_"+i)+"");
				}
	        	catch(Exception e)
				{
	        		cantidad=0;
				}
	        	if(cantidad>0)
	        	{
	        		
	        		detallePedido.put("codigoArticulo_"+numDetallePedido, materialesForm.getArticulos("codigoArticulo_"+i));
	        		detallePedido.put("cantidadDespacho_"+numDetallePedido, materialesForm.getArticulos("cantidad_"+i));
	        		
	                if(UtilidadTexto.getBoolean(materialesForm.getArticulos("existeBd_"+i).toString()))
	                	//modificacion
	                	detallePedido.put("accion_"+numDetallePedido,"modify");
	                else
	                	//insercion
	                	detallePedido.put("accion_"+numDetallePedido,"add");
	                
	                numDetallePedido++;
	                
	        	}
	        	else if(UtilidadTexto.getBoolean(materialesForm.getArticulos("existeBd_"+i).toString()))
	        	{
	        		//Eliminacion
	        		detalleEliminacion.put("codigoArticulo_"+numDetalleEliminacion, materialesForm.getArticulos("codigoArticulo_"+i));
	        		numDetalleEliminacion++;
	        	}
	        }
	        detalleEliminacion.put("numRegistros", numDetalleEliminacion);
	        //------------------------------------------------------------------------------------------------------------
        	try 
        	{
        		
				resp = pedidos.modificarPedidoYDetalle(
					con, 
					materialesForm.getNumeroPedido(), 
					materialesForm.getCentroCosto(), 
					materialesForm.getFarmacia(), 
					esUrgente, 
					"", // observaciones generales (No Aplica)
					esTerminado?ConstantesBD.codigoEstadoPedidoTerminado:ConstantesBD.codigoEstadoPedidoSolicitado, 
					"", //motivo anulacion (No Aplica)
					usuario.getLoginUsuario(), 
					detallePedido, 
					numDetallePedido,  //tamanio del detalle de pedidios
					detalleEliminacion,
					ConstantesBD.continuarTransaccion);
				
				//se toma el número del pedido ********************************************
		        
		    	materialesForm.reset(true);
		    	materialesForm.setEstado("guardarPedido");
		    	materialesForm.setNumeroPeticion(numeroPeticion);
		    	materialesForm.setNumeroPedido(numeroPedido);
		    	materialesForm.setFechaPeticion(fechaPeticion);
		    	materialesForm.setHoraPeticion(horaPeticion);
		    	materialesForm.setEstadoPeticion(estadoPeticion);
		    	
		    	//se inserta el pedido de la petición
				Peticion peticion = new Peticion();
				peticion.setNumeroPeticion(materialesForm.getNumeroPeticion());
				peticion.setNumeroPedido(materialesForm.getNumeroPedido());
				peticion.setUsuario(usuario.getLoginUsuario());
				resp2=peticion.actualizarPedidoPeticion(con);
				
				
				//*****Se carga el resumen del pedido************************
				pedidos.setNumeroPedido(materialesForm.getNumeroPedido());
				resp1=pedidos.cargarPedido(con);
		    	this.llenarForma(materialesForm,pedidos);
				this.cargarServiciosPeticion(con,materialesForm);
				
		        
		        // se verifica estado de la operación **************************************
		        if(resp>0&&resp1>0&&resp2>0)
		        	UtilidadBD.finalizarTransaccion(con);
		        else
		        	UtilidadBD.abortarTransaccion(con);
			} 
        	catch (SQLException e) 
        	{
        		resp=0;
	        	logger.error("Error en accionGuardarPedido de MaterialesQxAction: "+e);
			}
        	//***********************************************************************************
        }
        else
        {
        	//*************INSERCION**************************************************************
        	//*******verificación de los artículos *************************************************************
			//se verifica que a los artículos se les haya ingresado cantidad > 0, de lo contrario no se registran
	        for(int i=0;i<materialesForm.getNumArticulos();i++)
	        {
	        	int cantidad=0;
	        	try
				{
	        		cantidad=Integer.parseInt(materialesForm.getArticulos("cantidad_"+i)+"");
				}
	        	catch(Exception e)
				{
	        		cantidad=0;
				}
	        	if(cantidad>0)
	        	{
	                arrayListCodigos.add(contador,materialesForm.getArticulos("codigoArticulo_"+i));
	                arrayListCantidad.add(contador,materialesForm.getArticulos("cantidad_"+i));
	                contador++;
	        	}
	        }
	        try
			{

		        resp=pedidos.insertarPedidoYDetalle(con,
			               materialesForm.getCentroCosto(),
			               materialesForm.getFarmacia(),
			               esUrgente,
			               "",
			               usuario,
			               esTerminado,
			               arrayListCodigos,
			               arrayListCantidad,
			               ConstantesBD.continuarTransaccion,
			               materialesForm.getFecha(),
			               materialesForm.getHora(),
			               ConstantesBD.acronimoSi, ConstantesBD.acronimoNo); //es quirurgico ?
		        //se toma el número del pedido ********************************************
		        
		    	materialesForm.reset(true);
		    	materialesForm.setEstado("guardarPedido");
		    	materialesForm.setNumeroPeticion(numeroPeticion);
		    	materialesForm.setFechaPeticion(fechaPeticion);
		    	materialesForm.setHoraPeticion(horaPeticion);
		    	materialesForm.setEstadoPeticion(estadoPeticion);
		    	pedidos.setNumeroPedido(pedidos.siguientePedido(con));
		    	materialesForm.setNumeroPedido(pedidos.getNumeroPedido());
		    	
				
				//se inserta el pedido de la petición
				Peticion peticion = new Peticion();
				peticion.setNumeroPeticion(materialesForm.getNumeroPeticion());
				peticion.setNumeroPedido(materialesForm.getNumeroPedido());
				peticion.setUsuario(usuario.getLoginUsuario());
				resp2=peticion.insertarPedidoPeticion(con);
				
				
				//*****Se carga el resumen del pedido************************
				resp1=pedidos.cargarPedido(con);
		    	this.llenarForma(materialesForm,pedidos);
				this.cargarServiciosPeticion(con,materialesForm);
				
		        
		        // se verifica estado de la operación **************************************
		        if(resp>0&&resp1>0&&resp2>0)
		        	UtilidadBD.finalizarTransaccion(con);
		        else
		        	UtilidadBD.abortarTransaccion(con);
			}
	        catch(SQLException e)
			{
	        	resp=0;
	        	logger.error("Error en accionGuardarPedido de MaterialesQxAction: "+e);
			}
	        //*************************************************************************************
        }
        
        //Se recupera la informacion del peticion
        materialesForm.setNumeroIngreso(numeroIngreso);
        materialesForm.setTerminado(terminado);
        
        this.cerrarConexion(con);
        
        //***********se verifica resultado de la transacción***************************************************
        if(resp>0&&resp1>0&&resp2>0)
        {
        	return mapping.findForward("resumenPedido");
        }
        else
        {
        	logger.warn("Error en la transacción de Inserción Pedidos Qx. en BD");
			materialesForm.reset(false);
			request.setAttribute("codigoDescripcionError", "errors.sinIngresar");
			return mapping.findForward("paginaError");
        }
        
	}

	/**
	 * Método para cargar los datos del pedido en la forma
	 * @param materialesForm
	 * @param pedidos
	 */
	private void llenarForma(MaterialesQxForm materialesForm, PedidosInsumos pedidos)
	{
		materialesForm.setNumeroPedido(pedidos.getNumeroPedido());
		materialesForm.setFecha(pedidos.getFechaPedido());
		materialesForm.setHora(pedidos.getHoraPedido());
		materialesForm.setFechaGrabacion(pedidos.getFechaGrabacion());
		materialesForm.setHoraGrabacion(pedidos.getHoraGrabacion());
		logger.info("CENTRO DE COSTO=> "+pedidos.getCentroCosto());
		materialesForm.setCentroCosto(Integer.parseInt(pedidos.getCentroCosto().split(ConstantesBD.separadorSplit)[0]));
		materialesForm.setNombreCentroCosto(pedidos.getCentroCosto().split(ConstantesBD.separadorSplit)[1]);
		materialesForm.setFarmacia(Integer.parseInt(pedidos.getFarmacia().split(ConstantesBD.separadorSplit)[0]));
		materialesForm.setNombreFarmacia(pedidos.getFarmacia().split(ConstantesBD.separadorSplit)[1]);
		if(UtilidadTexto.getBoolean(pedidos.getUrgente()))
			materialesForm.setUrgente("Sí");
		else
			materialesForm.setUrgente("No");
		materialesForm.setUsuario(pedidos.getUsuario());
		materialesForm.setArticulos(pedidos.getArticulos());
		materialesForm.setNumArticulos(pedidos.getNumArticulos());
		materialesForm.setArticulos("fechaHoraGrabacion_0",materialesForm.getFechaGrabacion()+"-"+materialesForm.getHoraGrabacion());
		
	}

	/**
	 * Método usado para realizar la inserción de un nuevo artículo en la lista de pedidos
	 * @param materialesForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAgregar(MaterialesQxForm materialesForm, ActionMapping mapping, HttpServletRequest request) 
	{
		//se toma posición del mapa
		int pos=materialesForm.getNumArticulos();
		
		
		String ruta = request.getRequestURI();
		
		
		//según el path se define cual forward lanzar
		if(ruta.endsWith("ingresarModificar.do"))
		{
			///se asignan los datos consultados en el mapa
			materialesForm.setArticulos("articulo_"+pos,materialesForm.getArticulos("codigoArticulo_"+pos)+"-"+
					materialesForm.getArticulos("descripcionArticulo_"+pos));
			materialesForm.setArticulos("unidadMedida_"+pos,materialesForm.getArticulos("unidadMedidaArticulo_"+pos));
			materialesForm.setArticulos("totalPedidos_"+pos,"0");
			materialesForm.setArticulos("codigoArticulo_"+pos,materialesForm.getArticulos("codigoArticulo_"+pos));
			materialesForm.setArticulos("totalConsAnt_"+pos,"0");
			materialesForm.setArticulos("total_"+pos,"0");
			materialesForm.setArticulos("totalConsumos_"+pos,"0");
			materialesForm.setArticulos("totalHistorico_"+pos,"0");
			materialesForm.setArticulos("difPedCons_"+pos,"0");
			materialesForm.setArticulos("estaBd_"+pos, ConstantesBD.acronimoNo);
			
			//se almacena el artículo como artículo insertado
			String auxS0 = materialesForm.getCodigosArticulosInsertados();
			auxS0 += materialesForm.getArticulos("codigoArticulo_"+pos) + ",";
			materialesForm.setCodigosArticulosInsertados(auxS0);
			
			//se aumenta el tamaño del mapa
			pos++;
			
			materialesForm.setNumArticulos(pos);
			materialesForm.setArticulos("numRegistros",materialesForm.getNumArticulos()+"");
			
			
			
			//forward para el ingreso consumo Mat.
			if(materialesForm.isPorActo())
				return mapping.findForward("listadoArticuloXActo");
			else
				return mapping.findForward("listadoArticuloXCirugia");
				
		}
		else
		{
			//se asignan los datos consultados en el mapa
			materialesForm.setArticulos("articulo_"+pos,
				materialesForm.getArticulos("codigoArticulo_"+pos)+"-"+
				materialesForm.getArticulos("descripcionArticulo_"+pos));
			materialesForm.setArticulos("cantidad_"+pos,"0");
			materialesForm.setArticulos("totalPedidosAnt_"+pos, "0");
			materialesForm.setArticulos("total_"+pos, "0");
			materialesForm.setArticulos("existeBd_"+pos, ConstantesBD.acronimoNo);
			
			//se almacena el artículo como artículo insertado
			String auxS0 = materialesForm.getCodigosArticulosInsertados();
			auxS0 += materialesForm.getArticulos("codigoArticulo_"+pos) + ",";
			materialesForm.setCodigosArticulosInsertados(auxS0);
			
			//se aumenta el tamaño del mapa
			pos++;
			
			materialesForm.setNumArticulos(pos);
			materialesForm.setArticulos("numRegistros",materialesForm.getNumArticulos()+"");
			
			//logger.info("MAPA DE ARTICULOS=> "+materialesForm.getArticulos());
			
			//forward para el pedido Qx.
			return mapping.findForward("listadoArticulos");
		}
	}

	/**
	 * Método usado para iniciar el proceso de ingresar un pedido Qx. 
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarPedido(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//preparación de los datos
		materialesForm.setEstado("empezarPedido");
		materialesForm.setFecha(UtilidadFecha.getFechaActual());
		materialesForm.setHora(UtilidadFecha.getHoraActual());
		materialesForm.setNumeroPedido(0);
		materialesForm.setInstitucion(usuario.getCodigoInstitucion());
		materialesForm.setTipoArea(ConstantesBD.codigoTipoAreaSubalmacen+"");
		
		
		
		MaterialesQx materiales= new MaterialesQx();
		Connection con = UtilidadBD.abrirConexion();
		
		
		
		//1) Cargar los servicios de la petición***************************************************************************
		this.cargarServiciosPeticion(con,materialesForm);
		
		//2) Se cargan los nombres de los centros de costo******************************************************************
		try
		{
			materialesForm.setNombreCentroCosto(UtilidadValidacion.getNombreCentroCosto(con,materialesForm.getCentroCosto()));
			materialesForm.setNombreFarmacia(UtilidadValidacion.getNombreCentroCosto(con,materialesForm.getFarmacia()));
		}
		catch(SQLException e)
		{
			logger.warn("Error al consultar las descirpciones del centro de costo y almacen: "+e);
		}
		
		//3) Se verifica si existe un pedido pendiente (en estado solicitado)
		HashMap pedidoPendiente = materiales.consultarPedidoPendientePeticion(con, materialesForm.getNumeroPeticion()+"",materialesForm.getFarmacia());
		if(Integer.parseInt(pedidoPendiente.get("numRegistros").toString())>0)
		{
			materialesForm.setTerminado(ConstantesBD.acronimoNo);
			materialesForm.setNumeroPedido(Integer.parseInt(pedidoPendiente.get("codigoPedido").toString()));
			materialesForm.setFecha(pedidoPendiente.get("fecha").toString());
			materialesForm.setHora(pedidoPendiente.get("hora").toString());
			materialesForm.setUrgente(UtilidadTexto.getBoolean(pedidoPendiente.get("urgente").toString())?"true":"false");
			materialesForm.setArticulos((HashMap)pedidoPendiente.get("articulos"));
		}
		else
			materialesForm.setArticulos("numRegistros","0");
		
		//4) Se consultan los articulos de los pedidos anteriores  y se fusionan con los existentes
		materialesForm.setArticulos(materiales.adicionarArticulosPedidosAnterioresPeticion(con, materialesForm.getNumeroPeticion(), materialesForm.getArticulos()));
		
		materialesForm.setNumArticulos(Integer.parseInt(materialesForm.getArticulos("numRegistros")+""));
		this.organizarListadoArticulos(materialesForm,usuario);
		//*****************************************************************
		this.cerrarConexion(con);
		
		return mapping.findForward("ingresoPedido");
	}

	/**
	 * Método implementado para organizar el listado de articulos de la hoja de
	 * gastos para que quede acorde a la busqueda genérica de artículos 
	 * @param materialesForm
	 * @param usuario
	 */
	private void organizarListadoArticulos(MaterialesQxForm materialesForm, UsuarioBasico usuario) 
	{
		String auxS0 = "";
		String listado = "";
		String existencias = "";
		
		for(int i=0;i<materialesForm.getNumArticulos();i++)
		{
			//1) se obtiene el código del artículo para asignarlo al listado
			//de códigos articulos insertados
			auxS0 = materialesForm.getArticulos("codigoArticulo_"+i).toString();
			listado += auxS0 + ",";
			
			//2) se obtiene las existencias
			existencias = UtilidadInventarios.getExistenciasXArticulo(Integer.parseInt(auxS0),materialesForm.getFarmacia(),usuario.getCodigoInstitucionInt());
			
			//3) Se calcula el total de pedido por artículo
			int total = Integer.parseInt(materialesForm.getArticulos("cantidad_"+i).toString()) + Integer.parseInt(materialesForm.getArticulos("totalPedidosAnt_"+i).toString());
			materialesForm.setArticulos("total_"+i, total+"");
			
			materialesForm.setArticulos("existenciaXAlmacen_"+i,existencias);
			
			//Si no existe el key existeBd entonces se agrega como NO
			if(materialesForm.getArticulos("existeBd_"+i)==null)
				materialesForm.setArticulos("existeBd_"+i, ConstantesBD.acronimoNo);
			
		}
		//se asigna el listado de los códigos insertados
		materialesForm.setCodigosArticulosInsertados(listado);
		
	}

	/**
	 * Método usado para consultar las cirugias de la petición
	 * @param con
	 * @param materialesForm
	 */
	private void cargarServiciosPeticion(Connection con, MaterialesQxForm materialesForm) 
	{
		Peticion peticion = new Peticion();
		peticion.setNumeroPeticion(materialesForm.getNumeroPeticion());
		peticion.cargarServiciosPeticion(con,1,null);
		materialesForm.setCirugias(peticion.getServiciosMap());
		materialesForm.setNumCirugias(peticion.getNumServicios());
		
		logger.info("MAPA DE CIRUGIAS: "+materialesForm.getCirugias());
		//obtener el listado de cirugias separadas por comas
		String listado = "";
		for(int i=0;i<peticion.getNumServicios();i++)
		{
			if(!listado.equals(""))
				listado += ",";
			listado += materialesForm.getCirugias("codigo_servicio_"+i).toString();
		}
		materialesForm.setListadoCirugias(listado);
	}

	/**
	 * Método usado para realizar las validaciones generales de la funcionalidad
	 * @param paciente
	 * @param materialesForm
	 * @param mapping
	 * @param request
	 * @param logger
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward validacionesUsuarios(PersonaBasica paciente, MaterialesQxForm materialesForm, ActionMapping mapping, HttpServletRequest request, Logger logger, UsuarioBasico usuario) throws SQLException  
	{
		Connection con = null;
		//***************VALIDACIONES************************************************
		//verificar si es null (Paciente está cargado)
		if((paciente==null || paciente.getTipoIdentificacionPersona().equals("")))
		{
			con = UtilidadBD.abrirConexion();
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
		}
		//Validar que el usuario no se autoatienda
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
					
		
		//Las validaciones de cuenta solo aplican para pacientes con cuenta
		if(request.getServletPath().equals("/materialesQx/ingresarModificar.do"))
		{
			//Validaciones del paciente
			int idCuenta = paciente.getCodigoCuenta();
			
			con = UtilidadBD.abrirConexion();
			RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
			
			
			if(!resp.puedoSeguir)
			{
				con = UtilidadBD.abrirConexion();
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La cuenta no está cargada", resp.textoRespuesta, true);
			}
			
			
			
			//se asigna el Id de la cuenta al formulario
			materialesForm.setIdCuenta(idCuenta);
			
			//verificar que la cuenta es Hospitalizacion o Ambulatorios con Tipo Paciente Cirugia Ambulatoria
			
			if(con!=null || !con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
		}
		//***************************************************************************
		return null;
	}

	public void cargarEncabezado (MaterialesQxForm forma)
	{
		//se introduce el numero de la peticion a la forma.	
		forma.setPeticion(forma.getOrdenes("peticion_"+forma.getIndex())+"");
		//se introduce el estado de la peticion
		forma.setEstadoPeticion(forma.getOrdenes("estadopeticion_"+forma.getIndex())+"");
		//dr indroduce el numero de la solicitud
		forma.setOrden(Integer.parseInt(forma.getOrdenes("numero_solicitud_"+forma.getIndex())+""));
		//Se introduce ls fecha de la peticion
		forma.setFechaPeticion(forma.getOrdenes("fechapeticion_"+forma.getIndex())+"");
		//dr indroduce l ahora de la peticion
		forma.setHoraPeticion(forma.getOrdenes("horapeticion_"+forma.getIndex())+"");
		//SE toma la verificacion de si tiene pedidos solicitados
		forma.setTienePedidosSolicitados(UtilidadTexto.getBoolean(forma.getOrdenes("peticion_diferente_"+forma.getIndex())+""));
	}
	
	
	/**
	 * Método que inicia al flujo de ingreso/modificación de artículos
	 * en el acto quirúrgico, donde según al parámetro de general 'Materiales por Acto Qx. S/N'
	 * se elige la forma de asignar los artículos al acto
	 * @param materialesForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionMateriales(MaterialesQxForm materialesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Cargar informacion dela sesión*****************************************
		materialesForm.setInstitucion(usuario.getCodigoInstitucion());
		materialesForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		
		Connection con = UtilidadBD.abrirConexion();
		//se cargan los valores del encabezado
		if(!materialesForm.isConMenu()) //Cuando es sin menu quiere decir que se llama desde otra parte
		{
			materialesForm.setPeticion(Utilidades.getPeticionSolicitudCx(con, materialesForm.getOrden())+"");
			Peticion mundoPeticion = new Peticion();
			HashMap datosPeticion = mundoPeticion.cargarEncabezadoPeticion(con, Integer.parseInt(materialesForm.getPeticion()), null);
			materialesForm.setFechaPeticion(datosPeticion.get("fecha_peticion_0").toString());
			materialesForm.setHoraPeticion(datosPeticion.get("hora_peticion_0").toString());
			materialesForm.setEstadoPeticion(datosPeticion.get("estado_peticion_0").toString());
		}
		else
			cargarEncabezado(materialesForm);
		//se toma el parámetro general **************************************************************
		materialesForm.setPorActo(UtilidadTexto.getBoolean(ValoresPorDefecto.getMaterialesPorActo(usuario.getCodigoInstitucionInt())));
		
		
		//se preparan los datos********************************************************************
		MaterialesQx materiales= new MaterialesQx();
		materiales.setNumeroSolicitud(materialesForm.getOrden());
		
		
		//se carga la información de Materiales Qx *********************************
		this.cargarInformacionMateriales(con,materiales,materialesForm,usuario,true);
		
		this.cerrarConexion(con);
		
		//***********************VALIDACIONES DE INVENTARIOS***********************************************************************
		materialesForm.setTipoTransaccionPedido(ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(), true));
		//Si la peticion no tiene ningun pedido se debe validar que el centro de costo que solicita 
		//tenga parametrización de transacciones válidas por centro de costo
		if(!materialesForm.isTienePedido())
		{
			//***SE VERIFICA SI HAY CENTROS DE COSTO VALIDOS PARA SOLICITUD PACIENTES**********
	    	Vector restricciones= new Vector();
	        restricciones.add(0, materialesForm.getTipoTransaccionPedido());
	        
	        //se carga el registro de la transaccion válidad para el centro de costo solicitante
	        //y con el tipo transaccion SOLICITUD PACIENTES
	        HashMap mapaTransValidas=UtilidadInventarios.transaccionesValidasCentroCosto( 
	        		usuario.getCodigoInstitucionInt(), 
					materialesForm.getCentroCosto(), 
					restricciones, true);
	        //se asigna tamaño del mapa
	        if(Integer.parseInt(mapaTransValidas.get("numRegistros").toString())<=0)
	        {
	        	//error.inventarios.centroCostoSinTransVal
	        	ActionErrors errores = new ActionErrors();
	        	errores.add("", new ActionMessage("error.inventarios.centroCostoSinTransVal",materialesForm.getNombreCentroCosto(),"solicitudes de pacientes"));
	        	saveErrors(request, errores);
				return mapping.findForward("listadoOrdenes");
	        }
		}
		
		//****************************************************************************************************************************
		
		
		
		//Si el consumo ya está finalizado y hay permisos para Modificar indicativo Finalizacion 
		if(UtilidadTexto.getBoolean(materiales.getFinalizado()))
		{
			if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 675))
				return mapping.findForward("modificarFinalizacion");
			//Si no hay permisos solo se muestra el resumen
			else
				return mapping.findForward("resumenMateriales");
			
		}
		else
		{
			ActionErrors errores = this.cargarListaAlmacenes(materialesForm, usuario, materialesForm.getCentroCosto(), new ActionErrors());
			//Su hubo problemas consultando las farmacias del centro de costo
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				return mapping.findForward("listadoOrdenes");
			}
			return mapping.findForward("principal");
		}
			
	}

	/**
	 * Método usado para cargar los datos de Materiales Qx.
	 * @param con
	 * @param materiales
	 * @param materialesForm
	 * @param usuario
	 * @param articulosPedido 
	 */
	private void cargarInformacionMateriales(Connection con, MaterialesQx materiales, MaterialesQxForm materialesForm, UsuarioBasico usuario, boolean articulosPedido) 
	{
		//*****SE CARGAN LOS DATOS GENERALES ************************************************
		///Se consultan el centro de costo del pedido de la peticion qx.
		HashMap centrosCosto = MaterialesQx.consultarCentrosCostoPedidosPeticion(con, Integer.parseInt(materialesForm.getPeticion()));
		int codigoCentroCosto = usuario.getCodigoCentroCosto();
		String nombreCentroCosto = usuario.getCentroCosto();
		int codigoFarmacia = ConstantesBD.codigoNuncaValido;
		String nombreFarmacia = "";
		boolean existeFarmacia = false;
		
		logger.info("CONSULTA MAPA DATOS CENTROSCOSTO=> "+centrosCosto);
	
		if(Utilidades.convertirAEntero(centrosCosto.get("centroCosto")+"", true)>0)
		{
			codigoCentroCosto = Integer.parseInt(centrosCosto.get("centroCosto").toString());
			nombreCentroCosto = centrosCosto.get("nombreCentroCosto").toString();
			
			existeFarmacia = true;
			materialesForm.setTienePedido(true);
		}
		else
			materialesForm.setTienePedido(false);
		
		
		//se verifica si ya existe un registro de Materiales Qx. para esa órden
		if(!materiales.cargarDatosGenerales(con))
		{
			this.llenarFormDatosGenerales(materialesForm,false,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),codigoCentroCosto,nombreCentroCosto,ConstantesBD.acronimoNo,codigoFarmacia,nombreFarmacia,existeFarmacia);
		}
		else
		{
			//Si no existía farmacia se marca la farmacia que viene del registro de materiales especiales
			if(!existeFarmacia&&materiales.getFarmacia()>0)
			{
				existeFarmacia = true;
			}
			
			if(materiales.getCentroCosto()!=codigoCentroCosto&&codigoCentroCosto!=usuario.getCodigoCentroCosto())
			{
				materiales.setCentroCosto(codigoCentroCosto);
				materiales.setNombreCentroCosto(nombreCentroCosto);
			}
			
			this.llenarFormDatosGenerales(materialesForm,true,materiales.getFecha(),materiales.getHora(),materiales.getCentroCosto(),materiales.getNombreCentroCosto(),materiales.getFinalizado(),codigoFarmacia,nombreFarmacia,existeFarmacia);
		}
		//**********************************************************************************
		
		
		
		//*****SE CARGAN LAS CIRUGÍAS ******************************************************
		//se cargan las cirugias involucradas con el acto quirúrgico POR ACTO
		materiales.cargarCirugiasPorActo(con);
		materialesForm.setCirugias(materiales.getCirugias());
		materialesForm.setNumCirugias(Integer.parseInt(materialesForm.getCirugias("numRegistros")+""));
		//**********************************************************************************
		
		//*****SE CARGAN LOS ARTÍCULOS********************************************
		//se verifica si antes existia materiales por Acto o no existía para cargar los artículos por acto
		if((materiales.isEsActo()&&materialesForm.isExiste()))
			//se cargan artículos en el caso de que ya se encuentren parametrizados
			materiales.cargarArticulosMaterialesQx(con);
		else
			//se cargan artículos ya existente en materiales Qx por Cirugia
			materiales.cargarArticulosPorCirugia(con);
		
		//Se cargan articulos de despachos o pedidos terminados
		if(articulosPedido)
			materiales.setArticulos(materiales.consultarCantidadesArticulosDespacho(con, materiales.getArticulos(), Integer.parseInt(materiales.getArticulos().get("numRegistros")+""), materialesForm.isPorActo()));
		
		materialesForm.setCodigosArticulosInsertados(materiales.getArticulos().get("codigosArticulosInsertados").toString());
		materialesForm.setArticulos(materiales.getArticulos());
		materialesForm.setNumArticulos(Integer.parseInt(materialesForm.getArticulos("numRegistros")+""));
		//***************************************************************************
		
		
		
	}


	/**
	 * Método usado para cargar el formularios con los datos Generales
	 * del registro de materiales Qx.
	 * @param materialesForm
	 * @param existe
	 * @param fecha
	 * @param hora
	 * @param centroCosto
	 * @param nombreCentroCosto
	 * @param finalizado 
	 * @param nombreFarmacia 
	 * @param codigoFarmacia 
	 * @param existeFarmacia 
	 */
	private void llenarFormDatosGenerales(MaterialesQxForm materialesForm, boolean existe, String fecha, String hora, int centroCosto, String nombreCentroCosto, String finalizado, int codigoFarmacia, String nombreFarmacia, boolean existeFarmacia) 
	{
		materialesForm.setExiste(existe);
		materialesForm.setFecha(fecha);
		materialesForm.setHora(hora);
		materialesForm.setCentroCosto(centroCosto);
		materialesForm.setNombreCentroCosto(nombreCentroCosto);
		materialesForm.setFinalizado(finalizado);
		materialesForm.setFarmacia(codigoFarmacia);
		materialesForm.setNombreFarmacia(nombreFarmacia);
		materialesForm.setExisteFarmacia(existeFarmacia);
	}

	/**
	 * Método que gestiona el proceso inicial de la funcionalidad, en donde
	 * se consultan las ordenes de cirugía asociadas a la cuenta y según el 
	 * resultado se lanza el forward específico
	 * @param con
	 * @param materialesForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(MaterialesQxForm materialesForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		Connection con = UtilidadBD.abrirConexion();
		/**
		 * Validar concurrencia
		 * Si ya está en proceso de facturación, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
		/**
		 * Validar concurrencia
		 * Si ya está en proceso de distribucion, no debe dejar entrar
		 **/
		else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
		}
		else
		{
			//se limpia formulario
			int id = materialesForm.getIdCuenta();
			materialesForm.reset(false);
			materialesForm.setEstado("empezar");
			materialesForm.setIdCuenta(id);
			
			//se instancia objeto MaterialesQx
			MaterialesQx materiales=new MaterialesQx();
			//se consultan las ordenes de cirugia de la cuenta 
			materialesForm.setOrdenes(materiales.cargarOrdenesCirugia(con,materialesForm.getIdCuenta()));
			//se extraen las ordenes que tiene pedido sin despachar
			materialesForm.setOrdenesSinPedidoDespachado(materiales.getOrdenesSinPedidoDespachado());
			//se asigna el tamaño del mapa
			materialesForm.setNumRegistros(Integer.parseInt(materialesForm.getOrdenes("numRegistros")+""));
			
			
			this.cerrarConexion(con);
			
			
			//segun numero de ordenes se lanza el forward indicado
			if(materialesForm.getNumRegistros()==1&&materialesForm.getOrdenesSinPedidoDespachado().equals(""))
			{
				//se toma la única solicitud encontrada
				materialesForm.setOrden(Integer.parseInt(materialesForm.getOrdenes("numero_solicitud_0")+""));
				materialesForm.setPeticion(materialesForm.getOrdenes("peticion_0")+"");
				materialesForm.setEstadoPeticion(materialesForm.getOrdenes("estadoPeticion_0")+"");
				materialesForm.setTienePedidosSolicitados(UtilidadTexto.getBoolean(materialesForm.getOrdenes("peticion_diferente_0")+""));
				//se ingresa el numero de la orden
				//se cambia el estado
				logger.info("\n\n [Materiales QX entro al if]");
				Utilidades.imprimirMapa(materialesForm.getArticulos());
				materialesForm.setEstado("materiales");
				//cuando solo hay una solicitud se envía directo a materiales
				return accionMateriales(materialesForm,mapping,usuario,request);
			}
			else
			{
				logger.info("\n\n [Materiales QX ]");
				Utilidades.imprimirMapa(materialesForm.getArticulos());
				//cuando no hay o hay varias solicitudes se envía al listado de ordenes
				return mapping.findForward("listadoOrdenes");
			}
		}
	}
	
	/**
	 * Direcciona el flujo a la funcionalidad de Programacion de Salas
	 * @param MaterialesQxForm forma
	 * @param HttpServletResponse response
	 * */
	public void accionVolverFuncProgramacionCx(MaterialesQxForm forma,HttpServletResponse response)
	{		
		try
		{
			response.sendRedirect("../ingresarProgramacionCirugia/ingresar.do?estado=empezar&numeroPeticion="+forma.getNumeroPeticion());	
		}
		catch(IOException e)
		{
			logger.info("error al retornar a la funcionalidad de Programacion de Cx >> "+e);		
		}							
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}

}
