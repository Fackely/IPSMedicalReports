package com.mercury.mundo.odontologia;

import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.mundo.UsuarioBasico;

public class ConsentimientoInformado
{
	private int				codigo;

	private String			fecha;

	private UsuarioBasico	medico;
	
	private HashMap			valores;

	private boolean			enBD;
	
	private InfoDatosInt 	tipo;
	
	private int				numValores;
	
	public static final int TIPOCONSENTIMIENTOODONTOLOGIA=1;

	public static final int TIPOCONSENTIMIENTOODONTOPEDIATRIA=2;
	
	public static final int TIPOCONSENTIMIENTOENDODONCIA=3;

	public ConsentimientoInformado()
	{
		this.codigo = -1;
		this.fecha = "";
		this.medico = new UsuarioBasico();
		this.enBD = false;
		this.tipo = new InfoDatosInt();
		this.valores = new HashMap();
		this.numValores = 0;
	}

	/**
	 * @return Returns the valores.
	 */
	public HashMap getValores() 
	{
		return valores;
	}

	/**
	 * @param valores The valores to set.
	 */
	public void setValores(HashMap valores) 
	{
		this.valores = valores;
	}
	
	public void setValor(String key, Object value)
	{
		this.valores.put(key, value);
	}
	
	public Object getValor(String key)
	{
		return this.valores.get(key);
	}
	
	public int getNumValores()
	{
		return this.numValores;
	}
	
	public void setNumValores(int numValores)
	{
		this.numValores=numValores;
	}

	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo()
	{
		
		return codigo;
	}

	/**
	 * @param codigo
	 *            The codigo to set.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Returns the fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * @param fecha
	 *            The fecha to set.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * @return Returns the medico.
	 */
	public UsuarioBasico getMedico()
	{
		return medico;
	}

	/**
	 * @param medico
	 *            The medico to set.
	 */
	public void setMedico(UsuarioBasico medico)
	{
		this.medico = medico;
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
	 * @return Returns the tipo.
	 */
	public InfoDatosInt getTipo()
	{
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(InfoDatosInt tipo)
	{
		this.tipo = tipo;
	}
}
