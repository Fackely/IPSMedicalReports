package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;

public class DtoEvolucion implements Serializable
{

	
	private String codigoEvolucion;
	
	private String valoracionAsociada;
	
	private String fechaGrabacion;
	
	private String horaGrabacion;
	
	private String fechaEvolucion;
	
	private String horaEvolucion;
	
	private String informacionDadaPaciente; // Datos subjetivos.
	
	private String descComplicacion; // Analisis.
	
	private String tratamiento;
	
	private String resultadosTratamiento;
	
	private String cambiosManejo;
	
	private String hallazgosImportantes;
	
	private String procedQuirurgicosObst;//////Tipo Monitoreo.
	
	private String resultadoExamenesDiag;
	
	private String pronostico; // Plan Manejo.
	
	private String observaciones;
	
	private UsuarioBasico profesional;
	
	private boolean ordenSalida;
	
	private InfoDatosInt tipoEvolucion;
	
	private String recargo;
	
	private boolean cobrable;
	
	private boolean imprimirDiagnostico;
	
	private InfoDatosInt tipoDiagnosticoPrincipal;
	
	private String datosMedico;
	
	private InfoDatosInt centroCosto;
	
	private InfoDatosInt conductaSeguir;
	
	private String tipoReferencia;
	
	private String diagnosticoComplicacion;
	
	private String diagnosticoComplicacionCie;
	
	private String nombreDiagnosticoComplicacion;
	
	private String observacionesIncapacidad;
	
	private String diasIncapacidad;
	
	private String muerto;
	
	
	// Egreso
	private InfoDatosInt destinoSalida;
	
	private String otroDestinoSalida;
	
	private boolean estadoSalida;
	
	private String fechaEgreso;
	
	private String horaEgreso;
	
	private String fechaGrabacionEgreso;
	
	private String horaGrabacionEgreso;
	
	//private String diagnosticoMuerte;
	
	private String diagnosticoMuerteCie;
	
	private String certificadoDefuncion;
	
	private String fechaMuerte;
	
	private String horaMuerte;
	
	
	
	// Evoluciones Hospitalarias.
	private boolean datosSubjetivos;
	
	private boolean datosObjetivos;
	
	private boolean hallazgosEpicrisis;
	
	private boolean analisis;
	
	private boolean diagnosticosDefinitivos;
	
	private boolean balanceLiquidosEpicrisis;
	
	private boolean planManejo;
	
	private boolean epicrisis;
	
	
	//Diagnosticos
	private Diagnostico diagnosticoPrincipal;
	
	private Diagnostico diagnosticoComplicacion1;
	
	private Diagnostico diagnosticoMuerte;
	
	private Diagnostico diagnosticoIngreso;
	
	
	private ArrayList<Diagnostico> diagnosticos;
	
	private ArrayList<SignoVital> signosVitales;
	
	private ArrayList<DtoBalanceLiquidos> balanceLiquidos;
	
	private ArrayList<DtoEvolucionComentarios> comentarios;
	
	/////
	private int centroCostoMonitoreo;
	
	private int codigoCuenta ;
	
	
	private String especialidadProfResponde;
	
	/**
	 * MT 5568
	 */
	private InformacionCentroCostoValoracionDto infoCentroCostoValoracion;
	/**
	 * Fin MT 5568
	 */
	
	/**
	 * 
	 *
	 */
	public void clean()
	{
		
		this.codigoEvolucion="";
		this.fechaGrabacion="";
		this.horaGrabacion="";
		this.fechaEvolucion=UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual());
		this.horaEvolucion=UtilidadFecha.getHoraActual();
		this.informacionDadaPaciente="";
		this.descComplicacion="";
		this.tratamiento="";
		this.resultadosTratamiento="";
		this.cambiosManejo="";
		this.hallazgosImportantes="";
		this.procedQuirurgicosObst="";
		this.resultadoExamenesDiag="";
		this.pronostico="";
		this.observaciones="";
		this.profesional= new UsuarioBasico();
		this.especialidadProfResponde="";
		this.ordenSalida=false;
		this.recargo="";
		this.cobrable=false;
		this.muerto="";
		this.datosMedico="";
		this.tipoReferencia="";
		this.valoracionAsociada="";
		this.tipoEvolucion= new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tipoDiagnosticoPrincipal= new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.centroCosto= new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.conductaSeguir= new InfoDatosInt(ConstantesBD.codigoNuncaValido, "");
		this.diagnosticoComplicacion="";
		this.diagnosticoComplicacionCie="";
		this.nombreDiagnosticoComplicacion="";
		this.imprimirDiagnostico=false;
		
