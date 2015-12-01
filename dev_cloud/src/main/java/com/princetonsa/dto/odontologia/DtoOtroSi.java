package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.comun.DtoCheckBox;

import util.ConstantesBD;


/**
 * Clase que representa un Dto para la entidad de Otros Si
 * @author Ctisthian Murillo
 */
public class DtoOtroSi implements Serializable 
{

	/** * Serial */
	private static final long serialVersionUID = 1L;
	
	/** * Llave primaria asociada al dto */
	private long otroSi;
	
	/** * Presupuesto asociado */
	private long presupuesto;
	
	/** * Centro de Atencion asociado */
	private int centroAtencion;
	
	/** * Usuario asociado */
	private String usuario;
	
	/** * Lista de codigos de Inclusiones/Exclusiones del presupuesto del paciente */
	private ArrayList<DtoCheckBox> listaInclusionesExclusiones;
	
	/** * Indicador de valido o seleccionado */
	private boolean check;
	
	
	
	/** * Constructor */
	public DtoOtroSi() 
	{
		this.otroSi							= ConstantesBD.codigoNuncaValidoLong;
		this.presupuesto 					= ConstantesBD.codigoNuncaValidoLong;
		this.centroAtencion 				= ConstantesBD.codigoNuncaValido;
		this.usuario						= "";
		this.listaInclusionesExclusiones	= new ArrayList<DtoCheckBox>();
		this.check							= false;
	}

	

	public long getPresupuesto() {
		return presupuesto;
	}


	public void setPresupuesto(long presupuesto) {
		this.presupuesto = presupuesto;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public long getOtroSi() {
		return otroSi;
	}


	public void setOtroSi(long otroSi) {
		this.otroSi = otroSi;
	}



	public ArrayList<DtoCheckBox> getListaInclusionesExclusiones() {
		return listaInclusionesExclusiones;
	}



	public void setListaInclusionesExclusiones(
			ArrayList<DtoCheckBox> listaInclusionesExclusiones) {
		this.listaInclusionesExclusiones = listaInclusionesExclusiones;
	}



	public boolean isCheck() {
		return check;
	}



	public void setCheck(boolean check) {
		this.check = check;
	}

	
}
