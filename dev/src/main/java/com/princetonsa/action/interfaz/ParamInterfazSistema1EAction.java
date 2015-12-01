package com.princetonsa.action.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.interfaz.ParamInterfazSistema1EForm;
import com.princetonsa.dto.interfaz.DtoConceptosParam1E;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConceptosRetencion;
import com.princetonsa.mundo.interfaz.ParamInterfazSistema1E;
import com.princetonsa.sort.Interfaz.SortTiposInterfazDocumentosParam1E;



public class ParamInterfazSistema1EAction extends Action {
	Logger logger = Logger.getLogger(InterfazSistemaUnoAction.class);

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		Connection con = null;
		try{
			if (response == null)
				;

			if (form instanceof ParamInterfazSistema1EForm) {

				con = UtilidadBD.abrirConexion();

				if (con == null) {
					request.setAttribute("CodigoDescripcionError",
					"erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico) request.getSession()
				.getAttribute("usuarioBasico");

				ParamInterfazSistema1EForm forma = (ParamInterfazSistema1EForm) form;
				
				ParamInterfazSistema1E paramInt1E = new ParamInterfazSistema1E(); 


				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				logger.info("*----------------------------------------*");
				logger.info("Valor del Estado  -------> " + forma.getEstado());
				logger.info("*----------------------------------------*");

				if (estado == null) {
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo  (null)");
					request.setAttribute("CodigoDescripcionError",
					"errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				// Anexo 823 Cambios Funcionalidades
				if (estado.equals("conceptoRetencion")){
					return detalleConceptosParam1E(mapping, con, usuario, forma,
							paramInt1E); 
				}else if (estado.equals("addiconaConcRet")){
					return adicionarConceptoParam1E(mapping, con, forma);
				}else if (estado.equals("guardarConceptoParam")){
					return guardarConceptosParam1E(mapping, request, con, usuario,forma, paramInt1E);
				}else if (estado.equals("eliminarConcRet")){
					return eliminarConceptoParam1E(mapping, request, con, usuario,
							forma, paramInt1E, errores);
				}else if (estado.equals("tipoDocFuente")){
					return mapping.findForward("tiposDocFuente");
				}else if (estado.equals("consultarConcRet")){
					return consultarConceptoParam1E(mapping, con, forma, paramInt1E);
				}else if (estado.equals("consultarTipDocFuente")){
					return mapping.findForward("tiposDocFuente");
				}else if(estado.equals("ordenar")){
					return accionOrdenar(mapping, con, forma);
				}	
				// Fin Anexo 823 Cambios Funcionalidades


				//Cambios Anexo 833 Agosto 14
				if (estado.equals("observacion"))
					return mapping.findForward("tiposDocFuente");
				else if (estado.equals("eventosCarteraFuente"))
				{
					forma.resetEveCarGlosa();
					forma.setEventos(ParamInterfazSistema1E.consultarEventos(con));
					forma.setListaEventos(ParamInterfazSistema1E.consultarEventosParam1E(con,""));
					return mapping.findForward("eventosCarteraFuente");
				}
				else if (estado.equals("eventosCarteraFuenteConsulta"))
				{
					forma.resetEventos();
					forma.setEventos(ParamInterfazSistema1E.consultarEventos(con));
					forma.setListaEventos(ParamInterfazSistema1E.consultarEventosParam1E(con,""));
					return mapping.findForward("eventosCarteraFuente");
				}
				else if (estado.equals("nuevoEvento"))
				{
					forma.resetEventos();
					return mapping.findForward("eventosCarteraFuente");
				}
				else if (estado.equals("guardarEvento"))
				{
					forma.getDtoEventosParam1e().setParamGenerales1E(forma.getDtoInterfazParam().getConsecutivoPk());
					forma.getDtoEventosParam1e().setFecha(UtilidadFecha.getFechaActual());
					forma.getDtoEventosParam1e().setHora(UtilidadFecha.getHoraActual());
					forma.getDtoEventosParam1e().setUsuario(usuario.getLoginUsuario());
					if (!ParamInterfazSistema1E.validarEventoUnico(con, forma.getDtoEventosParam1e()))
					{
						if (ParamInterfazSistema1E.insertarEventosParam1E(con, forma.getDtoEventosParam1e()))
						{
							forma.setListaEventos(ParamInterfazSistema1E.consultarEventosParam1E(con,""));
							forma.resetEventos();
						}
					}
					else
					{
						errores.add("",	new ActionMessage("errors.notEspecific","Ya hay registrada información para el evento seleccionado."));
						saveErrors(request, errores);
					}
					return mapping.findForward("eventosCarteraFuente");
				}
				else if (estado.equals("inactivarEvento"))
				{
					String consecutivo=forma.getListaEventos().get(forma.getIndiceEvento()).getConsecutivoPk();
					forma.getDtoEventosParam1e().setConsecutivoPk(consecutivo);
					forma.getDtoEventosParam1e().setFechaInactivacion(UtilidadFecha.getFechaActual());
					forma.getDtoEventosParam1e().setHoraInactivacion(UtilidadFecha.getHoraActual());
					forma.getDtoEventosParam1e().setUsuarioInactivacion(usuario.getLoginUsuario());
					if (ParamInterfazSistema1E.inactivarEvento(con,forma.getDtoEventosParam1e()))
					{
						forma.setListaEventos(ParamInterfazSistema1E.consultarEventosParam1E(con,""));
						forma.resetEventos();
					}
					return mapping.findForward("eventosCarteraFuente");
				}

				//Ir al Estado para el Menu
				if (estado.equals("volverMenu"))
					return mapping.findForward("menu");

				if (estado.equals("ingresarModificar")) {
					forma.reset();
					
					forma.setListaCentrosCosto(ParamInterfazSistema1E.listaCentroCostoTipoArea(ConstantesBD.codigoTipoAreaIndirecto, true));
					
					forma.setCentrosAtencion(Utilidades
							.obtenerCentrosAtencion(usuario
									.getCodigoInstitucionInt()));
					forma.setUnidadesFuncionales(Utilidades
							.consultarUnidadesFuncionales(con, usuario
									.getCodigoInstitucionInt()));
					forma.setDtoInterfazParam(ParamInterfazSistema1E
							.consultarParamGenerales(con));
					forma.setCuentaContable(Utilidades
							.obtenerCuentaContableXCodigo(usuario
									.getCodigoInstitucionInt(), forma
									.getDtoInterfazParam().getCuentaAbonoPac()));
					forma.setArrayTiposDoc(ParamInterfazSistema1E
							.consultarTiposDocs(con));
					forma.setTiposDoc(ParamInterfazSistema1E
							.consultarTiposDoc1E(con));

					// Lleno el Dto Auxiliar para guardar el log
					forma.getDtoTiposInterfazAux().setParamGenerales(
							forma.getDtoInterfazParam().getConsecutivoPk());
					forma.getDtoTiposInterfazAux().setPathArchivoInconsis(
							forma.getDtoInterfazParam().getPathArchivoInconsis());
					forma.getDtoTiposInterfazAux().setPathArchivoInterfaz(
							forma.getDtoInterfazParam().getPathArchivoInterfaz());
					forma.getDtoTiposInterfazAux().setTerceroGenFacPar(
							forma.getDtoInterfazParam().getTerceroGenFacPar());
					forma.getDtoTiposInterfazAux().setTerceroGenPagPac(
							forma.getDtoInterfazParam().getTerceroGenPagPac());
					forma.getDtoTiposInterfazAux().setCuentaAbonoPac(
							forma.getDtoInterfazParam().getCuentaAbonoPac());
					forma.getDtoTiposInterfazAux().setCodConFlEfeMovCxp(
							forma.getDtoInterfazParam().getCodConFlEfeMovCxp());
					forma.getDtoTiposInterfazAux().setCodConFlEfeMovCon(
							forma.getDtoInterfazParam().getCodConFlEfeMovCon());
					forma.getDtoTiposInterfazAux().setRealizarCalRetenCxh(
							forma.getDtoInterfazParam().getRealizarCalRetenCxh());
					forma.getDtoTiposInterfazAux().setRealizarCalRestenCxes(
							forma.getDtoInterfazParam().getRealizarCalRestenCxes());
					forma.getDtoTiposInterfazAux().setRealizarCalAutoretFp(
							forma.getDtoInterfazParam().getRealizarCalAutoretFp());
					forma.getDtoTiposInterfazAux().setRealizarCalAutoretFv(
							forma.getDtoInterfazParam().getRealizarCalAutoretFv());
					forma.getDtoTiposInterfazAux().setFechaControlDesmarcar(
							forma.getDtoInterfazParam().getFechaControlDesmarcar());
					forma.getDtoTiposInterfazAux().setDocumentoCruceHi(
							forma.getDtoInterfazParam().getDocumentoCruceHi());
					forma.getDtoTiposInterfazAux()
					.setCentroAtencionContable(
							forma.getDtoInterfazParam()
							.getCentroAtencionContable());
					forma.getDtoTiposInterfazAux().setInstitucion(
							forma.getDtoInterfazParam().getInstitucion());
					forma.getDtoTiposInterfazAux().setFechaModifica(
							forma.getDtoInterfazParam().getFechaModifica());
					forma.getDtoTiposInterfazAux().setHoraModifica(
							forma.getDtoInterfazParam().getHoraModifica());
					forma.getDtoTiposInterfazAux().setUsuarioModifica(
							forma.getDtoInterfazParam().getUsuarioModifica());

					// Cambios Funcionalidad Anexo 823
					forma.getDtoTiposInterfazAux().setRealizarCalRetenCxda(
							forma.getDtoInterfazParam().getRealizarCalRetenCxda());
					logger.info("1 >>>>"+forma.getDtoTiposInterfazAux().getRealizarCalRetenCxda());
					forma.getDtoTiposInterfazAux().setRealizarCalAutoretCxCC(
							forma.getDtoInterfazParam().getRealizarCalAutoretCxCC());
					logger.info("2 >>>>"+forma.getDtoTiposInterfazAux().getRealizarCalAutoretCxCC());
					// Fin Cambios Funcionalidad Anexo 823

					//Cambios Anexo 833 Agosto 14
					//Estos cambios se quitaron por solicitud de Documentoación el 17 de noviembre 

					//				forma.getDtoTiposInterfazAux().setCentroAtencionContableDRC(
					//						forma.getDtoInterfazParam().getCentroAtencionContableDRC());
					//				
					//				forma.getDtoTiposInterfazAux().setCentroAtencionContableRG(
					//						forma.getDtoInterfazParam().getCentroAtencionContableRG());

					//Fin Cambios Anexo 833 Agosto 14

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("ingresarModificar");
				}

				if (estado.equals("nuevo")) {
					UtilidadBD.cerrarConexion(con);
					forma.getDtoTiposInterfaz().reset();
					forma.setMovimientoPaciente(false);
					return mapping.findForward("tiposDocFuente");//ingresarModificar
				}

				if (estado.equals("guardar")) {
					boolean banderaGuardar = true;
					//Si el consecutivo esta vacio al haber cargado los datos, es que no hay anda parametrizado y se debe guadar
					if (UtilidadTexto.isEmpty(forma.getDtoInterfazParam()
							.getConsecutivoPk())) {
						if (!UtilidadFileUpload
								.existeArchivo(forma.getDtoInterfazParam()
										.getPathArchivoInterfaz(), "")) {
							banderaGuardar = false;
							errores
							.add(
									"",
									new ActionMessage(
											"errors.notEspecific",
									"El Path de Archivo Interfaz no es válido. Por favor Verifique."));
							saveErrors(request, errores);
						}
						if (!UtilidadFileUpload
								.existeArchivo(forma.getDtoInterfazParam()
										.getPathArchivoInconsis(), "")) {
							banderaGuardar = false;
							errores
							.add(
									"",
									new ActionMessage(
											"errors.notEspecific",
									"El Path de Archivo de Inconsistencias no es válido. Por favor verifique. "));
							saveErrors(request, errores);
						}
						if (banderaGuardar) {
							forma.getDtoInterfazParam().setInstitucion(
									usuario.getCodigoInstitucionInt() + "");
							forma.getDtoInterfazParam().setFechaModifica(
									UtilidadFecha.getFechaActual());
							forma.getDtoInterfazParam().setHoraModifica(
									UtilidadFecha.getHoraActual());
							forma.getDtoInterfazParam().setUsuarioModifica(
									usuario.getLoginUsuario());
							ParamInterfazSistema1E.ingresarParamGenerales(con,
									forma.getDtoInterfazParam());
							forma.setDtoInterfazParam(ParamInterfazSistema1E
									.consultarParamGenerales(con));
						}
					} 
					//Si el consecutivo posee un valor, es que ya existe y se debe actualizar
					else {
						if (!UtilidadTexto.isEmpty(forma.getDtoInterfazParam()
								.getPathArchivoInterfaz())) {
							if (!UtilidadFileUpload.existeArchivo(
									forma.getDtoInterfazParam()
									.getPathArchivoInterfaz(), "")) {
								banderaGuardar = false;
								errores
								.add(
										"",
										new ActionMessage(
												"errors.notEspecific",
										"El Path de Archivo Interfaz no es válido, Por favor Verifique."));
								saveErrors(request, errores);
								forma.setCuentaContable(Utilidades
										.obtenerCuentaContableXCodigo(usuario
												.getCodigoInstitucionInt(), forma
												.getDtoInterfazParam().getCuentaAbonoPac()));
							}
						}

						if (!UtilidadTexto.isEmpty(forma.getDtoInterfazParam()
								.getPathArchivoInconsis())) {
							if (!UtilidadFileUpload.existeArchivo(
									forma.getDtoInterfazParam()
									.getPathArchivoInconsis(), "")) {
								banderaGuardar = false;
								errores
								.add(
										"",
										new ActionMessage(
												"errors.notEspecific",
										"El Path de Archivo de Inconsistencias no es válido, Por favor verifique. "));
								saveErrors(request, errores);
								forma.setCuentaContable(Utilidades
										.obtenerCuentaContableXCodigo(usuario
												.getCodigoInstitucionInt(), forma
												.getDtoInterfazParam().getCuentaAbonoPac()));
							}
						}

						if (banderaGuardar) {
							forma.getDtoInterfazParam().setInstitucion(
									usuario.getCodigoInstitucionInt() + "");
							forma.getDtoInterfazParam().setFechaModifica(
									UtilidadFecha.getFechaActual());
							forma.getDtoInterfazParam().setHoraModifica(
									UtilidadFecha.getHoraActual());
							forma.getDtoInterfazParam().setUsuarioModifica(
									usuario.getLoginUsuario());

							if (ParamInterfazSistema1E.actualizarParamGenerales(
									con, forma.getDtoInterfazParam()))
							{
								ParamInterfazSistema1E.insertarLog(con, forma
										.getDtoTiposInterfazAux());
								forma.setDtoInterfazParam(ParamInterfazSistema1E
										.consultarParamGenerales(con));
								forma.setCuentaContable(Utilidades
										.obtenerCuentaContableXCodigo(usuario
												.getCodigoInstitucionInt(), forma
												.getDtoInterfazParam().getCuentaAbonoPac()));
							}


						}
					}
					forma.setTiposDoc(ParamInterfazSistema1E
							.consultarTiposDoc1E(con));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("ingresarModificar");//tiposDocFuente
				}

				if (estado.equals("guardarDoc")) {

					if (UtilidadTexto.isEmpty(forma.getDtoTiposInterfaz().getTipoDocumento()))
					{
						errores.add(forma.getDtoTiposInterfaz().getTipoDocumento(), new ActionMessage("errors.required","El Tipo de documento "));
						saveErrors(request, errores);
					}
					if(UtilidadTexto.isEmpty(forma.getDtoTiposInterfaz().getTipoConsecutivo()))
					{
						errores.add("",new ActionMessage("errors.required","El Consecutivo a reportar "));
						saveErrors(request, errores);
					}

					if(UtilidadTexto.isEmpty(forma.getDtoTiposInterfaz().getTipoDocumento()))
					{
						errores.add("",new ActionMessage("errors.required","El Indicativo Tipo Doc. "));
						saveErrors(request, errores);
					}

					String llaveArray;
					String llaveDto;

					llaveDto=forma.getDtoTiposInterfaz().getTipoDocumento()+forma.getDtoTiposInterfaz().getTipoMovimiento();

					for(int i=0;i<forma.getDtoInterfazParam().getArrayListDtoTiposDocumentos().size();i++)
					{
						llaveArray=forma.getDtoInterfazParam().getArrayListDtoTiposDocumentos().get(i).getTipoDocumento()+forma.getDtoInterfazParam().getArrayListDtoTiposDocumentos().get(i).getTipoMovimiento();

						if (llaveArray.equals(llaveDto))
						{
							errores.add("Tipo de Documento", new ActionMessage("errors.notEspecific","El Tipo de Documento " +
									forma.getDtoInterfazParam().getArrayListDtoTiposDocumentos().get(i).getNombreDocumento()+" no se Puede Ingresar Nuevamente. "));
							saveErrors(request, errores);

						}
					}

					if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getDtoTiposInterfaz().getIndTipoDocumento()))
						errores.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Ind.Tipo Documento "));

					if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getDtoTiposInterfaz().getTipoDocumentoCruce()))
						errores.add("",new ActionMessage("errors.caracteresInvalidos","El Campo Tipo Documento Cruce"));

					if (Utilidades.convertirAEntero(forma.getDtoTiposInterfaz().getTipoDocumento())==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente
							||Utilidades.convertirAEntero(forma.getDtoTiposInterfaz().getTipoDocumento())==ConstantesBD.codigoTipoDocInteFacturaPaciente)
					{
						if (UtilidadTexto.isEmpty(forma.getDtoTiposInterfaz().getTipoMovimiento()))
						{
							errores.add("", new ActionMessage("errors.required","El Tipo de Movimiento "));
							saveErrors(request, errores);
						}
					}


					if(errores.isEmpty())
					{
						if (UtilidadTexto.isEmpty(forma.getDtoInterfazParam().getConsecutivoPk())) {
							errores.add("",new ActionMessage("errors.notEspecific","Es necesario guardar primero la prametrización para adjuntar tipos de documentos."));
							saveErrors(request, errores);
						} else {
							forma.getDtoTiposInterfaz().setParamGenerales(
									forma.getDtoInterfazParam().getConsecutivoPk());
							forma.getDtoTiposInterfaz().setInstitucion(
									usuario.getCodigoInstitucionInt() + "");
							forma.getDtoTiposInterfaz().setFecha(
									UtilidadFecha.getFechaActual());
							forma.getDtoTiposInterfaz().setHora(
									UtilidadFecha.getHoraActual());
							forma.getDtoTiposInterfaz().setUsuario(
									usuario.getLoginUsuario());
							forma.getDtoTiposInterfaz().setActivo(
									ConstantesBD.acronimoSi);
							ParamInterfazSistema1E.ingresarTiposDoc(con, forma
									.getDtoTiposInterfaz());
							forma.getDtoInterfazParam().setArrayListDtoTiposDocumentos(
									ParamInterfazSistema1E.consultarTiposDocs(con));
							forma.getDtoTiposInterfaz().reset();
						}
					}

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("tiposDocFuente");//ingresarModificar
				}

				if (estado.equals("inactivar")) {
					forma.setDtoTiposInterfaz(ParamInterfazSistema1E
							.consultarInfoTipoDocUnitario(con, forma.getIndice()));
					forma.getDtoTiposInterfaz().setFechaInactivacion(
							UtilidadFecha.getFechaActual());
					forma.getDtoTiposInterfaz().setHoraInactivacion(
							UtilidadFecha.getHoraActual());
					forma.getDtoTiposInterfaz().setUsuarioInactivacion(
							usuario.getLoginUsuario());
					ParamInterfazSistema1E.actualizarDocParam(con, forma
							.getIndice(), forma.getDtoTiposInterfaz());
					forma.getDtoInterfazParam().setArrayListDtoTiposDocumentos(
							ParamInterfazSistema1E.consultarTiposDocs(con));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("tiposDocFuente");//ingresarModificar
				}

				if (estado.equals("guardarActualizar")) {

					if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getDtoTiposInterfaz().getIndTipoDocumento()))
						errores.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Ind.Tipo Documento "));

					if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getDtoTiposInterfaz().getTipoDocumentoCruce()))
						errores.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Tipo Documento Cruce"));

					if(errores.isEmpty())
					{
						forma.getDtoTiposInterfaz().setFecha(
								UtilidadFecha.getFechaActual());
						forma.getDtoTiposInterfaz().setHora(
								UtilidadFecha.getHoraActual());
						forma.getDtoTiposInterfaz().setUsuario(
								usuario.getLoginUsuario());
						forma.getDtoTiposInterfaz().setFechaInactivacion(
								UtilidadFecha.getFechaActual());
						forma.getDtoTiposInterfaz().setHoraInactivacion(
								UtilidadFecha.getHoraActual());
						forma.getDtoTiposInterfaz().setUsuarioInactivacion(
								usuario.getLoginUsuario());
						if (ParamInterfazSistema1E.actualizarDocParam(con, forma
								.getIndice(), forma.getDtoTiposInterfaz()))
						{
							ParamInterfazSistema1E.ingresarTiposDoc(con, forma
									.getDtoTiposInterfaz());
							forma.getDtoInterfazParam().setArrayListDtoTiposDocumentos(
									ParamInterfazSistema1E.consultarTiposDocs(con));
							forma.getDtoTiposInterfaz().reset();
						}
					}else
						saveErrors(request, errores);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("tiposDocFuente");//ingresarModificar
				}

				if (estado.equals("consultar")) {
					forma.reset();
					forma.setCentrosAtencion(Utilidades
							.obtenerCentrosAtencion(usuario
									.getCodigoInstitucionInt()));
					forma.setDtoInterfazParam(ParamInterfazSistema1E
							.consultarParamGenerales(con));
					forma.setArrayTiposDoc(ParamInterfazSistema1E
							.consultarTiposDocs(con));
					forma.setCuentaContable(Utilidades
							.obtenerCuentaContableXCodigo(usuario
									.getCodigoInstitucionInt(), forma
									.getDtoInterfazParam().getCuentaAbonoPac()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("consultar");
				}

				if (estado.equals("actualizar")) {

					forma.setDtoTiposInterfaz(ParamInterfazSistema1E.consultarInfoTipoDocUnitario(con, forma.getIndice()));
					forma.setTiposConsecutivo(ParamInterfazSistema1E.getTiposConseTipDoc(con, forma.getDtoTiposInterfaz().getTipoDocumento()));

					if (Utilidades.convertirAEntero(forma.getDtoTiposInterfaz().getTipoDocumento())==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente
							||Utilidades.convertirAEntero(forma.getDtoTiposInterfaz().getTipoDocumento())==ConstantesBD.codigoTipoDocInteFacturaPaciente)
						forma.setMovimientoPaciente(true);
					else
					{
						forma.setMovimientoPaciente(false);
						//Se actualiza el tipo mov
						forma.getDtoTiposInterfaz().setTipoMovimiento("");
					}


					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("tiposDocFuente");//ingresarModificar
				}

				if (estado.equals("buscarCodigoXTipoDoc")) {

					forma.setTiposConsecutivo(ParamInterfazSistema1E.getTiposConseTipDoc(con, forma.getDtoTiposInterfaz().getTipoDocumento()));
					if (Utilidades.convertirAEntero(forma.getDtoTiposInterfaz().getTipoDocumento())==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente
							||Utilidades.convertirAEntero(forma.getDtoTiposInterfaz().getTipoDocumento())==ConstantesBD.codigoTipoDocInteFacturaPaciente)
						forma.setMovimientoPaciente(true);
					else
					{
						forma.setMovimientoPaciente(false);
						//Se actualiza el tipo mov
						forma.getDtoTiposInterfaz().setTipoMovimiento("");
					}

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("tiposDocFuente");//ingresarModificar
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

	private ActionForward consultarConceptoParam1E(ActionMapping mapping,
			Connection con, ParamInterfazSistema1EForm forma,
			ParamInterfazSistema1E paramInt1E) throws SQLException {
		forma.getDtoInterfazParam().getArrayConceptoParam1E().clear();
		forma.getDtoInterfazParam().setArrayConceptoParam1E(ParamInterfazSistema1E.cargarConceptosparam1E(con, 
				Utilidades.convertirAEntero(forma.getDtoInterfazParam().getConsecutivoPk())));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("conceptoRetencion");
	}

	/**
	 * Guardar Conceptos Param 1E
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param paramInt1E
	 * @return
	 * @throws SQLException
	 */
	private ActionForward guardarConceptosParam1E(ActionMapping mapping,
			HttpServletRequest request, Connection con, UsuarioBasico usuario,
			ParamInterfazSistema1EForm forma,
			ParamInterfazSistema1E paramInt1E) throws SQLException {
		ActionErrors errores = new ActionErrors();
		if(forma.getDtoInterfazParam().getArrayConceptoParam1E().size()>0)
		{
			for(int i=0;i<forma.getDtoInterfazParam().getArrayConceptoParam1E().size();i++)
			{
				if(forma.getDtoInterfazParam().getArrayConceptoParam1E().get(i).getClaseDocumento().equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("clase documento", new ActionMessage("errors.required","El Campo Clase Documento de la Posición["+i+"] "));
				for(int j=0;j<forma.getDtoInterfazParam().getArrayConceptoParam1E().size();j++){
					if(j!=i && forma.getDtoInterfazParam().getArrayConceptoParam1E().get(i).getIngresar().equals(ConstantesBD.acronimoSi)
							&& forma.getDtoInterfazParam().getArrayConceptoParam1E().get(j).getEliminar().equals(ConstantesBD.acronimoNo)
							&& forma.getDtoInterfazParam().getArrayConceptoParam1E().get(j).getSeccion().equals(forma.getSeccion()))
					{
						if(forma.getDtoInterfazParam().getArrayConceptoParam1E().get(i).getCodigoConceptoRetencion()!=ConstantesBD.codigoNuncaValido)
						{
							if(forma.getDtoInterfazParam().getArrayConceptoParam1E().get(i).getCodigoConceptoRetencion()
									== forma.getDtoInterfazParam().getArrayConceptoParam1E().get(j).getCodigoConceptoRetencion()
									&&
								forma.getDtoInterfazParam().getArrayConceptoParam1E().get(i).getClaseDocumento().equals(forma.getDtoInterfazParam().getArrayConceptoParam1E().get(j).getClaseDocumento())
									)
							{
								errores.add("concepto retencion", new ActionMessage("errors.notEspecific","Los campos Clase de Documento y Concepto Reteción de la Posición ["+i+"] están Registrados. "));
							}
						}
					}
				}
			}
		}else{
			errores.add("Concepto Retencion", new ActionMessage("errors.notEspecific","No se existen datos que guardar. "));
		}
		if(errores.isEmpty()){
			if(ParamInterfazSistema1E.guardarConceptosParam1E(con, usuario,
					forma.getDtoInterfazParam().getArrayConceptoParam1E(),
					Utilidades.convertirAEntero(forma.getDtoInterfazParam().getConsecutivoPk()),
					forma.getSeccion())!=ConstantesBD.codigoNuncaValido){
				return consultarConceptoParam1E(mapping, con, forma, paramInt1E);
			}else{
				errores.add("concepto param 1E", new ActionMessage("errors.sinIngresar"));
				saveErrors(request, errores);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("conceptoRetencion");
			}
		}else{
			saveErrors(request, errores);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("conceptoRetencion");
		}
	}

	/**
	 * Adicionar Conceptos Param 1E
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward adicionarConceptoParam1E(ActionMapping mapping,
			Connection con, ParamInterfazSistema1EForm forma)
			throws SQLException {
		DtoConceptosParam1E dto = new DtoConceptosParam1E();
		dto.setIngresar(ConstantesBD.acronimoSi);
		dto.setSeccion(forma.getSeccion());
		forma.getDtoInterfazParam().getArrayConceptoParam1E().add(dto);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("conceptoRetencion");
	}

	/**
	 * Detalle Conceptos Param 1E
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param paramInt1E
	 * @return
	 * @throws SQLException
	 */
	private ActionForward detalleConceptosParam1E(ActionMapping mapping,
			Connection con, UsuarioBasico usuario,
			ParamInterfazSistema1EForm forma, ParamInterfazSistema1E paramInt1E)
			throws SQLException {
		forma.getDtoInterfazParam().getArrayConceptoParam1E().clear();
		forma.getArrayConcpRen().clear();
		forma.getDtoInterfazParam().setArrayConceptoParam1E(ParamInterfazSistema1E.cargarConceptosparam1E(con, 
				Utilidades.convertirAEntero(forma.getDtoInterfazParam().getConsecutivoPk())));
		forma.setArrayConcpRen(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("conceptoRetencion");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param paramInt1E
	 * @param errores
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward eliminarConceptoParam1E(ActionMapping mapping,
			HttpServletRequest request, Connection con, UsuarioBasico usuario,
			ParamInterfazSistema1EForm forma,
			ParamInterfazSistema1E paramInt1E, ActionErrors errores) throws SQLException {
		ActionErrors errors = new ActionErrors();
		DtoConceptosParam1E dto = (DtoConceptosParam1E) forma.getDtoInterfazParam().getArrayConceptoParam1E().get(forma.getPosicion());
		dto.setUsuarioAnulacion(usuario.getLoginUsuario());
		if(errors.isEmpty()){
			if(ParamInterfazSistema1E.inactivarConceptoParam1E(con, dto)!=ConstantesBD.codigoNuncaValido){
				forma.getDtoInterfazParam().getArrayConceptoParam1E().get(forma.getPosicion()).setEliminar(ConstantesBD.acronimoSi);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("conceptoRetencion");
			}else{
				errores.add("concepto param 1E", new ActionMessage("errors.sinEliminar"));
				saveErrors(request, errors);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("conceptoRetencion");
			}
		}else{
			saveErrors(request, errors);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("conceptoRetencion");
		}
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping, Connection con,ParamInterfazSistema1EForm forma) {
		SortTiposInterfazDocumentosParam1E sort= new SortTiposInterfazDocumentosParam1E();
		sort.setPatronOrdenar(forma.getPatronOrdenar());
		Collections.sort(forma.getDtoInterfazParam().getArrayListDtoTiposDocumentos(), sort);
		forma.setEstado(forma.getEstadoAnterior());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tiposDocFuente");
	}
	
}