		this.destinoSalida= new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.otroDestinoSalida="";
		this.estadoSalida=false;
		this.fechaEgreso="";
		this.horaEgreso="";
		this.fechaGrabacionEgreso="";
		this.horaGrabacionEgreso="";
		this.diagnosticoMuerte=new Diagnostico();
		this.diagnosticoMuerteCie="";
		this.certificadoDefuncion="";
		this.fechaMuerte="";
		this.horaMuerte="";
		
		this.observacionesIncapacidad="";
		this.diasIncapacidad="";
		
		this.datosSubjetivos=false;
		this.datosObjetivos=false;
		this.hallazgosEpicrisis=false;
		this.analisis=false;
		this.diagnosticosDefinitivos=false;
		this.balanceLiquidosEpicrisis=false;
		this.planManejo=false;
		this.epicrisis=false;
		
		this.diagnosticos= new ArrayList<Diagnostico>();
		this.signosVitales= new ArrayList<SignoVital>();
		
		this.diagnosticoPrincipal= new Diagnostico();
		this.diagnosticoComplicacion1= new Diagnostico();
		this.diagnosticoIngreso = new Diagnostico();
		
		this.centroCostoMonitoreo=ConstantesBD.codigoNuncaValido;
		this.codigoCuenta = ConstantesBD.codigoNuncaValido;
		this.comentarios = new ArrayList<DtoEvolucionComentarios>();
		this.infoCentroCostoValoracion = new InformacionCentroCostoValoracionDto();
	}
	
	
	/**
	 * 
	 *
	 */
	public DtoEvolucion()
	{
		this.clean();
	}

	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoBalanceLiquidos> getBalanceLiquidos() {
		return balanceLiquidos;
	}

	/**
	 * 
	 * @param balanceLiquidos
	 */
	public void setBalanceLiquidos(ArrayList<DtoBalanceLiquidos> balanceLiquidos) {
		this.balanceLiquidos = balanceLiquidos;
	}

	/**
	 * 
	 * @return
	 */
	public String getCambiosManejo() {
		return cambiosManejo;
	}

	/**
	 * 
	 * @param cambiosManejo
	 */
	public void setCambiosManejo(String cambiosManejo) {
		this.cambiosManejo = cambiosManejo;
	}

	/**
	 * 
	 * @return
	 */	
	public int getCodigoCentroCosto() {
		return centroCosto.getCodigo();
	}

	/**
	 * 
	 * @param centroCosto
	 */
	public void setCodigoCentroCosto(int centroCosto) {
		this.centroCosto.setCodigo(centroCosto);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreCentroCosto() {
		return centroCosto.getNombre();
	}

	/**
	 * 
	 * @param centroCosto
	 */
	public void setNombreCentroCosto(String centroCosto) {
		this.centroCosto.setNombre(centroCosto);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isCobrable() {
		return cobrable;
	}

	/**
	 * 
	 * @param cobrable
	 */
	public void setCobrable(boolean cobrable) {
		this.cobrable = cobrable;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoEvolucion() {
		return codigoEvolucion;
	}

	/**
	 * 
	 * @param codigoEvolucion
	 */
	public void setCodigoEvolucion(String codigoEvolucion) {
		this.codigoEvolucion = codigoEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getDatosMedico() {
		return datosMedico;
	}

	/**
	 * 
	 * @param datosMedico
	 */
	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescComplicacion() {
		return descComplicacion;
	}

	/**
	 * 
	 * @param descComplicacion
	 */
	public void setDescComplicacion(String descComplicacion) {
		this.descComplicacion = descComplicacion;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Diagnostico> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * 
	 * @param diagnosticos
	 */
	public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaEvolucion() {
		return fechaEvolucion;
	}

	/**
	 * 
	 * @param fechaEvolucion
	 */
	public void setFechaEvolucion(String fechaEvolucion) {
		this.fechaEvolucion = fechaEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}

	/**
	 * 
	 * @param fechaGrabacion
	 */
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getHallazgosImportantes() {
		return hallazgosImportantes;
	}

	/**
	 * 
	 * @param hallazgosImportantes
	 */
	public void setHallazgosImportantes(String hallazgosImportantes) {
		this.hallazgosImportantes = hallazgosImportantes;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraEvolucion() {
		return horaEvolucion;
	}

	/**
	 * 
	 * @param horaEvolucion
	 */
	public void setHoraEvolucion(String horaEvolucion) {
		this.horaEvolucion = horaEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	/**
	 * 
	 * @param horaGrabacion
	 */
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getInformacionDadaPaciente() {
		return informacionDadaPaciente;
	}

	/**
	 * 
	 * @param informacionDadaPaciente
	 */
	public void setInformacionDadaPaciente(String informacionDadaPaciente) {
		this.informacionDadaPaciente = informacionDadaPaciente;
	}

	/**
	 * 
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOrdenSalida() {
		return ordenSalida;
	}

	/**
	 * 
	 * @param ordenSalida
	 */
	public void setOrdenSalida(boolean ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

	/**
	 * 
	 * @return
	 */
	public String getProcedQuirurgicosObst() {
		return procedQuirurgicosObst;
	}

	/**
	 * 
	 * @param procedQuirurgicosObst
	 */
	public void setProcedQuirurgicosObst(String procedQuirurgicosObst) {
		this.procedQuirurgicosObst = procedQuirurgicosObst;
	}

	/**
	 * 
	 * @return
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}

	/**
	 * 
	 * @param profesional
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}

	/**
	 * 
	 * @return
	 */
	public String getPronostico() {
		return pronostico;
	}

	/**
	 * 
	 * @param pronostico
	 */
	public void setPronostico(String pronostico) {
		this.pronostico = pronostico;
	}

	/**
	 * 
	 * @return
	 */
	public String getRecargo() {
		return recargo;
	}

	/**
	 * 
	 * @param recargo
	 */
	public void setRecargo(String recargo) {
		this.recargo = recargo;
	}

	/**
	 * 
	 * @return
	 */
	public String getResultadoExamenesDiag() {
		return resultadoExamenesDiag;
	}

	/**
	 * 
	 * @param resultadoExamenesDiag
	 */
	public void setResultadoExamenesDiag(String resultadoExamenesDiag) {
		this.resultadoExamenesDiag = resultadoExamenesDiag;
	}

	/**
	 * 
	 * @return
	 */
	public String getResultadosTratamiento() {
		return resultadosTratamiento;
	}

	/**
	 * 
	 * @param resultadosTratamiento
	 */
	public void setResultadosTratamiento(String resultadosTratamiento) {
		this.resultadosTratamiento = resultadosTratamiento;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<SignoVital> getSignosVitales() {
		return signosVitales;
	}
	
	/**
	 * 
	 * @param signosVitales
	 */
	public void setSignosVitales(ArrayList<SignoVital> signosVitales) {
		this.signosVitales = signosVitales;
	}
	
	/**
	 * 
	 * @return
	 */
	public InfoDatosInt getTipoEvolucion() {
		return tipoEvolucion;
	}

	/**
	 * 
	 * @param tipoEvolucion
	 */
	public void setTipoEvolucion(InfoDatosInt tipoEvolucion) {
		this.tipoEvolucion = tipoEvolucion;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodigoTipoEvolucion() {
		return tipoEvolucion.getCodigo();
	}

	/**
	 * 
	 * @param tipoEvolucion
	 */
	public void setCodigoTipoEvolucion(int tipoEvolucion) {
		this.tipoEvolucion.setCodigo(tipoEvolucion);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreTipoEvolucion() {
		return tipoEvolucion.getNombre();
	}

	/**
	 * 
	 * @param tipoEvolucion
	 */
	public void setNombreTipoEvolucion(String tipoEvolucion) {
		this.tipoEvolucion.setNombre(tipoEvolucion);
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoConductaSeguir() {
		return conductaSeguir.getCodigo();
	}
	
	/**
	 * 
	 * @param conductaSeguir
	 */
	public void setCodigoConductaSeguir(int conductaSeguir) {
		this.conductaSeguir.setCodigo(conductaSeguir);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreConductaSeguir() {
		return conductaSeguir.getNombre();
	}
	
	/**
	 * 
	 * @param conductaSeguir
	 */
	public void setNombreConductaSeguir(String conductaSeguir) {
		this.conductaSeguir.setNombre(conductaSeguir);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodigoTipoDiagnosticoPrincipal() {
		return tipoDiagnosticoPrincipal.getCodigo();
	}
	
	/**
	 * 
	 * @param tipoDiagnosticoPrincipal
	 */
	public void setCodigoTipoDiagnosticoPrincipal(int tipoDiagnosticoPrincipal) {
		this.tipoDiagnosticoPrincipal.setCodigo(tipoDiagnosticoPrincipal);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreTipoDiagnosticoPrincipal() {
		return tipoDiagnosticoPrincipal.getNombre();
	}
	
	/**
	 * 
	 * @param tipoDiagnosticoPrincipal
	 */
	public void setNombreTipoDiagnosticoPrincipal(String tipoDiagnosticoPrincipal) {
		this.tipoDiagnosticoPrincipal.setNombre(tipoDiagnosticoPrincipal);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodigoDestinoSalida() {
		return destinoSalida.getCodigo();
	}
	
	/**
	 * 
	 * @param destinoSalida
	 */
	public void setCodigoDestinoSalida(int destinoSalida) {
		this.destinoSalida.setCodigo(destinoSalida);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreDestinoSalida() {
		return destinoSalida.getNombre();
	}
	
	/**
	 * 
	 * @param destinoSalida
	 */
	public void setNombreDestinoSalida(String destinoSalida) {
		this.destinoSalida.setNombre(destinoSalida);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipoReferencia() {
		return tipoReferencia;
	}

	/**
	 * 
	 * @param tipoReferencia
	 */
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	/**
	 * 
	 * @return
	 */
	public String getTratamiento() {
		return tratamiento;
	}

	/**
	 * 
	 * @param tratamiento
	 */
	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	/**
	 * 
	 * @return
	 */
	public String getValoracionAsociada() {
		return valoracionAsociada;
	}

	/**
	 * 
	 * @param valoracionAsociada
	 */
	public void setValoracionAsociada(String valoracionAsociada) {
		this.valoracionAsociada = valoracionAsociada;
	}

	
	/**
	 * 
	 * @return
	 */
	public String getDiagnosticoMuerteCie() {
		return diagnosticoMuerteCie;
	}

	/**
	 * 
	 * @param diagnosticoMuerteCie
	 */
	public void setDiagnosticoMuerteCie(String diagnosticoMuerteCie) {
		this.diagnosticoMuerteCie = diagnosticoMuerteCie;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEstadoSalida() {
		return estadoSalida;
	}

	/**
	 * 
	 * @param estadoSalida
	 */
	public void setEstadoSalida(boolean estadoSalida) {
		this.estadoSalida = estadoSalida;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}

	/**
	 * 
	 * @param fechaEgreso
	 */
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaGrabacionEgreso() {
		return fechaGrabacionEgreso;
	}

	/**
	 * 
	 * @param fechaGrabacionEgreso
	 */
	public void setFechaGrabacionEgreso(String fechaGrabacionEgreso) {
		this.fechaGrabacionEgreso = fechaGrabacionEgreso;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}

	/**
	 * 
	 * @param horaEgreso
	 */
	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraGrabacionEgreso() {
		return horaGrabacionEgreso;
	}

	/**
	 * 
	 * @param horaGrabacionEgreso
	 */
	public void setHoraGrabacionEgreso(String horaGrabacionEgreso) {
		this.horaGrabacionEgreso = horaGrabacionEgreso;
	}

	/**
	 * 
	 * @return
	 */
	public String getOtroDestinoSalida() {
		return otroDestinoSalida;
	}

	/**
	 * 
	 * @param otroDestinoSalida
	 */
	public void setOtroDestinoSalida(String otroDestinoSalida) {
		this.otroDestinoSalida = otroDestinoSalida;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiagnosticoComplicacion() {
		return diagnosticoComplicacion;
	}

	/**
	 * 
	 * @param diagnosticoComplicacion
	 */
	public void setDiagnosticoComplicacion(String diagnosticoComplicacion) {
		this.diagnosticoComplicacion = diagnosticoComplicacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiagnosticoComplicacionCie() {
		return diagnosticoComplicacionCie;
	}

	/**
	 * 
	 * @param diagnosticoComplicacionCie
	 */
	public void setDiagnosticoComplicacionCie(String diagnosticoComplicacionCie) {
		this.diagnosticoComplicacionCie = diagnosticoComplicacionCie;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombreDiagnosticoComplicacion() {
		return nombreDiagnosticoComplicacion;
	}

	/**
	 * 
	 * @param nombreDiagnosticoComplicacion
	 */
	public void setNombreDiagnosticoComplicacion(
			String nombreDiagnosticoComplicacion) {
		this.nombreDiagnosticoComplicacion = nombreDiagnosticoComplicacion;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAnalisis() {
		return analisis;
	}

	/**
	 * 
	 * @param analisis
	 */
	public void setAnalisis(boolean analisis) {
		this.analisis = analisis;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBalanceLiquidosEpicrisis() {
		return balanceLiquidosEpicrisis;
	}

	/**
	 * 
	 * @param balanceLiquidosEpicrisis
	 */
	public void setBalanceLiquidosEpicrisis(boolean balanceLiquidosEpicrisis) {
		this.balanceLiquidosEpicrisis = balanceLiquidosEpicrisis;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDatosObjetivos() {
		return datosObjetivos;
	}

	/**
	 * 
	 * @param datosObjetivos
	 */
	public void setDatosObjetivos(boolean datosObjetivos) {
		this.datosObjetivos = datosObjetivos;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDatosSubjetivos() {
		return datosSubjetivos;
	}

	/**
	 * 
	 * @param datosSubjetivos
	 */
	public void setDatosSubjetivos(boolean datosSubjetivos) {
		this.datosSubjetivos = datosSubjetivos;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * 
	 * @param diagnosticosDefinitivos
	 */
	public void setDiagnosticosDefinitivos(boolean diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEpicrisis() {
		return epicrisis;
	}

	/**
	 * 
	 * @param epicrisis
	 */
	public void setEpicrisis(boolean epicrisis) {
		this.epicrisis = epicrisis;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isHallazgosEpicrisis() {
		return hallazgosEpicrisis;
	}

	/**
	 * 
	 * @param hallazgosEpicrisis
	 */
	public void setHallazgosEpicrisis(boolean hallazgosEpicrisis) {
		this.hallazgosEpicrisis = hallazgosEpicrisis;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPlanManejo() {
		return planManejo;
	}

	/**
	 * 
	 * @param planManejo
	 */
	public void setPlanManejo(boolean planManejo) {
		this.planManejo = planManejo;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isImprimirDiagnostico() {
		return imprimirDiagnostico;
	}

	/**
	 * 
	 * @param imprimirDiagnostico
	 */
	public void setImprimirDiagnostico(boolean imprimirDiagnostico) {
		this.imprimirDiagnostico = imprimirDiagnostico;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiasIncapacidad() {
		return diasIncapacidad;
	}

	/**
	 * 
	 * @param diasIncapacidad
	 */
	public void setDiasIncapacidad(String diasIncapacidad) {
		this.diasIncapacidad = diasIncapacidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getObservacionesIncapacidad() {
		return observacionesIncapacidad;
	}

	/**
	 * 
	 * @param observacionesIncapacidad
	 */
	public void setObservacionesIncapacidad(String observacionesIncapacidad) {
		this.observacionesIncapacidad = observacionesIncapacidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getCertificadoDefuncion() {
		return certificadoDefuncion;
	}

	/**
	 * 
	 * @param certificadoDefuncion
	 */
	public void setCertificadoDefuncion(String certificadoDefuncion) {
		this.certificadoDefuncion = certificadoDefuncion;
	}


	public String getFechaMuerte() {
		return fechaMuerte;
	}

	/**
	 * 
	 * @param fechaMuerte
	 */
	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraMuerte() {
		return horaMuerte;
	}

	/**
	 * 
	 * @param horaMuerte
	 */
	public void setHoraMuerte(String horaMuerte) {
		this.horaMuerte = horaMuerte;
	}

	/**
	 * 
	 * @return
	 */
	public Diagnostico getDiagnosticoPrincipal() {
		return diagnosticoPrincipal;
	}

	/**
	 * 
	 * @param diagnosticoPrincipal
	 */
	public void setDiagnosticoPrincipal(Diagnostico diagnosticoPrincipal) {
		this.diagnosticoPrincipal = diagnosticoPrincipal;
	}

	/**
	 * 
	 * @return
	 */
	public Diagnostico getDiagnosticoComplicacion1() {
		return diagnosticoComplicacion1;
	}

	/**
	 * 
	 * @param diagnosticoComplicacion1
	 */
	public void setDiagnosticoComplicacion1(Diagnostico diagnosticoComplicacion1) {
		this.diagnosticoComplicacion1 = diagnosticoComplicacion1;
	}

	/**
	 * 
	 * @return
	 */
	public Diagnostico getDiagnosticoMuerte() {
		return diagnosticoMuerte;
	}

	/**
	 * 
	 * @param diagnosticoMuerte
	 */
	public void setDiagnosticoMuerte(Diagnostico diagnosticoMuerte) {
		this.diagnosticoMuerte = diagnosticoMuerte;
	}

	/**
	 * 
	 * @return
	 */
	public String getMuerto() {
		return muerto;
	}

	/**
	 * 
	 * @param muerto
	 */
	public void setMuerto(String muerto) {
		this.muerto = muerto;
	}

	/**
	 * 
	 * @return
	 */
	public Diagnostico getDiagnosticoIngreso() {
		return diagnosticoIngreso;
	}

	/**
	 * 
	 * @param diagnosticoIngreso
	 */
	public void setDiagnosticoIngreso(Diagnostico diagnosticoIngreso) {
		this.diagnosticoIngreso = diagnosticoIngreso;
	}


	public int getCentroCostoMonitoreo() {
		return centroCostoMonitoreo;
	}


	public void setCentroCostoMonitoreo(int centroCostoMonitoreo) {
		this.centroCostoMonitoreo = centroCostoMonitoreo;
	}


	/**
	 * @return the codigoCuenta
	 */
	public int getCodigoCuenta() {
		return codigoCuenta;
	}


	/**
	 * @param codigoCuenta the codigoCuenta to set
	 */
	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoEvolucionComentarios> getComentarios() {
		return comentarios;
	}

	/**
	 * 
	 * @param comentarios
	 */
	public void setComentarios(ArrayList<DtoEvolucionComentarios> comentarios) {
		this.comentarios = comentarios;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComentariosTotales(String saltoLinea) 
	{
		String totalComentarios="";
		for(int w=0; w<this.comentarios.size(); w++)
		{	
			totalComentarios+=this.getComentarios().get(w).getValor()+saltoLinea;
		}
		return totalComentarios+saltoLinea;
	}


	/**
	 * @return the especialidadProfResponde
	 */
	public String getEspecialidadProfResponde() {
		return especialidadProfResponde;
	}


	/**
	 * @param especialidadProfResponde the especialidadProfResponde to set
	 */
	public void setEspecialidadProfResponde(String especialidadProfResponde) {
		this.especialidadProfResponde = especialidadProfResponde;
	}


	/**
	 * @return the infoCentroCostoValoracion
	 */
	public InformacionCentroCostoValoracionDto getInfoCentroCostoValoracion() {
		return infoCentroCostoValoracion;
	}


	/**
	 * @param infoCentroCostoValoracion the infoCentroCostoValoracion to set
	 */
	public void setInfoCentroCostoValoracion(InformacionCentroCostoValoracionDto infoCentroCostoValoracion) {
		this.infoCentroCostoValoracion = infoCentroCostoValoracion;
	}
	
}
