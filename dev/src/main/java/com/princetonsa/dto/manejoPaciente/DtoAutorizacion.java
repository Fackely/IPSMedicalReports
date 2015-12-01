/*
 * Abril 21, 2009
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * DTO Implementado para generar solicitud/envío de autorizacion
 * @author Sebastián Gomez 
 * 
 */
public class DtoAutorizacion implements Serializable
{
	private String codigoPK;
	private String consecutivo;
	private String anioConsecutivo;
	private String idIngreso;
	private String idSubCuenta;
	private InfoDatosInt tipoServicioSolicitado;
	private String observaciones;
	private String nuevaObservacion;
	private String estado;
	private String colorEstado;
	private InfoDatosInt tipoCobertura;
	private InfoDatosInt origenAtencion;
	private String fechaSolicitud;
	private String horaSolicitud;
	private UsuarioBasico usuarioSolicitud;
	private String tipoTramite;
	private InfoDatosInt convenio;
	private String activo;
	private String prioridadAtencion;
	private InfoDatosInt viaIngreso;
	private String idCuenta;
	private InfoDatosInt cama;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	private InfoDatosInt personaSolicita;
	private String formatoPresenSolEnvioXConve;
	private String codigoServicioAE;
	private String nombreServicioAE;
	private String cantidadServicioAE;
	private String fechaAdmision;
	private String horaAdmision;
	
	//Atributos para el manejo de validacion
	/**
	 * Me indica si la cobertura ya viene de la informacion del ingreso del paciente
	 */
	private boolean coberturaSaludResponsable;
	
	/**
	 * Me indica si la cuenta ya tiene el origen de la atencion guardada y no se necesita capturar
	 */
	private boolean origenAtencionGuardada;
	
	/**
	 * Me indica si puedo modificar diagnosticos
	 */
	private boolean puedoModificarDiagnosticos;
	
	/**
	 * Me indica si puedo modificar la informacion de la solicitud
	 * */
	private boolean puedoModificarSolicitud;	
	
	/**
	 * Detalle de las autorizaciones
	 */
	private ArrayList<DtoDetAutorizacion> detalle;

	/**
	 * Envios de una solicitud
	 */
	private DtoEnvioAutorizacion envioSolicitud;	
	
	
	/**
	 * aDJUNTOS de la autorizacion
	 */
	private ArrayList<DtoAdjAutorizacion> adjuntos;
	
	/**
	 * diagnósticos de la autorizacion
	 */
	private ArrayList<DtoDiagAutorizacion> diagnosticos;
	
	/**
	 * Mapa para almacenar los diagnósticos y poderlos manejar en la JSP
	 * junto con la busqueda genérica de diagnósticos
	 */
	private HashMap<String, Object> mapaDiagnosticos;
	
	/**
	 * 
	 * */
	private DtoCuentaAutorizacion cuentaAuto;
	
	/**
	 * 
	 */
	private String tipo;
	/**
	 * Campo para saber si el convenio de la autorizacion sí requiere la autorizacfion
	 */
	private boolean requiereAutorizacionConvenio;
	/**
	 * Campo para saber si este DTO hacce parte de una autorizacion ya registrada
	 */
	private boolean tieneAutorizacionRegistrada;
	//********************************************************************
	
	
	//********************************************************************
	// Atributos para el Informe Tecnico de Solicitud de Autrorizaciones
	private InfoDatosString convenioInformeTec; 
	
	private String nombreCobertura;
	
	private String nombreAtencio;
	
	private String tipoPaciente;
	
	private String servicioHospitalizacion;
	
	private String camaHospitalizacion;

	private String nombreUsuSol;
	
	private String telefonoUsuSol;
	
	private String telefonoCelUsuSol;
	
	private String nombreCargoUsuSol;
	
	private String impresion;
	
	//********************************************************************
	/**
	 * 
	 */
	private String archivoInconGenerado;
	/**
	 * 
	 */
	private String pathArchivoIncoXml;  
	
