/**
 * 
 */
package com.princetonsa.action.carteraPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.carteraPaciente.SaldosInicialesCarteraPacienteForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.DatosFinanciacion;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;
import com.princetonsa.mundo.carteraPaciente.SaldosInicialesCarteraPaciente;

/**
 * @author axioma
 *
 */
public class SaldosInicialesCarteraPacienteAction extends Action 
{

	Logger logger = Logger.getLogger(SaldosInicialesCarteraPacienteAction.class);
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
	{
		
		if (form instanceof SaldosInicialesCarteraPacienteForm)
		{
			SaldosInicialesCarteraPacienteForm forma=(SaldosInicialesCarteraPacienteForm)form;
		
			String estado = forma.getEstado();
			
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			logger.info("estado -->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no Valido dentro del Saldo Iniciales Cartera Paciente(null)");				 
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setPaises(Utilidades.obtenerPaises());
				return mapping.findForward("empezar");
			}
			else if(estado.equals("modificar"))
			{
				forma.reset();
				forma.setPaises(Utilidades.obtenerPaises());
				forma.setConvenios(Utilidades.obtenerConvenios("", "",false, "", true,"","", ""));
				forma.setTipoIdentificacionMap(Utilidades.obtenerTiposIdentificacion("",usuario.getCodigoInstitucionInt()));
				return mapping.findForward("busquedaRegistros");	
			}
			else if(estado.equals("nuevo"))
			{
				this.empezarNuevoDoc(forma,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario());
				return mapping.findForward("registroSaldoInicial");	
			}
			else if(estado.equals("cargarPaciente"))
			{
				
				Usuario persona=new Usuario();
				if(persona.cargarPersona(forma.getNumeroIdTemp(), forma.getTipoIdTemp()))
				{
					forma.setPacienteCargado(true);
					forma.getPaciente().setNumeroId(forma.getNumeroIdTemp());
					forma.getPaciente().setTipoId(forma.getTipoIdTemp());
					forma.getPaciente().setCodigo(persona.getCodigoPersona());
					forma.getPaciente().setPrimerNombre(persona.getPrimerNombrePersona());
					forma.getPaciente().setSegundoNombre(persona.getSegundoNombrePersona());
					forma.getPaciente().setPrimerApellido(persona.getPrimerApellidoPersona());
					forma.getPaciente().setSegundoApellido(persona.getSegundoApellidoPersona());
					forma.getPaciente().setDireccion(persona.getDireccion());
					forma.getPaciente().setTelefono(persona.getTelefono());
				}
				else
				{
					forma.setPacienteCargado(false);
				}
				return mapping.findForward("xmlPaciente");
            }
			else if(estado.equals("cargarDatosDeudor"))
            {
            	cargarDatosDeudor(forma,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario());
            	return mapping.findForward("registroSaldoInicial");	
            }
			else if(estado.equals("cargarDatosCodeudor"))
            {
            	cargarDatosCodeudor(forma,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario());
            	return mapping.findForward("registroSaldoInicial");	
            }
			else if(estado.equals("ponerNumeroCuotas"))
			{
				inicializarCuotasFinaciacion(forma,usuario.getLoginUsuario());
				return mapping.findForward("registroSaldoInicial");	
			}
			else if(estado.equals("ponerNumeroCuotasModificacion"))
			{
				inicializarCuotasFinaciacion(forma,usuario.getLoginUsuario());
				return mapping.findForward("modificacion");	
			}
			else if(estado.equals("guardarDatosFinan"))
            {
				return guardarDatosRSCP(forma,usuario,mapping);
            }
			else if(estado.equals("modificarDatosFinan"))
            {
				return modificarDatosRSCP(forma,usuario,mapping);
            }
			else if(estado.equals("cargarModificar"))
            {
				return cargarModificar(forma,usuario,mapping);
            }
			else if(estado.equals("buscarFactura"))
            {
				return mapping.findForward("registroSaldoInicial");
            }
			else if(estado.equals("cambiarPais"))
            {
				return mapping.findForward("registroSaldoInicial");
            }
			else if(estado.equals("validarConsecutivo"))
			{
				return validarConsecutivoAJAX(forma,response,usuario.getCodigoInstitucionInt());
			}
			else if(estado.equals("buscarFacturas"))
			{
				forma.setFacturas(DatosFinanciacion.consultarFacturas(forma.getFacturaBusqueda(),forma.getDatosFinanciacion().getDeudor().getTipoIdentificacion(),forma.getDatosFinanciacion().getDeudor().getNumeroIdentificacion(),forma.getDatosFinanciacion().getCodeudor().getTipoIdentificacion(),forma.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion()));
				return mapping.findForward("busquedaFacturas");
			}
			else if(estado.equals("filtroCiudades"))
			{
				return accionFiltroCiudades(forma,response);
			}
			else if(estado.equals("buscar"))
			{
				forma.setSaldosIniciales(SaldosInicialesCarteraPaciente.busquedaAvanzadaSaldosIncialesCP(this.cargarVOBusquedaSaldosInciales(forma)));
				return mapping.findForward("listadoConsulta");	
			}
            
		}				
		return null;
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarModificar(
			SaldosInicialesCarteraPacienteForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		Connection con=UtilidadBD.abrirConexion();
		
		//Cargar Info Paciente
		forma.setPaciente(cargarInfoPaciente(con, forma.getSaldosIniciales().get(forma.getPosArray()).getCodigoPaciente()));
		
		//Cargar Info Financiación
		forma.setDatosFinanciacion(cargarInfoFinanciacion(con, usuario.getCodigoInstitucion(), forma, usuario));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificacion");
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param forma 
	 * @param usuario 
	 * @param numeroId 
	 * @param tipoId 
	 * @param codigoPaciente
	 * @return
	 */
	private DtoDatosFinanciacion cargarInfoFinanciacion(Connection con,
			String codigoInstitucion, SaldosInicialesCarteraPacienteForm forma, UsuarioBasico usuario) {
		
		HashMap deudorCo = new HashMap();
		HashMap parametros = new HashMap();
		parametros.put("institucion", codigoInstitucion);
		parametros.put("tipoIdentificacion", forma.getPaciente().getTipoIdentificacion());
		parametros.put("numeroIdentificacion", forma.getPaciente().getNumeroIdentificacion());
		deudorCo = DocumentosGarantia.consultarDeudorCo(con, parametros);
		
		DtoDatosFinanciacion dto = new DtoDatosFinanciacion();
		
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		
		Utilidades.imprimirMapa(deudorCo);
		
		// Cargar Datos Deudor
		dto.getDeudor().setTipoDeudor(deudorCo.get("tipoDeudorCo")+"");
		dto.getDeudor().setTipoIdentificacion(deudorCo.get("tipoIdentificacion")+"");
		dto.getDeudor().setNumeroIdentificacion(deudorCo.get("numeroIdentificacion")+"");
		dto.getDeudor().setPaisID(deudorCo.get("codigoPais")+"");
		dto.getDeudor().setDepartamentoID(deudorCo.get("codigoDepartamento")+"");
		dto.getDeudor().setCiudadID(deudorCo.get("codigoCiudad")+"");
		dto.getDeudor().setPrimerApellido(deudorCo.get("primerApellido")+"");
		dto.getDeudor().setSegundoApellido(deudorCo.get("segundoApellido")+"");
		dto.getDeudor().setPrimerNombre(deudorCo.get("primerNombre")+"");
		dto.getDeudor().setSegundoNombre(deudorCo.get("segundoNombre")+"");
		dto.getDeudor().setDireccion(deudorCo.get("direccionReside")+"");
		dto.getDeudor().setTelefono(deudorCo.get("telefonoReside")+"");
		dto.getDeudor().setOcupacion(deudorCo.get("ocupacion")+"");
		dto.getDeudor().setEmpresa(deudorCo.get("empresa")+"");
		dto.getDeudor().setCargo(deudorCo.get("cargo")+"");
		dto.getDeudor().setAntiguedad(deudorCo.get("antiguedad")+"");
		dto.getDeudor().setDireccionOficina(deudorCo.get("direccionOficina")+"");
		dto.getDeudor().setTelefonoOficina(deudorCo.get("telefonoOficina")+"");
		dto.getDeudor().setNombresReferenciaFamiliar(deudorCo.get("nombresReferencia")+"");
		dto.getDeudor().setDireccionReferenciaFamiliar(deudorCo.get("direccionReferencia")+"");
		dto.getDeudor().setTelefonoReferenciaFamiliar(deudorCo.get("telefonoReferencia")+"");
		dto.getDeudor().setCodigoPK(Utilidades.convertirAEntero(deudorCo.get("codigoPk")+""));
		dto.getDeudor().setIngreso(Utilidades.convertirAEntero(deudorCo.get("ingreso")+""));
		dto.getDeudor().setInstitucion(Utilidades.convertirAEntero(deudorCo.get("institucion")+""));
		forma.setDeptoCiudadIDDeudor(deudorCo.get("codigoDepartamento")+ConstantesBD.separadorSplit+deudorCo.get("codigoCiudad"));
		
		deudorCo = new HashMap();
		parametros = new HashMap();
		parametros.put("institucion", codigoInstitucion);
		parametros.put("tipoIdentificacion", forma.getPaciente().getTipoIdentificacion());
		parametros.put("numeroIdentificacion", forma.getPaciente().getNumeroIdentificacion());
		deudorCo = DocumentosGarantia.consultarDeudorCo(con, parametros);
		
		// Cargar Datos Deudor
		dto.getCodeudor().setTipoDeudor(deudorCo.get("tipoDeudorCo")+"");
		dto.getCodeudor().setTipoIdentificacion(deudorCo.get("tipoIdentificacion")+"");
		dto.getCodeudor().setNumeroIdentificacion(deudorCo.get("numeroIdentificacion")+"");
		dto.getCodeudor().setPaisID(deudorCo.get("codigoPais")+"");
		dto.getCodeudor().setDepartamentoID(deudorCo.get("codigoDepartamento")+"");
		dto.getCodeudor().setCiudadID(deudorCo.get("codigoCiudad")+"");
		dto.getCodeudor().setPrimerApellido(deudorCo.get("primerApellido")+"");
		dto.getCodeudor().setSegundoApellido(deudorCo.get("segundoApellido")+"");
		dto.getCodeudor().setPrimerNombre(deudorCo.get("primerNombre")+"");
		dto.getCodeudor().setSegundoNombre(deudorCo.get("segundoNombre")+"");
		dto.getCodeudor().setDireccion(deudorCo.get("direccionReside")+"");
		dto.getCodeudor().setTelefono(deudorCo.get("telefonoReside")+"");
		dto.getCodeudor().setOcupacion(deudorCo.get("ocupacion")+"");
		dto.getCodeudor().setEmpresa(deudorCo.get("empresa")+"");
		dto.getCodeudor().setCargo(deudorCo.get("cargo")+"");
		dto.getCodeudor().setAntiguedad(deudorCo.get("antiguedad")+"");
		dto.getCodeudor().setDireccionOficina(deudorCo.get("direccionOficina")+"");
		dto.getCodeudor().setTelefonoOficina(deudorCo.get("telefonoOficina")+"");
		dto.getCodeudor().setNombresReferenciaFamiliar(deudorCo.get("nombresReferencia")+"");
		dto.getCodeudor().setDireccionReferenciaFamiliar(deudorCo.get("direccionReferencia")+"");
		dto.getCodeudor().setTelefonoReferenciaFamiliar(deudorCo.get("telefonoReferencia")+"");
		dto.getCodeudor().setCodigoPK(Utilidades.convertirAEntero(deudorCo.get("codigoPk")+""));
		dto.getCodeudor().setIngreso(Utilidades.convertirAEntero(deudorCo.get("ingreso")+""));
		dto.getCodeudor().setInstitucion(Utilidades.convertirAEntero(deudorCo.get("institucion")+""));
		forma.setDeptoCiudadIDCodeudor(deudorCo.get("codigoDepartamento")+ConstantesBD.separadorSplit+deudorCo.get("codigoCiudad"));
		
		// Cargar Datos Financiación
		dto = SaldosInicialesCarteraPaciente.cargarDatosFinanciacion(con, dto);
		
		// Valores x Defecto
		dto.setMaxNroCuotasFinan(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(Utilidades.convertirAEntero(codigoInstitucion))+""));
		dto.setMaxNroDiasFinanCuo(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(Utilidades.convertirAEntero(codigoInstitucion))+""));
		
