package util.manejoPaciente;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DetalleInconsistenciaFURIPS implements Serializable 
{
	/**
	 * 
	 */
	private String archivoPlano;
	
	/**
	 * 
	 */
	private String nombreCampo;
	
	/**
	 * 
	 */
	private String observaciones;

	/**
	 * 
	 */
	private String codigoServicioArticuloFURIPS2;
	
	/**
	 * CONSTRUCTOR PARA FURIPS2
	 * 
	 * @param archivoPlano
	 * @param nombreCampo
	 * @param observaciones
	 * @param ordenMedica
	 */
	public DetalleInconsistenciaFURIPS(	String archivoPlano, 
										String nombreCampo,
										String observaciones,
										String codigoServicioArticuloFURIPS2
										) 
	{
		super();
		this.archivoPlano = archivoPlano;
		this.nombreCampo = nombreCampo;
		this.observaciones = observaciones;
		this.codigoServicioArticuloFURIPS2= codigoServicioArticuloFURIPS2;
	}
	
	/** 
	 * 
	 * CONSTRUCTOR PARA FURIPS 1
	 * 
	 * @param archivoPlano
	 * @param nombreCampo
	 * @param observaciones
	 * @param ordenMedica
	 */
	public DetalleInconsistenciaFURIPS(	String archivoPlano, 
										String nombreCampo,
										String observaciones
										) 
	{
		super();
		this.archivoPlano = archivoPlano;
		this.nombreCampo = nombreCampo;
		this.observaciones = observaciones;
		this.codigoServicioArticuloFURIPS2= "";
	}

	/**
	 * @return the archivoPlano
	 */
	public String getArchivoPlano() {
		return archivoPlano;
	}

	/**
	 * @param archivoPlano the archivoPlano to set
	 */
	public void setArchivoPlano(String archivoPlano) {
		this.archivoPlano = archivoPlano;
	}

	/**
	 * @return the nombreCampo
	 */
	public String getNombreCampo() {
		return nombreCampo;
	}

	/**
	 * @param nombreCampo the nombreCampo to set
	 */
	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
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
	 * @return the codigoServicioFURIPS2
	 */
	public String getCodigoServicioArticuloFURIPS2() {
		return codigoServicioArticuloFURIPS2;
	}

	/**
	 * @param codigoServicioFURIPS2 the codigoServicioFURIPS2 to set
	 */
	public void setCodigoServicioArticuloFURIPS2(String codigoServicioArticuloFURIPS2) {
		this.codigoServicioArticuloFURIPS2 = codigoServicioArticuloFURIPS2;
	}
	
	
}
