package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.servinte.axioma.orm.PresuFirmasContrato;

/**
 * Contiene la informacion para la impresion del contrato odontologico
 * 
 * @author Cristhian Murillo
 */
public class DtoFormatoImpresionContratoOdontologico implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//--> 1.Sección Encabezado
	private String pathLogoInstitucion;
	private String ubicacionLogoInstitucion;
	private int codigoInstitucion;
	private String razonSocialInstitucion;
	private String nitInstitucion;
	private String actividadEcoInstitucion;
	private String direccionInstitucion;
	private String telefonoInstitucion;
	private int codCentroAtencionPresupuestoContratado;
	private String nomCentroAtencionPresupuestoContratado;
	private String estadoPresupuesto;
	
	//2.Seccin Datos Contrato
	private long codigoPkPresupuesto;
	private long numeroConsecutivoPresupuesto;
	private Long numeroConsecutivoContrato;
	private Date fechaContrato;
	private String valorTotalPresupuesto;
	
	// Datos Paciente del Presupuesto
	private String tipoId;
	private String numeroId;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String pacDireccion;
	private String pacTelefono;
	
	//3.Sección Cuerpo de la Plantilla
	private String  clausulasContrato;

	//4.Anexos Impresos
	private ArrayList<DtoCheckBox> listaAnexosImpresos;
	
	//5.Sección Firmas
	private ArrayList<PresuFirmasContrato> listaPresuFirmasContrato;
	
	//6. Sección Píe de Página: 
	private String piePagina;
	
	// Impresion Recomendaciones
	private ArrayList<DtoRecomendaciones> listaRecomendaciones;
	
	
	
	/**
	 *	Campo utilizado para generar el reporte con jasper report 
	 */
	JRDataSource anexosImpresosDS; 
	
	
	
	public DtoFormatoImpresionContratoOdontologico()
	{
		//--> 1.Sección Encabezado
		this.pathLogoInstitucion="";
		this.ubicacionLogoInstitucion="";
		this.codigoInstitucion=ConstantesBD.codigoNuncaValido;
		this.razonSocialInstitucion="";
		this.nitInstitucion="";
		this.actividadEcoInstitucion="";
		this.direccionInstitucion="";
		this.telefonoInstitucion="";
		this.codCentroAtencionPresupuestoContratado=ConstantesBD.codigoNuncaValido;
		this.nomCentroAtencionPresupuestoContratado="";
		this.estadoPresupuesto = "";

		//2.Sección Datos Contrato
		this.codigoPkPresupuesto=ConstantesBD.codigoNuncaValidoLong;
		this.numeroConsecutivoPresupuesto=ConstantesBD.codigoNuncaValidoLong;
		this.numeroConsecutivoContrato=ConstantesBD.codigoNuncaValidoLong;
		this.fechaContrato =new Date();
		this.valorTotalPresupuesto="";
		
		// pacientePresupuesto: Nombres, Apellidos, Tipo Id, Numero Id, Direccion, Telefono
		this.tipoId="";
		this.numeroId="";
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.pacDireccion="";
		this.pacTelefono="";
		
		//3.Sección Cuerpo de la Plantilla
		this.clausulasContrato="";

		//4.Anexos Impresos
		this.listaAnexosImpresos = new ArrayList<DtoCheckBox>();
		
		//5.Sección Firmas
		this.listaPresuFirmasContrato = new ArrayList<PresuFirmasContrato>();
		
		//6. Sección Píe de Página: 
		this.piePagina="";
		
		// Impresion Recomendaciones
		this.listaRecomendaciones = new ArrayList<DtoRecomendaciones>();
	}


	
	
	
	public String getPathLogoInstitucion() {
		return pathLogoInstitucion;
	}


	public String getUbicacionLogoInstitucion() {
		return ubicacionLogoInstitucion;
	}


	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public String getRazonSocialInstitucion() {
		return razonSocialInstitucion;
	}


	public String getNitInstitucion() {
		return nitInstitucion;
	}


	public String getActividadEcoInstitucion() {
		return actividadEcoInstitucion;
	}


	public String getDireccionInstitucion() {
		return direccionInstitucion;
	}


	public String getTelefonoInstitucion() {
		return telefonoInstitucion;
	}


	public int getCodCentroAtencionPresupuestoContratado() {
		return codCentroAtencionPresupuestoContratado;
	}


	public String getNomCentroAtencionPresupuestoContratado() {
		return nomCentroAtencionPresupuestoContratado;
	}


	public long getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}


	public long getNumeroConsecutivoPresupuesto() {
		return numeroConsecutivoPresupuesto;
	}


	public Long getNumeroConsecutivoContrato() {
		return numeroConsecutivoContrato;
	}


	public Date getFechaContrato() {
		return fechaContrato;
	}


	public String getTipoId() {
		return tipoId;
	}


	public String getNumeroId() {
		return numeroId;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}


	public String getPacDireccion() {
		return pacDireccion;
	}


	public String getPacTelefono() {
		return pacTelefono;
	}


	public String getClausulasContrato() {
		return clausulasContrato;
	}


	public ArrayList<DtoCheckBox> getListaAnexosImpresos() {
		return listaAnexosImpresos;
	}


	public void setPathLogoInstitucion(String pathLogoInstitucion) {
		this.pathLogoInstitucion = pathLogoInstitucion;
	}


	public void setUbicacionLogoInstitucion(String ubicacionLogoInstitucion) {
		this.ubicacionLogoInstitucion = ubicacionLogoInstitucion;
	}


	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public void setRazonSocialInstitucion(String razonSocialInstitucion) {
		this.razonSocialInstitucion = razonSocialInstitucion;
	}


	public void setNitInstitucion(String nitInstitucion) {
		this.nitInstitucion = nitInstitucion;
	}


	public void setActividadEcoInstitucion(String actividadEcoInstitucion) {
		this.actividadEcoInstitucion = actividadEcoInstitucion;
	}


	public void setDireccionInstitucion(String direccionInstitucion) {
		this.direccionInstitucion = direccionInstitucion;
	}


	public void setTelefonoInstitucion(String telefonoInstitucion) {
		this.telefonoInstitucion = telefonoInstitucion;
	}


	public void setCodCentroAtencionPresupuestoContratado(
			int codCentroAtencionPresupuestoContratado) {
		this.codCentroAtencionPresupuestoContratado = codCentroAtencionPresupuestoContratado;
	}


	public void setNomCentroAtencionPresupuestoContratado(
			String nomCentroAtencionPresupuestoContratado) {
		this.nomCentroAtencionPresupuestoContratado = nomCentroAtencionPresupuestoContratado;
	}


	public void setCodigoPkPresupuesto(long codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}


	public void setNumeroConsecutivoPresupuesto(long numeroConsecutivoPresupuesto) {
		this.numeroConsecutivoPresupuesto = numeroConsecutivoPresupuesto;
	}


	public void setNumeroConsecutivoContrato(Long numeroConsecutivoContrato) {
		this.numeroConsecutivoContrato = numeroConsecutivoContrato;
	}


	public void setFechaContrato(Date fechaContrato) {
		this.fechaContrato = fechaContrato;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	public void setPacDireccion(String pacDireccion) {
		this.pacDireccion = pacDireccion;
	}


	public void setPacTelefono(String pacTelefono) {
		this.pacTelefono = pacTelefono;
	}


	public void setClausulasContrato(String clausulasContrato) {
		if(clausulasContrato==null){
			this.clausulasContrato = "";
		}else{
			this.clausulasContrato = clausulasContrato;
		}
	}


	public void setListaAnexosImpresos(ArrayList<DtoCheckBox> listaAnexosImpresos) {
		this.listaAnexosImpresos = listaAnexosImpresos;
	}





	public String getValorTotalPresupuesto() {
		return valorTotalPresupuesto;
	}





	public void setValorTotalPresupuesto(String valorTotalPresupuesto) {
		this.valorTotalPresupuesto = valorTotalPresupuesto;
	}




	public JRDataSource getAnexosImpresosDS() {
		return anexosImpresosDS;
	}





	public void setAnexosImpresosDS(JRDataSource anexosImpresosDS) {
		this.anexosImpresosDS = anexosImpresosDS;
	}




	public ArrayList<PresuFirmasContrato> getListaPresuFirmasContrato() {
		return listaPresuFirmasContrato;
	}





	public void setListaPresuFirmasContrato(
			ArrayList<PresuFirmasContrato> listaPresuFirmasContrato) {
		this.listaPresuFirmasContrato = listaPresuFirmasContrato;
	}





	public String getPiePagina() {
		return piePagina;
	}





	public void setPiePagina(String piePagina) {
		this.piePagina = piePagina;
	}





	/**
	 * @return the listaRecomendaciones
	 */
	public ArrayList<DtoRecomendaciones> getListaRecomendaciones() {
		return listaRecomendaciones;
	}





	/**
	 * @param listaRecomendaciones the listaRecomendaciones to set
	 */
	public void setListaRecomendaciones(
			ArrayList<DtoRecomendaciones> listaRecomendaciones) {
		this.listaRecomendaciones = listaRecomendaciones;
	}





	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}





	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}



	

	
}

