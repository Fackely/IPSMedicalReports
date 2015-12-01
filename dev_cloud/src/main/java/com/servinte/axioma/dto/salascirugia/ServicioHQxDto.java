package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class ServicioHQxDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2531267916917623724L;

	private int codigo;
	private boolean esPos;
	
	private String codigoReferenciaServicio;
	private String descripcionReferenciaServicio;
	private EspecialidadDto especialidad;
	
	private int numeroServicio;
	/*NO SE SABE SI LO ESTAN USANDO EN ALGUN LADO*/
	private int codigoServicioXCirugia;
	private String tipoServicio;
	private String observacionesServicioXCirugia;
	private String naturalezaServicio;
	private EspecialidadDto especialidadServicioXCirugia;
	private boolean requiereDiagnostico;
	private boolean requiereInterpretacion;
	/*FIN NO SE SABE SI LO ESTAN USANDO EN ALGUN LADO*/
	
	private int finalidad;
	private TipoViaDto tipoVia;
	private boolean bilateral;
	private boolean viaAcceso;
	
	private int codigoContrato;
	private boolean cubierto;
	private List<ProfesionalHQxDto>profesionalesXServicio;
	
	private List<ProfesionalHQxDto>profesionalesAEliminarse=new ArrayList<ProfesionalHQxDto>(0);
	
	private List<FinalidadDto>finalidades;
	
	private boolean vieneDePeticion;
	
	private int numeroAutorizacion;
	
	/**
	 * JUSTIFICACION NO POS
	 * */
	private String cobertura;
	private String subCuenta;
	private boolean justificar;
	private boolean pendiente;
	private boolean haSidoJustificado;
	/**
	 * FIN JUSTIFICACION NO POS
	 * */
	
	/**
	 * Este servicio debe reemplazar el servicio actual en caso que sea de tipo Parto o Cesarea y se vaya a eliminar este
	 */
	private ServicioHQxDto nuevoServicioParto;
	
	public ServicioHQxDto() {
	}
	
	/**
	 * @return the profesionalesXServicio
	 */
	public List<ProfesionalHQxDto> getProfesionalesXServicio() {
		return profesionalesXServicio;
	}

	/**
	 * @param profesionalesXServicio the profesionalesXServicio to set
	 */
	public void setProfesionalesXServicio(
			List<ProfesionalHQxDto> profesionalesXServicio) {
		this.profesionalesXServicio = profesionalesXServicio;
	}

	/**
	 * Este servicio debe reemplazar el servicio actual en caso que sea de tipo Parto o Cesarea y se vaya a eliminar este
	 * 
	 * @return the nuevoServicioParto
	 */
	public ServicioHQxDto getNuevoServicioParto() {
		return nuevoServicioParto;
	}

	/**
	 * Este servicio debe reemplazar el servicio actual en caso que sea de tipo Parto o Cesarea y se vaya a eliminar este
	 * 
	 * @param nuevoServicioParto the nuevoServicioParto to set
	 */
	public void setNuevoServicioParto(ServicioHQxDto nuevoServicioParto) {
		this.nuevoServicioParto = nuevoServicioParto;
	}

	/**
	 * @return the codigoContrato
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * @return the cubierto
	 */
	public boolean isCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(boolean cubierto) {
		this.cubierto = cubierto;
	}

	public int getFinalidad() {
		return finalidad;
	}
	public void setFinalidad(int finalidad) {
		this.finalidad = finalidad;
	}
	public TipoViaDto getTipoVia() {
		return tipoVia;
	}
	public void setTipoVia(TipoViaDto tipoVia) {
		this.tipoVia = tipoVia;
	}
	public boolean isBilateral() {
		return bilateral;
	}
	public void setBilateral(boolean bilateral) {
		this.bilateral = bilateral;
	}
	public boolean isViaAcceso() {
		return viaAcceso;
	}
	public void setViaAcceso(boolean viaAcceso) {
		this.viaAcceso = viaAcceso;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public boolean isEsPos() {
		return esPos;
	}
	public void setEsPos(boolean esPos) {
		this.esPos = esPos;
	}
	public EspecialidadDto getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(EspecialidadDto especialidad) {
		this.especialidad = especialidad;
	}
	public int getCodigoServicioXCirugia() {
		return codigoServicioXCirugia;
	}
	public void setCodigoServicioXCirugia(int codigoServicioXCirugia) {
		this.codigoServicioXCirugia = codigoServicioXCirugia;
	}
	public String getTipoServicio() {
		return tipoServicio;
	}
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	public String getObservacionesServicioXCirugia() {
		return observacionesServicioXCirugia;
	}
	public void setObservacionesServicioXCirugia(
			String observacionesServicioXCirugia) {
		this.observacionesServicioXCirugia = observacionesServicioXCirugia;
	}
	public String getNaturalezaServicio() {
		return naturalezaServicio;
	}
	public void setNaturalezaServicio(String naturalezaServicio) {
		this.naturalezaServicio = naturalezaServicio;
	}
	public EspecialidadDto getEspecialidadServicioXCirugia() {
		return especialidadServicioXCirugia;
	}
	public void setEspecialidadServicioXCirugia(
			EspecialidadDto especialidadServicioXCirugia) {
		this.especialidadServicioXCirugia = especialidadServicioXCirugia;
	}
	public boolean isRequiereDiagnostico() {
		return requiereDiagnostico;
	}
	public void setRequiereDiagnostico(boolean requiereDiagnostico) {
		this.requiereDiagnostico = requiereDiagnostico;
	}
	public boolean isRequiereInterpretacion() {
		return requiereInterpretacion;
	}
	public void setRequiereInterpretacion(boolean requiereInterpretacion) {
		this.requiereInterpretacion = requiereInterpretacion;
	}
	public int getNumeroServicio() {
		return numeroServicio;
	}
	public void setNumeroServicio(int numeroServicio) {
		this.numeroServicio = numeroServicio;
	}
	public String getCodigoReferenciaServicio() {
		return codigoReferenciaServicio;
	}
	public void setCodigoReferenciaServicio(String codigoReferenciaServicio) {
		this.codigoReferenciaServicio = codigoReferenciaServicio;
	}
	public String getDescripcionReferenciaServicio() {
		return descripcionReferenciaServicio;
	}
	public void setDescripcionReferenciaServicio(
			String descripcionReferenciaServicio) {
		this.descripcionReferenciaServicio = descripcionReferenciaServicio;
	}

	/**
	 * @return the profesionalesAEliminarse
	 */
	public List<ProfesionalHQxDto> getProfesionalesAEliminarse() {
		return profesionalesAEliminarse;
	}

	/**
	 * @param profesionalesAEliminarse the profesionalesAEliminarse to set
	 */
	public void setProfesionalesAEliminarse(
			List<ProfesionalHQxDto> profesionalesAEliminarse) {
		this.profesionalesAEliminarse = profesionalesAEliminarse;
	}

	/**
	 * @return the finalidades
	 */
	public List<FinalidadDto> getFinalidades() {
		return finalidades;
	}

	/**
	 * @param finalidades the finalidades to set
	 */
	public void setFinalidades(List<FinalidadDto> finalidades) {
		this.finalidades = finalidades;
	}
	
	public boolean isVieneDePeticion() {
		return vieneDePeticion;
	}

	public void setVieneDePeticion(boolean vieneDePeticion) {
		this.vieneDePeticion = vieneDePeticion;
	}

	public int getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(int numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * @return the cobertura
	 */
	public String getCobertura() {
		return cobertura;
	}

	/**
	 * @param cobertura the cobertura to set
	 */
	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the haSidoJustificada
	 */
	public boolean isHaSidoJustificado() {
		return haSidoJustificado;
	}

	/**
	 * @param haSidoJustificada the haSidoJustificada to set
	 */
	public void setHaSidoJustificado(boolean haSidoJustificada) {
		this.haSidoJustificado = haSidoJustificada;
	}

	/**
	 * @return the justificar
	 */
	public boolean isJustificar() {
		return justificar;
	}

	/**
	 * @param justificar the justificar to set
	 */
	public void setJustificar(boolean justificar) {
		this.justificar = justificar;
	}

	/**
	 * @return the pendiente
	 */
	public boolean isPendiente() {
		return pendiente;
	}

	/**
	 * @param pendiente the pendiente to set
	 */
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
	
}
