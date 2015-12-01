/**
 * 
 */
package com.servinte.axioma.bl.salasCirugia.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.salas.ConstantesBDSalas;
import util.salas.UtilidadesSalas;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.NotasGeneralesEnfermeria;
import com.princetonsa.mundo.ordenesmedicas.cirugias.NotasRecuperacion;
import com.princetonsa.mundo.parametrizacion.TecnicaAnestesia;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.bl.salasCirugia.interfaz.IHojaQuirurgicaMundo;
import com.servinte.axioma.dto.salascirugia.CargoSolicitudDto;
import com.servinte.axioma.dto.salascirugia.DestinoPacienteDto;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.FinalidadDto;
import com.servinte.axioma.dto.salascirugia.HojaAnestesiaDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxEspecialidadDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.dto.salascirugia.ProgramacionPeticionQxDto;
import com.servinte.axioma.dto.salascirugia.SalaCirugiaDto;
import com.servinte.axioma.dto.salascirugia.ServicioHQxDto;
import com.servinte.axioma.dto.salascirugia.SolicitudCirugiaDto;
import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.dto.salascirugia.TipoHeridaDto;
import com.servinte.axioma.dto.salascirugia.TipoProfesionalDto;
import com.servinte.axioma.dto.salascirugia.TipoSalaDto;
import com.servinte.axioma.dto.salascirugia.TipoViaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class HojaQuirurgicaMundo implements IHojaQuirurgicaMundo{

	@Override
	public List<PeticionQxDto> consultarPeticiones(int codigoInstitucion,int codigoIngreso, int codigoPaciente) throws IPSException {
		List<PeticionQxDto>peticiones=new ArrayList<PeticionQxDto>();
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			
			String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
			if(UtilidadTexto.isEmpty(codigoTarifario)){
				codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
			}
			
			peticiones=HojaQuirurgica.consultarPeticiones(codigoTarifario, codigoIngreso, codigoPaciente, null,null, connection);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		
		return peticiones;
	}
	
	@Override
	public List<EspecialidadDto> consultarEspecialidadesInformeQx(int codigoSolicitudCx)
			throws IPSException {
		List<EspecialidadDto>especialidades=new ArrayList<EspecialidadDto>(0);
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			especialidades=HojaQuirurgica.consultarEspecialidadesInformeQx(connection, codigoSolicitudCx);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return especialidades;
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.salasCirugia.interfaz.IHojaQuirurgicaMundo#consultarPeticionCirugia(int, int, int, int)
	 */
	@Override
	public PeticionQxDto consultarPeticionCirugia(int codigoInstitucion,int codigoIngreso, int codigoPaciente,int codigoPeticionSolicitud,boolean esSolicitud) throws IPSException {
		Connection connection=null;
		PeticionQxDto peticionQxDto=null;
		try{
			connection=UtilidadBD.abrirConexion();
			List<PeticionQxDto>peticiones=null;
			
			String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
			if(UtilidadTexto.isEmpty(codigoTarifario)){
				codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
			}
			
			if(esSolicitud){
				peticiones=HojaQuirurgica.consultarPeticiones(codigoTarifario, codigoIngreso, codigoPaciente, null, codigoPeticionSolicitud, connection);
			}else{
				peticiones=HojaQuirurgica.consultarPeticiones(codigoTarifario, codigoIngreso, codigoPaciente, codigoPeticionSolicitud, null, connection);
			}
			if(!peticiones.isEmpty()){
				for(PeticionQxDto peticion:peticiones){
					if(esSolicitud){
						if(peticion.isEsSolicitud()&&peticion.getSolicitudCirugia().getNumeroSolicitud()==codigoPeticionSolicitud){
							peticionQxDto=peticion;
							break;
						}
					}else{
						if(peticion.getCodigoPeticion()==codigoPeticionSolicitud){
							peticionQxDto=peticion;
							break;
						}
					}
				}
			}
		}catch (IPSException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return peticionQxDto;
	}

	@Override
	public InformacionActoQxDto consultarInformacionActoQx(SolicitudCirugiaDto solicitudCirugiaDto,int codigoIngreso)
			throws IPSException {
		InformacionActoQxDto informacionActoQxDto=new InformacionActoQxDto();
		Connection connection=null;
		
		try{
			informacionActoQxDto.setAnestesiologo(new ProfesionalHQxDto());
			informacionActoQxDto.setTipoAnestesia(new TipoAnestesiaDto());
			connection=UtilidadBD.abrirConexion();
			
			boolean existeHQx=HojaQuirurgica.existeHojaQx(connection, solicitudCirugiaDto.getNumeroSolicitud()+"");
			boolean existeHA=UtilidadTexto.getBoolean(HojaQuirurgica.existeHojaAnestesia(connection, solicitudCirugiaDto.getNumeroSolicitud()+""));
			/**
			 * Aqui no se cargan los diagnosticos de la ultima valoracion o evolucion
			 * */
			if(existeHQx){
				informacionActoQxDto=HojaQuirurgica.consultarInformacionActoQx(connection, solicitudCirugiaDto.getNumeroSolicitud());
				
				DtoDiagnostico diagPrincipalHQx= HojaQuirurgica.consultarDiagnosticoPrincipalPreoperatorio(connection, solicitudCirugiaDto.getNumeroSolicitud());
				List<DtoDiagnostico> diagRelacionadosHQx= HojaQuirurgica.consultarDiagnosticosRelacionadosPreoperatorio(connection, solicitudCirugiaDto.getNumeroSolicitud());
				
				if(diagPrincipalHQx!=null){
					informacionActoQxDto.setDiagnosticoPrincipal(diagPrincipalHQx);
				}
				
				if(informacionActoQxDto.getAnestesiologo()==null){
					informacionActoQxDto.setAnestesiologo(new ProfesionalHQxDto());
				}
				
				if(diagRelacionadosHQx.size()>0){
					informacionActoQxDto.setDiagnosticosRelacionados(diagRelacionadosHQx);
				}else{
					informacionActoQxDto.setDiagnosticosRelacionados(new ArrayList<DtoDiagnostico>(0));
				}
				
			}
			
			if(existeHA){
				informacionActoQxDto.setParticipaAnestesiologo(true);
				//MT 7264: Verificación sobre datos de la hoja de anestesia (TIPO DE ANESTESIA)
				TipoAnestesiaDto tipoAnestesiaDto=consultarUltimoTipoAnestesiaHojaAnestesia(solicitudCirugiaDto.getNumeroSolicitud());
				informacionActoQxDto.setTipoAnestesia(tipoAnestesiaDto);
				
			}
	
			informacionActoQxDto.setExisteHojaAnestesia(existeHA);
		}catch (IPSException e) {
			throw e;
		}catch (Exception e) {
			throw new IPSException(e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return informacionActoQxDto;
	}

	@Override
	public List<ProfesionalHQxDto> consultarAnestesiologos(int codigoInstitucion)
			throws IPSException {
		List<ProfesionalHQxDto>profesionales=new ArrayList<ProfesionalHQxDto>(0);
		Connection connection=null;
		
		try{
			connection=UtilidadBD.abrirConexion();
			
			profesionales=HojaQuirurgica.consultarAnestesiologos(connection, codigoInstitucion,ValoresPorDefecto.getEspecialidadAnestesiologia(codigoInstitucion, true));
			
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return profesionales;
	}

	@Override
	public List<TipoAnestesiaDto> consultarTiposAnestesia(int codigoInstitucion, int codigoCentroCostos) throws IPSException {
		List<TipoAnestesiaDto>listaAnestesias=new ArrayList<TipoAnestesiaDto>(0);
		Connection connection=null;
		
		try{
			connection=UtilidadBD.abrirConexion();
			List<HashMap<String, Object>>mapaTiposAnestesia=null;
			
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(codigoInstitucion))){
				mapaTiposAnestesia=UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(connection,codigoInstitucion, codigoCentroCostos,"", "");
			}else{
				mapaTiposAnestesia=UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(connection, codigoInstitucion, codigoCentroCostos, ConstantesBD.acronimoSi, "");
			}
			
			for(HashMap<String, Object>anestesia:mapaTiposAnestesia){
				TipoAnestesiaDto tipoAnestesiaDto=new TipoAnestesiaDto();
				tipoAnestesiaDto.setCodigo(Utilidades.convertirAEntero(anestesia.get("codigo").toString()));
				tipoAnestesiaDto.setAcronimo(anestesia.get("acronimo").toString());
				tipoAnestesiaDto.setDescripcion(anestesia.get("nombre").toString());
				
				listaAnestesias.add(tipoAnestesiaDto);
			}
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return listaAnestesias;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<FinalidadDto> consultarFinalidades(String codigoNaturalezaServicio,int codigoInstitucion) throws IPSException {
		List<FinalidadDto> listaFinalidades=new ArrayList<FinalidadDto>(0); 
		Connection connection=null;
		
		try{
			connection=UtilidadBD.abrirConexion();
			HashMap criterios = new HashMap ();
			criterios.put("naturaleza", codigoNaturalezaServicio);
			criterios.put("institucion", codigoInstitucion);
			List<HashMap<String,Object>>finalidades=HojaQuirurgica.consultarFinalidadServicio(connection, criterios);
			
			for(HashMap<String, Object>finalidad:finalidades){
				FinalidadDto finalidadDto=new FinalidadDto();
				
				finalidadDto.setCodigo(Utilidades.convertirAEntero(finalidad.get("codigo").toString()));
				finalidadDto.setNombre(finalidad.get("finalidad").toString());
				
				listaFinalidades.add(finalidadDto);
			}
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return listaFinalidades;
	}

	@Override
	public List<TipoViaDto> consultarTiposVia() throws IPSException {
		List<TipoViaDto>listaTiposVia=new ArrayList<TipoViaDto>(0);
		try{
			MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
			
			TipoViaDto tipoViaDto=new TipoViaDto();
			tipoViaDto.setCodigo(ConstantesBD.codigoIgualCx);
			tipoViaDto.setAcronimo(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.acronimoIgual"));
			tipoViaDto.setNombre(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.igual"));
			
			listaTiposVia.add(tipoViaDto);
			
			tipoViaDto=new TipoViaDto();
			tipoViaDto.setCodigo(ConstantesBD.codigoDiferenteCx);
			tipoViaDto.setAcronimo(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.acronimoDiferente"));
			tipoViaDto.setNombre(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.diferente"));
			listaTiposVia.add(tipoViaDto);
		}catch (Exception e) {
			throw new IPSException(e);
		}
		
		return listaTiposVia;
	}

	@Override
	public List<TipoHeridaDto> consultarTiposHerida() throws IPSException {
		List<TipoHeridaDto>listaTiposHeridaDto=new ArrayList<TipoHeridaDto>(0);
		
		try{
			TipoHeridaDto tipoHeridaDto=new TipoHeridaDto();
			tipoHeridaDto.setAcronimo(ConstantesIntegridadDominio.acronimoTipoHeridaContaminada);
			tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaContaminada).toString());
			
			listaTiposHeridaDto.add(tipoHeridaDto);
			
			tipoHeridaDto=new TipoHeridaDto();
			tipoHeridaDto.setAcronimo(ConstantesIntegridadDominio.acronimoTipoHeridaLimpia);
			tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaLimpia).toString());
			
			listaTiposHeridaDto.add(tipoHeridaDto);
			
			tipoHeridaDto=new TipoHeridaDto();
			tipoHeridaDto.setAcronimo(ConstantesIntegridadDominio.acronimoTipoHeridaLimpiaContaminada);
			tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaLimpiaContaminada).toString());
			
			listaTiposHeridaDto.add(tipoHeridaDto);
			
			tipoHeridaDto=new TipoHeridaDto();
			tipoHeridaDto.setAcronimo(ConstantesIntegridadDominio.acronimoTipoHeridaSucia);
			tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaSucia).toString());
			
			listaTiposHeridaDto.add(tipoHeridaDto);
		}catch (Exception e) {
			throw new IPSException(e);
		}
		
		return listaTiposHeridaDto;
	}

	@Override
	public InformeQxEspecialidadDto consultarInformeQxXEspecialidad(PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto,PersonaBasica paciente, int institucion) throws IPSException {
		InformeQxEspecialidadDto informeQxEspecialidadDto=null;
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			informeQxEspecialidadDto=HojaQuirurgica.consultarDescripcionOperatoriaXEspecialidad(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), especialidadDto.getCodigo());
			
			informeQxEspecialidadDto.setEspecialidad(especialidadDto);
			
			String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
			if(UtilidadTexto.isEmpty(codigoTarifario)){
				codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
			}
			
			List<ServicioHQxDto>listaServicios=HojaQuirurgica.consultarServiciosPeticion(connection, peticionQxDto, especialidadDto, codigoTarifario, false,false);
			informeQxEspecialidadDto.setServicios(listaServicios);
			
			int codigoViaIngreso = 0;
			if(paciente!=null){
				codigoViaIngreso = Cuenta.obtenerCodigoViaIngresoCuenta(connection, paciente.getCodigoCuenta()+"");
			}
			
			for (ServicioHQxDto servicioHQxDto : listaServicios) {
				if(!UtilidadTexto.isEmpty(servicioHQxDto.getNaturalezaServicio())){
					List<FinalidadDto> finalidades = this.consultarFinalidades(servicioHQxDto.getNaturalezaServicio(), institucion);
					servicioHQxDto.setFinalidades(finalidades);
				}
				
				if(paciente!=null){
					InfoResponsableCobertura infoResponsableCobertura = 
			    			Cobertura.validacionCoberturaServicio(connection, paciente.getCodigoIngreso()+"", codigoViaIngreso, paciente.getCodigoTipoPaciente(), servicioHQxDto.getCodigo(), institucion, Utilidades.esSolicitudPYP(connection,peticionQxDto.getSolicitudCirugia().getNumeroSolicitud()), "" /*subCuentaCoberturaOPCIONAL*/);
					if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()){
						servicioHQxDto.setCubierto(true);
					}else{
						servicioHQxDto.setCubierto(false);
					}
					if(infoResponsableCobertura!=null&&infoResponsableCobertura.getDtoSubCuenta()!=null){
						servicioHQxDto.setCodigoContrato(infoResponsableCobertura.getDtoSubCuenta().getContrato());
						servicioHQxDto.setSubCuenta(infoResponsableCobertura.getDtoSubCuenta().getSubCuenta());
						
						if(infoResponsableCobertura.getDtoSubCuenta().getConvenio()!=null){
							servicioHQxDto.setCobertura(infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
						}
					}
				}
			}
			
			DtoDiagnostico dxPrincipal=HojaQuirurgica.consultarDiagnosticoPrincipalPostoperatorio(connection, informeQxEspecialidadDto.getCodigo());
			List<DtoDiagnostico> dxRelacionados=HojaQuirurgica.consultarDiagnosticosRelacionadosPostoperatorio(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), informeQxEspecialidadDto.getCodigo());
			DtoDiagnostico dxComplicacion=HojaQuirurgica.consultarDiagnosticoComplicacionPostoperatorio(connection, informeQxEspecialidadDto.getCodigo());
			
			informeQxEspecialidadDto.setDiagnosticoPostOperatorioPrincipal(dxPrincipal);
			
			if(dxRelacionados==null||dxRelacionados.isEmpty()){
				dxRelacionados=new ArrayList<DtoDiagnostico>(0);
			}
			informeQxEspecialidadDto.setDiagnosticosPostOperatorioRelacionados(dxRelacionados);
			
			informeQxEspecialidadDto.setDiagnosticoPostOperatorioComplicacion(dxComplicacion);
			
		}catch (IPSException e) {
			throw e;
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return informeQxEspecialidadDto;
	}

	@Override
	public List<EspecialidadDto> consultarEspecialidadesProfesional(ProfesionalHQxDto profesionalHQxDto, int codigoInstitucion,List<EspecialidadDto> descartarEspecialidades)
			throws IPSException {
		List<EspecialidadDto> especialidades=new ArrayList<EspecialidadDto>(0);
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			especialidades=Utilidades.consultarEspecialidadesProfesional(connection, profesionalHQxDto, codigoInstitucion, true, descartarEspecialidades);
			
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		
		return especialidades;
	}

	@Override
	public List<NotaAclaratoriaDto> consultarNotasAclaratorias(int codigoInformeQxEspecialidad, boolean esAscendente) throws IPSException {
		List<NotaAclaratoriaDto> notasAclaratorias = new ArrayList<NotaAclaratoriaDto>();
		Connection con =null;
		try{
			con=UtilidadBD.abrirConexion();
			notasAclaratorias=HojaQuirurgica.consultarNotasAclaratorias(con, codigoInformeQxEspecialidad, esAscendente);
			
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		
		return notasAclaratorias;
	}
	
	@Override
	public void guardarNotaAclaratotia(NotaAclaratoriaDto notaAclaratoriaDto, PeticionQxDto peticionQxDto, UsuarioBasico usuarioModifica, int codigoIngreso) throws IPSException {
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(con);
			HojaQuirurgica.guardarNotaAclaratotia(con, notaAclaratoriaDto, usuarioModifica);
			
			IngresoSalidaPacienteDto ingresoSalidaPacienteDto=HojaQuirurgica.consultarIngresoSalidaPaciente(con, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
			boolean esFinalizadaHQx=ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica();
			cambiarEstadosSolicitudHQx(con, peticionQxDto, esFinalizadaHQx, usuarioModifica, codigoIngreso);
			
			UtilidadBD.finalizarTransaccion(con);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
	}
	

	@Override
	public IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(int numeroSolicitud) throws IPSException {
		IngresoSalidaPacienteDto dto = null;
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			dto = HojaQuirurgica.consultarIngresoSalidaPaciente(con,numeroSolicitud);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}

		return dto;
	}

	@Override
	public List<TipoSalaDto> consultarTiposSala(int institucion) throws IPSException {
		
		List<TipoSalaDto> listaSalas = new ArrayList<TipoSalaDto>();
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			listaSalas=HojaQuirurgica.consultarTiposSala(con,institucion);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return listaSalas;
	}

	@Override
	public List<DestinoPacienteDto> consultarDestinosPaciente(int institucion) throws IPSException {
		
		List<DestinoPacienteDto> listaDestinos = new ArrayList<DestinoPacienteDto>();
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			listaDestinos=HojaQuirurgica.consultarDestinosPaciente(con,institucion);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return listaDestinos;
	}

	@Override
	public List<SalaCirugiaDto> consultarSalasCirugia(TipoSalaDto tipoSalaDto) throws IPSException {
		
		List<SalaCirugiaDto> listaSalas = new ArrayList<SalaCirugiaDto>();
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			listaSalas=HojaQuirurgica.consultarSalasCirugia(con,tipoSalaDto);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return listaSalas;
	}

	@Override
	public void reversarHojaQx(int numeroSolicitud) throws IPSException {
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			HojaQuirurgica.reversarHojaQx(con, numeroSolicitud);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
	}

	@Override
	public void guardarIngresoSalidaPaciente(PeticionQxDto peticionQxDto,IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica, int codigoIngreso) throws IPSException {
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(con);
			HojaQuirurgica.guardarIngresoSalidaPaciente(con, ingresoSalidaPacienteDto, usuarioModifica);

			cambiarEstadosSolicitudHQx(con, peticionQxDto, ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica(), usuarioModifica, codigoIngreso);
			
			UtilidadBD.finalizarTransaccion(con);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
	}

	/**
	 * Metodo que carga una lista de Notas enfermeria a partir de un numero de Solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotasEnfermeria
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 02/07/2013
	 */
	public List<NotaEnfermeriaDto> consultarNotasEnfermeria(int numeroSolicitud, boolean esAscendente) throws IPSException {
		List<NotaEnfermeriaDto>listaNotasEnfermeria=new ArrayList<NotaEnfermeriaDto>();
		NotasGeneralesEnfermeria notasGeneralesEnfermeria = new NotasGeneralesEnfermeria();
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			listaNotasEnfermeria=notasGeneralesEnfermeria.listaNotasEnfermeria(con, numeroSolicitud, esAscendente);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return listaNotasEnfermeria;
	}

	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @created 03/07/2013
	 */
	public void guardarNotaEnfermeria(NotaEnfermeriaDto dto)throws IPSException {
		NotasGeneralesEnfermeria notasGeneralesEnfermeria = new NotasGeneralesEnfermeria();
		
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(con);
			notasGeneralesEnfermeria.guardarNotaEnfermeria(con, dto);
			UtilidadBD.finalizarTransaccion(con);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}	
	}
	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param dto NotaRecuperacionDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public void guardarNotaRecuperacion(NotaRecuperacionDto dto)throws IPSException {
		NotasRecuperacion notasRecuperacion = new NotasRecuperacion();
		
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(con);
			notasRecuperacion.guardarNotaRecuperacion(con, dto);
			UtilidadBD.finalizarTransaccion(con);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}	
	}

	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 */
	public List<NotaRecuperacionDto> consultarNotasRecuperacion(int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente) throws IPSException {
		List<NotaRecuperacionDto>listaNotasRecuperacion=new ArrayList<NotaRecuperacionDto>();
		NotasRecuperacion notasRecuperacion = new NotasRecuperacion();
		
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			listaNotasRecuperacion=notasRecuperacion.listaNotasRecuperacion(con, codigoInstitucion, numeroSolicitud, soloEstructuraParametrizada,esAscendente);
			UtilidadBD.finalizarTransaccion(con);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(con);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		} finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return listaNotasRecuperacion;
	}
	
	@Override
	public void guardarInformacionActoQuirurgico(PeticionQxDto peticionQxDto,InformacionActoQxDto informacionActoQxDto, List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioBasico, int codigoIngreso)throws IPSException{
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			boolean existeHQx=false;
			try{
				existeHQx=HojaQuirurgica.existeHojaQx(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud()+"");
			}catch (Exception e) {
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}
			try{
				UtilidadBD.iniciarTransaccion(connection);
				HojaQuirurgica.guardarInformacionActoQuirurgico(connection, existeHQx, peticionQxDto, informacionActoQxDto, dxRelacionadosEliminar, usuarioBasico);
				
				IngresoSalidaPacienteDto ingresoSalidaPacienteDto=HojaQuirurgica.consultarIngresoSalidaPaciente(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				boolean esFinalizadaHQx=ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica();
				cambiarEstadosSolicitudHQx(connection, peticionQxDto, esFinalizadaHQx, usuarioBasico, codigoIngreso);
				
				UtilidadBD.finalizarTransaccion(connection);
			}catch (BDException e) {
				UtilidadBD.abortarTransaccion(connection);
				throw e;
			}catch (Exception e) {
				UtilidadBD.abortarTransaccion(connection);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void guardarDescripcionOperatoria(PersonaBasica paciente, PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioModifica, List<DtoDiagnostico> dxRelacionadosEliminar, List<ServicioHQxDto> serviciosEliminar, int codigoIngreso,
			Map<String, String> serviciosNOPOS, HashMap justificacionesNOPOSServicios)
			throws IPSException {
		Connection connection=null;
		boolean existeIQxE=false;
		boolean esFinalizadaHQx=false;
		try{
			connection=UtilidadBD.abrirConexion();
			try{
				
				informeQxEspecialidadDto.setCodigoSolicitudesCirugia(peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				
				if(informeQxEspecialidadDto.getCodigo()>0){
					existeIQxE=HojaQuirurgica.existeInformeQxEspecialidad(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), informeQxEspecialidadDto.getCodigo(),null);
				}else{
					existeIQxE=HojaQuirurgica.existeInformeQxEspecialidad(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), null, informeQxEspecialidadDto.getEspecialidad().getCodigo());
				}
				
				/**
				 * INICIO Cobertura
				 * Se consulta la covertura del servicio por parte del convenio
				 * */
				int codigoViaIngreso = Cuenta.obtenerCodigoViaIngresoCuenta(connection, paciente.getCodigoCuenta()+"");
				MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
				
				for(ServicioHQxDto servicio:informeQxEspecialidadDto.getServicios()){
					InfoResponsableCobertura infoResponsableCobertura = 
			    			Cobertura.validacionCoberturaServicio(connection, paciente.getCodigoIngreso()+"", codigoViaIngreso, paciente.getCodigoTipoPaciente(), servicio.getCodigo(), usuarioModifica.getCodigoInstitucionInt(), Utilidades.esSolicitudPYP(connection,peticionQxDto.getSolicitudCirugia().getNumeroSolicitud()), "" /*subCuentaCoberturaOPCIONAL*/);
					if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()){
						servicio.setCubierto(true);
					}else{
						servicio.setCubierto(false);
					}
					if(infoResponsableCobertura!=null&&infoResponsableCobertura.getDtoSubCuenta()!=null){
						servicio.setCodigoContrato(infoResponsableCobertura.getDtoSubCuenta().getContrato());
					}
					
					TipoViaDto tipoVia=servicio.getTipoVia();
						
					if(tipoVia.getCodigo()==ConstantesBD.codigoIgualCx){
						tipoVia.setAcronimo(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.acronimoIgual"));
						tipoVia.setNombre(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.igual"));
					}else{
						if(tipoVia.getCodigo()==ConstantesBD.codigoDiferenteCx){
							tipoVia.setAcronimo(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.acronimoDiferente"));
							tipoVia.setNombre(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.diferente"));
						}
					}
				}
				
				
				IngresoSalidaPacienteDto ingresoSalidaPacienteDto=HojaQuirurgica.consultarIngresoSalidaPaciente(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				esFinalizadaHQx=ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica();
				
				/** FIN Cobertura */
			}catch (BDException e) {
				throw e;
			}catch (Exception e) {
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}
			
			try {
				UtilidadBD.iniciarTransaccion(connection);
				
				String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuarioModifica.getCodigoInstitucionInt());
				if(UtilidadTexto.isEmpty(codigoTarifario)){
					codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
				}
				
				HojaQuirurgica.guardarDescripcionOperatoria(connection, existeIQxE, peticionQxDto, informeQxEspecialidadDto, usuarioModifica, dxRelacionadosEliminar, serviciosEliminar,codigoTarifario);
				
				/**
				 * JUSTIFICACIONES NO POS
				 * */
				for (int j = 0; j < informeQxEspecialidadDto.getServicios().size(); j++) {
					ServicioHQxDto servicioHQxDto=informeQxEspecialidadDto.getServicios().get(j);
					if (!servicioHQxDto.isEsPos() && !servicioHQxDto.isHaSidoJustificado() 
							&& servicioHQxDto.isJustificar()){
						
						justificacionesNOPOSServicios.put(servicioHQxDto.getCodigo()+"_subcuenta", servicioHQxDto.getSubCuenta());
						
						FormatoJustServNopos.ingresarJustificacion(
	                		connection,
	                		usuarioModifica.getCodigoInstitucionInt(), 
	                		usuarioModifica.getLoginUsuario(), 
	                		(HashMap) //this.justificacionesServicios,
	                		justificacionesNOPOSServicios.get(servicioHQxDto.getCodigo()+"_mapajustificacion"),
	                		peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(),
	                		ConstantesBD.codigoNuncaValido,
	                		servicioHQxDto.getCodigo(),
	                		usuarioModifica.getCodigoPersona());
	                }
				}
				/**
				 * FIN JUSTIFICACIONES NO POS
				 * */
				
				cambiarEstadosSolicitudHQx(connection, peticionQxDto, esFinalizadaHQx, usuarioModifica, codigoIngreso);
				
				UtilidadBD.finalizarTransaccion(connection);
			}catch (BDException e) {
				UtilidadBD.abortarTransaccion(connection);
				throw e;
			}catch (Exception e) {
				UtilidadBD.abortarTransaccion(connection);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
	}
	
	@Override
	public List<ProfesionalHQxDto> consultarProfesionales(int codigoInstitucion) throws IPSException{
		Connection connection=null;
		List<ProfesionalHQxDto>profesionales=new ArrayList<ProfesionalHQxDto>(0);
		try{
			connection=UtilidadBD.abrirConexion();
			profesionales=HojaQuirurgica.consultarProfesionales(connection, codigoInstitucion);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return profesionales;
	}
	
	@Override
	public InformeQxDto consultarProfesionalesInformeQx(PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto, PersonaBasica paciente, int institucion) throws IPSException{
		InformeQxDto informeQxDto=new InformeQxDto();
		
		InformeQxEspecialidadDto informeQxEspecialidadDto=this.consultarProfesionalesInformeQxXEspecialidad(peticionQxDto, especialidadDto, paciente, institucion);
		informeQxDto.setInformeQxEspecialidad(informeQxEspecialidadDto);
		
		List<ProfesionalHQxDto>otrosProfesionales=this.consultarOtrosProfesionalesInfoQx(peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
		informeQxDto.setOtrosProfesionales(otrosProfesionales);
		
		return informeQxDto;
	}
	
	/**
	 * Consulta los profesionales relacionados a una cirugia, tanto por servicios como por especialidad
	 * 
	 * @param peticionQxDto
	 * @param especialidadDto
	 * @param paciente 
	 * @param codigoViaIngreso
	 * @param institucion 
	 * @return informe qx por especialidad 
	 * @throws IPSException
	 * @author jeilones
	 * @created 9/07/2013
	 */
	private InformeQxEspecialidadDto consultarProfesionalesInformeQxXEspecialidad(PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto, PersonaBasica paciente, int institucion) throws IPSException{
		InformeQxEspecialidadDto informeQxEspecialidadDto=null;
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			
			informeQxEspecialidadDto=this.consultarInformeQxXEspecialidad(peticionQxDto, especialidadDto, paciente, institucion);
			
			informeQxEspecialidadDto.setEspecialidad(especialidadDto);
			
			String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
			if(UtilidadTexto.isEmpty(codigoTarifario)){
				codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
			}
			List<ServicioHQxDto>serviciosXEspecialidad=HojaQuirurgica.consultarServiciosPeticion(connection, peticionQxDto, especialidadDto, codigoTarifario, false, true);
			
			for (ServicioHQxDto servicioHQxDto : serviciosXEspecialidad) {
				if(!UtilidadTexto.isEmpty(servicioHQxDto.getNaturalezaServicio())){
					List<FinalidadDto> finalidades = this.consultarFinalidades(servicioHQxDto.getNaturalezaServicio(), institucion);
					servicioHQxDto.setFinalidades(finalidades);
				}
			}
			
			informeQxEspecialidadDto.setServicios(serviciosXEspecialidad);
			List<ProfesionalHQxDto>profesionalesXEspecialidad=HojaQuirurgica.consultarProfesionalesXEspecialidad(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), especialidadDto.getCodigo());
			
			for (ProfesionalHQxDto profesionalHQxDto : profesionalesXEspecialidad) {
				List<EspecialidadDto>especialidades=this.consultarEspecialidadesProfesional(profesionalHQxDto, institucion, new ArrayList<EspecialidadDto>(0));
				profesionalHQxDto.setEspecialidades(especialidades);
			}
			
			for (ServicioHQxDto servicio : informeQxEspecialidadDto.getServicios()) {
				for (ProfesionalHQxDto profesionalHQxDto : servicio.getProfesionalesXServicio()) {
					List<EspecialidadDto>especialidades=this.consultarEspecialidadesProfesional(profesionalHQxDto, institucion, new ArrayList<EspecialidadDto>(0));
					profesionalHQxDto.setEspecialidades(especialidades);
				}
			}
			
			informeQxEspecialidadDto.setProfesionales(profesionalesXEspecialidad);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return informeQxEspecialidadDto;
	}
	
	/**
	 * Consulta otros profesionales que participan en una cirugia
	 * 
	 * @param numeroSolicitud
	 * @return lista de otros profesionales
	 * @throws IPSException
	 * @author jeilones
	 * @created 9/07/2013
	 */
	private List<ProfesionalHQxDto> consultarOtrosProfesionalesInfoQx(int numeroSolicitud) throws IPSException{
		Connection connection=null;
		List<ProfesionalHQxDto>otrosProfesionales=new ArrayList<ProfesionalHQxDto>(0);
		try{
			connection=UtilidadBD.abrirConexion();
			otrosProfesionales=HojaQuirurgica.consultarOtrosProfesionalesInfoQx(connection, numeroSolicitud);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return otrosProfesionales;
	}
	
	@Override
	public List<TipoProfesionalDto> consultarTiposProfesionales(int codigoInstitucion) throws IPSException{
		List<TipoProfesionalDto>tiposProfesional=new ArrayList<TipoProfesionalDto>(0);
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			tiposProfesional=HojaQuirurgica.consultarTiposProfesionales(connection, codigoInstitucion);
		}catch (BDException e) {
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return tiposProfesional;
	}

	@Override
	public HashMap<?, ?> estaSalaOcupada(int numSol, int codSala, String fechaIni,String horaIni, String fechaFin, String horaFin) throws BDException{
		Connection con=null;
		HashMap<?, ?> result=null;
		try{
			con=UtilidadBD.abrirConexion();
			result= HojaQuirurgica.estaSalaOcupada(con, numSol, codSala, fechaIni, horaIni, fechaFin, horaFin);
			UtilidadBD.finalizarTransaccion(con);
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return result;
	}
	
	@Override
	public List<CargoSolicitudDto> consultarEstadoCargosSolicitud(int numeroSolicitud) throws BDException{
		Connection con=null;
		List<CargoSolicitudDto> listaCargosSolicitud=null;
		try{
			con=UtilidadBD.abrirConexion();
			listaCargosSolicitud= HojaQuirurgica.consultarEstadoCargosSolicitud(con, numeroSolicitud);
			UtilidadBD.finalizarTransaccion(con);
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return listaCargosSolicitud;
	}
	
	@Override 
	public ProgramacionPeticionQxDto consultarProgramacionPeticionQx(int codigoPeticion) throws BDException{
		Connection con=null;
		ProgramacionPeticionQxDto programacionPeticionQxDto=null;
		try{
			con=UtilidadBD.abrirConexion();
			programacionPeticionQxDto= HojaQuirurgica.consultarProgramacionPeticionQx(con, codigoPeticion);
			UtilidadBD.finalizarTransaccion(con);
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return programacionPeticionQxDto;
	}
		
	@Override
	public String consultarNaturalezaServicio(int codigoServicio) throws IPSException{
		Connection connection= null;
		String codigoNaturaleza=null;
		try{
			connection=UtilidadBD.abrirConexion();
		
			codigoNaturaleza=Utilidades.obtenerNaturalezaServicio(connection, codigoServicio+"");
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return codigoNaturaleza;
	}
	
	/**
	 * Consulta el ultimo tipo de anestesia registrado en la hoja de anestesia, 
	 * en caso que la hoja de anestesia no tenga guardado algun tipo de anestesia se retorna 
	 * una instancia de tipo de anestesia sin informacion.
	 * 
	 * @param numeroSolicitud
	 * @return tipo anestesia
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	private TipoAnestesiaDto consultarUltimoTipoAnestesiaHojaAnestesia(int numeroSolicitud) throws IPSException{
		TipoAnestesiaDto tipoAnestesiaDto=new TipoAnestesiaDto();
		Connection connection=null;
		try{
			tipoAnestesiaDto.setCodigo(ConstantesBD.codigoNuncaValido);
			connection=UtilidadBD.abrirConexion();
			List<TipoAnestesiaDto>tiposAnestesia=TecnicaAnestesia.consultarTecnicasAnestesiaXSolicitud(connection, numeroSolicitud);
			if(!tiposAnestesia.isEmpty()){
				tipoAnestesiaDto=tiposAnestesia.get(0);
			}
				
		}catch (IPSException e) {
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(connection);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return tipoAnestesiaDto;
	}
	
	
	/**
	 * Metodo que consulta los datos de la hoja de anestesia relacionada a un numero de solicitud
	 * @param dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	public HojaAnestesiaDto consultarHojaAnestesia(int numeroSolicitud) throws IPSException{

		HojaAnestesia hojaAnestesia = new HojaAnestesia();
		HojaAnestesiaDto hojaAnestesiaDto = new HojaAnestesiaDto();
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			hojaAnestesiaDto=hojaAnestesia.consultarHojaAnestesia(con, numeroSolicitud);		
		}catch (IPSException e) {
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		return hojaAnestesiaDto;
	}
	
	@Override
	public void guardarProfesionalesInformeQx(PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico, int codigoIngreso) throws IPSException{
		Connection connection=null;
		try{
			connection=UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(connection);
			HojaQuirurgica.guardarProfesionalesInformeQx(connection, peticionQxDto, informeQxDto, usuarioBasico);
			
			IngresoSalidaPacienteDto ingresoSalidaPacienteDto=HojaQuirurgica.consultarIngresoSalidaPaciente(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
			boolean esFinalizadaHQx=ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica();
			cambiarEstadosSolicitudHQx(connection, peticionQxDto, esFinalizadaHQx, usuarioBasico, codigoIngreso);
			
			UtilidadBD.finalizarTransaccion(connection);
		}catch (BDException e) {
			UtilidadBD.abortarTransaccion(connection);
			throw e;
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(connection);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
	}
	
	/**
	 * Cambia el estado de una solicitud segun flujo DCU 175 al momento de guardar la HQx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param finalizaHQx
	 * @param usuarioModifica
	 * @param codigoIngreso
	 * @throws IPSException
	 * @author jeilones
	 * @created 29/07/2013
	 */
	private void cambiarEstadosSolicitudHQx(Connection connection, PeticionQxDto peticionQxDto,boolean finalizaHQx, UsuarioBasico usuarioModifica, int codigoIngreso) throws IPSException  {
		boolean requiereInterpretacion=false;
		 int institucion=usuarioModifica.getCodigoInstitucionInt();
		 
		List<EspecialidadDto> especialidades=this.consultarEspecialidadesInformeQx(peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
		
		String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		if(UtilidadTexto.isEmpty(codigoTarifario)){
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		for (EspecialidadDto especialidadDto : especialidades) {
			List<ServicioHQxDto> servicios=HojaQuirurgica.consultarServiciosPeticion(connection, peticionQxDto, especialidadDto, codigoTarifario, false, false);
			for (ServicioHQxDto servicioHQxDto : servicios) {
				if(servicioHQxDto.isRequiereInterpretacion()){
					requiereInterpretacion=true;
					break;
				}
			}
		}
		
		if(!finalizaHQx||(finalizaHQx&&requiereInterpretacion)){
			HojaQuirurgica.cambiarEstadoSolicitud(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), ConstantesBD.codigoEstadoHCRespondida, null);
			HojaQuirurgica.cambiarEstadoPeticion(connection, peticionQxDto.getCodigoPeticion(), ConstantesBD.codigoEstadoPeticionAtendida);
		}else{
			if(!requiereInterpretacion){
				InformacionActoQxDto informacionActoQxDto=this.consultarInformacionActoQx(peticionQxDto.getSolicitudCirugia(), codigoIngreso);
				if(!informacionActoQxDto.getParticipaAnestesiologo()){
					HojaQuirurgica.cambiarEstadoSolicitud(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), ConstantesBD.codigoEstadoHCInterpretada, usuarioModifica);
					HojaQuirurgica.cambiarEstadoPeticion(connection, peticionQxDto.getCodigoPeticion(), ConstantesBD.codigoEstadoPeticionAtendida);
				}else{
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(institucion))){
						boolean finalizadaHA=HojaAnestesia.esFinalizadaHojaAnestesia(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
						if(finalizadaHA){
							HojaQuirurgica.cambiarEstadoSolicitud(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), ConstantesBD.codigoEstadoHCInterpretada, usuarioModifica);
							HojaQuirurgica.cambiarEstadoPeticion(connection, peticionQxDto.getCodigoPeticion(), ConstantesBD.codigoEstadoPeticionAtendida);
						}else{
							HojaQuirurgica.cambiarEstadoSolicitud(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), ConstantesBD.codigoEstadoHCRespondida, null);
							HojaQuirurgica.cambiarEstadoPeticion(connection, peticionQxDto.getCodigoPeticion(), ConstantesBD.codigoEstadoPeticionAtendida);
						}
					}else{
						HojaQuirurgica.cambiarEstadoSolicitud(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), ConstantesBD.codigoEstadoHCInterpretada, usuarioModifica);
						HojaQuirurgica.cambiarEstadoPeticion(connection, peticionQxDto.getCodigoPeticion(), ConstantesBD.codigoEstadoPeticionAtendida);
					}
				}
			}
		}
	}

	@Override
	public String getNombreDiagnostico(String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws IPSException 
	{
		String nombreDiagnostico = null;
		Connection connection=null;
		try{
			connection = UtilidadBD.abrirConexion();
			nombreDiagnostico = HojaQuirurgica.getNombreDiagnostico(connection, acronimoDiagnostico, tipoCieDiagnosticoInt);
		}catch (BDException e){
			throw e;
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		
		return nombreDiagnostico;
	}
	
	@Override
	public boolean requiereJustificacioServicio(int codigoConvenio) throws IPSException{
		boolean requiereJustificacion= false;
		Connection connection=null;
		try{
			connection = UtilidadBD.abrirConexion();
			requiereJustificacion=UtilidadesFacturacion.requiereJustificacioServ(connection, codigoConvenio);
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return requiereJustificacion;
	}
	
	@Override
	public boolean validarEspecialidadProfesionalSalud(UsuarioBasico usuario) throws IPSException{
		boolean cumpleEspecialidadProfesionalSalud= false;
		Connection connection=null;
		try{
			connection = UtilidadBD.abrirConexion();
			cumpleEspecialidadProfesionalSalud=UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(connection, usuario, true);
		}catch (Exception e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(null, null, connection);
		}
		return cumpleEspecialidadProfesionalSalud;
	}
}