	/**
	 * Constructor
	 */
	public DtoAutorizacion()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPK = "";
		this.consecutivo = "";
		this.anioConsecutivo = "";
		this.idIngreso = "";
		this.idSubCuenta = "";
		this.tipoServicioSolicitado = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.observaciones = "";
		this.nuevaObservacion = "";
		this.estado = "";
		this.tipoCobertura = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.origenAtencion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.usuarioSolicitud = new UsuarioBasico();
		this.tipoTramite = "";
		this.convenio = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.activo = "";
		this.prioridadAtencion = "";
		this.viaIngreso = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.idCuenta = "";
		this.cama = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
		this.personaSolicita = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.detalle = new ArrayList<DtoDetAutorizacion>();		
		this.envioSolicitud = new DtoEnvioAutorizacion(); 
		this.adjuntos = new ArrayList<DtoAdjAutorizacion>();
		this.diagnosticos = new ArrayList<DtoDiagAutorizacion>();
		this.mapaDiagnosticos = new HashMap<String, Object>();
		this.tipo = "";
		
		//atributos de validacion
		this.coberturaSaludResponsable = false;
		this.origenAtencionGuardada = false;
		this.puedoModificarDiagnosticos = false;
		this.puedoModificarSolicitud = false;
		this.formatoPresenSolEnvioXConve = ConstantesIntegridadDominio.acronimoAnexo3Res003047;
		this.requiereAutorizacionConvenio = false;
		this.tieneAutorizacionRegistrada = false;
		
		// atributos informe tecnico
		this.nombreCobertura = "";
		this.nombreAtencio = "";
		this.tipoPaciente = "";
		this.servicioHospitalizacion = "";
		this.camaHospitalizacion = "";
		this.convenioInformeTec = new InfoDatosString("","");
		this.nombreUsuSol = "";
		this.telefonoUsuSol = "";
		this.telefonoCelUsuSol = "";
		this.nombreCargoUsuSol = "";
		this.impresion = "" ;
		this.nombreServicioAE = "";
		this.codigoServicioAE = "";
		this.cantidadServicioAE = "";
		this.fechaAdmision = "";
		this.horaAdmision = "";
		
		this.colorEstado = "";		
		this.cuentaAuto = new DtoCuentaAutorizacion();
		
