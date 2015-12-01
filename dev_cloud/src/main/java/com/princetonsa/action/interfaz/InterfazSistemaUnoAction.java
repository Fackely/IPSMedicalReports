package com.princetonsa.action.interfaz;

import java.sql.Connection;
import java.util.HashMap;

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

import util.BackUpBaseDatos;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.InterfazSistemaUnoForm;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.interfaz.InterfazSistemaUno;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Andrés Silva Monsalve
 */

public class InterfazSistemaUnoAction extends Action
{
	private static final String HashMap = null;
	Logger logger = Logger.getLogger(InterfazSistemaUnoAction.class);
	
	public ActionForward execute (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Connection con = null;
		try{

			if(response == null);

			if (form instanceof InterfazSistemaUnoForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");		 

				InterfazSistemaUnoForm forma = (InterfazSistemaUnoForm)form;
				InterfazSistemaUno mundo=new InterfazSistemaUno();
				String estado = forma.getEstado();


				//Maneja el tipo de interfaz (todos, facturas, recibos de caja)
				String tipo =forma.getTipo();

				ActionErrors errores = new ActionErrors();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado  >> "+forma.getEstado());
				logger.info("-------------------------------------");

				if(estado == null)
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo de Archivos Planos  (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				// ################################	INTERFAZ SISTEMA UNO    ###########################


				else if(estado.equals("empezar"))
				{						 
					forma.reset();
					// verificarInterfaz(con, form, usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultar"))
				{
					if(tipo.equals(ConstantesIntegridadDominio.acronimoTipoInterfazFacturas) ) // PARA FACTURAS
					{
						if( this.consultaFacturas(con, forma, usuario, mundo, errores)>0) // SE VERIFICAN SI EXISTEN FACTURAS
						{
							this.insertarFormulario(con, forma, usuario, mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("resumenArchivos");
						}
						else
						{
							errores.add("descripcion",new ActionMessage("errors.required","No Existen Registros de Facturas para el Rango de Fechas"));
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						}

					}				 
					else if(tipo.equals(ConstantesIntegridadDominio.acronimoTipoInterfazRecibosCaja) ) // PARA RECIBOS DE CAJA
					{
						if(this.consultaRecibos (con, forma, usuario, mundo, errores)>0)
						{
							this.insertarFormulario(con, forma, usuario, mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("resumenArchivos");
						}
						else
						{
							errores.add("descripcion",new ActionMessage("errors.required","No Existen Registros de Recibos de Caja para el Rango de Fechas"));
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("principal"); 
						}

					}
					else if(tipo.equals(ConstantesIntegridadDominio.acronimoTipoInterfazTodos)) // PARA FACTURAS Y RECIBOS DE CAJA
					{
						if (this.consultarTodo(con, forma, usuario, mundo, errores)>0) 
						{
							this.insertarFormulario(con, forma, usuario, mundo);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("resumenArchivos");
						}
						else
						{
							errores.add("descripcion",new ActionMessage("errors.required","No Existen Registros de Facturas y/o Recibos de Caja para el Rango de Fechas"));
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("principal"); 
						}
					}
				}
				else if(estado.equals("generarArchivo"))
				{
					//Genera la Información del Archivo Plano
					generarArchivo(con, forma, usuario, mundo);

				}

				else if(estado.equals("insertar"))
				{
					this.insertarFormulario(con, forma, usuario, mundo);	 
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
		 
		 
	private void generarArchivo(Connection con, InterfazSistemaUnoForm forma, UsuarioBasico usuario, InterfazSistemaUno mundo) 
	{
		logger.info("----------GENERACION ARCHIVO PLANO  ---------");
		
	}


	
	private int consultarTodo(Connection con, InterfazSistemaUnoForm forma, UsuarioBasico usuario, InterfazSistemaUno mundo, ActionErrors errores) throws IPSException 
	 {
		
		logger.info("----------Entra a CONSULTAR TODOS --------- ");
		forma.setFacturas(mundo.consultarFacturas(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getReproceso()));			
		
		DtoFactura dto = null;
		
		// NUMERO DE REGISTROS FACTURAS
		int numRegistros =Utilidades.convertirAEntero(forma.getFacturas().get("numRegistros").toString());
		
		forma.setRecibosCaja(mundo.consultarRecibosCaja(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getReproceso()));			
		
		int numRegistrosRc =Utilidades.convertirAEntero(forma.getRecibosCaja().get("numRegistros").toString());
		
		
		if (numRegistros > 0 || numRegistrosRc > 0) // PREGUNTO SI EXISTEN REGISTROS DE FACTURAS O RECIBOS DE CAJA
		{
			
			if(numRegistros > 0) // PREGUNTO SI HAY FACTURAS
			{
				/**
				 * COMO YA SE QUE HAY FACTURAS LLENO EL DTO
				 */
				for(int w=0; w<Utilidades.convertirAEntero(forma.getFacturas().get("numRegistros").toString()); w++)
				{
					dto = new DtoFactura();
					dto=Factura.cargarFactura(con, forma.getFacturas().get("codigo_"+w).toString(), true); 
						
					// cargo el Array con el dto
					forma.getFacturaDto().add(dto);
				}
			
				mundo.setTransaccion(UtilidadBD.iniciarTransaccion(con));
				
				
				if(numRegistrosRc > 0) // PREGUNTO SI HAY RECIBOS CAJA
				{
					mundo.armarPlano(con, numRegistros, forma.getFacturaDto(), forma.getPath(), forma.getNombre() ,errores,true); // SE ABRE EL ARCHIVO Y NO SE CIERRA
				}
				else
				{
					mundo.armarPlano(con, numRegistros, forma.getFacturaDto(), forma.getPath(), forma.getNombre() ,errores,false); // SE ABRE EL ARCHIVO Y SE CIERRA
				}
					
			}	
			
			
			if(numRegistrosRc > 0) // PREGUNTO SI HAY RECIBOS DE CAJA
			{
				if(numRegistros > 0) // PREGUNTO SI HAY FACTURAS
				{
					mundo.armarPlanoRC(con, numRegistrosRc, forma.getRecibosCaja(), forma.getPath(), forma.getNombre() ,errores, true); // SE CONTINUA ALMACENANDO LOR RECIBOS EN EL ARCHIVO ABIERTO
				}
				else
				{
					mundo.armarPlanoRC(con, numRegistrosRc, forma.getRecibosCaja(), forma.getPath(), forma.getNombre() ,errores, false); // SE ABRE Y SE CIERRA EL ARCHIVO
				}
			}
			
			

			forma.setBackupArchivo(forma.getNombre()+".zip"); //CARGO LA VARIABLE QUE PERMITE LA DESCARGA DEL ARCHIVO
			logger.info("Path TODOS de Copia  *********** ->"+forma.getPath());
			logger.info("Nombre TODOS de Copia *********->"+forma.getBackupArchivo());
			
			
			UtilidadFileUpload.generarCopiaArchivo(forma.getPath(), forma.getNombre()+".txt", "InterfazSistemaUno", forma.getBackupArchivo());
			
			//GENERO COPIA EN .ZIP PARA PERMITIR LA DESCARGA
			BackUpBaseDatos.EjecutarComandoSO("zip "+ forma.getPath()+"/"+forma.getNombre()+".zip"+" "+forma.getPath()+"/"+forma.getNombre()+".txt" );
			
			String ubicando = ValoresPorDefecto.getFilePath();
			
			BackUpBaseDatos.EjecutarComandoSO("cp "+ forma.getPath()+"/"+forma.getNombre()+".zip"+" "+ubicando+"InterfazSistemaUno");
			
			
			mundo.guardarArchivosInconsistencia(forma.getPath(), forma.getNombre(), mundo.getInconsistenciasMap());
			
			if(mundo.isTransaccion())
				UtilidadBD.finalizarTransaccion(con);
			else
				UtilidadBD.abortarTransaccion(con);
				
			
			forma.setContenidoArchivo(mundo.cargarArchivo(forma.getPath(), forma.getNombre()));
			
			if(forma.getContenidoArchivo()== null)
			{
				forma.setContenidoArchivo(new HashMap());
				forma.getContenidoArchivo().put("numRegistros", 0);
			}
			
		} // FIN VALIDACION DE NUMERO DE REGISTROS
		
		numRegistros = numRegistros + numRegistrosRc;
		return numRegistros;
	 }


	private int consultaRecibos(Connection con, InterfazSistemaUnoForm forma, UsuarioBasico usuario, InterfazSistemaUno mundo, ActionErrors errores) 
	{
		
		
		logger.info("----------Entra a CONSULTAR RECIBOS DE CAJA --------- ");
		
		forma.setRecibosCaja(mundo.consultarRecibosCaja(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getReproceso()));			
		
		logger.info("Mapa de Recibos Caja ->"+forma.getRecibosCaja()); // IMPRIMO EL MAPA DE CAPTURA DE LOS RECIBOS DE CAJA.
		
		int numRegistros =Utilidades.convertirAEntero(forma.getRecibosCaja().get("numRegistros").toString());
		
		if(numRegistros>0)
		{
			mundo.armarPlanoRC(con, numRegistros, forma.getRecibosCaja(), forma.getPath(), forma.getNombre() ,errores, false);
			
			forma.setBackupArchivo(forma.getNombre()+".zip"); //CARGO LA VARIABLE QUE PERMITE LA DESCARGA DEL ARCHIVO
			
			logger.info("Path RECA de Copia   ->"+forma.getPath());
			logger.info("Nombre RECA de Copia ->"+forma.getBackupArchivo());
			
			UtilidadFileUpload.generarCopiaArchivo(forma.getPath(), forma.getNombre()+".txt", "InterfazSistemaUno", "BU"+forma.getNombre()+".txt");
			
			BackUpBaseDatos.EjecutarComandoSO("zip "+ forma.getPath()+"/"+forma.getNombre()+".zip"+" "+forma.getPath()+"/"+forma.getNombre()+".txt" );
			
		
			Utilidades.obtenerPathFuncionalidad(719); // PARA OBTENER EL PATH DE LA FUNCIONALIDAD INTERFAZ SISTEMA UNO
			
			String ubicando = ValoresPorDefecto.getFilePath();
			
			BackUpBaseDatos.EjecutarComandoSO("cp "+ forma.getPath()+"/"+forma.getNombre()+".zip"+" "+ubicando+"InterfazSistemaUno");
			
			mundo.guardarArchivosInconsistencia(forma.getPath(), forma.getNombre(), mundo.getInconsistenciasMap());
			
			
			forma.setContenidoArchivo(mundo.cargarArchivo(forma.getPath(), forma.getNombre()));
			
			if(forma.getContenidoArchivo()== null)
			{
				forma.setContenidoArchivo(new HashMap());
				forma.getContenidoArchivo().put("numRegistros", 0);
			}
		}
		return numRegistros;
		
	}


	private int consultaFacturas(Connection con, InterfazSistemaUnoForm forma, UsuarioBasico usuario, InterfazSistemaUno mundo, ActionErrors errores) throws IPSException 
	{
		
		
		logger.info("----------Entra a CONSULTAR FACTURAS --------- ");
			
		forma.setFacturas(mundo.consultarFacturas(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getReproceso()));			
			
		DtoFactura dto = null;
		int numRegistros =Utilidades.convertirAEntero(forma.getFacturas().get("numRegistros").toString());
		
		logger.info("Numero de Registros Facturas Action"+numRegistros);
		
		if (numRegistros > 0) // SE VERIFICA QUE EXISTAN REGISTROS DE FACTURAS EN EL DTO
		{
		
			for(int w=0; w<Utilidades.convertirAEntero(forma.getFacturas().get("numRegistros").toString()); w++)
			{
				dto = new DtoFactura();
				dto=Factura.cargarFactura(con, forma.getFacturas().get("codigo_"+w).toString(), true);
					
				// cargo el Array con el dto
				forma.getFacturaDto().add(dto);
			}
		
			UtilidadBD.iniciarTransaccion(con);
			mundo.setTransaccion(true);
			if(forma.getFacturaDto().size()<0)
			{
				logger.info("No hay Facturas para Subir al Plano");
			}
			else
			{
				mundo.armarPlano(con, numRegistros, forma.getFacturaDto(), forma.getPath(), forma.getNombre() ,errores, false);
			}
			forma.setBackupArchivo(forma.getNombre()+".zip"); //CARGO LA VARIABLE QUE PERMITE LA DESCARGA DEL ARCHIVO
			
			logger.info("Path Fac de Copia   ->"+forma.getPath());
			logger.info("Nombre Fac de Copia ->"+forma.getBackupArchivo());
			
			UtilidadFileUpload.generarCopiaArchivo(forma.getPath(), forma.getNombre()+".txt", "InterfazSistemaUno", forma.getBackupArchivo());
			
			//GENERO COPIA EN .ZIP PARA PERMITIR LA DESCARGA
			BackUpBaseDatos.EjecutarComandoSO("zip "+ forma.getPath()+"/"+forma.getNombre()+".zip"+" "+forma.getPath()+"/"+forma.getNombre()+".txt" );
			
			String ubicando = ValoresPorDefecto.getFilePath();
			
			BackUpBaseDatos.EjecutarComandoSO("cp "+ forma.getPath()+"/"+forma.getNombre()+".zip"+" "+ubicando+"InterfazSistemaUno");
			
			mundo.guardarArchivosInconsistencia(forma.getPath(), forma.getNombre(), mundo.getInconsistenciasMap());
			
			if(mundo.isTransaccion())
				UtilidadBD.finalizarTransaccion(con);
			else
				UtilidadBD.abortarTransaccion(con);
				
				
			forma.setContenidoArchivo(mundo.cargarArchivo(forma.getPath(), forma.getNombre()));
			
			if(forma.getContenidoArchivo() == null)
			{
				forma.setContenidoArchivo(new HashMap<String, Object>());
				forma.getContenidoArchivo().put("numRegistros","0");		
			}
		}
		return numRegistros;
				
	}


		public void insertarFormulario(Connection con, InterfazSistemaUnoForm forma, UsuarioBasico usuario, InterfazSistemaUno mundo)
		 {
			//Capturo los datos insertados en la JSP para mandar al dao
			 mundo.setInterfazSistemaUno(con,forma.getTipo(), forma.getReproceso(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getNombre(), forma.getPath(), usuario.getCodigoInstitucion(), usuario.getLoginUsuario());
		 }
}