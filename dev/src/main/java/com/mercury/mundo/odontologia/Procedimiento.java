package com.mercury.mundo.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.mundo.UsuarioBasico;

public class Procedimiento implements Serializable
{
	public static int				PLANEADO	= 1;

	public static int				REALIZADO	= 2;

	public static int				CANCELADO	= 3;

	private int						consecutivo;

	private int						codigoServicio;

	private int						codigoCUPS;

	private String					descripcionServicio;

	private int						codigoEspecialidadServicio;

	private String					descripcionEspecialidadServicio;

	private int						codigoTratamiento;

	private int						prioridad;

	private String					fechaRegistro;

	private transient UsuarioBasico	medicoRegistra;

	private String					fechaPlaneado;

	private int						diente;

	private InfoDatosInt			superficieDental;

	private int						estado;

	private String					fechaCerrado;

	private transient UsuarioBasico	medicoCierra;

	private String					observaciones;

	private String					obsNuevas;

	private boolean					enBD;
	
	private String 					aplicaA;
	
	private int estadoEnBD;
	
	private int finalidad;
	
	/**
	 * Listado de las superficies que aplican a la pieza dental seleccionada
	 */
	private ArrayList<DtoSectorSuperficieCuadrante> listaSuperficies;

	public Procedimiento()
	{
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.codigoCUPS = ConstantesBD.codigoNuncaValido;
		this.descripcionServicio = "";
		this.codigoEspecialidadServicio = ConstantesBD.codigoNuncaValido;
		this.descripcionEspecialidadServicio = "";
		this.codigoTratamiento = ConstantesBD.codigoNuncaValido;
		this.prioridad = ConstantesBD.codigoNuncaValido;
		this.fechaRegistro = "";
		this.medicoRegistra = new UsuarioBasico();
		this.fechaPlaneado = "";
		this.diente = -2;
		this.superficieDental = new InfoDatosInt();
		this.estado = 1;
		this.fechaCerrado = "";
		this.medicoCierra = new UsuarioBasico();
		this.observaciones = "";
		this.obsNuevas = "";
		this.enBD = false;
		this.aplicaA="";
		this.finalidad=ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return Returns the codigoTratamiento.
	 */
	public int getCodigoTratamiento()
	{
		return codigoTratamiento;
	}

	/**
	 * @param codigoTratamiento
	 *            The codigoTratamiento to set.
	 */
	public void setCodigoTratamiento(int codigoTratamiento)
	{
		this.codigoTratamiento = codigoTratamiento;
	}

	/**
	 * @return Returns the consecutivo.
	 */
	public int getConsecutivo()
	{
		return consecutivo;
	}

	/**
	 * @param consecutivo
	 *            The consecutivo to set.
	 */
	public void setConsecutivo(int consecutivo)
	{
		this.consecutivo = consecutivo;
	}

	/**
	 * @return Returns the diente.
	 */
	public int getDiente()
	{
		return diente;
	}

	/**
	 * @param diente
	 *            The diente to set.
	 */
	public void setDiente(int diente)
	{
		this.diente = diente;
	}

	/**
	 * @return Returns the enBD.
	 */
	public boolean getEnBD()
	{
		return enBD;
	}

	/**
	 * @param enBD
	 *            The enBD to set.
	 */
	public void setEnBD(boolean enBD)
	{
		this.enBD = enBD;
	}

	/**
	 * @return Returns the estado.
	 */
	public int getEstado()
	{
		return estado;
	}

	/**
	 * @param estado
	 *            The estado to set.
	 */
	public void setEstado(int estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Returns the fechaCerrado.
	 */
	public String getFechaCerrado()
	{
		return fechaCerrado;
	}

	/**
	 * @param fechaCerrado
	 *            The fechaCerrado to set.
	 */
	public void setFechaCerrado(String fechaCerrado)
	{
		this.fechaCerrado = fechaCerrado;
	}

	/**
	 * @return Returns the fechaPlaneado.
	 */
	public String getFechaPlaneado()
	{
		return fechaPlaneado;
	}

	/**
	 * @param fechaPlaneado
	 *            The fechaPlaneado to set.
	 */
	public void setFechaPlaneado(String fechaPlaneado)
	{
		this.fechaPlaneado = fechaPlaneado;
	}

	/**
	 * @return Returns the fechaRegistro.
	 */
	public String getFechaRegistro()
	{
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro
	 *            The fechaRegistro to set.
	 */
	public void setFechaRegistro(String fechaRegistro)
	{
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return Returns the medicoCierra.
	 */
	public UsuarioBasico getMedicoCierra()
	{
		return medicoCierra;
	}

	/**
	 * @param medicoCierra
	 *            The medicoCierra to set.
	 */
	public void setMedicoCierra(UsuarioBasico medicoCierra)
	{
		this.medicoCierra = medicoCierra;
	}

	/**
	 * @return Returns the medicoRegistra.
	 */
	public UsuarioBasico getMedicoRegistra()
	{
		return medicoRegistra;
	}

	/**
	 * @param medicoRegista
	 *            The medicoRegistra to set.
	 */
	public void setMedicoRegistra(UsuarioBasico medicoRegistra)
	{
		this.medicoRegistra = medicoRegistra;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * @param observaciones
	 *            The observaciones to set.
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the obsNuevas.
	 */
	public String getObsNuevas()
	{
		return obsNuevas;
	}

	/**
	 * @param obsNuevas
	 *            The obsNuevas to set.
	 */
	public void setObsNuevas(String obsNuevas)
	{
		this.obsNuevas = obsNuevas;
	}

	/**
	 * @return Returns the prioridad.
	 */
	public int getPrioridad()
	{
		return prioridad;
	}

	/**
	 * @param prioridad
	 *            The prioridad to set.
	 */
	public void setPrioridad(int prioridad)
	{
		this.prioridad = prioridad;
	}

	/**
	 * @return Returns the superficieDental.
	 */
	public InfoDatosInt getSuperficieDental()
	{
		return superficieDental;
	}

	/**
	 * @param superficieDental
	 *            The superficieDental to set.
	 */
	public void setSuperficieDental(InfoDatosInt superficieDental)
	{
		this.superficieDental = superficieDental;
	}

	/**
	 * @return Returns the codigoCUPS.
	 */
	public int getCodigoCUPS()
	{
		return codigoCUPS;
	}

	/**
	 * @param codigoCUPS
	 *            The codigoCUPS to set.
	 */
	public void setCodigoCUPS(int codigoCUPS)
	{
		this.codigoCUPS = codigoCUPS;
	}

	/**
	 * @return Returns the codigoEspecialidadServicio.
	 */
	public int getCodigoEspecialidadServicio()
	{
		return codigoEspecialidadServicio;
	}

	/**
	 * @param codigoEspecialidadServicio
	 *            The codigoEspecialidadServicio to set.
	 */
	public void setCodigoEspecialidadServicio(int codigoEspecialidadServicio)
	{
		this.codigoEspecialidadServicio = codigoEspecialidadServicio;
	}

	/**
	 * @return Returns the codigoServicio.
	 */
	public int getCodigoServicio()
	{
		return codigoServicio;
	}

	/**
	 * @param codigoServicio
	 *            The codigoServicio to set.
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return Returns the descripcionEspecialidadServicio.
	 */
	public String getDescripcionEspecialidadServicio()
	{
		return descripcionEspecialidadServicio;
	}

	/**
	 * @param descripcionEspecialidadServicio
	 *            The descripcionEspecialidadServicio to set.
	 */
	public void setDescripcionEspecialidadServicio(
			String descripcionEspecialidadServicio)
	{
		this.descripcionEspecialidadServicio = descripcionEspecialidadServicio;
	}

	/**
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio()
	{
		return descripcionServicio;
	}

	/**
	 * @param descripcionServicio
	 *            The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio)
	{
		this.descripcionServicio = descripcionServicio;
	}

	public ArrayList<DtoSectorSuperficieCuadrante> getListaSuperficies()
	{
		return listaSuperficies;
	}

	public void setListaSuperficies(
			ArrayList<DtoSectorSuperficieCuadrante> listaSuperficies)
	{
		this.listaSuperficies = listaSuperficies;
	}

	public String getAplicaA()
	{
		return aplicaA;
	}

	public void setAplicaA(String aplicaA)
	{
		this.aplicaA = aplicaA;
	}

	/**
	 * Asigna InfodatosInt con codigo y nombre superficie seleccionada
	 * @param codigoSuperficieSeleccionada
	 */
	public void asignarSuperficieDental(int codigoSuperficieSeleccionada)
	{
		superficieDental=new InfoDatosInt(codigoSuperficieSeleccionada);
		for(DtoSectorSuperficieCuadrante superf:listaSuperficies)
		{
			if(superf.getSuperficie().getCodigo()==codigoSuperficieSeleccionada)
			{
				superficieDental.setNombre(superf.getSuperficie().getNombre());
				return;
			}
		}
		
	}

	/**
	 * @return Retorna atributo estadoEnBD
	 */
	public int getEstadoEnBD()
	{
		return estadoEnBD;
	}

	/**
	 * @param estadoEnBD Asigna atributo estadoEnBD
	 */
	public void setEstadoEnBD(int estadoEnBD)
	{
		this.estadoEnBD = estadoEnBD;
	}

	public int getFinalidad() {
		return finalidad;
	}

	public void setFinalidad(int finalidad) {
		this.finalidad = finalidad;
	}
}
