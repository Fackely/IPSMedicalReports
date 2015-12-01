package util.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.historiaClinica.DtoJustificacionNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamOpcionesCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamSeccionesJusNoPos;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;

public class UtilidadesJustificacionNoPos
{
	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(UtilidadesJustificacionNoPos.class);
	
	/**
	 * Método para cargar la parametrización del formato de Justificación No Pos
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public static DtoParamJusNoPos cargarParametrizacion(Connection con, int institucion, String tipoJustificacion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().cargarParametrizacion(con, institucion, tipoJustificacion);
	}
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public static boolean existeJustificacion(Connection con, int institucion, String tipoJustificacion, String codigo, String solicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().existeJustificacion(con, institucion, tipoJustificacion, codigo, solicitud);
	}
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/solicitud general
	 * @param con
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public static boolean existeJustificacion(Connection con, int institucion, String codigo, String solicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().existeJustificacion(con, institucion, codigo, solicitud);
	}
	
	/**
	 * Metodo que consulta el codigo de una justificacion no pos si existe o no para un articulo/orden-solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public static int consultarCodigoJustificacion(Connection con, int institucion, String tipoJustificacion, String codigoArt,String codigoOrden, String numeroSolicitud){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().consultarCodigoJustificacion(con, institucion, tipoJustificacion, codigoArt, codigoOrden, numeroSolicitud);
	}
	
	/**
	 * Método para guardar la justificacion No Pos en la BD
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @param usuario
	 * @param esModificacion
	 * @return
	 */
	public static boolean guardarJustificacion(Connection con, DtoParamJusNoPos dtoJusParam, String tipoJustificacion, UsuarioBasico usuario)
	{
		DtoJustificacionNoPos dtoJus = new DtoJustificacionNoPos();
		dtoJus = convertirDtoJusParam(dtoJusParam, usuario);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().guardarJustificacion(con, dtoJus, tipoJustificacion);
	}
	
	/**
	 * Metodo para inserta la asociacion de solicitud y justificacion No Pos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoAsocioJustificacion
	 * @param cantidadSolicitada 

	 */
	
	public static void insertarAsocioSolicitudJustificacion(Connection con,int numeroSolicitud, int codigoAsocioJustificacion, int cantidadSolicitada) {
		 DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().insertarAsocioSolicitudJustificacion(con, numeroSolicitud, codigoAsocioJustificacion,cantidadSolicitada);
		
	}

	
	/**
	 * Método para inserta la asociacion de la orden ambulatoria y justificacion No Pos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoAsocioJustificacion
	 * @param cantidadSolicitada 

	 */
	
	public static void insertarAsocioOrdenJustificacion(Connection con,int numeroOrden, int codigoAsocioJustificacion, int cantidadSolicitada) {
		 DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().insertarAsocioOrdenJustificacion(con, numeroOrden, codigoAsocioJustificacion, cantidadSolicitada);
		
	}
	