		return dto;
	}

	/**
	 * Cargar Info Paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	private DtoPaciente cargarInfoPaciente(Connection con, int codigoPaciente) {
		PersonaBasica persona = new PersonaBasica();
		DtoPaciente paciente = new DtoPaciente();
		try {
			persona.cargar(con, codigoPaciente);
		} catch (Exception e) {
			logger.info("Exception : "+e);
		}
		paciente.setTipoId(persona.getTipoIdentificacionPersona(false));
		paciente.setNumeroId(persona.getNumeroIdentificacionPersona());
		paciente.setPrimerApellido(persona.getPrimerApellido());
		paciente.setSegundoApellido(persona.getSegundoApellido());
		paciente.setPrimerNombre(persona.getPrimerNombre());
		paciente.setSegundoNombre(persona.getSegundoNombre());
		paciente.setDireccion(persona.getDireccion());
		paciente.setTelefono(persona.getTelefono());
		paciente.setTipoIdentificacion(persona.getCodigoTipoIdentificacionPersona());
		paciente.setCodigo(persona.getCodigoPersona());
		return paciente;
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward modificarDatosRSCP(
			SaldosInicialesCarteraPacienteForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		boolean transaccion=true;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		// Actualizar Paciente
		if(transaccion && !UtilidadTexto.isEmpty(forma.getPaciente().getTipoId()) && !UtilidadTexto.isEmpty(forma.getPaciente().getNumeroId()))
		{
			forma.getPaciente().setCodigo(SaldosInicialesCarteraPaciente.insertarActualizarPersona(con,forma.getPaciente()));
			transaccion=(forma.getPaciente().getCodigo()>0);
		}
		
		// Actualizar Deudor
		if(transaccion)
			transaccion = DatosFinanciacion.actualizarDeudor(con, forma.getDatosFinanciacion().getDeudor());
		
		// Actualizar Co Deudor
		if(transaccion)
			transaccion = DatosFinanciacion.actualizarDeudor(con, forma.getDatosFinanciacion().getCodeudor());
		
		// Actualizar Datos Financiacion
		if(transaccion)
			transaccion = SaldosInicialesCarteraPaciente.actualizarDatosFinanciacion(con, forma.getDatosFinanciacion());
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("modificacion");
		}
		else
			UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return null;
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private HashMap<String, String> cargarVOBusquedaSaldosInciales(SaldosInicialesCarteraPacienteForm forma)
	{
		HashMap<String, String> mapa=new HashMap<String, String>();
		mapa.put("tipoDocGarantia", forma.getTipoDocumentoGarantia());
		mapa.put("fechaDocGarantia", forma.getFechaDocumentoGarantia());
		mapa.put("numDocGarantia", forma.getNumeroDocumentoGarantia());
		mapa.put("tipoBusqueda",forma.getTipoBusqueda());
		mapa.put("tipoId", forma.getTipoID());
		mapa.put("numeroId", forma.getNumeroID());
		mapa.put("nombrePersona", forma.getNombrePersona());
		mapa.put("apellidoPersona", forma.getApellidoPersona());
		mapa.put("docInicial",forma.getDocumentoInicial());
		mapa.put("docFinal", forma.getDocumentoFinal());
		mapa.put("convenio", forma.getConvenio());
		return mapa;
	}
	/**
	 * 
	 * @param forma
	 * @param response 
	 * @return
	 */
	private ActionForward validarConsecutivoAJAX(SaldosInicialesCarteraPacienteForm forma, HttpServletResponse response,int institucion) 
	{
		try
		{
			String respuesta="";
			String primerCons=DatosFinanciacion.obtenerPrimerConsecutivoUsadoDocumento(forma.getTipoDocGarantiaTempo(),institucion);
			if(Utilidades.convertirAEntero(primerCons)>0&&Utilidades.convertirAEntero(primerCons)<=Utilidades.convertirAEntero(forma.getConsecutivoDocumentoTempo()))
			{
				respuesta="<font color=\"red\"><b>El consecutivo es mayor al primer consecutivo usado ("+primerCons+").</b></font>";
			}
			if(DatosFinanciacion.esConsecutivoDocumentoUsado(forma.getTipoDocGarantiaTempo(),forma.getConsecutivoDocumentoTempo(),institucion))
			{
				if(!UtilidadTexto.isEmpty(respuesta))
					respuesta=respuesta+"<br/>";
				respuesta=respuesta+"<font color=\"red\"><b>El consecutivo ya fue usado.</b></font";
			}
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(respuesta);
			
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarMediosEnvios: "+e);
		}
		return null;
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping 
	 * @return 
	 */
	private ActionForward guardarDatosRSCP(SaldosInicialesCarteraPacienteForm forma,UsuarioBasico usuario, ActionMapping mapping) 
	{
		boolean transaccion=true;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		
		//si tiene info de paciente se almacena
		if(transaccion&&!UtilidadTexto.isEmpty(forma.getPaciente().getTipoId())&&!UtilidadTexto.isEmpty(forma.getPaciente().getNumeroId()))
		{
			forma.getPaciente().setCodigo(SaldosInicialesCarteraPaciente.insertarActualizarPersona(con,forma.getPaciente()));
			transaccion=(forma.getPaciente().getCodigo()>0);
		}
		logger.info("---->GUARDO PERSONA<----"+transaccion);
		if(transaccion)
		{
			String tempo[]=forma.getDeptoCiudadIDDeudor().split(ConstantesBD.separadorSplit);
			if(!tempo[0].equals(ConstantesBD.codigoNuncaValido+"")&&!tempo[1].equals(ConstantesBD.codigoNuncaValido+""))
			{
				forma.getDatosFinanciacion().getDeudor().setDepartamentoID(tempo[0]);
				forma.getDatosFinanciacion().getDeudor().setCiudadID(tempo[1]);
			}
			forma.getDatosFinanciacion().getDeudor().setCodigoPK(DatosFinanciacion.insertarDeudorCoGenerico(forma.getDatosFinanciacion().getDeudor()));
			transaccion=forma.getDatosFinanciacion().getDeudor().getCodigoPK()>0;
		}
		logger.info("---->GUARDO DEUDOR<----"+transaccion);
		if(transaccion&&!UtilidadTexto.isEmpty(forma.getDatosFinanciacion().getCodeudor().getTipoIdentificacion()))//si ingresaron informacion de codeudor se intenta insertar.
		{
			String tempo[]=forma.getDeptoCiudadIDCodeudor().split(ConstantesBD.separadorSplit);
			if(!tempo[0].equals(ConstantesBD.codigoNuncaValido+"")&&!tempo[1].equals(ConstantesBD.codigoNuncaValido+""))
			{
				forma.getDatosFinanciacion().getCodeudor().setDepartamentoID(tempo[0]);
				forma.getDatosFinanciacion().getCodeudor().setCiudadID(tempo[1]);
			}
			forma.getDatosFinanciacion().getCodeudor().setCodigoPK(DatosFinanciacion.insertarDeudorCoGenerico(forma.getDatosFinanciacion().getCodeudor()));
			transaccion=forma.getDatosFinanciacion().getCodeudor().getCodigoPK()>0;
		}
		logger.info("---->GUARDO CODEUDOR<----"+transaccion);
		if(transaccion)
		{
			forma.getDatosFinanciacion().setSaldoInicialCartera(ConstantesBD.acronimoSi);
			forma.getDatosFinanciacion().setCodigoDocumentoGarantia(DocumentosGarantia.insertarDocumentoGarantia(con, llenarDocumentoGarantia(forma)));
			transaccion=forma.getDatosFinanciacion().getCodigoDocumentoGarantia()>0;
		}
		
		if(transaccion)
		{
			String nombreConsecutiv="";
			if(forma.getDatosFinanciacion().getTipoDocumento().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
  		   	{
				nombreConsecutiv=ConstantesBD.nombreConsecutivoLetraCambio;
  		   	}
			else
			{
				nombreConsecutiv=ConstantesBD.nombreConsecutivoPagare;
  		   	}
			String aux = UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutiv,usuario.getCodigoInstitucionInt());
			forma.getDatosFinanciacion().setConsecutivo(aux);
			String anioCo = UtilidadBD.obtenerAnioConsecutivo(nombreConsecutiv,usuario.getCodigoInstitucionInt(), forma.getDatosFinanciacion().getConsecutivo());
			if(anioCo.equals(""))
				forma.getDatosFinanciacion().setAnioConsecutivo(" ");
			else
				forma.getDatosFinanciacion().setAnioConsecutivo(anioCo);
			transaccion = DatosFinanciacion.insertDatosFinanciacion(con, forma.getDatosFinanciacion())>0?true:false;
			if(transaccion)
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, nombreConsecutiv,usuario.getCodigoInstitucionInt(),forma.getDatosFinanciacion().getConsecutivo(),ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			else
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, nombreConsecutiv,usuario.getCodigoInstitucionInt(),forma.getDatosFinanciacion().getConsecutivo(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			}
				
		}
		logger.info("---->GUARDO DATOS DE FINANCIACION<----"+transaccion);
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("modificacion");
		}
		else
			UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return null;
	}
	
	
	/**
	 * 
	 * @param forma 
	 * @return
	 */
	public HashMap llenarDocumentoGarantia(SaldosInicialesCarteraPacienteForm forma)
	{
		String fechaActual=UtilidadFecha.getFechaActual();
		String horaActual=UtilidadFecha.getHoraActual();
		HashMap parametros = new HashMap(); 
		logger.info("entra llenar documento garantia");
		if(forma.getPaciente().getCodigo()>0)
			parametros.put("codigopaciente", forma.getPaciente().getCodigo());
		parametros.put("institucion", forma.getInstitucion());
		parametros.put("consecutivo", forma.getDatosFinanciacion().getConsecutivo());
		parametros.put("anioconsecutivo",fechaActual.split("/")[2]);
		parametros.put("tipodocumento", forma.getDatosFinanciacion().getTipoDocumento());
		parametros.put("fechageneracion", UtilidadFecha.conversionFormatoFechaABD(fechaActual));
		parametros.put("horageneracion", horaActual);
		parametros.put("estado", ConstantesIntegridadDominio.acronimoPolizaVigente);
		parametros.put("usuariomodifica", forma.getDatosFinanciacion().getUsuarioModifica());
		parametros.put("fechamodifica", UtilidadFecha.conversionFormatoFechaABD(fechaActual));
		parametros.put("horamodifica", horaActual);
		parametros.put("garantiaIngreso", ConstantesBD.acronimoNo);
		parametros.put("cartera", ConstantesBD.acronimoSi);
		parametros.put("valor", forma.getDatosFinanciacion().getValorTotalDocumento());
		parametros.put("fechadocumento", UtilidadFecha.conversionFormatoFechaABD(forma.getDatosFinanciacion().getFechaInicio()));
		parametros.put("saldoInicialCartera", forma.getDatosFinanciacion().getSaldoInicialCartera());
		logger.info("sale llenar documento garantia");
		return parametros;  
	}

	/**
	 * 
	 * @param forma
	 * @param loginUsuario 
	 */
	private void inicializarCuotasFinaciacion(SaldosInicialesCarteraPacienteForm forma, String loginUsuario) 
	{
		if(forma.getDatosFinanciacion().getCuotasDatosFinan().size()<forma.getDatosFinanciacion().getNroCoutas())
		{
			for(int i=forma.getDatosFinanciacion().getCuotasDatosFinan().size();i<forma.getDatosFinanciacion().getNroCoutas();i++)
	    	{
	    		DtoCuotasDatosFinanciacion dto = new DtoCuotasDatosFinanciacion();
	    		dto.setUsuarioModifica(loginUsuario);
	    		dto.setActivo(ConstantesBD.acronimoSi);
	    		dto.setNroCuota(i+1);
	    		forma.getDatosFinanciacion().getCuotasDatosFinan().add(dto);
	    	}
		}
		else
		{
			for(int i=forma.getDatosFinanciacion().getCuotasDatosFinan().size();i>forma.getDatosFinanciacion().getNroCoutas();i--)
	    	{
	    		forma.getDatosFinanciacion().getCuotasDatosFinan().remove((i-1));
	    	}
		}
	}

	/**
	 * 
	 * @param forma
	 * @param codigoInstitucionInt
	 * @param string 
	 */
	private void cargarDatosCodeudor(SaldosInicialesCarteraPacienteForm forma, int codigoInstitucionInt, String loginUsuario) 
	{
		forma.getDatosFinanciacion().getCodeudor().setClaseDeudor(ConstantesIntegridadDominio.acronimoCoDeudor);
		forma.getDatosFinanciacion().getCodeudor().setUsuarioModifica(loginUsuario);
		forma.getDatosFinanciacion().getCodeudor().setInstitucion(codigoInstitucionInt);
		if(forma.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente))
		{
			forma.getDatosFinanciacion().getCodeudor().setCodigoPaciente(forma.getPaciente().getCodigo()+"");
			forma.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(forma.getPaciente().getTipoId());
			forma.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion(forma.getPaciente().getNumeroId());
			forma.getDatosFinanciacion().getCodeudor().setPrimerApellido(forma.getPaciente().getPrimerApellido());
			forma.getDatosFinanciacion().getCodeudor().setSegundoApellido(forma.getPaciente().getSegundoApellido());
			forma.getDatosFinanciacion().getCodeudor().setPrimerNombre(forma.getPaciente().getPrimerNombre());
			forma.getDatosFinanciacion().getCodeudor().setSegundoNombre(forma.getPaciente().getSegundoNombre());
			forma.getDatosFinanciacion().getCodeudor().setDireccion(forma.getPaciente().getDireccion());
			forma.getDatosFinanciacion().getCodeudor().setTelefono(forma.getPaciente().getTelefono());
			forma.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
			forma.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
		}
		if(forma.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			if(forma.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro) && !forma.getDatosFinanciacion().getCodeudor().getTipoIdentificacion().equals("") && !forma.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion().equals(""))
			{
				Usuario persona=new Usuario();
				if(persona.cargarPersona(forma.getDatosFinanciacion().getCodeudor().getNumeroIdentificacion(), forma.getDatosFinanciacion().getCodeudor().getTipoIdentificacion()))
				{
					forma.getDatosFinanciacion().getCodeudor().setCodigoPaciente(persona.getCodigoPersona()+"");
					forma.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(persona.getCodigoTipoIdentificacion());
					forma.getDatosFinanciacion().getCodeudor().setNumeroIdentificacion(persona.getNumeroIdentificacion());
					forma.getDatosFinanciacion().getCodeudor().setPrimerApellido(persona.getPrimerApellidoPersona());
					forma.getDatosFinanciacion().getCodeudor().setSegundoApellido(persona.getSegundoApellidoPersona());
					forma.getDatosFinanciacion().getCodeudor().setPrimerNombre(persona.getPrimerNombrePersona());
					forma.getDatosFinanciacion().getCodeudor().setSegundoNombre(persona.getSegundoNombrePersona());
					forma.getDatosFinanciacion().getCodeudor().setDireccion(persona.getDireccion());
					forma.getDatosFinanciacion().getCodeudor().setTelefono(persona.getTelefono());
					forma.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
					forma.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
				}
				else
				{
					forma.getDatosFinanciacion().getCodeudor().setCodigoPaciente("");
					forma.getDatosFinanciacion().getCodeudor().setPrimerApellido("");
					forma.getDatosFinanciacion().getCodeudor().setSegundoApellido("");
					forma.getDatosFinanciacion().getCodeudor().setPrimerNombre("");
					forma.getDatosFinanciacion().getCodeudor().setSegundoNombre("");
					forma.getDatosFinanciacion().getCodeudor().setDireccion("");
					forma.getDatosFinanciacion().getCodeudor().setTelefono("");
					forma.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
					forma.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
					forma.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(codigoInstitucionInt));
					
				}
			}
			else
			{
				forma.getDatosFinanciacion().getCodeudor().setCodigoPaciente("");
				forma.getDatosFinanciacion().getCodeudor().setPrimerApellido("");
				forma.getDatosFinanciacion().getCodeudor().setSegundoApellido("");
				forma.getDatosFinanciacion().getCodeudor().setPrimerNombre("");
				forma.getDatosFinanciacion().getCodeudor().setSegundoNombre("");
				forma.getDatosFinanciacion().getCodeudor().setDireccion("");
				forma.getDatosFinanciacion().getCodeudor().setTelefono("");
				forma.getDatosFinanciacion().getCodeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				forma.getDatosFinanciacion().getCodeudor().setExistePerRes(ConstantesBD.acronimoNo);
				forma.getDatosFinanciacion().getCodeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(codigoInstitucionInt));
				
			}
		}
	}

	
	private ActionForward accionFiltroCiudades(SaldosInicialesCarteraPacienteForm forma,HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		ArrayList<HashMap<String, Object>> arrayResultado=new ArrayList<HashMap<String,Object>>();
		
		if(forma.getSufijo().equals("Deudor"))
		{
			forma.setCiudadesExpDeudor(Utilidades.obtenerCiudadesXPais(forma.getCodigoPaisTempo()));
			arrayResultado=(ArrayList<HashMap<String, Object>>) forma.getCiudadesExpDeudor().clone();
		}
		else if(forma.getSufijo().equals("Codeudor"))
		{
			forma.setCiudadesExpCodeudor(Utilidades.obtenerCiudadesXPais(forma.getCodigoPaisTempo()));
			arrayResultado=(ArrayList<HashMap<String, Object>>) forma.getCiudadesExpCodeudor().clone();
		}
		logger.info("CODIGO PAIS=>*"+forma.getCodigoPaisTempo()+"*");
		
		//Revision de las ciudades segun pais seleccionado
		for(int i=0;i<arrayResultado.size();i++)
		{
			HashMap elemento = (HashMap)arrayResultado.get(i);
			if(elemento.get("codigoPais").toString().equals(forma.getCodigoPaisTempo()))
				resultado += "<ciudad>" +
					"<codigo-departamento>"+elemento.get("codigoDepartamento")+"</codigo-departamento>"+
					"<codigo-ciudad>"+elemento.get("codigoCiudad")+"</codigo-ciudad>"+
					"<nombre-departamento>"+elemento.get("nombreDepartamento")+"</nombre-departamento>"+
					"<nombre-ciudad>"+elemento.get("nombreCiudad")+"</nombre-ciudad>"+
				 "</ciudad>";
		}
		
		resultado += "</respuesta>";
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param codigoInstitucionInt
	 */
	private void cargarDatosDeudor(SaldosInicialesCarteraPacienteForm forma,int institucion, String loginUsuario) 
	{
		forma.getDatosFinanciacion().getDeudor().setClaseDeudor(ConstantesIntegridadDominio.acronimoDeudor);
		forma.getDatosFinanciacion().getDeudor().setUsuarioModifica(loginUsuario);
		forma.getDatosFinanciacion().getDeudor().setInstitucion(institucion);
		if(forma.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente))
		{
			forma.getDatosFinanciacion().getDeudor().setCodigoPaciente(forma.getPaciente().getCodigo()+"");
			forma.getDatosFinanciacion().getDeudor().setTipoIdentificacion(forma.getPaciente().getTipoId());
			forma.getDatosFinanciacion().getDeudor().setNumeroIdentificacion(forma.getPaciente().getNumeroId());
			forma.getDatosFinanciacion().getDeudor().setPrimerApellido(forma.getPaciente().getPrimerApellido());
			forma.getDatosFinanciacion().getDeudor().setSegundoApellido(forma.getPaciente().getSegundoApellido());
			forma.getDatosFinanciacion().getDeudor().setPrimerNombre(forma.getPaciente().getPrimerNombre());
			forma.getDatosFinanciacion().getDeudor().setSegundoNombre(forma.getPaciente().getSegundoNombre());
			forma.getDatosFinanciacion().getDeudor().setDireccion(forma.getPaciente().getDireccion());
			forma.getDatosFinanciacion().getDeudor().setTelefono(forma.getPaciente().getTelefono());
			forma.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
			forma.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
		}
		if(forma.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			if(forma.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro) && !forma.getDatosFinanciacion().getDeudor().getTipoIdentificacion().equals("") && !forma.getDatosFinanciacion().getDeudor().getNumeroIdentificacion().equals(""))
			{
				Usuario persona=new Usuario();
				if(persona.cargarPersona(forma.getDatosFinanciacion().getDeudor().getNumeroIdentificacion(), forma.getDatosFinanciacion().getDeudor().getTipoIdentificacion()))
				{
					forma.getDatosFinanciacion().getDeudor().setCodigoPaciente(persona.getCodigoPersona()+"");
					forma.getDatosFinanciacion().getDeudor().setTipoIdentificacion(persona.getCodigoTipoIdentificacion());
					forma.getDatosFinanciacion().getDeudor().setNumeroIdentificacion(persona.getNumeroIdentificacion());
					forma.getDatosFinanciacion().getDeudor().setPrimerApellido(persona.getPrimerApellidoPersona());
					forma.getDatosFinanciacion().getDeudor().setSegundoApellido(persona.getSegundoApellidoPersona());
					forma.getDatosFinanciacion().getDeudor().setPrimerNombre(persona.getPrimerNombrePersona());
					forma.getDatosFinanciacion().getDeudor().setSegundoNombre(persona.getSegundoNombrePersona());
					forma.getDatosFinanciacion().getDeudor().setDireccion(persona.getDireccion());
					forma.getDatosFinanciacion().getDeudor().setTelefono(persona.getTelefono());
					forma.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
					forma.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
				}
				else
				{
					forma.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
					forma.getDatosFinanciacion().getDeudor().setPrimerApellido("");
					forma.getDatosFinanciacion().getDeudor().setSegundoApellido("");
					forma.getDatosFinanciacion().getDeudor().setPrimerNombre("");
					forma.getDatosFinanciacion().getDeudor().setSegundoNombre("");
					forma.getDatosFinanciacion().getDeudor().setDireccion("");
					forma.getDatosFinanciacion().getDeudor().setTelefono("");
					forma.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
					forma.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
					forma.getDatosFinanciacion().getDeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(institucion));
					
				}
			}
			else
			{
				forma.getDatosFinanciacion().getDeudor().setCodigoPaciente("");
				forma.getDatosFinanciacion().getDeudor().setPrimerApellido("");
				forma.getDatosFinanciacion().getDeudor().setSegundoApellido("");
				forma.getDatosFinanciacion().getDeudor().setPrimerNombre("");
				forma.getDatosFinanciacion().getDeudor().setSegundoNombre("");
				forma.getDatosFinanciacion().getDeudor().setDireccion("");
				forma.getDatosFinanciacion().getDeudor().setTelefono("");
				forma.getDatosFinanciacion().getDeudor().setExiteDeudor(ConstantesBD.acronimoNo);
				forma.getDatosFinanciacion().getDeudor().setExistePerRes(ConstantesBD.acronimoNo);
				forma.getDatosFinanciacion().getDeudor().setTipoIdentificacion(ValoresPorDefecto.getTipoId(institucion));
				
			}
		}
	}

	/**
	 * 
	 * @param forma
	 * @param loginUsuario 
	 */
	private void empezarNuevoDoc(SaldosInicialesCarteraPacienteForm forma,int institucion, String loginUsuario) 
	{
		
		forma.reset();
		forma.setPaises(Utilidades.obtenerPaises());
		forma.setTipoIdentificacionMap(Utilidades.obtenerTiposIdentificacion("",institucion));

		forma.setInstitucion(institucion);
		
			
		if(!forma.getDatosFinanciacion().getDeudor().getTipoDeudor().equals(""))
			forma.setSeccionDeudor(ConstantesBD.acronimoSi);
		if(!forma.getDatosFinanciacion().getCodeudor().getTipoDeudor().equals(""))
			forma.setSeccionCodeudor(ConstantesBD.acronimoSi);
		if(forma.getDatosFinanciacion().getMaxNroCuotasFinan()<=0)
			forma.getDatosFinanciacion().setMaxNroCuotasFinan(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(institucion)));
		if(forma.getDatosFinanciacion().getMaxNroDiasFinanCuo()<=0)
			forma.getDatosFinanciacion().setMaxNroDiasFinanCuo(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(institucion)));
		if(forma.getDatosFinanciacion().getNroCoutas()==ConstantesBD.codigoNuncaValido)
			forma.getDatosFinanciacion().setNroCoutas(1);
		if(forma.getDatosFinanciacion().getDiasPorCuota()==ConstantesBD.codigoNuncaValido)
			forma.getDatosFinanciacion().setDiasPorCuota(ConstantesBD.numeroDiasCuotas);
		forma.getDatosFinanciacion().setProcesoExito(ConstantesBD.acronimoNo);
		forma.getDatosFinanciacion().setProcesoExitosoCuo(ConstantesBD.acronimoNo);
		forma.getDatosFinanciacion().setIsNuevoDoc(ConstantesBD.acronimoSi);
		forma.getDatosFinanciacion().setUsuarioModifica(loginUsuario);
		
		inicializarCuotasFinaciacion(forma, loginUsuario);
		
	} 
}
