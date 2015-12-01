package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.actionform.odontologia.BeneficiariosTarjetaClienteForm;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoParentesco;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.enums.odontologia.TipoBusquedaAliado;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Tercero;
import com.princetonsa.mundo.odontologia.AliadoOdontologico;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;
import com.princetonsa.sort.odontologia.SortGenerico;


/**
 * 
 * @author axioma
 *
 */
public class BeneficiariosTarjetaClienteAction extends Action {

	
	private DtoTarjetaCliente tipotarjeta = new DtoTarjetaCliente() ;
	private Logger logger = Logger.getLogger(BeneficiariosTarjetaClienteAction.class);
	private DtoBeneficiarioCliente dtoWhereLog = new DtoBeneficiarioCliente();
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof BeneficiariosTarjetaClienteForm ) 
		{
			
			BeneficiariosTarjetaClienteForm  forma = (BeneficiariosTarjetaClienteForm ) form;
			logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxestado-->" + forma.getEstado());
			ActionErrors errores = new ActionErrors();
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			
			
			if (forma.getEstado().equals("empezar")) 
			{
				forma.reset();
				return accionEmpezar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("ingresar")) 
			{
				return accionIngresar(mapping, forma, usuario);
			}
			
			else if(forma.getEstado().equals("busquedaAvanzadaSi")){
			
				return accionBusquedaAvanzadaSi(mapping, forma,usuario);
			}
			
			else if(forma.getEstado().equals("busquedaAvanzadaNo"))
			{
				return accionBusquedaAvanzadaNo(mapping, forma,usuario);
			}
			
			else if(forma.getEstado().equals("BusquedaEmpresarial")){
				
				return accionBusquedaAvanzadaSi(mapping, forma,usuario);
			}
			
            else if(forma.getEstado().equals("BusquedaPersonal")){
				
				return accionBusquedaAvanzadaSi(mapping, forma,usuario);
			}
			
               else if(forma.getEstado().equals("BusquedaFamiliar")){
				
				return accionBusquedaAvanzadaSi(mapping, forma,usuario);
			}
			//<<ACCIONE DE CONSULTA Y CONTINUAR >
			else if(forma.getEstado().equals("continuarSi"))
			{
				forma.setRecargaPaginaContinuarSi(ConstantesBD.acronimoNo);
				return accionContinuarSi(mapping, forma, usuario, errores, request);
			}
		    else if(forma.getEstado().equals("continuarNo"))
		    {
		    	forma.setRecargaPaginaContinuarNo(ConstantesBD.acronimoNo);
				return accionContinuarNo(mapping, forma, usuario, errores, request);
			}
			//<FIN DE ACCIONES >
			
		    // <<ACCIONES DE GUARDAR >>
		    else if(forma.getEstado().equals("GuardarSi"))
		    {
				forma.getDtoBeneficiario().setIndicadorAlidado(ConstantesBD.acronimoSi);
				return accionGuardarSi(mapping, forma, usuario, request);
			}
			else if(forma.getEstado().equals("GuardarNo")){
				forma.getDtoBeneficiario().setIndicadorAlidado(ConstantesBD.acronimoNo);
				return accionGuardarSi(mapping, forma, usuario, request);
			}
            //<< FIN ACCIONES GUARDAR>>
			
             else if(forma.getEstado().equals("Buscar")){
 				
				forma.getDtoBusquedaBeneficiario().setIndicadorAlidado(ConstantesBD.acronimoSi);
				forma.setEstado("mostrarResultadosSi");
				logger.info("\n\n\n\n CODIGO ALIDADO -------->"+forma.getAliadoOdontologico().getCodigo()+"\n\n\n\n");
 				return accionBuscar(mapping, forma, usuario);
 			}
		    else if(forma.getEstado().equals("BuscarNo")){
 				
            	forma.getDtoBusquedaBeneficiario().setIndicadorAlidado(ConstantesBD.acronimoNo);
            	forma.setEstado("mostrarResultadosNo");
 				return accionBuscar(mapping, forma, usuario);
 			}
			
			else if (forma.getEstado().equals("ordenar")) 
			{
				return accionOrdenar(mapping, forma, usuario);
			}
			
			
			else if (forma.getEstado().equals("empezarAliadoSi")) 
			{
				forma.setRecargaPaginaContinuarSi(ConstantesBD.acronimoNo);
				return mapping.findForward("paginaPrincipal");
			}
			
			else if (forma.getEstado().equals("empezarAliadoNo")) 
			{
				forma.setRecargaPaginaContinuarNo(ConstantesBD.acronimoNo);
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if (forma.getEstado().equals("continuarNoB")) 
			{
				forma.setRecargaPaginaContinuarNo(ConstantesBD.acronimoSi); //tarea 
				return mapping.findForward("paginaPrincipal");
			}
			
			else if (forma.getEstado().equals("continuarSiB"))
			{
				forma.setRecargaPaginaContinuarSi(ConstantesBD.acronimoSi);
				return mapping.findForward("paginaPrincipal");
			}
			else if(forma.getEstado().equals("cargarUsuarioAjax"))
			{
				accionCargarUsuarios(response, forma);
			}
			
				
			
			
		}
	return null;	
	}



	
	

	/**
	 * CARGAR LOS USUARIO DEL SISTEMA 
	 * @param response
	 * @param forma
	 */
	private void accionCargarUsuarios(HttpServletResponse response,
										BeneficiariosTarjetaClienteForm forma) 
	
	
	{
	
		
		Paciente paciente = new Paciente();
		paciente.setTipoIdentificacion(forma.getCodigoTipoIdentificacionB());
		paciente.setNumeroIdentificacion(forma.getNumeroIdentificacionB());
		
		forma.setCodigoTipoIdentificacionB("");
		forma.setNumeroIdentificacionB("");
		
		
		ArrayList<Paciente> listTmpPaciente = new ArrayList<Paciente>();
		
		if(  !UtilidadTexto.isEmpty(paciente.getTipoIdentificacion())   &&    !UtilidadTexto.isEmpty(paciente.getNumeroIdentificacion()) )
		{
			listTmpPaciente= (UtilidadesManejoPaciente.obtenerDatosPaciente(paciente));	
		}
		
		
		String etiquetaXml="";
		
		/*
		 *VALIDAR EXISTENCIA PACIENTES 
		 */
		if(listTmpPaciente.size()>0)
		{
			Paciente pacienteObtenido=listTmpPaciente.get(0);
				
			if(pacienteObtenido!=null)
			{
		            logger.info("\n nombre>>> "+pacienteObtenido.getPrimerNombrePersona()); 
					etiquetaXml+="<paciente>";
						etiquetaXml+="<nombre-primero>"+pacienteObtenido.getPrimerNombrePersona(false)+"</nombre-primero>";
						etiquetaXml+="<nombre-segundo>"+pacienteObtenido.getSegundoNombrePersona(false)+"</nombre-segundo>";
						etiquetaXml+="<apellido-primero>"+pacienteObtenido.getPrimerApellidoPersona(false)+"</apellido-primero>";
						etiquetaXml+="<apellido-segundo>"+pacienteObtenido.getSegundoApellidoPersona(false)+"</apellido-segundo>"; 
					etiquetaXml+="</paciente>";
			}
				
				 forma.setRespuestaXml(etiquetaXml);
				 logger.info("----------Imprimir Etiquiteas XML------------");
				 logger.info(forma.getRespuestaXml());
				
				 response.setContentType("text/xml");
				 response.setHeader("Cache-Control", "no-cache");
				 response.setCharacterEncoding("UTF-8");
				
				 try {
					 response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					 response.getWriter().write("<respuesta>");
					 response.getWriter().write(forma.getRespuestaXml());
					 response.getWriter().write("</respuesta>");
				} catch (IOException e) 
				{
					logger.info(e);
				}
			
		}// FIN 
		
		
	}


	


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaAvanzadaNo(ActionMapping mapping,
			BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		
		forma.setArrayResultadoBusqueda(new ArrayList<DtoBeneficiarioCliente>());
		
		forma.getDtoBusquedaPrincipal().reset();
		forma.getDtoBusquedaBeneficiario().reset();
		forma.setDatosComprador(new InfoDatosStr());
		
		
		cargarTiposdeIdentificacion(forma, usuario);
		return mapping.findForward("paginaBusqueda");
	}


   

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionContinuarNo(ActionMapping mapping,	BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request) {
		 	
			
		
			cargarTipoVenta(forma, usuario);
			logger.info("	 CARGAR BENEFICIARIOS	");
			DtoBeneficiarioCliente dtoWhere = accionCargaBeneficiarioAliadoNo(forma, usuario);
			logger.info(" //////////////////////////////////////////////////");
			
			if(forma.getListaBeneficiarios().size() <= 0)
			{
				limpiarDtoBeneficiario(forma);
				if(accionValidarSerial(forma, errores, request,ConstantesBD.acronimoNo))
				{
				   return accionEmpezar(mapping, forma, usuario);
				}
			}
			
			
		
			
			if(forma.getListaBeneficiarios().size() > 0)
			{
				//VALIDAR QUE ES INDICATIVO  EN SI : TIPO DE VENTA FAMILIAR 
				if(forma.getListaBeneficiarios().get(0).getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar) && forma.getListaBeneficiarios().get(0).getIndicativoPrincipal().equals(ConstantesBD.acronimoSi))
				{	
					logger.info("\n\n\n\n\n\n\t CARGA EL INDICATIVO PRINCIPAL ");
					accionIndicativoSiFamiliar(forma, dtoWhere);
				}
				else
				{
					//VALIDAR QUE ES INDICICATIVO EN NO: TIPO DE VENTA FAMILIAR
					if((forma.getListaBeneficiarios().get(0).getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar) && forma.getListaBeneficiarios().get(0).getIndicativoPrincipal().equals(ConstantesBD.acronimoNo)))
					{
						cargarIndicativoPrincipalVentaFamiliar(forma);
						//accionTipoVentaPersonal(forma, dtoWhere);
					}
		    		 else
		    		 {
			    		 if(forma.getListaBeneficiarios().get(0).getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa))
			    		 {
			    			 cargarCompradorTerceroVentaEmpresarial(forma);
						 }
			    	 }
        	   	}
      		}
			else
			{
				////NOseeeeeeeeeeeeeeeeee ??????????????????'
		 		if(!forma.getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa))
				  {
					 forma.setEstado("empezarAliadoNo");
				  }
				  else
				  {
					  logger.info("\n	VENTA EMPRESARIAL \n\n\n");
					  InfoDatosStr ventaCargar = new InfoDatosStr(); 
					  ventaCargar = VentasTarjetasCliente.retornarVenta(forma.getSerial(), usuario.getCodigoInstitucionInt(), forma.getDtoTipoTarjeta().getCodigoPk(), forma.getDtoBeneficiario().getCodigoPk());
					  //CARGAR DATOS DEL TERCERO DE LA VENTA
					  if(!UtilidadTexto.isEmpty(ventaCargar.getCodigo()) &&  !UtilidadTexto.isEmpty(ventaCargar.getNombre()))
					  {
						  if(Integer.parseInt(ventaCargar.getNombre())  >  0)
						  {
							   Connection con = UtilidadBD.abrirConexion();
							   Tercero tercero = new Tercero();
			                   if(tercero.cargarResumen(con, Integer.parseInt( ventaCargar.getNombre())))
			                   {
			                	   logger.info("CARGO DATOS COMPRADOR EN SERIAL NUEVO*****************************************************************");
			                	   forma.setDatosComprador(new InfoDatosStr(tercero.getNumeroIdentificacion(),tercero.getDescripcion()));
			                	   
			                	   //MIRAR ESTO NO ENTIENDO******************************************
			                	   forma.getDtoBeneficiario().setIndicativoPrincipal(ConstantesBD.acronimoSi); ///??
			                	   forma.getDtoBeneficiario().setVentaTarjetaCliente(Double.parseDouble(ventaCargar.getCodigo()));
			                  
			                   }
			                   UtilidadBD.closeConnection(con);
						  }
						  else
						  {
							  logger.info("*************************ES TIPO VENTA EMPRESARIAL Y NO EXISTE   ***ERROR     **********************************************************");
						  }
					  }
					  else
					  {
						  logger.info("*************************ES TIPO VENTA EMPRESARIAL Y NO EXISTE   ***ERROR     **********************************************************");
					  }
				 }//FIN VALIDACIONES DE TERCERO 
			  }//FIN SI CARGA VENTA---
   
			return mapping.findForward("paginaPrincipal");
	}





	
	/**
	 * CARGA COMPRADOR TERCERO VENTA EMPRESARIAL
	 * @param forma
	 */
	private void cargarCompradorTerceroVentaEmpresarial(BeneficiariosTarjetaClienteForm forma) {
		logger.info("*******************************************************  ENTRO POR EMPRESARIAL *******************************************************************");
		logger.info("*******************************************************  ENTRO POR EMPRESARIAL *******************************************************************");
		logger.info("*******************************************************  ENTRO POR EMPRESARIAL *******************************************************************");
		DtoVentaTarjetasCliente dtoTerceroAconsultar = new DtoVentaTarjetasCliente();
		logger.info(" CODIGO VENTA================================================================"+forma.getListaBeneficiarios().get(0).getVentaTarjetaCliente());
		dtoTerceroAconsultar.setCodigoPk(forma.getListaBeneficiarios().get(0).getVentaTarjetaCliente());
		Tercero tercero = new Tercero();
		Connection con = UtilidadBD.abrirConexion();
		
		forma.setListaVentas(VentasTarjetasCliente.cargar(dtoTerceroAconsultar));
		  
		if(forma.getListaVentas().size() > 0)
		{
			/*
			logger.info("CARGAR DATOS COMPRADOR ");
			if(tercero.cargarResumen(con, Integer.parseInt(forma.getListaVentas().get(0).getTercero())))
			{
				logger.info("CARGO DATOS COMPRADOR*****************************************************************");
				forma.setDatosComprador(new InfoDatosStr(tercero.getNumeroIdentificacion(),tercero.getDescripcion()));
			}*/
		}
  	  
		  try
		  {
			  UtilidadBD.cerrarConexion(con);
		  } 
		  catch (SQLException e)
		  {
			  e.printStackTrace();
		  }
	}




	
	
	/**
	 * CARGAR  EL BENEFICIARIO CON INDICATIVO PRINCIPAL DE LA VENTA TIPO FAMILIAR 
	 * @param forma
	 */
	private void cargarIndicativoPrincipalVentaFamiliar(BeneficiariosTarjetaClienteForm forma) {
		logger.info("*******************************************************  ENTRO POR FAMILIAR NO *******************************************************************");
		logger.info("*******************************************************  ENTRO POR FAMILIAR NO *******************************************************************");
		logger.info("*******************************************************  ENTRO POR FAMILIAR NO *******************************************************************");
		//POSTULO EL PRINCIPAL
		logger.info("\n 		TIPO DE VENTA FAMILIAR INDICATIVO INDICATIVO PRINCIPAL NO \n\n\n\n\n" +
				"CARGAR EL INDICATIVO PRINCIAPAL \n\n\n");
		DtoBeneficiarioCliente dtoWhereP = new DtoBeneficiarioCliente();
		dtoWhereP.setIndicativoPrincipal(ConstantesBD.acronimoSi);
		dtoWhereP.setVentaTarjetaCliente(forma.getListaBeneficiarios().get(0).getVentaTarjetaCliente());

		
		if(BeneficiariosTarjetaCliente.cargar(dtoWhereP).size() > 0)
		{
			logger.info("\n\n\n\n\n\n EXISTEN EL INDICATIVO PRINCIPAL");
			forma.setDtoPrincipal(BeneficiariosTarjetaCliente.cargar(dtoWhereP).get(0));
		}
		else
		{
			logger.info("*************** EL PRINCIPAL NO EXISTE*******************************");
		}
	}





	/**
	 * ACCION CARGAR BENEFICIARIOS 
	 * SI EXISTEN BENEFICIARIO LLENA LA LISTA
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private DtoBeneficiarioCliente accionCargaBeneficiarioAliadoNo( BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
		DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();
		dtoWhere.setSerial(forma.getSerial());		 
		dtoWhere.setIndicadorAlidado(ConstantesBD.acronimoNo);
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		
		DtoTarjetaCliente dtoTipoTarjeta = new DtoTarjetaCliente();
		dtoTipoTarjeta.setCodigoPk(forma.getDtoTipoTarjeta().getCodigoPk());
		dtoTipoTarjeta.setAliado(ConstantesBD.acronimoNo);
		
		dtoWhere.setTipoTarjetaCliente(dtoTipoTarjeta);
		forma.setListaBeneficiarios(new ArrayList<DtoBeneficiarioCliente>());
		forma.setListaBeneficiarios(BeneficiariosTarjetaCliente.cargar(dtoWhere));
		
		if(forma.getListaBeneficiarios().size()>0)
		{
			forma.setModificando(Boolean.TRUE); //pilasssssssssssssssssssssssssss
			forma.setDtoBeneficiario(forma.getListaBeneficiarios().get(0)); //pilassssssssssssss
			if(UtilidadTexto.isEmpty(forma.getTipoVenta()))
			{
				forma.setTipoVenta(forma.getDtoBeneficiario().getTipoVenta());
			}
			//dtoWhereLog = (DtoBeneficiarioCliente)forma.getDtoBeneficiario().clone();
		}
		
		return dtoWhere;
	}




	/**
	 * CARGAR EL TIPO DE VENTA
	 * @param forma
	 * @param usuario
	 */
	private void cargarTipoVenta(BeneficiariosTarjetaClienteForm forma,	UsuarioBasico usuario) {
		//SE OBTIENE EL TIPO DE VENTA
		forma.setTipoVenta( VentasTarjetasCliente.retornarVenta( forma.getSerial(), usuario.getCodigoInstitucionInt(),  forma.getDtoTipoTarjeta().getCodigoPk(), forma.getDtoBeneficiario().getCodigoPk() ).getDescripcion() );
		logger.info("\n\n	TIPO DE VENTA ------------------>"+forma.getTipoVenta()+"<---");
	}





	
	/**
	 * LIMPIA EL DTO BENEFICIARIO
	 * CAMBIA EL ESTADO A GUARDAR 
	 * SETTEA EL CODIGO DEL SERIAL 
	 * @param forma
	 */
	private void limpiarDtoBeneficiario(BeneficiariosTarjetaClienteForm forma) {
		forma.setDtoBeneficiario(new DtoBeneficiarioCliente());
		forma.getDtoBeneficiario().setSerial(forma.getSerial());
		forma.setModificando(Boolean.FALSE); 
	}


	
	
	
	/**
	 * ACCION TIPO VENTA PERSONAL-- SETTEAR EL BENEFICIARIO Y LO CLONA EN UN DTOWHERELOG
	 * @param forma
	 * @param dtoWhere
	 */
	private void accionTipoVentaPersonal(BeneficiariosTarjetaClienteForm forma,	DtoBeneficiarioCliente dtoWhere) 
	{
			logger.info("--------------------                     SETTEAR BENEFICIARIO            -------------");
			logger.info("**************************************************************************************");
			if(forma.getListaBeneficiarios().size()>0)
			{
				
				logger.info("\n\n	EXISTE INFORMACION EN LA LISTA ");
				forma.setDtoBeneficiario(forma.getListaBeneficiarios().get(0)); //SOLO UN BENEFICIARIO CON UN SERIAL ALIDO EN SI
			}
			else
			{
				forma.setListaBeneficiarios(BeneficiariosTarjetaCliente.cargar(dtoWhere));
				if(forma.getListaBeneficiarios().size()>0)
				{
					forma.setDtoBeneficiario(forma.getListaBeneficiarios().get(0));
				}
				else
				{
					forma.setDtoBeneficiario(new DtoBeneficiarioCliente());
				}
				
			}
			
			forma.getDtoBeneficiario().setTipoTarjetaCliente(tipotarjeta);//??????? QUEEEEEEE
			
			//dtoWhereLog = (DtoBeneficiarioCliente)forma.getDtoBeneficiario().clone();
			logger.info("EL BENEFICIARIO SERIA"+UtilidadLog.obtenerString(forma.getDtoBeneficiario(),true));
	}



    /**
     * 
     * @param forma
     * @param dtoWhere
     */
	private void accionIndicativoSiFamiliar(BeneficiariosTarjetaClienteForm forma,	DtoBeneficiarioCliente dtoWhere) 
	{
		logger.info("ENTRA POR EL FLUJO DE LOS FAMILIAR  ACRONIMOS ");
		
		forma.setDtoBeneficiario(forma.getListaBeneficiarios().get(0)); //???
		forma.setDtoPrincipal(forma.getListaBeneficiarios().get(0));
		forma.getDtoBeneficiario().setTipoTarjetaCliente(forma.getDtoTipoTarjeta());
		forma.getDtoPrincipal().setTipoTarjetaCliente(forma.getDtoTipoTarjeta());	
		//dtoWhereLog = (DtoBeneficiarioCliente)forma.getDtoPrincipal().clone();
	}



  

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarEmpresarial(ActionMapping mapping,
			BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
			forma.getDtoBusquedaBeneficiario().setTipoVenta(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa);
			forma.setArrayResultadoBusqueda(BeneficiariosTarjetaCliente.cargarAvanzadoEmpresa(forma.getDtoBusquedaBeneficiario(), forma.getDatosComprador()));
			return mapping.findForward("paginaBusqueda");
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarFamiliar(ActionMapping mapping,
			BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
			forma.getDtoBusquedaBeneficiario().setTipoVenta(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar);
			forma.getDtoBusquedaPrincipal().setTipoVenta(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar);
			forma.setArrayResultadoBusqueda(BeneficiariosTarjetaCliente.cargarAvanzadoFamiliar(forma.getDtoBusquedaBeneficiario(), forma.getDtoBusquedaPrincipal()));
			forma.setEstado("mostrarResultados");
			return mapping.findForward("paginaBusqueda");
	}



  
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getArrayResultadoBusqueda(),sortG);
		
		if(forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona) || forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar) || forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa)  ){
		forma.setEstado("mostrarResultadosNo");
		}else{
			forma.setEstado("mostrarResultadosSi");
		}
		return mapping.findForward("paginaBusqueda");
		
	}
	
	

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscar(ActionMapping mapping,BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		//RESETEO VALORES
		logger.info("EL TIPO DE BUSQUEDA ES =========================================="+forma.getDtoBeneficiario().getTipoVenta());
		
		logger.info("\n\n ALIDADO ------------------>"+forma.getAliadoOdontologico().getCodigo()+" \n\n\n");
		
		if(forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
		{
			forma.getDtoBusquedaBeneficiario().setTipoVenta(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona);
			forma.setArrayResultadoBusqueda(BeneficiariosTarjetaCliente.cargar(forma.getDtoBusquedaBeneficiario()));
			//logger.info(UtilidadLog.obtenerString(forma.getDtoBusquedaBeneficiario(), true));
		}
		else
		{
			if(forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
			{
				this.accionBuscarFamiliar(mapping, forma, usuario);
			}
			else 
			{
				if(forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa))
				{
					this.accionBuscarEmpresarial(mapping, forma, usuario);
				}
				else
				{
					forma.setArrayResultadoBusqueda(BeneficiariosTarjetaCliente.cargar(forma.getDtoBusquedaBeneficiario()));
					//logger.info(UtilidadLog.obtenerString(forma.getDtoBusquedaBeneficiario(), true));
				}
			}
		}
		return mapping.findForward("paginaBusqueda");
	}


     /**
      * 
      * 
      * 
      * @param mapping
      * @param forma
      * @param usuario
      * @return
      */

	private ActionForward accionBusquedaAvanzadaSi(ActionMapping mapping,BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
	
		logger.info(" \n\n\n\n NOMBRE ALIDADO ");
		logger.info(" CODIGO ALIADO-------------------------->"+forma.getAliadoOdontologico().getCodigo());
		TipoBusquedaAliado tipoBusqueda= TipoBusquedaAliado.ALIADO_SI; 
		resetearBusqueda(forma);
		forma.setTipoBusqueda(tipoBusqueda);
		cargarTiposdeIdentificacion(forma, usuario);
		
		return mapping.findForward("paginaBusqueda");
	}




	/**
	 * 
	 * @param forma
	 */
	private void resetearBusqueda(BeneficiariosTarjetaClienteForm forma) {
		/**
		 * RESETEO
		 */
		forma.setArrayResultadoBusqueda(new ArrayList<DtoBeneficiarioCliente>());
		forma.getDtoBeneficiario().setTipoVenta("");
		forma.getDtoBusquedaPrincipal().reset();
		forma.getDtoBusquedaBeneficiario().reset();
		forma.setDatosComprador(new InfoDatosStr());
	}
	
	
	
	/**
	 * ACCION GUARDAR CON INDICATIVO ALIADO EN SI
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarSi(ActionMapping mapping,BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		return null;
		
		//boolean guardo=false;
		//ActionErrors errores= new ActionErrors();
				
		
		//forma.setIdBeneficiarios(BeneficiariosTarjetaCliente.consultar(forma.getDtoBeneficiario().getTipoIndentificacion(), forma.getDtoBeneficiario().getNumeroIdentificacion())); //??error
		
		//int n=Utilidades.convertirAEntero(forma.getIdBeneficiarios().get("numRegistros")+"");
		/// VALIDA EXISTENCIA DE NUMERO Y TIPO DE INDENTIFICACION OJO
	
		/*for(int i=0;i<n;i++)
		{
			if((forma.getIdBeneficiarios().get("numid_"+i)+"").equals(forma.getDtoBeneficiario().getNumeroIdentificacion()))
			{	
				if((forma.getIdBeneficiarios().get("tipoid_"+i)+"").equals(forma.getDtoBeneficiario().getTipoIndentificacion()))
				{
					errores.add("", new ActionMessage("errors.yaExiste", "El Beneficiario "));
					i=n;
				}
			}
		}
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request,errores);
		}
		else
		{		

			//FLUJO PARA GUARDAR 
			
			forma.getDtoBeneficiario().setFechaModifica(UtilidadFecha.getFechaActual());
			forma.getDtoBeneficiario().setHoraModifica(UtilidadFecha.getHoraActual());
			forma.getDtoBeneficiario().setUsuarioModifica(usuario.getLoginUsuario());
		 	forma.getDtoBeneficiario().setInstitucion(usuario.getCodigoInstitucionInt());
			
		 	
		 	if(forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar) && forma.getDtoBeneficiario().getIndicativoPrincipal().equals(ConstantesBD.acronimoNo) )
			 {
				logger.info("?????????????????????????????????????????????????????????????'NOooooo''");
		 		forma.getDtoBeneficiario().setIndicativoPrincipal(ConstantesBD.acronimoNo);
			 }
			 else
			 {
				 forma.getDtoBeneficiario().setIndicativoPrincipal(ConstantesBD.acronimoSi);
				 logger.info("????????????????????????????????????????????????????????????? SI");
			 }
			forma.getDtoBeneficiario().getTipoTarjetaCliente().setCodigoPk(forma.getDtoTipoTarjeta().getCodigoPk());
			
			
			logger.info("\n\n\n\n	EMPEZAR A GUARDAR  BENEFICIARIO");
			logger.info("es Modificado ->"+forma.isModificando()+"<-");
			if(!forma.isModificando())
			{
			    logger.info("GUARDANDO ************************************");
			    
			    //Tarea 136339 Para todo tipo de venta el estado es inactivo, asi ya este el registro de estado, podemos actualizarlo tranquilamente
			    forma.getDtoBeneficiario().setEstadoTarjeta(ConstantesIntegridadDominio.acronimoEstadoInactivo);
			    
				Connection con= UtilidadBD.abrirConexion();
				BeneficiariosTarjetaCliente.guardar(con, forma.getDtoBeneficiario());
			    UtilidadBD.closeConnection(con);
			    guardo=true;
			
			}
			else
			{
			
				logger.info("\n\n\n\n*******************************************************");
				logger.info(" codigo_Pk ="+forma.getDtoBeneficiario().getCodigoPk());
				logger.info(" 1Nombre   ="+forma.getDtoBeneficiario().getPrimerNombre());
				logger.info(" 1Apellido ="+forma.getDtoBeneficiario().getPrimerApellido());
				logger.info("CodigoVenta="+forma.getDtoBeneficiario().getVentaTarjetaCliente());
				logger.info("CodigoTarje="+forma.getDtoBeneficiario().getTipoTarjetaCliente().getCodigoPk()+" ");
			  	logger.info(" 	Serial  ="+forma.getDtoBeneficiario().getSerial());
			  	logger.info("aliadoOdon	="+forma.getDtoBeneficiario().getAlidadoOdontologico().getCodigo());
				logger.info("\n\n\n\n**********************************************************");
			  	
				if((forma.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar)) && forma.getDtoBeneficiario().getIndicativoPrincipal().equals(ConstantesBD.acronimoSi) )
				{
					logger.info("	MODIFICAR TIPO FAMILIAR EN SI");
					modificarTipoFamiliarSi(forma, usuario);
			    }
				 else
				 {
				 	//DATOS SETIADOS PARA GUARDAR
					logger.info("******************************************ENTRE POR MODIFICAR SOLO BENEFICIARIO***************************************************************");
				    modificarSoloBeneficiario(forma, usuario);
			     }				
				 guardo=true;
			}
		}
		//forma.reset();
		if(guardo)
			forma.setEstadoAnterior("guardado");//////////////////////PROBLEMAS 
		forma.setEstado("empezar");
		//return accionEmpezar(mapping, forma, usuario);
		return accionProcesoExitoso(mapping, forma, usuario);
		*/
	}

 
	

	
    /**
     * MOIDIFICAR BENEFICIARIO
     * @param forma
     * @param usuario
     */
	private void modificarSoloBeneficiario(	BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
		logger.info("MODIFICANDO ************************************");
		forma.setEstadoAnterior("guardado");
		DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();
		dtoWhere.setCodigoPk(forma.getDtoBeneficiario().getCodigoPk());
		dtoWhere.setVentaTarjetaCliente(forma.getDtoBeneficiario().getVentaTarjetaCliente());
		forma.getDtoBeneficiario().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoBeneficiario().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoBeneficiario().setUsuarioModifica(usuario.getLoginUsuario());
		BeneficiariosTarjetaCliente.modificar(forma.getDtoBeneficiario() ,dtoWhere);
		UtilidadLog.generarLog(usuario, this.dtoWhereLog, forma.getDtoBeneficiario() , ConstantesBD.tipoRegistroLogModificacion, ConstantesBD.logBeneficiarios);
	}



    /**
     * MODIFICAR TIPO VENTA FAMILIAR SI
     * @param forma
     * @param usuario
     */
	private void modificarTipoFamiliarSi(BeneficiariosTarjetaClienteForm forma,		UsuarioBasico usuario) 
	{
		    logger.info("GUARDANDO POR FLUJO FAMILIAR SI");
		    DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();
		    dtoWhere.setCodigoPk(forma.getDtoPrincipal().getCodigoPk());
		    forma.getDtoPrincipal().setFechaModifica(UtilidadFecha.getFechaActual());
			forma.getDtoPrincipal().setHoraModifica(UtilidadFecha.getHoraActual());
			forma.getDtoPrincipal().setUsuarioModifica(usuario.getLoginUsuario());
			BeneficiariosTarjetaCliente.modificar(forma.getDtoPrincipal(), dtoWhere);
			UtilidadLog.generarLog(usuario, this.dtoWhereLog, forma.getDtoPrincipal() , ConstantesBD.tipoRegistroLogModificacion, ConstantesBD.logBeneficiarios);
	}	


  
	/**
	 * ACCION CONTINUAR SI -- CONSULTAR LOS BENEFICIARIOS CON ALIADO EN SI  
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionContinuarSi(ActionMapping mapping,BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request) {
			
		
			//CREO EL DTOWHERE
			DtoBeneficiarioCliente dtoWhere = cargarBeneficiarioAliadoSi(forma,	usuario);
			if(forma.getListaBeneficiarios().size() > 0)
			{
				logger.info("\n\n\n	EXITE UN BENEFICIARIO PARA TIPO DE ALIADO SI. SE DISPONE A CARGAR ");
				accionTipoVentaPersonal(forma, dtoWhere); //HAY QUE BORRA ESTA LINEA
				//forma.setDtoBeneficiario(forma.getListaBeneficiarios().get(0));
				logger.info("\n\n\n\n OBJETO POSTULADO");
				forma.setModificando(Boolean.TRUE);
			}
			else
			{
				/**
				 * NO PUEDE REGISTRA DOS SERIALES IGUALES EN SI
				 */
				if(accionValidarSerial(forma, errores, request,ConstantesBD.acronimoSi))
				{
				   return accionEmpezar(mapping, forma, usuario);
				}
			   
			   if(! forma.isPrimeraEntrada())
			   {
					 logger.info("X ACA ENTRA **************************************** NO EXISTE ");
					 forma.reset();
					 forma.setEstado("empezar");
					 return accionEmpezar(mapping, forma, usuario);
				 }
			}
	      
		  forma.setPrimeraEntrada(false);
		  return mapping.findForward("paginaPrincipal");
	}






	/**
	 * CARGAR LOS BENEFICIARIOS PARA ALIDADO EN SI
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private DtoBeneficiarioCliente cargarBeneficiarioAliadoSi(	BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		logger.info("**********************************************************************");
		logger.info(" alaliadoaliadoaliadoaliadoaliadoaliadoiado------"+forma.getAliadoOdontologico().getCodigo());
		logger.info("**********************************************************************");
		logger.info("**********************************************************************");
		
		DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();		  
		dtoWhere.setSerial(forma.getSerial());		  
		dtoWhere.setIndicadorAlidado(ConstantesBD.acronimoSi);
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		dtoWhere.setAlidadoOdontologico(forma.getAliadoOdontologico());
		DtoTarjetaCliente dtoNewTarjeta = new DtoTarjetaCliente();	
		dtoNewTarjeta.setCodigoPk(forma.getDtoTipoTarjeta().getCodigoPk());
		dtoNewTarjeta.setAliado(forma.getDtoTipoTarjeta().getAliado());
		dtoWhere.setTipoTarjetaCliente(dtoNewTarjeta);
		logger.info("\n\n\n\n\n\n\n\n");
		logger.info("CARGANDO BENEFICIARIO TIPO ALIDADO SI");
		forma.setListaBeneficiarios(BeneficiariosTarjetaCliente.cargar(dtoWhere));
		
		if(forma.getListaBeneficiarios().size()>0)
		{
			logger.info("*******************************************************************");
			logger.info("SIIIIIIIIIIIIII ENCUENTRA UN REGISTRO BENEFICIARIO ");
			forma.setModificando(Boolean.TRUE);
			forma.setDtoBeneficiario(forma.getListaBeneficiarios().get(0));
			logger.info("TIPO DE VENTA BENEFICIARIO-->"+forma.getDtoBeneficiario().getTipoVenta());
		}
		else
		{
			logger.info("*******************************************************************");
			logger.info("NOOOOOOOOOO ENCUENTRA UN REGISTRO BENEFICIARIO ");
			forma.setDtoBeneficiario(new DtoBeneficiarioCliente());
			forma.getDtoBeneficiario().setSerial(forma.getSerial());
			forma.getDtoBeneficiario().setTipoTarjetaCliente(forma.getDtoTipoTarjeta());
			forma.getDtoBeneficiario().setAlidadoOdontologico(forma.getAliadoOdontologico());
			forma.setModificando(Boolean.FALSE);
		}
		
		return dtoWhere;
	}





	
	/**
	 * 	VALIDAR SERIAL EN SI y EN NO 
	 *  RETORNA  FALSE, NO ENCONTRO SERIAL 
	 *  RETORNA  TRUE, SI ENCONTRO SERIAL 
	 * @param forma
	 * @param errores
	 * @param request
	 */
	private boolean accionValidarSerial(BeneficiariosTarjetaClienteForm forma,	ActionErrors errores, HttpServletRequest request, String aliado) {
	
		DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();
		dtoWhere.setSerial(forma.getSerial());
		dtoWhere.setIndicadorAlidado(aliado);
	
		if( BeneficiariosTarjetaCliente.cargar(dtoWhere).size()>0)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "El Serial Ya se Encuentra Asignado "));
			saveErrors(request, errores);
			return Boolean.TRUE; 
		}
		return Boolean.FALSE;
	}


    
	/**
	 * CARGAR LA LISTA DE TIPOS DE INDENTIFICACION
	 * @param forma
	 * @param usuario
	 */
	private void cargarTiposdeIdentificacion(
			BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
			Connection con = UtilidadBD.abrirConexion();
			forma.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuario.getCodigoInstitucionInt()));
			UtilidadBD.closeConnection(con);
		}


    

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresar(ActionMapping mapping,	BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
	       
		forma.setEstadoAnterior("");
		//SETEO EL TIPO DE TARJETA ELEGIDA
		DtoTarjetaCliente dto =new DtoTarjetaCliente();     		
		dto.setCodigoPk(forma.getDtoBeneficiario().getTipoTarjetaCliente().getCodigoPk());
		forma.getDtoBeneficiario().setTipoTarjetaCliente(forma.getArrayTiposTarjetas().get(forma.getPosicionTarjeta()));
	    
		
		
		//nuevo
		forma.setDtoTipoTarjeta(forma.getArrayTiposTarjetas().get(forma.getPosicionTarjeta()));
	    
		
		//LA COLOCO EN UN OBJETO GLOBAL PARA SU POSTULACION
		tipotarjeta = forma.getDtoBeneficiario().getTipoTarjetaCliente();
		
		
		forma.getDtoBeneficiario().reset();
		 
		
		
		//IMPRIMO DATOS DE LA TRAJETA
		ArrayList<DtoTarjetaCliente> arrayTarjetas = new ArrayList<DtoTarjetaCliente>();
		arrayTarjetas = TarjetaCliente.cargar(dto);
		
		logger.info("LA POSICION SELECCIONADA ES +++++++++==="+forma.getPosicionTarjeta());
		// VEO TIPO DE ALIADO
		
		/**
		 * CARGAR TIPOS DE IDENTIFICACION
		 */
		
		cargarTiposdeIdentificacion(forma, usuario);
		forma.setArrayparentezco(UtilidadOdontologia.obtenerParentezco(new DtoParentesco()));
		
		if (arrayTarjetas.size() > 0 )
		{
		   if(arrayTarjetas.get(0).getAliado().equals(ConstantesBD.acronimoSi))
		   {
			   accionAliadoSi(forma, usuario);
		   }
		   else
		   {   //ALIDADO EN NO
			   forma.setEstado("empezarAliadoNo");
		   }
		}
		
		return mapping.findForward("paginaPrincipal");
	}

     
	
	


	
	/**
	 * ACCION ALIADO EN SI
	 * CARGAR EL ALIADO ODONTOLOGICO, SI LA TARJETA ESTA EN ALIDADO EN SI
	 * @param forma
	 * @param usuario
	 */
	private void accionAliadoSi(BeneficiariosTarjetaClienteForm forma,	UsuarioBasico usuario) {
		   logger.info("************** SIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII ");
		   DtoAliadoOdontologico aliadoWhere= new DtoAliadoOdontologico();
		   aliadoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		   aliadoWhere.setEstado(ConstantesBD.acronimoSi);
		   forma.setArrayAliados(AliadoOdontologico.cargar(aliadoWhere));
		   forma.setEstado("empezarAliadoSi");
	}
	

	
	
   
	/**
	 * 	ACCION EMPEZAR
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
			forma.reset();
			forma.setEstado("empezar");
			DtoTarjetaCliente dto =new DtoTarjetaCliente();
			dto.setInstitucion(usuario.getCodigoInstitucionInt());
			forma.setArrayTiposTarjetas(TarjetaCliente.cargar(dto));
        	return mapping.findForward("paginaPrincipal");
	}
	
	
	
	
	/**
	 * 	ACCION PROCESO EXISITOSO 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionProcesoExitoso(ActionMapping mapping, BeneficiariosTarjetaClienteForm forma, UsuarioBasico usuario) {
			return mapping.findForward("paginaResumen");
	}
	//********
}