	/**
	 * Metodo para convertir el dto de la parametrizacion al dto de la justificacion
	 * @param dtoJusParam
	 * @param usuario
	 * @return
	 */
	private static DtoJustificacionNoPos convertirDtoJusParam(DtoParamJusNoPos dtoJusParam, UsuarioBasico usuario) {
		DtoJustificacionNoPos dtoJus = new DtoJustificacionNoPos();
		
		dtoJus.setArticulo(dtoJusParam.getCodigoArticulo());
		dtoJus.setSolicitud(dtoJusParam.getSolicitud());
		dtoJus.setUsuarioModifica(usuario.getLoginUsuario());
		dtoJus.setInstitucion(usuario.getCodigoInstitucion());
		dtoJus.setOrdenAmbulatoria(dtoJusParam.getOrdenAmbulatoria());
		dtoJus.setSubcuenta(dtoJusParam.getSubCuenta());
		dtoJus.setCodigo(dtoJusParam.getCodigoJustificacion());

		for(int s=0; s<dtoJusParam.getSecciones().size(); s++){
			
			DtoParamSeccionesJusNoPos seccion = new DtoParamSeccionesJusNoPos();
			seccion = dtoJusParam.getSecciones().get(s);
			
			for(int c=0; c<seccion.getCampos().size(); c++){
				DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
				campo = seccion.getCampos().get(c);
				
				if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosBibliografia))
					dtoJus.setBibliografia(campo.getValor());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxComplicacion) && !campo.getValor().isEmpty()){
					Diagnostico diagnostico = new Diagnostico();
					diagnostico.setNombreTipoDiagnostico("COMP");
					diagnostico.setAcronimo(campo.getValor().split(ConstantesBD.separadorSplit)[0]);
					diagnostico.setTipoCIE(Utilidades.convertirAEntero(campo.getValor().split(ConstantesBD.separadorSplit)[1]));
					dtoJus.getDiagnosticos().add(diagnostico);
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxPrincipal) && !campo.getValor().isEmpty()){
					Diagnostico diagnostico = new Diagnostico();
					diagnostico.setNombreTipoDiagnostico("PRIN");
					diagnostico.setAcronimo(campo.getValor().split(ConstantesBD.separadorSplit)[0]);
					diagnostico.setTipoCIE(Utilidades.convertirAEntero(campo.getValor().split(ConstantesBD.separadorSplit)[1]));
					dtoJus.getDiagnosticos().add(diagnostico);
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxRelacionado)){
					for(int i=0; i<Utilidades.convertirAEntero(dtoJusParam.getDxRelacionados("numDx")+""); i++){
						if(UtilidadTexto.getBoolean(dtoJusParam.getDxRelacionados("checkbox_"+i)+"")){
							String[] dx = dtoJusParam.getDxRelacionados("relacionado_"+i).toString().split(ConstantesBD.separadorSplit);
							Diagnostico diagnostico = new Diagnostico();
							diagnostico.setNombreTipoDiagnostico("RELA");		
							diagnostico.setAcronimo(dx[0]);
							diagnostico.setTipoCIE(Utilidades.convertirAEntero(dx[1]));
							dtoJus.getDiagnosticos().add(diagnostico);
						}
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEstado))
					dtoJus.setEstado(campo.getValor());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosResumenJustifica))
					dtoJus.setJustificacion(campo.getValor());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosArticuloSustituye))
					dtoJus.setArticuloSustituto(campo.getValor().split(ConstantesBD.separadorSplit)[0]);
					
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadSustituye))
					dtoJus.setCantidadSustituto(campo.getValor());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosMedico))
					dtoJus.setProfesionalResponsable(campo.getValor());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadArtNoPos))
					dtoJus.setCantidad(campo.getValor());
				
				// Agregar todos los campos parametrizables no fijos que van a ser guardados en la bd
				else if (!UtilidadTexto.getBoolean(campo.getFijo()))
					dtoJus.getCamposParam().add(campo);
			}
		}
		
		return dtoJus;
	}

	/**
     * Imprime todos los campos opciones y valores de la justificacion no pos
     * @param dtoParam
     */
	public static void imprimirCamposDtoJustificacion(DtoParamJusNoPos dto) {
		logger.info("\n\n\n\n************************ JUSTIFICACIÓN NO POS ************************");
		for(int s=0; s<dto.getSecciones().size(); s++){
			DtoParamSeccionesJusNoPos seccion = new DtoParamSeccionesJusNoPos();
			seccion = dto.getSecciones().get(s);
			for(int c=0; c<seccion.getCampos().size(); c++){
				DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
				campo = seccion.getCampos().get(c);
				if(!campo.getValor().isEmpty()){
					logger.info(campo.getEtiqueta()+" "+campo.getValor());
				} else {
					logger.info(campo.getEtiqueta()+" "+campo.getValor());
					if(campo.getOpciones().size()>0){
						for(int o=0; o<campo.getOpciones().size(); o++)
							logger.info("     "+campo.getOpciones().get(o).getOpcion()+": "+campo.getOpciones().get(o).getValor()+" / Seleccionado: "+campo.getOpciones().get(o).getSeleccionado());
					}
				}	
			}
		}
		logger.info("\n********************** FIN JUSTIFICACIÓN NO POS **********************\n\n\n");
	}

	/**
	 * Método encargado de consultar una justificación No Pos guardada
	 * @param con
	 * @param Tipo Justificacion
	 * @param dtoParam (dto con la parametrización ya cargada)
	 * @param paciente 
	 * @return
	 */
	public static DtoParamJusNoPos consultarJustificacion(Connection con, String tipoJustificacion, DtoParamJusNoPos dtoParam, PersonaBasica paciente) {
		DtoJustificacionNoPos dtoJus = new DtoJustificacionNoPos(); 
		dtoJus = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().consultarJustificacion(con, tipoJustificacion, dtoParam);
		dtoParam = convertirDtoJus(con, dtoParam, dtoJus,paciente);
		return dtoParam;
	}

	/**
	 * 
	 * @param con 
	 * @param dtoParam
	 * @param dtoJus
	 * @param paciente
	 * @return
	 */
	private static DtoParamJusNoPos convertirDtoJus(Connection con, DtoParamJusNoPos dtoParam, DtoJustificacionNoPos dtoJus,PersonaBasica paciente) 
	{
		String tipoUsuario =dtoJus.getResponsables().get(0).getTipoUsuario(); 
				//UtilidadesFacturacion.consultarNombreTipoRegimen(con, Utilidades.convertirAEntero(dtoJus.getResponsables().get(0).getSubcuenta()));
		dtoParam.setCodigoJustificacion(dtoJus.getCodigo());
		
		FormatoJustArtNopos fjan = new FormatoJustArtNopos();
		fjan.setEmisor("modificar");
		fjan.setMedicamentoNoPos(dtoParam.getCodigoArticulo());
		
		HashMap sustitutosNoPos = null;
		
		if(!dtoParam.getOrdenAmbulatoria().isEmpty()){
			fjan.setProvieneOrdenAmbulatoria(true);
			fjan.setCodigoOrden(dtoParam.getOrdenAmbulatoria());
		}else{
			fjan.setProvieneOrdenAmbulatoria(false);
			fjan.setCodigoSolicitud(dtoParam.getSolicitud());
		}
		sustitutosNoPos = fjan.cargarSustitutosNoPos(con,fjan,paciente);
		
		for(int s=0; s<dtoParam.getSecciones().size(); s++){
			
			DtoParamSeccionesJusNoPos seccion = new DtoParamSeccionesJusNoPos();
			seccion = dtoParam.getSecciones().get(s);
			
			for(int c=0; c<seccion.getCampos().size(); c++){
				DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
				campo = seccion.getCampos().get(c);
				
				// *********** CARGAR CAMPOS FIJOS
				
				if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosInsNombrePaciente))
					campo.setValor(dtoJus.getNombrePaciente());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosInsIdPaciente))
					campo.setValor(dtoJus.getIdPaciente());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEntidad))
					campo.setValor(dtoJus.getResponsables().get(0).getConvenio());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosIngreso))
					campo.setValor(dtoJus.getIngreso());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosFecha))
					campo.setValor(dtoJus.getFechaJustificacion());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosTipoUsuario))
					campo.setValor(tipoUsuario);
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEdad))
					campo.setValor(UtilidadFecha.calcularEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(dtoJus.getFechaNacimientoPaciente()), UtilidadFecha.getFechaActual()));
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEstado)){
					dtoParam.setEstadoJus(dtoJus.getResponsables().get(0).getAcronimoEstado());
					campo.setValor(dtoJus.getResponsables().get(0).getAcronimoEstado());
					campo.setEtiquetaValor(dtoJus.getResponsables().get(0).getEstado());
				}	
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCentroCosto)){
					if(dtoJus.isOrdenAmbulatoria()){
						campo.setValor("Orden Ambulatoria");
					}else{
						campo.setValor(dtoJus.getSolicitadoEn());
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosConsecutivo))
					campo.setValor(dtoJus.getConsecutivo());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxComplicacion)){
					for(int dx=0; dx<dtoJus.getDiagnosticos().size(); dx++){
						if(dtoJus.getDiagnosticos().get(dx).isComplicacion()){
							campo.setEtiquetaValor(dtoJus.getDiagnosticos().get(dx).getAcronimo()+" - "+dtoJus.getDiagnosticos().get(dx).getTipoCIE()+" "+dtoJus.getDiagnosticos().get(dx).getNombre());
							campo.setValor(dtoJus.getDiagnosticos().get(dx).getAcronimo()+ConstantesBD.separadorSplit+dtoJus.getDiagnosticos().get(dx).getTipoCIE()+ConstantesBD.separadorSplit+dtoJus.getDiagnosticos().get(dx).getNombre());
						}
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxPrincipal)){
					for(int dx=0; dx<dtoJus.getDiagnosticos().size(); dx++){
						if(dtoJus.getDiagnosticos().get(dx).isPrincipal()){
							campo.setEtiquetaValor(dtoJus.getDiagnosticos().get(dx).getAcronimo()+" - "+dtoJus.getDiagnosticos().get(dx).getTipoCIE()+" "+dtoJus.getDiagnosticos().get(dx).getNombre());
							campo.setValor(dtoJus.getDiagnosticos().get(dx).getAcronimo()+ConstantesBD.separadorSplit+dtoJus.getDiagnosticos().get(dx).getTipoCIE()+ConstantesBD.separadorSplit+dtoJus.getDiagnosticos().get(dx).getNombre());
						}
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxRelacionado)){
					HashMap<String, Object> dxRelacionadosMap = new HashMap<String, Object>();
					int numDx=0;
					String dxSeleccionados="'"+ConstantesBD.codigoNuncaValido+"'";
					for(int dx=0; dx<dtoJus.getDiagnosticos().size(); dx++){
						if(!dtoJus.getDiagnosticos().get(dx).isPrincipal() && !dtoJus.getDiagnosticos().get(dx).isComplicacion()){
							
							dxRelacionadosMap.put("relacionado_"+numDx, dtoJus.getDiagnosticos().get(dx).getAcronimo()+ConstantesBD.separadorSplit+dtoJus.getDiagnosticos().get(dx).getTipoCIE()+ConstantesBD.separadorSplit+dtoJus.getDiagnosticos().get(dx).getNombre());
							dxRelacionadosMap.put("checkbox_"+numDx, true);
							dxSeleccionados += ", '"+dtoJus.getDiagnosticos().get(dx).getAcronimo()+"'";
							numDx++;
							
						}
					}
					dxRelacionadosMap.put("numDx", numDx);
					dxRelacionadosMap.put("dxSeleccionados", dxSeleccionados);
					dtoParam.setDxRelacionadosMap(dxRelacionadosMap);
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosResumenJustifica))
					campo.setValor(dtoJus.getJustificacion());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosBibliografia))
					campo.setValor(dtoJus.getBibliografia());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosNombreArtNoPos))
					campo.setValor(dtoJus.getNombreArticulo());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadArtNoPos))
					campo.setValor(dtoJus.getResponsables().get(0).getCantidad());
				
				/*SUSTITUTOS*/

				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosExisteSustituto)){
					campo.setDisabled(true);
					if (Utilidades.convertirAEntero(sustitutosNoPos.get("numRegistros").toString()) > 0)
						campo.setValor(ConstantesBD.acronimoSi);
					else
						campo.setValor(ConstantesBD.acronimoNo);
					
					for(int op=0; op<campo.getOpciones().size(); op++){
						if (campo.getOpciones().get(op).getValor().equals(campo.getValor()))
							campo.getOpciones().get(op).setSeleccionado(ConstantesBD.acronimoSi);
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosArticuloSustituye)){
					for(int r=0; r<Utilidades.convertirAEntero(sustitutosNoPos.get("numRegistros")+""); r++){
						if((dtoJus.getArticuloSustituto()==null||dtoJus.getArticuloSustituto().isEmpty())&&(campo.getValor()==null||campo.getValor().isEmpty())){
							campo.setValor(sustitutosNoPos.get("codigo_"+r)+ConstantesBD.separadorSplit+sustitutosNoPos.get("numdossisequiv_"+r));
						}else{
							if(dtoJus.getArticuloSustituto()!=null&&!dtoJus.getArticuloSustituto().isEmpty()&&dtoJus.getArticuloSustituto().equals(sustitutosNoPos.get("codigo_"+r).toString())){
								campo.setValor(sustitutosNoPos.get("codigo_"+r)+ConstantesBD.separadorSplit+sustitutosNoPos.get("numdossisequiv_"+r));
							}
						}
						DtoParamOpcionesCamposJusNoPos opcion = new DtoParamOpcionesCamposJusNoPos();
						opcion.setValor(sustitutosNoPos.get("codigo_"+r)+ConstantesBD.separadorSplit+sustitutosNoPos.get("numdossisequiv_"+r));
						opcion.setOpcion(sustitutosNoPos.get("nombre_"+r).toString());
						campo.getOpciones().add(opcion);
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadSustituye)){
					if (Utilidades.convertirAEntero(sustitutosNoPos.get("numRegistros").toString()) > 0)
						campo.setValor(sustitutosNoPos.get("numdossisequiv_0").toString());
				}
				/*FIN SUSTITUTOS*/
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosMedico))
					campo.setValor(dtoJus.getNombreMedico()+"");
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEspecialidadMedico))
					campo.setValor(dtoJus.getEspecialidadMedico()+"");
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosIdMedico))
					campo.setValor(dtoJus.getIdMedico());
			
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosRegistroMedico))
					campo.setValor(dtoJus.getRegistroMedico());
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosFirmaSelloMedico))
					campo.setValor(dtoJus.getFirmaDigital());
				
				
				// *********** CARGAR CAMPOS PARAMÉTRICOS
				
				else {
					for(int cp=0; cp<dtoJus.getCamposParam().size(); cp++){
						if(campo.getCodigo().equals(dtoJus.getCamposParam().get(cp).getCodigo())){
							campo.setValor(dtoJus.getCamposParam().get(cp).getValor());
							
							
							for(int op=0; op<campo.getOpciones().size(); op++){
								if (campo.getOpciones().get(op).getValor().equals(campo.getValor())){
									campo.getOpciones().get(op).setSeleccionado(ConstantesBD.acronimoSi);
								}
								else if(campo.getTipoHtml().equals("CHEC")){
									for(int opj=0; opj<dtoJus.getCamposParam().get(cp).getOpciones().size(); opj++){
										if(campo.getOpciones().get(op).getCodigo().equals(dtoJus.getCamposParam().get(cp).getOpciones().get(opj).getCodigo()))
											campo.getOpciones().get(op).setSeleccionado(ConstantesBD.acronimoSi);
									}
								}
							}
						}
					}
				}
			}
		}
		return dtoParam;
	}

	/**
	 * 
	 * @param con
	 * @param dtoParam
	 * @param acronimoInsumo
	 * @param usuario
	 */
	public static boolean actualizarJustificacion(Connection con, DtoParamJusNoPos dtoParam, String tipoJustificacion, UsuarioBasico usuario) 
	{
		DtoJustificacionNoPos dtoJus = new DtoJustificacionNoPos();
		dtoJus = convertirDtoJusParam(dtoParam, usuario);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().actualizarJustificacion(con, dtoJus, tipoJustificacion);
	}
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un Insumo/Orden Ambulatoria
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @param codigoArticulo
	 * @param codigoOrdenAmbulatoria
	 * @return
	 */
	public static boolean existeJustificacionOrdenAmbulatoria(Connection con, int institucion, String tipoJustificacion, String codigoArticulo, String codigoOrdenAmbulatoria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesJustificacionNoPosDao().existeJustificacionOrdenAmbulatoria(con, institucion, tipoJustificacion, codigoArticulo, codigoOrdenAmbulatoria);
	}
}