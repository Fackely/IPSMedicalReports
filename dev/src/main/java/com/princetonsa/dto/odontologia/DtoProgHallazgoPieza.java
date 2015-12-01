package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

/**
 * Guarda el encabezado de las relaciones programa superficie,
 * asoci&aacute;ndolo a un plan de tratamiento y una pieza dental
 * @author Juan David Ram&iacute;rez
 * @since 2010-05-11
 */
public class DtoProgHallazgoPieza implements Serializable, Cloneable
{
	/**
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Llave primaria de la tabla
	 */
	private int codigoPk;
	
	/**
	 * C&oacute;digo del plan de tratamiento
	 */
	private int planTratamiento;
	
	/**
	 * C&oacute;digo del programa
	 */
	private int programa;
	
	/**
	 * C&oacute;digo del hallazgo seleccionado
	 */
	private int hallazgo;
	
	/**
	 * C&oacute;digo de la pieza dental a la que aplica
	 */
	private Integer piezaDental;
	
	/**
	 * Secci&oacute;n a la que aplica
	 */
	private String seccion;
	
	/**
	 * Color de letra de los programas;
	 */
	private String colorLetra;

	/**
	 * Campos de auditor&iacute;a (Usuario, fecha y hora)
	 */
	private DtoInfoFechaUsuario infoFechaUsuario=new DtoInfoFechaUsuario();
	
	/**
	 * Listado de las superficies para las que aplica el programa
	 */
	private ArrayList<DtoSuperficiesPorPrograma> superficiesPorPrograma;
	
