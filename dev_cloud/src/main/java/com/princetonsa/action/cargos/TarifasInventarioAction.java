/*
 * TarifasInventarioAction.java 
 * Autor			:  Juan David Ramírez
 * Creado el	:  13-Mar-2005
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.action.cargos;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.ServletRequest;
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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cargos.TarifasInventarioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.cargos.TarifasInventario;
import com.princetonsa.pdf.TarifasInventarioPdf;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class TarifasInventarioAction extends Action
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifasInventarioAction.class);

	/**
	 * Cierra la conexión en el caso que esté abierta
	 * @param con
	 * @throws SQLException
	 */
	private void cerrarConexion(Connection con) 
	{
		try
		{
			if(con!=null && !con.isClosed())
				UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("no se pudo cerrar la conexion con la base de datos: "+e );
		}
	}

	/**
	 * Método execute del action
	 * @throws SQLException 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException
	{
		Connection con=null;
		try{
		try
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.error("Problemas con la base de datos "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			return mapping.findForward("paginaError");
		}
		
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		if( usuario == null )
		{
			this.cerrarConexion(con);				
	
			logger.error("El usuario no esta cargado (null)");
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");				
		}		

		if(form instanceof TarifasInventarioForm)
		{
			TarifasInventarioForm forma=(TarifasInventarioForm)form;
			return processTarifasINVENTARIO(con, usuario, mapping, forma, request);
		}
		this.cerrarConexion(con);
		logger.error("El form no es compatible con el form de TarifaISSForm o TarifaSOATForm o TarifasForm");
		request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("paginaError");			
	}

	public ActionForward processTarifasINVENTARIO(Connection con, UsuarioBasico usuario, ActionMapping mapping, TarifasInventarioForm forma, HttpServletRequest request) throws SQLException, IPSException
	{
		String estado = forma.getEstado(); 
		TarifasInventario mundo = new TarifasInventario();


		logger.warn("estado --> " + estado);

		if( estado.equals(""))
		{
			forma.reset(usuario.getCodigoInstitucionInt());
			logger.error("Estado no valido dentro del flujo de TarifasInventarioAction (null) ");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			this.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		//***********************************ESTADOS INGRESAR/MODIFICAR TARIFAS*********************************
		else if(estado.equals("empezar"))
		{
			return accionEmpezar(con, forma, request, mapping);
		}
		//Cuando se ingresa una nueva tarifa
		else if(estado.equals("ingresarNuevaTarifa"))
		{
			return accionIngresarNuevaTarifa(con,usuario,forma,mundo,mapping);
		}
		//Cuando se selecciona un esquema tarifario
		else if(estado.equals("ingresarModificar"))
		{
			return accionIngresarModificar(con, forma, mapping, mundo,usuario);
		}
		//cuando se guardan los cambios
		else if(estado.equals("guardar"))
		{
			return accionIngresarModificar(con, forma, mundo, mapping, request,usuario);
		}
		//Cuando se elimina un registro
		else if(estado.equals("eliminar"))
		{
			return accionEliminar(con, forma, mapping);
		}
		//Cuando se selecciona boton Nuevo .. para nueva Tarifa
		else if(estado.equals("nuevoTarifa"))
		{
			return accionNuevoTarifa(con,forma,mundo,mapping);
		}
		//Cuando se selecciona boton Modificar .. para modificar Tarifa
		else if(estado.equals("modificaTarifa"))
		{
			return accionModificaTarifa(con,forma,mundo,mapping);
		}
		//Cuando se desea Guardar Nuevo Tarifa 
		else if(estado.equals("guardarNuevo"))
		{
			ActionErrors errores = new ActionErrors();
			errores=validarFechaVigencia(forma);
			
			if (!errores.isEmpty())
			{
				forma.setEstado("nuevoTarifa");
				saveErrors(request,errores);	
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaModificacion");
			}
			return accionGuardarNuevoTarifa(con,forma,mundo,mapping,usuario);
		}
		//Cuando se desea Modificar Tarifa 
		else if(estado.equals("guardarModificacion"))
		{
			ActionErrors errores = new ActionErrors();
			errores=validarFechaVigencia(forma);
			
			if (!errores.isEmpty())
			{
				forma.setEstado("modificaTarifa");
				saveErrors(request,errores);	
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaModificacion");
			}
			return accionGuardarModificacionTarifa(con,forma,mundo,mapping,usuario);
		}
		//***********************************************************************************************
		
		
		
		
		
		else if(estado.equals("consultar"))
		{
			return accionConsultar(con, forma, request, mapping);
		}
		
		
		else if(estado.equals("consultarImprimir"))
		{
			forma.resetBusqueda();
			return accionBuscarParaConsulta(con, mapping);
		}
		else if(estado.equals("ordenar"))
		{
		   return accionOrdenar(forma, mapping, request, con);
		}
		else if(estado.equals("buscar"))
		{
			return accionBuscar(con, mundo, forma, mapping,request, usuario);
		}
		else if(estado.equals("consultarFechas"))
		{
			return accionAbrirFechasVigencia(con, mundo, forma, mapping,request, usuario);
		}
		else if(estado.equals("imprimir"))
		{
			return accionImprimir(con, forma, usuario, request, mapping, request);
		}
		
		
		this.cerrarConexion(con);
		return null; 			
	}
	
	
	public ActionErrors validarFechaVigencia (TarifasInventarioForm forma)
	{
		ActionErrors errors = new ActionErrors();
		String fecha=UtilidadFecha.getFechaActual();
		int cont=0;
		int aux=0;
		
		int numRegistros = Utilidades.convertirAEntero(forma.getTodasTarifasMap("numRegistros")+"");
		for (int i = 0; i < numRegistros; i++)
		{ 
			if(!(forma.getTodasTarifasMap("fechavigencia_"+i)+"").equals(""))
			{
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(((Timestamp)forma.getTodasTarifasMap("fechavigencia_"+i)).getTime());
				if(forma.getFechaVigencia().equals(UtilidadFecha.conversionFormatoFechaAAp(c.getTime())))
				{
					errors.add("descripcion",new ActionMessage("prompt.generico","La Fecha Vigencia que digito ya existe, por favor cambiela."));
				}
			}
		}
		
		
		for(int z=0;z<Utilidades.convertirAEntero(forma.getTodasTarifasMap("numRegistros")+"");z++)
		{
			if(forma.getEstado().equals("guardarNuevo"))
			{
				if((forma.getTodasTarifasMap("fechavigencia_"+z)+"").equals(""))
					cont=1;
			}
			if(forma.getEstado().equals("guardarModificacion"))
			{
				if(!(forma.getTodasTarifasMap("codigo_"+z)+"").equals((forma.getTodasTarifasMap("codigo_"+forma.getPosTarifas())+"")) && (forma.getTodasTarifasMap("fechavigencia_"+z)+"").equals(""))
					cont=1;
			}
			if(!forma.getFechaVigencia().equals(""))
			{
				if(!(forma.getTodasTarifasMap("codigo_"+z)+"").equals((forma.getTodasTarifasMap("codigo_"+forma.getPosTarifas())+"")) && UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTodasTarifasMap("fechavigencia_"+z)+""))
					aux=1;
			}
		}
		
		if(forma.getEstado().equals("guardarNuevo"))
		{
			
			if(cont==1)
			{
				if(forma.getFechaVigencia().equals(""))
					errors.add("descripcion",new ActionMessage("errors.required","La Fecha de Vigencia"));
			}
			if(!forma.getFechaVigencia().equals("") && aux==1)
				errors.add("descripcion",new ActionMessage("prompt.generico","La Fecha Vigencia que digito ya existe, por favor cambiela."));
		}
		
		if(forma.getEstado().equals("guardarModificacion"))
		{
			if(cont==1)
			{
				if(forma.getFechaVigencia().equals(""))
					errors.add("descripcion",new ActionMessage("errors.required","La Fecha de Vigencia"));
				else
				{
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha,forma.getFechaVigencia()))
						errors.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia",forma.getFechaVigencia(),fecha+" actual"));
				}
			}
			else
			{
				if(!forma.getFechaVigencia().equals(""))
				{
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha,forma.getFechaVigencia()))
						errors.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia",forma.getFechaVigencia(),fecha+" actual"));
				}
			}
			if(!forma.getFechaVigencia().equals("") && aux==1)
				errors.add("descripcion",new ActionMessage("prompt.generico","La Fecha Vigencia que digito ya existe, por favor cambiela."));
		}
		
		if(UtilidadTexto.isEmpty(forma.getTipoTarifa()))
		{
			errors.add("",new ActionMessage("errors.required","El Tipo Tarifa "));
		}
		else
		{
			String tipoTarifa=forma.getTipoTarifa();
			if(tipoTarifa.equals(ConstantesIntegridadDominio.acronimoCostoPromedio))
			{
				String costoPromedio=forma.getCostoPromedio();
				if(Utilidades.convertirAEntero(costoPromedio)<=0)
				{
					errors.add("error.errorEnBlanco",new ActionMessage("error.errorEnBlanco","El Articulo no tiene registrado el Costo Promedio "));
				}
			}
			else if(tipoTarifa.equals(ConstantesIntegridadDominio.acronimoPrecioCompraMasAlta))
			{
				String precioCompraAlta=forma.getPrecioCompraMasAlta();
				if(Utilidades.convertirAEntero(precioCompraAlta)<=0)
				{
					errors.add("error.errorEnBlanco",new ActionMessage("error.errorEnBlanco","El Articulo no tiene registrado el Precio Compra mas Alta."));
				}
			}
		}
		
		if(!(forma.getTarifa()+"").equals(""))
		{
		
			double tarifa = forma.getTarifa(); 
			
			if(forma.getAcronimoMetodoAjuste().equals(ConstantesBD.metodoAjusteUnidad))
			{
				tarifa = Math.round(tarifa);
			}
			else if(forma.getAcronimoMetodoAjuste().equals(ConstantesBD.metodoAjusteDecena))
			{
				tarifa = UtilidadCadena.redondearALaDecena((int)tarifa);
			}
			else if(forma.getAcronimoMetodoAjuste().equals(ConstantesBD.metodoAjusteCentena))
			{
				tarifa = UtilidadCadena.redondearALaCentena((int)tarifa);
			}
			
			if(forma.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoValorFijo) && tarifa<=0)
			{
				errors.add("Tarifa Menor 0", new ActionMessage("errors.floatMayorQue", "La Tarifa", "0"));
			}
		}
		else
		{
			if(forma.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoValorFijo))
				errors.add("Tarifa Menor 0", new ActionMessage("errors.floatMayorQue", "La Tarifa", "0"));
		}
		
		return errors;
	}

	/**
	 * Método que ingresa una nueva tarifa
	 * @param con
	 * @param usuario 
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarNuevaTarifa(Connection con, UsuarioBasico usuario, TarifasInventarioForm forma, TarifasInventario mundo, ActionMapping mapping) 
	{
		forma.setTodasTarifasMap(mundo.consultarTodasTarifasInventarios(con, forma.getEsquemaTarifario()+"", forma.getArticulo()+""));
		for(int i=0;i<Utilidades.convertirAEntero(forma.getTodasTarifasMap("numRegistros")+"");i++)
	    {
	    	if(forma.getTodasTarifasMap("fechavigencia_"+i).toString()!=null && !forma.getTodasTarifasMap("fechavigencia_"+i).toString().equals(""))
	    	{
		    	if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(),UtilidadFecha.conversionFormatoFechaAAp(forma.getTodasTarifasMap("fechavigencia_"+i)+"")))
		    		forma.setTodasTarifasMap("simodifica_"+i, "S");
		    	else
		    		forma.setTodasTarifasMap("simodifica_"+i, "N");
	    	}
	    	else
	    		forma.setTodasTarifasMap("simodifica_"+i, "S");
	    }
		
		Articulo mundoArticulo = new Articulo();
		mundoArticulo.cargarArticulo(con, forma.getArticulo());
		
		forma.setPrecioBaseVenta(mundoArticulo.getPrecioBaseVenta()+"");
		forma.setPrecioUltimaCompra(mundoArticulo.getPrecioUltimaCompra()+"");
		forma.setCostoPromedio(mundoArticulo.getCostoPromedio()+"");
		forma.setPrecioCompraMasAlta(mundoArticulo.getPrecioCompraMasAlta()+"");
		
		if(Utilidades.convertirAEntero(forma.getTodasTarifasMap("numRegistros")+"")==0)
		{
			if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoInterfaz))
				forma.setTodasTarifasMap("codarticulo_0",mundoArticulo.getCodigoInterfaz()+"");
			else
				forma.setTodasTarifasMap("codarticulo_0",mundoArticulo.getCodigo()+"");
			forma.setTodasTarifasMap("descripcionarticulo_0",mundoArticulo.getDescripcion()+"");
			forma.setTodasTarifasMap("naturalezaarticulo_0", mundoArticulo.getNomNaturaleza()+"");
			forma.setTodasTarifasMap("formafarmaceutica_0", mundoArticulo.getNomFormaFarmaceutica()+"");
			forma.setTodasTarifasMap("concentracionarticulo_0", mundoArticulo.getConcentracion()+"");
			forma.setTodasTarifasMap("unidadmedida_0", mundoArticulo.getNomUnidadMedida()+"");
		}
		/*
		int pos = forma.getNumTarifas();
		
		//Se busca la posicion del regisrto que está siendo modificado
		for(int i=0;i<forma.getNumTarifas();i++)
			if(!UtilidadTexto.getBoolean(forma.getTarifas("modificado_"+i).toString()))
				pos = i;		
		
		forma.setTarifas("modificado_"+pos, ConstantesBD.acronimoNo);		
		
		Articulo mundoArticulo = new Articulo();
		mundoArticulo.cargarArticulo(con, forma.getArticulo());		
		
		forma.setTarifas("precioBaseVenta_"+pos, mundoArticulo.getPrecioBaseVenta()+"");
		forma.setTarifas("precioUltimaCompra_"+pos, mundoArticulo.getPrecioUltimaCompra()+"");
		forma.setTarifas("descripcionArticulo_"+pos, mundoArticulo.getDescripcion());
		forma.setTarifas("naturalezaArticulo_"+pos, mundoArticulo.getNomNaturaleza());
		forma.setTarifas("formaFarmaceuticaArticulo_"+pos, mundoArticulo.getNomFormaFarmaceutica());
		forma.setTarifas("unidadMedidaArticulo_"+pos, mundoArticulo.getNomUnidadMedida());
		forma.setTarifas("concentracionArticulo_"+pos, mundoArticulo.getConcentracion());
		forma.setTarifas("costoPromedio_"+pos, mundoArticulo.getCostoPromedio()+"");
		forma.setTarifas("precioCompraMasAlta_"+pos, mundoArticulo.getPrecioCompraMasAlta()+"");		
		
		//Se verifica si existe tarifa del articulo
		if(UtilidadValidacion.existeTarifaParaArticulo(con, forma.getArticulo(), forma.getEsquemaTarifario()))
		{
			mundo.cargar(con, forma.getArticulo(), forma.getEsquemaTarifario());
			
			forma.setTarifas("codigo_"+pos,mundo.getCodigo());
			forma.setTarifas("esquemaTarifario_"+pos,mundo.getCodigoEsquemaTarifario());
			forma.setTarifas("codigoArticulo_"+pos,mundo.getCodigoArticulo());
			forma.setTarifas("valorTarifa_"+pos,Math.abs(mundo.getValorTarifa()));
			forma.setTarifas("iva_"+pos,mundo.getPorcentajeIva());
			forma.setTarifas("porcentajePositivo_"+pos,mundo.getPorcentaje()>=0?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			forma.setTarifas("porcentaje_"+pos,Math.abs(mundo.getPorcentaje()));
			forma.setTarifas("actualizacionAutomatica_"+pos,mundo.getActualizAutomatic());
			forma.setTarifas("tipoTarifa_"+pos,mundo.getTipoTarifa());
			forma.setTarifas("existeBD_"+pos, ConstantesBD.acronimoSi);
			forma.setTarifas("eliminado_"+pos, ConstantesBD.acronimoNo);
		}
		else
		{
			forma.setTarifas("codigo_"+pos,"");
			forma.setTarifas("esquemaTarifario_"+pos,forma.getEsquemaTarifario());
			forma.setTarifas("codigoArticulo_"+pos,forma.getArticulo());
			forma.setTarifas("valorTarifa_"+pos,"");
			forma.setTarifas("iva_"+pos,"0");
			forma.setTarifas("porcentajePositivo_"+pos,ConstantesBD.acronimoSi);
			forma.setTarifas("porcentaje_"+pos,"0");
			forma.setTarifas("actualizacionAutomatica_"+pos,"");
			forma.setTarifas("tipoTarifa_"+pos,"");
			forma.setTarifas("existeBD_"+pos, ConstantesBD.acronimoNo);
			forma.setTarifas("eliminado_"+pos, ConstantesBD.acronimoNo);
		}
		
		pos ++;
		forma.setNumTarifas(pos);
		forma.setTarifas("numRegistros", pos);		
		*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}
	
	/**
	 * Metodo para desplegar seccion nuevo Tarifa
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionNuevoTarifa(Connection con, TarifasInventarioForm forma, TarifasInventario mundo, ActionMapping mapping) 
	{
		forma.setModifInser("Nuevo");
		forma.setFechaVigencia("");
		forma.setTipoTarifa("-1");
		forma.setPorcentajePositivo(true);
		forma.setPorcentaje(0);
		forma.setTarifa(0);
		forma.setActualizAutomatic("-1");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}
	
	/**
	 * Metodod para desplegar seccion modificar tarifa y cargar
	 * los campos correspondientes
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificaTarifa(Connection con, TarifasInventarioForm forma, TarifasInventario mundo, ActionMapping mapping) 
	{
		Articulo mundoArticulo = new Articulo();
		mundoArticulo.cargarArticulo(con, forma.getArticulo());
		
		forma.setModifInser("Modifica");
		forma.setFechaVigencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getTodasTarifasMap("fechavigencia_"+forma.getPosTarifas())+""));
		forma.setTipoTarifa(forma.getTodasTarifasMap("tipotarifa_"+forma.getPosTarifas())+"");
		forma.setPorcentajePositivo(mundo.getPorcentaje()>=0?true:false);
		forma.setPorcentaje(Utilidades.convertirADouble(forma.getTodasTarifasMap("porcentaje_"+forma.getPosTarifas())+""));
		forma.setTarifa(Utilidades.convertirADouble(forma.getTodasTarifasMap("valortarifa_"+forma.getPosTarifas())+""));
		forma.setActualizAutomatic(forma.getTodasTarifasMap("actualizautomatic_"+forma.getPosTarifas())+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}
	
	/**
	 * Metodo para almacenar una nueva Tarifa
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarNuevoTarifa(Connection con, TarifasInventarioForm forma, TarifasInventario mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		llenarMundoP(forma, mundo, usuario, "paInsertar");
		if(mundo.insertar(con)>0)
		{
			
		}
		forma.setTodasTarifasMap(mundo.consultarTodasTarifasInventarios(con, forma.getEsquemaTarifario()+"", forma.getArticulo()+""));
		for(int i=0;i<Utilidades.convertirAEntero(forma.getTodasTarifasMap("numRegistros")+"");i++)
	    {
	    	if(forma.getTodasTarifasMap("fechavigencia_"+i).toString()!=null && !forma.getTodasTarifasMap("fechavigencia_"+i).toString().equals(""))
	    	{
		    	if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(),UtilidadFecha.conversionFormatoFechaAAp(forma.getTodasTarifasMap("fechavigencia_"+i)+"")))
		    		forma.setTodasTarifasMap("simodifica_"+i, "S");
		    	else
		    		forma.setTodasTarifasMap("simodifica_"+i, "N");
	    	}
	    	else
	    		forma.setTodasTarifasMap("simodifica_"+i, "S");
	    }
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}
	
	/**
	 * Metodo para modificar una Tarifa
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarModificacionTarifa(Connection con, TarifasInventarioForm forma, TarifasInventario mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		llenarMundoP(forma, mundo, usuario, "paModificar");
		if(mundo.modificar(con)>0)
		{
			
		}
		forma.setTodasTarifasMap(mundo.consultarTodasTarifasInventarios(con, forma.getEsquemaTarifario()+"", forma.getArticulo()+""));
		for(int i=0;i<Utilidades.convertirAEntero(forma.getTodasTarifasMap("numRegistros")+"");i++)
	    {
	    	if(forma.getTodasTarifasMap("fechavigencia_"+i).toString()!=null && !forma.getTodasTarifasMap("fechavigencia_"+i).toString().equals(""))
	    	{
		    	if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(),UtilidadFecha.conversionFormatoFechaAAp(forma.getTodasTarifasMap("fechavigencia_"+i)+"")))
		    		forma.setTodasTarifasMap("simodifica_"+i, "S");
		    	else
		    		forma.setTodasTarifasMap("simodifica_"+i, "N");
	    	}
	    	else
	    		forma.setTodasTarifasMap("simodifica_"+i, "S");
	    }
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}

	/**
	 * Método para abrir el pdf
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @param mapping
	 * @return Pagina anterior
	 */
	private ActionForward accionImprimir(Connection con, TarifasInventarioForm forma, UsuarioBasico usuario, ServletRequest request, ActionMapping mapping, HttpServletRequest request1)
	{
		String nombreArchivo;
		Random r=new Random();
		nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
		TarifasInventarioPdf.pdfTarifasInventario(ValoresPorDefecto.getFilePath()+nombreArchivo, forma.getNombreEsquemaTarifario(), forma.getListadoConsultaImpresion(), usuario,forma, request1);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Consulta Diagnósticos");
		forma.setEstado("resultadoBusqueda");
		cerrarConexion(con);
        return mapping.findForward("abrirPdf");
	}

	/**
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, TarifasInventario mundo, TarifasInventarioForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		mundo.reset();
		//Modificado por la Tarea 38488
		//forma.setListadoConsulta(mundo.buscar(con, forma.getArticulo(), forma.getNombreArticulo(), forma.getNaturalezaArticulo(), forma.getFormaFarmaceutica(),forma.getConcentracionArticulo(), forma.getEsquemaTarifario(), forma.getTarifa(), forma.getIva(), forma.getPorcentaje(), forma.getActualizAutomatic(), forma.getTipoTarifa(), usuario.getCodigoInstitucionInt()));
		forma.setListadoConsulta(mundo.buscar(con, forma.getArticuloBusqueda(), forma.getNombreArticulo(), forma.getNaturalezaArticulo(), forma.getFormaFarmaceutica(),forma.getConcentracionArticulo(), forma.getEsquemaTarifario(), forma.getTarifa(), forma.getIva(), forma.getPorcentaje(), forma.getActualizAutomatic(), forma.getTipoTarifa(), usuario.getCodigoInstitucionInt(), "consulta"));
		forma.setListadoConsultaImpresion(mundo.buscar(con, forma.getArticuloBusqueda(), forma.getNombreArticulo(), forma.getNaturalezaArticulo(), forma.getFormaFarmaceutica(),forma.getConcentracionArticulo(), forma.getEsquemaTarifario(), forma.getTarifa(), forma.getIva(), forma.getPorcentaje(), forma.getActualizAutomatic(), forma.getTipoTarifa(), usuario.getCodigoInstitucionInt(), "impresion"));
		forma.setColumna("articulo");
		return accionOrdenar(forma, mapping, request, con);
	}
	
	/**
	 * Metodo para el pop up de consultar fechas
	 * vigencia inventario por esquema articulo
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAbrirFechasVigencia(Connection con, TarifasInventario mundo, TarifasInventarioForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		forma.setFechasVigenciaMap(mundo.consultarFechasVigencia(con, forma.getEsquemaTarifario()+"", forma.getArticulo()+"", ""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("fechasVigenciaInventario")	;
	}

	/**
	 * Método que lleva a la página de busqueda para consulta
	 * de un tarifa de inventario
	 * @param con
	 * @param mapping
	 * @return Pagina de búsqueda
	 */
	private ActionForward accionBuscarParaConsulta(Connection con, ActionMapping mapping)
	{
		this.cerrarConexion(con);
		return mapping.findForward("consultar");
	}

	/**
	 * Método de ordenamiento del pager, según la columna que sea seleccionada. 
	 * @param adminForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenar(TarifasInventarioForm forma,
																			ActionMapping mapping,
																			HttpServletRequest request, 
																			Connection con) 
	{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		try
		{
			forma.setListadoConsulta(Listado.ordenarColumna(new ArrayList(forma.getListadoConsulta()),forma.getUltimaPropiedad(),forma.getColumna()));
			forma.setUltimaPropiedad(forma.getColumna());
			UtilidadBD.closeConnection(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de Tarifas Inventario ");
			UtilidadBD.closeConnection(con);
			forma.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado TarifasInventario");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoBusqueda")	;
	}
	
	
	/**
	 * Método para eliminar un registro
	 * @param con Conexión con la BD
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return Pagina Principal
	 */
	private ActionForward accionEliminar(Connection con, TarifasInventarioForm forma, ActionMapping mapping)
	{
		/**
		llenarMundo(forma, mundo,usuario);
		mundo.cargar(con, forma.getEliminado(), forma.getEsquemaTarifario());
		mundo.eliminar(con);
		String log =
			"\n ==============INFORMACION ELIMINADA====================" +
			"\n*  Código						["+forma.getCodigo()+"] "+
			"\n*  Código Esquema Tarifario		["+forma.getEsquemaTarifario()+"] "+
			"\n*  Código Articulo				["+forma.getArticulo()+"] "+				
			"\n*  Valor Tarifa					["+forma.getTarifa()+"] "+
			"\n*  Porcentaje IVA				["+forma.getIva()+"] "+
			
			"\n =======================================================\n";
		LogsAxioma.enviarLog(ConstantesBD.logTarifasInventario, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
		//forma.setArticulo(forma.getEliminado());
		//forma.resetEliminacion();
		//cerrarConexion(con);
		//return mapping.findForward("paginaModificacion");
		return accionIngresarModificar(con, forma, mapping, mundo,usuario);
		**/
		int pos = forma.getPosTarifas();
		forma.setTarifas("eliminado_"+pos, ConstantesBD.acronimoSi);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");

	}

	/**
	 * Método que se encarga de modificar (Si existe) o insertar una tarifa en el sistema
	 * @param con Conexión con la BD
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return Pagina de Ingreso Modificación de tarifas
	 */
	private ActionForward accionIngresarModificar(Connection con, TarifasInventarioForm forma, TarifasInventario mundo, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();
		int resp = 0;
		
		UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapa Tarifas=> "+forma.getTarifas());
		
		//Se iteran los registros
		for(int i=0;i<forma.getNumTarifas();i++)
		{
			
			//Se toman los eliminados
			if(UtilidadTexto.getBoolean(forma.getTarifas("eliminado_"+i).toString())&&
				UtilidadTexto.getBoolean(forma.getTarifas("existeBD_"+i).toString()))
			{
				//************************+ELIMINACION**********************************************
				mundo.setCodigo(Integer.parseInt(forma.getTarifas("codigo_"+i).toString()));
				resp = mundo.eliminar(con);
				
				if(resp>0)
				{
					forma.setTarifas("existeBD_"+i, ConstantesBD.acronimoNo); //no existe
					String log =
						"\n ==============INFORMACION ELIMINADA====================" +
						"\n*  Código						["+forma.getTarifas("codigo_"+i)+"] "+
						"\n*  Código Esquema Tarifario		["+forma.getTarifas("esquemaTarifario_"+i)+"] "+
						"\n*  Código Articulo				["+forma.getTarifas("codigoArticulo_"+i)+"] "+				
						"\n*  Valor Tarifa					["+forma.getTarifas("valorTarifa_"+i)+"] "+
						"\n*  Porcentaje IVA				["+forma.getTarifas("iva_"+i)+"] "+
						
						"\n =======================================================\n";
					LogsAxioma.enviarLog(ConstantesBD.logTarifasInventario, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
				}
				else
					errores.add("",new ActionMessage("errors.notEspecific","Problemas eliminando la tarifa del artículo "+forma.getTarifas("descripcionArticulo_"+i)));
				//**************************************************************************************
			}
			else if(!UtilidadTexto.getBoolean(forma.getTarifas("eliminado_"+i).toString())&&
					!UtilidadTexto.getBoolean(forma.getTarifas("modificado_"+i).toString()))
			{
				llenarMundo(forma, mundo,usuario,i);
				
				if(!UtilidadTexto.getBoolean(forma.getTarifas("existeBD_"+i).toString()))
				{
					
					//***************INSERCIÓN******************************************************
					resp = mundo.insertar(con);
					
					if(resp>0)
					{
						mundo.cargar(con, Integer.parseInt(forma.getTarifas("codigoArticulo_"+i).toString()), Integer.parseInt(forma.getTarifas("esquemaTarifario_"+i).toString()));
						
						forma.setTarifas("modificado_"+i, ConstantesBD.acronimoSi);
						forma.setTarifas("existeBD_"+i, ConstantesBD.acronimoSi);
						forma.setTarifas("codigo_"+i, mundo.getCodigo());
					}
					else
						errores.add("",new ActionMessage("errors.notEspecific","Problemas insertando la tarifa del artículo "+forma.getTarifas("descripcionArticulo_"+i)));
					//*******************************************************************************
					
				}
				else
				{
					//**************MODIFICACION**************************************************
					mundo.cargar(con, Integer.parseInt(forma.getTarifas("codigoArticulo_"+i).toString()), Integer.parseInt(forma.getTarifas("esquemaTarifario_"+i).toString()));

					//Se verifica si se modificó el registro
					double porcentajeNuevo = 0;
					
					if(!forma.getTarifas("porcentaje_"+i).toString().equals(""))
						porcentajeNuevo = UtilidadTexto.getBoolean(forma.getTarifas("porcentajePositivo_"+i).toString())?Double.parseDouble(forma.getTarifas("porcentaje_"+i).toString()):(Double.parseDouble(forma.getTarifas("porcentaje_"+i).toString())*-1);
					
					if(
							Double.parseDouble(forma.getTarifas("valorTarifa_"+i).toString())!=mundo.getValorTarifa() || 
							Double.parseDouble(forma.getTarifas("iva_"+i).toString())!=mundo.getPorcentajeIva() ||
							porcentajeNuevo!=mundo.getPorcentaje() ||
							!forma.getTarifas("actualizacionAutomatica_"+i).toString().equals(mundo.getActualizAutomatic()) ||
							!forma.getTarifas("tipoTarifa_"+i).toString().equals(mundo.getTipoTarifa())
						)
					{
						String log =
							"\n ===================INFORMACION ORIGINAL=======================" +
							"\n*  Código					["+mundo.getCodigo()+"] "+
							"\n*  Código Esquema Tarifario	["+mundo.getCodigoEsquemaTarifario()+"] "+
							"\n*  Código Articulo			["+mundo.getCodigoArticulo()+"] "+
							"\n*  Valor Tarifa				["+mundo.getValorTarifa()+"] "+
							"\n*  Porcentaje IVA			["+mundo.getPorcentajeIva()+"] "+
							"\n*  Porcentaje 				["+mundo.getPorcentaje()+"] "+
							"\n*  Actualizacion Automatica	["+mundo.getActualizAutomatic()+"] "+
							"\n*  Tipo Tarifa			    ["+mundo.getTipoTarifa()+"] "+
							"\n*  Usuario Modifica			["+mundo.getUsuarioModifica()+"] "+
							"\n*  Fecha Modificacion		["+mundo.getFechaModifica()+"] "+
							"\n*  Hora Modificacion			["+mundo.getHoraModifica()+"] "+
							"\n ==============================================================\n";
						
						llenarMundo(forma, mundo,usuario,i);
						
						resp = mundo.modificar(con);
						
						if(resp>0)
						{
							forma.setTarifas("modificado_"+i, ConstantesBD.acronimoSi);
							log+=
								"\n ==============INFORMACION DESPUES DE MODIFICACIÓN==============" +
								"\n*  Código					["+mundo.getCodigo()+"] "+
								"\n*  Código Esquema Tarifario	["+mundo.getCodigoEsquemaTarifario()+"] "+
								"\n*  Código Articulo			["+mundo.getCodigoArticulo()+"] "+
								"\n*  Valor Tarifa				["+mundo.getValorTarifa()+"] "+
								"\n*  Porcentaje IVA			["+mundo.getPorcentajeIva()+"] "+
								"\n*  Porcentaje 				["+mundo.getPorcentaje()+"] "+
								"\n*  Actualizacion Automatica	["+mundo.getActualizAutomatic()+"] "+
								"\n*  Tipo Tarifa				["+mundo.getTipoTarifa()+"] "+
								"\n*  Usuario Modifica          ["+mundo.getUsuarioModifica()+"] "+
								"\n*  Fecha Modificacion		["+mundo.getFechaModifica()+"] "+
								"\n*  Hora Modificacion			["+mundo.getHoraModifica()+"] "+		
								"\n ===============================================================\n";
							LogsAxioma.enviarLog(ConstantesBD.logTarifasInventario, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
						}
						else
							errores.add("",new ActionMessage("errors.notEspecific","Problemas modificando la tarifa del artículo "+forma.getTarifas("descripcionArticulo_"+i)));
					}
					//*****************************************************************************
				}
				
			}
			
			
			
		}
		
		if(!errores.isEmpty())
		{
			forma.setEstado("ingresarModificar");
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, errores);
		}
		else
			UtilidadBD.finalizarTransaccion(con);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}

	/**
	 * Método que controla la consulta
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return Página principal de selección de esquema tarifario
	 */
	private ActionForward accionConsultar(Connection con, TarifasInventarioForm forma,HttpServletRequest request, ActionMapping mapping)
	{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		forma.reset(usuario.getCodigoInstitucionInt());
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método que controla la página de ingreso y modificación
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario 
	 * @return Página de Ingreso Modificación
	 */
	private ActionForward accionIngresarModificar(Connection con, TarifasInventarioForm forma, ActionMapping mapping, TarifasInventario mundo, UsuarioBasico usuario) throws IPSException
	{
		EsquemaTarifario esquema = new EsquemaTarifario();
		try 
		{
			esquema.cargarXcodigo(con,forma.getEsquemaTarifario(),usuario.getCodigoInstitucionInt());
			forma.setAcronimoMetodoAjuste(esquema.getMetodoAjuste().getAcronimo());
		} 
		catch (SQLException e) 
		{
			logger.error("Error cargando Esquema Tarifario en aproximarTarifa de TarifasAction: "+e);
			
		}
		
		/*
		forma.setModificado(false);
		forma.setPrecioBaseVenta(UtilidadInventarios.obtenerPrecioBaseVentaArticulo(con,forma.getArticulo()));
		forma.setPrecioUltimaCompra(UtilidadInventarios.obtenerPrecioUltimaCompraArticulo(con,forma.getArticulo()));
		if(UtilidadValidacion.existeTarifaParaArticulo(con, forma.getArticulo(), forma.getEsquemaTarifario()))
		{
			mundo.cargar(con, forma.getArticulo(), forma.getEsquemaTarifario());
			llenarForma(mundo, forma);
			forma.setModificar(true);
		}
		else
		{
			forma.setTarifa(0);
			forma.setIva(0);
			forma.setPorcentaje(0);
			forma.setActualizAutomatic("");
			forma.setTipoTarifa("");
			
			forma.setModificar(false);
		}*/
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaModificacion");
	}

	/**
	 * Método para empezar el flujo de la funcionalidad
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return Página principal (Selección del esquema tarifario)
	 */
	private ActionForward accionEmpezar(Connection con, TarifasInventarioForm forma, HttpServletRequest request, ActionMapping mapping)
	{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		forma.reset(usuario.getCodigoInstitucionInt()); 
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal"); 			
	}

	/**
	 * Método que pasa los atributos del numdo al form
	 * @param mundo
	 * @param forma
	 */
	private void llenarForma(TarifasInventario mundo, TarifasInventarioForm forma)
	{
		forma.setCodigo(mundo.getCodigo());
		forma.setEsquemaTarifario(mundo.getCodigoEsquemaTarifario());
		forma.setArticulo(mundo.getCodigoArticulo());
		forma.setTarifa(Math.abs(mundo.getValorTarifa()));
		forma.setIva(mundo.getPorcentajeIva());
		forma.setPorcentajePositivo(mundo.getPorcentaje()>=0);
		forma.setPorcentaje(Math.abs(mundo.getPorcentaje()));
		forma.setActualizAutomatic(mundo.getActualizAutomatic());
		forma.setTipoTarifa(mundo.getTipoTarifa());
	}

	/**
	 * Método que pasa los atributos del form al mundo
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param pos 
	 */
	private void llenarMundo(TarifasInventarioForm forma, TarifasInventario mundo, UsuarioBasico usuario, int pos)
	{
		if(!forma.getTarifas("codigo_"+pos).toString().equals(""))
			mundo.setCodigo(Integer.parseInt(forma.getTarifas("codigo_"+pos).toString()));
		else
			mundo.setCodigo(0);
		mundo.setCodigoArticulo(Integer.parseInt(forma.getTarifas("codigoArticulo_"+pos).toString()));
		mundo.setCodigoEsquemaTarifario(Integer.parseInt(forma.getTarifas("esquemaTarifario_"+pos).toString()));
		mundo.setPorcentajeIva(Double.parseDouble(forma.getTarifas("iva_"+pos).toString()));
		mundo.setValorTarifa(Double.parseDouble(forma.getTarifas("valorTarifa_"+pos).toString()));

		if(!forma.getTarifas("porcentaje_"+pos).toString().equals(""))
		{
		
			if(UtilidadTexto.getBoolean(forma.getTarifas("porcentajePositivo_"+pos).toString()))
				mundo.setPorcentaje(Double.parseDouble(forma.getTarifas("porcentaje_"+pos).toString()));
			else
				mundo.setPorcentaje(Double.parseDouble(forma.getTarifas("porcentaje_"+pos).toString())*-1);
		}
		
		
		mundo.setActualizAutomatic(forma.getTarifas("actualizacionAutomatica_"+pos).toString());
		mundo.setTipoTarifa(forma.getTarifas("tipoTarifa_"+pos).toString());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		mundo.setFechaModifica(UtilidadFecha.getFechaActual());
		mundo.setHoraModifica(UtilidadFecha.getHoraActual());
		
	}
	
	/**
	 * Método que pasa los atributos del form al mundo Principal
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param pos 
	 */
	private void llenarMundoP(TarifasInventarioForm forma, TarifasInventario mundo, UsuarioBasico usuario, String estado)
	{
		if(estado.equals("paModificar"))
		{
			if(!forma.getTodasTarifasMap("codigo_"+forma.getPosTarifas()).toString().equals(""))
				mundo.setCodigo(Integer.parseInt(forma.getTodasTarifasMap("codigo_"+forma.getPosTarifas()).toString()));
			else
				mundo.setCodigo(0);
			mundo.setCodigoArticulo(Integer.parseInt(forma.getTodasTarifasMap("codarticulo_"+forma.getPosTarifas()).toString()));
			mundo.setCodigoEsquemaTarifario(Integer.parseInt(forma.getTodasTarifasMap("esquematarifario_"+forma.getPosTarifas()).toString()));
			mundo.setPorcentajeIva(0);
			mundo.setValorTarifa(forma.getTarifa());
	
			if(forma.getPorcentaje() > 0)
			{
				if(forma.isPorcentajePositivo())
					mundo.setPorcentaje(forma.getPorcentaje());
				else
					mundo.setPorcentaje(forma.getPorcentaje()*-1);
			}			
			
			if(!forma.getActualizAutomatic().equals("-1"))
				mundo.setActualizAutomatic(forma.getActualizAutomatic());
			else
				mundo.setActualizAutomatic("");
			mundo.setTipoTarifa(forma.getTipoTarifa());
			mundo.setUsuarioModifica(usuario.getLoginUsuario());
			mundo.setFechaModifica(UtilidadFecha.getFechaActual());
			mundo.setHoraModifica(UtilidadFecha.getHoraActual());
			mundo.setFechaVigencia(forma.getFechaVigencia());
		}
		else
		{
			if(estado.equals("paInsertar"))
			{
				mundo.setCodigoArticulo(forma.getArticulo());
				mundo.setCodigoEsquemaTarifario(forma.getEsquemaTarifario());
				mundo.setPorcentajeIva(0);
				mundo.setValorTarifa(forma.getTarifa());
				if(forma.getPorcentaje()>0)
				{
					if(forma.isPorcentajePositivo())
						mundo.setPorcentaje(forma.getPorcentaje());
					else
						mundo.setPorcentaje(forma.getPorcentaje()*-1);
				}
				if(!forma.getActualizAutomatic().equals("-1"))
					mundo.setActualizAutomatic(forma.getActualizAutomatic());
				else
					mundo.setActualizAutomatic("");
				mundo.setTipoTarifa(forma.getTipoTarifa());
				mundo.setUsuarioModifica(usuario.getLoginUsuario());
				mundo.setFechaModifica(UtilidadFecha.getFechaActual());
				mundo.setHoraModifica(UtilidadFecha.getHoraActual());
				mundo.setFechaVigencia(forma.getFechaVigencia());
			}
		}
	}
}
