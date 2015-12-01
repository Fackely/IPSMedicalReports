package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.AntecedentesGinecoObstetricosForm;
import com.princetonsa.actionform.historiaClinica.ImpresionCLAPForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.historiaClinica.ImpresionCLAP;
import com.princetonsa.mundo.historiaClinica.ImpresionResumenAtenciones;
import com.princetonsa.mundo.historiaClinica.InformacionParto;

public class ImpresionCLAPAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ImpresionCLAPAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {

		Connection con = null;
		try {
			if(form instanceof ImpresionCLAPForm)
			{
				ImpresionCLAPForm forma = (ImpresionCLAPForm) form;
				ImpresionCLAP mundo= new ImpresionCLAP();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= Utilidades.getPersonaBasicaSesion(request.getSession());
				String estado = forma.getEstado();
				if(!UtilidadValidacion.esProfesionalSalud(usuario))
				{
					request.setAttribute("codigoDescripcionError", "errors.noProfesionalSalud");
					return mapping.findForward("paginaError");
				}

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}


				logger.warn("estado -->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de InformacionRecienNacidosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto. getMaxPageItems(usuario.getCodigoInstitucionInt())));
					ActionForward forward= validacionAccesoPaciente(paciente, request, mapping);
					if(forward!=null)
					{
						UtilidadBD.closeConnection(con);
						return forward;
					}
					this.consultarSolicitudesPacinte(con,forma,mundo,paciente);
					if(Integer.parseInt(forma.getSolicitudes("numRegistros")+"")>0)
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaListado");
					}
					else
					{
						UtilidadBD.cerrarConexion(con);
						logger.warn("Paciente SIN SOLICITUDES");			
						request.setAttribute("codigoDescripcionError", "error.informacionRecienNacidos.pacienteSinSolicitudes");
						return mapping.findForward("paginaError");
					}
				}
				else if(estado.equals("imprimir"))
				{

					//se toma el id de la cuenta
					forma.setIdCuenta(forma.getSolicitudes("cuenta_"+forma.getItemSeleccionado()).toString());
					logger.info("VALOR DE LA CUENTA DESDE EL ACTION=> "+forma.getIdCuenta());
					////////////////////////SECCION TRABAJO PARA BOGOTA.//////////////////////////////////////
					this.generarRegistroLogImpresion(con,forma,mundo,paciente,usuario);

					this.consultarSeccionRecienNacido(con,forma,mundo,paciente);


					this.consultarSeccionInformacionParto(con,forma,usuario);

					this.consultarSeccionPartogramaYVigilancia(con, forma, usuario);

					this.consultarControlPrenatal(con, forma, usuario);
					////////////////////////FIN SECCION TRABAJO PARA BOGOTA./////////////////////////////////


					////////////////////////SECCION TRABAJO PARA MANIZALES.//////////////////////////////////////


					//------Se obtiene el numero de la solicitud seleccionado -----------//
					String codigoCirugia=UtilidadCadena.vString(forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+"");


					//------Se obtiene el numero del embarazo en Informaci?n Parto ---------//
					int numeroEmbarazo=Utilidades.obtenerNroEmbarazoCirugiaPaciente(con, paciente.getCodigoPersona(), codigoCirugia);

					//---Si es diferente de -1 se consulta la informaci?n en la hoja obst?trica ----//
					if(numeroEmbarazo != -1)
					{
						//---------Consultar Gestaci?n Actual ----------------------------//
						this.consultarGestacionActual(con, forma, mundo, paciente, numeroEmbarazo);

						//---------Consulta y Controles ----------------------------//
						this.consultarControlesGestacional(con, forma, mundo, paciente, numeroEmbarazo);
					}

					//-- Consulta de Antecedentes  
					cargarAntecedentesFamiliares(con, forma, paciente);
					cargarAntecedentesMedicos(con, forma, paciente);
					cargarAntecedentesToxicos(con, forma, paciente);
					cargarAntecedentesGinecoObstetricos(con, request, paciente, usuario, forma);				

					////////////////////////FIN SECCION TRABAJO PARA MANIZALES./////////////////////////////////


					////////////////////////FIN SECCION TRABAJO PARA MANIZALES./////////////////////////////////

					logger.info("\n\n\nVALOR DE LA CUENTA DESDE EL ACTION=> "+forma.getIdCuenta()+"\n\n\n");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("imprimir");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de InformacionRecienNacidosForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
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
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void consultarControlPrenatal(Connection con, ImpresionCLAPForm forma, UsuarioBasico usuario)
	{
		forma.setControlPrenatal(InformacionParto.obtenerControlPrenatal(con, forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+"", usuario.getCodigoInstitucionInt()));
		ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
		institucion.cargar(con, usuario.getCodigoInstitucionInt());
		forma.setPartoEn(institucion.getCodMinSalud());
	}

	/**
	 * M?todo implementado para realizar la consulta de la informacion del parto
	 * @param con
	 * @param forma
	 * @param usuario 
	 */
	private void consultarSeccionInformacionParto(Connection con, ImpresionCLAPForm forma, UsuarioBasico usuario) 
	{
		
		//Se instancia objeto de la informacion del parto
		InformacionParto infoParto = new InformacionParto();
		
		infoParto.setCodigoCirugia(forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+"");
		infoParto.cargar(con, usuario.getCodigoInstitucion());
		forma.setMapaSeccionInformacionParto(infoParto.getInfoParto());
		
		//Se verifica si hubo resultados exitosos en la consulta
		if(UtilidadTexto.getBoolean(forma.getMapaSeccionInformacionParto("existeBd").toString()))
		{
			
			//Se consultan datos adicionales del medico que ingreso el parto******************************************
			UsuarioBasico medico = new UsuarioBasico();
			try 
			{
				medico.cargarUsuarioBasico(con, forma.getMapaSeccionInformacionParto("usuario").toString());
			} catch (SQLException e) 
			{
				logger.error("Error al consultar los datos del medico de la informacion del parto: "+e);
			}
			forma.setMapaSeccionInformacionParto("nombreMedico", medico.getNombreUsuario());
			forma.setMapaSeccionInformacionParto("ocupacionMedico", medico.getOcupacionMedica());
			
			//Se verifica si hay partograma********************************************************************
			if(infoParto.existePartogramaDadoConsecutivoInfoParto(con, forma.getMapaSeccionInformacionParto("consecutivo").toString()))
				forma.setMapaSeccionInformacionParto("existePartograma",ConstantesBD.acronimoSi);
			else
				forma.setMapaSeccionInformacionParto("existePartograma",ConstantesBD.acronimoNo);
			
			//Se verifica si hay informacion en seccion de medicacion**********************************************
			int contador = 0;
			String valor = "";
			HashMap mapaMed = (HashMap)forma.getMapaSeccionInformacionParto("mapaMedicacion");
			for(int i=0;i<Integer.parseInt(mapaMed.get("numRegistros").toString());i++)
			{
				valor = mapaMed.get("valor_"+i).toString();
				if(valor.equals(ConstantesBD.acronimoNo)||valor.equals(ConstantesBD.acronimoSi))
					contador ++;
			}
			forma.setMapaSeccionInformacionParto("numMedicacion", contador++);
			
			//Se verifica si hay informacion en seccion de transfusion**********************************************
			contador = 0;
			valor = "";
			HashMap mapaTran = (HashMap)forma.getMapaSeccionInformacionParto("mapaTransfusion");
			for(int i=0;i<Integer.parseInt(mapaTran.get("numRegistros").toString());i++)
			{
				valor = mapaTran.get("valor_"+i).toString();
				if(valor.equals(ConstantesBD.acronimoNo)||valor.equals(ConstantesBD.acronimoSi))
					contador ++;
			}
			forma.setMapaSeccionInformacionParto("numTransfusion", contador++);
			
			//Se verifica si hay informacion en seccion de enfermedades**********************************************
			contador = 0;
			valor = "";
			HashMap mapaEnf = (HashMap)forma.getMapaSeccionInformacionParto("mapaEnfermedades");
			for(int i=0;i<Integer.parseInt(mapaEnf.get("numRegistros").toString());i++)
			{
				valor = mapaEnf.get("valor_"+i).toString();
				if(valor.equals(ConstantesBD.acronimoNo)||valor.equals(ConstantesBD.acronimoSi))
					contador ++;
			}
			forma.setMapaSeccionInformacionParto("numEnfermedades", contador++);
				
			//Se verifica si hay informacion de EGRESO MATERNA********************************************
			if(!forma.getMapaSeccionInformacionParto("fechaEgreso").toString().equals("")||
				!forma.getMapaSeccionInformacionParto("horaEgreso").toString().equals("")||
				!forma.getMapaSeccionInformacionParto("condicionEgreso").toString().equals("")||
				!forma.getMapaSeccionInformacionParto("antirubeola").toString().equals("")||
				!forma.getMapaSeccionInformacionParto("anticoncepcion").toString().equals(""))
			{
				forma.setMapaSeccionInformacionParto("existeEgreso", ConstantesBD.acronimoSi);
				//calculo de las horas egreso postparto
				forma.setMapaSeccionInformacionParto(
						"horasEgresoParto",
						infoParto.calcularHorasEgresoParto(
								con,
								forma.getMapaSeccionInformacionParto("fechaParto").toString(),
								forma.getMapaSeccionInformacionParto("horaParto").toString(),
								forma.getMapaSeccionInformacionParto("codigoCirugia").toString()
						)
					);
			}
			else
				forma.setMapaSeccionInformacionParto("existeEgreso", ConstantesBD.acronimoNo);
			
		}
		else
		{
			forma.setMapaSeccionInformacionParto("numRegistros", "0");
			forma.setMapaSeccionInformacionParto("existeEgreso", ConstantesBD.acronimoNo);
		}
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente 
	 * @param paciente
	 */
	private void consultarSeccionRecienNacido(Connection con, ImpresionCLAPForm forma, ImpresionCLAP mundo, PersonaBasica paciente)
	{
		forma.setMapaSeccionRecienNacido(mundo.consultarInformacionRecienNacido(con,forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+"",paciente.getCodigoPersona()+""));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 */
	private void generarRegistroLogImpresion(Connection con, ImpresionCLAPForm forma, ImpresionCLAP mundo, PersonaBasica paciente, UsuarioBasico usuario)
	{
		HashMap vo=new HashMap();
		vo.put("paciente", paciente.getCodigoPersona());
		vo.put("cuenta", forma.getSolicitudes("cuenta_"+forma.getItemSeleccionado()));
		vo.put("fecha",UtilidadFecha.getFechaActual());
		vo.put("hora", UtilidadFecha.getHoraActual());
		vo.put("usuario", usuario.getLoginUsuario());
		vo.put("codigocentroatencion", usuario.getCodigoCentroAtencion());
		vo.put("centroatencion", usuario.getCentroAtencion());
		mundo.generarRegistroLogImpresion(con,vo);
	}

	/**
	 * 
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward validacionAccesoPaciente(PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		else
		{
			if(paciente.getCodigoSexo()!=ConstantesBD.codigoSexoFemenino)
			{
				logger.warn("Paciente no Femenino");			
				request.setAttribute("codigoDescripcionError", "errors.paciente.noEsFemenino");
				return mapping.findForward("paginaError");
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 */
	private void consultarSolicitudesPacinte(Connection con, ImpresionCLAPForm forma, ImpresionCLAP mundo, PersonaBasica paciente)
	{
		HashMap vo=new HashMap();
		vo.put("codigoPaciente", paciente.getCodigoPersona()+"");
		forma.setSolicitudes(mundo.consultarSolicitudes(con,vo));
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void consultarSeccionPartogramaYVigilancia(Connection con, ImpresionCLAPForm forma, UsuarioBasico usuario)
	{
		//se carga la informacion de la seccion de vigilancia clinica del trabajo de parto y partograma
		String consecutivoInfoParto=InformacionParto.obtenerConsecutivoInfoPartoDadoCx(con, forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+"");
		
		if(!consecutivoInfoParto.trim().equals("") )
		{
			//Se instacnia objeto de InformacionParto
			InformacionParto parto = new InformacionParto();
			forma.setVigilanciaClinicaMap(parto.cargarVigilanciaClinica(con, consecutivoInfoParto));
			
			if(parto.existePartogramaDadoConsecutivoInfoParto(con, consecutivoInfoParto))
			{	
				forma.setPartogramaMap(parto.cargarPartograma(con, consecutivoInfoParto));
				forma.setPartogramaMap("existePartograma", ConstantesBD.acronimoSi);
				forma.inicializarTagsPartograma();
			}	
		}	
	}
	
	//----------------------------------------- MANIZALES ------------------------------------------------------//
	
	/**
	 * Metodo que consulta la informaci?n de la secci?n Consulta y Controles
	 * que se obtiene del Resumen Gestacional
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param numeroEmbarazo
	 */
	private void consultarControlesGestacional(Connection con, ImpresionCLAPForm forma, ImpresionCLAP mundo, PersonaBasica paciente, int numeroEmbarazo)
	{
		HashMap parametros=new HashMap();
		parametros.put("codigoPaciente", paciente.getCodigoPersona()+"");
		parametros.put("numeroEmbarazo", numeroEmbarazo+"");
		
		//-----Se consulta los encabezados de los hist?ricos de resumen gestacional para el embarazo actual  ---//
		parametros.put("nroConsulta", "2");
		forma.setMapaEncabezadoConsultaControles(mundo.consultarInformacionObstetrica(con,parametros));
		
		//-----Se consulta el detalle de la informacion historica de resumen gestacional para el embarazo actual  ---//
		int numRegEnca=0;
		
		numRegEnca=UtilidadCadena.vInt(forma.getMapaEncabezadoConsultaControles("numRegistros")+"");
		
		if(numRegEnca > 0)
			{
				parametros.put("nroConsulta", "3");
				//-Se obtienen los codigos historicos del resumen gestacional separados por comas -------//
				String codsEnca=UtilidadCadena.obtenerCadenaKey(forma.getMapaEncabezadoConsultaControles(), "codigo_enca", ",");
				
				parametros.put("codigosEnca", codsEnca);
				
				//-------Se consulta el detalle del resumen gestacional con los codigos de encabezados enviados por parametro ----//
				forma.setMapaDetalleConsultaControles(mundo.consultarInformacionObstetrica(con,parametros));
			}
	}
	
	/**
	 * Metodo que consulta la informaci?n a mostrar en la seccion Gestacion actual
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param numeroEmbarazo
	 */
	private void consultarGestacionActual(Connection con, ImpresionCLAPForm forma, ImpresionCLAP mundo, PersonaBasica paciente, int numeroEmbarazo)
	{
		HashMap parametros=new HashMap();
		parametros.put("codigoPaciente", paciente.getCodigoPersona()+"");
		parametros.put("numeroEmbarazo", numeroEmbarazo+"");
		
		//--------Se consulta la informaci?n de la hoja obst?trica del embarazo actual  ---//
		parametros.put("nroConsulta", "1");
		forma.setMapaGestacionActual(mundo.consultarInformacionObstetrica(con,parametros));
		
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------MANIZALES-----------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	private void cargarAntecedentesFamiliares(Connection con, ImpresionCLAPForm forma, PersonaBasica paciente)
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap mp = new HashMap();
		forma.getMapaAntFamiliares().clear();
		mp.put("nroConsulta","3");
		mp.put("paciente", paciente.getCodigoPersona() + "");
			
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapaAntFamiliares("observaciones", mp.get("observaciones_0")+"");

		if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
		{
			forma.setMapaAntFamiliares("hayAntecedentes","SI");
		}
		else
		{
			forma.setMapaAntFamiliares("hayAntecedentes","NO");
		}
		mp.put("nroConsulta","4");
		mp.put("paciente",paciente.getCodigoPersona()+"");
		
		forma.getMapaAntFamiliares().putAll(mundo.consultarInformacion(con, mp));
	}
	
	/**
	 * Metodo Para Cargar Los Antecedentes Medicos Y Quirurgicos. 
	 * @param con
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesMedicos(Connection con, ImpresionCLAPForm forma, PersonaBasica paciente) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap mp = new HashMap();
		forma.getMapaAntMedicos().clear();
		
		mp.put("nroConsulta","13");
		mp.put("paciente", paciente.getCodigoPersona() + "");
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapaAntMedicos("observaciones", mp.get("observaciones_0")+"");
		if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
		{
			forma.setMapaAntMedicos("hayAntecedentes","SI");
		}
		else
		{
			forma.setMapaAntMedicos("hayAntecedentes","NO");
		}
		mp.put("nroConsulta","14");
		mp.put("paciente",paciente.getCodigoPersona()+"");
		forma.getMapaAntMedicos().putAll(mundo.consultarInformacion(con, mp));
		mp.put("nroConsulta","15");  //-- consultar la seccion quirurgicos
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapaAntMedicos("numRegQuirur",mp.get("numRegistros")+"");
		mp.remove("numRegistros");
		forma.getMapaAntMedicos().putAll(mp);   
	}
	
	/**
	 * Metodo para cargar los antecedentes Toxicos.
	 * @param con
	 * @param forma
	 * @param paciente
	 * @throws SQLException
	 */
	private void cargarAntecedentesToxicos(Connection con, ImpresionCLAPForm forma, PersonaBasica paciente) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		forma.getMapaAntToxicos().clear();
		
		mp.put("nroConsulta","16");
		mp.put("paciente", paciente.getCodigoPersona() + "");
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapaAntToxicos("observaciones", mp.get("observaciones_0")+"");
		
		if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
		{
			forma.setMapaAntToxicos("hayAntecedentes","SI");
		}
		else
		{
			forma.setMapaAntToxicos("hayAntecedentes","NO");
		}
		
		mp.put("nroConsulta","17");
		mp.put("paciente",paciente.getCodigoPersona()+"");
		forma.getMapaAntToxicos().putAll(mundo.consultarInformacion(con, mp));
	}	
	
	
	
	/**
	 * Metodo para cargar los antecedentes Pediatricos. 
	 * @param con
	 * @param request
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesGinecoObstetricos(Connection con, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario, ImpresionCLAPForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		forma.getMapaAntGineco().clear();
		mp.put("nroConsulta","10");
		mp.put("paciente", paciente.getCodigoPersona() + "");
		mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapaAntGineco("observaciones", mp.get("observaciones_0")+"");
		if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
		{
			forma.setMapaAntGineco("hayAntecedentes","SI");
		}
		else
		{
			forma.setMapaAntGineco("hayAntecedentes","NO");
		}

		AntecedentesGinecoObstetricosForm antecedentesBean = new AntecedentesGinecoObstetricosForm();

		// Se carga toda la informacion de la bd en el momento para mostrar un resumen completo
		AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
		antecedentes.setPaciente(paciente);
			
		//-- Hacer Otra Funcion de Cargar. Con los Parametros de Busqueda en el MAPA
		HashMap mapa = new HashMap();
		mapa.put("paciente", paciente.getCodigoPersona()+"");
		antecedentes.cargar(con, mapa);
			
		cargarBeanCompleto(antecedentes,antecedentesBean,forma);
		
		request.setAttribute("numeroEmbarazos", ""+forma.getMapaAntGineco("NumeroEmbarazos"));
		request.setAttribute("numeroEmbarazos", ""+forma.getMapaAntGineco("NumMetodosAnticonceptivos"));

		/*int nroRows = UtilidadCadena.vInt(forma.getMapaAntGineco("NumeroEmbarazos")+"");
		for(int i=1; i<=nroRows; i++)
		{
			String nH = (String)antecedentesBean.getValue("numeroHijos_" +i);
			int numH = Integer.parseInt(nH);
			
			request.setAttribute("numeroHijos_"+i, nH);
			forma.setMapaAntGineco("numeroHijos_"+i, nH);
			
			for(int j=1; j<=numH; j++)
			{
				String numeroTiposPartoVaginalTemp = forma.getMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j)+"";
				
				if (numeroTiposPartoVaginalTemp!=null)
				{
					request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j));
					forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, forma.getMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j)+"");
				}
				else
				{
					request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, "0");
					forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, "0");
				}
				
			}
		}*/
	}
	
	/**
	 * Carga el bean de antecedentes ginecoobstetricos con los historicos
	 * @param forma 
 	 */
	public void cargarBeanCompleto(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, ImpresionCLAPForm forma)
	{
		// carga los basicos del bean
		cargarBean(antecedentes, bean, forma);
		
		// los historicos
		ArrayList historicos = antecedentes.getAntecedentesHistoricos();
		bean.setHistoricos(historicos);
		forma.setHistoricos(historicos);
	}
	
	
	/**
	 * Carga el form de antecedentes ginecoobst?tricos con la informaci?n
	 * pertinente contenida en el objeto.
	 * @param forma 
	 * @param 	AntecedentesGinecoObstetricos, antecedentes
	 * @param 	AntecedentesGinecoObstetricosForm, bean
	 */
	public void cargarBean(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, ImpresionCLAPForm forma)
	{	
		//---No historicos y no modificables despues de grabados
		if( !antecedentes.getRangoEdadMenarquia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getCodigo());
			forma.setMapaAntGineco("RangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getCodigo()+"");

			if( antecedentes.getRangoEdadMenarquia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenarquia("No se ha grabado informaci?n");
				forma.setMapaAntGineco("RangoEdadMenarquia","-1");
				forma.setMapaAntGineco("NombreRangoEdadMenarquia","No se ha grabado informacion");
			}	
			else
			{
				forma.setMapaAntGineco("ExisteRangoEdadMenarquia","true");
				forma.setMapaAntGineco("NombreRangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getValue()+"");
				bean.setNombreRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getValue());
				bean.setExisteRangoEdadMenarquia(true);
			}
		}

		forma.setMapaAntGineco("OtroEdadMenarquia",antecedentes.getOtroEdadMenarquia());
		bean.setOtraEdadMenarquia(antecedentes.getOtroEdadMenarquia());
		
		
		if( !antecedentes.getRangoEdadMenopausia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getCodigo());
			forma.setMapaAntGineco("RangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getCodigo()+"");
			
			if( antecedentes.getRangoEdadMenopausia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenopausia("No se ha grabado informaci?n");
				forma.setMapaAntGineco("NombreRangoEdadMenopausia","No se ha grabado informaci?n");
			}	
			else
			{
				forma.setMapaAntGineco("NombreRangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getValue()+"");
				forma.setMapaAntGineco("ExisteRangoEdadMenopausia","true");
				
				bean.setNombreRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getValue());
				bean.setExisteRangoEdadMenopausia(true);
			}
		}
		bean.setOtraEdadMenopausia(antecedentes.getOtroEdadMenopausia());
		forma.setMapaAntGineco("OtraEdadMenopausia",antecedentes.getOtroEdadMenopausia()+"");
		
		if(antecedentes.getInicioVidaSexual() == 0 )
		{
			bean.setInicioVidaSexual("");
			forma.setMapaAntGineco("InicioVidaSexual","");
		}	
		else
		{
			bean.setInicioVidaSexual(""+antecedentes.getInicioVidaSexual());
			forma.setMapaAntGineco("InicioVidaSexual",""+antecedentes.getInicioVidaSexual());
		}	

		if(antecedentes.getInicioVidaObstetrica() == 0 ) 
		{
			bean.setInicioVidaObstetrica("");
			forma.setMapaAntGineco("InicioVidaObstetrica","");
		}	
		else
		{
			bean.setInicioVidaObstetrica(""+antecedentes.getInicioVidaObstetrica());
			forma.setMapaAntGineco("InicioVidaObstetrica",""+antecedentes.getInicioVidaObstetrica());
		}	
		
		String observacionesStr =  antecedentes.getObservaciones();
		
		if( observacionesStr == null )
			observacionesStr = "";
		
		forma.setMapaAntGineco("observacionesStr",observacionesStr);
				
		
		//if( bean.estado.equals("resumen") )
		{
			bean.setObservacionesViejas(observacionesStr.replaceAll("\n", "<br>"));
			forma.setMapaAntGineco("ObservacionesViejas",observacionesStr.replaceAll("\n", "<br>"));
		}	
		//else
			//bean.setObservacionesViejas(observacionesStr.replaceAll("<br>", "\n"));
		//		Fin no historicos y no modificables despues de grabados
		
		// 		Metodos anticonceptivos
		ArrayList metodosAnticonceptivos = antecedentes.getMetodosAnticonceptivos();
		bean.setNumMetodosAnticonceptivos(metodosAnticonceptivos.size());
		forma.setMapaAntGineco("NumMetodosAnticonceptivos",metodosAnticonceptivos.size()+"");
		

		for( int i=0; i < metodosAnticonceptivos.size(); i++ )
		{
			InfoDatos metodo = (InfoDatos)metodosAnticonceptivos.get(i);
			
			bean.setValue("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			forma.setMapaAntGineco("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			
			bean.setValue("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getValue());
			forma.setMapaAntGineco("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getDescripcion());
			forma.setMapaAntGineco("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
			forma.setMapaAntGineco("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			
			if( !metodo.getDescripcion().trim().equals("") )
			{
				bean.setValue("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
				forma.setMapaAntGineco("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			}	
		}
		//		Fin metodos anticonceptivos
		
		//		Embarazos
		ArrayList embarazos = antecedentes.getEmbarazos();
		
		bean.setNumeroEmbarazos(embarazos.size());
		forma.setMapaAntGineco("NumeroEmbarazos", embarazos.size()+"");
		forma.setMapaAntGineco("NumGestaciones", embarazos.size()+"");
		bean.setNumGestaciones(bean.getNumeroEmbarazos());
		
		/*for(int i=1; i<=embarazos.size(); i++)
		{
			Embarazo embarazo = (Embarazo)embarazos.get(i-1);
			
			bean.setValue("codigo_"+i, embarazo.getCodigo()+"");
			forma.setMapaAntGineco("codigo_"+i, embarazo.getCodigo()+"");
			
			//Guardamos el n?mero de hijos por embarazo
			bean.setValue("numeroHijos_" + i, embarazo.getHijos().size() + "");
			forma.setMapaAntGineco("numeroHijos_" + i, embarazo.getHijos().size() + "");
						
			bean.setValue("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			forma.setMapaAntGineco("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			
			bean.setValue("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			forma.setMapaAntGineco("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			
			bean.setValue("duracion_"+i,embarazo.getDuracion());
			forma.setMapaAntGineco("duracion_"+i,embarazo.getDuracion());

			bean.setValue("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			forma.setMapaAntGineco("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			
			bean.setValue("legrado_"+i,embarazo.getLegrado());
			forma.setMapaAntGineco("legrado_"+i,embarazo.getLegrado());
			
			int compTempo[]=embarazo.getComplicacion();
			for(int y=0;y<compTempo.length;y++)
			{
				if(compTempo[y]!=0)
				{
					bean.setValue("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
					forma.setMapaAntGineco("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
				}
			}
			Vector nombresComplicaciones=embarazo.getNombresComplicaciones();
			for(int y=0;y<nombresComplicaciones.size();y++)
			{
				bean.setValue("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y));
				forma.setMapaAntGineco("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y)+"");
			}
			
			Vector otrasComplicaciones=embarazo.getOtraComplicacion();
			for(int j=0; j<otrasComplicaciones.size();j++)
			{
				bean.setValue("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
				forma.setMapaAntGineco("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
			}
			bean.setValue("numOtraComplicacion_"+i, new Integer(otrasComplicaciones.size()));
			forma.setMapaAntGineco("numOtraComplicacion_"+i, otrasComplicaciones.size()+"");
						
			bean.setValue("tipoTrabajoParto_"+i, Integer.toString(embarazo.getTrabajoParto().getCodigo()));
			forma.setMapaAntGineco("tipoTrabajoParto_"+i, embarazo.getTrabajoParto().getCodigo()+"");
			
			
			if( embarazo.getTrabajoParto().getCodigo() == -1 )
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, "");
				forma.setMapaAntGineco("nombreTipoTrabajoParto_"+i, "");
			}
			else
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
				forma.setMapaAntGineco("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
			}
				
			bean.setValue("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());
			forma.setMapaAntGineco("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());

			//	Hijos embarazo.
			ArrayList hijos = embarazo.getHijos();
			ArrayList formasPartoVaginal;
			bean.setValue("numeroHijos_"+i, ""+hijos.size());
			forma.setMapaAntGineco("numeroHijos_"+i, ""+hijos.size());

			for(int j=1; j<=hijos.size(); j++)
			{
				HijoBasico hijo = new HijoBasico(); 
				hijo = (HijoBasico)hijos.get(j-1);
				
				boolean partoVaginal = true;
				
				if( hijo.isVivo() )
				{
					bean.setNumVivos(bean.getNumVivos()+1);
					bean.setNumVivosGrabados(bean.getNumVivos());
					bean.setValue("vitalidad_"+i+"_"+j, "vivo");
					forma.setMapaAntGineco("vitalidad_"+i+"_"+j, "vivo");
				}
				else
				{
					bean.setNumMuertos(bean.getNumMuertos()+1);
					bean.setNumMuertosGrabados(bean.getNumMuertos());
					bean.setValue("vitalidad_"+i+"_"+j, "muerto");
					forma.setMapaAntGineco("vitalidad_"+i+"_"+j, "muerto");
				}
								
				if( hijo.isAborto() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");
					
					bean.setValue("tiposParto_"+i+"_"+j, new String("4"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("4"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));

					partoVaginal = false;
					bean.setNumAbortos(bean.getNumAbortos()+1);
					forma.setMapaAntGineco("NumAbortos", ""+bean.getNumAbortos()+1);

					bean.setNumAbortosGrabados(bean.getNumAbortos());
					forma.setMapaAntGineco("NumAbortosGrabados", ""+bean.getNumAbortos());
					
				} 
				else
				if( hijo.isCesarea() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");

					bean.setValue("tiposParto_"+i+"_"+j, new String("5"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("5"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					
					partoVaginal = false;
					bean.setNumCesareas(bean.getNumCesareas()+1);
					forma.setMapaAntGineco("NumCesareas", ""+(bean.getNumCesareas()+1));

					bean.setNumCesareasGrabadas(bean.getNumCesareas());
					forma.setMapaAntGineco("NumCesareasGrabadas", ""+bean.getNumCesareas());
				} 
				else
				if( hijo.getOtroTipoParto() != null && !hijo.getOtroTipoParto().equals("") )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");
					
					bean.setValue("tiposParto_"+i+"_"+j, new String("0"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("0"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					
					bean.setValue("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					forma.setMapaAntGineco("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					
					partoVaginal = false;
				}
				else
				if( ( formasPartoVaginal = hijo.getFormasNacimientoVaginal() ).size() > 0 )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");
					
					bean.setNumPartos(bean.getNumPartos()+1);
					forma.setMapaAntGineco("NumPartos", ""+(bean.getNumPartos()+1));
					
					bean.setNumPartosGrabados(bean.getNumPartos());
					forma.setMapaAntGineco("NumPartosGrabados", ""+bean.getNumPartos());
					
					boolean esvalido = true;					
					if(formasPartoVaginal.size()==1)
					{
						InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(0);
						// En este caso no debemos mostrarlo de forma tradicional
						if( tipoPVInfo.getCodigo() == -2 )
						{
							bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
							forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

							bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
							forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

							bean.setValue("numTiposPartoVaginal_"+i+"_"+j, "0");
							forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, "0");

							esvalido = false;
						}
					}
					if( esvalido )
					{

						bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
						forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

						bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
						forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

						bean.setValue("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
						forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
								
						for(int k=0; k<formasPartoVaginal.size(); k++)
						{
							InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(k);
							
							bean.setValue("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
							forma.setMapaAntGineco("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
								
							bean.setValue("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
							forma.setMapaAntGineco("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
														
						}

					}
					
				}
				if( partoVaginal && hijo.getOtraFormaNacimientoVaginal() != null && !hijo.getOtraFormaNacimientoVaginal().equals("") )
				{
					bean.setValue("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());
					forma.setMapaAntGineco("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());

					bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
				}
				String tempoSexo=hijo.getSexo()+"";
				if(tempoSexo!=null && !tempoSexo.equals("null") && (tempoSexo.equals(ConstantesBD.codigoSexoMasculino+"") || tempoSexo.equals(ConstantesBD.codigoSexoFemenino+"")))
				{
					bean.setValue("sexo_"+i+"_"+j,hijo.getSexo()+"");			
					forma.setMapaAntGineco("sexo_"+i+"_"+j,hijo.getSexo()+"");
				}
				else
				{
					bean.setValue("sexo_"+i+"_"+j,"-1");
					forma.setMapaAntGineco("sexo_"+i+"_"+j,"-1");
				}
				bean.setValue("peso_"+i+"_"+j, hijo.getPeso());
				forma.setMapaAntGineco("peso_"+i+"_"+j, hijo.getPeso());

				bean.setValue("lugar_"+i+"_"+j, hijo.getLugar());
				forma.setMapaAntGineco("lugar_"+i+"_"+j, hijo.getLugar());
			}
			
			//	Fin hijos embarazo			
		}
		//		Fin embarazos
		 */
	}
	

	
}