	/**
	 * Atributo que indica si se ha registrado
	 * correctamente en la base de datos.
	 */
	private boolean registrado;
	
	
	public DtoProgHallazgoPieza()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.planTratamiento=ConstantesBD.codigoNuncaValido;
		this.programa=ConstantesBD.codigoNuncaValido;
		this.hallazgo=ConstantesBD.codigoNuncaValido;
		this.piezaDental=null;
		this.seccion="";
		this.superficiesPorPrograma=new ArrayList<DtoSuperficiesPorPrograma>();
		this.registrado = false;
	}

	/**
	 * 
	 * Constructor de la clase
	 */
	public DtoProgHallazgoPieza(int codigoPk, String colorLetra)
	{
		this.codigoPk=codigoPk;
		this.planTratamiento=ConstantesBD.codigoNuncaValido;
		this.programa=ConstantesBD.codigoNuncaValido;
		this.hallazgo=ConstantesBD.codigoNuncaValido;
		this.piezaDental=null;
		this.seccion="";
		this.superficiesPorPrograma=new ArrayList<DtoSuperficiesPorPrograma>();
		this.colorLetra= colorLetra;
	}

	public DtoProgHallazgoPieza(boolean noNull)
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.planTratamiento=ConstantesBD.codigoNuncaValido;
		this.programa=ConstantesBD.codigoNuncaValido;
		this.hallazgo=ConstantesBD.codigoNuncaValido;
		this.piezaDental=0;
		this.seccion="";
		this.superficiesPorPrograma=new ArrayList<DtoSuperficiesPorPrograma>();
		this.colorLetra="";
		this.infoFechaUsuario= new DtoInfoFechaUsuario();
	}

	
	/**
	 * @return Retorna el atributo codigoPk
	 */
	public int getCodigoPk()
	{
		return codigoPk;
	}

	/**
	 * @param codigoPk Asigna el atributo codigoPk
	 */
	public void setCodigoPk(int codigoPk)
	{
		this.codigoPk = codigoPk;
	}

	/**
	 * @return Retorna el atributo planTratamiento
	 */
	public int getPlanTratamiento()
	{
		return planTratamiento;
	}

	/**
	 * @param planTratamiento Asigna el atributo planTratamiento
	 */
	public void setPlanTratamiento(int planTratamiento)
	{
		this.planTratamiento = planTratamiento;
	}

	/**
	 * @return Retorna el atributo programa
	 */
	public int getPrograma()
	{
		return programa;
	}

	/**
	 * @param programa Asigna el atributo programa
	 */
	public void setPrograma(int programa)
	{
		this.programa = programa;
	}

	/**
	 * @return Retorna el atributo hallazgo
	 */
	public int getHallazgo()
	{
		return hallazgo;
	}

	/**
	 * @param hallazgo Asigna el atributo hallazgo
	 */
	public void setHallazgo(int hallazgo)
	{
		this.hallazgo = hallazgo;
	}

	/**
	 * @return Retorna el atributo piezaDental
	 */
	public Integer getPiezaDental()
	{
		return piezaDental;
	}

	/**
	 * @param piezaDental Asigna el atributo piezaDental
	 */
	public void setPiezaDental(Integer piezaDental)
	{
		this.piezaDental = piezaDental;
	}

	/**
	 * @return Retorna el atributo seccion
	 */
	public String getSeccion()
	{
		return seccion;
	}

	/**
	 * @param seccion Asigna el atributo seccion
	 */
	public void setSeccion(String seccion)
	{
		this.seccion = seccion;
	}

	/**
	 * @return Retorna el atributo colorLetra
	 */
	public String getColorLetra()
	{
		return colorLetra;
	}

	/**
	 * @param colorLetra Asigna el atributo colorLetra
	 */
	public void setColorLetra(String colorLetra)
	{
		this.colorLetra = colorLetra;
	}

	/**
	 * @return Retorna el atributo infoFechaUsuario
	 */
	public DtoInfoFechaUsuario getInfoFechaUsuario()
	{
		return infoFechaUsuario;
	}

	/**
	 * @param infoFechaUsuario Asigna el atributo infoFechaUsuario
	 */
	public void setInfoFechaUsuario(DtoInfoFechaUsuario infoFechaUsuario)
	{
		this.infoFechaUsuario = infoFechaUsuario;
	}

	/**
	 * @return Retorna el atributo superficiesPorPrograma
	 */
	public ArrayList<DtoSuperficiesPorPrograma> getSuperficiesPorPrograma()
	{
		return superficiesPorPrograma;
	}

	/**
	 * @return Retorna el atributo superficiesPorPrograma
	 */
	public ArrayList<DtoSuperficiesPorPrograma> getSuperficiesPorProgramaNoBocaNiDiente()
	{
		//metieron una machetazo en las superfies, colocaron boca y diente, lo cual no es una superficie dental
		ArrayList<DtoSuperficiesPorPrograma> lista= new ArrayList<DtoSuperficiesPorPrograma>();
		for(DtoSuperficiesPorPrograma dto: this.getSuperficiesPorPrograma())
		{
			if(dto.getSuperficieDental()!=ConstantesBD.codigoSuperficieBoca && dto.getSuperficieDental()!=ConstantesBD.codigoSuperficieDiente)
			{
				lista.add(dto);
			}
		}
		return lista;
	}
	
	/**
	 * @param superficiesPorPrograma Asigna el atributo superficiesPorPrograma
	 */
	public void setSuperficiesPorPrograma(
			ArrayList<DtoSuperficiesPorPrograma> superficiesPorPrograma)
	{
		this.superficiesPorPrograma = superficiesPorPrograma;
	}

	@Override
	public DtoProgHallazgoPieza clone()
	{
		DtoProgHallazgoPieza clon=null;
		try
		{
			clon = (DtoProgHallazgoPieza)super.clone();
			clon.setInfoFechaUsuario((DtoInfoFechaUsuario)this.getInfoFechaUsuario().clone());
		} catch (CloneNotSupportedException e)
		{
			Log4JManager.info("Error clonando DtoProgHallazgoPieza", e);
		}
		return clon;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DtoProgHallazgoPieza [codigoPk=" + codigoPk + ", colorLetra="
				+ colorLetra + ", hallazgo=" + hallazgo + ", infoFechaUsuario="
				+ infoFechaUsuario + ", piezaDental=" + piezaDental
				+ ", planTratamiento=" + planTratamiento + ", programa="
				+ programa + ", seccion=" + seccion
				+ ", superficiesPorPrograma=" + superficiesPorPrograma + "]";
	}

	/**
	 * @param registrado the registrado to set
	 */
	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	/**
	 * @return the registrado
	 */
	public boolean isRegistrado() {
		return registrado;
	}
	
}