		this.archivoInconGenerado = "";
		this.pathArchivoIncoXml = "";
	}

	/**
	 * @return the viaIngreso
	 */
	public InfoDatosInt getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	
	/**
	 * @return the viaIngreso
	 */
	public int getCodigoViaIngreso() {
		return viaIngreso.getCodigo();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setCodigoViaIngreso(int viaIngreso) {
		this.viaIngreso.setCodigo(viaIngreso);
	}
	
	/**
	 * @return the viaIngreso
	 */
	public String getNombreViaIngreso() {
		return viaIngreso.getNombre();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setNombreViaIngreso(String viaIngreso) {
		this.viaIngreso.setNombre(viaIngreso);
	}

	/**
	 * @return the coberturaSaludResponsable
	 */
	public boolean isCoberturaSaludResponsable() {
		return coberturaSaludResponsable;
	}

	/**
	 * @param coberturaSaludResponsable the coberturaSaludResponsable to set
	 */
	public void setCoberturaSaludResponsable(boolean coberturaSaludResponsable) {
		this.coberturaSaludResponsable = coberturaSaludResponsable;
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the anioConsecutivo
	 */
	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}

	/**
	 * @param anioConsecutivo the anioConsecutivo to set
	 */
	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	/**
	 * @return the idIngreso
	 */
	public String getIdIngreso() {
		return idIngreso;
	}

	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * @return the idSubCuenta
	 */
	public String getIdSubCuenta() {
		return idSubCuenta;
	}

	/**
	 * @param idSubCuenta the idSubCuenta to set
	 */
	public void setIdSubCuenta(String idSubCuenta) {
		this.idSubCuenta = idSubCuenta;
	}

	/**
	 * @return the tipoServicioSolicitado
	 */
	public InfoDatosInt getTipoServicioSolicitado() {
		return tipoServicioSolicitado;
	}

	/**
	 * @param tipoServicioSolicitado the tipoServicioSolicitado to set
	 */
	public void setTipoServicioSolicitado(InfoDatosInt tipoServicioSolicitado) {
		this.tipoServicioSolicitado = tipoServicioSolicitado;
	}
	
	/**
	 * @return the tipoServicioSolicitado
	 */
	public int getCodigoTipoServicioSolicitado() {
		return tipoServicioSolicitado.getCodigo();
	}

	/**
	 * @param tipoServicioSolicitado the tipoServicioSolicitado to set
	 */
	public void setCodigoTipoServicioSolicitado(int tipoServicioSolicitado) {
		this.tipoServicioSolicitado.setCodigo( tipoServicioSolicitado);
	}
	
	/**
	 * @return the tipoServicioSolicitado
	 */
	public String getNombreTipoServicioSolicitado() {
		return tipoServicioSolicitado.getNombre();
	}

	/**
	 * @param tipoServicioSolicitado the tipoServicioSolicitado to set
	 */
	public void setNombreTipoServicioSolicitado(String tipoServicioSolicitado) {
		this.tipoServicioSolicitado.setNombre( tipoServicioSolicitado);
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the tipoCobertura
	 */
	public InfoDatosInt getTipoCobertura() {
		return tipoCobertura;
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setTipoCobertura(InfoDatosInt tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}
	
	/**
	 * @return the tipoCobertura
	 */
	public int getCodigoTipoCobertura() {
		return tipoCobertura.getCodigo();
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setCodigoTipoCobertura(int tipoCobertura) {
		this.tipoCobertura.setCodigo( tipoCobertura);
	}
	
	/**
	 * @return the tipoCobertura
	 */
	public String getNombreTipoCobertura() {
		return tipoCobertura.getNombre();
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setNombreTipoCobertura(String tipoCobertura) {
		this.tipoCobertura.setNombre( tipoCobertura);
	}

	/**
	 * @return the origenAdmision
	 */
	public InfoDatosInt getOrigenAtencion() {
		return origenAtencion;
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setOrigenAtencion(InfoDatosInt origenAtencion) {
		this.origenAtencion = origenAtencion;
	}
	
	/**
	 * @return the origenAdmision
	 */
	public int getCodigoOrigenAtencion() {
		return origenAtencion.getCodigo();
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setCodigoOrigenAtencion(int origenAtencion) {
		this.origenAtencion.setCodigo(origenAtencion);
	}
	
	/**
	 * @return the origenAdmision
	 */
	public String getNombreOrigenAtencion() {
		return origenAtencion.getNombre();
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setNombreOrigenAtencion(String origenAtencion) {
		this.origenAtencion.setNombre(origenAtencion);
	}

	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return the horaSolicitud
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * @param horaSolicitud the horaSolicitud to set
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return the usuarioSolicitud
	 */
	public UsuarioBasico getUsuarioSolicitud() {
		return usuarioSolicitud;
	}

	/**
	 * @param usuarioSolicitud the usuarioSolicitud to set
	 */
	public void setUsuarioSolicitud(UsuarioBasico usuarioSolicitud) {
		this.usuarioSolicitud = usuarioSolicitud;
	}

	/**
	 * @return the tipoTramite
	 */
	public String getTipoTramite() {
		return tipoTramite;
	}

	/**
	 * @param tipoTramite the tipoTramite to set
	 */
	public void setTipoTramite(String tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}
	
	/**
	 * @return the convenio
	 */
	public int getCodigoConvenio() {
		return convenio.getCodigo();
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setCodigoConvenio(int convenio) {
		this.convenio.setCodigo(convenio);
	}
	
	/**
	 * @return the convenio
	 */
	public String getNombreConvenio() {
		return convenio.getNombre();
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setNombreConvenio(String convenio) {
		this.convenio.setNombre(convenio);
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the prioridadAtencion
	 */
	public String getPrioridadAtencion() {
		return prioridadAtencion;
	}

	/**
	 * @param prioridadAtencion the prioridadAtencion to set
	 */
	public void setPrioridadAtencion(String prioridadAtencion) {
		this.prioridadAtencion = prioridadAtencion;
	}

	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return the cama
	 */
	public InfoDatosInt getCama() {
		return cama;
	}

	/**
	 * @param cama the cama to set
	 */
	public void setCama(InfoDatosInt cama) {
		this.cama = cama;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the detalle
	 */
	public ArrayList<DtoDetAutorizacion> getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(ArrayList<DtoDetAutorizacion> detalle) {
		this.detalle = detalle;
	}
	
	/**
	 * @return the adjuntos
	 */
	public ArrayList<DtoAdjAutorizacion> getAdjuntos() {
		return adjuntos;
	}

	/**
	 * @param adjuntos the adjuntos to set
	 */
	public void setAdjuntos(ArrayList<DtoAdjAutorizacion> adjuntos) {
		this.adjuntos = adjuntos;
	}
	
	/**
	 * Método para saber el número de archivos adjuntos de la autorizacion
	 * @return
	 */
	public int getNumAdjuntos()
	{
		return this.adjuntos.size();
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<DtoDiagAutorizacion> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(ArrayList<DtoDiagAutorizacion> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	
	
	/**
	 * Método para verificar si puedo modificar una solicitud
	 * @return
	 */
	public boolean isPuedoModificarSolicitud()
	{
		boolean puedoModificar = false;
		
		//Si no hay estado quiere decir que aún no hay informacuión del anexo
		if(this.estado.equals(""))
			puedoModificar = true;
		
		return puedoModificar;
	}

	/**
	 * @return the origenAtencionGuardada
	 */
	public boolean isOrigenAtencionGuardada() {
		return origenAtencionGuardada;
	}

	/**
	 * @param origenAtencionGuardada the origenAtencionGuardada to set
	 */
	public void setOrigenAtencionGuardada(boolean origenAtencionGuardada) {
		this.origenAtencionGuardada = origenAtencionGuardada;
	}

	/**
	 * @return the mapaDiagnosticos
	 */
	public HashMap<String, Object> getMapaDiagnosticos() {
		return mapaDiagnosticos;
	}

	/**
	 * @param mapaDiagnosticos the mapaDiagnosticos to set
	 */
	public void setMapaDiagnosticos(HashMap<String, Object> mapaDiagnosticos) {
		this.mapaDiagnosticos = mapaDiagnosticos;
	}
	
	/**
	 * @return the mapaDiagnosticos
	 */
	public Object getMapaDiagnosticos(String key) {
		return mapaDiagnosticos.get(key);
	}

	/**
	 * @param mapaDiagnosticos the mapaDiagnosticos to set
	 */
	public void setMapaDiagnosticos(String key, Object value) {
		this.mapaDiagnosticos.put(key, value);
	}
	
	/**
	 * Mapa que retorna el número dediag rel del mapa diagnósticos
	 * @return
	 */
	public int getNumMapaDiagnosticos()
	{
		return Utilidades.convertirAEntero(this.mapaDiagnosticos.get("numRegistros")+"", true);
	}
	
	/**
	 * Mapa que retorna el número dediag rel del mapa diagnósticos
	 * @return
	 */
	public void setNumMapaDiagnosticos(int valor)
	{
		this.mapaDiagnosticos.put("numRegistros",valor);
	}
	
	/**
	 * Método que asigna el número de diagnosticos relacionados del mapa diagnosticos
	 * @param numRegistros
	 */
	public void setNumDiagnosticos(int numRegistros)
	{
		this.mapaDiagnosticos.put("numRegistros",numRegistros);
	}
	
	/**
	 * Método para pasar los diagnósticos del DTO al HashMap
	 */
	public void diagnosticosDtoToHashMap()
	{
		String diagSeleccionados = "";
		for(DtoDiagAutorizacion diagnostico:this.diagnosticos)
		{			
			if(diagnostico.getDiagnostico().isPrincipal())
			{
				this.setMapaDiagnosticos("principal", diagnostico.getDiagnostico().getAcronimo()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getTipoCIE()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getNombre());				
			}
			else
			{
				int numDiag = this.getNumMapaDiagnosticos();
				this.setMapaDiagnosticos(numDiag+"", diagnostico.getDiagnostico().getAcronimo()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getTipoCIE()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getNombre());
				this.setMapaDiagnosticos("checkbox_"+numDiag, "true");
				diagSeleccionados += (diagSeleccionados.equals("")?"":",") + "'" + diagnostico.getDiagnostico().getAcronimo() + "'";
				numDiag++;
				this.setNumDiagnosticos(numDiag);
			}
		}
		this.setMapaDiagnosticos("diagSeleccionados", diagSeleccionados);		
	}
	
	/**
	 * Método para pasar los diagnósticos del HashMap al DTO
	 */
	public void diagnosticosHashMapToDto(String fechaActual,String horaActual, UsuarioBasico usuario)
	{
		boolean encontro = false;
		//*************SE PASA DIAGNOSTICO PRINCIPAL***************************
		if(this.getMapaDiagnosticos("principal")!=null)
		{
			String[] diagPrin = this.getMapaDiagnosticos("principal").toString().split(ConstantesBD.separadorSplit);
			if(diagPrin.length>=3)
			{
				//Se busca el diagnóstico principal
				for(DtoDiagAutorizacion diagnostico:this.diagnosticos)
				{
					if(diagnostico.getDiagnostico().isPrincipal())
					{
						diagnostico.getDiagnostico().setAcronimo(diagPrin[0]);
						diagnostico.getDiagnostico().setTipoCIE(Integer.parseInt(diagPrin[1]));
						diagnostico.getDiagnostico().setNombre(diagPrin[2]);
						diagnostico.setFechaModifica(fechaActual);
						diagnostico.setHoraModifica(horaActual);
						diagnostico.setUsuarioModifica(usuario);
						encontro = true;
					}
				}
				//----------------------------------------------
				//Si no se encontró el diagnostico principal se debe agregar---------
				if(!encontro)
				{
					DtoDiagAutorizacion diagnostico = new DtoDiagAutorizacion();
					diagnostico.getDiagnostico().setAcronimo(diagPrin[0]);
					diagnostico.getDiagnostico().setTipoCIE(Integer.parseInt(diagPrin[1]));
					diagnostico.getDiagnostico().setNombre(diagPrin[2]);
					diagnostico.getDiagnostico().setPrincipal(true);
					diagnostico.setFechaModifica(fechaActual);
					diagnostico.setHoraModifica(horaActual);
					diagnostico.setUsuarioModifica(usuario);
					this.diagnosticos.add(diagnostico);
				}
				//------------------------------------------------------------------
			}
		}
		//**********************************************************************
		//************SE PASAN DIAGNÓSTICOS RELACIONADOS*************************
			
		for(int i=0;i<this.getNumMapaDiagnosticos();i++)
		{
			encontro = false;
			//Se verifica que el diagnostico relacionado exista
			if(this.getMapaDiagnosticos(i+"")!=null)
			{
				
				String[] diagRel = this.getMapaDiagnosticos(i+"").toString().split(ConstantesBD.separadorSplit);
				boolean chequeado = UtilidadTexto.getBoolean(this.getMapaDiagnosticos("checkbox_"+i).toString());

				//Se verifica si el diagnóstico relacionado existe
				for(DtoDiagAutorizacion diagnostico:this.diagnosticos)
				{
					if(!diagnostico.getDiagnostico().isPrincipal()
							&& diagnostico.getDiagnostico().getAcronimo().equals(diagRel[0]))
					{
						encontro = true;
						//Se verifica si se debe eliminar
						if(!chequeado)
						{
							diagnostico.setEliminado(true);
						}
					}
				}
				
				//Si no se encontró el diagnóstico y fue chequeado se agrega---------------------------
				if(!encontro&&chequeado)
				{
					DtoDiagAutorizacion diagnostico = new DtoDiagAutorizacion();
					diagnostico.getDiagnostico().setAcronimo(diagRel[0]);
					diagnostico.getDiagnostico().setTipoCIE(Integer.parseInt(diagRel[1]));
					diagnostico.getDiagnostico().setNombre(diagRel[2]);
					diagnostico.getDiagnostico().setPrincipal(false);
					diagnostico.setFechaModifica(fechaActual);
					diagnostico.setHoraModifica(horaActual);
					diagnostico.setUsuarioModifica(usuario);
					this.diagnosticos.add(diagnostico);
				}
				//_---------------------------------------------------------------------------------------
			}
		}
		//************************************************************************
	}

	/**
	 * @return the puedoModificarDiagnosticos
	 */
	public boolean isPuedoModificarDiagnosticos() {
		return puedoModificarDiagnosticos;
	}

	public void setPuedoModificarDiagnosticos(boolean puedoModificarDiagnosticos) {
		this.puedoModificarDiagnosticos = puedoModificarDiagnosticos;
	}

	public void setPuedoModificarSolicitud(boolean puedoModificarSolicitud) {
		this.puedoModificarSolicitud = puedoModificarSolicitud;
	}
	
	/**
	 * @return the PersonaSolicita
	 */
	public InfoDatosInt getPersonaSolicita() {
		return personaSolicita;
	}

	/**
	 * @param PersonaSolicita the PersonaSolicita to set
	 */
	public void setPersonaSolicita(InfoDatosInt personaSolicita) {
		this.personaSolicita = personaSolicita;
	}
	
	/**
	 * @return the PersonaSolicita
	 */
	public int getCodigoPersonaSolicita() {
		return personaSolicita.getCodigo();
	}

	/**
	 * @param PersonaSolicita the PersonaSolicita to set
	 */
	public void setCodigoPersonaSolicita(int personaSolicita) {
		this.personaSolicita.setCodigo(personaSolicita);
	}
	
	/**
	 * @return the PersonaSolicita
	 */
	public String getNombrePersonaSolicita() {
		return personaSolicita.getNombre();
	}

	/**
	 * @param PersonaSolicita the PersonaSolicita to set
	 */
	public void setNombrePersonaSolicita(String personaSolicita) {
		this.personaSolicita.setNombre(personaSolicita);
	}

	public DtoEnvioAutorizacion getEnvioSolicitud() {
		return envioSolicitud;
	}

	public void setEnvioSolicitud(DtoEnvioAutorizacion envioSolicitud) {
		this.envioSolicitud = envioSolicitud;
	}

	public String getNuevaObservacion() {
		return nuevaObservacion;
	}

	public void setNuevaObservacion(String nuevaObservacion) {
		this.nuevaObservacion = nuevaObservacion;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFormatoPresenSolEnvioXConve() {
		return formatoPresenSolEnvioXConve;
	}

	public void setFormatoPresenSolEnvioXConve(String formatoPresenSolEnvioXConve) {
		this.formatoPresenSolEnvioXConve = formatoPresenSolEnvioXConve;
	}

	/**
	 * @return the requiereAutorizacionConvenio
	 */
	public boolean isRequiereAutorizacionConvenio() {
		return requiereAutorizacionConvenio;
	}

	/**
	 * @param requiereAutorizacionConvenio the requiereAutorizacionConvenio to set
	 */
	public void setRequiereAutorizacionConvenio(boolean requiereAutorizacionConvenio) {
		this.requiereAutorizacionConvenio = requiereAutorizacionConvenio;
	}

	/**
	 * @return the tieneAutorizacionRegistrada
	 */
	public boolean isTieneAutorizacionRegistrada() {
		return tieneAutorizacionRegistrada;
	}

	/**
	 * @param tieneAutorizacionRegistrada the tieneAutorizacionRegistrada to set
	 */
	public void setTieneAutorizacionRegistrada(boolean tieneAutorizacionRegistrada) {
		this.tieneAutorizacionRegistrada = tieneAutorizacionRegistrada;
	}

	/**
	 * @return the nombreCobertura
	 */
	public String getNombreCobertura() {
		return nombreCobertura;
	}

	/**
	 * @param nombreCobertura the nombreCobertura to set
	 */
	public void setNombreCobertura(String nombreCobertura) {
		this.nombreCobertura = nombreCobertura;
	}

	/**
	 * @return the nombreAtencio
	 */
	public String getNombreAtencio() {
		return nombreAtencio;
	}

	/**
	 * @param nombreAtencio the nombreAtencio to set
	 */
	public void setNombreAtencio(String nombreAtencio) {
		this.nombreAtencio = nombreAtencio;
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * @return the servicioHospitalizacion
	 */
	public String getServicioHospitalizacion() {
		return servicioHospitalizacion;
	}

	/**
	 * @param servicioHospitalizacion the servicioHospitalizacion to set
	 */
	public void setServicioHospitalizacion(String servicioHospitalizacion) {
		this.servicioHospitalizacion = servicioHospitalizacion;
	}

	/**
	 * @return the camaHospitalizacion
	 */
	public String getCamaHospitalizacion() {
		return camaHospitalizacion;
	}

	/**
	 * @param camaHospitalizacion the camaHospitalizacion to set
	 */
	public void setCamaHospitalizacion(String camaHospitalizacion) {
		this.camaHospitalizacion = camaHospitalizacion;
	}

	/**
	 * @return the convenioInformeTec
	 */
	public InfoDatosString getConvenioInformeTec() {
		return convenioInformeTec;
	}

	/**
	 * @param convenioInformeTec the convenioInformeTec to set
	 */
	public void setConvenioInformeTec(InfoDatosString convenioInformeTec) {
		this.convenioInformeTec = convenioInformeTec;
	}

	/**
	 * @return the nombreUsuSol
	 */
	public String getNombreUsuSol() {
		return nombreUsuSol;
	}

	/**
	 * @param nombreUsuSol the nombreUsuSol to set
	 */
	public void setNombreUsuSol(String nombreUsuSol) {
		this.nombreUsuSol = nombreUsuSol;
	}

	/**
	 * @return the telefonoUsuSol
	 */
	public String getTelefonoUsuSol() {
		return telefonoUsuSol;
	}

	/**
	 * @param telefonoUsuSol the telefonoUsuSol to set
	 */
	public void setTelefonoUsuSol(String telefonoUsuSol) {
		this.telefonoUsuSol = telefonoUsuSol;
	}

	/**
	 * @return the telefonoCelUsuSol
	 */
	public String getTelefonoCelUsuSol() {
		return telefonoCelUsuSol;
	}

	/**
	 * @param telefonoCelUsuSol the telefonoCelUsuSol to set
	 */
	public void setTelefonoCelUsuSol(String telefonoCelUsuSol) {
		this.telefonoCelUsuSol = telefonoCelUsuSol;
	}

	/**
	 * @return the nombreCargoUsuSol
	 */
	public String getNombreCargoUsuSol() {
		return nombreCargoUsuSol;
	}

	/**
	 * @param nombreCargoUsuSol the nombreCargoUsuSol to set
	 */
	public void setNombreCargoUsuSol(String nombreCargoUsuSol) {
		this.nombreCargoUsuSol = nombreCargoUsuSol;
	}

	/**
	 * @return the impresion
	 */
	public String getImpresion() {
		return impresion;
	}

	/**
	 * @param impresion the impresion to set
	 */
	public void setImpresion(String impresion) {
		this.impresion = impresion;
	}

	public String getCodigoServicioAE() {
		return codigoServicioAE;
	}

	public void setCodigoServicioAE(String codigoServicioAE) {
		this.codigoServicioAE = codigoServicioAE;
	}

	public String getNombreServicioAE() {
		return nombreServicioAE;
	}

	public void setNombreServicioAE(String nombreServicioAE) {
		this.nombreServicioAE = nombreServicioAE;
	}

	public String getCantidadServicioAE() {
		return cantidadServicioAE;
	}

	public void setCantidadServicioAE(String cantidadServicioAE) {
		this.cantidadServicioAE = cantidadServicioAE;
	}

	public String getFechaAdmision() {
		return fechaAdmision;
	}

	public void setFechaAdmision(String fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}

	public String getHoraAdmision() {
		return horaAdmision;
	}

	public void setHoraAdmision(String horaAdmision) {
		this.horaAdmision = horaAdmision;
	}

	public String getColorEstado() {
		return colorEstado;
	}

	public void setColorEstado(String colorEstado) {
		this.colorEstado = colorEstado;
	}

	public DtoCuentaAutorizacion getCuentaAuto() {
		return cuentaAuto;
	}

	public void setCuentaAuto(DtoCuentaAutorizacion cuentaAuto) {
		this.cuentaAuto = cuentaAuto;
	}

	/**
	 * @return the archivoInconGenerado
	 */
	public String getArchivoInconGenerado() {
		return archivoInconGenerado;
	}

	/**
	 * @param archivoInconGenerado the archivoInconGenerado to set
	 */
	public void setArchivoInconGenerado(String archivoInconGenerado) {
		this.archivoInconGenerado = archivoInconGenerado;
	}

	/**
	 * @return the pathArchivoIncoXml
	 */
	public String getPathArchivoIncoXml() {
		return pathArchivoIncoXml;
	}

	/**
	 * @param pathArchivoIncoXml the pathArchivoIncoXml to set
	 */
	public void setPathArchivoIncoXml(String pathArchivoIncoXml) {
		this.pathArchivoIncoXml = pathArchivoIncoXml;
	}
}